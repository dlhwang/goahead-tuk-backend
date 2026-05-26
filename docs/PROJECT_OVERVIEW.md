# Project Overview

This document is a quick guide to the current repository layout. It is intended
for contributors who need to understand what lives in this workspace before
reading the longer AI-DLC usage and operations guides.

## What Is In This Repository

The repository currently contains three related areas of work:

1. **TUK backend application** - a Kotlin and Spring Boot API under `src/`.
2. **AI-DLC workflow rules** - the agent rule files under `aidlc-rules/`.
3. **Project AI-DLC artifacts** - generated and maintained planning or
   implementation records under `aidlc-docs/`.

The backend application is the runnable code in this workspace. The AI-DLC
rules guide assisted development; `aidlc-docs/` records the work performed for
this project.

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
|-- aidlc-docs/                  # Project-specific AI-DLC artifacts
|-- docs/                        # Project overview and backend GitFlow guide
|-- .github/
|   `-- workflows/
|       `-- backend-gitflow.yml  # Backend branch-flow validation
|-- build.gradle.kts             # Application build definition
|-- settings.gradle.kts          # Gradle project name
`-- AGENTS.md                    # Repository guidance for coding agents
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

Run markdown lint for repository documentation when the tool is available:

```bash
npx markdownlint-cli2 "**/*.md"
```

On Windows PowerShell, use `.\gradlew.bat` instead of `./gradlew` for Gradle
commands.

## Documentation Map

- [`BACKEND_GITFLOW.md`](BACKEND_GITFLOW.md) defines branch and pull request
  policy for backend delivery.
- [`../AGENTS.md`](../AGENTS.md) records guidance for agents working in this
  repository.
- [`../aidlc-docs/`](../aidlc-docs/) contains project-specific AI-DLC planning,
  design, and implementation artifacts.

## External Pull Request Review Apps

CodeRabbit pull request comments are produced by the CodeRabbit GitHub App,
not by a workflow committed in this repository. No `.coderabbit.yaml` or
`.coderabbit.yml` configuration file is maintained here.

If automatic CodeRabbit reviews should be disabled, repository or organization
administrators must verify and change the app installation or its automatic
review setting in GitHub and CodeRabbit:

- Repository **Settings** > **Integrations** > **GitHub Apps**
- Organization **Settings** > **GitHub Apps** > **CodeRabbit**
- CodeRabbit dashboard automatic review settings for this repository

Deleting imported GitHub Actions workflows does not remove an installed GitHub
App or prevent the app from receiving pull request events.

## Notes For Contributors

- Keep application code under `src/`; AI-DLC-generated workflow artifacts
  belong in their documented locations instead of being mixed into source code.
- Do not rename `aidlc-rules/aws-aidlc-rules/` or
  `aidlc-rules/aws-aidlc-rule-details/`; those directory names are part of the
  distributed AI-DLC rules contract.
- Imported AI-DLC automation and auxiliary tooling are intentionally not part
  of this application repository; adopt new automation explicitly when needed.
