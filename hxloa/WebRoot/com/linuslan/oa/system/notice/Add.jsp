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
   	<form id="addNoticeForm" action="" class="form-horizontal">
   		<input name="operate" type="hidden" value="update">
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-2 control-label">标题：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<input name="notice.title" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-2 control-label">接收人：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<select name="userIds" multiple="multiple" class="form-control select2" style="width: 80%;">
						<option value="">请选择</option>
						<c:forEach items="${users }" var="user">
							<option value="${user.id }">${user.name }</option>
						</c:forEach>
					</select>
					<input type="checkbox" class="minimal" name="selectedAll" value="1"> 全选
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">接收部门：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<div class="departmentTree"></div>
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">接收用户组：</label>
				<div class="col-md-9 col-sm-10 no-padding">
					<div class="groupTree"></div>
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-2 control-label">内容：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<textarea id="addNoticeContent_add" name="notice.content" class="form-control" rows="3"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addNoticeForm").find(".select2").select2();
   			$("#addNoticeForm").find(".departmentTree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: false,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "departmentIds",
				pidField: "pid"
			});
			
			$("#addNoticeForm").find(".groupTree").combotree({
				url: getRoot() + "sys/group/queryTree.action",
				async: true,
				singleSelect: false,
				loadOnExpand: false,
				loadParams: {
					"id": "group.id"
				},
				idField: "id",
				textField: "text",
				name: "groupIds",
				pidField: "pid"
			});
			
			CKEDITOR.replace("addNoticeContent_add", {
   				height: "600px"
   			});
			
			$("#addNoticeForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
   		});
   	</script>
  </body>
</html>
