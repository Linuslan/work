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
    
    <title>新增采购申请</title>
    
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
   	<form id="addPurchaseReqForm" class="form-horizontal" action="" method="POST">
    	<div class="box-body padding-bottom5 bottom-dotted-border" >
    		<div class="form-group">
				<label for="text" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<select id="purchaseReqCompany_add" class="select2" name="purchaseReq.companyId" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${companys }" var="company">
							<option value="${company.id }">${company.name }</option>
						</c:forEach>
					</select>
				</div>
				<label for="text" class="col-md-2-sm col-sm-4 control-label">采购编号：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="purchaseReq.serialNo" type="text" value="${serialNo }" readonly="readonly" class="form-control showText" id="text">
				</div>
				<label for="text" class="col-md-2-sm col-sm-4 control-label">采购时间：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input readonly="readonly" name="purchaseReq.purchaseDate" type="text" class="form-control pull-right date">
					</div>
				</div>
			</div>
    	</div>
    	<div class="toolbar with-border">
			<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增采购项目" id="addPurchaseReqContent_add"><i class="fa fa-fw fa-plus"></i>新增</button>
		</div>
    	<div class="box-body">
    		<table id="purchaseReqContentDatagrid_add"></table>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addPurchaseReqForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#purchaseReqContentDatagrid_add").on("blur", "input.count", function() {
   				var rowid = $("#purchaseReqContentDatagrid_add").getGridParam("selrow");
				var $tr = $("#purchaseReqContentDatagrid_add").find("tr[id='"+rowid+"']");
				var price = $tr.find("input[name=price]").val();
				var quantity = $tr.find("input[name=quantity]").val();
				var $totalPrice = $tr.find("td[aria-describedby=purchaseReqContentDatagrid_add_totalPrice]");
				var loss = $tr.find("input[name=loss]").val();
				var $remainder = $tr.find("td[aria-describedby=purchaseReqContentDatagrid_add_remainder]");
				var totalPrice = price*quantity;
				totalPrice = totalPrice.toFixed(2);
				var remainder = quantity - loss;
				remainder = remainder.toFixed(2);
				$totalPrice.html(totalPrice);
				$remainder.html(remainder);
   			});
   			
   			$("#addPurchaseReqForm").find(".select2").select2();
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generatePurchaseReqContentGrid_add()", 0);
            
            $("#addPurchaseReqContent_add").click(function() {
            	if(purchaseReqLastSel){
               		jQuery("#purchaseReqContentDatagrid_add").saveRow(purchaseReqLastSel, false, "clientArray");
               	}
            	var rowId = Math.random();
            	$("#purchaseReqContentDatagrid_add").addRowData(rowId, {
            		id: "",
            		article: "",
            		format: "",
            		unit: "",
            		quantity: "",
            		price: "",
            		totalPrice: "",
            		arriveDate: "",
            		memo: "",
            		orderNo: "0"
            	});
            });
   		});
   		var rowDatas;
   		function generatePurchaseReqContentGrid_add() {
   			$("#purchaseReqContentDatagrid_add").jqGrid({
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
                	label: "商品名称", name: "article", editable: true, edittype: "text"
                }, {
                	label: "规格", name: "format", width: 150, align: "center", editable: true, edittype: "text"
                }, {
                	label: "单位", name: "unit", width: 150, align: "center", editable: true, edittype: "text"
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
                	label: "到货日期", name: "arriveDate", width: 150, align: "center", editable: true,
                	edittype: "custom",
                	editoptions: {
                		custom_element: createDateBox,
                		custom_value:operateDateValue,
                		name: "arriveDate"
                	}
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
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delAddPurchaseReqContent(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                loadComplete: function(data) {
                	rowDatas = data;
                },
                onSelectRow: function(id){
                	if(id && id!==purchaseReqLastSel){
                		jQuery("#purchaseReqContentDatagrid_add").saveRow(purchaseReqLastSel, false, "clientArray");
                		jQuery("#purchaseReqContentDatagrid_add").jqGrid("editRow", id);
                		purchaseReqLastSel=id;
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
			var cols = $("#purchaseReqContentDatagrid_add").getGridParam("colModel");
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
		
		function delAddPurchaseReqContent(id, rowId) {
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
		   						url: getRoot() + "workflow/purchaseReq/delContentById.action",
			    				data: "purchaseReqContent.id="+id,
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
			    						$("#purchaseReqContentDatagrid_add").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#purchaseReqContentDatagrid_add").delRowData(rowId);
		            	}
	   				}
	            }
   			});
		}
		
		/**
   		 * 创建日期选择框
   		 */
		function createDateBox (value, options) {
			var name = options.name
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
			inputEl.setAttribute("name", name);
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
   	</script>
  </body>
</html>
