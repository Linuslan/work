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
   	<form id="addCapitalForm" action="" class="form-horizontal">
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">固资类别：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="capital.className" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">编号：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="capital.serial" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">名称：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="capital.name" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">型号：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="capital.model" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">厂家：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="capital.shopName" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">资产存放地：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="capital.address" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">归属部门：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="capital.department" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">计量单位：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="capital.unit" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">状态：</label>
				<div class="col-md-2 col-sm-8 left-label">
					<input name="capital.state" type="radio" class="minimal" value="2" checked>&nbsp;正常
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="capital.state" type="radio" class="minimal" value="1">&nbsp;作废
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">借用时间：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="capital.borrowDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">借用单位：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="capital.borrowDepartment" type="text" class="form-control">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">使用方：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="capital.borrowUser" type="text" class="form-control" value="">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">购置时间：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="capital.buyDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">购置价格：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="capital.buyMoney" type="number" class="form-control" value="0">
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">折旧月数：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="capital.depreciationYear" type="text" class="form-control" value="">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2-sm col-sm-4 control-label">备注：</label>
				<div class="col-md-11-sm col-sm-8 no-padding">
					<textarea name="capital.info" class="form-control" rows="3"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addCapitalForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#addCapitalForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
   		});
   	</script>
  </body>
</html>
