<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="Слайдер на главной">
	<jsp:attribute name="initScript">
		E.initBannerImageList();
	</jsp:attribute>
    <jsp:body>
        <div class="list-area sys-banner-images">
			<te:list list="${page.content}"/>
        </div>
    </jsp:body>
</te:page>

