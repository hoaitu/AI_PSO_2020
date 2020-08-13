package models;

import java.util.Arrays;

public class ObjectIndexAssigned {
	private Assigned assigned;
	private int [] index;
	public ObjectIndexAssigned(Assigned assigned, int[] index) {
		super();
		this.assigned = assigned;
		this.index = index;
	}
	public Assigned getAssigned() {
		return assigned;
	}
	public void setAssigned(Assigned assigned) {
		this.assigned = assigned;
	}
	public int[] getIndex() {
		return index;
	}
	public void setIndex(int[] index) {
		this.index = index;
	}
	@Override
	public String toString() {
		return "ObjectIndexAssigned [assigned=" + assigned + ", index=" + Arrays.toString(index) + "]";
	}
	
	

}
