# 고해 반응 Application Components

<!-- markdownlint-disable MD060 -->

## 설계 범위

이 설계는 익명 열람자의 고해 반응 선택과 해제, 타입별 집계 조회,
그리고 활성화된 전체 Security Baseline의 책임 경계를 다룬다. 백엔드는
하나의 Spring Boot 배포 단위로 유지하며, `reaction`은 코드와 도메인
책임을 분리하는 독립 모듈이다.

## 구성 요소 목록

| 구성 요소 | 목적 | 주요 책임 |
| ---------- | ---- | --------- |
| Reaction API | 반응 변이 HTTP 경계 | `PUT`/`DELETE` 경로, `X-Device-Id`, 타입과 식별자 입력 수신, `204` 응답 |
| Reaction Application | 반응 사용 사례 오케스트레이션 | 고해 존재 확인, 선택/해제 요청 조정, 저장 포트 호출 |
| Reaction Domain | 반응 상태 계약 | `PRAY`, `COMFORT`, `TOGETHER`, 기기별 선택 식별, 집계 조회 포트 |
| Reaction Persistence | 별도 반응 선택 저장 | 선택 행 저장/삭제, 유일성 제약, 타입별 집계 질의 |
| Confession Read Application | 고해 조회 결과 조립 | Reaction 집계 조회와 협력하여 단건/목록 응답에 집계를 포함 |
| Public API Protection | 공개 API 공통 보호 | 입력 제한, 쓰기 요청 rate limiting, 안전한 HTTP 오류와 보안 헤더 경계 |
| Application Observability | 애플리케이션 보안 로그 경계 | 원문 기기 ID 비기록, 구조화 이벤트와 안전한 예외 로깅 요구 |
| Operational Evidence | 외부 운영 증빙 경계 | Railway/DB TLS와 저장 암호화, 중앙 로그, 보존, 경보, 대시보드 체크리스트 |
| Supply Chain Evidence | 공급망 증빙 경계 | SBOM 및 취약점 검사 명령/결과 기록, 의존성 출처와 이미지 검사 증빙 |

## Reaction 구성 요소

### Reaction API

- URI는 고해 하위 리소스 형태
  `/api/confessions/{confessionId}/reactions/{type}`를 유지하지만,
  구현 패키지는 독립 `reaction/api` 경계를 사용한다.
- 요청 본문을 받지 않으며 자유 텍스트나 댓글 경로를 제공하지 않는다.
- 기기 식별자는 선택/해제 입력으로만 전달하며 응답 또는 로그 원문에
  포함하지 않는다.

### Reaction Application

- 선택과 해제를 별도 멱등 명령으로 제공한다.
- 대상 고해 존재 여부는 기존 Confession domain port를 통해 확인한다.
- 읽기 측에서는 Confession 조회 흐름이 Reaction 집계 포트를 호출하도록
  지원한다.

### Reaction Domain 및 Persistence

- 허용 타입은 `PRAY`, `COMFORT`, `TOGETHER`로 제한한다.
- 저장 모델은 `(confession_id, device_id, reaction_type)` 선택 행을
  표현하며 해당 조합의 유일성을 영속성 제약으로 보장한다.
- 집계는 활성 선택 행으로부터 파생하며 `Confession` 테이블에 타입별
  카운트 컬럼을 추가하지 않는다.
- JPA entity와 Spring Data repository는 `reaction/infrastructure`
  내부 구현으로 둔다.

## 보안 구성 요소

### Public API Protection

- 기존 고해 작성과 새 반응 변이처럼 공개 쓰기 경로에 공통 throttling을
  적용할 수 있는 Spring filter 또는 interceptor 경계와 정책 저장소를
  둔다.
- API 입력의 길이, 형식, enum allowlist를 검증하며 내부 예외 정보가
  노출되지 않는 오류 응답을 유지한다.
- HTTP 보안 헤더와 CORS 설정은 NFR Design에서 구체화한다.

### 증빙 구성 요소

- `Operational Evidence`는 저장소에서 관리하는 체크리스트 및 검증
  결과로 외부 플랫폼 설정을 추적한다. IaC 도입은 이 설계 범위가 아니다.
- `Supply Chain Evidence`는 CI workflow 추가 없이 실행 가능한 SBOM,
  취약점 및 이미지 검사 절차와 결과 증빙을 책임진다.
- 두 구성 요소 모두 문서 책임만으로 기준을 충족한 것으로 간주하지
  않는다. Construction 단계 종료 전 검증 가능한 증빙이 없으면 관련
  Security Baseline finding은 차단 상태로 남는다.

## 인터페이스 경계

| 제공자 | 소비자 | 인터페이스 목적 |
| ------ | ------ | --------------- |
| Reaction Application | Reaction API | 선택/해제 사용 사례 |
| Reaction Domain Port | Reaction Application | 선택 저장 및 해제 |
| Reaction Aggregate Port | Confession Read Application | 고해별 타입 집계 |
| Confession Domain Port | Reaction Application | 대상 고해 존재 확인 |
| Public API Protection | HTTP API 구성 요소 | 제한, 검증 전처리 및 안전한 거부 |
| 증빙 구성 요소 | NFR/Infrastructure/Build and Test 단계 | Baseline 준수 또는 차단 finding 판단 자료 |
