<%--
  Created by IntelliJ IDEA.
  User: Aisen
  Date: 27.04.2016
  Time: 18:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="include.jsp"%>

<k:page htmlTitle="${staticPage.name}">
	<jsp:attribute name="initScript">
    </jsp:attribute>
    <jsp:body>
        <section class="md-well well--inset-1 text-md-left">
            <div class="container">
                <h3>${staticPage.name}</h3>

                <!-- Terms List -->
                <dl class="terms-list">
                    <dd>${text}</dd>
                </dl>
                <!-- END Terms List -->
            </div>
        </section>
    </jsp:body>
</k:page>
