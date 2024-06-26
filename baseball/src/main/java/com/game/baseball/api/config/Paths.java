package com.game.baseball.api.config;

public class Paths {
    public static final String MLB_PY_PATH = "src/main/resources/py/getTodayMLBgame.py"; // MLB의 경기 일정을 가져오는 파이썬파일 위치
    public static final String KBO_PY_PATH = "src/main/resources/py/getTodayKBOgame.py"; // KBO의 경기 일정을 가져오는 파이썬파일 위치
    public static final String MLB_RSLT_PY_PATH = "src/main/resources/py/getCurMLBrslt.py"; // MLB의 경기 결과를 가져오는 파이썬파일 위치
    public static final String KBO_RSLT_PY_PATH = "src/main/resources/py/getCurKBOrslt.py"; // KBO의 경기 결과를 가져오는 파이썬파일 위치
    public static final String BASEBALL_JSON_PATH = "src/main/resources/py/json/todaysGames/"; // 데이터 저장용 파일 위치
}
