resource "aws_apigatewayv2_api" "this" {
  name          = "${local.name}-api"
  protocol_type = "HTTP"
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
  integration_uri        = "http://${aws_instance.app.public_dns}:8084/{proxy}"
  payload_format_version = "1.0"
}

resource "aws_apigatewayv2_route" "bff" {
  api_id               = aws_apigatewayv2_api.this.id
  route_key            = "ANY /{proxy+}"
  target               = "integrations/${aws_apigatewayv2_integration.bff.id}"
  authorization_type   = "JWT"
  authorizer_id        = aws_apigatewayv2_authorizer.entra.id
  authorization_scopes = ["api.access"]
}

resource "aws_apigatewayv2_stage" "default" {
  api_id      = aws_apigatewayv2_api.this.id
  name        = "$default"
  auto_deploy = true
}
