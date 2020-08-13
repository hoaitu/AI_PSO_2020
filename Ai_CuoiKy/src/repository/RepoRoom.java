package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

//import models.Building;
import models.Room;

public class RepoRoom {
//	private static Connection connect;
//	private final static String getAll = "SELECT * from room";
	private static Map<String, Room> data = new HashMap<>();
	private static int id2;

	public static Map<String, Room> getAll() throws ClassNotFoundException, SQLException, IOException {
		if (!data.isEmpty())
			return data;
//		connect = GetConnection.getConnection();
//		PreparedStatement statement = connect.prepareStatement(getAll);
//		statement.execute();
//		ResultSet res = statement.getResultSet();
//		while (res.next()) {
////			String id = res.getString("id");
//			String name = res.getString("name");
//			int limit = res.getInt("limit");
//			String style = res.getString("species");
//			int builing = res.getInt("id_building");
//			int isactive = res.getInt("isActive");
//			if (isactive == 1) {
//				Room a = new Room(name, name, style, limit, getBuilding(builing), null);
//				data.put(name, a);
//			}
//		}
//		return data;
		BufferedReader br = null;

        try {   
            br = new BufferedReader(new FileReader("data/room.csv"));       
            String textInALine= br.readLine();
            int count=0;
            while ((textInALine = br.readLine()) != null) {
            	StringTokenizer token=new StringTokenizer(textInALine, ",");
            	String name = token.nextToken();
    			int limit = Integer.parseInt(token.nextToken());
    			String style = token.nextToken();
    			int builing = Integer.parseInt(token.nextToken());
    			int isactive =Integer.parseInt(token.nextToken());
    			if (isactive == 1) {
					Room a = new Room(name, name, style, limit, /* getBuilding(builing), */ null);
    				data.put(name, a);
//    				System.out.println("Count: "+count++ + "  " +a.toString());
    			}
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

//	public static Building getBuilding(int id) throws ClassNotFoundException, SQLException {
//		RepoBuilding repo = new RepoBuilding();
//		Map<String, Building> map = RepoBuilding.getAll();
//		Building build = map.get(id);
//		return build;
//	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		getAll();

	}

}
