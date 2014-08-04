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

public class EditedArticlesByPartnerReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	File reportFile = null;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String selectedPartner = request.getParameter("partnerdropdown");

		List<Object[]> editedArticlesList = ReportingHandler.getInstance()
				.getEditedDataByPartner(
						Integer.valueOf(Integer.parseInt(selectedPartner)));

		String root = getServletContext().getRealPath("/");

		File path = new File(root + "/reports");
		if (!path.exists()) {
			boolean bool = path.mkdirs();
		}
		String fileName = "GepflegteArtikel_Partner" + selectedPartner + ".xlsx";
		this.reportFile = new File(path + "/" + fileName);
		ExcelGenerator exgen = ExcelGenerator.getInstance();
		exgen.createEditedArticlesReport(this.reportFile, editedArticlesList);
		FileHandler fh = FileHandler.getInstance();
		fh.downloadFile(response, this.reportFile);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
