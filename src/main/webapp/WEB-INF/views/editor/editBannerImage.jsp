<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="${bannerImage.id > 0 ? bannerImage.name : 'Новое изображение'}">
	<jsp:attribute name="initScript">
		E.initBannerImagePage();
	</jsp:attribute>
    <jsp:body>

        <div class="banner-image-editor">
            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <h1 class="page-title txt-color-blueDark">
                        <i class="fa fa-pencil-square-o fa-fw fa"></i>${bannerImage.id > 0 ? bannerImage.name : 'Новое изображение'}
                        <a href="/editor/${activeMenu.href}/" class="btn btn-default right-header-button" role="button">Список</a>
                        <a href="/editor/${activeMenu.href}/ru/edit/" class="btn btn-info right-header-button margin-right-5" role="button">Добавить</a>
                    </h1>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <div class="jarviswidget jarviswidget-color-blueDark">
                        <header role="heading">
                            <h2>${bannerImage.id > 0 ? bannerImage.name : 'Новое изображение'}</h2>
                        </header>
                        <div role="content">
                            <div class="widget-body no-padding">
                                <form:form class="smart-form" commandName="bannerImage" role="form" method="post">
                                    <form:hidden path="id"/>
                                    <form:hidden path="type"/>

                                    <fieldset>
										<te:baseEntityNamed/>
                                       	
                                       	<c:if test="${bannerImage.type == 'slider'}">
	                                       	<section>
												<label class="label">Ссылка</label>
	                                            <label class="input">
	                                                <form:input class="form-control" type="text" path="link"/>
	                                            </label>
	                                        </section>  
                                        </c:if>                                  	 
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
            
            <c:if test="${bannerImage.type == 'slider' && bannerImage.id != 0}">
            	<div class="row image-panel banner-image">
					<div class="col-sm-12 col-md-12 col-lg-6">
						<div class="jarviswidget jarviswidget-color-blueDark">
							<header role="heading">
								<h2>Изображение</h2>
							</header>
							<div role="content">
								<div class="widget-body no-padding">
									<k:ajaxUpdate id="bannerImagePanel">
										<jsp:include page="panel/uploadImagePanel.jsp">
									        <jsp:param name="type" value="${bannerImage.type == 'slider' ? 'BANNER_IMAGE' : ''}"/>
									        <jsp:param name="entityId" value="${bannerImage.id}"/>
									        <jsp:param name="image" value="${image}"/>
									        <jsp:param name="isMultiple" value="false"/>
									    </jsp:include>
									</k:ajaxUpdate>
								</div>
							</div>
						</div>
					</div>
				</div>			
            </c:if>
        </div>
    </jsp:body>
</te:page>