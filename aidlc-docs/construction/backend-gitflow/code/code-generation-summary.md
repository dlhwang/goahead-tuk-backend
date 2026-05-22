# Backend GitFlow Code Generation Summary

## Created Files

- `docs/BACKEND_GITFLOW.md` - Backend GitFlow policy, branch flow, PR targets,
  validation boundary, and GitHub branch protection follow-up.
- `.github/workflows/backend-gitflow.yml` - Scoped backend PR branch-flow
  validation for backend source and Gradle path changes into `main` or
  `develop`.

## Modified Files

- `CONTRIBUTING.md` - Directs backend delivery contributors to the GitFlow
  guide while preserving non-backend repository workflows.
- `AGENTS.md` - Adds concise backend GitFlow guidance for coding agents.
- `.github/pull_request_template.md` - Adds backend GitFlow review checklist
  items while preserving the contributor acknowledgment.

## Validation Performed

- `npx.cmd markdownlint-cli2 "AGENTS.md" "CONTRIBUTING.md" ".github/pull_request_template.md" "docs/BACKEND_GITFLOW.md" "aidlc-docs/**/*.md"`
  - Passed for changed Markdown and AI-DLC artifacts.
- `git diff --check`
  - Passed.
- `npx.cmd markdownlint-cli2 "**/*.md"`
  - Attempted for the full repository and failed on pre-existing
    `HELP.md` Markdown lint findings outside this change.

## Workflow Validation Notes

- The backend GitFlow workflow uses built-in GitHub Actions context and shell
  logic without external actions.
- Local `actionlint` and local YAML parser commands were not available in this
  environment, so workflow validation remains subject to GitHub Actions
  execution or maintainer workflow lint tooling.

## Manual Follow-Up

- Create and protect the `develop` branch before requiring the backend GitFlow
  check.
- Configure protected branch and required-check settings for `main` and
  `develop` as described in `docs/BACKEND_GITFLOW.md`.
- Decide which backend CI checks should be required alongside the GitFlow branch
  validation check.
