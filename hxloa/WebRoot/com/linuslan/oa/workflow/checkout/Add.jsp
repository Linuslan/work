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
    
    <title>新增出库申请</title>
    
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
   	<form id="addCheckoutForm" class="form-horizontal" action="<%=basePath %>workflow/checkout/add.action" method="POST" enctype="multipart/form-data">
    	<div class="box-body padding-bottom5 bottom-dotted-border" >
    		<div class="form-group">
				<label for="text" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<select id="checkoutCompany_add" class="select2" name="checkout.companyId" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${companys }" var="company">
							<option value="${company.id }">${company.name }</option>
						</c:forEach>
					</select>
				</div>
				<label for="text" class="col-md-2-sm col-sm-4 control-label">出库编号：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="checkout.serialNo" type="text" value="${serialNo }" readonly="readonly" class="form-control showText" id="text">
				</div>
				<label for="text" class="col-md-2-sm col-sm-4 control-label">出库仓库：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<select class="select2" name="checkout.warehouseId" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${warehouses }" var="warehouse">
							<option value="${warehouse.id }">${warehouse.name }</option>
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
							<option value="${dictionary.id }">${dictionary.text }</option>
						</c:forEach>
					</select>
				</div>
				<label for="text" class="col-md-2-sm col-sm-4 control-label">销售单号：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="checkout.saleSerialNo" type="text" value="${checkout.saleSerialNo }" class="form-control showText" id="text">
				</div>
				<label for="text" class="col-md-2-sm col-sm-4 control-label">出库时间：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input readonly="readonly" name="checkout.checkoutDate" type="text" class="form-control pull-right date">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="moneyDate" class="col-md-2-sm col-sm-4 control-label">客户名称：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<select id="addCheckoutCustomer" class="select2" name="checkout.customerId" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${customers }" var="customer">
							<option value="${customer.id }" telephone="${customer.telephone }"
							address="${customer.address }" receiver="${customer.receiver }"
							receiverPhone="${customer.receiverPhone }">${customer.name }</option>
						</c:forEach>
					</select>
				</div>
				<label for="groupId" class="col-md-2-sm col-sm-4 control-label">联系电话：</label>
				<div class="col-md-2 col-sm-8 telephone no-padding" >
					<input name="checkout.telephone" type="text" class="form-control telephone">
				</div>
				<label for="groupId" class="col-md-2-sm col-sm-4 control-label">收货人：</label>
				<div class="col-md-2 col-sm-8 telephone no-padding" >
					<input name="checkout.receiver" type="text" class="form-control receiver">
				</div>
			</div>
			<div class="form-group padding-bottom5">
				<label for="groupId" class="col-md-2-sm col-sm-4 control-label">收货人联系电话：</label>
				<div class="col-md-2 col-sm-8 telephone no-padding" >
					<input name="checkout.receiverPhone" type="text" class="form-control receiverPhone">
				</div>
				<label for="orderNo" class="col-md-2-sm col-sm-4 control-label">单位地址：</label>
				<div class="col-md-7-sm col-sm-8 address no-padding">
					<input name="checkout.address" type="text" class="form-control address">
				</div>
			</div>
    	</div>
    	<div class="toolbar with-border">
			<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增出库项目" id="addCheckoutContent_add"><i class="fa fa-fw fa-plus"></i>新增</button>
		</div>
    	<div class="box-body">
    		<table id="checkoutContentDatagrid_add"></table>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addCheckoutForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#addCheckoutForm").find("select[name='checkout.customerId']").change(function() {
   				var $option = $(this).find("option:selected");
   				var telephone = $option.attr("telephone");
   				var address = $option.attr("address");
   				var receiver = $option.attr("receiver");
   				var receiverPhone = $option.attr("receiverPhone");
   				alert("telephone: "+telephone+", receiver:"+receiver+", receiverPhone: "+receiverPhone);
   				$("#addCheckoutForm").find("input.telephone").val(telephone);
   				$("#addCheckoutForm").find("input.address").val(address);
   				$("#addCheckoutForm").find("input.receiver").val(receiver);
   				$("#addCheckoutForm").find("input.receiverPhone").val(receiverPhone);
   			});
   			$("#checkoutContentDatagrid_add").on("blur", "input.count", function() {
   				var rowid = $("#checkoutContentDatagrid_add").getGridParam("selrow");
				var $tr = $("#checkoutContentDatagrid_add").find("tr[id='"+rowid+"']");
				var price = $tr.find("input[name=price]").val();
				var quantity = $tr.find("input[name=quantity]").val();
				var $totalPrice = $tr.find("td[aria-describedby=checkoutContentDatagrid_add_totalPrice]");
				var loss = $tr.find("input[name=loss]").val();
				var $remainder = $tr.find("td[aria-describedby=checkoutContentDatagrid_add_remainder]");
				var totalPrice = price*quantity;
				totalPrice = totalPrice.toFixed(2);
				var remainder = quantity - loss;
				remainder = remainder.toFixed(2);
				$totalPrice.html(totalPrice);
				$remainder.html(remainder);
   			});
   			
   			$("#addCheckoutForm").find(".select2").select2();
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateCheckoutContentGrid_add()", 0);
            
            $("#addCheckoutContent_add").click(function() {
            	if(checkoutLastSel){
               		jQuery("#checkoutContentDatagrid_add").saveRow(checkoutLastSel, false, "clientArray");
               	}
            	var checkoutCompanyId = $("#checkoutCompany_add").val();
				if(!checkoutCompanyId) {
					BootstrapDialog.danger("请先选择归属公司");
					return false;
				}
				var customerId = $("#addCheckoutCustomer").val();
				if(!customerId) {
					BootstrapDialog.danger("请先选择客户");
					return false;
				}
            	var rowId = Math.random();
            	$("#checkoutContentDatagrid_add").addRowData(rowId, {
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
   		function generateCheckoutContentGrid_add() {
   			$("#checkoutContentDatagrid_add").jqGrid({
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
                	label: "商品名称", name: "checkoutArticleId", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/article/queryCheckoutArticles.action",
                		name: "checkoutArticleId",
                		custom_element: createCheckoutArticleSelect2_add,
                		custom_value: operateCheckoutArticleSelect2Value_add
					},
					formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!checkoutSelectedArticle_add[rowId]) {
               				checkoutSelectedArticle_add[rowId] = {id: rowObject.checkoutArticleId, name: rowObject.checkinArticleName}
               			}
               			text = checkoutSelectedArticle_add[rowId].name ? checkoutSelectedArticle_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return checkoutSelectedArticle_add[rowId].id;
                	}
                }, {
                	label: "规格", name: "formatId", width: 150, align: "center", editable: true, edittype: "custom",
					editoptions: {
						url: "workflow/article/queryFormatsByArticleId.action",
                		name: "formatId",
                		id: "checkoutFormatId_add",
                		custom_element: createCheckoutFormatSelect2_add,
                		custom_value: operateCheckoutFormatSelect2Value_add
					},
					formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!checkoutSelectedFormat_add[rowId]) {
               				checkoutSelectedFormat_add[rowId] = {id: rowObject.formatId, name: rowObject.formatName}
               			}
               			text = checkoutSelectedFormat_add[rowId].name ? checkoutSelectedFormat_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return checkoutSelectedFormat_add[rowId].id;
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
                	var companyId = $("#checkoutCompany_add").val();
                	var customerId = $("#addCheckoutCustomer").val();
                	if(companyId && customerId) {
                		return true;
                	} else {
                		return false;
                	}
                },
                onSelectRow: function(id){
                	if(id && id!==checkoutLastSel){
                		jQuery("#checkoutContentDatagrid_add").saveRow(checkoutLastSel, false, "clientArray");
                		var rowData = $("#checkoutContentDatagrid_add").getRowData(checkoutLastSel);
                		jQuery("#checkoutContentDatagrid_add").jqGrid("editRow", id, {
                			oneditfunc: function(id) {
                				var $tr = $("#checkoutContentDatagrid_add").find("tr[id='"+id+"']");
                				$tr.find("select").select2();
                				
                				$tr.find("select[name=checkoutArticleId]").change(function() {
									var rowid = $("#checkoutContentDatagrid_add").getGridParam("selrow");
									var $tr = $("#checkoutContentDatagrid_add").find("tr[id='"+rowid+"']");
									var $formatSelect = $tr.find("select[name=formatId]");
									var $option = $(this).find("option:selected");
									var articleId = $option.attr("checkinArticleId");
									var unit = $option.attr("unit");
									var serialNo = $option.attr("serialNo");
									getCheckoutFormatOptions_add($formatSelect, articleId);
									$tr.find("td[aria-describedby=checkoutContentDatagrid_add_unit]").html(unit);
									$tr.find("td[aria-describedby=checkoutContentDatagrid_add_serialNo]").html(serialNo);
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
			var cols = $("#checkoutContentDatagrid_add").getGridParam("colModel");
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
		
		function delAddCheckoutContent(id, rowId) {
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
			    						$("#checkoutContentDatagrid_add").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#checkoutContentDatagrid_add").delRowData(rowId);
		            	}
	   				}
	            }
   			});
		}
		
		/**
		 * 创建报销类别选择框
		 */
		function createCheckoutArticleSelect2_add(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#checkoutContentDatagrid_add").getGridParam("selrow");
			
			//得到选中的值
			var selectedVal;
			if(checkoutSelectedArticle_add[rowid]) {
				selectedVal = checkoutSelectedArticle_add[rowid].id;
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
			var companyId = $("#checkoutCompany_add").val();
			var customerId = $("#addCheckoutCustomer").val();
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
		var checkoutSelectedArticle_add = new Object();
		function operateCheckoutArticleSelect2Value_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var articleId = $("select", elem).find("option:selected").attr("checkinArticleId");
				var rowid = $("#checkoutContentDatagrid_add").getGridParam("selrow");
				checkoutSelectedArticle_add[checkoutLastSel] = {id: val, name: text, checkinArticleId: articleId};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		/**
		 * 创建报销类别选择框
		 */
		function createCheckoutFormatSelect2_add(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#checkoutContentDatagrid_add").getGridParam("selrow");
			//得到选中的值
			var selectedVal;
			if(checkoutSelectedFormat_add[rowid]) {
				selectedVal = checkoutSelectedFormat_add[rowid].id;
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
			var article = checkoutSelectedArticle_add[rowid];
			$(selectEl).select2();
			if(article && article.id && ""!=$.trim(article.id)) {
				getCheckoutFormatOptions_add($(selectEl), article.checkinArticleId);
			}
			
			return div;
		}
		//存放选中的报销类别的id
		var checkoutSelectedFormat_add = new Object();
		function operateCheckoutFormatSelect2Value_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#checkoutContentDatagrid_add").getGridParam("selrow");
				checkoutSelectedFormat_add[checkoutLastSel] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function getCheckoutFormatOptions_add($select, articleId) {
			//$select.html("");
			var rowid = $("#checkoutContentDatagrid_add").getGridParam("selrow");
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
								selectVal = checkoutSelectedFormat_add[rowid]
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
