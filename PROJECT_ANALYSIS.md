# Loopers Kotlin Commerce Project 분석

## 📋 프로젝트 개요

**Loopers Template** - Spring Boot + Kotlin 기반 E-commerce API 프로젝트

- **프로젝트명**: emotional-commerce
- **템플릿**: Loopers에서 제공하는 Spring Boot + Kotlin 표준 템플릿
- **아키텍처**: DDD 기반 헥사고날 아키텍처 (포트 & 어댑터)
- **빌드 도구**: Gradle (Kotlin DSL)
- **언어**: Kotlin + Java 21

## 🏗️ 아키텍처 및 계층구조

### 전체 아키텍처

```
┌─────────────────────────────────────────────┐
│              Interfaces Layer               │ ← REST API, Controllers
├─────────────────────────────────────────────┤
│             Application Layer               │ ← Use Cases, Facades
├─────────────────────────────────────────────┤
│               Domain Layer                  │ ← Business Logic, Entities
├─────────────────────────────────────────────┤
│            Infrastructure Layer             │ ← Database, External APIs
└─────────────────────────────────────────────┘
```

### 멀티모듈 구조

```
Root
├── apps (실행 애플리케이션)
│   └── 📦 commerce-api - E-commerce REST API
├── modules (재사용 가능한 설정)
│   └── 📦 jpa - JPA 공통 설정, BaseEntity
└── supports (부가기능 애드온)
    ├── 📦 jackson - JSON 직렬화 설정
    ├── 📦 logging - 로깅 설정 (Slack 연동)
    └── 📦 monitoring - 메트릭스, 트레이싱 (Prometheus, Grafana)
```

### 계층별 상세 구조

#### 🎯 Domain Layer (도메인 계층)

**핵심 비즈니스 로직, 인프라에 독립적**

- **엔티티 (Entities)**:
    - `User` - 사용자 정보 (로그인ID, 이메일, 생년월일, 성별)
    - `Product` - 상품 정보 (브랜드, 이름, 가격, 재고)
    - `Point` - 포인트 정보 (사용자별 보유 포인트)
    - `Brand` - 브랜드 정보
    - `ProductLike` - 상품 좋아요
    - `ProductSummary` - 상품 집계 정보 (좋아요 수)

- **값 객체 (Value Objects)**:
    - `LoginId`, `Email`, `BirthDate`, `Gender` - 사용자 관련
    - `ProductInfo`, `ProductLikeStatus` - 상품 관련

- **도메인 서비스**:
    - `*QueryService` - 조회 전용 서비스
    - `*WriteService` - 변경 전용 서비스
    - `*Service` - 비즈니스 로직 서비스

- **리포지토리 인터페이스**: 데이터 접근 추상화

#### 🚀 Application Layer (애플리케이션 계층)

**유스케이스 오케스트레이션**

- **Facade 패턴으로 구현**:
    - `UserFacade` - 회원가입, 내 정보 조회
    - `ProductFacade` - 상품 좋아요/취소
    - `ProductQueryFacade` - 상품 목록/상세 조회
    - `PointFacade` - 포인트 충전/조회

- **Input/Output DTO**: 계층간 데이터 전송
- **트랜잭션 경계**: `@Transactional` 관리

#### 🔧 Infrastructure Layer (인프라스트럭처 계층)

**외부 시스템과의 연동**

- **JPA Repository 구현체**:
    - `*RepositoryImpl` - 도메인 리포지토리 인터페이스 구현
    - `*JpaRepository` - Spring Data JPA 인터페이스

- **BaseEntity**: 공통 엔티티 기능
    - 생성/수정/삭제 시간 자동 관리
    - Soft Delete 지원 (`deletedAt`)
    - 엔티티 검증 (`guard()` 메서드)

#### 🌐 Interfaces Layer (인터페이스 계층)

**외부와의 접점**

- **REST API Controllers**:
    - `UserV1Controller` - 사용자 관련 API
    - `ProductV1Controller` - 상품 관련 API
    - `PointV1Controller` - 포인트 관련 API
    - `LikeV1Controller` - 상품 좋아요 API

- **통일된 응답 형식**: `ApiResponse<T>` 래퍼
- **전역 예외 처리**: `ApiControllerAdvice`
- **API 문서화**: SpringDoc OpenAPI 자동 생성

## 🛠️ 기술 스택

### 프레임워크

- **Spring Boot** - 웹 애플리케이션 프레임워크
- **Spring Data JPA** - 데이터 접근 계층
- **Spring Validation** - 입력 데이터 검증

### 데이터베이스

- **MySQL** - 메인 데이터베이스
- **Kotlin JDSL** - QueryDSL 대안 (타입 세이프 쿼리)

### 테스트

- **Kotest** - Kotlin 테스트 프레임워크
- **Testcontainers** - 통합 테스트용 컨테이너
- **SpringMockK** - Kotlin용 모킹 라이브러리

### 모니터링 & 로깅

- **Prometheus** - 메트릭 수집
- **Grafana** - 메트릭 시각화
- **Logback** - 로깅 프레임워크
- **Slack Appender** - Slack 로그 알림

### 기타

- **Jackson** - JSON 직렬화/역직렬화
- **ktlint** - Kotlin 코드 스타일
- **pre-commit** - 커밋 전 코드 검증

## ⚡ 주요 기능

### 1. 사용자 관리

- **회원가입**: ID/이메일/생년월일/성별 검증
- **내 정보 조회**: 로그인 ID 기반 사용자 정보 조회

### 2. 포인트 시스템

- **포인트 조회**: 현재 보유 포인트 확인
- **포인트 충전**: 포인트 충전 및 로그 기록
- **포인트 로그**: 충전/사용 이력 추적

### 3. 상품 관리

- **상품 목록 조회**: 브랜드 필터링, 정렬 지원 (최신순, 가격순, 좋아요순)
- **상품 상세 조회**: 개별 상품 정보 조회
- **브랜드 정보**: 브랜드 상세 정보 조회

### 4. 상품 좋아요

- **좋아요 등록/취소**: 로그인 사용자 대상
- **좋아요한 상품 목록**: 내가 좋아요한 상품 조회
- **좋아요 수 집계**: ProductSummary를 통한 실시간 집계

## 🎨 설계 패턴 및 원칙

### 적용된 패턴

- **Repository 패턴**: 데이터 접근 추상화
- **Facade 패턴**: 복잡한 서브시스템 단순화
- **Value Object 패턴**: 도메인 개념 표현
- **CQRS**: Query/Write 서비스 분리

### 설계 원칙

- **의존성 역전**: Domain이 Infrastructure에 의존하지 않음
- **단일 책임**: 각 계층별 명확한 역할 분리
- **도메인 중심**: 비즈니스 로직을 Domain Layer에 집중

## 🔄 의존성 흐름

```
Controller → Facade → DomainService → Repository(Interface)
    ↓           ↓           ↓              ↑
Interfaces  Application  Domain    Infrastructure
                                   (RepositoryImpl)
```

## 🧪 테스트 전략

### 테스트 계층

- **단위 테스트**: 도메인 객체 검증 로직
- **통합 테스트**: 서비스 계층 테스트 (with TestContainers)
- **E2E 테스트**: API 엔드포인트 테스트

### 테스트 명명 규칙

- `*Test.kt` - 단위 테스트
- `*IGTest.kt` - 통합 테스트 (Integration Test)
- `*E2ETest.kt` - E2E 테스트

## 📚 향후 개발 계획

### Phase 1 (현재 구현 완료)

- ✅ 사용자 관리 (회원가입, 조회)
- ✅ 포인트 시스템 (충전, 조회)
- ✅ 상품 관리 (목록, 상세, 브랜드)
- ✅ 상품 좋아요 기능

### Phase 2 (계획)

- 🔄 주문 시스템 구현
- 🔄 결제 시스템 (포인트 결제)
- 🔄 재고 관리 시스템
- 🔄 배송지 관리

## 🚀 개발 환경 설정

### 필수 설치

```bash
# 프로젝트 초기 설정
make init

# 개발 인프라 실행
docker-compose -f ./docker/infra-compose.yml up

# 모니터링 환경 실행
docker-compose -f ./docker/monitoring-compose.yml up
```

### 모니터링 접속

- **Grafana**: http://localhost:3000 (admin/admin)
- **Application**: 실행 후 actuator 엔드포인트 활용

## 📁 주요 파일 구조

```
apps/commerce-api/src/main/kotlin/com/loopers/
├── application/          # 애플리케이션 계층
│   ├── user/            # 사용자 유스케이스
│   ├── product/         # 상품 유스케이스
│   └── point/           # 포인트 유스케이스
├── domain/              # 도메인 계층
│   ├── user/            # 사용자 도메인
│   ├── product/         # 상품 도메인
│   ├── point/           # 포인트 도메인
│   └── brand/           # 브랜드 도메인
├── infrastructure/      # 인프라 계층
│   ├── user/            # 사용자 저장소 구현
│   ├── product/         # 상품 저장소 구현
│   └── point/           # 포인트 저장소 구현
└── interfaces/          # 인터페이스 계층
    └── api/             # REST API
        ├── user/        # 사용자 API
        ├── product/     # 상품 API
        ├── point/       # 포인트 API
        └── like/        # 좋아요 API
```

---

*이 문서는 프로젝트 분석을 통해 자동 생성되었습니다.*