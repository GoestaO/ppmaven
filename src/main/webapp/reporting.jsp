<%@ page contentType="text/html; charset=ISO-8859-15"
	pageEncoding="ISO-8859-15"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<link rel="stylesheet" type="text/css" href="css/bootstrap-select.css">
<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
<title>Reportingseite</title>


<script>
	$(function() {
		$("#datepicker").datepicker({
			dateFormat : 'dd.mm.yy'
		});
	});
</script>

<script>
	jQuery(function($) {
		$.datepicker.regional['de'] = {
			clearText : 'löschen',
			clearStatus : 'aktuelles Datum löschen',
			closeText : 'schließen',
			closeStatus : 'ohne Änderungen schließen',
			prevText : '<zurück', prevStatus: 'letzten Monat zeigen',
            nextText: 'Vor>',
			nextStatus : 'nächsten Monat zeigen',
			currentText : 'heute',
			currentStatus : '',
			monthNames : [ 'Januar', 'Februar', 'März', 'April', 'Mai', 'Juni',
					'Juli', 'August', 'September', 'Oktober', 'November',
					'Dezember' ],
			monthNamesShort : [ 'Jan', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun',
					'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dez' ],
			monthStatus : 'anderen Monat anzeigen',
			yearStatus : 'anderes Jahr anzeigen',
			weekHeader : 'Wo',
			weekStatus : 'Woche des Monats',
			dayNames : [ 'Sonntag', 'Montag', 'Dienstag', 'Mittwoch',
					'Donnerstag', 'Freitag', 'Samstag' ],
			dayNamesShort : [ 'So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa' ],
			dayNamesMin : [ 'So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa' ],
			dayStatus : 'Setze DD als ersten Wochentag',
			dateStatus : 'Wähle D, M d',
			dateFormat : 'dd.mm.yy',
			firstDay : 1,
			initStatus : 'Wähle ein Datum',
			isRTL : false
		};
		$.datepicker.setDefaults($.datepicker.regional['de']);
	});
</script>
<script>
	$(window).on('load', function() {

		$('.selectpicker').selectpicker({
			'selectedText' : 'cat'
		});

		// $('.selectpicker').selectpicker('hide');
	});
</script>

<script>
	$(document).ready(function() {
		$.getJSON("getPartnerList.do", function(options) {
			var dropdown = $('#partnerdropdown');
			$('>option', dropdown).remove(); // Clean old options first.
			if (options) {
				$.each(options, function(index, item) {
					dropdown.append($('<option/>').text(item));
				});
			}
		});
	});
</script>
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
			<li><c:if
					test="${currentSessionUser.rolle.rollenname != 'User'|| currentSessionUser.rolle.rollenname != null}">
					<li class="active"><a href="reporting.jsp">Reporting</a>
					<li><a href="upload.jsp">Upload</a></li>
				</c:if></li>
		</ul>
		<div class="navbar-inner pull-right padding-top">
			<img style="top: 0px; left: 768px"
				src="images/200px-Zalando_logo.svg.png">
		</div>
	</div>
	</nav>

	<!--  Hauptcontainer -->
	<c:if
		test="${currentSessionUser.rolle.rollenname == 'User'|| currentSessionUser.rolle.rollenname == null}">
		<c:redirect url="http://localhost:8080/Partnerprogramm/showLogin.do" />
	</c:if>


	<!-- <br>
	<br>
	<br> -->
	<div class="jumbotron" style="font-size: 14px">

		<center>
			<table class="table table-condensed">
				<tr>
					<td>
						<div>
							<h3>Leistungsreport</h3>
							Bitte Tag angeben:
							<form action="getLeistungen.do" method="post">
								<p>
									Datum: <input type="text" class="form-control"
										style="width: 50%" id="datepicker" name="date" />
								</p>
								<input class="btn btn-primary" type="submit" name="Ok"
									value="Ok">
							</form>
						</div>
					</td>
					<td>
						<h3>Partnerreport</h3> Alle offenen Artikel ausgeben

						<form action="getOpenArticlesReport.do">
							<!-- <p>
			Datum: <input type="text" id="datepicker" name="date" />
		</p> -->
							<input class="btn btn-primary" type="submit" name="Ok" value="Ok">
						</form>
					</td>
				<tr>
					<td>
						<div>
							<h3>KAM Report</h3>

							Alle Artikel mit Kommentaren ausgeben

							<form action="getKeyAccountReport.do">

								<input class="btn btn-primary" type="submit" name="Ok"
									value="Ok">
							</form>
						</div>
					</td>
					<td>
						<div>
							<h3>Alle bereits gepflegten Artikel eines Partners</h3>
							Wähle Partner:
							<form action="getEditedArticlesReport.do">
								<select name="partnerdropdown" id="partnerdropdown">
									<option></option>
								</select> <input class="btn btn-primary" type="submit" name="Ok"
									value="Ok">
							</form>
						</div>
					</td>
					<td></td>
					<td>

						<h3>Upload Report</h3> Alle hochgeladenen Artikel sortiert nach
						Datum und Partner
						<form action="report.do">
							<input class="btn btn-primary" type="submit" name="Ok" value="Ok">
						</form>

					</td>
			</table>
		</center>
	</div>
</body>
</html>