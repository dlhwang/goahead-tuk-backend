# UOW-REACTION Code Generation 계획

<!-- markdownlint-disable MD060 -->

## 단위 맥락

- **담당 스토리**: `US-2` 익명 열람자가 지지 반응을 선택하고 해제한다.
- **책임**: 기기별 Reaction 선택/해제, 선택 저장, 멱등성, 자기 고해
  반응 거부, aggregate port 제공.
- **의존성**: 기존 Confession 조회 경계와 `UOW-SECURITY`의 파생
  device key 및 공개 쓰기 보호.
- **공유 계약**:
  `PUT /api/confessions/{confessionId}/reactions/{type}` 및 동일
  경로 `DELETE`, 성공 시 `204 No Content`.
- **저장 엔터티**: 별도 Reaction 선택 행.
  `Confession` 타입별 count 컬럼은 만들지 않는다.

## 현재 교체 대상

| 현재 파일 또는 구성 | 변경 목적 |
| ------------------- | --------- |
| `src/main/kotlin/io/goahead/tuk/confession/api/ConfessionController.kt` | 기존 `POST` 카운터 API를 `PUT`/`DELETE` 선택 API와 필수 header 계약으로 교체 |
| `src/main/kotlin/io/goahead/tuk/confession/application/port/ReactConfessionUseCase*.kt` | 증가 명령을 선택/해제 use case로 교체 |
| `src/main/kotlin/io/goahead/tuk/confession/infrastructure/ConfessionReactionJpaEntity.kt` | count 행을 기기별 선택 행으로 대체 |
| `src/main/kotlin/io/goahead/tuk/confession/infrastructure/persistence/ConfessionReactionCounterAdapter.kt` | 카운터 adapter 제거 및 선택 repository adapter 추가 |
| `src/main/kotlin/io/goahead/tuk/confession/enums/ReactionType.kt` | MVP 타입 `PRAY`, `COMFORT`, `TOGETHER`로 정리 |

## 생성 및 수정 경로

- 기존 package 관례를 존중하되 새 Reaction 소유 모델은
  `src/main/kotlin/io/goahead/tuk/reaction/` 아래 domain/application/
  infrastructure 경계로 생성한다.
- 기존 Confession controller와 response는 API 통합 지점으로
  in-place 수정한다.
- 테스트는 `src/test/kotlin/io/goahead/tuk/reaction/` 및 필요한
  기존 Confession 테스트 위치에서 추가 또는 수정한다.
- 구현 요약은
  `aidlc-docs/construction/uow-reaction/code/code-summary.md`에
  작성한다.

## 실행 단계

- [ ] **Step 1 - Domain/API Contract**:
  허용 타입 `PRAY`, `COMFORT`, `TOGETHER`, 선택/해제 command,
  선택 repository 및 aggregate reader port를 생성하고 기존
  counter 명칭과 legacy 타입 의존을 제거한다.
- [ ] **Step 2 - Application Logic**:
  선택 및 해제 use case를 구현하여 고해 존재와 자기 고해 거부를
  검사하고, 파생 device key를 사용해 멱등 상태 전이를 수행한다.
- [ ] **Step 3 - Persistence Model**:
  Reaction 선택 JPA entity/repository/adapter를 별도 테이블 의미로
  구현하고 삼중 키 유일 제약, 삭제 및 타입별 집계 조회를 제공한다.
  H2 demo schema 생성에서도 동일 제약이 반영되어야 한다.
- [ ] **Step 4 - HTTP Endpoints**:
  Confession controller의 기존 `POST /reactions` 카운터 endpoint를
  제거하고 필수 `X-Device-Id`를 받는 `PUT`/`DELETE
  /{confessionId}/reactions/{type}` endpoint를 추가한다.
- [ ] **Step 5 - Unit/Integration Tests**:
  허용 타입, 멱등 선택/해제, 복수 타입, 자기 고해 `403`, 없는
  고해, 잘못된 타입과 선택 유일성 테스트를 추가한다.
- [ ] **Step 6 - Property Tests**:
  Kotest Property Testing으로 임의 선택/해제 시퀀스의 set 모델
  일치, 중복 부재와 집계 count 불변식을 검증한다.
- [ ] **Step 7 - Code Summary**:
  수정/신규/제거 파일, API 계약, demo 저장 제약과 검증 항목을 단위
  코드 요약 문서에 기록한다.

## 완료 기준

- `US-2` 계약과 Reaction 전용 저장 의미가 코드 및 테스트에 반영된다.
- 기존 카운터 증가 방식과 legacy 반응 타입은 사용자 노출 경계에서
  남지 않는다.
- `UOW-SECURITY`와의 통합 보호를 포함한 전체 계획이 함께 완료되어야
  이 단위가 전달 가능하다.
