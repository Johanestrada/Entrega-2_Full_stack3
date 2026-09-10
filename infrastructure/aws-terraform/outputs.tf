output "dns_publico_ec2" {
  description = "DNS público de la instancia EC2."
  value       = aws_instance.app.public_dns
}

output "url_frontend" {
  description = "URL HTTPS principal del frontend servido mediante API Gateway."
  value       = var.enable_cloudfront ? "https://${aws_cloudfront_distribution.frontend[0].domain_name}" : aws_apigatewayv2_api.this.api_endpoint
}

output "url_frontend_ec2" {
  description = "URL HTTP directa de diagnóstico de la EC2."
  value       = "http://${aws_instance.app.public_dns}"
}

output "endpoint_api_gateway" {
  description = "Endpoint público de API Gateway."
  value       = aws_apigatewayv2_api.this.api_endpoint
}

output "dns_alb_interno" {
  description = "DNS interno del Application Load Balancer."
  value       = data.external.discovery.result.alb_exists == "true" ? data.aws_lb.existing[0].dns_name : aws_lb.bff[0].dns_name
}

output "id_vpc_link" {
  description = "ID del VPC Link usado por API Gateway."
  value       = local.vpc_link_id
}

output "arn_vpc_link" {
  description = "ARN del VPC Link usado por API Gateway."
  value       = data.external.discovery.result.vpc_link_exists == "true" ? data.aws_apigatewayv2_vpc_link.existing[0].arn : aws_apigatewayv2_vpc_link.this[0].arn
}

output "endpoint_rds" {
  description = "Endpoint privado de RDS MySQL."
  value       = local.rds_address
}
