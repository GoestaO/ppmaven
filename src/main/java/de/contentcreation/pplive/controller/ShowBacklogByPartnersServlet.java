package de.contentcreation.pplive.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import manager.DatabaseHandler;
import manager.SessionHandler;
import model.BacklogArticle;

public class ShowBacklogByPartnersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		DatabaseHandler dh = DatabaseHandler.getInstance();

		SessionHandler sessionChecker = new SessionHandler();
		if (!sessionChecker.userLogged(request)) {
			response.sendRedirect("showLogin.do");
		} else {
			HttpSession session = request.getSession(false);

			List<Integer> partnerList = (List) session
					.getAttribute("partnerList");

			List<BacklogArticle> backlogList = dh
					.getBacklogByPartner2(partnerList);

			request.setAttribute("backlogList", backlogList);

			RequestDispatcher rd = request
					.getRequestDispatcher("/backlogUebersicht.jsp");
			rd.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
