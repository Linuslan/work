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
   	<form id="editPostForm" action="" class="form-horizontal">
   		<input type="hidden" value="${post.id }" name="post.id" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">岗位名称：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="post.name" type="text" class="form-control" value="${post.name }" placeholder="请输入名称">
				</div>
				<label for="pid" class="col-sm-2 control-label">所属部门：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<div class="parentCombotree" value="${post.departmentId }" text="${post.departmentName }"></div>
				</div>
			</div>
			<div class="form-group">
				
				<label for="pid" class="col-sm-2 control-label">排序值：</label>
				<div class="col-md-4 col-sm-10 no-padding">
					<input name="post.orderNo" value="${post.orderNo }" type="text" class="form-control" placeholder="请输入数字">
				</div>
				<label for="name" class="col-sm-2 control-label"></label>
				<div class="col-md-4 col-sm-10 no-padding">
					
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-10 col-sm-10 no-padding">
					<textarea name="post.remark" class="form-control" rows="3" placeholder="请输入">${post.remark }</textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			//初始化父部门树，只有在这里等弹出框的内容都加载完之后再加载树才能够获取到宽度
	    	//初始化上级组字段的选择项
	    	$("#editPostForm").find(".parentCombotree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "post.departmentId",
				pidField: "pid"
			});
   		});
   	</script>
  </body>
</html>
