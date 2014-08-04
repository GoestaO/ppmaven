package de.contentcreation.pplive.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manager.DatabaseHandler;

public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DatabaseHandler dh = DatabaseHandler.getInstance();
		String nick = request.getParameter("nick");
		String vorname = request.getParameter("vorname");
		String nachname = request.getParameter("nachname");
		String passwort = dh.md5(request.getParameter("pw"));
		String forwardPage = dh.registerUser(nick, vorname, nachname, passwort);
		RequestDispatcher rd = request.getRequestDispatcher(forwardPage);
		rd.forward(request, response);
	}
}
