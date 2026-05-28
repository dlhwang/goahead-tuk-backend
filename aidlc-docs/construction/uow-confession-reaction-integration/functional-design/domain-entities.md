# UOW-CONFESSION-REACTION-INTEGRATION Domain Entities

## ConfessionReactionSummary

고해 읽기 응답에 결합되는 반응 읽기 값이다. 단건 조회에서는 요청
기기의 자기 선택 상태를 포함하고 목록 조회에서는 집계만 사용한다.

### 속성

- `confessionId`: 집계가 속한 고해 식별자.
- `counts`: 세 허용 타입에 대한 `ReactionCountItem` 목록.
- `selectedTypesByRequester`: 단건 조회 조립 시에만 사용하는 요청
  기기의 활성 반응 타입 집합. 응답에는 boolean으로만 매핑된다.

### 불변식

- 성공 결과에는 `PRAY`, `COMFORT`, `TOGETHER`가 정확히 한 번씩
  존재한다.
- 각 count는 `0` 이상이며 미선택 타입은 `0`이다.
- 단건 응답은 세 타입마다 `selectedByMe`를 정확히 한 번 포함한다.
- 응답에는 raw 기기 식별자, 해시된 기기 키 또는 타인의 개인 선택
  행을 포함하지 않는다.

## ReactionCountItem

- `type`: 허용 반응 타입.
- `count`: 해당 타입의 활성 선택 수.
- `selectedByMe`: 단건 조회에서 요청 기기가 해당 타입을 선택했는지
  나타내는 파생 boolean.

## 집계 실패 결과

집계 실패는 성공한 `ConfessionReactionSummary`의 변형이 아니다.
성공 응답 없이 안전한 읽기 실패로 전달되며 `0` 집계 값으로
표현되지 않는다.

## 관계

1. 하나의 조회된 Confession은 하나의 `ConfessionReactionSummary`와
   결합된다.
2. summary는 `UOW-REACTION`이 제공한 aggregate 결과를 정규화한다.
3. 단건 summary 조립은 요청 기기의 해시 키에 해당하는 선택 타입
   집합만 소비하며, 외부 결과에는 boolean만 노출한다.
4. 목록 summary는 자기 선택 집합을 소비하지 않는다.

## Testable Properties (PBT-01)

- 세 타입 완전성 및 비음수 count는 **Invariant** 속성이다.
- 선택 집합에서 summary로의 타입별 집계는 참조 `groupBy/count`
  모델과 비교하는 **Oracle** 속성이다.
- 요청 기기 선택 타입 집합에서 단건 summary의 `selectedByMe`로의
  변환은 집합 포함 여부를 비교하는 **Oracle** 속성이다.
- raw ID와 타인 선택을 응답에서 제외하는 규칙은 **Invariant**
  속성이다.
- 실패에서 성공 summary가 생성되지 않는 규칙은 결과 구조를 검사하는
  **Easy verification** 속성이다.
