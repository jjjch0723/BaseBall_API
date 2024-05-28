import statsapi

def get_team_hitting_stats(team_id, year):
    # 팀의 타격 통계 데이터를 가져옴
    endpoint = f'teams/stats?teamId={team_id}&season={year}&group=hitting&stats=season'
    data = statsapi.get(endpoint)
    return data

# 예제: 2023년 뉴욕 양키스(팀 ID: 147)의 타격 통계 가져오기
team_id = 147  # 뉴욕 양키스 팀 ID
year = 2023  # 연도
hitting_stats = get_team_hitting_stats(team_id, year)
print(hitting_stats)
