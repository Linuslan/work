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
   	<form id="viewUserSalaryForm" action="" class="form-horizontal">
   		<input type="hidden" name="userSalary.id" value="${userSalary.id }" />
    	<div class="box-body">
    		<div class="form-group">
				<label for="name" class="col-sm-2 control-label">用户：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.userName }
				</div>
				<label for="pid" class="col-sm-2 control-label">归属部门：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.departmentName }
				</div>
			</div>
    		<div class="form-group">
    			<label for="name" class="col-sm-2 control-label">岗位：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.postName }
				</div>
				<label for="name" class="col-sm-2 control-label">薪资类型：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.typeName }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">基本工资：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.basicSalary }
				</div>
				<label for="name" class="col-sm-2 control-label">岗位工资：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.postSalary }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">绩效工资：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.achievementSalary }
				</div>
				<label for="name" class="col-sm-2 control-label">试用期工资：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.probationSalary }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">试用期开始时间：</label>
				<div class="col-md-4 col-sm-10 left-label">
					<fmt:formatDate value="${userSalary.probationStartTime }" pattern="yyyy-MM-dd"/>
				</div>
				<label for="name" class="col-sm-2 control-label">试用期结束时间：</label>
				<div class="col-md-4 col-sm-10 left-label">
					<fmt:formatDate value="${userSalary.probationEndTime }" pattern="yyyy-MM-dd"/>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">生效时间：</label>
				<div class="col-md-4 col-sm-10 left-label">
					<fmt:formatDate value="${userSalary.effectDate }" pattern="yyyy-MM-dd"/>
				</div>
				<label for="sex" class="col-sm-2 control-label">是否强制生效：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.isForceEffect == 0 ? "否" : "是" }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">社保基数：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.socialInsurance }
				</div>
				<label for="name" class="col-sm-2 control-label">社保开始时间：</label>
				<div class="col-md-4 col-sm-10 left-label">
					<fmt:formatDate value="${userSalary.socialInsuranceStartDate }" pattern="yyyy-MM-dd"/>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">医保基数：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.healthInsurance }
				</div>
				<label for="name" class="col-sm-2 control-label">医保开始时间：</label>
				<div class="col-md-4 col-sm-10 left-label">
					<fmt:formatDate value="${userSalary.healthInsuranceStartDate }" pattern="yyyy-MM-dd"/>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">公积金：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.housingFund }
				</div>
				<label for="name" class="col-sm-2 control-label">医保开始时间：</label>
				<div class="col-md-4 col-sm-10 left-label">
					<fmt:formatDate value="${userSalary.housingFundStartDate }" pattern="yyyy-MM-dd"/>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">话费补贴：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.telCharge }
				</div>
				<label for="name" class="col-sm-2 control-label">话费补贴开始时间：</label>
				<div class="col-md-4 col-sm-10 left-label">
					<fmt:formatDate value="${userSalary.telChargeStartDate }" pattern="yyyy-MM-dd"/>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">交通补贴：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.travelAllowance }
				</div>
				<label for="name" class="col-sm-2 control-label">交通补贴开始时间：</label>
				<div class="col-md-4 col-sm-10 left-label">
					<fmt:formatDate value="${userSalary.travelAllowanceStartDate }" pattern="yyyy-MM-dd"/>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">工龄工资：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.serviceAgeSalary }
				</div>
				<label for="name" class="col-sm-2 control-label">工龄工资开始时间：</label>
				<div class="col-md-4 col-sm-10 left-label">
					<fmt:formatDate value="${userSalary.serviceAgeSalaryStartDate }" pattern="yyyy-MM-dd"/>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">住房补贴：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.housingSubsidy }
				</div>
				<label for="name" class="col-sm-2 control-label">住房补贴开始时间：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.housingSubsidyStartTime }
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">子女抚养费：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.childcareExpense}
				</div>
				<label class="col-sm-2 control-label">继续教育费：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.continuingEducationFee}
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">大病医疗费：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.seriousIllnessExpense}
				</div>
				<label class="col-sm-2 control-label">住房贷款利息：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.housingLoan}
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">住房租金费：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.housingRent}
				</div>
				<label class="col-sm-2 control-label">赡养老人费：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.alimony}
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">工作餐补贴：</label>
				<div class="col-md-4 col-sm-10 left-label">
					${userSalary.mealSubsidy }
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-10 col-sm-10 left-label">
					${userSalary.remark }
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
