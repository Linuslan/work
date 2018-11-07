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
    
    <title>结算单</title>
    
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
   	<form id="statementViewSpecialSaleForm" action="" class="form-horizontal">
   		<input id="statementViewSpecialSaleId" class="specialSaleId" name="specialSale.id" type="hidden" value="${specialSale.id }" />
    	<div class="box-body padding-bottom5 bottom-dotted-border" >
    		<div class="form-group" style="text-align: center; font-size: 20px;">
    			${specialSale.companyName }
    		</div>
    		<div class="form-group" style="text-align: center; font-size: 16px; margin-top: 20px;">
    			结算单
    		</div>
    		<table style="width: 100%;" class="tab_css_2">
    			<tr>
    				<td class="td-center" style="width: 5%">客户：</td>
    				<td class="td-center" style="width: 5%">${specialSale.customer }</td>
    				<td class="td-center" style="width: 5%">手机：</td>
					<td class="td-center" style="width: 5%">
						${specialSale.customerPhone }
					</td>
					<td class="td-center" style="width: 5%">订单号：</td>
					<td class="td-center" style="width: 5%">
						${specialSale.serialNo }
					</td>
					<td class="td-center" style="width: 5%">订单日期：</td>
					<td class="td-center" style="width: 5%">
						<fmt:formatDate value="${specialSale.saleDate }" pattern="yyyy-MM-dd"/>
					</td>
					<td class="td-center" style="width: 5%">出货日期：</td>
					<td class="td-center" style="width: 5%">
						<fmt:formatDate value="${specialSale.checkoutDate }" pattern="yyyy-MM-dd"/>
					</td>
					<td class="td-center" style="width: 5%">收货人：</td>
					<td class="td-center" style="width: 5%">
						${specialSale.receiver }
					</td>
					<td class="td-center" style="width: 5%">手机：</td>
					<td class="td-center" style="width: 5%">
						${specialSale.receiverPhone }
					</td>
    			</tr>
    			<tr>
    				<td class="td-center">定金：</td>
					<td class="td-center">
						${specialSale.frontMoney }
					</td>
					<td class="td-center">总金额：</td>
					<td class="td-center">
						${specialSale.totalAmount }
					</td>
					<td class="td-center">成品金额：</td>
					<td class="td-center">
						${specialSale.productAmount }
					</td>
					<td class="td-center">运费：</td>
					<td class="td-center">
						${specialSale.freight }
					</td>
					<td class="td-center">是否开票：</td>
					<td class="td-center">
						${specialSale.isInvoice == 0 ? "否" : "是" }
					</td>
					<td>
					</td>
					<td>
					</td>
					<td>
					</td>
					<td>
					</td>
    			</tr>
    			<tr>
    				<td class="td-center">序号</td>
    				<td class="td-center">品名</td>
    				<td class="td-center">基材</td>
    				<td class="td-center">效果</td>
    				<td class="td-center">基材规格</td>
    				<td class="td-center">产品规格宽(mm)</td>
    				<td class="td-center">产品规格高(mm)</td>
    				<td class="td-center">单位</td>
    				<td class="td-center">数量</td>
    				<td class="td-center">单价(元/m2，公斤.桶，米)</td>
    				<td class="td-center">耗损基材费用</td>
    				<td class="td-center">产品金额</td>
    				<td class="td-center">配件金额</td>
    				<td class="td-center">备注</td>
    			</tr>
    		<c:forEach items="${contents }" var="content">
    			<tr>
    				<td class="td-center">${content.orderNo }</td>
    				<td class="td-center">${content.saleArticleName }</td>
    				<td class="td-center">${content.materialName }</td>
    				<td class="td-center">${content.effectName }</td>
    				<td class="td-center">${content.materialFormatName }</td>
    				<td class="td-center">${content.width }</td>
    				<td class="td-center">${content.height }</td>
    				<td class="td-center">${content.articleUnitName }</td>
    				<td class="td-center">${content.quantity }</td>
    				<td class="td-center">${content.price }</td>
    				<td class="td-center">${content.lossAreaAmount }</td>
    				<td class="td-center">${content.materialAmount }</td>
    				<td class="td-center">${content.photoFrameAmount }</td>
    				<td class="td-center">${content.remark }</td>
    			</tr>
    		</c:forEach>
    		</table>
			<div class="form-group" style="margin-top: 20px;">
				<table style="width: 100%;">
					<tr>
						<td class="align-right" style="font-weight: bold; width:10%">付款</td>
						<td class="align-right" style="width: 10%">户名：</td>
						<td class="align-left" style="width: 15%">
							${specialSale.receiver }
						</td>
						<td class="align-right" style="width: 10%">账号：</td>
						<td style="width: 15%" class="bankNo">
							<span>${specialSale.bankNo }</span>
						</td>
						<td class="align-right" style="width: 10%">开户行：</td>
						<td style="width: 15%" class="bank">
							<span>${specialSale.bank }</span>
						</div>
					<tr>
				</table>
			</div>
			<div class="form-group" style="margin-top: 20px;">
				<table style="width: 100%;">
					<tr>
						<td style="width: 30%"></td>
						<td class="align-right" style="width: 10%">业务员：</td>
						<td class="" style="width: 10%"></td>
						<td class="align-right" style="width: 10%">日期：</td>
						<td class="" style="width: 10%"></td>
						<td style="width: 30%"></td>
					</tr>
				</table>
			</div>
    	</div>
    	<!-- <div class="box-body" style="overflow-x: auto;">
    		<table id="specialSaleContentDatagrid_statementView"></table>
    	</div> -->
   	</form>
   	<script type="text/javascript">
   		$(function() {
            //需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			//setTimeout("buildSpecialSaleContentGrid_statementView()", 0);
   		});
   		
   		function setAccount_specialSale_statement(obj) {
   			var $formGroup = $(obj).parent().parent();
   			$option = $(obj).find("option:selected");
   			var val = $option.val();
   			var text = $option.text();
   			var bankNo = $option.attr("bankNo");
   			var bank = $option.attr("bank");
   			$formGroup.find("input[name='specialSale.bankNo']").val(bankNo);
   			$formGroup.find("input[name='specialSale.bank']").val(bank);
   			$formGroup.find("td.bankNo").find("span").html(bankNo);
   			$formGroup.find("td.bank").find("span").html(bank);
   		}
   		
   		/*function buildSpecialSaleContentGrid_statementView() {
   			var specialSaleId = $("#statementViewSpecialSaleForm").find("input[type=hidden][name='specialSale.id']").val();
   			$("#specialSaleContentDatagrid_statementView").jqGrid({
                mtype: "POST",
                url: getRoot() + "workflow/specialSale/queryContentsBySpecialSaleId.action?specialSale.id="+specialSaleId,
                //shrinkToFit: false,
                //autowidth: false,
                scrollrows: false,
                scroll: true,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "序号", name: "orderNo", width: 70, align: "center"
                }, {
                	label: "品名", name: "saleArticleName", width: 150, align: "center"
                }, {
                	label: "图编号", name: "pictureNo", width: 150, align: "center"
                }, {
                	label: "基材", name: "materialName", width: 150, align: "center"
                }, {
                	label: "打印面", name: "faceId", width: 100, align: "center"
                }, {
                	label: "光泽度", name: "glossinessName", width: 150, align: "center"
                }, {
                	label: "效果", name: "effectName", width: 150, align: "center"
                }, {
                	label: "基材规格", name: "materialFormatName", width: 150, align: "center"
                }, {
                	label: "产品规格宽(mm)", name: "width", width: 120, align: "center"
                }, {
                	label: "产品规格高(mm)", name: "height", width: 120, align: "center"
                }, {
                	label: "单位", name: "articleUnitName", width: 150, align: "center"
                }, {
                	label: "数量", name: "quantity", width: 100, align: "center"
                }, {
                	label: "单价", name: "price", width: 100, align: "center"
                }, {
                	label: "耗损基材费用", name: "lossAreaAmount", width: 100, align: "center"
                }, {
                	label: "产品金额", name: "materialAmount", width: 100, align: "center"
                }, {
                	label: "配件金额", name: "photoFrameAmount", width: 100, align: "center"
                }, {
                	label: "备注", name: "remark", width: 200, align: "center"
                }],
				viewrecords: true,
                height: "200px",
                //width: "100%",
                rowNum: 100
            });
   		}*/
   	</script>
  </body>
</html>
