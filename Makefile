 clean:
	cd gateway && ./gradlew clean
	cd auth && ./gradlew clean
	cd recommender && ./gradlew clean
	cd movie && ./gradlew clean

gate:
	cd gateway && ./gradlew build -x test

auth:
	cd auth && ./gradlew build -x test

recommender:
	cd recommender && ./gradlew build -x test

movie:
	cd movie && ./gradlew build -x test

# Wszystkie taski budowania pojedynczego modułu zostały zastąpione tym jednym.
# Teraz można to wywołać poprzez
# make runone NAME=[nazwa modułu]
# np
# make runone NAME=gateway
# Możecie też dodać do .bashrc (lub .bash_profile na maku) taką funkcję:
#function rebuild() {
#    size=${#1}
#    if [ $size == 0 ]
#    then
#      make all -B
#    else
#      make runone NAME="$1"
#    fi
#}

# Wtedy można to odpalać poprzez rebuild [nazwa modułu],
# np.
# rebuild gateway

runone:
	cd ${NAME} && ./gradlew clean build -x test
	docker-compose build ${NAME}
	docker-compose up -d ${NAME}

run:
	docker-compose build
	docker-compose up -d

stop:
	docker-compose stop

build: auth gate recommender movie

all: clean build run

push: clean build
	docker-compose build
	docker-compose push

docker-clean:
	docker rm $(docker ps -aq)
	docker rmi $(docker images -aq)
