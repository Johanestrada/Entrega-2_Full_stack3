output "dns_publico_ec2" {
  description = "DNS público de la instancia EC2."
  value       = aws_instance.app.public_dns
}

output "url_frontend" {
  description = "URL temporal del frontend servido por la EC2."
  value       = "http://${aws_instance.app.public_dns}"
}

output "endpoint_api_gateway" {
  description = "Endpoint público de API Gateway."
  value       = aws_apigatewayv2_api.this.api_endpoint
}

output "dns_alb_interno" {
  description = "DNS interno del Application Load Balancer."
  value       = aws_lb.this.dns_name
}

output "id_vpc_link" {
  description = "ID del VPC Link usado por API Gateway."
  value       = aws_apigatewayv2_vpc_link.this.id
}

output "arn_vpc_link" {
  description = "ARN del VPC Link usado por API Gateway."
  value       = aws_apigatewayv2_vpc_link.this.arn
}

output "endpoint_rds" {
  description = "Endpoint privado de RDS MySQL."
  value       = aws_db_instance.mysql.address
}
