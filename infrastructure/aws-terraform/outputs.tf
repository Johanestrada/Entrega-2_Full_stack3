output "frontend_url" { value = "http://${aws_instance.app.public_dns}" }
output "api_url" { value = aws_apigatewayv2_api.this.api_endpoint }
output "rds_endpoint" { value = aws_db_instance.mysql.address }
