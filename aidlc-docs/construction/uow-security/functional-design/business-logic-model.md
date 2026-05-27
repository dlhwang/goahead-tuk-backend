# UOW-SECURITY Business Logic Model

## 단위 목적

Reaction 기능 단위와 조회 통합 단위가 전체 차단 Security Baseline
아래에서 전달될 수 있도록 공통 보호 의존성과 검증 게이트를 정의한다.

## 기능 경계

1. 공개 쓰기 요청은 기능 use case 진입 전에 공통 남용 제한 경계를
   통과한다.
2. 기능 단위는 입력 검증, 기기 식별 비노출 및 안전한 오류 계약을
   지켜야 한다.
3. 운영 및 공급망 보안 증빙은 기능 성공 결과가 아닌 전체 전달
   승인의 전제 조건으로 평가된다.
4. 공통 보호의 구체적 정책 값과 기술 선택은 NFR Design에서
   상세화한다.

## 완료 게이트

- `UOW-REACTION` 및 `UOW-CONFESSION-REACTION-INTEGRATION`은 이 단위와
  병행 설계 및 구현할 수 있다.
- 통합 검증과 Build and Test 승인 전까지 공통 보호 적용과 적용 가능한
  모든 Security Baseline 증빙 판정이 완료되어야 한다.
- 증빙이 없거나 기준을 충족하지 않는 항목은 차단 finding으로 남는다.

## Testable Properties (PBT-01)

- 이 Functional Design 단계에서 `UOW-SECURITY`는 확정된 비즈니스
  상태 변환 알고리즘을 소유하지 않으므로 PBT-01 대상 기능 속성은
  식별되지 않는다.
- 향후 NFR Design에서 stateful rate-limit 정책 저장 모델이 확정되면
  제한 불변식과 상태 기반 PBT 적용 가능성을 다시 평가한다.
