<%@ tag pageEncoding="utf-8" %>
<%@ include file="../views/include.jsp" %>

<%@ attribute name="initScript" fragment="true" %>
<%@ attribute name="htmlTitle" type="java.lang.String" %>
<%@ attribute name="htmlDescription" type="java.lang.String" %>
<%@ attribute name="htmlKeywords" type="java.lang.String" %>

<!DOCTYPE html>
<html class="boxed smoothscroll wow-animation">
<head>
    <title>${htmlTitle} | Strela</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="author" content="Strela"/>
    <meta name="description" content="${htmlDescription}"/>
    <meta name="keywords" content="${htmlKeywords}"/>

	<meta name="format-detection" content="telephone=no"/>
	<meta name="viewport" content="width=device-width, height=device-height, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>

	<!-- Stylesheets -->
	<link rel="icon" href="/resources/favicon.ico" type="/favicon.ico"/>
	<link href='//fonts.googleapis.com/css?family=Ubuntu:300%7CRoboto+Condensed:700' rel='stylesheet' type='text/css'>
	<link rel="stylesheet" href="/resources/css/style.css">
	<link rel="stylesheet" href="/resources/css/fix.css">

	<!--[if lt IE 10]>
	<script src="/resources/js/html5shiv.min.js"></script>
	<![endif]-->
	<!-- Core Scripts -->
	<script src="/resources/js/core.min.js"></script>
	<script src="/resources/js/strela.js"></script>
</head>
<body>

	<div class="page text-center">

		<!--For older internet explorer-->
		<div class="old-ie"
			 style='background: #212121; padding: 10px 0; box-shadow: 3px 3px 5px 0 rgba(0,0,0,.3); clear: both; text-align:center; position: relative; z-index:1;'>
			<a href="http://windows.microsoft.com/en-US/internet-explorer/..">
				<img src="/resources/img/ie8-panel/warning_bar_0000_us.jpg" height="42" width="820"
					 alt="You are using an outdated browser. For a faster, safer browsing experience, upgrade for free today."/>
			</a>
		</div>
		<!--END block for older internet explorer-->

		<jsp:include page="../views/header.jsp" />

		<main class="page-content">
			<jsp:doBody/>
		</main>

		<jsp:include page="../views/footer.jsp" />
	</div>


</body>

	<!-- Additional Functionality Scripts -->
	<script src="/resources/js/script.js"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			S.initBase();
			<jsp:invoke fragment="initScript" />
		});
	</script>
</html>