package com.game.baseball.api.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.game.baseball.api.mapper.BaseBallMapper;

@Repository
public class BaseBallDaoImpl implements BaseBallDao {

    @Autowired
    private BaseBallMapper baseBallMapper;

    @Override
    public void createTemporaryMLBtbl() {
        baseBallMapper.createTemporaryMLBtbl();
    }

    @Override
    public void createTemporaryKBOtbl() {
        baseBallMapper.createTemporaryKBOtbl();
    }

    @Override
    public void dropMLBtbl() {
        baseBallMapper.dropMLBtbl();
    }

    @Override
    public void dropKBOtbl() {
        baseBallMapper.dropKBOtbl();
    }

    @Override
    public void createTemporaryMLBresulttbl() {
        baseBallMapper.createTemporaryMLBresulttbl();
    }

    @Override
    public void createTemporaryKBOresulttbl() {
        baseBallMapper.createTemporaryKBOresulttbl();
    }

    @Override
    public void dropMLBResulttbl() {
        baseBallMapper.dropMLBResulttbl();
    }

    @Override
    public void dropKBOResulttbl() {
        baseBallMapper.dropKBOResulttbl();
    }

    @Override
    public void insertTdyMLB(List<Map<String, Object>> params) {
        if (params == null || params.isEmpty()) {
            return; // 빈 리스트는 무시 경기 일정이 비어있을 수 있어서
        }
        for (Map<String, Object> param : params) {
            baseBallMapper.insertTdyMLB(param);
        }
    }

    @Override
    public void insertTdyKBO(List<Map<String, Object>> params) {
        if (params == null || params.isEmpty()) {
            return; // 빈 리스트는 무시 월요일KBO 쉬기때문에
        }
        for (Map<String, Object> param : params) {
            baseBallMapper.insertTdyKBO(param);
        }
    }

    @Override
    public void insertResultMLB(List<Map<String, Object>> params) {
        if (params == null || params.isEmpty()) {
            return; // 빈 리스트는 무시 어제 경기 기록이 비어있을 수 있어서
        }
        for (Map<String, Object> param : params) {
            baseBallMapper.insertResultMLB(param);
        }
    }

    @Override
    public void insertResultKBO(List<Map<String, Object>> params) {
        if (params == null || params.isEmpty()) {
            return; // 빈 리스트는 무시 월요일KBO 쉬기때문에
        }
        for (Map<String, Object> param : params) {
            baseBallMapper.insertResultKBO(param);
        }
    }

    @Override
    public void moveTempMLBResultToMainTable() {
        baseBallMapper.moveTempMLBResultToMainTable();
    }

    @Override
    public void moveTempKBOResultToMainTable() {
        baseBallMapper.moveTempKBOResultToMainTable();
    }

	@Override
	public void createTemporaryGPTAnly() {
		baseBallMapper.createTemporaryGPTAnly();
	}

	@Override
	public void dropGPTAnlytbl() {
		baseBallMapper.dropGPTAnlytbl();
	}

	@Override
	public void insertTdyExepect(List<Map<String, Object>> params) {
		baseBallMapper.insertTdyExepect(params);
	}

	@Override
	public void moveTempGPTexepectToMainTable() {
		baseBallMapper.moveTempGPTexepectToMainTable();
	}
}
