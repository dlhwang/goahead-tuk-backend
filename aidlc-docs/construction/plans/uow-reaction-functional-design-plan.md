# UOW-REACTION Functional Design 계획

<!-- markdownlint-disable MD053 -->

## 목표

익명 반응 선택과 해제, 별도 선택 저장, Reaction aggregate port의
비즈니스 규칙과 도메인 모델을 설계하고, Property-Based Testing에서
검증할 상태 속성을 식별한다.

## 입력 범위

- `US-2` 익명 열람자가 지지 반응을 선택하고 해제한다.
- `UOW-REACTION` 익명 반응 선택과 집계 제공.
- `SelectReactionUseCase`, `ClearReactionUseCase`,
  `ReactionSelectionRepository`, `ReactionAggregateReader`.
- 전체 차단 Security Baseline 중 입력 검증, 비노출 및 데이터 무결성
  요구.

## 계획 체크리스트

- [x] 승인된 단위, 사용자 스토리와 Application Design을 검토한다.
- [x] 아래 기능 질문의 답변을 받고 모순 또는 불명확성을 분석한다.
- [x] `aidlc-docs/construction/uow-reaction/functional-design/business-logic-model.md`에
  선택, 해제, 집계 제공 흐름을 정의한다.
- [x] `aidlc-docs/construction/uow-reaction/functional-design/business-rules.md`에
  타입, 멱등성, 유일성, 검증 및 오류 규칙을 정의한다.
- [x] `aidlc-docs/construction/uow-reaction/functional-design/domain-entities.md`에
  선택 엔터티와 값 개념 및 관계를 정의한다.
- [x] PBT-01에 따라 테스트 가능 속성, 상태 모델과 PBT 전달 요구를
  문서화한다.
- [x] 생성 산출물에 대한 사용자의 명시적 승인을 요청하고 승인받는다.

## Question 1

사용자가 반응을 `DELETE`로 해제할 때 도메인 상태는 어떻게
모델링할까?

A) 활성 선택 행을 제거한다. 집계와 선택 상태는 현재 활성 행만으로
표현하고, 별도 선택 이력은 이번 기능 도메인의 범위에 포함하지 않는다.

B) 선택 행을 유지하고 비활성 상태로 표시한다. 집계는 활성 행만
포함하고 선택/해제 이력은 도메인 데이터로 보존한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2

고해를 작성한 익명 기기가 자신의 고해에 허용된 반응을 남기는
시나리오는 어떻게 처리할까?

A) 다른 열람자와 동일하게 허용한다. MVP는 작성자 본인 여부에 따른
반응 금지 규칙을 두지 않는다.

B) 동일 기기가 작성한 고해에는 반응 선택과 해제를 거부한다.
익명 작성자 식별과 반응 기기 식별을 비교하는 도메인 규칙을 둔다.

X) Other (please describe after [Answer]: tag below)

[Answer]: B

## 답변 분석 결과

- `Q1=A`: 반응 해제는 활성 선택 행 삭제로 모델링한다. 이번 MVP에서
  선택 이력 저장은 기능 도메인 범위에 포함하지 않는다.
- `Q2=B`: 고해 작성 기기와 반응 요청 기기가 동일하면 해당 고해에
  대한 반응 선택 및 해제를 거부한다. 따라서 Reaction Application은
  대상 고해 작성자의 익명 기기 식별과 요청 기기 식별을 비교할 수
  있는 협력 계약을 상세 설계에 포함한다.
- 두 답변은 승인된 요구사항에 추가되는 구체적 비즈니스 규칙이며
  서로 모순되지 않는다.
