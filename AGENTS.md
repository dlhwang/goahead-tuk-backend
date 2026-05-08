# AGENTS.md

## Project Overview

This project is a Kotlin + Spring Boot 3.x backend service.

The architecture follows Clean Architecture principles:

- api: HTTP controllers and request/response DTOs
- application: use cases, application services, commands, ports
- domain: pure Kotlin domain models, value objects, domain rules
- infrastructure: JPA entities, Spring Data repositories, external libraries, adapters

## Core Rules

- Do not modify code before explaining the plan.
- Keep changes small and focused.
- Prefer simple code over unnecessary abstraction.
- Follow YAGNI.
- Keep the domain layer free from Spring, JPA, database, and external library dependencies.
- Application layer may depend on domain and ports.
- Infrastructure layer implements application/domain ports.
- API layer must not expose domain internals directly unless explicitly intended.

## Kotlin Style

- Prefer immutable values.
- Prefer data class for DTO-like structures.
- Prefer value objects for domain identifiers.
- Avoid nullable propagation. Convert nulls at boundaries.
- Avoid using Lombok.
- Do not use Java-style boilerplate unless needed.
- Use expressive method names.

## Domain Modeling Rules

- Treat identifiers like ConfessionId and AuthorId as Value Objects.
- ID generation is done through an application port.
- UUIDv7 implementation belongs to infrastructure.
- Domain models must not know UUID libraries, JPA, or Spring annotations.
- JPA Entity and domain model must be separated.

## Testing Rules

- Prefer application service tests first.
- Add domain tests only when domain behavior is meaningful.
- Do not add tests just to satisfy coverage.
- When changing code, run relevant Gradle tests if possible.

## Before Editing

Before modifying files, always provide:

1. Problem summary
2. Proposed changes
3. Files to change
4. Risk points
5. Test plan

Wait for explicit approval before applying broad structural changes.

## Commit Style

Use concise conventional commit style.

Examples:

- feat(confession): add confession creation use case
- refactor(confession): separate domain model from JPA entity
- fix(confession): validate empty confession content
- test(confession): add creation service test