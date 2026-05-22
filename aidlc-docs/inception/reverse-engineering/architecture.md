# System Architecture

## System Overview

This repository combines a Kotlin Spring Boot backend application with a
separate Markdown-centered AI-DLC methodology distribution and helper tooling.
The backend is organized by domain area and uses application ports plus
persistence adapters. The AI-DLC content is documentation and rule material
rather than runtime code for the backend.

## Architecture View

Text architecture view:

1. HTTP controllers expose author and confession endpoints.
2. Application services and use cases coordinate domain objects and repository
   contracts.
3. Domain models define author, confession, identifier, content, and reaction
   semantics.
4. Infrastructure adapters implement repository contracts with Spring Data JPA.
5. H2 or PostgreSQL supplies persistence depending on the active profile.
6. Common API and security configuration supply shared HTTP behavior.

## Component Descriptions

- **`author/api`**: Application HTTP endpoints and DTO mapping for authors;
  depends on `author/application`.
- **`author/application`**: Author retrieval, creation, and save orchestration;
  depends on `author/domain`.
- **`author/domain`**: Author and repository contracts backed by Kotlin domain
  types.
- **`author/infrastructure`**: JPA entity and repository adapter for authors;
  depends on Spring Data JPA.
- **`confession/api`**: Confession HTTP endpoints and request or response
  mapping; depends on `confession/application`.
- **`confession/application`**: Write, get, list, and react use cases; depends
  on confession domain contracts and the author application service.
- **`confession/domain`**: Confession entities, value objects, repository
  contract, and reaction counter contract.
- **`confession/infrastructure`**: Id generation, JPA entities, reaction counter,
  and repository adapters; depends on Spring Data JPA and UUID generation.
- **`common`**: Shared error handling, identifier abstraction, security, and
  CORS behavior.
- **`aidlc-rules`**: Distributable AI-DLC workflow files for Markdown consumers.
- **`scripts`**: Evaluator, design review, and traceability helpers using Python
  toolchains per script.

## Key Data Flows

### Write Confession

1. `ConfessionController.write` receives `X-Device-Id` and request content.
2. `WriteConfessionUseCaseImpl` gets or creates the author.
3. `ConfessionIdGenerator` creates the confession identifier.
4. `Confession.write` creates the domain object.
5. `ConfessionRepositoryAdapter` maps and persists the JPA entity.
6. The controller returns a confession response with HTTP `201`.

### React to Confession

1. `ConfessionController.react` receives confession id and reaction type.
2. `ReactConfessionUseCaseImpl` validates that the confession exists.
3. `ConfessionReactionCounterAdapter` increments the persisted reaction count.
4. The controller returns HTTP `204`.

## Integration Points

- **External APIs**: No outbound third-party API integration was found in the
  inspected backend code.
- **Databases**: H2 local profile and PostgreSQL Railway profile.
- **HTTP clients**: Browser or frontend clients allowed from
  `http://localhost:5173` for local CORS.
- **Documentation consumers**: Coding agents and repository contributors load
  AI-DLC Markdown rule content.

## Infrastructure Components

- **Deployment model**: Spring Boot process or container image. The current
  working tree includes `Dockerfile` and `railway.json` deployment hints.
- **Networking**: HTTP server port uses `${PORT:8080}` and context path `/tuk`.
- **Database configuration**: Local H2 creates and drops schema; Railway
  profile expects PostgreSQL environment variables and validates schema.
- **CI and release automation**: GitHub workflows maintain AI-DLC rule release,
  security scanning, PR validation, and CodeBuild integration.
