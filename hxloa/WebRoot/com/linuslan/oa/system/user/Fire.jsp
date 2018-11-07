<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'Fire.jsp' starting page</title>
    
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
    <form id="fireUserForm" action="" class="form-horizontal">
    	<input name="user.id" value="${param.userId }" type="hidden" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">离职日期：</label>
				<div class="col-md-9 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="user.leaveDate" type="text" class="form-control date" placeholder="请选择日期">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">离职原因：</label>
				<div class="col-md-9 col-sm-10 no-padding">
					<textarea name="user.leaveMemo" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#fireUserForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   		});
   	</script>
  </body>
</html>
