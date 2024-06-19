package main;

import config.Paths;
import dao.resultDAO;
import jsonFileManager.getDay;
import jsonFileManager.readManager.readJSONImpl;
import runPy.runPyfileImpl;

public class resultController {

	public void rCont() {
		getDay gd = new getDay();
		readJSONImpl rji = new readJSONImpl();
		runPyfileImpl rpi = new runPyfileImpl();
		resultDAO rd = new resultDAO();
		
		// 오늘 날짜 MLB에 사용
		String tdy = gd.getTodaydate();
		// 어제 날짜 KBO에 사용
		String ysd = gd.getYesterdaydate();
		System.out.println(ysd);
		
		String mlbPyPath = Paths.MLB_RSLT_PY_PATH;
        String kboPyPath = Paths.KBO_RSLT_PY_PATH;
		
        // 파이썬 파일 실행부분
		rpi.pyRunner(mlbPyPath);
		rpi.pyRunner(kboPyPath);
		
        // json파일 생성을 위해 2초간 대기
		try {
			Thread.sleep(4000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 경기 일정을 읽는 부분
		rji.readMLBrsltfile(tdy);
		rji.readKBOrsltfile(ysd);
		
		// 만약 테이블이 있다면 drop
		rd.dropMLBResulttbl();
		rd.dropKBOResulttbl();
		
		rd.createTemporaryMLBresulttbl();
		rd.createTemporaryKBOresulttbl();
		
		rd.insertResultMLB();
		rd.insertResultKBO();
	}
}
