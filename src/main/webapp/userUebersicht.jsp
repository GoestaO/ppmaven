<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="sorttable.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test</title>
</head>
<body>

	<script type="text/javascript">
		function filter(phrase, _id) {
			var words = phrase.value.toLowerCase().split(" ");
			var table = document.getElementById(_id);
			var ele;
			for ( var r = 1; r < table.rows.length; r++) {
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

	 <form>
	 Filter:
    	<b>Include:</b> <input name="filt" onkeyup="filter(this, 'userList', '1')" type="text">
    </form> 

	<table class="sortable" id="userList" border="1">
		<tr>
			<th>Nickname</th>
			<th>Vorname</th>
			<th>Nachname</th>
			<c:forEach items="${userList}" var="user">
				<tr>
					<td>${user.nick}</td>
					<td>${user.vorname}</td>
					<td>${user.nachname}</td>
					<td><a href='sm_klaerfallanzeige.php?id=".$id'>Bearbeiten</a>
				</tr>
			</c:forEach>
	</table>
</body>
</html>