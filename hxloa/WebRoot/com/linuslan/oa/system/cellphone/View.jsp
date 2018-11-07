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
   	<form id="viewCellphoneForm" action="" class="form-horizontal">
   		<input type="hidden" value="${cellphone.id }" name="cellphone.id" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">使用部门：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${cellphone.department }
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">电话号码：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${cellphone.phoneNo }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">使用人：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${cellphone.userName }
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">报销限额：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${cellphone.reimburseLimit }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">月租：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${cellphone.monthlyRent }
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">户名：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${cellphone.accountName }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">缴费方式：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${cellphone.paymentWay }
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">是否提醒：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${cellphone.isRemind == 0 ? "是" : "否" }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">套餐结束时间：</label>
				<div class="col-md-3 col-sm-8 left-label">
					<fmt:formatDate value="${cellphone.packageEndDate }" pattern="yyyy-MM-dd" />
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label"></label>
				<div class="col-md-3 col-sm-8 left-label">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">套餐：</label>
				<div class="col-md-9 col-sm-9 left-label">
					${cellphone.phonePackage }
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">用途：</label>
				<div class="col-md-9 col-sm-9 left-label">
					${cellphone.content }
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-9 col-sm-9 left-label">
					${cellphone.info }
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			
   		});
   	</script>
  </body>
</html>
