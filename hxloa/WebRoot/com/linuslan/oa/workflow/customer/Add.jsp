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
   	<form id="addCustomerForm" action="" class="form-horizontal">
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">客户名称：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.name" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">客户编号：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.serialNo" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<select class="form-control select2" name="customer.companyId">
						<option value="">请选择</option>
						<c:forEach items="${companys }" var="company">
							<option value="${company.id }">${company.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">归属地区：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<select class="form-control select2" name="customer.areaId">
						<option value="">请选择</option>
						<c:forEach items="${areas }" var="area">
							<option value="${area.id }">${area.name }</option>
						</c:forEach>
					</select>
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">排序值：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.orderNo" type="number" value="0" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">单位电话：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.telephone" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">单位传真：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.fax" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">联系人：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.linkman" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">手机号：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.cellphone" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">邮箱：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.email" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">地址：</label>
				<div class="col-md-7-sm col-sm-8 no-padding">
					<input name="customer.address" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">开户行：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.bankName" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">开户名称：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.bankAccountName" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">账号：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.bankAccount" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">开户行地址：</label>
				<div class="col-md-11-sm col-sm-8 no-padding">
					<input name="customer.bankAddress" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">收货人：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.receiver" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">收货人电话：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.receiverPhone" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">邮编：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="customer.zipCode" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">收货人地址：</label>
				<div class="col-md-11-sm col-sm-8 no-padding">
					<input name="customer.receiverAddress" type="text" class="form-control">
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addCustomerForm").find(".select2").select2();
   		});
   	</script>
  </body>
</html>
