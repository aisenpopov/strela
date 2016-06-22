<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="include.jsp"%>

<k:page htmlTitle="Залы">
	<jsp:attribute name="initScript">
    </jsp:attribute>
    <jsp:body>
        <section class="md-well well--inset-2">
            <div class="container">
                <div class="row">
                    <div class="col-md-4">
                        <div class="row text-md-left inset-1 flow-offset-2">
                            <div class="col-sm-preffix-3 col-sm-6 col-md-preffix-0 col-md-12 col-md-push-1">
                                <h3 class="offset-6">города:</h3>
                                <ul class="list">
                                    <c:forEach items="${citiesHasGym}" var="city">
                                        <li>
                                            <div class="heading-6"><a class="${(fn:contains(currentHref, 'cityId='.concat(city.id)) ? 'active' : '')}" href="/gym/?cityId=${city.id}">${city.name}</a></div>
                                        </li>
                                    </c:forEach>
                                </ul>

                                <div class="divider divider-small offset-4 md-visible"></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-8 relative">
                        <h2 class="offset-6">${cityName}</h2>
                        <c:forEach items="${gyms}" var="item">
                            <article class="news-post">
                                <a href="#">
                                    <img class="sm-max-width" src="${item.image}" alt="">
                                </a>
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
                </div>
            </div>
        </section>
    </jsp:body>
</k:page>