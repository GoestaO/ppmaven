package de.contentcreation.pplive.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.contentcreation.pplive.services.DatabaseHandler;

public class ShowPartnersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<String> partnerList = createPartnerTable(30);
		request.setAttribute("partnerList", partnerList);
		RequestDispatcher rd = request.getRequestDispatcher("/loginPage.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected List<String> createPartnerTable(int columns) {
		DatabaseHandler dh = DatabaseHandler.getInstance();

		List<String> partnerListString = dh.getPartnerListString();

		List<String> partnerTabelle = new ArrayList();

		int anzahlSpalten = columns;
		int counter = 0;
		int limit = anzahlSpalten;
		partnerTabelle.add("<table class = 'table'><thead></thead><tbody>");
		while (counter < partnerListString.size()) {
			if (counter > partnerListString.size() - anzahlSpalten) {
				limit = partnerListString.size() - counter;
			}
			partnerTabelle.add("<tr>");
			for (int i = 0; i < limit; i++) {
				String row = "<td><input type='checkbox' id='partner_group[]' name='partner_group[]' value='"
						+ (String) partnerListString.get(counter)
						+ "'>"
						+ (String) partnerListString.get(counter) + "</td>";

				partnerTabelle.add(row);

				counter++;
			}
			partnerTabelle.add("</tr>");
		}
		partnerTabelle.add("</tbody></table>");

		return partnerTabelle;
	}
}
