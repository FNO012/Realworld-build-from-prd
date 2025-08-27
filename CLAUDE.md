# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Language
- Communicate in Korean

## Project Overview

This is a live coding learning project to implement the RealWorld Conduit application (Medium.com clone) from a Product Requirements Document (PRD). The project follows a documentation-first approach with comprehensive planning before implementation.

**Key Characteristics:**
- Educational project focused on learning full-stack development patterns
- Spring Boot (Java) + Next.js (TypeScript) technology stack
- MVP-focused implementation with core features only
- Live coding methodology for real-time learning

## Architecture & Technology Stack

### Backend Stack
- **Framework**: Spring Boot
- **Language**: Java 21
- **Database**: MariaDB (hosted on development machine, not containerized)
- **ORM**: MyBatis
- **Build Tool**: Gradle
- **Authentication**: JWT 토큰 기반 인증 시스템 (구현 완료)
- **Testing**: JUnit

### Frontend Stack  
- **Framework**: Next.js 15 (App Router)
- **Language**: TypeScript 5.6.x
- **Styling**: Tailwind CSS + shadcn/ui components
- **State Management**: Zustand
- **HTTP Client**: Axios
- **Build Tool**: Next.js built-in (Vite-based)
- **Package Manager**: npm

### Project Structure
```
Realworld-build-from-prd/
├── docs/                          # Project documentation
│   ├── realworld-prd.md          # Complete requirements specification
│   ├── design.md                 # System architecture & design
│   └── tasks.md                  # MVP implementation roadmap
├── backend/                       # Spring Boot backend (Java 21 + Gradle)
│   ├── src/main/java/             # Java source code
│   │   └── com/realworld/conduit/ # Main package
│   │       ├── controller/        # REST controllers
│   │       ├── service/           # Business logic
│   │       ├── mapper/            # MyBatis mappers
│   │       ├── model/             # Domain models
│   │       ├── dto/               # Data transfer objects
│   │       ├── config/            # Spring configuration
│   │       └── util/              # Utility classes
│   ├── src/main/resources/        # Configuration and resources
│   │   ├── mapper/                # MyBatis XML mappers
│   │   ├── application.yml        # Spring Boot configuration
│   │   ├── schema.sql             # Database schema
│   │   └── data.sql               # Test data
│   └── src/test/                  # Test code and HTTP test files
└── frontend/                      # Next.js frontend (TypeScript + Tailwind)
    ├── src/app/                   # Next.js App Router pages
    ├── src/components/            # React components
    ├── src/store/                 # Zustand state stores
    ├── src/lib/                   # API utilities
    └── src/types/                 # TypeScript type definitions
```

## Development Commands

**Current State**: Both `backend/` and `frontend/` directories are implemented and functional. The project has moved beyond the planning phase.

### Development Commands

#### Backend Development
```bash
cd backend
./gradlew bootRun              # Start Spring Boot server (port 8080)
./gradlew test                 # Run all backend tests
./gradlew test --tests "*UserServiceTest*"  # Run specific test class
./gradlew build               # Build backend application
./gradlew clean               # Clean build artifacts
./gradlew bootRun --args='--spring.profiles.active=dev'  # Run with specific profile
```

#### Frontend Development  
```bash
cd frontend
npm install                   # Install dependencies
npm run dev                   # Start development server (port 3000) with Turbopack
npm run build                 # Build for production with Turbopack
npm run start                 # Start production server
npm run lint                  # Run ESLint
npm run free-port             # Kill processes on port 3000
```

**Note**: TypeScript type checking is integrated into Next.js build process. Use `npx tsc --noEmit` for manual type checking.

#### Database Setup
```bash
# MariaDB setup (on host machine)
mysql -u root -p
CREATE DATABASE realworld_conduit;
CREATE USER 'realworld'@'localhost' IDENTIFIED BY 'conduit';
GRANT ALL PRIVILEGES ON realworld_conduit.* TO 'realworld'@'localhost';
FLUSH PRIVILEGES;

# Initialize schema and test data
cd backend
mysql -u realworld -p realworld_conduit < src/main/resources/schema.sql
mysql -u realworld -p realworld_conduit < src/main/resources/data.sql
```

#### GitHub CLI Operations
```bash
# Issue management
gh issue list                     # List all issues
gh issue view <issue-number>      # View specific issue
gh issue edit <issue-number>      # Edit issue content
gh issue comment <issue-number>   # Add comment to issue

# Repository operations
gh repo view                      # View repository information
gh pr create                      # Create pull request
gh pr list                        # List pull requests
```

## Implementation Roadmap

The project follows a structured MVP approach with GitHub issues tracking progress:

1. **Issue #4: MVP Environment Setup**
   - Create `backend/` and `frontend/` directories
   - Database schema setup (User, Article, Comment tables)
   - Basic project configuration

2. **Issue #5: Backend Spring Boot Implementation**  
   - Core API endpoints (auth, articles, comments)
   - MyBatis integration
   - Basic authentication system

3. **Issue #6: Frontend Next.js Implementation**
   - Essential pages (home, login, register, editor, article detail)
   - React components and Zustand state management
   - API integration with backend

4. **Issue #7: MVP Integration & Testing**
   - End-to-end user flow testing
   - CORS configuration
   - Production-ready optimizations

## Core Domain Models

### Database Schema (From design.md)
- **Users**: id, username, email, password, bio, image
- **Articles**: id, slug, title, description, body, author_id, created_at, updated_at  
- **Comments**: id, body, article_id, author_id, created_at

### API Endpoints (MVP Scope)
- `POST /api/users` - Registration
- `POST /api/users/login` - Authentication
- `GET /api/articles` - Article listing
- `GET /api/articles/{slug}` - Article detail
- `POST /api/articles` - Article creation
- `GET /api/articles/{slug}/comments` - Comment listing
- `POST /api/articles/{slug}/comments` - Comment creation

## Development Guidelines

### Code Organization Principles
- **Backend**: Standard Spring Boot layered architecture (Controller → Service → Mapper → Database)
- **Frontend**: Component-based React with co-located styles and tests
- **API Design**: RESTful endpoints following RealWorld specification
- **State Management**: Zustand stores for authentication and article data

### Key Implementation Notes
- Database connection points to host machine MariaDB (not containerized)
- **Authentication**: JWT 토큰 기반 인증 시스템 구현 완료
  - Spring Security + JWT (jjwt 라이브러리)
  - 로그인 성공 시 JWT 토큰 자동 발급 (24시간 유효)
  - Bearer 토큰 방식: `Authorization: Bearer <token>`
  - JwtAuthenticationFilter를 통한 자동 인증 처리
- Frontend uses Next.js App Router (not Pages Router)
- Responsive design mandatory (mobile-first approach)
- CORS configuration required for frontend-backend communication

### Development Methodology
- **Test-Driven Development (TDD)**: Backend and **Frontend core business logic** must be implemented using TDD approach
  - **Backend**: Write failing tests first, implement minimal code to pass tests, refactor while keeping tests green
  - **Frontend**: Core business logic (stores, utilities, data transformation) must follow TDD approach using Jest/React Testing Library
  - Write failing tests first for all business logic functions
  - Implement minimal code to pass tests
  - Refactor while keeping tests green
- **Code Quality Standards**: Strict adherence to linting and type checking rules
  - **ESLint Rule Enforcement**: ESLint 규칙 비활성화 절대 금지 (`eslint-disable` 주석 사용 금지)
  - **TypeScript Type Safety**: `@ts-ignore`, `any` 타입 사용 최소화
  - **Test Coverage**: 코어 비즈니스 로직은 반드시 테스트 커버리지 확보
- **Git Hook Testing**: 커밋 전 자동 테스트 검증 시스템
  - **Pre-commit Hook**: 커밋 시 테스트 실행 및 통과 여부 확인
  - **테스트 실패 시 커밋 차단**: 모든 테스트가 통과해야만 커밋 허용
  - **기존 코드 테스트 추가**: 테스트가 없는 기존 코어 로직에 대해 테스트 우선 추가
- **SOLID Principles**: All backend code must adhere to SOLID design principles
  - Single Responsibility Principle
  - Open/Closed Principle
  - Liskov Substitution Principle
  - Interface Segregation Principle
  - Dependency Inversion Principle
- **Clean Architecture**: Backend architecture must follow Clean Architecture patterns
  - Domain layer independent of external concerns
  - Use cases/application services as orchestrators
  - Infrastructure details isolated from business logic
  - Dependency injection for loose coupling

### Testing Strategy
- **Backend**: JUnit unit tests for services and integration tests for APIs
  - **TDD 필수**: 모든 서비스 로직은 테스트 우선 작성
  - **테스트 커버리지**: 코어 비즈니스 로직 100% 커버리지 목표
- **Frontend**: Jest/React Testing Library for component tests
  - **TDD 필수**: 코어 비즈니스 로직 (Zustand stores, utilities, data transformation) 테스트 우선 작성
  - **Component Testing**: UI 컴포넌트 테스트
  - **Integration Testing**: API 통합 테스트
- **End-to-end**: Manual testing of complete user flows (registration → login → article creation → commenting)
- **HTTP Request Test Files**: Controller 생성 시 해당 Controller 이름의 .http 파일을 `src/test/` 하위에 생성해야 함
  - 파일명 형식: `{ControllerName}.http` (예: UserController → UserControllerTest.http)
  - 각 Controller의 모든 API 엔드포인트에 대한 HTTP 요청 테스트 케이스 포함
  - 성공 케이스와 실패 케이스(검증 오류, 비즈니스 로직 오류) 모두 작성
- **Git Hook Integration**: 커밋 전 테스트 자동 실행
  - Pre-commit hook에서 모든 테스트 실행
  - 테스트 실패 시 커밋 차단
  - Lint, TypeScript 타입 체크도 함께 실행

### Commit and Issue Tracking Guidelines
- **GitHub CLI Usage**: All GitHub-related operations must use the GitHub CLI (gh command)
  - Issue management: `gh issue list`, `gh issue view`, `gh issue edit`, `gh issue comment`
  - Repository operations: `gh repo view`, `gh pr create`, `gh pr list`
  - Always prefer CLI over web interface for consistency and automation
- **Issue Closing Policy**: **커밋하기 전에는 이슈를 닫지 마세요**
  - 이슈는 관련 코드가 커밋된 후에만 닫아야 합니다
  - 작업 완료를 이슈에 코멘트로 보고한 후, 커밋을 먼저 진행하세요
  - 커밋이 성공한 후에 이슈를 닫거나 다음 단계로 진행하세요
- **Commit-level Completion**: Each commit must represent a complete, working increment of functionality
  - All tests must pass at commit time
  - Code must compile/build successfully
  - No broken functionality should be introduced
- **Issue Progress Tracking**: For each task completion, add a comment to the related GitHub issue using CLI
  - Use `gh issue comment <issue-number>` to add progress updates
  - Include commit hash and brief description of what was accomplished
  - Verify acceptance criteria are met before marking items as complete
  - Provide evidence (test results, screenshots, API responses) when applicable
  - Example: `gh issue comment 5 --body "✅ Completed user registration API endpoint - Commit: abc123f. All tests passing, validates email format and password strength."`
- **Continuous Validation**: Before moving to next task, ensure current implementation:
  - Passes all existing tests
  - Meets acceptance criteria defined in issue
  - Integrates properly with existing codebase
  - Follows established coding standards and architecture patterns

## Learning Objectives

This project emphasizes understanding:
- **Full-stack architecture**: How frontend and backend communicate via REST APIs
- **Spring Boot patterns**: Configuration, dependency injection, data access layers
- **React/Next.js patterns**: Component composition, state management, routing
- **Database design**: Relational modeling and ORM usage
- **Authentication flows**: User registration, login, session management
- **Development workflows**: From PRD to implementation to testing

## Documentation References

- `docs/realworld-prd.md` - Complete functional requirements and user stories
- `docs/design.md` - Detailed system architecture, database design, and API specifications  
- `docs/tasks.md` - Step-by-step MVP implementation checklist
- [RealWorld Spec](https://api.realworld.show) - Standard API specification to follow
- [RealWorld Demo](https://demo.realworld.build) - Reference implementation for UI/UX

## Current State

**Phase**: Implementation Phase (MVP Development)
**Status**: Full-stack application implemented with core features
**Components**: Spring Boot backend with MyBatis, Next.js frontend with Zustand, MariaDB database
**Authentication**: JWT-based authentication system fully implemented

## Architecture Overview

This codebase follows a clean layered architecture pattern:

### Backend Architecture (Spring Boot + MyBatis)
- **Controller Layer**: REST API endpoints (`@RestController`)
- **Service Layer**: Business logic and transaction management (`@Service`)
- **Mapper Layer**: MyBatis data access layer with XML mappers
- **Model Layer**: Domain entities representing database tables
- **DTO Layer**: Data transfer objects for API requests/responses
- **Security**: JWT authentication with Spring Security filters
- **Configuration**: Centralized in `SecurityConfig` and `WebConfig`

### Frontend Architecture (Next.js + Zustand)
- **App Router**: File-based routing with `src/app/` directory
- **Component Architecture**: Reusable React components in `src/components/`
- **State Management**: Zustand stores (`authStore`, `articleStore`, `userStore`)
- **API Layer**: Centralized HTTP client in `src/lib/api.ts`
- **TypeScript**: Strong typing with interfaces in `src/types/`

### Key Integration Points
- **Authentication Flow**: JWT tokens stored in Zustand, sent via Authorization header
- **API Communication**: Axios-based HTTP client with automatic token attachment
- **Error Handling**: Centralized error responses using `ApiResponse<T>` wrapper
- **Database**: MyBatis XML mappers for complex queries, entity-based operations

### Testing Strategy Implementation
- **Backend**: JUnit tests for all service classes and controllers
- **HTTP Testing**: `.http` files for manual API testing in `src/test/`
- **Frontend**: Component tests using Jest/React Testing Library (TDD for business logic)
- **Integration**: End-to-end user flow testing