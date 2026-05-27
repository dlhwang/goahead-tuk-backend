# UOW-REACTION Domain Entities

## ReactionSelection

활성 반응 하나를 나타내는 도메인 개념이다.

### 속성

- `reactionSelectionId`: 반응 선택 식별자.
- `confessionId`: 대상 고해 식별자.
- `deviceId`: 선택한 익명 기기 식별자.
- `reactionType`: `PRAY`, `COMFORT`, `TOGETHER` 중 하나.

### 불변식

- 동일 `(confessionId, deviceId, reactionType)`에 둘 이상의 활성
  선택이 존재하지 않는다.
- 선택 객체는 활성 상태만 표현한다. 해제된 선택은 객체로 유지하지
  않는다.

## ReactionType

서비스가 허용하는 긍정 반응의 닫힌 값 집합이다.

- `PRAY`: 기도할게요.
- `COMFORT`: 토닥토닥.
- `TOGETHER`: 함께할게요.

## ReactionCounts

고해별 허용 타입의 활성 선택 개수를 나타내는 읽기 값이다.

### 규칙

- 각 count는 음수가 될 수 없다.
- count는 활성 `ReactionSelection` 수로부터 계산한다.
- 개인 식별자를 포함하지 않는다.

## Confession Author Identity View

자기 고해 반응 금지 판단에 필요한 협력 읽기 개념이다.

- Reaction application은 대상 고해의 작성자 익명 기기 식별과 요청
  기기 식별의 동일성만 판단에 사용한다.
- 이 식별 정보는 Reaction 응답이나 집계 조회로 노출하지 않는다.

## 관계

1. 하나의 고해는 여러 `ReactionSelection`을 가질 수 있다.
2. 하나의 기기는 같은 고해에서 타입별로 최대 한 선택을 가진다.
3. 작성 기기와 동일한 기기는 해당 고해의 선택을 생성할 수 없다.

## Testable Properties (PBT-01)

- `ReactionSelection` 집합에 대해 유일성은 **Invariant** 속성이다.
- 선택과 삭제는 **Idempotence** 속성이다.
- `ReactionCounts`와 선택 집합의 일치는 **Invariant** 및 단순
  집합 모델을 oracle로 사용하는 **Oracle** 속성이다.
