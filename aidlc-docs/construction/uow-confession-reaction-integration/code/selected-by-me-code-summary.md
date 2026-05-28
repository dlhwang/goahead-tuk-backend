# `selectedByMe` 단건 조회 코드 생성 요약

<!-- markdownlint-disable MD060 -->

## 구현 범위

- 단건 `GET /api/confessions/{confessionId}`가 `X-Device-Id`를 받아
  타입별 `selectedByMe`를 반환하도록 변경했다.
- 작성 및 목록 응답은 기존 count 전용 구조를 유지한다.
- 기존 Reaction 선택 저장 모델과 목록 batch 집계 계약은 변경하지
  않았다.

## 변경 경계

| 경계 | 구현 내용 |
| ---- | --------- |
| API | 단건 조회에서 `X-Device-Id`를 검증하고 `ConfessionDetailResponse`를 반환한다. |
| Application | `GetConfessionResponse`가 count와 요청 기기의 타입별 선택 상태를 조립한다. |
| Domain Port | `ReactionSelectionReader`가 요청 기기의 활성 타입 집합 읽기를 정의한다. |
| Persistence | 해시된 기기 키와 고해 ID로 반응 타입만 조회한다. |
| Test | controller 전달/검증, use case 조립/해시 사용, PBT oracle을 추가한다. |

## 보안 및 PBT 판단

- `X-Device-Id`는 API 검증 후 `DeviceKeyHasher`를 거쳐 저장 조회에
  사용되며 응답에 포함되지 않는다. (`SECURITY-05`,
  `SECURITY-08`, `SECURITY-13`)
- 자기 선택 조회 오류를 `false`로 대체하는 fallback은 구현하지
  않아 예외가 안전 오류 처리 경계로 전달된다. (`SECURITY-15`)
- 임의 타입 선택 목록에서 파생한 집합과 응답의 `selectedByMe`가
  일치하는 Kotest property test를 추가했다.
- 핵심 조회 전달 및 선택 표시 시나리오는 예제 기반 테스트로도
  보호한다.

## 수행한 예비 검증

```powershell
.\gradlew.bat test
```

- **결과**: 성공.
- **참고**: sandbox 환경에서 Kotlin compile daemon 임시 파일 접근
  경고가 발생했으나 Gradle이 fallback 컴파일로 전환하여 빌드와
  테스트는 성공했다.

정식 Build and Test 단계에서는 전체 Markdown 검증과 diff 점검을
포함해 최종 검증 결과를 별도로 기록한다.
