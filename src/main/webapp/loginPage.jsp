<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <link href="css/design.css" rel="stylesheet" type="text/css"> -->
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1256">
<title>Login Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">
<title>Artikelanzeige</title>
<link rel="shortcut icon" href="css/zalando-icon.ico">
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script type="text/javascript" src="javascript/bootstrap-select.js"></script>

<!-- 3.0 -->

<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="javascript/bootstrap-select.js"></script>
<script type="text/javascript" src="javascript/jqBootstrapValidation.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap-select.css">
<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
</head>
<body>
	<script>
		$(window).on('load', function() {

			$('.selectpicker').selectpicker({
				'selectedText' : 'cat'
			});

			// $('.selectpicker').selectpicker('hide');
		});
	</script>

	<div class="pull-right padding-top">
		<img style="top: 0px; left: 768px"
			src="images/200px-Zalando_logo.svg.png">
	</div>
	<br>
	<br>
	<br>
	<div class="jumbotron" style="font-size: 14px">
		<!-- <legend>Login</legend> -->
		<div>
			<form class="form-horizontal" role="form" action="login.do"
				method="POST">

				<div class="row">
					<div class="col-xs-4">
						<label class="control-label" for="nick">Nutzername</label> <input
							class="span6 form-control" id="nick" type="text" name="nick"
							placeholder="Nutzername" required="required" />
					</div>
				</div>
				<div class="row">
					<div class="col-xs-4">
						<label class="control-label" for="pw">Passwort</label> <input
							class="form-control" type="password" name="pw" id="pw"
							placeholder="Passwort" required="required" />
					</div>
				</div>
				<label class="control-label">Bitte wähle deine Partner</label>
				<div class="row">
					<div class="col-xs-24">
						<c:forEach items="${partnerList}" var="row">
							${row}

						</c:forEach>
						<%-- <c:forEach items="${partnerList}" var="partnerID">
							<label class="checkbox-inline no_indent"> <input
								type="checkbox" id="partner_group[]" name="partner_group[]"
								value="${partnerID}"> ${partnerID}
							</label>
						</c:forEach> --%>
					</div>
				</div>
				<br>
				<div class="controls">
					<input class="btn btn-primary" type="submit" value="submit"
						disabled>
				</div>
				<script>
					var checkboxes = $("input[type='checkbox']"), submitButt = $("input[type='submit']");

					checkboxes
							.click(function() {
								submitButt.attr("disabled", !checkboxes
										.is(":checked"));
							});
				</script>
			</form>
		</div>
	</div>
</body>
</html>