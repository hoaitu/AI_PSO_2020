package models;

import java.util.Comparator;
import java.util.List;

public class Room implements Comparable<Room> {
	private String id, name;
	private String styleROoom;
	private int limit;
//	private Building building;
	private List<Department> department;

	public Room(String id, String name, String styleROoom, int limit,
			/* Building building, */ List<Department> department) {
		super();
		this.id = id;
		this.name = name;
		this.styleROoom = styleROoom;
		this.limit = limit;
//		this.building = building;
		this.department = department;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStyleROoom() {
		return styleROoom;
	}

	public void setStyleROoom(String styleROoom) {
		this.styleROoom = styleROoom;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

//	public Building getBuilding() {
//		return building;
//	}
//
//	public void setBuilding(Building building) {
//		this.building = building;
//	}

	public List<Department> getDepartment() {
		return department;
	}

	public void setDepartment(List<Department> department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", name=" + name + ", styleROoom=" + styleROoom + ", limit=" + limit
				+ ", department=" + department + "]";
	}

	@Override
	public int compareTo(Room arg0) {
		return arg0.getStyleROoom().compareTo(this.getStyleROoom());
	}

}
