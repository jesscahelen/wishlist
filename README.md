# Wishlist

Create and manage your wishlist.

- Add a product to a client's wishlist
- Remove a product from the wishlist
- Verify if the product is already in wishlist
- Get all products from wishlist

## Requirements

- Java JDK 17
- Docker
- Docker Compose
- Gradle

## Installation and Execution

Build the project with Gradle:

```bash
gradle build
```

Start mongodb container with docker-compose:

```bash
docker-compose up -d
```

Check swagger documentation:

```
http://localhost:8080/swagger-ui.html
```

## Tests

Run unit tests:

```bash
gradle test
```

## Code coverage

You can generate code coverage reports using Jacoco.

```bash
gradle jacocoTestReport
```

After running the tests, you can find the report at `build/reports/jacoco/test/index.html`.