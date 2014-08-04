package de.contentcreation.pplive.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.contentcreation.pplive.services.DatabaseHandler;
import model.BacklogArticle;

public class WriteToDatabaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<BacklogArticle> backlogList = new ArrayList();
		DatabaseHandler dh = DatabaseHandler.getInstance();
		long start = -System.currentTimeMillis();
		File uploadedFile = (File) request.getAttribute("uploadedFile");
		if (uploadedFile.getAbsolutePath().endsWith(".csv")) {
			FileReader reader = new FileReader(uploadedFile);
			BufferedReader br = new BufferedReader(reader);
			br.readLine();
			String line = br.readLine();
			while (line != null) {
				String[] data = line.split(";");
				String config = data[0];
				int partnerID = Integer.parseInt(data[1]);
				String warengruppenpfad = data[7];
				String saison = data[8];
				int appdomainID = Integer.parseInt(data[9]);
				String identifier = config + partnerID + appdomainID;
				BacklogArticle ba = new BacklogArticle();
				ba.setIdentifier(identifier);
				ba.setAppdomainId(appdomainID);
				ba.setConfig(config);
				ba.setPartnerId(partnerID);
				ba.setCgPath(warengruppenpfad);
				ba.setSaison(saison);
				ba.setDatum(Calendar.getInstance().getTime());
				ba.setOffen(true);
				backlogList.add(ba);
				line = br.readLine();
			}
			br.close();

			int counter = dh.checkAndAdd(backlogList);
			request.setAttribute("counter", Integer.valueOf(counter));
		}
		String dauer = (start + System.currentTimeMillis()) / 1000L
				+ " Sekunden";
		request.setAttribute("dauer", dauer);

		RequestDispatcher rd = request
				.getRequestDispatcher("/uploadSuccessful.jsp");
		rd.forward(request, response);
	}
}
