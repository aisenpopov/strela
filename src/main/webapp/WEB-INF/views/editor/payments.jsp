<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="Платежи">
	<jsp:attribute name="initScript">
		E.initPaymentList();
	</jsp:attribute>
    <jsp:body>
        <div class="list-area sys-payments">
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
							<h2>Платежи</h2>
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
									<k:ajaxUpdate id="list">
										<table class="table table-striped table-bordered">
											<thead>
												<tr>
													<th>ID</th>
													<th>Атлет</th>
													<th>Тариф</th>
													<th>Купон</th>
													<th>Сумма</th>
													<th>Оператор</th>
													<th>Дата</th>
													<th><i class="glyphicon glyphicon-cog"></i></th>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="item" items="${page.content}" varStatus="loop">
													<tr class="sys-item" iid="${item.id}">
														<td class="col-md-1"><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.id}</a></td>
														<td><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.athleteTariff.athlete.displayName}</a></td>
														<td><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.athleteTariff.tariff.name}</a></td>
														<td><a href="/editor/${activeMenu.href}/edit/${item.id}/">${not empty item.athleteTariff ? item.athleteTariff.coupon.name : ''}</a></td>
														<td><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.amount}</a></td>
														<td><a href="/editor/${activeMenu.href}/edit/${item.id}/">${item.operator.login}</a></td>
														<td><a href="/editor/${activeMenu.href}/edit/${item.id}/">${dateUtils:formatFull(item.date)}</a></td>

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
        </div>
    </jsp:body>
</te:page>

