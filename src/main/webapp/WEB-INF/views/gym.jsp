<%--
  Created by IntelliJ IDEA.
  User: Aisen
  Date: 27.04.2016
  Time: 18:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="include.jsp"%>

<k:page htmlTitle="${gym.name}">
	<jsp:attribute name="initScript">
        S.initGymPage();
    </jsp:attribute>
    <jsp:body>
        <!-- map -->
        <section class="well--inset-2 md-well">
            <div class="container">
                <div class="rd-google-map">
                    <div id="map" class="rd-google-map__model" data-latitude="${gym.latitude}" data-longitude="${gym.longitude}"></div>
                </div>
            </div>
        </section>
        <!-- END map-->
        <div class="divider divider-big offset-2 md-visible"></div>

        <section class="md-well well--inset-1 gym">
            <div class="container">
                <h3>${gym.name}</h3>

                <!-- Terms List -->
                <dl class="terms-list">
                    <dd>${text}</dd>
                </dl>
                <!-- END Terms List -->
                <div class="row inset-1 flow-offset-2">
                    <div class="col-sm-preffix-3 col-sm-6 col-md-preffix-0 col-md-12 col-md-push-1">
                        <h5 class="offset-6">тренера:</h5>
                        <ul class="list">
                            <c:forEach items="${gym.instructors}" var="instructor">
                                <li>
                                    <div class="heading-6">${instructor.displayName}</div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </section>
    </jsp:body>
</k:page>
