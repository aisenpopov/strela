<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="include.jsp"%>

<t:page htmlTitle="${currentAthlete.displayName}">
	<jsp:attribute name="initScript">
        S.initAccountPage();
    </jsp:attribute>
    <jsp:body>
        <section class="md-well well--inset-2">
            <div class="container">
                <div class="row">
                    <div class="col-md-5">
                        <div class="row text-md-left inset-1 flow-offset-2">
                            <div class="col-sm-preffix-3 col-sm-6 col-md-preffix-0 col-md-12 col-md-push-1">
                                <%--<div class="divider divider-small offset-4 md-visible"></div>--%>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-7 relative">
                        <c:if test="${not empty athlete}">
                            <h2 class="offset-6">Профиль</h2>
                            <span class="form-success athlete-success">Изменения сохранены</span>
                            <form:form class='athlete rd-mailform' commandName="athlete" method="post">
                                <form:hidden path="id"/>
                                <fieldset>
                                    <div class="row flow-offset-4">
                                        <t:ajaxUpdate id="firstNameField" cssClass="col-md-12">
                                            <label>
                                                    <span class="title">Имя<span>*</span></span>
                                                    <div class="field-error" id="firstName-error">
                                                        <form:errors class="help-block form-error" path="firstName"/>
                                                    </div>
                                                    <form:input type="text"
                                                                path="firstName"
                                                                data-validation="required"
                                                                data-validation-error-msg="Поле не может быть пустым"
                                                                data-validation-error-msg-container="#firstName-error"/>
                                            </label>
                                        </t:ajaxUpdate>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <label>
                                                <span class="title">Фамилия</span>
                                                <form:input type="text"
                                                            path="lastName"/>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <label>
                                                <span class="title">Отчество</span>
                                                <form:input type="text"
                                                            path="middleName"/>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <t:ajaxUpdate id="birthdayField" cssClass="col-md-12">
                                            <label>
                                                    <span class="title">Дата рождения</span>
                                                    <div class="field-error" id="birthday-error">
                                                        <form:errors class="help-block form-error" path="birthday"/>
                                                    </div>
                                                    <form:input type="text"
                                                                path="birthday"
                                                                class="datepicker"
                                                                data-validation="date"
                                                                data-validation-format="dd.mm.yyyy"
                                                                data-validation-error-msg="Ипользуйте формат ДД.ММ.ГГГГ"
                                                                data-validation-error-msg-container="#birthday-error"/>
                                            </label>
                                        </t:ajaxUpdate>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <span class="title">Пол</span>
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
                                        </div>
                                    </div>

                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <label>
                                                <span class="title">Вес(кг)</span>
                                                <div class="field-error" id="weight-error">
                                                    <form:errors class="help-block form-error" path="weight"/>
                                                </div>
                                                <form:input type="text"
                                                            path="weight"
                                                            data-validation="number"
                                                            data-validation-allowing="float"
                                                            data-validation-error-msg="Введите корректное число"
                                                            data-validation-error-msg-container="#weight-error"/>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <label>
                                                <span class="title">Рост(см)</span>
                                                <div class="field-error" id="height-error">
                                                    <form:errors class="help-block form-error" path="height"/>
                                                </div>
                                                <form:input type="text"
                                                            path="height"
                                                            data-validation="number"
                                                            data-validation-allowing="float"
                                                            data-validation-error-msg="Введите корректное число"
                                                            data-validation-error-msg-container="#height-error"/>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <label>
                                                <span class="title">Размер ГИ</span>
                                                <form:input type="text"
                                                            path="giSize"/>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <label>
                                                <span class="title">Размер рашгарда</span>
                                                <form:input type="text"
                                                            path="rashguardSize"/>
                                            </label>
                                        </div>
                                    </div>

                                    <div class="divider divider-small offset-4"></div>

                                    <div class="row flow-offset-4">
                                        <t:ajaxUpdate id="emailField" cssClass="col-md-12">
                                            <label>
                                                <span class="title">Эл. почта</span>
                                                <div class="field-error" id="email-error">
                                                    <form:errors class="help-block form-error" path="email"/>
                                                </div>
                                                <form:input type="text"
                                                            path="email"
                                                            data-validation="email, required"
                                                            data-validation-error-msg="Введите валидный e-mail"
                                                            data-validation-error-msg-container="#email-error"/>
                                            </label>
                                        </t:ajaxUpdate>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <t:ajaxUpdate id="mobileNumberField" cssClass="col-md-12">
                                            <label>
                                                <span class="title">Мобильный телефон</span>
                                                <div class="field-error" id="mobileNumber-error">
                                                    <form:errors class="help-block form-error" path="mobileNumber"/>
                                                </div>
                                                <form:input type="text"
                                                            path="mobileNumber"
                                                            data-validation="custom"
                                                            data-validation-regexp="^[0-9-()+]+$"
                                                            data-validation-error-msg="Введите валидный номер телефона"
                                                            data-validation-error-msg-container="#mobileNumber-error"/>
                                            </label>
                                        </t:ajaxUpdate>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <label>
                                                <span class="title">Vkontakte</span>
                                                <form:input type="text"
                                                            path="vk"/>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <label>
                                                <span class="title">Facebook</span>
                                                <form:input type="text"
                                                            path="facebook"/>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <label>
                                                <span class="title">Instagram</span>
                                                <form:input type="text"
                                                            path="instagram"/>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <label>
                                                <span class="title">Skype</span>
                                                <form:input type="text"
                                                            path="skype"/>
                                            </label>
                                        </div>
                                    </div>

                                    <div class="divider divider-small offset-4"></div>

                                    <div class="row flow-offset-4">
                                        <form:hidden path="person.id"/>
                                        <t:ajaxUpdate id="loginField" cssClass="col-md-12">
                                            <label>
                                                <span class="title">Login<span>*</span></span>
                                                <div class="field-error" id="login-error">
                                                    <form:errors class="help-block form-error" path="person.login"/>
                                                </div>
                                                <form:input type="text"
                                                            path="person.login"
                                                            data-validation="length"
                                                            data-validation-length="3-20"
                                                            data-validation-error-msg="Длина должна быть от 3 до 20 символов"
                                                            data-validation-error-msg-container="#login-error"/>
                                            </label>
                                        </t:ajaxUpdate>
                                    </div>

                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <span class="title">Тарифы</span>
                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th class="col-lg-1">Тариф</th>
                                                        <th class="col-lg-2">Купон</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${athleteTariffs}" var="item">
                                                        <tr class="" data-athlete-tariff-id="${item.id}">
                                                            <td>${item.tariff.name}</td>
                                                            <td>${not empty item.coupon ? item.coupon.name : ''}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>


                                    <div class="divider divider-small offset-4"></div>
                                    <div class="mfControls btn-group text-center text-md-left">
                                        <button class="btn btn-md btn-default" type="submit">Сохранить</button>
                                    </div>
                                </fieldset>
                            </form:form>
                        </c:if>
                        <c:if test="${not empty passwordForm}">
                            <h2 class="offset-4">Пароль</h2>
                            <span class="form-success password-success">Пароль изменен</span>
                            <form:form class='password rd-mailform' commandName="passwordForm" method="post">
                                <fieldset>
                                    <div class="row flow-offset-4">
                                        <t:ajaxUpdate id="oldPasswordField" cssClass="col-md-12">
                                            <label>
                                                <span class="title">Старый пароль<span>*</span></span>
                                                <div class="field-error" id="oldPassword-error">
                                                    <form:errors class="help-block form-error" path="oldPassword"/>
                                                </div>
                                                <form:input type="password"
                                                            path="oldPassword"
                                                            data-validation="required"
                                                            data-validation-error-msg="Поле не может быть пустым"
                                                            data-validation-error-msg-container="#oldPassword-error"/>
                                            </label>
                                        </t:ajaxUpdate>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <t:ajaxUpdate id="newPasswordField" cssClass="col-md-12">
                                            <label>
                                                <span class="title">Новый пароль<span>*</span></span>
                                                <div class="field-error" id="newPassword-error">
                                                    <form:errors class="help-block form-error" path="newPassword"/>
                                                </div>
                                                <form:input type="password"
                                                            path="newPassword"
                                                            data-validation="length"
                                                            data-validation-length="6-20"
                                                            data-validation-error-msg="Длина должна быть от 6 до 20 символов"
                                                            data-validation-error-msg-container="#newPassword-error"/>
                                            </label>
                                        </t:ajaxUpdate>
                                    </div>
                                    <div class="row flow-offset-4">
                                        <t:ajaxUpdate id="repPasswordField" cssClass="col-md-12">
                                            <label>
                                                <span class="title">Повторите новый пароль<span>*</span></span>
                                                <div class="field-error" id="repPassword-error">
                                                    <form:errors class="help-block form-error" path="repPassword"/>
                                                </div>
                                                <form:input type="password"
                                                            path="repPassword"
                                                            data-validation="length"
                                                            data-validation-length="6-20"
                                                            data-validation-error-msg="Длина - от 6 до 20"
                                                            data-validation-error-msg-container="#repPassword-error"/>
                                            </label>
                                        </t:ajaxUpdate>
                                    </div>

                                    <div class="divider divider-small offset-4"></div>

                                    <div class="mfControls btn-group text-center text-md-left">
                                        <button class="btn btn-md btn-default" type="submit">Сохранить</button>
                                    </div>
                                </fieldset>
                            </form:form>
                        </c:if>

                        <div class="divider divider-big divider-md-vertical divider-left md-visible"></div>
                    </div>
                </div>
            </div>
        </section>
    </jsp:body>
</t:page>
