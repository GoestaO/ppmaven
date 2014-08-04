package de.contentcreation.pplive.reportingClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import de.contentcreation.pplive.services.ReportingHandler;
import model.BacklogArticle;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

public class Reporting {
	public static void createNewArticlesReporting(File filename) {
		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet("Neue SKUs");

		HSSFCellStyle dateCellStyle = workbook.createCellStyle();

		short df = workbook.createDataFormat().getFormat("dd.MM.yyyy");

		dateCellStyle.setDataFormat(df);

		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setFontName("Calibri");

		CellStyle fontStyle = workbook.createCellStyle();
		fontStyle.setFont(font);
		ReportingHandler rh = ReportingHandler.getInstance();
		List<BacklogArticle> newArticles = rh.getOpenBacklogArticles();

		Row headerRow = sheet.createRow(0);

		Cell headerCell0 = headerRow.createCell(0);
		headerCell0.setCellValue("Partner ID");

		Cell headerCell1 = headerRow.createCell(1);
		headerCell1.setCellValue("Config");

		Cell headerCell2 = headerRow.createCell(2);
		headerCell2.setCellValue("Saison");

		Cell headerCell3 = headerRow.createCell(3);
		headerCell3.setCellValue("Datum");

		Cell headerCell4 = headerRow.createCell(4);
		headerCell4.setCellValue("Appdomain ID");

		int rownum = 1;
		for (BacklogArticle ba : newArticles) {
			Row row = sheet.createRow(rownum++);

			Cell cell1 = row.createCell(0);

			cell1.setCellValue(ba.getPartnerId());

			Cell cell2 = row.createCell(1);
			cell2.setCellValue(ba.getConfig());

			Cell cell3 = row.createCell(2);
			cell3.setCellValue(ba.getSaison());

			Cell cell4 = row.createCell(3);

			cell4.setCellStyle(dateCellStyle);
			try {
				cell4.setCellValue(ba.getDatum());
			} catch (NullPointerException npex) {
				cell4.setCellValue("");
			}
			Cell cell5 = row.createCell(4);
			cell5.setCellValue(ba.getAppdomainId());
		}
		try {
			FileOutputStream out = new FileOutputStream(filename);
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
