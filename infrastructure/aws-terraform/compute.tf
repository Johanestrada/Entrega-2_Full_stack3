resource "aws_instance" "app" {
  ami                         = data.aws_ami.amazon_linux.id
  instance_type               = var.instance_type
  subnet_id                   = aws_subnet.public[0].id
  vpc_security_group_ids      = [aws_security_group.ec2.id]
  associate_public_ip_address = true

  user_data = templatefile("${path.module}/user-data.sh.tftpl", {
    repository_url = var.github_repository_url
    branch         = var.github_branch
    db_host        = aws_db_instance.mysql.address
    db_user        = var.mysql_admin_login
    db_password    = var.mysql_admin_password
    issuer         = var.jwt_issuer_uri
    audience       = var.jwt_audience
    api_url        = aws_apigatewayv2_api.this.api_endpoint
    image_prefix   = var.image_prefix
    image_tag      = var.image_tag
  })
}
