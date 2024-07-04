package com.game.baseball.api.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseBallMapper {
	// 최근 경기 일정
	void createTemporaryMLBtbl();
	void createTemporaryKBOtbl();
	void dropMLBtbl();
	void dropKBOtbl();
	void insertTdyMLB(Map<String, Object> params);
	void insertTdyKBO(Map<String, Object> params);
	// 최근 경기 일정 끝
	
	// 최근 경기 기록
	void createTemporaryMLBresulttbl();
	void createTemporaryKBOresulttbl();
	void dropMLBResulttbl();
	void dropKBOResulttbl();
	void insertResultMLB(Map<String, Object> parms);
	void insertResultKBO(Map<String, Object> parms);
	// 최근 경기 기록 끝
	
	// 임시테이블 데이터 노말 테이블로 데이터 삽입
    void moveTempMLBResultToMainTable();
    void moveTempKBOResultToMainTable();
	// 임시테이블 데이터 노말 테이블로 데이터 삽입 끝
    
	// gpt예상 결과
	void createTemporaryGPTAnly();
	void dropGPTAnlytbl();
	void insertTdyExepect(List<Map<String, Object>> params);
	void moveTempGPTexepectToMainTable();
	// gpt예상 결과 끝
    
}
