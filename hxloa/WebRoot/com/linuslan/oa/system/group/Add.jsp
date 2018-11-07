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
    	<div class="box-body">
			<div class="form-group">
				<label for="text" class="col-sm-2 control-label">用户组名称：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<input name="group.text" type="text" class="form-control required" id="text" placeholder="请输入名称">
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="pid" class="col-sm-2 control-label">上级用户组：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<div id="parentCombotree"></div>
				</div>
			</div>
			<div class="form-group">
				<label for="departmentId" class="col-sm-2 control-label">归属部门：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<div id="departmentCombotree"></div>
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="groupId" class="col-sm-2 control-label">组ID：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<input name="group.groupId" type="text" class="form-control" id="groupId" placeholder="请输入字母，数字或下划线">
				</div>
			</div>
			<div class="form-group">
				<label for="orderNo" class="col-sm-2 control-label">排序值：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<input name="group.orderNo" type="text" class="form-control" id="orderNo" placeholder="请选择正整数">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<textarea name="group.memo" class="form-control" rows="3" placeholder="请输入"></textarea>
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
