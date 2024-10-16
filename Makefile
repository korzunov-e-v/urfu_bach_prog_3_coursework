build:
	docker compose build

build-no-cache:
	docker compose build --no-cache

up:
	docker compose up -d --force-recreate

down:
	docker compose down

up-app:
	docker compose up app -d --force-recreate

down-app:
	docker compose down app

logs:
	docker compose logs --tail 100

shell:
	docker compose run -it --remove-orphans app /bin/bash
