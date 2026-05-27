# UOW-SECURITY Infrastructure Design

<!-- markdownlint-disable MD060 -->

## 범위

이 문서는 Reaction 기능이 공유하는 Railway demo compute, H2
메모리 datasource, secret, 공개 ingress, 관측 및 공급망 책임을
매핑한다. 첫 배포는 영속 production이 아닌 기능 확인용 preview로,
데이터 초기화 위험을 명시적으로 허용한다.

## 인프라 결정

| 범주 | 결정 | 완료 조건 |
| ---- | ---- | --------- |
| Compute | in-memory limiter를 사용하는 demo는 단일 application replica 전제로 배포한다. | demo 배포 안내와 가능한 Railway 설정 확인 |
| Storage | 전용 demo profile의 H2 메모리 database를 사용한다. | 데이터 초기화 고지, H2 console 및 SQL 출력 비활성화 테스트 |
| Secret | `DEVICE_KEY_HMAC_SECRET`을 Railway secret/environment variable로 공급한다. | 저장소에 값이 없고 누락 시 기동 실패 테스트 |
| Monitoring | demo에서는 민감정보 없는 구조화 로그를 출력한다. | production 전환 전 중앙 수집, 90일 보존, 경보와 dashboard 증빙 |
| Networking | 기존 Railway 공개 ingress를 사용한다. | demo HTTPS 확인; production 전환 전 access logging/network 증빙 |
| Artifact integrity | 공식 Temurin 이미지를 승인 digest로 고정하고 Gradle verification을 추가한다. | Dockerfile, dependency locking/verification, SBOM 및 scan 결과 |

## 구성 변경 책임

- Code Generation에서 H2 메모리 datasource, H2 console/SQL 출력
  비활성화와 HMAC secret 필수 구성을 가진 전용 demo profile을
  추가한다.
- Code Generation에서 demo springdoc 차단, 보안 헤더,
  구조화 logging, safe error handler와 rate limiter를 구현한다.
- Dockerfile base image digest와 Gradle dependency
  locking/verification, CycloneDX 및 dependency scan task는 코드
  변경 단계에 포함한다.
- 영속 production 전환 시에는 storage encryption, ingress logging,
  중앙 관측과 replica 전략에 대한 새 증빙 검토가 필요하다.

## Security Baseline 판정

| 규칙 | 현재 Infrastructure Design 판정 | 다음 해소 조건 |
| ---- | -------------------------------- | ------------- |
| `SECURITY-01` | production 이월/알려진 demo 위험 | 영속 저장소 TLS 및 at-rest 암호화 증빙 제출 |
| `SECURITY-02` | production 이월 | Railway ingress access logging 증빙 또는 적용 불가 근거 |
| `SECURITY-03` | demo 코드 적용/운영 이월 | raw ID 비로그 및 구조화 로그 테스트; production 중앙 수집/보존 |
| `SECURITY-04` | demo 구현 대기 | headers 및 demo docs 차단 코드 테스트 |
| `SECURITY-05` | 구현 대기 | API validation 및 요청 크기 제한 테스트 |
| `SECURITY-06` | 판정 대기 | 배포 IAM 범위 증빙 또는 `N/A` 근거 |
| `SECURITY-07` | production 이월 | ingress/network boundary 증빙 또는 `N/A` 근거 |
| `SECURITY-08` | 구현 대기 | 공개 경계 및 자기 반응 거부 테스트 |
| `SECURITY-09` | demo 구현 대기 | H2 console/docs 비노출 및 안전 오류 테스트 |
| `SECURITY-10` | 구현 대기 | pinned digest, verification, SBOM 및 scan 결과 |
| `SECURITY-11` | demo 구현 대기 | 단일 instance 전제 기록 및 rate limiter 테스트 |
| `SECURITY-12` | 판정 대기 | 인증 기능 부재 확인 또는 해당 통제 증빙 |
| `SECURITY-13` | 구현/증빙 대기 | 유일성/PBT, 수동 SQL 감사 및 pipeline 변경 기록 |
| `SECURITY-14` | production 이월 | 90일 보존, 경보, dashboard 및 로그 무결성 증빙 |
| `SECURITY-15` | 구현 대기 | fail-closed 및 일반 오류 테스트 |

## 배포 승인 제약

- demo Code Generation은 입력 검증, raw ID 비로그, 안전 오류,
  rate limiter, 보안 헤더, H2 console/API docs 비노출, HMAC secret,
  PBT와 공급망 검증을 구현하는 조건으로 진행할 수 있다.
- demo는 저장 암호화와 장기 운영 관측 기준을 충족한 production
  출시가 아니며, 데이터 손실 가능성이 있는 preview로만 표시한다.
- 영속 production 전환 전에는 `SECURITY-01`, `02`, `03`, `07`,
  `14`의 운영 증빙과 확장 시 rate limiter 전략을 다시 승인받는다.
