from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.service import Service as ChromeService
from webdriver_manager.chrome import ChromeDriverManager
import json
import os
import time

# 크롬 드라이버 설정
options = webdriver.ChromeOptions()
options.add_argument('--headless')  # 헤드리스 모드 설정 (옵션)
options.add_argument('--no-sandbox')
options.add_argument('--disable-dev-shm-usage')

# 웹 드라이버 실행
driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()), options=options)

# KBO 선수 등록 페이지 URL
url = "https://www.koreabaseball.com/Player/Register.aspx"
driver.get(url)

# 페이지가 로드될 때까지 기다림
WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.CSS_SELECTOR, '.teams ul li')))

# 팀별 선수 정보를 저장할 리스트
all_players = []

# 각 팀의 data-id와 이름 매핑
team_data_ids = {
    "KIA": "HT",
    "두산": "OB",
    "LG": "LG",
    "삼성": "SS",
    "NC": "NC",
    "SSG": "SK",
    "KT": "KT",
    "한화": "HH",
    "롯데": "LT",
    "키움": "WO"
}

# 각 팀의 data-id를 사용하여 버튼을 클릭하고 선수 정보를 수집
for team_name, team_id in team_data_ids.items():
    try:
        # 각 팀의 버튼을 찾음
        team_button = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.CSS_SELECTOR, f'.teams ul li[data-id="{team_id}"] a'))
        )
        team_button.click()
        
        # 팀 이름이 제대로 로드될 때까지 기다림
        WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, '.teams ul li.on span'))
        )
        
        time.sleep(2)  # 페이지 로드 시간을 기다림

        # 선수 테이블을 찾음
        tables = driver.find_elements(By.CSS_SELECTOR, 'table.tNData')
        
        for table in tables:
            # 테이블 헤더에서 포지션 컬럼의 인덱스를 찾음
            headers = table.find_elements(By.TAG_NAME, 'th')
            position_index = None
            for i, header in enumerate(headers):
                if header.text in ["포수", "투수", "내야수", "외야수"]:
                    position_index = i
                    break

            # 테이블의 각 행을 순회
            rows = table.find_elements(By.TAG_NAME, 'tr')
            for row in rows[1:]:  # 첫 번째 행은 헤더이므로 제외
                cols = row.find_elements(By.TAG_NAME, 'td')
                if len(cols) > 1 and cols[1].text not in ["감독", "코치"]:  # 감독, 코치 등 제외
                    player_name = cols[1].text.strip()
                    position = headers[position_index].text if position_index is not None else "포지션 없음"
                    player_data = {
                        'teamName': team_name,
                        'playerName': player_name,
                        'position': position
                    }
                    all_players.append(player_data)
    except Exception as e:
        print(f"Error processing team {team_name}: {e}")

# 웹 드라이버 종료
driver.quit()

# JSON 파일로 저장
save_path = 'C:/DevTool/BASEBALL/json/kbo_players.json'
os.makedirs(os.path.dirname(save_path), exist_ok=True)
with open(save_path, 'w', encoding='utf-8') as f:
    json.dump(all_players, f, indent=4, ensure_ascii=False)

print(f"모든 선수 정보를 {save_path} 파일에 저장했습니다.")
