## 1. 프로젝트 개요

- **프로젝트명:** Study Match (스터디/프로젝트 팀원 매칭 플랫폼 백엔드)
- **GitHub 주소:** https://github.com/kokohama8572/study-match.git
- **프로젝트 설명:**
    
    공부나 프로젝트를 함께할 팀원을 구할 수 있는 프로그램입니다. Spring Security와 JWT를 도입해 안전한 회원 인증을 도입하고 JPA를 이용해 사용자와 계시글의 관계를 1대 다수로 설계했습니다.
    

## 2. 개발 환경 및 기술 스택

- **OS:** macOS
- **IDE:** IntelliJ IDEA
- **Language:** Java 21
- **Framework:** Spring Boot 4.1.0
- **Database:** MySQL 9.x, Spring Data JPA
- **Security:** Spring Security, JJWT (0.12.5)
- **Build Tool:** Gradle

## 3. 핵심 기능 명세서

### User (사용자 인증)

| **기능** | **HTTP Method** | **URL Path** | **설명** | **권한** |
| --- | --- | --- | --- | --- |
| **회원가입** | Post | `/api/auth/signup` | 이메일과 비밀번호를 입력받아 새로운 회원을 생성합니다. 비밀번호는 BCrypt 알고리즘으로 안전하게 암호화되어 저장됩니다. | 누구나 |
| **로그인** | Post | `/api/auth/login` | 가입된 정보로 인증을 진행하고, 성공 시 접근 권한이 담긴 JWT 토큰을 발급하여 반환합니다. | 누구나 |

### Recruit (스터디 모집글 CRUD)

| **기능** | **HTTP Method** | **URL Path** | **설명** | **권한** |
| --- | --- | --- | --- | --- |
| **게시글 작성** | `POST` | `/api/recruits` | JWT 토큰을 통해 인증된 사용자가 새로운 모집글(제목, 내용)을 작성합니다. | **인증된 유저** |
| **목록 조회** | `GET` | `/api/recruits` | 현재 등록된 모든 스터디 모집글의 목록(작성자, 마감 여부 포함)을 조회합니다. | **인증된 유저** |
| **게시글 수정** | `PUT` | `/api/recruits/{id}` | 특정 게시글의 내용을 수정합니다. 해당 글을 작성한 본인만 수정할 수 있도록 권한을 검증합니다. | **작성자 본인** |
| **게시글 삭제** | `DELETE` | `/api/recruits/{id}` | 특정 게시글을 삭제합니다. 역시 작성자 본인 여부를 서버 단에서 철저히 검증합니다. | **작성자 본인** |

## 4. Auth System

본 프로젝트는 세션 대신 **Spring Security와 JWT를 결합한 토큰 기반 인증 아키텍처**를 채택하여 서버의 확장성을 높였습니다.

### ⚙️ 시스템 동작 흐름

1. **토큰 발급 (Login):**
    
    사용자가 로그인에 성공하면 `JwtProvider` 클래스에서 서버의 비밀키(Secret Key)를 사용해 사용자의 이메일과 권한(Role) 정보가 담긴 JWT를 생성하여 발급합니다.
    
2. **API 요청 (Request):**
    
    클라이언트는 이후 보안이 필요한 API(예: 게시글 작성)를 호출할 때, HTTP Header의 `Authorization` 필드에 `Bearer {토큰}` 형태로 토큰을 동봉하여 전송합니다.
    
3. **토큰 검증 (Security Filter):**
    
    서버로 들어오는 모든 요청은 가장 먼저 커스텀 필터인 `JwtAuthenticationFilter`를 거치게 됩니다.
    
    - 토큰의 존재 여부 및 위변조 여부를 서명(Signature)을 통해 검증합니다.
    - 토큰이 유효하다면 내부에 담긴 사용자 정보를 추출하여 Spring Security의 `SecurityContext`에 인증 객체(`Authentication`)로 저장합니다.
4. **인가 및 비즈니스 로직 처리:**
    
    인증이 완료된 요청만 Controller로 전달되며, 비즈니스 로직(Service) 처리 시 `@Transactional` 내에서 현재 로그인한 사용자의 정보를 안전하게 꺼내어 사용할 수 있습니다.
    

### 보안 고려 사항

- **비밀번호 암호화:** `SecurityConfig`에 등록된 `BCryptPasswordEncoder`를 단방향 해시 함수로 사용하여, DB가 탈취되더라도 비밀번호 원문을 알 수 없도록 방어했습니다.
- **Stateless 정책:** 설정에서 세션 생성 정책을 `STATELESS`로 명시하여, 서버가 클라이언트의 상태를 기억하지 않도록 강제함으로써 JWT의 장점을 극대화했습니다.

## 5. 데이터베이스 설계

JPA(Hibernate)를 이용하여 객체 지향적인 데이터 모델링을 진행했습니다.

- **Users 테이블:** 회원의 고유 ID, 이메일(Unique), 암호화된 비밀번호, 권한(Role)을 관리합니다.
- **Recruits 테이블:** 게시글 번호, 제목, 내용, 마감 여부를 관리합니다.
- **연관관계 (N:1):** 한 명의 사용자가 여러 개의 모집글을 작성할 수 있으므로, Recruits 테이블에 `user_id`를 외래키(FK)로 설정하고 `@ManyToOne(fetch = FetchType.LAZY)`를 적용했습니다. 특히 실무에서 성능 저하의 주범이 되는 N+1 문제를 방지하기 위해 **지연 로딩** 전략을 기본으로 채택한 것이 특징입니다.

## 6. 회고 (Review)

- **트러블슈팅 경험:**
    
    개발 초기, 엔티티 매핑 시 MySQL의 예약어(`user`) 충돌 문제로 인해 테이블이 생성되지 않는 오류(SQL Syntax Error)와 마주했습니다. 이를 해결하기 위해 `@Table(name = "users")` 어노테이션을 활용하여 명시적으로 테이블명을 매핑함으로써 문제를 해결했습니다. 또한, 필터 단에서 발생하는 에러가 시큐리티 설정에 의해 403 Forbidden으로 덮어씌워지는 현상을 겪었으며, (`.authorizeHttpRequests`)에 `/error` 경로를 명시적으로 허용하여 내부 예외 처리를 원활하게 개선할 수 있었습니다.
    
- **성장한 점:**
    
    JWT의 개념과 JWT 필터가 스프링 시큐리티 내부에서 어떻게 동작하고 요청을 가로채는지 그 핵심 원리에 관해서 이해할 수 있었습니다. 스프링 CRUD를 복습할 수 있었습니다.
