import json

# 주어진 JSON 데이터
data = {
  "data": [
    {
      "id": 112345,
      "topleveldomain": "게임",
      "level2": "롤플레잉",
      "level3": "월드오브워크래프트",
      "term": "부탱",
      "sense_no": 1,
      "definition": "메인 탱커를 보조하는 탱커라는 말로, 레이드와 같이 큰 규모의 전투가 진행될 때 방어를 담당하는 탱커 역할",
      "pos": "NNG",
      "bts": [
        {
          "id": 12231,
          "term": "탱커"
        }
      ],
      "nts": [],
      "rts": [
        {
          "id": 21212,
          "type": "origin",
          "term": "보조탱커"
        },
        {
          "id": 21211,
          "type": "sibling",
          "term": "메인탱"
        }
      ],
      "facet": ["역할"]
    }
    # 추가 데이터 항목들...
  ]
}

# JSON 데이터를 텍스트 형식으로 변환
text_data = []

for item in data['data']:
    text_item = (
        f"ID: {item['id']}\n"
        f"Top Level Domain: {item['topleveldomain']}\n"
        f"Level 2: {item['level2']}\n"
        f"Level 3: {item['level3']}\n"
        f"Term: {item['term']}\n"
        f"Sense Number: {item['sense_no']}\n"
        f"Definition: {item['definition']}\n"
        f"Part of Speech: {item['pos']}\n"
        f"Backward Terms: {', '.join([bt['term'] for bt in item['bts']]) if item['bts'] else 'None'}\n"
        f"Narrower Terms: {', '.join([nt['term'] for nt in item['nts']]) if item['nts'] else 'None'}\n"
        f"Related Terms: {', '.join([rt['term'] for rt in item['rts']]) if item['rts'] else 'None'}\n"
        f"Facet: {', '.join(item['facet'])}\n"
    )
    text_data.append(text_item)

# 변환된 텍스트 데이터를 파일로 저장
with open('training_data.txt', 'w', encoding='utf-8') as f:
    for text in text_data:
        f.write(text + "\n" + "="*80 + "\n")

# 결과 출력 (optional)
for text in text_data:
    print(text)
    print("="*80)
