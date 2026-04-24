.PHONY: up rebuild down logs ps build restart clean prune

up:
	docker compose up -d
	@echo ""
	@echo "  Frontend : http://localhost:3000"
	@echo "  Backend  : http://localhost:3001"
	@echo "  Swagger  : http://localhost:3001/swagger-ui.html"
	@echo "  MinIO    : http://localhost:9001"

rebuild:
	docker compose up -d --build

down:
	docker compose down

restart:
	docker compose restart

build:
	docker compose build

logs:
	docker compose logs -f

ps:
	docker compose ps

prune:
	docker image prune -f

clean:
	docker compose down --rmi local -v
