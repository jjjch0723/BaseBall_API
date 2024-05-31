import json
import os

# JSON 파일 경로 설정
json_dir = 'C:/DevTool/BaseBall/json'
all_players_file_path = os.path.join(json_dir, 'all_kbo_players.json')

# all_kbo_players.json 파일 읽기
with open(all_players_file_path, 'r', encoding='utf-8') as file:
    all_players = json.load(file)

# 팀별 position 파일 처리
for file_name in os.listdir(json_dir):
    if file_name.startswith('position_') and file_name.endswith('_KBO.json'):
        team_name = file_name.split('_')[1]
        position_file_path = os.path.join(json_dir, file_name)
        
        # position_팀명_KBO.json 파일 읽기
        with open(position_file_path, 'r', encoding='utf-8') as file:
            team_positions = json.load(file)
        
        for player in all_players:
            if player["teamName"] == team_name:
                first_name = player["firstName"]
                last_name = player["lastName"]
                full_name = player["fullName"]
                
                # 선수 이름 비교
                player_key = f"{first_name}{last_name}"
                if player_key in team_positions:
                    player["position"] = team_positions[player_key]
                elif full_name in team_positions:
                    player["position"] = team_positions[full_name]

# 갱신된 all_kbo_players.json 파일 저장
with open(all_players_file_path, 'w', encoding='utf-8') as file:
    json.dump(all_players, file, ensure_ascii=False, indent=4)

print("all_kbo_players.json 파일이 성공적으로 업데이트되었습니다.")
