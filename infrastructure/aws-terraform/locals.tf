locals {
  name = "${var.project}-${var.environment}"

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

  vpc_link_security_group_id = var.existing_vpc_link_security_group_id != "" ? var.existing_vpc_link_security_group_id : aws_security_group.vpc_link[0].id
  ec2_security_group_id      = var.existing_ec2_security_group_id != "" ? var.existing_ec2_security_group_id : aws_security_group.ec2[0].id

  tags = {
    Project     = var.project
    Environment = var.environment
    ManagedBy   = "Terraform"
  }
}
