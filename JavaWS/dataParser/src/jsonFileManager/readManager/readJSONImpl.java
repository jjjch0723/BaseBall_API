package jsonFileManager.readManager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.FilePathsUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class readJSONImpl implements readJSON {

    // 오늘 MLB 경기 일정을 가져옴
    @Override
    public List<HashMap<String, Object>> readMLBfile(String day) {
        String mlbJfilePath = FilePathsUtil.getMLBFilePath(day);
        System.out.println("path : " + mlbJfilePath);
        ObjectMapper objMapper = new ObjectMapper();
        List<HashMap<String, Object>> glist = null;

        try {
            File file = new File(mlbJfilePath);
            if (!file.exists()) {
                System.out.println("MLB result file not found: " + mlbJfilePath);
                return null;
            }

            glist = objMapper.readValue(
                file,
                new TypeReference<List<HashMap<String, Object>>>() {}
            );

            if (glist.isEmpty()) {
                HashMap<String, Object> defaultMap = new HashMap<>();
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inputFormat.parse(day);
                String formattedDay = outputFormat.format(date);

                defaultMap.put("TEAM1", "경기없음");
                defaultMap.put("TEAM2", "경기없음");
                defaultMap.put("DATE", formattedDay);
                glist.add(defaultMap);
            } else {
                for (HashMap<String, Object> map : glist) {
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
    public List<HashMap<String, Object>> readKBOfile(String day) {
        String kboJfilePath = FilePathsUtil.getKBOFilePath(day);
        System.out.println("path : " + kboJfilePath);
        ObjectMapper objMapper = new ObjectMapper();
        List<HashMap<String, Object>> glist = null;

        try {
            File file = new File(kboJfilePath);
            if (!file.exists()) {
                System.out.println("KBO result file not found: " + kboJfilePath);
                return null;
            }

            glist = objMapper.readValue(
                file,
                new TypeReference<List<HashMap<String, Object>>>() {}
            );

            if (glist.isEmpty()) {
                HashMap<String, Object> defaultMap = new HashMap<>();
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inputFormat.parse(day);
                String formattedDay = outputFormat.format(date);

                defaultMap.put("TEAM1", "경기없음");
                defaultMap.put("TEAM2", "경기없음");
                defaultMap.put("DATE", formattedDay);
                glist.add(defaultMap);
            } else {
                for (HashMap<String, Object> map : glist) {
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
    public List<HashMap<String, Object>> readMLBrsltfile(String day) {
        String mlbJfilePath = FilePathsUtil.getMLBResultFilePath(day);
        System.out.println("ReadJsonfile Method path : " + mlbJfilePath);
        ObjectMapper objMapper = new ObjectMapper();
        List<HashMap<String, Object>> glist = null;

        try {
            File file = new File(mlbJfilePath);
            if (!file.exists()) {
                System.out.println("MLB result file not found: " + mlbJfilePath);
                return null;
            }

            glist = objMapper.readValue(
                file,
                new TypeReference<List<HashMap<String, Object>>>() {}
            );

            if (glist.isEmpty()) {
                HashMap<String, Object> defaultMap = new HashMap<>();
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inputFormat.parse(day);
                String formattedDay = outputFormat.format(date);

                defaultMap.put("DATE", formattedDay);
                defaultMap.put("WINTEAM", "경기없음");
                defaultMap.put("LOSETEAM", "경기없음");
                defaultMap.put("WINSCORE", "경기없음");
                defaultMap.put("LOSESCORE", "경기없음");
                glist.add(defaultMap);
            } else {
                for (HashMap<String, Object> map : glist) {
                    String date = (String) map.get("DATE");
                    String winTeam = (String) map.get("WINTEAM");
                    String loseTeam = (String) map.get("LOSETEAM");
                    String winScore = (String) map.get("WINSCORE");
                    String loseScore = (String) map.get("LOSESCORE");

                    map.put("DATE", date);
                    map.put("WINTEAM", winTeam);
                    map.put("LOSETEAM", loseTeam);
                    map.put("WINSCORE", winScore);
                    map.put("LOSESCORE", loseScore);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return glist;
    }

    @Override
    public List<HashMap<String, Object>> readKBOrsltfile(String day) {
        String kboJfilePath = FilePathsUtil.getKBOResultFilePath(day);
        System.out.println("ReadJsonfile Method path : " + kboJfilePath);
        ObjectMapper objMapper = new ObjectMapper();
        List<HashMap<String, Object>> glist = null;

        try {
            File file = new File(kboJfilePath);
            if (!file.exists()) {
                System.out.println("KBO result file not found: " + kboJfilePath);
                return null;
            }

            glist = objMapper.readValue(
                file,
                new TypeReference<List<HashMap<String, Object>>>() {}
            );

            if (glist.isEmpty()) {
                HashMap<String, Object> defaultMap = new HashMap<>();
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inputFormat.parse(day);
                String formattedDay = outputFormat.format(date);

                defaultMap.put("DATE", formattedDay);
                defaultMap.put("WINTEAM", "경기없음");
                defaultMap.put("LOSETEAM", "경기없음");
                defaultMap.put("WINSCORE", "경기없음");
                defaultMap.put("LOSESCORE", "경기없음");
                glist.add(defaultMap);
            } else {
                for (HashMap<String, Object> map : glist) {
                    String date = (String) map.get("DATE");
                    String winTeam = (String) map.get("WINTEAM");
                    String loseTeam = (String) map.get("LOSETEAM");
                    String winScore = (String) map.get("WINSCORE");
                    String loseScore = (String) map.get("LOSESCORE");

                    map.put("DATE", date);
                    map.put("WINTEAM", winTeam);
                    map.put("LOSETEAM", loseTeam);
                    map.put("WINSCORE", winScore);
                    map.put("LOSESCORE", loseScore);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return glist;
    }
}
