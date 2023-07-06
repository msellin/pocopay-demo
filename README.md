## Usage

* Prerequisites
    * Java 17
    * Gradle 8
    * Docker
    * etc
* Installation
    * Clone the repo
    * ./gradlew clean build
    * docker-compose up (or ./run.sh)
    * docker-compose down

## Notes
* added rabbitmq just to demonstrate messages being sent and received (not using it for crediting though)
* tests expect db and rabbitmq to be running (docker-compose -f db.yml -f rabbitmq.yml up)
