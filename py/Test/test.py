import statsapi
import json

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

# 1. 날짜를 설정합니다.
game_date = '2024-03-28'

# 2. 해당 날짜의 첫 번째 경기를 찾습니다.
schedule = statsapi.schedule(start_date=game_date, end_date=game_date)
if not schedule:
    print(f"{game_date}에 경기가 없습니다.")
else:
    # 첫 번째 경기의 ID를 가져옵니다.
    first_game = schedule[0]
    game_id = first_game['game_id']

    # 3. 경기의 상세 정보를 가져옵니다.
    game_info = statsapi.boxscore_data(game_id)

    # 홈팀과 원정팀의 팀 코드를 가져옵니다.
    home_team_code = get_team_code(game_info, 'home')
    away_team_code = get_team_code(game_info, 'away')

    # 배터 기록 수집
    home_batters = game_info['homeBatters']
    away_batters = game_info['awayBatters']
    all_batters = home_batters + away_batters
    batter_stats_list = []

    for batter_data in all_batters:
        if batter_data['personId'] == 0:
            continue

        full_name = get_player_full_name(batter_data['personId'])
        team_code = home_team_code if batter_data in home_batters else away_team_code

        batter_stats = {
            '이름': full_name,
            '팀코드': team_code,
            '포지션': batter_data.get('position', 'N/A'),
        }

        for key, value in batting_key_mapping.items():
            batter_stats[key] = batter_data.get(value, 'N/A')

        batter_stats_list.append(batter_stats)

    # 피처 기록 수집
    home_pitchers = game_info['homePitchers']
    away_pitchers = game_info['awayPitchers']
    all_pitchers = home_pitchers + away_pitchers
    pitcher_stats_list = []

    for pitcher_data in all_pitchers:
        if pitcher_data['personId'] == 0:
            continue

        full_name = get_player_full_name(pitcher_data['personId'])
        team_code = home_team_code if pitcher_data in home_pitchers else away_team_code

        pitcher_stats = {
            '이름': full_name,
            '팀코드': team_code,
            '포지션': pitcher_data.get('position', 'N/A'),
            'ERA': pitcher_data.get('era', 'N/A'),
        }

        for key, value in pitching_key_mapping.items():
            pitcher_stats[key] = pitcher_data.get(value, 'N/A')

        pitcher_stats_list.append(pitcher_stats)

    # 결과를 JSON 형식으로 저장합니다.
    with open('batter_stats_2024_03_28.json', 'w', encoding='utf-8') as f:
        json.dump(batter_stats_list, f, ensure_ascii=False, indent=4)
    print("배터 스탯이 batter_stats_2024_03_28.json 파일에 저장되었습니다.")

    with open('pitcher_stats_2024_03_28.json', 'w', encoding='utf-8') as f:
        json.dump(pitcher_stats_list, f, ensure_ascii=False, indent=4)
    print("피처 스탯이 pitcher_stats_2024_03_28.json 파일에 저장되었습니다.")
