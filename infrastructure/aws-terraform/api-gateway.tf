resource "aws_apigatewayv2_api" "this" {
  name          = "${local.name}-api"
  protocol_type = "HTTP"

  cors_configuration {
    allow_headers     = ["Authorization", "Content-Type"]
    allow_methods     = ["GET", "POST", "PUT", "DELETE", "OPTIONS"]
    allow_origins     = var.cors_allowed_origins == "auto" ? ["*"] : split(",", var.cors_allowed_origins)
    allow_credentials = false
  }
}

resource "aws_apigatewayv2_authorizer" "entra" {
  api_id           = aws_apigatewayv2_api.this.id
  name             = "microsoft-entra-jwt"
  authorizer_type  = "JWT"
  identity_sources = ["$request.header.Authorization"]

  jwt_configuration {
    audience = [var.jwt_audience]
    issuer   = var.jwt_issuer_uri
  }
}

resource "aws_apigatewayv2_integration" "bff" {
  api_id                 = aws_apigatewayv2_api.this.id
  integration_type       = "HTTP_PROXY"
  integration_method     = "ANY"
  integration_uri        = aws_lb_listener.bff.arn
  payload_format_version = "1.0"
  connection_type        = "VPC_LINK"
  connection_id          = local.vpc_link_id
}

resource "aws_apigatewayv2_integration" "frontend" {
  api_id             = aws_apigatewayv2_api.this.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  integration_uri    = "http://${aws_instance.app.public_dns}:4173"
  request_parameters = {
    "overwrite:path" = "$request.path"
  }
}

resource "aws_apigatewayv2_route" "bff" {
  api_id               = aws_apigatewayv2_api.this.id
  route_key            = "ANY /academico/{proxy+}"
  target               = "integrations/${aws_apigatewayv2_integration.bff.id}"
  authorization_type   = "JWT"
  authorizer_id        = aws_apigatewayv2_authorizer.entra.id
  authorization_scopes = ["api.access"]
}

resource "aws_apigatewayv2_route" "bff_root" {
  api_id               = aws_apigatewayv2_api.this.id
  route_key            = "ANY /academico"
  target               = "integrations/${aws_apigatewayv2_integration.bff.id}"
  authorization_type   = "JWT"
  authorizer_id        = aws_apigatewayv2_authorizer.entra.id
  authorization_scopes = ["api.access"]
}

resource "aws_apigatewayv2_route" "frontend_root" {
  api_id             = aws_apigatewayv2_api.this.id
  route_key          = "ANY /"
  target             = "integrations/${aws_apigatewayv2_integration.frontend.id}"
  authorization_type = "NONE"
}

resource "aws_apigatewayv2_route" "frontend" {
  api_id             = aws_apigatewayv2_api.this.id
  route_key          = "ANY /{proxy+}"
  target             = "integrations/${aws_apigatewayv2_integration.frontend.id}"
  authorization_type = "NONE"
}

resource "aws_apigatewayv2_stage" "default" {
  api_id      = aws_apigatewayv2_api.this.id
  name        = "$default"
  auto_deploy = true
}
