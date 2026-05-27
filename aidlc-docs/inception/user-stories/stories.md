# User Stories

## Story Map

- **US-1**
  - **Persona**: Anonymous Confession Writer
  - **Capability**: Write a confession from a device id
- **US-2**
  - **Persona**: 익명 고해 열람자
  - **Capability**: 허용된 지지 반응 선택 및 해제
- **US-3**
  - **Persona**: 익명 고해 열람자
  - **Capability**: 타입별 지지 반응 집계 확인

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

## US-2 익명 열람자가 지지 반응을 선택하고 해제한다

익명 고해 열람자로서,
나는 댓글이나 자유 텍스트 없이 허용된 지지 반응을 선택하거나
해제하고 싶다.
그래서 나를 공개하지 않고도 고해에 긍정적인 마음을 전할 수 있다.

### 수용 기준

1. 유효한 `X-Device-Id`를 가진 열람자가 존재하는 고해에 `PRAY`,
   `COMFORT`, `TOGETHER` 중 하나를 `PUT`으로 선택하면, 서비스는
   해당 선택을 활성화하고 `204 No Content`를 반환한다.
2. 열람자가 이미 활성화한 같은 고해와 같은 타입에 `PUT`을 반복하면,
   서비스는 중복 선택을 만들거나 집계를 증가시키지 않고
   `204 No Content`를 반환한다.
3. 열람자가 동일 고해에 서로 다른 허용 타입을 선택하면, 각 선택은
   독립적으로 유지되어 여러 지지 표현을 함께 남길 수 있다.
4. 열람자가 활성화한 반응 타입을 `DELETE`로 해제하면, 서비스는 해당
   선택을 제거하고 `204 No Content`를 반환한다.
5. 이미 해제된 반응 타입에 대해 `DELETE`를 반복하면, 서비스는 상태를
   바꾸지 않고 `204 No Content`를 반환한다.
6. 지원하지 않는 타입 또는 유효하지 않거나 누락된 기기 식별 입력은
   선택이나 해제를 만들지 않고 안전한 요청 오류로 거부된다.
7. 대상 고해가 존재하지 않으면 선택 또는 해제는 이루어지지 않고
   `404 Not Found`가 반환된다.
8. 정상적인 익명 반응 사용은 사용할 수 있어야 하며, 반복적인 남용
   요청은 서비스의 throttling 정책에 따라 제한된다.

### 오류 및 경계 사례

- 반응 입력은 허용된 enum 타입으로 제한되며 댓글이나 자유 텍스트
  저장 경로를 열지 않는다.
- 같은 기기의 같은 고해/타입 선택은 하나만 존재한다.
- 한 기기의 여러 반응 타입 선택은 충돌로 취급하지 않는다.
- 응답은 기기 식별자를 공개하지 않는다.

### 설계 및 구현 메모

- 선택 상태는 `Confession`의 타입별 카운트 컬럼이 아니라 별도 반응
  선택 데이터로 관리한다.
- `(confessionId, deviceId, reactionType)` 유일성은 저장소 수준에서도
  보장되어야 한다.
- 선택/해제 API는
  `PUT /api/confessions/{confessionId}/reactions/{type}`와 같은 경로의
  `DELETE`를 사용한다.
- `X-Device-Id` 원문은 애플리케이션 로그에 기록하지 않는다.
- 공개 변이 API의 남용 방지 방식은 NFR 설계에서 구체화한다.

### 테스트 고려사항

- 허용 타입별 최초 선택과 해제를 예제 기반 테스트로 검증한다.
- 반복 `PUT`과 반복 `DELETE`가 멱등이라는 속성을 property-based
  testing으로 검증한다.
- 임의의 선택/해제 순서에서 중복 선택이 없다는 상태 불변식을
  property-based testing으로 검증한다.
- 미지원 타입, 누락 또는 잘못된 식별자, 없는 고해 실패를 검증한다.
- 정상 요청과 제한되는 남용 요청의 동작을 검증한다.

### INVEST 확인

- **Independent**: 반응 선택 상태 변경 능력에 집중한다.
- **Negotiable**: 저장 및 throttling 구현 방식은 설계 단계에서
  확정한다.
- **Valuable**: 열람자가 익명으로 지지 의사를 표현할 수 있다.
- **Estimable**: 선택 테이블, 멱등 API, 입력 검증 및 남용 제한으로
  범위가 명확하다.
- **Small**: 조회 집계는 별도 스토리로 분리한다.
- **Testable**: 선택, 해제, 멱등성, 오류 및 남용 제한 결과가 명시되어
  있다.

## US-3 익명 열람자가 고해의 지지 반응 집계를 확인한다

익명 고해 열람자로서,
나는 고해에 모인 허용된 지지 반응의 수를 타입별로 확인하고 싶다.
그래서 글에 보내진 긍정적 지지를 개인 식별 정보 없이 이해할 수 있다.

### 수용 기준

1. 고해 단건 또는 목록을 조회하면 응답은 `PRAY`, `COMFORT`,
   `TOGETHER`의 활성 선택 수를 타입별로 제공한다.
2. 어떤 허용 타입도 선택되지 않았으면 해당 타입은 `0`으로 표현될 수
   있다.
3. 한 기기가 동일 고해에 서로 다른 타입을 선택한 경우 각 타입의
   집계에 각각 반영된다.
4. 반복 선택 또는 반복 해제 요청은 집계에 중복 영향을 주지 않는다.
5. 조회 응답은 개별 `X-Device-Id` 또는 특정 반응자의 선택 내역을
   공개하지 않는다.

### 설계 및 구현 메모

- 집계는 별도 반응 선택 데이터의 활성 행으로부터 파생한다.
- 기존 반응 카운터 증가 저장 모델은 기기별 멱등 선택 계약을 충족하지
  않으므로 교체 대상이다.
- JPA 엔터티와 Spring Data 저장소는 infrastructure 계층에 남는다.

### 테스트 고려사항

- 미선택 타입의 `0` 집계와 복수 타입 선택 집계를 예제 기반 테스트로
  검증한다.
- 임의 선택 집합의 타입별 집계가 활성 선택 수와 동일하다는 불변식을
  property-based testing으로 검증한다.
- 조회 응답이 기기 식별자를 포함하지 않는지 검증한다.

### INVEST 확인

- **Independent**: 선택 상태를 읽기 응답으로 집계하는 능력에 집중한다.
- **Negotiable**: 집계 질의 최적화 방식은 설계에서 정할 수 있다.
- **Valuable**: 열람자가 고해에 도착한 지지를 확인한다.
- **Estimable**: 허용 타입별 집계 응답과 데이터 비노출에 국한된다.
- **Small**: 변이 API는 US-2에서 다룬다.
- **Testable**: 집계 값과 비노출 조건이 검증 가능하다.
