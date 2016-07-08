<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="Мой баланс">
	<jsp:attribute name="initScript">
        E.initBalancePage();
	</jsp:attribute>
    <jsp:body>
        <div class="balance">
            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <h1 class="page-title txt-color-blueDark">
                        <i class="fa fa-money fa-fw fa"></i>Ваш баланс: ${not empty personAccount ? personAccount.account : 0} руб.
                    </h1>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <div class="jarviswidget jarviswidget-color-blueDark">
                        <header role="heading">
                            <h2>Списать с баланса</h2>
                        </header>
                        <div role="content">
                            <div class="widget-body no-padding">
                                <form:form class="smart-form" commandName="transaction" role="form" method="post" action="/editor/balance/">
                                    <fieldset>
                                       	<section>
											<label class="label">Сумма</label>
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