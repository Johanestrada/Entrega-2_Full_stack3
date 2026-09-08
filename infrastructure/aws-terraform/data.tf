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

data "aws_vpc" "existing" {
  id = var.existing_vpc_id
}

data "aws_route_table" "public" {
  subnet_id = var.existing_public_subnet_ids[0]
}

data "aws_lb_target_group" "existing" {
  name = var.existing_target_group_name
}
