# 📈 주식 종목 토론방 
**Spring 을 사용한 RestfulAPI 개인 프로젝트**

<br/>

## 🖥️ 프로젝트 소개
Spring 으로 만든 종목토론방 프로젝트입니다.

주요 기능으로는 뉴스피드 기능과 주식종목 데이터 제공 기능이 있습니다.

<br>


## 🕰️ 개발 기간
* 24.01.24일 ~ 24.03.04일
<br/>

## ⚙️ 개발 환경
- `Java 17`
- **프레임워크(Framework)** : Springboot(3.x,2.x)
- **데이터베이스(Database)** : MySQL (8.0.22)
- **분산 데이터 처리 및 메시징** : Redis (7.2.4), Kafka (5.5.1), Zookeeper (5.5.1)
- **지속적 통합 및 배포(CI/CD)** : Jenkins (2.444)
- **컨테이너화 및 가상화** : Docker (25.0.0)
- **통합 개발 환경(IDE)** : IntelliJ 2202.3.3
<br/>

## 📍 시스템 구성도
![image](https://github.com/user-attachments/assets/54928cc2-80f8-4869-8b8a-62b5c1c530b5)


<br/>

## 📍 MS 별 주요 기능
- **USER MS**
  - **유저** : 상세 페이지 <a href="https://github.com/KoKimSS/stockDiscussionMSA/wiki/User" >상세보기 - WIKI 이동</a>
  - **팔로우** : 상세 페이지 <a href="https://github.com/KoKimSS/stockDiscussionMSA/wiki/Follow" >상세보기 - WIKI 이동</a>
- **ACTIVITY MS**
  - **포스터** : 상세 페이지 <a href="https://github.com/KoKimSS/stockDiscussionMSA/wiki/POSTER" >상세보기 - WIKI 이동</a>
  - **댓글** : 상세 페이지 <a href="https://github.com/KoKimSS/stockDiscussionMSA/wiki/REPLY" >상세보기 - WIKI 이동</a>
  - **좋아요** : 상세 페이지 <a href="https://github.com/KoKimSS/stockDiscussionMSA/wiki/LIKE" >상세보기 - WIKI 이동</a>
- **NEWSFEED MS**
  - **뉴스피드** : 상세 페이지 <a href="https://github.com/KoKimSS/stockDiscussionMSA/wiki/NEWSFEED" >상세보기 - WIKI 이동</a>
- **STOCK MS**
  - **주식** : 상세 페이지 <a href="https://github.com/KoKimSS/stockDiscussionMSA/wiki/Stock" >상세보기 - WIKI 이동</a>
<br/>

## ⚡ 주요 기능 WorkFlow
- **NewsFeed 데이터 생성 - kafka 활용**
  <details>
  
  ![MSA시스템구성도-페이지-2 drawio](https://github.com/KoKimSS/stockDiscussionMSA/assets/97881804/af55691f-8e54-4213-97a9-fe74945d16bb)

  </details>


<br/>

<br/>

## 💢 Trouble Shooting

### 📌게시글 좋아요 수 구현의 레이스 컨디션 해결

**문제**
- 게시글 좋아요 수 구현 시 발생한 레이스 컨디션 문제를 해결해야 했습니다.

**해결 방법**
- Redis의 `incr` 명령어를 활용하여 좋아요 수를 저장하고, 스케줄러 기반 배치 처리를 통해 DB에 반영하였습니다.
- 레이스 컨디션 문제 해결을 위한 락 성능 테스트를 진행했습니다.

| 지표 | 락 없음 | 낙관적 락 | 비관적 락 | Redis |
| --- | --- | --- | --- | --- |
| 응답 성공률(%) | 100% | 39% | 100% | 100% |
| 좋아요 수 반영 비율 (%) | 27% | 39% | 100% | 100% |
| 응답 속도 (ms) | 18 | 43 | 420 | 13 |

**결론**
- Redis의 `incr` 명령어 사용 시 좋아요 수가 100% 반영되었고, 응답 속도가 13ms로 최대 32배 빨랐습니다.

### 📌뉴스피드 서비스 부하 해결

**문제**
- FanOutOnWrite 방식의 뉴스피드 구현 시 팔로워 수 만큼의 뉴스피드 데이터를 미리 생성하여 서버에 부하가 발생했습니다.

**해결 방법**
- 메시지 브로커인 Kafka를 활용하여 뉴스피드 서비스에서 메시지를 순차적으로 소비하는 방식으로 전환했습니다.
- 뉴스피드 생성 성능 테스트를 진행했습니다.

| 지표 | Kafka 사용 전 | Kafka 사용 후 |
| --- | --- | --- |
| 동시요청 응답시간 (ms) | 2,697 | 15 |
| CPU 사용률 | 42% | 5% |

**결과**
- Kafka를 활용한 비동기 처리를 구현하여 최대 응답 속도를 180배 개선하고, CPU 사용률을 1/8 절감했습니다.

### 📌대용량 일봉 데이터 저장 성능 개선

**문제** 
- 5년치 차트 정보인 360만개의 일봉 데이터를 저장하는 작업의 성능이 JPA `saveAll()` 메소드 사용 시 7시간이 걸렸습니다.

**해결 방법**
- MySQL의 벌크 인서트를 사용하여 쿼리 수를 1/1000로 줄이고 멀티 쓰레드를 적용했습니다.

| 지표 | JPA saveAll() | 벌크 인서트 적용 | 멀티 쓰레드 적용 |
| --- | --- | --- | --- |
| 저장 수행 시간 | 7h 1m 35s | 32s | 32s |

**결과**
- 벌크 인서트와 멀티 쓰레드를 사용하여 100배 이상 성능을 개선했습니다.

### 📌일봉 테이블 페이징 쿼리 성능 개선

**문제**
- 360만개의 일봉 테이블 페이징 쿼리 성능이 1초가 넘었습니다.

**해결 방법**
- 쿼리의 `where`절 조건에 맞게 종목코드와 날짜를 복합인덱스로 설정했습니다.

| 페이징 조건 | 인덱스 적용 전 | 인덱스 적용 후 |
| --- | --- | --- |
| 한달치 데이터 조회 | 1.66s | 0.00s |
| 일년치 데이터 조회 | 1.70s | 0.00s |

**결과**
- Where절 조건에 맞는 복합인덱스 생성으로 100배 이상 페이징 쿼리 성능을 개선했습니다.

<br/>  


