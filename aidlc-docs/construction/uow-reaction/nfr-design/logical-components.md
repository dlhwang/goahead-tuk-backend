# UOW-REACTION Logical Components

<!-- markdownlint-disable MD060 -->

## 논리 컴포넌트

| 컴포넌트 | 책임 | 협력 및 제약 |
| -------- | ---- | ------------ |
| `ReactionController` | `PUT`/`DELETE` 입력과 `204` 응답 경계 | 공통 validation, throttling과 안전한 오류 변환을 통과한다. |
| `SelectReactionUseCase` | 자기 반응 거부 확인 후 활성 선택 보장 | `ConfessionAuthorLookup`, `ReactionSelectionRepository`를 사용한다. |
| `ClearReactionUseCase` | 활성 선택 제거의 멱등 처리 | 행이 없어도 성공 상태를 유지한다. |
| `ReactionSelectionRepository` | 활성 선택 저장 및 삭제 port | 저장 구현은 삼중 키 유일성을 반드시 제공한다. |
| `ReactionSelectionJpaAdapter` | 짧은 트랜잭션과 유일 충돌 변환 | 관계형 저장소 유일 제약 위반 중 멱등 중복만 성공으로 해석한다. |
| `ReactionAggregateReader` | 단일 고해의 타입별 활성 count 제공 | count는 선택 행에서 파생하고 타입 누락은 응답 계층에서 `0`으로 채운다. |
| `ReactionPropertyTests` | 명령 시퀀스 및 집계 불변식 검증 | Kotest 모델 비교와 seed 출력 정책을 따른다. |

## 저장소 경계

- 별도 반응 선택 테이블은 고해 참조, 파생 기기 key, 반응 타입 및
  필요한 생성 시각만 저장한다.
- 유일 제약은 `(confession_id, device_key, reaction_type)`에 둔다.
- `Confession` 테이블에는 반응별 count 컬럼을 추가하지 않는다.

## 구현 시 검증 항목

- 중복 선택, 반복 해제, 자기 반응 및 허용하지 않은 타입의 상태 코드
- 동시에 수행한 동일 선택 뒤 활성 행 하나만 남는지 여부
- 선택 행을 임의로 생성한 PBT 집계 결과와 reader 결과의 일치
