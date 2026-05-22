# API Documentation

## REST APIs

Base context path: `/tuk`

- **Get author**: `GET /api/authors/{authorId}` retrieves an author by id from
  path variable `authorId` and returns `AuthorResponse`.
- **Save author**: `POST /api/authors` accepts `SaveAuthorRequest` and returns
  `AuthorResponse`.
- **Write confession**: `POST /api/confessions` accepts header `X-Device-Id`
  and `WriteConfessionRequest`, then returns `ConfessionResponse` with HTTP
  `201`.
- **Get confession**: `GET /api/confessions/{confessionId}` retrieves one
  confession and returns `ConfessionResponse`.
- **List confessions**: `GET /api/confessions` returns a list of
  `ConfessionResponse`.
- **React to confession**: `POST /api/confessions/{confessionId}/reactions`
  accepts `ReactConfessionRequest` and returns HTTP `204`.

## Error Mapping

- **Missing author**: HTTP `404` from `AuthorNotFoundException` handled by
  `ApiExceptionHandler`.
- **Missing confession**: HTTP `404` from `ConfessionNotFoundException` handled
  by `ApiExceptionHandler`.
- **Illegal argument**: HTTP `400` from `IllegalArgumentException` handled by
  `ApiExceptionHandler`.

## Internal APIs

- **`AuthorRepository`**: `findById` and `save` persistence boundary for author
  data.
- **`ConfessionRepository`**: `existsById`, `findById`, `findAll`, and `save`
  persistence boundary for confession data.
- **`WriteConfessionUseCase`**: `execute` write confession orchestration
  boundary.
- **`GetConfessionUseCase`**: `execute` single confession retrieval boundary.
- **`ListConfessionsUseCase`**: `execute` confession list retrieval boundary.
- **`ReactConfessionUseCase`**: `execute` reaction increment orchestration
  boundary.
- **`ConfessionReactionCounter`**: `increase` reaction increment abstraction.
- **`ConfessionIdGenerator`**: `generate` identifier generation abstraction.

## Data Models

### Author

- **Fields found**: author id, nickname, creation timestamp.
- **Persistence**: JPA entity and repository adapter in `author/infrastructure`.

### Confession

- **Fields found**: confession id, author id, content, reaction counts,
  creation timestamp.
- **Validation**: Value object constructors and enum parsing may reject invalid
  ids, content, or reaction type values through `IllegalArgumentException`.

### Reaction Counts

- **Fields found**: reaction type and count items grouped per confession.
- **Persistence**: Dedicated reaction JPA entity and repository queries.
