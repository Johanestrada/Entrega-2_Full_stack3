locals {
  name = "${var.project}-${var.environment}"

  target_group_name = var.existing_target_group_name != "" ? var.existing_target_group_name : "${local.name}-bff-v1"
  alb_name          = var.existing_alb_name != "" ? var.existing_alb_name : "${local.name}-alb"

  create_managed_vpc = var.existing_vpc_id == "" && length(data.aws_vpcs.default.ids) == 0

  vpc_id = var.existing_vpc_id != "" ? var.existing_vpc_id : (
    length(data.aws_vpcs.default.ids) > 0 ? data.aws_vpcs.default.ids[0] : aws_vpc.this[0].id
  )

  public_subnet_ids = length(var.existing_public_subnet_ids) > 0 ? var.existing_public_subnet_ids : (
    length(data.aws_subnets.default.ids) > 0 ? slice(data.aws_subnets.default.ids, 0, min(length(data.aws_subnets.default.ids), 2)) : (
      length(aws_subnet.public) > 0 ? aws_subnet.public[*].id : []
    )
  )

  public_subnet_id = length(local.public_subnet_ids) > 0 ? local.public_subnet_ids[0] : ""

  vpc_link_security_group_id = var.existing_vpc_link_security_group_id != "" ? var.existing_vpc_link_security_group_id : (
    data.external.discovery.result.vpc_link_security_group_exists == "true" ? data.aws_security_group.vpc_link_existing[0].id : aws_security_group.vpc_link[0].id
  )
  ec2_security_group_id = var.existing_ec2_security_group_id != "" ? var.existing_ec2_security_group_id : aws_security_group.ec2[0].id
  rds_security_group_id = var.existing_rds_security_group_id != "" ? var.existing_rds_security_group_id : (
    data.external.discovery.result.rds_security_group_exists == "true" ? data.aws_security_group.rds_existing[0].id : aws_security_group.rds[0].id
  )
  alb_security_group_id = data.external.discovery.result.alb_exists == "true" ? data.aws_security_group.alb_existing[0].id : aws_security_group.alb[0].id
  alb_arn               = data.external.discovery.result.alb_exists == "true" ? data.aws_lb.existing[0].arn : aws_lb.bff[0].arn
  target_group_arn      = data.external.discovery.result.target_group_exists == "true" ? data.aws_lb_target_group.existing[0].arn : aws_lb_target_group.bff[0].arn
  vpc_link_id           = data.external.discovery.result.vpc_link_exists == "true" ? data.aws_apigatewayv2_vpc_link.existing[0].id : aws_apigatewayv2_vpc_link.this[0].id
  rds_address           = data.external.discovery.result.rds_exists == "true" ? data.aws_db_instance.existing[0].address : aws_db_instance.mysql[0].address

  tags = {
    Project     = var.project
    Environment = var.environment
    ManagedBy   = "Terraform"
  }
}
