# UOW-CONFESSION-REACTION-INTEGRATION Business Logic Model

## 단위 목적

기존 고해 읽기 흐름이 Reaction 집계를 결합하여 단건 조회에서는
요청 기기 자신의 선택 여부를 함께 제공하고, raw 기기 식별자와
타인의 선택 정보는 노출하지 않는 응답을 제공한다.

## 단건 조회 흐름

1. API가 `X-Device-Id`의 형식을 검증하고 단건 읽기 application에
   전달한다.
2. 기존 Confession read application이 요청한 고해를 조회한다.
3. 고해가 존재하면 Reaction 조회 경계가 해당 고해의 집계와,
   해시된 요청 기기 키에 해당하는 선택 타입 집합을 읽는다.
4. 집계 값을 `PRAY`, `COMFORT`, `TOGETHER` 세 타입으로 정규화하고
   선택이 없는 타입은 `0`으로 채운다.
5. 각 타입이 요청 기기의 선택 타입 집합에 포함되었는지에 따라
   `selectedByMe`를 조립한다.
6. 고해 정보와 정규화한 타입별 count 및 자기 선택 상태를 결합해
   응답을 생성한다.

## 목록 조회 흐름

1. 기존 Confession 목록을 조회한다.
2. 조회된 고해 ID 목록을 사용해 Reaction aggregate port에 일괄 집계
   조회를 요청한다.
3. 각 고해마다 세 허용 타입을 항상 포함하는 집계 응답을 결합한다.
4. 집계에는 반응 주체 식별 정보 또는 `selectedByMe`가 포함되지
   않는다.

## 실패 경로

- 기존 고해 단건 조회가 실패하면 기존 not-found 정책을 유지한다.
- 고해 데이터가 존재하지만 Reaction 집계를 읽지 못하면 응답 전체를
  안전한 실패로 처리한다.
- 단건 조회의 요청 기기 선택 상태를 읽지 못하면 안전한 실패로
  처리하며 임의의 `false` 결과를 생성하지 않는다.
- 집계 실패를 실제 `0`인 성공 응답으로 대체하지 않는다.
- 실패 응답의 외부 표현과 로깅은 `UOW-SECURITY`의 안전한 오류
  경계와 결합한다.

## Testable Properties (PBT-01)

- **Invariant**: 모든 성공 응답의 반응 타입 집합은 정확히 `PRAY`,
  `COMFORT`, `TOGETHER`다.
- **Invariant**: 각 성공 응답의 타입별 수는 aggregate port에서 받은
  활성 선택 수와 같고 음수가 아니다.
- **Oracle**: 임의 선택 집합의 단순 타입별 `count` 모델과 조회 응답
  집계가 같다.
- **Oracle**: 임의 요청 기기의 타입별 선택 집합과 단건 응답의
  `selectedByMe` 값이 타입별로 같다.
- **Privacy invariant**: 성공 응답은 raw `X-Device-Id`, 기기 파생
  키 또는 타인의 개별 선택을 포함하지 않는다.
- **Easy verification**: 집계 조회 실패가 발생한 입력에서는 성공
  응답이 생성되지 않으며 위조된 `0` 집계가 관찰되지 않는다.
- **Easy verification**: 자기 선택 조회 실패가 발생한 입력에서는
  위조된 `selectedByMe: false` 응답이 생성되지 않는다.
