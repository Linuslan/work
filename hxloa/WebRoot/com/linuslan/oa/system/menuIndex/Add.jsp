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
   	<form id="addMenuIndexForm" action="" class="form-horizontal">
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">索引名称：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="menuIndex.name" type="text" class="form-control">
				</div>
				<label for="pid" class="col-sm-2 control-label">所属菜单：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="parentCombotree" value="${menu.id }" text="${menu.text }"></div>
				</div>
			</div>
			<div class="form-group">
				<label for="pid" class="col-sm-2 control-label">索引值：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="menuIndex.value" type="text" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label"></label>
				<div class="col-md-4 col-sm-10 no-padding">
					
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-10 col-sm-10 no-padding">
					<textarea name="menuIndex.remark" class="form-control" rows="3" placeholder="请输入"></textarea>
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
