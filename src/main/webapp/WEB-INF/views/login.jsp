<%--
  Created by IntelliJ IDEA.
  User: Aisen
  Date: 27.04.2016
  Time: 18:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="include.jsp"%>

<t:page htmlTitle="Войти">
	<jsp:attribute name="initScript">
        S.initLoginPage();
    </jsp:attribute>
    <jsp:body>
        <section class="text-md-left well--inset-2">
            <div class="container">
                <div class="row">
                    <div class="col-md-4"></div>
                    <div class="col-md-4">
                        <h5 class="text-center">авторизация</h5>
                        <form class='login rd-mailform' method="post">
                            <input type="hidden" name="remember" value="true"/>
                            <fieldset>
                                <div class="row flow-offset-4">
                                    <div class="col-md-12">
                                        <label>
                                            <span class="title">Login<span>*</span></span>
                                            <div class="field-error" id="login-error">
                                            </div>
                                            <input type="text"
                                                   name="username"
                                                   placeholder="Введите ваш login"
                                                   data-validation="required"
                                                   data-validation-error-msg="Поле не может быть пустым"
                                                   data-validation-error-msg-container="#login-error"/>
                                        </label>
                                    </div>
                                </div>
                                <div class="row flow-offset-4">
                                    <div class="col-md-12">
                                        <label>
                                            <span class="title">Пароль<span>*</span></span>
                                            <div class="field-error" id="password-error">
                                            </div>
                                            <input type="password"
                                                   name="password"
                                                   placeholder="Введите ваш пароль"
                                                   data-validation="required"
                                                   data-validation-error-msg="Поле не может быть пустым"
                                                   data-validation-error-msg-container="#password-error"/>
                                        </label>
                                    </div>
                                </div>

                                <div class="mfControls btn-group text-center text-md-left">
                                    <button class="btn btn-md btn-default" type="submit">Войти</button>
                                    <a href="/recovery/" class="btn btn-md">Забыли пароль?</a>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                    <div class="col-md-4"></div>
                </div>
            </div>
        </section>
    </jsp:body>
</t:page>
