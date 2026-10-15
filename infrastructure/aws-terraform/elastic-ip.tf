resource "aws_eip" "app" {
  domain = "vpc"

  tags = {
    Name = "${local.name}-app-eip"
  }
}

resource "aws_eip_association" "app" {
  allocation_id = aws_eip.app.id
  instance_id   = aws_instance.app.id
}
