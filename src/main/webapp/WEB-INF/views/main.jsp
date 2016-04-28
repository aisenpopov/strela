<%--
  Created by IntelliJ IDEA.
  User: Aisen
  Date: 27.04.2016
  Time: 18:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="include.jsp"%>

<k:page htmlTitle="Главная">
	<jsp:attribute name="initScript">
        L.initMainPage();
    </jsp:attribute>
    <jsp:body>
        <div style="position: absolute; left: 50%; top: 50%; margin-top: -190px; margin-left: -188px;">
            <img src="/resources/img/logo380x376.jpg">
            <p style="text-align: center;">
                Скоро открытие...
            </p>
        </div>
    </jsp:body>
</k:page>
