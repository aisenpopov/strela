<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="../include.jsp"%>

<t:page htmlTitle="Восстановление пароля">
	<jsp:attribute name="initScript">
        S.initRecoveryPage();
    </jsp:attribute>
    <jsp:body>
        <section class="text-md-left well--inset-2">
            <div class="container">
                <div class="row">
                    <div class="col-md-4"></div>
                    <div class="col-md-4">
                        <c:choose>
                            <c:when test="${state == 'recovery'}">
                                <h5 class="text-center">введите почту</h5>
                                <form:form class="email rd-mailform" method="post" commandName="emailForm" action="/recovery/send_code">
                                    <fieldset>
                                        <div class="row flow-offset-4">
                                            <div class="col-md-12">
                                                <label>
                                                    <span class="title">Email<span>*</span></span>
                                                    <div class="field-error" id="email-error">
                                                        <form:errors class="help-block form-error" path="email"/>
                                                    </div>
                                                    <form:input type="text"
                                                                path="email"
                                                                data-validation="required"
                                                                data-validation-error-msg="Поле не может быть пустым"
                                                                data-validation-error-msg-container="#email-error"/>
                                                </label>
                                            </div>
                                        </div>

                                        <div class="mfControls btn-group text-center text-md-left">
                                            <button class="btn btn-md btn-default" type="submit">Отправить</button>
                                        </div>
                                    </fieldset>
                                </form:form>
                            </c:when>
                            <c:when test="${state == 'send_code'}">
                                <div class="text-center">
                                    <h5>Код отправлен</h5>
                                    <p>Пожалуйста проверьте указанную почту.</p>
                                    <p><a href="/" class="text-underline">Перейти на главную</a></p>
                                </div>
                            </c:when>
                            <c:when test="${state == 'wrong_code'}">
                                <div class="text-center">
                                    <h5>Неправильный код</h5>
                                </div>
                            </c:when>
                            <c:when test="${state == 'change_password'}">
                                <h5 class="text-center">восстановление</h5>
                                <form:form class='password rd-mailform' commandName="passwordForm" action="/recovery/save_new_password/" method="post">
                                    <form:hidden path="login"/>
                                    <fieldset>
                                        <div class="row flow-offset-4">
                                            <div class="col-md-12">
                                                <label>
                                                    <span class="title">Новый пароль<span>*</span></span>
                                                    <div class="field-error" id="new-password-error">
                                                        <form:errors class="help-block form-error" path="newPassword"/>
                                                    </div>
                                                    <form:input type="password"
                                                                path="newPassword"
                                                                data-validation="required"
                                                                data-validation-error-msg="Поле не может быть пустым"
                                                                data-validation-error-msg-container="#new-password-error"/>
                                                </label>
                                            </div>
                                        </div>
                                        <div class="row flow-offset-4">
                                            <div class="col-md-12">
                                                <label>
                                                    <span class="title">Повторите пароль<span>*</span></span>
                                                    <div class="field-error" id="rep-password-error">
                                                        <form:errors class="help-block form-error" path="repPassword"/>
                                                    </div>
                                                    <form:input type="password"
                                                                path="repPassword"
                                                                data-validation="required"
                                                                data-validation-error-msg="Поле не может быть пустым"
                                                                data-validation-error-msg-container="#rep-password-error"/>
                                                </label>
                                            </div>
                                        </div>

                                        <div class="mfControls btn-group text-center text-md-left">
                                            <button class="btn btn-md btn-default" type="submit">Сохранить</button>
                                        </div>
                                    </fieldset>
                                </form:form>
                            </c:when>
                            <c:when test="${state == 'complete_recovery'}">
                                <div class="text-center">
                                    <h5>Пароль изменен</h5>
                                    <p><a href="/account/" class="text-underline">Перейти в личный кабинет</a></p>
                                </div>
                            </c:when>
                        </c:choose>
                    </div>
                    <div class="col-md-4"></div>
                </div>
            </div>
        </section>
    </jsp:body>
</t:page>
