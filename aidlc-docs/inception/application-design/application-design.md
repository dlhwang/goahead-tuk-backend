# 고해 반응 Application Design

<!-- markdownlint-disable MD060 -->

## 결정 요약

- `reaction`은 단일 Spring Boot 배포 단위 안의 독립 최상위 모듈로
  설계한다.
- 고해 하위 URI는 유지하되, 변이 처리는 Reaction API와 Reaction
  Application이 소유한다.
- 저장 모델은 기기별 선택 행이며, `Confession` 카운트 컬럼 없이
  활성 선택에서 집계를 파생한다.
- 공개 쓰기 API throttling은 애플리케이션 내부 공통 보호 구성 요소가
  담당한다.
- Railway/DB/중앙 로그/경보 같은 외부 운영 제어는 저장소 내 증빙
  체크리스트로 추적한다. 이번 범위에서 새 IaC는 도입하지 않는다.
- SBOM 및 취약점 검사는 명령과 결과 증빙 책임으로 둔다. 새 CI
  workflow 추가는 요구하지 않지만 증빙 부재는 보안 기준 실패다.

## 구성 요소 뷰

| 영역 | 구성 요소 | 핵심 책임 |
| ---- | --------- | --------- |
| 기능 | Reaction API/Application/Domain/Persistence | 멱등 선택과 해제, 별도 저장, 집계 제공 |
| 협력 | Confession Read Application | 집계 port를 사용한 고해 응답 조립 |
| 공통 보안 | Public API Protection/Application Observability | throttling, validation 경계, 안전한 로깅/오류/헤더 |
| 검증 증빙 | Operational Evidence/Supply Chain Evidence | 전체 Security Baseline 완료 판정 자료 |

## 주요 인터페이스

| 인터페이스 | 제공 기능 |
| ---------- | --------- |
| `SelectReactionUseCase` | 고해와 기기, 타입에 대한 멱등 선택 |
| `ClearReactionUseCase` | 고해와 기기, 타입에 대한 멱등 해제 |
| `ReactionSelectionRepository` | 선택 행 저장 및 삭제 |
| `ReactionAggregateReader` | 고해별 허용 타입 활성 수 조회 |
| `PublicWriteRateLimiter` | 공개 쓰기 요청 남용 제한 |
| `SecurityEvidenceVerifier` | 운영 및 공급망 증빙 판정 |

## 서비스 상호작용

### 반응 변경

1. 공통 HTTP 보호가 제한 정책과 입력 보호를 적용한다.
2. Reaction API가 경로 변수와 필수 `X-Device-Id`를 수신한다.
3. Reaction Application이 고해 존재와 허용 타입을 확인한다.
4. Reaction Persistence가 선택 또는 해제 상태를 멱등하게 반영한다.
5. 성공 시 본문 없는 `204 No Content`를 반환한다.

### 고해 읽기

1. Confession Read Application이 기존 고해 데이터를 읽는다.
2. Reaction Aggregate Port를 통해 선택 테이블에서 타입별 집계를
   얻는다.
3. 응답은 집계만 포함하고 개별 기기 식별자와 선택 내역은 포함하지
   않는다.

## 데이터 경계

- 저장 테이블은 최소한 선택 식별자, `confession_id`, `device_id`,
  `reaction_type`을 표현한다.
- `(confession_id, device_id, reaction_type)` 유일성 제약을 요구한다.
- 허용 타입은 `PRAY`, `COMFORT`, `TOGETHER`만이다.
- 기존 타입별 카운터 행과 증가 endpoint는 선택 집합 계약에 맞는
  구현으로 교체 대상이다.

## 보안 및 품질 경계

- 원문 `X-Device-Id`를 애플리케이션 로그에 남기지 않는다.
- API 입력 검증, 안전한 오류, 보안 헤더와 CORS 정책은 NFR 단계에서
  상세화하고 구현 검증 대상으로 삼는다.
- 선택/해제 상태는 PBT에서 멱등성, 유일성 및 집계 불변식의 대상이다.
- 전체 Security Baseline은 차단 조건으로 유지된다. 운영 설정 또는
  공급망 검사 증빙이 제출되지 않으면 관련 항목은 완료되지 않는다.

## 후속 단계 입력

- Units Generation은 Reaction 기능 단위와 공통 보안/증빙 단위의
  책임 및 순서를 구체화한다.
- Construction 단계는 기능 규칙, PBT 프레임워크와 생성기, rate-limit
  정책, 운영 증빙 체크리스트와 검증 명령을 확정한다.
- 코드 생성은 모든 설계 승인이 끝난 뒤 수행한다.
