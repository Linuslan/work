<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/c" %>
<%@ taglib prefix="fmt" uri="/fmt" %>
<%@ taglib prefix="fn" uri="/fn" %>
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
   	<form id="viewUserForm" action="" class="form-horizontal">
   		<input name="user.id" type="hidden" value="${user.id }">
   		<div class="col-xs-12" style="height: 99%">
   			<div class="box box-solid" style="height: 99%">
		    	<div class="box-body">
		    		<div class="nav-tabs-top-border">
			   			<ul class="nav nav-tabs">
			   				<li class="active">
			   					<a href="#user_info_view" data-toggle="tab">个人信息</a>
			   				</li>
			   				<li>
			   					<a href="#user_userSalary_view" data-toggle="tab">薪资福利</a>
			   				</li>
			   			</ul>
			   			<div class="tab-content">
			   				<div class="active tab-pane" id="user_info_view">
			   					<div class="box box-solid">
			   						<div class="box-body">
			   							<div class="form-group">
											<label for="name" class="col-md-2-sm col-sm-2 control-label">姓名：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.name }
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">工号：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.employeeNo }
											</div>
											<label for="loginName" class="col-md-2-sm col-sm-2 control-label">登陆名：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.loginName }
											</div>
										</div>
										<div class="form-group">
											<label for="password" class="col-md-2-sm col-sm-2 control-label">密码：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.realPassword }
											</div>
											<label for="department.id" class="col-md-2-sm col-sm-2 control-label">所属部门：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.departmentName }
											</div>
											<label for="department.id" class="col-md-2-sm col-sm-2 control-label">归属公司：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.companyName }
											</div>
										</div>
										<div class="form-group">
											<label for="leader.id" class="col-md-2-sm col-sm-2 control-label">上级领导：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.leaderName }
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">排序号：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.orderNum }
											</div>
											<label for="mainGroup.id" class="col-md-2-sm col-sm-2 control-label">主用户组：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.groupText }
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2-sm col-sm-2 control-label">其他用户组：</label>
											<div class="col-md-11-sm col-sm-10 left-label">
												${userGroupTexts }
											</div>
										</div>
										<div class="form-group">
											<label for="password" class="col-md-2-sm col-sm-2 control-label">岗位：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.postName }
											</div>
											<label for="password" class="col-md-2-sm col-sm-2 control-label">入职时间：</label>
											<div class="col-md-2 col-sm-10 left-label">
												<fmt:formatDate pattern="yyyy-MM-dd" value="${user.hiredate }"/>
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">在职状态：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.inserviceStatus == 0 ? "试用" : "转正" }
											</div>
										</div>
										<div class="form-group">
											<label for="sex" class="col-md-2-sm col-sm-2 control-label">性别：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.sex == 0 ? "男" : "女" }
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">身份证号：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.idNumber }
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">生日：</label>
											<div class="col-md-2 col-sm-10 left-label">
												<fmt:formatDate pattern="yyyy-MM-dd" value="${user.birthday }"/>
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2-sm col-sm-2 control-label">婚姻状态：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.matrimony == 0 ? "未婚" : "已婚" }
											</div>
											<label for="email" class="col-md-2-sm col-sm-2 control-label">邮箱：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.email }
											</div>
											<label for="telephone" class="col-md-2-sm col-sm-2 control-label">联系电话：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.telephone }
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2-sm col-sm-2 control-label">学历：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.education }
											</div>
											<label for="email" class="col-md-2-sm col-sm-2 control-label">毕业学校：</label>
											<div class="col-md-7-sm col-sm-10 left-label">
												${user.graduateSchool }
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2-sm col-sm-2 control-label">专业：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.major }
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">户籍所在地：</label>
											<div class="col-md-7-sm col-sm-10 left-label">
												${user.domicilePlace }
											</div>
										</div>
										<div class="form-group">
											<label for="otherPhone" class="col-md-2-sm col-sm-2 control-label">其他联系电话：</label>
											<div class="col-md-2 col-sm-10 left-label">
												${user.otherPhone }
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">联系地址：</label>
											<div class="col-md-7-sm col-sm-10 left-label">
												${user.contactAddress }
											</div>
										</div>
										
										<div class="form-group">
											<label for="leaderGroup" class="col-md-2-sm col-sm-2 control-label">角色：</label>
											<div class="col-md-11-sm col-sm-10 left-label">
												<c:forEach items="${userRoles }" var="selectRole">
													<c:set var="roleNames" value="${selectRole.name },"></c:set>
												</c:forEach>
												<c:out value="${fn:substring(roleNames, 0, fn:length(roleNames)-1) }"></c:out>
											</div>
										</div>
										
										<c:if test="${user.isLeave == 0 }">
											<div class="form-group">
												<label class="col-md-2-sm col-sm-2 control-label">离职时间：</label>
												<div class="col-md-2 col-sm-10 left-label">
													<fmt:formatDate pattern="yyyy-MM-dd" value="${user.leaveDate }"/>
												</div>
												<label class="col-md-2-sm col-sm-2 control-label">离职原因：</label>
												<div class="col-md-7-sm col-sm-10 left-label">
													${user.leaveMemo }
												</div>
											</div>
										</c:if>
			   						</div>
			   					</div>
			   				</div>
			   				<div class="tab-pane" id="user_userSalary_view">
			   					<input type="hidden" name="userSalary.id" value="${userSalary.id }"/>
			   					<div class="box box-solid">
			   						<div class="box-body">
			   							<div class="form-group">
							    			<label for="name" class="col-sm-2 control-label">岗位：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.postName }
											</div>
											<div class="col-md-1 col-sm-12 "></div>
											<label for="name" class="col-sm-2 control-label">薪资类型：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.typeName }
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-sm-2 control-label">基本工资：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.basicSalary }
											</div>
											<div class="col-md-1 col-sm-12 "></div>
											<label for="name" class="col-sm-2 control-label">岗位工资：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.postSalary }
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-sm-2 control-label">绩效工资：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.achievementSalary }
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-sm-2 control-label">生效时间：</label>
											<div class="col-md-3 col-sm-10 left-label">
												<fmt:formatDate value="${userSalary.effectDate }" pattern="yyyy-MM-dd"/>
											</div>
											<div class="col-md-1 col-sm-12 "></div>
											<label for="sex" class="col-sm-2 control-label">是否强制生效：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.isForceEffect == 0 ? "否" : "是" }
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-sm-2 control-label">社保基数：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.socialInsurance }
											</div>
											<div class="col-md-1 col-sm-12 "></div>
											<label for="name" class="col-sm-2 control-label">社保开始时间：</label>
											<div class="col-md-3 col-sm-10 left-label">
												<fmt:formatDate value="${userSalary.socialInsuranceStartDate }" pattern="yyyy-MM-dd"/>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-sm-2 control-label">医保基数：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.healthInsurance }
											</div>
											<div class="col-md-1 col-sm-12 "></div>
											<label for="name" class="col-sm-2 control-label">医保开始时间：</label>
											<div class="col-md-3 col-sm-10 left-label">
												<fmt:formatDate value="${userSalary.healthInsuranceStartDate }" pattern="yyyy-MM-dd"/>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-sm-2 control-label">公积金：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.housingFund }
											</div>
											<div class="col-md-1 col-sm-12 "></div>
											<label for="name" class="col-sm-2 control-label">医保开始时间：</label>
											<div class="col-md-3 col-sm-10 left-label">
												<fmt:formatDate value="${userSalary.housingFundStartDate }" pattern="yyyy-MM-dd"/>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-sm-2 control-label">话费补贴：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.telCharge }
											</div>
											<div class="col-md-1 col-sm-12 "></div>
											<label for="name" class="col-sm-2 control-label">话费补贴开始时间：</label>
											<div class="col-md-3 col-sm-10 left-label">
												<fmt:formatDate value="${userSalary.telChargeStartDate }" pattern="yyyy-MM-dd"/>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-sm-2 control-label">交通补贴：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.travelAllowance }
											</div>
											<div class="col-md-1 col-sm-12 "></div>
											<label for="name" class="col-sm-2 control-label">交通补贴开始时间：</label>
											<div class="col-md-3 col-sm-10 left-label">
												<fmt:formatDate value="${userSalary.travelAllowanceStartDate }" pattern="yyyy-MM-dd"/>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-sm-2 control-label">工龄工资：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.serviceAgeSalary }
											</div>
											<div class="col-md-1 col-sm-12 "></div>
											<label for="name" class="col-sm-2 control-label">工龄工资开始时间：</label>
											<div class="col-md-3 col-sm-10 left-label">
												<fmt:formatDate value="${userSalary.serviceAgeSalaryStartDate }" pattern="yyyy-MM-dd"/>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-sm-2 control-label">工作餐补贴：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.mealSubsidy }
											</div>
											<div class="col-md-1 col-sm-12 "></div>
											<label for="name" class="col-sm-2 control-label">住房补贴：</label>
											<div class="col-md-3 col-sm-10 left-label">
												${userSalary.housingSubsidy }
											</div>
										</div>
										<div class="form-group">
											<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
											<div class="col-md-9 col-sm-10 left-label">
												${userSalary.remark }
											</div>
										</div>
			   						</div>
			   					</div>
			   				</div>
			   			</div>
			   		</div>
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
