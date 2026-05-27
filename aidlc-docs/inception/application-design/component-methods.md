# 고해 반응 Component Methods

<!-- markdownlint-disable MD060 -->

## 범위와 수준

아래 계약은 구성 요소 간 고수준 인터페이스를 정한다. 트랜잭션 경합
처리, validation 규칙의 정확한 정규식, rate-limit 용량과 PBT 생성기는
후속 Functional Design 및 NFR Design에서 확정한다.

## Reaction API Boundary

```kotlin
interface ReactionHttpApi {
    fun select(confessionId: String, type: String, deviceId: String)
    fun clear(confessionId: String, type: String, deviceId: String)
}
```

| 메서드 | HTTP 계약 | 목적 |
| ------ | --------- | ---- |
| `select` | `PUT /api/confessions/{confessionId}/reactions/{type}`, 성공 `204` | 허용 타입을 선택된 상태로 만든다. |
| `clear` | `DELETE /api/confessions/{confessionId}/reactions/{type}`, 성공 `204` | 허용 타입을 해제된 상태로 만든다. |

두 메서드는 필수 `X-Device-Id`를 입력으로 받고 원문을 응답이나
애플리케이션 로그에 공개하지 않는다.

## Reaction Use Cases

```kotlin
interface SelectReactionUseCase {
    fun execute(confessionId: String, deviceId: String, type: String)
}

interface ClearReactionUseCase {
    fun execute(confessionId: String, deviceId: String, type: String)
}
```

| 계약 | 입력 | 결과 |
| ---- | ---- | ---- |
| 선택 사용 사례 | 고해 ID, 기기 ID, 반응 타입 | 존재하는 선택을 유지하거나 새 선택을 저장한다. |
| 해제 사용 사례 | 고해 ID, 기기 ID, 반응 타입 | 존재하는 선택을 제거하거나 이미 없는 상태를 유지한다. |

## Reaction Domain Ports

```kotlin
interface ReactionSelectionRepository {
    fun select(selection: ReactionSelection)
    fun clear(confessionId: ConfessionId, deviceId: DeviceId, type: ReactionType)
}

interface ReactionAggregateReader {
    fun countsOf(confessionId: ConfessionId): ReactionCounts
    fun countsOf(confessionIds: Collection<ConfessionId>): Map<ConfessionId, ReactionCounts>
}
```

- `ReactionSelection`은 고해 ID, 익명 기기 ID, 허용 반응 타입을
  나타낸다.
- 영속성 adapter는 동일 삼중 키를 중복 저장하지 않으며 집계 결과에는
  모든 허용 타입의 `0` 표현이 가능해야 한다.

## Confession 협력 계약

```kotlin
interface ConfessionExistenceReader {
    fun existsById(confessionId: ConfessionId): Boolean
}
```

- Reaction Application은 선택과 해제 전에 대상 고해 존재 여부를
  확인하고 없으면 기존 정책과 일관된 not-found 오류를 전달한다.
- Confession 단건/목록 조회 application은 `ReactionAggregateReader`를
  호출해 응답 모델의 반응 집계를 조립한다.

## 공통 보안 계약

```kotlin
interface PublicWriteRateLimiter {
    fun acquire(requestKey: RateLimitKey): RateLimitDecision
}

interface SecurityEvidenceVerifier {
    fun verifyOperationalEvidence(): EvidenceResult
    fun verifySupplyChainEvidence(): EvidenceResult
}
```

| 계약 | 책임 |
| ---- | ---- |
| `PublicWriteRateLimiter` | 공개 쓰기 요청에 내부 throttling 정책을 적용하고 초과 요청을 안전하게 거부한다. |
| 입력 검증 경계 | 경로, 헤더, 요청 데이터에 명시적 길이/형식/allowlist를 적용한다. |
| 관측 경계 | device ID 원문 없이 구조화 이벤트와 안전한 예외 기록 정책을 제공한다. |
| `SecurityEvidenceVerifier` | 외부 운영 설정과 공급망 실행 증빙의 존재 및 판정 결과를 후속 단계에 제공한다. |

## 오류 응답 계약

| 조건 | 외부 결과 |
| ---- | --------- |
| 존재하지 않는 고해 | `404 Not Found` |
| 허용되지 않는 타입 또는 잘못된 입력 | `400 Bad Request` |
| rate limit 초과 | NFR Design에서 확정할 제한 응답, 내부 정보 비노출 |
| 처리되지 않은 내부 실패 | 안전한 일반 오류, 상세 스택/저장소 정보 비노출 |
