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
    
    <title>编辑出库申请</title>
    
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
   	<form id="editCheckoutForm" class="form-horizontal" action="<%=basePath %>workflow/checkout/update.action" method="POST" enctype="multipart/form-data">
   		<input id="editCheckoutId" class="checkoutId" name="checkout.id" type="hidden" value="${checkout.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#checkout_edit" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#checkoutAuditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="checkout_edit">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select id="checkoutCompany_edit" class="select2" name="checkout.companyId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${companys }" var="company">
										<option value="${company.id }" ${checkout.companyId == company.id ? "selected='selected'" : "" }>${company.name }</option>
									</c:forEach>
								</select>
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">出库编号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="checkout.serialNo" type="text" value="${checkout.serialNo }" readonly="readonly" class="form-control showText" id="text">
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">出库仓库：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select class="select2" name="checkout.warehouseId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${warehouses }" var="warehouse">
										<option value="${warehouse.id }" ${checkout.warehouseId == warehouse.id ? "selected='selected'" : "" }>${warehouse.name }</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">出库类型：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select class="select2" name="checkout.checkoutTypeId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${dictionaries }" var="dictionary">
										<option value="${dictionary.id }" ${checkout.checkoutTypeId == dictionary.id ? "selected='selected'" : "" }>${dictionary.text }</option>
									</c:forEach>
								</select>
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">采购单号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="checkout.saleSerialNo" type="text" value="${checkout.saleSerialNo }" class="form-control showText" id="text">
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">出库时间：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input value="<fmt:formatDate value="${checkout.checkoutDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" name="checkout.checkoutDate" type="text" class="form-control pull-right date">
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="moneyDate" class="col-md-2-sm col-sm-4 control-label">客户名称：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select id="editCheckoutCustomer" class="select2" name="checkout.customerId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${customers }" var="customer">
										<option ${checkout.customerId == customer.id ? "selected='selected'" : "" }
										value="${customer.id }" telephone="${customer.telephone }"
										address="${customer.address }">${customer.name }</option>
									</c:forEach>
								</select>
							</div>
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">联系电话：</label>
							<div class="col-md-2 col-sm-8 telephone no-padding" >
								<input value="${checkout.telephone }" name="checkout.telephone" type="text" class="form-control telephone">
							</div>
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">收货人：</label>
							<div class="col-md-2 col-sm-8 telephone no-padding" >
								<input name="checkout.receiver" type="text" value="${checkout.receiver }" class="form-control receiver">
							</div>
						</div>
						<div class="form-group padding-bottom5">
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">收货人联系电话：</label>
							<div class="col-md-2 col-sm-8 telephone no-padding" >
								<input name="checkout.receiverPhone" type="text" value="${checkout.receiverPhone }" class="form-control receiver">
							</div>
							<label for="orderNo" class="col-md-2-sm col-sm-4 control-label">单位地址：</label>
							<div class="col-md-7-sm col-sm-8 address no-padding">
								<input value="${checkout.address }" name="checkout.address" type="text" class="form-control address">
							</div>
						</div>
			    	</div>
			    	<div class="toolbar with-border">
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增出库项目" id="addCheckoutContent_edit"><i class="fa fa-fw fa-plus"></i>新增</button>
					</div>
			    	<div class="box-body">
			    		<table id="checkoutContentDatagrid_edit"></table>
			    	</div>
			    </div>
			   	<div class="tab-pane" id="checkoutAuditorLogs">
   					<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="editCheckoutAuditorlogsDatagrid"></table>
	   							<div id="editCheckoutAuditorlogsDatagridpager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			checkoutLastSel = null;
   			$("#editCheckoutForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#editCheckoutForm").find("select[name='checkout.customerId']").change(function() {
   				var $option = $(this).find("option:selected");
   				var telephone = $option.attr("telephone");
   				var address = $option.attr("address");
   				var receiver = $option.attr("receiver");
   				var receiverPhone = $option.attr("receiverPhone");
   				$("#editCheckoutForm").find("input.telephone").val(telephone);
   				$("#editCheckoutForm").find("input.address").val(address);
   				$("#editCheckoutForm").find("input.receiver").val(receiver);
   				$("#editCheckoutForm").find("input.receiverPhone").val(receiverPhone);
   			});
   			$("#checkoutContentDatagrid_edit").on("blur", "input.count", function() {
   				var rowid = $("#checkoutContentDatagrid_edit").getGridParam("selrow");
				var $tr = $("#checkoutContentDatagrid_edit").find("tr[id='"+rowid+"']");
				var price = $tr.find("input[name=price]").val();
				var quantity = $tr.find("input[name=quantity]").val();
				var $totalPrice = $tr.find("td[aria-describedby=checkoutContentDatagrid_edit_totalPrice]");
				var loss = $tr.find("input[name=loss]").val();
				var $remainder = $tr.find("td[aria-describedby=checkoutContentDatagrid_edit_remainder]");
				var totalPrice = price*quantity;
				totalPrice = totalPrice.toFixed(2);
				var remainder = quantity - loss;
				remainder = remainder.toFixed(2);
				$totalPrice.html(totalPrice);
				$remainder.html(remainder);
   			});
   			
   			$("#editCheckoutForm").find(".select2").select2();
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateCheckoutContentGrid_edit()", 0);
            
            $("#addCheckoutContent_edit").click(function() {
            	if(checkoutLastSel){
               		jQuery("#checkoutContentDatagrid_edit").saveRow(checkoutLastSel, false, "clientArray");
               	}
            	var checkoutCompanyId = $("#checkoutCompany_edit").val();
				if(!checkoutCompanyId) {
					BootstrapDialog.danger("请先选择归属公司");
					return false;
				}
				var customerId = $("#editCheckoutCustomer").val();
				if(!customerId) {
					BootstrapDialog.danger("请先选择客户");
					return false;
				}
            	var rowId = Math.random();
            	$("#checkoutContentDatagrid_edit").addRowData(rowId, {
            		id: "",
            		serialNo: "",
            		checkoutArticleId: "",
            		formatId: "",
            		unit: "",
            		quantity: "",
            		price: "",
            		totalPrice: "",
            		memo: "",
            		orderNo: "0"
            	});
            });
   		});
   		var rowDatas;
   		function generateCheckoutContentGrid_edit() {
   			var id = $("#editCheckoutForm").find("input.checkoutId").val();
   			$("#checkoutContentDatagrid_edit").jqGrid({
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
                	label: "商品名称", name: "checkoutArticleId", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/article/queryCheckoutArticles.action",
                		name: "checkoutArticleId",
                		custom_element: createCheckoutArticleSelect2_edit,
                		custom_value: operateCheckoutArticleSelect2Value_edit
					},
					formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!checkoutSelectedArticle_edit[rowId]) {
               				checkoutSelectedArticle_edit[rowId] = {id: rowObject.checkoutArticleId, name: rowObject.checkinArticleName}
               			}
               			text = checkoutSelectedArticle_edit[rowId].name ? checkoutSelectedArticle_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return checkoutSelectedArticle_edit[rowId].id;
                	}
                }, {
                	label: "规格", name: "formatId", width: 150, align: "center", editable: true, edittype: "custom",
					editoptions: {
						url: "workflow/article/queryFormatsByArticleId.action",
                		name: "formatId",
                		id: "checkoutFormatId_edit",
                		custom_element: createCheckoutFormatSelect2_edit,
                		custom_value: operateCheckoutFormatSelect2Value_edit
					},
					formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!checkoutSelectedFormat_edit[rowId]) {
               				checkoutSelectedFormat_edit[rowId] = {id: rowObject.formatId, name: rowObject.formatName}
               			}
               			text = checkoutSelectedFormat_edit[rowId].name ? checkoutSelectedFormat_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return checkoutSelectedFormat_edit[rowId].id;
                	}
                }, {
                	label: "单位", name: "unit", width: 150, align: "center", editable: false
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
                	label: "金额", name: "totalPrice", width: 150, align: "center", editable: false
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
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delAddCheckoutContent(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                loadComplete: function(data) {
                	rowDatas = data;
                },
                beforeSelectRow: function(rowid) {
                	var companyId = $("#checkoutCompany_edit").val();
                	var customerId = $("#editCheckoutCustomer").val();
                	if(companyId && customerId) {
                		return true;
                	} else {
                		return false;
                	}
                },
                onSelectRow: function(id){
                	if(id && id!==checkoutLastSel){
                		jQuery("#checkoutContentDatagrid_edit").saveRow(checkoutLastSel, false, "clientArray");
                		var rowData = $("#checkoutContentDatagrid_edit").getRowData(checkoutLastSel);
                		jQuery("#checkoutContentDatagrid_edit").jqGrid("editRow", id, {
                			oneditfunc: function(id) {
                				var $tr = $("#checkoutContentDatagrid_edit").find("tr[id='"+id+"']");
                				$tr.find("select").select2();
                				$tr.find("select[name=checkoutArticleId]").change(function() {
									var rowid = $("#checkoutContentDatagrid_edit").getGridParam("selrow");
									var $tr = $("#checkoutContentDatagrid_edit").find("tr[id='"+rowid+"']");
									var $formatSelect = $tr.find("select[name=formatId]");
									var $option = $(this).find("option:selected");
									var articleId = $option.attr("checkinArticleId");
									var unit = $option.attr("unit");
									var serialNo = $option.attr("serialNo");
									getCheckoutFormatOptions_edit($formatSelect, articleId);
									$tr.find("td[aria-describedby=checkoutContentDatagrid_edit_unit]").html(unit);
									$tr.find("td[aria-describedby=checkoutContentDatagrid_edit_serialNo]").html(serialNo);
								});
                			}
                		});
                		checkoutLastSel=id;
                	}
			    },
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20
            });
   		}
   		
   		function generateEditCheckoutLogGrid() {
   			$("#editCheckoutAuditorlogsDatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=checkout&wfId="+$("#editCheckoutId").val(),
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
                pager: "#editCheckoutAuditorlogsDatagridpager"
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
		 * 通过配置的name获取列内容
		 */
		function getColModelByName(name) {
			var cols = $("#checkoutContentDatagrid_edit").getGridParam("colModel");
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
		
		function delEditCheckoutContent(id, rowId) {
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
		   						url: getRoot() + "workflow/checkout/delContentById.action",
			    				data: "checkoutContent.id="+id,
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
			    						$("#checkoutContentDatagrid_edit").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#checkoutContentDatagrid_edit").delRowData(rowId);
		            	}
	   				}
	            }
   			});
		}
		
		/**
		 * 创建出库类别选择框
		 */
		function createCheckoutArticleSelect2_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#checkoutContentDatagrid_edit").getGridParam("selrow");
			
			//得到选中的值
			var selectedVal;
			if(checkoutSelectedArticle_edit[rowid]) {
				selectedVal = checkoutSelectedArticle_edit[rowid].id;
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
			var companyId = $("#checkoutCompany_edit").val();
			var customerId = $("#editCheckoutCustomer").val();
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
		var checkoutSelectedArticle_edit = new Object();
		function operateCheckoutArticleSelect2Value_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var articleId = $("select", elem).find("option:selected").attr("checkinArticleId");
				var rowid = $("#checkoutContentDatagrid_edit").getGridParam("selrow");
				checkoutSelectedArticle_edit[checkoutLastSel] = {id: val, name: text, checkinArticleId: articleId};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		/**
		 * 创建出库类别选择框
		 */
		function createCheckoutFormatSelect2_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#checkoutContentDatagrid_edit").getGridParam("selrow");
			//得到选中的值
			var selectedVal;
			if(checkoutSelectedFormat_edit[rowid]) {
				selectedVal = checkoutSelectedFormat_edit[rowid].id;
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
			var article = checkoutSelectedArticle_edit[rowid];
			$(selectEl).select2();
			if(article && article.id && ""!=$.trim(article.id)) {
				getCheckoutFormatOptions_edit($(selectEl), article.checkinArticleId);
			}
			
			return div;
		}
		//存放选中的出库类别的id
		var checkoutSelectedFormat_edit = new Object();
		function operateCheckoutFormatSelect2Value_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#checkoutContentDatagrid_edit").getGridParam("selrow");
				checkoutSelectedFormat_edit[checkoutLastSel] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function getCheckoutFormatOptions_edit($select, articleId) {
			//$select.html("");
			var rowid = $("#checkoutContentDatagrid_edit").getGridParam("selrow");
			try {
				$select.select2("val", "");
			} catch(ex) {
				
			}
			$select.empty();
			if(articleId && "" != $.trim(articleId)) {
				var option = document.createElement("option");
				option.value="";
				option.text="请选择";
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
								selectVal = checkoutSelectedFormat_edit[rowid]
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
