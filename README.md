# BaseBall_data_Crowling

참고 사이트 및 사용 api   
KBO 데이터 크롤링 사이트 스탯티즈 :  [(https://statiz.sporki.com/)]   
MLB 데이터 크롤링 사용 API : [(https://pypi.org/project/MLB-StatsAPI/)]
***
# baseball API란
파이썬의 statsapi, bs4의 외부라이브러리를 이용하여 MLB와 KBO의 오늘경기(MLB의경우 내일의 경기)일정 및 어제 경기 내용을 들고와 데이터베이스에 일정 및 결과를 삽입하는 Batch프로그램입니다.
batch프로그램은 서버에 service로 등록되어 매일 오전 10시에 자동실행되고, 엔드 포인트 요청시 경기 일정, 경기 결과를 json형태로 응답받을수 있습니다. 
또한 baseball api가 실행되는 서버에 json파일의 형태로 남아있기때문에 파일로 확인도 가능합니다.
***
# baseball API 설명
* http://52.78.209.102:8080/api/v1/schedule/process
  * POST요청시 데이터베이스에서 임시테이블에 있는 다음날 경기 일정을 가져옵니다.
![예시데이터](https://github.com/user-attachments/assets/16042bb5-2554-43f4-8c92-32374ae09ec8)
  * KBO의 경우 매주 월요일이 휴일이며, 경기가 없을경우 "경기 없음"을 반환합니다.
  * 팀코드의 경우 아래의 표를 참고해주세요.
  * **보완해야 할 점 : 우천취소시 몇회에 마무리가 되었는지 기록은 없습니다.**
* http://52.78.209.102:8080/api/v1/results/process
  * POST요청시 데이터베이스에서 임시테이블에 있는 어제 경기 기록을 가져옵니다.
![results예시](https://github.com/user-attachments/assets/800472b1-a8e4-4c0e-80ce-880229c13d59)
  * 팀코드의 경우 아래의 표를 참고해주세요.
  * **보완해야 할 점 : MLB의 경우 서부, 중부, 동부로 나누어져있기때문에 시간관계상 기록을 가져오지 못하는 경우가 있습니다.**

    
|팀명|팀코드|
|---|---|
|두산 베어스|6002|
|엘지 트윈스|5002|
|케이티 위즈|12001|
|에스에스지 랜더스|9002|
|엔씨 다이노스|11001|
|기아 타이거즈|2002|
|롯데 자이언츠|3001|
|삼성 라이온즈|1001|
|한화 이글스|7002|
|키움 히어로즈|10001|
|애틀랜타 브레이브스|144|
|마이애미 말린스|146|
|뉴욕 메츠|121|
|필라델피아 필리스|143|
|워싱턴 내셔널스|120|
|시카고 컵스|112|
|신시내티 레즈|113|
|밀워키 브루어스|158|
|피츠버그 파이리츠|134|
|세인트루이스 카디널스|138|
|애리조나 다이아몬드백스|109|
|콜로라도 로키스|115|
|로스앤젤레스 다저스|119|
|샌디에이고 파드리스|135|
|샌프란시스코 자이언츠|137|
|볼티모어 오리올스|110|
|보스턴 레드삭스|111|
|뉴욕 양키스|147|
|탬파베이 레이스|139|
|토론토 블루제이스|141|
|시카고 화이트삭스|145|
|클리블랜드 가디언스|114|
|디트로이트 타이거스|116|
|캔자스시티 로열스|118|
|미네소타 트윈스|142|
|휴스턴 애스트로스|117|
|로스앤젤레스 에인절스|108|
|오클랜드 애슬레틱스|133|
|시애틀 매리너스|136|
|텍사스 레인저스|140|
|현대 유니콘스|4004|
***
# baseball API 사용법
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
