# Requirements

## Intent Analysis

- **User request**: Complete the MVP API work for an anonymous device-based
  confession service by improving confession author handling and preserving the
  current clean architecture boundaries.
- **Request type**: Backend API enhancement.
- **Scope estimate**: Multiple backend components across confession and author
  API/application/domain/infrastructure packages plus focused tests.
- **Complexity estimate**: Moderate. The desired user behavior is explicit, but
  the implementation must keep device identity, author creation, use case
  commands, and persistence dependencies on the correct side of layer
  boundaries.
- **Requirements depth**: Minimal. The user supplied the MVP behavior and key
  architectural constraints directly.

## Context

The backend is a Kotlin and Spring Boot application with domain-oriented
packages for `author` and `confession`. HTTP controllers call application
services or use cases. Domain repositories are ports, while JPA entities and
Spring Data repositories live under infrastructure.

The confession write API already accepts the `X-Device-Id` request header as the
anonymous author identifier. The MVP should treat `Author` as an anonymous
device identity rather than a separately registered member prerequisite.

## Functional Requirements

### FR-1 Anonymous Device Author Identity

The confession write API shall use `X-Device-Id` as the anonymous author
identity for the MVP.

The implementation shall not require a separate member registration workflow
before a device can write a confession.

### FR-2 Author Auto-Creation On Confession Write

When a confession write request references an anonymous device id that does not
already have an `Author`, the write flow shall create the `Author` automatically
and continue writing the confession.

The confession write flow shall not return HTTP `404` solely because the
anonymous device author does not already exist.

### FR-3 Existing Author Reuse

When an `Author` already exists for the `X-Device-Id`, the write flow shall use
that existing author identity and persist the confession without creating a
duplicate author.

### FR-4 Controller Boundary

The confession controller shall map HTTP header and request body data without
depending directly on application command value objects.

The application boundary shall own command creation or an equivalent use case
input adaptation that keeps transport DTOs out of domain logic.

### FR-5 Persistence Boundary

JPA entities and Spring Data `JpaRepository` types shall remain inside
infrastructure packages.

Application logic shall depend on domain repository ports rather than
infrastructure repository implementations.

## Non-Functional Requirements

### NFR-1 Architectural Consistency

The change shall preserve the existing clean architecture direction:

- API code handles HTTP concerns and response mapping.
- Application code orchestrates author lookup or creation and confession write
  use cases.
- Domain code holds repository ports and business value types.
- Infrastructure code holds persistence adapters, JPA entities, and Spring Data
  repositories.

### NFR-2 Testability

Focused tests shall cover both confession write paths:

- Existing anonymous author.
- Missing anonymous author that is created automatically.

Tests should also protect the controller-to-use-case boundary when the affected
API contract changes.

### NFR-3 MVP Scope

The change shall stay focused on APIs needed for the anonymous confession MVP.
It shall not introduce a member account model, authentication redesign, or
unrelated author lifecycle features.

## Out Of Scope

- Replacing `X-Device-Id` with a login or token-based identity scheme.
- Moving JPA entities or Spring Data repositories out of infrastructure.
- Redesigning read or reaction APIs unless a targeted compatibility fix is
  required by the confession write change.
- Enabling the Security Baseline or Property-Based Testing AI-DLC extensions
  that are currently recorded as disabled for this workflow state.

## Quality and Review Considerations

- The reverse-engineering artifacts already describe author and confession as
  separate backend domain areas with repository adapters.
- This API enhancement affects a user-facing write path, so Workflow Planning
  should decide whether the existing requirements are enough or a small story
  artifact would improve reviewability before code generation.
- Code generation planning should inspect the current `WriteConfessionUseCase`
  and `AuthorRepository` boundaries before changing public application inputs.

## Requirement Summary

This AI-DLC run shall adjust the confession write API so anonymous devices can
write immediately: missing device-backed authors are created automatically,
existing authors are reused, and the change keeps controllers, application
ports, domain repositories, and infrastructure persistence separated.
