# UOW-SECURITY Code Generation 계획

<!-- markdownlint-disable MD060 -->

## 단위 맥락

- **지원 스토리**: `US-2`, `US-3`의 공개 Reaction 변경 및 조회를
  안전하게 전달한다.
- **책임**: demo 구현 게이트인 입력 검증, raw ID 비로그, 안전 오류,
  throttling, 보안 헤더, H2 console/API docs 비노출, HMAC secret,
  PBT와 공급망 검증.
- **이월 범위**: 영속 production 저장 암호화, 장기 로그 보존,
  access logging, 경보/dashboard 증빙.

## 현재 확인된 변경 지점

| 현재 파일 또는 구성 | 변경 목적 |
| ------------------- | --------- |
| `src/main/kotlin/io/goahead/tuk/confession/api/ConfessionController.kt` | raw `X-Device-Id` 로그 제거와 validated header 사용 |
| `src/main/kotlin/io/goahead/tuk/confession/api/response/ConfessionResponse.kt` | device-backed 작성자 식별의 공개 응답 노출 제거 |
| `src/main/kotlin/io/goahead/tuk/common/config/SecurityConfig.kt` | 보안 헤더, CORS header 제한 및 공개 API 보호 filter 연결 |
| `src/main/kotlin/io/goahead/tuk/common/api/ApiExceptionHandler.kt` | 내부 정보 비노출 오류, `403`/`429` 응답 경계 |
| `src/main/resources/application-local.yml` | 로컬 설정은 유지하되 공개 demo에서 재사용하지 않음 |
| 새 `src/main/resources/application-demo.yml` | H2 memory, console/SQL/docs 비활성 demo profile |
| `build.gradle.kts`, `Dockerfile` | PBT, SBOM/취약점 검사, dependency verification 준비와 image digest 고정 |

## 생성 및 수정 경로

- 공통 보호 클래스는 `src/main/kotlin/io/goahead/tuk/common/security/`
  또는 기존 `common/config` 관례에 맞는 하위 경계에 추가한다.
- demo 설정은 `src/main/resources/application-demo.yml`에 둔다.
- 보안 및 limiter 테스트는 `src/test/kotlin/io/goahead/tuk/common/`
  아래 추가한다.
- 구현 요약은
  `aidlc-docs/construction/uow-security/code/code-summary.md`에
  작성한다.

## 실행 단계

- [ ] **Step 1 - Input and Privacy Boundary**:
  device ID, confession ID와 Reaction type의 명시적 길이/형식 검증을
  적용하고 raw device ID 로그 및 공개 응답의 device-backed 작성자
  식별을 제거한다. HMAC 파생 key provider는
  `DEVICE_KEY_HMAC_SECRET` 누락 시 안전하게 기동 실패해야 한다.
- [ ] **Step 2 - Abuse Protection**:
  설정 가능한 bounded in-memory rate limiter와 filter를 구현하여
  공개 쓰기 요청에 파생 device key 분당 `10`회, 요청 출처 분당
  `60`회 기본값 및 `429` 응답을 적용한다.
- [ ] **Step 3 - Safe Errors and Headers**:
  자기 반응 `403`, 입력 오류 `400`, 제한 `429`, 내부 오류의 일반
  응답을 구현하고 Spring Security에서 HSTS, nosniff, frame 거부와
  referrer 보호를 구성한다. CORS header wildcard도 필요한 header로
  제한한다.
- [ ] **Step 4 - Demo Deployment Profile**:
  `application-demo.yml`을 생성해 H2 메모리 database를 사용하면서
  H2 console, SQL 출력과 springdoc endpoint를 비활성화하고 demo
  데이터 초기화 제약을 코드 요약에 기록한다.
- [ ] **Step 5 - Security Tests and Limiter PBT**:
  보안 header, docs/console 비노출, raw ID 비로그 가능한 경계,
  안전 오류, rate-limit 용량/시간창/key 독립성의 Kotest stateful
  property 테스트와 seed 재현 구성을 추가한다.
- [ ] **Step 6 - Supply-Chain Configuration**:
  Kotest 및 CycloneDX/의존성 취약점 검사 구성을 Gradle에 추가하고,
  dependency locking/verification 적용 범위를 설정한다. 공식
  Temurin base image는 검증 가능한 digest로 고정하며, 이미지
  취약점 검사 명령과 SBOM 결과 위치를 문서화한다.
- [ ] **Step 7 - Demo Deployment Notes and Code Summary**:
  Railway에서 활성화할 demo profile과 HMAC secret 설정, 단일
  instance 전제, 데이터 유실 표시, production 이월 보안 항목 및
  생성된 검증 산출물을 코드 요약 문서에 기록한다.

## 조정 및 실행 순서

- 이 단위의 Step 1부터 Step 4는 Reaction endpoint를 외부에
  노출하기 전에 함께 적용한다.
- `UOW-REACTION`과 조회 통합 테스트 뒤 이 단위의 공통 보호 및 PBT,
  공급망 검증을 포함해 Build and Test로 넘긴다.
- 검증 가능한 base image digest 또는 vulnerability scan 실행에
  네트워크 접근이 필요하면 해당 단계에서 승인을 요청한다.

## 완료 기준

- demo용 코드 수준 보호가 구현·테스트되어야 Code Generation 완료로
  보고한다.
- production으로 이월된 운영 증빙은 demo 완료로 닫지 않으며
  코드 요약과 Build and Test 보고에 잔여 위험으로 명시한다.
