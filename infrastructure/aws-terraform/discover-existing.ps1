$request = [Console]::In.ReadToEnd() | ConvertFrom-Json

function Invoke-AwsJson {
  param([string[]]$Arguments)

  $output = & aws @Arguments 2>$null
  if ($LASTEXITCODE -ne 0 -or [string]::IsNullOrWhiteSpace(($output -join ''))) {
    return $null
  }

  try {
    return ($output -join "`n") | ConvertFrom-Json
  } catch {
    return $null
  }
}

$region = $request.region
$projectName = $request.name
$vpcId = $request.vpc_id

$targetGroup = Invoke-AwsJson @(
  'elbv2', 'describe-target-groups', '--region', $region, '--names', "$projectName-bff-v1", '--output', 'json'
)
$targetGroup = if ($null -ne $targetGroup) { $targetGroup.TargetGroups | Select-Object -First 1 } else { $null }

$securityGroups = Invoke-AwsJson @(
  'ec2', 'describe-security-groups', '--region', $region, '--filters', "Name=group-name,Values=$projectName-vpc-link", "Name=vpc-id,Values=$vpcId", '--output', 'json'
)
$vpcLinkSecurityGroup = if ($null -ne $securityGroups) { $securityGroups.SecurityGroups | Select-Object -First 1 } else { $null }

$vpcLinks = Invoke-AwsJson @(
  'apigatewayv2', 'get-vpc-links', '--region', $region, '--output', 'json'
)
$vpcLink = if ($null -ne $vpcLinks) { $vpcLinks.Items | Where-Object { $_.Name -eq $request.vpc_link_name } | Select-Object -First 1 } else { $null }

$loadBalancers = Invoke-AwsJson @(
  'elbv2', 'describe-load-balancers', '--region', $region, '--names', "$projectName-alb", '--output', 'json'
)
$loadBalancer = if ($null -ne $loadBalancers) { $loadBalancers.LoadBalancers | Select-Object -First 1 } else { $null }

$apiList = Invoke-AwsJson @(
  'apigatewayv2', 'get-apis', '--region', $region, '--output', 'json'
)
$api = if ($null -ne $apiList) { $apiList.Items | Where-Object { $_.Name -eq "$projectName-api" } | Select-Object -First 1 } else { $null }

$rds = Invoke-AwsJson @(
  'rds', 'describe-db-instances', '--region', $region, '--db-instance-identifier', $request.rds_identifier, '--output', 'json'
)
$rds = if ($null -ne $rds) { $rds.DBInstances | Select-Object -First 1 } else { $null }

$rdsSecurityGroups = Invoke-AwsJson @(
  'ec2', 'describe-security-groups', '--region', $region, '--filters', "Name=group-name,Values=$projectName-rds", "Name=vpc-id,Values=$vpcId", '--output', 'json'
)
$rdsSecurityGroup = if ($null -ne $rdsSecurityGroups) { $rdsSecurityGroups.SecurityGroups | Select-Object -First 1 } else { $null }

$apiId = if ($null -ne $api) { [string]$api.ApiId } else { '' }
$apiArn = if ($null -ne $api) { [string]$api.ApiEndpoint } else { '' }
$albArn = if ($null -ne $loadBalancer) { [string]$loadBalancer.LoadBalancerArn } else { '' }
$albSecurityGroupId = if ($null -ne $loadBalancer -and $loadBalancer.SecurityGroups.Count -gt 0) { [string]$loadBalancer.SecurityGroups[0] } else { '' }

$result = [ordered]@{
  target_group_exists       = ([string]($null -ne $targetGroup)).ToLowerInvariant()
  target_group_arn          = if ($null -ne $targetGroup) { [string]$targetGroup.TargetGroupArn } else { '' }
  vpc_link_security_group_exists = ([string]($null -ne $vpcLinkSecurityGroup)).ToLowerInvariant()
  vpc_link_security_group_id = if ($null -ne $vpcLinkSecurityGroup) { [string]$vpcLinkSecurityGroup.GroupId } else { '' }
  vpc_link_exists            = ([string]($null -ne $vpcLink)).ToLowerInvariant()
  vpc_link_id                = if ($null -ne $vpcLink) { [string]$vpcLink.VpcLinkId } else { '' }
  alb_exists                 = ([string]($null -ne $loadBalancer)).ToLowerInvariant()
  alb_arn                    = $albArn
  alb_security_group_id      = $albSecurityGroupId
  api_exists                 = ([string]($null -ne $api)).ToLowerInvariant()
  api_id                     = $apiId
  rds_exists                 = ([string]($null -ne $rds)).ToLowerInvariant()
  rds_address                = if ($null -ne $rds) { [string]$rds.Endpoint.Address } else { '' }
  rds_security_group_exists  = ([string]($null -ne $rdsSecurityGroup)).ToLowerInvariant()
  rds_security_group_id      = if ($null -ne $rdsSecurityGroup) { [string]$rdsSecurityGroup.GroupId } else { '' }
}

$result | ConvertTo-Json -Compress