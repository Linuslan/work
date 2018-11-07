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
   	<form id="viewPurchaseReqForm" action="" class="form-horizontal">
   		<input class="purchaseReqId" type="hidden" name="purchaseReq.id" id="viewPurchaseReqId" value="${purchaseReq.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#purchaseReq_view" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#purchaseReqAuditorLogs_view" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="purchaseReq_view">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${purchaseReq.companyName }
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">采购编号：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${purchaseReq.serialNo }
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">采购时间：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<fmt:formatDate value="${purchaseReq.purchaseDate }" pattern="yyyy-MM-dd"/>
							</div>
						</div>
			    	</div>
			    	
			    	<div class="box-body">
			    		<table id="purchaseReqContentDatagrid_view"></table>
			    	</div>
			    </div>
			   	<div class="tab-pane" id="purchaseReqAuditorLogs_view">
   					<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="purchaseReqViewAuditorlogsDatagrid"></table>
	   							<div id="purchaseReqViewAuditorlogsDatagridPager"></div>
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
			setTimeout("generatePurchaseReqAuditLogGrid()", 0);
			setTimeout("generatePurchaseReqContentGrid_view()", 0);
   		});
   		
   		function generatePurchaseReqAuditLogGrid() {
   			var id = $("#viewPurchaseReqForm").find("input.purchaseReqId").val();
   			$("#purchaseReqViewAuditorlogsDatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=purchaseReq&wfId="+id,
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
                pager: "#purchaseReqViewAuditorlogsDatagridPager"
            });
   		}
   		
   		function generatePurchaseReqContentGrid_view() {
   			var id = $("#viewPurchaseReqForm").find("input.purchaseReqId").val();
   			$("#purchaseReqContentDatagrid_view").jqGrid({
   				url: getRoot() + "workflow/purchaseReq/queryContentsByPurchaseReqId.action?purchaseReq.id="+id,
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "purchaseReqClassName": "d", "purchaseReqClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "商品名称", name: "article"
                }, {
                	label: "规格", name: "format", width: 150, align: "center"
                }, {
                	label: "单位", name: "unit", width: 150, align: "center"
                }, {
                	label: "数量", name: "quantity", width: 150, align: "center"
                }, {
                	label: "单价", name: "price", width: 150, align: "center"
                }, {
                	label: "金额", name: "totalPrice", width: 150, align: "center"
                }, {
                	label: "到货日期", name: "arriveDate", width: 300, align: "center"
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
