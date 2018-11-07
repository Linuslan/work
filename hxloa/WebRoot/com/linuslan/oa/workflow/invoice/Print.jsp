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
    
    <title>开票详情</title>
    
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<link rel="stylesheet" href="<%=basePath %>resources/bootstrap/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%=basePath %>resources/Font-Awesome-master/css/font-awesome.min.css">
    <link rel="stylesheet" href="<%=basePath %>resources/ionicons-master/css/ionicons.min.css">
    <link rel="stylesheet" href="<%=basePath %>resources/bootstrap/dist/css/AdminLTE.css">
    <link rel="stylesheet" href="<%=basePath %>resources/bootstrap/dist/css/skins/_all-skins.min.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>resources/css/mybooststrap.css">
    <!-- jQuery 2.1.4 -->
    <script src="<%=basePath %>resources/bootstrap/plugins/jQuery/jQuery-2.1.4.min.js"></script>
    <!-- Bootstrap 3.3.5 -->
    <script src="<%=basePath %>resources/bootstrap/bootstrap/js/bootstrap.min.js"></script>
  </head>
  
  <body onload="window.print();">
   	<form id="printInvoiceForm" action="" class="form-horizontal print">
		<div class="box-body">
	   		<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">开票公司：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${invoice.companyName }
				</div>
				<div class="col-md-1 col-xs-1"></div>
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">编号：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${invoice.serialNo }
				</div>
			</div>
			<div class="form-group">
				<label for="departmentId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">收入归属部门：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${invoice.incomeDeptName }
				</div>
				<div class="col-md-1 col-xs-1"></div>
				<label for="departmentId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">开票类型：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${invoice.invoiceTypeName }
				</div>
			</div>
			<div class="form-group">
				<label for="moneyDate" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">开票时间：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					<fmt:formatDate value="${invoice.invoiceDate }" pattern="yyyy-MM-dd"/>
				</div>
				<div class="col-md-1 col-xs-1"></div>
				<label for="groupId" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">开票金额：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${invoice.invoiceMoney }
				</div>
			</div>
			<div class="form-group">
				<label for="orderNo" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">应回款金额：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${invoice.supposedMoney }
				</div>
				<div class="col-md-1 col-xs-1"></div>
				<label for="orderNo" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">实回款金额：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${invoice.actualMoney }
				</div>
			</div>
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">预计到款日：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					<fmt:formatDate value="${invoice.planRestreamDate }" pattern="yyyy-MM-dd"/>
				</div>
				<div class="col-md-1 col-xs-1"></div>
				<label for="orderNo" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">纳税人识别号：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${invoice.taxPayerId }
				</div>
			</div>
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">电话：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${invoice.phone }
				</div>
				<div class="col-md-1 col-xs-1"></div>
				<label for="orderNo" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">开户行：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${invoice.bank }
				</div>
			</div>
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">银行账号：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					${invoice.bankNo }
				</div>
				<div class="col-md-1 col-xs-1"></div>
				<label class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">状态：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					<c:choose>
						<c:when test="${invoice.invoiceStatus == 0 }">
							已回款
						</c:when>
						<c:when test="${invoice.invoiceStatus == 1 }">
							部分回款
						</c:when>
						<c:when test="${invoice.invoiceStatus == 2 }">
							未回款
						</c:when>
						<c:when test="${invoice.invoiceStatus == 5 }">
							未开票作废
						</c:when>
						<c:when test="${invoice.invoiceStatus == 6 }">
							已开票作废
						</c:when>
						<c:otherwise>
							未回款
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">地址：</label>
				<div class="col-md-10 col-sm-8 col-xs-3">
					${invoice.address }
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">财务开票时间：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					<fmt:formatDate value="${invoice.confirmInvoiceDate }" pattern="yyyy-MM-dd"/> （${invoice.invoiceUserName == null ? "无" : invoice.invoiceUserName }）
				</div>
				<div class="col-md-1 col-xs-1"></div>
				<label class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">回款时间：</label>
				<div class="col-md-3 col-sm-8 col-xs-3">
					<fmt:formatDate value="${invoice.confirmRestreamDate }" pattern="yyyy-MM-dd"/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">开票名称：</label>
				<div class="col-md-9 col-sm-8 col-xs-3">
					${invoice.title }
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">开票项目：</label>
				<div class="col-md-9 col-sm-8 col-xs-3">
					${invoice.content }
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 col-xs-2 control-label" style="text-align: right;">备注：</label>
				<div class="col-md-9 col-sm-8 col-xs-3">
					${invoice.remark }
				</div>
			</div>
	   	</div>
   	</form>
  </body>
</html>
