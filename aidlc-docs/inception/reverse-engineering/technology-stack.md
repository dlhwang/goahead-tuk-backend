# Technology Stack

## Programming Languages

- **Kotlin**: Kotlin Gradle plugin `2.2.21`; backend source and tests.
- **Java**: Java toolchain `21`; JVM runtime target.
- **Markdown**: Repository content for AI-DLC rules and documentation.
- **Python**: `scripts/` tool workspaces for evaluator, design review, and
  traceability tooling.

## Frameworks

- **Spring Boot**: Gradle plugin `4.0.6` for backend application runtime.
- **Spring MVC**: `spring-boot-starter-web` for HTTP controllers.
- **Spring Security**: `spring-boot-starter-security` for security filter chain
  and CORS.
- **Spring Data JPA**: `spring-boot-starter-data-jpa` for persistence
  repositories and adapters.
- **springdoc OpenAPI UI**: `3.0.3` for API documentation UI support.

## Infrastructure

- **H2**: Local in-memory persistence in PostgreSQL compatibility mode.
- **PostgreSQL**: Runtime relational database driver and Railway profile target.
- **Docker**: Container deployment support in the current working tree.
- **Railway**: Deployment configuration hint in the current working tree.
- **GitHub Actions and CodeBuild**: Repository validation and AI-DLC release
  automation.

## Build Tools

- **Gradle Kotlin DSL**: Backend build, dependency management, and tests.
- **`npx markdownlint-cli2`**: Markdown linting.
- **`uv`**: Python tooling and evaluator test execution.

## Testing Tools

- **JUnit Platform**: Kotlin backend tests.
- **Spring Boot test starter**: Spring integration support.
- **Kotlin test and AssertJ usage**: Unit-level assertions and test APIs.
- **`pytest`**: Script-level Python tests.
