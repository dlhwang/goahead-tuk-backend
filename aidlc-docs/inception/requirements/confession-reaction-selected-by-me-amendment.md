# 고해 반응 자기 선택 조회 보완 요구사항

## 요청 요약

고해 단건 조회 시 요청 기기가 각 반응 타입을 선택했는지 확인할 수
있도록 `selectedByMe`를 반환한다. 이 조회는 `X-Device-Id`를
입력으로 받는다.

응답 예시는 다음과 같다.

```json
{
  "id": "confession_123",
  "reactions": [
    {
      "type": "PRAY",
      "count": 3,
      "selectedByMe": true
    },
    {
      "type": "COMFORT",
      "count": 2,
      "selectedByMe": true
    },
    {
      "type": "TOGETHER",
      "count": 0,
      "selectedByMe": false
    }
  ]
}
```

## 기존 결정과의 변경점

기존 `UOW-CONFESSION-REACTION-INTEGRATION`은 조회가 집계만
반환하고 개별 선택 상태는 노출하지 않도록 설계되었다. 이번
보완안은 raw 기기 식별자를 노출하지 않으면서, 요청 기기 본인의
타입별 선택 여부만 단건 조회 응답에 포함하도록 그 결정을 좁게
수정한다.

## 기능 요구사항

### FR-SBM-1 단건 조회 입력

`GET /api/confessions/{confessionId}`는 `X-Device-Id` 헤더를
필수로 받는다.

```http
GET /api/confessions/{confessionId}
X-Device-Id: {deviceId}
```

- 헤더 값은 기존 반응 선택 및 해제 API와 같은 형식 검증을 적용한다.
- 누락되거나 유효하지 않은 값은 안전한 `400 Bad Request`로
  거부한다.

### FR-SBM-2 타입별 자기 선택 상태

성공한 단건 조회 응답의 각 반응 항목은 다음 값을 포함한다.

| 필드           | 의미                                      |
| -------------- | ----------------------------------------- |
| `type`         | 허용된 반응 타입                          |
| `count`        | 해당 타입의 전체 활성 선택 수             |
| `selectedByMe` | 요청 기기가 해당 타입을 선택했으면 `true` |

- 응답은 항상 `PRAY`, `COMFORT`, `TOGETHER` 세 항목을 포함한다.
- 요청 기기의 선택이 없는 타입도 `count`와 `selectedByMe: false`를
  포함한다.
- 한 기기가 여러 타입을 선택한 경우 각 타입에 독립적으로 `true`를
  반환한다.

### FR-SBM-3 범위 제한

- 이번 보완의 API 계약 변경 대상은 단건
  `GET /api/confessions/{confessionId}`다.
- 목록 `GET /api/confessions`와 작성 응답은 현재 집계 응답 계약을
  유지하며 `X-Device-Id` 요구 또는 `selectedByMe`를 추가하지
  않는다.
- 반응 선택 및 해제의 기존 `PUT`/`DELETE` 계약은 변경하지 않는다.

## 데이터 및 보안 요구사항

### DR-SBM-1 저장 모델 유지

`selectedByMe`는 이미 저장된 반응 선택 행과 요청 기기의 파생 값이다.
별도 상태 컬럼 또는 `Confession` 타입별 카운트 컬럼을 추가하지
않는다.

### NFR-SBM-1 기기 식별 보호

- `X-Device-Id` 원문은 응답과 기능 로그에 포함하지 않는다.
- 저장 조회에는 기존 `DeviceKeyHasher`를 사용해 파생한 기기 키만
  사용한다.
- 자기 선택 여부는 요청 기기 본인의 선택 타입에 한정하며, 다른
  기기의 선택 행 또는 식별자를 공개하지 않는다.

### NFR-SBM-2 안전한 조회와 오류

- 고해 식별자와 `X-Device-Id`는 기존 allowlist 및 길이 제한을
  유지하거나 동일 수준으로 검증한다. (`SECURITY-05`)
- 집계 또는 자기 선택 조회 실패를 정상 응답의 `false`로 대체하지
  않고 안전한 실패 경계로 전달한다. (`SECURITY-15`)
- JPA 매개변수 기반 조회만 사용한다. (`SECURITY-05`)

## Property-Based Testing 요구

- 임의의 요청 기기 선택 집합에 대해 각 타입의 `selectedByMe`는
  해당 기기의 활성 선택 포함 여부와 일치해야 한다. (Oracle)
- 세 타입의 전체성 및 타입별 count 불변식은 기존 집계 속성과 함께
  유지되어야 한다. (Invariant)
- 예제 기반 테스트는 여러 타입 동시 선택, 선택 없음, 잘못된
  `X-Device-Id` 입력을 명시적으로 검증한다.

## 수용 기준

1. 유효한 `X-Device-Id`를 포함한 단건 조회는 세 반응 타입별
   `count`와 `selectedByMe`를 반환한다.
2. 동일 기기가 여러 반응을 선택했다면 각 선택 타입이 각각
   `selectedByMe: true`로 반환된다.
3. 단건 조회에서 기기 식별 헤더가 없거나 유효하지 않으면 안전하게
   거부된다.
4. 목록 조회 및 작성 응답 계약은 이번 변경으로 확장되지 않는다.
5. 원문 기기 ID와 다른 기기의 선택 정보는 응답 또는 로그로
   노출되지 않는다.

## 판단 및 확인 상태

- 사용자 요청이 단건 `get` 메서드와 `X-Device-Id` 필요성을
  명시했으므로 별도 확인 질문 없이 요구사항 보완안을 작성했다.
- `Security Baseline`과 `Property-Based Testing` 확장은 기존 Reaction
  MVP 결정에 따라 계속 활성 상태로 적용한다.
- 이 요구사항 보완안은 구현 전에 명시적 검토 승인을 받아야 한다.
