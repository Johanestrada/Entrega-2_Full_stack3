resource "aws_instance" "app" {
  ami                         = data.aws_ami.amazon_linux.id
  instance_type               = var.instance_type
  subnet_id                   = local.public_subnet_id
  vpc_security_group_ids      = [local.ec2_security_group_id]
  associate_public_ip_address = true
  user_data_replace_on_change = true

  root_block_device {
    volume_size = 30
    volume_type = "gp3"
  }

  lifecycle {
    create_before_destroy = true
  }

  user_data = templatefile("${path.module}/user-data.sh.tftpl", {
    repository_url = var.github_repository_url
    branch         = var.github_branch
    db_host        = local.rds_address
    db_user        = var.mysql_admin_login
    db_password    = var.mysql_admin_password
    issuer         = var.jwt_issuer_uri
    audience       = var.jwt_audience
    entra_tenant   = var.entra_tenant_id
    client_id      = var.frontend_client_id
    api_scope      = var.frontend_api_scope
    api_url        = aws_apigatewayv2_api.this.api_endpoint
    cors_origins   = var.cors_allowed_origins == "auto" ? aws_apigatewayv2_api.this.api_endpoint : var.cors_allowed_origins
    image_prefix   = var.image_prefix
    image_tag      = var.image_tag
  })
}
