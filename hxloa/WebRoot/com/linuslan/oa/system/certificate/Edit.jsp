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
   	<form id="editCertificateForm" action="" class="form-horizontal">
   		<input type="hidden" value="${certificate.id }" name="certificate.id" />
   		<div class="form-group">
			<label for="name" class="col-md-2 col-sm-4 control-label">公司名称：</label>
			<div class="col-md-3 col-sm-8 no-padding">
				<input name="certificate.company" type="text" class="form-control" placeholder="请输入名称" value="${certificate.company }">
			</div>
			<div class="col-md-1 col-sm-12"></div>
			<label for="name" class="col-md-2 col-sm-4 control-label">证件名称：</label>
			<div class="col-md-3 col-sm-8 no-padding">
				<input name="certificate.name" type="text" class="form-control" placeholder="请输入名称" value="${certificate.name }">
			</div>
		</div>
		<div class="form-group">
			<label for="name" class="col-md-2 col-sm-4 control-label">开始时间：</label>
			<div class="col-md-3 col-sm-8 no-padding">
				<div class="input-group">
					<div class="input-group-addon">
						<i class="fa fa-calendar"></i>
					</div>
					<input name="certificate.startDate" type="text" value="<fmt:formatDate value="${certificate.startDate }" pattern="yyyy-MM-dd" />" readonly="readonly" class="form-control pull-right date showText" id="text">
				</div>
			</div>
			<div class="col-md-1 col-sm-12"></div>
			<label for="name" class="col-md-2 col-sm-4 control-label">结束时间：</label>
			<div class="col-md-3 col-sm-8 no-padding">
				<div class="input-group">
					<div class="input-group-addon">
						<i class="fa fa-calendar"></i>
					</div>
					<input name="certificate.endDate" type="text" value="<fmt:formatDate value="${certificate.endDate }" pattern="yyyy-MM-dd" />" readonly="readonly" class="form-control pull-right date showText" id="text">
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="name" class="col-md-2 col-sm-4 control-label">年检时间：</label>
			<div class="col-md-3 col-sm-8 no-padding">
				<div class="input-group">
					<div class="input-group-addon">
						<i class="fa fa-calendar"></i>
					</div>
					<input name="certificate.inspectionDate" type="text" value="<fmt:formatDate value="${certificate.inspectionDate }" pattern="yyyy-MM-dd" />" readonly="readonly" class="form-control pull-right date showText" id="text">
				</div>
			</div>
			<div class="col-md-1 col-sm-12"></div>
			<label for="name" class="col-md-2 col-sm-4 control-label">是否提醒：</label>
			<div class="col-md-3 col-sm-8 left-label">
				<input name="certificate.isRemind" type="radio" class="minimal" value="0" ${certificate.isRemind == 0 ? "checked" : "" }>&nbsp;是
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input name="certificate.isRemind" type="radio" class="minimal" value="1" ${certificate.isRemind == 1 ? "checked" : "" }>&nbsp;否
			</div>
		</div>
		<div class="form-group">
			<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
			<div class="col-md-9 col-sm-9 no-padding">
				<textarea name="certificate.memo" class="form-control" rows="3" placeholder="请输入">${certificate.memo }</textarea>
			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#editCertificateForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#editCertificateForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
   		});
   	</script>
  </body>
</html>
