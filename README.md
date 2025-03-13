# Product Management API (Spring Boot)

> - A barebones scenario to practice REST API development using Spring Boot.
>
> - The requests to the server are cached utilizing the `spring-boot-starter-cache` package.
>
> - Supports multiple nodes with cache synchronization with `Hazelcast`.

## To run the app (Single Node)

Use the tools provided by your IDE or navigate to the root of the project via command line and execute the command

```shell
mvn spring-boot:run
```

The application will run on http://localhost:8080/.

### To see the API documentation

Visit http://localhost:8080/swagger-ui/index.html. This endpoint is automatically generated with the
[
`the springdoc-openapi-starter-webmvc-ui`](https://central.sonatype.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui)
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

   You can create as many nodes as you want, provided that the `--server.port` arguments are set to be **unique** across
   runs.
   The created nodes will share a distributed cache between them, as enabled by Hazelcast.*

   The applications will start on `localhost` using the ports specified by the `--server.port` arguments.

   > [!NOTE]
   > Although these nodes share a cache, the underlying datasets on which they depend are **not distributed**.

<details>

<summary>Caching Demonstration</summary>

For convenience, the application uses an in-memory H2 database, which does not support distributed operations. However, this setup clearly demonstrates distributed caching communication between the nodes, as shown in the following example.

### Example

> Prerequisites: two nodes running on ports 8080 and 8081 on localhost (see [To Run Multiple Nodes](#to-run-multiple-nodes)).

> [!NOTE]
> These examples rely on the assumption that all H2 databases start out empty.

1. Add a product to the node running on port 8080:
   ```shell
   curl --location 'http://localhost:8080/products/' \
   --header 'Content-Type: application/json' \
   --data '{
       "name": "laptop1",
       "price": 100
   }'

   # Expected response:
   # {
   #     "id": 1,
   #     "name": "laptop1",
   #     "price": 100
   # }
   ```

2. Search for the added product on the other node (port 8081):
   ```shell
   curl --location 'http://localhost:8081/products/1'

   # Expected response: 404 since the product is not in the global cache or in the local DB.
   ```

3. Search for the added product on the original node (port 8080):
   ```shell
   curl --location 'http://localhost:8080/products/1'

   # Expected response:
   # {
   #     "id": 1,
   #     "name": "laptop1",
   #     "price": 100
   # }
   ```
   This saves the response in the distributed cache.

4. Repeat step 2:
   ```shell
   curl --location 'http://localhost:8081/products/1'

   # Expected response (retrieved from the distributed cache):
   # {
   #     "id": 1,
   #     "name": "laptop1",
   #     "price": 100
   # }
   ```

</details>
