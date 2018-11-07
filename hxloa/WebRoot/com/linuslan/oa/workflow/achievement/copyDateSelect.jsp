<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新增绩效申请</title>
    
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
  	
   	<form id="copeAchievementDateSelectForm" action="" class="form-horizontal">
   		<div class="form-horizontal">
	  		<div class="box-body padding-bottom5 bottom-dotted-border" >
		  		<div class="form-group">
					<label for="text" class="col-md-2 col-sm-4 control-label">日期：</label>
					<div class="col-md-3 col-sm-8 no-padding">
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>
							<input readonly="readonly" name="date" type="text" class="form-control pull-right date">
						</div>
					</div>
				</div>
			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#copeAchievementDateSelectForm").find(".date").datepicker({
   				startView: 1,
   				maxViewMode: 1,
   				minViewMode:1,
   				forceParse: false,
   				language: "zh-CN",
   				format: "yyyy-mm"
   			});
   		});
   	</script>
  </body>
</html>
