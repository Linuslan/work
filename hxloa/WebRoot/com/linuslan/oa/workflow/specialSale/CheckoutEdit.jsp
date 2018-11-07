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
    
    <title>出货单</title>
    
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
   	<form id="checkoutViewSpecialSaleForm" action="" class="form-horizontal">
   		<input id="checkoutViewSpecialSaleId" class="specialSaleId" name="specialSale.id" type="hidden" value="${specialSale.id }" />
    	<div class="box-body padding-bottom5 bottom-dotted-border" >
    		<div class="form-group" style="text-align: center; font-size: 20px;">
    			${specialSale.companyName }
    		</div>
    		<div class="form-group" style="text-align: center; font-size: 16px; margin-top: 20px;">
    			出货单
    		</div>
    		<table class="tab_css_2">
    			<tr>
    				<td class="td-center" style="width:10%">收货人：</td>
					<td class="td-center" style="width:10%">
						${specialSale.receiver }
					</td>
					<td class="td-center" style="width:10%">手机：</td>
					<td class="td-center" style="width:10%">
						${specialSale.receiverPhone }
					</td>
					<td class="td-center" style="width:10%">订单号：</td>
					<td class="td-center" style="width:10%">
						${specialSale.serialNo }
					</td>
					<td class="td-center" style="width:10%">出货日期：</td>
					<td class="td-center" style="width:10%">
						<fmt:formatDate value="${specialSale.checkoutDate }" pattern="yyyy-MM-dd"/>
					</td>
					<td></td>
					<td></td>
    			</tr>
    			<tr>
    				<td class="td-center">序号</td>
    				<td class="td-center">品名</td>
    				<td class="td-center">基材</td>
    				<td class="td-center">效果</td>
    				<td class="td-center">基材规格</td>
    				<td class="td-center">产品规格宽(mm)</td>
    				<td class="td-center">产品规格高(mm)</td>
    				<td class="td-center">原材料规格</td>
    				<td class="td-center">单位</td>
    				<td class="td-center">数量</td>
    			</tr>
    		<c:forEach items="${contents }" var="content">
    			<tr class="contents">
    				<input type="hidden" name="contentId" value="${content.id }" />
    				<td class="td-center">${content.orderNo }</td>
    				<td class="td-center">${content.saleArticleName }</td>
    				<td class="td-center">${content.materialName }</td>
    				<td class="td-center">${content.effectName }</td>
    				<td class="td-center">${content.materialFormatName }</td>
    				<td class="td-center">${content.width }</td>
    				<td class="td-center">${content.height }</td>
    				<td class="td-center" style="padding: 5px;">
    					<input name="originalMaterialFormat" class="update form-control" value="${content.originalMaterialFormat }" />
    				</td>
    				<td class="td-center" style="padding: 5px;">
    					<input name="checkoutUnit" class="update form-control" value="${content.checkoutUnit }" />
    				</td>
    				<td class="td-center" style="padding: 5px;">
    					<input name="checkoutQuantity" type="number" class="update form-control" value="${content.checkoutQuantity }" />
    				</td>
    			</tr>
    		</c:forEach>
    		</table>
    		<div class="form-group" style="margin-top: 20px;">
				<table style="width: 100%;">
					<tr>
						<td style="width: 30%"></td>
						<td class="align-right" style="width: 10%">发货人：</td>
						<td class="" style="width: 10%"></td>
						<td class="align-right" style="width: 10%">发货日期：</td>
						<td class="" style="width: 10%"></td>
						<td style="width: 30%"></td>
					</tr>
				</table>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
            //需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
   		});
   	</script>
  </body>
</html>
