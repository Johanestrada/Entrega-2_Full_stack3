resource "aws_lb_target_group" "bff" {
  count            = var.existing_target_group_name == "" ? 1 : 0
  name             = "${local.name}-bff-v1"
  port             = 8084
  protocol         = "HTTP"
  vpc_id           = local.vpc_id
  target_type      = "instance"
  
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
  name               = "${local.name}-alb"
  internal           = true
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets            = local.public_subnet_ids
}

resource "aws_lb_listener" "bff" {
  load_balancer_arn = aws_lb.bff.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = var.existing_target_group_name != "" ? data.aws_lb_target_group.existing[0].arn : aws_lb_target_group.bff[0].arn
  }
}

resource "aws_lb_target_group_attachment" "bff" {
  target_group_arn = var.existing_target_group_name != "" ? data.aws_lb_target_group.existing[0].arn : aws_lb_target_group.bff[0].arn
  target_id        = aws_instance.app.id
  port             = 8084
}

resource "aws_apigatewayv2_vpc_link" "this" {
  name               = var.vpc_link_name
  security_group_ids = [local.vpc_link_security_group_id]
  subnet_ids         = local.public_subnet_ids
}