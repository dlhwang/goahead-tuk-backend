# Unit Of Work

## Unit 1: Anonymous Confession Write Flow

### Purpose

Deliver the MVP write behavior that lets an anonymous device submit a
confession without separately registering an author first.

### Scope

- Use `X-Device-Id` as the anonymous writer identity input for confession
  writes.
- Reuse an existing author record for a known device id.
- Create an anonymous author record for a previously unseen device id during a
  valid confession write.
- Keep the confession controller free of direct write command value object
  construction.
- Preserve domain repository ports and infrastructure persistence boundaries.

### Existing Components In Scope

- `confession/api`
  - HTTP header and write request DTO handling.
  - Confession response mapping.
- `confession/application`
  - Write use case boundary and write orchestration.
  - Confession id generation dependency.
- `author/application`
  - Anonymous author lookup or creation behavior.
- `author/domain` and `confession/domain`
  - Identifier and repository port contracts already used by the write flow.
- `author/infrastructure` and `confession/infrastructure`
  - Existing adapters remain persistence implementations behind ports.

### Out Of Scope

- Member account registration or authentication redesign.
- New deployment units, scaling policies, or database topology changes.
- Broad read or reaction API redesign.
- Moving JPA entities or Spring Data repositories outside infrastructure.

### Delivery Shape

- **Service model**: Existing Spring Boot backend.
- **Unit type**: Single brownfield backend unit.
- **Ownership**: One backend delivery owner or team can implement the change
  sequentially.
- **Primary story**: US-1 Anonymous Device Writes A Confession.

### Success Criteria

- First writes from unseen device ids do not fail only because an author record
  was absent before the request.
- Later writes from known device ids reuse the anonymous author identity.
- The controller and application boundary satisfy the approved architecture
  constraint.
- Focused tests cover the changed write flow.

## 활성 변경: 고해 반응 MVP

### 전달 형태

- 기존 Spring Boot 백엔드 하나에 포함되는 세 개의 논리 작업 단위다.
- 하나의 백엔드 팀이 순차 또는 병행으로 구현할 수 있으며 별도
  배포 서비스는 만들지 않는다.
- 기능 단위의 통합 검증과 전체 Build and Test 승인은 보안 단위가
  제공할 공통 보호 적용 및 전체 Security Baseline 증빙 판정 뒤에만
  가능하다.

### UOW-REACTION: 익명 반응 선택과 집계 제공

#### 목적

익명 열람자가 허용된 반응을 멱등하게 선택하거나 해제할 수 있게 하고,
활성 선택으로부터 타입별 집계를 제공한다.

#### 범위

- `reaction` 최상위 모듈의 API, application, domain, infrastructure
  경계를 구현한다.
- `PUT` 및 `DELETE /api/confessions/{confessionId}/reactions/{type}`를
  통해 `PRAY`, `COMFORT`, `TOGETHER` 선택 상태를 다룬다.
- `(confession_id, device_id, reaction_type)` 유일성이 있는 별도
  반응 선택 저장 모델을 제공한다.
- Confession 읽기 통합 단위가 사용할 Reaction aggregate port를
  제공한다.
- 반복 선택/해제의 멱등성, 선택 유일성과 집계 불변식에 대한
  property-based testing 설계 책임을 가진다.

#### 범위 제외

- Confession 단건/목록 응답 모델 변경 자체.
- 공통 rate limiting 및 전체 Security Baseline 증빙 판정.

### UOW-CONFESSION-REACTION-INTEGRATION: 고해 조회 집계 연동

#### 목적

기존 고해 단건 및 목록 조회가 Reaction 타입별 활성 수를 개인 식별
정보 없이 제공하도록 통합한다.

#### 범위

- 기존 `confession` 읽기 application이 Reaction aggregate port를
  소비하도록 한다.
- 조회 응답에 `PRAY`, `COMFORT`, `TOGETHER` 집계를 반영하고
  미선택 타입의 `0` 표현을 지원한다.
- 응답에 `X-Device-Id` 또는 개별 선택 내용을 노출하지 않는다.

#### 범위 제외

- Reaction 선택 행 저장과 변이 API 구현.
- 공통 HTTP 보안 및 운영 증빙 판정.

### UOW-SECURITY: 공통 보호와 보안 증빙

#### 목적

Reaction 기능과 기존 영향 API가 전체 차단 Security Baseline 아래에서
검증될 수 있도록 런타임 보호와 검증 증빙을 제공한다.

#### 범위

- 공개 쓰기 경로에 적용할 애플리케이션 내부 throttling 경계를
  구체화한다.
- 원문 기기 식별자 비기록, 안전한 오류 응답, 입력 검증 및 HTTP
  보안 설정 책임을 다룬다.
- Railway/PostgreSQL 암호화, 중앙 로그, 보존/경보/대시보드에 대한
  운영 증빙 요구를 추적한다.
- SBOM 및 취약점 검사 명령과 결과 증빙을 추적한다.
- 전체 Security Baseline 규칙별 준수, 열린 finding 또는 검증 가능한
  `N/A` 판정을 준비한다.

#### 완료 게이트

- 기능 단위와 병행 구현은 허용된다.
- `UOW-REACTION`과 `UOW-CONFESSION-REACTION-INTEGRATION`의 최종
  통합 검증 및 전체 Build and Test 승인은 이 단위의 공통 보호 적용과
  증빙 판정 뒤에만 가능하다.
