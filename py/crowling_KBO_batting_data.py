import json
import requests
from bs4 import BeautifulSoup

# 기본 URL 및 JSON 파일 저장 경로 정의
base_url = "https://statiz.sporki.com/stats/?m=team&m2=batting&m3=default&so=WAR&ob=DESC&year={year}&sy=&ey=&te=&po=&lt=10100&reg=&pe=&ds=&de=&we=&hr=&ha=&ct=&st=&vp=&bo=&pt=&pp=&ii=&vc=&um=&oo=&rr=&sc=&bc=&ba=&li=&as=&ae=&pl=&gc=&lr=&pr=50&ph=&hs=&us=&na=&ls=&sf1=&sk1=&sv1=&sf2=&sk2=&sv2="
output_file = "C:\\DevTool\\BaseBall\\BaseBall_data_Crowling\\json\\yearTeamStatus\\KBO_batting_data_2000_to_2023.json"

# 데이터를 가져올 연도 정의
years = range(2000, 2024)

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
    
    # 헤더에서 키값 추출
    headers = team_table.find_all("th")
    keys = ["year"] + [header.get_text(strip=True) for header in headers if header.get_text(strip=True) not in ["정렬", "비율"]]
    
    # 테이블 행 추출
    rows = team_table.find_all("tr")[1:]  # 헤더 행은 제외

    year_data = []
    
    for row in rows:
        cols = row.find_all("td")
        if len(cols) > 1:
            values = [str(year)] + [col.get_text(strip=True) if col.get_text(strip=True) else "" for col in cols]
            # keys와 values의 길이가 같은지 확인하고 맞추기
            if len(values) == len(keys):
                team_data = {keys[i]: values[i] for i in range(len(keys))}
            else:
                print(f"키와 값의 길이가 맞지 않습니다: {len(keys)} vs {len(values)}")
                print(f"keys: {keys}")
                print(f"values: {values}")
                continue
            year_data.append(team_data)
    return year_data

# 연도를 순회하며 데이터 가져오기
for year in years:
    print(f"📅 {year}년 데이터 가져오는 중...")
    year_data = fetch_data_for_year(year)
    all_data.extend(year_data)

# 데이터를 JSON 파일로 저장
with open(output_file, "w", encoding="utf-8") as f:
    json.dump(all_data, f, ensure_ascii=False, indent=4)

print(f"✅ 데이터가 {output_file}에 저장되었습니다!")
