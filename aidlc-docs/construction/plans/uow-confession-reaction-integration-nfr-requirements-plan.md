# UOW-CONFESSION-REACTION-INTEGRATION NFR Requirements 계획

<!-- markdownlint-disable MD053 -->

## 목표

고해 읽기 응답에 반응 집계를 결합하는 경로의 성능 및 신뢰성 기준을
확정한다.

## 현재 확정된 품질 입력

- 성공 응답은 세 허용 타입과 미선택 타입의 `0` count를 항상 포함한다.
- 집계 조회 실패 시 실제 `0`으로 대체하지 않고 전체 조회를 안전하게
  실패 처리한다.
- 응답은 익명 기기 식별이나 개별 선택 내역을 노출하지 않는다.

## NFR 범주 판단

- **Performance/Scalability**: 목록 조회 시 집계 결합 비용을 제한할
  기준이 아직 없다.
- **Availability/Reliability**: fail-closed 동작은 확정되어 있고,
  운영 모니터링 및 경보는 보안 단위가 상세화한다.
- **Security**: 데이터 비노출 및 집계 무결성은 확정된 차단 기준이다.
- **Tech Stack/Usability**: 새 기술 선택이나 UI 범위는 없다.

## 계획 체크리스트

- [x] 조회 통합 기능 설계와 집계 속성을 검토한다.
- [x] 아래 NFR 질문의 답변을 받고 모순 또는 모호성을 분석한다.
- [x] `aidlc-docs/construction/uow-confession-reaction-integration/nfr-requirements/nfr-requirements.md`
  를
  생성한다.
- [x] `aidlc-docs/construction/uow-confession-reaction-integration/nfr-requirements/tech-stack-decisions.md`
  를
  생성한다.
- [x] 집계 무결성 및 공통 오류 경계와의 일관성을 검증한다.
- [x] 생성 산출물에 대한 사용자의 명시적 승인을 요청한다.

## Question 1

고해 목록 조회에서 Reaction 집계 결합이 가져야 할 성능 경계는
어떻게 정의할까?

A) 목록 한 번당 집계 조회를 배치로 한 번 수행해 N+1 집계 질의를
금지하고, 정상 운영 조건에서 목록 응답 `p95 <= 500 ms`를 목표로
한다.

B) 목록 한 번당 집계 조회를 배치로 한 번 수행해 N+1 집계 질의를
금지하되, 이번 MVP에는 정량 지연 목표를 차단 조건으로 두지 않는다.

C) 초기 데이터 규모가 작다고 가정하고 고해별 개별 집계 조회를
허용한다. 성능 최적화는 후속 작업으로 둔다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## 답변 분석 결과

- `Q1=A`: 목록 응답은 고해별 집계를 개별 반복 조회하지 않고 한 번의
  batch aggregate 조회로 결합하며, 정상 운영 조건의 서버 처리 시간
  `p95 <= 500 ms`를 목표로 한다.
- 이 기준은 집계 실패 시 fail-closed 규칙 및 타입별 집계 무결성과
  충돌하지 않는다.
