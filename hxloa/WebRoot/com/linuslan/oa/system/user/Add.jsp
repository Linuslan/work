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
   	<form id="addUserForm" action="" class="form-horizontal">
   		<div class="col-xs-12" style="height: 99%">
   			<div class="box box-solid" style="height: 99%">
		    	<div class="box-body">
		    		<div class="nav-tabs-top-border">
			   			<ul class="nav nav-tabs">
			   				<li class="active">
			   					<a href="#user_info_add" data-toggle="tab">个人信息</a>
			   				</li>
			   				<li>
			   					<a href="#user_userSalary_add" data-toggle="tab">薪资福利</a>
			   				</li>
			   			</ul>
			   			<div class="tab-content">
			   				<div class="active tab-pane" id="user_info_add">
			   					<div class="box box-solid">
			   						<div class="box-body">
			   							<div class="form-group">
											<label for="name" class="col-md-2-sm col-sm-2 control-label">姓名：</label>
											<div class="col-md-2 col-sm-10 ">
												<input name="user.name" required type="text" class="form-control" placeholder="">
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">工号：</label>
											<div class="col-md-2 col-sm-10 ">
												<input name="user.employeeNo" type="text" required class="form-control" placeholder="">
											</div>
											<label for="loginName" class="col-md-2-sm col-sm-2 control-label">登陆名：</label>
											<div class="col-md-2 col-sm-10 ">
												<input name="user.loginName" required type="text" class="form-control" placeholder="">
											</div>
										</div>
										<div class="form-group">
											<label for="password" class="col-md-2-sm col-sm-2 control-label">密码：</label>
											<div class="col-md-2 col-sm-10 ">
												<input name="user.password" type="password" required class="form-control" placeholder="" value="123456">（密码初始值为123456）
											</div>
											<label for="department.id" class="col-md-2-sm col-sm-2 control-label">所属部门：</label>
											<div class="col-md-2 col-sm-10 ">
												<div class="departmentTree"></div>
											</div>
											<label for="department.id" class="col-md-2-sm col-sm-2 control-label">归属公司：</label>
											<div class="col-md-2 col-sm-10 ">
												<select name="user.companyId" class="form-control select2">
													<option value="">请选择</option>
													<c:forEach items="${companys }" var="company">
														<option value="${company.id }">${company.name }</option>
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label for="leader.id" class="col-md-2-sm col-sm-2 control-label">上级领导：</label>
											<div class="col-md-2 col-sm-10 ">
												<select name="user.leaderId" class="form-control select2" style="width: 100%;">
													<option value="">请选择</option>
													<c:forEach items="${users }" var="user">
														<option value="${user.id }">${user.name }</option>
													</c:forEach>
												</select>
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">排序号：</label>
											<div class="col-md-2 col-sm-10 ">
												<input name="user.orderNum" type="number" class="form-control" placeholder="" value="0">
											</div>
											<label for="mainGroup.id" class="col-md-2-sm col-sm-2 control-label">主用户组：</label>
											<div class="col-md-2 col-sm-10 ">
												<div class="mainGroupTree"></div>
											</div>
										</div>
										<div class="form-group">
											<label for="leader.id" class="col-md-2-sm col-sm-2 control-label">其他用户组：</label>
											<div class="col-md-11-sm col-sm-10 ">
												<div class="otherGroupTree"></div>
											</div>
										</div>
										<div class="form-group">
											<label for="password" class="col-md-2-sm col-sm-2 control-label">岗位：</label>
											<div class="col-md-2 col-sm-10 ">
												<select id="addUserPosts" name="user.postId" class="form-control select2">
													<option value="">请选择</option>
													<c:forEach items="${posts }" var="post">
														<option value="${post.id }">${post.name }</option>
													</c:forEach>
												</select>
											</div>
											<label for="password" class="col-md-2-sm col-sm-2 control-label">入职时间：</label>
											<div class="col-md-2 col-sm-10 ">
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input name="user.hiredate" type="text" class="form-control pull-right date">
												</div>
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">在职状态：</label>
											<div class="col-md-2 col-sm-10 left-label">
												<input type="radio" name="user.inserviceStatus" class="minimal" value="0" checked>&nbsp;试用&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" name="user.inserviceStatus" class="minimal" value="1" >&nbsp;转正
											</div>
										</div>
										<div class="form-group">
											<label for="sex" class="col-md-2-sm col-sm-2 control-label">性别：</label>
											<div class="col-md-2 col-sm-10 left-label">
												<input type="radio" name="user.sex" class="minimal" value="0" checked>&nbsp;男&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" name="user.sex" class="minimal" value="1" >&nbsp;女
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">身份证号：</label>
											<div class="col-md-2 col-sm-10 ">
												<input name="user.idNumber" type="text" required class="form-control" placeholder="" value="">
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">生日：</label>
											<div class="col-md-2 col-sm-10 ">
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input name="user.birthday" type="text" class="form-control pull-right date">
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2-sm col-sm-2 control-label">婚姻状态：</label>
											<div class="col-md-2 col-sm-10 left-label">
												<input type="radio" name="user.matrimony" class="minimal" value="0" checked>&nbsp;未婚&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" name="user.matrimony" class="minimal" value="1" >&nbsp;已婚
											</div>
											<label for="email" class="col-md-2-sm col-sm-2 control-label">邮箱：</label>
											<div class="col-md-2 col-sm-10 ">
												<input name="user.email" type="email" class="form-control" placeholder="">
											</div>
											<label for="telephone" class="col-md-2-sm col-sm-2 control-label">联系电话：</label>
											<div class="col-md-2 col-sm-10 ">
												<input name="user.telephone" type="text" class="form-control" placeholder="">
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2-sm col-sm-2 control-label">学历：</label>
											<div class="col-md-2 col-sm-10 ">
												<input name="user.education" type="text" class="form-control" placeholder="" value="">
											</div>
											<label for="email" class="col-md-2-sm col-sm-2 control-label">毕业学校：</label>
											<div class="col-md-7-sm col-sm-10 ">
												<input name="user.graduateSchool" type="text" class="form-control" placeholder="">
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2-sm col-sm-2 control-label">专业：</label>
											<div class="col-md-2 col-sm-10 ">
												<input name="user.major" type="text" class="form-control" placeholder="" value="">
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">户籍所在地：</label>
											<div class="col-md-7-sm col-sm-10 ">
												<input name="user.domicilePlace" type="text" class="form-control" placeholder="">
											</div>
										</div>
										<div class="form-group">
											<label for="otherPhone" class="col-md-2-sm col-sm-2 control-label">其他联系电话：</label>
											<div class="col-md-2 col-sm-10 ">
												<input name="user.otherPhone" type="text" class="form-control" placeholder="">
											</div>
											<label class="col-md-2-sm col-sm-2 control-label">联系地址：</label>
											<div class="col-md-7-sm col-sm-10 ">
												<input name="user.contactAddress" type="text" class="form-control" placeholder="">
											</div>
										</div>
										
										<div class="form-group">
											<label for="leaderGroup" class="col-md-2-sm col-sm-2 control-label">角色：</label>
											<div class="col-md-11-sm col-sm-10 ">
												<select name="roleIds" multiple="multiple" class="form-control select2" data-placeholder="请选择角色" style="width: 100%">
													<option value="">请选择</option>
													<c:forEach items="${roles }" var="role">
														<option value="${role.id }">${role.name }</option>
													</c:forEach>
												</select>
											</div>
										</div>
			   						</div>
			   					</div>
			   					
			   				</div>
			   				<div class="tab-pane" id="user_userSalary_add">
			   					<div class="box box-solid">
			   						<div class="box-body">
			   							<div class="form-group">
							    			<label for="name" class="col-md-2 control-label">岗位：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<select class="form-control" name="userSalary.postId">
													<option value="">请选择</option>
													<c:forEach items="${posts }" var="post">
														<option value="${post.id }">${post.name }</option>
													</c:forEach>
												</select>
											</div>
											<div class="col-md-1 col-sm-12"></div>
											<label for="name" class="col-md-2 control-label">薪资类型：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<select class="form-control" name="userSalary.typeId">
													<option value="">请选择</option>
													<c:forEach items="${types }" var="type">
														<option value="${type.id }">${type.text }</option>
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-md-2 control-label">基本工资：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<input name="userSalary.basicSalary" type="number" value="0" class="form-control">
											</div>
											<div class="col-md-1 col-sm-12"></div>
											<label for="name" class="col-md-2 control-label">岗位工资：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<input name="userSalary.postSalary" type="number" value="0" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-md-2 control-label">绩效工资：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<input name="userSalary.achievementSalary" type="number" value="0" class="form-control">
											</div>
											<div class="col-md-1 col-sm-12"></div>
											<label for="name" class="col-md-2 control-label">试用期工资：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<input name="userSalary.probationSalary" type="number" value="0" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-md-2 control-label">试用期开始时间：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input name="userSalary.probationStartTime" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
												</div>
											</div>
											<div class="col-md-1 col-sm-12"></div>
											<label for="name" class="col-md-2 control-label">试用期结束时间：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input name="userSalary.probationEndTime" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
												</div>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-md-2 control-label">生效时间：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input name="userSalary.effectDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
												</div>
											</div>
											<div class="col-md-1 col-sm-12"></div>
											<label for="sex" class="col-md-2 control-label">是否强制生效：</label>
											<div class="col-md-3 col-sm-10 left-label">
												<input type="radio" name="userSalary.isForceEffect" class="minimal" value="0" checked>否&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" name="userSalary.isForceEffect" class="minimal" value="1" >&nbsp;是
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-md-2 control-label">社保基数：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<input name="userSalary.socialInsurance" type="text" value="0" class="form-control">
											</div>
											<div class="col-md-1 col-sm-12"></div>
											<label for="name" class="col-md-2 control-label">社保开始时间：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input name="userSalary.socialInsuranceStartDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
												</div>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-md-2 control-label">医保基数：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<input name="userSalary.healthInsurance" type="text" value="0" class="form-control">
											</div>
											<div class="col-md-1 col-sm-12"></div>
											<label for="name" class="col-md-2 control-label">医保开始时间：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input name="userSalary.healthInsuranceStartDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
												</div>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-md-2 control-label">公积金：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<input name="userSalary.housingFund" type="text" value="0" class="form-control">
											</div>
											<div class="col-md-1 col-sm-12"></div>
											<label for="name" class="col-md-2 control-label">医保开始时间：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input name="userSalary.housingFundStartDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
												</div>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-md-2 control-label">话费补贴：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<input name="userSalary.telCharge" type="text" value="0" class="form-control">
											</div>
											<div class="col-md-1 col-sm-12"></div>
											<label for="name" class="col-md-2 control-label">话费补贴开始时间：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input name="userSalary.telChargeStartDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
												</div>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-md-2 control-label">交通补贴：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<input name="userSalary.travelAllowance" type="text" value="0" class="form-control">
											</div>
											<div class="col-md-1 col-sm-12"></div>
											<label for="name" class="col-md-2 control-label">交通补贴开始时间：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input name="userSalary.travelAllowanceStartDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
												</div>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-md-2 control-label">工龄工资：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<!-- <input name="userSalary.serviceAgeSalary" type="text" value="0" class="form-control"> -->
											</div>
											<div class="col-md-1 col-sm-12"></div>
											<label for="name" class="col-md-2 control-label">工龄工资开始时间：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input name="userSalary.serviceAgeSalaryStartDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
												</div>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-md-2 control-label">住房补贴：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<input name="userSalary.housingSubsidy" type="text" value="0" class="form-control">
											</div>
											<div class="col-md-1 col-sm-12"></div>
											<label for="name" class="col-md-2 control-label">住房补贴开始时间：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input name="userSalary.housingSubsidyStartTime" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
												</div>
											</div>
										</div>
										<div class="form-group">
											<label for="name" class="col-md-2 control-label">工作餐补贴：</label>
											<div class="col-md-3 col-sm-10 no-padding">
												<input name="userSalary.mealSubsidy" type="text" value="0" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<label for="leaderGroup" class="col-md-2 control-label">备注：</label>
											<div class="col-md-9 col-sm-10 no-padding">
												<textarea name="userSalary.remark" class="form-control" rows="3" placeholder="请输入"></textarea>
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
   		//初始化上级组字段的选择项
	    	$("#addUserForm").find(".departmentTree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "user.departmentId",
				pidField: "pid",
				onRowClick: function(rowObject) {
					$("#addUserPosts").html("");
					var option = document.createElement("option");
					option.value = "";
					option.text = "请选择";
					$("#addUserPosts").append(option);
					if(rowObject.id) {
						$.ajax({
							url: getRoot() + "sys/post/queryByDepartmentId.action?departmentId="+rowObject.id,
							type: "POST",
							success: function(data) {
								var json = eval("("+data+")");
								if(json.success && json.posts) {
									var posts = eval("("+json.posts+")");
									if(posts && 0 < posts.length) {
										for(var i = 0 ; i < posts.length; i ++) {
											var post = posts[i];
											var option = document.createElement("option");
											option.text = post.name;
											option.value = post.id;
											$("#addUserPosts").append(option);
										}
									}
								} else {
									BootstrapDialog.danger(json.msg);
								}
							},
							error: function() {
								BootstrapDialog.danger("获取岗位信息时系统异常，请联系管理员");
							}
						});
					}
				}
			});
				
	    	$("#addUserForm").find(".mainGroupTree").combotree({
				url: getRoot() + "sys/group/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "group.id"
				},
				idField: "id",
				textField: "text",
				name: "user.groupId",
				pidField: "pid"
			});
				
	    	$("#addUserForm").find(".otherGroupTree").combotree({
				url: getRoot() + "sys/group/queryTree.action",
				async: true,
				singleSelect: false,
				loadOnExpand: false,
				loadParams: {
					"id": "group.id"
				},
				idField: "id",
				textField: "text",
				name: "userGroupIds",
				pidField: "pid"
			});
	    	$("#addUserForm").find(".select2[name!='roleIds']").select2();
	    	$("#addUserForm").find(".select2[name='roleIds']").select2().val(["2"]).trigger("change");
	    	$("#addUserForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
	    	$("#addUserForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
   		});
   	</script>
  </body>
</html>
