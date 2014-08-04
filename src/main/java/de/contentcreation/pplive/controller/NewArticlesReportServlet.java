package de.contentcreation.pplive.controller;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.contentcreation.pplive.services.FileHandler;
import de.contentcreation.pplive.reporting.ExcelGenerator;

public class NewArticlesReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final int BUFFER_SIZE = 16384;
	File reportFile = null;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String root = getServletContext().getRealPath("/");

		File path = new File(root + "/reports");
		if (!path.exists()) {
			boolean bool = path.mkdirs();
		}
		String fileName = "neueArtikel.xlsx";
		this.reportFile = new File(path + "/" + fileName);
		ExcelGenerator exgen = ExcelGenerator.getInstance();
		exgen.createNewArticlesReport(this.reportFile);
		FileHandler fh = FileHandler.getInstance();
		fh.downloadFile(response, this.reportFile);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
