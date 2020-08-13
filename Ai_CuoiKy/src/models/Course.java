package models;

public class Course {
	private int id;
	private String name;
	private Department department;
	private int limiStudent;
	private boolean classify;

	public Course(int id, String name, Department department, int limiStudent, boolean classify) {
		super();
		this.id = id;
		this.name = name;
		this.department = department;
		this.limiStudent = limiStudent;
		this.classify = classify;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public int getLimiStudent() {
		return limiStudent;
	}

	public void setLimiStudent(int limiStudent) {
		this.limiStudent = limiStudent;
	}

	public boolean isClassify() {
		return classify;
	}

	public void setClassify(boolean classify) {
		this.classify = classify;
	}

	@Override
	public String toString() {
		return "id = " + id + ", name=" + name ;
	}

}