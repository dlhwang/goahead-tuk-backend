# 고해 반응 Units Generation 확인 질문

<!-- markdownlint-disable MD053 -->

## 확인이 필요한 이유

초기 답변은 선택/해제, 집계 제공, Confession 조회 결합을 별도 기능
단위로 분리하고, 보안 지원 단위도 별도로 두는 방향을 선택했다.
그러나 원 질문의 보안 순서 선택지는 하나의 `UOW-REACTION`만을
전제로 했으므로, 실제 단위 목록과 보안 게이트의 적용 관계를
확정해야 한다.

## Question 1

기능 및 보안 작업 단위 목록은 어떤 형태로 확정할까?

A) 네 단위로 확정한다: `UOW-REACTION-MUTATION`(선택/해제),
`UOW-REACTION-AGGREGATE`(집계 port 제공),
`UOW-CONFESSION-REACTION-INTEGRATION`(고해 읽기 응답 결합),
`UOW-SECURITY`(공통 보호와 증빙).

B) 선택/해제와 집계 port 제공은 하나의 `UOW-REACTION`으로 다시
묶고, `UOW-CONFESSION-REACTION-INTEGRATION`과 `UOW-SECURITY`를
더한 세 단위로 확정한다.

C) 선택/해제, 집계 제공, Confession 읽기 결합을 하나의
`UOW-REACTION`으로 다시 묶고 `UOW-SECURITY`만 별도인 두 단위로
확정한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 2

별도 `UOW-SECURITY`의 완료 게이트는 분리된 기능 단위에 어떻게
적용할까?

A) 기능 단위들은 병행 구현할 수 있지만, 공개 변이 API와 고해 조회
결합의 통합 검증 및 전체 Build and Test 승인은 `UOW-SECURITY`의
공통 보호 적용과 Security Baseline 증빙 판정 이후에만 가능하다.

B) `UOW-SECURITY`의 공통 보호 설계를 먼저 확정한 뒤 기능 단위 구현을
시작하고, 운영/공급망 증빙 판정은 최종 Build and Test 전까지
완료한다.

C) 각 기능 단위가 필요한 보안 통제를 개별 검증하고,
`UOW-SECURITY`는 운영/공급망 증빙만 최종 승인 전에 검증한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A
