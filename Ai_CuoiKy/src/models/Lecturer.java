package models;

import java.util.GregorianCalendar;

public class Lecturer {
	private String id, name;
	private Department department;
	private GregorianCalendar birthDay;
	private String email;

	public Lecturer(String id, String name, Department department, GregorianCalendar birthDay) {
		super();
		this.id = id;
		this.name = name;
		this.department = department;
		this.birthDay = birthDay;
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

	public Department getDepartment() {
		return department;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public GregorianCalendar getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(GregorianCalendar birthDay) {
		this.birthDay = birthDay;
	}

	@Override
	public String toString() {
		return "Lecturer [id=" + id + ", name=" + name +  "]";
	}
	
}
