# UOW-SECURITY Business Rules

## 기능 단위 적용 경계

### BR-SECURITY-1 공개 쓰기 보호

Reaction 선택 및 해제와 영향받는 기존 공개 쓰기 경로는 공통
throttling 경계의 적용 대상이다. 제한 용량과 응답 세부 계약은
NFR Design에서 확정한다.

### BR-SECURITY-2 민감 식별자 비기록

원문 `X-Device-Id`는 애플리케이션 로그에 기록하지 않는다.

### BR-SECURITY-3 안전한 오류

입력 오류, 자기 고해 반응 거부, 집계 실패 및 내부 실패는 구현 세부나
민감 식별 데이터를 외부에 드러내지 않는 오류 경계를 사용한다.

### BR-SECURITY-4 증빙 게이트

운영 암호화, 중앙 로그/보존/경보와 공급망 SBOM/취약점 검사 증빙이
전체 Security Baseline 기준에 따라 판정되기 전에는 최종 기능 승인과
Build and Test 완료를 보고하지 않는다.

## 후속 설계 이관

- rate limiting key, 용량, 저장 방식과 제한 응답은 NFR
  Requirements 및 NFR Design으로 이관한다.
- TLS, DB 암호화, 중앙 관측 구성과 운영 증빙은 Infrastructure
  Design으로 이관한다.
- SBOM 및 취약점 검사 명령과 결과 판정은 NFR Design 및 Build and
  Test로 이관한다.

## PBT-01 판정

이 단위에는 현재 확정된 도메인 변환 또는 알고리즘이 없어 Functional
Design 시점의 testable property는 `N/A`다. NFR 단계에서 rate limiting
상태 모델이 정해지면 새 PBT 속성 식별 여부를 재평가한다.
