<%--
  Created by IntelliJ IDEA.
  User: Aisen
  Date: 27.04.2016
  Time: 18:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="include.jsp"%>

<t:page htmlTitle="${news.name}">
	<jsp:attribute name="initScript">
    </jsp:attribute>
    <jsp:body>
        <section class="md-well well--inset-1 text-md-left">
            <div class="container">
                <h3>${news.name}</h3>

                <!-- Terms List -->
                <dl class="terms-list">
                    <dt class="heading-5">${dateUtils:formatFull(news.publish)}</dt>

                    <dd>${text}</dd>
                </dl>
                <!-- END Terms List -->
            </div>
        </section>
    </jsp:body>
</t:page>
