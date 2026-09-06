# Despliegue AWS

Esta infraestructura cumple la pauta con EC2, RDS MySQL y API Gateway. Microsoft Entra ID se conserva como proveedor de identidad: no se migra el tenant ni MSAL.

## Recursos

- Una EC2 ejecuta Docker Compose con Angular, BFF, Eureka y los microservicios.
- RDS MySQL privado reemplaza al contenedor MySQL local.
- API Gateway HTTP usa un JWT authorizer configurado con el issuer, audience y scope `api.access` de Entra ID.
- Un ALB interno y VPC Link impiden que el BFF quede expuesto directamente a Internet.

## Antes de aplicar

1. Suba estos cambios a un repositorio GitHub accesible desde EC2 (para una entrega, público es lo más simple).
2. En Entra ID mantenga el scope `api.access` y obtenga tenant ID, Application ID URI de la API y client ID del frontend.
3. Cree `terraform.tfvars` a partir del ejemplo. No escriba contraseñas en ese archivo.
4. En AWS CloudShell instale Terraform si no está disponible y ejecute `terraform init`.

```bash
export TF_VAR_mysql_admin_password='una-clave-larga-y-unica'
terraform init
terraform plan
terraform apply
```

Al terminar, copie `terraform output frontend_url` y agréguela como Redirect URI de tipo SPA en la app Angular de Entra ID. Use `terraform output api_url` como URL pública del BFF; el frontend la recibe dinámicamente al iniciar su contenedor.

## Costos y apagado

EC2, RDS, ALB y VPC Link tienen costos. Al finalizar las pruebas:

```bash
terraform destroy
```

Esto borra también RDS porque el entorno está configurado para entrega (`skip_final_snapshot = true`).
