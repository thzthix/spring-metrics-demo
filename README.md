# Spring Boot + Custom Metrics Exporter Demo

Spring Boot 애플리케이션과 Prometheus Custom Exporter를 활용한 **실시간 메트릭 수집** 데모 프로젝트

## 프로젝트 구조
```
├── docker-compose.yml          # 전체 시스템 오케스트레이션
├── spring-app/                 # Spring Boot 애플리케이션 모듈
│   ├── src/
│   ├── build.gradle
│   └── ...
├── metrics-exporter/           # Python Prometheus Exporter 모듈
│   ├── Dockerfile             # Python 컨테이너 설정
│   ├── exporter.py            # 실시간 메트릭 수집기 (READ COMMITTED)
│   └── requirements.txt
├── monitoring/                 # 모니터링 설정 모듈
│   └── prometheus.yml         # Prometheus 설정 파일
└── README.md                   # 이 파일
```

## ✨ 주요 특징
- 🚀 **실시간 메트릭 업데이트**: 사용자 추가 시 1-2초 내 자동 반영
- 🔄 **MySQL READ COMMITTED**: 트랜잭션 격리 수준 문제 해결
- 📊 **완전한 모니터링 스택**: Spring Boot + MySQL + Python Exporter + Prometheus
- 🏗️ **모듈화된 마이크로서비스**: 각 컴포넌트별 독립적 컨테이너
- 🐳 **원클릭 실행**: docker-compose up -d로 전체 시스템 구동

## 실행 방법

### 1. 사전 요구사항
- Docker & Docker Compose

### 2. 프로젝트 클론
```bash
git clone https://github.com/thzthix/spring-metrics-demo.git
cd spring-metrics-demo
```

### 3. 원클릭 실행
```bash
# 전체 시스템 실행 (MySQL + Spring Boot + Python Exporter + Prometheus)
docker-compose up -d
```

### 4. 서비스 확인
```bash
# 서비스 실행 확인
curl http://localhost:8080/api/users    # Spring Boot API
curl http://localhost:9090              # Prometheus UI
curl http://localhost:8000/metrics     # Raw Metrics
```

### 5. 재실행 시
```bash
# 기존 컨테이너 정리 후 재실행
docker-compose down
docker-compose up -d
```

## 테스트 시나리오

### 1. 초기 상태 확인
```bash
# 현재 사용자 조회
curl http://localhost:8080/api/users

# 메트릭 확인
curl http://localhost:8000/metrics | grep user_count
```

### 2. 사용자 추가
```bash
# 개발자 추가
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "홍길동", "age": 32, "job": "개발자", "specialty": "Python"}'

# 엔지니어 추가  
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "김영희", "age": 29, "job": "엔지니어", "specialty": "Cloud"}'
```

### 3. 메트릭 변화 확인
```bash
# 1초 후 메트릭 자동 업데이트 확인
curl http://localhost:8000/metrics | grep user_count
```

## 예상 메트릭 결과
```
# HELP user_count Number of user
# TYPE user_count gauge
user_count 7.0

# HELP user_count_by_group Number of user by group  
# TYPE user_count_by_group gauge
user_count_by_group{job="DEVELOPER"} 4.0
user_count_by_group{job="ENGINEER"} 3.0
```

## 포트 정보
- **Spring Boot**: 8080
- **MySQL**: 3306  
- **Python Metrics Exporter**: 8000
- **Prometheus**: 9090

## 학습 목표
1. Spring Boot REST API 개발
2. MySQL 데이터베이스 연동
3. Prometheus Custom Exporter 개발
4. 실시간 메트릭 수집 및 모니터링
5. Docker Compose를 활용한 완전한 모니터링 스택 구성
6. 모듈화된 마이크로서비스 아키텍처 설계

## 트러블슈팅

### MySQL 연결 오류
```bash
# MySQL 컨테이너 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs mysql
```

### Prometheus 접속 문제
```bash
# Prometheus 컨테이너 상태 확인
docker-compose ps prometheus

# Prometheus 로그 확인
docker-compose logs prometheus
```

### Python 패키지 설치 오류
```bash
# 가상환경 사용 권장
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
pip install -r requirements.txt
```

## 확장 아이디어
- Grafana 대시보드 연동 (monitoring/ 디렉토리에 추가)
- 알림 시스템 추가 (Prometheus AlertManager)
- 더 복잡한 비즈니스 메트릭 수집
- Kubernetes 환경으로 배포
- 각 모듈별 독립적 Docker 이미지 생성