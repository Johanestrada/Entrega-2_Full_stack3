resource "aws_lb" "bff" {
  name               = var.existing_alb_name
  internal           = true
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets            = var.existing_public_subnet_ids
}

resource "aws_lb_listener" "bff" {
  load_balancer_arn = aws_lb.bff.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = data.aws_lb_target_group.existing.arn
  }
}

resource "aws_lb_target_group_attachment" "bff" {
  target_group_arn = data.aws_lb_target_group.existing.arn
  target_id        = aws_instance.app.id
  port             = 8084
}

resource "aws_apigatewayv2_vpc_link" "this" {
  name               = var.vpc_link_name
  security_group_ids = [aws_security_group.vpc_link.id]
  subnet_ids         = var.existing_public_subnet_ids
}