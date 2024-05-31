import statsapi
import json
from datetime import datetime, timedelta

# Function to get schedule for a specific date range
def get_schedule(start_date, end_date):
    schedule = []
    current_date = start_date

    while current_date <= end_date:
        games = statsapi.schedule(start_date=current_date.strftime('%Y-%m-%d'), end_date=current_date.strftime('%Y-%m-%d'))
        for game in games:
            game_info = {
                "date": game['game_date'],
                "away_team": game['away_name'],
                "home_team": game['home_name'],
                "venue": game['venue_name'],
                "status": game['status'],
                "away_score": game.get('away_score', 'N/A'),
                "home_score": game.get('home_score', 'N/A')
            }
            schedule.append(game_info)
        current_date += timedelta(days=1)

    return schedule

# Get the schedule for June 10, 2000
test_date = datetime(2000, 6, 10)
schedule = get_schedule(test_date, test_date)

# Display the results for June 10, 2000
print(json.dumps(schedule, indent=4))
