## Development

To start your application in the dev profile, simply run:

    ./mvnw

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].
## Swagger
Access link below to view list of apis
    
    http://localhost:8888/swagger-ui.html

## Building for production

### Packaging as jar

To build the final jar and optimize the userManagementService application for production, run:

    ./mvnw -Pprod clean verify

To ensure everything worked, run:

    java -jar target/*.jar

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

    ./mvnw -Pprod,war clean verify

## Testing

To launch your application's tests, run:

    ./mvnw verify

For more information, refer to the [Running tests page][].

### Code quality

Run all unit tests

````
./mvn test
````

Run all unit tests and integration tests

````
./mvnw verify
````

Run all test with Sonar analysis:

```
./mvnw clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

