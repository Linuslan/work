<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'Add.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
   	<form action="" class="form-horizontal">
   		<input type="hidden" value="${rule.id }" name="rule.id" />
   		<div class="box-body">
   			<!-- <div class="form-group">
				<div class="col-md-10 col-sm-10 no-padding">
					${rule.title }
				</div>
			</div> -->
			<div class="form-group">
				<div class="col-md-12 col-sm-12 no-padding">
					${rule.content }
				</div>
			</div>
   		</div>
    	
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			/*CKEDITOR.replace("ruleContent_edit", {
   				height: "600px"
   			});*/
   		});
   	</script>
  </body>
</html>
