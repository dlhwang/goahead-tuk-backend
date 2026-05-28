# UOW-CONFESSION-REACTION-INTEGRATION Business Rules

## 집계 표현

### BR-INTEGRATION-1 완전한 타입 집합

성공한 단건 및 목록 응답은 항상 `PRAY`, `COMFORT`, `TOGETHER`의
세 집계 항목을 포함한다.

### BR-INTEGRATION-2 빈 선택의 표현

활성 선택이 없는 타입은 생략하지 않고 `count: 0`으로 반환한다.

### BR-INTEGRATION-3 파생 집계

응답의 반응 수는 Reaction aggregate port가 제공하는 활성 선택
집계에서만 파생한다.

## 데이터 보호 및 실패

### BR-INTEGRATION-4 제한된 자기 선택 공개

단건 조회 응답은 요청 기기 자신의 타입별 활성 선택 여부만
`selectedByMe`로 포함할 수 있다. 응답은 `X-Device-Id`, 저장된
기기 파생 키, 작성자 판단용 기기 정보 또는 다른 기기의 개별 선택
내용을 포함하지 않는다. 목록 조회와 작성 응답은 자기 선택 상태를
포함하지 않는다.

### BR-INTEGRATION-5 집계 실패 시 fail-closed

Reaction 집계를 획득할 수 없으면 고해 본문만을 성공 응답으로
반환하거나 집계를 `0`으로 대체하지 않는다. 조회 전체를 안전한
실패로 처리한다.

### BR-INTEGRATION-6 단건 조회 기기 입력

자기 선택 상태를 제공하는 단건 조회는 유효한 `X-Device-Id`를
필수로 받는다. 원문 값은 기존 반응 변경 API와 같은 형식 검증을
통과해야 하며, 저장 조회에는 해시된 기기 키만 사용한다.

### BR-INTEGRATION-7 자기 선택 정규화

단건 조회는 세 허용 타입마다 요청 기기의 활성 선택 존재 여부를
판별한다. 선택된 타입은 `selectedByMe: true`, 선택되지 않은
타입은 `selectedByMe: false`로 반환하며, 전체 count와 독립적으로
조립한다.

### BR-INTEGRATION-8 자기 선택 실패 시 fail-closed

요청 기기의 선택 상태를 획득할 수 없으면 모든 타입을
`selectedByMe: false`로 간주하지 않는다. 단건 조회 전체를 안전한
실패로 처리한다.

## PBT 전달 요구

- BR-INTEGRATION-1부터 BR-INTEGRATION-3은 임의 선택 집합의 집계
  정규화 **Invariant** 및 단순 모델 비교 **Oracle** 속성으로
  검증한다.
- BR-INTEGRATION-4부터 BR-INTEGRATION-8은 생성된 요청 기기 선택
  집합과 단건 읽기 결과의 `selectedByMe` 비교 **Oracle**, raw ID와
  타인 선택 비노출 구조 검증 속성, 실패 경로 예제 테스트로
  보호한다.
- 목록 조회는 여러 고해 선택 집합을 생성하여 각 고해별 정규화가
  서로 섞이지 않는지 검증한다.
