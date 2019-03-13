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
   	<form id="addUserSalaryForm" action="" class="form-horizontal">
    	<div class="box-body">
    		<div class="form-group">
				<label for="name" class="col-sm-2 control-label">用户：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${user.name }
					<input name="userSalary.userId" type="hidden" class="form-control" value="${user.id }">
				</div>
				<label for="pid" class="col-sm-2 control-label">归属部门：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="departmentCombotree" value="${user.departmentId }" text="${user.departmentName }"></div>
				</div>
			</div>
    		<div class="form-group">
    			<label for="name" class="col-sm-2 control-label">岗位：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<select class="form-control" name="userSalary.postId">
						<option value="">请选择</option>
						<c:forEach items="${posts }" var="post">
							<option value="${post.id }" ${user.postId == post.id ? "selected='selected'" : "" }>${post.name }</option>
						</c:forEach>
					</select>
				</div>
				<label for="name" class="col-sm-2 control-label">薪资类型：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<select class="form-control" name="userSalary.typeId">
						<option value="">请选择</option>
						<c:forEach items="${types }" var="type">
							<option value="${type.id }">${type.text }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">基本工资：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.basicSalary" type="number" value="0" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">岗位工资：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.postSalary" type="number" value="0" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">绩效工资：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.achievementSalary" type="number" value="0" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">试用期工资：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.achievementSalary" type="number" value="0" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">试用期开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.probationStartTime" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
				<label for="name" class="col-sm-2 control-label">试用期结束时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.probationEndTime" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">生效时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.effectDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
				<label for="sex" class="col-sm-2 control-label">是否强制生效：</label>
				<div class="col-md-4 col-sm-10 left-label">
					<input type="radio" name="userSalary.isForceEffect" class="minimal" value="0" checked>否&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="userSalary.isForceEffect" class="minimal" value="1" >&nbsp;是
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">社保基数：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.socialInsurance" type="text" value="0" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">社保开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.socialInsuranceStartDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">医保基数：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.healthInsurance" type="text" value="0" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">医保开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.healthInsuranceStartDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">公积金：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.housingFund" type="text" value="0" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">医保开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.housingFundStartDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">话费补贴：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.telCharge" type="text" value="0" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">话费补贴开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.telChargeStartDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">交通补贴：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.travelAllowance" type="text" value="0" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">交通补贴开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.travelAllowanceStartDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">工龄工资：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.serviceAgeSalary" type="text" value="0" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">工龄工资开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.serviceAgeSalaryStartDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">住房补贴：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.housingSubsidy" type="text" value="0" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">住房补贴开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.housingSubsidyStartTime" type="text" value="" readonly="readonly" class="form-control pull-right date showText">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">子女抚养费：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.childcareExpense" type="number" value="0" class="form-control">
				</div>
				<label class="col-sm-2 control-label">继续教育费：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.continuingEducationFee" type="number" value="0" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">大病医疗费：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.seriousIllnessExpense" type="number" value="0" class="form-control">
				</div>
				<label class="col-sm-2 control-label">住房贷款利息：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.housingLoan" type="number" value="0" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">住房租金费：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.housingRent" type="number" value="0" class="form-control">
				</div>
				<label class="col-sm-2 control-label">赡养老人费：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.alimony" type="number" value="0" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">工作餐补贴：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.mealSubsidy" type="text" value="0" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-10 col-sm-10 no-padding">
					<textarea name="userSalary.remark" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addUserSalaryForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
	    	$("#addUserSalaryForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
   			$("#addUserSalaryForm").find(".departmentCombotree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "userSalary.departmentId",
				pidField: "pid"
			});
   		});
   	</script>
  </body>
</html>
