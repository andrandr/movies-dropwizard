# Simple movie database powered by [Dropwizard](http://www.dropwizard.io/) & [Spring Boot](http://projects.spring.io/spring-boot/)
This project is an implementation of a very simple movie database.

## Testing
To run all automated tests issue the following command in the terminal:
```
$ mvn test
```

## Running
To run the application issue the following command:
```
$ mvn clean install
$ java -jar target/movies-dropwizard-0.1.0.jar server config.yml
```
It will start a local HTTP server listening on port `8080`, equipped with a H2 database. This is an in-memory database so all data stored in it will be lost once the application is shut down. As such, it is a good choice if you want to just play with available REST endpoints without rolling your own database.

However, you can also run the application with your own PostgreSQL database. To do that, simply edit `src/main/resources/application-postgres.properties` and modify the three properties named `datasource.url`, `datasource.username`, `datasource.password`. Then, issue the following command in the terminal:
```
$ mvn clean install
$ java -Dspring.profiles.active=postgres -jar target/movies-dropwizard-0.1.0.jar server config.yml
```
This will start the HTTP server listening on port `8080` and connect the application to the specified database. All needed database objects will be automatically created.

## Endpoints
The application exposes serveral REST endpoints for manipulating users and their movies. All of them expect data in JSON format and return JSON as output.

Every endpoint, except `POST /users` and `GET /status`, expects an access token which must be passed as an HTTP header with name `X-Auth-Token`. This access token must be obtained via a call to `POST /users` (see below). Passing illegal access token causes an HTTP error `404 Not found`.

### Users

#### Registering user account: `POST /users`
Input: `{"login":"user login", "password":"user password"}`  
Output: `{"accessToken":"access token"}`  
This endpoint causes the specified user credentials to be registered in the user database. It returns an access token associated with that user, which must be later passed to all other user- and movie-related endpoints in a header named `X-Auth-Token`.  
Calling this endpoint with the same data causes the same access token to be returned.  
Calling this endpoint with login of an existing user but wrong password causes an HTTP error `403 Forbidden`.

#### Deleting user account: `DELETE /users`
Input: none  
Output: none  
This endpoint deletes user account associated with the specified access token. All movies of that user are also deleted.  
Calling this endpoint with an unknown access token causes an HTTP error `404 Not found`.

### Movies

#### Getting list of user movies: `GET /movies`
Input: none  
Output: list of movie data: `{"id":"movie id", "title":"movie title", "description":"movie description", "watched":true|false}`  
This endpoint returns all movies previously added by the user associated with the specified access token.  
Calling this endpoint with an unknown access token causes an HTTP error `404 Not found`.

#### Filtering user movies by watched flag: `GET /movies?watched=true|false`
This endpoint returns a list of user's watched movies (in case of `watched=true`) or not watched movies (in case of `watched=false`).

#### Adding user movie: `POST /movies`
Input: `{"title":"movie title", "description":"movie description", "watched":true|false}`  
Output: `{"id":"movie id", "title":"movie title", "description":"movie description", "watched":true|false}`

#### Editing user movie: `PUT /movies/{movieId}`
Input: `{"id":"movie id", "title":"movie title", "description":"movie description", "watched":true|false}`  
Output: `{"id":"movie id", "title":"movie title", "description":"movie description", "watched":true|false}`  
This endpoint updates the information about the movie with the specified ID.  
Calling this endpoint with ID of an unknown movie causes an HTTP error `404 Not found`.

#### Deleting user movie: `DELETE /movies/{movieId}`
Input: none  
Output: none  
This endpoint deletes movie with the specified ID.  
Calling this endpoint with ID of an unknown movie causes an HTTP error `404 Not found`.

### Health

#### Application status: `GET /status`
Input: none  
Output: `{"status":"ok"}`  
This endpoint acts as a mean of checking if the application is up and running.
