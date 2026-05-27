# UOW-CONFESSION-REACTION-INTEGRATION Deployment Architecture

## 읽기 배치 구조

```mermaid
flowchart LR
    Client["익명 열람자"] --> Api["Confession 읽기 API"]
    Api --> Query["ConfessionQueryService"]
    Query --> ConfessionDb["Confession 조회"]
    Query --> AggregateDb["Reaction 그룹 집계 1회"]
    ConfessionDb --> Assemble["응답 조립"]
    AggregateDb --> Assemble
    Assemble --> Response["세 타입 count 응답"]
```

## 장애와 관측 흐름

```mermaid
flowchart LR
    Aggregate["Reaction 집계 조회"] -->|성공| Assemble["응답 조립"]
    Aggregate -->|실패| Error["안전한 오류 경계"]
    Error --> Log["구조화 오류 event 및 metric"]
    Log --> Monitor["Railway 또는 승인 중앙 관측 서비스"]
```

## 배포 전제

- read replica, 별도 query 서비스, queue 또는 장애 복구 store를
  MVP에 추가하지 않는다.
- demo 데이터 초기화는 허용된 preview 제약이다.
- 중앙 관측 서비스의 보존 및 경보 기준은 영속 production 전환 전
  증빙한다.
