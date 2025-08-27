# Dependency Update Command

다음 작업을 수행하여 프로젝트의 모든 의존성을 검사하고 업데이트하세요:

## 1. 현재 의존성 상태 검사

### Backend (Java/Gradle)
```bash
cd backend
./gradlew dependencies --configuration runtimeClasspath
./gradlew dependencyUpdates
```

다음 사항을 확인하고 보고하세요:
- 현재 사용 중인 주요 의존성 버전들 (Spring Boot, MyBatis, JWT, MariaDB 등)
- 보안 취약점이 있는 의존성
- 업데이트 가능한 의존성들과 권장 버전

### Frontend (Node.js/npm)
```bash
cd frontend
npm audit
npm outdated
npx npm-check-updates --interactive
```

다음 사항을 확인하고 보고하세요:
- 현재 사용 중인 주요 의존성 버전들 (Next.js, React, TypeScript, Tailwind 등)
- 보안 취약점이 있는 패키지
- 업데이트 가능한 패키지들과 권장 버전
- Breaking changes가 있는 major 업데이트 식별

### Root Level
프로젝트 루트에 package.json이 있다면 동일하게 검사하세요.

## 2. 업데이트 우선순위 설정

다음 기준으로 업데이트 우선순위를 설정하세요:

### 높은 우선순위 (즉시 업데이트)
- **보안 취약점**: Critical/High severity 보안 이슈
- **버그 수정**: 현재 사용 중인 기능에 영향을 주는 버그 수정
- **안정성 개선**: Patch 버전 업데이트 (예: 1.0.1 → 1.0.2)

### 중간 우선순위 (계획된 업데이트)
- **Minor 버전**: 새로운 기능 추가, 하위 호환성 유지 (예: 1.0.0 → 1.1.0)
- **성능 개선**: 성능 최적화가 포함된 업데이트
- **개발 도구**: ESLint, TypeScript 등 개발 환경 도구

### 낮은 우선순위 (신중한 검토 필요)
- **Major 버전**: Breaking changes 포함 (예: 1.0.0 → 2.0.0)
- **실험적 기능**: Beta, RC 버전
- **프레임워크 코어**: Next.js, Spring Boot 등 핵심 프레임워크의 major 업데이트

## 3. 업데이트 실행 계획

### Backend 업데이트 절차
1. `build.gradle`에서 버전 업데이트
2. 테스트 실행으로 호환성 확인: `./gradlew test`
3. 애플리케이션 빌드 및 실행 테스트: `./gradlew bootRun`
4. Breaking changes 확인 및 코드 수정
5. 커밋 전 모든 테스트 통과 확인

### Frontend 업데이트 절차
1. `package.json`에서 버전 업데이트 또는 `npm update` 사용
2. 타입 체크: `npx tsc --noEmit`
3. 린트 확인: `npm run lint`
4. 빌드 테스트: `npm run build`
5. 개발 서버 실행 테스트: `npm run dev`
6. 모든 페이지 및 기능 동작 확인

## 4. 호환성 및 충돌 검사

### 버전 충돌 확인
- Java 버전 호환성 (현재 Java 21)
- Node.js 버전 호환성 (프로젝트 요구사항 확인)
- 의존성 간 버전 충돌 (peer dependencies)

### 테스트 수행
```bash
# Backend 전체 테스트
cd backend && ./gradlew clean test

# Frontend 빌드 및 타입 체크
cd frontend && npm run build && npx tsc --noEmit
```

## 5. 문서화 및 보고

업데이트 완료 후 다음 정보를 정리하여 보고하세요:

### 업데이트 요약
- 업데이트된 의존성 목록 (이전 버전 → 새 버전)
- 보안 취약점 해결 내역
- Breaking changes 및 코드 수정 사항
- 성능 개선 또는 새로운 기능

### 주의사항
- 업데이트로 인한 잠재적 문제점
- 추가 테스트가 필요한 영역
- 프로덕션 배포 시 고려사항

### 차기 업데이트 계획
- 현재 보류된 major 업데이트들
- 다음 업데이트 주기 권장사항
- 모니터링이 필요한 의존성

## 6. 자동화 제안

의존성 관리 자동화를 위한 도구 설정 제안:
- **Dependabot** 설정 (.github/dependabot.yml)
- **Renovate** 설정
- **npm audit fix** 자동 실행을 위한 pre-commit hook
- 정기적인 의존성 검사를 위한 GitHub Actions workflow

---

**중요**: 업데이트 전후로 반드시 전체 테스트를 실행하고, 모든 기능이 정상 동작하는지 확인하세요. 프로덕션 환경에 영향을 줄 수 있는 변경사항은 별도의 브랜치에서 작업하고 충분한 테스트 후 병합하세요.