package models;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import constant.CriteriaSwap;
import main.Utility;
import repository.RepoAssigned;
//import repository.RepoBusy;
import repository.RepoLecture;
import repository.RepoRoom;
import util.ExcelOut;
import util.MethodUtil;

public class TimeTable {

	private Assigned[][][] timeTable;
	private int fitness;
	private List<Assigned> listClassTime;
	private List<Lecturer> listLecturers;
	// private List<Busy> listBusy;
	private List<Room> rooms;
	private int velocity;

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public TimeTable(List<Assigned> listClassTime, List<Lecturer> lecturers,
			List<Room> romList/* , List<Busy> listBusy */) {
		this.listClassTime = listClassTime;
		Collections.sort(romList);
		this.rooms = romList;
		// this.listBusy = listBusy;
		this.listLecturers = lecturers;
		timeTable = new Assigned[7][romList.size()][4];

	}

	public void setTimeTable(Assigned[][][] timeTable) {
		this.timeTable = timeTable;
		this.refreshFiness();
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public void setListClassTime(List<Assigned> listClassTime) {
		this.listClassTime = listClassTime;
	}

	public void setListLecturers(List<Lecturer> listLecturers) {
		this.listLecturers = listLecturers;
	}

//	public void setListBusy(List<Busy> listBusy) {
//		this.listBusy = listBusy;
//	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public Assigned[][][] getTimeTable() {
		return timeTable;
	}

	public int getFitness() {
		return fitness;
	}

	public List<Assigned> getListClassTime() {
		return listClassTime;
	}

	public List<Lecturer> getListLecturers() {
		return listLecturers;
	}

//	public List<Busy> getListBusy() {
//		return listBusy;
//	}

	public List<Room> getRooms() {
		return rooms;
	}

	public boolean CheckErrorNewAssigned(Assigned assigned, int dayOfWeek, int room, int session) {
		return CheckLecturer(assigned.getLecturer(), dayOfWeek, session) && checkType(assigned.getClassify(), room)
				&& checkEmptySlot(dayOfWeek, room, session);
	}

	private boolean checkEmptySlot(int dayOfWeek, int room, int session) {
		// TODO Auto-generated method stub
		if(timeTable[dayOfWeek][room][session] != null) {
			return false;
		}
		else {
			return true;
		}
	}

	private boolean CheckLecturer(Lecturer lecturer, int dayOfWeek, int session) {
		// TODO Auto-generated method stub
		for (int i = 0; i < rooms.size(); i++) {
			if((timeTable[dayOfWeek][i][session] != null)) {
				Assigned ass = timeTable[dayOfWeek][i][session];
				if(ass.getLecturer().getId() == lecturer.getId()) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkType(String classify, int room) {
		// TODO Auto-generated method stub
		Room r = rooms.get(room);		
		return r.getStyleROoom().equals(classify);
	}
	
	public TimeTable() {
	}

	// check trùng gi�?
	public int checkErrorLecturer(Assigned[][][] timeTable) {
		int erro = 0;
		for (int o = 0; o < listLecturers.size(); o++) {
			Lecturer gv = listLecturers.get(o);
			// ngày
			for (int i = 0; i < 7; i++) {
				int countErrDay = 0;
				// phòng
				String seesion = "";
				for (int j = 0; j < rooms.size(); j++) {
					// ca
					for (int k = 0; k < 4; k++) {
						Assigned Clt = timeTable[i][j][k];
						if (Clt != null) {
							Lecturer lecturerCa = Clt.getLecturer();
							if (gv.getId() == lecturerCa.getId()) {
								if (seesion.contains(k + "") && seesion.length() >= 1) {
									countErrDay++;
								}
								seesion += k;
							}
						}
					}
				}
				// System.out.println("Thứ " + (i + 2) + " Lan " + countErrDay + " Gv " +
				// gv.getName());
				erro += countErrDay;

			}

		}
		return erro;
	}

	public boolean checkErroTypeRoom(Assigned arr[][][]) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < rooms.size(); j++) {
				Room room = rooms.get(j);
				for (int j2 = 0; j2 < 4; j2++) {
					if (arr[i][j][j2] != null) {
						Assigned ass = arr[i][j][j2];
						if (!ass.getClassify().equals(room.getStyleROoom())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public int checkErrorLecturer() {
		int erro = 0;
		for (int o = 0; o < listLecturers.size(); o++) {
			Lecturer gv = listLecturers.get(o);
			// ngày
			for (int i = 0; i < 7; i++) {
				int countErrDay = 0;
				// phòng
				String seesion = "";
				for (int j = 0; j < rooms.size(); j++) {
					// ca
					for (int k = 0; k < 4; k++) {
						Assigned Clt = timeTable[i][j][k];
						if (Clt != null) {
							Lecturer lecturerCa = Clt.getLecturer();
							if (gv.getId() == lecturerCa.getId()) {
								if (seesion.contains(k + "") && seesion.length() >= 1) {
									countErrDay++;
								}
								seesion += k;
							}
						}
					}
				}
				// System.out.println("Thứ " + (i + 2) + " Lan " + countErrDay + " Gv " +
				// gv.getName());
				erro += countErrDay;

			}

		}
		return erro;
	}

	// bat loi so sinh vien 1 phong va limit sinh vien cua mon hoc
	public int checkErrorLimit() {
		int error = 0;
		for (int i = 0; i < 7; i++) {
			int countErrDay = 0;
			// phòng
			for (int j = 0; j < rooms.size(); j++) {
				// ca
				for (int k = 0; k < 4; k++) {
					Assigned Clt = timeTable[i][j][k];
					if (Clt != null) {
//						Course course = Clt.getCourse();
						Room ro = rooms.get(j);
						if (ro.getLimit() < Clt.getLimit()) {
							countErrDay++;
						}
					}

				}
			}
			error += countErrDay;
//			System.out.println("Thứ " + (i + 2) + " Lan " + countErrDay + " Gv ");
		}
		return error;
	}

	public boolean checkHardBinding() {
		int errorLecturer = checkErrorLecturer();
		int errorLimitRoom = checkErrorLimit();
		if (errorLecturer == 0) // errorLecturer == errorLimitRoom &&
			return false;
		return true;
	}
	// Ràng buộc m�?m
	// Th�?i khóa biểu của một lớp hạn chế các ngày h�?c cả
	// sáng và chi�?u.
	//  Th�?i khóa biểu của một lớp các ca h�?c của một buổi
	// phải liên tục,
	// hạn chế ca trống xen giữa.
	//  Hạn chế xếp th�?i khóa biểu h�?c vào buổi tối hay thứ
	// bảy và chủ nhật.
	// Tính ñộ thích nghi dựa vào số buổi lên lớp của GV

	// check bận
	/**
	 * Function �?ộ_thích_nghi_LUA (Cathe) Begin Count = 0 {Biến ñếm số
	 * lần vi phạmràng buộc} For each phòng: Begin For each ngày, tiết
	 * h�?c: Begin lớp =Cathe [phòng, ngày, tiết] gv = Giảng_dạy (lớp)
	 * If (Gv_bận_gi�? (gv, ngày, tiết)) Then Count = Count + 1 End
	 */
//	private int checkBusyLecture() {
//		int erro = 0;
//		for (int o = 0; o < listBusy.size(); o++) {
//			Lecturer gv = listBusy.get(o).getLecturer();
//			if (gv != null) {
//
//				// ngày
//				for (int i = 0; i < 7; i++) {
//					int countErrDay = 0;
//					// phòng
//					for (int j = 0; j < rooms.size(); j++) {
//						// ca
//						for (int k = 0; k < 4; k++) {
//
//							Assigned Clt = timeTable[i][j][k];
//							if (Clt != null) {
//
//								Lecturer lecturerCa = Clt.getLecturer();
//								if (gv.getId().equalsIgnoreCase(lecturerCa.getId()) && listBusy.get(o).getDate() == i
//										&& listBusy.get(o).getSession() == k) {
//									countErrDay++;
////									System.out.println(
////											"Lich ban giao vien: " + lecturerCa.getName() + " ca:" + k + " thu: " + i);
//								}
//							}
//						}
//					}
////				if (countErrDay > 0)
////					countErrDay -= 1;
//					erro += countErrDay;
//
//				}
//
//			}
//		}
//		return erro;
//	}
//
//	private int checkBusyRoom() {
//		int erro = 0;
//		for (int o = 0; o < listBusy.size(); o++) {
//			Room room = listBusy.get(o).getRoom();
//			if (room != null) {
//
//				// ngày
//				for (int i = 0; i < 7; i++) {
//					int countErrDay = 0;
//					// phòng
//					for (int j = 0; j < rooms.size(); j++) {
//						// ca
//						for (int k = 0; k < 4; k++) {
//
//							Assigned Clt = timeTable[i][j][k];
//							if (Clt != null) {
//
//								if (room.getId().equalsIgnoreCase(rooms.get(j).getId())
//										&& listBusy.get(o).getDate() == i && listBusy.get(o).getSession() == k) {
//									countErrDay++;
////									System.out.println(
////											"Lich phong hoc ban: " + room.getName() + " ca:" + k + " thu: " + i);
//								}
//							}
//						}
//					}
////				if (countErrDay > 0)
////					countErrDay -= 1;
//					erro += countErrDay;
//
//				}
//
//			}
//		}
//		return erro;
//	}

	// giới hạn số ngày lên lớp >> đây là ràng buộc m�?m
	public int limitDayMeeting(int limit) {
		int erro = 0;
		for (int o = 0; o < listLecturers.size(); o++) {
			Lecturer gv = listLecturers.get(o);
			// ngày
			int countMeetings = 0;
			for (int i = 0; i < 7; i++) {
				// phòng
				room: for (int j = 0; j < rooms.size(); j++) {
					// ca
					for (int k = 0; k < 4; k++) {
						Assigned Clt = timeTable[i][j][k];
						if (Clt != null) {
							Lecturer lecturerCa = Clt.getLecturer();
							if (gv.getId() == lecturerCa.getId()) {
								countMeetings++;
								break room;
							}
						}
					}
				}

			}
//			System.out.println("GV: " + gv.getName() + ", Số ngày lên lớp: " + countMeetings);
			if (countMeetings > limit)
				erro += (countMeetings - limit);
		}
		return erro;

	}

	// gioi han 1 ngay giao vien chi day 2 ca ly thuyet
	public int limitDayTeaching(int limit) {
		int erro = 0;
		for (int o = 0; o < listLecturers.size(); o++) {
			Lecturer gv = listLecturers.get(o);
			// ngày
			for (int i = 0; i < 7; i++) {
				// phòng
				int sotietLTtrongngay = 0;
				int sotiettrongngay = 0;
				for (int j = 0; j < rooms.size(); j++) {
					// ca
					for (int k = 0; k < 4; k++) {
						Assigned Clt = timeTable[i][j][k];
						if (Clt != null) {
							Lecturer lecturerCa = Clt.getLecturer();
							if (gv.getId() == lecturerCa.getId()) {
								sotiettrongngay++;
							}
							if (Clt.getClassify().equals("LT") && gv.getId() == lecturerCa.getId()) {
								sotietLTtrongngay++;
							}
						}
					}
				}

				if (sotietLTtrongngay >= limit) {
//					System.out.println("Vuot qua so 2 tiet LT trong ngay GV: " + gv.getName() + ", Thu: " + (i + 2));
					erro++;
				}
				if (sotiettrongngay > 3) {
//					System.out.println("Vuot qua so 3 tiet trong ngay GV: " + gv.getName() + ", Thu: " + (i + 2));
//					erro++;
				}
				if (sotiettrongngay == 1) {
//					erro+=5;
				}
			}

		}
		return erro;
	}

	public int limitSeesionInDay() {
		int erro = 0;
		for (int o = 0; o < listLecturers.size(); o++) {
			Lecturer gv = listLecturers.get(o);
			// ngày
			for (int i = 0; i < 7; i++) {
				// phòng
				int sotiettrongngay = 0;
				for (int j = 0; j < rooms.size(); j++) {
					// ca
					for (int k = 0; k < 4; k++) {
						Assigned Clt = timeTable[i][j][k];
						if (Clt != null) {
							Lecturer lecturerCa = Clt.getLecturer();
							if (gv.getId() == lecturerCa.getId()) {
								sotiettrongngay++;
							}
						}
					}
				}
				if (sotiettrongngay > 3) {
					erro++;
				}
				if (sotiettrongngay == 1) {
					erro++;
				}
			}

		}
		return erro;

	}

	public int DoesNotCloseSession(Assigned[][][] arr) {
		int erro = 0;
		for (int o = 0; o < listLecturers.size(); o++) {
			Lecturer gv = listLecturers.get(o);
			// ngày
			for (int i = 0; i < 7; i++) {
				// phòng
				int count = 0;
				List<Integer> list = new ArrayList<Integer>();
				for (int j = 0; j < rooms.size(); j++) {
					// ca
					for (int k = 0; k < 4; k++) {
						Assigned Clt = arr[i][j][k];
						if (Clt != null) {
							Lecturer lecturerCa = Clt.getLecturer();
							if (gv.getId() == lecturerCa.getId() && count < 2) {
								count++;
								list.add(k);
							}
						}
					}
				}
				if (list.size() > 1) {
					if (list.get(0) == 0 || list.get(0) == 1) {
						if ((list.get(0) + 3) == list.get(1)) {
							erro++;
//							System.out.println("GV: "+gv.getName()+" Thu: "+(i+2));
						}
					}
				}
			}
		}
		return erro;

	}

	// giới hạn số phòng dạy trong ngày >> đây là ràng buộc m�?m
	private int limitRoomMeeting(int limit) {
		int erro = 0;
		for (int o = 0; o < listLecturers.size(); o++) {
			Lecturer gv = listLecturers.get(o);
			// ngày
			for (int i = 0; i < 7; i++) {
				// phòng
				int countMeetings = 0;
				for (int j = 0; j < rooms.size(); j++) {
					// ca
					session: for (int k = 0; k < 4; k++) {
						Assigned Clt = timeTable[i][j][k];
						if (Clt != null) {
							Lecturer lecturerCa = Clt.getLecturer();
							if (gv.getId() == lecturerCa.getId()) {
								countMeetings++;
								break session;
							}
						}
					}
				}

//				System.out.println("GV: " + gv.getName() + " Thứ " + (i + 2) + ", Số phòng phải dạy: " + countMeetings);
				if (countMeetings > limit)
					erro += (countMeetings - limit);
			}

		}
		return erro;

	}

	int countAss = 0;

	public void printTimeTable(Assigned[][][] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.println("Thứ : " + (i + 2));
			for (int j = 0; j < rooms.size(); j++) {
				for (int j2 = 0; j2 < 4; j2++) {
					if (arr[i][j][j2] != null) {
						countAss++;
						System.out.println("Phòng : " + rooms.get(j).getName() + " " + "Ca : " + (j2 + 1) + " "
								+ arr[i][j][j2] + "\t");
					}
				}
			}

		}

	}

	public void printTimeTable() {
		for (int i = 0; i < timeTable.length; i++) {
			System.out.println("Thứ : " + (i + 2));
			for (int j = 0; j < rooms.size(); j++) {
				for (int j2 = 0; j2 < 4; j2++) {
					if (timeTable[i][j][j2] != null) {
						countAss++;
						System.out.println("Phòng : " + rooms.get(j).getName() + " " + "Ca : " + (j2 + 1) + " "
								+ timeTable[i][j][j2] + "\t");
					}
				}
			}

		}

	}

	private int checkSaturdayMeeting() {
		int countAss = 0;
		for (int j = 0; j < rooms.size(); j++) {
			for (int k = 0; k < 4; k++) {
				Assigned Clt = timeTable[5][j][k];
				if (Clt != null) {
					countAss++;
				}
			}

		}
//		System.out.println(countAss);
		return countAss;

	}

	// Swap 2 Assigned in timetable;
	public void swapTowAssigned(int[] indexSoure, int[] indexDestination) {
		Assigned temp = this.timeTable[indexDestination[0]][indexDestination[1]][indexDestination[2]];
		this.timeTable[indexDestination[0]][indexDestination[1]][indexDestination[2]] = this.timeTable[indexSoure[0]][indexSoure[1]][indexSoure[2]];
		this.timeTable[indexSoure[0]][indexSoure[1]][indexSoure[2]] = temp;
		this.refreshFiness();

	}

	public void swapAssAccordingCriteriaAlgSA(CriteriaSwap criteria, int[] indexSoure) {
		switch (criteria) {
		case DAY: {
			int index = Utility.rand.nextInt(6);
			Assigned temp = this.timeTable[indexSoure[0]][indexSoure[1]][indexSoure[2]];
			this.timeTable[indexSoure[0]][indexSoure[1]][indexSoure[2]] = this.timeTable[index][indexSoure[1]][indexSoure[2]];
			this.timeTable[index][indexSoure[1]][indexSoure[2]] = temp;

			break;
		}
		case ROM: {
			int indexRoom = indexSoure[1];
			int index = indexRoom > 4 ? Utility.rand.nextInt(rooms.size() - 4) + 4 : Utility.rand.nextInt(4);
			Assigned temp = this.timeTable[indexSoure[0]][indexSoure[1]][indexSoure[2]];
			this.timeTable[indexSoure[0]][indexSoure[1]][indexSoure[2]] = this.timeTable[indexSoure[0]][index][indexSoure[2]];
			this.timeTable[indexSoure[0]][index][indexSoure[2]] = temp;
			break;
		}
		case SESSION: {
			int index = Utility.rand.nextInt(4);
			Assigned temp = this.timeTable[indexSoure[0]][indexSoure[1]][indexSoure[2]];
			this.timeTable[indexSoure[0]][indexSoure[1]][indexSoure[2]] = this.timeTable[indexSoure[0]][indexSoure[1]][index];
			this.timeTable[indexSoure[0]][indexSoure[1]][index] = temp;
			break;
		}
		default:
			break;
		}
		this.refreshFiness();

	}

	public boolean checkErrorClazztify() {
		for (int i = 0; i < 7; i++) {
			int countErrDay = 0;
			// phòng
			for (int j = 0; j < rooms.size(); j++) {
				// ca
				for (int k = 0; k < 4; k++) {
					Assigned Clt = timeTable[i][j][k];
					if (Clt != null) {
						if ((Clt.getClassify().equalsIgnoreCase("TH") && j >= 4)
								|| (Clt.getClassify().equalsIgnoreCase("LT") && j < 4)) {
							return true;

//							System.out.println(
//									"Lich phong hoc ban: " + room.getName() + " ca:" + k + " thu: " + i);
						}
					}
				}
			}
		}
		return false;
	}

	// finess
	public int calculatorFitness() {
		int errorLecturer = checkErrorLecturer();
		int errorLimitRoom = checkErrorLimit() * 10;
		int errorDaymMeetingOfLecgture = limitDayMeeting(3) * 20;
//		int errorBusyLecture = checkBusyLecture() * 20;
//		int errorBusyRoom = checkBusyRoom() * 20;
		int errorLimitRoomMeetingOfLecture = limitRoomMeeting(2) * 20;
		int errorDayTeaching = limitDayTeaching(3) * 10;
		int errorMettingSaturday = checkSaturdayMeeting() * 20;
		int optimize = (int) (optimizeTimeTable() * 0.1);
		int errorNotCloseSession = DoesNotCloseSession(timeTable) * 30;
		int errorLimitSesstion = limitSeesionInDay() * 100;
		return errorNotCloseSession + errorDayTeaching /* + errorBusyLecture + errorBusyRoom */ + errorLecturer
				+ errorLimitRoom + errorLimitRoomMeetingOfLecture + errorDaymMeetingOfLecgture + errorMettingSaturday
				+ errorLimitSesstion;
	}

	public void refreshFiness() {
		this.fitness = calculatorFitness();
	}

	// giảm điểm khi giảng viên có tiết li�?n k�?
	public int optimizeTimeTable() {
		int score = 0;
		for (int o = 0; o < listLecturers.size(); o++) {
			Lecturer gv = listLecturers.get(o);
			// ngày
			for (int i = 0; i < 7; i++) {
				// phòng
				int scoreSameDay = 0;
				for (int j = 0; j < rooms.size(); j++) {
					int scoreSameRoom = 0;
					// ca
					int sessNext = -2;
					session: for (int k = 0; k < 4; k++) {
						Assigned Clt = timeTable[i][j][k];
						if (Clt != null) {
							Lecturer lecturerCa = Clt.getLecturer();
							if (gv.getId() == lecturerCa.getId()) {
								if (++sessNext == k)
									score += 5;
								sessNext = k;
								scoreSameRoom += 3;
								scoreSameDay++;
//								System.out.println(gv.getName() + " Phòng " + rooms.get(j).getName() + " Thứ " + i+2);
							}
//							if(scoreSameRoom >1) {
//								System.out.println("Lớn hơn 1");
//								System.out.println(gv.getName() + " Phòng " + rooms.get(j).getName() + " Thứ " + i+2);
////								System.out.println(gv.getName() + " Phòng " + rooms.get(j).getName() + " Thứ " + i+2);
//
//							}
						}
					}
					if (scoreSameRoom > 3)
//					System.out.println(scoreSameRoom);
						score += scoreSameRoom;
				}
				if (scoreSameDay > 1) {
					score += scoreSameDay;
//					System.out.println(gv.getName() + " Thứ " + i+2 + " score" + scoreSameDay);

				}
				if (scoreSameDay == 1)
					score -= 5;

//				System.out.println("GV: " + gv.getName() + " Thứ " + (i + 2) + ", Số phòng phải dạy: " + countMeetings);
			}

		}
		return score;
	}
	// có cả sức chứa tối đa và bận

	/**
	 * Function �?ộ_thích_nghi_RTS (Cathe) Begin Count = 0 {Biến ñếm số
	 * lần vi phạm ràng buộc} For each phòng: Begin For each ngày, tiết
	 * h�?c: Begin lớp = Cathe[phòng, ngày, tiết] If (Số_SV (lớp) >
	 * Số_chỗ_ngồi (phòng)) Then Count = Count+1 End End Return 1/ (Count *
	 * Tr�?ng số) End
	 */
	private int checkErrorRoom() {
		return 0;

	}

	@Override
	public TimeTable clone() throws CloneNotSupportedException {
		try {
			return (TimeTable) super.clone();

		} catch (Exception e) {
			TimeTable clone = new TimeTable(this.listClassTime, this.listLecturers, this.rooms/* , this.listBusy */);
			clone.setFitness(this.fitness);
			Assigned[][][] cloneArr = new Assigned[7][this.rooms.size()][4];
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < this.rooms.size(); j++) {
					for (int j2 = 0; j2 < 4; j2++) {
						cloneArr[i][j][j2] = this.timeTable[i][j][j2];
					}
				}
			}
			clone.setTimeTable(cloneArr);
			return clone;
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
	
	}

	public void set(Assigned assigned, int dayWeek, int room, int session) {
		// TODO Auto-generated method stub
		timeTable[dayWeek][room][session] = assigned;
	}

}
