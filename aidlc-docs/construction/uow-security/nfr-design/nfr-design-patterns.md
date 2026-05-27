# UOW-SECURITY NFR Design Patterns

<!-- markdownlint-disable MD060 -->

## 범위

`UOW-SECURITY`는 공개 API 공통 통제, 운영 증빙, 공급망 검증과
상태 기반 보안 테스트 패턴을 소유한다.

Railway 첫 배포는 이후 승인된 변경에 따라 H2 메모리 database를
사용하는 일회성 demo preview다. 아래 운영 증빙 패턴 가운데 저장소
암호화와 장기 관측 증빙은 영속 production 전환 전에 적용한다.

## 애플리케이션 통제 패턴

| 관심사 | 선택 패턴 | 구현 및 증빙 |
| ------ | --------- | ------------ |
| Throttling | 제한된 크기와 만료를 갖는 인메모리 버킷 | 기본 제한은 파생 기기 key 분당 `10`회, 요청 출처 분당 `60`회이며 설정 주입을 지원한다. 단일 인스턴스에서만 일관됨을 명시한다. |
| 민감 식별 보호 | 비밀키 기반 HMAC 파생 key | 비밀키는 환경 변수 또는 secret manager로 공급하며 raw ID와 파생 key를 모두 로그에서 배제한다. |
| 요청 추적 | filter 기반 request ID 및 JSON stdout 로그 | 허용된 request ID만 전달하고 나머지는 생성한다. Railway 중앙 수집, 보존과 경보는 외부 증빙으로 닫는다. |
| 안전한 응답 | Spring Security 설정과 전역 예외 handler | 보안 헤더, HSTS, production springdoc 차단, 일반 내부 오류 응답을 제공한다. |
| 공급망 | Gradle 생성/검사 task 및 이미지 검사 명령 | CycloneDX, Dependency Check와 Docker build 후 Trivy 결과를 Build and Test 증빙으로 기록한다. |

## Throttling 제약

- 인메모리 limiter는 MVP 단일 인스턴스 전제에서만 올바른 전체
  제한량을 보장한다.
- 수평 확장 전 PostgreSQL 또는 Redis 호환 공유 상태 저장소 설계를
  승인받아 교체해야 한다. 이 제한은 배포 증빙에 기록한다.
- limiter는 테스트 가능한 clock을 주입받아 시간창 경계를 결정적으로
  검증할 수 있어야 한다.
- 제한 응답은 `429 Too Many Requests`만 노출하고 내부 key 또는
  버킷 상태를 포함하지 않는다.

## Security Baseline 책임 매핑

| 규칙 | NFR Design 책임 | 이후 완료 증빙 |
| ---- | --------------- | -------------- |
| `SECURITY-01` | 영속 production 저장소 설계로 이월 | production Infrastructure Design의 TLS/저장 암호화 증빙 |
| `SECURITY-02`, `SECURITY-06`, `SECURITY-07`, `SECURITY-12` | 배포 범위 조사 및 적용 여부 판정 | 외부 설정 증빙 또는 근거 있는 `N/A` |
| `SECURITY-03`, `SECURITY-14` | demo에서 HMAC 보호, 구조화 로그, 상관 ID 구현 | production 전환 시 중앙 로그/90일 보존/경보 증빙 |
| `SECURITY-04`, `SECURITY-09`, `SECURITY-15` | 보안 헤더, docs 차단, 안전한 오류 | 구성 및 endpoint 테스트 |
| `SECURITY-05`, `SECURITY-08`, `SECURITY-11` | validation, 자기 반응 거부, throttling | API와 abuse 테스트 |
| `SECURITY-10`, `SECURITY-13` | 공급망 task와 PBT/무결성 검증 | SBOM 및 스캔 결과, 테스트 출력 |

## 공급망 및 PBT 패턴

- Gradle에 CycloneDX SBOM 및 의존성 취약점 검사 실행 경계를
  추가하고 결과 위치를 Build and Test 문서에 기록한다.
- 컨테이너 이미지 생성 뒤 Trivy 또는 승인된 동등 도구를 실행한다.
- Docker base image 고정 방식과 dependency pinning/verification은
  Code Generation 전에 Infrastructure Design에서 확정한다.
- limiter의 capacity, 시간창 재설정, key 독립성을 결정적 모델과
  비교하는 Kotest stateful property 테스트를 구현한다.
- 모든 PBT 실행은 seed를 출력하고 실패 시 축소 입력을 보존한다.
