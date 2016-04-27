<%@ include file="../../views/include.jsp" %>

<%@ tag pageEncoding='UTF-8' %>
<%@ attribute name="filter" required="true" type="ru.strela.model.filter.OrderFilter" %>
<%@ attribute name="field" required="true" type="java.lang.String" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>

<jsp:doBody/>

<th class="sys-sortable" data-sort-direction="${not empty filter ? filter.orders[0].direction : ''}" data-sort-field="${field}">
	${title}
	<c:if test="${filter.orders[0].field == field}">
		<c:choose>
			<c:when test="${filter.orders[0].direction == 'Asc'}">
				<i class="fa fa-sort-up"></i>
			</c:when>
			<c:otherwise>
				<i class="fa fa-sort-down"></i>
			</c:otherwise>
		</c:choose>
	</c:if>
</th>
