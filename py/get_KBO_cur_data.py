import requests
from bs4 import BeautifulSoup
import json
import os
import time
from concurrent.futures import ThreadPoolExecutor, as_completed

# 저장 디렉토리 설정
save_dir = 'C:/DevTool/BaseBall/BaseBall_data_Crowling/json'
os.makedirs(save_dir, exist_ok=True)

# Base URL
base_url = "https://statiz.sporki.com/schedule/?year={year}&month={month}"

# Years and months to iterate over
years = range(2000, 2024)
months = range(4, 12)

# Headers for the request
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
}

# Function to parse a single page and extract game results
def parse_page(year, month):
    data = []
    url = base_url.format(year=year, month=month)
    print(f"Fetching data from URL: {url}")
    response = requests.get(url, headers=headers)
    soup = BeautifulSoup(response.text, 'html.parser')

    # Find all day rows
    days = soup.find_all('td')
    
    for day in days:
        date_tag = day.find('span', class_='day')
        if not date_tag:
            continue
        
        date = f"{year}{month:02d}{int(date_tag.text):02d}"
        
        games = day.find_all('div', class_='games')
        for game in games:
            game_items = game.find_all('li')
            for item in game_items:
                spans = item.find_all('span')
                if len(spans) < 4:
                    continue

                weather = "비 없음"
                win_team = ""
                lose_team = ""
                win_score = ""
                lose_score = ""
                team1 = ""
                team2 = ""

                for span in spans:
                    if 'team' in span['class']:
                        if not team1:
                            team1 = span.text.strip()
                        else:
                            team2 = span.text.strip()
                    if 'lead' in span['class']:
                        win_score = span.text.strip()
                    elif 'score' in span['class']:
                        lose_score = span.text.strip()
                    if 'color:#FFFFFF' in span.get('style', ''):
                        win_team = span.text.strip()
                    elif 'background-color:#EEEEEE' in span.get('style', ''):
                        lose_team = span.text.strip()
                    if 'weather' in span['class']:
                        weather = "우천취소"
                        win_team = ""
                        lose_team = ""
                        win_score = ""
                        lose_score = ""
                        print(date)

                data.append({
                    "day": date,
                    "team1": team1,
                    "team2": team2,
                    "weather": weather,
                    "winTeam": win_team,
                    "loseTeam": lose_team,
                    "winScore": win_score,
                    "loseScore": lose_score
                })
    
    return data

# Function to process data for a given year and month
def process_data(year, month):
    return parse_page(year, month)

# Using ThreadPoolExecutor for parallel processing
all_data = []
with ThreadPoolExecutor(max_workers=10) as executor:
    futures = [executor.submit(process_data, year, month) for year in years for month in months]
    for future in as_completed(futures):
        result = future.result()
        if result:
            all_data.extend(result)
        time.sleep(0.1)  # Slight delay to avoid overloading the server

# Save the data to a JSON file
file_path = os.path.join(save_dir, "game_results_2000_to_2023.json")
with open(file_path, "w", encoding="utf-8") as f:
    json.dump(all_data, f, ensure_ascii=False, indent=4)

print(f"모든 경기 결과가 {file_path} 파일에 성공적으로 저장되었습니다.")
