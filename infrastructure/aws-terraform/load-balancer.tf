resource "aws_lb_target_group" "bff" {
  count       = data.external.discovery.result.target_group_exists == "true" ? 0 : 1
  name        = local.target_group_name
  port        = 8084
  protocol    = "HTTP"
  vpc_id      = local.vpc_id
  target_type = "instance"

  health_check {
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 3
    interval            = 30
    path                = "/academico"
    matcher             = "401"
    port                = 8084
  }
}

resource "aws_lb" "bff" {
  count              = data.external.discovery.result.alb_exists == "true" ? 0 : 1
  name               = "${local.name}-alb"
  internal           = true
  load_balancer_type = "application"
  security_groups    = [local.alb_security_group_id]
  subnets            = local.public_subnet_ids
}

resource "aws_lb_listener" "bff" {
  load_balancer_arn = local.alb_arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = local.target_group_arn
  }
}

resource "aws_lb_target_group_attachment" "bff" {
  target_group_arn = local.target_group_arn
  target_id        = aws_instance.app.id
  port             = 8084
}

resource "aws_apigatewayv2_vpc_link" "this" {
  count              = data.external.discovery.result.vpc_link_exists == "true" ? 0 : 1
  name               = var.vpc_link_name
  security_group_ids = [local.vpc_link_security_group_id]
  subnet_ids         = local.public_subnet_ids
}