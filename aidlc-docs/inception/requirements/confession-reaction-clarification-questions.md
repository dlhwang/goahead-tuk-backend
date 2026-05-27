# 고해 반응 MVP 추가 확인 질문

<!-- markdownlint-disable MD053 -->

## 확인된 답변

- 동일 기기는 같은 고해에 같은 반응 타입을 한 번만 남긴다.
- 동일 기기는 한 고해에 서로 다른 반응 타입을 추가로 남길 수 있다.
- 반응 API는 `X-Device-Id`를 필수로 사용한다.
- 반응 변경과 취소를 MVP에 포함한다.
- 성공한 반응 요청은 갱신된 본문 없이 처리한다.
- Security Baseline과 Property-Based Testing 확장을 적용한다.

아래 결정은 변경과 취소의 데이터 무결성 및 HTTP 계약에 직접 영향을
준다. 각 `[Answer]:` 뒤에 선택 문자와 필요한 설명을 작성해 달라.

## Question 1

동일한 기기가 한 고해에 `PRAY`와 `COMFORT`를 모두 남긴 상태에서
`PRAY`를 `COMFORT`로 변경하려고 하면 어떻게 처리해야 하는가?

A) 이미 존재하는 `COMFORT` 하나만 유지하고 `PRAY`를 삭제한다. 즉,
변경 결과는 타입당 1개 규칙에 맞게 병합한다.

B) 변경 대상 타입이 이미 존재하므로 요청을 거부하고 기존 반응 두 개를
유지한다.

C) 변경 API는 대상 타입이 아직 선택되지 않은 경우만 허용하며, 이미
선택된 타입으로의 변경 요청은 성공 응답을 반환하되 아무것도 바꾸지
않는다.

X) Other (please describe after [Answer]: tag below)

[Answer]: X

- `PRAY`를 `COMFORT`로 변경하는 API 개념은 제공하지 않는다. 반응은 단일 선택값 변경 모델이 아니라, 기기별로 각 반응 타입을 독립적으로 선택/해제하는 Set
  기반 모델로 본다. 따라서 동일 기기는 하나의 고해에 `PRAY`와 `COMFORT`를 동시에 남길 수 있다. `PRAY`를 없애려면 `PRAY`를 해제하고, `COMFORT`
  를 남기려면 `COMFORT`를 선택한다. 이미 선택된 반응 타입을 다시 선택하는 요청은 멱등하게 성공 처리한다.

## Question 2

반응 추가, 변경, 취소 API의 성공 응답과 HTTP 메서드는 어떤 계약으로
제공할까?

A) 생성은 `POST /api/confessions/{confessionId}/reactions`, 변경은
`PATCH /api/confessions/{confessionId}/reactions`, 취소는
`DELETE /api/confessions/{confessionId}/reactions/{type}`로 제공하고
모두 `204 No Content`를 반환한다.

B) 생성과 변경은 하나의
`PUT /api/confessions/{confessionId}/reactions/{type}` 멱등 API로
제공하고, 취소는 같은 경로의 `DELETE`로 제공하며 모두
`204 No Content`를 반환한다.

C) 생성만 기존 `POST`와 `204 No Content` 계약으로 구현하고, 변경과
취소의 HTTP 계약은 별도 후속 기능에서 확정한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: B

- 생성과 변경을 구분하지 않고, 타입별 반응 선택 상태를 멱등 API로 제공한다.
  `PUT /api/confessions/{confessionId}/reactions/{type}`는 해당 타입 반응을 선택한 상태로 만들고,
  `DELETE /api/confessions/{confessionId}/reactions/{type}`는 해당 타입 반응을 해제한 상태로 만든다. 모두 성공 시
  `204 No Content`를 반환한다. 이미 선택된 타입에 대한 `PUT`, 이미 해제된 타입에 대한 `DELETE`도 멱등하게 성공 처리한다.
