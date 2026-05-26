# AGENTS.md

## Project Overview

This repository contains the GoAhead TUK Kotlin/Spring Boot backend together
with AI-DLC rules used during assisted development and the resulting project
documentation.

## Repository Structure

```text
src/                          # Kotlin Spring Boot application and tests
aidlc-rules/                  # Imported AI-DLC rule set used by agents
aidlc-docs/                   # Project-specific AI-DLC generated documents
docs/
|-- BACKEND_GITFLOW.md        # Backend branch and pull request policy
`-- PROJECT_OVERVIEW.md       # Current repository overview
.github/
|-- workflows/
|   `-- backend-gitflow.yml   # Backend branch-flow validation
`-- pull_request_template.md  # Project pull request template
```

## Important Constraints

- Keep `aidlc-rules/aws-aidlc-rules/` and
  `aidlc-rules/aws-aidlc-rule-details/` in place; agents resolve rule
  references using these paths.
- Keep `aidlc-docs/`; these are project artifacts, not imported upstream
  sample files.
- Kotlin backend delivery under `src/` follows
  [docs/BACKEND_GITFLOW.md](docs/BACKEND_GITFLOW.md).
- Do not reintroduce upstream AI-DLC release, evaluation, or scheduled
  security-scanner workflows unless this project explicitly adopts them.

## Local Validation

Run backend tests from the repository root:

```powershell
.\gradlew.bat test
```

For Markdown changes, lint when `markdownlint-cli2` is available:

```powershell
npx markdownlint-cli2 "**/*.md"
```

## Pull Requests

- Use conventional commit style for commit and pull request titles.
- Use the structure in `.github/pull_request_template.md`.
- For changes under `src/`, follow the backend GitFlow guide.
