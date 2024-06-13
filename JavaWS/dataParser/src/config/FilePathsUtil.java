package config;

public class FilePathsUtil {

    public static String getMLBFilePath(String day) {
        return Paths.BASE_PATH + day + "MLBgames.json";
    }

    public static String getKBOFilePath(String day) {
        return Paths.BASE_PATH + day + "KBOgames.json";
    }
}
