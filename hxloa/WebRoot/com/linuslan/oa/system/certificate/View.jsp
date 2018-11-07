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
   	<form id="viewCertificateForm" action="" class="form-horizontal">
   		<input type="hidden" value="${certificate.id }" name="certificate.id" />
   		<div class="form-group">
			<label for="name" class="col-md-2 col-sm-4 control-label">公司名称：</label>
			<div class="col-md-3 col-sm-8 left-label">
				${certificate.company }
			</div>
			<div class="col-md-1 col-sm-12"></div>
			<label for="name" class="col-md-2 col-sm-4 control-label">证件名称：</label>
			<div class="col-md-3 col-sm-8 left-label">
				${certificate.name }
			</div>
		</div>
		<div class="form-group">
			<label for="name" class="col-md-2 col-sm-4 control-label">开始时间：</label>
			<div class="col-md-3 col-sm-8 left-label">
				<fmt:formatDate value="${certificate.startDate }" pattern="yyyy-MM-dd" />
			</div>
			<div class="col-md-1 col-sm-12"></div>
			<label for="name" class="col-md-2 col-sm-4 control-label">结束时间：</label>
			<div class="col-md-3 col-sm-8 left-label">
				<fmt:formatDate value="${certificate.endDate }" pattern="yyyy-MM-dd" />
			</div>
		</div>
		<div class="form-group">
			<label for="name" class="col-md-2 col-sm-4 control-label">年检时间：</label>
			<div class="col-md-3 col-sm-8 left-label">
				<fmt:formatDate value="${certificate.inspectionDate }" pattern="yyyy-MM-dd" />
			</div>
			<div class="col-md-1 col-sm-12"></div>
			<label for="name" class="col-md-2 col-sm-4 control-label">是否提醒：</label>
			<div class="col-md-3 col-sm-8 left-label">
				${certificate.isRemind == 0 ? "是" : "否" }
			</div>
		</div>
		<div class="form-group">
			<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
			<div class="col-md-9 col-sm-9 left-label">
				${certificate.memo }
			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			
   		});
   	</script>
  </body>
</html>
