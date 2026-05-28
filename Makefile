PROFILE   ?= local
BUCKET    := developer
MINIO_USER := developer
MINIO_PASS := developer
SEED_DIR  := docker/minio/seed
MC_IMAGE  := minio/mc:latest

.PHONY: help env up down reset wait-keycloak wait-minio seed-images run start

help:
	@echo "make start        - одной командой: инфра + картинки + приложение"
	@echo "make up            - поднять инфру (postgres, keycloak, minio)"
	@echo "make seed-images  - залить дефолт-картинки в MinIO"
	@echo "make run          - запустить приложение (профиль $(PROFILE))"
	@echo "make reset        - down + заново поднять инфру (свежая БД)"
	@echo "make down         - погасить стек"

env:
	@test -f .env || (cp .env.example .env && echo ".env создан из .env.example")

up: env
	docker compose up -d

down:
	docker compose down

wait-keycloak:
	@echo "Ждём Keycloak..."
	@until curl -sf http://localhost:9090/realms/rental-service-realm/.well-known/openid-configuration >/dev/null 2>&1; do sleep 3; done
	@echo "Keycloak готов."

wait-minio:
	@echo "Ждём MinIO..."
	@until curl -sf http://localhost:9000/minio/health/ready >/dev/null 2>&1; do sleep 2; done
	@echo "MinIO готов."

seed-images: wait-minio
	@echo "Заливаю картинки из $(SEED_DIR) в бакет '$(BUCKET)'..."
	docker run --rm --network host -v "$(PWD)/$(SEED_DIR)":/seed:ro --entrypoint sh $(MC_IMAGE) -c '\
	  mc alias set local http://localhost:9000 $(MINIO_USER) $(MINIO_PASS) && \
	  mc mb --ignore-existing local/$(BUCKET) && \
	  mc anonymous set download local/$(BUCKET) && \
	  ( ls /seed/*.png >/dev/null 2>&1 && mc cp /seed/*.png local/$(BUCKET)/ || echo "Картинок (*.png) в $(SEED_DIR) пока нет" )'
	@echo "Готово."

run:
	./mvnw spring-boot:run -Dspring-boot.run.profiles=$(PROFILE)

start: up wait-keycloak seed-images run

reset: down up wait-keycloak seed-images
	@echo "Инфра пересоздана (БД свежая). Запусти 'make run'."
