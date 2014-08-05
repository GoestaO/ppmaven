<%@ page contentType="text/html; charset=ISO-8859-15"
	pageEncoding="ISO-8859-15"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">
<title>Artikelanzeige</title>

<!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> -->

<!--   <script type="text/javascript" -->
<!-- 	src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>  -->
<!-- 	<script src="javascript/jquery.autocomplete.js"></script> -->
<!-- <script type="text/javascript" -->
<!-- 	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script> -->
<script type="text/javascript" src="javascript/bootstrap-select.js"></script>

<link rel="stylesheet" type="text/css" href="css/bootstrap-select.css">

<!-- <link rel="shortcut icon" href="css/zalando-icon.ico"> -->
<!-- <!-- 3.0 -->

<link href="css/bootstrap.css" rel="stylesheet" type="text/css">

<!-- <!-- <script -->

<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>

<!-- <!-- <link href="./design.css" rel="stylesheet" type="text/css"> -->

<!-- <!--  <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"> -->

<!-- <!-- <script src="http://code.jquery.com/jquery-1.9.1.js"></script> -->

<!-- <!-- <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script> -->

<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css">
</head>
<body>
	<div class="pull-right padding-top">
		<img style="top: 0px; left: 768px"
			src="images/200px-Zalando_logo.svg.png">

	</div>
	<div>
		<form action="logout.do">
			<input type="submit" value="Logout"
				class="btn btn-default navbar-btn">
		</form>
	</div>

	<br>
	<br>
	<br>
	<div class="jumbotron">
		<form
			onSubmit="if(!confirm('Artikelstatus aktualisieren?')){return false;}"
			action='updateArticle.do' method="GET" name="statusUpdate">

			<table class="table table-bordered" style="font-size: 12px">
				<thead>
					<tr>
						<th>Identifier</th>
						<th>Config</th>
						<th>WG-Pfad</th>
						<th>Partner</th>
						<th>Datum</th>
						<th>Saison</th>
						<th>Appdomain</th>
						<th>Bemerkung 1</th>
						<th>Bemerkung 2</th>
						<th>Bemerkung 3</th>
						<th>Bemerkung KAM</th>
						<th>Neuer Status</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>${backlogArticle.identifier}</td>
						<td>${backlogArticle.config}</td>
						<td>${backlogArticle.cgPath}</td>
						<td>${backlogArticle.partnerId}</td>
						<td><fmt:formatDate pattern="dd.MM.yyyy"
								value="${backlogArticle.datum}" /></td>
						<td><p>
								<select name="saison" type="hidden" class="form-control"
									data-width="100px">
									<option selected="selected">${backlogArticle.saison}</option>
									<c:forEach items="${seasons}" var="season">
										<option>${season}</option>
									</c:forEach>
								</select>
							</p></td>
						<td>${backlogArticle.appdomainId}</td>
						<td><textarea class="form-control" style="font-size: 12px"
								name="bemerkung1" id="bemerkung1">${backlogArticle.bemerkung1} </textarea></td>
						<td><textarea class="form-control" style="font-size: 12px"
								name="bemerkung2" id="bemerkung2">${backlogArticle.bemerkung2} </textarea></td>
						<td><textarea class="form-control" style="font-size: 12px"
								name="bemerkung3" id="bemerkung3"> ${backlogArticle.bemerkung3} </textarea></td>
						<td><textarea class="form-control" style="font-size: 12px"
								name="bemerkungKAM"> ${backlogArticle.bemerkungKAM} </textarea></td>
						<td><p>
								<select class="form-control" data-width="75px" name="status"
									type="hidden">
									<option selected="selected">offen</option>
									<option>fertig</option>
								</select>
							</p></td>
						<td>
							<button class="btn btn-default">
								<span class="glyphicon glyphicon-ok"></span>
							</button>
						</td>
					</tr>
				</tbody>
			</table>
			<input type="hidden" name="id" value="${backlogArticle.identifier}">
		</form>
	</div>

	<!-- 	<script>
		// $("#bemerkung1").autocomplete("autocomplete.do"); //
		$("#bemerkung2").autocomplete("autocomplete.do"); //
		$("#bemerkung3").autocomplete("autocomplete.do");
	</script> -->
	<script>
		$(function() {
			$("#bemerkung1").autocomplete(
					{
						source : function(request, response) {
							$.getJSON("autocomplete.do", {
								term : request.term
							}, response);
						},
						minLength : 2,
						select : function(event, ui) {
							log(ui.item ? "Selected: " + ui.item.value
									+ " aka " + ui.item.id
									: "Nothing selected, input was "
											+ this.value);
						}
					});
		});
	</script>
</body>
</html>