import json
import os
from datetime import datetime, timedelta

def migrate_data(year):
    input_dir = "C:\\DevTool\\BaseBall\\BaseBall_data_Crowling\\json"
    input_file = f"game_result_MLB_{year}.json"
    output_file = f"migrated_game_result_MLB_{year}.json"
    
    input_path = os.path.join(input_dir, input_file)
    output_path = os.path.join(input_dir, output_file)
    
    if not os.path.exists(input_path):
        print(f"File not found: {input_path}")
        return
    
    with open(input_path, 'r', encoding='utf-8') as infile:
        games = json.load(infile)
    
    migrated_games = []
    for game in games:
        date = datetime.strptime(game['date'], '%Y-%m-%d') + timedelta(days=1)
        date_str = date.strftime('%Y-%m-%d')
        
        if game['away_score'] > game['home_score']:
            win_team = game['away_team']
            lose_team = game['home_team']
            win_score = str(game['away_score'])
            lose_score = str(game['home_score'])
        else:
            win_team = game['home_team']
            lose_team = game['away_team']
            win_score = str(game['home_score'])
            lose_score = str(game['away_score'])
        
        migrated_game = {
            "date": date_str,
            "winTeam": win_team,
            "loseTeam": lose_team,
            "winScore": win_score,
            "loseScore": lose_score
        }
        
        migrated_games.append(migrated_game)
    
    with open(output_path, 'w', encoding='utf-8') as outfile:
        json.dump(migrated_games, outfile, ensure_ascii=False, indent=4)
    
    print(f"Migration complete for year {year}. Output file: {output_path}")

for year in range(2000, 2024):
    migrate_data(year)
