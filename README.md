# Rick and Morty

## 📚Project description:
This project is a Java Client for Rick and Morty api https://rickandmortyapi.com/documentation/

## 📂Structure
* `controller` - contains endpoints
* `exception` - custom exceptions
* `model` - models for entities in DB
* `repository` - communication with DB
* `service` - contains business logic and connects controller with dao
* `service/mapper` - contains mappers for dtos
* `util` - contains patterns required for parsing json data

## 🔧Endpoints
All endpoints use HTTP Get method
* `/characters`
    * `/{id}` - get the episode by id
    * `/random` - get the episode randomly
    * `/all` - get all episodes
      * `/by-pattern` - get all episodes by a pattern in their names
* `/episodes`
  * `/{id}` - get the episode by id
  * `/all` - get all episodes
* `/locations`
  * `/{id}` - get the location by id
  * `/all` - get all locations

## ⚙️Used technologies and libraries:
* JDK 17;
* Spring Boot 3.1.2
* Spring Data;
* PostgreSQL;
* Lombok