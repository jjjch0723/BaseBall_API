package com.game.baseball.api.config;

public class FilePathsUtil {

	// 경기 일정용 json파일
    public static String getMLBFilePath(String day) {
        return Paths.BASEBALL_JSON_PATH + day + "MLBgames.json";
    }

    public static String getKBOFilePath(String day) {
        return Paths.BASEBALL_JSON_PATH + day + "KBOgames.json";
    }
    // 
    
    // 경기 기록용 json파일
    public static String getMLBResultFilePath(String day) {
    	return Paths.BASEBALL_JSON_PATH + day + "MLBresult.json";
    }
    
    public static String getKBOResultFilePath(String day) {
    	return Paths.BASEBALL_JSON_PATH + day + "KBOresult.json";
    }
    //
}
