package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import models.Department;

public class RepoDepartment {
	private static Connection connect;
	private final static String getAll = "SELECT * from department";
	private static Map<String, Department> data = new HashMap<>();

//	public static Map<String, Department> getAll() throws ClassNotFoundException, SQLException {
//		if (!data.isEmpty())
//			return data;
//		connect = GetConnection.getConnection();
//		PreparedStatement statement = connect.prepareStatement(getAll);
//		statement.execute();
//		ResultSet res = statement.getResultSet();
//		while (res.next()) {
//			String id = res.getString("name");
//			String name = res.getString("description");
//			Department a = new Department(id, name);
//			data.put(id, a);
//		}
//		return data;
//	}
//	public static Department getDepartmentById (String id) throws ClassNotFoundException, SQLException {
//		if(data.isEmpty())
//			getAll();
//		return data.get(id);
//		
//	}
//
//	public static void main(String[] args) throws ClassNotFoundException, SQLException {
//		RepoDepartment a = new RepoDepartment();
//		System.out.println(a.getAll());
//	}

}
