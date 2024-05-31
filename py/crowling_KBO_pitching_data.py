import json
import requests
from bs4 import BeautifulSoup

# 기본 URL 및 JSON 파일 저장 경로 정의
base_url = "https://statiz.sporki.com/stats/?m=team&m2=pitching&m3=default&so=WAR&ob=DESC&year={year}&sy=&ey=&te=&po=&lt=10100&reg=&pe=&ds=&de=&we=&hr=&ha=&ct=&st=&vp=&bo=&pt=&pp=&ii=&vc=&um=&oo=&rr=&sc=&bc=&ba=&li=&as=&ae=&pl=&gc=&lr=&pr=50&ph=&hs=&us=&na=&ls=&sf1=&sk1=&sv1=&sf2=&sk2=&sv2="
output_file = "C:\\DevTool\\BaseBall\\BaseBall_data_Crowling\\json\\yearTeamStatus\\KBO_pitching_data_2000_to_2023.json"

# 데이터를 가져올 연도 정의
years = range(2000, 2024)

# 필요한 키 매핑
key_mapping = {
    "GS": "gs",
    "CG": "cg",
    "SHO": "sho",
    "S": "s",
    "IP": "ip",
    "ER": "er",
    "R": "r",
    "HR": "hr",
    "BB": "bb",
    "HP": "hp",
    "SO": "so"
}

# 모든 데이터를 저장할 리스트 초기화
all_data = []

# 주어진 연도의 데이터를 가져와서 파싱하는 함수 정의
def fetch_data_for_year(year):
    url = base_url.format(year=year)
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')
    
    # "팀 기록" 테이블 선택
    tables = soup.find_all("table")
    team_table = tables[1]  # 두 번째 테이블이 팀 기록 테이블
    
    # 헤더에서 인덱스 매핑 추출
    headers = team_table.find_all("th")
    indices = {header.get("so"): idx for idx, header in enumerate(headers) if header.get("so") in key_mapping}
    team_name_index = [idx for idx, header in enumerate(headers) if header.get_text(strip=True) == "Team"][0]

    # 테이블 행 추출
    rows = team_table.find_all("tr")[1:]  # 헤더 행은 제외

    year_data = []
    
    for row in rows:
        cols = row.find_all("td")
        if len(cols) > 1:
            team_data = {"year": str(year)}
            team_data["teamName"] = cols[team_name_index].text.strip()
            for key, idx in indices.items():
                team_data[key_mapping[key]] = cols[idx].text.strip()
            year_data.append(team_data)
    return year_data

# 연도를 순회하며 데이터 가져오기
for year in years:
    print(f"{year}년 데이터 가져오는 중...")
    year_data = fetch_data_for_year(year)
    all_data.extend(year_data)

# 데이터를 JSON 파일로 저장
with open(output_file, "w", encoding="utf-8") as f:
    json.dump(all_data, f, ensure_ascii=False, indent=4)

print(f"데이터가 {output_file}에 저장되었습니다!")
