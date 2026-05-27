# UOW-CONFESSION-REACTION-INTEGRATION Code Generation 계획

<!-- markdownlint-disable MD060 -->

## 단위 맥락

- **담당 스토리**: `US-3` 익명 열람자가 고해의 지지 반응 집계를
  확인한다.
- **책임**: Confession 단건 및 목록 응답에 Reaction aggregate를
  결합하고 허용된 세 타입을 항상 반환한다.
- **의존성**: `UOW-REACTION`의 aggregate reader port와
  `UOW-SECURITY`의 안전 오류 및 비노출 경계.

## 현재 교체 대상

| 현재 파일 또는 구성 | 변경 목적 |
| ------------------- | --------- |
| `src/main/kotlin/io/goahead/tuk/confession/infrastructure/persistence/ConfessionRepositoryAdapter.kt` | Reaction 행 직접 결합 책임을 분리하고 Confession 저장 책임만 유지 |
| `src/main/kotlin/io/goahead/tuk/confession/application/port/GetConfessionUseCaseImpl.kt` | 단건 aggregate reader 결합 |
| `src/main/kotlin/io/goahead/tuk/confession/application/port/ListConfessionsUseCaseImpl.kt` | 목록 batch aggregate reader 결합 |
| `src/main/kotlin/io/goahead/tuk/confession/api/response/ConfessionResponse.kt` | 세 MVP 타입과 zero count 응답을 유지하고 공개 `authorId` 노출 제거 |

## 생성 및 수정 경로

- 조회 서비스와 응답은 기존
  `src/main/kotlin/io/goahead/tuk/confession/` 경계에서 in-place
  수정한다.
- aggregate port 구현은 `UOW-REACTION`의 새 package가 소유한다.
- 관련 테스트는 기존 `GetConfessionUseCaseImplTest.kt`,
  `ListConfessionsUseCaseImplTest.kt`와 persistence 통합 테스트로
  보강한다.
- 구현 요약은
  `aidlc-docs/construction/uow-confession-reaction-integration/code/code-summary.md`에
  작성한다.

## 실행 단계

- [ ] **Step 1 - Query Boundary Refactor**:
  Confession repository adapter에서 기존 counter 행 집계 결합을
  제거하고 읽기 use case가 Reaction aggregate reader를 소비하도록
  경계를 이동한다.
- [ ] **Step 2 - Aggregate Assembly**:
  단건 조회와 목록 조회가 `PRAY`, `COMFORT`, `TOGETHER` count를
  항상 응답하며 미선택 타입을 `0`으로 조립하도록 구현한다. 공개
  응답에서 device-backed 작성자 식별자는 제거한다.
- [ ] **Step 3 - Batched Read**:
  목록 조회는 고해 ID 집합의 aggregate를 단일 batch/group 조회로
  가져오고 고해별 반복 Reaction 질의를 만들지 않는다.
- [ ] **Step 4 - Fail-Closed Handling**:
  aggregate 조회 실패가 정상 zero 집계로 변환되지 않고 공통 안전
  오류 경계로 전달되게 구현한다.
- [ ] **Step 5 - Tests and Properties**:
  zero-fill, 복수 타입 count, 목록 분리, aggregate 실패, batch 조회
  및 익명 작성자 비노출, 임의 선택 set에 대한 count/완전성 property
  테스트를 추가한다.
- [ ] **Step 6 - Code Summary**:
  읽기 통합 경계, 성능 설계 목표와 demo 제약, 검증 결과 위치를 단위
  코드 요약 문서에 기록한다.

## 완료 기준

- `US-3`가 새 선택 저장소 집계를 사용하고 작성자 및 반응자 device
  정보는 공개 응답에 포함되지 않는다.
- 목록 집계가 N+1 방식으로 회귀하지 않는다.
- Reaction과 Security 단위의 구현 및 검증이 함께 완료되어야 한다.
