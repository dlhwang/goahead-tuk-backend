# Unit Test Execution

## Unit Test Scope

The focused unit verification for this change is the confession write use case
test suite.

## Run Focused Unit Tests

```powershell
.\gradlew.bat test --tests io.goahead.tuk.confession.application.port.WriteConfessionUseCaseImplTest
```

## Expected Scenarios

- Generated confession ids are persisted on write.
- A known anonymous author id is reused.
- A missing anonymous author is created before the confession is saved.

## Run All Backend Tests

```powershell
.\gradlew.bat test
```

## Review Results

- Gradle should report zero failed tests.
- Test reports are available under `build/reports/tests/test/`.

## `selectedByMe` 보완 검증

### 단건 조회 예제 테스트

```powershell
.\gradlew.bat test --tests io.goahead.tuk.confession.application.port.GetConfessionUseCaseImplTest --tests io.goahead.tuk.confession.api.ConfessionControllerTest
```

- 단건 조회는 유효한 `X-Device-Id`를 use case에 전달한다.
- count와 `selectedByMe`는 세 허용 타입별로 조립된다.
- 저장 조회에는 raw 기기 ID가 아니라 해시된 기기 키가 전달된다.
- 유효하지 않은 기기 ID는 조회 수행 전에 거부된다.

### Property-Based Testing

```powershell
.\gradlew.bat test --tests io.goahead.tuk.confession.domain.ReactionSelectionPropertyTest
```

- 임의 타입 선택 집합에 대해 `selectedByMe`가 타입 포함 여부와
  일치해야 한다.
- Kotest의 shrinking 및 seed 기반 실패 재현 흐름은 기존 Gradle
  테스트 실행 경계를 유지한다.
