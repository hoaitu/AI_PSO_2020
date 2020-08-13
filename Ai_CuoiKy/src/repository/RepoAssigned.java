package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import models.Assigned;
import models.Course;
import models.Lecturer;

public class RepoAssigned {
//	private static Connection connect;
//	private final static String getAll = "SELECT * from assigned";
	

	public static List<Assigned> getAll() throws ClassNotFoundException, SQLException, IOException {
		List<Assigned> data = new ArrayList<Assigned>();
		if (!data.isEmpty())
			return data;
//		connect = GetConnection.getConnection();
//		PreparedStatement statement = connect.prepareStatement(getAll);
//		statement.execute();
//		ResultSet res = statement.getResultSet();
//		while (res.next()) {
//			int id = res.getInt("id");
//			int idLec = res.getInt("id_Lecture");
//			int idCor = res.getInt("id_Course");
//			String startweek = res.getString("datestart");
//			int numsess = res.getInt("sotiet");
//			int limit = res.getInt("limit");
//			StringTokenizer classify = new StringTokenizer(res.getString("species"));
//			while (classify.hasMoreTokens()) {
//				Assigned assigned = new Assigned(id,getCourse(idCor), getCLecturer(idLec), null, classify.nextToken(), 0,
//						startweek, limit, 0, 0, 0);
//				data.add(assigned);
//
//			}
//		}
//		return data;
		BufferedReader br = null;

        try {   
            br = new BufferedReader(new FileReader("data/assigned.csv"));       
            String textInALine;
            int count=0;
            while ((textInALine = br.readLine()) != null) {
            	StringTokenizer token=new StringTokenizer(textInALine, ",");
            	int id = Integer.parseInt(token.nextToken());
            	int id_lec = Integer.parseInt(token.nextToken());
            	int id_course = Integer.parseInt(token.nextToken());
            	int limit = Integer.parseInt(token.nextToken());
            	String clazz=token.nextToken();
            	String group=token.nextToken();
            	String classify=token.nextToken();
    			Assigned ass=new Assigned(id, getCourse(id_course), getCLecturer(id_lec), null, classify, 0, "", limit, 0, 0, 0);
                data.add(ass);
//                System.out.println("Count: "+count++ + "  " +ass.toString());
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

	public static Assigned getAss(int id) throws ClassNotFoundException, SQLException, IOException {
		List<Assigned> list = getAll();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == id) {
				return 	list.get(i);
			}
		}
		return null;
	}

	public static Course getCourse(int id) throws ClassNotFoundException, SQLException {
		RepoCourse repocourse = new RepoCourse();
		Map<Integer, Course> map = repocourse.getAll();
		Course course = map.get(id);
		return course;
	}

	public static Lecturer getCLecturer(int id) throws ClassNotFoundException, SQLException {
		RepoLecture repolec = new RepoLecture();
		Map<Integer, Lecturer> map = repolec.getAll();
		Lecturer lecture = map.get(id);
		return lecture;
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		List<Assigned> list=getAll();
		for (Assigned assigned : list) {
			System.out.println(assigned.toString());
		}
	}
}
