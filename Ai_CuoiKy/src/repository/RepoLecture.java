package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import models.Lecturer;

public class RepoLecture {
	private static Connection connect;

	private final static String getAll = "SELECT * from lecture";
	private static Map<Integer, Lecturer> data = new HashMap<>();

	public static Map<Integer, Lecturer> getAll() throws ClassNotFoundException, SQLException {
		if (!data.isEmpty())
			return data;
//		connect = GetConnection.getConnection();
//		PreparedStatement statement = connect.prepareStatement(getAll);
//		statement.execute();
//		ResultSet res = statement.getResultSet();
//		while (res.next()) {
//			String id = res.getString("id");
//			String name = res.getString("name");
//			String department = res.getString("department");
//			Lecturer a=new Lecturer(id, name, RepoDepartment.getDepartmentById(department), null);
//			data.put(Integer.parseInt(id), a);
//		}
//		return data;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("data/lecture.csv"));

			String textInALine= br.readLine();
			int count =0;
			while ((textInALine = br.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(textInALine, ",");
				String id = token.nextToken();
				String idlc = token.nextToken();
				String name = token.nextToken();
				String email = token.nextToken();
				String birthday = token.nextToken();
				String department = token.nextToken();

				Lecturer a = new Lecturer(id, name, null, null);
				data.put(Integer.parseInt(id), a);
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
