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
                        <a href="index.html" class="brand-name primary-color">
                            <span class="text-bold">strela</span>
                        </a>
                    </div>
                    <!-- END RD Navbar Brand -->
                </div>
                <!-- END RD Navbar Panel -->

                <div class="rd-navbar-nav-wrap">
                    <!-- RD Navbar Nav -->
                    <ul class="rd-navbar-nav">
                        <li class="active">
                            <a href="/">Главная</a>
                        </li>
                        <li>
                            <a href="#">Залы</a>
                        </li>
                        <li>
                            <a href="#">Новости</a>
                        </li>
                        <li>
                            <a href="#">Контакты</a>
                        </li>
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
    <c:if test="${showBanner}">
        <div class="container md-well well--inset-2">
            <!-- Owl Carousel -->
            <div class="owl-carousel"
                 data-nav="true"
                 data-autoplay="true"
                 data-autoplay-timeout="7000">
                <c:forEach items="${bannerImageList}" var="item">
                    <div class="owl-item">
                        <!--News Post-->
                        <article class="news-post text-center">
                            <img height="596" width="1170" src="${item.image}" alt="">
                        </article>
                    </div>
                </c:forEach>
            </div>
            <!-- END Owl Carousel -->
        </div>
        <div class="divider divider-big"></div>
    </c:if>
</header>