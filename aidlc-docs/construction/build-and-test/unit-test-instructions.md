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
