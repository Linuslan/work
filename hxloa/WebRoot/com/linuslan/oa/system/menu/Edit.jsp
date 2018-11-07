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
   		<input type="hidden" value="${menu.id }" name="menu.id" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">菜单名称：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<input name="menu.text" type="text" class="form-control" value="${menu.text }" placeholder="">
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="pid" class="col-sm-2 control-label">上级菜单：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<div class="parentCombotree" value="${menu.pid == null ? "" : menu.pid }" text="${parent.text }"></div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">菜单值：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<input name="menu.value" value="${menu.value }" type="text" class="form-control" placeholder="">
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="pid" class="col-sm-2 control-label">排序值：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<input name="menu.orderNo" value="${menu.orderNo }" type="text" class="form-control" placeholder="">
				</div>
			</div>
			<div class="form-group">
				<label for="orderNo" class="col-sm-2 control-label">菜单类型：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<input name="menu.xtype" value="${menu.xtype }" type="text" class="form-control" placeholder="">
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="orderNo" class="col-sm-2 control-label">首页图标：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<input name="menu.indexIcon" type="text" class="form-control" value="${menu.indexIcon }">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">URL：</label>
				<div class="col-md-9 col-sm-10 no-padding">
					<input name="menu.url" value="${menu.url }" type="text" class="form-control" placeholder="">
				</div>
			</div>
			<div class="form-group">
				<label for="icon" class="col-sm-2 control-label">图标URL：</label>
				<div class="col-md-9 col-sm-10 no-padding">
					<input name="menu.icon" value="${menu.icon }" type="text" class="form-control" placeholder="">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-9 col-sm-10 no-padding">
					<textarea name="menu.content" class="form-control" rows="3" placeholder="">${menu.content }</textarea>
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
