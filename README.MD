
# Different ways of starting Tarantool container during tests

[![Java CI with Maven](https://github.com/andrei-punko/tarantool-testcontainers-usage/actions/workflows/maven.yml/badge.svg)](https://github.com/andrei-punko/tarantool-testcontainers-usage/actions/workflows/maven.yml)

## Prerequisites

- JDK 21
- Docker

## Example of TestContainers library usage

TestContainers Tarantool module situated [here](https://github.com/tarantool/cartridge-java-testcontainers)

## Example of Palantir-Docker library usage

For Win OS need to add env variables with path to Docker executable files `docker.exe` and `docker-compose.exe`:

      DOCKER_LOCATION=C:\\Program Files\\Docker\\Docker\\resources\\bin\\docker.exe
      DOCKER_COMPOSE_LOCATION=C:\\Program Files\\Docker\\Docker\\resources\\bin\\docker-compose.exe

  It could be done using next commands:

      setx DOCKER_LOCATION "C:\\Program Files\\Docker\\Docker\\resources\\bin\\docker.exe"
      setx DOCKER_COMPOSE_LOCATION "C:\\Program Files\\Docker\\Docker\\resources\\bin\\docker-compose.exe"

  These variables used by the library Palantir-Docker.
