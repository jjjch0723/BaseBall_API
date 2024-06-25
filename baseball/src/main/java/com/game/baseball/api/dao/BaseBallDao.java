package com.game.baseball.api.dao;

import java.util.List;
import java.util.Map;

public interface BaseBallDao {
	// 최근 경기 일정
	void createTemporaryMLBtbl();
	void createTemporaryKBOtbl();
	void dropMLBtbl();
	void dropKBOtbl();
	void insertTdyMLB(List<Map<String, Object>> params);
	void insertTdyKBO(List<Map<String, Object>> params);
	// 최근 경기 일정 끝
	
	// 최근 경기 기록
	void createTemporaryMLBresulttbl();
	void createTemporaryKBOresulttbl();
	void dropMLBResulttbl();
	void dropKBOResulttbl();
	void insertResultMLB(List<Map<String, Object>> parms);
	void insertResultKBO(List<Map<String, Object>> parms);
	// 최근 경기 기록 끝
	
	// 최근 경기 기록 리그별 NT에 삽입
	void moveTempMLBResultToMainTable();
	void moveTempKBOResultToMainTable();
	//
}
