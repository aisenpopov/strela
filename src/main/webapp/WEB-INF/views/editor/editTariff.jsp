<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="${tariff.id > 0 ? tariff.name : 'Новый тариф'}">
	<jsp:attribute name="initScript">
		E.initTariffPage();
	</jsp:attribute>
    <jsp:body>

        <div class="tariff-editor">
            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <h1 class="page-title txt-color-blueDark">
                        <i class="fa fa-pencil-square-o fa-fw fa"></i>${tariff.id > 0 ? tariff.name : 'Новый тариф'}
                        <a href="/editor/${activeMenu.href}/" class="btn btn-default right-header-button" role="button">Список</a>
                        <a href="/editor/${activeMenu.href}/edit/" class="btn btn-info right-header-button margin-right-5" role="button">Добавить</a>
                    </h1>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <div class="jarviswidget jarviswidget-color-blueDark">
                        <header role="heading">
                            <h2>${tariff.id > 0 ? tariff.name : 'Новый тариф'}</h2>
                        </header>
                        <div role="content">
                            <div class="widget-body no-padding">
                                <form:form class="smart-form" commandName="tariff" role="form" method="post">
                                    <form:hidden path="id"/>

                                    <fieldset>
                                    	<te:baseEntityNamed isHideable="false"/>

                                        <section>
                                            <label class="label">Зал <span>*</span></label>
                                            <label class="input">
                                            	<form:input class="form-control" type="text" path="gym"/>
												<form:errors class="help-block error" path="gym"/>
                                            </label>
                                        </section>
                                        <section>
                                            <label class="label">Дата истечения</label>
                                            <label class="input">
                                                <form:input class="form-control datepicker" type="text" path="expiration"/>
                                            </label>
                                        </section>
                                    </fieldset>
                                    <header role="heading">
                                        <h2>Цены</h2>
                                    </header>
                                    <fieldset>
                                        <section>
                                            <label class="label">Цена за разовое посещение</label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="priceOnce"/>
                                                <form:errors class="help-block error" path="priceOnce"/>
                                            </label>
                                        </section>
                                        <section>
                                            <label class="label">Цена за месяц</label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="priceMonth"/>
                                                <form:errors class="help-block error" path="priceMonth"/>
                                            </label>
                                        </section>
                                        <section>
                                            <label class="label">Цена за квартал</label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="priceQuarter"/>
                                                <form:errors class="help-block error" path="priceQuarter"/>
                                            </label>
                                        </section>
                                        <section>
                                            <label class="label">Цена за полгода</label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="priceHalfYear"/>
                                                <form:errors class="help-block error" path="priceHalfYear"/>
                                            </label>
                                        </section>
                                        <section>
                                            <label class="label">Цена за год</label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="priceYear"/>
                                                <form:errors class="help-block error" path="priceYear"/>
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
        </div>
    </jsp:body>
</te:page>