<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript" src="sorttable.js"></script>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</style>
<link href="css/design.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/sorttable.js"></script>
<title>Backlog Partnerprogramm</title>
</head>
<body>

	</style>

	<script type="text/javascript">
		function filter(phrase, _id) {
			var words = phrase.value.toLowerCase().split(" ");
			var table = document.getElementById(_id);
			var ele;
			for ( var r = 2; 2; r++) {
				//for ( var r = 1; r < table.rows.length; r++) {
				ele = table.rows[r].innerHTML.replace(/<[^>]+>/g, "");
				var displayStyle = 'none';
				for ( var i = 0; i < words.length; i++) {
					if (ele.toLowerCase().indexOf(words[i]) >= 0)
						displayStyle = '';
					else {
						displayStyle = 'none';
						break;
					}
				}
				table.rows[r].style.display = displayStyle;
			}
		}
	</script>

	<!-- <form>
		<b>Filter:</b> <input name="filt"
			onkeyup="filter(this, 'partnerListe', '1')" type="text">
	</form> -->
	<h3>Wähle deine Partner</h2>
	
	<div style="float: center; width: 600px; margin: auto">
		<form action = "getBacklogByPartner.do" method = "POST">
			<c:forEach items="${partnerList}" var="partnerID">
				<div-class= "checkboxPartner"> <input class=checkbox
					" type="checkbox" name="partner_group[]" value="${partnerID}" />${partnerID}</div-class>				
			</c:forEach>
			<br> <input type="submit" value="Abschicken">
			
		</form>
	</div>
</body>
</html>