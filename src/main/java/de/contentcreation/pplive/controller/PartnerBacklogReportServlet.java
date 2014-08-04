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
import model.BacklogArticle;
import reporting.ExcelGenerator;

public class PartnerBacklogReportServlet extends HttpServlet {
	File reportFile = null;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ReportingHandler rh = ReportingHandler.getInstance();
		List<BacklogArticle> partnerReport = rh.getOpenBacklogArticles();

		String root = getServletContext().getRealPath("/");

		File path = new File(root + "/reports");
		if (!path.exists()) {
			boolean bool = path.mkdirs();
		}
		String fileName = "KeyAccountReport.xlsx";
		this.reportFile = new File(path + "/" + fileName);
		ExcelGenerator exgen = ExcelGenerator.getInstance();
		exgen.createPartnerReport(this.reportFile, partnerReport);
		FileHandler fh = FileHandler.getInstance();
		fh.downloadFile(response, this.reportFile);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
