# 고해 반응 Units Generation 계획

<!-- markdownlint-disable MD053 MD060 -->

## 목표

승인된 Reaction Application Design을 구현 가능한 작업 단위로 분해한다.
기존 익명 고해 작성 흐름의 승인 산출물은 보존하고, 이번 Reaction
변경과 전체 Security Baseline 준비 상태를 담당할 단위 정의를
확정한다.

## 확정된 입력

- `reaction`은 단일 Spring Boot 배포 안의 독립 최상위 모듈이다.
- Reaction 기능 스토리는 `US-2` 선택/해제와 `US-3` 집계 조회다.
- 공개 쓰기 rate limiting은 애플리케이션 내부 공통 보호 구성
  요소의 책임이다.
- 운영 및 공급망 증빙은 이 저장소에서 추적하되, 증빙 부재는 전체
  Security Baseline 통과로 인정되지 않는다.
- Property-Based Testing은 Reaction 상태 불변식에 적용한다.

## 계획 체크리스트

- [x] 승인된 요구사항, 사용자 스토리와 Application Design을 읽고
  분해 입력을 확인한다.
- [x] 아래 단위 분해 질문에 대한 답변을 받고 모순 또는 모호성이
  없는지 검토한다.
- [x] 답변에 따라 기존
  `aidlc-docs/inception/application-design/unit-of-work.md`를
  Reaction 활성 변경을 포함하도록 갱신한다.
- [x] `aidlc-docs/inception/application-design/unit-of-work-dependency.md`에
  기능, 공통 보안 및 증빙 단위의 의존성과 전달 순서를 갱신한다.
- [x] `aidlc-docs/inception/application-design/unit-of-work-story-map.md`에
  `US-2`, `US-3` 및 보안 차단 책임의 매핑을 갱신한다.
- [x] 모든 승인된 스토리와 전체 Security Baseline 책임이 단위에
  빠짐없이 할당되었는지 검증한다.
- [ ] 생성된 단위 산출물의 사용자 승인을 요청하고 승인받는다.

## Question 1

Reaction의 두 사용자 스토리는 어떤 기능 단위로 묶을까?

A) `US-2` 선택/해제와 `US-3` 집계 조회를 하나의 `UOW-REACTION`으로
묶는다. 동일 선택 데이터 모델과 집계 불변식을 함께 구현하고
검증한다.

B) 선택/해제와 집계 조회를 서로 다른 Reaction 작업 단위로 분리한다.
선택 저장을 먼저 구현하고 조회 집계가 이후 연동한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: B

## 답변 분석 결과

- `Q1=B`는 반응 선택/해제와 반응 집계 조회를 별도 Reaction 작업
  단위로 분리한다.
- `Q2=A`는 공통 보호 및 증빙을 별도 `UOW-SECURITY`로 분리한다.
- `Q3=A`는 기능과 보안 작업의 병행을 허용하되 보안 적용 및 증빙
  판정 이후에만 최종 통합 검증과 Build and Test 승인을 허용한다.
- `Q4=A`는 모든 단위가 하나의 백엔드 팀과 단일 Spring Boot 배포
  안에서 전달된다는 뜻이다.
- `Q5=B`는 Confession 조회 응답 결합까지 별도 통합 작업 단위로
  분리한다.

`Q1` 및 `Q5`의 결과로 기능 관련 작업은 적어도 세 단위가 되지만,
`Q2`와 `Q3`의 선택지는 단일 `UOW-REACTION`과 `UOW-SECURITY` 관계로
기술되어 있다. 따라서 보안 단위가 어느 기능 단위들과 어떤 최종
검증 게이트로 연결되는지 추가 확인이 필요하다. 해당 확인 전에는
단위 산출물을 생성하지 않는다.

## 확인 답변 반영

- 확인 `Q1=B`: 선택/해제와 집계 port 제공은 하나의
  `UOW-REACTION`으로 묶고, Confession 조회 응답 결합은
  `UOW-CONFESSION-REACTION-INTEGRATION`으로 분리한다.
- 확인 `Q2=A`: `UOW-REACTION`,
  `UOW-CONFESSION-REACTION-INTEGRATION`, `UOW-SECURITY`는 병행
  구현할 수 있으나, 공개 변이 API와 조회 결합의 통합 검증 및 전체
  Build and Test 승인은 `UOW-SECURITY`의 공통 보호 적용과 전체
  Security Baseline 증빙 판정 이후에만 가능하다.
- 앞선 `Q1=B`의 세부 분리는 확인 답변에서 수정되어 선택 저장과 집계
  port가 한 기능 단위로 합쳐졌다. 앞선 `Q5=B`의 Confession 통합
  분리는 그대로 유지된다.
- 모순과 미해결 모호성은 없다. 승인 후 아래 세 단위를 산출물로
  생성한다.

## 승인 후 생성할 단위

| 단위 | 책임 | 주요 연결 |
| ---- | ---- | --------- |
| `UOW-REACTION` | 반응 선택/해제, 선택 저장, aggregate port, PBT 상태 규칙 | Confession 존재 확인, Integration에 집계 제공 |
| `UOW-CONFESSION-REACTION-INTEGRATION` | 고해 단건/목록 응답에 반응 집계 결합 | `UOW-REACTION` aggregate port 소비 |
| `UOW-SECURITY` | 공통 throttling, 안전한 로그/오류/헤더, 운영 및 공급망 증빙 | 두 기능 단위의 최종 검증 차단 게이트 |

## Question 2

전체 차단 Security Baseline의 공통 보호와 증빙 책임은 기능 단위와
어떻게 분해할까?

A) `UOW-SECURITY`라는 별도 지원 단위로 분리한다. 공통 rate limiting,
로그/오류/헤더 통제와 운영/공급망 증빙을 맡고 `UOW-REACTION`과
통합 검증한다.

B) 별도 보안 단위를 두지 않고 모든 보안 통제와 증빙 작업을
`UOW-REACTION`의 일부로 묶는다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 3

Reaction 기능과 공통 보안 작업의 수행 및 검증 순서는 어떻게 둘까?

A) 두 단위를 병행할 수 있지만, Reaction 공개 변이 API의 통합 검증과
Build and Test 승인은 `UOW-SECURITY`의 적용 및 증빙 판정 이후에만
가능하게 한다.

B) `UOW-SECURITY`를 먼저 완료하고 승인한 뒤에만 `UOW-REACTION`
구현을 시작한다.

C) `UOW-REACTION` 기능을 먼저 완료하고 이후 보안 단위가 전체 공개
경로를 보완한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 4

두 논리 단위의 소유권과 전달 형태는 어떻게 기록할까?

A) 하나의 백엔드 팀이 동일한 Spring Boot 배포 단위 안에서 순차 또는
병행 구현하는 논리 작업 단위로 기록한다. 별도 서비스 배포는 없다.

B) 같은 배포 단위이지만 Reaction 기능과 Security 준비 상태를 서로
다른 담당자가 독립적으로 인수인계 가능한 작업 스트림으로 기록한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 5

Confession 조회 응답에 Reaction 집계를 결합하는 변경은 어느 단위가
소유할까?

A) `UOW-REACTION`이 Reaction aggregate port와 함께 Confession 읽기
연동 변경까지 소유한다. 고해 조회는 Reaction 집계의 소비자다.

B) 기존 Confession 흐름을 별도 통합 작업 단위로 두고,
`UOW-REACTION`은 집계 port 제공까지만 책임진다.

X) Other (please describe after [Answer]: tag below)

[Answer]: B
