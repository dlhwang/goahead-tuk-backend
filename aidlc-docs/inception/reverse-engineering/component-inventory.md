# Component Inventory

## Application Packages

- `io.goahead.tuk.author` - Author HTTP, application, domain, and persistence layers.
- `io.goahead.tuk.confession` - Confession HTTP, application, domain, reactions, and persistence layers.
- `io.goahead.tuk.common` - Shared API exceptions, identifier support, and Spring security configuration.

## Infrastructure Packages

- `src/main/resources` - Spring profile and datasource configuration.
- `.github/workflows` - CI, release, PR validation, security scanning, and CodeBuild automation.
- Repository-root container and deployment files - `Dockerfile` and `railway.json` are present in the current working tree.

## Shared Packages

- `aidlc-rules/aws-aidlc-rules` - Core workflow entry point for distributed AI-DLC rules.
- `aidlc-rules/aws-aidlc-rule-details` - Common, inception, construction, operations, and extension rule details.
- `docs` - Contributor, workflow, operations, and writing-input documentation.

## Tooling Packages

- `scripts/aidlc-evaluator` - Evaluation framework.
- `scripts/aidlc-designreview` - Design review helpers and tests.
- `scripts/aidlc-traceability` - Traceability helpers and agent integration code.

## Test Packages

- `src/test/kotlin/io/goahead/tuk` - Spring application and use case tests for the backend.
- Script test directories under `scripts/` - Python-focused tests for AI-DLC tooling.

## Total Count

- **Backend domain areas**: 3
- **Backend source files under `src/` scanned**: 61
- **AI-DLC rule distribution roots**: 2
- **Script workspaces identified**: 3
