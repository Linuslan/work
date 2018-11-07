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
   	<form id="editCustomerPaybackForm" action="" class="form-horizontal">
   		<input name="customerPayback.id" type="hidden" value="${customerPayback.id }">
    	<div class="box-body">
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">归属公司：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<select class="select2" name="customerPayback.companyId" style="width: 100%" onchange="getCustomers_payback_edit(this)">
						<option value="">请选择</option>
						<c:forEach items="${companys }" var="company">
							<option value="${company.id }" ${company.id == customerPayback.companyId ? "selected='selected'" : "" }>${company.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="name" class="col-sm-2 control-label">归属客户：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<select class="select2" name="customerPayback.customerId" style="width: 100%" onchange="getSaleInfo_payback_edit(this)">
						<option value="">请选择</option>
						<c:forEach items="${customers }" var="customer">
							<option value="${customer.id }" ${customer.id == customerPayback.customerId ? "selected='selected'" : "" }>${customer.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="showPaybackInfo form-group">
				<label class="col-md-2 col-sm-12 control-label">
					总销售额：
				</label>
				<div class="total-sale col-md-2 col-sm-12 left-label">${TOTAL_SALE }</div>
				<label class="col-md-2 col-sm-12 control-label">
					总回款：
				</label>
				<div class="total-payback col-md-2 col-sm-12 left-label">${TOTAL_PAYBACK }</div>
				<label class="col-md-2 col-sm-12 control-label">
					未回款：
				</label>
				<div class="total-unpayback col-md-2 col-sm-12 left-label">${TOTAL_UNPAYBACK }</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">回款时间：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input value="<fmt:formatDate value="${customerPayback.payDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" name="customerPayback.payDate" type="text" class="form-control pull-right date">
					</div>
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="name" class="col-sm-2 control-label">回款金额：</label>
				<div class="col-md-3 col-sm-10 no-padding">
					<input name="customerPayback.payMoney" type="number" class="form-control" value="${customerPayback.payMoney }">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-sm-2 control-label">备注：</label>
				<div class="col-md-9 col-sm-10 no-padding">
					<textarea name="customerPayback.memo" class="form-control" rows="3">${customerPayback.memo }</textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
			$("#editCustomerPaybackForm").find(".select2").select2();
			$("#editCustomerPaybackForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
		});
		
		function getCustomers_payback_edit(obj) {
			var $customers = $("#editCustomerPaybackForm").find(".select2[name='customerPayback.customerId']");
			$customers.select2("val", "");
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
		}
		
		function getSaleInfo_payback_edit(obj) {
			try {
				var customerId = $(obj).val();
	   			var companyId = $("#editCustomerPaybackForm").find(".select2[name='customerPayback.companyId']").val();
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
   								$("#editCustomerPaybackForm").find("div.showPaybackInfo").find("div.total-sale").html(totalSale.toFixed(2));
   								$("#editCustomerPaybackForm").find("div.showPaybackInfo").find("div.total-payback").html(totalPayback.toFixed(2));
   								$("#editCustomerPaybackForm").find("div.showPaybackInfo").find("div.total-unpayback").html(totalUnpayback.toFixed(2));
	   						}
	   					}
	   				}
	   			});
			} catch(ex) {
				$("#editCustomerPaybackForm").find("div.showPaybackInfo").find("div.total-sale").html(0);
				$("#editCustomerPaybackForm").find("div.showPaybackInfo").find("div.total-payback").html(0);
				$("#editCustomerPaybackForm").find("div.showPaybackInfo").find("div.total-unpayback").html(0);
			}
			
		}
   	</script>
  </body>
</html>
