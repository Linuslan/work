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
   	<form id="addPhoneForm" action="" class="form-horizontal">
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">使用部门：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="phone.department" type="text" class="form-control">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">电话号码：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="phone.phoneNo" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">使用人：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="phone.userName" type="text" class="form-control">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">使用地点：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="phone.address" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">月租：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="phone.monthlyRent" type="text" class="form-control">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">户名：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="phone.accountName" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">缴费方式：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="phone.paymentWay" type="text" class="form-control">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">是否提醒：</label>
				<div class="col-md-3 col-sm-8 left-label">
					<input name="phone.isRemind" type="radio" class="minimal" value="0" checked>&nbsp;是
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="phone.isRemind" type="radio" class="minimal" value="1">&nbsp;否
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">套餐结束时间：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="phone.packageEndDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
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
					<textarea name="phone.phonePackage" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">用途：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<textarea name="phone.content" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<textarea name="phone.info" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addPhoneForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#addPhoneForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
   		});
   	</script>
  </body>
</html>
