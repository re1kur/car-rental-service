PROFILE    ?= local
BUCKET     := developer
MINIO_USER := developer
MINIO_PASS := developer
SEED_DIR   := docker/minio/seed
MC_IMAGE   := minio/mc:latest

.DEFAULT_GOAL := help
.PHONY: help start up up-infra down reset clean logs ps seed-images run-local env wait-minio

help:
	@echo "RentCar — commands (make <command>):"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) \
		| awk 'BEGIN {FS = ":.*?## "}; {printf "  %-12s %s\n", $$1, $$2}'

start: up seed-images ## Запустить ВЕСЬ стек в Docker (приложение + инфра + картинки)
	@echo ""
	@echo "Ready. http://localhost:8080"

up: env ## Собрать и поднять весь стек, включая приложение (compose up --build)
	docker compose up -d --build

up-infra: env ## Поднять только инфраструктуру (Postgres, Keycloak, MinIO), без приложения
	docker compose up -d postgres kc-postgres keycloak minio

run-local: env ## Запустить приложение ЛОКАЛЬНО через Maven (нужна поднятая инфра: make up-infra)
	./mvnw spring-boot:run -Dspring-boot.run.profiles=$(PROFILE)

seed-images: wait-minio ## Залить дефолтные картинки (docker/minio/seed) в MinIO
	@echo "Uploading images into bucket '$(BUCKET)'..."
	docker run --rm --network host -v "$(PWD)/$(SEED_DIR)":/seed:ro --entrypoint sh $(MC_IMAGE) -c '\
	  mc alias set local http://localhost:9000 $(MINIO_USER) $(MINIO_PASS) && \
	  mc mb --ignore-existing local/$(BUCKET) && \
	  mc anonymous set download local/$(BUCKET) && \
	  ( ls /seed/*.png >/dev/null 2>&1 && mc cp /seed/*.png local/$(BUCKET)/ || echo "No images (*.png) в $(SEED_DIR)" )'

logs: ## Смотреть логи приложения (Ctrl+C — выйти)
	docker compose logs -f app

ps: ## Показать статус контейнеров
	docker compose ps

down: ## Остановить и удалить все контейнеры
	docker compose down

reset: down up seed-images ## Пересоздать стек с нуля (свежая БД)
	@echo "Stack is recreated with cleaned db."

clean: ## Полная очистка: контейнеры, тома, сеть, собранный образ и target/
	docker compose down -v --rmi local --remove-orphans
	rm -rf target
	@echo "Cleaned: containers, tomes, network, image of app and target/."

env:
	@test -f .env || (cp .env.example .env && echo ".env is created from .env.example")

wait-minio:
	@echo "Waiting for readiness MinIO..."
	@until curl -sf http://localhost:9000/minio/health/ready >/dev/null 2>&1; do sleep 2; done