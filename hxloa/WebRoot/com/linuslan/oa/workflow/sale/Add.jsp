<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新增销售订单申请</title>
    
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
   	<form id="addSaleForm" action="" class="form-horizontal">
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#sale_add" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#sale_invoice_add" data-toggle="tab">开票信息</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="sale_add">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select id="saleCompany_add" class="select2" name="sale.companyId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${companys }" var="company">
										<option value="${company.id }">${company.name }</option>
									</c:forEach>
								</select>
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">编号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.serialNo" type="text" value="${serialNo }" readonly="readonly" class="form-control showText" id="text">
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">订单编号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.orderSerialNo" type="text" value="" class="form-control showText" id="text">
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">订单日期：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="sale.saleDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
								</div>
							</div>
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">归属客户：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select id="saleCustomer_add" class="select2" name="sale.customerId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${customers }" var="customer">
										<option value="${customer.id }" telephone="${customer.telephone }"
										address="${customer.address }" zipCode="${customer.zipCode }"
    									receiver="${customer.receiver }" receiverPhone="${customer.receiverPhone }"
    									receiverAddress="${customer.receiverAddress }">${customer.name }</option>
									</c:forEach>
								</select>
							</div>
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">联系电话：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.telephone" type="text" value="" class="form-control telephone" id="text">
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">收货人：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.receiver" type="text" value="" class="form-control receiver" id="text">
							</div>
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">手机：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.receiverPhone" type="text" value="" class="form-control receiverPhone" id="text">
							</div>
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">邮编：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.zipCode" type="text" value="" class="form-control zipCode" id="text">
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">收货地址：</label>
							<div class="col-md-7-sm col-sm-8 no-padding">
								<input name="sale.receiverAddress" type="text" value="" class="form-control" id="text">
							</div>
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">是否开票：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<input name="sale.isInvoice" type="radio" value="0" class="minimal" checked />&nbsp;否&nbsp;&nbsp;&nbsp;&nbsp;
								<input name="sale.isInvoice" type="radio" value="1" class="minimal"/>&nbsp;是
							</div>
						</div>
			    	</div>
			    	<div class="toolbar with-border">
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增销售订单项目" id="addSaleContent_add"><i class="fa fa-fw fa-plus"></i>新增</button>
					</div>
			    	<div class="box-body" style="overflow-x: auto;">
			    		<table id="saleContentDatagrid_add"></table>
			    	</div>
			    </div>
			    <div class="tab-pane" id="sale_invoice_add">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" style="display:none;" >
			    		<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">开票金额：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.invoiceMoney" type="number" value="0" class="form-control" id="text">
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">应回款：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.supposedMoney" type="number" value="0" class="form-control" id="text">
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">收入归属部门：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<div class="departmentTree"></div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">预计回款时间：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="sale.planRestreamDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
								</div>
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">开票类型：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select class="select2" name="sale.invoiceTypeId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${dictionaries }" var="dictionary">
										<option value="${dictionary.id }">${dictionary.text }</option>
									</c:forEach>
								</select>
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">税号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.taxPayerId" type="text" class="form-control" id="text">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">联系电话：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.invoicePhone" type="text" class="form-control" id="text">
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">开户行：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.invoiceBank" type="text" class="form-control" id="text">
							</div>
							<label class="col-md-2-sm col-sm-4 control-label">账号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="sale.invoiceBankNo" type="text" class="form-control" id="text">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2-sm col-sm-4 control-label">地址：</label>
							<div class="col-md-11-sm col-sm-8 no-padding">
								<input name="sale.invoiceAddress" type="text" class="form-control" id="text">
							</div>
						</div>
			    	</div>
			    </div>
			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addSaleForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#addSaleForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
	        $("#addSaleForm").find("input[type='radio'][name='sale.isInvoice']").on("ifChecked", function() {
	        	var val = $(this).val();
	        	if(val == 0) {
	        		$("#sale_invoice_add").find("div:eq(0)").hide();
	        	} else {
	        		$("#sale_invoice_add").find("div:eq(0)").show();
	        	}
	        });
	        $("#addSaleForm").find(".departmentTree").combotree({
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
   			$("#addSaleForm").find(".select2").select2();
   			$("#addSaleForm").find("select[name='sale.customerId']").change(function() {
   				var $option = $(this).find("option:selected");
   				var telephone = $option.attr("telephone");
   				var receiverAddress = $option.attr("receiverAddress");
   				var receiver = $option.attr("receiver");
   				var receiverPhone = $option.attr("receiverPhone");
   				var zipCode = $option.attr("zipCode");
   				
   				$("#addSaleForm").find("input.telephone").val(telephone);
   				$("#addSaleForm").find("input.receiverAddress").val(receiverAddress);
   				$("#addSaleForm").find("input.receiver").val(receiver);
   				$("#addSaleForm").find("input.receiverPhone").val(receiverPhone);
   				$("#addSaleForm").find("input.zipCode").val(zipCode);
   			});
   			$("#saleContentDatagrid_add").on("blur", "input.count", function() {
   				var rowid = $("#saleContentDatagrid_add").getGridParam("selrow");
				var $tr = $("#saleContentDatagrid_add").find("tr[id='"+rowid+"']");
				var price = $tr.find("input[name=price]").val();
				var quantity = $tr.find("input[name=quantity]").val();
				var $totalPrice = $tr.find("td[aria-describedby=saleContentDatagrid_add_totalAmount]");
				var loss = $tr.find("input[name=loss]").val();
				var $remainder = $tr.find("td[aria-describedby=saleContentDatagrid_add_remainder]");
				var totalPrice = price*quantity;
				totalPrice = totalPrice.toFixed(2);
				var remainder = quantity - loss;
				remainder = remainder.toFixed(2);
				$totalPrice.html(totalPrice);
				$remainder.html(remainder);
   			});
   			
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("buildSaleContentGrid_add()", 0);
            
            $("#addSaleContent_add").click(function() {
            	if(saleLastSel){
               		jQuery("#saleContentDatagrid_add").saveRow(saleLastSel, false, "clientArray");
               	}
            	var saleCompanyId = $("#saleCompany_add").val();
				if(!saleCompanyId) {
					BootstrapDialog.danger("请先选择归属公司");
					return false;
				}
				var customerId = $("#saleCustomer_add").val();
				if(!customerId) {
					BootstrapDialog.danger("请先选择客户");
					return false;
				}
            	var rowId = Math.random();
            	$("#saleContentDatagrid_add").addRowData(rowId, {
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
   		});
   		var rowDatas;
   		function buildSaleContentGrid_add() {
   			$("#saleContentDatagrid_add").jqGrid({
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "local",
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "商品编号", name: "serialNo", editable: false
                }, {
                	label: "商品名称", name: "articleId", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/article/queryCheckoutArticles.action",
                		name: "articleId",
                		custom_element: createSaleArticleSelect2_add,
                		custom_value: operateSaleArticleSelect2Value_add
					},
					formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!saleSelectedArticle_add[rowId]) {
               				saleSelectedArticle_add[rowId] = {id: rowObject.articleId, name: rowObject.articleName}
               			}
               			text = saleSelectedArticle_add[rowId].name ? saleSelectedArticle_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return saleSelectedArticle_add[rowId].id;
                	}
                }, {
                	label: "规格", name: "formatId", width: 150, align: "center", editable: true, edittype: "custom",
					editoptions: {
						url: "workflow/article/queryFormatsByArticleId.action",
                		name: "formatId",
                		custom_element: createSaleFormatSelect2_add,
                		custom_value: operateSaleFormatSelect2Value_add
					},
					formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!saleSelectedFormat_add[rowId]) {
               				saleSelectedFormat_add[rowId] = {id: rowObject.formatId, name: rowObject.formatName}
               			}
               			text = saleSelectedFormat_add[rowId].name ? saleSelectedFormat_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return saleSelectedFormat_add[rowId].id;
                	}
                }, {
                	label: "单位", name: "unit", width: 100, align: "center", editable: false
                }, {
                	label: "出货日期", name: "deliverDate", width: 170, align: "center", editable: true, edittype: "custom",
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
                	label: "总价", name: "totalAmount", width: 150, align: "center", editable: false
                }, {
                	label: "质量要求", name: "quality", width: 300, align: "center", editable: true, edittype: "textarea", editoptions: {rows:"2",width:"100%"}
                }, {
                	label: "备注", name: "memo", width: 300, align: "center", editable: true, edittype: "textarea", editoptions: {rows:"2",width:"100%"}
                }, {
                	label: "排序号", name: "orderNo", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "orderNo"
                	}
                }, {
                	label: "操作", name: "operationCell", align: "center", width: 70, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		var rowId = options.rowId;
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delSaleContent_add(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                loadComplete: function(data) {
                	rowDatas = data;
                },
                beforeSelectRow: function(rowid) {
                	var companyId = $("#saleCompany_add").val();
                	var customerId = $("#saleCustomer_add").val();
                	if(companyId && customerId) {
                		return true;
                	} else {
                		return false;
                	}
                },
                onSelectRow: function(id){
                	if(id && id!==saleLastSel){
                		jQuery("#saleContentDatagrid_add").saveRow(saleLastSel, false, "clientArray");
                		var rowData = $("#saleContentDatagrid_add").getRowData(saleLastSel);
                		jQuery("#saleContentDatagrid_add").jqGrid("editRow", id, {
                			oneditfunc: function(id) {
                				var $tr = $("#saleContentDatagrid_add").find("tr[id='"+id+"']");
                				$tr.find("select").select2();
                				
                				$tr.find("select[name=articleId]").change(function() {
									var rowid = $("#saleContentDatagrid_add").getGridParam("selrow");
									var $tr = $("#saleContentDatagrid_add").find("tr[id='"+rowid+"']");
									var $formatSelect = $tr.find("select[name=formatId]");
									var $option = $(this).find("option:selected");
									var articleId = $option.attr("checkinArticleId");
									var unit = $option.attr("unit");
									var serialNo = $option.attr("serialNo");
									getSaleFormatOptions_add($formatSelect, articleId);
									$tr.find("td[aria-describedby=saleContentDatagrid_add_unit]").html(unit);
									$tr.find("td[aria-describedby=saleContentDatagrid_add_serialNo]").html(serialNo);
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
		 * 通过配置的name获取列内容
		 */
		function getColModelByName(name) {
			var cols = $("#saleContentDatagrid_add").getGridParam("colModel");
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
		
		function delSaleContent_add(id, rowId) {
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
			    						$("#saleContentDatagrid_add").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#saleContentDatagrid_add").delRowData(rowId);
		            	}
	   				}
	            }
   			});
		}
		
		/**
		 * 创建报销类别选择框
		 */
		function createSaleArticleSelect2_add(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#saleContentDatagrid_add").getGridParam("selrow");
			
			//得到选中的值
			var selectedVal;
			if(saleSelectedArticle_add[rowid]) {
				selectedVal = saleSelectedArticle_add[rowid].id;
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
			var companyId = $("#saleCompany_add").val();
			var customerId = $("#saleCustomer_add").val();
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
							if(id == selectedVal) {
								option.selected=true;
							}
							selectEl.appendChild(option);
						}
						$(selectEl).select2();
					},
					error: function() {
						
					},
					complete: function() {
						
					}
				});
			} else {
				$(selectEl).select2();
			}
			
			return div;
		}
		//存放选中的报销类别的id
		var saleSelectedArticle_add = new Object();
		function operateSaleArticleSelect2Value_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var articleId = $("select", elem).find("option:selected").attr("checkinArticleId");
				var rowid = $("#saleContentDatagrid_add").getGridParam("selrow");
				saleSelectedArticle_add[saleLastSel] = {id: val, name: text, checkinArticleId: articleId};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		/**
		 * 创建报销类别选择框
		 */
		function createSaleFormatSelect2_add(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#saleContentDatagrid_add").getGridParam("selrow");
			//得到选中的值
			var selectedVal;
			if(saleSelectedFormat_add[rowid]) {
				selectedVal = saleSelectedFormat_add[rowid].id;
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
			var article = saleSelectedArticle_add[rowid];
			$(selectEl).select2();
			if(article && article.id && ""!=$.trim(article.id)) {
				getSaleFormatOptions_add($(selectEl), article.checkinArticleId);
			}
			
			return div;
		}
		//存放选中的报销类别的id
		var saleSelectedFormat_add = new Object();
		function operateSaleFormatSelect2Value_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#saleContentDatagrid_add").getGridParam("selrow");
				saleSelectedFormat_add[saleLastSel] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function getSaleFormatOptions_add($select, articleId) {
			//$select.html("");
			var rowid = $("#saleContentDatagrid_add").getGridParam("selrow");
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
								selectVal = saleSelectedFormat_add[rowid]
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
