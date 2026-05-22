# Domain Entities

## Author

### Role In This Unit

`Author` represents the anonymous device-backed writer identity used by the MVP
confession write flow.

### Relevant Attributes

- Author id derived from the device identity value.
- Nickname or default anonymous display state already held by the author
  domain model.
- Creation timestamp already held by the author domain model.

### Unit Rules

- An author may already exist before the confession write.
- A missing author may be created as part of a valid first write.
- The author is not redesigned into a registered member account in this unit.

## Confession

### Role In This Unit

`Confession` is the domain record written for the anonymous author.

### Relevant Attributes

- Generated confession id.
- Resolved anonymous author id.
- Validated confession content.
- Creation timestamp.

### Unit Rules

- The confession is written only after an anonymous author id is resolved.
- The confession preserves the relationship to the resolved author identity.
- Existing content validation remains part of confession creation.

## Value And Boundary Concepts

### Anonymous Device Id

- Originates at the API boundary through `X-Device-Id`.
- Supplies the author identity lookup or creation value for the MVP flow.
- Should reach the application write operation as transport-neutral input data.

### Repository Ports

- Author and confession persistence are reached through domain repository
  contracts.
- Repository ports are not replaced by direct Spring Data dependencies in
  application design.

### Persistence Adapters

- Existing infrastructure adapters map domain models to persistence details.
- JPA entities and Spring Data repositories remain outside this functional
  domain model.

## Relationship Summary

1. One resolved anonymous `Author` identity is used by each written
   `Confession`.
2. The write flow can create the `Author` before creating the `Confession` when
   the author is absent.
3. The API input value identifies the anonymous writer for this unit without
   introducing member authentication.
