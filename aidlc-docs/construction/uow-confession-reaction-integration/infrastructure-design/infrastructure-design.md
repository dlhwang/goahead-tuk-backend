# UOW-CONFESSION-REACTION-INTEGRATION Infrastructure Design

<!-- markdownlint-disable MD060 -->

## 범위

기존 Spring Boot Confession 읽기 경로가 전용 H2 demo datasource의
Reaction 선택 테이블에서 batch aggregate를 읽어 응답을 조립한다.
이 배치는 일회성 preview 용도이며 데이터 지속성을 보장하지 않는다.

## 컴포넌트 매핑

| 논리 컴포넌트 | 실제 배치 | 운영 제약 |
| -------------- | --------- | --------- |
| `ConfessionQueryService` | 기존 Railway 애플리케이션 인스턴스 | 별도 읽기 서비스나 replica를 추가하지 않는다. |
| `ReactionAggregateJpaAdapter` | 기존 datasource 연결 | 목록 요청마다 그룹 집계 질의를 한 번만 수행한다. |
| `ReactionSummaryAssembler` | 애플리케이션 메모리 | 누락 타입만 `0`으로 보완한다. |
| `SafeReadErrorBoundary` | 애플리케이션 오류 처리 및 공통 로그 | 저장소 실패를 성공 응답으로 위장하지 않는다. |

## 성능과 장애 경계

- 읽기 topology는 기존 서비스와 H2 demo datasource를 사용한다.
- 목록 aggregate는 Reaction 선택 테이블의 인덱스를 사용하는 단일
  그룹 집계 질의로 수행한다.
- 집계 실패 시 요청 경로에서 재시도하거나 queue에 넘기지 않고
  안전한 외부 오류를 반환한다.
- 읽기 오류 event와 metric은 애플리케이션이 출력하고, 중앙 수집과
  경보·보존은 공유 운영 인프라 증빙으로 검증한다.

## 검증 및 증빙

- integration 테스트에서 목록당 aggregate query가 반복되지 않음을
  검증한다.
- 실패 시 `0` count 응답이 아닌 안전한 오류가 반환됨을 검증한다.
- demo에서는 안전한 로그/metric 출력까지 코드 검증 대상으로 삼고,
  장기 보존 및 경보 증빙은 production 전환 조건으로 추적한다.
