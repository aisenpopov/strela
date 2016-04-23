<%@ include file="../../views/include.jsp" %>
<%@ tag pageEncoding='UTF-8' %>

<%@ attribute name="isHideable" required="false" type="java.lang.Boolean" %>

<c:if test="${empty isHideable}">
	<c:set var="isHideable" value="true"/>
</c:if>

<jsp:doBody/>

<section>
    <label class="label">Название <span>*</span></label>
    <label class="input">
		<form:input type="text" path="name" class="form-control"/>
		<form:errors class="help-block error" path="name"/>
    </label>
</section>

<c:if test="${isHideable}">
	<section>
		<label class="checkbox">
			<form:checkbox path="visible"/>
			<i></i>
			Отображать на сайте
		</label>
	</section>
</c:if>
