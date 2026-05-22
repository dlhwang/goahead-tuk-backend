# Backend GitFlow Code Generation Plan

This plan is the source of truth for implementing the approved backend GitFlow
delivery policy in the brownfield repository.

## Unit Context

- **Unit name**: `backend-gitflow`
- **Goal**: Document and support standard GitFlow for Kotlin Spring Boot
  backend delivery.
- **Policy target**: Backend changes under `src/`.
- **Permitted support files**: Repository documentation, AI agent guidance, PR
  template files, and GitHub workflow files required to guide or validate the
  backend flow.
- **Out of scope**: Backend runtime behavior, API behavior, data models, AI-DLC
  rule distribution release behavior unless an explicit backend support boundary
  must be stated.

## Existing Inputs

- Requirements: `aidlc-docs/inception/requirements/requirements.md`
- Execution plan: `aidlc-docs/inception/plans/execution-plan.md`
- Existing contributor guidance: `CONTRIBUTING.md`
- Existing AI agent guidance: `AGENTS.md`
- Existing PR guidance: `.github/pull_request_template.md`
- Existing PR validation workflow: `.github/workflows/pull-request-lint.yml`
- Existing AI-DLC release CodeBuild workflow: `.github/workflows/codebuild.yml`

## Implementation Principles

- Modify existing brownfield files in place.
- Keep GitFlow policy focused on backend delivery.
- Avoid broad changes to AI-DLC release workflows when a dedicated backend
  validation path is safer.
- Document Git hosting settings that cannot be encoded by committed files.
- Do not overwrite unrelated uncommitted deployment changes.

## Planned Steps

- [x] Step 1: Review existing backend and repository guidance touchpoints.
  - Confirm where contributor instructions currently assume PRs target `main`.
  - Confirm which existing workflows are AI-DLC-release-specific versus suitable
    for backend GitFlow validation.

- [x] Step 2: Add backend GitFlow policy documentation.
  - Create a durable backend delivery document under `docs/`.
  - Define `main`, `develop`, `feature/*`, `release/*`, and `hotfix/*`.
  - Define branch origins, PR targets, release tagging, hotfix back-merge
    expectations, and branch protection settings that require Git hosting
    configuration.

- [x] Step 3: Update contributor and agent guidance.
  - Update `CONTRIBUTING.md` so backend contributions point to the GitFlow
    policy instead of relying only on latest-`main` guidance.
  - Update `AGENTS.md` with concise backend GitFlow guidance for future coding
    agents without turning the whole repository policy into backend GitFlow.

- [x] Step 4: Update PR guidance.
  - Update `.github/pull_request_template.md` with backend GitFlow review
    prompts that help reviewers confirm the PR target and branch purpose.
  - Preserve the existing contributor acknowledgment.

- [x] Step 5: Implement feasible workflow validation.
  - Prefer a focused GitHub workflow or scoped validation logic for backend PRs.
  - Validate backend PR base branch expectations where event context makes that
    feasible.
  - Avoid breaking AI-DLC release PR validation and CodeBuild evaluation paths.

- [x] Step 6: Validate generated repository changes.
  - Run Markdown lint for affected Markdown files.
  - Run whitespace and patch consistency checks.
  - Validate workflow syntax or workflow-facing checks available locally.
  - Record manual GitHub branch protection follow-up where repository files
    cannot enforce settings directly.

- [x] Step 7: Write Code Generation summary.
  - Create a markdown summary under
    `aidlc-docs/construction/backend-gitflow/code/`.
  - List created and modified files, validation performed, and any remaining
    manual repository settings.

## Expected Deliverables

- Backend GitFlow policy documentation.
- Contributor and AI agent guidance updates.
- PR template guidance for backend GitFlow review.
- Feasible workflow validation for backend GitFlow PR flow.
- Code generation summary and verification record.

## Traceability

- **FR-1 GitFlow Policy**: Steps 2 and 3.
- **FR-2 Backend Scope Boundary**: Steps 2, 3, and 5.
- **FR-3 Repository Guidance**: Steps 2, 3, and 4.
- **FR-4 Automation-Oriented Rules**: Steps 4, 5, and 6.
- **FR-5 CI and Deployment Implications**: Steps 2, 5, and 6.
- **FR-6 AI-DLC Alignment**: Steps 1 and 7.
