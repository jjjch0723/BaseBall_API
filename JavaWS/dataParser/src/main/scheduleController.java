package main;

import config.Paths;
import dao.scheduleDAO;
import jsonFileManager.getDay;
import jsonFileManager.readManager.readJSONImpl;
import runPy.runPyfileImpl;

public class scheduleController {

    public void sCont() {
        getDay gd = new getDay();
        readJSONImpl rji = new readJSONImpl();
        runPyfileImpl rpi = new runPyfileImpl();
        scheduleDAO sd = new scheduleDAO();

        // 오늘 날짜 KBO에 사용
        String tdy = gd.getTodaydate();
        // 내일 날짜 MLB에 사용
        String tmr = gd.getTomorrowdate();
        //System.out.println("날짜" + tdy + tmr);

        String mlbPyPath = Paths.MLB_PY_PATH;
        String kboPyPath = Paths.KBO_PY_PATH;

        // 파이썬 파일 실행부분
        rpi.pyRunner(mlbPyPath);
        rpi.pyRunner(kboPyPath);

        // json파일 생성을 위해 2초간 대기
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // 경기 일정을 읽는 부분
        rji.readMLBfile(tmr);
        rji.readKBOfile(tdy);

        // 만약 테이블이 있다면 테이블 drop
        sd.dropMLBtbl();
        sd.dropKBOtbl();

        // 경기 일정용 임시테이블 생성
        sd.createTemporaryMLBtbl();
        sd.createTemporaryKBOtbl();

        // 경기일정 db에 insert
        sd.insertTdyMLB();
        sd.insertTdyKBO();
    }
}
