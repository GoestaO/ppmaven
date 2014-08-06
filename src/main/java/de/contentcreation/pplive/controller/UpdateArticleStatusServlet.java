package de.contentcreation.pplive.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.contentcreation.pplive.services.DatabaseHandler;
import de.contentcreation.pplive.services.SessionHandler;


public class UpdateArticleStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("ISO-8859-15");
		response.setCharacterEncoding("ISO-8859-15");
//		DatabaseHandler dh = DatabaseHandler.getInstance();
//		String neuerStatus = request.getParameter("status");
//		String bemerkung1 = request.getParameter("bemerkung1").trim();
//		String bemerkung2 = request.getParameter("bemerkung2").trim();
//		String bemerkung3 = request.getParameter("bemerkung3").trim();
//		String bemerkungKAM = request.getParameter("bemerkungKAM").trim();
//		String saison = request.getParameter("saison");
//		String identifier = request.getParameter("id");
//		SessionHandler sessionChecker = new SessionHandler();
//		if (!sessionChecker.userLogged(request)) {
//			response.sendRedirect("loginPage.jsp");
//			return;
//		}
//		User currentUser = sessionChecker.getCurrentUser(request);
//		dh.updateArticleStatus(identifier, bemerkung1, bemerkung2, bemerkung3,
//				bemerkungKAM, neuerStatus, currentUser, saison);
//		response.sendRedirect("getBacklogByPartner.do");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
