# Spring Boot + Kotlin 프로젝트

로컬 환경에서 **인프라 기동 → 데모 데이터 시딩 → 애플리케이션 실행**까지의 절차를 안내합니다.

## 1) 요구사항
- JDK 17+
- Docker & Docker Compose
- Python 3.9+ (시더 실행용)

## 2) 로컬 인프라 기동 (MySQL, Redis)
```bash
docker-compose -f ./docker/infra-compose.yml up -d
```

MySQL: localhost:3306 / DB명: loopers (구성 파일에 따라 변경 가능)


## 3) 데모 데이터 시딩 (Python)

레포에 포함된 스크립트를 사용합니다.


scripts/seed/
 ├─ requirements.txt
 ├─ .env
 └─ seed.py


### 3-1) 의존성 설치 & 시딩 실행

```bash
cd scripts/seed
python3 -m venv .venv
source .venv/bin/activate

python -m pip install -r requirements.txt   # 반드시 활성화된 파이썬으로 설치
python -m pip show Faker                    # 설치 확인(정보가 나오면 OK)
# 이미 데이터가 있으면 자동 스킵됩니다.
python seed.py
```
대량 시딩 전, 작은 수치로 먼저 검증하세요.

### 3-3) 시딩 실행


## 4) 애플리케이션 실행

./gradlew :apps:commerce-api:bootRun

기본 프로필이 local이라면 다음 JPA/데이터소스 설정이 적용됩니다(레포 설정 기준):
- JDBC: jdbc:mysql://localhost:3306/loopers?rewriteBatchedStatements=true&useConfigs=maxPerformance
- USER/PWD: application / application

참고
- 시딩 데이터 규모가 크면 MySQL 리소스 사용량이 급증할 수 있습니다. Docker 메모리/CPU 제한을 확인하세요.
- 시딩 스크립트와 애플리케이션의 시더 로직이 중복되지 않도록 환경을 분리하는 것을 권장합니다.
