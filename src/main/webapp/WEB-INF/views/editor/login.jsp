<!DOCTYPE html>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ru" lang="ru" id="extr-page">
	<head>
		<meta charset="utf-8"/>
		<title>Strela</title>
		<meta name="description" content=""/>
		<meta name="author" content="Strela"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
		
	    <script src="/resources/js/libs/jquery-2.0.2.min.js"></script>
		<script src="/resources/js/app.min.js"></script>
		
		<link rel="stylesheet" type="text/css" media="screen" href="/resources/css/libs/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href="/resources/css/libs/font-awesome.min.css"/>

		<link rel="stylesheet" type="text/css" media="screen" href="/resources/css/libs/smartadmin-production.min.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href="/resources/css/libs/smartadmin-skins.min.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href="/resources/css/editor.css"/>

		<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,700italic,300,400,700"/>

		<meta name="apple-mobile-web-app-capable" content="yes"/>
		<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
	</head>
	
	<body class="animated fadeInDown">
		<div id="main" role="main" class="login-page">
			<div id="content" class="container">
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-offset-3 col-md-5 col-lg-4 col-lg-offset-4">
						<div class="well no-padding">
							<form id="login-form" action="/sign_in" class="smart-form client-form" method="post">
								<header>
									Вход
								</header>
									
								<fieldset>
									<section>
										<label class="label">Логин</label>
										<label class="input"> <i class="icon-append fa fa-user"></i>
											<input type="text" name="username"/>
											<b class="tooltip tooltip-top-right"><i class="fa fa-user txt-color-teal"></i> Введите логин</b></label>
									</section>
									<section>
										<label class="label">Пароль</label>
										<label class="input"> <i class="icon-append fa fa-lock"></i>
											<input type="password" name="password"/>
											<b class="tooltip tooltip-top-right"><i class="fa fa-lock txt-color-teal"></i> Введите пароль</b> </label>
									</section>
									<input type="hidden" name="remember" value="true"/>
									<input type="hidden" name="lb_redirect" value="/editor/"/>
								</fieldset>
								<footer>
									<button name="login" type="submit" class="btn btn-primary">
										Вход
									</button>
								</footer>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
