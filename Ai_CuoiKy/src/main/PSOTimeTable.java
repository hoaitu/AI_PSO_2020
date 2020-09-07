package main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import models.Assigned;
//import models.Busy;
//import models.Busy;
import models.Lecturer;
import models.ObjectIndexAssigned;
import models.Room;
import models.TimeTable;
import repository.RepoAssigned;
import repository.RepoLecture;
import repository.RepoRoom;
import util.ExcelOut;
import util.MethodUtil;

public class PSOTimeTable {
	private TimeTable[] timeTables;
	private int quantityTimetable;
	private int iteration;
	private List<Assigned> listAssigned;
	private List<Room> romList;
//	private List<Busy> listBusy;
	private List<Lecturer> listLecture;
	private int W, C1, C2;
	private int N, sizeRoom;
	private TimeTable best;
	Random random = new Random();

	public PSOTimeTable() {
	}

	public PSOTimeTable(int quantityTimetable, int iteration) throws ClassNotFoundException, SQLException, IOException {
		this.quantityTimetable = quantityTimetable;
		this.timeTables = new TimeTable[quantityTimetable];
		this.listAssigned = RepoAssigned.getAll();
		this.romList = new ArrayList<Room>(RepoRoom.getAll().values());
		this.listLecture = new ArrayList<Lecturer>(RepoLecture.getAll().values());
		this.iteration = iteration;
		this.sizeRoom = romList.size();
		this.N = listAssigned.size();
		this.W = 1;
		this.C1 = 1;
		this.C2 = 1;
	}
	// Tạo 1 tkb ngẫu nhiên
	private TimeTable generateTimeTable(List<Assigned> listClassTime, List<Room> rooms, List<Lecturer> lecturers) {
		// Assigned[][][] timeTable = new Assigned[7][rooms.size()][4];
		TimeTable result = new TimeTable(listClassTime, lecturers, rooms);
		
		Assigned assigned = null;
		for (int i = 0; i < listClassTime.size(); i++) {
			assigned = listClassTime.get(i);
			boolean fit = false;
			int dayWeek = -1;
			int room = -1;
			int session = -1;
			while (fit == false) {
				dayWeek = random.nextInt(6);
				room = random.nextInt(sizeRoom);
				session = random.nextInt(4);
				fit = result.CheckErrorNewAssigned(assigned, dayWeek, room, session);
			}
			result.set(assigned, dayWeek, room, session);
		}
		return result;
	}

//	private TimeTable localSearchTimeTable(List<Assigned> listClassTime, List<Room> rooms, List<Lecturer> lecturers)
//			throws IOException {
//		Assigned[][][] timeTable = new Assigned[7][rooms.size()][4]; // tao ra [][][] moi null
//		TimeTable result = new TimeTable(listClassTime, lecturers, rooms); // chay tao timetable
//		Random random = new Random();
//		List<Assigned> cloneListAss = new ArrayList<Assigned>(listClassTime);// copy list assigned
//		Assigned first = cloneListAss.get(0);
//		int room = first.getClassify().equalsIgnoreCase("TH") ? random.nextInt(4)
//				: random.nextInt(rooms.size() - 4) + 4;
//		timeTable[0][room][0] = first;
//		cloneListAss.remove(first);
//
//		for (int i = 0; i < listAssigned.size() - 1; i++) {
//			TreeMap<Double, ObjectIndexAssigned> listMap = new TreeMap<Double, ObjectIndexAssigned>();
//			for (int o = 0; o < cloneListAss.size(); o++) {
//				double[][][] scoreAss = new double[6][rooms.size()][4]; // tao mang int[][][];
//				scoreAss = MethodUtil.setMax(scoreAss);
//				Assigned classTime = cloneListAss.get(o);
//				Assigned[][][] assSocre = MethodUtil.coppyArray3D(timeTable);
//				for (int j = 0; j < assSocre.length - 1; j++) {
//					for (int j2 = 0; j2 < assSocre[j].length; j2++) {
//						for (int k = 0; k < assSocre[j][j2].length; k++) {
//							if (timeTable[j][j2][k] == null) {
//								assSocre[j][j2][k] = classTime;
//								if (result.checkErrorLecturer(assSocre) == 0
//										&& result.checkErroTypeRoom(assSocre) == false) {
//									result.setTimeTable(assSocre);
//									scoreAss[j][j2][k] = result.getFitness();
//								}
//								assSocre[j][j2][k] = null;
//							}
//						}
//					}
//				}
//				int[] min = MethodUtil.indexMinOfArray3D(scoreAss);
//				ObjectIndexAssigned v = new ObjectIndexAssigned(classTime, min);
//				if (!listMap.containsKey(scoreAss[min[0]][min[1]][min[2]]) || random.nextInt(5) == 2) {
//					listMap.put(scoreAss[min[0]][min[1]][min[2]], v);
//
//				}
//			}
//
//			ObjectIndexAssigned a = listMap.get(listMap.firstKey());
//			int[] index = a.getIndex();
//			Assigned b = a.getAssigned();
//			timeTable[index[0]][index[1]][index[2]] = b;
//			cloneListAss.remove(b);
//			System.out.println("Assinged " + i + " /" + listAssigned.size());
//			result.printTimeTable(timeTable);
//
//		}
//		result.setTimeTable(timeTable);
//		System.out.println(result.getFitness());
////		MethodUtil.writeJavaToMySQL(timeTable);
//		System.out.println(timeTable);
//		ExcelOut.writeByExcel(result);
//		return result;
//	}

	private void initalizationPopulation() throws ClassNotFoundException, SQLException, IOException {
		
		/*
		 * java.io.File fileLocal = new java.io.File("data/localsearch.csv"); if
		 * (fileLocal.exists()) { TimeTable first = new TimeTable(listAssigned,
		 * listLecture, romList);
		 * first.setTimeTable(ExcelOut.readByExcel("data/localsearch.csv"));
		 * timeTables[0] = first; System.out.println("Run File Localsearch"); } else
		 * timeTables[0] = localSearchTimeTable(listAssigned, romList, listLecture);
		 */
		 
		for (int i = 0; i < timeTables.length; i++) {
			timeTables[i] = generateTimeTable(listAssigned, romList, listLecture);
//			System.out.println(timeTables[i].checkHardBinding());
		}
	}

	// Chay thuat toan o day nay
	public TimeTable PSOAlgorithm()
			throws ClassNotFoundException, SQLException, IOException, CloneNotSupportedException {
		TimeTable res = null;
		//Khởi tạo quần thể
		initalizationPopulation();
		//Tìm phần từ có fitness nhỏ nhất làm GBest
		best = cloneTable(GetFitestTimetable());
		double GbestFitnness = best.getFitness();
		//Mảng chứa Pbest của từng cá thể
		TimeTable[] particlesBest = new TimeTable[quantityTimetable];
		// Khởi tạo Pbest = vị trí hiện thời của cá thể
		for (int i = 0; i < particlesBest.length; i++) {
			particlesBest[i] = cloneTable(timeTables[i]);  
		}
		int i = 1;
		Random rand = new Random();
		//Set vận tốc ban đầu là Random cho từng cá thể
		for (int j = 0; j < timeTables.length; j++) {
			timeTables[j].setVelocity(rand.nextInt(10));
		}
		System.out.println("startfit: " + GbestFitnness);
		int bestIndex = -1;
		// Chạy theo số vòng lặp
		while (i < iteration) {
			System.out.println("iteration: " + i);
			// Di chuyển từng cá thể trong quần thể
			for (int j = 0; j < timeTables.length; j++) {
				if(j==bestIndex) {
					swapWithItself(best, (100/best.getFitness())*100);
					if(best.getFitness() < timeTables[j].getFitness()) {
						timeTables[j].setTimeTable(best.getTimeTable());
						particlesBest[j].setTimeTable(best.getTimeTable());
						continue;
					}
				}
//				System.out.println("particle  "+ j);
				TimeTable particle = timeTables[j];
				TimeTable next = null;
				// Tính toán độ ảnh hưởng của Pbest lên vận tốc mới của cá thể
				List<int[]> pmi = calculateInfluence(particle, particlesBest[j]);
				// Tính toán độ ảnh hưởng của Gbest lên vận tốc mới của cá thể
				List<int[]> si = calculateInfluence(particle, this.best);
				double selfswap = rand.nextDouble()*timeTables[j].getVelocity(); 
				double pBestswap =  (0.2 + (rand.nextDouble())) * (pmi.size());
				double gBestswap = (1 + (rand.nextDouble())) * (si.size());
				// tính vần tốc mới
				int newVelocity = (int) (selfswap + pBestswap + gBestswap);
				//int newVelocity = (int) (this.W * timeTables[j].getVelocity() + rand.nextInt(1));
//				System.out.println("new Velocity: " + newVelocity);
//				 (next == null || next.getFitness() > timeTables[j].getFitness() + 5000) {
					

					
					// vị trí mới của cá thể
					// Hàm moving forward trả về 1 time table là vị trí mới của cá thể
					// nhận vào vận tốc là số cặp tráo đổi với GBest
					next = movingForward(cloneTable(particle), (int) pBestswap, particlesBest[j]);
					next.refreshFiness();
					next = movingForward(next, (int)gBestswap, best);
					next.refreshFiness();
					swapWithItself(next, (int) selfswap);
					next.refreshFiness();
					System.out.println("nextFiness: " + next.getFitness());
	
					//Nếu vị trí mới của cá thể có fitness nhỏ hơn vị trí hiện tại
					if (next.getFitness() < particlesBest[j].getFitness()) {
						System.out.println("reset Pbest");
					//	timeTables[j].setTimeTable(next.getTimeTable());
						// set pBest = vị trí mới
						particlesBest[j].setTimeTable(next.getTimeTable());
					}
					// Nếu vị trí mới có finess nhỏ hơn Gbest
					if (next.getFitness() < best.getFitness()) {
						// set lại Gbest = vị trí mới
						System.out.println("reset Gbest");
						bestIndex = j;
						best.setTimeTable(next.getTimeTable());
					}
					if(next.getFitness() < particlesBest[j].getFitness() + 100) {
						timeTables[j].setTimeTable(next.getTimeTable());
					}
					// set lại vận tốc cho timetable
					timeTables[j].setVelocity(newVelocity);
			}
			//best = GetFitestTimetable();
		//	swapWithItself(best, 2);
			System.out.println("iter  "+ i +" fit "+ best.getFitness());
			i++;
		}
		
		res = cloneTable(best);
		return res;
	}
	// Lấy vị trí của 1 assign xác định trong 1 TKB xác định
	// Trả về mảng int có 3 giá trị index của : ngày học, phòng học, ca học
	public static int[] getIndexOfAssigned(Assigned ass, TimeTable timetable) {
		Assigned[][][] assi = timetable.getTimeTable();
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < timetable.getRooms().size(); j++) {
				for (int k = 0; k < 4; k++) {
					if (assi[i][j][k] != null)
						if (assi[i][j][k].getId() == ass.getId()) {
							int[] c = { i, j, k };
							return c;
						}
				}
			}
		}
		return null;
	}
	private TimeTable movingForward(TimeTable cloneTable, int newVelocity, TimeTable goal) {
		// TODO Auto-generated method stub
		// Assigned[][][] cloneAssigned = cloneTable.getTimeTable();
		// mảng 3 chiều chứa các assign của Gbest
		Assigned[][][] bestAssigned = goal.getTimeTable();
		Random rand = new Random();
		int i =0;
		while(i < newVelocity) {
			i++;
			//Thực hiện random ngày học, phòng học ca học
			int dayOfWeek = rand.nextInt(7);
			int room = rand.nextInt(romList.size());
			int k = rand.nextInt(4);
			// Lấy ra 1 assign có vị trí dựa vào ngày, phòng ,ca từ Gbest
			Assigned randomAssignInBest = bestAssigned[dayOfWeek][room][k];
			// Thực hiện lấy lại cho đến khi chọn được assign khác null
				while(randomAssignInBest == null) {
					dayOfWeek = rand.nextInt(7);
					room = rand.nextInt(romList.size());
					k = rand.nextInt(4);
					randomAssignInBest = bestAssigned[dayOfWeek][room][k];
				}
				// Lấy ra index của assign mới chọn đc từ vị trí của cá thể cần tráo đổi
				int[] indexOfAssignInParticle = getIndexOfAssigned(randomAssignInBest, cloneTable);
				// ghi lại vị trí mới đc random
				int[] particleIndex = new int[] { dayOfWeek, room, k };
				if(indexOfAssignInParticle == particleIndex) {
					i--;
					continue;
				}
				// tráo đổi 2 vị trí này trong cá thể cần đc tráo đổi
				cloneTable.swapTowAssigned(indexOfAssignInParticle, particleIndex);
				// Kiểm tra lại các ràng buộc cứng, nếu vi phạm thì swap ngược lại
				if (cloneTable.checkErrorClazztify() || cloneTable.checkHardBinding()) {
					cloneTable.swapTowAssigned(indexOfAssignInParticle, particleIndex);
					i--;
				}
		}
		return cloneTable;
	}
	public void swapWithItself(TimeTable tb, int w) {
		for (int i = 0; i < w; i++) {
			int fit = tb.getFitness();
			int index1 = random.nextInt(listAssigned.size());
		//	int index2 = random.nextInt(listAssigned.size());
			int dayOfWeek = random.nextInt(7);
			int room = random.nextInt(romList.size());
			int k = random.nextInt(4);
//			while(tb.getTimeTable()[dayOfWeek][room][k].getId() != listAssigned.get(index1).getId()) {
//				//index2 = random.nextInt(listAssigned.size());
//				dayOfWeek = random.nextInt(5) + 2;
//				room = random.nextInt(romList.size());
//				k = random.nextInt(4);
//			}
			int[] indexOfAssign1 = getIndexOfAssigned(listAssigned.get(index1), tb);
	//		int[] indexOfAssign2 = getIndexOfAssigned(listAssigned.get(index2), tb);
			int[] indexOfAssign2 = new int[] {dayOfWeek, room, k};
			tb.swapTowAssigned(indexOfAssign1, indexOfAssign2);
			int count = 0;
			while(tb.checkErrorClazztify() || tb.checkHardBinding() || tb.calculatorFitness() > fit) {
				if(count > w/4) { break;}
				else {
				i--;
				count++;
				tb.swapTowAssigned(indexOfAssign1, indexOfAssign2);
				}
			}
		}
		return;
		
	}
//	public void swapGbest() {
//		be
//	}
	// de tinh khac biet giua 2 timetable, do anh huong len van toc
	private List<int[]> calculateInfluence(TimeTable particle, TimeTable goal) {
		// TODO Auto-generated method stub
		//int infuence = 0;
		List<int[]> list = new ArrayList<int[]>();
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < romList.size(); j++) {
				for (int j2 = 0; j2 < 4; j2++) {
					if(goal.getTimeTable()[i][j][j2] != null) {
						if(particle.getTimeTable()[i][j][j2] != goal.getTimeTable()[i][j][j2]) {
							list.add(new int[] {i, j, j2});
						}
					}
				}
			}
		}
		return list;
	}

	// lay ra time table co fitness tot nhat
	private TimeTable GetFitestTimetable() {
		// TODO Auto-generated method stub
		TimeTable fittest = timeTables[0];
		fittest.refreshFiness();
		for (int i = 1; i < timeTables.length; i++) {
			if (timeTables[i].getFitness() < fittest.getFitness()) {
				fittest = timeTables[i];
				fittest.refreshFiness();
				System.out.println("fittest " + i);
			}
		}
		return fittest;
	}
	public TimeTable cloneTable(TimeTable tb) {
		TimeTable cloneTable = new TimeTable();
		cloneTable.setListClassTime(tb.getListClassTime());
		cloneTable.setListLecturers(tb.getListLecturers());
		cloneTable.setRooms(tb.getRooms());
		cloneTable.setTimeTable(MethodUtil.coppyArray3D(tb.getTimeTable()));
		return cloneTable;
	}

	public static void main(String[] args)
			throws ClassNotFoundException, SQLException, IOException, CloneNotSupportedException {
		PSOTimeTable algo = new PSOTimeTable(40, 40);
//		TimeTable ex = algo.generateTimeTable(algo.listAssigned, algo.romList, algo.listLecture);
//		TimeTable ex = algo.localSearchTimeTable(algo.listAssigned, algo.romList, algo.listLecture);
//		System.out.println(ex.getFitness());
		long milis = System.currentTimeMillis();
		TimeTable ex = algo.PSOAlgorithm();
		System.out.println("fitness = " + ex.getFitness());

		ExcelOut.writeByExcel(ex);
		
		
		 TimeTable first = new TimeTable(algo.listAssigned, algo.listLecture, algo.romList);
		 first.setTimeTable(ExcelOut.readByExcel("result.csv"));
		 System.out.println(first.checkErrorClazztify() || first.checkHardBinding());
		 System.out.println("fitness = " + first.getFitness());
		 Long milis2 = System.currentTimeMillis();
		 System.out.println("time= " + (milis2 - milis)/1000);

	}

}
