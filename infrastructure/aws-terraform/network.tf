resource "aws_internet_gateway" "public" {
	vpc_id = var.existing_vpc_id
}

resource "aws_route" "public_internet" {
	route_table_id         = data.aws_route_table.public.id
	destination_cidr_block = "0.0.0.0/0"
	gateway_id             = aws_internet_gateway.public.id
}
