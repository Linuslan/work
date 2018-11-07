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
   	<form id="editCellphoneForm" action="" class="form-horizontal">
   		<input type="hidden" value="${cellphone.id }" name="cellphone.id" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">使用部门：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="cellphone.department" type="text" class="form-control" value="${cellphone.department }">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">电话号码：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="cellphone.phoneNo" type="text" class="form-control" value="${cellphone.phoneNo }">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">使用人：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="cellphone.userName" type="text" class="form-control" value="${cellphone.userName }">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">报销限额：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="cellphone.reimburseLimit" type="text" class="form-control" value="${cellphone.reimburseLimit }">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">月租：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="cellphone.monthlyRent" type="text" class="form-control" value="${cellphone.monthlyRent }">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">户名：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="cellphone.accountName" type="text" class="form-control" value="${cellphone.accountName }">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">缴费方式：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="cellphone.paymentWay" type="text" class="form-control" value="${cellphone.paymentWay }">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">是否提醒：</label>
				<div class="col-md-3 col-sm-8 left-label">
					<input name="cellphone.isRemind" type="radio" class="minimal" value="0" ${cellphone.isRemind == 0 ? "checked" : "" }>&nbsp;是
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="cellphone.isRemind" type="radio" class="minimal" value="1" ${cellphone.isRemind == 1 ? "checked" : "" }>&nbsp;否
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">套餐结束时间：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="cellphone.packageEndDate" type="text" value="<fmt:formatDate value="${cellphone.packageEndDate }" pattern="yyyy-MM-dd" />" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label"></label>
				<div class="col-md-3 col-sm-8 no-padding">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">套餐：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<textarea name="cellphone.phonePackage" class="form-control" rows="3" placeholder="请输入">${cellphone.phonePackage }</textarea>
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">用途：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<textarea name="cellphone.content" class="form-control" rows="3" placeholder="请输入">${cellphone.content }</textarea>
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<textarea name="cellphone.info" class="form-control" rows="3" placeholder="请输入">${cellphone.info }</textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#editCellphoneForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#editCellphoneForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
   		});
   	</script>
  </body>
</html>
