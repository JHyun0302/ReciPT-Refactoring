## Refactoring

1. **양방향 맵핑을 단방향 맵핑으로!!**
    - 참고 : https://www.youtube.com/watch?v=dJ5C4qRqAgA


2. **랜덤 값 추천 기능**
    - 기존 방식 : DB의 random 함수 사용해서 랜덤 값 추출
    - refactoring : `java`의 Random 함수 적용해서 미리 random PK 값을 뽑아서 DB에서 가져오게 만들기.
    - 이유
        1. `Controller`에서 분기문을 넣어줘야함. (SRP 어김)
        2. `DB 종류가 바뀌면 Repository, Service, Controller에 값을 추가하거나 빼야한다.`

4. **`jwt 구현 방식 변경`**
    - 기존 방식 : DB의 `WebSecurityConfigurerAdapter` 사용
    - refactoring : `@PreAuthorized` 사용하기
    - 이유
        1. 스프링 버전이 올라가면서 `WebSecurityConfigurerAdapter` 지원하지 않음

5. `@Bean passwordEncoder` Config class 생성
    - passwordEncoder 빈 등록하는 config class 생성

6. `@PostConstruct` 이용해 스프링 시작시 init 데이터 삽입
    - 기존 : 스프링 시작시 `InsertInitData.insertInitData` 메서드 바로 실행하는 방법
    - refactoring : `@PostConstruct` 통해 init 데이터 삽입
    - 이유
        1. 스프링 시작 클래스에 기존의 `getBean`을 통해 `InsertInitData.insertInitData` 메서드 실행하는 방식은 메인 클래스와 `InsertInitData` 클래스 사이
           의존관계 생성됨
        2. 기존 방식은 `InsertInitData` 클래스에도 필요없는 클래스의 의존 관계가 있어서 코드 리팩토링이 필요했다고 판단

### [ERD 분석 - Figma](https://www.figma.com/file/rJlqqSI2Ssyokn2VRqT2z3/ReciPT-%EB%B6%84%EC%84%9D?type=whiteboard&node-id=0-1&t=0inp0EkyTL42uJTP-0)

### [ERD 분석 - ERDCloud](https://www.erdcloud.com/d/Q7WxraMMoDsuDJS3j)