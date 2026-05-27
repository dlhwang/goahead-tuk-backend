# 고해 반응 Services

<!-- markdownlint-disable MD060 -->

## 서비스 구조

서비스는 하나의 Spring Boot 배포 단위 안에서 동작한다. 이 문서의
`Reaction`은 독립 배포 서비스가 아니라 독립 애플리케이션 모듈이며,
기존 `confession` 모듈과 명시적인 port를 통해 협력한다.

## Reaction Selection Service

### 책임

- 반응 선택 및 해제 HTTP 요청에서 전달된 값을 도메인 입력으로
  변환한다.
- 대상 고해 존재를 확인한다.
- 선택 저장 또는 해제 port를 호출해 멱등 사용 사례를 수행한다.
- 허용 타입 외 입력이나 부적절한 식별 값을 안전한 요청 오류로
  전달한다.

### 오케스트레이션

1. Public API Protection이 요청 제한 및 공통 입력 보호를 수행한다.
2. Reaction API가 경로 변수와 `X-Device-Id`를 수신한다.
3. Reaction Selection Service가 고해 존재와 도메인 입력을 확인한다.
4. Reaction Persistence가 선택 행을 저장하거나 삭제한다.
5. 성공 결과는 본문 없는 `204 No Content`로 반환된다.

## Reaction Aggregate Collaboration

### 책임

- Reaction Persistence는 고해 ID 하나 또는 목록에 대해 활성 선택을
  타입별 집계로 조회한다.
- Confession Read Application은 자기 고해 데이터와 집계 결과를 결합해
  응답을 생성한다.
- 집계 응답에는 기기 식별자 또는 선택 주체별 세부 정보가 포함되지
  않는다.

### 교체 대상

- 현재 `confession` 영역의 카운터 증가 서비스와 타입별 집계 행 저장은
  이 협력 방식과 맞지 않는다.
- 후속 구현에서는 새 선택 저장소가 집계의 원본이 되고 기존 카운터
  동작은 제거 또는 교체된다.

## Public API Protection Service

### 책임

- Spring filter 또는 interceptor에서 공개 쓰기 경로의 남용 제한을
  수행한다.
- 반응 변이와 기존 고해 작성 경로에 동일 정책 체계를 적용할 수
  있도록 구성한다.
- 원문 device ID가 관측 로그에 남지 않는 rate-limit key 처리 방식을
  NFR Design에서 확정한다.
- HTTP security headers, CORS 허용 범위, 안전한 전역 오류 응답과
  연결된다.

## Security Evidence Services

### Operational Evidence

- Railway 및 PostgreSQL 운영 환경의 전송/저장 암호화 증빙을 추적한다.
- 중앙 로그, 보존 기간, 경보와 대시보드 존재 여부를 체크리스트와
  결과 기록으로 관리한다.
- 저장소에서 직접 구성할 수 없는 설정은 외부 검증 결과를 요구하며,
  미제출 상태는 통과가 아니다.

### Supply Chain Evidence

- SBOM 생성, 의존성/컨테이너 이미지 취약점 검사 및 출처 확인을
  실행 가능한 절차와 결과 기록으로 관리한다.
- 사용자가 선택한 경계에 따라 새 GitHub Actions security workflow는
  이 설계에서 추가 요구하지 않는다.
- 검사 증빙이 없거나 차단 수준 취약점이 해결되지 않으면
  `SECURITY-10`은 Build and Test 승인 전에 열린 finding으로 남는다.

## 후속 단계로 전달할 결정

| 후속 단계 | 전달할 설계 입력 |
| --------- | ---------------- |
| Units Generation | Reaction 기능 단위와 보안 증빙/공통 제어 단위의 분리 및 의존성 |
| Functional Design | 선택/해제 멱등성, 유일성, 집계 불변식, PBT 대상 상태 |
| NFR Requirements/Design | 제한 응답, 정책 저장소, 로그 비노출, 보안 헤더, 증빙 판정 |
| Infrastructure Design | Railway/DB 암호화, 중앙 로그, 보존/경보/대시보드 증빙 |
| Build and Test | SBOM/취약점 검사 결과 및 Security Baseline compliance summary |
