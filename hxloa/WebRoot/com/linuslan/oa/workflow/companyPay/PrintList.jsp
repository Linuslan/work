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
   	<form id="printCompanyPayForm" action="" class="form-horizontal print">
   		<c:forEach items="${companyPays }" var="companyPay" varStatus="index">
   			<div style="margin-bottom: 150px; width: 99%;" class="printDiv">
 				<table class="tab_css_2" border="1" style="width: 100%;">
 					<tr class="tr_css">
						<td style="text-align: right; width: 20%">归属公司：</td>
						<td style="width: 25%;">
							${companyPay.companyName }
						</td>
						
						<td style="text-align: right; width: 20%;">编号：</td>
						<td style="width: 25%">
							${companyPay.serialNo }
						</td>
					</tr>
					<tr>
						<td for="departmentId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">费用承担部门：</td>
						<td class="col-md-3 col-sm-8 col-xs-3">
							${companyPay.payDeptName }
						</td>
						<td for="departmentId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">费用承担公司：</td>
						<td class="col-md-3 col-sm-8 col-xs-3">
							${companyPay.payCompanyName }
						</td>
					</tr>
					<tr class="tr_css">
						<td for="moneyDate" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">费用产生时间：</td>
						<td class="col-md-3 col-sm-8 col-xs-3">
							<fmt:formatDate value="${companyPay.moneyDate }" pattern="yyyy-MM-dd"/>
						</td>
						<td for="groupId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">金额：</td>
						<td class="col-md-3 col-sm-8 col-xs-3">
							${companyPay.money }
						</td>
					</tr>
					<tr>
						<td for="orderNo" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">付款方式：</td>
						<td class="col-md-3 col-sm-8 col-xs-3">
							<%--${companyPay.payType == 0 ? "银行转账" : "现金" } --%>
							${companyPay.payTypeName }
						</td>
						<td for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">收款方：</dt>
						<td class="col-md-3 col-sm-8 col-xs-3">
							${companyPay.receiver }
						</td>
					</tr>
					<tr class="tr_css">
						<td for="orderNo" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">开户行：</td>
						<td class="col-md-3 col-sm-8 col-xs-3">
							${companyPay.bank }
						</td>
						<td for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">银行账号：</td>
						<td class="col-md-3 col-sm-8 col-xs-3">
							${companyPay.bankNo }
						</td>
					</tr>
					<tr>
						<td for="leaderGroup" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">付款项目：</td>
						<td class="col-md-9 col-sm-8 col-xs-3 textarea" colspan="3">
							${companyPay.content }
						</td>
					</tr>
					<tr class="tr_css">
						<td for="leaderGroup" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">备注：</td>
						<td class="col-md-9 col-sm-8 col-xs-3 textarea" colspan="3">
							${companyPay.remark }
						</td>
					</tr>
	   				
	   			</table>
   				<div style='width: 50%; float:right; text-align: left; margin-top: 10px; height: 20px;'>
					<label style='font-size: 14px; width: 100px; height: 2px; text-align: right;'>总经理签名：</label><label style='border-bottom:1px solid black; width: 150px; height: 20px;'></label>
				</div>
 			</div>
 			<c:if test="${(index.index + 1) % 2 == 0 }">
   				<p style="page-break-after:always">&nbsp;</p>
   			</c:if>
   		</c:forEach>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#printCompanyPayForm .textarea").each(function() {
   				var str = decode($(this).html());
   				$(this).html(str);
   			});
   		});
   	</script>
  </body>
</html>
