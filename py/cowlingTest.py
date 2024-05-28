# -*- coding: utf-8 -*-

import pandas as pd
import requests
import os
from io import StringIO

team_codes = {
    1: ("2002", "KIA"),       # KIA
    2: ("6002", "두산"),       # 두산
    3: ("3001", "롯데"),       # 롯데
    4: ("5002", "LG"),         # LG
    5: ("1001", "삼성"),       # 삼성
    6: ("11001", "NC"),        # NC
    7: ("9002", "SSG"),        # SSG
    8: ("12001", "KT"),        # KT
    9: ("7002", "한화"),       # 한화
    10: ("10001", "키움")      # 키움
}

def fetch_and_extract_data(year, team_code):
    base_url = f"https://statiz.sporki.com/stats/?m=team&m2=all&m3=default&so=WAA_Fielding&ob=DESC&year={year}&sy={year}&ey={year}&te={team_code}&po=&lt=10100&reg=&pe=&ds=&de=&we=&hr=&ha=&ct=&st=&vp=&bo=&pt=&pp=&ii=&vc=&um=&oo=&rr=&sc=&bc=&ba=&li=&as=&ae=&pl=&gc=&lr=&pr=50&ph=&hs=&us=&na=&ls=&sf1=&sk1=&sv1=&sf2=&sk2=&sv2="
    response = requests.get(base_url)
    response.raise_for_status()  # 요청 실패 시 오류 발생
    
    tables = pd.read_html(StringIO(response.text))
    
    # 필요한 테이블 선택 (여러 테이블이 있을 경우 첫 번째 테이블 사용)
    df = tables[0]
    df["연도"] = year
    
    return df

def save_to_json(df, filename):
    # MultiIndex를 단일 수준으로 변환
    if isinstance(df.columns, pd.MultiIndex):
        df.columns = [' '.join(col).strip() for col in df.columns.values]

    # 중복 컬럼명을 수정
    df.columns = [col.replace(' ', '_').replace('.', '') for col in df.columns]
    
    # 제외할 열 목록
    exclude_columns = ["WAA_야수", "WAA_투수", "Rep_Win_투수", "WAR_투수"]
    
    # 열을 제외한 데이터프레임 생성
    df = df.drop(columns=exclude_columns, errors='ignore')
    
    file_path = os.path.join("C:\\DevTool\\workspace", filename)
    df.to_json(file_path, orient="records", force_ascii=False, indent=4)
    print(f"{file_path} SAVE OK.")

def main():
    year = input("Enter year: ")
    
    all_data = []
    for team_code, team_name in team_codes.values():
        df = fetch_and_extract_data(year, team_code)
        df.insert(0, "팀명", team_name)
        all_data.append(df)
    
    # 모든 데이터를 하나의 데이터프레임으로 결합
    combined_df = pd.concat(all_data, ignore_index=True)
    
    json_filename = os.path.join("C:\\DevTool\\workspace", f"{year}.json")
    save_to_json(combined_df, json_filename)

if __name__ == "__main__":
    main()