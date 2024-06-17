package main;

import config.Paths;
import dao.scheduleDAO;
import jsonFileManager.getDay;
import jsonFileManager.readManager.readJSONImpl;
import runPy.runPyfileImpl;

public class controller {

	public void cont() {
		getDay gd = new getDay();
		readJSONImpl rji = new readJSONImpl();
		runPyfileImpl rpi = new runPyfileImpl();
		scheduleDAO sd = new scheduleDAO();
		
		String tdy = gd.getTodaydate();
		String tmr = gd.getTomorrowdate();
		System.out.println("날짜" + tdy + tmr);

		String mlbPyPath = Paths.MLB_PY_PATH;
		String kboPyPath = Paths.KBO_PY_PATH;
		
		rpi.pyRunner(mlbPyPath);
		rpi.pyRunner(kboPyPath);
		
		rji.readMLBfile(tmr);
		rji.readKBOfile(tdy);
		
		sd.dropMLBtbl();
		sd.dropKBOtbl();
		
		sd.createTemporaryMLBtbl();
		sd.createTemporaryKBOtbl();

		sd.insertTdyMLB();
		sd.insertTdyKBO();
	}
}
