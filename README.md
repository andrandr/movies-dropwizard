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

## Endpoints
The application exposes serveral REST endpoints for manipulating users and their movies. All of them expect data in JSON format and return JSON as output.

Every endpoint, except `POST /users` and `GET /status`, expects an access token which must be passed as an HTTP header with name `X-Auth-Token`. This access token must be obtained via a call to `POST /users` (see below). Passing illegal access token causes an HTTP error `404 Not found`.

### Users

#### Registering user account: `POST /users`
Input: `{"login":"user login", "password":"user password"}`  
Output: `{"accessToken":"access token"}`  
This endpoint causes the specified user credentials to be registered in the user database. It returns an access token associated with that user, which must be later passed to all other endpoints in a header named `X-Auth-Token`.  
Calling this endpoint with the same data causes the same access token to be returned.  
Calling this endpoint with login of an existing user but wrong password causes an HTTP error `403 Forbidden`.

#### Deleting user account: `DELETE /users`
Input: none  
Output: none  
This endpoint deletes user account associated with the specified access token. All movies of that user are also deleted.  
Calling this endpoint with an unknown access token causes an HTTP error `404 Not found`.

### Movies

### Health

#### Application status: `GET /status`
Input: none  
Output: `{"status":"ok"}`  
This endpoint acts as a mean of checking if the application is up and running.
