# RealWorld Conduit - Product Requirements Document (PRD)

## 1. 프로젝트 개요

### 1.1 비전
RealWorld Conduit는 Medium.com의 클론으로, 소셜 블로깅 플랫폼을 구현하는 풀스택 웹 애플리케이션입니다. 이 프로젝트는 실제 프로덕션 수준의 애플리케이션 개발 패턴과 모범 사례를 학습하기 위한 교육 목적의 프로젝트입니다.

### 1.2 목표
- 현실적이고 포괄적인 풀스택 애플리케이션 구현
- 다양한 프론트엔드와 백엔드 기술 조합 가능한 모듈러 아키텍처
- CRUD 작업, 인증, 라우팅, 페이지네이션 등 실제 애플리케이션 기능 구현
- 표준화된 API 스펙을 통한 일관된 사용자 경험 제공

## 2. 핵심 기능 요구사항

### 2.1 사용자 관리 (User Management)
- **회원가입**: 이메일, 사용자명, 비밀번호로 계정 생성
- **로그인/로그아웃**: JWT 토큰 기반 인증
- **프로필 관리**: 사용자 정보 조회 및 수정
- **팔로우/언팔로우**: 다른 사용자 팔로우 기능

### 2.2 아티클 관리 (Article Management)
- **아티클 작성**: 제목, 내용, 태그로 글 작성
- **아티클 편집**: 기존 글 수정
- **아티클 삭제**: 본인 글 삭제
- **아티클 목록**: 전체/팔로우 사용자 글 목록 조회
- **아티클 필터링**: 태그, 작성자, 즐겨찾기별 필터링
- **아티클 검색**: 키워드 기반 검색

### 2.3 소셜 기능 (Social Features)
- **좋아요/즐겨찾기**: 아티클에 좋아요 표시
- **댓글 시스템**: 아티클에 댓글 작성, 수정, 삭제
- **태그 시스템**: 아티클 분류를 위한 태그 기능
- **피드**: 팔로우한 사용자들의 최신 글 모음

## 3. 사용자 스토리

### 3.1 익명 사용자
- 홈페이지에서 최신 아티클들을 볼 수 있다
- 인기 태그 목록을 볼 수 있다
- 특정 태그를 클릭하여 관련 아티클들을 필터링할 수 있다
- 회원가입 및 로그인을 할 수 있다
- 개별 아티클을 읽을 수 있다

### 3.2 로그인 사용자
- 새 아티클을 작성할 수 있다
- 본인의 아티클을 편집하고 삭제할 수 있다
- 다른 사용자를 팔로우/언팔로우할 수 있다
- 아티클에 좋아요를 표시할 수 있다
- 아티클에 댓글을 작성, 삭제할 수 있다
- 개인 프로필을 수정할 수 있다
- 팔로우한 사용자들의 피드를 볼 수 있다

## 4. 페이지 구조

### 4.1 필수 페이지
1. **홈페이지 (Home)** - `/`
   - Global Feed (전체 아티클)
   - Your Feed (팔로우 피드) - 로그인 시
   - Popular Tags 사이드바

2. **로그인 (Sign In)** - `/login`
   - 이메일/비밀번호 입력 폼

3. **회원가입 (Sign Up)** - `/register`
   - 사용자명/이메일/비밀번호 입력 폼

4. **설정 (Settings)** - `/settings`
   - 프로필 정보 수정
   - 비밀번호 변경
   - 로그아웃

5. **프로필 페이지 (Profile)** - `/profile/:username`
   - 사용자 정보 표시
   - My Articles / Favorited Articles 탭
   - 팔로우/언팔로우 버튼

6. **에디터 (Editor)** - `/editor/:slug?`
   - 아티클 작성/편집 폼

7. **아티클 상세 (Article)** - `/article/:slug`
   - 아티클 내용 표시
   - 댓글 섹션
   - 좋아요/팔로우 액션

## 5. API 스펙

### 5.1 인증 (Authentication)
- `POST /api/users/login` - 로그인
- `POST /api/users` - 회원가입
- `GET /api/user` - 현재 사용자 정보
- `PUT /api/user` - 사용자 정보 수정

### 5.2 프로필 (Profiles)
- `GET /api/profiles/:username` - 프로필 조회
- `POST /api/profiles/:username/follow` - 팔로우
- `DELETE /api/profiles/:username/follow` - 언팔로우

### 5.3 아티클 (Articles)
- `GET /api/articles` - 아티클 목록 조회
- `GET /api/articles/feed` - 팔로우 피드
- `POST /api/articles` - 아티클 생성
- `GET /api/articles/:slug` - 아티클 상세 조회
- `PUT /api/articles/:slug` - 아티클 수정
- `DELETE /api/articles/:slug` - 아티클 삭제
- `POST /api/articles/:slug/favorite` - 좋아요
- `DELETE /api/articles/:slug/favorite` - 좋아요 취소

### 5.4 댓글 (Comments)
- `GET /api/articles/:slug/comments` - 댓글 목록
- `POST /api/articles/:slug/comments` - 댓글 작성
- `DELETE /api/articles/:slug/comments/:id` - 댓글 삭제

### 5.5 태그 (Tags)
- `GET /api/tags` - 인기 태그 목록

## 6. 기술 요구사항

### 6.1 프론트엔드
- **런타임**: Node.js 22.17.0 (LTS)
  - 공식 문서: https://nodejs.org/en/docs
  - 설치: `npm install -g node@lts`
- **프레임워크**: Next.js 15.x (Latest)
  - 공식 문서: https://nextjs.org/docs
  - 설치: `npx create-next-app@latest`
- **타입 시스템**: TypeScript 5.9.2 (Latest)
  - 공식 문서: https://www.typescriptlang.org/docs
  - 설치: `npm install -D typescript@latest`
- **CSS**: shadcn/ui + Tailwind CSS 3.x (Latest)
  - Tailwind CSS 공식 문서: https://tailwindcss.com/docs/installation
  - 설치: `npm install -D tailwindcss@latest postcss autoprefixer`
- **라우팅**: Next.js App Router (파일 기반)
- **빌드 도구**: Next.js 내장 (Vite 기반)
- **상태 관리**: Zustand
- **패키지 관리**: npm
- **HTTP 클라이언트**: Axios
- **반응형 디자인**: 모바일/태블릿/데스크톱 지원
- **API 통신**: RESTful API와의 비동기 통신
- **인증**: JWT 토큰 기반 인증 상태 관리

### 6.2 백엔드

- **프로그래밍 언어**: Java 17+ (LTS)
  - 공식 문서: https://docs.oracle.com/en/java/javase/17/
  - 설치: `sdk install java 17.0.4.1-librca` (SDKMAN 사용)
- **웹 프레임워크**: Spring Boot 3.5.3 (Latest)
  - 공식 문서: https://docs.spring.io/spring-boot/docs/current/reference/html/
  - 설치: Spring Initializr (https://start.spring.io/)
- **데이터베이스 접근**: MyBatis
- **데이터베이스**: MariaDB (Host PC에 존재)
- **빌드 도구**: Gradle 8.x
  - 공식 문서: https://docs.gradle.org/current/userguide/userguide.html
- **테스트**: JUnit 5
- **API**: RESTful API 설계
- **인증**: JWT 토큰 기반 인증/인가
- **CORS**: 크로스 오리진 요청 처리
- **에러 처리**: 표준화된 에러 응답
- **보안**: Spring Security, 비밀번호 해싱, SQL 인젝션 방지

### 6.3 인프라 및 배포
- 컨테이너: Docker
- 개발 환경 표준화
- 배포 환경 일관성
- 개발 환경: Docker Compose
- 로컬 개발 스택 구성
- 서비스 간 네트워킹
- 배포: Docker 이미지 기반
- 프로세스 관리: Makefile
- DB는 HostPC에 있음.

### 6.4 데이터 모델

#### User
```json
{
  "id": "integer",
  "username": "string",
  "email": "string",
  "bio": "string",
  "image": "string",
  "following": ["array of user ids"]
}
```

#### Article
```json
{
  "slug": "string",
  "title": "string",
  "description": "string",
  "body": "string",
  "tagList": ["array of strings"],
  "createdAt": "datetime",
  "updatedAt": "datetime",
  "favorited": "boolean",
  "favoritesCount": "integer",
  "author": "User object"
}
```

#### Comment
```json
{
  "id": "integer",
  "body": "string",
  "createdAt": "datetime",
  "author": "User object"
}
```

## 7. 비기능 요구사항

### 7.1 성능
- 페이지 로드 시간 3초 이내
- API 응답 시간 1초 이내
- 이미지 최적화 및 lazy loading

### 7.2 보안
- HTTPS 통신 강제
- XSS, CSRF 공격 방지
- 입력값 검증 및 sanitization
- 비밀번호 복잡도 요구사항

### 7.3 사용성
- 직관적인 사용자 인터페이스
- 접근성 가이드라인 준수
- 에러 메시지의 명확성
- 로딩 상태 표시

### 7.4 확장성
- 모듈화된 코드 구조
- 재사용 가능한 컴포넌트
- 테스트 가능한 아키텍처
- 코드 품질 도구 활용

## 8. 제약사항 및 전제조건

### 8.1 제약사항
- RealWorld API 스펙 준수 필수
- 모바일 반응형 필수 지원

### 8.2 전제조건
- 개발 환경 설정 (Node.js, Git 등)
- 기본적인 웹 개발 지식
- 선택한 기술 스택에 대한 이해

## 9. 성공 기준

### 9.1 기능적 기준
- 모든 필수 페이지 구현 완료
- 모든 API 엔드포인트 정상 작동
- 사용자 시나리오별 테스트 통과

### 9.2 품질 기준
- 코드 커버리지 80% 이상
- 웹 접근성 WCAG 2.1 AA 수준
- 성능 점수 90점 이상 (Lighthouse)

### 9.3 완성 기준
- RealWorld 스펙 100% 준수
- 데모 사이트와 동일한 기능 구현
- 코드 리뷰 및 문서화 완료