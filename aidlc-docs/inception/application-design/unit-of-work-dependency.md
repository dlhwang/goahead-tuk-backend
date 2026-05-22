# Unit Of Work Dependency

## Units

- **UOW-1**: Anonymous Confession Write Flow

## Dependency View

UOW-1 is a single delivery unit. Dependencies below are internal collaborators
or implementation dependencies inside the existing backend.

- **`confession/api`**: Accept device id and write request data.
- **`confession/application`**: Orchestrate confession write behavior.
- **`author/application`**: Lookup or create anonymous author.
- **Domain repository ports**: Keep application persistence direction.
- **Infrastructure persistence adapters**: Persist existing author and
  confession data.

## Dependency Rules

- The controller depends on the confession application boundary, not on a write
  command value object constructor.
- Confession application behavior may use author application behavior to obtain
  the anonymous author identity.
- Application code depends on domain repository ports.
- JPA entities and Spring Data repositories remain implementation details in
  infrastructure.

## Coordination Notes

- No cross-unit release sequencing is required because UOW-1 is delivered as
  one backend unit.
- A verification checkpoint is still required after the write use case boundary
  changes so controller and application tests agree on the new call shape.
