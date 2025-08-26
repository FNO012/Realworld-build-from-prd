# RealWorld Conduit - 시스템 설계 문서

## 개요

이 문서는 RealWorld Conduit 애플리케이션의 시스템 설계를 정의합니다. 라이브 코딩 학습을 위한 MVP 구현에 중점을 두어 핵심 아키텍처와 설계 결정사항을 문서화합니다.

## 시스템 아키텍처

### 전체 시스템 구조

```mermaid
graph TB
    subgraph "Client Layer"
        WEB[Web Browser]
        MOB[Mobile Browser]
    end
    
    subgraph "Frontend Layer"
        NEXT[Next.js Application]
        COMP[React Components]
        STATE[Zustand Store]
    end
    
    subgraph "Backend Layer"
        API[Spring Boot API]
        AUTH[Authentication Service]
        BIZ[Business Logic Services]
    end
    
    subgraph "Data Layer"
        DB[(MariaDB Database)]
        CACHE[Local Cache]
    end
    
    WEB --> NEXT
    MOB --> NEXT
    NEXT --> COMP
    COMP --> STATE
    STATE --> API
    API --> AUTH
    API --> BIZ
    BIZ --> DB
    AUTH --> CACHE
```

### 기술 스택 매핑

```mermaid
graph LR
    subgraph "Frontend Stack"
        A[Next.js 15] --> B[TypeScript]
        B --> C[Tailwind CSS]
        C --> D[Zustand]
        D --> E[Axios]
    end
    
    subgraph "Backend Stack"
        F[Spring Boot] --> G[MyBatis]
        G --> H[MariaDB Connector]
        H --> I[Gradle]
    end
    
    subgraph "Communication"
        E --> J[REST API]
        J --> F
    end
```

## 데이터베이스 설계

### ERD (Entity Relationship Diagram)

```mermaid
erDiagram
    USER {
        int id PK
        varchar username UK
        varchar email UK
        varchar password
        text bio
        varchar image
        timestamp created_at
        timestamp updated_at
    }
    
    ARTICLE {
        int id PK
        varchar slug UK
        varchar title
        text description
        text body
        int author_id FK
        timestamp created_at
        timestamp updated_at
    }
    
    COMMENT {
        int id PK
        text body
        int article_id FK
        int author_id FK
        timestamp created_at
    }
    
    USER ||--o{ ARTICLE : writes
    USER ||--o{ COMMENT : writes
    ARTICLE ||--o{ COMMENT : has
```

### 데이터베이스 스키마

#### Users 테이블
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    bio TEXT,
    image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### Articles 테이블
```sql
CREATE TABLE articles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    slug VARCHAR(255) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    body TEXT NOT NULL,
    author_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);
```

#### Comments 테이블
```sql
CREATE TABLE comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    body TEXT NOT NULL,
    article_id INT NOT NULL,
    author_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);
```

## API 설계

### API 아키텍처

```mermaid
graph TD
    CLIENT[Frontend Client] --> CORS[CORS Filter]
    CORS --> AUTH[Authentication Filter]
    AUTH --> CONTROLLER[REST Controllers]
    
    subgraph "Controller Layer"
        UC[UserController]
        AC[ArticleController]
        CC[CommentController]
    end
    
    subgraph "Service Layer"
        US[UserService]
        AS[ArticleService]
        CS[CommentService]
    end
    
    subgraph "Data Access Layer"
        UM[UserMapper]
        AM[ArticleMapper]
        CM[CommentMapper]
    end
    
    CONTROLLER --> UC
    CONTROLLER --> AC
    CONTROLLER --> CC
    
    UC --> US
    AC --> AS
    CC --> CS
    
    US --> UM
    AS --> AM
    CS --> CM
    
    UM --> DB[(Database)]
    AM --> DB
    CM --> DB
```

### MVP API 엔드포인트

#### 인증 관련 API
- `POST /api/users` - 회원가입
- `POST /api/users/login` - 로그인
- `GET /api/user` - 현재 사용자 정보 (향후 확장용)

#### 아티클 관련 API
- `GET /api/articles` - 아티클 목록 조회
- `GET /api/articles/{slug}` - 아티클 상세 조회
- `POST /api/articles` - 아티클 생성
- `PUT /api/articles/{slug}` - 아티클 수정 (향후 확장용)
- `DELETE /api/articles/{slug}` - 아티클 삭제 (향후 확장용)

#### 댓글 관련 API
- `GET /api/articles/{slug}/comments` - 댓글 목록 조회
- `POST /api/articles/{slug}/comments` - 댓글 작성

### API 응답 형식

#### 성공 응답 예시
```json
{
  "success": true,
  "data": {
    "article": {
      "slug": "how-to-train-your-dragon",
      "title": "How to train your dragon",
      "description": "Ever wonder how?",
      "body": "It takes a Jacobian",
      "createdAt": "2016-02-18T03:22:56.637Z",
      "updatedAt": "2016-02-18T03:48:35.824Z",
      "author": {
        "username": "jake",
        "bio": "I work at statefarm",
        "image": "https://i.stack.imgur.com/xHWG8.jpg"
      }
    }
  }
}
```

#### 에러 응답 예시
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "입력 데이터가 유효하지 않습니다.",
    "details": {
      "title": ["제목은 필수입니다."],
      "body": ["내용은 필수입니다."]
    }
  }
}
```

## 프론트엔드 설계

### 컴포넌트 구조

```mermaid
graph TD
    APP[App Layout] --> NAV[Navigation]
    APP --> MAIN[Main Content]
    APP --> FOOTER[Footer]
    
    MAIN --> HOME[HomePage]
    MAIN --> LOGIN[LoginPage]
    MAIN --> REGISTER[RegisterPage]
    MAIN --> ARTICLE[ArticlePage]
    MAIN --> EDITOR[EditorPage]
    
    HOME --> FEED[ArticleList]
    FEED --> CARD[ArticleCard]
    
    ARTICLE --> CONTENT[ArticleContent]
    ARTICLE --> COMMENTS[CommentSection]
    
    COMMENTS --> CLIST[CommentList]
    COMMENTS --> CFORM[CommentForm]
```

### 상태 관리 설계

```mermaid
graph LR
    subgraph "Zustand Store"
        AUTH[AuthStore]
        ARTICLE[ArticleStore]
        UI[UIStore]
    end
    
    subgraph "Components"
        LOGIN[LoginForm]
        NAVBAR[Navigation]
        ALIST[ArticleList]
        DETAIL[ArticleDetail]
    end
    
    LOGIN --> AUTH
    NAVBAR --> AUTH
    ALIST --> ARTICLE
    DETAIL --> ARTICLE
    
    AUTH --> API[API Calls]
    ARTICLE --> API
    UI --> LOADING[Loading States]
```

### 페이지 라우팅 구조

```mermaid
graph TD
    ROOT[/ - HomePage] --> LOGIN[/login - LoginPage]
    ROOT --> REGISTER[/register - RegisterPage]
    ROOT --> EDITOR[/editor - EditorPage]
    ROOT --> ARTICLE[/article/[slug] - ArticlePage]
    
    LOGIN --> |success| ROOT
    REGISTER --> |success| ROOT
    EDITOR --> |publish| ARTICLE
```

## 인증 시스템 설계

### MVP 인증 플로우

```mermaid
sequenceDiagram
    participant Client
    participant Frontend
    participant Backend
    participant Database
    
    Client->>Frontend: 로그인 요청
    Frontend->>Backend: POST /api/users/login
    Backend->>Database: 사용자 조회
    Database-->>Backend: 사용자 정보
    Backend->>Backend: 비밀번호 검증
    Backend-->>Frontend: 로그인 성공 + 사용자 정보
    Frontend->>Frontend: 사용자 상태 저장
    Frontend-->>Client: 로그인 완료
```

### 세션 관리 (단순화)

```mermaid
graph LR
    LOGIN[로그인] --> SESSION[세션 저장]
    SESSION --> LOCALSTORAGE[localStorage]
    LOCALSTORAGE --> AUTH[인증 상태 확인]
    AUTH --> API[API 요청 시 확인]
```

## 백엔드 아키텍처 설계

### Spring Boot 레이어 구조

```mermaid
graph TD
    subgraph "Presentation Layer"
        CONTROLLER[Controllers]
        DTO[DTOs/Request/Response]
    end
    
    subgraph "Business Layer"
        SERVICE[Services]
        DOMAIN[Domain Models]
    end
    
    subgraph "Persistence Layer"
        MAPPER[MyBatis Mappers]
        ENTITY[Entities]
    end
    
    subgraph "Infrastructure Layer"
        CONFIG[Configuration]
        UTIL[Utilities]
    end
    
    CONTROLLER --> SERVICE
    SERVICE --> MAPPER
    MAPPER --> DB[(Database)]
    
    CONFIG --> SERVICE
    UTIL --> SERVICE
```

### 패키지 구조

```
backend/
├── src/main/java/com/realworld/conduit/
│   ├── config/          # 설정 클래스
│   ├── controller/      # REST 컨트롤러
│   ├── service/         # 비즈니스 로직
│   ├── mapper/          # MyBatis 매퍼 인터페이스
│   ├── model/           # 도메인 모델/엔티티
│   ├── dto/             # 데이터 전송 객체
│   └── util/            # 유틸리티 클래스
├── src/main/resources/
│   ├── mapper/          # MyBatis XML 매퍼
│   ├── application.yml  # 애플리케이션 설정
│   └── schema.sql       # 데이터베이스 스키마
└── src/test/            # 테스트 코드
```

## 보안 고려사항

### MVP 보안 요구사항

```mermaid
graph TD
    INPUT[사용자 입력] --> VALIDATION[입력 검증]
    VALIDATION --> SANITIZATION[데이터 정제]
    SANITIZATION --> PROCESSING[비즈니스 로직 처리]
    
    PASSWORD[비밀번호] --> HASHING[해싱 처리]
    HASHING --> STORAGE[안전한 저장]
    
    CLIENT[클라이언트] --> CORS[CORS 정책]
    CORS --> API[API 접근]
```

### 기본 보안 조치
- **입력 검증**: 모든 사용자 입력에 대한 기본 검증
- **비밀번호 해싱**: 평문 비밀번호 저장 금지
- **CORS 설정**: 허용된 도메인에서만 API 접근 허용
- **SQL 인젝션 방지**: MyBatis 파라미터 바인딩 사용

## 성능 고려사항

### 기본 성능 최적화

```mermaid
graph LR
    subgraph "Frontend"
        LAZY[지연 로딩]
        MEMO[메모이제이션]
        BUNDLE[번들 최적화]
    end
    
    subgraph "Backend"
        CACHE[기본 캐싱]
        CONN[DB 커넥션 풀]
        INDEX[DB 인덱스]
    end
    
    subgraph "Database"
        QUERY[쿼리 최적화]
        NORM[정규화]
    end
```

## 배포 아키텍처

### 개발 환경 구조

```mermaid
graph TB
    subgraph "Development Environment"
        DEV[개발자 로컬]
        FRONT[Frontend:3000]
        BACK[Backend:8080]
        DB[MariaDB:3306]
    end
    
    DEV --> FRONT
    DEV --> BACK
    BACK --> DB
    FRONT --> BACK
```

### 향후 확장 가능한 배포 구조

```mermaid
graph TB
    subgraph "Production Environment (Future)"
        LB[Load Balancer]
        WEB1[Frontend Instance]
        WEB2[Frontend Instance]
        API1[Backend Instance]
        API2[Backend Instance]
        DB[Database]
    end
    
    LB --> WEB1
    LB --> WEB2
    WEB1 --> API1
    WEB2 --> API2
    API1 --> DB
    API2 --> DB
```

## 개발 가이드라인

### 코딩 컨벤션

#### 백엔드 (Java/Spring Boot)
- **패키지명**: 소문자, 점(.) 구분
- **클래스명**: PascalCase
- **메소드명**: camelCase
- **상수**: UPPER_SNAKE_CASE

#### 프론트엔드 (TypeScript/React)
- **컴포넌트**: PascalCase
- **변수/함수**: camelCase
- **상수**: UPPER_SNAKE_CASE
- **파일명**: kebab-case 또는 PascalCase

### Git 브랜치 전략

```mermaid
gitgraph
    commit
    branch develop
    checkout develop
    commit
    branch feature/user-auth
    checkout feature/user-auth
    commit
    commit
    checkout develop
    merge feature/user-auth
    branch feature/article-crud
    checkout feature/article-crud
    commit
    commit
    checkout develop
    merge feature/article-crud
    checkout main
    merge develop
```

## 테스트 전략

### 테스트 피라미드

```mermaid
graph TB
    subgraph "테스트 레벨"
        E2E[E2E Tests]
        INT[Integration Tests]
        UNIT[Unit Tests]
    end
    
    subgraph "테스트 도구"
        JEST[Jest/Testing Library]
        SPRING[Spring Test]
        TESTCONTAINERS[TestContainers]
    end
    
    E2E --> TESTCONTAINERS
    INT --> SPRING
    UNIT --> JEST
    UNIT --> SPRING
```

## 향후 확장 계획

### Phase 2 기능 확장

```mermaid
graph LR
    MVP[MVP 완성] --> AUTH[고급 인증]
    AUTH --> SOCIAL[소셜 기능]
    SOCIAL --> SEARCH[검색 기능]
    SEARCH --> ANALYTICS[분석 기능]
```

### 기술 스택 진화

```mermaid
timeline
    title 기술 스택 진화 계획
    
    section MVP
        기본 Spring Boot    : MyBatis
                           : 기본 인증
    
    section Phase 2
        Spring Security    : JWT 토큰
                          : Redis 캐싱
    
    section Phase 3
        마이크로서비스      : Docker 컨테이너
                          : CI/CD 파이프라인
```

## 결론

이 설계 문서는 RealWorld Conduit 애플리케이션의 MVP 구현을 위한 기본 아키텍처를 제시합니다. 라이브 코딩 학습에 최적화되어 있으며, 단계적으로 확장 가능한 구조로 설계되었습니다.

핵심 원칙:
- **단순성**: 복잡한 기능보다 기본 기능에 집중
- **학습성**: 이해하기 쉬운 구조와 명확한 분리
- **확장성**: 향후 기능 추가에 유연한 아키텍처
- **실용성**: 실제 프로덕션에서 사용 가능한 패턴