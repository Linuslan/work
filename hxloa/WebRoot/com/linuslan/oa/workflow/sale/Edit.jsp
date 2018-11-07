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
    
    <title>编辑销售订单申请</title>
    
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
   	<form id="editSaleForm" action="" class="form-horizontal">
   		<input id="editSaleId" class="saleId" name="sale.id" type="hidden" value="${sale.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#sale_edit" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#sale_invoice_edit" data-toggle="tab">开票信息</a>
   				</li>
   				<li>
   					<a href="#sale_auditLog_edit" data-toggle="tab">审核记录</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="sale_edit">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select id="saleCompany_edit" class="select2" name="sale.companyId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${companys }" var="company">
										<option value="${company.id }" ${sale.companyId == company.id ? "selected='selected'" : "" }>${company.name }</option>
									</c:forEach>
								</select>
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">编号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.serialNo" type="text" value="${sale.serialNo }" readonly="readonly" class="form-control showText" id="text">
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">订单编号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.orderSerialNo" type="text" value="${sale.orderSerialNo }" class="form-control showText" id="text">
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">订单日期：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="sale.saleDate" type="text" value="<fmt:formatDate value="${sale.saleDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
								</div>
							</div>
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">归属客户：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select id="saleCustomer_edit" class="select2" name="sale.customerId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${customers }" var="customer">
										<option value="${customer.id }" telephone="${customer.telephone }"
										address="${customer.address }" zipCode="${customer.zipCode }"
    									receiver="${customer.receiver }" receiverPhone="${customer.receiverPhone }"
    									receiverAddress="${customer.receiverAddress }"
    									${sale.customerId == customer.id ? "selected='selected'" : "" }>${customer.name }</option>
									</c:forEach>
								</select>
							</div>
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">联系电话：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.telephone" type="text" value="${sale.telephone }" class="form-control telephone" id="text">
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">收货人：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.receiver" type="text" value="${sale.receiver }" class="form-control receiver" id="text">
							</div>
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">手机：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.receiverPhone" type="text" value="${sale.receiverPhone }" class="form-control receiverPhone" id="text">
							</div>
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">邮编：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.zipCode" type="text" value="${sale.zipCode }" class="form-control zipCode" id="text">
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">收货地址：</label>
							<div class="col-md-7-sm col-sm-8 no-padding">
								<input name="sale.receiverAddress" type="text" value="${sale.receiverAddress }" class="form-control" id="text">
							</div>
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">是否开票：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<input name="sale.isInvoice" type="radio" value="0" class="minimal" ${sale.isInvoice == 0 ? "checked" : "" } />&nbsp;否&nbsp;&nbsp;&nbsp;&nbsp;
								<input name="sale.isInvoice" type="radio" value="1" class="minimal" ${sale.isInvoice == 1 ? "checked" : "" }/>&nbsp;是
							</div>
						</div>
			    	</div>
			    	<div class="toolbar with-border">
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增销售订单项目" id="addSaleContent_edit"><i class="fa fa-fw fa-plus"></i>新增</button>
					</div>
			    	<div class="box-body" style="overflow-x: auto;">
			    		<table id="saleContentDatagrid_edit"></table>
			    	</div>
			    </div>
			    <div class="tab-pane" id="sale_invoice_edit">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" style="${sale.isInvoice == 0 ? "display: none;" : ""}" >
			    		<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">开票金额：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.invoiceMoney" type="number" value="${sale.invoiceMoney }" class="form-control" id="text">
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">应回款：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.supposedMoney" type="number" value="${sale.supposedMoney }" class="form-control" id="text">
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">收入归属部门：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<div class="departmentTree" value="${sale.incomeDepartmentId }" text="${sale.incomeDepartmentName }"></div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">预计回款时间：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="sale.planRestreamDate" type="text" value="<fmt:formatDate value="${sale.planRestreamDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
								</div>
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">开票类型：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select class="select2" name="sale.invoiceTypeId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${invoiceTypes }" var="invoiceType">
										<option value="${invoiceType.id }" ${sale.invoiceTypeId == invoiceType.id ? "selected='selected'" : "" }>${invoiceType.text }</option>
									</c:forEach>
								</select>
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">税号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.taxPayerId" type="text" value="${sale.taxPayerId }" class="form-control" id="text">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">联系电话：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.invoicePhone" type="text" value="${sale.invoicePhone }" class="form-control" id="text">
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">开户行：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.invoiceBank" type="text" value="${sale.invoiceBank }" class="form-control" id="text">
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">账号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.invoiceBankNo" type="text" value="${sale.invoiceBankNo }" class="form-control" id="text">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">地址：</label>
							<div class="col-md-11-sm col-sm-8 no-padding">
								<input name="sale.invoiceAddress" type="text" value="${sale.invoiceAddress }" class="form-control" id="text">
							</div>
						</div>
			    	</div>
			    </div>
			    <div class="tab-pane" id="sale_auditLog_edit">
			    	<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="saleAuditorlogsDatagrid_edit"></table>
	   							<div id="saleAuditorlogsDatagridPager_edit"></div>
   							</div>
		   				</div>
		   			</div>
			    </div>
			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			saleLastSel = null;
   			$("#editSaleForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#editSaleForm").find("select[name='sale.customerId']").change(function() {
   				var $option = $(this).find("option:selected");
   				var telephone = $option.attr("telephone");
   				var receiverAddress = $option.attr("receiverAddress");
   				var receiver = $option.attr("receiver");
   				var receiverPhone = $option.attr("receiverPhone");
   				var zipCode = $option.attr("zipCode");
   				
   				$("#editSaleForm").find("input.telephone").val(telephone);
   				$("#editSaleForm").find("input.receiverAddress").val(address);
   				$("#editSaleForm").find("input.receiver").val(receiver);
   				$("#editSaleForm").find("input.receiverPhone").val(receiverPhone);
   				$("#editSaleForm").find("input.zipCode").val(zipCode);
   			});
   			$("#saleContentDatagrid_edit").on("blur", "input.count", function() {
   				var rowid = $("#saleContentDatagrid_edit").getGridParam("selrow");
				var $tr = $("#saleContentDatagrid_edit").find("tr[id='"+rowid+"']");
				var price = $tr.find("input[name=price]").val();
				var quantity = $tr.find("input[name=quantity]").val();
				var $totalPrice = $tr.find("td[aria-describedby=saleContentDatagrid_edit_totalAmount]");
				var loss = $tr.find("input[name=loss]").val();
				var $remainder = $tr.find("td[aria-describedby=saleContentDatagrid_edit_remainder]");
				var totalPrice = price*quantity;
				totalPrice = totalPrice.toFixed(2);
				var remainder = quantity - loss;
				remainder = remainder.toFixed(2);
				$totalPrice.html(totalPrice);
				$remainder.html(remainder);
   			});
   			$("#editSaleForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
	        $("#editSaleForm").find("input[type='radio'][name='sale.isInvoice']").on("ifChecked", function() {
	        	var val = $(this).val();
	        	if(val == 0) {
	        		$("#sale_invoice_add").find("div:eq(0)").hide();
	        	} else {
	        		$("#sale_invoice_add").find("div:eq(0)").show();
	        	}
	        });
   			$("#editSaleForm").find(".select2").select2();
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("buildSaleContentGrid_edit()", 0);
            setTimeout("buildSaleLogGrid_edit", 0)
            $("#addSaleContent_edit").click(function() {
            	if(saleLastSel){
               		jQuery("#saleContentDatagrid_edit").saveRow(saleLastSel, false, "clientArray");
               	}
            	var saleCompanyId = $("#saleCompany_edit").val();
				if(!saleCompanyId) {
					BootstrapDialog.danger("请先选择归属公司");
					return false;
				}
				var customerId = $("#saleCustomer_edit").val();
				if(!customerId) {
					BootstrapDialog.danger("请先选择客户");
					return false;
				}
            	var rowId = Math.random();
            	$("#saleContentDatagrid_edit").addRowData(rowId, {
            		id: "",
            		serialNo: "",
            		saleArticleId: "",
            		formatId: "",
            		unit: "",
            		deliverDate: "",
            		quantity: "",
            		price: "",
            		totalAmount: "",
            		quality: "",
            		memo: "",
            		orderNo: "0"
            	});
            });
            
            $("#editSaleForm a[data-toggle='tab']").on("shown.bs.tab", function (e) {
   				var href = $(this).attr("href");
   				var id = href.substring(1);
   				if(id == "sale_auditLog_edit") {
   					$("#"+id).find("table").setGridWidth($("#"+id).width()*0.99);
   				}
   				
   				if(id == "sale_invoice_edit") {
   					saleDepartment_edit.destory();
   					$("#editSaleForm").find(".departmentTree").combotree({
						url: getRoot() + "sys/department/queryTree.action",
						async: true,
						singleSelect: true,
						loadOnExpand: false,
						loadParams: {
							"id": "department.id"
						},
						idField: "id",
						textField: "name",
						name: "specialSale.incomeDepartmentId",
						pidField: "pid"
					});
   				}
   			});
   			
   			var saleDepartment_edit = $("#editSaleForm").find(".departmentTree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "sale.incomeDepartmentId",
				pidField: "pid"
			});
   		});
   		var rowDatas;
   		function buildSaleContentGrid_edit() {
   			var id = $("#editSaleForm").find("input.saleId").val();
   			$("#saleContentDatagrid_edit").jqGrid({
   				url: getRoot() + "workflow/sale/queryContentsBySaleId.action?sale.id="+id,
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "saleClassName": "d", "saleClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "商品编号", name: "serialNo", editable: false
                }, {
                	label: "商品名称", name: "articleId", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/article/queryCheckoutArticles.action",
                		name: "articleId",
                		custom_element: createSaleArticleSelect2_edit,
                		custom_value: operateSaleArticleSelect2Value_edit
					},
					formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!saleSelectedArticle_edit[rowId]) {
               				saleSelectedArticle_edit[rowId] = {id: rowObject.articleId, name: rowObject.articleName}
               			}
               			text = saleSelectedArticle_edit[rowId].name ? saleSelectedArticle_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return saleSelectedArticle_edit[rowId].id;
                	}
                }, {
                	label: "规格", name: "formatId", width: 150, align: "center", editable: true, edittype: "custom",
					editoptions: {
						url: "workflow/article/queryFormatsByArticleId.action",
                		name: "formatId",
                		id: "saleFormatId_edit",
                		custom_element: createSaleFormatSelect2_edit,
                		custom_value: operateSaleFormatSelect2Value_edit
					},
					formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!saleSelectedFormat_edit[rowId]) {
               				saleSelectedFormat_edit[rowId] = {id: rowObject.formatId, name: rowObject.formatName}
               			}
               			text = saleSelectedFormat_edit[rowId].name ? saleSelectedFormat_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return saleSelectedFormat_edit[rowId].id;
                	}
                }, {
                	label: "单位", name: "unit", width: 100, align: "center", editable: false
                }, {
                	label: "出货日期", name: "deliverDate", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createDateBox,
                		custom_value: operateDateValue
                	}
                }, {
                	label: "数量", name: "quantity", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "quantity",
                		cls: "count"
                	}
                }, {
                	label: "单价", name: "price", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		cls: "count",
                		name: "price"
                	}
                }, {
                	label: "总金额", name: "totalAmount", width: 150, align: "center", editable: false
                }, {
                	label: "质量要求", name: "quality", width: 300, align: "center", editable: true, edittype: "textarea", editoptions: {rows:"2",width:"100%"}
                }, {
                	label: "备注", name: "memo", width: 300, align: "center", editable: true, edittype: "textarea", editoptions: {rows:"2",width:"100%"}
                }, {
                	label: "排序号", name: "orderNo", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "orderNo"
                	}
                }, {
                	label: "操作", name: "operationCell", align: "center", width: 70, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		var rowId = options.rowId;
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delAddSaleContent(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                loadComplete: function(data) {
                	rowDatas = data;
                },
                beforeSelectRow: function(rowid) {
                	var companyId = $("#saleCompany_edit").val();
                	var customerId = $("#saleCustomer_edit").val();
                	if(companyId && customerId) {
                		return true;
                	} else {
                		return false;
                	}
                },
                onSelectRow: function(id){
                	if(id && id!==saleLastSel){
                		jQuery("#saleContentDatagrid_edit").saveRow(saleLastSel, false, "clientArray");
                		var rowData = $("#saleContentDatagrid_edit").getRowData(saleLastSel);
                		jQuery("#saleContentDatagrid_edit").jqGrid("editRow", id, {
                			oneditfunc: function(id) {
                				var $tr = $("#saleContentDatagrid_edit").find("tr[id='"+id+"']");
                				$tr.find("select").select2();
                				$tr.find("select[name=articleId]").change(function() {
									var rowid = $("#saleContentDatagrid_edit").getGridParam("selrow");
									var $tr = $("#saleContentDatagrid_edit").find("tr[id='"+rowid+"']");
									var $formatSelect = $tr.find("select[name=formatId]");
									var $option = $(this).find("option:selected");
									var articleId = $option.attr("checkinArticleId");
									var unit = $option.attr("unit");
									var serialNo = $option.attr("serialNo");
									getSaleFormatOptions_edit($formatSelect, articleId);
									$tr.find("td[aria-describedby=saleContentDatagrid_edit_unit]").html(unit);
									$tr.find("td[aria-describedby=saleContentDatagrid_edit_serialNo]").html(serialNo);
								});
                			}
                		});
                		saleLastSel=id;
                	}
			    },
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20
            });
   		}
   		
   		function buildSaleLogGrid_edit() {
   			$("#editSaleAuditorlogsDatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=sale&wfId="+$("#editSaleId").val(),
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
                pager: "#editSaleAuditorlogsDatagridpager"
            });
   		}
   		
   		/**
   		 * 创建日期选择框
   		 */
		function createDateBox (value, options) {
			var inputGroup = document.createElement("div");
			inputGroup.setAttribute("class", "input-group");
			
			var inputGroupAddon = document.createElement("div");
			inputGroup.appendChild(inputGroupAddon);
			inputGroupAddon.setAttribute("class", "input-group-addon");
			
			var iEl = document.createElement("i");
			inputGroupAddon.appendChild(iEl);
			iEl.setAttribute("class", "fa fa-calendar");
		
			var inputEl = document.createElement("input");
			inputEl.value = value;
			inputGroup.appendChild(inputEl);
			inputEl.setAttribute("readonly", "readonly");
			inputEl.setAttribute("name", "remittanceDate");
			inputEl.setAttribute("type", "text");
			inputEl.setAttribute("class", "form-control pull-right date");
			$(inputEl).datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
			
			return inputGroup;
		}
		 
		function operateDateValue(elem, operation, value) {
		    if(operation === "get") {
		    	return $("input", elem).val();
		    } else if(operation === "set") {
		    	$("input",elem).val(value);
		    }
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
		
		/**
		 * 通过配置的name获取列内容
		 */
		function getColModelByName(name) {
			var cols = $("#saleContentDatagrid_edit").getGridParam("colModel");
			var selectCol;
			for(var i in cols) {
				var col = cols[i];
				var colName = col.name;
				if(colName == name) {
					selectCol = col;
				}
			}
			return selectCol;
		}
		
		function delEditSaleContent(id, rowId) {
			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定删除吗？",
	            type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	            	
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	                	if(id && ""!=$.trim(id) && isNumber(id)) {
	                		$.ajax({
		   						url: getRoot() + "workflow/sale/delContentById.action",
			    				data: "saleContent.id="+id,
			    				type: "POST",
			    				beforeSend: function() {
			    					//mask($("#userdatagrid").parent());
			    				},
			    				complete: function() {
			    					//unmask($("#userdatagrid").parent());
			    				},
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						$("#saleContentDatagrid_edit").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#saleContentDatagrid_edit").delRowData(rowId);
		            	}
	   				}
	            }
   			});
		}
		
		/**
		 * 创建出库类别选择框
		 */
		function createSaleArticleSelect2_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#saleContentDatagrid_edit").getGridParam("selrow");
			
			//得到选中的值
			var selectedVal;
			if(saleSelectedArticle_edit[rowid]) {
				selectedVal = saleSelectedArticle_edit[rowid].id;
			}
			var selectEl = document.createElement("select");
			div.appendChild(selectEl);
			selectEl.setAttribute("class", "select2");
			selectEl.setAttribute("name", name);
			selectEl.setAttribute("style", "width: 100%");
			var option = document.createElement("option");
			option.value="";
			option.text="请选择";
			selectEl.appendChild(option);
			var url = options.url;
			var datas;
			var companyId = $("#saleCompany_edit").val();
			var customerId = $("#saleCustomer_edit").val();
			$(selectEl).select2();
			if(url) {
				$.ajax({
					url: getRoot() + url,
					data: "companyId="+companyId+"&customerId="+customerId,
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						for(var i = 0; i < json.length; i ++) {
							var article = json[i];
							var id = article.id;
							var name = article.checkinArticleName;
							var unit = article.unit;
							var serialNo = article.serialNo;
							var checkinArticleId = article.checkinArticleId;
							var option = document.createElement("option");
							option.value=id;
							option.text=name;
							$(option).attr("unit", unit);
							$(option).attr("serialNo", serialNo);
							$(option).attr("checkinArticleId", checkinArticleId);
							selectEl.appendChild(option);
							if(id == selectedVal) {
								option.selected=true;
								$(selectEl).val(id).trigger("change");
							}
						}
						
					},
					error: function() {
						
					},
					complete: function() {
						
					}
				});
			}
			
			return div;
		}
		//存放选中的出库类别的id
		var saleSelectedArticle_edit = new Object();
		function operateSaleArticleSelect2Value_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var articleId = $("select", elem).find("option:selected").attr("checkinArticleId");
				var rowid = $("#saleContentDatagrid_edit").getGridParam("selrow");
				saleSelectedArticle_edit[saleLastSel] = {id: val, name: text, checkinArticleId: articleId};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		/**
		 * 创建出库类别选择框
		 */
		function createSaleFormatSelect2_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#saleContentDatagrid_edit").getGridParam("selrow");
			//得到选中的值
			var selectedVal;
			if(saleSelectedFormat_edit[rowid]) {
				selectedVal = saleSelectedFormat_edit[rowid].id;
			}
			var selectEl = document.createElement("select");
			div.appendChild(selectEl);
			selectEl.setAttribute("class", "select2");
			selectEl.setAttribute("name", name);
			selectEl.setAttribute("style", "width: 100%");
			var id = options.id;
			selectEl.setAttribute("id", id);
			
			var url = options.url;
			var datas;
			var article = saleSelectedArticle_edit[rowid];
			$(selectEl).select2();
			if(article && article.id && ""!=$.trim(article.id)) {
				getSaleFormatOptions_edit($(selectEl), article.checkinArticleId);
			}
			
			return div;
		}
		//存放选中的出库类别的id
		var saleSelectedFormat_edit = new Object();
		function operateSaleFormatSelect2Value_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#saleContentDatagrid_edit").getGridParam("selrow");
				saleSelectedFormat_edit[saleLastSel] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function getSaleFormatOptions_edit($select, articleId) {
			//$select.html("");
			var rowid = $("#saleContentDatagrid_edit").getGridParam("selrow");
			try {
				$select.select2("val", "");
			} catch(ex) {
				
			}
			$select.empty();
			if(articleId && "" != $.trim(articleId)) {
				var option = document.createElement("option");
				option.value = "";
				option.text = "请选择";
				$select.append(option);
				var url = getRoot() + "workflow/article/queryFormatsByArticleId.action";
			
				$.ajax({
					url: url,
					data: {
						"article.id": articleId
					},
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						if(json && json.length > 0) {
							var selectVal;
							if(rowid) {
								selectVal = saleSelectedFormat_edit[rowid]
							}
							for(var i = 0; i < json.length; i ++) {
								var format = json[i];
								var option = document.createElement("option");
								option.value=format.id;
								option.text=format.name;
								$select.append(option);
								if(selectVal && selectVal.id && selectVal.id == format.id) {
									option.selected = true;
									$select.val(format.id).trigger("change");
								}
							}
						}
					},
					error: function(data) {
						
					}
				});
			}
			
		}
   	</script>
  </body>
</html>
