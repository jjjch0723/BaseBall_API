package config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCutil {
	public static void close(Connection con) {
		if(con != null) {
			try {
				con.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void close(PreparedStatement psmt) {
		if(psmt != null) {
			try {
				psmt.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void close(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
