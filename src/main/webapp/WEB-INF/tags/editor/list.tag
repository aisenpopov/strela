<%@ tag import="java.util.List" %>
<%@ include file="../../views/include.jsp" %>

<%@ tag pageEncoding='UTF-8' %>
<%@ attribute name="list" required="true" type="java.util.List" %>
<%@ attribute name="isSortable" required="false" type="java.lang.Boolean" %>
<%@ attribute name="isCategorized" required="false" type="java.lang.Boolean" %>


<c:if test="${empty isCategorized}">
	<c:set var="isCategorized" value="false"/>
</c:if>
<c:if test="${empty isSortable}">
	<c:set var="isSortable" value="true"/>
</c:if>

<jsp:doBody/>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-title txt-color-blueDark">
			<i class="fa ${activeMenu.icon} fa-fw"></i>${activeMenu.caption} 
			<a href="/editor/${activeMenu.href}/edit/"class="btn btn-info right-header-button" role="button">Добавить</a>
		</h1>
	</div>
</div>

<div class="row ${isCategorized ? 'categorized' : ''}">
	<div class="col-lg-12">
		<div class="jarviswidget jarviswidget-color-blueDark">
			<header role="heading">
				<h2>${title}</h2>
			</header>
			<div role="content">
				<div class="widget-body">
					<form:form class="smart-form sys-filter" role="form" method="get" commandName="filter">
						<fieldset class="no-padding">
							<section class="col col-4">
								<label class="label">Поиск</label> 
								<label class="input"> 
									<form:input class="form-control" type="text" path="query" />
								</label>
							</section>							
							<c:if test="${isCategorized}">
								<form:hidden path="categoryId"/>
							</c:if>
							<section class="col col-4">
								<button class="btn btn-primary" type="submit">Поиск</button>
							</section>
						</fieldset>
					</form:form>
					
					<div class="table-responsive">
						<k:ajaxUpdate id="list">
							<table class="table table-striped table-bordered">
								<thead>
									<tr>
										<th>ID</th>
										<th>Название</th>
										
										<c:forEach items="${tableColumns}" var="column">
											<th>${column.name}</th>
										</c:forEach>
										
										<c:if test="${isSortable}">
											<th class="action" colspan="2"></th>
										</c:if>
										<th><i class="glyphicon glyphicon-cog"></i></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${list}" varStatus="loop">
										<tr class="sys-item" iid="${item.id}">
											<td class="col-md-1"><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.id}</a></td>
											<td><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.name}</a></td>
													
											<c:forEach items="${tableColumns}" var="column">
												<td>${item[column.field]}</td>
											</c:forEach>		
																		         
								            <c:if test="${isSortable}">
									            <td class="action">
										        	<c:if test="${!loop.first}">
										            	<a href="#" class="sys-sort-up">
										            		<i class="fa fa-sort-up"></i>
										            	</a>
										        	</c:if>
										        </td>
										        <td class="action">
										        	<c:if test="${!loop.last}">
										            	<a href="#" class="sys-sort-down">
										            		<i class="fa fa-sort-down"></i>
										            	</a>
										        	</c:if>
										        </td>
									        </c:if>
									        
									        <td>
									        	<a href="/editor/${activeMenu.href}/remove/${item.id}/" class="sys-remove">
									        		<i class="glyphicon glyphicon-remove"></i>
									        	</a>
									        </td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</k:ajaxUpdate>
						
						<te:paging page="${page}" url="${pagerPath}" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
