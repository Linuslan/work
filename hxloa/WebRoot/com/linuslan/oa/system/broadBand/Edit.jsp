<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="/fmt" %>
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
   	<form id="editBroadBandForm" action="" class="form-horizontal">
   		<input type="hidden" value="${broadBand.id }" name="broadBand.id" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">业务号：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="broadBand.businessNo" type="text" class="form-control" value="${broadBand.businessNo }">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">宽带类型：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="broadBand.typeName" type="text" class="form-control" value="${broadBand.typeName }">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">月租：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="broadBand.monthlyRent" type="text" class="form-control" value="${broadBand.monthlyRent }">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">带宽：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="broadBand.kbps" type="text" class="form-control" value="${broadBand.kbps }">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">IP：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="broadBand.ips" type="text" class="form-control" value="${broadBand.ips }">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">是否提醒：</label>
				<div class="col-md-3 col-sm-8 left-label">
					<input name="broadBand.isRemind" type="radio" class="minimal" value="0" ${broadBand.isRemind == 0 ? "checked" : "" }>&nbsp;是
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="broadBand.isRemind" type="radio" class="minimal" value="1" ${broadBand.isRemind == 1 ? "checked" : "" }>&nbsp;否
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<textarea name="broadBand.info" class="form-control" rows="3" placeholder="请输入">${broadBand.info }</textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#editBroadBandForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#editBroadBandForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
   		});
   	</script>
  </body>
</html>
