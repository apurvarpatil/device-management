.PHONY: build run docker-build docker-run docker-stop compose-up compose-down clean help

# Variables
APP_NAME = device-management
PORT = 8080
TAG = latest

help:	## Show this help menu
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

build:	## Build the application
	./gradlew clean build

docker-build:	## Build Docker image
	docker build -t $(APP_NAME):$(TAG) .

docker-run:	## Run Docker container
	docker run -d --name $(APP_NAME) -p $(PORT):$(PORT) $(APP_NAME):$(TAG)

docker-stop:	## Stop Docker container
	docker stop $(APP_NAME) || true
	docker rm $(APP_NAME) || true

compose-up:	## Start all services with Docker Compose
	docker-compose up -d

compose-down:	## Stop all services
	docker-compose down

clean:	## Clean build files
	./gradlew clean
	docker system prune -f