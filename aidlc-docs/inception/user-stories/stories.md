# User Stories

## Story Map

- **US-1**
  - **Persona**: Anonymous Confession Writer
  - **Capability**: Write a confession from a device id

## US-1 Anonymous Device Writes A Confession

As an anonymous confession writer,
I want to submit a confession from my device without registering an author
first,
so that I can use the MVP confession flow immediately.

### Acceptance Criteria

1. Given a valid confession write request with an `X-Device-Id` that does not
   yet map to an author, when the confession is written, then the service
   creates the anonymous author identity and persists the confession.
2. Given a valid confession write request with an `X-Device-Id` that already
   maps to an author, when the confession is written, then the service reuses
   the existing author identity and persists the confession.
3. Given a first confession write for a previously unseen device id, when the
   request is otherwise valid, then the write flow does not return HTTP `404`
   merely because the author does not exist before the request.
4. Given the MVP anonymous identity model, when a confession is written, then
   the author identity used by the confession is derived from the device id
   header rather than from a member registration workflow.
5. Given invalid write input that is already rejected by the confession write
   API or domain validation, when that validation fails, then the new
   auto-create behavior does not convert the invalid request into a successful
   confession write.

### Error And Edge Cases

- Missing-author state is a recoverable first-write condition for this story,
  not a missing resource response.
- Existing domain or API validation for malformed confession content or invalid
  identity values remains in force.
- Repeated writes from the same device should avoid creating duplicate author
  records for the same anonymous id.

### Architecture And Implementation Notes

- The HTTP controller should handle `X-Device-Id` and request DTO mapping
  without importing or constructing application command value objects directly.
- The application write flow should orchestrate anonymous author lookup or
  creation through a domain repository port or an application service that uses
  that port.
- JPA entities and Spring Data repositories should remain in infrastructure
  packages behind adapters.
- The confession write implementation should keep author identity behavior
  explicit enough for tests to cover both existing-author and new-author paths.

### Test Considerations

- Cover a write with an existing anonymous author.
- Cover a write where the author is absent and is created before the confession
  is saved.
- Cover the controller-to-use-case boundary if the application input contract
  changes to remove command value object knowledge from the controller.
- Keep invalid-request behavior covered where the changed flow intersects
  existing validation.

### INVEST Check

- **Independent**: Focused on the confession write capability.
- **Negotiable**: Leaves implementation details open within the approved layer
  boundaries.
- **Valuable**: Removes first-write friction for anonymous users.
- **Estimable**: Bounded to author lookup or creation, write orchestration, and
  focused tests.
- **Small**: One MVP API behavior change.
- **Testable**: Acceptance criteria distinguish first-write and existing-author
  scenarios.
