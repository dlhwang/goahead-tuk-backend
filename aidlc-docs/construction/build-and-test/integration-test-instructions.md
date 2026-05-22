# Integration Test Instructions

## Purpose

Validate that the committed backend GitFlow workflow enforces the approved
branch pairs for backend changes.

## Manual Workflow Dispatch Scenarios

Run `.github/workflows/backend-gitflow.yml` with these dispatch inputs:

| Base branch | Head branch              | Expected result |
| ----------- | ------------------------ | --------------- |
| `develop`   | `feature/confession-api` | Pass            |
| `develop`   | `hotfix/reaction-count`  | Pass            |
| `main`      | `release/backend-v0.4.0` | Pass            |
| `main`      | `feature/confession-api` | Fail            |
| `develop`   | `docs/readme-update`     | Fail            |

## Pull Request Scenarios

After `develop` exists, use a backend path change such as `src/**` or
`build.gradle.kts` to verify:

1. A `feature/*` backend PR into `develop` triggers the backend GitFlow
   validation job and passes.
2. A `feature/*` backend PR into `main` triggers the backend GitFlow validation
   job and fails.
3. A `release/*` backend PR into `main` passes.
4. A `release/*` or `hotfix/*` back-merge PR into `develop` passes.

## Cleanup

- Close any intentionally failing test pull requests.
- Remove temporary branch-flow test branches after verification.
