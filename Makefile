build-project:
	$(MAKE) -C ipfs-status-service/ build-service
	$(MAKE) -C ipfs-status-app/ build-app
