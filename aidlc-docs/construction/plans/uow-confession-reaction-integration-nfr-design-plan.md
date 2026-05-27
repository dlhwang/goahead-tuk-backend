# UOW-CONFESSION-REACTION-INTEGRATION NFR Design 계획

<!-- markdownlint-disable MD053 -->

## 목표

고해 읽기 응답에 반응 집계를 결합하는 경로의 batch 조회와 안전한
실패 처리 패턴을 결정한다.

## 승인된 설계 입력

- 성공한 단일 및 목록 조회 응답은 `PRAY`, `COMFORT`, `TOGETHER`를
  항상 포함하고 선택이 없는 타입은 `0`으로 제공한다.
- 집계 조회 실패는 실제 `0`으로 꾸미지 않으며 전체 읽기 요청을
  안전하게 실패 처리한다.
- 목록 조회는 N+1 집계 질의를 허용하지 않고 목록 응답의 정상 운영
  목표를 `p95 <= 500 ms`로 둔다.

## 계획 체크리스트

- [x] Functional Design 및 승인된 NFR Requirements를 검토한다.
- [x] 아래 NFR Design 질문의 답변을 분석한다.
- [x] `aidlc-docs/construction/uow-confession-reaction-integration/nfr-design/nfr-design-patterns.md`를
  생성한다.
- [x] `aidlc-docs/construction/uow-confession-reaction-integration/nfr-design/logical-components.md`
  를
  생성한다.
- [x] 집계 완전성, fail-closed 동작 및 성능 목표를 점검한다.
- [x] 생성 산출물에 대한 사용자의 명시적 승인을 요청한다.

## Question 1

목록 응답의 반응 집계를 만드는 batch 조회 패턴은 무엇으로 정할까?

A) 고해 식별자 목록을 받아 타입별 활성 선택 수를 한 번의 그룹 집계
질의로 읽고, 누락된 타입의 `0`은 애플리케이션 응답 조립 시 채운다.

B) 고해 식별자 목록에 해당하는 활성 선택 행을 한 번에 모두 읽은 뒤
타입별 count 계산까지 애플리케이션 메모리에서 수행한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2

집계 저장소의 일시적 읽기 실패 시 응답 경로의 복원 패턴은 어떻게
정할까?

A) 요청 경로에서는 재시도하지 않고 즉시 안전한 오류로 실패하며,
구조화 로그와 지표를 남겨 경보 및 운영 대응으로 연결한다.

B) 읽기 작업에 한해 한 번의 제한된 재시도를 수행하고, 재시도도
실패하면 안전한 오류로 전체 요청을 실패 처리한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A
