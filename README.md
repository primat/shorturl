# shorturl
Simple URL shortener made with Spring Boot.

The app features a web server with a form for getting a short URL from a long one. 
It also handles the redirection of these short URLs and has a REST controller for CRUD operations. Finally, it has
an embedded HSQLDB which persists mapped URLs between app reloads.

The design is simple: use a base 62 representation of an autoincrement ID to represent the short URL (path). 
Base 62 numbers are represented in the same way that hexadecimal numbers are: with letters corresponding 
to digits between 10 and 61 (i.e. all lower and uppercase letters) 
Using the ID this way is a little bit awkward but leverages database features like autoincrement and sorting. 

The short URL path can contain anywhere from 1 to 10 characters. This means, the app will generate up to 62^10
short URLs with "0" being the smallest and "ZZZZZZZZZZ" being the largest.

## Getting Started

Try and get the project set up in your favorite IDE (I'm using Intellij).
Edit application.properties to match your own environment. Make sure the first time you run the app, to use 
```
spring.jpa.hibernate.ddl-auto=create
```
so that it creates the database. Then use:
```
spring.jpa.hibernate.ddl-auto=validate
```
so that the DB doesn't get dropped. (I'll fix this shortly)

You may want to update your hosts file to include
```
cl.ip   127.0.0.1
```
But this is not mandatory.

Run maven/ buid project. Then, navigate to http://cl.ip/ (or whatever you have configured.)

The web server root (e.g. http://cl.ip/) is a page with a field for entering a URL. 
Submitting this URL will call a REST endpoint and return an existing or new short version of the URL. 

The following URL will handle the redirection from the short URL to the long one: http://cl.ip/^[a-zA-Z0-9]{1,10}$, if it exists in storage. 
If it doesn't a HTTP 404 is returned instead.

### Prerequisites

JDK 1.8+


## Running the tests

Run tests as you would any other Spring Boot project. Personally, I have them configured to run through a config in the IDE.

### Unit tests

There's basically one class with some meat on it:  the Base62Service class.
This class takes care of the conversion between decimal and base 62 numbers and the unit tests cover it.

### Integration tests
Work in progress.

