# UOW-CONFESSION-REACTION-INTEGRATION Functional Design 계획

<!-- markdownlint-disable MD053 -->

## 목표

기존 고해 단건 및 목록 조회가 Reaction aggregate port를 소비해
허용 타입별 집계를 안전하게 반환하는 상세 기능 규칙을 설계한다.

## 입력 범위

- `US-3` 익명 열람자가 고해의 지지 반응 집계를 확인한다.
- `UOW-CONFESSION-REACTION-INTEGRATION` 고해 조회 집계 연동.
- `ReactionAggregateReader`와 기존 Confession 읽기 use case.
- 응답 비노출과 집계 무결성 Security Baseline 책임.

## 계획 체크리스트

- [x] 승인된 단위, 사용자 스토리와 Application Design을 검토한다.
- [x] 아래 기능 질문의 답변을 받고 모순 또는 불명확성을 분석한다.
- [x] `aidlc-docs/construction/uow-confession-reaction-integration/functional-design/business-logic-model.md`
  에
  단건 및 목록 조회 결합 흐름을 정의한다.
- [x] `aidlc-docs/construction/uow-confession-reaction-integration/functional-design/business-rules.md`에
  집계 표현, 비노출 및 실패 규칙을 정의한다.
- [x] `aidlc-docs/construction/uow-confession-reaction-integration/functional-design/domain-entities.md`
  에
  읽기 모델과 집계 값 개념을 정의한다.
- [x] PBT-01에 따라 집계 불변식의 테스트 가능 속성과 전달 요구를
  문서화한다.
- [x] 생성 산출물에 대한 사용자의 명시적 승인을 요청하고 승인받는다.

## Question 1

고해 조회 응답에서 선택 수가 `0`인 허용 반응 타입은 어떻게
표현할까?

A) 모든 응답이 `PRAY`, `COMFORT`, `TOGETHER` 세 타입을 항상 포함하고
선택이 없는 타입은 `count: 0`으로 반환한다.

B) 활성 선택이 있는 타입만 반환한다. 클라이언트는 누락된 허용 타입을
`0`으로 해석한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: B

## 답변 분석 결과

- `Q1=B`: 활성 선택이 있는 타입만 조회 응답에 포함하는 선택이다.
  그러나 승인된 요구사항 `FR-6`은 미선택 허용 타입도 `0`으로 표현할
  수 있어야 한다고 정했으므로, 이 선택이 API 계약 변경 의도인지
  확인이 필요하다.
- `Q2=B`: Reaction 집계 조회 실패 시 성공 응답에 모든 집계를 `0`으로
  대체하는 선택이다. 이는 실제 `0`과 집계 실패를 구별할 수 없게 해
  집계 무결성과 안전한 실패 원칙을 약화할 수 있으므로 명시적 의도
  확인이 필요하다.
- 해당 두 항목이 확정될 때까지 조회 통합 Functional Design
  산출물과 PBT 속성을 생성하지 않는다.

## 확인 답변 반영

- 확인 `Q1=A`: 초기 `Q1=B`를 대체한다. 모든 고해 조회 응답은
  `PRAY`, `COMFORT`, `TOGETHER`를 항상 포함하고 미선택 타입은
  `count: 0`으로 표현한다.
- 확인 `Q2=A`: 초기 `Q2=B`를 대체한다. 집계 조회 실패는 실제
  `0`으로 위장하지 않고 고해 조회 전체를 안전한 실패로 처리한다.
- 승인된 `FR-6` 및 집계 무결성 기준과의 충돌은 해소되었다.

## Question 2

고해 데이터는 조회되었지만 Reaction 집계를 정상적으로 가져오지
못한 경우, 읽기 응답은 어떻게 처리할까?

A) 집계를 `0`으로 대체하지 않고 조회 전체를 안전한 실패로 처리한다.
잘못된 지지 집계를 성공 응답으로 제공하지 않는다.

B) 고해 본문 조회는 성공으로 반환하고 반응 집계는 모두 `0`으로
대체한다. 집계 오류는 사용자 응답과 별도로 기록한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: B
