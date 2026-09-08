resource "aws_db_subnet_group" "mysql" {
  name       = "${local.name}-mysql-subnets"
  subnet_ids = var.existing_public_subnet_ids
}

resource "aws_db_instance" "mysql" {
  identifier              = var.existing_rds_identifier
  allocated_storage       = 20
  db_name                 = "colegio"
  engine                  = "mysql"
  engine_version          = "8.0"
  instance_class          = "db.t3.micro"
  username                = var.mysql_admin_login
  password                = var.mysql_admin_password
  db_subnet_group_name    = aws_db_subnet_group.mysql.name
  vpc_security_group_ids  = [aws_security_group.rds.id]
  publicly_accessible     = false
  skip_final_snapshot     = true
  deletion_protection     = false
  backup_retention_period = 1
}
