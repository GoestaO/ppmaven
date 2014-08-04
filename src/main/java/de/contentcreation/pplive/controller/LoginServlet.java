package de.contentcreation.pplive.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import manager.DatabaseHandler;
import model.User;
import model.UserDAO;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 3480567019147467744L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			DatabaseHandler dh = DatabaseHandler.getInstance();
			String[] choosenPartners = request
					.getParameterValues("partner_group[]");
			List<Integer> partnerList = new ArrayList();
			for (String partner : choosenPartners) {
				partnerList.add(Integer.valueOf(Integer.parseInt(partner)));
			}
			User user = new User();
			user.setNick(request.getParameter("nick"));

			user.setPasswort(dh.md5(request.getParameter("pw")));

			user = UserDAO.login(user);
			if (user.isValid()) {
				HttpSession session = request.getSession(true);
				session.setAttribute("currentSessionUser", user);
				session.setAttribute("partnerList", partnerList);
				response.sendRedirect("getBacklogByPartner.do");
			} else {
				response.sendRedirect("invalidLogin.jsp");
			}
		} catch (Throwable theException) {
			System.out.println(theException);
		}
	}
}
