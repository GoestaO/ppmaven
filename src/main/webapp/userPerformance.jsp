<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link href="css/design.css" rel="stylesheet" type="text/css">
<!-- <script type="text/javascript" src="js/sorttable.js"></script> -->

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Report</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"
	type="text/javascript">
	
</script>
<script type="text/javascript" src="javascript/sorttable.js"></script>

<script type="text/javascript" src="javascript/jquery.fixheadertable.js"></script>
<link rel="stylesheet" type="text/css" href="css/base.css" />
<link rel="stylesheet" type="text/css"
	href="css/jquery-ui/css/smoothness/jquery-ui-1.8.4.custom.css" />
</head>
<body>



	<table id='myTable' border="1">

		<thead>
			<tr>
				<th>Name</th>
				<th>Config</th>
				<th>Appdomain</th>
				<th>Partner</th>
				<th>WG-Pfad</th>
				<th>Saison</th>
				<th>Datum</th>
				<th>Bemerkung 1</th>
				<th>Bemerkung 2</th>
				<th>Bemerkung 3</th>
			</tr>
		</thead>

		<tbody>
			<c:forEach items="${userReport}" var="item">

				<tr>
					<td>${item.name}</td>
					<td>${item.config}</td>
					<td>${item.appdomainId}</td>
					<td>${item.partnerId}</td>
					<td>${item.cgPath}</td>
					<td>${item.saison}</td>
					<td><fmt:formatDate pattern="dd.MM.yyyy" value="${item.datum}" /></td>
					<td>${item.bemerkung1}</td>
					<td>${item.bemerkung2}</td>
					<td>${item.bemerkung3}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#myTable').fixheadertable({
				caption : 'Bearbeitungsstatistik',
				height : 700,
				resizeCol : true,
				sortable : true
			});
		});
	</script>
</body>
</html>