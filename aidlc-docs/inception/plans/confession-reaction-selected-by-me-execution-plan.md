# 고해 반응 자기 선택 조회 실행 계획

<!-- markdownlint-disable MD060 -->

## 사전 계획

1. **Requirement summary**: 고해 단건 조회에 `X-Device-Id`를
   필수 입력으로 추가하고, 허용 반응 타입별로 요청 기기의 선택
   여부인 `selectedByMe`를 반환한다.
2. **Task type**: 기존 Reaction API 계약 보완과 조회 계층 기능 변경.
3. **Selected AI-DLC execution mode**: `Design Track`.
4. **Reason for selected mode**: 기존 승인 설계가 개별 선택 상태
   비노출을 명시하고 있으므로 API 계약과 보호 경계를 먼저
   재결정해야 한다. 데이터 저장 모델과 배포 구조 변경은 예상하지
   않는다.
5. **Required context files**:
   `aidlc-docs/aidlc-state.md`,
   `aidlc-docs/inception/requirements/confession-reaction-requirements.md`,
   `aidlc-docs/construction/uow-confession-reaction-integration/functional-design/`,
   `aidlc-docs/construction/plans/uow-confession-reaction-integration-code-generation-plan.md`,
   `src/main/kotlin/io/goahead/tuk/confession/`의 API, application,
   domain service, persistence 코드와 관련 테스트.
6. **Expected files to change**: 이 계획과 요구사항 보완안 승인 후
   `aidlc-docs`의 관련 보완 설계 및 코드 생성 기록,
   `ConfessionController.kt`, 단건 조회 use case/응답 모델,
   Reaction 조회 port 및 adapter/repository, 관련 테스트.
7. **Files or directories that must not change**:
   `aidlc-rules/**`, `Confession` 저장 테이블의 카운트 컬럼 구조,
   목록 및 작성 API 응답 계약, 배포 프로필과 GitFlow 정책.
8. **Validation commands**:
   `.\gradlew.bat test`,
   `npx.cmd markdownlint-cli2 "**/*.md"`,
   `git diff --check`,
   `git diff --stat`.
9. **Risks or assumptions**: `selectedByMe`는 단건 조회에만 필요하다고
   해석한다. 기기 식별자를 해시한 뒤 선택 존재 여부만 조회하며,
   raw `X-Device-Id` 노출이나 새 저장 컬럼은 허용하지 않는다.

## 영향 분석

- 기존 단건 조회는 타입별 count만 제공하며 요청 기기를 알지 못한다.
- 이번 변경은 요청자별 읽기 결과를 만들므로 기존
  `BR-INTEGRATION-4` 비노출 규칙을 “다른 기기 및 raw ID 비노출”로
  좁혀야 한다.
- 저장된 반응 선택 행은 이미 기기 파생 키와 타입을 가진다. 저장
  모델 변경 없이 자기 선택 조회 port를 보강할 수 있다.
- 목록 조회는 기기별 상태를 요구하지 않아 batch 집계 계약을 그대로
  유지한다.

## 실행 단계

### Inception

- [x] Workspace 확인 - 기존 brownfield 코드와 Reaction MVP 진행
  기록을 확인했다.
- [x] Requirements Analysis minimal - 요청의 단건 조회 계약 변경과
  기존 승인 결정의 충돌을 식별하고 보완 요구사항을 작성했다.
- [x] Requirements Analysis 승인 - 사용자가 요구사항 보완안의 진행을
  승인했다.
- [x] Workflow Planning minimal - 승인된 단건 조회 범위와 생략 단계를
  확정했다.

### Construction

- [x] Functional Design - 단건 조회의 자기 선택 파생 규칙,
  비노출 경계와 PBT 속성을 기존 통합 단위에 보완했다.
- [x] Functional Design 승인 - 사용자가 다음 단계 진행을 승인했다.
- [x] Code Generation Planning - 수정 파일, port 계약, 응답 모델과
  테스트 단계를 체크리스트로 확정했다.
- [ ] Code Generation Plan 승인 - Kotlin 코드 변경 전 사용자
  승인을 받는다.
- [x] Code Generation - 승인된 계획에 따라 Kotlin 코드와 테스트를
  수정하고 예비 Gradle 테스트를 통과했다.
- [x] Code Generation 승인 - 사용자가 Build and Test 진행을
  승인했다.
- [x] Build and Test - 전체 Gradle 테스트 성공, 변경 문서 lint
  성공, diff 점검 성공, 범위 외 기존 `HELP.md` 전체 lint 오류 기록.
- [ ] Build and Test 승인 - 사용자 검토 대기.

## 생략 단계와 사유

| 단계                    | 판단 | 사유                                            |
| ----------------------- | ---- | ----------------------------------------------- |
| Reverse Engineering     | 생략 | 기존 Reaction 산출물과 현재 코드 조사로 충분함 |
| User Stories            | 생략 | 기존 `US-3`의 표시 데이터 보완이며 새 흐름 아님 |
| Application Design      | 생략 | 기존 읽기/Reaction 의존 경계를 유지함          |
| Units Generation        | 생략 | 기존 통합 단위 내부 변경임                     |
| NFR Requirements        | 생략 | 활성 보안/PBT 기준을 그대로 적용함             |
| NFR Design              | 생략 | 새 운영 또는 성능 정책이 없음                  |
| Infrastructure Design  | 생략 | 저장 모델과 배포 구성이 바뀌지 않음            |

## 검증 계획

- 단건 use case가 기기별 선택 집합을 세 타입의 boolean으로 정규화하는
  예제 기반 테스트를 추가한다.
- controller 또는 API 레벨에서 단건 조회의 `X-Device-Id` 전달 및
  검증을 확인한다.
- 자기 선택 oracle property가 필요한 구현 형태라면 기존 Kotest PBT
  테스트를 보강한다.
- 전체 Gradle 테스트로 기존 목록 집계, 반응 선택/해제 및 보안
  동작의 회귀를 확인한다.

## Extension 적용

- **Security Baseline**: 활성. `SECURITY-05`, `SECURITY-08`,
  `SECURITY-13`, `SECURITY-15`가 이번 읽기 계약 보완에 직접
  적용된다. raw 기기 식별자 비로그와 다른 기기 정보 비노출을
  유지한다.
- **Property-Based Testing**: 활성. 타입 완전성, 집계 불변식과
  요청 기기의 선택 집합에 대한 `selectedByMe` oracle을 검증
  대상으로 유지한다.
