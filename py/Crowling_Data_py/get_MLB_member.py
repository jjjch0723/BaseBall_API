import statsapi
import json
import os
from concurrent.futures import ThreadPoolExecutor, as_completed

def get_player_info(player_id, team_id, team_name, league_name):
    player_info = statsapi.player_stat_data(player_id)
    return {
        'fullName': player_info['first_name'] + ' ' + player_info['last_name'],
        'firstName': player_info['first_name'],
        'lastName': player_info['last_name'],
        'position': player_info['position'],
        'teamCode': team_id,
        'teamName': team_name,
        'leagueName': league_name,
        'playerId': player_id
    }

def get_team_roster(team_id, team_name, league_name):
    roster = statsapi.get('team_roster', {'teamId': team_id})['roster']
    player_data = []
    with ThreadPoolExecutor() as executor:
        futures = [executor.submit(get_player_info, player['person']['id'], team_id, team_name, league_name) for player in roster]
        for future in as_completed(futures):
            player_data.append(future.result())
    return player_data

def get_all_players():
    teams = statsapi.get('teams', {'sportId': 1})['teams']
    all_players = []

    with ThreadPoolExecutor() as executor:
        futures = [executor.submit(get_team_roster, team['id'], team['name'], team['league']['name']) for team in teams]
        for future in as_completed(futures):
            all_players.extend(future.result())

    return all_players

# 모든 선수 정보를 가져와서 JSON 파일로 저장
all_players = get_all_players()
save_path = 'C:/DevTool/VS-WORKSPACE/json/all_mlb_players.json'
os.makedirs(os.path.dirname(save_path), exist_ok=True)
with open(save_path, 'w', encoding='utf-8') as f:
    json.dump(all_players, f, indent=4, ensure_ascii=False)

print(f"모든 선수 정보를 {save_path} 파일에 저장했습니다.")
