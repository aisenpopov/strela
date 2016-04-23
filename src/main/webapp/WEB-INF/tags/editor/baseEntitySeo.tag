<%@ include file="../../views/include.jsp" %>
<%@ tag pageEncoding='UTF-8' %>

<%@ attribute name="showPath" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTitle" required="false" type="java.lang.Boolean" %>

<c:if test="${empty showPath}">
	<c:set var="showPath" value="true"/>
</c:if>
<c:if test="${empty showTitle}">
	<c:set var="showTitle" value="true"/>
</c:if>

<jsp:doBody/>

<c:if test="${showPath}">
	<section>
		<label class="label">Путь</label>
		<label class="input">
		<form:input class="form-control" type="text" path="path"/>
		<form:errors class="help-block error" path="path"/>
		</label>
	</section>
</c:if>
<c:if test="${showTitle}">
	<section>
		<label class="label">HTML:TITLE</label>
		<label class="input">
			<form:input class="form-control" type="text" path="htmlTitle"/>
		</label>
	</section>
</c:if>
<section>
	<label class="label">META:DESCRIPTION</label>
	<label class="input">
		<form:input class="form-control" type="text" path="metaDescription"/>
	</label>
</section>
<section>
	<label class="label">META:KEYWORDS</label>
	<label class="input">
		<form:input class="form-control" type="text" path="metaKeywords"/>
	</label>
</section>

