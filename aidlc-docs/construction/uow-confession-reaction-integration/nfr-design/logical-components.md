# UOW-CONFESSION-REACTION-INTEGRATION Logical Components

<!-- markdownlint-disable MD060 -->

## 논리 컴포넌트

| 컴포넌트 | 책임 | 협력 및 제약 |
| -------- | ---- | ------------ |
| `ConfessionQueryService` | 기존 고해 읽기와 Reaction 집계 결합 | 목록 식별자 전체를 aggregate reader에 한 번 전달한다. |
| `ReactionAggregateReader` | 단건 및 batch 집계 port | 활성 선택만 집계하고 내부 선택 행은 노출하지 않는다. |
| `ReactionAggregateJpaAdapter` | 그룹 집계 질의 실행 | 목록에서 고해별 반복 질의를 실행하지 않는다. |
| `ReactionSummaryAssembler` | 세 허용 타입 응답 구성 | 누락 타입에만 `0`을 채우며 저장소 실패를 숨기지 않는다. |
| `SafeReadErrorBoundary` | 집계 실패의 외부 오류 변환 | 내부 원인 및 저장소 세부 사항을 응답에 포함하지 않는다. |
| `AggregatePropertyTests` | 다중 고해 집계 모델 검증 | 완전성, count 일치와 고해 간 분리를 검사한다. |

## 읽기 흐름

1. 고해 읽기 서비스가 읽힌 고해 식별자 집합을 수집한다.
2. 집계 adapter가 한 번의 그룹 집계 질의로 타입별 count를 반환한다.
3. assembler가 각 고해에 세 타입을 배치하고 누락된 count를 `0`으로
   채운다.
4. 집계 adapter가 실패하면 전체 읽기는 안전한 오류로 종료한다.

## 구현 시 검증 항목

- 단건 및 목록 응답에 세 타입이 항상 존재하는지 여부
- 목록 조회에서 N+1 집계 쿼리가 발생하지 않는지 여부
- 집계 실패가 정상 `0` 응답으로 관측되지 않는지 여부
