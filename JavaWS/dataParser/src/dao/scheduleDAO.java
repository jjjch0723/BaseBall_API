package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.mysql.cj.protocol.Resultset;

import config.JDBCutil;
import config.connectionProvider;
import jsonFileManager.getDay;
import jsonFileManager.readManager.readJSONImpl;

public class scheduleDAO {
	Connection con = null;
	PreparedStatement psmt = null;
	Resultset rs = null;
	readJSONImpl rj = new readJSONImpl();
	getDay gd = new getDay();
	JDBCutil jdbCutil = new JDBCutil();
	
	public String createTemporaryMLBtbl() {
		String sql = "CREATE TABLE baseball.TBL_MLBSCHEDULE_TTP ("
				+ "    SEQ INT PRIMARY KEY AUTO_INCREMENT,"
				+ "    TEAM1 TEXT,"
				+ "    TEAM2 TEXT,"
				+ "    TDY TEXT"
				+ ");";
		try {
			con = connectionProvider.getConnection();
			psmt = con.prepareStatement(sql);
			psmt.executeUpdate();
			return "temporary mlbtbl created success";
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		} finally {
			JDBCutil.close(con);
			JDBCutil.close(psmt);
			
		}
	}

	public String createTemporaryKBOtbl() {
		String sql = "CREATE TABLE baseball.TBL_KBOSCHEDULE_TTP ("
				+ "    SEQ INT PRIMARY KEY AUTO_INCREMENT,"
				+ "    TEAM1 TEXT,"
				+ "    TEAM2 TEXT,"
				+ "    TDY TEXT"
				+ ");";
		try {
			con = connectionProvider.getConnection();
			psmt = con.prepareStatement(sql);
			psmt.executeUpdate();
			return "temporary mlbtbl created success";
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		} finally {
			JDBCutil.close(con);
			JDBCutil.close(psmt);
		}
	}
	
	public String dropMLBtbl() {
	    String sql = "DROP TABLE IF EXISTS baseball.TBL_MLBSCHEDULE_TTP;";
	    try {
	        con = connectionProvider.getConnection();
	        psmt = con.prepareStatement(sql);
	        psmt.executeUpdate();
	        return "mlbtbl dropped successfully";
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "error dropping temporary mlbtbl";
	    } finally {
	        JDBCutil.close(con);
	        JDBCutil.close(psmt);
	    }
	}
	
	public String dropKBOtbl() {
	    String sql = "DROP TABLE IF EXISTS baseball.TBL_KBOSCHEDULE_TTP;";
	    try {
	        con = connectionProvider.getConnection();
	        psmt = con.prepareStatement(sql);
	        psmt.executeUpdate();
	        return "kbotbl dropped successfully";
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "error dropping temporary mlbtbl";
	    } finally {
	        JDBCutil.close(con);
	        JDBCutil.close(psmt);
	    }
	}
	
	public String insertTdyMLB() {
	    String sql = "INSERT INTO TBL_MLBSCHEDULE_TTP (TEAM1, TEAM2, TDY) VALUES (?, ?, ?)";
	    List<HashMap<String, Object>> glist = rj.readMLBfile(gd.getTomorrowdate());
	    Connection con = null;
	    PreparedStatement psmt = null;
	    
	    try {
	        con = connectionProvider.getConnection();
	        psmt = con.prepareStatement(sql);
	        
	        for (HashMap<String, Object> game : glist) {
	            String team1 = (String) game.get("TEAM1");
	            String team2 = (String) game.get("TEAM2");
	            String tdy = (String) game.get("TDY");
	            
	            psmt.setString(1, team1);
	            psmt.setString(2, team2);
	            psmt.setString(3, tdy);
	            
	            psmt.executeUpdate();
	        }
	        
	        return "Data inserted successfully.";
	    } catch (SQLException e) {
			e.printStackTrace();
			return "error";
		} finally {
			JDBCutil.close(con);
			JDBCutil.close(psmt);
		}
	}
	
	public String insertTdyKBO() {
	    String sql = "INSERT INTO TBL_KBOSCHEDULE_TTP (TEAM1, TEAM2, TDY) VALUES (?, ?, ?)";
	    List<HashMap<String, Object>> glist = rj.readKBOfile(gd.getTodaydate());
	    Connection con = null;
	    PreparedStatement psmt = null;
	    
	    try {
	        con = connectionProvider.getConnection();
	        psmt = con.prepareStatement(sql);
	        
	        for (HashMap<String, Object> game : glist) {
	            String team1 = (String) game.get("TEAM1");
	            String team2 = (String) game.get("TEAM2");
	            String tdy = (String) game.get("TDY");
	            
	            psmt.setString(1, team1);
	            psmt.setString(2, team2);
	            psmt.setString(3, tdy);
	            
	            psmt.executeUpdate();
	        }
	        
	        return "Data inserted successfully.";
	    } catch (SQLException e) {
			e.printStackTrace();
			return "error";
		} finally {
			JDBCutil.close(con);
			JDBCutil.close(psmt);
		}
	}
}
