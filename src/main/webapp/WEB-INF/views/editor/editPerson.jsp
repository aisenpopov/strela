<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="${person.id > 0 ? person.login : 'Новый пользователь'}">
	<jsp:attribute name="initScript">
		E.initPersonPage();
	</jsp:attribute>
    <jsp:body>

        <div class="person-editor">
            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <h1 class="page-title txt-color-blueDark">
                        <i class="fa fa-pencil-square-o fa-fw fa"></i>${person.id > 0 ? person.login : 'Новый пользователь'}
                        <a href="/editor/${activeMenu.href}/" class="btn btn-default right-header-button" role="button">Список</a>
                        <a href="/editor/${activeMenu.href}/edit/" class="btn btn-info right-header-button margin-right-5" role="button">Добавить</a>
                    </h1>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <div class="jarviswidget jarviswidget-color-blueDark">
                        <header role="heading">
                            <h2>${person.id > 0 ? person.login : 'Новый пользователь'}</h2>
                        </header>
                        <div role="content">
                            <div class="widget-body no-padding">
                                <form:form class="smart-form" commandName="person" role="form" method="post">
                                    <form:hidden path="id"/>

                                    <fieldset>
		                            	<section>
										    <label class="label">Логин <span>*</span></label>
										    <label class="input">
												<form:input type="text" path="login" class="form-control"/>
												<form:errors class="help-block error" path="login"/>
										    </label>
										</section>
										<section>
										    <label class="label">Новый пароль</label>
										    <label class="input">
												<form:input type="text" path="password" class="form-control"/>
												<form:errors class="help-block error" path="password"/>
										    </label>
										</section>
										<section>
											<label class="checkbox">
												<form:checkbox path="admin"/>
												<i></i>
												Админ
											</label>
										</section>
										<section>
											<label class="checkbox">
												<form:checkbox path="disabled"/>
												<i></i>
												Заблокировать
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