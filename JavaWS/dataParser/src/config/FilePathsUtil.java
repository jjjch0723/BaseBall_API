package config;

public class FilePathsUtil {

    public static String getMLBFilePath(String day) {
    	System.out.println(Paths.BASE_PATH + day + "MLBgames.json");
        return Paths.MLB_JSON_PATH + day + "MLBgames.json";
    }

    public static String getKBOFilePath(String day) {
        return Paths.KBO_JSON_PATH + day + "KBOgames.json";
    }
}
