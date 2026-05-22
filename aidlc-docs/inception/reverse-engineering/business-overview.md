# Business Overview

## Business Context

The runnable product in this workspace is the TUK backend API. It serves client
flows for anonymous or device-oriented authors and confessions. The repository
also contains AI-DLC workflow rules and tooling used to guide and evaluate
agent-driven development.

Text context view:

1. A client calls the TUK HTTP API under `/tuk/api`.
2. Author operations create or fetch author records.
3. Confession operations write confessions, fetch confessions, list confessions,
   and increment reaction counts.
4. Persistence adapters store authors, confessions, and reaction counts through
   Spring Data JPA.
5. Local execution uses H2 in PostgreSQL compatibility mode, while the Railway
   profile uses PostgreSQL connection environment variables.

## Business Description

- **Backend business purpose**: Provide a simple confession service where a
  device identity can act as an author and interact with confession content.
- **Workflow material purpose**: Package AI-DLC rules, guides, release
  automation, and evaluator utilities for AI-assisted software delivery.

## Business Transactions

- **Retrieve author**: Fetch an author by public identifier.
- **Save author**: Create or replace an author-facing record from identifier
  and nickname input.
- **Write confession**: Resolve or create the author for a device id, generate
  a confession id, and persist content.
- **Get confession**: Load one confession and its current reaction counts.
- **List confessions**: Return confessions ordered by creation time with
  reaction counts.
- **React to confession**: Validate the target confession and increment one
  reaction type count.
- **Package AI-DLC rules**: Maintain and release the distributable workflow
  rules under `aidlc-rules/`.

## Business Dictionary

- **Author**: Identity that owns confession content.
- **Device id**: HTTP `X-Device-Id` value used as an author identifier for
  confession writes.
- **Confession**: User-authored content with generated id, author id, creation
  time, and reactions.
- **Reaction**: A typed counter attached to a confession.
- **AI-DLC artifact**: Workflow document generated under `aidlc-docs/` during
  guided development.

## Component Level Business Descriptions

### TUK Backend

- **Purpose**: Serve author and confession APIs.
- **Responsibilities**: Validate request shape through Kotlin types and use
  cases, orchestrate domain operations, map failures to HTTP responses, and
  persist entities.

### AI-DLC Rule Distribution

- **Purpose**: Provide reusable agent workflow rules and phase-specific details.
- **Responsibilities**: Keep the published `aidlc-rules/` contract stable and
  document how supported agents load the rules.

### Supporting Tooling

- **Purpose**: Evaluate, review, and trace AI-DLC workflow quality.
- **Responsibilities**: Host Python-based scripts and tests under `scripts/`.
