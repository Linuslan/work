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
   		<input type="hidden" value="${button.id }" name="button.id" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">按钮名称：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="button.name" type="text" class="form-control" value="${button.name }" placeholder="请输入名称">
				</div>
				<label for="pid" class="col-sm-2 control-label">所属菜单：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="parentCombotree" value="${button.menuId }" text="${button.menuName }"></div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">按钮值：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="button.value" value="${button.value }" type="text" class="form-control" placeholder="请输入数字，字母或者下划线">
				</div>
				<label for="pid" class="col-sm-2 control-label">排序值：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="button.orderNo" value="${button.orderNo }" type="text" class="form-control" placeholder="请输入数字">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-10 col-sm-10 no-padding">
					<textarea name="button.memo" class="form-control" rows="3" placeholder="请输入">${button.memo }</textarea>
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
