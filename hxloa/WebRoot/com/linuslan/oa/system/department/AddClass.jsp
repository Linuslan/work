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
   	<form id="addDepartmentReimburseClassForm" action="" class="form-horizontal">
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">报销类别：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<select class="select2" name="reimburseClass.id" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${classes }" var="cls">
							<option value="${cls.id }">${cls.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="pid" class="col-sm-2 control-label">归属部门：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<div id="parentCombotree" value="${department.id }" text="${department.name }"></div>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addDepartmentReimburseClassForm").find(".select2").select2();
   			$("#addDepartmentReimburseClassForm").find("#parentCombotree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "department.id"
			});
   			
   		});
   	</script>
  </body>
</html>
