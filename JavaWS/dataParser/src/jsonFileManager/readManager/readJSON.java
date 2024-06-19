package jsonFileManager.readManager;

import java.util.HashMap;
import java.util.List;

public interface readJSON {
	public List<HashMap<String, Object>> readMLBfile(String day);
	public List<HashMap<String, Object>> readKBOfile(String day);
	public List<HashMap<String, Object>> readMLBrsltfile(String day);
	public List<HashMap<String, Object>> readKBOrsltfile(String day);
}
