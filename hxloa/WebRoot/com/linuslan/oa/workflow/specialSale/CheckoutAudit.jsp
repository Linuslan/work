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
   	<form id="checkoutAuditSpecialSaleForm" action="" class="form-horizontal">
   		<input id="checkoutAuditSpecialSaleId" class="specialSaleId" name="specialSale.id" type="hidden" value="${specialSale.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#specialSale_checkoutAudit" data-toggle="tab">详情</a>
   				</li>
   				
   				<li>
   					<a href="#specialSale_checkoutAuditLog_checkoutAudit" data-toggle="tab">审核记录</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="specialSale_checkoutAudit">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-1 col-sm-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.companyName }
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">是否开票：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.isInvoice == 0 ? "否" : "是" }
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-1 col-sm-4 control-label">订单号：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.serialNo }
							</div>
							<label for="text" class="col-md-1 col-sm-4 control-label">订单日期：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<fmt:formatDate value="${specialSale.saleDate }" pattern="yyyy-MM-dd"/>
							</div>
							<label for="text" class="col-md-1 col-sm-4 control-label">出货日期：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<fmt:formatDate value="${specialSale.checkoutDate }" pattern="yyyy-MM-dd"/>
							</div>
							<label for="groupId" class="col-md-1 col-sm-4 control-label">结款日期：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<fmt:formatDate value="${specialSale.payDate }" pattern="yyyy-MM-dd"/>
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">业务员：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								${specialSale.salesman }
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">客户：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.customer }
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">手机：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.customerPhone }
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">付款方式：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.payTypeName }
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">送货方式：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.deliverTypeName }
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">收货人：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.receiver }
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">手机：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.receiverPhone }
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">收货地址：</label>
							<div class="col-md-11 col-sm-8 left-label">
								${specialSale.receiverAddress }
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">定金：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.frontMoney }
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">总金额：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.totalAmount }
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">成品金额：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.productAmount }
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">运费：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${specialSale.freight }
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 col-sm-4 control-label">出库状态：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select name="specialSale.checkoutStatus" class="form-control">
    								<option value="0" ${specialSale.checkoutStatus == 0 ? "selected='selected'" : "" }>未出库</option>
	    							<option value="1" ${specialSale.checkoutStatus == 1 ? "selected='selected'" : "" }>部分出库</option>
	    							<option value="2" ${specialSale.checkoutStatus == 2 ? "selected='selected'" : "" }>已出库</option>
    							</select>
							</div>
							<label for="groupId" class="col-md-1 col-sm-4 control-label">仓管备注：</label>
							<div class="col-md-8 col-sm-8 no-padding">
								<textarea class="form-control" name="specialSale.stockmanInfo">${specialSale.stockmanInfo }</textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 col-sm-4 control-label">财务备注：</label>
							<div class="col-md-11 col-sm-8 left-label">
								${specialSale.financeInfo }
							</div>
						</div>
						<div class="form-group">
							
							
						</div>
			    	</div>
			    	<div class="box-body" style="overflow-x: auto;">
			    		<table id="specialSaleContentDatagrid_checkoutAudit"></table>
			    	</div>
			    </div>
			    <div class="tab-pane" id="specialSale_checkoutAuditLog_checkoutAudit">
			    	<div class="box box-solid">
		   				<div class="box-body">
		   					<div class="form-group bottom-dotted-border padding-bottom5">
								<label for="leaderGroup" class="col-md-1 col-sm-1 control-label">意见：</label>
								<div class="col-md-10 col-sm-10">
									<textarea name="opinion" class="form-control" rows="3" placeholder="请输入"></textarea>
								</div>
							</div>
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="specialSaleAuditorlogsDatagrid_checkoutAudit"></table>
	   							<div id="specialSaleAuditorlogsDatagridPager_checkoutAudit"></div>
   							</div>
		   				</div>
		   			</div>
			    </div>
			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
            //需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("buildSpecialSaleContentGrid_checkoutAudit()", 0);
			setTimeout("buildSpecialSaleLogGrid_checkoutAudit()", 0);
   		});
   		
   		function buildSpecialSaleLogGrid_checkoutAudit() {
   			$("#specialSaleAuditorlogsDatagrid_checkoutAudit").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=specialSale&wfId="+$("#checkoutAuditSpecialSaleId").val(),
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "ID", name: "id", hidden: true, key: true
                }, {
                	label: "审核人", name: "auditorName", width: 150, align: "center"
                }, {
                	label: "审核时间", name: "auditDate", width: 200, align: "center"
                }, {
                	label: "意见", name: "opinion", width: 400, align: "center"
                }, {
                	label: "操作类型", name: "passType", width: 100, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		if(rowObject.passType == "0") {
                			return "通过";
                		} else {
                			return "退回";
                		}
                	}
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20,
                pager: "#specialSaleAuditorlogsDatagridPager_checkoutAudit"
            });
   		}
   		
   		function buildSpecialSaleContentGrid_checkoutAudit() {
   			var specialSaleId = $("#checkoutAuditSpecialSaleForm").find("input[type=hidden][name='specialSale.id']").val();
   			$("#specialSaleContentDatagrid_checkoutAudit").jqGrid({
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
   		}
   	</script>
  </body>
</html>
