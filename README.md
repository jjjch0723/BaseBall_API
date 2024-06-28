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
~~baseball-1.0.7.jar~~ *경로 문제 해결 및 스케쥴링을 추가하였습니다.*

### 2024.06.28
__baseball-1.1.0.jar__ *파일의 경로, DBconnecting, Schedule시간 조정을 메인 프로젝트에서 변경 가능하도록 하였습니다.*



***
## Update 방향성
* Rest API로 변경 예정.
* 파일의 경로, Scheduling시간 및 DB설정을 사용자가 입력하여 지정.
