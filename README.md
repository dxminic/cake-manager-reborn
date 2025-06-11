# Cake Management Application

## Features

This application tackles the following issues set out in the project readme:

- [x] **CRUD operations on Cakes**: Provide a RESTful API to create, read, update, and delete cakes.
- [x] **External API Integration**: Fetch cakes from an external API and store them in the database.
- [x] **Some unit tests**: Implemented unit tests for the service.

On Application startup, the CakeConsumerService will fetch the cakes from the external API and store them in the
database.

Any changes made to the cakes through the API will be persisted in the database.

## Local Development

To run the application, use the following command:

```bash
  mvn clean package
```

In IntelliJ IDEA, you can run the application by executing the `Application` class.

You must provide the environment variable `MICRONAUT_ENVIRONMENTS=local` which spins up the local in-mem H2 DB, serves
the endpoints on port 8080, and pulls the URL for fetching the cakes.

This can be done by setting it in your IDE or by exporting it in your terminal before running the application.

## Architecture

The application is built using the Micronaut framework and follows a layered architecture:

- **Controller Layer**: Handles HTTP requests and responses.
- **Service Layer**: Contains business logic and interacts with the repository layer.
- **Repository Layer**: Interacts with the database using Micronaut Data.
- **Model Layer**: Represents the data structure of the application.
- **Configuration Layer**: Contains application configuration and properties.
- **External API Integration**: Fetches cakes from an external API and stores them in the database.
- **Database Migration**: Uses Liquibase for managing database schema changes.

The models make use of the `@MappedEntity` annotation to define the database schema, and the repository layer uses
Micronaut Data's `@Repository` annotation to provide CRUD operations.

## Future Enhancements

CakeConsumerService is marked as `@Singleton` to ensure it is instantiated once and can be used throughout the
application. It fetches cakes from an external API and stores them in the database using the CakeRepository. If we
wanted to make this service more robust, we could add error handling and logging to ensure that any issues with fetching
or storing cakes are properly managed.

Additionally, we could implement a caching mechanism to reduce the number of calls made to the external API, especially
if the data does not change frequently. This would improve performance and reduce latency for users accessing the cake
data.

Furthermore, we could enhance the API by adding features such as:

- Pagination and sorting for the list of cakes.
- Search functionality to find cakes by name or other attributes.
- Authentication and authorization to secure the API endpoints.
- Containerization using Docker to simplify deployment and scaling.
- More comprehensive unit tests to cover edge cases and ensure the reliability of the service. In the interest of time,
  we have only implemented basic unit tests for the service layer.
- Integration tests to verify the end-to-end functionality of the application, including interactions with the database
  and external API. This could be implemented using Micronaut's testing framework, which provides support for testing
  controllers, services, and repositories.

The project already has Micronaut Security included, which can be used to implement authentication and authorization
features. To implement authentication, we could use JWT tokens or OAuth2, depending on the requirements. For
authorization, we could define roles and permissions to control access to different API endpoints.

Docker was not used in this project, but it can be easily integrated by creating a Dockerfile that builds the
application and runs it in a container. This would allow for easy deployment and scaling of the application in different
environments. Utilising something like Devcontainer for this project would also allow for a consistent development
environment across different machines, making it easier for developers to contribute to the project.

---

## Micronaut 4.8.2 Documentation

- [User Guide](https://docs.micronaut.io/4.8.2/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.8.2/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.8.2/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
- [Micronaut Maven Plugin documentation](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/)

## Feature security documentation

- [Micronaut Security documentation](https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html)

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#nettyHttpClient)

## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)

## Feature liquibase documentation

- [Micronaut Liquibase Database Migration documentation](https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/index.html)

- [https://www.liquibase.org/](https://www.liquibase.org/)

## Feature data-jdbc documentation

- [Micronaut Data JDBC documentation](https://micronaut-projects.github.io/micronaut-data/latest/guide/index.html#jdbc)

## Feature jul-to-slf4j documentation

- [https://www.slf4j.org/legacy.html#jul-to-slf4jBridge](https://www.slf4j.org/legacy.html#jul-to-slf4jBridge)

## Feature maven-enforcer-plugin documentation

- [https://maven.apache.org/enforcer/maven-enforcer-plugin/](https://maven.apache.org/enforcer/maven-enforcer-plugin/)

## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)

## Feature jdbc-hikari documentation

- [Micronaut Hikari JDBC Connection Pool documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc)


