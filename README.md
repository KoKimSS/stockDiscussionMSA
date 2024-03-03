# 📈 주식 종목 토론방 
**Spring 을 사용한 RestfulAPI 개인 프로젝트**

<br/>

## 🖥️ 프로젝트 소개
Spring 으로 만든 종목토론방 프로젝트입니다.

주요 기능으로는 뉴스피드 기능과 주식종목 데이터 제공 기능이 있습니다.

<br>


## 🕰️ 개발 기간
* 24.01.24일 ~
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
<details>
  <summary> 시스템 구성도 열기 </summary>
  
![MSA시스템구성도-페이지-1 drawio](https://github.com/KoKimSS/stockDiscussionMSA/assets/97881804/6485b467-1ff0-4f9f-b363-f6f83cfd552b)

</details>

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
    <summary> WorkFlow 열기 </summary>
  
  ![MSA시스템구성도-페이지-2 drawio](https://github.com/KoKimSS/stockDiscussionMSA/assets/97881804/af55691f-8e54-4213-97a9-fe74945d16bb)

  </details>


<br/>

- **StockCandle 데이터 생성 - batch 활용**
- **JWT 인증 - jwt 활용**
<br/>

## 💢 Trouble Shooting
- **NewsFeed 데이터 생성 비동기 처리**
- **StockCandle 데이터 생성 Batch 성능 ISSUE**
- **StockCandle 데이터 조회 성능 ISSUE**
- **좋아요 count 동시성 문제 ISSUE**
<br/>  
  
## **Docker Compose 사용 가이드**
- **Docker Compose 명령어** : 상세 페이지 <a href="https://github.com/KoKimSS/stockDiscussionMSA/wiki/DockerCompose%EB%AA%85%EB%A0%B9%EC%96%B4" >상세보기 - WIKI 이동</a>


