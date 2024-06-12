import json
import statsapi
from datetime import datetime, timedelta

# 한국 시간 기준으로 현재 날짜 가져오기
now_kst = datetime.now() + timedelta(hours=14)
today_kst = now_kst.strftime('%Y-%m-%d')

# 오늘 날짜의 경기 일정 가져오기
games = statsapi.schedule(start_date=today_kst, end_date=today_kst)

# 경기 일정 JSON 형식으로 변환
games_json = []
if games:
    for game in games:
        games_json.append({
            "away_team_id": game['away_id'],
            "home_team_id": game['home_id'],
            "game_date": game['game_date']
        })
else:
    games_json = {"message": "오늘은 경기가 없습니다."}

# 파일 경로 및 이름 설정
file_path = f"C:/DevTool/BaseBall/BaseBall_data_Crowling/json/todaysGames/{now_kst.strftime('%Y%m%d')}MLBgames.json"

# JSON 데이터를 파일로 저장
with open(file_path, 'w', encoding='utf-8') as f:
    json.dump(games_json, f, ensure_ascii=False, indent=4)

print(f"경기 일정이 '{file_path}' 파일에 저장되었습니다.")
