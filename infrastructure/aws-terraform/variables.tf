variable "aws_region" {
  type    = string
  default = "us-east-1"
}
variable "project" { type = string }
variable "environment" { type = string }
variable "github_repository_url" { type = string }
variable "github_branch" {
  type    = string
  default = "main"
}
variable "jwt_issuer_uri" {
  type    = string
  default = "https://login.microsoftonline.com/39428fa5-d349-476e-8a21-6570cfd7fa42/v2.0"
}
variable "jwt_audience" {
  type    = string
  default = "e0d39aa2-d7b9-4ef5-9bef-84e418dcae72"
}
variable "entra_tenant_id" {
  type    = string
  default = "39428fa5-d349-476e-8a21-6570cfd7fa42"
}
variable "frontend_client_id" {
  type    = string
  default = "a43b07f8-2ca4-4985-b355-c62623fd8bc9"
}
variable "frontend_api_scope" {
  type    = string
  default = "api://e0d39aa2-d7b9-4ef5-9bef-84e418dcae72/api.access"
}
variable "cors_allowed_origins" {
  type        = string
  description = "Orígenes del navegador separados por comas que pueden consumir la API."
  default     = "auto"
}
variable "enable_cloudfront" {
  type        = bool
  description = "Habilita CloudFront para publicar el frontend por HTTPS. AWS Academy puede no permitir cloudfront:CreateDistribution."
  default     = false
}
variable "admin_cidr" {
  type        = string
  description = "CIDR reservado para SSH restringido; SSH no se habilita por defecto."
  default     = ""
}
variable "image_prefix" {
  type    = string
  default = ""
}
variable "image_tag" {
  type    = string
  default = "latest"
}
variable "mysql_admin_password" {
  type      = string
  sensitive = true
}
variable "mysql_admin_login" {
  type    = string
  default = "colegioadmin"
}
variable "instance_type" {
  type    = string
  default = "t3.medium"
}

variable "existing_vpc_id" {
  type        = string
  description = "VPC existente que se reutilizara para todo el despliegue. Si se deja vacío, se usa la VPC por defecto o se crea una nueva."
  default     = ""
}

variable "existing_public_subnet_ids" {
  type        = list(string)
  description = "Subredes publicas existentes para EC2 y VPC Link. Si se dejan vacías, se usan las subredes de la VPC por defecto o se crean nuevas."
  default     = []
}

variable "existing_alb_name" {
  type    = string
  default = "colegiojp2026-dev-alb"
}

variable "existing_target_group_name" {
  type    = string
  default = "colegiojp2026-dev-bff"
}

variable "existing_rds_identifier" {
  type    = string
  default = "colegiojp2026-dev-mysql"
}

variable "existing_ec2_security_group_id" {
  type        = string
  description = "Security Group existente para la instancia EC2. Dejar vacío para crear uno nuevo."
  default     = ""
}

variable "existing_vpc_link_security_group_id" {
  type        = string
  description = "Security Group existente para el VPC Link. Dejar vacío para crear uno nuevo."
  default     = ""
}

variable "vpc_link_name" {
  type    = string
  default = "colegiojp2026-dev-vpc-link-reused"
}
