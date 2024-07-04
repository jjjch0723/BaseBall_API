package com.game.baseball.api.jsonFileManager.readManager;

import java.util.List;
import java.util.Map;

public interface readJSON {
	public List<Map<String, Object>> readMLBfile(String day);
	public List<Map<String, Object>> readKBOfile(String day);
	public List<Map<String, Object>> readMLBrsltfile(String day);
	public List<Map<String, Object>> readKBOrsltfile(String day);
	public List<Map<String, Object>> readGPTExecpet();
}
