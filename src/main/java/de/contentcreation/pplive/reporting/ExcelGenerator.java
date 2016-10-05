package de.contentcreation.pplive.reporting;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.reportingClasses.RejectReportBemerkung1;
import de.contentcreation.pplive.reportingClasses.RejectReportBemerkung2;
import de.contentcreation.pplive.reportingClasses.RejectReportOverview;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import de.contentcreation.pplive.services.ReportingHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import de.contentcreation.pplive.reportingClasses.UserReport;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Diese Bean ist für die Generierung von Excel-Dateien mit Hilfe der
 * Apache-POI-Bibliothek für die verschiedenen Reports zuständig.
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
@Named
@RequestScoped
public class ExcelGenerator {

	@EJB
	private ReportingHandler rh;

	/**
	 * Erzeugt den Excel-Report für die neu hochgeladenen Artikel
	 *
	 * @param filename
	 *            Die Datei, in der der Report gespeichert werden soll.
	 * @param datum1
	 *            Startdatum
	 * @param datum2
	 *            Enddatum
	 */
	public void createNewArticlesReport(File filename, Date datum1, Date datum2) {
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("Neue SKUs");

		CellStyle dateCellStyle = workbook.createCellStyle();

		short df = workbook.createDataFormat().getFormat("dd.MM.yyyy");

		dateCellStyle.setDataFormat(df);

		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setFontName("Calibri");

		CellStyle fontStyle = workbook.createCellStyle();

		List<BacklogArticle> newArticles = rh.getNewBacklogArticles(datum1, datum2);

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
		for (int i = 0; i < 11; i++) {
			sheet.autoSizeColumn(i);
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

	/**
	 * Erzeugt den Excel-Report für die bearbeiteten Artikel
	 *
	 * @param filename
	 *            Die Datei, in der der Report gespeichert werden soll
	 * @param partnerReport
	 *            Das Resultat der Datenbankabfrage.
	 */
	public void createEditedArticlesReport(File filename, List<Object[]> partnerReport) {
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("Report");

		CellStyle dateCellStyle = workbook.createCellStyle();

		short df = workbook.createDataFormat().getFormat("dd.MM.yyyy");

		dateCellStyle.setDataFormat(df);

		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setFontName("Calibri");

		CellStyle fontStyle = workbook.createCellStyle();
		fontStyle.setFont(font);

		Row headerRow = sheet.createRow(0);

		Cell partnerHeader = headerRow.createCell(0);
		partnerHeader.setCellValue("Partner ID");

		Cell configHeader = headerRow.createCell(1);
		configHeader.setCellValue("Config");

		Cell appdomainHeader = headerRow.createCell(2);
		appdomainHeader.setCellValue("Appdomain ID");

		Cell cgPathHeader = headerRow.createCell(3);
		cgPathHeader.setCellValue("Warengruppenpfad");

		Cell seasonHeader = headerRow.createCell(4);
		seasonHeader.setCellValue("Saison");

		Cell dateHeader = headerRow.createCell(5);
		dateHeader.setCellValue("Datum");

		Cell comment1Header = headerRow.createCell(6);
		comment1Header.setCellValue("Bemerkung 1");

		Cell comment2Header = headerRow.createCell(7);
		comment2Header.setCellValue("Bemerkung 2");

		Cell comment3Header = headerRow.createCell(8);
		comment3Header.setCellValue("Bemerkung 3");

		Cell commentKAMHeader = headerRow.createCell(9);
		commentKAMHeader.setCellValue("Bemerkung KAM");

		int rownum = 1;
		for (Object[] ba : partnerReport) {
			Row row = sheet.createRow(rownum++);

			Cell partnerCell = row.createCell(0);

			partnerCell.setCellValue(ba[1].toString());

			Cell configCell = row.createCell(1);
			configCell.setCellValue(ba[2].toString());

			Cell appdomainCell = row.createCell(2);
			appdomainCell.setCellValue(ba[3].toString());

			Cell cgPathCell = row.createCell(3);
			cgPathCell.setCellValue(ba[4].toString());

			Cell seasonCell = row.createCell(4);
			try {
				seasonCell.setCellValue(ba[5].toString());
			} catch (NullPointerException npex) {
				seasonCell.setCellValue("");
			}
			Cell dateCell = row.createCell(5);
			dateCell.setCellStyle(dateCellStyle);
			String date = ba[6].toString();
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				dateCell.setCellValue(sdf.parse(date));
			} catch (NullPointerException npex) {
				dateCell.setCellValue("");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			dateCell.setCellStyle(dateCellStyle);

			Cell comment1Cell = row.createCell(6);
			try {
				comment1Cell.setCellValue(ba[7].toString());
			} catch (NullPointerException npex) {
				comment1Cell.setCellValue("");
			}
			Cell comment2Cell = row.createCell(7);
			try {
				comment2Cell.setCellValue(ba[8].toString());
			} catch (NullPointerException npex) {
				comment2Cell.setCellValue("");
			}
			Cell comment3Cell = row.createCell(8);
			try {
				comment3Cell.setCellValue(ba[9].toString());
			} catch (NullPointerException npex) {
				comment3Cell.setCellValue("");
			}
			Cell commentKAMCell = row.createCell(9);
			try {
				commentKAMCell.setCellValue(ba[10].toString());
			} catch (NullPointerException npex) {
				commentKAMCell.setCellValue("");
			}
		}
		for (int i = 0; i < 12; i++) {
			sheet.autoSizeColumn(i);
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

	/**
	 * Erzeugt den Excel-Report für den Partner-Report
	 *
	 * @param filename
	 *            Die Datei, in der der Report gespeichert werden soll
	 * @param partnerReport
	 *            Das Resultat der Datenbankabfrage.
	 */
	public void createPartnerReport(File filename, List<BacklogArticle> partnerReport) {
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("Partner Report");

		CellStyle dateCellStyle = workbook.createCellStyle();

		short df = workbook.createDataFormat().getFormat("dd.MM.yyyy");

		dateCellStyle.setDataFormat(df);

		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setFontName("Calibri");

		CellStyle fontStyle = workbook.createCellStyle();
		fontStyle.setFont(font);

		Row headerRow = sheet.createRow(0);

		Cell headerCell0 = headerRow.createCell(0);
		headerCell0.setCellValue("Partner ID");

		Cell headerCell1 = headerRow.createCell(1);
		headerCell1.setCellValue("Config");

		Cell headerCell2 = headerRow.createCell(2);
		headerCell2.setCellValue("Appdomain ID");

		Cell headerCell3 = headerRow.createCell(3);
		headerCell3.setCellValue("Warengruppenpfad");

		Cell headerCell4 = headerRow.createCell(4);
		headerCell4.setCellValue("Saison");

		Cell headerCell5 = headerRow.createCell(5);
		headerCell5.setCellValue("Bemerkung 1");

		Cell headerCell6 = headerRow.createCell(6);
		headerCell6.setCellValue("Bemerkung 2");

		Cell headerCell7 = headerRow.createCell(7);
		headerCell7.setCellValue("Bemerkung 3");

		Cell headerCellcommentKAM = headerRow.createCell(8);
		headerCellcommentKAM.setCellValue("Bemerkung KAM");

		int rownum = 1;
		for (BacklogArticle ba : partnerReport) {
			Row row = sheet.createRow(rownum++);

			Cell partnerIdCell = row.createCell(0);
			partnerIdCell.setCellValue(ba.getPartnerId());

			Cell configCell = row.createCell(1);
			configCell.setCellValue(ba.getConfig());

			Cell appdomainCell = row.createCell(2);
			appdomainCell.setCellValue(ba.getAppdomainId());

			Cell cgPathCell = row.createCell(3);
			cgPathCell.setCellValue(ba.getCgPath());

			Cell seasonCell = row.createCell(4);
			seasonCell.setCellValue(ba.getSaison());

			Cell comment1Cell = row.createCell(5);
			try {
				comment1Cell.setCellValue(ba.getBemerkung1());
			} catch (NullPointerException npex) {
				comment1Cell.setCellValue("");
			}
			Cell comment2Cell = row.createCell(6);
			try {
				comment2Cell.setCellValue(ba.getBemerkung2());
			} catch (NullPointerException npex) {
				comment2Cell.setCellValue("");
			}
			Cell comment3Cell = row.createCell(7);
			try {
				comment3Cell.setCellValue(ba.getBemerkung3());
			} catch (NullPointerException npex) {
				comment3Cell.setCellValue("");
			}
			Cell commentKAMCell = row.createCell(7);
			try {
				commentKAMCell.setCellValue(ba.getBemerkungKAM());
			} catch (NullPointerException npex) {
				commentKAMCell.setCellValue("");
			}
		}
		for (int i = 0; i < 12; i++) {
			sheet.autoSizeColumn(i);
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

	/**
	 * Erzeugt den Excel-Report für den User-Report
	 *
	 * @param filename
	 *            Die Datei, in der der Report gespeichert werden soll
	 * @param userReport
	 *            Das Resultat der Datenbankabfrage.
	 */
	public void createUserReport(File filename, List<UserReport> userReport) {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		Row headerRow = sheet.createRow(0);

		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setFontName("Calibri");

		CellStyle fontStyle = wb.createCellStyle();
		fontStyle.setFont(font);

		CellStyle dateCellStyle = wb.createCellStyle();

		short df = wb.createDataFormat().getFormat("dd.MM.yyyy");

		dateCellStyle.setDataFormat(df);

		Cell nameHeader = headerRow.createCell(0);
		nameHeader.setCellValue("Name");

		Cell configHeader = headerRow.createCell(1);
		configHeader.setCellValue("Config");

		Cell adddomainHeader = headerRow.createCell(2);
		adddomainHeader.setCellValue("Appdomain");

		Cell partnerHeader = headerRow.createCell(3);
		partnerHeader.setCellValue("Partner");

		Cell cgPathHeader = headerRow.createCell(4);
		cgPathHeader.setCellValue("WG-Pfad");

		Cell seasonHeader = headerRow.createCell(5);
		seasonHeader.setCellValue("Saison");

		Cell dateHeader = headerRow.createCell(6);
		dateHeader.setCellValue("Datum");

		Cell statusHeader = headerRow.createCell(7);
		statusHeader.setCellValue("Status");

		Cell comment1Header = headerRow.createCell(8);
		comment1Header.setCellValue("Bemerkung 1");

		Cell comment2Header = headerRow.createCell(9);
		comment2Header.setCellValue("Bemerkung 2");

		Cell comment3Header = headerRow.createCell(10);
		comment3Header.setCellValue("Bemerkung 3");

		Cell commentKAMHeader = headerRow.createCell(11);
		commentKAMHeader.setCellValue("Bemerkung KAM");

		int rownum = 1;
		for (UserReport ur : userReport) {
			Row row = sheet.createRow(rownum++);

			Cell nameCell = row.createCell(0);
			try {
				nameCell.setCellValue(ur.getName());
			} catch (NullPointerException ex) {
				nameCell.setCellValue("");
			}

			Cell configCell = row.createCell(1);
			configCell.setCellValue(ur.getConfig());

			Cell appdomainCell = row.createCell(2);
			appdomainCell.setCellValue(ur.getAppdomainId());

			Cell partnerCell = row.createCell(3);
			partnerCell.setCellValue(ur.getPartnerId());

			Cell cgPathCell = row.createCell(4);
			cgPathCell.setCellValue(ur.getCgPath());

			Cell seasonCell = row.createCell(5);
			seasonCell.setCellValue(ur.getSaison());

			Cell dateCell = row.createCell(6);
			dateCell.setCellValue(ur.getDatum());
			dateCell.setCellStyle(dateCellStyle);

			Cell statusCell = row.createCell(7);
			statusCell.setCellValue(ur.getStatus());

			Cell comment1Cell = row.createCell(8);
			comment1Cell.setCellValue(ur.getBemerkung1());

			Cell comment2Cell = row.createCell(9);
			comment2Cell.setCellValue(ur.getBemerkung2());

			Cell comment3Cell = row.createCell(10);
			comment3Cell.setCellValue(ur.getBemerkung3());

			Cell commentKAMCell = row.createCell(11);
			commentKAMCell.setCellValue(ur.getBemerkungKAM());
		}
		for (int i = 0; i < 12; i++) {
			sheet.autoSizeColumn(i);
		}
		try {
			FileOutputStream out = new FileOutputStream(filename);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createRejectReport(File filename, List<RejectReportOverview> overviewList,
			List<RejectReportBemerkung1> bemerkung1List, List<RejectReportBemerkung2> bemerkung2List) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("RejectReport");

		Row labelRow = sheet.createRow(0);

		Row headerRow = sheet.createRow(2);

		// Gesamtübersicht
		Cell overViewLabel = labelRow.createCell(0);
		overViewLabel.setCellValue("Gesamtübersicht");
		headerRow.createCell(0).setCellValue("Partner");
		headerRow.createCell(1).setCellValue("Anzahl");
		int rowNr = 3;
		for (RejectReportOverview r : overviewList) {
			Row row = sheet.createRow(rowNr);
			row.createCell(0).setCellValue(r.getPartnerId());
			row.createCell(1).setCellValue(r.getQuantity());
			rowNr++;
		}

		// Übersicht Bemerkung 1
		Cell bemerkung1Label = labelRow.createCell(3);
		bemerkung1Label.setCellValue("Übersicht Bemerkung 1");
		headerRow.createCell(3).setCellValue("Partner");
		headerRow.createCell(4).setCellValue("Bemerkung 1");
		headerRow.createCell(5).setCellValue("Anzahl");
		headerRow.createCell(6).setCellValue("Status");
		rowNr = 3;
		for (RejectReportBemerkung1 r : bemerkung1List) {
			Row row = null;
			row = sheet.getRow(rowNr);
			if (row == null) {
				row = sheet.createRow(rowNr);
			}
			row.createCell(3).setCellValue(r.getPartnerId());
			row.createCell(4).setCellValue(r.getBemerkung1());
			row.createCell(5).setCellValue(r.getQuantity());
			row.createCell(6).setCellValue(r.getStatus());
			rowNr++;
		}

		// Übersicht Bemerkung 2
		Cell bemerkung2Label = labelRow.createCell(8);
		bemerkung2Label.setCellValue("Übersicht Bemerkung 2");
		headerRow.createCell(8).setCellValue("Partner");
		headerRow.createCell(9).setCellValue("Bemerkung 2");
		headerRow.createCell(10).setCellValue("Anzahl");
		headerRow.createCell(11).setCellValue("Status");
		rowNr = 3;
		for (RejectReportBemerkung2 r : bemerkung2List) {
			Row row = null;
			row = sheet.getRow(rowNr);
			if (row == null) {
				row = sheet.createRow(rowNr);
			}
			row.createCell(8).setCellValue(r.getPartnerId());
			row.createCell(9).setCellValue(r.getBemerkung2());
			row.createCell(10).setCellValue(r.getQuantity());
			row.createCell(11).setCellValue(r.getStatus());
			rowNr++;
		}

		// Gesamtübersichtsheader zusammenfassen
		sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
				0, // last row (0-based)
				0, // first column (0-based)
				1 // last column (0-based)
		));

		// Bemerkung1Header zusammenfassen
		sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
				0, // last row (0-based)
				3, // first column (0-based)
				6 // last column (0-based)
		));

		// Bemerkung2Header zusammenfassen
		sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
				0, // last row (0-based)
				8, // first column (0-based)
				11 // last column (0-based)
		));

		// Spaltenbreite automatisch anpassen
		for (int i = 1; i < 12; i++) {
			sheet.autoSizeColumn(i, true);
		}
		//
		sheet.setColumnWidth(0, 2500);

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

	/**
	 * Erzeugt den Excel-Report für den KAM-Report
	 *
	 * @param filename
	 *            Die Datei, in der der Report gespeichert werden soll
	 * @param partnerReport
	 *            Das Resultat der Datenbankabfrage.
	 */
	public void createKeyAccountReport(File filename, List<Object[]> partnerReport) {
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("Key Account Report");

		CellStyle dateCellStyle = workbook.createCellStyle();

		short df = workbook.createDataFormat().getFormat("dd.MM.yyyy");

		dateCellStyle.setDataFormat(df);

		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setFontName("Calibri");

		CellStyle fontStyle = workbook.createCellStyle();
		fontStyle.setFont(font);

		Row headerRow = sheet.createRow(0);

		Cell partnerHeader = headerRow.createCell(0);
		partnerHeader.setCellValue("Partner ID");

		Cell configHeader = headerRow.createCell(1);
		configHeader.setCellValue("Config");

		Cell appdomainHeader = headerRow.createCell(2);
		appdomainHeader.setCellValue("Appdomain ID");

		Cell cgPathHeader = headerRow.createCell(3);
		cgPathHeader.setCellValue("Warengruppenpfad");

		Cell seasonHeader = headerRow.createCell(4);
		seasonHeader.setCellValue("Saison");

		Cell userHeader = headerRow.createCell(5);
		userHeader.setCellValue("Letzer Bearbeiter");

		Cell comment1Header = headerRow.createCell(6);
		comment1Header.setCellValue("Bemerkung 1");

		Cell comment2Header = headerRow.createCell(7);
		comment2Header.setCellValue("Bemerkung 2");

		Cell comment3Header = headerRow.createCell(8);
		comment3Header.setCellValue("Bemerkung 3");

		Cell commentKAMHeader = headerRow.createCell(9);
		commentKAMHeader.setCellValue("Datum");

		Cell openHeader = headerRow.createCell(10);
		openHeader.setCellValue("Offen");	
		

		int rownum = 1;
		for (Object[] pr : partnerReport) {
			Row row = sheet.createRow(rownum++);

			Cell partnerCell = row.createCell(0);
			partnerCell.setCellValue((Integer) pr[1]);

			Cell configCell = row.createCell(1);
			configCell.setCellValue((String) pr[2]);

			Cell appdomainCell = row.createCell(2);
			appdomainCell.setCellValue(pr[3].toString());

			Cell cgPathCell = row.createCell(3);
			cgPathCell.setCellValue((String) pr[4]);

			Cell seasonCell = row.createCell(4);
			seasonCell.setCellValue((String) pr[5]);

			Cell userCell = row.createCell(5);
			userCell.setCellValue((String) pr[6] + " " + (String) pr[7]);

			Cell comment1Cell = row.createCell(6);
			try {
				comment1Cell.setCellValue((String) pr[8]);
			} catch (NullPointerException npex) {
				comment1Cell.setCellValue("");
			}

			Cell comment2Cell = row.createCell(7);
			try {
				comment2Cell.setCellValue((String) pr[9]);
			} catch (NullPointerException npex) {
				comment2Cell.setCellValue("");
			}

			Cell comment3Cell = row.createCell(8);
			try {
				comment3Cell.setCellValue((String) pr[10]);
			} catch (NullPointerException npex) {
				comment3Cell.setCellValue("");
			}

			Cell lastTimestampCell = row.createCell(9);
                        lastTimestampCell.setCellValue(pr[13].toString());
			lastTimestampCell.setCellStyle(dateCellStyle);
			

			Cell openCell = row.createCell(10);
			openCell.setCellValue(pr[12].toString());
			
			
			
		}
		for (int i = 0; i < 6; i++) {
			sheet.autoSizeColumn(i);
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

	public void exportCurrentSelection(File filename, List<BacklogArticle> articleList){
    	 Workbook workbook = new XSSFWorkbook();

         Sheet sheet = workbook.createSheet("Key Account Report");

         CellStyle dateCellStyle = workbook.createCellStyle();

         short df = workbook.createDataFormat().getFormat("dd.MM.yyyy");

         dateCellStyle.setDataFormat(df);

         Font font = workbook.createFont();
         font.setFontHeightInPoints((short) 11);
         font.setFontName("Calibri");

         CellStyle fontStyle = workbook.createCellStyle();
         fontStyle.setFont(font);

         Row headerRow = sheet.createRow(0);

         Cell configHeader = headerRow.createCell(0);
         configHeader.setCellValue("Config");
         
         Cell eanHeader = headerRow.createCell(1);
         eanHeader.setCellValue("EAN");
         
         Cell amountHeader =headerRow.createCell(2);
         amountHeader.setCellValue("Anzahl");
         
         Cell cgPathHeader = headerRow.createCell(3);
         cgPathHeader.setCellValue("Warengruppenpfad");
         
         Cell partnerHeader = headerRow.createCell(4);
         partnerHeader.setCellValue("Partner");

         Cell dateHeader = headerRow.createCell(5);
         dateHeader.setCellValue("Datum");
                 
         Cell seasonHeader = headerRow.createCell(6);
         seasonHeader.setCellValue("Saison");
         
         Cell appdomainHeader = headerRow.createCell(7);
         appdomainHeader.setCellValue("Appdomain ID"); 
         
         Cell comment1Header = headerRow.createCell(8);
         comment1Header.setCellValue("Bemerkung 1");

         Cell comment2Header = headerRow.createCell(9);
         comment2Header.setCellValue("Bemerkung 2");

         Cell comment3Header = headerRow.createCell(10);
         comment3Header.setCellValue("Bemerkung 3");

         Cell commentKAMHeader = headerRow.createCell(11);
         commentKAMHeader.setCellValue("Bemerkung KAM");        

         int rownum = 1;
         for (BacklogArticle b : articleList) {
             Row row = sheet.createRow(rownum++);

             Cell configCell = row.createCell(0);
             configCell.setCellValue(b.getConfig());

             Cell eanCell = row.createCell(1);
             try {
            	 eanCell.setCellValue(b.getEan());
             } catch (NullPointerException npex) {
            	 eanCell.setCellValue("");
             }             

             Cell amountCell = row.createCell(2);
             amountCell.setCellValue(b.getCounter());

             Cell cgPathCell = row.createCell(3);
             cgPathCell.setCellValue(b.getCgPath());

             Cell partnerCell = row.createCell(4);
             partnerCell.setCellValue(b.getPartnerId());

             Cell dateCell = row.createCell(5);
             dateCell.setCellValue(b.getDatum());
             dateCell.setCellStyle(dateCellStyle);
             
             Cell seasonCell = row.createCell(6);
             seasonCell.setCellValue(b.getSaison());
             
             Cell appdomainCell = row.createCell(7);
             appdomainCell.setCellValue(b.getAppdomainId());

             Cell comment1Cell = row.createCell(8);
             try {
                 comment1Cell.setCellValue(b.getBemerkung1());
             } catch (NullPointerException npex) {
                 comment1Cell.setCellValue("");
             }

             Cell comment2Cell = row.createCell(9);
             try {
                 comment2Cell.setCellValue(b.getBemerkung2());
             } catch (NullPointerException npex) {
                 comment2Cell.setCellValue("");
             }

             Cell comment3Cell = row.createCell(10);
             try {
                 comment3Cell.setCellValue(b.getBemerkung3());
             } catch (NullPointerException npex) {
                 comment3Cell.setCellValue("");
             }

             Cell commentKAMCell = row.createCell(11);
             try {
                 commentKAMCell.setCellValue(b.getBemerkungKAM());
             } catch (NullPointerException npex) {
                 commentKAMCell.setCellValue("");
             }

           
         }
         for (int i = 0; i < 13; i++) {
             sheet.autoSizeColumn(i);
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
