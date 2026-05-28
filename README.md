# GoAhead TUK

이 저장소는 TUK 익명 고해 백엔드 애플리케이션과 그 개발을 운영하기
위한 AI-DLC 기반 작업 하네스를 함께 관리한다. 루트 README는 처음
저장소를 여는 기여자가 실행 코드, 개발 규칙, SDD 산출물의 위치를 빠르게
파악하기 위한 안내서다.

## 저장소에 포함된 것

현재 저장소는 서로 연결된 네 가지 영역을 포함한다.

1. **TUK 백엔드 애플리케이션**: `src/` 아래 Kotlin/Spring Boot API.
2. **AI-DLC 워크플로우 규칙**: `aidlc-rules/` 아래 에이전트 작업 규칙.
3. **프로젝트 AI-DLC 산출물**: `aidlc-docs/` 아래 계획, 설계, 구현,
   검증 기록.
4. **하네스 엔지니어링 및 SDD 작업 기반**: 에이전트가 요구사항,
   설계 결정, 코드 생성 계획, 검증 증빙을 일관된 절차로 남기기 위한
   저장소 내 작업 하네스.

백엔드 애플리케이션은 이 저장소의 실행 코드이며, AI-DLC 규칙과
산출물은 기능 변경을 specification-driven development 방식으로
진행하기 위한 기록과 절차다.

## TUK 백엔드

TUK 백엔드는 작성자와 고해 관련 API 흐름을 제공한다.

- `author`는 식별자 기반 작성자 생성 및 조회를 담당한다.
- `confession`은 고해 작성, 단건 조회, 목록 조회, 반응 집계를 담당한다.
- `common`은 공통 API 오류 처리, 식별자 계약, Spring Security 설정을
  제공한다.

애플리케이션은 도메인 중심 패키지 구조를 따른다.

- `api/`는 HTTP controller와 request 또는 response DTO를 포함한다.
- `application/`은 use case, command, orchestration service를 포함한다.
- `domain/`은 domain model, repository contract, domain service를
  포함한다.
- `infrastructure/`는 persistence adapter, JPA entity, repository,
  UUID 생성 같은 구현 세부사항을 포함한다.

## 하네스 엔지니어링과 SDD

이 저장소는 단순한 애플리케이션 코드 저장소가 아니라, AI 보조 개발을
검증 가능한 절차로 실행하기 위한 하네스도 포함한다.

- `AGENTS.md`는 이 저장소에서 작업하는 코드 어시스트가 가장 먼저 따라야
  하는 루트 지침이다.
- `aidlc-rules/`는 AI-DLC workflow와 phase별 rule detail을 담는다.
- `aidlc-docs/`는 이 프로젝트에서 실제로 생성한 요구사항, 사용자
  스토리, 설계, 코드 생성 계획, 테스트 및 감사 기록을 담는다.
- `docs/BACKEND_GITFLOW.md`는 백엔드 전달 작업의 branch, pull request,
  release 흐름을 정의한다.

여기서 SDD는 별도 도구 하나를 뜻하기보다, AI-DLC 산출물을
specification과 design trail로 삼아 구현 전에 의도, 범위, 결정, 검증
방법을 기록하는 작업 방식을 의미한다. 작은 문서 보강은 Fast Track으로
처리하고, API 계약이나 도메인 경계가 바뀌는 작업은 Standard Track,
Design Track 또는 Full Track으로 올려 필요한 산출물과 승인을 남긴다.

## 기술 스택

- Language: Kotlin on Java 21
- Framework: Spring Boot
- Build: Gradle Kotlin DSL
- Web API: Spring MVC with OpenAPI UI support
- Persistence: Spring Data JPA
- Local data: H2 in-memory database in PostgreSQL compatibility mode
- Runtime data: PostgreSQL driver for external database profiles
- Tests: JUnit Platform through Spring Boot and Kotlin test dependencies

기본 애플리케이션 설정은 다음과 같다.

- Server port `8080`
- Context path `/tuk`
- H2 console path `/h2-console`
- Virtual threads enabled
- Local CORS allowance for `http://localhost:5173`

## 저장소 구조

```text
goahead-tuk/
|-- src/                         # Kotlin Spring Boot application and tests
|   |-- main/kotlin/io/goahead/tuk/
|   |   |-- author/              # Author API, use cases, domain, persistence
|   |   |-- confession/          # Confession API, domain, reactions, persistence
|   |   `-- common/              # Shared API and configuration code
|   |-- main/resources/          # Spring application configuration
|   `-- test/kotlin/             # Application and use case tests
|-- aidlc-rules/                 # Distributable AI-DLC rule files
|   |-- aws-aidlc-rules/         # Core workflow entry point
|   `-- aws-aidlc-rule-details/  # Phase-specific and extension rules
|-- aidlc-docs/                  # Project-specific SDD and AI-DLC artifacts
|-- docs/                        # Project overview and backend GitFlow guide
|-- .github/
|   `-- workflows/
|       `-- backend-gitflow.yml  # Backend branch-flow validation
|-- build.gradle.kts             # Application build definition
|-- settings.gradle.kts          # Gradle project name
|-- AGENTS.md                    # Repository guidance for coding agents
`-- README.md                    # Repository entry point
```

## 주요 애플리케이션 진입점

- `src/main/kotlin/io/goahead/tuk/GoaheadTukApplication.kt`는 Spring Boot
  애플리케이션을 시작한다.
- `src/main/kotlin/io/goahead/tuk/author/api/AuthorController.kt`는 작성자
  HTTP endpoint를 정의한다.
- `src/main/kotlin/io/goahead/tuk/confession/api/ConfessionController.kt`는
  고해 HTTP endpoint를 정의한다.
- `src/main/resources/application.yaml`은 기본 로컬 런타임 설정을
  정의한다.

## 자주 쓰는 로컬 명령

저장소 루트에서 백엔드를 실행한다.

```bash
./gradlew bootRun
```

백엔드 테스트를 실행한다.

```bash
./gradlew test
```

문서 변경 후 markdown lint를 실행한다.

```bash
npx markdownlint-cli2 "**/*.md"
```

Windows PowerShell에서는 Gradle 명령에 `./gradlew` 대신 `.\gradlew.bat`를
사용한다.

## 문서 지도

- [docs/PROJECT_OVERVIEW.md](docs/PROJECT_OVERVIEW.md)는 기존 저장소
  개요 문서다.
- [docs/BACKEND_GITFLOW.md](docs/BACKEND_GITFLOW.md)는 백엔드 branch 및
  pull request 정책을 정의한다.
- [AGENTS.md](AGENTS.md)는 이 저장소에서 작업하는 코드 어시스트의 루트
  지침이다.
- [aidlc-docs/](aidlc-docs/)는 프로젝트별 AI-DLC/SDD 계획, 설계, 구현,
  검증 산출물을 포함한다.
- [aidlc-rules/aws-aidlc-rules/core-workflow.md](aidlc-rules/aws-aidlc-rules/core-workflow.md)는
  AI-DLC workflow 진입 규칙이다.

## 외부 Pull Request 리뷰 앱

CodeRabbit pull request comment는 이 저장소에 커밋된 workflow가 아니라
CodeRabbit GitHub App에서 생성된다. 현재 저장소는 `.coderabbit.yaml`
또는 `.coderabbit.yml` 설정 파일을 유지하지 않는다.

자동 CodeRabbit review를 비활성화해야 한다면 repository 또는
organization 관리자가 GitHub와 CodeRabbit에서 앱 설치 또는 자동 review
설정을 확인해야 한다.

- Repository **Settings** > **Integrations** > **GitHub Apps**
- Organization **Settings** > **GitHub Apps** > **CodeRabbit**
- 이 저장소에 대한 CodeRabbit dashboard automatic review settings

가져온 GitHub Actions workflow를 삭제해도 설치된 GitHub App이 제거되거나
pull request event 수신이 중지되지는 않는다.

## 기여자 참고사항

- 애플리케이션 코드는 `src/` 아래에 둔다.
- AI-DLC 또는 SDD workflow 산출물은 `aidlc-docs/`의 문서화된 위치에
  둔다.
- `aidlc-rules/aws-aidlc-rules/`와
  `aidlc-rules/aws-aidlc-rule-details/`의 이름과 위치를 임의로 바꾸지
  않는다.
- 가져온 AI-DLC 자동화와 보조 도구는 애플리케이션 저장소의 기본 실행
  코드가 아니다. 새 자동화는 필요할 때 명시적으로 채택한다.
