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
variable "jwt_issuer_uri" { type = string }
variable "jwt_audience" { type = string }
variable "image_prefix" {
  type    = string
  default = "ghcr.io/Johanestrada/Entrega-2_Full_stack3-"
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
