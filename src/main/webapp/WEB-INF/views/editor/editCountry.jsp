<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="${country.id > 0 ? country.name : 'Новая страна'}">
	<jsp:attribute name="initScript">
		E.initCountryPage();
	</jsp:attribute>
    <jsp:body>

        <div class="country-editor">
            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <h1 class="page-title txt-color-blueDark">
                        <i class="fa fa-pencil-square-o fa-fw fa"></i>${country.id > 0 ? country.name : 'Новая страна'}
                        <a href="/editor/${activeMenu.href}/" class="btn btn-default right-header-button" role="button">Список</a>
                        <a href="/editor/${activeMenu.href}/edit/" class="btn btn-info right-header-button margin-right-5" role="button">Добавить</a>
                    </h1>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <div class="jarviswidget jarviswidget-color-blueDark">
                        <header role="heading">
                            <h2>${country.id > 0 ? country.name : 'Новая страна'}</h2>
                        </header>
                        <div role="content">
                            <div class="widget-body no-padding">
                                <form:form class="smart-form" commandName="country" role="form" method="post">
                                    <form:hidden path="id"/>

                                    <fieldset>
                                    	<te:baseEntityNamed isHideable="false"/>
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
        </div>
    </jsp:body>
</te:page>