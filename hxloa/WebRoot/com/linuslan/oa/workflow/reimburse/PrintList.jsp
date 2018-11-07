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
   		<c:forEach items="${reimburses }" var="reimburse">
   			<div style="margin-bottom: 60px; width: 99%;" class="reimburses_print_list">
   				<div class="reimburse box-body padding-bottom5 bottom-dotted-border">
	   				<table class="tab_css_2" border="1" style="width: 100%;">
	   					<tr class="tr_css">
							<td class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right; width: 20%;">申请人：</td>
							<td class="col-md-3 col-sm-8 col-xs-3" style="width: 30%;">
								${reimburse.userName }
							</td>
							<td class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right; width: 20%;">归属部门：</td>
							<td class="col-md-3 col-sm-8 col-xs-3" style="width: 30%;">
								${reimburse.userDeptName }
							</td>
						</tr>
				   		<tr>
							<td for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">出款公司：</td>
							<td class="col-md-3 col-sm-8 col-xs-3">
								${reimburse.companyName }
							</td>
							<td for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">编号：</td>
							<td class="col-md-3 col-sm-8 col-xs-3">
								${reimburse.serialNo }
							</td>
						</tr>
						<tr class="tr_css">
							<td for="departmentId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">报销部门：</td>
							<td class="col-md-3 col-sm-8 col-xs-3">
								${reimburse.reimburseDeptName }
							</td>
							<td for="departmentId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">收款人：</td>
							<td class="col-md-3 col-sm-8 col-xs-3">
								${reimburse.receiver }
							</td>
						</tr>
						<tr>
							<td for="moneyDate" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">开户行</td>
							<td class="col-md-3 col-sm-8 col-xs-3">
								${reimburse.bank }
							</td>
							<td for="groupId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">账号：</td>
							<td class="col-md-3 col-sm-8 col-xs-3">
								${reimburse.bankNo }
							</td>
						</tr>
						<tr class="tr_css">
							<td for="orderNo" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">总金额：</td>
							<td class="col-md-3 col-sm-8 col-xs-3">
								${reimburse.totalMoney }
							</td>
							<td for="text" class="col-md-2 col-sm-4 col-xs-2 control-label"></td>
							<td class="col-md-3 col-sm-8 col-xs-3">
							</td>
						</tr>
	   				</table>
			   	</div>
		   		<table class="content tab_css_2" width="100%">
	   				<thead>
	   					<tr>
	   						<th width="5%" >序号</th>
	   						<th width="15%" >费用时间</th>
	   						<th width="18%" >报销类别</th>
	   						<th width="27%" >报销项目</th>
	   						<th width="9%" >金额</th>
	   						<th width="29%" >备注</th>
	   					</tr>
	   				</thead>
	   				<tbody>
	   					<c:forEach items="${reimburse.contents }" var="content" varStatus="index">
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
			   	<div style='width: 50%; float:right; text-align: left; margin-top: 10px; height: 20px;'>
					<label style='font-size: 14px; width: 100px; height: 2px; text-align: right;'>总经理签名：</label><label style='border-bottom:1px solid black; width: 150px; height: 20px;'></label>
				</div>
   			</div>
   		</c:forEach>
   	</form>
  </body>
</html>
