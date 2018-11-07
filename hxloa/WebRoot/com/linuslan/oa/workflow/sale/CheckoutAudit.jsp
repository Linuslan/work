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
   	<form id="checkoutAuditSaleForm" action="" class="form-horizontal">
   		<input id="checkoutAuditSaleId" class="saleId" name="sale.id" type="hidden" value="${sale.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#sale_checkoutAudit" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#sale_invoice_checkoutAudit" data-toggle="tab">开票信息</a>
   				</li>
   				<li>
   					<a href="#sale_checkoutAuditLog_checkoutAudit" data-toggle="tab">审核记录</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="sale_checkoutAudit">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.companyName }
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">编号：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${serialNo }
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">订单编号：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.orderSerialNo }
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">订单日期：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<fmt:formatDate value="${sale.saleDate }" pattern="yyyy-MM-dd"/>
							</div>
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">归属客户：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.customerName }
							</div>
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">联系电话：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.telephone }
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">收货人：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.receiver }
							</div>
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">手机：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.receiverPhone }
							</div>
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">邮编：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.zipCode }
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">收货地址：</label>
							<div class="col-md-7-sm col-sm-8 left-label">
								${sale.receiverAddress }
							</div>
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">是否开票：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.isInvoice == 0 ? "否" : "是" }
							</div>
						</div>
						<c:if test="${sale.isInvoice == 1 }">
							<div class="form-group">
								<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">开票状态：</label>
								<div class="col-md-2 col-sm-8 left-label">
									<c:choose>
										<c:when test="${sale.invoiceStatus == 1}">
											已开票
										</c:when>
										<c:when test="${sale.invoiceStatus == 2 }">
											未开票作废
										</c:when>
										<c:when test="${sale.invoiceStatus == 3 }">
											已开票作废
										</c:when>
										<c:otherwise>
											未开票
										</c:otherwise>
									</c:choose>
								</div>
								<label for="groupId" class="col-md-2-sm col-sm-4 control-label">开票时间：</label>
								<div class="col-md-2 col-sm-8 left-label">
									<fmt:formatDate value="${sale.confirmInvoiceDate }" pattern="yyyy-MM-dd"/>（${sale.invoiceUserName }）
								</div>
								<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">回款状态：</label>
								<div class="col-md-2 col-sm-8 left-label">
									<c:choose>
										<c:when test="${sale.restreamStatus == 1 }">
											部分回款
										</c:when>
										<c:when test="${sale.restreamStatus == 2 }">
											已回款
										</c:when>
										<c:otherwise>
											未回款
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-2-sm col-sm-4 control-label">实回款：</label>
								<div class="col-md-2 col-sm-8 left-label">
									${sale.actualMoney }
								</div>
								<label for="groupId" class="col-md-2-sm col-sm-4 control-label">回款时间：</label>
								<div class="col-md-2 col-sm-8 left-label">
									<fmt:formatDate value="${sale.confirmRestreamDate }" pattern="yyyy-MM-dd"/>（${sale.confirmRestreamUserName }）
								</div>
								<label for="departmentId" class="col-md-2-sm col-sm-4 control-label"></label>
								<div class="col-md-2 col-sm-8 left-label">
									
								</div>
							</div>
						</c:if>
						<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">出库状态：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select name="sale.checkoutStatus" class="form-control">
    								<option value="0" ${sale.checkoutStatus == 0 ? "selected='selected'" : "" }>未出库</option>
	    							<option value="1" ${sale.checkoutStatus == 1 ? "selected='selected'" : "" }>部分出库</option>
	    							<option value="2" ${sale.checkoutStatus == 2 ? "selected='selected'" : "" }>已出库</option>
    							</select>
							</div>
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">仓管备注：</label>
							<div class="col-md-7-sm col-sm-8 no-padding">
								<textarea class="form-control" name="sale.stockmanInfo">${sale.stockmanInfo }</textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">财务备注：</label>
							<div class="col-md-11-sm col-sm-8 left-label">
								${sale.financeInfo }
							</div>
						</div>
						
			    	</div>
			    	<div class="box-body" style="overflow-x: auto;">
			    		<table id="saleContentDatagrid_checkoutAudit"></table>
			    	</div>
			    </div>
			    <div class="tab-pane" id="sale_invoice_checkoutAudit">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" style="${sale.isInvoice == 0 ? "display: none;" : ""}" >
			    		<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">开票金额：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.invoiceMoney }
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">应回款：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.supposedMoney }
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">收入归属部门：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.incomeDepartmentName }
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">预计回款时间：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<fmt:formatDate value="${sale.planRestreamDate }" pattern="yyyy-MM-dd"/>
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">开票类型：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.invoiceTypeName }
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">税号：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.taxPayerId }
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">联系电话：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.invoicePhone }
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">开户行：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.invoiceBank }
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">账号：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${sale.invoiceBankNo }
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">地址：</label>
							<div class="col-md-11-sm col-sm-8 left-label">
								${sale.invoiceAddress }
							</div>
						</div>
			    	</div>
			    </div>
			    <div class="tab-pane" id="sale_checkoutAuditLog_checkoutAudit">
			    	<div class="box box-solid">
		   				<div class="box-body">
		   					<div class="form-group bottom-dotted-border padding-bottom5">
								<label for="leaderGroup" class="col-md-1 col-sm-1 control-label">意见：</label>
								<div class="col-md-10 col-sm-10">
									<textarea name="opinion" class="form-control" rows="3" placeholder="请输入"></textarea>
								</div>
							</div>
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="saleAuditorlogsDatagrid_checkoutAudit"></table>
	   							<div id="saleAuditorlogsDatagridPager_checkoutAudit"></div>
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
			setTimeout("buildSaleContentGrid_checkoutAudit()", 0);
			setTimeout("buildSaleLogGrid_checkoutAudit()", 0);
   		});
   		
   		function buildSaleLogGrid_checkoutAudit() {
   			$("#saleAuditorlogsDatagrid_checkoutAudit").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=sale&wfId="+$("#checkoutAuditSaleId").val(),
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
                pager: "#saleAuditorlogsDatagridPager_checkoutAudit"
            });
   		}
   		
   		function buildSaleContentGrid_checkoutAudit() {
   			var saleId = $("#checkoutAuditSaleForm").find("input[type=hidden][name='sale.id']").val();
   			$("#saleContentDatagrid_checkoutAudit").jqGrid({
                mtype: "POST",
                url: getRoot() + "workflow/sale/queryContentsBySaleId.action?sale.id="+saleId,
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "商品编号", name: "serialNo", editable: false
                }, {
                	label: "商品名称", name: "articleName"
                }, {
                	label: "规格", name: "formatName", width: 150, align: "center"
                }, {
                	label: "单位", name: "unit", width: 150, align: "center", editable: false
                }, {
                	label: "出货日期", name: "deliverDate", width: 150, align: "center", editable: false
                }, {
                	label: "数量", name: "quantity", width: 150, align: "center"
                }, {
                	label: "已出库", name: "checkoutQuantity", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "checkoutQuantity",
                		cls: "count"
                	}
                }, {
                	label: "单价", name: "price", width: 150, align: "center"
                }, {
                	label: "总金额", name: "totalAmount", width: 150, align: "center", editable: false
                }, {
                	label: "质量要求", name: "quality", width: 300, align: "center"
                }, {
                	label: "备注", name: "memo", width: 300, align: "center"
                }, {
                	label: "排序号", name: "orderNo", width: 150, align: "center"
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20,
                onSelectRow: function(id){
                	if(id && id!==auditSaleLastSel){
                		jQuery("#saleContentDatagrid_checkoutAudit").saveRow(auditSaleLastSel, false, "clientArray");
                		var rowData = $("#saleContentDatagrid_checkoutAudit").getRowData(auditSaleLastSel);
                		jQuery("#saleContentDatagrid_checkoutAudit").jqGrid("editRow", id);
                		auditSaleLastSel=id;
                	}
			    }
            });
   		}
   		
   		/**
		 * 创建金额输入框
		 */
		function createNumberBox(value, options) {
			if(!value) {
				value = 0;
			}
			var name = options.name;
			var cls = options.cls;
			if(!cls) {
				cls="";
			}
			//创建外围的div
			var div = document.createElement("div");
			div.setAttribute("class", "input-group spinner ");
			div.setAttribute("style", "width: 99%");
			
			//创建输入框
			var input = document.createElement("input");
			div.appendChild(input);
			input.setAttribute("name", name);
			input.setAttribute("type", "number");
			input.setAttribute("class", "form-control "+cls);
			input.setAttribute("style", "width: 99%");
			input.setAttribute("value", value);
			return div;
		}
		
		function operateNumberValue(elem, operation, value) {
			if(operation == "get") {
				return $("input", elem).val();
			} else {
				$("input", elem).val(value);
			}
		}
   	</script>
  </body>
</html>
