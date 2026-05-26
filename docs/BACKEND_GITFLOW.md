# Backend GitFlow

This guide defines the GitFlow policy for the Kotlin Spring Boot backend under
`src/`. The repository also contains AI-DLC rule material and generated project
documentation. Those areas do not become backend GitFlow work just because
they share this repository.

Repository-level support files may change with backend delivery when they guide
or validate the backend flow. Examples include contributor guidance, agent
guidance, pull request templates, and GitHub workflows scoped to backend
changes.

## Branch Roles

- `main` is the stable backend release line.
- `develop` is the backend integration line for planned work.
- `feature/*` carries planned backend work from `develop` back to `develop`.
- `release/*` stabilizes backend release candidates from `develop` into `main`.
- `hotfix/*` carries urgent released-backend fixes from `main`.

Use short branch suffixes that identify the backend change. Examples:

- `feature/confession-search`
- `release/backend-v0.4.0`
- `hotfix/confession-reaction-count`

## Branch Flow

### Feature Flow

1. Branch `feature/*` from `develop`.
2. Keep the branch focused on one backend change.
3. Open the backend pull request into `develop`.
4. Delete the feature branch after merge.

Backend dependency update branches such as `dependabot/*` should also target
`develop` when they change backend build or source files.

### Release Flow

1. Branch `release/*` from `develop` when the backend release scope is fixed.
2. Limit the branch to release stabilization, release notes, and fixes required
   for that release.
3. Open the release pull request into `main`.
4. Tag the backend release from the merged `main` state.
5. Merge or backport the release result into `develop` so the integration line
   keeps the released fixes.

Use a backend tag namespace such as `backend-v0.4.0` so application releases
remain distinct from any AI-DLC rule snapshots retained in repository history.

### Hotfix Flow

1. Branch `hotfix/*` from `main`.
2. Keep the branch limited to the urgent released-backend fix.
3. Open the hotfix pull request into `main`.
4. Tag the corrected backend release if a release is cut.
5. Merge or backport the hotfix into `develop`.

## Pull Request Targets

Backend pull requests are expected to follow these targets:

| Head branch     | Base branch       | Purpose                         |
| --------------- | ----------------- | ------------------------------- |
| `feature/*`     | `develop`         | Planned backend changes         |
| `dependabot/*`  | `develop`         | Backend dependency maintenance  |
| `release/*`     | `main`, `develop` | Release and release back-merge  |
| `hotfix/*`      | `main`, `develop` | Hotfix and hotfix back-merge    |

The `develop` back-merge target is permitted for `release/*` and `hotfix/*`
branches because GitFlow requires stabilized fixes to return to the integration
line.

## Validation Boundary

The backend GitFlow GitHub workflow checks pull requests to `main` or `develop`
when they change backend paths such as `src/`, Gradle build files, or Gradle
wrapper files. The workflow is scoped to backend branch flow and does not
publish or validate the imported `aidlc-rules/` content.

## GitHub Settings

Some GitFlow controls belong in Git hosting settings rather than committed
files. Configure these after the `develop` branch exists:

- Protect `main` and `develop`.
- Require pull requests before merging into protected branches.
- Require the backend GitFlow check for backend delivery pull requests.
- Require backend CI checks selected by the maintainers.
- Restrict direct pushes to `main` and `develop` to the maintainers who need
  release or recovery access.
- Keep tag permissions clear for backend release and recovery procedures.

## Contributor Checklist

Before opening a backend pull request:

1. Confirm the head branch prefix matches the work type.
2. Confirm the base branch matches the table above.
3. Explain whether the pull request is backend delivery work or repository
   support work for backend delivery.
4. Document release or back-merge follow-up when the branch is `release/*` or
   `hotfix/*`.
