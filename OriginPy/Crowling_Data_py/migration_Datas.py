import json

# 매핑된 팀 코드
team_codes = {
    "Oakland Athletics": "133",
    "Pittsburgh Pirates": "134",
    "San Diego Padres": "135",
    "Seattle Mariners": "136",
    "San Francisco Giants": "137",
    "St. Louis Cardinals": "138",
    "Tampa Bay Rays": "139",
    "Texas Rangers": "140",
    "Toronto Blue Jays": "141",
    "Minnesota Twins": "142",
    "Philadelphia Phillies": "143",
    "Atlanta Braves": "144",
    "Chicago White Sox": "145",
    "Miami Marlins": "146",
    "New York Yankees": "147",
    "Milwaukee Brewers": "158",
    "Los Angeles Angels": "108",
    "Anaheim Angels": "108",  # Anaheim Angels mapped to Los Angeles Angels
    "Arizona Diamondbacks": "109",
    "Baltimore Orioles": "110",
    "Boston Red Sox": "111",
    "Chicago Cubs": "112",
    "Cincinnati Reds": "113",
    "Cleveland Indians": "114",
    "Cleveland Guardians": "114",
    "Colorado Rockies": "115",
    "Detroit Tigers": "116",
    "Houston Astros": "117",
    "Kansas City Royals": "118",
    "Los Angeles Dodgers": "119",
    "Washington Nationals": "120",
    "Montreal Expos": "120",  # Montreal Expos mapped to Washington Nationals
    "New York Mets": "121",
    "Florida Marlins": "146",  # Florida Marlins mapped to Miami Marlins
    "Tampa Bay Devil Rays": "139"  # Tampa Bay Devil Rays mapped to Tampa Bay Rays
}

kbo_team_codes = {
    "KIA": "2002",
    "두산": "6002",
    "LG": "5002",
    "KT": "12001",
    "SSG": "9002",
    "NC": "11001",
    "롯데": "3001",
    "삼성": "1001",
    "한화": "7002",
    "키움": "10001",
    "SK": "9002", 
    "해태": "2002", 
    "현대": "10001" 
}
# JSON 파일 경로
file_path = "C:/DevTool/BaseBall/BaseBall_data_Crowling/json/game_result_00~23/game_result_MLB_2000_to_2023.json"
kbo_file_path = "C:/DevTool/BaseBall/BaseBall_data_Crowling/json/game_result_00~23/game_result_KBO_2000_to_2023.json"
# JSON 파일 읽기
with open(file_path, 'r', encoding='utf-8') as file:
    game_results = json.load(file)

with open(kbo_file_path, 'r', encoding='utf-8') as file: 
    kbo_game_results = json.load(file)

# 팀 이름을 매핑된 팀 코드로 변경
for game in game_results:
    win_team = game["winTeam"]
    lose_team = game["loseTeam"]
    if win_team in team_codes:
        game["winTeam"] = team_codes[win_team]
    if lose_team in team_codes:
        game["loseTeam"] = team_codes[lose_team]

for game in kbo_game_results:
    win_team = game["winTeam"]
    lose_team = game["loseTeam"]
    if win_team in kbo_team_codes:
        game["winTeam"] = kbo_team_codes[win_team]
    if lose_team in team_codes:
        game["loseTeam"] = kbo_team_codes[lose_team]

# 변경된 내용을 새로운 JSON 파일로 저장
output_file_path = "C:/DevTool/BaseBall/BaseBall_data_Crowling/json/game_result_00~23/game_result_MLB_2000_to_2023_teamCode.json"
output_KBO_file_path = "C:/DevTool/BaseBall/BaseBall_data_Crowling/json/game_result_00~23/game_result_KBO_2000_to_2023_teamCode.json"

with open(output_file_path, 'w', encoding='utf-8') as file:
    json.dump(game_results, file, ensure_ascii=False, indent=4)

with open(output_KBO_file_path, 'w', encoding='utf-8') as file:
    json.dump(kbo_game_results, file, ensure_ascii=False, indent=4)

print("팀 이름을 팀 코드로 변경 완료 및 새로운 JSON 파일 저장 완료.")
