# Project Overview

This document is a quick guide to the current repository layout. It is intended
for contributors who need to understand what lives in this workspace before
reading the longer AI-DLC usage and operations guides.

## What Is In This Repository

The repository currently contains two related areas of work:

1. **TUK backend application** - a Kotlin and Spring Boot API under `src/`.
2. **AI-DLC workflow material** - rule files, documentation, and supporting
   scripts under `aidlc-rules/`, `docs/`, and `scripts/`.

The backend application is the runnable code in this workspace. The AI-DLC
material provides workflow rules and tooling that support AI-driven development
and evaluation.

## TUK Backend

The TUK backend exposes API flows for authors and confessions:

- `author` creates and retrieves authors by identifier.
- `confession` writes confessions, retrieves one confession, lists confessions,
  and adds reaction counts.
- `common` provides shared API error handling, identifier contracts, and Spring
  security configuration.

The application follows a domain-oriented package layout:

- `api/` contains HTTP controllers and request or response DTOs.
- `application/` contains use cases, commands, and orchestration services.
- `domain/` contains domain models, repository contracts, and domain services.
- `infrastructure/` contains persistence adapters, JPA entities, repositories,
  and implementation details such as UUID generation.

## Technology Stack

- Language: Kotlin on Java 21
- Framework: Spring Boot
- Build: Gradle Kotlin DSL
- Web API: Spring MVC with OpenAPI UI support
- Persistence: Spring Data JPA
- Local data: H2 in-memory database configured in PostgreSQL compatibility mode
- Runtime data: PostgreSQL driver available for external database profiles
- Tests: JUnit Platform through Spring Boot and Kotlin test dependencies

The default application configuration uses:

- Server port `8080`
- Context path `/tuk`
- H2 console path `/h2-console`
- Virtual threads enabled
- Local CORS allowance for `http://localhost:5173`

## Repository Structure

```text
goahead-tuk/
|-- src/                         # Kotlin Spring Boot application and tests
|   |-- main/kotlin/io/goahead/tuk/
|   |   |-- author/              # Author API, use cases, domain, persistence
|   |   |-- confession/          # Confession API, domain, reactions, persistence
|   |   `-- common/              # Shared API and configuration code
|   |-- main/resources/          # Spring application configuration
|   `-- test/kotlin/             # Application and use case tests
|-- aidlc-rules/                 # Distributable AI-DLC rule files
|   |-- aws-aidlc-rules/         # Core workflow entry point
|   `-- aws-aidlc-rule-details/  # Phase-specific and extension rules
|-- docs/                        # Repository and AI-DLC guides
|-- scripts/
|   |-- aidlc-evaluator/         # Workflow evaluator framework
|   |-- aidlc-designreview/      # Design review tooling
|   `-- aidlc-traceability/      # Traceability tooling and guidance
|-- assets/                      # Images and other documentation assets
|-- .github/                     # GitHub workflows and repository automation
|-- build.gradle.kts             # Application build definition
|-- settings.gradle.kts          # Gradle project name
`-- README.md                    # AI-DLC setup and usage guide
```

## Main Application Entry Points

- `src/main/kotlin/io/goahead/tuk/GoaheadTukApplication.kt` starts the Spring
  Boot application.
- `src/main/kotlin/io/goahead/tuk/author/api/AuthorController.kt` defines HTTP
  endpoints for author operations.
- `src/main/kotlin/io/goahead/tuk/confession/api/ConfessionController.kt`
  defines HTTP endpoints for confession operations.
- `src/main/resources/application.yaml` defines the default local runtime
  configuration.

## Common Local Commands

Run the backend from the repository root:

```bash
./gradlew bootRun
```

Run backend tests:

```bash
./gradlew test
```

Run markdown lint for repository documentation:

```bash
npx markdownlint-cli2 "**/*.md"
```

Run evaluator tests from the evaluator workspace:

```bash
cd scripts/aidlc-evaluator
uv run pytest
```

On Windows PowerShell, use `.\gradlew.bat` instead of `./gradlew` for Gradle
commands.

## Documentation Map

- [`README.md`](../README.md) covers AI-DLC setup, supported agents, usage, and
  extension overview.
- [`WORKING-WITH-AIDLC.md`](WORKING-WITH-AIDLC.md) covers interaction patterns
  for running the AI-DLC workflow.
- [`GENERATED_DOCS_REFERENCE.md`](GENERATED_DOCS_REFERENCE.md) describes the
  generated `aidlc-docs/` artifact structure.
- [`DEVELOPERS_GUIDE.md`](DEVELOPERS_GUIDE.md) covers local build and security
  scanner guidance for AI-DLC tooling.
- [`ADMINISTRATIVE_GUIDE.md`](ADMINISTRATIVE_GUIDE.md) covers CI/CD, releases,
  permissions, and administration.

## Notes For Contributors

- Keep application code under `src/`; AI-DLC-generated workflow artifacts
  belong in their documented locations instead of being mixed into source code.
- Do not rename `aidlc-rules/aws-aidlc-rules/` or
  `aidlc-rules/aws-aidlc-rule-details/`; those directory names are part of the
  distributed AI-DLC rules contract.
- Read the existing guide for the area you are changing before updating rules,
  scripts, CI/CD workflows, or release behavior.
