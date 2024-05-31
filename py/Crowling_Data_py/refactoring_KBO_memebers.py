import json
import random

# JSON 파일 경로
file_path = 'C:/DevTool/BaseBall/json/kbo_players.json'

# JSON 파일 읽기
with open(file_path, 'r', encoding='utf-8') as file:
    players = json.load(file)

# 포지션 값 변경 및 조건에 따라 데이터 필터링 및 새로운 구조로 변환
processed_players = []
for player in players:
    if player["position"] == "포지션 없음":
        continue
    if player["position"] == "포수":
        player["position"] = "C"
    elif player["position"] == "투수":
        player["position"] = "P"
    
    first_name = player["playerName"][0]
    last_name = player["playerName"][1:]
    
    # 팀 코드 할당
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
    
    team_code = team_codes.get(player["teamName"], 9999)  # 알 수 없는 팀명은 9999로 할당
    
    processed_player = {
        "fullName": player["playerName"],
        "firstName": first_name,
        "lastName": last_name,
        "position": player["position"],
        "teamCode": team_code,
        "teamName": player["teamName"],
        "playerId": random.randint(10000, 20000)
    }
    
    processed_players.append(processed_player)

# JSON 파일 덮어쓰기
with open(file_path, 'w', encoding='utf-8') as file:
    json.dump(processed_players, file, ensure_ascii=False, indent=4)

print("kbo_players.json 파일이 성공적으로 업데이트되었습니다.")
