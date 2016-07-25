<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="include.jsp" %>

<header class="page-header">
    <!-- RD Navbar -->
    <div class="rd-navbar-wrap">
        <nav class="rd-navbar" data-rd-navbar-lg="rd-navbar-static">
            <div class="rd-navbar-inner">
                <!-- RD Navbar Panel -->
                <div class="rd-navbar-panel">

                    <!-- RD Navbar Toggle -->
                    <button class="rd-navbar-toggle" data-rd-navbar-toggle=".rd-navbar"><span></span></button>
                    <!-- END RD Navbar Toggle -->

                    <!-- RD Navbar Brand -->
                    <div class="rd-navbar-brand">
                        <a href="/" class="brand-name primary-color">
                            <span class="text-bold">strela</span>
                        </a>
                    </div>
                    <!-- END RD Navbar Brand -->
                </div>
                <!-- END RD Navbar Panel -->

                <div class="rd-navbar-nav-wrap">
                    <!-- RD Navbar Nav -->
                    <ul class="rd-navbar-nav">
                        <li class="${currentHref == '/' ? 'active' : ''}">
                            <a href="/">Главная</a>
                        </li>
                        <li class="${(fn:contains(currentHref, 'gym') ? 'active' : '')}">
                            <a class="sys-change-city" href="/gym/">Залы</a>
                            <ul class="rd-navbar-dropdown">
                                <c:forEach items="${citiesHasGym}" var="city">
                                    <li>
                                        <a data-city-id="${city.id}" class="sys-change-city" href="/gym/?cityId=${city.id}">${city.name}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </li>
                        <li class="${(fn:contains(currentHref, 'news') ? 'active' : '')}">
                            <a href="/news/">Новости</a>
                        </li>
                        <sec:authorize access="isAnonymous()">
                            <li class="${(fn:contains(currentHref, 'login') ? 'active' : '')}">
                                <a href="/account/login">Войти</a>
                            </li>
                        </sec:authorize>
                        <sec:authorize access="isAuthenticated()">
                            <li class="${(fn:contains(currentHref, 'account') ? 'active' : '')}">
                                <a href="/account/">Личный кабинет</a>
                                <ul class="rd-navbar-dropdown">
                                    <sec:authorize access="hasAnyRole('ROLE_INSTRUCTOR', 'ROLE_ADMIN')">
                                        <li>
                                            <a href="#">Оплата</a>
                                            <ul class="rd-navbar-dropdown">
                                                <li>
                                                    <a href="/account/balance/">Мой баланс</a>
                                                </li>
                                                <li>
                                                    <a href="/editor/payment/">Платежи</a>
                                                </li>
                                                <li>
                                                    <a href="/editor/payed_status/">Даты истечения</a>
                                                </li>
                                                <li>
                                                    <a href="/editor/transaction/">Списания</a>
                                                </li>
                                                <li>
                                                    <a href="/editor/tariff/">Тарифы</a>
                                                </li>
                                                <li>
                                                    <a href="/editor/coupon/">Купоны</a>
                                                </li>
                                            </ul>
                                        </li>
                                        <li>
                                            <a href="#">Справочники</a>
                                            <ul class="rd-navbar-dropdown">
                                                <li>
                                                    <a href="/editor/athlete/">Атлеты</a>
                                                </li>
                                                <li>
                                                    <a href="/editor/team/">Команды</a>
                                                </li>
                                                <li>
                                                    <a href="/editor/gym/">Залы</a>
                                                </li>
                                            </ul>
                                        </li>
                                    </sec:authorize>
                                    <li>
                                        <a class="logout" href="/account/logout">Выйти</a>
                                    </li>
                                </ul>
                            </li>
                        </sec:authorize>
                    </ul>
                    <!-- END RD Navbar Nav -->

                    <!-- RD Navbar Search -->
                    <div class="rd-navbar-search">
                        <form class="rd-navbar-search-form" action="search.php" method="GET">
                            <label class="rd-navbar-search-form-input">
                                <input type="text" name="s" placeholder="" autocomplete="off"/>
                            </label>
                            <button class="rd-navbar-search-form-submit" type="submit"></button>
                        </form>
                        <span class="rd-navbar-live-search-results"></span>
                        <button class="rd-navbar-search-toggle" data-rd-navbar-toggle=".rd-navbar-search"></button>
                    </div>
                    <!-- END RD Navbar Search -->
                </div>
            </div>
        </nav>
    </div>
    <!-- END RD Navbar -->
    <c:if test="${not empty showBanner && showBanner}">
        <div class="container md-well well--inset-2">
            <!-- Owl Carousel -->
            <div class="owl-carousel-wrapper">
                <div class="owl-carousel"
                     data-nav="true"
                     data-autoplay="true"
                     data-autoplay-timeout="7000"
                     data-autoplay-speed="500"
                     data-nav-speed="500">
                    <c:forEach items="${bannerImageList}" var="item">
                        <div class="owl-item">
                            <!--News Post-->
                            <c:if test="${item.bannerImage.type == 'advert'}">
                                <a href="/${item.bannerImage.link}/">
                            </c:if>
                            <article class="news-post text-center">
                                <img height="596" width="1170" src="${item.image}" alt="">
                                <c:if test="${item.bannerImage.type == 'advert'}">
                                    <h4 class="advert-header">${item.bannerImage.name}</h4>
                                </c:if>
                            </article>
                            <c:if test="${item.bannerImage.type == 'advert'}">
                                </a>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <!-- END Owl Carousel -->
        </div>
        <div class="divider divider-big"></div>
    </c:if>
</header>