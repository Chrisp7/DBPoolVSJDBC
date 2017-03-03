package com.chirsp7.DatabasePoolTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;

public class DatabasePoolTest extends Thread{
	public static BasicDataSource ds = null;
	public final static String DRIVER_NAME = "com.mysql.jdbc.Driver";
	public final static String USER_NAME = "cp";
	public final static String PASSWORD = "123456";
	public final static String DB_URL = "jdbc:mysql://localhost/cloud_study";

	// 数据库连接池的创建
	public static void dbpoolInit() {
		ds = new BasicDataSource();
		ds.setUrl(DB_URL);
		ds.setDriverClassName(DRIVER_NAME);
		ds.setUsername(USER_NAME);
		ds.setPassword(PASSWORD);
		ds.setMaxActive(2);
	}

	public static void dbPoolTest() {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// 之前这里是要注册一个数据库驱动程序用class.forname，现在这里不需要了。直接使用数据库连接池获取数据库物理连接。
			connection = ds.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select * from users");
			while (rs.next()) {
				System.out.println(rs.getString("username"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
	}

	public static void JDBCtest() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {

			Class.forName(DRIVER_NAME).newInstance();
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);

			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from users");

			while (rs.next()) {
				System.out.println(rs.getString("username"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

	}
	public void run(){
		long start=System.currentTimeMillis();
		while (System.currentTimeMillis()-start<10000) {
			//JDBCtest();
			dbPoolTest();
		}
	}
	
	public static void main(String[] args) {
		dbpoolInit();
		for (int i = 0; i < 10; i++) {
			new DatabasePoolTest().start();
		}
	}

}
