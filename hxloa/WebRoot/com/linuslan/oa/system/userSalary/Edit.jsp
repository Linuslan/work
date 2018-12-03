<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/c" %>
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
   	<form id="editUserSalaryForm" action="" class="form-horizontal">
   		<input type="hidden" name="userSalary.id" value="${userSalary.id }" />
    	<div class="box-body">
    		<div class="form-group">
				<label for="name" class="col-sm-2 control-label">用户：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.userName }
					<input name="userSalary.userId" type="hidden" class="form-control" value="${userSalary.userId }">
				</div>
				<label for="pid" class="col-sm-2 control-label">归属部门：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="departmentCombotree" value="${userSalary.departmentId }" text="${userSalary.departmentName }"></div>
				</div>
			</div>
    		<div class="form-group">
    			<label for="name" class="col-sm-2 control-label">岗位：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<select class="form-control" name="userSalary.postId">
						<option value="">请选择</option>
						<c:forEach items="${posts }" var="post">
							<option value="${post.id }" ${userSalary.postId == post.id ? "selected='selected'" : "" }>${post.name }</option>
						</c:forEach>
					</select>
				</div>
				<label for="name" class="col-sm-2 control-label">薪资类型：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<select class="form-control" name="userSalary.typeId">
						<option value="">请选择</option>
						<c:forEach items="${types }" var="type">
							<option value="${type.id }" ${userSalary.typeId == type.id ? "selected='selected'" : "" }>${type.text }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">基本工资：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.basicSalary" type="number" value="${userSalary.basicSalary }" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">岗位工资：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.postSalary" type="number" value="${userSalary.postSalary }" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">绩效工资：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.achievementSalary" type="number" value="${userSalary.achievementSalary }" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">试用期工资：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.probationSalary" type="number" value="${userSalary.probationSalary }" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">试用期开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.probationStartTime" type="text" value="<fmt:formatDate value="${userSalary.probationStartTime }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
				<label for="name" class="col-sm-2 control-label">试用期结束时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.probationEndTime" type="text" value="<fmt:formatDate value="${userSalary.probationEndTime }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
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
						<input name="userSalary.effectDate" type="text" value="<fmt:formatDate value="${userSalary.effectDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText">
					</div>
				</div>
				<label for="sex" class="col-sm-2 control-label">是否强制生效：</label>
				<div class="col-md-4 col-sm-10 left-label">
					<input type="radio" name="userSalary.isForceEffect" class="minimal" value="0" ${userSalary.isForceEffect == 0 ? "checked" : "" } >否&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="userSalary.isForceEffect" class="minimal" value="1" ${userSalary.isForceEffect == 1 ? "checked" : "" } >&nbsp;是
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">社保基数：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.socialInsurance" type="text" value="${userSalary.socialInsurance }" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">社保开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.socialInsuranceStartDate" type="text" value="<fmt:formatDate value="${userSalary.socialInsuranceStartDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">医保基数：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.healthInsurance" type="text" value="${userSalary.healthInsurance }" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">医保开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.healthInsuranceStartDate" type="text" value="<fmt:formatDate value="${userSalary.healthInsuranceStartDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">公积金：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.housingFund" type="text" value="${userSalary.housingFund }" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">医保开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.housingFundStartDate" type="text" value="<fmt:formatDate value="${userSalary.housingFundStartDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">话费补贴：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.telCharge" type="text" value="${userSalary.telCharge }" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">话费补贴开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.telChargeStartDate" type="text" value="<fmt:formatDate value="${userSalary.telChargeStartDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">交通补贴：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.travelAllowance" type="text" value="${userSalary.travelAllowance }" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">交通补贴开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.travelAllowanceStartDate" type="text" value="<fmt:formatDate value="${userSalary.travelAllowanceStartDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">工龄工资：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.serviceAgeSalary" type="text" value="${userSalary.serviceAgeSalary }" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">工龄工资开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.serviceAgeSalaryStartDate" type="text" value="<fmt:formatDate value="${userSalary.serviceAgeSalaryStartDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">住房补贴：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.housingSubsidy" type="text" value="${userSalary.housingSubsidy }" class="form-control">
				</div>
				<label for="name" class="col-sm-2 control-label">住房补贴开始时间：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userSalary.housingSubsidyStartTime" type="text" value="<fmt:formatDate value="${userSalary.housingSubsidyStartTime }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">工作餐补贴：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="userSalary.mealSubsidy" type="text" value="${userSalary.mealSubsidy }" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-10 col-sm-10 no-padding">
					<textarea name="userSalary.remark" class="form-control" rows="3" placeholder="请输入">${userSalary.remark }</textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   		
   			$("#editUserSalaryForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
	    	$("#editUserSalaryForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
   			
   			//初始化父部门树，只有在这里等弹出框的内容都加载完之后再加载树才能够获取到宽度
	    	//初始化上级组字段的选择项
   			$("#editUserSalaryForm").find(".departmentCombotree").combotree({
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
