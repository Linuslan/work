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
   	<form id="addCustomerReceiverForm" action="" class="form-horizontal">
   		<input name="customerReceiver.customerId" type="hidden" value="${param.customerId }" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">姓名：</label>
				<div class="col-md-10 col-sm-8 no-padding">
					<input name="customerReceiver.name" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">手机号：</label>
				<div class="col-md-10 col-sm-8 no-padding">
					<input name="customerReceiver.cellphone" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">收货地址：</label>
				<div class="col-md-10 col-sm-8 no-padding">
					<input name="customerReceiver.receiverAddress" type="text" class="form-control">
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		
   	</script>
  </body>
</html>
