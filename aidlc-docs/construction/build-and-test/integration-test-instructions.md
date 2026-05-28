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

## `selectedByMe` 단건 조회 시나리오

### 요청 기기의 선택 상태 조회

1. 다른 기기가 작성한 고해에 `reader-1`이 `PRAY`와 `COMFORT`를
   `PUT`으로 선택한다.
2. 다음 단건 조회를 실행한다.

   ```http
   GET /api/confessions/{confessionId}
   X-Device-Id: reader-1
   ```

3. 응답의 `PRAY` 및 `COMFORT`가 `selectedByMe: true`이고
   `TOGETHER`가 `selectedByMe: false`인지 확인한다.
4. 같은 고해를 다른 유효 기기 ID로 조회하면 해당 기기의 선택
   상태만 반영되는지 확인한다.

### 계약 격리 확인

- `GET /api/confessions` 목록 응답에는 `selectedByMe`가 추가되지
  않는다.
- 고해 작성 응답에도 `selectedByMe`가 추가되지 않는다.
- 로그 및 응답에서 raw `X-Device-Id` 또는 해시 기기 키가
  노출되지 않는다.
