# UOW-REACTION Tech Stack Decisions

## 유지할 기술 경계

- 애플리케이션 런타임은 기존 Kotlin 및 Spring Boot 백엔드를
  유지한다.
- 영속성은 기존 JPA repository adapter 경계를 유지하고,
  Reaction 선택용 별도 테이블과 유일성 제약을 사용한다.
- Reaction은 기존 배포 단위 안의 독립 최상위 모듈이다.

## 테스트 결정

- Property-based testing framework는 공통 선택인
  `Kotest Property Testing`을 사용한다. (`PBT-09`)
- 도메인 generator는 허용 타입, 유효 고해, 서로 다른 작성/반응
  기기 및 선택/해제 명령 시퀀스를 생성할 수 있어야 한다.
- 실패 시 seed와 축소된 최소 입력을 출력하며 backend test와 CI에서
  실행한다. (`PBT-08`)
- 예제 기반 테스트는 PBT를 대체하지 않으며 핵심 API 계약을 고정한다.

## 성능 결정

- 단일 고해 집계 및 변이 요청의 목표는 `p95 <= 500 ms`다.
- 초기 변이 처리 용량 목표는 단일 인스턴스 지속 `20 requests/second`다.
- 구체적인 측정 도구 및 fixture는 NFR Design 또는 Build and Test
  지침에서 정한다.
