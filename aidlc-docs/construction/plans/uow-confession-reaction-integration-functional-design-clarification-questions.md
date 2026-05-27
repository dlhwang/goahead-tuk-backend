# UOW-CONFESSION-REACTION-INTEGRATION Functional Design 확인 질문

<!-- markdownlint-disable MD053 -->

## 확인이 필요한 이유

조회 통합 계획의 답변은 승인된 API 계약 및 데이터 무결성 요구와
다르게 해석될 수 있다.

- 승인된 `FR-6`은 미선택 허용 타입도 `0`으로 표현 가능한 조회 응답을
  요구한다. 활성 타입만 반환하면 클라이언트가 타입 목록과 기본값을
  별도로 알아야 한다.
- 집계 조회 실패를 실제 집계 `0`으로 대체하면 사용자가 잘못된 지지
  수를 정상 결과로 보게 되고, `SECURITY-13`의 데이터 무결성 검증에
  영향을 준다.

## Question 1

미선택 타입의 조회 응답 계약은 어떤 결정을 확정할까?

A) 승인된 계약을 유지한다. 모든 조회 응답은 `PRAY`, `COMFORT`,
`TOGETHER`를 항상 포함하고 미선택 타입은 `count: 0`으로 반환한다.

B) API 계약을 변경한다. 활성 선택이 있는 타입만 반환하고, 누락된
허용 타입은 클라이언트가 `0`으로 해석하도록 요구사항과 스토리를
수정한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2

집계 조회 실패 시 사용자가 실제 값과 실패를 혼동하지 않도록 어떤
응답 규칙을 확정할까?

A) 집계를 가져오지 못하면 고해 조회 전체를 안전한 실패로 처리한다.
성공 응답에는 실제로 계산된 집계만 포함한다.

B) 고해 조회 응답은 성공으로 유지하되, 집계가 이용 불가능하다는
명시적 상태를 응답 계약에 추가한다. 실제 `0` 집계로 위장하지 않는다.

C) 집계 실패를 실제 `0` 집계와 구별하지 않고 성공 응답으로
반환한다. 이는 집계 무결성 완화로 기록하고 관련 Security Baseline
finding을 수용한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A
