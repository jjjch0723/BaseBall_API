package com.game.baseball.api.jsonFileManager.readManager;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.baseball.api.config.FilePathsUtil;

@Component // 객체 등록
public class readJSONImpl implements readJSON {

    @Autowired
    private FilePathsUtil filePathsUtil;

    // 오늘 MLB 경기 일정을 가져옴
    @Override
    public List<Map<String, Object>> readMLBfile(String day) {
        String mlbJfilePath = filePathsUtil.getMLBFilePath(day);
        System.out.println(day + "의 MLB경기 일정을 가져옵니다.");
        ObjectMapper objMapper = new ObjectMapper();
        List<Map<String, Object>> glist = null;

        try {
            File file = new File(mlbJfilePath);
            if (!file.exists()) {
                System.out.println("MLB file not found: " + mlbJfilePath);
                return null;
            }

            glist = objMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});

            if (glist.isEmpty()) {
                System.out.println("Empty MLB file: " + mlbJfilePath);
                return null;
            } else {
                for (Map<String, Object> map : glist) {
                    int team1 = (int) map.get("home_team");
                    int team2 = (int) map.get("away_team");
                    String date = (String) map.get("game_date");
                    map.put("TEAM1", Integer.toString(team1));
                    map.put("TEAM2", Integer.toString(team2));
                    map.put("DATE", date);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return glist;
    }

    // 오늘 KBO 경기 일정을 가져옴
    @Override
    public List<Map<String, Object>> readKBOfile(String day) {
        String kboJfilePath = filePathsUtil.getKBOFilePath(day);
        System.out.println(day + "의 KBO경기 일정을 가져옵니다.");
        ObjectMapper objMapper = new ObjectMapper();
        List<Map<String, Object>> glist = null;

        try {
            File file = new File(kboJfilePath);
            if (!file.exists()) {
                return null;
            }

            glist = objMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});

            if (glist.isEmpty()) {
                System.out.println("Empty KBO file: " + kboJfilePath);
                return null;
            } else {
                for (Map<String, Object> map : glist) {
                    int team1 = (int) map.get("home_team");
                    int team2 = (int) map.get("away_team");
                    String date = (String) map.get("game_date");
                    map.put("TEAM1", Integer.toString(team1));
                    map.put("TEAM2", Integer.toString(team2));
                    map.put("DATE", date);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return glist;
    }

    @Override
    public List<Map<String, Object>> readMLBrsltfile(String day) {
        String mlbJfilePath = filePathsUtil.getMLBResultFilePath(day);
        System.out.println(day + "의 MLB경기 결과를 가져옵니다.");
        ObjectMapper objMapper = new ObjectMapper();
        List<Map<String, Object>> glist = null;

        try {
            File file = new File(mlbJfilePath);
            if (!file.exists()) {
                System.out.println("MLB result file not found: " + mlbJfilePath);
                return null;
            }

            glist = objMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});

            if (glist.isEmpty()) {
                System.out.println("Empty MLB result file: " + mlbJfilePath);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return glist;
    }

    @Override
    public List<Map<String, Object>> readKBOrsltfile(String day) {
        String kboJfilePath = filePathsUtil.getKBOResultFilePath(day);
        System.out.println(day + "의 KBO경기 결과를 가져옵니다.");
        ObjectMapper objMapper = new ObjectMapper();
        List<Map<String, Object>> glist = null;

        try {
            File file = new File(kboJfilePath);
            if (!file.exists()) {
                System.out.println("KBO result file not found: " + kboJfilePath);
                return null;
            }

            glist = objMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});

            if (glist.isEmpty()) {
                System.out.println("Empty KBO result file: " + kboJfilePath);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return glist;
    }

	@Override
	public List<Map<String, Object>> readGPTExecpet() {
		String gptFilePath = filePathsUtil.getGPTexecptFilePath();
		System.out.println("gpt의 예상 결과를 가져옵니다.");
		ObjectMapper objMapper = new ObjectMapper();
		List<Map<String, Object>> glist = null;
		
		try {
			File file = new File(gptFilePath);
			if(!file.exists()) {
				System.out.println("GPT execpt file not found: " + gptFilePath);
                return null;
			}
			
			glist = objMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
            if (glist.isEmpty()) {
                System.out.println("Empty GPT execpt file: " + gptFilePath);
                return null;
            }
            
		}catch (Exception e) {
			e.printStackTrace();
		}
		return glist;
	}
}
