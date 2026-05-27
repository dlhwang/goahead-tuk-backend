# UOW-SECURITY NFR Requirements 계획

<!-- markdownlint-disable MD053 MD060 -->

## 목표

전체 차단 Security Baseline, 공개 API 남용 보호, 운영/공급망 증빙,
그리고 PBT 프레임워크와 재현성 기준을 구현 및 검증 가능한 요구로
확정한다.

## 저장소에서 확인된 현재 증빙 상태

| 항목            | 현재 근거                                                     | 판정 방향         |
|---------------|-----------------------------------------------------------|---------------|
| `SECURITY-01` | Railway PostgreSQL URL에 TLS 강제 파라미터 또는 저장 암호화 증빙이 없음      | 증빙 또는 구성 필요   |
| `SECURITY-03` | 기존 고해 작성 controller가 `X-Device-Id` 원문을 기록하며 중앙 로그 증빙 없음   | 코드 보완 및 증빙 필요 |
| `SECURITY-04` | Spring Security 구성에 명시적 요구 헤더 설정 증빙 없음; OpenAPI UI 의존성 존재 | 노출 범위 결정 필요   |
| `SECURITY-05` | 공개 API의 포괄적 길이/형식/요청 크기 validation 근거 부족                  | 코드 요구 필요      |
| `SECURITY-10` | SBOM/취약점 검사 구성이 없고 Docker base image가 고정 digest가 아님       | 절차 및 증빙 필요    |
| `SECURITY-11` | 공개 API rate limiting 구성 없음                                | 정책 결정 필요      |
| `SECURITY-14` | 중앙 로그, 90일 보존, 경보/대시보드 증빙 없음                              | 외부 증빙 필요      |
| `SECURITY-15` | 전역 예외 처리 일부 존재하지만 안전한 일반 내부 오류 처리 근거 부족                   | 코드 요구 필요      |

## 사전 적용 판단

- `SECURITY-02`, `SECURITY-06`, `SECURITY-07`은 저장소에 load balancer,
  gateway, IAM 또는 네트워크 정책 정의가 없어 Railway 외부 구성
  증빙을 확인한 후 적용 또는 `N/A`로 판정한다.
- `SECURITY-08`은 API가 익명 공개 서비스임을 명시하되, 자기 고해
  반응 금지 및 개별 기기 비노출을 application-level control로
  검증한다.
- `SECURITY-12`는 회원 로그인 또는 관리자 인증 기능이 이번
  저장소/변경 범위에서 확인되지 않아, 후속 증빙 확인 뒤 `N/A`
  후보로 다룬다.
- 보안 규칙을 완화하는 선택지는 제공하지 않는다. 증빙되지 않은
  적용 항목은 차단 finding이다.

## NFR 범주 판단

- **Scalability/Performance**: 공개 API 제한 정책의 용량과 key를
  정해야 한다.
- **Availability/Reliability**: 안전한 오류, 중앙 관측, 경보와
  보존 증빙을 정해야 한다.
- **Security**: TLS, 민감 로그, 보안 헤더, 공급망 및 접근 제어의
  증빙 책임을 정해야 한다.
- **Tech Stack/Maintainability**: PBT 프레임워크와 SBOM/취약점 검사
  도구가 아직 선택되지 않았다.
- **Usability**: 사용자는 제한 또는 거부 시 안전하고 예측 가능한
  상태 코드를 받아야 한다.

## 계획 체크리스트

- [x] Security Baseline, 기능 설계 및 현재 구성 근거를 검토한다.
- [x] 아래 NFR 질문의 답변을 받고 모순 또는 모호성을 분석한다.
- [x] `aidlc-docs/construction/uow-security/nfr-requirements/nfr-requirements.md`를
  생성한다.
- [x] `aidlc-docs/construction/uow-security/nfr-requirements/tech-stack-decisions.md`를
  생성한다.
- [x] 각 Security Baseline 항목의 적용, 증빙 필요 또는 `N/A` 근거를
  추적한다.
- [x] `PBT-09` 프레임워크 선택 및 `PBT-08` 재현성 요구를 문서화한다.
- [x] 생성 산출물에 대한 사용자의 명시적 승인을 요청한다.

## Question 1

공개 쓰기 API의 애플리케이션 내부 throttling 기본 정책은 어떻게
정할까?

A) 원문을 저장하지 않는 파생 key 기준으로 기기당 분당 `30`회,
IP 또는 동등 요청 출처당 분당 `120`회를 허용하고 초과 시
`429 Too Many Requests`를 반환한다.

B) 원문을 저장하지 않는 파생 key 기준으로 기기당 분당 `10`회,
IP 또는 동등 요청 출처당 분당 `60`회를 허용하고 초과 시
`429 Too Many Requests`를 반환한다.

C) 정책 수치는 환경 설정으로 주입하되, 구현과 검증에는 보수적인
기본값인 기기당 분당 `10`회와 요청 출처당 분당 `60`회를 사용하고
초과 시 `429 Too Many Requests`를 반환한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: C

## Question 2

자기 고해 반응 금지처럼 요청은 유효하지만 정책상 허용되지 않는
반응 변경의 외부 오류 코드는 어떻게 둘까?

A) `403 Forbidden`으로 반환하고, 식별 비교 세부 정보는 응답에
드러내지 않는다.

B) 자원 존재나 작성자 관계 노출을 줄이기 위해 `404 Not Found`로
반환한다.

C) 지원되지 않는 작업 입력으로 보아 `400 Bad Request`로 반환한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## 답변 분석 결과

- `Q1=C`: 공개 쓰기 제한 값은 환경 설정으로 주입하고 기본값은
  기기 파생 key당 분당 `10`회, 요청 출처당 분당 `60`회이며 초과
  응답은 `429 Too Many Requests`다.
- `Q2=A`: 자기 고해 반응 금지는 식별 관계 세부를 숨긴
  `403 Forbidden`으로 응답한다.
- `Q3=A`: production에서는 OpenAPI UI와 API docs endpoint를
  비활성화하며 REST API에도 기본 보안 헤더와 HSTS를 적용한다.
- `Q4=A`: 외부 운영 설정은 화면 또는 내보낸 설정 등 검증 가능한
  증빙을 요구한다.
- `Q5=A`: CycloneDX SBOM, OWASP Dependency Check 계열 의존성 검사,
  Trivy 계열 이미지 검사를 기본 공급망 증빙 절차로 정한다.
- `Q6=A`, `Q7=A`: `Kotest Property Testing`을 선정하고 모든 backend
  test 실행에 포함하며, 실패 seed와 축소 입력 및 CI 실행 seed를
  기록한다.
- 답변은 전체 차단 Security Baseline 및 PBT 규칙을 완화하지 않으며
  후속 구현과 증빙 완료 전까지 열린 의무로 유지한다.

## Question 3

Production의 OpenAPI UI 또는 기타 HTML 제공 경로는 어떻게 보안
기준에 반영할까?

A) production에서는 OpenAPI UI 및 API docs endpoint를 비활성화하고,
REST API 응답에도 기본 보안 헤더와 HSTS를 적용한다.

B) production에서도 OpenAPI UI를 제공하며, 모든 HTML 경로에
`SECURITY-04`의 CSP, HSTS, nosniff, frame 및 referrer header를
적용하고 검증한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 4

Railway와 PostgreSQL처럼 저장소 밖에서 관리되는 암호화 및 관측
설정의 증빙은 어떤 방식으로 완료 조건에 포함할까?

A) 배포 전 체크리스트에 PostgreSQL TLS 강제 연결, 저장 암호화,
중앙 로그 수집, 최소 90일 보존, 보안 경보 및 대시보드 설정의
스크린샷 또는 내보낸 설정 증빙을 요구한다.

B) 위 증빙을 Railway 운영 책임자의 서면 확인 기록으로 수집하고,
실제 설정 내보내기나 화면 증빙은 요구하지 않는다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 5

이번 저장소에서 실행 증빙으로 사용할 공급망 보안 도구 조합은
어떻게 정할까?

A) Gradle `org.cyclonedx.bom`으로 SBOM을 생성하고, OWASP Dependency
Check 또는 동등한 의존성 취약점 검사를 실행하며, Trivy 또는 동등한
도구로 Docker image 취약점을 검사하는 절차를 문서화한다.

B) CycloneDX SBOM 생성과 Gradle 의존성 보고만 수행하고, 취약점 및
image 검사는 외부 배포 플랫폼 증빙으로만 확인한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 6

Kotlin Reaction 상태 속성 검증을 위한 PBT 프레임워크는 무엇으로
선정할까?

A) `Kotest Property Testing`을 도입한다. Kotlin 친화적인 generator,
shrinking, seed 재현성과 JUnit 실행 연동을 사용한다.

B) `jqwik`을 도입한다. JUnit Platform 기반 property 및 stateful
testing 도구를 사용하고 Kotlin 테스트에서 호출한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 7

PBT의 CI 실행과 재현성 기준은 어떻게 둘까?

A) 모든 backend test 실행에 PBT를 포함하고 실패 시 seed와 축소된
입력을 기록한다. CI에서는 매 실행 seed를 출력해 재실행 가능하게
한다.

B) 모든 backend test 실행에 PBT를 포함하고 고정 seed를 사용한다.
새 무작위 seed 탐색은 수동 실행으로 분리한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A
