<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="include.jsp"%>

<k:page htmlTitle="Залы">
	<jsp:attribute name="initScript">
    </jsp:attribute>
    <jsp:body>
        <section class="md-well well--inset-2">
            <div class="container">
                <div class="row row-md-reverse">
                    <div class="col-md-8 relative">
                        <h2 class="offset-6">${cityName}</h2>
                        <c:forEach items="${gyms}" var="item">
                            <article class="news-post">
                                <img class="sm-max-width" src="${item.image}" alt="">
                                <time datetime="2001-05-15">${item.gym.address}</time>
                                <h2><a href="#">${item.gym.name}</a> </h2>

                                <p class="text-md-left"></p>

                                <div class="text-md-right postfix-1 offset-4">
                                    <a href="#" class="link">ПОДРОБНЕЕ</a>
                                </div>
                            </article>
                            <div class="divider divider-small"></div>
                        </c:forEach>

                        <div class="divider divider-big divider-md-vertical divider-left md-visible"></div>
                    </div>
                    <div class="col-md-4">
                        <div class="row text-md-left inset-1 flow-offset-2">
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </jsp:body>
</k:page>
