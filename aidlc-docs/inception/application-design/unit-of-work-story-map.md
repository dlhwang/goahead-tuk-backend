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

## 활성 변경: 고해 반응 MVP 스토리 배정

### US-2 익명 열람자가 지지 반응을 선택하고 해제한다

- **주요 단위**: `UOW-REACTION`
- **지원 단위**: `UOW-SECURITY`
- **기능 커버리지**:
  - 허용 타입 세 가지의 `PUT` 선택과 `DELETE` 해제.
  - 동일 기기/고해/타입의 멱등 상태 및 유일성.
  - 여러 타입의 독립 선택.
  - 없는 고해와 잘못된 입력의 오류 계약.
  - property-based testing으로 검증할 멱등성과 상태 불변식.
- **보안 커버리지**:
  - 공개 변이 API throttling.
  - 원문 기기 ID 비기록과 안전한 오류/입력 검증.
  - 최종 Security Baseline 판정 전에는 완료 승인 불가.

### US-3 익명 열람자가 고해의 지지 반응 집계를 확인한다

- **주요 단위**: `UOW-CONFESSION-REACTION-INTEGRATION`
- **제공 의존 단위**: `UOW-REACTION`
- **지원 단위**: `UOW-SECURITY`
- **기능 커버리지**:
  - Reaction aggregate port로부터 얻은 단건 및 목록 집계 결합.
  - `PRAY`, `COMFORT`, `TOGETHER` 타입별 활성 수와 `0` 표현.
  - 집계가 활성 선택 수와 일치하는 불변식의 통합 검증.
- **보안 커버리지**:
  - 조회 결과의 기기 식별자 및 개별 선택 비노출.
  - 전체 Security Baseline 증빙 판정과 통합 검증 게이트.

### 보안 차단 책임 매핑

- `UOW-SECURITY`는 별도 사용자 스토리를 생성하지 않는다. `US-2`와
  `US-3`를 안전하게 전달하기 위한 차단 지원 단위다.
- 운영/공급망 증빙, 공통 보호 적용 및 나머지 Security Baseline
  판정은 `UOW-SECURITY`의 완료 조건이다.

## Reaction 변경 검증

- `US-2`와 `US-3`는 모두 논리 단위에 배정되었다.
- Reaction 집계 제공과 Confession 응답 결합의 소유권이 분리되었고
  aggregate port 의존성이 명시되었다.
- 전체 Security Baseline과 PBT 책임이 기능 또는 지원 단위에
  배정되어 후속 설계 단계의 입력으로 사용할 수 있다.
