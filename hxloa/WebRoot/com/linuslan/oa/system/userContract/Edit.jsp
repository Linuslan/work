<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/fmt" prefix="fmt" %>
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
   	<form id="editUserContractForm" action="" class="form-horizontal">
   		<input type="hidden" value="${userContract.id }" name="userContract.id" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">签约人：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${userContract.userName }
					<input name="userContract.userId" type="hidden" class="form-control" value="${userContract.userId }">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">合同编号：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="userContract.num" type="text" class="form-control" value="${userContract.num }">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">合同名称：</label>
				<div class="col-md-9 col-sm-8 no-padding">
					<input name="userContract.title" type="text" class="form-control" value="${userContract.title }">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">开始时间：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userContract.startDate" type="text" value="<fmt:formatDate value="${userContract.startDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">结束时间：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="userContract.endDate" type="text" value="<fmt:formatDate value="${userContract.endDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">到期提醒：</label>
				<div class="col-md-3 col-sm-8 left-label">
					<input name="userContract.isRemind" type="radio" class="minimal" value="0" ${userContract.isRemind == 0 ? "checked" : "" }>&nbsp;是
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="userContract.isRemind" type="radio" class="minimal" value="1" ${userContract.isRemind == 1 ? "checked" : "" }>&nbsp;否
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">是否生效：</label>
				<div class="col-md-3 col-sm-8 left-label">
					<input name="userContract.isEffective" type="radio" class="minimal" value="0" ${userContract.isEffective == 0 ? "checked" : "" }>&nbsp;是
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="userContract.isEffective" type="radio" class="minimal" value="1" ${userContract.isEffective == 1 ? "checked" : "" }>&nbsp;否
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">合同内容：</label>
				<div class="col-md-9 col-sm-8 no-padding">
					<textarea name="userContract.content" class="form-control" rows="3">${userContract.content }</textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#editUserContractForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#editUserContractForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
   		});
   	</script>
  </body>
</html>
