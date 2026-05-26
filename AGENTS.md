# AGENTS.md

## 목적과 적용 범위

이 문서는 이 저장소에서 작업하는 코드 어시스트가 가장 먼저 읽고
따라야 하는 루트 지침이다. 별도의 "Using AI-DLC" 요청이 없어도 모든
개발 및 문서 작업은 이 문서와 AI-DLC 작업 흐름을 기본 방식으로
사용한다.

이 저장소는 Kotlin/Spring Boot 백엔드와 그 개발 과정에서 사용하는
AI-DLC 규칙 및 프로젝트 산출물을 함께 관리한다.

## 작업 시작 순서

작업을 시작할 때 다음 순서를 따른다.

1. 이 문서와 사용자 요청을 읽는다.
2. 현재 작업과 관련된 저장소 파일 및 기존 산출물을 확인한다.
3. `aidlc-rules/aws-aidlc-rules/core-workflow.md`를 읽고 작업 규모와
   유형에 맞는 AI-DLC 단계 및 관련 상세 규칙을 식별한다.
4. AI-DLC 규칙에서 요구하는 계획, 승인, 산출물 및 검증 절차를
   수행한다.
5. 완료 시 변경 요약과 실제 수행한 검증 결과를 보고한다.

이 문서는 AI-DLC 규칙에서 요구하는 계획 외에 별도의 implementation
plan 산출물을 추가로 요구하지 않는다.

## AI-DLC 기본 작업 방식

AI-DLC는 이 저장소의 기본 개발 절차이며 선택적인 주문어가 아니다.
현재 작업에 필요한 단계와 깊이는
`aidlc-rules/aws-aidlc-rules/core-workflow.md`와 관련 상세 규칙에 따라
결정한다.

이 저장소에서 사용하는 AI-DLC 경로는 다음과 같이 구분한다.

```text
aidlc-rules/
|-- aws-aidlc-rules/core-workflow.md       # 워크플로우 진입 규칙
`-- aws-aidlc-rule-details/                # 단계별/공통/확장 상세 규칙

aidlc-docs/                                # 이 프로젝트에서 생성한 산출물
|-- aidlc-state.md                         # 작업 상태
|-- audit.md                               # 작업 기록
|-- inception/                             # 분석 및 계획 산출물
`-- construction/                          # 설계, 구현, 검증 산출물
```

- 이 저장소에서는 `aidlc-rules/aws-aidlc-rule-details/`를 상세 규칙의
  기준 경로로 사용한다. 가져온 워크플로우 문서가 다른 설치 경로를
  예시로 들더라도 현재 저장소의 실제 경로를 우선한다.
- 작업을 시작하거나 재개할 때 `aidlc-docs/aidlc-state.md`와 필요한 기존
  산출물을 확인해 이미 결정된 내용과 진행 상태를 존중한다.
- 작업 단계에서 요구하는 상세 규칙만 `aidlc-rules/`에서 읽고 적용한다.
- `aidlc-docs/`의 기존 문서는 이 프로젝트가 만든 기록과 산출물이다.
  삭제하거나 외부 템플릿, 가져온 예제, 정리 대상 파일로 취급하지
  않는다.
- 외부 AI-DLC에서 가져온 파일의 유지 기준은 `aidlc-rules/` 중심이다.
  불필요해 보이는 외부 문서, 예제, 템플릿 또는 자동화 파일을 발견해도
  즉시 삭제하지 말고 경로와 판단 근거를 정리 대상 목록으로 먼저
  제안한다.

## 저장소 구조와 보호 범위

현재 주요 경로는 다음과 같다.

```text
src/                          # Kotlin Spring Boot 애플리케이션과 테스트
aidlc-rules/                  # 가져온 AI-DLC 규칙과 워크플로우
aidlc-docs/                   # 프로젝트별 AI-DLC 산출물과 기록
docs/
|-- BACKEND_GITFLOW.md        # 백엔드 브랜치 및 PR 정책
`-- PROJECT_OVERVIEW.md       # 저장소 개요
.github/
|-- workflows/backend-gitflow.yml
`-- pull_request_template.md
```

- `aidlc-rules/aws-aidlc-rules/`와
  `aidlc-rules/aws-aidlc-rule-details/`의 경로를 임의로 이동하거나
  이름을 바꾸지 않는다.
- `aidlc-docs/`를 가져온 샘플 콘텐츠로 보아 정리하거나 삭제하지 않는다.
- `src/` 아래 백엔드 변경에는 `docs/BACKEND_GITFLOW.md`를 적용한다.
- 프로젝트가 명시적으로 채택하지 않은 upstream AI-DLC 릴리스,
  평가, 정기 보안 스캐너 workflow를 다시 추가하지 않는다.

## 문서 작성 언어

새로 작성하거나 실질적으로 수정하는 문서, 계획, 요약, 설명 산출물은
기본적으로 한국어로 작성한다. 코드 식별자, 명령어, 파일 경로, 프로토콜
명칭, 외부 고유명과 사용자가 정확한 원문 보존을 요구한 내용은 원문
형식을 유지한다. 기존 영문 문서를 언어 변경만을 위해 일괄 번역하지
않는다.

## GitFlow와 Pull Request

백엔드 전달 작업의 상세 정책은 `docs/BACKEND_GITFLOW.md`를 기준으로
한다.

- `feature/*`는 `develop`에서 분기하고 `develop` 대상 pull request로
  병합한다.
- `release/*`는 `develop`에서 분기하고 `main` 대상 pull request로
  릴리스를 안정화한 뒤, 릴리스 결과를 `develop`에 반영한다.
- `hotfix/*`는 `main`에서 분기하고 `main` 대상 pull request로 긴급
  수정한 뒤, 수정 결과를 `develop`에 반드시 병합하거나 backport한다.
- `main`과 `develop`에는 직접 push하지 않고 pull request 및 요구되는
  검증을 거친다.
- 커밋 제목과 pull request 제목은 conventional commit 형식을 사용하고,
  pull request 설명은 `.github/pull_request_template.md` 구조를 따른다.

## 검증과 완료 보고

AI-DLC 단계에서 수립한 계획과 변경 유형에 맞는 검증을 수행한다.
이 저장소의 기본 검증 명령은 다음과 같다.

```powershell
# 백엔드 코드 또는 동작 변경
.\gradlew.bat test

# Markdown 문서 변경 (`markdownlint-cli2` 사용 가능 시)
npx markdownlint-cli2 "**/*.md"
```

완료 보고에는 다음 내용을 간결하게 포함한다.

- 변경한 파일과 핵심 변경 내용
- 생성, 수정, 삭제하지 않은 보호 대상 또는 범위상 제외한 파일
- 실제 실행한 검증 명령과 성공/실패 결과
- 검증을 실행하지 못했거나 기존 문제로 통과하지 못한 경우 그 이유
- 삭제 전 검토가 필요한 외부 AI-DLC 정리 후보를 발견한 경우 그 목록
