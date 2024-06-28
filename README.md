# BaseBall_data_Crowling

참고 사이트 및 사용 api   
KBO 데이터 크롤링 사이트 스탯티즈 :  [(https://statiz.sporki.com/)]   
MLB 데이터 크롤링 사용 API : [(https://pypi.org/project/MLB-StatsAPI/)]
***
# baseball 라이브러리
파이썬의 statsapi, bs4를 이용하여 MLB와 KBO의 오늘경기 일정 및 어제 경기 내용을 들고와 DB에 데이터를 insert하는 Batch라이브러리입니다.
데이터의 크롤링 및 마이그레이션은 매일 정각에 실행되고, json파일의 형태로 남아있기때문에 파일로 확인도 가능합니다.
***
# Version 설명
### 2024.06.25
~~baseball-1.0.0.jar~~ *Paths에 문제가 있어 사용하지 않습니다.*

### 2024.06.26
~~baseball-1.0.7.jar~~ *Paths 문제 해결 및 Scheduling을 추가하였습니다.*

### 2024.06.28
__baseball-1.1.0.jar__ *파일의 Paths, DBconnection, Schedule시간 조정을 메인 프로젝트에서 변경 가능하도록 하였습니다.*



***
### 의존성 주입 
![image](https://github.com/jjjch0723/BaseBall_API/assets/83578340/cb97a289-5df9-403b-9155-58eaa0fceaad)

### py파일 경로.java | Getter, Setter을 만들어야합니다. 혹은 Lombok도 사용가능합니다.
![image](https://github.com/jjjch0723/BaseBall_API/assets/83578340/0ccbde93-a4ed-4a36-9294-00354b2d3193)

### Schedule-cron.java | Getter, Setter을 만들어야합니다. 혹은 Lombok도 사용가능합니다.
![image](https://github.com/jjjch0723/BaseBall_API/assets/83578340/1648991c-972e-4adc-9c5e-6b82e0a3e009)

### DB설정.java | Getter, Setter을 만들어야합니다. 혹은 Lombok도 사용가능합니다.
![image](https://github.com/jjjch0723/BaseBall_API/assets/83578340/68741d4d-1052-42c1-9c6c-445a38a3228c)

### API에 필요한 정보들을 관리하는 Service.java
![image](https://github.com/jjjch0723/BaseBall_API/assets/83578340/46398032-cddb-40ea-b7a1-f122fef01795)

### api객체 등록 및 mapper.xml파일 읽도록 설정 | 어노테이션을 참고해 주세요.
![image](https://github.com/jjjch0723/BaseBall_API/assets/83578340/73d71db6-568d-4023-9be0-a1458e964852)

### Application.properties 혹은 Application.yml에 경로, DB, Scheduling 시간을 설정합니다.
![image](https://github.com/jjjch0723/BaseBall_API/assets/83578340/bc4b1e92-cbaf-4d34-b270-e61772735fe2)

***
## Update 방향성
* Rest API로 변경 예정.
* ~~파일의 경로, Scheduling시간 및 DB설정을 사용자가 입력하여 지정.~~
