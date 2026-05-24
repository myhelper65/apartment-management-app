#!/bin/bash

# Herhangi bir komut hata verirse betiğin çalışmasını durdurun
set -e

# Uygulamayı Maven Wrapper ile paketleyin
./mvnw clean package

# Tek Dockerfile üzerinden Apartman Yönetimi Uygulaması Docker İmajını İnşa Edin
docker build  -t "apartment-app:dev" .