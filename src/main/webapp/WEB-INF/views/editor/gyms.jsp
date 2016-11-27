<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="Залы">
	<jsp:attribute name="initScript">
		E.initGymList();
	</jsp:attribute>
    <jsp:body>
        <div class="list-area sys-gyms">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-title txt-color-blueDark">
						<i class="fa ${activeMenu.icon} fa-fw"></i>${activeMenu.caption} 
						<a href="/editor/${activeMenu.href}/edit/"class="btn btn-info right-header-button" role="button">Добавить</a>
					</h1>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-12">
					<div class="jarviswidget jarviswidget-color-blueDark">
						<header role="heading">
							<h2>Залы</h2>
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
										<section class="col col-4">
											<button class="btn btn-primary" type="submit">Поиск</button>
										</section>
									</fieldset>
								</form:form>
								
								<div class="table-responsive">
									<t:ajaxUpdate id="list">
										<table class="table table-striped table-bordered">
											<thead>
												<tr>
													<th>ID</th>
													<th>Название</th>
													<th>Команда</th>
													<th>Город</th>
													<th>Адрес</th>
													<th>Инструкторы</th>
													<th>Показывать</th>
													<th><i class="glyphicon glyphicon-cog"></i></th>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="item" items="${page.content}" varStatus="loop">
													<tr class="sys-item" iid="${item.id}">
														<td class="col-md-1"><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.id}</a></td>
														<td><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.name}</a></td>
														<td><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.team.name}</a></td>
														<td><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.city.name}</a></td>
														<td><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.address}</a></td>
														<td>
															<a href="/editor/${activeMenu.href}/edit/${item.id}/">
																<c:forEach items="${item.instructors}" var="instructor" varStatus="status">
																	${instructor.displayName}
																	<c:if test="${!status.last}">, </c:if>
																</c:forEach>
															</a>
														</td>
														<td><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.article.visible ? "Да" : "Нет"}</a></td>

												        <td>
												        	<a href="/editor/${activeMenu.href}/remove/${item.id}/" class="sys-remove">
												        		<i class="glyphicon glyphicon-remove"></i>
												        	</a>
												        </td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</t:ajaxUpdate>
									
									<te:paging page="${page}" url="${pagerPath}" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
        </div>
    </jsp:body>
</te:page>

