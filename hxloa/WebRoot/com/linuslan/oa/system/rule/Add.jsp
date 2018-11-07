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
   	<form action="<%=basePath %>sys/rule/add.action" class="form-horizontal" method="POST" enctype="multipart/form-data">
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-1 col-sm-2 control-label">制度名称：</label>
				<div class="col-md-10 col-sm-10 no-padding">
					<input name="rule.title" type="text" class="form-control" placeholder="请输入名称">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-1 col-sm-2 control-label">上传文件：</label>
				<div class="col-md-10 col-sm-10 no-padding">
					<input name="file" type="file" class="form-control files">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-1 col-sm-2 control-label">排序值：</label>
				<div class="col-md-10 col-sm-10 no-padding">
					<input name="rule.orderNo" type="number" value="0" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-1 col-sm-2 control-label">备注：</label>
				<div class="col-md-10 col-sm-10 no-padding">
					<textarea name="rule.memo" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
			
			<div class="form-group">
				<label for="leaderGroup" class="col-md-1 col-sm-2 control-label">内容：</label>
				<div class="col-md-10 col-sm-10 no-padding">
					<textarea name="rule.content" id="ruleContent_add" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			/*CKEDITOR.replace("ruleContent_add", {
   				height: "600px"
   			});*/
   		});
   	</script>
  </body>
</html>
