package config;

public class FilePathsUtil {

	// 경기 일정용 json파일
    public static String getMLBFilePath(String day) {
    	System.out.println(Paths.BASEBALL_JSON_PATH + day + "MLBgames.json");
        return Paths.BASEBALL_JSON_PATH + day + "MLBgames.json";
    }

    public static String getKBOFilePath(String day) {
    	System.out.println(Paths.BASEBALL_JSON_PATH + day + "KBOgames.json");
        return Paths.BASEBALL_JSON_PATH + day + "KBOgames.json";
    }
    // 
    
    // 경기 기록용 json파일
    public static String getMLBResultFilePath(String day) {
    	System.out.println("Json Path : " + Paths.BASEBALL_JSON_PATH + day + "MLBresult.json");
    	return Paths.BASEBALL_JSON_PATH + day + "MLBresult.json";
    }
    
    public static String getKBOResultFilePath(String day) {
    	System.out.println("Json Path : " + Paths.BASEBALL_JSON_PATH + day + "KBOresult.json");
    	return Paths.BASEBALL_JSON_PATH + day + "KBOresult.json";
    }
    //
}
