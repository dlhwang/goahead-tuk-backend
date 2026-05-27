# UOW-CONFESSION-REACTION-INTEGRATION Tech Stack Decisions

## 조회 통합 경계

- 기존 Spring Boot/Kotlin Confession 읽기 application을 유지하고
  Reaction aggregate port를 소비한다.
- 목록 읽기는 고해 ID collection을 받는 aggregate 조회 계약으로
  batch 결합한다.
- JPA 영속성 세부는 Reaction infrastructure adapter 내부에 남는다.

## 품질 검증 결정

- 목록 집계는 N+1 질의를 허용하지 않는 구조적 검증 대상으로 둔다.
- 목록 응답의 성능 목표는 정상 운영 조건 `p95 <= 500 ms`다.
- 집계 표현 및 모델 비교 PBT는 `Kotest Property Testing` 공통
  선택을 사용하고 seed 기록 정책을 따른다.
- 집계 실패가 성공 응답으로 변환되지 않는 경로는 예제 기반 테스트와
  구조 검증으로 고정한다.
