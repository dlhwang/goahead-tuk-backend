# Anonymous Confession Write Flow Code Generation Plan

## Plan Role

This plan is the source of truth for code generation for UOW-1.

## Unit Context

- **Unit**: Anonymous Confession Write Flow
- **Story**: US-1 Anonymous Device Writes A Confession
- **Workspace root**: `D:\workspace\goahead-tuk`
- **Project type**: Brownfield Kotlin Spring Boot backend
- **Primary dependency**: Existing author application behavior resolves or
  creates the anonymous author through domain repository ports.
- **Owned persistence shape**: Existing author and confession persistence
  adapters remain in infrastructure. No migration or new entity is planned.

## Expected Interfaces And Contracts

- `ConfessionController.write` receives `X-Device-Id` and
  `WriteConfessionRequest`.
- `WriteConfessionUseCase` exposes a write operation that can be called from the
  controller with transport-neutral values rather than a command value object
  constructed in the controller.
- `WriteConfessionUseCaseImpl` keeps anonymous author lookup or creation before
  confession persistence.
- Existing `WriteConfessionResponse` and `ConfessionResponse` mapping remain the
  write result contract.

## Code Generation Steps

### Step 1: Prepare The Write Boundary

- [x] Modify
  `src/main/kotlin/io/goahead/tuk/confession/application/port/WriteConfessionUseCase.kt`
  so the write boundary accepts transport-neutral write values.
- [x] Modify
  `src/main/kotlin/io/goahead/tuk/confession/application/port/WriteConfessionUseCaseImpl.kt`
  to use the updated boundary while keeping author get-or-create behavior.
- [x] Remove
  `src/main/kotlin/io/goahead/tuk/confession/application/port/command/WriteConfessionCommand.kt`
  if the updated write flow no longer needs it.

### Step 2: Update The API Layer

- [x] Modify
  `src/main/kotlin/io/goahead/tuk/confession/api/ConfessionController.kt`
  so it passes device id and content through the write use case boundary without
  constructing the write command value object.
- [x] Confirm the API response mapping and `201` status behavior remain
  unchanged.

### Step 3: Update Focused Business Logic Tests

- [x] Modify
  `src/test/kotlin/io/goahead/tuk/confession/application/port/WriteConfessionUseCaseImplTest.kt`
  for the updated write boundary.
- [x] Preserve coverage for generated confession id and existing anonymous
  author reuse.
- [x] Preserve coverage for missing anonymous author auto-creation.

### Step 4: Add Boundary Coverage If Needed

- [x] Inspect available confession API test patterns.
- [x] Add or update focused test coverage only if needed to protect the changed
  controller-to-use-case call shape.

### Step 5: Verify Persistence Boundaries

- [x] Review changed imports and paths to confirm application code still depends
  on domain repository ports rather than infrastructure Spring Data types.
- [x] Confirm JPA entities and Spring Data repositories remain in infrastructure
  packages.
- [x] Confirm no duplicate brownfield source files were created.

### Step 6: Record Code Summary

- [x] Create
  `aidlc-docs/construction/anonymous-confession-write-flow/code/code-generation-summary.md`
  with modified files, behavior notes, and tests planned for Build and Test.

## Story Traceability

- **US-1 AC-1**: Keep missing-author auto-creation path in write use case and
  tests.
- **US-1 AC-2**: Keep existing-author reuse path in write use case and tests.
- **US-1 AC-3**: Avoid missing-author `404` by using get-or-create behavior.
- **US-1 architecture notes**: Remove controller construction of the write
  command value object and preserve port/adaptor direction.

## Out Of Scope For Code Generation

- New database migrations or schema changes.
- Deployment artifact changes.
- Frontend components.
- Broad author API redesign.
