# UOW-SECURITY Tech Stack Decisions

## 런타임 보호 결정

- 공개 쓰기 throttling은 Spring Boot 애플리케이션 내부 공통 보호
  구성 요소로 구현한다.
- 제한 수치는 환경 설정으로 주입하고, 검증 기본값은 기기 파생
  key당 분당 `10`회와 요청 출처당 분당 `60`회다.
- 초과 응답은 `429 Too Many Requests`, 자기 고해 반응 거부는
  `403 Forbidden`이다.
- production OpenAPI UI와 API docs endpoint는 비활성화한다.

## 운영 증빙 결정

- Railway/PostgreSQL 암호화 및 관측 설정은 화면 또는 내보낸 설정과
  검토 체크리스트로 증빙한다.
- 요구 증빙은 TLS 강제, 저장 암호화, 중앙 로그 수집, 최소 90일
  보존, 보안 경보 및 모니터링 대시보드를 포함한다.
- IaC 도입은 이 결정에 포함하지 않지만 증빙을 생략할 수는 없다.

## 공급망 기술 결정

- SBOM 기본 도구: Gradle `org.cyclonedx.bom`.
- 의존성 취약점 검사 기본 도구: OWASP Dependency Check.
- 컨테이너 이미지 취약점 검사 기본 도구: Trivy.
- production 이미지의 base image와 CI/CD 도구 버전 고정 방식은
  NFR Design 및 Code Generation에서 확정한다.

## PBT 기술 결정

- 프레임워크: `Kotest Property Testing`. (`PBT-09`)
- 요구 역량: domain-specific generators, shrinking, seed 기반
  재실행 및 기존 test runner 통합.
- 실행 정책: backend test 및 CI 실행에 PBT를 포함하고 매 실행 seed를
  출력하며 실패 시 축소 입력을 남긴다. (`PBT-08`)
- 대상: Reaction 멱등성/상태 불변식, 집계 모델 비교와 타입 완전성.
- rate limiting 상태 속성은 후속 NFR Design에서 제어 모델이
  확정되면 추가 판단한다.
