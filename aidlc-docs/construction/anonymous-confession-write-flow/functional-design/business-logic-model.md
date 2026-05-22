# Business Logic Model

## Unit

- **Name**: Anonymous Confession Write Flow
- **Primary story**: US-1 Anonymous Device Writes A Confession

## Write Flow

1. The API receives confession content and a device-backed anonymous writer
   identity value from `X-Device-Id`.
2. The application write boundary receives transport-neutral values for the
   writer identity and confession content.
3. The write flow resolves the anonymous author:
   - If the author already exists for the device id, reuse that author.
   - If the author is absent, create the anonymous author as part of the valid
     write flow before the confession is persisted.
4. The write flow creates a confession with:
   - A generated confession id.
   - The resolved anonymous author id.
   - Validated confession content.
   - Creation time.
5. The confession is persisted and a write result is returned to the API.

## Alternative Paths

### Existing Anonymous Author

- The author lookup returns an author for the device id.
- No new author creation is needed.
- The confession uses the existing author id.

### First Write From Anonymous Device

- The author lookup returns no author for the device id.
- The write flow creates the anonymous author identity.
- The confession write continues with the created author id.
- Missing author state does not become a missing resource response for this
  valid first write.

### Invalid Write Input

- Existing validation failures still fail the write.
- Auto-creation of a missing author does not promise a successful confession for
  invalid content or invalid identity input.

## Boundary Model

- The HTTP layer maps header and request data.
- The application layer owns write orchestration and anonymous author
  resolution.
- Domain models own author and confession identity/value semantics.
- Infrastructure persists through adapters behind domain repository ports.

## Integration Scope

- No external system or frontend component interaction is added by this unit.
- The design stays inside the existing backend API and persistence boundaries.
