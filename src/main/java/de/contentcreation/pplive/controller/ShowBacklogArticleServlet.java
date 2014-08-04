package de.contentcreation.pplive.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manager.DatabaseHandler;
import manager.SessionHandler;
import model.BacklogArticle;

public class ShowBacklogArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("ISO-8859-15");
		response.setCharacterEncoding("ISO-8859-15");
		try {
			DatabaseHandler dh = DatabaseHandler.getInstance();
			SessionHandler sessionChecker = new SessionHandler();
			if (!sessionChecker.userLogged(request)) {
				response.sendRedirect("showLogin.do");
			} else {
				String identifier = request.getParameter("identifier");
				List<String> seasons = dh.getSeasons();
				BacklogArticle backlogArticle = dh
						.getBacklogArticle(identifier);

				request.setAttribute("seasons", seasons);
				request.setAttribute("backlogArticle", backlogArticle);
				RequestDispatcher rd = request
						.getRequestDispatcher("/artikelanzeige.jsp");
				rd.forward(request, response);
			}
		} catch (NullPointerException npex) {
			response.sendRedirect("loginPage.jsp");
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
