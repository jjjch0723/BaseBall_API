import json

# 파일 경로 설정
input_file = "C:\\DevTool\\BaseBall\\BaseBall_data_Crowling\\json\\yearTeamStatus\\MLB_batting_data.json"
output_file = "C:\\DevTool\\BaseBall\\BaseBall_data_Crowling\\json\\yearTeamStatus\\MLB_batting_data_converted.json"

# JSON 파일 읽기
with open(input_file, "r", encoding="utf-8") as f:
    data = json.load(f)

# avg, obp, slg 필드의 값을 문자열에서 실수로 변환한 후 다시 문자열로 변환
for entry in data:
    entry["avg"] = "{:.3f}".format(float(entry["avg"]))
    entry["obp"] = "{:.3f}".format(float(entry["obp"]))
    entry["slg"] = "{:.3f}".format(float(entry["slg"]))

# 변환된 데이터를 새로운 JSON 파일로 저장
with open(output_file, "w", encoding="utf-8") as f:
    json.dump(data, f, ensure_ascii=False, indent=4)

print(f"데이터가 {output_file}에 저장되었습니다.")
