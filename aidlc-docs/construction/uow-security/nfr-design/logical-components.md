# UOW-SECURITY Logical Components

<!-- markdownlint-disable MD060 -->

## 애플리케이션 컴포넌트

| 컴포넌트 | 책임 | 제약 및 검증 |
| -------- | ---- | ------------ |
| `PublicWriteRateLimitFilter` | 공개 변이 요청에 기기 및 출처 제한 적용 | 설정 값 주입, 제한 초과 `429`, 안전한 오류 응답 |
| `DerivedDeviceKeyProvider` | HMAC 기반 파생 key 생성 | 비밀키 누락 시 fail-fast, 원문 및 key 비로그 |
| `BoundedRateLimitStore` | 만료되는 단일 인스턴스 버킷 상태 | 최대 크기, 주입 clock, 수평 확장 제한 문서화 |
| `RequestCorrelationFilter` | 검증된 request ID 전달 또는 생성 | 식별 입력을 로그 필드에 넣지 않는다. |
| `StructuredSecurityLogger` | JSON stdout 보안 이벤트 기록 | 제한, 안전한 오류와 경보 연계용 이벤트만 기록한다. |
| `SecurityConfig` | 응답 헤더와 production surface 하드닝 | HSTS, content type/frame/referrer 헤더 및 docs 차단 검증 |
| `GlobalSafeExceptionHandler` | 내부 실패의 일반 외부 응답 처리 | stack trace, 저장소 원인 및 식별자 비노출 |

## 검증 및 증빙 컴포넌트

| 컴포넌트 | 책임 | 산출물 |
| -------- | ---- | ------ |
| `RateLimiterPropertyTests` | 상태 모델 기반 limiter PBT | seed 및 shrink 결과가 포함된 테스트 출력 |
| `SecurityEndpointTests` | validation, headers, docs, 오류와 자기 반응 검증 | backend test 결과 |
| `SbomTask` | CycloneDX SBOM 생성 | SBOM 파일 및 실행 로그 |
| `DependencyVulnerabilityTask` | 의존성 취약점 검사 | Dependency Check 또는 승인 도구 보고서 |
| `ContainerImageScanCommand` | 생성 이미지 취약점 검사 | Trivy 또는 승인 도구 결과 |
| `OperationalEvidenceChecklist` | Railway 외부 설정 검토 | TLS, 암호화, 중앙 로그, 보존, 경보 및 적용성 판정 |

## 구현 경계

- 애플리케이션은 HMAC, 제한, 구조화 로그, 헤더, safe error와
  빌드 태스크를 구현한다.
- Infrastructure Design은 secret 공급, PostgreSQL TLS/암호화,
  단일 인스턴스 배포 제한 및 운영 증빙 확보 방법을 연결한다.
- 외부 증빙이 제출되지 않은 적용 보안 항목은 최종 승인 시 여전히
  차단 finding으로 남는다.
