resource "aws_vpc" "this" {
  count      = local.create_managed_vpc ? 1 : 0
  cidr_block = "10.0.0.0/16"

  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = merge(local.tags, {
    Name = "${local.name}-vpc"
  })
}

resource "aws_subnet" "public" {
  count                   = local.create_managed_vpc ? 2 : 0
  vpc_id                  = aws_vpc.this[0].id
  cidr_block              = cidrsubnet(aws_vpc.this[0].cidr_block, 8, count.index)
  availability_zone       = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = true

  tags = merge(local.tags, {
    Name = "${local.name}-public-${count.index + 1}"
  })
}

resource "aws_internet_gateway" "public" {
  count  = local.create_managed_vpc ? 1 : 0
  vpc_id = aws_vpc.this[0].id

  tags = merge(local.tags, {
    Name = "${local.name}-igw"
  })
}

resource "aws_route_table" "public" {
  count  = local.create_managed_vpc ? 1 : 0
  vpc_id = aws_vpc.this[0].id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.public[0].id
  }

  tags = merge(local.tags, {
    Name = "${local.name}-public-rt"
  })
}

resource "aws_route_table_association" "public" {
  count          = local.create_managed_vpc ? length(aws_subnet.public) : 0
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public[0].id
}

resource "aws_route" "public_internet" {
  count                  = local.create_managed_vpc ? 1 : 0
  route_table_id         = aws_route_table.public[0].id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.public[0].id
}
