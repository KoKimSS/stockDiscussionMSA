# 주식 종목 토론방 
스프링 부트를 사용한 RestfulAPI 개인 프로젝트



## 🖥️ 프로젝트 소개
Spring으로 만든 종목토론방 프로젝트입니다.

주요 기능으로는 뉴스피드 기능과 주식종목 데이터 제공 기능이 있습니다.
<br>


## 🕰️ 개발 기간
* 24.01.24일 ~


## ⚙️ 개발 환경
- `Java 17`
- **통합 개발 환경(IDE)** : IntelliJ 2202.3.3
- **프레임워크(Framework)** : Springboot(3.x,2.x)
- **데이터베이스(Database)** : MySQL (8.0.22)
- **지속적 통합 및 배포(CI/CD)** : Jenkins (2.444)
- **분산 데이터 처리 및 메시징** : Redis (7.2.4), Kafka (5.5.1), Zookeeper (5.5.1)
- **컨테이너화 및 가상화** : Docker (25.0.0)

## 📍 시스템 구성도
![MSA시스템구성도 (2)](https://github.com/KoKimSS/stockDiscussionMSA/assets/97881804/34661f94-7761-413b-8e3c-5f31a80f3d24)

# **Docker Compose 사용 가이드**

## **1. 컴포즈 실행**

### **1.1 기본 실행**

컴포즈 파일이 존재하는 디렉터리에서 실행합니다.

```bash
docker-compose up -d
```

### **1.2 특정 파일 사용**

다른 컴포즈 파일을 사용하려면 파일 경로를 지정합니다.

```bash
docker-compose -f 컴포즈파일_경로 up
```

### **1.3 백그라운드 실행**

컴포즈를 백그라운드에서 실행합니다.

```bash
docker-compose up -d
```

### **1.4 서비스 스케일 조정**

특정 서비스의 컨테이너 개수를 조정합니다.

```bash
docker-compose --scale 서비스_명=서비스수 up
```

## **2. 컴포즈 종료**

### **2.1 모든 컨테이너 종료 및 삭제**

모든 컴포즈 컨테이너를 종료하고 삭제합니다.

```bash
docker-compose down
```

## **3. 컴포즈 정지**

### **3.1 모든 컨테이너 정지**

모든 컴포즈 컨테이너를 정지합니다.

```bash
docker-compose stop
```

## **4. 컴포즈 컨테이너 확인**

컴포즈로 실행 중인 컨테이너의 상태를 확인합니다.

```bash
docker-compose ps
```

## **5. 로그 확인**

### **5.1 특정 서비스의 로그 확인**

특정 서비스의 로그를 확인합니다.

```bash
docker-compose logs 서비스_이름 -f
```

### **5.2 실시간 로그 확인**

실시간으로 로그를 확인합니다.

## **6. 컨테이너 조작**

### **6.1 컨테이너 실행**

서비스에 지정된 컨테이너를 실행합니다.

```bash
docker-compose run 서비스_명
```

### **6.2 컨테이너 시작 / 정지 / 일시정지 / 재개**

서비스에 지정된 컨테이너를 시작, 정지, 일시정지, 재개합니다.

```bash
docker-compose start 서비스_명
docker-compose stop 서비스_명
docker-compose pause 서비스_명
docker-compose unpause 서비스_명
```

## **7. 공개된 포트 표시**

컴포즈로 실행 중인 서비스의 공개된 포트를 표시합니다.

```bash
docker-compose port
```
