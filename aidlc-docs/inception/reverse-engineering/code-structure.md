# Code Structure

## Build System

- **Type**: Gradle Kotlin DSL
- **Configuration**: `build.gradle.kts`, `settings.gradle.kts`, Gradle wrapper scripts
- **Runtime target**: Java 21 with Kotlin JVM compilation

## Key Modules

- **`src/main/kotlin/io/goahead/tuk/author`**: Author API and persistence flow.
- **`src/main/kotlin/io/goahead/tuk/confession`**: Confession API, domain,
  reactions, and persistence.
- **`src/main/kotlin/io/goahead/tuk/common`**: Cross-cutting HTTP and
  configuration behavior.
- **`src/main/resources`**: Spring profiles and datasource configuration.
- **`aidlc-rules`**: Distributed AI-DLC workflow rules.
- **`docs`**: Human-facing guides.
- **`scripts`**: AI-DLC helper tooling.

## Existing Files Inventory

### Backend Entry and Common Files

- `src/main/kotlin/io/goahead/tuk/GoaheadTukApplication.kt` - Spring Boot entry point.
- `src/main/kotlin/io/goahead/tuk/common/IdGenerator.kt` - Shared identifier abstraction.
- `src/main/kotlin/io/goahead/tuk/common/api/ApiExceptionHandler.kt` - HTTP error mapping.
- `src/main/kotlin/io/goahead/tuk/common/config/SecurityConfig.kt` - Security and CORS configuration.

### Author Files

- `src/main/kotlin/io/goahead/tuk/author/api/AuthorController.kt` - Author endpoints.
- `src/main/kotlin/io/goahead/tuk/author/api/dto/AuthorResponse.kt` - Author response DTO.
- `src/main/kotlin/io/goahead/tuk/author/api/dto/SaveAuthorRequest.kt` - Author write request DTO.
- `src/main/kotlin/io/goahead/tuk/author/application/AuthorService.kt` - Author application service.
- `src/main/kotlin/io/goahead/tuk/author/application/command/SaveAuthorCommand.kt` - Save command.
- `src/main/kotlin/io/goahead/tuk/author/application/port/SaveAuthorUseCase.kt` - Save use case contract.
- `src/main/kotlin/io/goahead/tuk/author/application/port/SaveAuthorUseCaseImpl.kt` - Save use case implementation.
- `src/main/kotlin/io/goahead/tuk/author/domain/Author.kt` - Author domain model.
- `src/main/kotlin/io/goahead/tuk/author/domain/AuthorId.kt` - Author id value type.
- `src/main/kotlin/io/goahead/tuk/author/domain/repository/AuthorRepository.kt` - Author repository contract.
- `src/main/kotlin/io/goahead/tuk/author/exception/AuthorNotFoundException.kt` - Missing author exception.
- `src/main/kotlin/io/goahead/tuk/author/infrastructure/AuthorEntity.kt` - Author JPA entity.
- `src/main/kotlin/io/goahead/tuk/author/infrastructure/persistence/AuthorRepositoryAdapter.kt` - Author persistence adapter.
- `src/main/kotlin/io/goahead/tuk/author/infrastructure/repository/AuthorJpaRepository.kt` - Author Spring Data repository.

### Confession Files

- `src/main/kotlin/io/goahead/tuk/confession/api/ConfessionController.kt` - Confession endpoints.
- `src/main/kotlin/io/goahead/tuk/confession/api/request/ReactConfessionRequest.kt` - Reaction request DTO.
- `src/main/kotlin/io/goahead/tuk/confession/api/request/WriteConfessionRequest.kt` - Confession write request DTO.
- `src/main/kotlin/io/goahead/tuk/confession/api/response/ConfessionResponse.kt` - Confession response DTO.
- `src/main/kotlin/io/goahead/tuk/confession/application/ConfessionIdGenerator.kt` - Confession id generator contract.
- `src/main/kotlin/io/goahead/tuk/confession/application/port/GetConfessionUseCase.kt` - Get confession contract.
- `src/main/kotlin/io/goahead/tuk/confession/application/port/GetConfessionUseCaseImpl.kt` - Get confession implementation.
- `src/main/kotlin/io/goahead/tuk/confession/application/port/ListConfessionsUseCase.kt` - List confession contract.
- `src/main/kotlin/io/goahead/tuk/confession/application/port/ListConfessionsUseCaseImpl.kt` - List confession implementation.
- `src/main/kotlin/io/goahead/tuk/confession/application/port/ReactConfessionUseCase.kt` - React contract.
- `src/main/kotlin/io/goahead/tuk/confession/application/port/ReactConfessionUseCaseImpl.kt` - React implementation.
- `src/main/kotlin/io/goahead/tuk/confession/application/port/WriteConfessionUseCase.kt` - Write contract.
- `src/main/kotlin/io/goahead/tuk/confession/application/port/WriteConfessionUseCaseImpl.kt` - Write implementation.
- `src/main/kotlin/io/goahead/tuk/confession/application/port/command/ReactConfessionCommand.kt` - Reaction command.
- `src/main/kotlin/io/goahead/tuk/confession/application/port/command/WriteConfessionCommand.kt` - Write command.
- `src/main/kotlin/io/goahead/tuk/confession/application/port/command/WriteConfessionResponse.kt` - Write result mapping.
- `src/main/kotlin/io/goahead/tuk/confession/domain/AuthorId.kt` - Confession author id value type.
- `src/main/kotlin/io/goahead/tuk/confession/domain/Confession.kt` - Confession domain model.
- `src/main/kotlin/io/goahead/tuk/confession/domain/ConfessionContent.kt` - Content value type.
- `src/main/kotlin/io/goahead/tuk/confession/domain/ConfessionId.kt` - Confession id value type.
- `src/main/kotlin/io/goahead/tuk/confession/domain/ReactionCount.kt` - Reaction count value type.
- `src/main/kotlin/io/goahead/tuk/confession/domain/ReactionCountItem.kt` - Reaction count item.
- `src/main/kotlin/io/goahead/tuk/confession/domain/ReactionCounts.kt` - Reaction count collection.
- `src/main/kotlin/io/goahead/tuk/confession/domain/repository/ConfessionRepository.kt` - Confession repository contract.
- `src/main/kotlin/io/goahead/tuk/confession/domain/service/ConfessionReactionCounter.kt` - Reaction counter contract.
- `src/main/kotlin/io/goahead/tuk/confession/enums/ReactionType.kt` - Reaction type enum.
- `src/main/kotlin/io/goahead/tuk/confession/exception/ConfessionNotFoundException.kt` - Missing confession exception.
- `src/main/kotlin/io/goahead/tuk/confession/infrastructure/ConfessionJpaEntity.kt` - Confession JPA entity.
- `src/main/kotlin/io/goahead/tuk/confession/infrastructure/ConfessionReactionJpaEntity.kt` - Reaction JPA entity.
- `src/main/kotlin/io/goahead/tuk/confession/infrastructure/UuidV7ConfessionIdGenerator.kt` - UUID v7 id generator.
- `src/main/kotlin/io/goahead/tuk/confession/infrastructure/persistence/ConfessionReactionCounterAdapter.kt` - Reaction persistence adapter.
- `src/main/kotlin/io/goahead/tuk/confession/infrastructure/persistence/ConfessionRepositoryAdapter.kt` - Confession persistence adapter.
- `src/main/kotlin/io/goahead/tuk/confession/infrastructure/repository/ConfessionJpaRepository.kt` - Confession Spring Data repository.
- `src/main/kotlin/io/goahead/tuk/confession/infrastructure/repository/ConfessionReactionJpaRepository.kt` - Reaction Spring Data repository.

### Resources and Tests

- `src/main/resources/application.yml` - Shared Spring application settings.
- `src/main/resources/application-local.yml` - H2 local profile configuration.
- `src/main/resources/application-railway.yml` - PostgreSQL Railway profile configuration.
- `src/test/kotlin/io/goahead/tuk/GoaheadTukApplicationTests.kt` - Application smoke test.
- `src/test/kotlin/io/goahead/tuk/confession/application/port/GetConfessionUseCaseImplTest.kt` - Get confession use case tests.
- `src/test/kotlin/io/goahead/tuk/confession/application/port/ListConfessionsUseCaseImplTest.kt` - List confession use case tests.
- `src/test/kotlin/io/goahead/tuk/confession/application/port/ReactConfessionUseCaseImplTest.kt` - Reaction use case tests.
- `src/test/kotlin/io/goahead/tuk/confession/application/port/WriteConfessionUseCaseImplTest.kt` - Write confession use case tests.
- `src/test/kotlin/io/goahead/tuk/confession/infrastructure/UuidV7ConfessionIdGeneratorTest.kt` - Identifier generator tests.

## Design Patterns

### Layered Domain Package Layout

- **Location**: Author and confession packages.
- **Purpose**: Keep transport, orchestration, domain, and persistence responsibilities separate.
- **Implementation**: `api`, `application`, `domain`, and `infrastructure` folders under each domain area.

### Ports and Adapters

- **Location**: Use case interfaces, repository contracts, and JPA adapters.
- **Purpose**: Isolate domain and application behavior from persistence details.
- **Implementation**: Domain repository interfaces are implemented by infrastructure adapters.

### Profile-Specific Configuration

- **Location**: `application-local.yml` and `application-railway.yml`.
- **Purpose**: Separate local and runtime datasource behavior.

## Critical Dependencies

- Spring Boot and Spring MVC for the backend runtime.
- Spring Data JPA for relational persistence.
- H2 and PostgreSQL drivers for profile-specific database access.
- Spring Security for CORS and security filter chain configuration.
- Java UUID Generator for confession id generation.
