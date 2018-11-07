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
   	<form id="addEffectForm" action="" class="form-horizontal">
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">效果名称：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="effect.name" type="text" class="form-control">
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">排序值：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="effect.orderNo" type="number" value="0" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-9 col-sm-10 no-padding">
					<textarea name="effect.remark" class="form-control" rows="3"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			
   		});
   	</script>
  </body>
</html>
