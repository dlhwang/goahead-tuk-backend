# Integration Test Instructions

## Purpose

Validate that the anonymous confession write behavior still works inside the
existing Spring Boot backend boundary.

## Current Automated Coverage

- The project-wide Gradle test suite includes the Spring application smoke test.
- The changed confession write orchestration is covered by focused use case
  tests.

## Manual API Scenario

### First Write From Device

1. Start the backend with a local profile suitable for the current workspace.
2. Send `POST /tuk/api/confessions` with:
   - Header `X-Device-Id` set to a previously unseen device id.
   - JSON body containing valid confession content.
3. Verify HTTP `201` and a confession response.
4. Verify the request does not fail with missing-author `404`.

### Later Write From Same Device

1. Send another valid confession write with the same `X-Device-Id`.
2. Verify HTTP `201`.
3. Verify the service continues through the same anonymous author identity
   behavior rather than requiring author registration.

## Cleanup

- Stop the local backend process after manual API checks.
- Use the local database profile cleanup behavior already configured by the
  project.
