<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" isELIgnored="false"%>
<%@ include file="../../include.jsp" %>

<c:set var="crop" value="${param.crop}"/>
<c:set var="type" value="${param.type}"/>
<c:set var="format" value="${param.format}"/>
<c:set var="entityId" value="${param.entityId}"/>
<c:set var="image" value="${param.image}"/>

<c:set var="isMultiple" value="${param.isMultiple}"/>
<c:if test="${empty isMultiple}">
	<c:set var="isMultiple" value="false"/>
</c:if>

<c:if test="${empty crop}">
	<c:set var="crop" value="true"/>
</c:if>

<div class="upload-image-from">
	<div class="smart-form">
		<fieldset>
			<div class="crop-image-panel">
				<k:ajaxUpdate id="cropImagePanel${not empty format ? format : ''}">
					<section>
						<div class="image-wrapper">
							<div class="image" style="${empty image ? 'display: none;' : ''}">
								<img class="crop-image" src="${image}"/>
							</div>
							
							<c:if test="${not empty image}">
								<div class="remove-image">
									<a href="#">
										<i class="glyphicon glyphicon-remove"> Удалить</i>
									</a>
								</div>
							</c:if>
						</div>
					</section>
					<section>
						<button class="btn btn-primary save-image" style="display: none;">Сохранить</button>
					</section>
				</k:ajaxUpdate>
			</div>
			
			<section>
				<label class="label">Файл</label>
				<label class="input input-file" for="upload">
					<div class="button">
						<c:choose>
							<c:when test="${isMultiple}">
								<input id="upload" type="file" name="uploadImage" multiple="multiple"/>
							</c:when>
							<c:otherwise>
								<input id="upload" type="file" name="uploadImage"/>
							</c:otherwise>
						</c:choose>
						Открыть
					</div>
					<input type="text" placeholder="Выберите файл"/>
				</label>
			</section>
			<section>
				<label class="label">Ссылка</label>
				<label class="input">
					<input type="text" name="imageUrl" class="form-control"/>
		   		</label>
			</section>
			<c:if test="${crop}">
				<%--<section>
					<label class="label"></label>
					<label class="checkbox">
						<input type="checkbox" name="increase"/>
						<i></i> Увеличить до пропорции
					</label>
				</section>--%>
			</c:if>
			<section>
				<button class="btn btn-primary upload">Загрузить</button>
			</section>
			
	   		<input type="hidden" name="type" value="${type}"/>
	   		<input type="hidden" name="id" value="${entityId}"/>
	   		<input type="hidden" name="format" value="${format}"/>
	   		<input type="hidden" name="mode" value="${crop ? 'mode_tmp_upload' : 'mode_upload'}"/>
	   	</fieldset>
	</div>
</div>


