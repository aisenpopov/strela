<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="Регионы регистрации">
	<jsp:attribute name="initScript">
		E.initRegistrationRegionList();
	</jsp:attribute>
    <jsp:body>
        <div class="list-area sys-registration-regions">
			<te:list list="${page.content}" isSortable="false"/>
        </div>
    </jsp:body>
</te:page>

