<%@ tag pageEncoding='UTF-8' %>
<%@ include file="../../views/include.jsp" %>

<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="initScript" fragment="true" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ru" lang="ru">
	<head>
	    <meta charset="utf-8" />
	    <title>${title}</title>
	
		<link rel="icon" href="/resources/favicon.ico" type="/favicon.ico"/>
	
		<link rel="stylesheet" type="text/css" media="screen" href="/resources/css/libs/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href="/resources/css/libs/font-awesome.min.css"/>
	    <link rel="stylesheet" type="text/css" media="screen" href="/resources/css/libs/smartadmin-production.min.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href="/resources/css/libs/smartadmin-skins.min.css"/>
	    <link rel="stylesheet" type="text/css" media="all" href="/resources/css/editor.css"/>
	    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,700italic,300,400,700"/>
	
		<script src="/resources/js/libs/jquery-2.0.2.min.js"></script>
		<script src="/resources/js/libs/jquery-ui-1.10.3.min.js"></script>
		<script src="/resources/js/libs/jquery-ui-1.8.23.custom.min.js"></script>
		<script src="/resources/js/libs/jquery-migrate-1.2.1.min.js"></script>
		<script src="/resources/js/libs/jquery.timers.js" type="text/javascript"></script>
		<script src="/resources/js/libs/jquery.fileupload.js"></script>
		<script src="/resources/js/libs/jquery.fileupload-process.js"></script>
		<script src="/resources/js/libs/jquery.fileupload-validate.js"></script>
		<script src="/resources/js/libs/jquery.Jcrop.js"></script>
		<script src="/resources/js/libs/jquery.uploadify.js"></script>
				
		<script src="/resources/js/libs/bootstrap/bootstrap.min.js"></script>
		<script src="/resources/js/libs/notification/SmartNotification.min.js"></script>
		<script src="/resources/js/libs/smartwidgets/jarvis.widget.min.js"></script>
		<script src="/resources/js/libs/plugin/select2/select2.min.js"></script>
		<script src="/resources/js/libs/plugin/bootstrap-slider/bootstrap-slider.min.js"></script>
		<script src="/resources/js/libs/plugin/summernote/summernote.min.js"></script>
		<script src="/resources/js/libs/plugin/superbox/superbox.min.js"></script>
		<script src="/resources/js/libs/plugin/bootstrap-timepicker.min.js"></script>
		<script src="/resources/js/app.config.js"></script>
		<script src="/resources/js/app.min.js"></script>
		<script src="https://api-maps.yandex.ru/2.1/?lang=ru_RU" type="text/javascript"> </script>
		
	    <script type="text/javascript" src="/resources/js/commons.js"></script>
		<script type="text/javascript" src="/resources/js/util.js"></script>
	    <script type="text/javascript" src="/resources/js/editor.js"></script>
	</head>
	<body>
		<header id="header">
			<div id="logo-group">
				<span id="logo">
					<h1>Strela</h1>
				</span>
			</div>

            <div id="logout" class="btn-header transparent pull-right">
                <span> <a href="/editor/logout" title="Sign Out" data-action="userLogout" data-logout-msg="You can improve your security further after logging out by closing this opened browser"><i class="fa fa-sign-out"></i></a> </span>
            </div>
		</header>
		
		<aside id="left-panel">
			<div class="login-info">
				<span>
					<a href="javascript:void(0);" id="show-shortcut" data-action="toggleShortcut">
						<span>
							Пользователь: <sec:authentication property="principal.username"/>
						</span>
					</a>
				</span>
			</div>
			<nav>
				<ul>
                    <c:forEach items="${menu}" var="menuItem">
				  		<li class="${fn:contains(currentHref, menuItem.href) ? 'active' : ''}">
				  			<a href="${menuItem.href != '#' ? '/editor/' : ''}${menuItem.href}/">
				  				<i class="fa fa-lg fa-fw ${menuItem.icon}"></i>
				  				<span class="menu-item-parent">${menuItem.caption}</span>
				  			</a>
				  			<c:if test="${fn:length(menuItem.items) gt 0}">
								<ul>
				  				<c:forEach items="${menuItem.items}" var="menuSubItem">
				  					<li class="${fn:contains(currentHref, menuSubItem.href) ? 'active' : ''}">
				    					<a href="/editor/${menuSubItem.href}/">${menuSubItem.caption}</a>
				    				</li>
				  				</c:forEach>
				  				</ul>
							</c:if>
				  		</li>
				  	</c:forEach>
				</ul>
			</nav>
		</aside>
		
		<div id="main">
			<div id="ribbon">
				<ol class="breadcrumb">
					<li>Администрирование</li>
                    <li>${activeMenu.caption}</li>
				</ol>
			</div>
            <div id="content">
				<jsp:doBody/>
          	</div>
		</div>
		
		<div class="page-footer">
			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<span class="txt-color-white">Strela</span>
				</div>
		
			</div>
		</div>
	
		<script type="text/javascript">
		    $(document).ready(function() {
		        E.initBase();
		        <jsp:invoke fragment="initScript" />
		    });
		</script>
	
	</body>
</html>