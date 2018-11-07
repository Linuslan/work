package com.linuslan.oa.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtil {
	public static Connection getConnection() {
		Connection conn = null;
		try {
			InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("config/jdbc.properties");
			Properties props = new Properties();
			props.load(is);
			String url = null;
			String userName = null;
			String password = null;
			String driver = null;
			if(CodeUtil.isNotEmpty(props.getProperty("jdbc.url"))) {
				url = props.getProperty("jdbc.url");
			}
			if(CodeUtil.isNotEmpty(props.getProperty("jdbc.driver"))) {
				driver = props.getProperty("jdbc.driver");
			}
			if(CodeUtil.isNotEmpty(props.getProperty("jdbc.username"))) {
				userName = props.getProperty("jdbc.username");
			}
			if(CodeUtil.isNotEmpty(props.getProperty("jdbc.password"))) {
				password = props.getProperty("jdbc.password");
			}
			if(CodeUtil.isNotEmpty(url) && CodeUtil.isNotEmpty(userName)
					&& CodeUtil.isNotEmpty(password) && CodeUtil.isNotEmpty(driver)) {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, userName, password);
			}
		} catch(Exception ex) {
			
		}
		return conn;
	}
	
	/**
	 * 关闭数据库连接
	 * @param conn
	 */
	public static void closeConn(Connection conn) {
		if(null != conn) {
			try {
				//if(!conn.isClosed()) {
					conn.close();
				//}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭PreparedStatement
	 * @param ps
	 */
	public static void closePreparedStatement(PreparedStatement ps) {
		if(null != ps) {
			try {
				//if(!ps.isClosed()) {
					ps.close();
				//}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭ResultSet
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		if(null != rs) {
			try {
				//if(!rs.isClosed()) {
					rs.close();
				//}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭Statement和ResultSet
	 * @param ps
	 * @param rs
	 */
	public static void closeStatementAndResultSet(PreparedStatement ps, ResultSet rs) {
		JdbcUtil.closeResultSet(rs);
		JdbcUtil.closePreparedStatement(ps);
	}
	
	/**
	 * 关闭所有连接
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static void closeDB(Connection conn, PreparedStatement ps, ResultSet rs) {
		JdbcUtil.closeStatementAndResultSet(ps, rs);
		JdbcUtil.closeConn(conn);
	}
}
