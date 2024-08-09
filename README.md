# Baseball API

## 목차
1. [Baseball API란](#11-baseball-api란)
    1.1. [Baseball API란](#11-baseball-api란)
    1.2. [참고 사이트 및 사용 API](#12-참고-사이트-및-사용-api)
2. [Baseball API 아키텍처 구조](#21-baseball-api-아키텍처-구조)
    2.1. [아키텍처 구조](#21-baseball-api-아키텍처-구조)
    2.2. [Baseball API 프로젝트 구조](#22-baseball-api-프로젝트-구조)
    2.3. [임의의 항목 2.3](#23-임의의-항목-23)
    2.4. [임의의 항목 2.4](#24-임의의-항목-24)
    2.5. [임의의 항목 2.5](#25-임의의-항목-25)
3. [상세 설명](#31-상세-설명)
    3.1. [Python 스크립트](#31-python-스크립트)
    3.2. [API 설명 및 사용법](#32-api-설명-및-사용법)
    3.3. [팀코드](#33-팀코드)
4. [느낀점 및 보완점](#41-느낀점-및-보완점)
    4.1. [느낀점](#41-느낀점)
    4.2. [보완점](#42-보완점)

---

## 1.1 Baseball API란

파이썬의 statsapi, bs4의 외부 라이브러리를 이용하여 MLB와 KBO의 오늘 경기(MLB의 경우 내일의 경기) 일정, 어제 경기 기록, 일정에 대한 GPT의 게임 결과 예상을 데이터베이스에 삽입하는 배치 프로그램입니다.

배치 프로그램은 서버에 서비스로 등록되어 매일 오전 10시에 자동 실행되고, 엔드포인트 요청 시 경기 일정, 경기 결과를 JSON 형태로 응답받을 수 있습니다. 또한 Baseball API가 실행되는 서버에 JSON 파일 형태로 남아있기 때문에 파일로 확인도 가능합니다.

## 1.2 참고 사이트 및 사용 API

| 이름     | 설명                                       | api명         | URL                                               | version |
|----------|--------------------------------------------|---------------|---------------------------------------------------|---------|
| 스탯티즈 | KBO의 데이터 사이트                        | X             | [Statiz](https://statiz.sporki.com/)              | X       |
| MLB-StatsAPI | 1871년부터 현재 시즌까지의 모든 데이터를 제공 | MLB-StatsAPI  | [MLB-StatsAPI](https://pypi.org/project/MLB-StatsAPI/) | 1.7.2   |
| OpenAI   | 게임 결과 예상을 위한 GPT-3.5-turbo 사용   | openai        | [OpenAI](https://pypi.org/project/openai/)        | 0.28.0  |
| BeautifulSoup | 스탯티즈에서 원하는 데이터를 크롤링하기 위해 사용 | bs4           | [BeautifulSoup](https://pypi.org/project/beautifulsoup4/) | 0.0.2   |

---

## 2.1 Baseball API 아키텍처 구조

~~~~
┌──────────┐
│  Python  │
│ 스크립트 │
└────▲─────┘
     │
     ▼
┌──────────┐
│ JSON 파일 │
└────▲─────┘
     │
     ▼
┌──────────┐
│   Java   │
│ 프로그램 │──────┐
└────▲─────┘      │
     │            ▼
     │       ┌────────────┐
     │       │ PostgreSQL │
     │       │ 데이터베이스 │
     │       └────────────┘
     │            ▲
     │            │
     ▼            ▼
┌──────────┐  ┌───────────┐
│    API   │  │ 예측 결과  │
│ 엔드포인트 │  │ (OpenAI)   │
└──────────┘  └───────────┘
~~~~

## 2.2 Baseball API 프로젝트 구조

~~~~
├── src/main/java
│   ├── com.game.baseball.api      * 프로젝트의 주요 패키지
│   │    ├── config                * 설정 관련 패키지
│   │    │   ├── BatchConfig.java     - 배치 작업 설정 클래스
│   │    │   ├── ExternalConfig.java  - 외부 설정 클래스
│   │    │   ├── ExternalConfigImpl.java - 외부 설정 구현 클래스
│   │    │   └── FilePathsUtil.java    - 파일 경로 유틸리티 클래스
│   │    │
│   │    ├── controller            * 컨트롤러 클래스 패키지
│   │    │   └── BaseBallController.java - 메인 컨트롤러 클래스
│   │    │
│   │    ├── dao                   * 데이터 접근 객체 패키지
│   │    │   ├── BaseBallDao.java     - DAO 인터페이스
│   │    │   └── BaseBallDaoImpl.java  - DAO 구현 클래스
│   │    │
│   │    ├── jsonFileManager       * JSON 파일 관리 패키지
│   │    │   ├── readManager          - JSON 읽기 관리 클래스
│   │    │   │   ├── readJSON.java       - JSON 읽기 기능
│   │    │   │   └── readJSONImpl.java   - JSON 읽기 구현 클래스
│   │    │   └── getDay.java         - 날짜 가져오기 기능
│   │    │
│   │    ├── mapper                * 매퍼 패키지
│   │    │   ├── BaseBallMapper.java   - 베이스볼 매퍼 인터페이스
│   │    │   └── BaseBallMapper.java   - 베이스볼 매퍼 클래스
│   │    │
│   │    ├── runPy                 * Python 스크립트 실행 패키지
│   │    │   ├── runPyfile.java      - Python 파일 실행 클래스
│   │    │   └── runPyfileImpl.java  - Python 파일 실행 구현 클래스
│   │    │
│   │    └── service               * 서비스 클래스 패키지
│   │        ├── CombinedTasklet.java   - 조합된 태스크릿 클래스
│   │        ├── GptPyRunnerImpl.java   - GPT Python 러너 구현 클래스
│   │        ├── PyRunner.java          - Python 러너 인터페이스
│   │        ├── ResultPyRunnerImpl.java- 결과 Python 러너 구현 클래스
│   │        ├── SchedulePyRunnerImpl.java - 스케줄 Python 러너 구현 클래스
│   │        └── BaseballApiApplication.java - 메인 애플리케이션 클래스
│   │
├── src/main/resources
│   ├── mappers                   * 매퍼 XML 설정 파일
│   │    └── baseball.xml            - 베이스볼 매퍼 XML 파일
│   │
│   ├── py                        * Python 관련 파일 폴더
│   │    ├── json                    - JSON 데이터 폴더
│   │    │    ├── todaysGames
│   │    │    │    ├── gameanalylis.json      - 경기 예상 결과 JSON
│   │    │    │    ├── yyyyMMddKBOresult.json - 특정 날짜 KBO 결과 JSON
│   │    │    │    ├── yyyyMMddMLBgames.json  - 특정 날짜 MLB 경기 JSON
│   │    │    │    ├── yyyyMMddKBOgames.json  - 특정 날짜 KBO 경기 JSON
│   │    │    │    └── yyyyMMddMLBresult.json - 특정 날짜 MLB 결과 JSON
│   │    │
│   │    ├── getBaseBallRating.py   - 베이스볼 등급 가져오기 Python 스크립트
│   │    ├── getCurKBOrst.py        - 현재 KBO 순위 가져오기 Python 스크립트
│   │    ├── getCurMLBrst.py        - 현재 MLB 순위 가져오기 Python 스크립트
│   │    ├── getTodayKBOgame.py     - 오늘의 KBO 경기 가져오기 Python 스크립트
│   │    └── getTodayMLBgame.py     - 오늘의 MLB 경기 가져오기 Python 스크립트
│   │
│   └── application.yml          * 애플리케이션 설정 파일
│

~~~~

## 2.3 테이블 네이밍 룰
- **tbl**: 모든 테이블 이름은 `tbl`로 시작합니다.
- **주제**: 테이블이 다루는 주요 주제에 따라 명명됩니다. 예: `kbo`, `mlb`, `unt`(유니티), `board`(게시판) 등.
- **세부주제**: 주제에 대한 세부 내용을 나타냅니다. 예: `bat`(타자), `pit`(투수), `player`(선수), `schedule`(일정) 등.
- **버전**: 테이블 버전을 나타내는 `nt`, `mt`, `ct` 등의 접미사를 사용합니다.
  - `nt`: 일반적인 데이터 테이블 (예: `tbl_kbopit_nt01`)
  - `mt`: 마스터 데이터 테이블 (예: `tbl_team_mt01`)
  - `ct`: 코드 또는 시퀀스 테이블 (예: `tbl_unt_item_type_ct01`)
  - `ttp`: 임시 테이블 (예: `tbl_kboschedule_ttp`)

## 2. 주제 및 세부주제 네이밍 규칙

### 2.1 KBO 및 MLB 관련 테이블
- **주제**: `kbo`, `mlb`
- **세부주제**: 
  - `bat`: 타자 스탯 (예: `tbl_kbobat_nt02`)
  - `pit`: 투수 스탯 (예: `tbl_kbopit_nt01`)
  - `player`: 선수 목록 (예: `tbl_kboplayer_nt01`)
  - `schedule`: 경기 일정 (예: `tbl_kboschedule_ttp`)
  - `res`: 경기 결과 (예: `tbl_kbores_ttp`)
  - `rslt`: 경기 최종 결과 (예: `tbl_kborslt_nt03`)

### 2.2 유니티 관련 테이블
- **주제**: `unt`
- **세부주제**: 
  - `dstats`: 육성 캐릭터 스탯 (예: `tbl_unt_dstats_nt01`)
  - `item`: 아이템 정보 (예: `tbl_unt_item_mt01`)
  - `quest`: 퀘스트 정보 (예: `tbl_unt_quest_nt01`)
  - `inven`: 유저 인벤토리 (예: `tbl_unt_inven_nt01`)
  - `gptlog`: GPT 대화 로그 (예: `tbl_unt_gptlog_nt01`)

### 2.3 웹 및 API 관련 테이블
- **주제**: `board`, `user`
- **세부주제**: 
  - `board`: 게시판 정보 (예: `tbl_board_nt01`)
  - `comment`: 게시글 댓글 (예: `tbl_comment_nt01`)
  - `userinfo`: 유저 정보 (예: `tbl_user_nt_01`)

## 3. 버전 네이밍 규칙

- **`nt` (Normal Table)**: 일반 테이블, 주로 데이터를 저장하고 관리하는 데 사용됩니다. 예: `tbl_kbopit_nt01`
- **`mt` (Master Table)**: 마스터 데이터 관리 테이블, 변경이 적고 참조되는 기본 데이터를 저장합니다. 예: `tbl_team_mt01`
- **`ct` (Code Table)**: 코드 또는 시퀀스 관리 테이블, 시스템에서 사용하는 고정된 코드 값을 관리합니다. 예: `tbl_unt_item_type_ct01`
- **`ttp` (Temporary Table)**: 임시 데이터 저장을 위한 테이블로, 배치 작업 중이나 일시적으로 데이터가 필요한 경우 사용합니다. 예: `tbl_kboschedule_ttp`

## 4. 추가 규칙

- **모든 테이블 이름은 소문자**로 작성됩니다.
- **단어는 밑줄(`_`)로 구분**되며, 약어는 대문자로 작성하지 않습니다.


## 2.4 데이터베이스 및 테이블 목록
[데이터베이스 설계서 - 데이터 베이스 목록.pdf](https://github.com/user-attachments/files/16557746/-.pdf)
[데이터베이스 설계서 - 테이블 목록.pdf](https://github.com/user-attachments/files/16557748/-.pdf)



## 2.5 임의의 항목 2.5

**내용을 여기에 추가하세요**

---

## 3.1 상세 설명: Python 스크립트

- **기능**: KBO와 MLB의 경기 데이터를 수집하여 JSON 파일로 저장합니다.
- **데이터 수집**:
  - **KBO 데이터**: Statiz에서 크롤링
  - **MLB 데이터**: MLB-StatsAPI 라이브러리를 사용

## 3.2 API 설명 및 사용법

* `http://13.125.238.85:8080/api/v1/schedule/process`
  * POST 요청 시 데이터베이스에서 임시 테이블에 있는 다음날 경기 일정을 가져옵니다.
![예시데이터](https://github.com/user-attachments/assets/16042bb5-2554-43f4-8c92-32374ae09ec8)
  * KBO의 경우 매주 월요일이 휴일이며, 경기가 없을 경우 "경기 없음"을 반환합니다.
  * 팀코드의 경우 아래의 표를 참고해주세요.
  * **현재 우천취소 시 몇 회에 마무리가 되었는지 기록은 없습니다.**

* `http://13.125.238.85:8080/api/v1/results/process`
  * POST 요청 시 데이터베이스에서 임시 테이블에 있는 어제 경기 기록을 가져옵니다.
![results예시](https://ithub.com/user-attachments/assets/800472b1-a8e4-4c0e-80ce-880229c13d59)
  * 팀코드의 경우 아래의 표를 참고해주세요.
  * **현재 MLB의 경우 서부, 중부, 동부로 나누어져 있기 때문에 시간 관계상 기록을 가져오지 못하는 경우가 있습니다.**

* **GPT의 경기 결과 예측은 OpenAI에 충전된 제 자금, 고유 key를 사용하기에 보안의 위험 및 무분별한 사용을 막기 위해 배치 프로그램 내부에 있습니다.**

## 3.3 팀코드

| 팀명               | 팀코드   |
|--------------------|----------|
| 두산 베어스        | 6002     |
| 엘지 트윈스        | 5002     |
| 케이티 위즈        | 12001    |
| 에스에스지 랜더스  | 9002     |
| 엔씨 다이노스      | 11001    |
| 기아 타이거즈      | 2002     |
| 롯데 자이언츠      | 3001     |
| 삼성 라이온즈      | 1001     |
| 한화 이글스        | 7002     |
| 키움 히어로즈      | 10001    |
| 애틀랜타 브레이브스 | 144      |
| 마이애미 말린스    | 146      |
| 뉴욕 메츠          | 121      |
| 필라델피아 필리스  | 143      |
| 워싱턴 내셔널스    | 120      |
| 시카고 컵스        | 112      |
| 신시내티 레즈      | 113      |
| 밀워키 브루어스    | 158      |
| 피츠버그 파이리츠  | 134      |
| 세인트루이스 카디널스 | 138   |
| 애리조나 다이아몬드백스 | 109   |
| 콜로라도 로키스    | 115      |
| 로스앤젤레스 다저스 | 119      |
| 샌디에이고 파드리스 | 135     |
| 샌프란시스코 자이언츠 | 137   |
| 볼티모어 오리올스  | 110      |
| 보스턴 레드삭스    | 111      |
| 뉴욕 양키스        | 147      |
| 탬파베이 레이스    | 139      |
| 토론토 블루제이스  | 141      |
| 시카고 화이트삭스  | 145      |
| 클리블랜드 가디언스 | 114     |
| 디트로이트 타이거스 | 116     |
| 캔자스시티 로열스  | 118      |
| 미네소타 트윈스    | 142      |
| 휴스턴 애스트로스  | 117      |
| 로스앤젤레스 에인절스 | 108   |
| 오클랜드 애슬레틱스 | 133     |
| 시애틀 매리너스    | 136      |
| 텍사스 레인저스    | 140      |
| 현대 유니콘스      | 4004     |

---

## 4.1 느낀점

꽤 많은 시간을 데이터 마이그레이션에 투자하였으나 생각보다 데이터의 일관성을 유지하는 것이 힘들었습니다. 예를 들면 KBO와 MLB의 선수 포지션 차이, MLB 선수의 경우 middle name 때문에 middle name을 last name과 합치는 상황, 경기 고유 코드의 로직 설계를 잘못하여 경기 고유 코드로 검색 시 데이터가 나오지 않는 상황 등이 있었습니다. 조금 더 DB 설계를 정규화하고 레벨을 세심하게 나누었다면 검색과 업데이트가 훨씬 편리했을 것입니다.

또한, 테이블의 컬럼 구성도 아쉬움이 남았습니다. 한 테이블에 많은 데이터가 있다 보니 AWS 무료 서버만으로는 부하를 견디지 못해 DB가 계속 뻗었고, 그로 인해 2만 건 가량의 데이터를 삭제해야 했으나, 학생 레벨에서 힘들게 모은 데이터를 삭제하는 것은 매우 가슴 아팠습니다. 그래서 삭제할 데이터를 다른 테이블(예: tbl_delrslt_ct01)에 코드 형태로 저장해두었거나, tbl_delrslt_nt01 같은 테이블에 모아두었다면 더욱 효율적이었을 것입니다.

RESTful API 부분에 있어서는 처음 설계하는 API였기 때문에 모든 것이 서툴렀습니다. 프로젝트 내부에서 서비스되긴 했지만, 다른 프로젝트에서도 사용할 수 있도록 엔드포인트를 세밀하게 나누는 것이 나았을 것입니다. 예를 들어, 선수 검색, 팀의 로스터 검색, 팀 경기 결과 검색 등으로.

AWS 부분도 처음 서버를 직접 생성하다 보니, 유저 권한, 파일 접근 권한, 로그 작성 권한, 파이썬 가상환경 등 많은 어려움이 있었습니다. 해결한 방법은 버그 리포트 보고서를 확인하시길 바랍니다.

## 4.2 보완점

* ~~Rest API로 변경 예정.~~
* Matchcode 생성 로직 변경 예정. (현재 yyyyMMdd + winteamcode + loseteamcode로 생성되고 있으나 matchcode가 실제 데이터와 꼬이는 상황이 발생. hometeam + awayteam으로 하게 되면 일관성이 생길 것 같음.)
* KBO 우천취소 시 몇 회에서 끝났고, 끝날 당시의 각 팀의 점수를 표시.
* 데이터베이스의 마스터 테이블인 팀 테이블의 MLB팀 세부화.
  * 세부화 후 서, 중, 동부로 엔드포인트를 나누어 경기 기록이 모두 나올 수 있도록 함.
* `/search/results/teamcode` 엔드포인트로 팀코드 요청 시 팀의 경기 기록이 JSON 형태로 나오도록 함.
* `/search/player/teamcode` 엔드포인트로 팀코드 요청 시 팀 로스터가 JSON 형태로 나오도록 함. (포지션을 입력하는 방법도 생각 중)
