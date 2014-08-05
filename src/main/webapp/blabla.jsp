<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>jQuery UI Autocomplete - Default functionality</title>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.10.2.js">
	
</script>
<script type="text/javascript"
	src="http://code.jquery.com/ui/1.10.3/jquery-ui.js">
	
</script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" />
<script>
	$(document).ready(function() {
		$(function() {
			$("#tags").autocomplete({
				source : function(request, response) {
					$.ajax({
						url : "autocomplete.do",
						type : "GET",
						data : {
							term : request.term
						},

						dataType : "json",

						success : function(data) {
							response(data);
						}
					});
				}
			});
		});
	});
</script>
</head>
<body>
	<div class="ui-widget">
		<label for="tags">Tags: </label> <input id="tags">
	</div>
</body>
</html>