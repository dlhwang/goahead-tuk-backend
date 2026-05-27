# UOW-SECURITY NFR Design 계획

<!-- markdownlint-disable MD053 MD060 -->

## 목표

승인된 Security Baseline 및 PBT 요구를 애플리케이션 통제, 운영 증빙,
공급망 검증 및 테스트 구조로 변환한다.

## 승인된 설계 입력

| 범위      | 확정 요구                                                         |
|---------|---------------------------------------------------------------|
| 남용 방지   | 설정 가능한 제한, 기본값은 파생 기기 key 기준 분당 `10`회 및 요청 출처 기준 분당 `60`회     |
| 오류 및 노출 | 제한 초과 `429`, 자기 반응 `403`, 원문 기기 식별 비로그/비노출                    |
| 운영 하드닝  | production OpenAPI 비활성화, 기본 보안 헤더와 HSTS 적용                    |
| 외부 증빙   | Railway/PostgreSQL 암호화, 중앙 로그, 90일 보존, 경보/대시보드 증빙             |
| 공급망     | CycloneDX SBOM, Dependency Check 또는 동등 검사, Trivy 또는 동등 이미지 검사 |
| 테스트     | Kotest Property Testing, 매 실행 seed 기록 및 shrink 실패 결과 보존       |

## 계획 체크리스트

- [x] Functional Design, 승인된 NFR Requirements 및 Security Baseline을
  검토한다.
- [x] 아래 NFR Design 질문의 답변을 분석한다.
- [x] `aidlc-docs/construction/uow-security/nfr-design/nfr-design-patterns.md`를
  생성한다.
- [x] `aidlc-docs/construction/uow-security/nfr-design/logical-components.md`를
  생성한다.
- [x] Security Baseline 항목별 구현 또는 외부 증빙 책임을 매핑한다.
- [x] PBT 상태 모델, 재현성 및 Build and Test 증빙 경계를 점검한다.
- [x] 생성 산출물에 대한 사용자의 명시적 승인을 요청한다.

## Question 1

단일 인스턴스 MVP에서 시작하는 throttling 상태 저장 방식은 무엇으로
정할까?

A) 만료와 최대 크기가 제한된 인메모리 저장소를 사용하고, 단일
인스턴스에서만 제한이 일관됨을 문서화하여 수평 확장 전 공유 저장소
전환을 필수 조건으로 남긴다.

B) MVP부터 PostgreSQL 기반 공유 제한 버킷을 사용해 여러
애플리케이션 인스턴스에서도 제한을 일관되게 적용한다.

C) MVP부터 Redis 호환 공유 저장소를 추가하여 제한 상태를 관리한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2

원문 `X-Device-Id`를 로그나 제한 상태에 남기지 않는 파생 기기 key는
어떻게 구성할까?

A) secret manager 또는 환경 변수로 공급하는 애플리케이션 비밀키와
HMAC을 사용하며, 원문과 파생 key 모두 로그에 기록하지 않는다.

B) 비밀키 없이 일방향 해시를 사용하고 파생 key는 디버깅 로그에서
허용한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 3

요청 추적과 Railway 중앙 로그 증빙을 위한 애플리케이션 관측 패턴은
어떻게 정할까?

A) servlet filter가 검증된 request ID를 전달하거나 생성하고 JSON
구조화 stdout 로그를 출력하며, 중앙 수집과 보존은 외부 증빙으로
확인한다.

B) 애플리케이션에 별도 외부 로그 agent 또는 exporter 통합을
추가하여 전송까지 코드 범위에서 구현한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 4

안전한 오류, 보안 헤더 및 production API 문서 차단은 어떤
구현 경계에 둘까?

A) Spring Security 헤더 구성, production property/profile의
springdoc 비활성화, 일반 내부 오류를 안전하게 변환하는 전역 handler로
구성한다.

B) 각 endpoint 또는 응답 interceptor에서 오류와 헤더, 문서 노출을
개별적으로 제어한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 5

공급망 증빙 작업을 저장소 구현 범위에 어떻게 포함할까?

A) Gradle plugin/task와 실행 명령을 추가해 SBOM 및 의존성 검사
산출물을 만들고, Docker image 생성 후 이미지 검사 명령과 결과를
Build and Test 증빙으로 기록한다.

B) 저장소에는 명령이나 plugin을 추가하지 않고 배포 운영
체크리스트에서만 수동 증빙을 생성한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 6

상태를 가지는 rate limiter가 도입될 경우 PBT 범위는 어떻게 정할까?

A) 용량 경계, 시간창 재설정 및 key 간 독립성을 결정적 모델과
비교하는 Kotest stateful property 테스트를 예제 테스트와 함께
구현한다.

B) rate limiter는 예제 기반 테스트만 작성하고 PBT는 반응 선택과
집계 도메인에만 적용한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A
