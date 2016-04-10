package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.collections.functors.WhileClosure;


public class MysqlMethod {
	public static final String url = "jdbc:mysql://demo.51xuanshi.com:3306/xswy_new";
	public static final String driver = "com.mysql.jdbc.Driver";
	public static final String name = "root";
	public static final String password = "xilieduNew"; 
	
	public Connection conn = null;
	public Statement st = null;
	
	public boolean MysqlMethod(String sql, String ac) throws SQLException{
		boolean flag = false;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, name, password);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
//				System.out.println(rs.getString(1));
				if (rs.getString(1).equals(ac)) {
					flag = true;
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		
		return flag;
	}
	
	public static void main(String[] args) {
		String sql = "select uid from sys_account where phone = 13580475555";
		try {
			System.out.println(new MysqlMethod().MysqlMethod(sql, "123456"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
