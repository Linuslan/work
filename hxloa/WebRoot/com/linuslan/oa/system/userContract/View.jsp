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
   	<form id="viewUserContractForm" action="" class="form-horizontal">
   		<input type="hidden" value="${userContract.id }" name="userContract.id" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">签约人：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${userContract.userName }
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">合同编号：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${userContract.num }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">合同名称：</label>
				<div class="col-md-9 col-sm-8 left-label">
					${userContract.title }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">开始时间：</label>
				<div class="col-md-3 col-sm-8 left-label">
					<fmt:formatDate value="${userContract.startDate }" pattern="yyyy-MM-dd"/>
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">结束时间：</label>
				<div class="col-md-3 col-sm-8 left-label">
					<fmt:formatDate value="${userContract.endDate }" pattern="yyyy-MM-dd"/>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">到期提醒：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${userContract.isRemind == 0 ? "是" : "否" }
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">是否生效：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${userContract.isEffective == 0 ? "是" : "否" }
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">合同内容：</label>
				<div class="col-md-9 col-sm-8 left-label">
					${userContract.content }
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
