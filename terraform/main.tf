# ==========================================
# 1. PROVIDER & BÖLGE AYARLARI
# ==========================================
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-1" # İsteğe göre değiştirebilirsin
}

# ==========================================
# 2. VPC & AĞ ALTYAPISI
# ==========================================
# ==========================================
# 2. VPC & AĞ ALTYAPISI
# ==========================================
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.0.0"

  name = "apartman-pure-ec2-vpc"
  cidr = "10.0.0.0/16"

  # BURASI KRİTİK: Her iki bölge de kendi çift tırnakları içinde olmalı.
  azs            = ["us-east-1a", "us-east-1b"]
  public_subnets = ["10.0.101.0/24", "10.0.102.0/24"]

  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    Environment = "DevOps-Lab"
    Project     = "ApartmanYonetimi"
  }
}
# ==========================================
# 3. AWS ECR (Konteyner Kayıt Alanı)
# ==========================================
resource "aws_ecr_repository" "app_repo" {
  name                 = "apartman-app-repo"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}

# ==========================================
# 4. GÜVENLİK GRUPLARI (Security Groups)
# ==========================================

# A) DevOps & Docker Sunucu Güvenlik Grubu
resource "aws_security_group" "devops_sg" {
  name        = "devops_and_docker_sg"
  description = "Jenkins, Nexus, Monitoring ve Docker Staging icin portlar"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 8081
    to_port     = 8081
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 9090
    to_port     = 9090
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 3000
    to_port     = 3000
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# B) Kubernetes Cluster Güvenlik Grubu (Master & Worker Ortak)
resource "aws_security_group" "k8s_sg" {
  name        = "kubernetes_sg"
  description = "K8s Master ve Worker dugumleri arasi iletisim portlari"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 6443
    to_port     = 6443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # K8s kendi iç iletişimi için tüm iç trafiğe izin veriyoruz
  ingress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    self      = true
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ==========================================
# 5. AMI (İşletim Sistemi Seçimi)
# ==========================================
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"] # Canonical

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
}

# ==========================================
# 6. EC2 SUNUCULARI (Instances)
# ==========================================

resource "aws_instance" "devops_server" {
  ami                         = data.aws_ami.ubuntu.id
  instance_type               = "t3.large"
  subnet_id                   = module.vpc.public_subnets[0]
  vpc_security_group_ids      = [aws_security_group.devops_sg.id]
  associate_public_ip_address = true
  tags = { Name = "DevOps-Master-Server" }
}

resource "aws_instance" "docker_staging" {
  ami                         = data.aws_ami.ubuntu.id
  instance_type               = "t3.medium"
  subnet_id                   = module.vpc.public_subnets[1]
  vpc_security_group_ids      = [aws_security_group.devops_sg.id]
  associate_public_ip_address = true
  tags = { Name = "Docker-Staging-Server" }
}

resource "aws_instance" "k8s_master" {
  ami                         = data.aws_ami.ubuntu.id
  instance_type               = "t3.medium"
  subnet_id                   = module.vpc.public_subnets[0]
  vpc_security_group_ids      = [aws_security_group.k8s_sg.id]
  associate_public_ip_address = true
  tags = { Name = "K8s-Master-Node" }
}

resource "aws_instance" "k8s_worker" {
  ami                         = data.aws_ami.ubuntu.id
  instance_type               = "t3.medium"
  subnet_id                   = module.vpc.public_subnets[1]
  vpc_security_group_ids      = [aws_security_group.k8s_sg.id]
  associate_public_ip_address = true
  tags = { Name = "K8s-Worker-Node" }
}

# ==========================================
# 7. ÇIKTILAR
# ==========================================
output "devops_server_ip" { value = aws_instance.devops_server.public_ip }
output "docker_staging_ip" { value = aws_instance.docker_staging.public_ip }
output "k8s_master_ip"     { value = aws_instance.k8s_master.public_ip }
output "k8s_worker_ip"     { value = aws_instance.k8s_worker.public_ip }
output "ecr_repo_url"      { value = aws_ecr_repository.app_repo.repository_url }