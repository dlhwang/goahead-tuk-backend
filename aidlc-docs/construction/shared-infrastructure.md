# Confession Reaction MVP 공유 인프라 설계

<!-- markdownlint-disable MD060 -->

## 공유 대상

| 공유 인프라 | 사용하는 단위 | 결정 |
| ----------- | ------------- | ---- |
| Railway Spring Boot 서비스 | Reaction, Confession Reaction Integration, Security | 기존 서비스에 기능과 공통 filter를 추가하며 demo는 단일 instance 전제로 둔다. |
| H2 메모리 demo datasource | Reaction, Confession Reaction Integration | 전용 demo profile에서 테이블을 만들고 재시작 시 데이터 초기화를 허용한다. |
| Railway secret 공급 | Security, Reaction | `DEVICE_KEY_HMAC_SECRET`을 환경 secret으로 공급한다. |
| 공개 ingress | 전체 API | demo는 기존 HTTPS ingress를 사용하고 production 전 access logging/network 증빙을 요구한다. |
| 중앙 관측 | 전체 API | demo는 구조화 로그를 출력하고 production 전 보존, 경보와 dashboard 기준을 증빙한다. |
| 공급망 증빙 | 배포 artifact 전체 | 공식 Temurin digest, Gradle verification, SBOM 및 취약점 검사를 요구한다. |

## 저장소와 배포 책임 경계

- 저장소 변경은 Reaction 도메인, SQL 전달 산출물, 보안 구성,
  테스트와 공급망 task를 제공한다.
- 운영자는 demo의 데이터 초기화 제약과 secret 공급을 확인한다.
- 영속 production으로 승격할 때 schema 변경, 저장 암호화/TLS,
  ingress와 중앙 관측 증빙을 제공한다.
- demo 배포는 production Security Baseline 충족 판정이 아니며,
  이월된 운영 증빙은 production 전환 전 차단 finding이다.

## 비적용 인프라

- 별도 Reaction microservice, read replica, message broker,
  distributed cache와 API gateway는 MVP 설계에 추가하지 않는다.
- 영속 저장, 수평 확장 또는 production 승격을 원하면 위 공유
  인프라 결정을 다시 설계해야 한다.
