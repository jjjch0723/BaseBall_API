from bs4 import BeautifulSoup
import json
import os
import requests

# 팀 코드 및 이름 매핑
team_codes = {
    "KIA": 2002,
    "두산": 6002,
    "LG": 5002,
    "KT": 12001,
    "SSG": 9002,
    "NC": 11001,
    "롯데": 3001,
    "삼성": 1001,
    "한화": 7002,
    "키움": 10001
}

# 포지션 매핑
position_mapping = {
    "c": "C",
    "fb": "1B",
    "sb": "2B",
    "tb": "3B",
    "ss": "SS",
    "lf": "LF",
    "cf": "CF",
    "rf": "RF",
    "dh": "DH",
    "sp": "P",
    "rp": "P"
}

def extract_depth_players(soup):
    players = {}
    depth_players_sections = soup.select('.item_box .sh_box .box_cont .player_position')
    if len(depth_players_sections) > 1:
        depth_section = depth_players_sections[1]  # 두 번째 player_position 섹션 (뎁스)
        player_divs = depth_section.find_all('div', class_=lambda x: x and 'player_m' in x.split())
        for player_div in player_divs:
            position_class = ' '.join(player_div['class']).split()[0]
            position = position_mapping.get(position_class.lower())
            player_links = player_div.find_all('a')
            for player_link in player_links:
                player_name = player_link.text.strip()
                players[player_name] = position
    return players

# 저장 디렉토리 설정
save_dir = 'C:/DevTool/BaseBall/json'
os.makedirs(save_dir, exist_ok=True)

for team_name, team_code in team_codes.items():
    url = f'https://statiz.sporki.com/team/?m=seasonPosition&t_code={team_code}&year=2024'
    response = requests.get(url)
    response.encoding = 'utf-8'
    soup = BeautifulSoup(response.text, 'html.parser')
    
    team_players = extract_depth_players(soup)
    
    # JSON 파일로 저장
    file_path = os.path.join(save_dir, f'position_{team_name}_KBO.json')
    with open(file_path, 'w', encoding='utf-8') as file:
        json.dump(team_players, file, ensure_ascii=False, indent=4)

print("모든 팀의 position_{team_name}_KBO.json 파일이 성공적으로 저장되었습니다.")
