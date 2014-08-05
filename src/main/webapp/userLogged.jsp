<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256" import="model.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1256">
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script type="text/javascript" src="javascript/bootstrap-select.js"></script>

<!-- 3.0 -->

<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="javascript/bootstrap-select.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap-select.css">
<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
<title>Login erfolgreich</title>
</head>
<body>
	
	<div class="pull-right padding-top">
		<img style="top: 0px; left: 768px"
			src="images/200px-Zalando_logo.svg.png">
	</div>
<br>
<br>
<br>
	<div class="jumbotron">
		<center>

			<p>
				<%
					User currentUser = (User) (session
							.getAttribute("currentSessionUser"));
				%>
				Willkommen
				<%=currentUser.getVorname() + " "
					+ currentUser.getNachname()%>
			</p>
			<p>
				<a class="btn btn-primary btn-lg" href="getBacklogByPartner.do">Zum
					Backlog</a>
		</center>
	</div>


</body>
</html>