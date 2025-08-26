# 데이터베이스 설정 가이드

## MariaDB 설치 및 실행

### macOS (Homebrew)
```bash
# MariaDB 설치
brew install mariadb

# MariaDB 시작
brew services start mariadb

# 또는 일회성 실행
mariadb-safe --datadir=/usr/local/var/mysql
```

### Windows
1. MariaDB 공식 사이트에서 다운로드: https://mariadb.org/download/
2. 설치 후 서비스 시작

### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install mariadb-server
sudo systemctl start mariadb
```

## 데이터베이스 초기화

MariaDB가 실행 중일 때 다음 명령어로 스키마와 테스트 데이터를 생성하세요:

```bash
# 루트 사용자로 MariaDB 접속
mysql -u root

# 스키마 생성 (MariaDB 콘솔에서)
source src/main/resources/schema.sql;

# 테스트 데이터 삽입
source src/main/resources/data.sql;

# 데이터 확인
USE realworld_conduit;
SELECT * FROM users;
SELECT * FROM articles;
SELECT * FROM comments;
```

또는 명령행에서 직접 실행:

```bash
# 현재 backend/ 디렉토리에서
mysql -u root < src/main/resources/schema.sql
mysql -u root < src/main/resources/data.sql
```

## 연결 설정 확인

`src/main/resources/application.yml` 파일의 데이터베이스 설정을 확인하세요:

```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/realworld_conduit
    username: root
    password: 
```

비밀번호가 설정된 경우 `password` 필드를 업데이트하세요.

## 테스트 사용자 정보

생성된 테스트 사용자:
- username: `john`, email: `john@example.com`
- username: `jane`, email: `jane@example.com`  
- username: `admin`, email: `admin@example.com`

**참고**: 비밀번호는 해시화되어 저장되므로 실제 로그인 기능 구현 시 적절한 인증 로직이 필요합니다.