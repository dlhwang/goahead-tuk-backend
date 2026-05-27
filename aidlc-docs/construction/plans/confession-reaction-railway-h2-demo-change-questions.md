# Railway H2 Demo 배포 전제 변경 질문

<!-- markdownlint-disable MD053 -->

## 배경

기존 Infrastructure Design은 Railway PostgreSQL production 배포를
전제로 작성되었다. 사용자가 실제 현재 Railway 배포는 H2이며,
Reaction 기능을 빠르게 배포해 확인하고 싶다고 알렸다.

저장소의 현재 H2 구성인 `application-local.yml`은 메모리 데이터베이스,
`ddl-auto: create-drop`, H2 console 활성화를 사용한다. 그대로 공개
배포에 재사용하면 재시작 또는 재배포 시 데이터가 사라지고, 내부 관리
화면 노출 위험도 생긴다.

## 영향

- PostgreSQL TLS 및 저장 암호화 증빙을 전제로 작성한 현재
  Infrastructure Design은 Railway demo 배포에는 맞지 않는다.
- 데이터 영속성이 없는 demo를 허용하면 Reaction 선택 데이터와 고해가
  서비스 재시작 때 초기화될 수 있다.
- 이전에 선택한 전체 차단 Security Baseline을 그대로 유지하면,
  운영 로그·보존·경보 증빙 전까지 공개 demo 구현 단계도 열 수 없다.
- 단, demo로 범위를 조정하더라도 입력 검증, 원문 기기 ID 비로그,
  안전 오류, rate limiting, H2 console 비노출, 보안 헤더와 PBT는
  생략할 수 없는 애플리케이션 보호로 유지하는 것이 안전하다.

## 변경 체크리스트

- [x] 사용자에게서 현재 Railway H2 배포 전제를 확인한다.
- [x] 기존 PostgreSQL Infrastructure Design과의 충돌을 분석한다.
- [x] 아래 변경 질문의 답변을 읽고 영향 범위를 확정한다.
- [x] 승인된 변경에 따라 Infrastructure Design 및 실행 계획을
  수정한다.
- [x] 수정된 설계 승인 후 Code Generation 계획을 연다.

## Question 1

이번 Reaction 배포의 환경 목적과 데이터 지속성 기대를 어떻게
정의할까?

A) 공개 가능한 일회성 demo/preview로 정의한다. H2 메모리 저장소를
사용하며 재시작 또는 재배포 시 고해와 Reaction 데이터가 초기화될
수 있음을 허용하고 사용자에게 표시할 배포 제약으로 기록한다.

B) MVP라도 반응과 고해 데이터 유지가 필요하므로, H2 demo가 아닌
영속 database 배포 설계로 돌아간다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2

기존에 선택한 전체 차단 Security Baseline을 demo 배포 단계에서는
어떻게 다룰까?

A) 이 배포를 production 출시가 아닌 demo preview로 재분류한다.
코드로 적용 가능한 보호인 입력 검증, raw device ID 비로그, 안전
오류, rate limiting, H2 console/docs 비노출, 보안 헤더, PBT와
공급망 검증은 구현 게이트로 유지한다. 90일 로그 보존, dashboard,
관리형 저장 암호화 같은 운영 증빙은 production 승인의 차단
조건으로 이월하고 demo Code Generation은 허용한다.

B) demo preview라도 기존 전체 차단 Security Baseline을 유지한다.
중앙 로그·보존·경보 및 저장 암호화 증빙이 마련될 때까지 Code
Generation과 공개 배포를 진행하지 않는다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 3

Railway H2 demo의 Spring profile은 어떻게 구성할까?

A) `application-local.yml`을 배포에 재사용하지 않고, H2 메모리
database를 사용하되 H2 console과 SQL 노출을 비활성화한 전용
`application-demo.yml` 또는 동등 profile을 추가한다. demo 데이터
초기화 특성을 명시한다.

B) 현재 `application-local.yml`을 그대로 Railway에서 사용한다.
H2 console 활성화와 `create-drop` 동작도 현재 preview의 일부로
허용한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A
