# Git Hooks 설정 가이드

## 개요

이 프로젝트는 코드 품질을 자동으로 관리하기 위해 **Husky**와 **lint-staged**를 사용한 Git hooks가 설정되어 있습니다.

## 설치된 Hooks

### Pre-commit Hook

커밋 전에 자동으로 실행되는 검사 항목:

1. **프론트엔드 코드 품질 검사**
   - ESLint로 JavaScript/TypeScript 코드 검사
   - Prettier로 코드 포맷팅 자동 적용
   - 변경된 파일에만 적용 (lint-staged)

2. **백엔드 코드 컴파일 검사**
   - Java 소스 코드 컴파일 확인
   - 기본적인 문법 오류 검출

## 사용법

### 일반적인 커밋

```bash
git add .
git commit -m "feat: 새로운 기능 추가"
```

커밋 시 자동으로 다음과 같은 출력을 볼 수 있습니다:

```
🔍 Running pre-commit hooks...
📝 Checking frontend code...
✨ Done in 2.34s.
☕ Checking backend compilation...
✅ Pre-commit hooks completed successfully!
```

### Hook 실패 시 대응

Hook이 실패하면 커밋이 차단됩니다:

```bash
# 에러 해결 후 다시 커밋
git add .
git commit -m "fix: 코드 품질 문제 해결"
```

### 긴급 시 Hook 우회 (권장하지 않음)

```bash
git commit --no-verify -m "긴급 수정"
```

⚠️ **주의**: Hook 우회는 팀 코드 품질에 악영향을 줄 수 있으므로 가급적 사용하지 마세요.

## 프로젝트 설정

### 루트 레벨 Scripts

`package.json`에 정의된 편의 스크립트:

```bash
# 개발 서버 실행
npm run dev:frontend    # Next.js 개발 서버
npm run dev:backend     # Spring Boot 개발 서버

# 빌드
npm run build:all       # 전체 프로젝트 빌드
npm run build:frontend  # 프론트엔드만 빌드
npm run build:backend   # 백엔드만 빌드

# 테스트
npm run test:all        # 전체 프로젝트 테스트
npm run test:frontend   # 프론트엔드만 테스트
npm run test:backend    # 백엔드만 테스트

# 린트
npm run lint:all        # 전체 프로젝트 린트
npm run lint:frontend   # 프론트엔드만 린트
npm run lint:backend    # 백엔드만 린트
```

## 추가 Hook (향후 확장 가능)

### Pre-push Hook

빌드 성공 여부를 확인하여 원격 저장소에 잘못된 코드가 푸시되는 것을 방지:

```bash
npx husky add .husky/pre-push "npm run build:all"
```

### Commit Message Hook

커밋 메시지 형식을 검증 (Conventional Commits):

```bash
npx husky add .husky/commit-msg "npx commitlint --edit $1"
```

## 문제 해결

### 권한 문제

```bash
chmod +x .husky/pre-commit
```

### Node.js 의존성 문제

```bash
# 루트에서
npm install

# 프론트엔드에서
cd frontend && npm install
```

### 백엔드 컴파일 문제

```bash
cd backend
./gradlew clean build
```

## 팀 협업 가이드

1. **새 팀원 온보딩**
   ```bash
   git clone <repository>
   npm install
   cd frontend && npm install
   ```

2. **Hook 업데이트 시**
   ```bash
   npm run prepare
   ```

3. **코드 리뷰 전**
   - Hook이 통과한 코드만 리뷰 요청
   - 자동 포맷팅된 코드는 리뷰에서 제외

## 이점

- 🚀 **일관된 코드 품질**: 모든 커밋이 동일한 품질 기준 통과
- ⚡ **빠른 피드백**: 커밋 시점에서 즉시 문제 발견
- 🛡️ **안전성**: 컴파일되지 않는 코드의 커밋 방지
- 📏 **코드 포맷팅**: 자동으로 일관된 코드 스타일 유지