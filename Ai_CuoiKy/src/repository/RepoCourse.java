package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import models.Course;

public class RepoCourse {
	private static Connection connect;

	private final static String getAll = "SELECT * from course";
	private static Map<Integer, Course> data = new HashMap<>();

	public static Map<Integer, Course> getAll() throws ClassNotFoundException, SQLException {
		if (!data.isEmpty())
			return data;
//		connect = GetConnection.getConnection();
//		PreparedStatement statement = connect.prepareStatement(getAll);
//		statement.execute();
//		ResultSet res = statement.getResultSet();
//		while (res.next()) {
//			int id = res.getInt("id");
//			String name = res.getString("name");
//			String department = res.getString("department");
//			int limit = res.getInt("limit");
//			Course a = new Course(id, name, RepoDepartment.getDepartmentById(department), limit, true);
//			data.put(id, a);
//		}
//		return data;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("data/course.csv"));
			String textInALine=br.readLine();
			int count =0;
			while ((textInALine = br.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(textInALine, ",");
				int id = Integer.parseInt(token.nextToken());
				String name = token.nextToken();
				int numlession = Integer.parseInt(token.nextToken());
				int numcredit = Integer.parseInt(token.nextToken());
				int limit = Integer.parseInt(token.nextToken());
				String department = token.nextToken();

				Course a = new Course(id, name, null, limit, true);
				data.put(id, a);
//				System.out.println("Count: "+count++ + "  " +a.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;

	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		getAll();
	}
}
