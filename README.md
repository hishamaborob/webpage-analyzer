# Web Page Analyzer

Simple MVC web app based on Spring Boot and Java 8. (This is the first time I modify an HTML template)

This app connects to a remote URL, parse HTML, and return some analysis.

Normal Controller handles the request and validate URL before passing it to the service with analysing criteria.

Service uses helper to connect and parse remote HTML document.

Helper component for HTML parsing, makes it easy to connect during runtime and to be mocked during test.

Service methods are basic and dynamic, they do one thing, and are provided with analysis criteria of choice.

Criteria is a list of Lambda functions that are executed by the service to generate analysis.

Current written criteria is basic, 
it might no be a production ready (And it based on Backend Engineer limited knowledge with HTML UI tags).

Service is tested with integration test and mocking, this test doesn't cover every single case.

### Summary

- Spring Boot, Java 8, Tomcat, HTTP.
- jsoup: Java HTML Parser.
- Service, Component, validator, and Entity.
- Basic Integration Test and mocking.
- Patterns: MVC, DI, Single-Responsibility, Opened-Closed.

### Usage

Requirements:

```
Java 8, port 8088
```

Testing and Packaging:

```
mvn clean instal
```

Run the Application:

```
java -jar target/webpage-analyzer-0.1.0.jar
```

Interface:

```
http://localhost:8088/webpage-analyzer
```
