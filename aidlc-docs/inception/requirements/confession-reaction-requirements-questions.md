# 고해 반응 MVP 요구사항 확인 질문

<!-- markdownlint-disable MD053 -->

## 작업 배경

이번 변경은 고해를 읽은 익명 사용자가 자유 텍스트나 댓글 없이 서비스가
허용한 긍정적 반응을 남길 수 있게 하는 백엔드 기능이다. 요청에서 확정된
MVP 반응 타입은 다음 세 가지이다.

- `PRAY`: 기도할게요
- `COMFORT`: 토닥토닥
- `TOGETHER`: 함께할게요

또한 반응은 `Confession` 테이블의 타입별 카운트 컬럼으로 저장하지 않고
별도 도메인과 테이블로 분리한다.

현재 저장소에는 별도 `confession_reaction` 집계 엔터티와 반응 API의
초기 구현이 있으나 반응 타입과 반복 반응 정책이 이번 요청과 일치하는지
확정되지 않았다. 아래 질문의 각 `[Answer]:` 뒤에 선택 문자와 필요한
설명을 작성해 달라.

## Question 1

동일한 익명 기기가 같은 고해에 반응을 반복해서 누르는 경우 MVP 동작은
무엇이어야 하는가?

A) 클릭할 때마다 집계한다. 동일 기기의 같은 타입 반복 반응과 여러 타입
반응을 모두 허용한다.

B) 고해별로 기기당 한 번만 반응할 수 있다. 이미 어떤 타입이든 반응했다면
추가 요청은 집계하지 않는다.

C) 고해와 반응 타입별로 기기당 한 번만 반응할 수 있다. 다른 타입 반응은
추가로 남길 수 있다.

X) Other (please describe after [Answer]: tag below)

[Answer]: C

## Question 2

반응 생성 API에서 익명 반응 주체 식별을 어떻게 처리해야 하는가?

A) 반복 집계를 허용하므로 식별 헤더 없이 반응 타입만 받는다.

B) 작성 API와 동일하게 `X-Device-Id` 헤더를 필수로 받아 중복 방지나
반응 변경 판단에 사용한다.

C) MVP에서는 `X-Device-Id`를 선택적으로 받아 기록하되 중복 방지에는
사용하지 않는다.

X) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 3

사용자가 이미 남긴 반응에 다시 접근하는 경우 반응 변경 또는 취소가 MVP
범위에 포함되는가?

A) 포함하지 않는다. 이번 범위는 허용된 반응 추가와 조회 집계만 구현한다.

B) 반응 타입 변경은 포함하고 취소는 포함하지 않는다.

C) 반응 타입 변경과 취소를 모두 포함한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: C

## Question 4

반응 요청이 성공한 뒤 API 응답 계약은 무엇으로 할까?

A) 기존 초기 구현처럼 `204 No Content`를 반환하고, 갱신된 집계는 고해
조회 API에서 확인한다.

B) `200 OK`와 함께 갱신된 해당 고해 및 반응 집계를 반환한다.

C) `201 Created`와 함께 생성된 반응 또는 갱신 집계를 반환한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 5

이번 AI-DLC 실행에서 보안 확장 규칙을 적용할까?

A) 적용한다. Security Baseline 규칙을 차단 조건으로 적용한다.

B) 적용하지 않는다. 현재 기록된 설정처럼 이 기능에서도 비활성화한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 6

이번 AI-DLC 실행에서 property-based testing 확장 규칙을 적용할까?

A) 적용한다. 적용 가능한 반응 도메인 및 직렬화 로직에 차단 조건으로
적용한다.

B) 일부 적용한다. 순수 도메인 함수와 직렬화 왕복에만 적용한다.

C) 적용하지 않는다. 예제 기반 단위 및 통합 테스트로 검증한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A
