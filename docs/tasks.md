# RealWorld Conduit MVP 구현 작업 목록

## 개요
라이브 코딩 학습을 위한 RealWorld Conduit MVP 구현 작업 목록입니다.
핵심 기능만 구현하여 빠르게 동작하는 애플리케이션을 만드는 것이 목표입니다.
Spring Boot (백엔드) + Next.js (프론트엔드) 조합으로 구현합니다.

## 1. MVP 핵심 환경 설정

### 1.1 기본 프로젝트 구조
- [ ] 루트 디렉토리 구조 생성
  - `backend/` - Spring Boot 백엔드 프로젝트
  - `frontend/` - Next.js 프론트엔드 프로젝트
- [ ] 기본 `README.md` 작성

### 1.2 데이터베이스 설정 (간단화)
- [ ] MariaDB 기본 스키마 생성 (User, Article, Comment 테이블만)
- [ ] 기본 테스트 데이터 생성

## 2. 백엔드 MVP (Spring Boot 기본)

### 2.1 Spring Boot 기본 설정
- [x] Spring Boot 프로젝트 생성 ✅ **완료**
- [x] 기본 의존성 설정 ✅ **완료**
  - Spring Boot Starter Web
  - Spring Boot Starter Security
  - MyBatis Spring Boot Starter  
  - MariaDB Connector
  - **JWT 라이브러리 (jjwt 0.12.3)** ✅
- [x] 기본 설정 파일 (`application.yml`) ✅ **완료**

### 2.2 핵심 데이터 모델
- [x] User 엔티티 (기본 필드만: id, username, email, password) ✅ **완료**
- [ ] Article 엔티티 (기본 필드만: id, title, body, author_id, created_at)
- [ ] Comment 엔티티 (기본 필드만: id, body, article_id, author_id)

### 2.3 필수 API만 구현
- [x] 사용자 회원가입 API (`POST /api/users`) ✅ **완료**
- [x] 사용자 로그인 API (`POST /login`) - **JWT 토큰 기반 인증 구현 완료** ✅
  - Spring Security + JWT 토큰 발급 시스템
  - JwtAuthenticationFilter 및 CustomUserDetailsService
  - 24시간 유효 JWT 토큰, Bearer 인증 방식
- [ ] 아티클 목록 조회 (`GET /api/articles`)
- [ ] 아티클 상세 조회 (`GET /api/articles/:slug`)
- [ ] 아티클 생성 (`POST /api/articles`)
- [ ] 댓글 조회 (`GET /api/articles/:slug/comments`)
- [ ] 댓글 작성 (`POST /api/articles/:slug/comments`)

## 3. 프론트엔드 MVP (Next.js 기본)

### 3.1 Next.js 기본 설정
- [ ] Next.js 프로젝트 생성 (TypeScript)
- [ ] 기본 의존성만 설치
  - Tailwind CSS (기본 스타일링)
  - Axios (API 통신)
- [ ] 기본 Tailwind 설정

### 3.2 핵심 페이지만 구현
- [ ] 홈페이지 (`/`) - 아티클 목록 표시
- [ ] 로그인 페이지 (`/login`) - 기본 로그인 폼
- [ ] 회원가입 페이지 (`/register`) - 기본 회원가입 폼  
- [ ] 아티클 상세 페이지 (`/article/[slug]`) - 아티클 내용과 댓글
- [ ] 아티클 작성 페이지 (`/editor`) - 간단한 글쓰기 폼

### 3.3 기본 컴포넌트
- [ ] 네비게이션 바 (로그인/로그아웃 상태 표시)
- [ ] 아티클 카드 컴포넌트
- [ ] 댓글 컴포넌트
- [ ] 기본 폼 컴포넌트

### 3.4 기본 API 연동
- [ ] 회원가입/로그인 API 호출
- [ ] 아티클 목록/상세 API 호출
- [ ] 아티클 생성 API 호출
- [ ] 댓글 조회/작성 API 호출

## 4. MVP 완성 및 테스트

### 4.1 기본 동작 확인
- [ ] 회원가입 → 로그인 → 글쓰기 → 댓글 작성 플로우 테스트
- [ ] 프론트엔드-백엔드 연동 확인
- [ ] 기본 CORS 설정

### 4.2 배포 준비 (선택사항)
- [ ] 로컬 환경에서 정상 동작 확인
- [ ] 기본 에러 처리 추가

## MVP 학습 목표

이 MVP를 통해 학습할 수 있는 핵심 개념들:

### 백엔드 학습 포인트
- Spring Boot 기본 설정 및 구조
- RESTful API 설계 기본원칙
- 데이터베이스 연동 (MyBatis)
- 기본적인 사용자 인증

### 프론트엔드 학습 포인트  
- Next.js 기본 구조 및 라우팅
- React 컴포넌트 설계
- API 호출 및 상태 관리 기본
- 기본적인 반응형 UI

### 통합 개발 학습 포인트
- 프론트엔드-백엔드 API 연동
- CORS 처리
- 사용자 세션 관리
- 전체적인 웹 애플리케이션 구조 이해

## 구현 우선순위

1. **1단계**: 백엔드 기본 API (회원가입, 로그인, 글 조회/작성)
2. **2단계**: 프론트엔드 기본 페이지 (홈, 로그인, 회원가입, 글쓰기)
3. **3단계**: 댓글 기능 추가
4. **4단계**: 기본 스타일링 및 UX 개선

> **중요**: 복잡한 기능(팔로우, 좋아요, 태그, 고급 검색 등)은 MVP에서 제외하고, 기본 CRUD와 인증만 구현하여 빠르게 동작하는 애플리케이션을 완성하는 것이 목표입니다.