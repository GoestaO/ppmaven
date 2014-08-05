<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=ISO-8859-15"
	pageEncoding="ISO-8859-15" import="model.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script type="text/javascript" src="javascript/bootstrap-select.js"></script>

<!-- 3.0 -->

<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="javascript/bootstrap-select.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap-select.css">
<link rel="stylesheet" type="text/css" href="css/bootstrap.css">

<title>Upload erfolgreich</title>

</head>
<body>
	<nav class="navbar navbar-default navbar-fixed-top" role="navigation">

	<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
		<ul class="nav navbar-nav">
			<li><form action="logout.do">
					<input type="submit" value="Logout"
						class="btn btn-default navbar-btn">
				</form></li>
			<li><a href="getBacklogByPartner.do">Backlog</a></li>
			<c:if
				test="${currentSessionUser.rolle.rollenname != 'User' && currentSessionUser.rolle.rollenname != null}">
				<li><a href="reporting.jsp">Reporting</a>
				<li><a href="upload.jsp">Upload</a></li>
			</c:if>
		</ul>
		<div class="navbar-inner pull-right padding-top">
			<img style="top: 0px; left: 768px"
				src="images/200px-Zalando_logo.svg.png">
		</div>
	</div>
	</nav>

	<c:if
		test="${currentSessionUser.rolle.rollenname == 'User'|| currentSessionUser.rolle.rollenname == null}">
		<c:redirect url="http://localhost:8080/Partnerprogramm/showLogin.do" />
	</c:if>

	<br>
	<br>
	<br>
	<div class="jumbotron">
		<center>
			<p>Update erfolgreich, es wurden ${counter} neue Artikel eingespielt</p>
			
			<!-- <p>
				<a class="btn btn-primary btn-lg" href="showLogin.do"> Zum
					Backlog </a>
			</p>
			<br> -->
			<p>Dauer des Uploads: ${dauer}</p>
			<br>
			<div>Alle offenen Artikel ausgeben</div>
			<a class="btn btn-primary btn-lg" href="getOpenArticlesReport.do">
				Ok </a>
			
		</center>
	</div>

</body>
</html>