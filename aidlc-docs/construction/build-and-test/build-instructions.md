# Build Instructions

## Scope

Build and verify the Kotlin Spring Boot backend after the anonymous confession
write flow boundary update.

## Prerequisites

- **Build tool**: Gradle wrapper in the workspace.
- **Runtime target**: Java 21 toolchain configured by the project.
- **Dependencies**: Resolved by Gradle from `build.gradle.kts`.
- **Environment variables**: None required for the backend test suite with the
  current local test configuration.

## Build Steps

### 1. Build And Test The Backend

```powershell
.\gradlew.bat test
```

### 2. Run The Focused Write-Flow Test During Iteration

```powershell
.\gradlew.bat test --tests io.goahead.tuk.confession.application.port.WriteConfessionUseCaseImplTest
```

### 3. Verify Build Success

- Gradle reports `BUILD SUCCESSFUL`.
- Compiled classes and test reports are generated under `build/`.
- Confession write tests cover both existing-author and auto-create-author
  write paths.

## Troubleshooting

### Kotlin Compile Daemon Warnings

Gradle may report Kotlin daemon connection or temp-file access warnings in
restricted environments and then compile with its fallback strategy. Treat the
Gradle task result as authoritative when the build completes successfully.

### Test Failures

Review the Gradle output and the reports under `build/reports/tests/test/`,
fix the failing code or test, and rerun the same Gradle command.
