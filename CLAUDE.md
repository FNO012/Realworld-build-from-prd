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
- **Language**: Java 17+
- **Database**: MariaDB (hosted on development machine, not containerized)
- **ORM**: MyBatis
- **Build Tool**: Gradle
- **Authentication**: JWT-based (planned), simplified session for MVP
- **Testing**: JUnit

### Frontend Stack  
- **Framework**: Next.js 15 (App Router)
- **Language**: TypeScript 5.6.x
- **Styling**: Tailwind CSS + shadcn/ui components
- **State Management**: Zustand
- **HTTP Client**: Axios
- **Build Tool**: Next.js built-in (Vite-based)
- **Package Manager**: npm

### Project Structure (Planned)
```
Realworld-build-from-prd/
├── docs/                   # Project documentation (current)
│   ├── realworld-prd.md   # Complete requirements specification
│   ├── design.md          # System architecture & design
│   └── tasks.md           # MVP implementation roadmap
├── backend/               # Spring Boot backend (to be created)
└── frontend/              # Next.js frontend (to be created)
```

## Development Commands

**Important Note**: The actual `backend/` and `frontend/` directories don't exist yet. This is a planning phase repository. Refer to GitHub issues #4-7 for implementation roadmap.

### Expected Commands (Once Implemented)

#### Backend Development
```bash
cd backend
./gradlew bootRun              # Start Spring Boot server (port 8080)
./gradlew test                 # Run backend tests
./gradlew build               # Build backend application
```

#### Frontend Development  
```bash
cd frontend
npm install                   # Install dependencies
npm run dev                   # Start development server (port 3000)
npm run build                 # Build for production
npm run lint                  # Lint code
npm run typecheck            # TypeScript type checking
```

#### Database Setup
```bash
# MariaDB setup (on host machine)
mysql -u root -p
CREATE DATABASE realworld_conduit;
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
- Authentication starts simple (session-based) before JWT implementation
- Frontend uses Next.js App Router (not Pages Router)
- Responsive design mandatory (mobile-first approach)
- CORS configuration required for frontend-backend communication

### Development Methodology
- **Test-Driven Development (TDD)**: Backend and core business logic must be implemented using TDD approach
  - Write failing tests first
  - Implement minimal code to pass tests
  - Refactor while keeping tests green
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
- Backend: JUnit unit tests for services and integration tests for APIs
- Frontend: Jest/React Testing Library for component tests
- End-to-end: Manual testing of complete user flows (registration → login → article creation → commenting)

### Commit and Issue Tracking Guidelines
- **GitHub CLI Usage**: All GitHub-related operations must use the GitHub CLI (gh command)
  - Issue management: `gh issue list`, `gh issue view`, `gh issue edit`, `gh issue comment`
  - Repository operations: `gh repo view`, `gh pr create`, `gh pr list`
  - Always prefer CLI over web interface for consistency and automation
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

**Phase**: Planning and Documentation (Pre-Implementation)
**Status**: Ready for implementation following GitHub issue roadmap
**Next Steps**: Begin with Issue #4 (Environment Setup) to create actual code directories