# UOW-REACTION NFR Requirements 계획

<!-- markdownlint-disable MD053 -->

## 목표

반응 선택/해제와 집계 제공 단위의 응답성, 신뢰성 및 테스트 기술
선택에 필요한 비기능 요구사항을 확정한다.

## 현재 확정된 품질 입력

- 반응 선택과 해제는 공개 API이며 `204 No Content`로 멱등 동작한다.
- 자기 고해 반응은 거부하며, 기기 식별 원문은 노출하거나 기록하지
  않는다.
- 선택 유일성, 멱등성 및 집계 일치는 PBT 대상 속성이다.
- rate limiting과 공통 오류/로그 경계는 `UOW-SECURITY`가 소유한다.

## NFR 범주 판단

- **Scalability/Performance**: 변이 요청과 집계 조회의 목표 부하 및
  지연 기준이 아직 없다.
- **Availability/Reliability**: 데이터베이스 실패 시 안전한 실패
  원칙은 확정되었고, 정량 가용성 목표는 아직 없다.
- **Security**: 입력 검증과 자기 반응 거부가 필수이며 공통 제어는
  보안 단위 질문에서 다룬다.
- **Tech Stack/Maintainability**: PBT 도구 선택은 공통 보안/품질
  계획에서 한 번 확정해 기능 단위에 적용한다.
- **Usability**: API 성공 계약은 이미 확정되어 추가 사용자 인터페이스
  질문은 없다.

## 계획 체크리스트

- [x] 기능 설계와 식별된 PBT 속성을 검토한다.
- [x] 아래 NFR 질문의 답변을 받고 모순 또는 모호성을 분석한다.
- [x] `aidlc-docs/construction/uow-reaction/nfr-requirements/nfr-requirements.md`를
  생성한다.
- [x] `aidlc-docs/construction/uow-reaction/nfr-requirements/tech-stack-decisions.md`를
  생성한다.
- [x] 공통 보안 및 PBT 결정과의 일관성을 검증한다.
- [x] 생성 산출물에 대한 사용자의 명시적 승인을 요청한다.

## Question 1

Reaction 선택/해제 API와 단일 고해 집계 port의 초기 응답 시간 목표는
어떤 수준으로 둘까?

A) 정상 운영 조건의 서버 처리 시간 `p95 <= 300 ms`를 목표로 하고,
성능 검증 기준으로 기록한다.

B) 정상 운영 조건의 서버 처리 시간 `p95 <= 500 ms`를 목표로 하고,
성능 검증 기준으로 기록한다.

C) 이번 MVP에서는 정량 지연 목표를 차단 조건으로 두지 않고,
정상 기능 및 보안 검증만 필수로 한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 2

초기 Reaction 쓰기 부하를 기준으로 설계할 최소 처리 용량은 어떻게
기록할까?

A) 단일 애플리케이션 인스턴스 기준 지속 `20 requests/second`의
Reaction 변이 요청을 오류 없이 처리하는 것을 목표로 한다.

B) 단일 애플리케이션 인스턴스 기준 지속 `100 requests/second`의
Reaction 변이 요청을 오류 없이 처리하는 것을 목표로 한다.

C) 현재 예상 트래픽 근거가 없으므로 정량 처리량은 후속 운영 측정으로
남기고, rate limiting 및 무결성 동작만 이번 차단 기준으로 둔다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## 답변 분석 결과

- `Q1=B`: Reaction 선택/해제 API와 단일 고해 집계 port는 정상 운영
  조건의 서버 처리 시간 `p95 <= 500 ms`를 목표로 한다.
- `Q2=A`: 단일 애플리케이션 인스턴스는 지속 `20 requests/second`의
  Reaction 변이 요청을 오류 없이 처리하는 목표를 가진다.
- 두 답변은 MVP의 기능 범위와 공통 보안 제한 정책을 함께 상세화할
  수 있는 구체적인 성능 기준이며 상충하지 않는다.
