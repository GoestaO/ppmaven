package de.contentcreation.pplive.services;

import java.io.PrintStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.User;

public class SessionHandler {
	HttpSession session;

	public boolean userLogged(HttpServletRequest request) {
		boolean value = true;
		try {
			this.session = request.getSession(false);
			if (this.session.getAttribute("currentSessionUser") == null) {
				value = false;
			} else {
				value = true;
			}
		} catch (NullPointerException npe) {
			return false;
		}
		return value;
	}

	public User getCurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		User currentUser = (User) session.getAttribute("currentSessionUser");
		return currentUser;
	}

	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		try {
			session.invalidate();
		} catch (NullPointerException npex) {
			System.out
					.println("Logout nicht notwendig, die Session war schon geschlossen");
		}
	}
}
