<%--
  Created by IntelliJ IDEA.
  User: Aisen
  Date: 27.04.2016
  Time: 18:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="include.jsp"%>

<t:page htmlTitle="Новости">
	<jsp:attribute name="initScript">
    </jsp:attribute>
    <jsp:body>
        <section class="well well--inset-1 page-content">
            <div class="container">
                <h1>новости</h1>

                <div class="row text-md-left flow-offset-1">
                    <c:forEach items="${newsList}" var="newsItem">
                        <div class="col-md-4 relative">
                            <!--News Post-->
                            <article class="news-post inset-1">
                                <img height="222" width="337" src="${newsItem.image}" alt="">
                                <time>${dateUtils:formatFull(newsItem.news.publish)}</time>
                                <h5><a href="/news/${newsItem.news.path}/">${newsItem.news.name}</a></h5>

                                <p>${newsItem.news.shortText}</p>
                                <a href="/news/${newsItem.news.path}" class="link">ПОДРОБНЕЕ</a>

                            </article>
                            <div class="divider divider-md-vertical divider-small divider-right offset-1"></div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>
        <section class="well--inset-1">
            <div class="container">
                <t:paging page="${page}" url="${pagerPath}" />
            </div>
        </section>
    </jsp:body>
</t:page>
