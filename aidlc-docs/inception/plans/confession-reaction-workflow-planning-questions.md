# 고해 반응 MVP Workflow Planning 확인 질문

<!-- markdownlint-disable MD053 MD060 -->

## 확인 배경

요구사항 단계에서 `Security Baseline`을 모든 차단 조건으로 적용하도록
선택했다. 활성화된 규칙을 현재 저장소와 대조한 결과, Reaction 기능
구현 자체에 필요한 보완 외에도 기존 애플리케이션 또는 배포/운영
증빙에서 다음 차단 finding이 확인되었다.

| 규칙            | 현재 확인된 차단 finding                           |
|---------------|---------------------------------------------|
| `SECURITY-03` | 기존 고해 작성 API가 `X-Device-Id` 원문을 로그에 기록한다.   |
| `SECURITY-05` | 현재 API 입력에 일관된 길이/형식 검증 근거가 없다.             |
| `SECURITY-10` | 저장소에서 취약점 스캔 또는 SBOM 생성 구성을 확인할 수 없다.       |
| `SECURITY-11` | 공개 API에 rate limiting 또는 throttling 구성이 없다. |
| `SECURITY-14` | 보안 경보, 로그 보존, 대시보드 구성 증빙을 확인할 수 없다.         |

또한 `SECURITY-01`의 Railway PostgreSQL 전송 및 저장 암호화,
`SECURITY-03`의 중앙 로그 전송은 저장소 파일만으로 준수 여부를
증명할 수 없다.

Reaction 작업과 직접 연결되는 `SECURITY-03`, `SECURITY-05`,
`SECURITY-11`의 애플리케이션 변경은 실행 계획에 포함할 수 있다.
반면 배포 및 운영 증빙, 공급망 구성까지 모두 차단으로 유지하면 이번
기능 전달 범위가 전사적 보안 기준 보완 작업으로 확대된다.

## Question 1

이번 Reaction 기능 작업에서 Security Baseline 차단 범위를 어떻게
정할까?

A) 선택한 전체 Security Baseline을 그대로 차단 조건으로 유지한다.
Reaction 구현과 함께 기존 API 보안 보완, 공급망/SBOM, 배포 암호화
증빙, 중앙 로그 및 모니터링/경보 증빙까지 모두 해결한 뒤에만
코드 생성을 완료한다.

B) 이번 기능 전달에 직접 적용 가능한 보안 규칙만 차단 조건으로
한정한다. Reaction 및 관련 익명 API의 입력 검증, 원문 기기 ID 로그
제거, 집계 비노출, 안전한 오류, CORS 검토, rate limiting을 구현하고,
배포/운영 및 공급망 수준 항목은 후속 보안 작업 후보로 기록한다.

C) 이번 Reaction 실행에서는 Security Baseline 확장을 비활성화하고,
승인된 기능 요구사항에 포함된 기본 입력 검증과 비노출만 일반 품질
기준으로 구현한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A
