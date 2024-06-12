import statsapi
import json
import os
from datetime import datetime

def get_team_code(game_info, team_type):
    return game_info[team_type]['team']['id']

def get_player_full_name(person_id):
    player_info = statsapi.get('person', {'personId': person_id})
    return player_info['people'][0].get('fullName', 'N/A')

def fetch_game_data(game_date):
    schedule = statsapi.schedule(start_date=game_date, end_date=game_date)
    if not schedule:
        print(f"No games on {game_date}.")
        return [], []
    
    batter_stats_list = []
    pitcher_stats_list = []

    game = schedule[0]  # Get the first game of the day
    game_id = game['game_id']

    print(f"Fetching boxscore data for game ID: {game_id}")
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

        batter_stats.update(batter_data)  # Include all original data for debugging

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

        pitcher_stats.update(pitcher_data)  # Include all original data for debugging

        pitcher_stats_list.append(pitcher_stats)

    print(f"Data collection for {game_date} complete.")
    return batter_stats_list, pitcher_stats_list

# 2024년 3월 28일의 첫 경기 데이터를 가져옵니다.
test_date = '2024-03-28'
batter_stats, pitcher_stats = fetch_game_data(test_date)

# 결과를 JSON 형식으로 저장합니다.
save_dir = 'C:/DevTool/BaseBall/BaseBall_data_Crowling/json/2024_MLB_team_status'
os.makedirs(save_dir, exist_ok=True)

with open(os.path.join(save_dir, 'batter_stats_test.json'), 'w', encoding='utf-8') as f:
    json.dump(batter_stats, f, ensure_ascii=False, indent=4)
print("Batter stats have been saved.")

with open(os.path.join(save_dir, 'pitcher_stats_test.json'), 'w', encoding='utf-8') as f:
    json.dump(pitcher_stats, f, ensure_ascii=False, indent=4)
print("Pitcher stats have been saved.")
