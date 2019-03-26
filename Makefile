build-project:
	$(MAKE) -C ipfs-status-service/ build-service
	$(MAKE) -C ipfs-status-app/ build-app

start-project: build-project
	docker-compose up -d

stop-project:
	docker-compose down -v --remove-orphans
