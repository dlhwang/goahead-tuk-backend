# UOW-CONFESSION-REACTION-INTEGRATION Domain Entities

## ConfessionReactionSummary

고해 읽기 응답에 결합되는 반응 집계 읽기 값이다.

### 속성

- `confessionId`: 집계가 속한 고해 식별자.
- `counts`: 세 허용 타입에 대한 `ReactionCountItem` 목록.

### 불변식

- 성공 결과에는 `PRAY`, `COMFORT`, `TOGETHER`가 정확히 한 번씩
  존재한다.
- 각 count는 `0` 이상이며 미선택 타입은 `0`이다.
- 기기 식별자 또는 개인 선택 행은 포함하지 않는다.

## ReactionCountItem

- `type`: 허용 반응 타입.
- `count`: 해당 타입의 활성 선택 수.

## 집계 실패 결과

집계 실패는 성공한 `ConfessionReactionSummary`의 변형이 아니다.
성공 응답 없이 안전한 읽기 실패로 전달되며 `0` 집계 값으로
표현되지 않는다.

## 관계

1. 하나의 조회된 Confession은 하나의 `ConfessionReactionSummary`와
   결합된다.
2. summary는 `UOW-REACTION`이 제공한 aggregate 결과를 정규화한다.
3. summary는 Reaction 선택 주체에 대한 정보를 알지 못한다.

## Testable Properties (PBT-01)

- 세 타입 완전성 및 비음수 count는 **Invariant** 속성이다.
- 선택 집합에서 summary로의 타입별 집계는 참조 `groupBy/count`
  모델과 비교하는 **Oracle** 속성이다.
- 실패에서 성공 summary가 생성되지 않는 규칙은 결과 구조를 검사하는
  **Easy verification** 속성이다.
