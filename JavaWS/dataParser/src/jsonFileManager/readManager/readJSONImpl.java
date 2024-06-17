package jsonFileManager.readManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import config.FilePathsUtil;

public class readJSONImpl implements readJSON {

    // 오늘 MLB 경기 일정을 가져옴
    @Override
    public List<HashMap<String, Object>> readMLBfile(String day) {
        // 우선은 절대경로위치로 설정
        String mlbJfilePath = FilePathsUtil.getMLBFilePath(day);
        System.out.println("path : " + mlbJfilePath);
        ObjectMapper objMapper = new ObjectMapper();
        List<HashMap<String, Object>> glist = null;

        try {
            glist = objMapper.readValue(
                new File(mlbJfilePath),
                new TypeReference<List<HashMap<String, Object>>>() {}
            );

            if (glist.isEmpty()) {
                // 경기가 없는 경우 기본 값을 가진 항목 추가
                HashMap<String, Object> defaultMap = new HashMap<>();
                
                // 날짜 포맷 변경
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd"); // 입력된 day 포맷 (예: 20240617)
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd"); // 원하는 출력 포맷
                Date date = inputFormat.parse(day);
                String formattedDay = outputFormat.format(date);

                defaultMap.put("TEAM1", "경기없음");
                defaultMap.put("TEAM2", "경기없음");
                defaultMap.put("TDY", formattedDay);
                glist.add(defaultMap);
            } else {
                for (HashMap<String, Object> map : glist) {
                    int team1 = (int) map.get("home_team");
                    int team2 = (int) map.get("away_team");
                    String date = (String) map.get("game_date");
                    map.put("TEAM1", Integer.toString(team1));
                    map.put("TEAM2", Integer.toString(team2));
                    map.put("TDY", date);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return glist;
    }

    // 오늘 KBO 경기 일정을 가져옴
    @Override
    public List<HashMap<String, Object>> readKBOfile(String day) {
        String kboJfilePath = FilePathsUtil.getKBOFilePath(day);
        System.out.println("path : " + kboJfilePath);
        ObjectMapper objMapper = new ObjectMapper();
        List<HashMap<String, Object>> glist = null;

        try {
            glist = objMapper.readValue(
                new File(kboJfilePath),
                new TypeReference<List<HashMap<String, Object>>>() {}
            );

            if (glist.isEmpty()) {
                // 경기가 없는 경우 기본 값을 가진 항목 추가
                HashMap<String, Object> defaultMap = new HashMap<>();
                
                // 날짜 포맷 변경
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd"); // 입력된 day 포맷 (예: 20240617)
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd"); // 원하는 출력 포맷
                Date date = inputFormat.parse(day);
                String formattedDay = outputFormat.format(date);

                defaultMap.put("TEAM1", "경기없음");
                defaultMap.put("TEAM2", "경기없음");
                defaultMap.put("TDY", formattedDay);
                glist.add(defaultMap);
            } else {
                for (HashMap<String, Object> map : glist) {
                    int team1 = (int) map.get("home_team");
                    int team2 = (int) map.get("away_team");
                    String date = (String) map.get("game_date");
                    map.put("TEAM1", Integer.toString(team1));
                    map.put("TEAM2", Integer.toString(team2));
                    map.put("TDY", date);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return glist;
    }
}
