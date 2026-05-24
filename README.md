


Burada Java 21 kullanacak şekilde güncellenmiş görev akışı (Task Flow) yer alıyor. Tablodaki açıklamalar ve kurulum script'i `java-21-amazon-corretto` paketini çekecek şekilde düzenlendi.

## Flow of Tasks for Project Realization

| Epic | Task | Task # | Task Definition | Branch |
| --- | --- | --- | --- | --- |
| Local Development Environment | Prepare Development Server Manually on EC2 Instance | APT-1 | Prepare development server manually on Ubuntu/Amazon Linux for developers, enabled with Docker, Docker-Compose, Java 21, Git. |  |
| Local Development Environment | Prepare GitHub Repository for the Project | APT-2-1 | Initialize the Apartment Management app repository. |  |
| Local Development Environment | Prepare GitHub Repository for the Project | APT-2-2 | Prepare base branches namely `main`, `dev`, `release` for DevOps cycle. |  |
| Local Development Environment | Check the Maven Build Setup on Dev Branch | APT-3 | Check the Maven builds for `test`, `package`, and `install` phases on `dev` branch. |  |
| Local Development Environment | Prepare a Script for Packaging the Application | APT-4 | Prepare a script to package the application with Maven wrapper. | feature/apt-4 |
| Local Development Environment | Prepare Development Server Terraform Files | APT-5 | Prepare development server folder with Terraform File for developers, enabled with Docker, Docker-Compose, Java 21, Git. | feature/apt-5 |
| Local Development Build | Prepare Dockerfiles for App | APT-6 | Prepare Dockerfiles for the Spring Boot backend and frontend (if separated). | feature/apt-6 |
| Local Development Build | Prepare Script for Building Docker Images | APT-7 | Prepare a script to package and build the docker image for the application. | feature/apt-7 |
| Local Development Build | Create Docker Compose File for Local Development | APT-8-1 | Prepare docker-compose file to deploy the application and database (MySQL/H2) locally. | feature/apt-8 |
| Local Development Build | Create Docker Compose File for Local Development | APT-8-2 | Prepare a script to test the deployment of the app locally. | feature/apt-8 |
| CI Server Setup | Prepare Jenkins Server | APT-9 | Prepare Jenkins Server for CI/CD Pipeline. | feature/apt-9 |
| CI Server Setup | Configure Jenkins Server for Project | APT-10 | Configure Jenkins Server for Project Setup. |  |
| Testing Environment Setup | Implement Unit Tests | APT-11-1 | Implement Unit Tests locally for the Apartment API. | feature/apt-11 |
| Testing Environment Setup | Setup Code Coverage Tool | APT-11-2 | Update POM file for Code Coverage Report (Jacoco). | feature/apt-11 |
| Testing Environment Setup | Implement Code Coverage | APT-11-3 | Generate Code Coverage Report manually. | feature/apt-11 |
| CI Server Setup | Prepare CI Pipeline | APT-12 | Prepare CI pipeline (UT only) for all `dev`, `feature` and `bugfix` branches. | feature/apt-1 |
| Testing Environment Setup | Prepare Selenium Tests | APT-13-1 | Prepare Selenium Jobs for QA Automation Tests (Frontend validation). | feature/apt-13 |
| Testing Environment Setup | Implement Selenium Tests | APT-13-2 | Run Selenium Tests against local environment. | feature/apt-13 |
| Registry Setup for Development | Create Docker Registry for Dev Manually | APT-14 | Create Docker Registry on AWS ECR manually using Jenkins job. | feature/apt-14 |
| QA Automation Setup for Development | Create a QA Automation Environment - Part-1 | APT-15 | Create a QA Automation Environment with Kubernetes. | feature/apt-15 |
| QA Automation Setup for Development | Create a QA Automation Environment - Part-2 | APT-16 | Create a QA Automation Environment with Kubernetes. | feature/apt-16 |
| QA Automation Setup for Development | Prepare Apartment App Kubernetes YAML Files | APT-17 | Prepare Apartment App Kubernetes YAML Files. | feature/apt-17 |
| QA Automation Setup for Development | Prepare a QA Automation Pipeline | APT-18 | Prepare a QA Automation Pipeline on `dev` branch for Nightly Builds. | feature/apt-18 |
| QA Setup for Release | Create a QA Infrastructure with eksctl | APT-19 | Create a Permanent QA Infrastructure for Kubernetes Cluster with eksctl. | feature/apt-19 |
| QA Setup for Release | Prepare Build Scripts for QA Environment | APT-20 | Prepare Build Scripts for QA Environment. | feature/apt-20 |
| QA Setup for Release | Build and Deploy App on QA Environment Manually | APT-21 | Build and Deploy App for QA Environment Manually using Jenkins Jobs. | feature/apt-21 |
| QA Setup for Release | Prepare a QA Pipeline | APT-22 | Prepare a QA Pipeline using Jenkins on `release` branch for Weekly Builds. | feature/apt-22 |
| Staging and Production Setup | Prepare HA RKE Kubernetes Cluster | APT-23 | Prepare High-availability RKE Kubernetes Cluster on AWS EC2. | feature/apt-23 |
| Staging and Production Setup | Install Rancher App on RKE K8s Cluster | APT-24 | Install Rancher App on RKE Kubernetes Cluster. |  |
| Staging Deployment Setup | Prepare and Configure Nexus Server | APT-25 | Create and Configure Nexus Server for Pipelines. | feature/apt-25 |
| Staging Deployment Setup | Prepare a Staging Pipeline | APT-26 | Prepare a Staging Pipeline on Jenkins Server. | feature/apt-26 |
| Production Deployment Setup | Prepare a Production Pipeline | APT-27 | Prepare a Production Pipeline on Jenkins Server. | feature/apt-27 |
| Production Deployment Setup | Set Domain Name and TLS for Production | APT-28 | Set Domain Name and TLS for Production Pipeline with Route 53. | feature/apt-28 |
| Production Deployment Setup | Set Monitoring Tools | APT-29 | Set Monitoring tools, Prometheus and Grafana. |  |

---

# # # # # # # # # # # # # # # # # # # # # # # # # # # #

### APT 1 - Prepare Development Server Manually on EC2 Instance

# # # # # # # # # # # # # # # # # # # # # # # # # # # #

Prepare development server manually on Amazon Linux 2023 (or Ubuntu) for developers, enabled with Docker, Docker-Compose, Java 21, and Git.

```bash
#! /bin/bash
sudo dnf update -y
sudo hostnamectl set-hostname apartment-dev-server
sudo dnf install docker -y
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -a -G docker ec2-user
sudo curl -SL https://github.com/docker/compose/releases/download/v2.29.3/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
sudo dnf install git -y
sudo dnf install java-21-amazon-corretto -y
newgrp docker

```

# # # # # # # # # # # # # # # # # # # # # # # # # # # #

### APT 2 - Prepare GitHub Repository for the Project

# # # # # # # # # # # # # # # # # # # # # # # # # # # #

Navigate to your workspace and initialize your local Apartment Management App code as a Git repository.

```bash
cd apartment-management-app
rm -rf .git

```

Create a new repository on your Github account with the name `apartment-management-app`.

Initiate the local repository to make it a git repository and push it to your remote repository.

```bash
git init
git add .
git config --global user.email "you@example.com"
git config --global user.name "Your Name"
git commit -m "first commit"
git branch -M main
git remote add origin https://[github username]:[your-token]@github.com/[your-git-account]/apartment-management-app.git
git push -u origin main

```

Prepare base branches namely `dev` and `release` for the DevOps cycle.

Create `dev` base branch.

```bash
git checkout main
git branch dev
git checkout dev
git push --set-upstream origin dev

```

Create `release` base branch.

```bash
git checkout dev
git branch release
git checkout release
git push --set-upstream origin release

```

# # # # # # # # # # # # # # # # # # # # # # # # # # # #

### APT 3 - Check the Maven Build Setup on Dev Branch

# # # # # # # # # # # # # # # # # # # # # # # # # # # #

Switch to the `dev` branch.

```bash
git checkout dev

```

Test the compiled source code.
*(Note: If you get permission denied error, try to give execution permission to mvnw via `chmod +x mvnw`)*

```bash
./mvnw clean test

```

Take the compiled code and package it in its distributable JAR format.

```bash
./mvnw clean package

```

Install distributable JARs into local repository.

```bash
./mvnw clean install

```

# # # # # # # # # # # # # # # # # # # # # # # # # # # #

### APT 4 - Prepare a Script for Packaging the Application

# # # # # # # # # # # # # # # # # # # # # # # # # # # #

Create `feature/apt-4` branch from `dev`.

```bash
git checkout dev
git branch feature/apt-4
git checkout feature/apt-4

```

Prepare a script to package the application with maven wrapper and save it as `package-with-mvn-wrapper.sh` under the `apartment-management-app` folder.

```bash
echo "./mvnw clean package" > package-with-mvn-wrapper.sh

```

Give execution permission to the script.

```bash
chmod +x package-with-mvn-wrapper.sh

```

Commit and push the new script to remote repo.

```bash
git add .
git commit -m 'added packaging script'
git push --set-upstream origin feature/apt-4
git checkout dev
git merge feature/apt-4
git push origin dev

```

# # # # # # # # # # # # # # # # # # # # # # # # # # # #

### APT 5 - Prepare Development Server Terraform Files

# # # # # # # # # # # # # # # # # # # # # # # # # # # #

Create `feature/apt-5` branch from `dev`.

```bash
git checkout dev
git branch feature/apt-5
git checkout feature/apt-5

```

Create a folder for infrastructure setup with the name of `infrastructure` under the `apartment-management-app` folder.

```bash
mkdir -p infrastructure/apt-5-dev-server-of-apartment

```

Prepare development server scripts with Terraform files (`dev-server.tf`, `dev-variable.tf`, `dev.auto.tfvars`, `apartment-userdata.sh`) for developers, enabled with Docker, Docker-Compose, Java 21, and Git, and save them under the `infrastructure` folder.

Commit and push the new script to remote repo.

```bash
git add .
git commit -m 'added terraform files for dev server'
git push --set-upstream origin feature/apt-5
git checkout dev
git merge feature/apt-5
git push origin dev

```
















# Apartman Tamirat & Envanter Yönetimi - Kurumsal DevOps Mimarisi

Bu proje, React tabanlı (Single Page Application) bir Apartman Yönetim Sisteminin tam teşekküllü bir CI/CD ve DevOps yaşam döngüsü ile nasıl dağıtıldığını göstermektedir. Proje altyapısı "Infrastructure as Code" (IaC) prensipleriyle yönetilmekte, konteynerize edilmekte ve yüksek erişilebilirlikli ortamlara (ECS & Kubernetes) otomatik olarak dağıtılmaktadır.

## 🚀 Kullanılan Teknolojiler

*   **Uygulama:** React.js, TailwindCSS, HTML5 (Statik Frontend)
*   **IaC (Altyapı Kodlaması):** Terraform
*   **Konfigürasyon Yönetimi:** Ansible
*   **Konteynerizasyon:** Docker
*   **Artifact Yönetimi:** Sonatype Nexus (Bağımlılıklar/Ara Sürümler)
*   **Konteyner Kaydı (Registry):** AWS ECR (Elastic Container Registry)
*   **CI/CD Pipeline:** Jenkins
*   **Orkestrasyon & Dağıtım:**
    *   AWS ECS (Staging / Test Ortamı)
    *   Kubernetes (EKS) (Production / Canlı Ortam)
*   **Trafik Yönetimi:** Nginx / HAProxy (Reverse Proxy)
*   **Gözlem & İzleme (Monitoring):** Prometheus & Grafana

---

## 🏗️ Mimari Şema ve CI/CD Akışı (Flow)

Aşağıdaki şema, kodun geliştiricinin bilgisayarından çıkıp canlı ortama ulaşana kadar izlediği yolu ve sistemin genel yapısını göstermektedir.

```mermaid
graph TD;
    subgraph Geliştirme & Altyapı
        Dev[Geliştirici] -->|Git Push| Repo(Git / GitHub)
        TF[Terraform] -->|Provisioning| AWS_Cloud((AWS Cloud Altyapısı))
        Ans[Ansible] -->|Konfigürasyon| Servers(Jenkins, Nexus, Monitoring)
    end

    subgraph CI/CD (Jenkins)
        Repo -->|Webhook| Jenkins[Jenkins Pipeline]
        Jenkins -->|Build & Test| DockerBuild[Docker Image Build]
        DockerBuild -->|Ara Sürüm Yedek| Nexus[Nexus Repository]
        DockerBuild -->|Push Image| ECR[AWS ECR]
    end

    subgraph Dağıtım (Deployment)
        ECR -->|Pull Image| ECS[AWS ECS - Staging Ortamı]
        ECR -->|Pull Image| K8S[Kubernetes Cluster - Prod Ortamı]
    end

    subgraph Trafik & İzleme
        Kullanici[Son Kullanıcı] --> Proxy[Reverse Proxy - Nginx/HAProxy]
        Proxy -->|Route Traffic| ECS
        Proxy -->|Route Traffic| K8S
        
        Prometheus[Prometheus] -->|Scrape Metrics| ECS
        Prometheus -->|Scrape Metrics| K8S
        Prometheus -->|Scrape Metrics| Proxy
        Grafana[Grafana] -->|Görselleştirme| Prometheus
    end


---

##

===================================

⚙️ Uygulama Nasıl Çalışıyor?
Apartman Yönetimi uygulaması, index.html içerisinde React ve Tailwind kullanılarak derlenmiş bir frontend uygulamasıdır. Herhangi bir arka uç (backend) sunucusuna ihtiyaç duymadan, verileri tarayıcının localStorage ve sessionStorage belleklerinde tutar.

DevOps mimarisinde bu uygulamayı sunmak için standart bir Nginx web sunucusu kullanılır. Uygulama Docker ile paketlenirken, temel bir Nginx imajının içine gömülerek yayınlanır.

🛠️ Adım Adım Kurulum ve Dağıtım Rehberi
Bu projeyi kendi ortamınızda ayağa kaldırmak için aşağıdaki adımları sırasıyla uygulayın.

Adım 1: Altyapı Kurulumu (Terraform)
Tüm AWS kaynakları (VPC, Subnetler, EKS, ECS, ECR, EC2 Sunucuları) Terraform ile oluşturulur.

terraform/ dizinine gidin.

Terraform'u başlatın ve altyapıyı ayağa kaldırın:

Bash
terraform init
terraform plan
terraform apply -auto-approve


### Adım 2: Sunucu Konfigürasyonları (Ansible)
Oluşturulan EC2 sunucularına gerekli yazılımların (Jenkins, Docker, Nexus, Prometheus, Grafana, Reverse Proxy) kurulması Ansible ile yapılır.
1. `ansible/` dizinine gidin.
2. Dinamik veya statik `inventory` dosyanızı güncelleyin.
3. Playbook'u çalıştırın:
   ```bash
   ansible-playbook -i inventory.ini setup-all.yml
   
Adım 3: Konteynerizasyon (Docker)
Uygulamanın çalışması için ana dizinde bulunan Dockerfile kullanılarak imaj oluşturulur.
(Not: Bu adım CI/CD sürecinde Jenkins tarafından otomatik yapılır, ancak lokal test için:)

Bash
# Örnek Dockerfile içeriği arka planda şöyledir:
# FROM nginx:alpine
# COPY index.html /usr/share/nginx/html/
# EXPOSE 80
# CMD ["nginx", "-g", "daemon off;"]

docker build -t apartman-app:latest .
docker run -p 8080:80 apartman-app:latest


Adım 4: Artifact ve İmaj Yönetimi (Nexus & ECR)
Nexus: Uygulamanın ara sürümleri ve statik dosyaları yedekleme amacıyla Nexus'a gönderilir.
AWS ECR: Jenkins, Docker imajını derledikten sonra AWS ECR'a push eder.

Bash
aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <aws_account_id>.dkr.ecr.<region>.amazonaws.com
docker tag apartman-app:latest <aws_account_id>.dkr.ecr.<region>[.amazonaws.com/apartman-app:v1](https://.amazonaws.com/apartman-app:v1)
docker push <aws_account_id>.dkr.ecr.<region>[.amazonaws.com/apartman-app:v1](https://.amazonaws.com/apartman-app:v1)


### Adım 5: Sürekli Entegrasyon (Jenkins CI/CD)
1. Ansible ile kurulan Jenkins arayüzüne (Örn: `http://<jenkins-ip>:8080`) giriş yapın.
2. Git deponuzu bağlayarak yeni bir Pipeline oluşturun.
3. Proje dizinindeki `Jenkinsfile` sayesinde kod her push edildiğinde:
   * Kod çekilir.
   * Docker build işlemi yapılır.
   * İmaj ECR ve Nexus'a push edilir.
   * Başarılı olursa ECS veya Kubernetes'e otomatik deployment tetiklenir.

### Adım 6: Ortamlara Dağıtım (ECS & Kubernetes)
* **Staging Ortamı (ECS):** Jenkins, `aws ecs update-service` komutu ile test ortamındaki Fargate/EC2 görevlerini yeni imajla günceller.
* **Production Ortamı (Kubernetes):** Canlı ortam için `kubectl` kullanılarak deployment yapılır.
  ```bash
  kubectl apply -f k8s/deployment.yaml
  kubectl apply -f k8s/service.yaml
  
Adım 7: Trafik Yönlendirme (Reverse Proxy)
Kullanıcıların tek bir IP/Domain üzerinden ortamlara erişmesi için Nginx Reverse Proxy kullanılır. Ansible ile yapılandırılan Nginx sunucusu:

staging.apartman.com -> AWS ECS Load Balancer'a

app.apartman.com -> Kubernetes Ingress / Load Balancer'a yönlendirir.

Adım 8: İzleme ve Alarm (Prometheus & Grafana)
Sistemin sağlığını izlemek için:

Prometheus, ECS metriklerini (CloudWatch exporter üzerinden) ve Kubernetes Node/Pod metriklerini sürekli toplar.

Grafana arayüzüne (http://<grafana-ip>:3000) girerek hazır dashboardlar üzerinden uygulamanın CPU/RAM tüketimini ve trafik durumunu anlık olarak izleyebilirsiniz.

Herhangi bir çökme durumunda Prometheus Alertmanager üzerinden Slack/Mail bildirimleri gönderilir.


http://localhost:8080/swagger-ui/index.html

