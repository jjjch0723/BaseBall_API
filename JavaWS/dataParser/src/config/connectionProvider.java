package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class connectionProvider {
	public static Connection getConnection() {
		Connection con = null;
		try {
			String url = "jdbc:mysql://192.168.0.78/baseball?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
			String id = "studyuser";
			String pw = "1111";
			String driver = "com.mysql.jdbc.Driver";
			Class.forName(driver);
			con = DriverManager.getConnection(url, id, pw);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}
