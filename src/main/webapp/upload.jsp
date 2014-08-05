<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

<!-- <script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script> -->
<script type="text/javascript" src="javascript/bootstrap-select.js"></script>
<link rel="shortcut icon" href="css/zalando-icon.ico">
<!-- 3.0 -->

<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="javascript/bootstrap-select.js"></script>


</script>
<link rel="stylesheet" type="text/css" href="css/bootstrap-select.css">
<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
<title>Upload</title>
</head>
<body>

<nav class="navbar navbar-default navbar-fixed-top" role="navigation">

		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><form action="logout.do">
						<input type="submit" value="Logout"
							class="btn btn-default navbar-btn">
					</form></li>
				<li><a href="getBacklogByPartner.do">Backlog</a></li>
				<c:if
					test="${currentSessionUser.rolle.rollenname != 'User' && currentSessionUser.rolle.rollenname != null}">
					<li><a href="reporting.jsp">Reporting</a>
					<li class="active"><a href="upload.jsp">Upload</a></li>
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




	<div class="pull-right padding-top">
		<img style="top: 0px; left: 768px"
			src="images/200px-Zalando_logo.svg.png">
	</div>

	<div>
		<form action="logout.do">
			<!-- <input type="submit" value="Logout" class="btn btn-default"> -->
			<button type="submit" class="btn btn-default">
				<span class="glyphicon glyphicon-log-out"></span>
			</button>
		</form>
	</div>
	<!-- 	</div> -->
	<!--  Hauptcontainer -->
	<c:if
		test="${currentSessionUser.rolle.rollenname == 'User'|| currentSessionUser.rolle.rollenname == null}">
		<c:redirect url="http://localhost:8080/Partnerprogramm/showLogin.do" />
	</c:if>

	
	<br>
	<div class="jumbotron" style="font-size: 14px">

		<center>
			<legend>Upload</legend>
			<form method="post" action="upload.do" enctype="multipart/form-data">
				Backlogdatei auswählen: <input class="filestyle" type="file"
					name="uploadFile" accept="text/csv" /> <br> <input
					class="btn btn-primary" type="submit" value="Upload" />
			</form>
		</center>
	</div>
</body>
</html>