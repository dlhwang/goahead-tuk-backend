# Unit Of Work

## Unit 1: Anonymous Confession Write Flow

### Purpose

Deliver the MVP write behavior that lets an anonymous device submit a
confession without separately registering an author first.

### Scope

- Use `X-Device-Id` as the anonymous writer identity input for confession
  writes.
- Reuse an existing author record for a known device id.
- Create an anonymous author record for a previously unseen device id during a
  valid confession write.
- Keep the confession controller free of direct write command value object
  construction.
- Preserve domain repository ports and infrastructure persistence boundaries.

### Existing Components In Scope

- `confession/api`
  - HTTP header and write request DTO handling.
  - Confession response mapping.
- `confession/application`
  - Write use case boundary and write orchestration.
  - Confession id generation dependency.
- `author/application`
  - Anonymous author lookup or creation behavior.
- `author/domain` and `confession/domain`
  - Identifier and repository port contracts already used by the write flow.
- `author/infrastructure` and `confession/infrastructure`
  - Existing adapters remain persistence implementations behind ports.

### Out Of Scope

- Member account registration or authentication redesign.
- New deployment units, scaling policies, or database topology changes.
- Broad read or reaction API redesign.
- Moving JPA entities or Spring Data repositories outside infrastructure.

### Delivery Shape

- **Service model**: Existing Spring Boot backend.
- **Unit type**: Single brownfield backend unit.
- **Ownership**: One backend delivery owner or team can implement the change
  sequentially.
- **Primary story**: US-1 Anonymous Device Writes A Confession.

### Success Criteria

- First writes from unseen device ids do not fail only because an author record
  was absent before the request.
- Later writes from known device ids reuse the anonymous author identity.
- The controller and application boundary satisfy the approved architecture
  constraint.
- Focused tests cover the changed write flow.
