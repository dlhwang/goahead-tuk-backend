# Dependencies

## Internal Dependencies

Text dependency view:

1. `author/api` depends on `author/application`.
2. `author/application` depends on `author/domain` repository contracts.
3. `author/infrastructure` implements `author/domain` persistence boundaries.
4. `confession/api` depends on confession use case ports.
5. `confession/application` depends on confession domain contracts and the
   author application service for author creation during writes.
6. `confession/infrastructure` implements confession persistence, id generation,
   and reaction counting.
7. `common` is used by the backend for error handling and security behavior.

## External Dependencies

- **Kotlin JVM plugin `2.2.21`**: Kotlin compilation.
- **Kotlin Spring plugin `2.2.21`**: Spring-compatible Kotlin behavior.
- **Kotlin JPA plugin `2.2.21`**: JPA-friendly Kotlin classes.
- **Spring Boot plugin `4.0.6`**: Application and dependency platform.
- **Spring dependency management plugin `1.1.7`**: Managed dependency versions.
- **Spring Web starter**: HTTP API runtime.
- **Spring Security starter**: Security filter chain.
- **Spring Actuator starter**: Operational endpoints.
- **Spring Data JPA starter**: Persistence.
- **springdoc OpenAPI UI `3.0.3`**: API documentation UI.
- **Jackson Kotlin module**: Kotlin JSON support.
- **Java UUID Generator `5.2.0`**: UUID generation support.
- **H2**: Local database.
- **PostgreSQL JDBC driver**: Runtime database driver.
- **Spring Boot test starter**: Backend tests.
- **Kotlin test JUnit5**: Kotlin test integration.

## Dependency Notes

- No outbound service client dependency was found in the inspected backend code.
- The application layer generally depends inward on domain contracts, while JPA
  implementation details remain in infrastructure adapters.
- The repository-wide documentation and workflow automation dependencies are
  separate from backend runtime dependencies.
