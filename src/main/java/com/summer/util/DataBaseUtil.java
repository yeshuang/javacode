package com.summer.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseUtil {

	public Connection getConnection(String url, String user, String password) throws Exception {
		// 加载驱动
		Class.forName("com.mysql.jdbc.Driver");
		// 使用driverManager获取数据库链接
		// return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lcma", "root", "iflytek");
		return DriverManager.getConnection(url, user, password);
	}

	public int insert(Connection conn, String sql) {
		int rows = 0;
		Statement stat = null;
		try {
			// 设置不自动提交
			conn.setAutoCommit(false);
			// 使用Connection创建一个Statement
			stat = conn.createStatement();

			// 执行SQL（insert/update/delete）语句，返回数据库影响的行数
			rows = stat.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rows;
	}

}
