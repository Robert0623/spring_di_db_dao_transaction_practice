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

## 07.06
### ch3-13 - Spring으로 DB연결하기
- pom.xml - mysql-connector-java(JDBC), spring-jdbc, spring-test, junit버전 변경
- DBConnectionTest - MySQL 연결 테스트, select query문 사용법
- root-context.xml - DataSource를 property태그를 이용해서 bean으로 등록
- DBConnectionTest2 - bean으로 등록된 DataSource로 DB와 연결 테스트 
- DBConnectionTest2Test - TDD, @RunWith, @ContextConfiguration, @Autowired로 DataSource를 자동 주입 받는 법

### ch3-14 - Spring으로 DB다루기 - TDD
- User - 하던대로 작성. hashCode & equals 추가
- DBConnectionTest2Test - select, insert, update, delete 작성 및 테스트코드 작성

### ch3-15 - DAO의 작성과 적용 - 1
- root-context.xml - component-scan태그 작성
- UserDao - CRUD 메서드 interface
- UserDaoImpl - UserDao 구현, 예외처리, close(), try-with-resources
- UserDaoImplTest - update 테스트 코드 작성

### ch3-16 - DAO의 작성과 적용 - 2
- index.jsp, menu.css - 홈화면
- LoginController - 로그인 - userDao주입, userDao를 이용해서 loginCheck()변경.
- RegisterController - userDao주입, userDao를 이용해서 DB에 신규회원 정보를 저장
- loginForm.jsp, registerForm.jsp, registerInfo.jsp - registerInfo에 hobby제거. 나머지는 ch2와 동일
- UserValidator, error_message.properties - error_message의 인코딩 수정, 나머지는 ch2와 동일
- pom.xml - validation-api 추가
- servlet-context.xml - resource mapping 수정, view-controller에 홈 추가, messageSource 빈 추가
- web.xml - 한글 변환 필터 추가

### ch3-17 - Transaction, Commit, Rollback
- DBConnectionTest2Test - Test코드로 Tx 테스트

## 07.07
### ch3-18 - AOP의 개념과 용어
- AopMain - Pattern, @Transactional을 붙여서 Reflection API로 AOP를 구현
- pom.xml - aspectjrt(이미 있음), spring-aop, aspectjweaver 추가
- root-context_aop.xml - component-scan태그 수정, aop:aspectj태그 추가
- MyMath - 핵심 기능(사칙연산), @Component로 bean 등록
- LogginAdvice - 부가 기능(시작, 종료 시간), @Component, @Aspect, @Around, pointcut
- AopMain2 - Root AC(root-context_aop.xml)로 MyMath클래스를 이용해서 AOP 실습

### ch3-19 - 서비스 계층의 분리와 @Transactional - 1
- A1Dao - 테스트 코드로 Tx 사용을 위해, conn(getConnection(), close()) 수정, insert, deleteAll 작성
- A1DaoTest - Tx Manager 수동 생성 및 commit, rollback에 Tx 적용 테스트

### ch3-20 - 서비스 계층의 분리와 @Transactional - 2
- root-context.xml - TxManager bean으로 등록
- A1DaoTest - bean으로 등록된 TxManager를 자동 주입받게 수정
- B1Dao - A1Dao 복사
- TxService - A1Dao, B1Dao를 주입받아서, @Transactional로 TxManager 사용, rollbackFor
- TxServiceTest - TxService의 @Transactional 테스트 코드 작성

### ch3-21 - 서비스 계층의 분리와 @Transactional - 3
- TxService - @Transactional에 propagation - REQUIRED, REQUIRES_NEW, rollbackFor 설정
- TxServiceTest - TxService의 @Transactional 테스트 코드