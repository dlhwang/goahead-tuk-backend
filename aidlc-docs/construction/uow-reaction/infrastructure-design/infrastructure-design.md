# UOW-REACTION Infrastructure Design

<!-- markdownlint-disable MD060 -->

## 범위

첫 Railway 배포는 H2 메모리 database를 사용하는 일회성 demo
preview다. Reaction 선택 데이터는 demo datasource 안의 별도
테이블로 배치하며, Confession 테이블에 타입별 count 컬럼은
추가하지 않는다. 재시작 또는 재배포 시 데이터 초기화를 허용한다.

## 저장소 매핑

| 논리 요소 | 인프라 매핑 | 배포 및 무결성 요구 |
| --------- | ----------- | ------------------- |
| 활성 선택 | H2 demo Reaction 선택 테이블 | `confession_id`, HMAC 파생 기기 key, `reaction_type`, 생성 시각 저장 |
| 멱등 선택 | H2 관계형 유일 제약 | `(confession_id, device_key, reaction_type)` 중복 방지 |
| 단일 및 목록 집계 | H2 그룹 집계 질의 | 고해와 타입 기반 인덱스로 집계 읽기 지원 |
| 참조 무결성 | Confession foreign key | 삭제 정책은 기존 고해 수명주기와 일치시킨다. |

## Demo Schema 배포 결정

- 최초 demo는 전용 `application-demo.yml`에서 H2 메모리 datasource와
  Hibernate schema 생성을 사용한다.
- `application-local.yml`은 공개 Railway 배포에 재사용하지 않으며,
  demo profile에서는 H2 console과 SQL 출력, API docs 노출을
  비활성화한다.
- 기존 PostgreSQL 수동 SQL 선택은 영속 production 전환 설계 때
  다시 적용 여부를 확인한다. demo 구현의 선행 조건은 아니다.

## 인덱스 및 집계 경계

- 유일 제약은 멱등성과 경쟁 안전성의 최종 방어선이다.
- batch 집계는 고해 목록 및 반응 타입으로 그룹화할 수 있는 인덱스
  또는 동등한 JPA schema 구성을 demo에서도 제공한다.
- count는 활성 선택 행에서 파생되며 별도 counter 저장소를 도입하지
  않는다.

## 검증 및 증빙

- repository/integration 테스트는 중복 선택, 반복 해제, foreign key
  및 그룹 집계 결과를 검증한다.
- 제한된 동시성 테스트는 동일 선택 경쟁 뒤 활성 행이 하나뿐인지
  확인한다.
- H2 메모리 저장으로 인한 데이터 유실은 demo 제약으로 표시한다.
- 영속 저장소 TLS와 저장 암호화 증빙은 production 전환 조건으로
  `UOW-SECURITY`가 추적한다.
