# Product Management API (Spring Boot)

> - A barebones scenario to practice REST API development using Spring Boot.
>
> - The requests to the server are cached utilizing the `spring-boot-starter-cache` package.
>
> - Supports multiple nodes with cache synchronization with `Hazelcast`.

## To run the app

Use the tools provided by your IDE or navigate to the root of the project via command line and execute the command

```shell
mvn spring-boot:run
```

The application will run on http://localhost:8080/.

### To see the API documentation

Visit http://localhost:8080/swagger-ui/index.html. This endpoint is automatically generated with the
[`the springdoc-openapi-starter-webmvc-ui`](https://central.sonatype.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui)
package.

## To Run Multiple Nodes

1. Navigate to the root of the project via the command line and compile the application with the following command:

    ```shell
    mvn clean package -DskipTests
    ```

2. Run the generated JAR file in different terminals:

    ```shell
    # Run this in terminal 1:
    java -jar target/demo-0.0.1-SNAPSHOT.jar --server.port=8080 --management.server.port=9090
    # .........................

    # Run this in terminal 2:
    java -jar target/demo-0.0.1-SNAPSHOT.jar --server.port=8081 --management.server.port=9090
    # .........................
    ```

   You can create as many nodes as you want like this, provided that the `--server.port` arguments are set to be **unique** across runs.

   The applications will start on `localhost` with the ports specified by the `--server.port` arguments.
