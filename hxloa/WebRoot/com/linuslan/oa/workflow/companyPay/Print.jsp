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
   		<div class="box-body">
	   		<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">归属公司：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${companyPay.companyName }
				</div>
				<div class="col-md-1 col-sm-12 col-xs-1 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">编号：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${companyPay.serialNo }
				</div>
			</div>
			<div class="form-group">
				<label for="departmentId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">费用承担部门：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${companyPay.payDeptName }
				</div>
				<div class="col-md-1 col-sm-12 col-xs-1 no-padding"></div>
				<label for="departmentId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">费用承担公司：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${companyPay.payCompanyName }
				</div>
			</div>
			<div class="form-group">
				<label for="moneyDate" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">费用产生时间：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					<fmt:formatDate value="${companyPay.moneyDate }" pattern="yyyy-MM-dd"/>
				</div>
				<div class="col-md-1 col-sm-12 col-xs-1 no-padding"></div>
				<label for="groupId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">金额：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${companyPay.money }
				</div>
			</div>
			<div class="form-group">
				<label for="orderNo" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">付款方式：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					<%--${companyPay.payType == 0 ? "银行转账" : "现金" } --%>
					${companyPay.payTypeName }
				</div>
				<div class="col-md-1 col-sm-12 col-xs-1 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">收款方：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${companyPay.receiver }
				</div>
			</div>
			<div class="form-group">
				<label for="orderNo" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">开户行：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${companyPay.bank }
				</div>
				<div class="col-md-1 col-sm-12 col-xs-1 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">银行账号：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${companyPay.bankNo }
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">付款项目：</label>
				<div class="col-md-9 col-sm-8 col-xs-3 textarea">
					${companyPay.content }
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">备注：</label>
				<div class="col-md-9 col-sm-8 col-xs-3 textarea">
					${companyPay.remark }
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12 col-sm-12 col-xs-12 textarea text-align: right">
					<label style='font-size: 14px; width: 100px; height: 2px; text-align: right;'>总经理签名：</label><label style='border-bottom:1px solid black; width: 150px; height: 20px;'></label>
				</div>
			</div>
	   	</div>
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
