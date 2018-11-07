<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/c" %>
<%@ taglib prefix="fmt" uri="/fmt"%>
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
   	<form id="addCustomerPaybackForm" action="" class="form-horizontal">
    	<div class="box-body">
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">归属公司：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<select class="select2" name="customerPayback.companyId" style="width: 100%" onchange="getCustomers_payback(this)">
						<option value="">请选择</option>
						<c:forEach items="${companys }" var="company">
							<option value="${company.id }">${company.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="name" class="col-sm-2 control-label">归属客户：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<select class="select2" name="customerPayback.customerId" style="width: 100%" onchange="getSaleInfo_payback(this)">
						<option value="">请选择</option>
						<c:forEach items="${customers }" var="customer">
							<option value="${customer.id }">${customer.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="showPaybackInfo form-group">
				<label class="col-md-2 col-sm-12 control-label">
					总销售额：
				</label>
				<div class="total-sale col-md-2 col-sm-12 left-label"></div>
				<label class="col-md-2 col-sm-12 control-label">
					总回款：
				</label>
				<div class="total-payback col-md-2 col-sm-12 left-label"></div>
				<label class="col-md-2 col-sm-12 control-label">
					未回款：
				</label>
				<div class="total-unpayback col-md-2 col-sm-12 left-label"></div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">回款时间：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input readonly="readonly" name="customerPayback.payDate" type="text" class="form-control pull-right date">
					</div>
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="name" class="col-sm-2 control-label">回款金额：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<input name="customerPayback.payMoney" type="number" class="form-control" value="0">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-9 col-sm-10 no-padding">
					<textarea name="customerPayback.memo" class="form-control" rows="3"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addCustomerPaybackForm").find(".select2").select2();
   			$("#addCustomerPaybackForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   		});
   		
   		function getCustomers_payback(obj) {
   			var $customers = $("#addCustomerPaybackForm").find(".select2[name='customerPayback.customerId']");
   			$customers.empty();
   			var option = document.createElement("option");
			$customers.append(option);
			option.value = "";
			option.text = "请选择";
   			var val = $(obj).val();
   			if(val) {
   				$.ajax({
   					url: getRoot() + "workflow/customer/queryByCompanyId.action",
   					data: {
   						"companyId": val
   					},
   					type: "POST",
   					success: function(data) {
   						if(data) {
   							var json = eval("("+data+")");
   							if(json && 0 < json.length) {
   								for(var i = 0; i < json.length; i ++) {
   									var opData = json[i];
   									if(opData.id) {
   										var option = document.createElement("option");
   										$customers.append(option);
   										option.value = opData.id;
   										option.text = opData.name;
   									}
   								}
   							}
   						}
   					}
   				});
   			}
   			$customers.select2("val", "");
   		}
   		
   		function getSaleInfo_payback(obj) {
   			try {
   				var customerId = $(obj).val();
   	   			var companyId = $("#addCustomerPaybackForm").find(".select2[name='customerPayback.companyId']").val();
   	   			if(!customerId || "" == $.trim(customerId)
   	   					|| !companyId || "" == $.trim(companyId)) {
   	   				BootstrapDialog.warning("请选择公司和客户");
   	   				return false;
   	   			}
   	   			$.ajax({
   	   				url: getRoot() + "workflow/customerPayback/countByCompanyIdAndCustomerId.action",
   	   				data: {
   	   					"companyId": companyId,
   	   					"customerId": customerId
   	   				},
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						if(json) {
   	   							var totalSale = json.TOTAL_SALE;
   	   							var totalPayback = json.TOTAL_PAYBACK;
   	   							var totalUnpayback = json.TOTAL_UNPAYBACK;
   								$("#addCustomerPaybackForm").find("div.showPaybackInfo").find("div.total-sale").html(totalSale.toFixed(2));
   								$("#addCustomerPaybackForm").find("div.showPaybackInfo").find("div.total-payback").html(totalPayback.toFixed(2));
   								$("#addCustomerPaybackForm").find("div.showPaybackInfo").find("div.total-unpayback").html(totalUnpayback.toFixed(2));
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
				$("#addCustomerPaybackForm").find("div.showPaybackInfo").find("div.total-sale").html(0);
				$("#addCustomerPaybackForm").find("div.showPaybackInfo").find("div.total-payback").html(0);
				$("#addCustomerPaybackForm").find("div.showPaybackInfo").find("div.total-unpayback").html(0);
   			}
   			
   		}
   	</script>
  </body>
</html>
