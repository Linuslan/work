<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/c" prefix="c" %>
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
   	<form id="selectAccount" action="" class="form-horizontal">
    	<div class="box-body">
			<div class="form-group">
				<select class="select2" name="account" style="width: 100%;">
					<option value="">请选择</option>
					<c:forEach var="account" items="${accounts }">
						<option value="${account.id }" receiver="${account.receiver }"
						 taxPayerId="${account.taxPayerId }" address="${account.address }"
						 cellphone="${account.cellphone }" bankNo="${account.bankNo }"
						 bankName="${account.bankName }" >${account.receiver }</option>
					</c:forEach>
				</select>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#selectAccount").find(".select2").select2();
   		});
   	</script>
  </body>
</html>
