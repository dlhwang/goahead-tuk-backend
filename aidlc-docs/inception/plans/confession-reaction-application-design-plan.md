# 고해 반응 및 보안 기준 Application Design 계획

<!-- markdownlint-disable MD053 -->

## 목표

승인된 Reaction 기능과 전체 차단 Security Baseline을 기존 Spring Boot
백엔드 구조에 반영하기 위해, 구성 요소 책임과 인터페이스, 서비스
오케스트레이션 및 의존성 경계를 정의한다.

## 입력 범위

- Reaction 요구사항:
  `aidlc-docs/inception/requirements/confession-reaction-requirements.md`
- Reaction 사용자 스토리: `US-2`, `US-3`
- 실행 계획:
  `aidlc-docs/inception/plans/confession-reaction-execution-plan.md`
- 기존 구조: `confession`, `common`, Railway 배포 단서와 Spring
  Security 설정

## 설계 체크리스트

- [x] 승인된 요구사항, 스토리, 실행 계획과 기존 컴포넌트 구조를
  검토한다.
- [x] 아래 설계 질문의 답변을 받고 모순 또는 불명확성이 없는지
  분석한다.
- [x] `aidlc-docs/inception/application-design/components.md`에
  Reaction과 Security Baseline 구성 요소 책임을 정의한다.
- [x] `aidlc-docs/inception/application-design/component-methods.md`에
  주요 인터페이스와 고수준 메서드 계약을 정의한다.
- [x] `aidlc-docs/inception/application-design/services.md`에
  선택/해제 오케스트레이션, 공통 보안 제어 및 운영 증빙 책임을
  정의한다.
- [x] `aidlc-docs/inception/application-design/component-dependency.md`에
  구성 요소 의존성과 데이터 흐름을 정의한다.
- [x] `aidlc-docs/inception/application-design/application-design.md`에
  설계 결정을 통합 요약한다.
- [x] 전체 Security Baseline finding이 다음 설계 단계에서 누락되지
  않도록 추적 관계를 검토한다.

## 답변 분석 결과

- `Q1=B`: Reaction을 기존 `confession` 하위가 아닌 독립 애플리케이션
  구성 요소와 최상위 패키지로 분리한다. 기존 카운터 증가는 선택
  저장과 집계 협력으로 교체 대상이다.
- `Q2=A`: 공개 변이 요청의 throttling은 Spring Boot 공통 HTTP 제어
  구성 요소와 정책 저장소의 책임으로 설계한다.
- `Q3=A`: Railway 등 외부 운영 설정은 저장소의 검증 체크리스트와
  증빙 요구로 추적하고, 이번 범위에서 새 IaC 도입은 요구하지 않는다.
- `Q4=B`: 공급망 통제는 실행 명령과 결과 증빙으로 다룬다. CI workflow
  추가는 설계 범위에 포함하지 않지만, `SECURITY-10` 증빙이 실제로
  확보되지 않으면 전체 차단 기준을 충족한 것으로 보지 않는다.
- 네 답변은 책임 경계를 구체적으로 결정하며 상호 모순되지 않는다.
  추가 확인 질문은 필요하지 않다.

## Question 1

Reaction의 멱등 선택/해제와 조회 집계 책임은 기존 `confession` 도메인
영역에서 어떻게 구성할까?

A) 기존 `confession` 패키지 안에 반응 선택 application port와 domain
port를 추가하고, 기존 카운터 구현을 선택 저장 adapter로 교체한다.
Reaction은 별도 배포 컴포넌트가 아닌 고해 하위 기능으로 유지한다.

B) `reaction`을 독립 최상위 패키지와 애플리케이션 구성 요소로
분리하고, confession 조회가 별도 reaction 서비스와 협력해 집계한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 2

공개 API의 rate limiting은 애플리케이션 설계에서 어떤 경계로
모델링할까?

A) Spring Boot 애플리케이션 내부의 공통 HTTP 필터 또는 interceptor와
정책 저장소로 모델링하고, Reaction 변이 API를 포함한 공개 쓰기
경로에 적용한다.

B) Railway 앞단의 외부 gateway 또는 edge 구성 책임으로 모델링하고,
애플리케이션은 제한 응답 계약과 증빙 문서만 가진다.

C) 애플리케이션 내부 제어와 외부 edge 제어를 겹쳐 적용하는 방어 계층
구성으로 모델링한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 3

전체 Security Baseline의 배포/운영 증빙 구성 요소는 어디까지 이번
저장소 산출물과 코드 생성 범위로 다룰까?

A) Railway와 연결되는 운영 구성을 이 저장소에서 관리 가능한 설정 및
운영 문서 구성 요소로 정의한다. 외부 콘솔에서 설정해야 하는 TLS,
DB 암호화, 중앙 로그, 보존 및 경보는 검증 체크리스트와 증빙 요구로
추적한다.

B) 이번 저장소에 IaC 또는 자동화 가능한 운영 구성을 새로 도입하는
구성 요소로 정의하고, TLS/로그/경보/대시보드를 코드로 관리하는 것을
목표로 한다.

X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 4

공급망 보안 구성 요소는 어떤 책임 경계로 정의할까?

A) Gradle 의존성 검증/고정, SBOM 생성 및 GitHub Actions 보안 검사를
저장소 지원 구성 요소로 추가한다.

B) SBOM과 취약점 검사 명령 및 검증 증빙만 문서화하고, CI workflow
추가는 이번 범위에서 하지 않는다.

X) Other (please describe after [Answer]: tag below)

[Answer]: B
