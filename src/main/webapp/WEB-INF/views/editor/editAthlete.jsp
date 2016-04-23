<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="${athlete.id > 0 ? athlete.displayName : 'Новый атлет'}">
	<jsp:attribute name="initScript">
		E.initAthletePage();
	</jsp:attribute>
    <jsp:body>

        <div class="athlete-editor">
            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <h1 class="page-title txt-color-blueDark">
                        <i class="fa fa-pencil-square-o fa-fw fa"></i>${athlete.id > 0 ? athlete.displayName : 'Новый атлет'}
                        <a href="/editor/${activeMenu.href}/" class="btn btn-default right-header-button" role="button">Список</a>
                        <a href="/editor/${activeMenu.href}/edit/" class="btn btn-info right-header-button margin-right-5" role="button">Добавить</a>
                    </h1>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <div class="jarviswidget jarviswidget-color-blueDark">
                        <header role="heading">
                            <h2>${athlete.id > 0 ? athlete.displayName : 'Новый атлет'}</h2>
                        </header>
                        <div role="content">
                            <div class="widget-body no-padding">
                                <form:form class="smart-form" commandName="athlete" role="form" method="post">
                                    <form:hidden path="id"/>

                                    <fieldset>
                                    	<section>
										    <label class="label">Имя <span>*</span></label>
										    <label class="input">
												<form:input type="text" path="firstName" class="form-control"/>
												<form:errors class="help-block error" path="firstName"/>
										    </label>
										</section>
										<section>
										    <label class="label">Фамилия <span>*</span></label>
										    <label class="input">
												<form:input type="text" path="lastName" class="form-control"/>
												<form:errors class="help-block error" path="lastName"/>
										    </label>
										</section>
										<section>
										    <label class="label">Отчество <span>*</span></label>
										    <label class="input">
												<form:input type="text" path="middleName" class="form-control"/>
												<form:errors class="help-block error" path="middleName"/>
										    </label>
										</section>
										<section>
										    <label class="label">Ник</label>
										    <label class="input">
												<form:input type="text" path="nickName" class="form-control"/>
										    </label>
										</section>
										
										<section>
											<label class="label">Пол<span>*</span></label>
											<div class="inline-group">
									    		<label class="radio">
													<form:radiobutton path="sex" value="male" checked="true"/>
													<i></i>
													Мужской
												</label>
												<label class="radio">
													<form:radiobutton path="sex" value="female"/>
													<i></i>
													Женский
												</label>
											</div>
										</section>
										
										<section>
										    <label class="label">Вес(кг) <span>*</span></label>
										    <label class="input">
												<form:input type="text" path="weight" class="form-control"/>
												<form:errors class="help-block error" path="weight"/>
										    </label>
										</section>
										<section>
										    <label class="label">Рост(см) <span>*</span></label>
										    <label class="input">
												<form:input type="text" path="height" class="form-control"/>
												<form:errors class="help-block error" path="height"/>
										    </label>
										</section>
										<section>
										    <label class="label">Размер ГИ</label>
										    <label class="input">
												<form:input type="text" path="giSize" class="form-control"/>
										    </label>
										</section>
										<section>
										    <label class="label">Размер рашгарда</label>
										    <label class="input">
												<form:input type="text" path="rashguardSize" class="form-control"/>
										    </label>
										</section>
										<section>
										    <label class="label">Номер паспорта</label>
										    <label class="input">
												<form:input type="text" path="passportNumber" class="form-control"/>
										    </label>
										</section>
                                        
	                                    <section>
											<label class="label">Дата рождения</label>
											<label class="input">
												<form:input class="form-control datepicker" type="text" path="birthday"/>
											</label>
										</section>
										<section>
											<label class="label">Дата начала тренировок</label>
											<label class="input">
												<form:input class="form-control datepicker" type="text" path="startDate"/>
											</label>
										</section>
										<section>
											<label class="checkbox">
												<form:checkbox path="instructor"/>
												<i></i>
												Инструктор
											</label>
										</section>
										<section>
											<label class="checkbox">
												<form:checkbox path="retired"/>
												<i></i>
												В отставке
											</label>
										</section>
									</fieldset>
									
									<fieldset>
										<section>
                                            <label class="label">Регион регистрации</label>
                                            <label class="input">
                                            	<form:input class="form-control" type="text" path="registrationRegion"/>
                                            </label>
                                        </section>
										<section>
                                            <label class="label">Пользователь</label>
                                            <label class="input">
                                            	<form:input class="form-control" type="text" path="person"/>
                                            </label>
                                        </section>
									</fieldset>
									
									<header role="heading">
			                            <h2>Контактная информация</h2>
			                        </header>
									<fieldset>
										<section>
										    <label class="label">Эл. почта</label>
										    <label class="input">
												<form:input type="text" path="email" class="form-control"/>
												<form:errors class="help-block error" path="email"/>
										    </label>
										</section>
										<section>
										    <label class="label">Телефон</label>
										    <label class="input">
												<form:input type="text" path="phoneNumber" class="form-control"/>
												<form:errors class="help-block error" path="phoneNumber"/>
										    </label>
										</section>
										<section>
										    <label class="label">Мобильный телефон</label>
										    <label class="input">
												<form:input type="text" path="mobileNumber" class="form-control"/>
												<form:errors class="help-block error" path="mobileNumber"/>
										    </label>
										</section>
										<section>
										    <label class="label">Vkontakte</label>
										    <label class="input">
												<form:input type="text" path="vk" class="form-control"/>
										    </label>
										</section>
										<section>
										    <label class="label">Facebook</label>
										    <label class="input">
												<form:input type="text" path="facebook" class="form-control"/>
										    </label>
										</section>
										<section>
										    <label class="label">Instagram</label>
										    <label class="input">
												<form:input type="text" path="instagram" class="form-control"/>
										    </label>
										</section>
										<section>
										    <label class="label">Skype</label>
										    <label class="input">
												<form:input type="text" path="skype" class="form-control"/>
										    </label>
										</section>
									</fieldset>
									<fieldset>
										<section>
                                            <label class="label">Комментарий</label>
                                            <label class="input">
                                                <form:textarea class="form-control" path="comment"/>
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
            
            <c:if test="${athlete.id != 0}">
            	<div class="row image-panel athlete-image">
					<div class="col-sm-12 col-md-12 col-lg-6">
						<div class="jarviswidget jarviswidget-color-blueDark">
							<header role="heading">
								<h2>Фотография</h2>
							</header>
							<div role="content">
								<div class="widget-body no-padding">
									<jsp:include page="panel/upload_image_panel.jsp">
								        <jsp:param name="type" value="ATHLETE_MIDDLE"/>
								        <jsp:param name="entityId" value="${athlete.id}"/>
								        <jsp:param name="image" value="${athleteImage}"/>
								        <jsp:param name="isMultiple" value="false"/>
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