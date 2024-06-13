package jsonFileManager.readManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import config.FilePathsUtil;

public class readJSONImpl implements readJSON{

	// 오늘 mlb경기 일정을 가져옴
	@Override
    public List<HashMap<String, Object>> readMLBfile(String day) {  	
        // 우선은 절대경로위치로 설정
        String mlbJfilePath = FilePathsUtil.getMLBFilePath(day);
//        System.out.println(mlbJfilePath);
        ObjectMapper objMapper = new ObjectMapper();
        List<HashMap<String, Object>> glist = null;

        try {
            glist = objMapper.readValue(
                new File(mlbJfilePath),	
                new TypeReference<List<HashMap<String, Object>>>() {}
            );
            for (HashMap<String, Object> map : glist) {
                int team1 = (int) map.get("home_team");
                int team2 = (int) map.get("away_team");
                String date = (String) map.get("game_date");
                map.put("team1", team1);
                map.put("team2", team2);
                map.put("date", date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(glist);
        return glist;
    }

    // 오늘 KBO 경기 일정을 가져옴
	@Override
    public List<HashMap<String, Object>> readKBOfile(String day) {
        String kboJfilePath = FilePathsUtil.getKBOFilePath(day);
//        System.out.println(kboJfilePath);
        ObjectMapper objMapper = new ObjectMapper();
        List<HashMap<String, Object>> glist = null;

        try {
            glist = objMapper.readValue(
                new File(kboJfilePath),
                new TypeReference<List<HashMap<String, Object>>>() {}
            );
            for (HashMap<String, Object> map : glist) {
                int team1 =  (int) map.get("home_team");
                int team2 = (int) map.get("away_team");
                String date = (String) map.get("game_date");
                map.put("team1", team1);
                map.put("team2", team2);
                map.put("date", date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(glist);
        return glist;
    }
}
