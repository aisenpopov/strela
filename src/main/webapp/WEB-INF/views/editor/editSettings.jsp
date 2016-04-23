<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="Настройки">
	<jsp:attribute name="initScript">
		E.initSettingsPage();
	</jsp:attribute>
    <jsp:body>

        <div class="settings-editor">
            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <h1 class="page-title txt-color-blueDark">
                        <i class="fa fa-pencil-square-o fa-fw fa"></i>Настройки
                    </h1>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <div class="jarviswidget jarviswidget-color-blueDark">
                        <header role="heading">
                            <h2>Настройки</h2>
                        </header>
                        <div role="content">
                            <div class="widget-body no-padding">
                                <form:form class="smart-form" commandName="settings" role="form" method="post">
                                    <form:hidden path="id"/>

                                    <fieldset>                                 		                                       	 			
                                       	<section>
											<label class="label">Email</label>
											<label class="input">
												<form:input class="form-control" type="text" path="email"/>
												<form:errors class="help-block error" path="email"/>
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