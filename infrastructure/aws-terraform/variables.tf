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
variable "cors_allowed_origins" {
  type        = string
  description = "Orígenes del navegador separados por comas que pueden consumir la API."
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
