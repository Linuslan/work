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
   	<form id="editSupplierForm" action="" class="form-horizontal">
   		<input type="hidden" value="${supplier.id }" name="supplier.id" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">供货商名称：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.name }" name="supplier.name" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">供货商编号：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.serialNo }" name="supplier.serialNo" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label"></label>
				<div class="col-md-2 col-sm-8 no-padding">
					
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">排序值：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.orderNo }" name="supplier.orderNo" type="number" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">单位电话：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.telephone }" name="supplier.telephone" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">单位传真：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.fax }" name="supplier.fax" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">联系人：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.linkman }" name="supplier.linkman" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">手机号：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.cellphone }" name="supplier.cellphone" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">邮箱：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.email }" name="supplier.email" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">地址：</label>
				<div class="col-md-11-sm col-sm-8 no-padding">
					<input value="${supplier.address }" name="supplier.address" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">开户行：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.bankName }" name="supplier.bankName" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">开户名称：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.bankAccountName }" name="supplier.bankAccountName" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">账号：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.bankAccount }" name="supplier.bankAccount" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">开户行地址：</label>
				<div class="col-md-11-sm col-sm-8 no-padding">
					<input value="${supplier.bankAddress }" name="supplier.bankAddress" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">收货人：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.receiver }" name="supplier.receiver" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">收货人电话：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.receiverPhone }" name="supplier.receiverPhone" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">邮编：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input value="${supplier.zipCode }" name="supplier.zipCode" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">收货人地址：</label>
				<div class="col-md-11-sm col-sm-8 no-padding">
					<input value="${supplier.receiverAddress }" name="supplier.receiverAddress" type="text" class="form-control">
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#editSupplierForm").find(".select2").select2();
   		});
   	</script>
  </body>
</html>
