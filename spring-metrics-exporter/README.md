# Spring Boot Metrics Exporter 실습 프로젝트

## 프로젝트 개요
Spring Boot 애플리케이션에서 메트릭을 수집하여 Prometheus 형식으로 노출하는 Custom Exporter 개발 실습

## 현재 환경 설정

### Spring Boot 애플리케이션
- **경로**: `/home/seoha/spring-tutorial-7th-revise`
- **실행 방법**: `docker-compose up -d`
- **포트**: 8080
- **API 엔드포인트**: `http://localhost:8080/api/users`

### 데이터베이스 (MySQL)
- **컨테이너**: mysql_asac
- **포트**: 3306
- **데이터베이스**: asac
- **사용자**: asac / 1234

### 테스트 데이터
현재 3명의 사용자가 등록되어 있음:
1. 김철수 (25세, 개발자, Spring Boot)
2. 이영희 (28세, 엔지니어, Database)
3. 박민수 (30세, 개발자, React)

## Custom Exporter 개발 계획

### 목표 아키텍처
로그인 위변조 탐지 시스템을 모방한 데이터 변화 탐지 시스템
- 주기적으로 데이터 스캔
- 이전 상태와 현재 상태 비교
- 변화량을 메트릭으로 수집 및 노출

### 수집할 메트릭
```
# 기본 메트릭
spring_app_users_total                    # 전체 사용자 수
spring_app_users_by_job{job="개발자|엔지니어"}  # 직업별 사용자 수
spring_app_up                            # 애플리케이션 상태
spring_app_api_response_time_seconds     # API 응답 시간
spring_app_db_connection_up              # DB 연결 상태

# 변화 탐지 메트릭
user_data_changes_total{type="added|modified|deleted"}  # 변화 유형별 카운트
user_data_drift_percentage                              # 전체 데이터 대비 변화율
last_scan_timestamp                                     # 마지막 스캔 시간
data_integrity_score                                    # 데이터 무결성 점수
```

### 개발 환경
- **언어**: Python 3
- **라이브러리**: 
  - `prometheus_client` (메트릭 노출)
  - `requests` (API 호출)
  - `mysql-connector-python` (DB 연결)
- **포트**: 8000 (메트릭 서버)

## 실습 시나리오

### 1단계: 기본 메트릭 수집
- Spring Boot API 호출하여 사용자 데이터 수집
- 기본 메트릭 계산 및 노출

### 2단계: 데이터베이스 직접 접근
- MySQL에 직접 연결하여 데이터 수집
- API와 DB 데이터 비교

### 3단계: 변화 탐지 구현
- 이전 상태 저장 로직
- 변화 탐지 및 메트릭 생성
- 시간대별 변화 패턴 추적

### 4단계: 테스트 및 검증
- 사용자 데이터 변경 시 메트릭 변화 확인
- Prometheus 형식으로 메트릭 노출 확인

## 명령어 모음

### Spring Boot 앱 실행
```bash
cd /home/seoha/spring-tutorial-7th-revise
docker-compose up -d
```

### 사용자 생성 (POST)
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "홍길동", "age": 32, "job": "개발자", "specialty": "Python"}'
```

### 사용자 조회 (GET)
```bash
curl http://localhost:8080/api/users
```

### Python 패키지 설치
```bash
pip install prometheus_client requests mysql-connector-python
```

### 메트릭 확인
```bash
curl http://localhost:8000/metrics
```

## 참고사항
- Docker 컨테이너 간 네트워크 통신 시 `mysql` 호스트명 사용
- JobType enum: "개발자", "엔지니어" (한국어)
- 메트릭 수집 주기: 30초 권장
- 로그 레벨: INFO 권장

## 다음 단계
1. Python 파일 생성 및 기본 구조 작성
2. 메트릭 정의 및 수집 로직 구현
3. HTTP 서버 시작 및 테스트
4. 변화 탐지 로직 추가
5. 종합 테스트 및 검증