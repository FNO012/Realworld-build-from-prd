# RealWorld Conduit - Live Coding Implementation

> ### 라이브 코딩 학습을 통한 RealWorld 애플리케이션 구현 프로젝트

이 프로젝트는 [RealWorld](https://github.com/gothinkster/realworld) 사양을 기반으로 **라이브 코딩 기법**을 사용하여 풀스택 웹 애플리케이션을 구현하는 학습 프로젝트입니다.

## 📋 프로젝트 개요

### 🎯 목표
- **라이브 코딩**을 통한 실시간 개발 과정 학습
- PRD(요건정의서)에서 시작하여 완전한 애플리케이션까지 구현
- Spring Boot + Next.js 풀스택 개발 경험
- 실제 프로덕션 수준의 개발 패턴 학습

### 🌟 RealWorld란?
RealWorld는 "Medium.com의 클론"으로, 다음과 같은 핵심 기능을 포함한 소셜 블로깅 플랫폼입니다:
- ✅ CRUD 작업 (Create, Read, Update, Delete)
- ✅ 사용자 인증 및 권한 관리
- ✅ 라우팅 및 페이지네이션
- ✅ 실제 데이터베이스 연동
- ✅ 프론트엔드-백엔드 API 통신

**🔗 RealWorld 데모**: https://demo.realworld.build/

## 🛠 기술 스택

### 백엔드
- **Language**: Java
- **Framework**: Spring Boot
- **Database**: MariaDB
- **ORM**: MyBatis
- **Build Tool**: Gradle

### 프론트엔드
- **Framework**: Next.js 15
- **Language**: TypeScript
- **Styling**: Tailwind CSS + shadcn/ui
- **State Management**: Zustand
- **HTTP Client**: Axios

### 인프라
- **Containerization**: Docker & Docker Compose
- **Database**: MariaDB (Host PC)

## 🚀 시작하기

### 필수 요구사항
- Node.js 20.x (LTS)
- Java 17+
- MariaDB
- Docker & Docker Compose (선택사항)

### 설치 및 실행

1. **저장소 클론**
```bash
git clone https://github.com/your-username/Realworld-build-from-prd.git
cd Realworld-build-from-prd
```

2. **데이터베이스 설정**
```bash
# MariaDB에 데이터베이스 생성
mysql -u root -p
CREATE DATABASE realworld_conduit;
```

3. **백엔드 실행**
```bash
cd backend
./gradlew bootRun
```

4. **프론트엔드 실행**
```bash
cd frontend
npm install
npm run dev
```

5. **브라우저에서 확인**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api

## 📚 프로젝트 구조

```
Realworld-build-from-prd/
├── docs/                   # 프로젝트 문서
│   ├── realworld-prd.md   # 요구사항 정의서
│   └── tasks.md           # MVP 구현 작업 목록
├── backend/               # Spring Boot 백엔드
└── frontend/              # Next.js 프론트엔드
```

## 🎯 MVP 구현 범위

이 프로젝트는 **라이브 코딩 학습**을 위한 MVP에 중점을 두고 있습니다:

### ✅ 포함된 기능
- 사용자 회원가입/로그인
- 아티클 작성/조회/수정/삭제
- 댓글 작성/조회/삭제
- 기본적인 사용자 인터페이스

### ❌ MVP에서 제외된 기능
- 팔로우/언팔로우
- 좋아요/즐겨찾기
- 태그 시스템
- 고급 검색 및 필터링
- 복잡한 인증/보안 시스템

> 자세한 구현 계획은 [`docs/tasks.md`](./docs/tasks.md)를 참고하세요.

## 🎥 라이브 코딩 학습 포인트

### 백엔드 학습 내용
- Spring Boot 프로젝트 구조 및 설정
- RESTful API 설계 원칙
- MyBatis를 통한 데이터베이스 연동
- 기본적인 인증 및 세션 관리

### 프론트엔드 학습 내용
- Next.js 앱 라우터 및 페이지 구조
- React 컴포넌트 설계 패턴
- API 호출 및 상태 관리
- Tailwind CSS를 활용한 반응형 UI

### 풀스택 통합 학습 내용
- 프론트엔드-백엔드 API 연동
- CORS 처리 및 해결
- 사용자 세션 관리
- 전체 애플리케이션 아키텍처 이해

## 📖 관련 문서

- [📋 요구사항 정의서 (PRD)](./docs/realworld-prd.md)
- [✅ MVP 구현 작업 목록](./docs/tasks.md)
- [🌐 RealWorld 공식 사이트](https://realworld-docs.netlify.app/)
- [📚 RealWorld GitHub](https://github.com/gothinkster/realworld)

## 🤝 기여하기

이 프로젝트는 학습 목적으로 만들어졌습니다. 개선 사항이나 버그를 발견하신다면 이슈를 등록해 주세요.

## 📄 라이선스

이 프로젝트는 [MIT 라이선스](LICENSE) 하에 배포됩니다.

---

> **💡 참고**: 이 프로젝트는 실제 라이브 코딩 세션을 통해 구현되는 과정을 문서화한 것입니다. 완성된 코드보다는 **개발 과정과 사고 과정**을 중시합니다.
