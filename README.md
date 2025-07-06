# Spring Boot + Custom Metrics Exporter Demo

Spring Boot 애플리케이션과 Prometheus Custom Exporter를 활용한 메트릭 수집 데모 프로젝트

## 프로젝트 구조
```
├── docker-compose.yml          # Spring Boot + MySQL 실행
├── src/                        # Spring Boot 애플리케이션
│   └── main/java/com/example/demo/
├── spring-metrics-exporter/    # Python Prometheus Exporter
│   ├── exporter.py
│   ├── requirements.txt
│   └── README.md
└── README.md                   # 이 파일
```

## 실행 방법

### 1. 사전 요구사항
- Docker & Docker Compose
- Python 3.x
- Java 17+ (Docker 사용 시 불필요)

### 2. 프로젝트 클론
```bash
# 프로젝트 클론
git clone https://github.com/thzthix/spring-metrics-demo.git
cd spring-metrics-demo
```

### 3. Spring Boot + MySQL 실행
```bash

# MySQL + Spring Boot 실행
docker-compose up -d

# 앱 실행 확인
curl http://localhost:8080/api/users
```

### 4. Python Metrics Exporter 실행
```bash
# Python 패키지 설치
cd spring-metrics-exporter
pip install -r requirements.txt

# Exporter 실행
python exporter.py
```

### 5. 메트릭 확인
```bash
# Prometheus 메트릭 확인
curl http://localhost:8000/metrics
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
- **Metrics Exporter**: 8000

## 학습 목표
1. Spring Boot REST API 개발
2. MySQL 데이터베이스 연동
3. Prometheus Custom Exporter 개발
4. 실시간 메트릭 수집 및 모니터링
5. Docker Compose를 활용한 멀티 컨테이너 환경

## 트러블슈팅

### MySQL 연결 오류
```bash
# MySQL 컨테이너 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs mysql
```

### Python 패키지 설치 오류
```bash
# 가상환경 사용 권장
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
pip install -r requirements.txt
```

## 확장 아이디어
- Grafana 대시보드 연동
- 알림 시스템 추가
- 더 복잡한 비즈니스 메트릭 수집
- Kubernetes 환경으로 배포