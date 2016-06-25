<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="${gym.id > 0 ? gym.name : 'Новый зал'}">
	<jsp:attribute name="initScript">
		E.initGymPage();
	</jsp:attribute>
    <jsp:body>

        <div class="gym-editor">
            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <h1 class="page-title txt-color-blueDark">
                        <i class="fa fa-pencil-square-o fa-fw fa"></i>${gym.id > 0 ? gym.name : 'Новый зал'}
                        <a href="/editor/${activeMenu.href}/" class="btn btn-default right-header-button" role="button">Список</a>
                        <a href="/editor/${activeMenu.href}/edit/" class="btn btn-info right-header-button margin-right-5" role="button">Добавить</a>
                    </h1>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <div class="jarviswidget jarviswidget-color-blueDark">
                        <header role="heading">
                            <h2>${gym.id > 0 ? gym.name : 'Новый зал'}</h2>
                        </header>
                        <div role="content">
                            <div class="widget-body no-padding">
                                <form:form class="smart-form" commandName="gym" role="form" method="post">
                                    <form:hidden path="id"/>
                                    <form:hidden path="article.id"/>
                                    <form:hidden path="longitude"/>
                                    <form:hidden path="latitude"/>

                                    <fieldset>
                                    	<te:baseEntityNamed isHideable="false"/>
                                        <section>
                                            <label class="label">Адрес <span>*</span></label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="address"/>
                                                <form:errors class="help-block error" path="address"/>
                                            </label>
                                        </section>
										
										<section>
                                            <label class="label">Город <span>*</span></label>
                                            <label class="input">
                                            	<form:input class="form-control" type="text" path="city"/>
												<form:errors class="help-block error" path="city"/>
                                            </label>
                                        </section>
                                        <section>
                                            <label class="label">Команда <span>*</span></label>
                                            <label class="input">
                                            	<form:input class="form-control" type="text" path="team"/>
												<form:errors class="help-block error" path="team"/>
                                            </label>
                                        </section>
                                        <section>
                                            <label class="label">Инструкторы</label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="instructors"/>
                                                <form:errors class="help-block error" path="instructors"/>
                                            </label>
                                        </section>
                                        <section>
                                            <label class="label">Текст</label>
                                            <label class="input">
                                                <form:textarea class="form-control wysiwyg" path="article.text"/>
                                            </label>
                                        </section>
                                    </fieldset>
                                    <footer>
                                        <button class="btn btn-primary" type="submit" name="save">
                                            <i class="fa fa-save"></i> Сохранить
                                        </button>
                                    </footer>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <c:if test="${gym.id != 0}">
                <div class="row">
                    <div class="col-sm-12 col-md-12 col-lg-6">
                        <div class="jarviswidget jarviswidget-color-blueDark">
                            <header role="heading">
                                <h2>Карта</h2>
                            </header>
                            <div role="content">
                                <div class="widget-body no-padding">
                                    <div id="map" style="height: 400px;"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row image-panel gym-preview">
                    <div class="col-sm-12 col-md-12 col-lg-6">
                        <div class="jarviswidget jarviswidget-color-blueDark">
                            <header role="heading">
                                <h2>Изображение превью</h2>
                            </header>
                            <div role="content">
                                <div class="widget-body no-padding">
                                    <jsp:include page="panel/uploadImagePanel.jsp">
                                        <jsp:param name="type" value="GYM_PREVIEW"/>
                                        <jsp:param name="entityId" value="${gym.id}"/>
                                        <jsp:param name="image" value="${gymImage}"/>
                                        <jsp:param name="isMultiple" value="false"/>
                                    </jsp:include>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row image-panel content-image">
                    <div class="col-sm-12 col-md-12 col-lg-6">
                        <div class="jarviswidget jarviswidget-color-blueDark">
                            <header role="heading">
                                <h2>Изображения для контента</h2>
                            </header>
                            <div role="content">
                                <div class="widget-body">
                                    <div class="superbox list-images">
                                        <k:ajaxUpdate id="image-list">
                                            <c:forEach items="${images}" var="image">
                                                <div class="superbox-list block-image" iid="${image.id}">
                                                    <img src="${image.image}" class="superbox-img" data-img="${image.image}" width="150"/>
                                                    <div class="code">#[image=${image.id}]</div>
                                                </div>
                                            </c:forEach>
                                        </k:ajaxUpdate>
                                        <div class="superbox-float"></div>
                                    </div>

                                    <jsp:include page="panel/uploadImagePanel.jsp">
                                        <jsp:param name="type" value="GYM_CONTENT"/>
                                        <jsp:param name="entityId" value="${gym.id}"/>
                                        <jsp:param name="crop" value="false"/>
                                        <jsp:param name="isMultiple" value="true"/>
                                    </jsp:include>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </jsp:body>
</te:page>