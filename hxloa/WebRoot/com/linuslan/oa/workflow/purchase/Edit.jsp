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
    
    <title>编辑采购申请</title>
    
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
   	<form id="editPurchaseForm" class="form-horizontal" action="" method="POST">
   		<input id="editPurchaseId" class="purchaseId" name="purchase.id" type="hidden" value="${purchase.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#purchase_edit" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#purchaseAuditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="purchase_edit">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select id="purchaseCompany_edit" class="select2" name="purchase.companyId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${companys }" var="company">
										<option value="${company.id }" ${purchase.companyId == company.id ? "selected='selected'" : "" }>${company.name }</option>
									</c:forEach>
								</select>
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">采购编号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="purchase.serialNo" type="text" value="${purchase.serialNo }" readonly="readonly" class="form-control showText" id="text">
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">供货商：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select class="select2" name="purchase.supplierId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${suppliers }" var="supplier">
										<option value="${supplier.id }" ${supplier.id == purchase.supplierId ? "selected='selected'" : "" }>${supplier.name }</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">采购时间：</label>
								<div class="col-md-2 col-sm-8 no-padding">
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input value="<fmt:formatDate value="${purchase.purchaseDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" name="purchase.purchaseDate" type="text" class="form-control pull-right date">
									</div>
								</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">总价：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<input name="purchase.totalAmount" type="hidden" value="0">
								<span class="purchase_total_amount_edit">0</span>
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label"></label>
							<div class="col-md-2 col-sm-8 no-padding">
								
							</div>
						</div>
			    	</div>
			    	<div class="toolbar with-border">
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增采购项目" id="addPurchaseContent_edit"><i class="fa fa-fw fa-plus"></i>新增</button>
					</div>
			    	<div class="box-body">
			    		<table id="purchaseContentDatagrid_edit"></table>
			    	</div>
			    </div>
			   	<div class="tab-pane" id="purchaseAuditorLogs">
   					<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="editPurchaseAuditorlogsDatagrid"></table>
	   							<div id="editPurchaseAuditorlogsDatagridpager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			purchaseLastSel = null;
   			$("#editPurchaseForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#editPurchaseForm").find("select[name='purchase.customerId']").change(function() {
   				var $option = $(this).find("option:selected");
   				var telephone = $option.attr("telephone");
   				var address = $option.attr("address");
   				var receiver = $option.attr("receiver");
   				var receiverPhone = $option.attr("receiverPhone");
   				$("#editPurchaseForm").find("input.telephone").val(telephone);
   				$("#editPurchaseForm").find("input.address").val(address);
   				$("#editPurchaseForm").find("input.receiver").val(receiver);
   				$("#editPurchaseForm").find("input.receiverPhone").val(receiverPhone);
   			});
   			$("#purchaseContentDatagrid_edit").on("blur", "input.count", function() {
   				var rowid = $("#purchaseContentDatagrid_edit").getGridParam("selrow");
				var $tr = $("#purchaseContentDatagrid_edit").find("tr[id='"+rowid+"']");
				var price = $tr.find("input[name=price]").val();
				var quantity = $tr.find("input[name=quantity]").val();
				var $totalPrice = $tr.find("td[aria-describedby=purchaseContentDatagrid_edit_totalPrice]");
				var loss = $tr.find("input[name=loss]").val();
				var $remainder = $tr.find("td[aria-describedby=purchaseContentDatagrid_edit_remainder]");
				var totalPrice = price*quantity;
				totalPrice = totalPrice.toFixed(2);
				var remainder = quantity - loss;
				remainder = remainder.toFixed(2);
				$totalPrice.html(totalPrice);
				$remainder.html(remainder);
   			});
   			
   			$("#editPurchaseForm").find(".select2").select2();
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generatePurchaseContentGrid_edit()", 0);
            
            $("#addPurchaseContent_edit").click(function() {
            	if(purchaseLastSel){
               		jQuery("#purchaseContentDatagrid_edit").saveRow(purchaseLastSel, false, "clientArray");
               	}
            	var rowId = Math.random();
            	$("#purchaseContentDatagrid_edit").addRowData(rowId, {
            		id: "",
            		article: "",
            		format: "",
            		unit: "",
            		quantity: "",
            		price: "",
            		totalPrice: "",
            		memo: "",
            		memo2: "",
            		orderNo: "0"
            	});
            });
   		});
   		var rowDatas;
   		function generatePurchaseContentGrid_edit() {
   			var id = $("#editPurchaseForm").find("input.purchaseId").val();
   			$("#purchaseContentDatagrid_edit").jqGrid({
   				url: getRoot() + "workflow/purchase/queryContentsByPurchaseId.action?purchase.id="+id,
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "purchaseClassName": "d", "purchaseClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
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
                	label: "备注", name: "memo", width: 300, align: "center", editable: true, edittype: "textarea", editoptions: {rows:"2",width:"100%"}
                }, {
                	label: "备注2", name: "memo2", width: 300, align: "center", editable: true, edittype: "textarea", editoptions: {rows:"2",width:"100%"}
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
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delAddPurchaseContent(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                loadComplete: function(data) {
                	rowDatas = data;
                },
                onSelectRow: function(id){
                	if(id && id!==purchaseLastSel){
                		jQuery("#purchaseContentDatagrid_edit").saveRow(purchaseLastSel, false, "clientArray");
                		var rowData = $("#purchaseContentDatagrid_edit").getRowData(purchaseLastSel);
                		jQuery("#purchaseContentDatagrid_edit").jqGrid("editRow", id);
                		purchaseLastSel=id;
                	}
			    },
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20
            });
   		}
   		
   		function generateEditPurchaseLogGrid() {
   			$("#editPurchaseAuditorlogsDatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=purchase&wfId="+$("#editPurchaseId").val(),
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
                pager: "#editPurchaseAuditorlogsDatagridpager"
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
			var cols = $("#purchaseContentDatagrid_edit").getGridParam("colModel");
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
		
		function delEditPurchaseContent(id, rowId) {
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
		   						url: getRoot() + "workflow/purchase/delContentById.action",
			    				data: "purchaseContent.id="+id,
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
			    						$("#purchaseContentDatagrid_edit").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#purchaseContentDatagrid_edit").delRowData(rowId);
		            	}
	   				}
	            }
   			});
		}
		
		/**
		 * 得到总价
		 */
		function getPurchaseTotalAmount_edit() {
			var rows = $("#purchaseContentDatagrid_edit").getRowData();
			var totalAmount = 0;
			if(rows && rows.length > 0) {
				for(var i = 0; i < rows.length; i ++) {
					var row = rows[i];
					var price = 0;
					var quantity = 0;
					for(var colName in row) {
						if(colName == "price") {
							price = row[colName];
						}
						if(quantity == "quantity") {
							quantity = row[colName];
						}
					}
					totalPrice = price * quantity;
					totalAmount = totalAmount + totalPrice;
				}
			}
			$("#editPurchaseForm").find("input[name='purchase.totalAmount']").val(totalAmount);
			$("#editPurchaseForm").find("span.purchase_total_amount_edit").html(totalAmount);
		}
   	</script>
  </body>
</html>
