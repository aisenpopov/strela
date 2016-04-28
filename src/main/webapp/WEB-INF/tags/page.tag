<%@ tag pageEncoding="utf-8" %>
<%@ include file="../views/include.jsp" %>

<%@ attribute name="initScript" fragment="true" %>
<%@ attribute name="htmlTitle" type="java.lang.String" %>
<%@ attribute name="htmlDescription" type="java.lang.String" %>
<%@ attribute name="htmlKeywords" type="java.lang.String" %>

<!DOCTYPE html>
<html style="height: 100%;">
<head>
    <title>${htmlTitle} | Strela</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="author" content="Strela"/>
    <meta name="description" content="${htmlDescription}"/>
    <meta name="keywords" content="${htmlKeywords}"/>
</head>
<body style="height: 100%;">
	<jsp:include page="../views/header.jsp" />

	<jsp:doBody/>

	<jsp:include page="../views/footer.jsp" />

	<script type="text/javascript">
	    $(document).ready(function() {
	        L.initBase();
	        <jsp:invoke fragment="initScript" />
	    });
	</script>

</body>
</html>