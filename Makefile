HASH := 

build-project:
	$(MAKE) -C ipfs-status-service/ build-service
	$(MAKE) -C ipfs-status-app/ build-app

start-project: build-project
	docker-compose up -d

stop-project:
	docker-compose down -v --remove-orphans

compile-app:
	cd ipfs-status-app && ng build --prod --aot

add:
	ipfs add -r ipfs-status-app/dist/ipfs-status-app

pin: 
	curl https://ipfs.infura.io:5001/api/v0/pin/add?arg=/ipfs/$(HASH)
