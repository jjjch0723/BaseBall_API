package com.game.baseball.api.jsonFileManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component // 객체 등록
public class getDay {
	
	// 포맷형식 관리
	public DateTimeFormatter dateFormater() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		return dtf;
	}
		
	// 오늘 날짜를 반환하는 메서드.
	// mlb 기록 삭제용
	public String getTodaydate() {
		LocalDate td = LocalDate.now();
		DateTimeFormatter dtf = dateFormater();
		return td.format(dtf);
	}
	
	// mlb는 내일의 날짜를 가져옴.
	// 자정 기준으로 들고오므로 자정이 되면 한국 날짜로 어제의 경기는 끝나있기 때문.
	// mlb에만 사용
	public String getTomorrowdate() {
		LocalDate tm = LocalDate.now().plusDays(1);
		DateTimeFormatter dtf = dateFormater();
		return tm.format(dtf);
	}
	
	// 어제 날짜를 반환하는 메서드
	// kbo 기록 삭제용
	public String getYesterdaydate() {
		LocalDate yd = LocalDate.now().minusDays(1);
		DateTimeFormatter dtf = dateFormater();
		return yd.format(dtf);
	}
}
