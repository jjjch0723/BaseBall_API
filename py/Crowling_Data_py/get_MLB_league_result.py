import statsapi
import json
import os
from concurrent.futures import ThreadPoolExecutor, as_completed
from datetime import datetime, timedelta

def get_team_code(game_info, team_type):
    return game_info[team_type]['team']['id']

def get_player_full_name(person_id):
    player_info = statsapi.get('person', {'personId': person_id})
    return player_info['people'][0].get('fullName', 'N/A')

pitching_key_mapping = {
    "gamesStarted": "gs",
    "completeGames": "cg",
    "shutouts": "sho",
    "saves": "s",
    "hits": "h",
    "inningsPitched": "ip",
    "earnedRuns": "er",
    "runs": "r",
    "homeRuns": "hr",
    "baseOnBalls": "bb",
    "hitBatsmen": "hp",
    "strikeOuts": "so"
}

batting_key_mapping = {
    "atBats": "ab",
    "runs": "r",
    "hits": "h",
    "doubles": "2b",
    "triples": "3b",
    "homeRuns": "hr",
    "rbi": "rbi",
    "stolenBases": "sb",
    "caughtStealing": "cs",
    "baseOnBalls": "bb",
    "strikeOuts": "so",
    "avg": "avg",
    "obp": "obp",
    "slg": "slg"
}

def fetch_game_data(game_date):
    schedule = statsapi.schedule(start_date=game_date, end_date=game_date)
    if not schedule:
        print(f"{game_date}에 경기가 없습니다.")
        return [], []
    
    batter_stats_list = []
    pitcher_stats_list = []

    for game in schedule:
        game_id = game['game_id']

        game_info = statsapi.boxscore_data(game_id)

        home_team_code = get_team_code(game_info, 'home')
        away_team_code = get_team_code(game_info, 'away')

        home_batters = game_info['homeBatters']
        away_batters = game_info['awayBatters']
        all_batters = home_batters + away_batters

        for batter_data in all_batters:
            if batter_data['personId'] == 0:
                continue

            full_name = get_player_full_name(batter_data['personId'])
            team_code = home_team_code if batter_data in home_batters else away_team_code

            batter_stats = {
                'fullName': full_name,
                'teamCode': team_code,
                'awayTeam': away_team_code if team_code == home_team_code else home_team_code,
                'position': batter_data.get('position', 'N/A'),
                'date': game_date
            }

            for key, value in batting_key_mapping.items():
                batter_stats[value] = batter_data.get(key, 'N/A')
                if batter_stats[value] == 'N/A':
                    batter_stats[value] = '0'

            batter_stats_list.append(batter_stats)

        home_pitchers = game_info['homePitchers']
        away_pitchers = game_info['awayPitchers']
        all_pitchers = home_pitchers + away_pitchers

        for pitcher_data in all_pitchers:
            if pitcher_data['personId'] == 0:
                continue

            full_name = get_player_full_name(pitcher_data['personId'])
            team_code = home_team_code if pitcher_data in home_pitchers else away_team_code

            pitcher_stats = {
                'fullName': full_name,
                'teamCode': team_code,
                'awayTeam': away_team_code if team_code == home_team_code else home_team_code,
                'position': 'P',
                'ERA': pitcher_data.get('era', 'N/A'),
                'date': game_date
            }

            for key, value in pitching_key_mapping.items():
                pitcher_stats[value] = pitcher_data.get(key, 'N/A')
                if pitcher_stats[value] == 'N/A':
                    pitcher_stats[value] = '0'

            pitcher_stats_list.append(pitcher_stats)

    print(f"{game_date} 데이터 수집 완료.")
    return batter_stats_list, pitcher_stats_list

# 2024년 3월 28일부터 6월 10일까지의 날짜 범위를 생성합니다.
start_date = datetime.strptime('2024-03-28', '%Y-%m-%d')
end_date = datetime.strptime('2024-06-10', '%Y-%m-%d')
date_range = [(start_date + timedelta(days=x)).strftime('%Y-%m-%d') for x in range((end_date - start_date).days + 1)]

# 병렬 처리로 데이터를 가져옵니다.
all_batter_stats = []
all_pitcher_stats = []

with ThreadPoolExecutor() as executor:
    futures = {executor.submit(fetch_game_data, game_date): game_date for game_date in date_range}
    for future in as_completed(futures):
        game_date = futures[future]
        try:
            batter_stats, pitcher_stats = future.result()
            all_batter_stats.extend(batter_stats)
            all_pitcher_stats.extend(pitcher_stats)
        except Exception as exc:
            print(f"{game_date} 데이터 수집 중 오류 발생: {exc}")

# 결과를 JSON 형식으로 저장합니다.
save_dir = 'C:/DevTool/BaseBall/BaseBall_data_Crowling/json/2024_MLB_team_status'
os.makedirs(save_dir, exist_ok=True)

with open(os.path.join(save_dir, 'batter_stats_2024_03_28_to_2024_06_10.json'), 'w', encoding='utf-8') as f:
    json.dump(all_batter_stats, f, ensure_ascii=False, indent=4)
print("배터 스탯이 저장되었습니다.")

with open(os.path.join(save_dir, 'pitcher_stats_2024_03_28_to_2024_06_10.json'), 'w', encoding='utf-8') as f:
    json.dump(all_pitcher_stats, f, ensure_ascii=False, indent=4)
print("피처 스탯이 저장되었습니다.")
