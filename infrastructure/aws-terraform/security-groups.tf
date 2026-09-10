resource "aws_security_group" "vpc_link" {
  count       = var.existing_vpc_link_security_group_id == "" ? 1 : 0
  name        = "${local.name}-vpc-link"
  description = "Security group for API Gateway VPC Link"
  vpc_id      = local.vpc_id

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "alb" {
  name        = "${local.name}-alb"
  description = "Security group for internal BFF ALB"
  vpc_id      = local.vpc_id

  ingress {
    protocol        = "tcp"
    from_port       = 80
    to_port         = 80
    security_groups = [local.vpc_link_security_group_id]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "ec2" {
  count       = var.existing_ec2_security_group_id == "" ? 1 : 0
  name        = "${local.name}-ec2"
  description = "Security group for frontend and BFF EC2"
  vpc_id      = local.vpc_id

  ingress {
    protocol    = "tcp"
    from_port   = 4173
    to_port     = 4173
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    protocol    = "tcp"
    from_port   = 80
    to_port     = 80
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    protocol        = "tcp"
    from_port       = 8084
    to_port         = 8084
    security_groups = [aws_security_group.alb.id]
  }

  dynamic "ingress" {
    for_each = var.admin_cidr == "" ? [] : [var.admin_cidr]
    content {
      protocol    = "tcp"
      from_port   = 22
      to_port     = 22
      cidr_blocks = [ingress.value]
      description = "Temporary SSH access for diagnostics"
    }
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "rds" {
  count       = var.existing_rds_security_group_id == "" ? 1 : 0
  name        = "${local.name}-rds"
  description = "Security group for MySQL RDS"
  vpc_id      = local.vpc_id

  ingress {
    protocol        = "tcp"
    from_port       = 3306
    to_port         = 3306
    security_groups = [local.ec2_security_group_id]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}
