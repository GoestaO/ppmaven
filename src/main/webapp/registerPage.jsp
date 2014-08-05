<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="./design.css" rel="stylesheet" type="text/css">
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
<link rel="shortcut icon" href="css/zalando-icon.ico">
<title>Registrierung</title>
</head>
<body>
	<div class="pull-right padding-top">
		<img style="top: 0px; left: 768px"
			src="images/200px-Zalando_logo.svg.png">
	</div>

	<div class="jumbotron" style="font-size: 14px">
		<form class="form-horizontal" role="form" action="register.do"
			method="POST">
			<legend>Registrierung</legend>

			<div class="row">
				<div class="col-xs-2">
					<label class="control-label" for="nick">Nutzername</label> <input
						class="span6 form-control" required = "required" id="nick" type="text" name="nick"
						placeholder="Nutzername" />
				</div>
			</div>

			<div class="row">
				<div class="col-xs-2">
					<label class="control-label" for="vorname">Vorname</label> <input
						class="span6 form-control" required = "required" id="vorname" type="text" name="vorname"
						placeholder="Vorname" />
				</div>
			</div>

			<div class="row">
				<div class="col-xs-2">
					<label class="control-label" for="nachname">Nachname</label> <input
						class="span6 form-control" required = "required" id="nachname" type="text"
						name="nachname" placeholder="Nachname" />
				</div>
			</div>
			<div class="row">
				<div class="col-xs-2">
					<label class="control-label" for="pw">Passwort</label> <input
						class="form-control" required = "required" type="password" name="pw" id="pw"
						placeholder="Passwort" />
				</div>
			</div>


			<br>
			<div class="controls">
				<input class="btn btn-primary" type="submit" value="submit">
			</div>

		</form>
	</div>

	<!-- <table>
		<tr>

			<form action="register.do" method="POST">
				<td>Nutzername</td>
				<td><input type="text" name="nick" /><br></td>
		</tr>
		<tr>
			<td>Vorname</td>
			<td><input type="text" name="vorname" /><br></td>
		</tr>
		<tr>
			<td>Nachname</td>
			<td><input type="text" name="nachname" /><br></td>
		</tr>
		<tr>
			<td>Passwort</td>
			<td><input type="password" name="pw" /></td>
			<td></td>
			<td><input type="submit" value="submit"></td>

		</tr>
		</form>
	</table> -->
</body>
</html>