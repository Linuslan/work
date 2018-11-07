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
   	<form id="addDepartmentForm" action="" class="form-horizontal">
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">部门名称：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<input name="department.name" type="text" class="form-control" id="name" placeholder="请输入名称">
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="pid" class="col-sm-2 control-label">上级部门：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<div id="parentCombotree"></div>
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">领导用户组：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<div class="groupCombotree"></div>
					<!-- <input name="department.groupId" type="text" class="form-control" id="leaderGroup" placeholder="请选择"> -->
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="orderNo" class="col-sm-2 control-label">归属公司：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<select class="form-control" name="department.companyId">
						<option value="">请选择</option>
						<c:forEach items="${companys }" var="company">
							<option value="${company.id }">${company.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="orderNo" class="col-sm-2 control-label">排序值：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<input name="department.orderNo" type="text" class="form-control" id="orderNo" placeholder="请选择正整数">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<textarea name="department.memo" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addDepartmentForm").find("#parentCombotree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "department.pid"
			});
   			$("#addDepartmentForm").find(".groupCombotree").combotree({
				url: getRoot() + "sys/group/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "group.id"
				},
				idField: "id",
				textField: "text",
				name: "department.groupId"
			});
   		});
   	</script>
  </body>
</html>
