# UOW-REACTION Business Rules

## 반응 타입 및 입력

### BR-REACTION-1 허용 타입

허용되는 반응 타입은 `PRAY`, `COMFORT`, `TOGETHER`뿐이다. 댓글 또는
자유 텍스트 반응 값은 입력 모델에 포함하지 않는다.

### BR-REACTION-2 익명 요청 주체

선택과 해제는 유효한 `X-Device-Id`를 요구한다. 기기 식별 원문은
응답 또는 기능 로그에 포함하지 않는다.

## 선택 상태

### BR-REACTION-3 독립 선택

하나의 기기는 하나의 고해에 서로 다른 허용 타입을 동시에 선택할
수 있다.

### BR-REACTION-4 유일성

동일한 `(confessionId, deviceId, reactionType)` 조합의 활성 선택은
최대 하나다.

### BR-REACTION-5 선택 멱등성

이미 존재하는 동일 선택에 대한 선택 요청은 새 선택을 만들지 않으며
성공 결과를 유지한다.

### BR-REACTION-6 해제 멱등성 및 삭제 모델

해제는 활성 선택 행을 제거한다. 이미 선택이 없는 해제 요청도 성공
상태를 유지하며, 삭제 이력은 이 기능 도메인에서 저장하지 않는다.

## 대상 고해 및 작성자 규칙

### BR-REACTION-7 대상 존재

선택 및 해제는 존재하는 고해에만 적용할 수 있다.

### BR-REACTION-8 자기 고해 반응 금지

요청 기기 식별이 대상 고해 작성자의 익명 기기 식별과 같으면 선택과
해제를 모두 허용하지 않는다. 비교에 필요한 작성자 식별은 외부
응답으로 노출하지 않는다.

## 집계

### BR-REACTION-9 집계 파생

반응 수는 활성 선택 행에서 파생하며 `Confession`에 타입별 카운트
컬럼으로 저장하지 않는다.

### BR-REACTION-10 비노출

집계 port는 고해별 타입 수만 제공하며, 개별 기기나 기기의 선택
목록을 소비자에게 전달하지 않는다.

## PBT 전달 요구

- BR-REACTION-4부터 BR-REACTION-6과 BR-REACTION-9는 상태 기반
  property-based tests의 불변식과 멱등성 속성으로 구현한다.
- BR-REACTION-8은 자기 고해와 타인 고해 기기 관계를 생성하는
  domain generator 및 예제 기반 거부 테스트로 검증한다.
- PBT framework, seed 재현 및 shrinking 설정은 NFR Requirements에서
  확정한다.
