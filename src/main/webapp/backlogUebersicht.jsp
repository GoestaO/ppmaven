
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html; charset=ISO-8859-15"
	pageEncoding="ISO-8859-15" import="model.User"%>
<fmt:setLocale value="de_DE" />
<!DOCTYPE PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">
<link rel="shortcut icon" href="css/zalando-icon.ico">

<title>Backlogübersicht</title>

<!-- Styles -->
<!-- <link href="css/design.css" rel="stylesheet" type="text/css">-->
<link href="css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="css/bootstrap-select.css" rel="stylesheet" type="text/css">
<style type="text/css">
input,select {
	height: 30px;
	border: 1px solid #cccccc;
	border-radius: 4px;
	padding: 6px 12px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
	-webkit-transition: border-color ease-in-out 0.15s, box-shadow
		ease-in-out 0.15s;
	transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s;
}

input:focus {
	border-color: #FF6600;
	outline: 0;
	-webkit-box-shadow: inset 0 1px 1px rgba(255, 102, 0, 1), 0 0 8px
		rgba(255, 102, 0, 1);
	box-shadow: inset 0 1px 1px rgba(255, 102, 0, 1), 0 0 8px
		rgba(255, 102, 0, 1);

	/* 
	box-shadow: 0 0 5px rgba(255, 102, 0, 1);
	-webkit-box-shadow: 0 0 5px rgba(0, 0, 255, 1);
	-moz-box-shadow: 0 0 5px rgba(0, 0, 255, 1); */
}

/* input {
  display: block;
  width: 100%;
  height: 34px;
  padding: 6px 12px;
  font-size: 14px;
  line-height: 1.428571429;
  color: #555555;
  vertical-align: middle;
  background-color: #ffffff;
  border: 1px solid #cccccc;
  border-radius: 4px;
  -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
          box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
  -webkit-transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s;
          transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s;
}

input:focus {
  border-color: #66afe9;
  outline: 0;
  -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(102, 175, 233, 0.6);
          box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(102, 175, 233, 0.6);
} */
</style>
<link href="css/table-fixed-header.css" rel="stylesheet" type="text/css">
<!-- <link rel="stylesheet" type="text/css" href="css/base.css" />
<link rel="stylesheet" type="text/css"
	href="css/jquery-ui/css/ui-lightness/jquery-ui-1.8.4.custom.css" />  -->
<link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css" />
<link rel="stylesheet" type="text/css" href="css/demo_table_jui.css" />
<link href="css/bootstrap.css" rel="stylesheet" type="text/css">

<link href="css/bootstrap-select.css" rel="stylesheet" type="text/css">
<link href="css/bootstrap-select.min.css" rel="stylesheet"
	type="text/css">
<link href="css/datatable-bootstrap.css" rel="stylesheet">


<!-- Skripte -->

<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script type="text/javascript" src="javascript/sorttable.js"></script>
<script src="javascript/bootstrap.js"></script>
<script src="javascript/bootstrap-select.js"></script>
<script src="javascript/bootstrap-select.min.js"></script>
<script type="text/javascript" src="javascript/tablefilter.js"></script>
<script type="text/javascript" src="javascript/sorttable.js"></script>
<script type="text/javascript" src="javascript/jquery.dataTables.js"></script>
<script type="text/javascript" src="javascript/jquery.FixedHeader.js"></script>
<script src="javascript/bootstrap-select.js"></script>
<script src="javascript/jquery.fixheadertable.js"></script>
<script src="javascript/table-fixed-header.js"></script>
<!-- <script src="javascript/datatable-bootstrap.js"></script> -->
<!-- 3.0 -->
<link
	href="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>


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
				<li class="active"><a href="getBacklogByPartner.do">Backlog</a></li>
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

	<c:if test="${currentSessionUser == null}">
		<c:redirect url="showLogin.do" />
	</c:if>

	<br>
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
	<div class="jumbotron" style="font-size: 12px">
		
		<table
			class="table table-striped table-bordered table-condensed table-fixed-header"
			id="dataTable" style="font-size: 12px">
			<thead class="header">
				<tr>
					<th>Identifier</th>
					<th>Config</th>
					<th>Anzahl</th>
					<th>WG-Pfad</th>
					<th>Partner</th>
					<th>Datum</th>
					<th>Saison</th>
					<th>Appdomain</th>
					<th>Bemerkung 1</th>
					<th>Bemerkung 2</th>
					<th>Bemerkung 3</th>
					<th>Bemerkung KAM</th>
					<th></th>
				</tr>
			</thead>
			<tbody style="height: 50%; overflow: scroll">
				<c:forEach items="${backlogList}" var="backlogArticle">
					<tr>
						<td>${backlogArticle.identifier}</td>
						<td>${backlogArticle.config}</td>
						<td>${backlogArticle.counter}</td>
						<td>${backlogArticle.cgPath}</td>
						<td>${backlogArticle.partnerId}</td>
						<td><fmt:formatDate pattern="dd.MM.yyyy"
								value="${backlogArticle.datum}" /></td>
						<td>${backlogArticle.saison}</td>
						<td>${backlogArticle.appdomainId}</td>
						<td>${backlogArticle.bemerkung1}</td>
						<td>${backlogArticle.bemerkung2}</td>
						<td>${backlogArticle.bemerkung3}</td>
						<td>${backlogArticle.bemerkungKAM}</td>
						<td><a
							href='showBacklogArticle.do?identifier=${backlogArticle.identifier}'><button
									class="btn btn-default">
									<span class="glyphicon glyphicon-edit"></span>
								</button></a></td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<td><input class="search_init" type="text" value="Identifier"
						name="search_identifier"></input></td>
					<td><input class="search_init" type="text" value="Config"
						name="search_config"></input></td>
					<td><input class="search_init" type="text" value="Anzahl"
						name="search_config"></input></td>
					<td><input class="search_init" type="text" value="WG-Pfad"
						name="search_cgPath"></input></td>
					<td><input class="search_init" type="text" value="Partner"
						name="search_partner"></input></td>
					<td><input class="search_init" type="text" value="Datum"
						name="search_date"></input></td>
					<td><input class="search_init" type="text" value="Saison"
						name="search_season"></input></td>
					<td><input class="search_init" type="text" value="Appdomain"
						name="search_appdomain"></input></td>
					<td><input class="search_init" type="text" value="Bemerkung1"
						name="search_bem1"></input></td>
					<td><input class="search_init" type="text" value="Bemerkung2"
						name="search_bem2"></input></td>
					<td><input class="search_init" type="text" value="Bemerkung3"
						name="search_bem3"></input></td>
					<td><input class="search_init" type="text"
						value="Bemerkung KAM" name="search_bemKAM"></input></td>
					<td></td>
				</tr>
			</tfoot>

		</table>

	</div>
	<!-- <script type="text/javascript">
		$(document).ready(function() {
			// make the header fixed on scroll
			$('#dataTable').fixedHeader();
		});
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#dataTable').fixheadertable({
				caption : 'Backlog-?bersicht',
				sortable : true
			});
		});
	</script> -->




	<script type="text/javascript">
		var asInitVals = new Array();
		var oTable;
		$(document).ready(function drawTable() {

			oTable = $('#dataTable').dataTable({
				"bFilter" : true,
				"bStateSave" : true,
				"iDisplayLength" : 100,
				"oLanguage" : {
					"sSearch" : "Filter:",
					"sLengthMenu" : "Anzeige von _MENU_ Artikeln pro Seite",
					"sZeroRecords" : "Leider keine Artikel gefunden",
					"sInfo" : "Zeige _START_ bis _END_ von _TOTAL_ Artikeln",
					"sInfoEmpty" : "Zeige 0 bis 0 von 0 Artikeln",
					"sInfoFiltered" : "(Gefiltert von gesamt _MAX_ Artikeln)",
					"oPaginate" : {
						"sNext" : "Weiter",
						"sPrevious" : "Zurück"
					}
				}
			
			
			}
			
			);

			
			$("tfoot input").keyup(function() {
				/* Filter on the column (the index) of this element */
				oTable.fnFilter(this.value, $("tfoot input").index(this));
			});

			/*
			 * Support functions to provide a little bit of 'user friendlyness' to the textboxes in 
			 * the footer
			 */
			$("tfoot input").each(function(i) {
				asInitVals[i] = this.value;
			});

			$("tfoot input").focus(function() {
				if (this.className == "search_init") {
					this.className = "";
					this.value = "";
				}
			});

			$("tfoot input").blur(function(i) {
				if (this.value == "") {
					this.className = "search_init";
					this.value = asInitVals[$("tfoot input").index(this)];
				}
			});
			$.extend($.fn.dataTableExt.oStdClasses, {
				"sWrapper" : "dataTables_wrapper form-inline"
			});
		});
	</script>

</body>
</html>