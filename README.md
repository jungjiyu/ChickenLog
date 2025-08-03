# 🍗 ChickenLog — 실시간 치킨 가게 랭킹 플랫폼

**ChickenLog**는 경상북도 지역의 치킨 가게 정보를 실시간으로 수집하고, 리뷰 수 기반으로 랭킹화하여 사용자에게 제공합니다. 자동 크롤링, 데이터베이스 자동 반영, 개인 메모 기능 등 치킨을 사랑하는 사람들을 위한 정보를 한눈에 볼 수 있도록 설계된 웹 애플리케이션입니다.

---

## 🚀 주요 기능

- **🗺 지도 기반 상위 랭킹 가게 표시**  
  경상북도 지도 위에 상위 치킨 가게를 시각화하여 확인 가능

- **📦 실시간 데이터 자동 수집 및 반영**  
  Selenium + FastAPI 기반 크롤링 서버가 매 시간마다 데이터를 수집하고 Spring Boot 서버가 자동으로 DB에 반영

- **📂 Spring Data JPA 기반 REST API 제공**  
  대부분의 쿼리를 JPA 메서드 기반으로 처리하며, 필요한 경우 JPQL `@Query`, `Fetch Join`을 활용하여 최적화

- **📝 사용자 메모 기능**  
  로그인한 사용자는 특정 가게에 대한 메모를 생성, 수정, 삭제할 수 있음

- **💬 최신 리뷰 및 대표 메뉴 정보 제공**  
  가게별 대표 메뉴와 리뷰(최대 10개)를 확인 가능

- **📡 Docker 기반 컨테이너 환경 구성**  
  백엔드, 크롤러, DB, Nginx 를 Docker 컨테이너로 관리

---

## 🛠 기술 스택

| 영역       | 기술 스택 |
|------------|------------|
| 백엔드     | Spring Boot, Spring Data JPA|
| 크롤러     | FastAPI, Selenium, Uvicorn |
| 데이터베이스 | MySQL |
| 배포 및 인프라 | Docker, Nginx |

---

## 📐 시스템 아키텍처

<img width="1674" height="846" alt="image" src="https://github.com/user-attachments/assets/39c22367-2768-45b0-83e8-286219d08d69" />


---


## 💡 주요 구현
- 요기요 플랫폼에서 치킨 가게 상세 정보, 메뉴, 리뷰 등을 Selenium으로 수집 및 csv 파일 기록
- Spring Boot의 `@Scheduled`로 FastAPI 호출 → 최신 CSV 파싱 → DB 자동 반영의 동기 처리
- `@Query`, `Fetch Join`으로 N+1 문제 해결
