# UOW-SECURITY Infrastructure Design 계획

<!-- markdownlint-disable MD053 MD060 -->

## 후속 Demo 범위 변경

기존 답변은 Railway PostgreSQL production 전제에서 수집되었으나,
이후 사용자는 H2 메모리 database 기반 demo preview와 production
운영 증빙 이월을 승인했다. 아래 생성 산출물은 demo 코드 보호와
향후 production 차단 게이트를 구분해 반영한다.

## 목표

선택된 애플리케이션 보안 패턴을 Railway 배포, PostgreSQL 연결,
비밀 관리, 운영 관측 및 공급망 증빙의 실제 인프라 결정에 매핑한다.

## 확인된 현재 구성과 보안 격차

| 범주                        | 확인된 상태와 결정 필요 사항                                                         |
|---------------------------|--------------------------------------------------------------------------|
| Deployment Environment    | `railway.json`은 Docker build와 healthcheck만 정의하며 production 증빙 범위가 미확정이다. |
| Compute Infrastructure    | in-memory rate limiter는 단일 instance에서만 일관되므로 replica 제한 또는 설계 변경이 필요하다.  |
| Storage Infrastructure    | PostgreSQL JDBC URL에는 TLS 강제 파라미터가 보이지 않고 저장 암호화 증빙이 없다.                 |
| Messaging Infrastructure  | 현재 보안 패턴은 동기 filter/로그 기반이며 메시징 컴포넌트 요구가 없어 `N/A`다.                      |
| Networking Infrastructure | 공개 ingress의 TLS 종료, intermediary access logging 및 네트워크 제한 증빙이 없다.        |
| Monitoring Infrastructure | 중앙 로그, 90일 보존, 경보와 dashboard 증빙이 없다.                                     |
| Shared Infrastructure     | 비밀키, logging, headers 및 공급망 검증은 서비스 전체가 공유한다.                            |
| Supply Chain              | Docker base image가 digest로 고정되지 않았고 SBOM/취약점 검사 구성이 없다.                  |

## 계획 체크리스트

- [x] Functional Design, NFR Design, Security Baseline 및 현재 배포 구성을
  검토한다.
- [x] 아래 Infrastructure Design 질문의 답변을 분석한다.
- [x] `aidlc-docs/construction/uow-security/infrastructure-design/infrastructure-design.md`를
  생성한다.
- [x] `aidlc-docs/construction/uow-security/infrastructure-design/deployment-architecture.md`를
  생성한다.
- [x] 필요 시 `aidlc-docs/construction/shared-infrastructure.md`를
  생성한다.
- [x] Security Baseline 항목별 해결 설계 또는 차단 증빙 격차를
  매핑한다.
- [x] 생성 산출물에 대한 사용자의 명시적 승인을 요청한다.

## Question 1

인메모리 throttling을 사용하는 MVP의 Railway compute 배포 경계는
어떻게 정할까?

A) MVP production 배포를 단일 application replica로 제한하고 이를
배포 증빙으로 확인하며, 수평 확장 전에 공유 rate-limit 저장소로
변경한다.

B) MVP 구현 단계에서 PostgreSQL 기반 공유 rate-limit 저장소로
설계를 변경하여 여러 replica 배포도 허용한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2

Railway PostgreSQL의 전송 및 저장 암호화 요구를 어떤 증빙 경계로
확정할까?

A) JDBC 연결에 인증서를 검증하는 TLS 구성을 적용하고, Railway가
제공하는 PostgreSQL 저장 암호화 및 연결 인증서 정보를 증빙으로
보관한다.

B) JDBC 연결에는 암호화 전송을 요구하되 인증서 검증 수준은 플랫폼
기본값에 맡기고, Railway의 저장 암호화와 TLS 보장 문서를 증빙으로
보관한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 3

HMAC 파생 key를 위한 application secret은 어떤 배포 구성을 사용할까?

A) Railway secret/environment variable로 `DEVICE_KEY_HMAC_SECRET`을
주입하고 저장소 파일에는 값을 기록하지 않으며, 누락 시 시작을
실패시킨다.

B) 별도 외부 secret manager 연동을 MVP에 추가하고 Railway에는
접근 자격만 공급한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 4

중앙 로그, 최소 90일 보존, 보안 경보 및 dashboard 증빙은 어떤
운영 인프라를 기준으로 할까?

A) Railway 또는 연동 가능한 승인 관측 기능으로 먼저 검증하고,
90일 보존·변경 불가성·경보 요구를 충족하지 못하면 외부 중앙
로그/모니터링 sink를 추가하기 전까지 production 승인을 차단한다.

B) MVP부터 외부 중앙 로그 및 모니터링 서비스 연동을 배포 구성에
포함하고 그 서비스의 보존·경보 증빙을 제출한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 5

공개 ingress, network intermediary 및 access logging 범위는 어떻게
설계할까?

A) 별도 gateway/CDN은 추가하지 않고 Railway 공개 ingress의 HTTPS,
접근 로그 및 네트워크 경계를 증빙하여 적용 가능한 Security Baseline
항목을 판정한다.

B) MVP부터 별도 API gateway 또는 reverse proxy 계층을 추가하고
해당 계층에서 TLS와 access logging을 직접 구성한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 6

production artifact 무결성과 Docker base image 고정은 어떤 방식으로
구현할까?

A) 공식 Temurin base image의 승인된 digest로 Dockerfile을
고정하고, Gradle dependency locking/verification과
SBOM/취약점 검사 task를 함께 추가한다.

B) 조직이 제공하는 private registry의 승인 digest 이미지를
사용하고, 동일하게 Gradle dependency locking/verification과
SBOM/취약점 검사 task를 추가한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A
