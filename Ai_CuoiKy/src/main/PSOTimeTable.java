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
	private double W, C1, C2;
	private int N, sizeRoom;
	private TimeTable best;

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
		this.C1 = 2.0;
		this.C2 = 2.0;
	}

	private TimeTable generateTimeTable(List<Assigned> listClassTime, List<Room> rooms, List<Lecturer> lecturers) {
	//	Assigned[][][] timeTable = new Assigned[7][rooms.size()][4];
		TimeTable result = new TimeTable(listClassTime, lecturers, rooms);
		Random random = new Random();
		Assigned assigned = null;
		System.out.println(listClassTime.size());
		for (int i = 0; i < listClassTime.size(); i++) {
			assigned = listClassTime.get(i);
			System.out.println(assigned.getId());
			boolean fit = false;
			int dayWeek = -1;
			int room = -1;
			int session = -1;
			while (fit == false) {
				dayWeek = random.nextInt(6);
				room = random.nextInt(sizeRoom);
				session = random.nextInt(4);
				fit = result.CheckErrorNewAssigned(assigned, dayWeek, room, session);
				System.out.println(fit);
			}
			result.set(assigned, dayWeek, room, session);
		}
		return result;
	}

	private TimeTable localSearchTimeTable(List<Assigned> listClassTime, List<Room> rooms, List<Lecturer> lecturers)
			throws IOException {
		Assigned[][][] timeTable = new Assigned[7][rooms.size()][4]; // tao ra [][][] moi null
		TimeTable result = new TimeTable(listClassTime, lecturers, rooms); // chay tao timetable
		Random random = new Random();
		List<Assigned> cloneListAss = new ArrayList<Assigned>(listClassTime);// copy list assigned
		Assigned first = cloneListAss.get(0);
		int room = first.getClassify().equalsIgnoreCase("TH") ? random.nextInt(4)
				: random.nextInt(rooms.size() - 4) + 4;
		timeTable[0][room][0] = first;
		cloneListAss.remove(first);

		for (int i = 0; i < listAssigned.size() - 1; i++) {
			TreeMap<Double, ObjectIndexAssigned> listMap = new TreeMap<Double, ObjectIndexAssigned>();
			for (int o = 0; o < cloneListAss.size(); o++) {
				double[][][] scoreAss = new double[6][rooms.size()][4]; // tao mang int[][][];
				scoreAss = MethodUtil.setMax(scoreAss);
				Assigned classTime = cloneListAss.get(o);
				Assigned[][][] assSocre = MethodUtil.coppyArray3D(timeTable);
				for (int j = 0; j < assSocre.length - 1; j++) {
					for (int j2 = 0; j2 < assSocre[j].length; j2++) {
						for (int k = 0; k < assSocre[j][j2].length; k++) {
							if (timeTable[j][j2][k] == null) {
								assSocre[j][j2][k] = classTime;
								if (result.checkErrorLecturer(assSocre) == 0
										&& result.checkErroTypeRoom(assSocre) == false) {
									result.setTimeTable(assSocre);
									scoreAss[j][j2][k] = result.getFitness();
								}
								assSocre[j][j2][k] = null;
							}
						}
					}
				}
				int[] min = MethodUtil.indexMinOfArray3D(scoreAss);
				ObjectIndexAssigned v = new ObjectIndexAssigned(classTime, min);
				if (!listMap.containsKey(scoreAss[min[0]][min[1]][min[2]]) || random.nextInt(5) == 2) {
					listMap.put(scoreAss[min[0]][min[1]][min[2]], v);

				}

			}

			ObjectIndexAssigned a = listMap.get(listMap.firstKey());
			int[] index = a.getIndex();
			Assigned b = a.getAssigned();
			timeTable[index[0]][index[1]][index[2]] = b;
			cloneListAss.remove(b);
			System.out.println("Assinged " + i + " /" + listAssigned.size());
			result.printTimeTable(timeTable);

		}
		result.setTimeTable(timeTable);
		System.out.println(result.getFitness());
//		MethodUtil.writeJavaToMySQL(timeTable);
		System.out.println(timeTable);
		ExcelOut.writeByExcel(result);
		return result;
	}

	private void initalizationPopulation() throws ClassNotFoundException, SQLException, IOException {
		java.io.File fileLocal = new java.io.File("data/localsearch.csv");
		if (fileLocal.exists()) {
			TimeTable first = new TimeTable(listAssigned, listLecture, romList);
			first.setTimeTable(ExcelOut.readByExcel("data/localsearch.csv"));
			timeTables[0] = first;
			System.out.println("Run File Localsearch");
		} else
			timeTables[0] = localSearchTimeTable(listAssigned, romList, listLecture);
		for (int i = 1; i < timeTables.length; i++) {
			timeTables[i] = generateTimeTable(listAssigned, romList, listLecture);
//			System.out.println(timeTables[i].checkHardBinding());
		}
	}

	// Chay thuat toan o day nay
	public TimeTable PSOAlgorithm()
			throws ClassNotFoundException, SQLException, IOException, CloneNotSupportedException {
		TimeTable res = null;
		initalizationPopulation();
		this.best = GetFitestTimetable();
		double Gbest = this.best.calculatorFitness();
		TimeTable[] particlesBest = new TimeTable[quantityTimetable];
		particlesBest = Arrays.copyOf(timeTables, quantityTimetable);
		int i = 0;
		Random rand = new Random();
		for (int j = 0; j < timeTables.length; j++) {
			timeTables[j].setVelocity(rand.nextInt(10));
		}
		System.out.println(iteration);
		TimeTable next = null;
		while (i < iteration) {
			for (int j = 0; j < timeTables.length; j++) {
				TimeTable particle = timeTables[j];
				TimeTable cloneTable = timeTables[j].clone();
				
				int pmi = calculateInfluence(particle, particlesBest[j]);
				int si = calculateInfluence(particle, this.best);
				int newVelocity = (int) (this.W * timeTables[j].getVelocity() + this.C1 * pmi + this.C2 * si);
				System.out.println(newVelocity);
				while (next == null || next.getFitness() > timeTables[j].getFitness()) {
					cloneTable = particle.clone();
					next = movingForward(cloneTable, newVelocity);
					if (next.getFitness() < timeTables[j].getFitness()) {
						timeTables[j] = next;
						particlesBest[j] = next;
					}
				}
				timeTables[j].setVelocity(newVelocity);
			}
			best = GetFitestTimetable();
			i++;
		}
		res = best;
		return res;
	}

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

	private TimeTable movingForward(TimeTable cloneTable, int newVelocity) {
		// TODO Auto-generated method stub
	//	Assigned[][][] cloneAssigned = cloneTable.getTimeTable();
		Assigned[][][] bestAssigned = best.getTimeTable();
		Random rand = new Random();
		System.out.println(cloneTable.getFitness());
		for (int i = 0; i < newVelocity; i++) {
			int dayOfWeek = rand.nextInt(5) + 2;
			int room = rand.nextInt(romList.size());
			int k = rand.nextInt(4);
			Assigned randomAssignInBest = bestAssigned[dayOfWeek][room][k];
			int[] indexOfAssignInParticle = getIndexOfAssigned(randomAssignInBest, cloneTable);
			int[] particleIndex = new int[] { dayOfWeek, room, k };
			System.out.println(indexOfAssignInParticle);
			System.out.println("parti" + particleIndex.toString());
			cloneTable.swapTowAssigned(indexOfAssignInParticle, particleIndex);
			if (cloneTable.checkErrorClazztify() == true || cloneTable.checkHardBinding() == true) {
				cloneTable.swapTowAssigned(indexOfAssignInParticle, particleIndex);
				i--;
			}

		}
		return cloneTable;
	}

	// de tinh khac biet giua 2 timetable, do anh huong len van toc
	private int calculateInfluence(TimeTable particle, TimeTable goal) {
		// TODO Auto-generated method stub
		int influence = goal.getFitness() - particle.getFitness();
		return influence;
	}

	// lay ra time table co fitness tot nhat
	private TimeTable GetFitestTimetable() {
		// TODO Auto-generated method stub
		TimeTable fittest = timeTables[0];
		for (int i = 1; i < timeTables.length; i++) {
			if (timeTables[i].getFitness() < fittest.getFitness())
				fittest = timeTables[i];
		}
		return fittest;
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, CloneNotSupportedException {
		PSOTimeTable algo = new PSOTimeTable(50, 10);
//		TimeTable ex = algo.generateTimeTable(algo.listAssigned, algo.romList, algo.listLecture);
//		TimeTable ex = algo.localSearchTimeTable(algo.listAssigned, algo.romList, algo.listLecture);
//		System.out.println(ex.calculatorFitness());

		TimeTable ex = algo.PSOAlgorithm();

		ExcelOut.ResultToExcel(ex);
	}

}
