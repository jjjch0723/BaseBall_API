import mysql.connector
import pandas as pd
import json

db_config = {
    'user': 'studyuser',  # 데이터베이스 사용자 이름
    'password': '1111',  # 데이터베이스 비밀번호
    'host': '192.168.0.78',  # 데이터베이스 호스트 주소
    'database': 'baseball'  # 데이터베이스 이름
}

# 데이터베이스에 연결
connection = mysql.connector.connect(**db_config)
cursor = connection.cursor()

# 테이블을 생성하는 함수
def create_table(cursor, table_name, columns):
    columns_with_types = ', '.join([f"{col} TEXT" for col in columns])
    create_table_query = f"""
    CREATE TABLE IF NOT EXISTS {table_name} (
        id INT AUTO_INCREMENT PRIMARY KEY,
        {columns_with_types}
    );
    """
    cursor.execute(create_table_query)

# 테이블에 데이터를 삽입하는 함수
def insert_data(cursor, table_name, data):
    placeholders = ', '.join(['%s'] * len(data[0]))
    columns = ', '.join(data[0].keys())
    insert_query = f"INSERT INTO {table_name} ({columns}) VALUES ({placeholders})"
    for row in data:
        cursor.execute(insert_query, list(row.values()))

# JSON 파일을 로드하고 처리
file_paths = {
    'KBO_game_result': r'C:\DevTool\BaseBall\BaseBall_data_Crowling\json\game_result_00~23\game_result_KBO_2000_to_2023_teamCode.json',
    'MLB_game_result': r'C:\DevTool\BaseBall\BaseBall_data_Crowling\json\game_result_00~23\game_result_MLB_2000_to_2023_teamCode.json',
}

for table_name, file_path in file_paths.items():
    # JSON 데이터 로드
    with open(file_path, 'r') as file:
        data = json.load(file)
    
    # 파일이 너무 큰 경우 pandas를 사용하여 읽기
    if isinstance(data, list):
        columns = data[0].keys()
    else:
        df = pd.read_json(file_path, lines=True)
        columns = df.columns

    # 테이블 생성
    create_table(cursor, table_name, columns)
    
    # 데이터 삽입
    insert_data(cursor, table_name, data)
    connection.commit()

# 연결 종료
cursor.close()
connection.close()
