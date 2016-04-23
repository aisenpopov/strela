<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="${title}">
	<jsp:attribute name="initScript">
		E.initArticleList();
	</jsp:attribute>
    <jsp:body>
        <div class="list-area sys-articles">
			<te:list list="${page.content}" isSortable="false"/>
        </div>
    </jsp:body>
</te:page>

