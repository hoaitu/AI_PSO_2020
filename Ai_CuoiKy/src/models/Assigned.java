package models;

public class Assigned {
	private int id;
	private Course course;
	private Lecturer lecturer;
	private Class clazz;
	private String classify;
	private int numberWeek;
	private String startWeek;
	private int limit;
	private int semester;
	private int year;
	//private int numsession;// so tiet hoc

	public Assigned(int id, Course course, Lecturer lecturer, Class clazz, String classify, int numberWeek,
			String startWeek, int limit, int semester, int year, int numsession) {
		super();
		this.id = id;
		this.course = course;
		this.lecturer = lecturer;
		this.clazz = clazz;
		this.classify = classify;
		this.numberWeek = numberWeek;
		this.startWeek = startWeek;
		this.limit = limit;
		this.semester = semester;
		this.year = year;
	//	this.numsession = numsession;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Lecturer getLecturer() {
		return lecturer;
	}

	public void setLecturer(Lecturer lecturer) {
		this.lecturer = lecturer;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public int getNumberWeek() {
		return numberWeek;
	}

	public void setNumberWeek(int numberWeek) {
		this.numberWeek = numberWeek;
	}

	public String getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(String startWeek) {
		this.startWeek = startWeek;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	/*
	 * public int getNumsession() { return numsession; }
	 * 
	 * public void setNumsession(int numsession) { this.numsession = numsession; }
	 */

	@Override
	public String toString() {
		return course + " " + " limit= " + limit + " " + lecturer + "  classify=" + classify;

	}
}
