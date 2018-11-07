<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>OA登录</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="resources/bootstrap/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="resources/dialog/dist/css/bootstrap-dialog.css">
	<link rel="stylesheet" type="text/css" href="resources/login/css/login.css">
	<script src="resources/bootstrap/plugins/jQuery/jQuery-2.1.4.min.js"></script>
	<script src="resources/bootstrap/bootstrap/js/bootstrap.min.js"></script>
	<script src="resources/dialog/dist/js/bootstrap-dialog.js"></script>
	<script type="text/javascript" src="resources/javascript/bstoa.js"></script>
    <script type="text/javascript" src="resources/javascript/util.js"></script>
    <style type="text/css">
    	* {
    		-webkit-box-sizing: content-box;
    		box-sizing: content-box;
    	}
    	.bootstrap-dialog-body {
    		color: black;
    	}
    </style>
  </head>
  
  <body>
    <div class="container clearfix">
		<!-----------------页面左侧文字------------------>
		<div class="secL">
			<h2></h2>
			<p>
				 
			</p>
		</div>
		<!-----------------页面右侧表单------------------>
		<div class="secR">
			<!-----页面右侧透明背景----->
			<div class="box-bg"></div>
			<!--------表单内容------------>
			<form action="javascript:void(0);" method="post" name="frmLogin" id="loginForm">
				<div class="form">
					<h1>登录平台</h1>
					<p>
						请输入<span class="f9c442">用户名、密码以及验证码</span>
					</p>
					<div class="item clearfix">
						<label for="userNameIpt"></label> 
						<input type="text" tabindex="1" id="userNameIpt" name="user.loginName"
							notnull="true" info="用户名" placeholder="用户名"/>
					</div>
					<div class="item itempass clearfix">
						<label for="password"></label> 
						<input type="password" tabindex="2" id="password" name="user.password" notnull="true" info="密码"
							autocomplete="off" placeholder="请输入密码" />
					</div>
					<div class="item validatepic clearfix">
						<img id="Code" src="<%=basePath %>validateImageServlet.valid" width="75" height="30"
							style="display: inline; float: left;"/> 
						<input id="pwdInput" name="validateNum"
							tabindex="3" class="ipt ipt-y f_l" type="text" style="margin-right: 5px; display: inline;" notnull="true"
							info="验证码" autocomplete="off" disableautocomplete /> 
						<a tabindex="4" class="changepic" id="forGetPassword" href="javascript:refreshValidateCode();">换一张?</a>
					</div>
					<div class="item">
					   <button type="button" tabindex="5" id="login">登&nbsp;&nbsp;录</button>
					</div>
				</div>
			</form>
		</div>
	</div>

	<script type="text/javascript">
		var isLogin = false;
		$(function() {
			$("#userNameIpt").focus();
			$(document).keyup(function(event) {
	  			var keyCode = event.keyCode;
	  			if(keyCode == "13" && !isLogin) {
	  				$("#login").click();
	  			}
	  		});
			BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DEFAULT] = "温馨提示";
    		BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_INFO] = "温馨提示";
    		BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_PRIMARY] = "温馨提示";
    		BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_SUCCESS] = "成功";
    		BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_WARNING] = "警告";
    		BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DANGER] = "错误";
    		BootstrapDialog.DEFAULT_TEXTS['OK'] = "确定";
    		BootstrapDialog.DEFAULT_TEXTS['CANCEL'] = "取消";
    		BootstrapDialog.DEFAULT_TEXTS['CONFIRM'] = "温馨提示";
			$("#login").click(function() {
				isLogin = true;
				$.ajax({
					url: getRoot() + "sys/user/login.action",
					data: $("#loginForm").serialize(),
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						if(json.success == true) {
							window.location.href=getRoot() + "sys/menu/index.action";
						} else {
							BootstrapDialog.show({
								type: BootstrapDialog.TYPE_DANGER,
								title: "温馨提示",
					            message: json.msg,
					            onhidden: function(dialogRef){
					                isLogin = false;
					            }
					        });
						}
					},
					error: function() {
						BootstrapDialog.show({
							type: BootstrapDialog.TYPE_DANGER,
							title: "温馨提示",
							message: "系统异常，请联系管理员",
							onhidden: function() {
								isLogin = false;
							}
						});
					}
				});
			});
		});
		
		function refreshValidateCode() {
			document.getElementById("Code").src = getRoot() + "validateImageServlet.valid?"+Math.random();
	    }
	</script>

  </body>
</html>
