# Spring Boot/React JWT

### A simple stateless JWT auth implementation using Spring Boot and React. 

Spring Boot's auto-configured CSRF protection is disabled. Instead, this implementation creates it's own stateless CSRF 
protection using `java.util.UUID.randomUUID`. 

The CSRF token is placed as a claim in the JWT payload, and the JWT is 
sent to the React client as an HttpOnly cookie. 

On successful authentication, the response headers from the API are:
```
CSRF: d770603a-5aaa-40e6-b7d2-5178e1373b2d
Set-Cookie: JWT=eyJ... 9w; HttpOnly

```

Then on the React side, an HTTP interceptor takes the CSRF token and saves it in local storage.
On each requrest from the client, the interceptor adds the CSRF header and the API verifies that the CSRF token in both
the (HttpOnly cookie) JWT matches the CSRF header value (as well as the other claims).  

See: [100% Stateless with JWT (JSON Web Token) by Hubert Sablonnière](https://www.youtube.com/watch?v=67mezK3NzpU) for
full explanation.


Versions used for Spring Boot && React: 
```
Spring Boot: 2.2.0 MILESTONE 5
React (create-react-app): 16.9 (3.1)
```
