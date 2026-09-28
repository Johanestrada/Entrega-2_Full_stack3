data "aws_availability_zones" "available" {
  state = "available"
}

data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-*-x86_64"]
  }
}

data "aws_vpcs" "default" {
  filter {
    name   = "isDefault"
    values = ["true"]
  }
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [local.vpc_id]
  }

  filter {
    name   = "availability-zone"
    values = ["us-east-1a", "us-east-1b", "us-east-1c", "us-east-1d", "us-east-1f"]
  }
}

data "aws_lb_target_group" "existing" {
  count = data.external.discovery.result.target_group_exists == "true" ? 1 : 0
  name  = local.target_group_name
}

data "aws_security_group" "vpc_link_existing" {
  count = data.external.discovery.result.vpc_link_security_group_exists == "true" ? 1 : 0
  id    = data.external.discovery.result.vpc_link_security_group_id
}

data "aws_apigatewayv2_vpc_link" "existing" {
  count       = data.external.discovery.result.vpc_link_exists == "true" ? 1 : 0
  vpc_link_id = data.external.discovery.result.vpc_link_id
}

data "aws_lb" "existing" {
  count = data.external.discovery.result.alb_exists == "true" ? 1 : 0
  name  = local.alb_name
}

data "aws_security_group" "alb_existing" {
  count = data.external.discovery.result.alb_exists == "true" ? 1 : 0
  id    = data.external.discovery.result.alb_security_group_id
}

data "aws_db_instance" "existing" {
  count                  = data.external.discovery.result.rds_exists == "true" ? 1 : 0
  db_instance_identifier = var.existing_rds_identifier
}

data "aws_security_group" "rds_existing" {
  count = data.external.discovery.result.rds_security_group_exists == "true" ? 1 : 0
  id    = data.external.discovery.result.rds_security_group_id
}

data "external" "discovery" {
  program = [
    "powershell.exe",
    "-NoProfile",
    "-ExecutionPolicy",
    "Bypass",
    "-File",
    "${path.module}/discover-existing.ps1",
  ]

  query = {
    region         = var.aws_region
    name           = local.name
    vpc_id         = local.vpc_id
    vpc_link_name  = var.vpc_link_name
    rds_identifier = var.existing_rds_identifier
  }
}
