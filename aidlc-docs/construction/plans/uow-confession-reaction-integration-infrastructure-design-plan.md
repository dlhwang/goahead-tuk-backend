# UOW-CONFESSION-REACTION-INTEGRATION Infrastructure Design 계획

<!-- markdownlint-disable MD053 MD060 -->

## 후속 Demo 범위 변경

기존 답변은 Railway PostgreSQL production 전제에서 수집되었으나,
이후 사용자는 첫 배포를 H2 메모리 database 기반의 일회성 demo로
재정의했다. 읽기 통합 산출물은 전용 demo profile의 datasource를
현재 배치 경계로 사용한다.

## 목표

Confession 읽기와 Reaction batch 집계를 결합하는 경로를 기존
Spring Boot/Railway 배포 및 PostgreSQL 질의 구조에 매핑한다.

## 확인된 현재 구성과 범주 판단

| 범주                        | 현재 판단                                               |
|---------------------------|-----------------------------------------------------|
| Deployment Environment    | 기존 Railway 배포 서비스 안의 읽기 API를 확장한다.                  |
| Compute Infrastructure    | 별도 읽기 서비스나 read replica 요구는 승인되지 않았다.               |
| Storage Infrastructure    | 목록 집계용 그룹 질의를 지원하는 인덱스 배치가 필요하다.                    |
| Messaging Infrastructure  | 읽기 응답은 동기 집계이며 queue/event stream은 `N/A`다.          |
| Networking Infrastructure | 외부 노출은 기존 API ingress를 공유하며 이 단위만의 gateway가 없다.     |
| Monitoring Infrastructure | 집계 실패 관측은 공통 구조화 로그 및 경보 증빙을 사용한다.                  |
| Shared Infrastructure     | `UOW-REACTION`의 선택 테이블과 `UOW-SECURITY` 운영 경계를 공유한다. |

## 계획 체크리스트

- [x] Functional Design, NFR Design 및 현재 배포 파일을 검토한다.
- [x] 아래 Infrastructure Design 질문의 답변을 분석한다.
- [x] `aidlc-docs/construction/uow-confession-reaction-integration/infrastructure-design/infrastructure-design.md`
  를
  생성한다.
- [x] `aidlc-docs/construction/uow-confession-reaction-integration/infrastructure-design/deployment-architecture.md`
  를
  생성한다.
- [x] N+1 방지, fail-fast 및 공통 관측 책임을 점검한다.
- [x] 생성 산출물에 대한 사용자의 명시적 승인을 요청한다.

## Question 1

목록 집계 읽기 경로의 초기 배포 topology는 무엇으로 정할까?

A) 기존 Spring Boot 인스턴스와 기본 PostgreSQL 연결을 그대로
사용하고, 그룹 집계 질의 및 인덱스로 읽기 요구를 만족시킨다.

B) MVP부터 Reaction 집계를 위한 별도 읽기 서비스 또는 read replica를
도입한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2

집계 조회 실패에 대한 운영 관측을 어느 인프라 경계에서 제공할까?

A) 애플리케이션이 안전한 구조화 오류 이벤트와 metric을 출력하고,
Railway 또는 승인된 중앙 관측 서비스가 수집·경보·보존 증빙을
제공한다.

B) 집계 읽기 경로만을 위한 별도 APM 또는 queue 기반 장애 복구
컴포넌트를 MVP 인프라에 추가한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A
