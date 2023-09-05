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

### [ERD 분석](https://www.figma.com/file/rJlqqSI2Ssyokn2VRqT2z3/ReciPT-%EB%B6%84%EC%84%9D?type=whiteboard&node-id=0-1&t=0inp0EkyTL42uJTP-0)
