import json
import statsapi

# 데이터를 저장할 JSON 파일 경로 정의
output_file_pitching = "C:\\DevTool\\BaseBall\\BaseBall_data_Crowling\\json\\yearTeamStatus\\MLB_pitching_data.json"
output_file_batting = "C:\\DevTool\\BaseBall\\BaseBall_data_Crowling\\json\\yearTeamStatus\\MLB_batting_data.json"

# 필요한 키 매핑
pitching_key_mapping = {
    "gamesStarted": "gs",
    "completeGames": "cg",
    "shutouts": "sho",
    "saves": "s",
    "hits": "h",
    "inningsPitched": "ip",
    "earnedRuns": "er",
    "runs": "r",
    "homeRuns": "hr",
    "baseOnBalls": "bb",
    "hitBatsmen": "hp",
    "strikeOuts": "so"
}

batting_key_mapping = {
    "atBats": "ab",
    "runs": "r",
    "hits": "h",
    "doubles": "2b",
    "triples": "3b",
    "homeRuns": "hr",
    "rbi": "rbi",
    "stolenBases": "sb",
    "caughtStealing": "cs",
    "baseOnBalls": "bb",
    "strikeOuts": "so",
    "avg": "avg",
    "obp": "obp",
    "slg": "slg"
}

# 주어진 연도의 데이터를 가져와서 파싱하는 함수 정의
def fetch_team_stats(year, stat_type, key_mapping):
    stats = []
    teams = statsapi.get('teams', {'sportId': 1})['teams']
    for team in teams:
        team_id = team['id']
        team_name = team['name']
        team_stats = statsapi.get('team_stats', {'teamId': team_id, 'season': year, 'group': stat_type, 'stats': 'season'})
        
        if 'stats' in team_stats:
            for stat in team_stats['stats']:
                if 'splits' in stat:
                    for split in stat['splits']:
                        team_data = {"year": str(year), "teamName": team_name}
                        if 'stat' in split:
                            for api_key, json_key in key_mapping.items():
                                team_data[json_key] = str(split['stat'].get(api_key, ""))
                            stats.append(team_data)
                        else:
                            print(f"No stats found for team: {team_name}, year: {year}")
                else:
                    print(f"No splits found for team: {team_name}, year: {year}")
        else:
            print(f"No team stats found for team: {team_name}, year: {year}")
    return stats

# 연도를 순회하며 데이터 가져오기
all_pitching_data = []
all_batting_data = []

for year in range(2000, 2024):
    print(f"{year}년 데이터 가져오는 중...")
    
    # 피칭 데이터 가져오기
    pitching_data = fetch_team_stats(year, "pitching", pitching_key_mapping)
    all_pitching_data.extend(pitching_data)
    
    # 타격 데이터 가져오기
    batting_data = fetch_team_stats(year, "hitting", batting_key_mapping)
    all_batting_data.extend(batting_data)

# 데이터를 JSON 파일로 저장
with open(output_file_pitching, "w", encoding="utf-8") as f:
    json.dump(all_pitching_data, f, ensure_ascii=False, indent=4)

with open(output_file_batting, "w", encoding="utf-8") as f:
    json.dump(all_batting_data, f, ensure_ascii=False, indent=4)

print(f"Pitching data saved to {output_file_pitching}")
print(f"Batting data saved to {output_file_batting}")
