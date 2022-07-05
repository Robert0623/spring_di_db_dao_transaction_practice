# 스프링 DI, DB연동, TDD, DAO, Transaction, 계층 분리 복습 및 훈련

## 07.05
### ch3-1 ~ ch3-3 - Spring DI 흉내내기
- Main1 - 다형성, factory method로 map과 외부파일(config.xml) 연결
- Main2 - 객체 컨테이너(ApplicationContext)로 객체 저장소 구현
- Main3 - guava-api로 Component-scan 구현
- Main4 - @Component, @Autowired, @Resource

### ch3-4 - Spring DI 활용하기 - 실습
- config1.xml - bean으로 클래스 등록, bean으로 초기화(setter, 생성자 이용)
- config.xml - component-scan태그 등록
- SpringDiTest - @Component, @Autowired, @Qualifier, @Resource, @Value, 

### ch3-5 - Spring DI 활용하기 - 이론 1
- bean이란? - 재사용 가능한 컴포넌트로 Spring container가 관리
- ApplicationContext(AC)의 종류 - non-Web(XML, Java Config), Web(XML, Java Config)
- web.xml - Root AC - 부모, 공통(non-Web, DB관련 bean), Servlet AC - 자식, 개별(Web관련 bean)

### ch3-6 - Spring DI 활용하기 - 이론 2
- Root AC, Servler AC - 톰캣 시작 후 각각 초기화 부분의 log 확인
- servlet-context.xml - 정규식으로 conponent-scan태그 수정
- HomeController - Root AC, Servlet AC의 메서드

### ch3-7 - Spring DI 활용하기 - 이론 3
- ApplicationContextTest - IoC, DI, @Autowired 

### ch3-8 - Spring DI 활용하기 - 이론 4
- setting.properties - @Value, @PropertySource
- ApplicationContextTest - 시스템 환경변수, 시스템 프로퍼티