package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import models.Assigned;
//import models.Busy;
import models.Lecturer;
import models.Room;
import models.TimeTable;
import repository.RepoAssigned;
//import repository.RepoBusy;
import repository.RepoLecture;
import repository.RepoRoom;

public class ExcelOut {
	private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
		HSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 15);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		return style;
	}

	public static void ResultToExcel(TimeTable timeTable) throws IOException {
		Assigned[][][] assign = timeTable.getTimeTable();
		HSSFWorkbook workBook = new HSSFWorkbook();
		List<Lecturer> listlt = timeTable.getListLecturers();

		for (int i = 0; i < listlt.size(); i++) {
			Lecturer lt = listlt.get(i);
			HSSFSheet sheet = workBook.createSheet(lt.getName());
			HSSFCellStyle style = createStyleForTitle(workBook);
			HSSFCell cell;
			HSSFRow row;
			row = sheet.createRow(0);
			cell = row.createCell(0, CellType.STRING);
			cell.setCellValue("Tên Môn Học");
			cell.setCellStyle(style);

			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue("Thứ");
			cell.setCellStyle(style);

			cell = row.createCell(2, CellType.STRING);
			cell.setCellValue("Phòng học");
			cell.setCellStyle(style);

			cell = row.createCell(3, CellType.STRING);
			cell.setCellValue("Ca");
			cell.setCellStyle(style);
			cell = row.createCell(4, CellType.STRING);
			cell.setCellValue("Loại");
			cell.setCellStyle(style);
			int count = 1;
			for (int j = 0; j < 7; j++) {
				for (int j2 = 0; j2 < timeTable.getRooms().size(); j2++) {
					for (int k = 0; k < 4; k++) {
						if (assign[j][j2][k] != null) {
							if (assign[j][j2][k].getLecturer().equals(lt)) {
								row = sheet.createRow(count++);
								cell = row.createCell(0, CellType.STRING);
								cell.setCellValue(assign[j][j2][k].getCourse().getName());
								cell = row.createCell(1, CellType.STRING);
								cell.setCellValue(j + 2);
								cell = row.createCell(2, CellType.STRING);
								cell.setCellValue(timeTable.getRooms().get(j2).getName());
								cell = row.createCell(3, CellType.STRING);
								cell.setCellValue(k);
								cell = row.createCell(4, CellType.STRING);
								cell.setCellValue(assign[j][j2][k].getClassify());
							}
						}
					}
				}
			}
		}

		File file = new File("result.csv");
		FileOutputStream outfile = new FileOutputStream(file);
		workBook.write(outfile);
		System.out.println("Created file:");

	}

	public static void ResultToExcel2(TimeTable timeTable) throws IOException {
		Assigned[][][] assign = timeTable.getTimeTable();
		HSSFWorkbook workBook = new HSSFWorkbook();

		HSSFSheet sheet = workBook.createSheet("TimeTable");
		HSSFCellStyle style = createStyleForTitle(workBook);
		HSSFCell cell;
		HSSFRow row;
		row = sheet.createRow(0);

		cell = row.createCell(0, CellType.STRING);
		cell.setCellValue("Giáº£ng viÃªn");
		cell.setCellStyle(style);

		cell = row.createCell(1, CellType.STRING);
		cell.setCellValue("TÃªn mÃ´n há»�c");
		cell.setCellStyle(style);

		cell = row.createCell(2, CellType.STRING);
		cell.setCellValue("Thá»©");
		cell.setCellStyle(style);

		cell = row.createCell(3, CellType.STRING);
		cell.setCellValue("PhÃ²ng há»�c");
		cell.setCellStyle(style);

		cell = row.createCell(4, CellType.STRING);
		cell.setCellValue("Ca");
		cell.setCellStyle(style);

		cell = row.createCell(5, CellType.STRING);
		cell.setCellValue("Loáº¡i");
		cell.setCellStyle(style);
		int count = 1;
		for (int j = 0; j < 7; j++) {
			for (int j2 = 0; j2 < timeTable.getRooms().size(); j2++) {
				for (int k = 0; k < 4; k++) {
					if (assign[j][j2][k] != null) {
						row = sheet.createRow(count++);

						cell = row.createCell(0, CellType.STRING);
						cell.setCellValue(assign[j][j2][k].getLecturer().getName());
						cell = row.createCell(1, CellType.STRING);
						cell.setCellValue(assign[j][j2][k].getCourse().getName());
						cell = row.createCell(2, CellType.STRING);
						cell.setCellValue(j + 2);
						cell = row.createCell(3, CellType.STRING);
						cell.setCellValue(timeTable.getRooms().get(j2).getName());
						cell = row.createCell(4, CellType.STRING);
						cell.setCellValue(k);
						cell = row.createCell(5, CellType.STRING);
						cell.setCellValue(assign[j][j2][k].getClassify());
					}
				}
			}
		}

		File file = new File("timetable.xls");
		FileOutputStream outfile = new FileOutputStream(file);
		workBook.write(outfile);
		System.out.println("Created file: ");

	}

	public static void writeByExcel(TimeTable timeTable) throws FileNotFoundException, UnsupportedEncodingException {
		Assigned[][][] assign = timeTable.getTimeTable();
		PrintWriter writer = new PrintWriter("localsearch.csv", "UTF-8");
		writer.print("ID");
		writer.print(",");
		writer.print("Giảng viên");
		writer.print(",");
		writer.print("Môn học");
		writer.print(",");
		writer.print("Thứ");
		writer.print(",");
		writer.print("Phòng");
		writer.print(",");
		writer.println("Ca");
		for (int j = 0; j < 7; j++) {
			for (int j2 = 0; j2 < timeTable.getRooms().size(); j2++) {
				for (int k = 0; k < 4; k++) {
					if (assign[j][j2][k] != null) {
						writer.print(assign[j][j2][k].getId());
						writer.print(",");
						writer.print(assign[j][j2][k].getLecturer().getName());
						writer.print(",");
						writer.print(assign[j][j2][k].getCourse().getName());
						writer.print(",");
						writer.print(j + 2);
						writer.print(",");
						writer.print(timeTable.getRooms().get(j2).getName());
						writer.print(",");
						writer.println(k + 1);
					}
				}
			}
		}
		writer.close();
	}

	public static Assigned[][][] readByExcel(String path)
			throws ClassNotFoundException, SQLException, NumberFormatException, IOException {
		Assigned[][][] timetable = new Assigned[7][RepoRoom.getAll().size()][4];
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(path));
		String textInALine =br.readLine();
		try {
			while ((textInALine = br.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(textInALine, ",");
				int idass = Integer.parseInt(token.nextToken());
				String giangvien = token.nextToken();
				String monhoc = token.nextToken();
				int day = Integer.parseInt(token.nextToken());
				String room = token.nextToken();
				int session = Integer.parseInt(token.nextToken());
		
				timetable[day-2][MethodUtil.getNameRoom2(room)][session-1] = RepoAssigned.getAss(idass);

//            System.out.println("Count: "+count++ + "  " +ass.toString());
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
		return timetable;
	}
	public static void appendfitnesstoExcel(String path,int quantity, int iteration, double fitness) {
		File file = new File(path);
		FileWriter fr = null;
		BufferedWriter br = null;
		PrintWriter pr = null;
		try {
			String text= quantity+","+iteration+","+fitness;
			// to append to file, you need to initialize FileWriter using below constructor
			fr = new FileWriter(file, true);
			br = new BufferedWriter(fr);
			pr = new PrintWriter(br,true);
			pr.println(text);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				pr.close();
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		Map<String, Room> rooms = RepoRoom.getAll();
		Map<Integer, Lecturer> lectures = RepoLecture.getAll();
		List<Room> listRoom = new ArrayList<Room>(rooms.values());
		List<Lecturer> listLecturers = new ArrayList<Lecturer>(lectures.values());
		List<Assigned> listAss = RepoAssigned.getAll();
//		List<Busy> listbusy = RepoBusy.getAll();
		TimeTable tb = new TimeTable(listAss, listLecturers, listRoom/* , listbusy */);
//		TimeTable a = tb.generateTimeTable(listAss, listRoom, listLecturers, listbusy);
//		a.printTimeTable();
//		ResultToExcel2(a);
//		writeByExcel(a);
//		a.setTimeTable(readByExcel("localsearch.csv"));
//		a.printTimeTable();
//		System.out.println(a.getFitness());
		appendfitnesstoExcel("appendfitness.csv",50, 20, 2000.0);
	}
}
