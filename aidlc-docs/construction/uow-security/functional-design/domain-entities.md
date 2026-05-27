# UOW-SECURITY Domain Entities

## 기능 도메인 엔터티 판정

`UOW-SECURITY`는 Reaction 선택이나 Confession 집계와 같은 사용자
기능 도메인 엔터티를 새로 소유하지 않는다.

## SecurityControlDecision

후속 NFR 단계에서 구체화할 공통 보호 판단 개념이다.

- 요청이 기능 use case로 진행 가능한지 또는 제한되는지 나타낸다.
- 원문 기기 식별 정보를 외부 응답이나 로그로 전달하지 않는다.
- 현재 단계에서는 정책 키, 저장 형태와 임계치를 확정하지 않는다.

## SecurityEvidenceAssessment

운영 및 공급망 증빙의 완료 판정 개념이다.

- 기준 항목: 적용 가능한 Security Baseline rule.
- 판정: 준수, 차단 finding 또는 근거가 있는 `N/A`.
- 기능 도메인 응답의 일부가 아니라 전달 승인 판단 자료다.

## Testable Properties (PBT-01)

- `SecurityControlDecision`은 NFR 정책이 확정되기 전이므로 현재
  단계에서 구체적인 PBT 속성을 정의하지 않는다.
- `SecurityEvidenceAssessment`는 증빙 검토 판정이며 무작위 입력
  기반의 기능 변환 대상이 아니므로 PBT-01은 `N/A`다.
