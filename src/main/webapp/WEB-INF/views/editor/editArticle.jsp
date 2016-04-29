<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="${article.id > 0 ? article.name : (article.type == 'news' ? 'Новая новость' : 'Новая страница')}">
	<jsp:attribute name="initScript">
		E.initArticlePage();
	</jsp:attribute>
    <jsp:body>

        <div class="article-editor">
            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <h1 class="page-title txt-color-blueDark">
                        <i class="fa fa-pencil-square-o fa-fw fa"></i>${article.id > 0 ? article.name : (article.type == 'news' ? 'Новая новость' : 'Новая страница')}
                        <a href="/editor/${activeMenu.href}/" class="btn btn-default right-header-button" role="button">Список</a>
                        <a href="/editor/${activeMenu.href}/edit/" class="btn btn-info right-header-button margin-right-5" role="button">Добавить</a>
                    </h1>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <div class="jarviswidget jarviswidget-color-blueDark">
                        <header role="heading">
                            <h2>${article.id > 0 ? article.name : (article.type == 'news' ? 'Новая новость' : 'Новая страница')}</h2>
                        </header>
                        <div role="content">
                            <div class="widget-body no-padding">
                                <form:form class="smart-form" commandName="article" role="form" method="post">
                                    <form:hidden path="id"/>
                                    <form:hidden path="type"/>

                                    <fieldset>
                                    	<te:baseEntityNamed/>
                                       	<c:if test="${article.type == 'news'}">
	                                       	<section>
												<label class="label">Дата<span>*</span></label>
												<label class="input">
													<form:input class="form-control datepicker" type="text" path="publish"/>
													<form:errors class="help-block error" path="publish"/>
												</label>
											</section>
	                                        <section>
												<label class="label">Краткое описание</label>
	                                            <label class="input">
	                                                <form:textarea class="form-control" path="shortText"/>
	                                            </label>
	                                        </section>
										</c:if>
										
                                        <section>
                                            <label class="label">Текст</label>
                                            <label class="input">
                                                <form:textarea class="form-control wysiwyg" path="text"/>
                                            </label>
                                        </section>
									</fieldset>
									<fieldset>									
                              			<te:baseEntitySeo/>
                                    </fieldset>
                                    <footer>
                                        <button class="btn btn-primary" type="submit" name="save">
                                            <i class="fa fa-save"></i> Сохранить
                                        </button>
                                    </footer>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <c:if test="${article.id != 0}">
            	<c:if test="${article.type == 'news'}">
	            	<div class="row image-panel article-image">
						<div class="col-sm-12 col-md-12 col-lg-6">
							<div class="jarviswidget jarviswidget-color-blueDark">
								<header role="heading">
									<h2>Изображение превью</h2>
								</header>
								<div role="content">
									<div class="widget-body no-padding">
										<jsp:include page="panel/uploadImagePanel.jsp">
									        <jsp:param name="type" value="ARTICLE_PREVIEW"/>
									        <jsp:param name="entityId" value="${article.id}"/>
									        <jsp:param name="image" value="${articleImage}"/>
									        <jsp:param name="isMultiple" value="false"/>
									    </jsp:include>
									</div>
								</div>
							</div>
						</div>
					</div>
            	</c:if>
				
            	<div class="row image-panel content-image">
					<div class="col-sm-12 col-md-12 col-lg-6">
						<div class="jarviswidget jarviswidget-color-blueDark">
							<header role="heading">
								<h2>Изображения для контента</h2>
							</header>
							<div role="content">
								<div class="widget-body">
									<div class="superbox list-images">
										<k:ajaxUpdate id="image-list">
											<c:forEach items="${images}" var="image">
												<div class="superbox-list block-image" iid="${image.id}">
													<img src="${image.image}" class="superbox-img" data-img="${image.image}" width="150"/>
													<div class="code">#[image=${image.id}]</div>
												</div>
											</c:forEach>
										</k:ajaxUpdate>
										<div class="superbox-float"></div>
									</div>
										
									<jsp:include page="panel/uploadImagePanel.jsp">
										<jsp:param name="type" value="ARTICLE_CONTENT"/>
									    <jsp:param name="entityId" value="${article.id}"/>
									    <jsp:param name="crop" value="false"/>
									    <jsp:param name="isMultiple" value="true"/>
									</jsp:include>
								</div>
							</div>
						</div>
					</div>
				</div>	
				
				<div class="row">
					<div class="col-sm-12 col-md-12 col-lg-6">
						<div class="jarviswidget jarviswidget-color-blueDark">
							<header role="heading">
								<h2>Видео для контента</h2>
							</header>
							<div class="widget-body">
								<form class="smart-form videos">
                                    <header>Видео</header>
									<fieldset>
										<section>
											<k:ajaxUpdate id="video-panel">
												<table class="table table-bordered">
													<thead>
														<tr>
															<th>Ссылка</th>
															<th>Для вставки</th>
															<th ></th>
														</tr>
													</thead>
													<tbody>
														<tr class="block new-block">
															<td>
																<label class="input">
																	<input name="link" type="text" class="form-control"/>
																</label>
															</td>
															<td>
																
															</td>
															<td class="last">
																<a href="#" class="remove-block">
										                			<i class="glyphicon glyphicon-remove"></i>
										                		</a>
										                		<input name="videoId" type="hidden" value="0"/>
															</td>
														</tr>
														<c:forEach items="${videos}" var="video" varStatus="loop">
															<c:set var="position" value="${loop.index}"/>
															<tr class="block" eid="${file.id}">
																<input name="videoId${position}" value="${video.id}" type="hidden"/>
																
																<td>
																	<label class="input">
																		<input name="link${position}" value="${video.link}" type="text" class="form-control"/>
																	</label>
																</td>
																<td>
																	<label class="input code">
																		#[video=${video.id}]
																	</label>
																</td>
																<td class="last">
																	<a href="#" class="remove-block">
										                				<i class="glyphicon glyphicon-remove"></i>
											                			</a>
																	</td>
																</tr>
															</c:forEach>								
														</tbody>
													</table>
				                                </k:ajaxUpdate>
											</section>
											<section class="add-block">
												<a href="#"><i class="glyphicon glyphicon-plus"></i>Добавить блок</a>
											</section>
										</fieldset>
                                    <footer>
                                        <button class="btn btn-primary" type="submit" name="save">
                                            <i class="fa fa-save"></i> Сохранить
                                        </button>
                                    </footer>
                                </form>
							</div>
						</div>
					</div>
				</div>		
            </c:if>
        </div>
    </jsp:body>
</te:page>