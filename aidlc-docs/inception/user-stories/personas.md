# Personas

## Persona 1: Anonymous Confession Writer

### Profile

- **Identity**: A person using the confession service from a device identified
  by `X-Device-Id`.
- **Account model**: Does not create or manage a member account for the MVP.
- **Primary goal**: Write a confession immediately without a separate author
  registration step.

### Needs

- The first confession from a device should succeed when request data is valid.
- Later confessions from the same device should continue to use the same
  anonymous author identity.
- The service should not expose persistence or author setup details as friction
  in the write flow.

### Frustrations To Avoid

- Receiving a missing-author response before the first confession can be
  submitted.
- Being forced into a registration flow that is outside MVP scope.
- Seeing inconsistent confession write behavior between first and later writes
  from the same device.

### Related Stories

- US-1 Anonymous Device Writes A Confession
