package de.contentcreation.pplive.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manager.FileHandler;
import manager.ReportingHandler;
import reporting.ExcelGenerator;
import reportingClasses.PartnerReport;

public class PartnerKeyAccountReportServlet extends HttpServlet {
	File reportFile = null;
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ReportingHandler rh = ReportingHandler.getInstance();
		List<Object[]> partnerReport = rh.getProblemArticles();

		String root = getServletContext().getRealPath("/");

		File path = new File(root + "/reports");
		if (!path.exists()) {
			boolean bool = path.mkdirs();
		}
		String fileName = "KeyAccountReport.xlsx";
		this.reportFile = new File(path + "/" + fileName);
		ExcelGenerator exgen = ExcelGenerator.getInstance();
		exgen.createKeyAccountReport(this.reportFile, partnerReport);
		FileHandler fh = FileHandler.getInstance();
		fh.downloadFile(response, this.reportFile);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
