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
   	<form id="viewCheckoutForm" action="" class="form-horizontal">
   		<input class="checkoutId" type="hidden" name="checkout.id" id="viewCheckoutId" value="${checkout.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#checkout_view" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#checkoutAuditorLogs_view" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="checkout_view">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${checkout.companyName }
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">出库编号：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${checkout.serialNo }
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">出库仓库：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${checkout.warehouseName }
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">出库类型：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${checkout.checkoutTypeName }
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">销售单号：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${checkout.saleSerialNo }
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">出库时间：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<fmt:formatDate value="${checkout.checkoutDate }" pattern="yyyy-MM-dd"/>
							</div>
						</div>
						<div class="form-group">
							<label for="moneyDate" class="col-md-2-sm col-sm-4 control-label">客户名称：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${checkout.customerName }
							</div>
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">联系电话：</label>
							<div class="col-md-2 col-sm-8 telephone left-label" >
								${checkout.telephone }
							</div>
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">收货人：</label>
							<div class="col-md-2 col-sm-8 telephone left-label" >
								${checkout.receiver }
							</div>
						</div>
						<div class="form-group padding-bottom5">
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">收货人联系电话：</label>
							<div class="col-md-2 col-sm-8 telephone left-label" >
								${checkout.receiverPhone }
							</div>
							<label for="orderNo" class="col-md-2-sm col-sm-4 control-label">单位地址：</label>
							<div class="col-md-7-sm col-sm-8 address left-label">
								${checkout.address }
							</div>
						</div>
			    	</div>
			    	
			    	<div class="box-body">
			    		<table id="checkoutContentDatagrid_view"></table>
			    	</div>
			    </div>
			   	<div class="tab-pane" id="checkoutAuditorLogs_view">
   					<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="checkoutViewAuditorlogsDatagrid"></table>
	   							<div id="checkoutViewAuditorlogsDatagridPager"></div>
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
			setTimeout("generateCheckoutAuditLogGrid()", 0);
			setTimeout("generateCheckoutContentGrid_view()", 0);
   		});
   		
   		function generateCheckoutAuditLogGrid() {
   			var id = $("#viewCheckoutForm").find("input.checkoutId").val();
   			$("#checkoutViewAuditorlogsDatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=checkout&wfId="+id,
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
                pager: "#checkoutViewAuditorlogsDatagridPager"
            });
   		}
   		
   		function generateCheckoutContentGrid_view() {
   			var id = $("#viewCheckoutForm").find("input.checkoutId").val();
   			$("#checkoutContentDatagrid_view").jqGrid({
   				url: getRoot() + "workflow/checkout/queryContentsByCheckoutId.action?checkout.id="+id,
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "checkoutClassName": "d", "checkoutClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "商品编号", name: "serialNo", editable: false
                }, {
                	label: "商品名称", name: "checkinArticleName"
                }, {
                	label: "规格", name: "formatName", width: 150, align: "center"
                }, {
                	label: "单位", name: "unit", width: 150, align: "center", editable: false
                }, {
                	label: "数量", name: "quantity", width: 150, align: "center"
                }, {
                	label: "单价", name: "price", width: 150, align: "center"
                }, {
                	label: "金额", name: "totalPrice", width: 150, align: "center", editable: false
                }, {
                	label: "备注", name: "memo", width: 300, align: "center"
                }, {
                	label: "排序号", name: "orderNo", width: 150, align: "center"
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20
            });
   		}
   	</script>
  </body>
</html>
