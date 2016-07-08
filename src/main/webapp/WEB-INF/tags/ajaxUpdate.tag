<%@tag description="initComponent" pageEncoding="UTF-8"%>

<%@attribute name="id" required="true" type="java.lang.String" %>
<%@attribute name="cssClass" required="false" type="java.lang.String" %>

<div id="${id}" ${not empty cssClass ? 'class='.concat(cssClass) : ''}>
	<jsp:doBody/>
</div>


