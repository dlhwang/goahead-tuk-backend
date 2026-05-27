# UOW-SECURITY NFR Requirements

<!-- markdownlint-disable MD060 -->

## 범위

`UOW-SECURITY`는 공개 API 공통 보호, 민감 로그와 안전한 오류 처리,
production hardening, 외부 운영 증빙 및 공급망/PBT 검증 정책을
소유한다. 이 요구는 전체 차단 Security Baseline을 만족하기 위한
완료 조건이며, 문서화만으로 런타임 준수를 의미하지 않는다.

## Demo 배포 범위 보정

- Railway 최초 배포는 H2 메모리 저장소를 사용하는 일회성 demo
  preview로 재분류되었으며, 데이터 지속성을 보장하지 않는다.
- demo Code Generation의 차단 요구는 입력 검증, 민감 로그 제거,
  안전 오류, throttling, H2 console/API docs 비노출, 보안 헤더,
  PBT와 공급망 검증이다.
- TLS 기반 관리형 저장소, 중앙 로그 장기 보존, 경보와 dashboard
  증빙은 영속 production 전환 전 차단 조건으로 이월한다.

## 공개 API 보호

- Reaction 선택 및 해제를 포함한 공개 쓰기 API에 내부 throttling을
  적용한다. (`SECURITY-11`)
- 정책 값은 환경 설정으로 주입할 수 있어야 하며, 기본값과 검증
  기준은 원문을 저장하지 않는 기기 파생 key당 분당 `10`회,
  요청 출처당 분당 `60`회다.
- 제한 초과는 `429 Too Many Requests`로 반환하며 내부 key 또는
  식별 정보를 노출하지 않는다.
- 자기 고해 반응 금지는 `403 Forbidden`으로 반환하되 작성자 관계
  판단 내용을 노출하지 않는다. (`SECURITY-08`)

## 애플리케이션 보안

- 영향받는 API 입력은 형식, 길이, enum allowlist와 요청 크기 제한을
  명시적으로 적용해야 한다. (`SECURITY-05`)
- 원문 `X-Device-Id`를 로그에 기록하지 않으며, 요청/상관 ID와
  수준을 갖춘 구조화 로그를 중앙 수집에 연결해야 한다.
  (`SECURITY-03`)
- 전역 예외 처리기는 내부 실패와 집계 실패를 일반적이고 안전한
  외부 오류로 처리해야 한다. (`SECURITY-09`, `SECURITY-15`)
- production에서는 OpenAPI UI 및 API docs endpoint를 비활성화한다.
- REST API 응답에도 HSTS, `nosniff`, frame 제한과 안전한 referrer
  정책 등 적용 가능한 보안 헤더를 설정한다. HTML endpoint가 있다면
  CSP 요구도 충족해야 한다. (`SECURITY-04`)

## 운영 및 인프라 증빙

- 영속 production 전환 시 PostgreSQL 또는 선정된 저장소 연결은
  TLS 1.2 이상을 강제하고 저장 암호화가 활성화된 증빙을 제출해야
  한다. (`SECURITY-01`)
- 중앙 로그 수집, 최소 90일 보존, 보안 경보 및 모니터링 대시보드의
  화면 또는 내보낸 설정 증빙을 제출해야 한다. (`SECURITY-03`,
  `SECURITY-14`)
- 외부 network intermediary, IAM 및 network policy가 존재하는지
  확인해 `SECURITY-02`, `SECURITY-06`, `SECURITY-07`을 증빙하거나
  적용 대상 없음의 근거를 기록한다.
- 회원 또는 관리자 인증 경로가 배포 범위에 없다면 `SECURITY-12`는
  근거가 있는 `N/A`로 판정하고, 있다면 해당 기준을 충족해야 한다.

## 공급망 보안

- 생산 배포를 위한 SBOM은 CycloneDX 형식으로 생성한다.
- 의존성 취약점 검사는 OWASP Dependency Check를 기본 도구로 하고,
  컨테이너 이미지 취약점 검사는 Trivy를 기본 도구로 사용한다.
- 동등 도구로 변경하려면 검증 범위와 결과 형식이 동일함을 문서화해
  승인받아야 한다.
- dependency version pinning/verification 및 Docker base image
  고정 방식을 NFR Design과 Code Generation에서 구체화한다.
  (`SECURITY-10`, `SECURITY-13`)

## Property-Based Testing

- Kotlin PBT framework는 `Kotest Property Testing`으로 선정한다.
  (`PBT-09`)
- Reaction과 조회 통합의 PBT는 모든 backend test 실행과 CI에
  포함한다.
- 실패 시 seed와 축소된 최소 입력을 기록하며, CI는 매 실행 seed를
  출력해 재현할 수 있어야 한다. (`PBT-08`)
- `UOW-SECURITY`의 rate limiting 상태 모델이 NFR Design에서
  구체화되면 추가 stateful PBT 속성을 재평가한다.

## Security Baseline 추적표

| 규칙 | NFR 요구 판정 | 완료에 필요한 후속 증빙 |
| ---- | ------------- | ------------------------ |
| `SECURITY-01` | production 이월 | 영속 저장소 TLS 강제 및 저장 암호화 설정 증빙 |
| `SECURITY-02` | 판정 대기 | 외부 intermediary 존재 조사 및 로그 증빙 또는 `N/A` |
| `SECURITY-03` | demo 코드 적용/운영 이월 | 원문 ID 로그 제거; production 중앙 로그와 보존 증빙 |
| `SECURITY-04` | 적용 | API docs 비활성화 및 보안 헤더 검증 |
| `SECURITY-05` | 적용 | API validation 및 요청 크기 제한 테스트 |
| `SECURITY-06` | 판정 대기 | IAM 존재 조사 및 최소 권한 증빙 또는 `N/A` |
| `SECURITY-07` | production 이월 | network 설정 조사 및 제한 증빙 또는 `N/A` |
| `SECURITY-08` | 적용 | 공개 endpoint 선언, 자기 반응 거부, 비노출 테스트 |
| `SECURITY-09` | 적용 | production 노출 surface와 안전한 오류 검증 |
| `SECURITY-10` | 적용 | SBOM, Dependency Check, Trivy, 고정/검증 결과 |
| `SECURITY-11` | 적용 | rate-limit 구성 및 abuse test |
| `SECURITY-12` | 판정 대기 | 인증 범위 조사 후 준수 또는 `N/A` 근거 |
| `SECURITY-13` | 적용 | 무결성/PBT/파이프라인 변경 추적 증빙 |
| `SECURITY-14` | production 이월 | 90일 로그 보존, 경보와 대시보드 증빙 |
| `SECURITY-15` | 적용 | 전역 오류 및 외부 실패 fail-closed 테스트 |

## 완료 게이트

- NFR Requirements는 적용 범위와 필요한 증빙을 확정한다.
- runtime 구현, 운영 설정 및 공급망 실행 결과가 아직 없으므로 해당
  준수 판정은 NFR Design, Infrastructure Design, Code Generation 및
  Build and Test에서 닫아야 한다.
- 위 적용 항목 가운데 증빙이 없는 항목은 최종 Build and Test 승인
  시점에 차단 finding이다.
