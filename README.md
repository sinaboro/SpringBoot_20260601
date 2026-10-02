# 🌱 Spring Boot 기초 (2026.06)

Spring Boot로 **회원 관리 웹 애플리케이션**을 단계별로 만드는 실습 저장소입니다.
같은 회원 CRUD를 **MyBatis → JPA → Spring Security** 순으로 바꿔 가며, 데이터 접근 기술과 인증·인가를 비교하며 배웁니다.

| 구분 | 내용 |
|---|---|
| 언어 | Java 21 |
| 프레임워크 | Spring Boot 3.5.x, Spring MVC, Spring Data JPA, MyBatis 3, Spring Security 6 |
| 뷰 | Thymeleaf + Bootstrap 5 |
| DB | MySQL 8 (`memberdb`), H2 (인메모리) |
| 빌드 | Gradle |
| 기타 | Lombok, Bean Validation, JUnit 5, MockMvc |

---

## 📁 프로젝트 구성 (학습 순서)

| 순서 | 폴더 | 주제 | DB | 핵심 기술 |
|:-:|---|---|---|---|
| 1 | [`member`](#1-member--mybatis-회원-crud) | MyBatis 회원 CRUD | MySQL | MyBatis XML Mapper, Thymeleaf, Validation |
| 2 | [`jpamember`](#2-jpamember--jpa-회원-crud) | 1번을 JPA로 전환 | MySQL | Spring Data JPA, 쿼리 메서드, 더티 체킹, MockMvc |
| 3 | [`jpa-study`](#3-jpa-study--jpa-쿼리와-연관관계) | JPA 쿼리와 연관관계 | H2 | `@Query`, JPQL·Native Query, `@OneToMany`/`@ManyToOne`, LAZY |
| 4 | [`jpastudy`](#4-jpastudy--도서-rest-api) | 도서 관리 REST API | H2 | `@RestController`, DTO, `JOIN FETCH`, `CommandLineRunner` |
| 5 | [`jpasecurity`](#5-jpasecurity--spring-security-로그인회원가입) | 로그인, 회원가입, 권한 | MySQL | Spring Security, BCrypt, `UserDetailsService`, CSRF |

> `member.zip`, `jpasecurity.zip`은 같은 이름 폴더의 압축본입니다.
> `git_spring.sh`는 `add → commit → push`를 한 번에 실행하는 스크립트입니다.

---

## ⚙️ 실행 환경 준비

### 1. MySQL 설정 (`member`, `jpamember`, `jpasecurity`)
```sql
CREATE DATABASE memberdb DEFAULT CHARACTER SET utf8mb4;
CREATE USER 'test'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON memberdb.* TO 'test'@'localhost';

-- member(MyBatis) 프로젝트용 테이블 (JPA 프로젝트는 ddl-auto: update로 자동 생성)
USE memberdb;
CREATE TABLE member (
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(50)  NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);
```
> 계정과 비밀번호는 각 프로젝트의 `application.properties` / `application.yaml`에서 본인 환경에 맞게 바꾸세요.

### 2. 실행
```bash
cd jpamember
./gradlew bootRun          # Windows: gradlew.bat bootRun
```
IntelliJ에서 각 폴더를 **별도 프로젝트로 열고** `*Application.java`를 실행해도 됩니다. Lombok 사용을 위해 *Annotation Processing* 을 켜 두세요.

---

## 📚 프로젝트별 정리

### 1. `member` — MyBatis 회원 CRUD
SQL을 직접 작성하는 **MyBatis**로 회원 등록, 조회, 수정, 삭제, 검색을 구현합니다.

```
Controller ──► Service ──► Mapper(interface) ──► MemberMapper.xml ──► MySQL
     │
     └──► Thymeleaf (list / form / editForm)
```

| 구성 | 내용 |
|---|---|
| `entity/Member` | `id`, `name`, `email`. `@NotBlank`, `@Email` 검증 |
| `mapper/MemberMapper` + `resources/mapper/MemberMapper.xml` | `findAll`, `findById`, `findByNameContaining`(`LIKE CONCAT('%', #{keyword}, '%')`), `findByEmail`, `insertMember`(`useGeneratedKeys`), `updateMember`, `deleteMember` |
| `service/MemberService` | 이메일 중복 검사, `@Transactional` |
| `controller/MemberController` | `/member/list`, `/member/new`, `/member/edit/{id}`, `/member/delete/{id}`. `BindingResult` 검증, `RedirectAttributes` 플래시 메시지(PRG 패턴) |
| `application.properties` | `mybatis.mapper-locations`, `type-aliases-package`, `spring.thymeleaf.cache=false` |
| 테스트 | `MemberMapperTest`(Mapper 메서드별 단위 테스트), `MemberServiceTest` |

**배운 점**: 인터페이스 메서드 이름과 XML의 `id`, 그리고 `namespace`가 정확히 일치해야 합니다. `#{}`로 값을 바인딩하면 SQL Injection이 방지됩니다.

---

### 2. `jpamember` — JPA 회원 CRUD
1번 프로젝트와 같은 화면과 기능을 **SQL 없이 Spring Data JPA**로 다시 구현합니다.

| 구성 | 내용 |
|---|---|
| `entity/JpaMember` | `@Entity`, `@Table(name="jpa_member")`, `@Id @GeneratedValue(IDENTITY)`, `@Column(unique=true)`, `@Builder` |
| `repository/MemberRepository` | `JpaRepository<JpaMember, Long>` 상속. 쿼리 메서드 `findByNameContaining`, `findByEmail`(Optional), `findAllByOrderByIdDesc` |
| `service/MemberService` | `save()`는 id가 없으면 INSERT, 있으면 UPDATE. 중복 이메일이면 `IllegalStateException` |
| `controller/MemberController` | 생성자 주입(`@RequiredArgsConstructor`), 키워드 검색 |
| 테스트 | `MemberRepositoryTest`: 저장·조회, 수정, **더티 체킹**(`@Transactional` + `@Rollback(false)`), 삭제, 검색<br>`MemberServiceTest`: 샘플 데이터 입력<br>`MemberControllerTest`: **MockMvc**로 목록, 검색, 등록(리다이렉트와 플래시 속성) 검증 |

**MyBatis vs JPA**
| | MyBatis (`member`) | JPA (`jpamember`) |
|---|---|---|
| SQL | XML에 직접 작성 | 메서드 이름으로 자동 생성 |
| 테이블 | 직접 생성 | `ddl-auto: update`로 자동 생성 |
| 수정 | `UPDATE` 쿼리 실행 | 엔티티 값만 바꾸면 **변경 감지**로 자동 UPDATE |
| 결과 없음 | `null` 반환 | `Optional` 반환 |

---

### 3. `jpa-study` — JPA 쿼리와 연관관계
H2 인메모리 DB로 JPA 쿼리 작성법과 **엔티티 연관관계**를 실습합니다.

| 구성 | 내용 |
|---|---|
| `entity/Member` | `members` 테이블, `@Column(name="user_name")`. `@OneToMany(mappedBy="member", cascade=ALL, orphanRemoval=true)` |
| `entity/Order` | `orders` 테이블. `@ManyToOne(fetch=LAZY) @JoinColumn(name="member_id")` |
| `repository/MemberRepository` | **쿼리 메서드**: `findByEmail`, `findByUsernameContaining`, `findByAgeGreaterThanEqual`, `findAllByOrderByUsernameAsc`<br>**JPQL**: `findByAgeRange(min, max)`, `AVG(m.age)` 집계<br>**Native Query**: `searchByName` (`nativeQuery = true`) |
| `service/MemberService` | `@Transactional` CRUD. `update()`에서 setter만 호출해도 변경 감지로 반영 |
| 테스트 | `queryTest`(쿼리 메서드, JPQL, 집계)<br>`relationTest`(`em.flush()`, `em.clear()` 후 LAZY 로딩으로 주문자 조회) |

**H2 콘솔**: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:testdb`, 사용자 `sa`)

---

### 4. `jpastudy` — 도서 REST API
화면 없이 JSON을 주고받는 **REST API**와 **DTO 계층**을 만듭니다.

| 구성 | 내용 |
|---|---|
| `entity/Book`, `entity/Category` | 도서(N)와 카테고리(1) 관계 |
| `dto/BookRequestDto`, `BookResponseDto` | 요청과 응답 분리. 엔티티를 직접 노출하지 않고 `categoryName`만 평탄화해서 반환 |
| `repository/BookRepository` | `findByTitleContaining`, `findByCategoryId`, `findByPriceLessThanEqual`, `JOIN FETCH`로 **N+1 문제 해결**(`findAllWithCategory`) |
| `service/BookService` | 클래스에 `@Transactional(readOnly = true)`, 쓰기 메서드만 `@Transactional` |
| `DataInitializer` | `CommandLineRunner`로 시작할 때 카테고리 2개와 도서 4권 입력 |

**API 목록**
| Method | URL | 설명 |
|---|---|---|
| `POST` | `/api/books` | 도서 등록 (201 Created) |
| `GET` | `/api/books` | 전체 조회 (카테고리 포함) |
| `GET` | `/api/books/{id}` | 단건 조회 |
| `GET` | `/api/books/search?keyword=자바` | 제목 검색 |
| `PUT` | `/api/books/{id}` | 수정 |
| `DELETE` | `/api/books/{id}` | 삭제 (204 No Content) |

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"이펙티브 자바","author":"조슈아 블로크","price":36000,"stock":10,"categoryId":1}'
```

---

### 5. `jpasecurity` — Spring Security 로그인·회원가입
2번의 JPA 회원 관리에 **인증(로그인)과 인가(본인만 수정·삭제)**를 추가합니다.

```
브라우저 ─► SecurityFilterChain ─► (미인증) /auth/login
                 │
                 └─► 로그인 요청 ─► UserDetailsServiceImpl.loadUserByUsername()
                                    └─► UserAccount(UserDetails) ─► BCrypt 비밀번호 비교
```

| 구성 | 내용 |
|---|---|
| `config/SecurityConfig` | `BCryptPasswordEncoder` 등록. `/auth/**`와 정적 리소스는 허용하고 나머지는 인증 필요. 커스텀 로그인 페이지, 로그인 성공 시 `/member/list`, 로그아웃 |
| `dto/RegisterDto` | 회원가입 검증: 아이디(영문·숫자 4~20자), 비밀번호(4자 이상), 전화번호(`010-0000-0000` 정규식), 이메일 |
| `entity/JpaMember` | `username`, `password`(암호화 저장), `role`, `phone` 추가. `update()` 도메인 메서드 |
| `service/MemberService` | 아이디·이메일 중복 검사(`existsBy...`), `passwordEncoder.encode()`, `@Builder`로 엔티티 생성 |
| `service/UserAccount` | `UserDetails` 구현. 권한(`getAuthorities`), 계정 상태 |
| `service/UserDetailsServiceImpl` | `findByUsername`으로 사용자를 찾아 `UserAccount`로 감싸서 반환 |
| `controller/AuthController` | `/auth/login`, `/auth/register` |
| `controller/MemberContoller` | 목록과 검색. `@AuthenticationPrincipal`로 **본인 계정만 수정·삭제** 허용 (아니면 `?error=forbidden`) |
| 템플릿 | `thymeleaf-extras-springsecurity6`. `sec:authentication="principal.username"`으로 로그인 사용자 표시, `th:action`으로 **CSRF 토큰 자동 삽입**, 로그아웃은 POST |

**흐름**: 회원가입(`/auth/register`) → 로그인(`/auth/login`) → 회원 목록(`/member/list`) → 본인 정보 수정·삭제 → 로그아웃

---

## 🔑 핵심 개념 요약

| 개념 | 처음 나오는 프로젝트 |
|---|---|
| 계층 구조 (Controller → Service → Repository/Mapper) | member |
| Bean Validation (`@Valid`, `BindingResult`) | member |
| PRG 패턴, 플래시 메시지 (`RedirectAttributes`) | member |
| `@Transactional` | member |
| 엔티티 매핑, 쿼리 메서드 | jpamember |
| 변경 감지 (Dirty Checking) | jpamember, jpa-study |
| MockMvc 컨트롤러 테스트 | jpamember |
| JPQL, Native Query, 집계 | jpa-study |
| 연관관계 매핑, 지연 로딩, 영속성 컨텍스트 (`flush`/`clear`) | jpa-study |
| REST API, DTO, `ResponseEntity` | jpastudy |
| `JOIN FETCH` (N+1 해결), `readOnly` 트랜잭션 | jpastudy |
| 인증·인가, BCrypt, `UserDetailsService`, CSRF | jpasecurity |
