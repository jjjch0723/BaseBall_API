import json
import os

def combine_files(start_year, end_year, output_file):
    input_dir = "C:\\DevTool\\BaseBall\\BaseBall_data_Crowling\\json"
    combined_data = []
    
    for year in range(start_year, end_year + 1):
        input_file = f"migrated_game_result_MLB_{year}.json"
        input_path = os.path.join(input_dir, input_file)
        
        if not os.path.exists(input_path):
            print(f"File not found: {input_path}")
            continue
        
        with open(input_path, 'r', encoding='utf-8') as infile:
            data = json.load(infile)
            combined_data.extend(data)
    
    output_path = os.path.join(input_dir, output_file)
    
    with open(output_path, 'w', encoding='utf-8') as outfile:
        json.dump(combined_data, outfile, ensure_ascii=False, indent=4)
    
    print(f"Combined file created: {output_path}")

# 2000년부터 2023년까지의 모든 파일을 합쳐서 새로운 파일 생성
combine_files(2000, 2023, "game_result_MLB_2000_to_2023.json")
