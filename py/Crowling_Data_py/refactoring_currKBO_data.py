import json
import os
from datetime import datetime

def migrate_kbo_data(input_file, output_file):
    input_dir = "C:\\DevTool\\BaseBall\\BaseBall_data_Crowling\\json"
    
    input_path = os.path.join(input_dir, input_file)
    output_path = os.path.join(input_dir, output_file)
    
    if not os.path.exists(input_path):
        print(f"File not found: {input_path}")
        return
    
    with open(input_path, 'r', encoding='utf-8') as infile:
        games = json.load(infile)
    
    migrated_games = []
    for game in games:
        date = datetime.strptime(game['day'], '%Y%m%d')
        date_str = date.strftime('%Y-%m-%d')
        
        migrated_game = {
            "date": date_str,
            "winTeam": game['winTeam'],
            "loseTeam": game['loseTeam'],
            "winScore": game['winScore'],
            "loseScore": game['loseScore']
        }
        
        migrated_games.append(migrated_game)
    
    with open(output_path, 'w', encoding='utf-8') as outfile:
        json.dump(migrated_games, outfile, ensure_ascii=False, indent=4)
    
    print(f"Migration complete. Output file: {output_path}")

# KBO 데이터를 마이그레이션하여 새로운 파일로 저장
migrate_kbo_data("origin_game_results_KBO_2000_to_2023.json", "game_result_KBO_2000_to_2023.json")
