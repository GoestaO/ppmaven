package de.contentcreation.pplive.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manager.FileHandler;
import manager.ReportingHandler;
import reporting.ExcelGenerator;
import reportingClasses.UserReport;

public class LeistungsreportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final int BUFFER_SIZE = 16384;
	File reportFile = null;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		String date = request.getParameter("date");
		Date date1 = null;
		try {
			date1 = sdf.parse(date);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Date firstDate = cal1.getTime();

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date1);
		cal2.add(5, 1);
		Date secondDate = cal2.getTime();

		ReportingHandler rh = ReportingHandler.getInstance();
		List<UserReport> userReport = rh.getUserReport(firstDate, secondDate);

		String root = getServletContext().getRealPath("/");

		File path = new File(root + "/reports");
		if (!path.exists()) {
			boolean bool = path.mkdirs();
		}
		String fileName = "UserReport.xlsx";
		this.reportFile = new File(path + "/" + fileName);
		ExcelGenerator exgen = ExcelGenerator.getInstance();
		exgen.createUserReport(this.reportFile, userReport);
		FileHandler fh = FileHandler.getInstance();
		fh.downloadFile(response, this.reportFile);
	}
}
