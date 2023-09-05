## Refactoring

1. **양방향 맵핑을 단방향 맵핑으로!!**
    - 참고 : https://www.youtube.com/watch?v=dJ5C4qRqAgA


2. **랜덤 값 추천 기능**
    - 기존 방식 : DB의 random 함수 사용해서 랜덤 값 추출
    - refactoring : `java`의 Random 함수 적용해서 미리 random PK 값을 뽑아서 DB에서 가져오게 만들기.
    - 이유
        1. `Controller`에서 분기문을 넣어줘야함. (SRP 어김)
        2. `DB 종류가 바뀌면 Repository, Service, Controller에 값을 추가하거나 빼야한다.`

3. **`Recipe` & `registerRecipe` 추상화**
    - 기존 방식 : 추상화 없이 구체화 클래스만 존재
    - refactoring : 공통 작업을 추상화 할 수 있는 인터페이스 & config 도입
    - 이유
        1. `Controller`에 분기문을 넣어서 recipe를 쓸지 registerRecipe를 쓸지 결정해야함. (SRP 어김)
        2. 역할과 구현 나누기

4. **`jwt 구현 방식 변경`**
    - 기존 방식 : DB의 `WebSecurityConfigurerAdapter` 사용
    - refactoring : `@PreAuthorized` 사용하기
    - 이유
        1. 스프링 버전이 올라가면서 `WebSecurityConfigurerAdapter` 지원하지 않음

### [ERD 분석](https://www.figma.com/file/rJlqqSI2Ssyokn2VRqT2z3/ReciPT-%EB%B6%84%EC%84%9D?type=whiteboard&node-id=0-1&t=0inp0EkyTL42uJTP-0)
