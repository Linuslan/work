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
    
    <title>My JSP 'assignCompany.jsp' starting page</title>
    
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
    <form id="assignCompanyFlowForm" action="" class="form-horizontal">
    	<input name="flowId" type="hidden" value="${flow.id }" />
    	<div class="box-body">
    		<div class="form-group">
				<label for="text" class="col-sm-3 control-label">归属公司：</label>
				<div class="col-md-8 col-sm-8 no-padding">
					<select multiple="multiple" class="form-control select2" name="companyIds" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${companys }" var="company">
							<c:set var="selected" value="false"></c:set>
							<c:forEach items="${flow.companys }" var="selectCompany">
								<c:choose>
									<c:when test="${company.id == selectCompany.id }">
										<c:set var="selected" value="true"></c:set>
									</c:when>
								</c:choose>
							</c:forEach>
							<option value="${company.id }" ${selected == true ? "selected='selected'" : "" }>${company.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>
    	</div>
   	</form>
  </body>
  <script type="text/javascript">
  	$(function() {
  		$("#assignCompanyFlowForm").find(".select2").select2();
  	});
  </script>
</html>
