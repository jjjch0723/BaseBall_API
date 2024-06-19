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

public class resultDAO {
	Connection con = null;
	PreparedStatement psmt = null;
	Resultset rs = null;
	readJSONImpl rj = new readJSONImpl();
	getDay gd = new getDay();
	JDBCutil jdbCutil = new JDBCutil();
	
	public String createTemporaryMLBresulttbl() {
		String sql = "CREATE TABLE baseball.TBL_MLBRES_TTP ("
				+ "    SEQ INT PRIMARY KEY AUTO_INCREMENT,"
				+ "    DATE TEXT,"
				+ "    WINTEAM TEXT,"
				+ "    LOSETEAM TEXT,"
				+ "    WINSCORE TEXT,"
				+ "    LOSESCORE TEXT"
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

	public String createTemporaryKBOresulttbl() {
		String sql = "CREATE TABLE baseball.TBL_KBORES_TTP ("
				+ "    SEQ INT PRIMARY KEY AUTO_INCREMENT,"
				+ "    DATE TEXT,"
				+ "    WINTEAM TEXT,"
				+ "    LOSETEAM TEXT,"
				+ "    WINSCORE TEXT,"
				+ "    LOSESCORE TEXT"
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
	
	public String dropMLBResulttbl() {
	    String sql = "DROP TABLE IF EXISTS baseball.TBL_MLBRES_TTP;";
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
	
	public String dropKBOResulttbl() {
	    String sql = "DROP TABLE IF EXISTS baseball.TBL_KBORES_TTP;";
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

	public String insertResultMLB() {
	    String sql = "INSERT INTO TBL_MLBRES_TTP (DATE, WINTEAM, LOSETEAM, WINSCORE, LOSESCORE) VALUES (?, ?, ?, ?, ?)";
	    List<HashMap<String, Object>> glist = rj.readMLBrsltfile(gd.getTodaydate());
	    Connection con = null;
	    PreparedStatement psmt = null;
	    
	    try {
	        con = connectionProvider.getConnection();
	        psmt = con.prepareStatement(sql);
	        
	        for (HashMap<String, Object> game : glist) {
	            String date = (String) game.get("DATE");
	            String winTeam = (String) game.get("WINTEAM");
	            String loseTeam = (String) game.get("LOSETEAM");
	            String winScore = (String) game.get("WINSCORE");
	            String loseScore = (String) game.get("LOSESCORE");
	            
	            psmt.setString(1, date);
	            psmt.setString(2, winTeam);
	            psmt.setString(3, loseTeam);
	            psmt.setString(4, winScore);
	            psmt.setString(5, loseScore);
	            
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
	
	public String insertResultKBO() {
	    String sql = "INSERT INTO TBL_KBORES_TTP (DATE, WINTEAM, LOSETEAM, WINSCORE, LOSESCORE) VALUES (?, ?, ?, ?, ?)";
	    List<HashMap<String, Object>> glist = rj.readKBOrsltfile(gd.getYesterdaydate());
	    Connection con = null;
	    PreparedStatement psmt = null;
	    // System.out.println("DAO glist" + glist);
	    try {
	        con = connectionProvider.getConnection();
	        psmt = con.prepareStatement(sql);
	        
	        for (HashMap<String, Object> game : glist) {
	            String date = (String) game.get("DATE");
	            String winTeam = (String) game.get("WINTEAM");
	            String loseTeam = (String) game.get("LOSETEAM");
	            String winScore = (String) game.get("WINSCORE");
	            String loseScore = (String) game.get("LOSESCORE");

	            psmt.setString(1, date);
	            psmt.setString(2, winTeam);
	            psmt.setString(3, loseTeam);
	            psmt.setString(4, winScore);
	            psmt.setString(5, loseScore);
	            
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
