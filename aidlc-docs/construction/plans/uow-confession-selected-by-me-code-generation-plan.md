# UOW-CONFESSION-REACTION-INTEGRATION `selectedByMe` 코드 생성 계획

<!-- markdownlint-disable MD060 -->

## 단위 맥락

- **담당 스토리 보완**: `US-3` 단건 조회가 요청 기기의 타입별 자기
  선택 여부를 표시한다.
- **승인된 범위**: 단건 `GET /api/confessions/{confessionId}`만
  `X-Device-Id`와 `selectedByMe` 계약을 가진다.
- **의존성**: 기존 Reaction 선택 저장소, `DeviceKeyHasher`,
  집계 reader port, 안전한 입력/오류 경계.
- **유지할 경계**: 목록 조회와 작성 응답, Reaction 변이 API,
  기존 저장 모델과 배포 구성은 변경하지 않는다.

## 구현 결정

| 결정 | 근거 |
| ---- | ---- |
| 단건 조회 전용 application/API 응답 모델을 둔다. | 현재 작성과 목록이 같은 집계 응답 모델을 공유하므로 `selectedByMe`가 다른 endpoint로 새지 않게 한다. |
| Reaction 선택 조회를 별도 read port로 추가한다. | 변이 port와 count aggregate port의 기존 책임을 유지하면서 자기 선택 읽기를 명시한다. |
| 요청 기기 ID는 use case에서 `DeviceKeyHasher`로 변환한다. | 반응 변경 경로와 동일하게 raw ID를 저장 조회 및 결과 모델에서 차단한다. |
| 목록 batch 집계 경로는 수정하지 않는다. | 승인된 범위 밖의 요청자별 목록 결과 및 추가 조회 비용을 만들지 않는다. |

## 수정 및 생성 대상

| 파일 또는 경계 | 변경 목적 |
| -------------- | --------- |
| `src/main/kotlin/io/goahead/tuk/confession/api/ConfessionController.kt` | 단건 GET에서 `X-Device-Id`를 받고 검증 후 use case에 전달 |
| `src/main/kotlin/io/goahead/tuk/confession/api/response/ConfessionResponse.kt` | 단건 전용 응답/반응 항목에 `selectedByMe` 매핑 추가, 기존 집계 응답 유지 |
| `src/main/kotlin/io/goahead/tuk/confession/application/port/GetConfessionUseCase.kt` | 단건 조회 입력에 기기 식별자를 추가하고 단건 전용 응답 계약 사용 |
| `src/main/kotlin/io/goahead/tuk/confession/application/port/GetConfessionUseCaseImpl.kt` | aggregate와 요청 기기 선택 타입을 결합하고 raw ID를 해시 처리 |
| `src/main/kotlin/io/goahead/tuk/confession/application/port/command/` | 단건 조회 결과와 `selectedByMe` 항목 모델 추가 |
| `src/main/kotlin/io/goahead/tuk/confession/domain/service/ConfessionReactionSelections.kt` | 기기별 선택 타입을 읽는 전용 read port 추가 |
| `src/main/kotlin/io/goahead/tuk/confession/infrastructure/repository/ConfessionReactionJpaRepository.kt` | 고해 ID 및 해시 기기 키의 활성 선택 타입 조회 추가 |
| `src/main/kotlin/io/goahead/tuk/confession/infrastructure/persistence/ConfessionReactionSelectionAdapter.kt` | 선택 read port 구현 추가 |
| `src/test/kotlin/io/goahead/tuk/confession/application/port/GetConfessionUseCaseImplTest.kt` | count와 자기 선택 조립, 해시 전달, 실패/미선택 예제 검증 |
| `src/test/kotlin/io/goahead/tuk/confession/domain/ReactionSelectionPropertyTest.kt` | 선택 집합과 `selectedByMe` boolean 매핑 oracle 보강 |

## 실행 단계

- [x] **Step 1 - Single Read Contract**:
  단건 조회 전용 응답 모델을 추가하고 `GetConfessionUseCase`와
  controller가 검증된 `X-Device-Id`를 단건 경로에만 전달하도록
  변경한다.
- [x] **Step 2 - Selection Read Boundary**:
  Reaction domain service에 요청 기기의 활성 타입 집합을 읽는
  전용 port를 추가하고, JPA repository와 adapter가
  `(confessionId, hashedDeviceKey)`로 타입만 조회하도록 구현한다.
- [x] **Step 3 - Secure Assembly**:
  단건 use case가 `DeviceKeyHasher`로 raw 기기 값을 변환한 후
  aggregate count와 선택 타입 집합을 결합해 세 타입의
  `selectedByMe`를 조립하도록 구현한다.
- [x] **Step 4 - Endpoint Isolation Tests**:
  단건 조회의 복수 선택/미선택/기기 해시 사용 및 누락·잘못된 헤더
  검증을 테스트하고, 목록 및 작성 응답에는 새 필드가 추가되지
  않았음을 보호한다.
- [x] **Step 5 - Property Verification**:
  임의의 유효 반응 선택 타입 집합에 대해 각 타입의
  `selectedByMe`가 집합 포함 여부와 일치하는 oracle 속성을 기존
  Kotest PBT에 추가한다.
- [x] **Step 6 - Code Summary and Evidence**:
  변경 경계, `Security Baseline` 및 `Property-Based Testing` 준수
  판단, 실제 수행한 테스트 결과를 코드 요약 및 Build and Test
  기록에 반영한다.

## 검증 명령

```powershell
.\gradlew.bat test
npx.cmd markdownlint-cli2 "**/*.md"
git diff --check
git diff --stat
```

## Extension 준수 계획

### Security Baseline

- **적용**: `SECURITY-05`, `SECURITY-08`, `SECURITY-13`,
  `SECURITY-15`.
- **구현 증빙**: 단건 GET 헤더 형식 검증, HMAC 파생 키 조회,
  응답의 raw/hashed ID 비노출, 저장 조회 실패의 fail-closed 경로.
- **변경 범위상 N/A**: 이번 보완은 새 인프라, 인증, 네트워크 또는
  배포 구성을 추가하지 않으므로 그 관련 기준은 기존 Reaction MVP
  판정을 변경하지 않는다.

### Property-Based Testing

- **적용**: 타입별 `selectedByMe` oracle, 기존 타입 완전성 및
  집계 불변식.
- **구현 증빙**: Kotest 생성 타입 집합과 응답 boolean 매핑 비교,
  핵심 단건 시나리오에 대한 예제 기반 단위 테스트 병행.
- **재현성**: 기존 Kotest Property Testing 실행 및 Gradle 테스트
  흐름을 유지한다.

## 완료 기준

- 단건 조회가 유효한 `X-Device-Id`와 함께 세 타입의 count 및
  `selectedByMe`를 반환한다.
- 목록 및 작성 응답에는 `selectedByMe`가 나타나지 않는다.
- 조회 저장 경계는 raw 기기 ID가 아닌 해시된 기기 키만 소비한다.
- 예제 기반 테스트와 PBT가 자기 선택 결과를 검증하며 전체 Gradle
  테스트가 통과한다.
