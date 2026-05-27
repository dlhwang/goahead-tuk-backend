# UOW-REACTION NFR Design 계획

<!-- markdownlint-disable MD053 -->

## 목표

승인된 반응 선택/해제 NFR을 트랜잭션, 동시성, 성능 검증 및 확장성
패턴으로 변환한다.

## 승인된 설계 입력

- 선택 및 해제는 멱등하며 성공 시 `204 No Content`를 반환한다.
- 같은 기기, 고해, 타입 조합의 활성 선택은 최대 하나이다.
- 선택/해제와 단일 고해 집계 제공의 서버 처리 시간 목표는 정상 운영
  조건에서 `p95 <= 500 ms`이다.
- 단일 애플리케이션 인스턴스의 초기 반응 변이 처리 목표는 지속
  `20 requests/second`이다.
- 멱등성, 유일성 및 활성 선택과 집계 일치는 PBT 대상이다.

## 계획 체크리스트

- [x] Functional Design 및 승인된 NFR Requirements를 검토한다.
- [x] 아래 NFR Design 질문의 답변을 분석한다.
- [x] `aidlc-docs/construction/uow-reaction/nfr-design/nfr-design-patterns.md`를
  생성한다.
- [x] `aidlc-docs/construction/uow-reaction/nfr-design/logical-components.md`를
  생성한다.
- [x] 동시성 처리와 PBT 검증 패턴의 일관성을 점검한다.
- [x] 생성 산출물에 대한 사용자의 명시적 승인을 요청한다.

## Question 1

동일 반응 선택 요청이 동시에 도착할 때 멱등성과 유일성을 보장하는
핵심 패턴은 무엇으로 정할까?

A) 데이터베이스 유일 제약과 트랜잭션을 최종 보장으로 사용하고,
동시 삽입 충돌은 이미 선택된 상태로 해석하여 멱등 성공으로 처리한다.

B) 애플리케이션 잠금으로 동일 조합의 요청을 직렬화하고 데이터베이스
제약은 보조 방어로만 사용한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2

`p95 <= 500 ms` 및 `20 requests/second` 목표를 MVP 전달 전에 어떻게
검증할까?

A) repository/integration 테스트에서 질의 및 동시성 동작을 검증하고,
목표 부하를 재현하는 집중 성능 검증 결과를 Build and Test 증빙에
기록한다.

B) 기능 테스트만 수행하고 정량 성능 목표는 배포 후 운영 지표로만
판정한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: X

MVP에서는 정량 성능 목표를 운영급 부하 테스트로 완전히 증명하지 않고,
다음 수준까지만 Build and Test 증빙으로 남긴다.

- repository/integration 테스트로 유일 제약, 멱등 선택/해제, 집계 일치 검증
- 제한된 동시성 테스트로 동일 반응 동시 요청 시 하나의 활성 선택만 남는지 검증
- p95 <= 500 ms, 20 requests/second는 설계 목표로 기록하되 MVP 게이트 실패 조건으로 삼지 않음
- 실제 운영 배포 후 actuator/로그/간단한 측정으로 후속 검증

즉, 기능적 안정성과 동시성 안전성은 MVP에서 검증하고,
정량 성능 목표는 후속 hardening 대상으로 둔다.

## Question 3

초기 Reaction 선택 저장소의 확장성 경계는 어떻게 설계할까?

A) 초기에는 단일 애플리케이션 인스턴스와 PostgreSQL 유일 제약을
기준으로 설계하고, 수평 확장 시에도 저장소 제약은 유지한다.

B) MVP부터 별도 공유 캐시 또는 분산 조정 저장소를 반응 선택 경로에
필수로 추가한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A
