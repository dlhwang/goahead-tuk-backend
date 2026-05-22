# Unit Test Execution

## Scope

No Kotlin unit tests are required for this GitFlow documentation and workflow
change because backend runtime code is unchanged.

## Static Checks

Run changed Markdown lint:

```powershell
npx.cmd markdownlint-cli2 "AGENTS.md" "CONTRIBUTING.md" ".github/pull_request_template.md" "docs/BACKEND_GITFLOW.md" "aidlc-docs/**/*.md"
```

Run patch formatting validation:

```powershell
git diff --check
```

## Review Results

- Markdown lint should report zero errors for changed Markdown files.
- Patch validation should report no whitespace or conflict marker issues.
- Kotlin test suites remain optional for this change because no Kotlin source or
  test files are modified.
