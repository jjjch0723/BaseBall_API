import json
import openai
import psycopg2
import time
import os
import concurrent.futures
from psycopg2 import pool
from threading import Lock
import openai
from openai.error import OpenAIError
import traceback
import sys

openai.api_key = ''

# Database connection pool
db_pool = pool.SimpleConnectionPool(
    1, 20,  # Minimum 1, maximum 20 connections
    host='',
    port='5432',
    user='',
    password='',
    database=''
)

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

# Analysis count and lock for thread-safe increment
analysis_count = 0
analysis_count_lock = Lock()

def getKBOData():
    try:
        conn = db_pool.getconn()
        cursor = conn.cursor()
        kbosql = '''
        select ttm2.teamname_kr, ttm1.teamname_kr, tkt.game_date
        from tbl_kboschedule_ttp tkt 
        inner join tbl_team_mt01 ttm1 on tkt.team1 = ttm1.team_code 
        inner join tbl_team_mt01 ttm2 on tkt.team2 = ttm2.team_code 
        '''
        cursor.execute(kbosql)
        result = cursor.fetchall()
        
        # 읽어온 데이터 출력
        for row in result:
            print(f"KBO Game: Team1={row[0]}, Team2={row[1]}, Game Date={row[2]}")
        
        return json.dumps([{'team1_name': row[0], 'team2_name': row[1], 'game_date': row[2]} for row in result], ensure_ascii=False, indent=4)
    except Exception as e:
        print(f"Error in getKBOData: {e}")
        traceback.print_exc()
        sys.exit(1)
    finally:
        db_pool.putconn(conn)

def getMLBData():
    try:
        conn = db_pool.getconn()
        cursor = conn.cursor()
        mlbsql = '''
        select ttm2.teamname_kr, ttm1.teamname_kr, tkt.game_date
        from tbl_mlbschedule_ttp tkt 
        inner join tbl_team_mt01 ttm1 on tkt.team1 = ttm1.team_code 
        inner join tbl_team_mt01 ttm2 on tkt.team2 = ttm2.team_code 
        '''
        cursor.execute(mlbsql)
        result = cursor.fetchall()
        
        # 읽어온 데이터 출력
        for row in result:
            print(f"MLB Game: Team1={row[0]}, Team2={row[1]}, Game Date={row[2]}")
        
        return json.dumps([{'team1_name': row[0], 'team2_name': row[1], 'game_date': row[2]} for row in result], ensure_ascii=False, indent=4)
    except Exception as e:
        print(f"Error in getMLBData: {e}")
        traceback.print_exc()
        sys.exit(1)
    finally:
        db_pool.putconn(conn)

def parse_analysis(response, team1_name, team2_name):
    try:
        parsed = {}
        lines = response.split('\n')

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

        for key in required_keys.keys():
            if key not in parsed:
                if key == "game_analysis":
                    parsed[key] = "No detailed analysis provided."
                elif key in ["team1_score", "team2_score"]:
                    parsed[key] = "0점"
                elif key in ["team1_win_rate", "team2_win_rate"]:
                    parsed[key] = "50%"

        return parsed
    except Exception as e:
        print(f"Error parsing response: {e}")
        traceback.print_exc()
        sys.exit(1)

def store_analysis_to_file(game_analysis, filename='game_analysis.json'):
    try:
        base_dir = os.path.dirname(__file__)
        file_path = os.path.join(base_dir, 'json', 'todaysGames', filename)
        os.makedirs(os.path.dirname(file_path), exist_ok=True)

        with open(file_path, 'w', encoding='utf-8') as f:
            json.dump(game_analysis, f, ensure_ascii=False, indent=4)

        os.system(f'sudo chown baseball:baseball {file_path}')
        os.system(f'sudo chmod 777 {file_path}')
        
        print(f"Analysis results stored in {file_path}")
    except Exception as e:
        print(f"Error storing analysis to file: {e}")
        traceback.print_exc()
        sys.exit(1)

def analyze_game(game):
    global analysis_count
    team1_name = game.get('team1_name')
    team2_name = game.get('team2_name')
    game_date = game.get('game_date')

    # 데이터 검증 출력
    if not team1_name or not team2_name or not game_date:
        print(f"유효하지 않은 게임 데이터: Team1={team1_name}, Team2={team2_name}, Game Date={game_date}")
        return None

    try:
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

        response = openai.ChatCompletion.create(
            model="gpt-4o-mini",
            messages=[
                {"role": "system", "content": "You are an assistant that analyzes baseball games by searching the internet for various information based on the given parameters."},
                {"role": "user", "content": prompt}
            ],
            max_tokens=400
        )

        # 응답 내용 출력
        analysis_text = response.choices[0]['message']['content'].strip()
        print(f"OpenAI API 응답 내용:\n{analysis_text}")

        parsed_analysis = parse_analysis(analysis_text, team1_name, team2_name)

        if parsed_analysis and parsed_analysis["game_analysis"] != "No detailed analysis provided.":
            parsed_analysis['team1_name'] = team_id_map.get(team1_name, team1_name)
            parsed_analysis['team2_name'] = team_id_map.get(team2_name, team2_name)

            with analysis_count_lock:
                analysis_count += 1
                print(f"Total number of analyses completed so far: {analysis_count}")

            return {
                'DATE': game_date,
                'TEAM1': parsed_analysis['team1_name'],
                'TEAM2': parsed_analysis['team2_name'],
                'TEAM1_WINRATE': parsed_analysis['team1_win_rate'],
                'TEAM2_WINRATE': parsed_analysis['team2_win_rate'],
                'TEAM1_SCORE': parsed_analysis['team1_score'],
                'TEAM2_SCORE': parsed_analysis['team2_score'],
                'GAME_ANALYSIS': parsed_analysis['game_analysis']
            }
    except openai.error.OpenAIError as e:
        # OpenAI API 호출 실패 시 오류 메시지 출력
        print(f"OpenAI API Error for game {team1_name} vs {team2_name} on {game_date}: {e}")
        traceback.print_exc()
        sys.exit(1)
    except Exception as e:
        # 다른 모든 예외 처리
        print(f"Unexpected error for game {team1_name} vs {team2_name} on {game_date}: {e}")
        traceback.print_exc()
        sys.exit(1)

    return None

def getAnalysisParallel(getDataFunction):
    try:
        games = json.loads(getDataFunction())
        games_analysis = []

        with concurrent.futures.ThreadPoolExecutor(max_workers=5) as executor:
            future_to_game = {executor.submit(analyze_game, game): game for game in games}

            for future in concurrent.futures.as_completed(future_to_game):
                try:
                    game_analysis = future.result()
                    if game_analysis:
                        games_analysis.append(game_analysis)
                except Exception as e:
                    game = future_to_game[future]
                    error_message = f"Error occurred for game {game['team1_name']} vs {game['team2_name']} on {game['game_date']}: {e}"
                    print(error_message)
                    traceback.print_exc()
                    sys.exit(1)

        return games_analysis
    except Exception as e:
        print(f"Error in getAnalysisParallel: {e}")
        traceback.print_exc()
        sys.exit(1)

def remove_duplicates_and_empty_analysis(games_analysis):
    try:
        unique_games = {}
        for game in games_analysis:
            key = (game['TEAM1'], game['TEAM2'], game['DATE'])
            if key not in unique_games or game['GAME_ANALYSIS']:
                unique_games[key] = game
        
        filtered_games = [game for game in unique_games.values() if game['GAME_ANALYSIS']]
        return filtered_games
    except Exception as e:
        print(f"Error in remove_duplicates_and_empty_analysis: {e}")
        traceback.print_exc()
        sys.exit(1)

if __name__ == '__main__':
    try:
        all_analysis_results = []

        print("KBO Analysis Results:")
        kbo_analysis_results = getAnalysisParallel(getKBOData)
        all_analysis_results.extend(kbo_analysis_results)
        
        print("\nMLB Analysis Results:")
        mlb_analysis_results = getAnalysisParallel(getMLBData)
        all_analysis_results.extend(mlb_analysis_results)

        if all_analysis_results:
            all_analysis_results = remove_duplicates_and_empty_analysis(all_analysis_results)
            store_analysis_to_file(all_analysis_results, filename='game_analysis.json')
            
            # Print the analysis results
            print("\nFinal Analysis Results:")
            for analysis in all_analysis_results:
                print(json.dumps(analysis, ensure_ascii=False, indent=4))
        else:
            print("No analysis results found.")
    except Exception as e:
        print(f"Unexpected error in main: {e}")
        traceback.print_exc()
        sys.exit(1)
