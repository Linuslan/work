<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/c" %>
<%@ taglib prefix="fmt" uri="/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP "Add.jsp" starting page</title>
    
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
   	<form id="viewReimburseForm" action="" class="form-horizontal print">
   		<input class="reimburseId" type="hidden" name="reimburse.id" id="viewReimburseId" value="${reimburse.id }" />
   		
		<div class="box-body padding-bottom5 bottom-dotted-border">
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">申请人：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${reimburse.userName }
				</div>
				<div class="col-md-1 col-sm-12 col-xs-1 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">归属部门：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${reimburse.userDeptName }
				</div>
			</div>
	   		<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">出款公司：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${reimburse.companyName }
				</div>
				<div class="col-md-1 col-sm-12 col-xs-1 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">编号：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${reimburse.serialNo }
				</div>
			</div>
			<div class="form-group">
				<label for="departmentId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">报销部门：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${reimburse.reimburseDeptName }
				</div>
				<div class="col-md-1 col-sm-12 col-xs-1 no-padding"></div>
				<label for="departmentId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">收款人：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${reimburse.receiver }
				</div>
			</div>
			<div class="form-group">
				<label for="moneyDate" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">开户行</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${reimburse.bank }
				</div>
				<div class="col-md-1 col-sm-12 col-xs-1 no-padding"></div>
				<label for="groupId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">账号：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${reimburse.bankNo }
				</div>
			</div>
			<div class="form-group">
				<label for="orderNo" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">总金额：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${reimburse.totalMoney }
				</div>
				<div class="col-md-1 col-sm-12 col-xs-1 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label"></label>
				<div class="col-md-3 col-sm-8 col-xs-3">
				</div>
			</div>
	   	</div>
	   	<div class="box-body">
	   		<table id="addContent" class="tab_css_2 tab_css_2_list" width="100%" style="table-layout:fixed;word-wrap:break-word;">
   				<thead>
   					<tr>
   						<th width="5%">序号</th>
   						<th width="15%">费用时间</th>
   						<th width="18%">报销类别</th>
   						<th width="27%">报销项目</th>
   						<th width="9%">金额</th>
   						<th width="29%">备注</th>
   					</tr>
   				</thead>
   				<tbody>
   					<c:forEach items="${contents }" var="content" varStatus="index">
   						<tr>
   							<td>${index.index + 1 }</td>
   							<td><fmt:formatDate value="${content.remittanceDate }" pattern="yyyy-MM-dd" /></td>
   							<td>
   								${content.reimburseClassName }
   							</td>
   							<td>
   								${content.content }
   							</td>
   							<td>
   								${content.money }
   							</td>
   							<td>
   								${content.remark }
   							</td>
   						</tr>
   					</c:forEach>
   				</tbody>
   			</table>
	   	</div>
   	</form>
  </body>
</html>
