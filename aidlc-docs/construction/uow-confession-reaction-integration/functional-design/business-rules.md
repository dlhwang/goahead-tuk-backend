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

### BR-INTEGRATION-4 비노출

조회 응답은 `X-Device-Id`, 작성자 판단용 기기 정보 또는 개별 반응
선택 내용을 포함하지 않는다.

### BR-INTEGRATION-5 집계 실패 시 fail-closed

Reaction 집계를 획득할 수 없으면 고해 본문만을 성공 응답으로
반환하거나 집계를 `0`으로 대체하지 않는다. 조회 전체를 안전한
실패로 처리한다.

## PBT 전달 요구

- BR-INTEGRATION-1부터 BR-INTEGRATION-3은 임의 선택 집합의 집계
  정규화 **Invariant** 및 단순 모델 비교 **Oracle** 속성으로
  검증한다.
- BR-INTEGRATION-4와 BR-INTEGRATION-5는 생성된 읽기 결과와 실패
  결과의 구조 검증 속성 및 예제 기반 오류 테스트로 보호한다.
- 목록 조회는 여러 고해 선택 집합을 생성하여 각 고해별 정규화가
  서로 섞이지 않는지 검증한다.
