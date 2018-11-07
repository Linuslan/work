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
   	<form id="editNoticeForm" action="" class="form-horizontal">
   		<input type="hidden" value="${notice.id }" name="notice.id" />
   		<input name="operate" type="hidden" value="update">
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-2 control-label">标题：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<input name="notice.title" type="text" class="form-control" value="${notice.title }">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-2 control-label">接收人：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<select name="userIds" multiple="multiple" class="form-control select2" style="width: 80%;">
						<option value="">请选择</option>
						<c:forEach items="${users }" var="user">
							<c:set var="selected" value="false"></c:set>
							<c:forEach items="${selectedUsers }" var="selectedUser">
								<c:choose>
									<c:when test="${user.id == selectedUser.id }">
										<c:set var="selected" value="true"></c:set>
									</c:when>
								</c:choose>
							</c:forEach>
							<option value="${user.id }" ${selected == true ? "selected='selected'" : "" }>${user.name }</option>
						</c:forEach>
					</select>
					<input type="checkbox" class="minimal" name="selectedAll" value="1" ${selectedAll == 1 ? "checked" : "" }> 全选
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">接收部门：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<div class="departmentTree" value="${departmentIdList }" text="${departmentNameList }"></div>
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">接收用户组：</label>
				<div class="col-md-9 col-sm-10 no-padding">
					<div class="groupTree" value="${groupIdList }" text="${groupNameList }"></div>
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-2 control-label">内容：</label>
				<div class="col-md-9 col-sm-9 no-padding">
					<textarea id="addNoticeContent_edit" name="notice.content" class="form-control" rows="3">${notice.content }</textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#editNoticeForm").find(".select2").select2();
   			$("#editNoticeForm").find(".departmentTree").combotree({
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
			
			$("#editNoticeForm").find(".groupTree").combotree({
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
			
			CKEDITOR.replace("addNoticeContent_edit", {
   				height: "600px"
   			});
			
			$("#editNoticeForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
   		});
   	</script>
  </body>
</html>
