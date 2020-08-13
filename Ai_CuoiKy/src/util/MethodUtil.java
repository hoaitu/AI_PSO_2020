//package util;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
//import org.apache.poi.hpsf.Array;
//
//import models.Assigned;
//
//public class MethodUtil {
//	public static Assigned[][][] coppyArray3D(Assigned[][][] arrayy) {
//		Assigned[][][] newArr = new Assigned[arrayy.length][][];
//		for (int i = 0; i < newArr.length; i++) {
//			newArr[i] = new Assigned[arrayy[i].length][];
//			for (int j = 0; j < newArr[i].length; j++) {
//				newArr[i][j] = Arrays.copyOf(arrayy[i][j], arrayy[i][j].length);
//
//			}
//
//		}
//		return newArr;
//
//	}
//
//	// get min
//	public static int[] indexMinOfArray3D(int[][][] arr, int index) {
//		HashMap<Integer, int[]> list = new HashMap<Integer, int[]>();
//		list = getListMintoMax( arr);
//		int[] getindex = list.get(index);
//		return getindex;
//	}
//
//	// get hashmap
//	public static HashMap<Integer, int[]> getListMintoMax( int[][][] arr) {
//		HashMap<Integer, int[]> listmintomax = new HashMap<Integer, int[]>();
//		int count = 0;
//		List<int[]> list=new ArrayList<int[]>();
//		for (int i = 0; i < arr.length; i++) {
//			for (int j = 0; j < arr[i].length; j++) {
//				for (int k = 0; k < 4; k++) {
//					int[] array=new int[3];
//					array[0] = i;
//					array[1] = j;
//					array[2] = k;
//					list.add(array);
//				}
//			}
//		}
//        for (int i = 0 ; i < list.size() - 1; i++) {
//            for (int j = i + 1; j < list.size(); j++) {
//            	int[] a=list.get(i);
//            	int[] b=list.get(j);
//                if (arr[a[0]][a[1]][a[2]]>arr[b[0]][b[1]][b[2]]) {
//                    list.set(i, list.get(j));
//                    list.set(j, list.get(i));
//                }
//            }
//        }
////        List<int[]> clonelist=new ArrayList<int[]>(list);
////        for (int i = 0; i < clonelist.size(); i++) {
////			for (int j = 1; j < clonelist.size()-1; j++) {
////				if(clonelist.get(i)==clonelist.get(j)) {
////					list.remove(clonelist.get(j));
////				}
////			} 
////		}
//        for (int i = 0; i < list.size(); i++) {
//        	listmintomax.put(count++, list.get(i));
//		}
//		
//		return listmintomax;
//	}
//
//	public static void main(String args[]) {
//		// khoi tao hashMap
//		HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
//		// them cac phan tu vao hashMap
//		hashMap.put(3, "C++");
//		hashMap.put(1, "Java");
//		hashMap.put(4, "Python");
//		hashMap.put(2, "PHP");
//		// hien thi HashMap
//		int count = 0;
//		System.out.println(hashMap);
//		for (int i = 0; i < 10; i++) {
//			System.out.println(count++);
//
//		}
//	}
//}
package util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import models.Assigned;
import models.Room;
import repository.RepoRoom;

public class MethodUtil {

	public static Assigned[][][] coppyArray3D(Assigned[][][] arrayy) {
		Assigned[][][] newArr = new Assigned[arrayy.length][][];
		for (int i = 0; i < newArr.length; i++) {
			newArr[i] = new Assigned[arrayy[i].length][];
			for (int j = 0; j < newArr[i].length; j++) {
				newArr[i][j] = Arrays.copyOf(arrayy[i][j], arrayy[i][j].length);

			}

		}
		return newArr;

	}

	// get min
	public static int[] indexMinOfArray3D(double[][][] arr) {
		double min = 9999;
		int[] minindex = null;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				for (int j2 = 0; j2 < arr[i][j].length; j2++) {
					if (min > arr[i][j][j2] && arr[i][j][j2] != Integer.MAX_VALUE) { // && arr[i][j][j2] !=0
						min = arr[i][j][j2];
						minindex = new int[] { i, j, j2 };
					}

				}
			}

		}
		return minindex;
	}

	public static int countDifMaxInt(int[][][] arr) {
		int min = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				for (int j2 = 0; j2 < arr[i][j].length; j2++) {
					if (arr[i][j][j2] != Integer.MAX_VALUE) { // && arr[i][j][j2] !=0
						min++;
					}

				}
			}

		}
		return min;
	}

	// get hashmap
	public static HashMap<Integer, int[]> getListMintoMax(int[] minindex, int[][][] arr, int min) {
		HashMap<Integer, int[]> list = new HashMap<Integer, int[]>();
		int count = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				for (int j2 = 0; j2 < arr[i][j].length; j2++) {
					if (min > arr[i][j][j2]) {
						min = arr[i][j][j2];
						minindex[0] = i;
						minindex[1] = j;
						minindex[2] = j2;
					}

				}
			}

		}
		return list;
	}

	public static int[] listIndexMintoMax(int[][][] arr, int index) {
		int[] minindex = new int[3];
//		for (int i = 0; i < list.size() - 1; i++) {
//			for (int j = 1; j < list.size(); j++) {
//				int[] a = list.get(i);
//				int[] b = list.get(j);
//				if (arr[a[0]][a[1]][a[2]] < arr[b[0]][b[1]][b[2]]) {
//					list.set(i, b);
//					list.set(j, a);
//				}
//			}
//		}
		List<int[]> list = listint(arr);
		minindex = list.get(index);
		return minindex;
	}

	public static List<int[]> listint(int[][][] arr) {
		int[] minindex = new int[3];
		int min = 999999999;
		List<int[]> list = new ArrayList<int[]>();
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				for (int j2 = 0; j2 < arr[i][j].length; j2++) {
					int[] newInt = new int[3];
					if (min > arr[i][j][j2]) {
						min = arr[i][j][j2];
						minindex[0] = i;
						minindex[1] = j;
						minindex[2] = j2;
					}
					newInt[0] = i;
					newInt[1] = j;
					newInt[2] = j2;
					list.add(newInt);

				}
			}

		}
		list.add(minindex);
		return list;
	}

	public static double[][][] setMax(double[][][] scoreAss) {
		for (int q = 0; q < scoreAss.length; q++) {
			for (int v = 0; v < scoreAss[q].length; v++) {
				for (int v2 = 0; v2 < scoreAss[q][v].length; v2++) {
					scoreAss[q][v][v2] = Integer.MAX_VALUE;
				}

			}
		}
		return scoreAss;
	}

	

	

	public static String getNameRoom(int id) throws ClassNotFoundException, SQLException, IOException {
		List<Room> listroom = new ArrayList<Room>(RepoRoom.getAll().values());
		String name = listroom.get(id).getName();
		return name;
	}

	public static int getNameRoom2(String id) throws ClassNotFoundException, SQLException, IOException {
		List<Room> listroom = new ArrayList<Room>(RepoRoom.getAll().values());
		Collections.sort(listroom);
		for (int i = 0; i < listroom.size(); i++) {
			if (id.equals(listroom.get(i).getId())) {
				return i;
			}
		}
		return -1;
	}

	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {
		// khoi tao hashMap
		int[] a = { 1, 2, 3 };
	}
}
