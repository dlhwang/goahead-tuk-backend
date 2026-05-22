# Build Instructions

## Scope

This change adds backend GitFlow documentation, contributor and agent guidance,
PR template guidance, and a GitHub Actions workflow. It does not change Kotlin
backend runtime code.

## Prerequisites

- **Documentation lint tool**: `markdownlint-cli2` through `npx.cmd` on Windows.
- **Patch check tool**: Git.
- **Workflow validation**: GitHub Actions execution or maintainer workflow lint
  tooling such as `actionlint` when available.

## Build Steps

### 1. Validate Changed Markdown

```powershell
npx.cmd markdownlint-cli2 "AGENTS.md" "CONTRIBUTING.md" ".github/pull_request_template.md" "docs/BACKEND_GITFLOW.md" "aidlc-docs/**/*.md"
```

### 2. Validate Patch Formatting

```powershell
git diff --check
```

### 3. Validate GitHub Workflow Locally When Tooling Exists

```powershell
actionlint .github/workflows/backend-gitflow.yml
```

If `actionlint` is unavailable, use GitHub Actions validation after pushing the
workflow or run the manual dispatch scenarios from the integration test
instructions.

## Verify Success

- Changed Markdown reports zero lint errors.
- `git diff --check` reports no patch errors.
- GitHub Actions accepts and executes
  `.github/workflows/backend-gitflow.yml`.

## Troubleshooting

### Full Repository Markdown Lint Fails

The full repository lint command may expose unrelated existing Markdown issues.
Review whether the failing file is part of the current change before treating it
as a regression.

### Workflow Validation Cannot Run Locally

Use the workflow `workflow_dispatch` inputs to validate example head and base
branch pairs in GitHub Actions after the workflow is available on a branch.
