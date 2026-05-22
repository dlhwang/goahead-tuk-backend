# Business Rules

## BR-1 Device Identity Source

The confession write flow uses the anonymous device identity supplied through
the existing `X-Device-Id` API input.

## BR-2 Missing Author Recovery

A missing author for a device id is a first-write condition. For a valid
confession write, the application resolves it by creating an anonymous author
before the confession is persisted.

## BR-3 Existing Author Reuse

When an anonymous author already exists for the device id, the write flow
reuses that author identity and does not model a second author creation as
normal behavior.

## BR-4 Confession Validation Remains Effective

Author auto-creation does not override validation for confession content or
other write input values already validated by the API or domain model.

## BR-5 Controller Boundary

The controller passes transport-neutral input values to the confession write
application boundary and does not construct the application write command value
object directly.

## BR-6 Repository Direction

Application behavior depends on domain repository ports or application services
that themselves use those ports. Persistence implementation types remain behind
infrastructure adapters.

## BR-7 Infrastructure Containment

JPA entities and Spring Data repositories remain infrastructure implementation
details and do not move into API or application code.

## Rule Outcomes

- A valid first write for a previously unseen device can produce a confession
  write result.
- A valid later write for a known device can produce a confession write result
  without replacing its anonymous author identity.
- Invalid write inputs still fail according to the existing validation path.
