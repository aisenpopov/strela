<%@ tag import="java.util.List" %>
<%@ tag import="java.util.ArrayList" %>
<%@ include file="../../views/include.jsp" %>

<%@ tag pageEncoding='UTF-8' %>
<%@ attribute name="page" required="true" type="org.springframework.data.domain.Page" %>
<%@ attribute name="url" required="true" type="java.lang.String" %>

<jsp:doBody/>

<jsp:useBean id="page" type="org.springframework.data.domain.Page"/>

<%
    int pc = 3;

    int number = page.getNumber() + 1;
    int start = Math.max(1, number - (pc /2));
    int over = start + pc - page.getTotalPages() - 1;
    if ( over > 0 ) {
        start -= over;
    }
    if ( start < 1 ) {
        start = 1;
    }

    List<Integer> pages = new ArrayList<Integer>();
    int max = page.getTotalPages() + 1;
    int c = 0;
    while(start < max && c < pc) {
        pages.add(start++);
        c++;
    }
    jspContext.setAttribute("pages", pages);
%>

<c:if test="${fn:length(pages) gt 1}">
	<div class="paginator">	
		<c:if test="${page.number != 0}"><a class="paginatorprew" href="${url}page=${page.number}"></a></c:if>
	
		<div class="pages">
	 		<c:forEach var="p" items="${pages}">
	 			<c:choose>
	            	<c:when test="${p eq (page.number+1)}">
	            		<span class="page active">${p}</span>
	            	</c:when>
	            	<c:otherwise>
	            		<a href="${url}page=${p}" class="page">${p}</a>
	            	</c:otherwise>
	            </c:choose>
	 		</c:forEach>	
 		</div>
 		
 		<c:if test="${page.number != page.totalPages - 1}"><a class="paginatornext" href="${url}page=${page.number+2}"></a></c:if>
	</div>
</c:if>