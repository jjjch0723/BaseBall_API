import statsapi
import json
import os
from datetime import datetime, timedelta

# 저장 디렉토리 설정
save_dir = 'C:/DevTool/BaseBall/BaseBall_data_Crowling/json'
os.makedirs(save_dir, exist_ok=True)

def get_schedule(start_date, end_date):
    schedule = []
    current_date = start_date

    while current_date <= end_date:
        games = statsapi.schedule(start_date=current_date.strftime('%Y-%m-%d'), end_date=current_date.strftime('%Y-%m-%d'))
        for game in games:
            game_info = {
                "date": game['game_date'],
                "away_team": game['away_name'],
                "home_team": game['home_name'],
                "venue": game['venue_name'],
                "status": game['status'],
                "away_score": game.get('away_score', 'N/A'),
                "home_score": game.get('home_score', 'N/A')
            }
            schedule.append(game_info)
        current_date += timedelta(days=1)

    return schedule

# Function to process data for a given year
def process_data(year):
    start_date = datetime(year, 3, 1)
    end_date = datetime(year, 11, 30)
    year_schedule = get_schedule(start_date, end_date)

    # Save the data to a JSON file
    file_path = os.path.join(save_dir, f"game_result_MLB_{year}.json")
    with open(file_path, "w", encoding="utf-8") as f:
        json.dump(year_schedule, f, ensure_ascii=False, indent=4)

    print(f"{year}년의 경기 결과가 {file_path} 파일에 성공적으로 저장되었습니다.")

# Get the data for each year from 2000 to 2023
start_year = 2000
end_year = 2023

for year in range(start_year, end_year + 1):
    process_data(year)