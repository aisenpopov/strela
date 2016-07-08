<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="${transaction.id > 0 ? 'Списание' : 'Новое списание'}">
	<jsp:attribute name="initScript">
        E.initTransactionPage();
	</jsp:attribute>
    <jsp:body>
        <div class="transaction-editor">
            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <h1 class="page-title txt-color-blueDark">
                        <i class="fa fa-pencil-square-o fa-fw fa"></i>${transaction.id > 0 ? 'Списание' : 'Новое списание'}
                        <a href="/editor/${activeMenu.href}/" class="btn btn-default right-header-button" role="button">Список</a>
                    </h1>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <div class="jarviswidget jarviswidget-color-blueDark">
                        <header role="heading">
                            <h2>${transaction.id > 0 ? 'Списание' : 'Новое списание'}</h2>
                        </header>
                        <div role="content">
                            <div class="widget-body no-padding">
                                <form:form class="smart-form" commandName="transaction" role="form" method="post">
                                    <form:hidden path="id"/>

                                    <fieldset>
                                        <section>
                                            <label class="label">Оператор <span>*</span></label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="person" readonly="true"/>
                                                <form:errors class="help-block error" path="person"/>
                                            </label>
                                        </section>
                                        <c:if test="${not empty athlete}">
                                            <section>
                                                <label class="input">
                                                    <input class="form-control" value="${athlete.displayName}" type="text" name="athlete" readonly="true"/>
                                                </label>
                                            </section>
                                        </c:if>
                                        <section>
                                            <label class="label">Дата <span>*</span></label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="date" readonly="true"/>
                                                <form:errors class="help-block error" path="date"/>
                                            </label>
                                        </section>
                                        <section>
                                            <label class="label">Сумма <span>*</span></label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="amount"/>
                                                <form:errors class="help-block error" path="amount"/>
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