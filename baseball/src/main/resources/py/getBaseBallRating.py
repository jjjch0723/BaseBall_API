import json
import openai
import psycopg2
import time
import os

openai.api_key = ''

# team_id_map 정의
team_id_map = {
    "두산 베어스": "6002",
    "엘지 트윈스": "5002",
    "케이티 위즈": "12001",
    "에스에스지 랜더스": "9002",
    "엔씨 다이노스": "11001",
    "기아 타이거즈": "2002",
    "롯데 자이언츠": "3001",
    "삼성 라이온즈": "1001",
    "한화 이글스": "7002",
    "키움 히어로즈": "10001",
    "애틀랜타 브레이브스": "144",
    "마이애미 말린스": "146",
    "뉴욕 메츠": "121",
    "필라델피아 필리스": "143",
    "워싱턴 내셔널스": "120",
    "시카고 컵스": "112",
    "신시내티 레즈": "113",
    "밀워키 브루어스": "158",
    "피츠버그 파이리츠": "134",
    "세인트루이스 카디널스": "138",
    "애리조나 다이아몬드백스": "109",
    "콜로라도 로키스": "115",
    "로스앤젤레스 다저스": "119",
    "샌디에이고 파드리스": "135",
    "샌프란시스코 자이언츠": "137",
    "볼티모어 오리올스": "110",
    "보스턴 레드삭스": "111",
    "뉴욕 양키스": "147",
    "탬파베이 레이스": "139",
    "토론토 블루제이스": "141",
    "시카고 화이트삭스": "145",
    "클리블랜드 가디언스": "114",
    "디트로이트 타이거스": "116",
    "캔자스시티 로열스": "118",
    "미네소타 트윈스": "142",
    "휴스턴 애스트로스": "117",
    "로스앤젤레스 에인절스": "108",
    "오클랜드 애슬레틱스": "133",
    "시애틀 매리너스": "136",
    "텍사스 레인저스": "140"
}

def getKBOData():
    conn = psycopg2.connect(
            host='',
            port='5432',
            user='',
            password='',
            database=''
        )

    cursor = conn.cursor()

    kbosql = '''
    select ttm2.teamname_kr, ttm1.teamname_kr, tkt.game_date
    from tbl_kboschedule_ttp tkt 
    inner join tbl_team_mt01 ttm1 on tkt.team1 = ttm1.team_code 
    inner join tbl_team_mt01 ttm2 on tkt.team2 = ttm2.team_code 
    '''

    cursor.execute(kbosql)

    result = cursor.fetchall()

    games = []
    for game in result:
        games.append({
            'team1_name': game[0],
            'team2_name': game[1],
            'game_date': game[2]
        })

    if not games:
        print("No games found in database query.")
    else:
        print(f"Found {len(games)} games in database query.")
    
    json_data = json.dumps(games, ensure_ascii=False, indent=4)
    return json_data

def getMLBData():
    conn = psycopg2.connect(
            host='',
            port='5432',
            user='',
            password='',
            database=''
        )

    cursor = conn.cursor()

    mlbsql = '''
    select ttm2.teamname_kr, ttm1.teamname_kr, tkt.game_date
    from tbl_mlbschedule_ttp tkt 
    inner join tbl_team_mt01 ttm1 on tkt.team1 = ttm1.team_code 
    inner join tbl_team_mt01 ttm2 on tkt.team2 = ttm2.team_code 
    '''

    cursor.execute(mlbsql)

    result = cursor.fetchall()

    games = []
    for game in result:
        games.append({
            'team1_name': game[0],
            'team2_name': game[1],
            'game_date': game[2]
        })

    if not games:
        print("No games found in database query.")
    else:
        print(f"Found {len(games)} games in database query.")
    
    json_data = json.dumps(games, ensure_ascii=False, indent=4)
    return json_data

def parse_analysis(response, team1_name, team2_name):
    try:
        parsed = {}
        lines = response.split('\n')

        # Define required keys and their corresponding format in the response
        required_keys = {
            "team1_win_rate": [f"{team1_name} 승률"],
            "team2_win_rate": [f"{team2_name} 승률"],
            "team1_score": [f"{team1_name} 점수"],
            "team2_score": [f"{team2_name} 점수"],
            "game_analysis": [f"{team1_name} 과 {team2_name}의 경기분석"]
        }

        for key, korean_list in required_keys.items():
            for line in lines:
                for korean in korean_list:
                    if korean in line:
                        value = line.split(':', 1)[1].strip()
                        parsed[key] = value
                        break

        # Check if all required keys are in the parsed dictionary
        for key in required_keys.keys():
            if key not in parsed:
                if key == "game_analysis":
                    # Set a default value for game_analysis if it's missing
                    parsed[key] = "No detailed analysis provided."
                else:
                    # Set default values for missing scores or win rates
                    if key in ["team1_score", "team2_score"]:
                        parsed[key] = "0점"  # 기본값으로 0점 설정
                    elif key in ["team1_win_rate", "team2_win_rate"]:
                        parsed[key] = "50%"  # 기본 승률 50% 설정

        return parsed
    except Exception as e:
        print(f"Error parsing response: {e}")
        print("Response content:", response)
        return None


def store_analysis_to_file(game_analysis, filename='game_analysis.json'):
    base_dir = os.path.dirname(__file__)
    file_path = os.path.join(base_dir, 'json', 'todaysGames', filename)
    os.makedirs(os.path.dirname(file_path), exist_ok=True)

    # JSON 파일 작성
    with open(file_path, 'w', encoding='utf-8') as f:
        json.dump(game_analysis, f, ensure_ascii=False, indent=4)

    # 파일 소유자 및 권한 설정
    os.system(f'sudo chown tomcat:tomcat {file_path}')
    os.system(f'sudo chmod 777 {file_path}')
    
    print(f"Analysis results stored in {file_path}")

def getAnalysis(getDataFunction):
    games = json.loads(getDataFunction())
    games_analysis = []
    failed_games = []

    for game in games:
        team1_name = game['team1_name']
        team2_name = game['team2_name']
        game_date = game['game_date']

        prompt = (
            f"I will analyze the games based on past matches. "
            f"I don't care about players' conditions, weather, etc. "
            f"I want to get information based on current data and game records. "
            f"Analyze the game between {team1_name} and {team2_name} on {game_date} based on a hypothetical scenario. "
            f"Provide the win rate for each team in percentage and mention the key players of {team1_name} and {team2_name}. "
            f"Think based on current information and game records. "
            f"I just want to hear your opinion, GPT. "
            f"Also, predict the game score. "
            f"This is a hypothetical scenario analysis. "
            f"Please follow the format strictly to avoid data parsing errors.\n"
            f"Provide all information in Korean."
            f"Answer only according to the format below.\n"
            f"Response example: \n"
            f"{team1_name} 승률: 50%\n"
            f"{team2_name} 승률: 50%\n"
            f"{team1_name} 점수: xx점\n"
            f"{team2_name} 점수: xx점\n"
            f"{team1_name} 주요선수: {team1_name} player, {team1_name} player\n"
            f"{team2_name} 주요선수: {team2_name} player, {team2_name} player\n"
            f"{team1_name} 과 {team2_name}의 경기분석: analysis content\n"
        )

        success = False
        while not success:
            try:
                response = openai.ChatCompletion.create(
                    model="gpt-4o-mini",
                    messages=[
                        {"role": "system", "content": "You are an assistant that analyzes baseball games by searching the internet for various information based on the given parameters."},
                        {"role": "user", "content": prompt}
                    ],
                    max_tokens=600
                )

                analysis_text = response.choices[0]['message']['content'].strip()
                parsed_analysis = parse_analysis(analysis_text, team1_name, team2_name)
                if parsed_analysis and parsed_analysis["game_analysis"] != "No detailed analysis provided.":
                    parsed_analysis['team1_name'] = team_id_map.get(team1_name, team1_name)
                    parsed_analysis['team2_name'] = team_id_map.get(team2_name, team2_name)
                    
                    game_analysis = {
                        'DATE': game_date,
                        'TEAM1': parsed_analysis['team1_name'],
                        'TEAM2': parsed_analysis['team2_name'],
                        'TEAM1_WINRATE': parsed_analysis['team1_win_rate'],
                        'TEAM2_WINRATE': parsed_analysis['team2_win_rate'],
                        'TEAM1_SCORE': parsed_analysis['team1_score'],
                        'TEAM2_SCORE': parsed_analysis['team2_score'],
                        'GAME_ANALYSIS': parsed_analysis['game_analysis']
                    }
                    games_analysis.append(game_analysis)
                    success = True
                else:
                    print(f"Error parsing response or detailed analysis missing for game {team1_name} vs {team2_name} on {game_date}")
                    failed_games.append(game)
            except Exception as e:
                print(f"Error occurred for game {team1_name} vs {team2_name} on {game_date}: {e}")
                time.sleep(1)  # 잠시 대기 후 다시 시도

    if not games_analysis:
        print("No analysis results were generated.")
    else:
        print(f"Generated analysis for {len(games_analysis)} games.")
        
    return games_analysis, failed_games

def remove_duplicates_and_empty_analysis(games_analysis):
    unique_games = {}
    for game in games_analysis:
        key = (game['TEAM1'], game['TEAM2'], game['DATE'])
        if key not in unique_games or game['GAME_ANALYSIS']:
            unique_games[key] = game
    
    # Only include games with non-empty GAME_ANALYSIS
    filtered_games = [game for game in unique_games.values() if game['GAME_ANALYSIS']]

    return filtered_games

# 분석 결과 출력
if __name__ == '__main__':
    all_analysis_results = []
    all_failed_games = []

    print("KBO Analysis Results:")
    kbo_analysis_results, kbo_failed_games = getAnalysis(getKBOData)
    all_analysis_results.extend(kbo_analysis_results)
    all_failed_games.extend(kbo_failed_games)
    
    print("\nMLB Analysis Results:")
    mlb_analysis_results, mlb_failed_games = getAnalysis(getMLBData)
    all_analysis_results.extend(mlb_analysis_results)
    all_failed_games.extend(mlb_failed_games)

    while all_failed_games:
        print(f"\nRetrying analysis for {len(all_failed_games)} failed games...")
        retry_analysis_results, retry_failed_games = getAnalysis(lambda: json.dumps(all_failed_games, ensure_ascii=False, indent=4))
        all_analysis_results.extend(retry_analysis_results)
        all_failed_games = retry_failed_games

    if all_analysis_results:
        print(json.dumps(all_analysis_results, ensure_ascii=False, indent=4))
        # 중복 제거 및 GAME_ANALYSIS 값이 빈 값이 아닌 항목만 남기기
        all_analysis_results = remove_duplicates_and_empty_analysis(all_analysis_results)
        # JSON 파일로 저장
        store_analysis_to_file(all_analysis_results, filename='game_analysis.json')
    else:
        print("No analysis results found.")
