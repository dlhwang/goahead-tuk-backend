# Personas

## Persona 1: Anonymous Confession Writer

### Profile

- **Identity**: A person using the confession service from a device identified
  by `X-Device-Id`.
- **Account model**: Does not create or manage a member account for the MVP.
- **Primary goal**: Write a confession immediately without a separate author
  registration step.

### Needs

- The first confession from a device should succeed when request data is valid.
- Later confessions from the same device should continue to use the same
  anonymous author identity.
- The service should not expose persistence or author setup details as friction
  in the write flow.

### Frustrations To Avoid

- Receiving a missing-author response before the first confession can be
  submitted.
- Being forced into a registration flow that is outside MVP scope.
- Seeing inconsistent confession write behavior between first and later writes
  from the same device.

### Related Stories

- US-1 Anonymous Device Writes A Confession

## Persona 2: 익명 고해 열람자

### 프로필

- **정체성**: `X-Device-Id`로만 구별되는 기기에서 다른 사람의 고해를
  읽는 익명 사용자.
- **계정 모델**: 회원 계정이나 공개 프로필 없이 반응한다.
- **주요 목표**: 문장을 남기거나 자신을 드러내지 않고 정해진 지지
  표현으로 고해에 응답한다.

### 필요

- 서비스가 허용한 긍정적 반응만 간단히 선택하거나 해제할 수 있어야
  한다.
- 같은 반응을 반복해서 눌러도 의도치 않게 수가 증가하지 않아야 한다.
- 여러 종류의 지지 표현을 동시에 전할 수 있어야 한다.
- 고해에 모인 익명 지지의 타입별 집계를 볼 수 있어야 한다.

### 피하고 싶은 경험

- 자유 텍스트나 공개 댓글을 작성해야만 마음을 표현할 수 있는 흐름.
- 자신의 기기 식별 정보가 조회 응답이나 로그를 통해 드러나는 상황.
- 정상적인 반응 선택이 남용 방지 정책으로 과도하게 차단되는 상황.

### 관련 스토리

- US-2 익명 열람자가 지지 반응을 선택하고 해제한다
- US-3 익명 열람자가 고해의 지지 반응 집계를 확인한다
