# Unit Of Work Story Map

## Story Assignment

- **Story**: US-1
- **Unit**: UOW-1 Anonymous Confession Write Flow
- **Coverage**: Full story and acceptance criteria

## Coverage Notes

### US-1 Anonymous Device Writes A Confession

UOW-1 covers:

- First valid write from an unseen device id.
- Valid write from a device id with an existing author.
- Avoiding a missing-author `404` for the first valid write.
- Preserving device-backed anonymous identity for MVP scope.
- Architecture notes for controller, application, domain port, and
  infrastructure boundaries.
- Focused tests for write-flow behavior and boundary changes.

## Validation

- All approved user stories for this change are assigned.
- No separate unit is needed for author identity because the approved scope
  treats it as an internal collaborator in the write flow.
