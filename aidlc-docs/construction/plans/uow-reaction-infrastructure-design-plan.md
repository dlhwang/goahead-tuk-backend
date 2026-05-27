# UOW-REACTION Infrastructure Design 계획

<!-- markdownlint-disable MD053 MD060 -->

## 후속 Demo 범위 변경

기존 답변은 Railway PostgreSQL production 전제에서 수집되었으나,
이후 사용자는 첫 배포를 H2 메모리 database 기반의 일회성 demo로
재정의했다. 아래 생성 산출물은 후속 승인된 demo 전제를 우선한다.

## 목표

Reaction 활성 선택 저장과 멱등 변이 패턴을 현재 Railway PostgreSQL
배포 구조의 실제 데이터베이스 및 마이그레이션 구성에 매핑한다.

## 확인된 현재 구성과 범주 판단

| 범주                        | 현재 판단                                                     |
|---------------------------|-----------------------------------------------------------|
| Deployment Environment    | Railway Docker 배포와 Railway용 PostgreSQL profile이 이미 존재한다.  |
| Compute Infrastructure    | Reaction은 기존 Spring Boot 서비스 안에 배치되며 별도 compute가 필요하지 않다. |
| Storage Infrastructure    | 별도 선택 테이블과 유일/집계 인덱스의 배포 방식이 미확정이다.                       |
| Messaging Infrastructure  | 반응 선택과 집계는 동기 API 범위이며 메시징 요구가 없어 `N/A`다.                 |
| Networking Infrastructure | Reaction 고유 네트워크 컴포넌트는 없고 공통 공개 API 경계를 사용한다.             |
| Monitoring Infrastructure | 변이 실패 및 유일 충돌 관측은 공통 보안/운영 설계에 연결한다.                      |
| Shared Infrastructure     | 기존 애플리케이션과 PostgreSQL을 Confession과 공유한다.                  |

## 계획 체크리스트

- [x] Functional Design, NFR Design 및 현재 배포 파일을 검토한다.
- [x] 아래 Infrastructure Design 질문의 답변을 분석한다.
- [x] `aidlc-docs/construction/uow-reaction/infrastructure-design/infrastructure-design.md`를
  생성한다.
- [x] `aidlc-docs/construction/uow-reaction/infrastructure-design/deployment-architecture.md`를
  생성한다.
- [x] 저장 무결성, 마이그레이션 및 공통 보안 증빙 책임을 점검한다.
- [x] 생성 산출물에 대한 사용자의 명시적 승인을 요청한다.

## Question 1

별도 Reaction 선택 테이블과 유일 제약의 production schema 변경을
어떤 방식으로 배포할까?

A) Flyway versioned migration을 애플리케이션에 추가하고 production은
Hibernate `validate`를 유지하여 명시적인 migration 이후에만 실행한다.

B) Railway 배포 전에 운영자가 별도 SQL 절차를 수동 실행하고,
애플리케이션에는 migration 도구를 추가하지 않는다.

X) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 2

활성 선택 저장과 batch 집계를 위한 PostgreSQL 배치 경계는 어떻게
정할까?

A) 기존 Railway PostgreSQL database에 Reaction 선택 테이블을
추가하고, 삼중 키 유일 제약과 고해/타입 집계용 인덱스를 함께
관리한다.

B) Reaction 데이터를 위한 별도 database 또는 schema를 배치하여
Confession 저장소와 논리적으로 분리한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A
