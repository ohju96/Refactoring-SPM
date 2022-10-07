# Refactoring : Small parking management Project
##### 리팩토링 : 소단지 경비원을 위한 주차 관리 웹 애플리케이션

🙋🏻‍♂️ 2022 개인 프로젝트

## 소단지 경비원을 위한 주차 관리 웹 애플리케이션 리팩토링

### 1. 프로젝트 소개
[소단지 경비원을 위한 주차 관리 웹 애플리케이션](https://github.com/ohju96/small-parking-Management-Project) 프로젝트를 진행하고 이 프로젝트를 실사용 하고 싶다는 요청을 받았다.

취업도 해야 하고 서버나 각종 API를 사용하는데 금전적인 부담이 있었기 때문에 실제 배포로 이어지진 않았으나 통화 미팅을 통해 받은 피드백과 문제를 개선하고자 기능을 추가하고 리팩토링을 계획하게 되었다.

### 2. 새로운 기능

> *1. 실시간 채팅 기능*   > 
> 이슈 :  https://github.com/ohju96/Refactoring-SPM/issues/20
> 사용 기술 : Web Socket   
> 내용 : 생성된 채팅방 목록을 5초마다 보여주며 실시간으로 채팅이 가능하다.


> *2. 자동 번역 문자 전송 기능*   
> 이슈 :  https://github.com/ohju96/Refactoring-SPM/issues/19
> 사용 기술 : [CoolSMS API](https://coolsms.co.kr/), [Papago API](https://developers.naver.com/docs/papago/README.md)   
> 내용 : 전달 사항을 입력하고 전화번호를 입력하면 자동으로 한글 -> 영어 번역을 완료하고 문자를 전송한다.


> *3. 경비원 공지 게시판*   
> 이슈 :  https://github.com/ohju96/Refactoring-SPM/issues/18
> 사용 기술 : JPA   
> 내용 : 공지 사항 처럼 활용이 가능한 게시판이다.


> *4. 날씨 확인 기능*   
> 이슈 :  https://github.com/ohju96/Refactoring-SPM/issues/14
> 사용 기술 : [OpenWeather API](https://openweathermap.org/api)   
> 내용 : 지금 온도 및 당일 해 뜨는 시간 및 지는 시간 등 날씨에 대한 정보를 얻을 수 있다.


> *5. Docker Container를 활용한 MongoDB, MariaDB 관리*   
> 이슈 :  https://github.com/ohju96/Refactoring-SPM/issues/12
> 사용 기술 : AWS EC2 Ubuntu, Docker   
> 블로그 : [AWS EC2 Ubuntu 20.04에서 DOcker를 활용해서 MariaDB, MongoDB 구축 후 연동](https://velog.io/@ohju96/%EC%86%8C%EA%B2%BD%EA%B4%80-AWS-EC2-Ubuntu-20.04%EC%97%90%EC%84%9C-Docker%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%B4%EC%84%9C-MariaDB-MongoDB-%EA%B5%AC%EC%B6%95-%ED%9B%84-%EC%97%B0%EB%8F%99)

### 3. Git Emoji
Commit에 대한 내용을 조금 더 직관적으로 활용하기 위해 깃 이모지를 활용한다. 규칙은 아래 표를 참고한다.

|이모지|♻️|✨|🐛|💄|➕
|--|--|--|--|--|--|
|규칙|기존 코드 리팩토링|새로운 추가 기능 코드|버그 수정|View 수정|의존성 추가|

### 4. 관련 링크
[# 블로그](https://velog.io/tags/%EC%86%8C%EA%B2%BD%EA%B4%80)   
[# 추가 기능 동영상](https://www.notion.so/ohju96/441ba8fb7123425faec6265fcafdb3f1#ce4ad2baf8bf4129b9f705ad71ae9740)   
[# 기존 프로젝트](https://github.com/ohju96/small-parking-Management-Project)
