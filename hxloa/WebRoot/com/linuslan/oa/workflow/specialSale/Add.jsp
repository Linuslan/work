<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="UTF-8"%>
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
   	<form id="addSpecialSaleForm" action="" class="form-horizontal">
    	<div class="box-body padding-bottom5 bottom-dotted-border" >
    		<div class="form-group">
				<label for="text" class="col-md-1 col-sm-4 control-label">归属公司：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<select class="select2" name="specialSale.companyId" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${companys }" var="company">
							<option value="${company.id }">${company.name }</option>
						</c:forEach>
					</select>
				</div>
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">是否开票：</label>
				<div class="col-md-2 col-sm-8 left-label">
					<input name="specialSale.isInvoice" type="radio" value="0" class="minimal" checked />&nbsp;否&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="specialSale.isInvoice" type="radio" value="1" class="minimal"/>&nbsp;是
				</div>
			</div>
			<div class="form-group">
				<label for="text" class="col-md-1 col-sm-4 control-label">订单号：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="specialSale.serialNo" type="text" value="${serialNo }" readonly="readonly" class="form-control showText" id="text">
				</div>
				<label for="text" class="col-md-1 col-sm-4 control-label">订单日期：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<% String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); %>
						<input name="specialSale.saleDate" type="text" value="<%= currentDate %>" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
				<label for="text" class="col-md-1 col-sm-4 control-label">出货日期：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="specialSale.checkoutDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
				<label for="groupId" class="col-md-1 col-sm-4 control-label">结款日期：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="specialSale.payDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
				
			</div>
			<div class="form-group">
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">业务员：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="specialSale.salesman" type="text" value="" class="form-control" id="text">
				</div>
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">客户：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<select onchange="addSpecialSaleSpecialCustomerInfo_add(this)" class="select2" name="specialSale.customerId" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${customers }" var="customer">
							<option value="${customer.id }" customer="${customer.name }"
							 customerPhone="${customer.cellphone }" receiver="${customer.receiver }"
							 receiverPhone="${customer.receiverPhone }"
							 receiverAddress="${customer.receiverAddress }">${customer.name }</option>
						</c:forEach>
					</select>
					<input name="specialSale.customer" type="hidden" value="" class="form-control" id="text">
				</div>
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">手机：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="specialSale.customerPhone" type="text" value="" class="form-control" id="text">
				</div>
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">付款方式：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<select class="select2" name="specialSale.payTypeId" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${payTypes }" var="dictionary">
							<option value="${dictionary.id }">${dictionary.text }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">送货方式：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<select class="select2" name="specialSale.deliverTypeId" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${deliverTypes }" var="deliverType">
							<option value="${deliverType.id }">${deliverType.text }</option>
						</c:forEach>
					</select>
				</div>
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">收货人：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<select onchange="addSpecialSaleSpecialCustomerReceiverInfo_add(this)" class="select2" name="specialSale.receiverId" style="width: 100%">
						<option value="">请选择</option>
					</select>
					<input name="specialSale.receiver" type="hidden" value="" class="form-control" id="text">
				</div>
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">手机：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="specialSale.receiverPhone" type="text" value="" class="form-control" id="text">
				</div>
				
			</div>
			<!-- 
			<div class="form-group">
				<label class="col-md-2-sm col-sm-4 control-label">物流：</label>
				<div class="col-md-11-sm col-sm-8 no-padding">
					<input name="specialSale.logistics" type="text" value="" class="form-control" id="text">
				</div>
			</div>
			 -->
			<div class="form-group">
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">收货地址：</label>
				<div class="col-md-11 col-sm-8 no-padding">
					<input name="specialSale.receiverAddress" type="text" value="" class="form-control" id="text">
				</div>
			</div>
			<div class="form-group">
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">定金：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="specialSale.frontMoney" type="number" value="0" class="form-control" id="text">
				</div>
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">总金额：</label>
				<div class="col-md-2 col-sm-8 left-label">
					<label class="totalAmount"></label>
					<input name="specialSale.totalAmount" type="hidden" value="0" class="form-control" id="text">
				</div>
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">成品金额：</label>
				<div class="col-md-2 col-sm-8 left-label">
					<label class="productAmount"></label>
					<input name="specialSale.productAmount" type="hidden" value="0" class="form-control" id="text">
				</div>
				<label for="departmentId" class="col-md-1 col-sm-4 control-label">运费：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="specialSale.freight" type="number" value="0" class="form-control freight" id="text">
				</div>
			</div>
    	</div>
    	<div class="toolbar with-border">
			<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增销售订单项目" id="addSpecialSaleContent_add"><i class="fa fa-fw fa-plus"></i>新增</button>
		</div>
    	<div class="box-body" style="overflow-x: auto;">
    		<table id="specialSaleContentDatagrid_add"></table>
    	</div>
   	</form>
   	<script type="text/javascript">
   		//存放选中的销售订单品名的id和name
		var specialSaleSaleArticleSelected_add = new Object();
		//存放选中的销售订单材质的id和name
   		var specialSaleMaterialSelected_add = new Object();
   		//存放选中的销售订单光泽度的id和name
   		var specialSaleGlossinessSelected_add = new Object();
   		//存放选中的销售订单面数的id和name
   		var specialSaleFaceSelected_add = new Object();
   		//存放选中的销售订单效果的id和name
   		var specialSaleEffectSelected_add = new Object();
   		var specialSaleArticleUnitSelected_add = new Object();
   		//存放选中的销售订单材质规格的id和name
   		var specialSaleMaterialFormatSelected_add = new Object();
   		$(function() {
   			$("#addSpecialSaleForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#addSpecialSaleForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
	        $("#addSpecialSaleForm").find("input[type='radio'][name='specialSale.isInvoice']").on("ifChecked", function() {
	        	var val = $(this).val();
	        	if(val == 0) {
	        		$("#specialSale_invoice_add").find("div:eq(0)").hide();
	        	} else {
	        		$("#specialSale_invoice_add").find("div:eq(0)").show();
	        	}
	        });
	        $("#addSpecialSaleForm").find(".departmentTree").combotree({
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
   			$("#addSpecialSaleForm").find(".select2").select2();
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("buildSpecialSaleContentGrid_add()", 0);
            
            $("#addSpecialSaleContent_add").click(function() {
            	if(specialSaleLastSel2){
            		//只有有在编辑的才重新计算，否则会取不到要计算的值，而算出的值为0
            		countSpecialSale_add();
               		jQuery("#specialSaleContentDatagrid_add").saveRow(specialSaleLastSel2, false, "clientArray");
               		specialSaleLastSel2 = undefined;
               	}
               	
            	var rowId = Math.random();
            	$("#specialSaleContentDatagrid_add").addRowData(rowId, {
            		id: "",
            		orderNo: getOrderNo("specialSaleContentDatagrid_add"),
            		saleArticleId: "",
            		pictureNo: "",
            		materialId: "",
            		glossinessId: "",
            		faceId: "",
            		effectId: "",
            		materialFormatId: "",
            		height: "0",
            		width: "0",
            		quantity: "0",
            		price: "0",
            		lossAreaAmount: "0",
            		materialAmount: "0",
            		photoFrameAmount: "0",
            		remark: "",
            		articleUnitId: ""
            	});
            });
            
            $("#addSpecialSaleForm").on("blur", "input.count", function() {
            	countSpecialSale_add();
   			});
   			
   			/**
   			 * 运费单独做事件运算，因为如果已经不在编辑状态，则countSpecialSale_add方法运行出来的费用有误
   			 */
   			$("#addSpecialSaleForm").on("blur", "input.freight", function() {
   				var freight = $(this).val();
   				var totalAmount = $("#addSpecialSaleForm").find("input[name='specialSale.totalAmount']").val();
   				var productAmount = $("#addSpecialSaleForm").find("input[name='specialSale.productAmount']").val();
   				var totalAmount = parseFloat(productAmount) + parseFloat(freight);
   				totalAmount = totalAmount.toFixed(2);
   				$("#addSpecialSaleForm").find("input[name='specialSale.totalAmount']").val(totalAmount);
   				$("#addSpecialSaleForm").find("label.totalAmount").html(totalAmount);
   			});
   		});
   		var rowDatas;
   		function buildSpecialSaleContentGrid_add() {
   			$("#specialSaleContentDatagrid_add").jqGrid({
                mtype: "POST",
                //shrinkToFit: false,
                //autowidth: false,
                scrollrows: false,
                scroll: true,
				styleUI : "Bootstrap",
                datatype: "local",
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "序号", name: "orderNo", width: 70, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "orderNo"
                	}
                }, {
                	label: "品名", name: "saleArticleId", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/saleArticle/queryAllSaleArticles.action",
                		name: "saleArticleId",
                		custom_element: createSpecialSaleSaleArticleSelect_add,
                		custom_value: operateSpecialSaleSaleArticleSelectValue_add
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleSaleArticleSelected_add[rowId]) {
               				specialSaleSaleArticleSelected_add[rowId] = {id: rowObject.saleArticleId, name: rowObject.saleArticleName}
               			}
               			text = specialSaleSaleArticleSelected_add[rowId].name ? specialSaleSaleArticleSelected_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleSaleArticleSelected_add[rowId].id;
                	}
                }, {
                	label: "图编号", name: "pictureNo", width: 150, align: "center", editable: true, edittype: "text"
                }, {
                	label: "基材", name: "materialId", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/saleArticle/queryMaterialsByArticleId.action",
                		name: "materialId",
                		custom_element: createSpecialSaleMaterialSelect_add,
                		custom_value: operateSpecialSaleMaterialSelectValue_add
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleMaterialSelected_add[rowId]) {
               				specialSaleMaterialSelected_add[rowId] = {id: rowObject.materialId, name: rowObject.materialName}
               			}
               			text = specialSaleMaterialSelected_add[rowId].name ? specialSaleMaterialSelected_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleMaterialSelected_add[rowId].id;
                	}
                }, {
                	label: "打印面", name: "faceId", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/face/queryAll.action",
                		name: "faceId",
                		custom_element: createSpecialSaleFaceSelect_add,
                		custom_value: operateSpecialSaleFaceSelectValue_add
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleFaceSelected_add[rowId]) {
               				specialSaleFaceSelected_add[rowId] = {id: rowObject.faceId, name: rowObject.faceName}
               			}
               			text = specialSaleFaceSelected_add[rowId].name ? specialSaleFaceSelected_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleFaceSelected_add[rowId].id;
                	}
                }, {
                	label: "光泽度", name: "glossinessId", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/glossiness/queryAll.action",
                		name: "glossinessId",
                		custom_element: createSpecialSaleGlossinessSelect_add,
                		custom_value: operateSpecialSaleGlossinessSelectValue_add
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleGlossinessSelected_add[rowId]) {
               				specialSaleGlossinessSelected_add[rowId] = {id: rowObject.glossinessId, name: rowObject.glossinessName}
               			}
               			text = specialSaleGlossinessSelected_add[rowId].name ? specialSaleGlossinessSelected_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleGlossinessSelected_add[rowId].id;
                	}
                }, {
                	label: "效果", name: "effectId", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/effect/queryAll.action",
                		name: "effectId",
                		custom_element: createSpecialSaleEffectSelect_add,
                		custom_value: operateSpecialSaleEffectSelectValue_add
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleEffectSelected_add[rowId]) {
               				specialSaleEffectSelected_add[rowId] = {id: rowObject.effectId, name: rowObject.effectName}
               			}
               			text = specialSaleEffectSelected_add[rowId].name ? specialSaleEffectSelected_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleEffectSelected_add[rowId].id;
                	}
                }, {
                	label: "基材规格", name: "materialFormatId", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/saleArticle/queryMaterialFormatsByArticleId.action",
                		name: "materialFormatId",
                		custom_element: createSpecialSaleMaterialFormatSelect_add,
                		custom_value: operateSpecialSaleMaterialFormatSelectValue_add
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleMaterialFormatSelected_add[rowId]) {
               				specialSaleMaterialFormatSelected_add[rowId] = {id: rowObject.materialFormatId, name: rowObject.materialFormatName}
               			}
               			text = specialSaleMaterialFormatSelected_add[rowId].name ? specialSaleMaterialFormatSelected_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleMaterialFormatSelected_add[rowId].id;
                	}
                }, {
                	label: "产品规格宽(mm)", name: "width", width: 120, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		//cls: "count",
                		name: "width"
                	}
                }, {
                	label: "产品规格高(mm)", name: "height", width: 120, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		//cls: "count",
                		name: "height"
                	}
                }, {
                	label: "单位", name: "articleUnitId", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/articleUnit/queryAll.action",
                		name: "articleUnitId",
                		custom_element: createSpecialSaleArticleUnitSelect_add,
                		custom_value: operateSpecialSaleArticleUnitSelectValue_add
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleArticleUnitSelected_add[rowId]) {
               				specialSaleArticleUnitSelected_add[rowId] = {id: rowObject.articleUnitId, name: rowObject.articleUnitName}
               			}
               			text = specialSaleArticleUnitSelected_add[rowId].name ? specialSaleArticleUnitSelected_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleArticleUnitSelected_add[rowId].id;
                	}
                }, {
                	label: "数量", name: "quantity", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		cls: "count",
                		name: "quantity"
                	}
                }, {
                	label: "单价", name: "price", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		cls: "count",
                		name: "price"
                	}
                }, {
                	label: "耗损基材费用", name: "lossAreaAmount", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		cls: "count",
                		name: "lossAreaAmount"
                	}
                }, {
                	label: "产品金额", name: "materialAmount", width: 100, align: "center"/*, editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "materialAmount"
                	}*/
                }, {
                	label: "配件金额", name: "photoFrameAmount", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		cls: "count",
                		name: "photoFrameAmount"
                	}
                }, {
                	label: "备注", name: "remark", width: 200, align: "center", editable: true, edittype: "textarea", editoptions: {rows:"2",width:"100%"}
                }, {
                	label: "操作", name: "operationCell", align: "center", width: 70, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		var rowId = options.rowId;
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delSpecialSaleContent_add(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                loadComplete: function(data) {
                	rowDatas = data;
                },
                beforeSelectRow: function(rowid) {
                	return true;
                },
                onSelectRow: function(id){
                	if(id && id!==specialSaleLastSel2){
                		jQuery("#specialSaleContentDatagrid_add").saveRow(specialSaleLastSel2, false, "clientArray");
                		var rowData = $("#specialSaleContentDatagrid_add").getRowData(specialSaleLastSel2);
                		jQuery("#specialSaleContentDatagrid_add").jqGrid("editRow", id, {
                			oneditfunc: function(id) {
                				var $tr = $("#specialSaleContentDatagrid_add").find("tr[id='"+id+"']");
                				$tr.find("select").select2();
                				
                				$tr.find("select[name=saleArticleId]").change(function() {
									var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
									var $tr = $("#specialSaleContentDatagrid_add").find("tr[id='"+rowid+"']");
									var $materialFormatSelect = $tr.find("select[name=materialFormatId]");
									var $materialSelect = $tr.find("select[name=materialId]");
									var $option = $(this).find("option:selected");
									var articleId = $option.attr("value");
									var formatUrl = getRoot() + "workflow/saleArticle/queryMaterialFormatsByArticleId.action?saleArticle.id="+articleId
									var materialUrl = getRoot() + "workflow/saleArticle/queryMaterialsByArticleId.action?saleArticle.id="+articleId;
									getSaleSpecialOptions_add($materialFormatSelect, formatUrl, specialSaleMaterialFormatSelected_add);
									getSaleSpecialOptions_add($materialSelect, materialUrl, specialSaleMaterialSelected_add);
								});
                			}
                		});
                		specialSaleLastSel2=id;
                	}
			    },
				viewrecords: true,
                height: "200px",
                //width: "100%",
                rowNum: 100
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
		 * 创建销售订单品名选择框
		 */
		function createSpecialSaleSaleArticleSelect_add(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
			//得到选中的值
			var specialSaleSaleArticleSelected_addVal;
			if(specialSaleSaleArticleSelected_add[rowid]) {
				specialSaleSaleArticleSelected_addVal = specialSaleSaleArticleSelected_add[rowid].id;
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
			if(url) {
				$.ajax({
					url: getRoot() + url,
					data: "",
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						for(var i = 0; i < json.length; i ++) {
							var id = json[i].id;
							var name = json[i].name;
							var option = document.createElement("option");
							option.value=id;
							option.text=name;
							
							if(id == specialSaleSaleArticleSelected_addVal) {
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
		function operateSpecialSaleSaleArticleSelectValue_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
				specialSaleSaleArticleSelected_add[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		/**
		 * 创建销售订单类别选择框
		 */
		function createSpecialSaleMaterialSelect_add(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
			//得到选中的值
			var specialSaleSaleArticleSelected_addVal;
			if(specialSaleMaterialSelected_add[rowid]) {
				specialSaleMaterialSelected_addVal = specialSaleMaterialSelected_add[rowid].id;
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
			var article = specialSaleSaleArticleSelected_add[rowid];
			$(selectEl).select2();
			if(article && article.id && ""!=$.trim(article.id)) {
				var materialUrl = getRoot() + "workflow/saleArticle/queryMaterialsByArticleId.action?saleArticle.id="+article.id;
				getSaleSpecialOptions_add($(selectEl), materialUrl, specialSaleMaterialSelected_add);
			}
			
			return div;
		}
		function operateSpecialSaleMaterialSelectValue_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
				specialSaleMaterialSelected_add[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		/**
		 * 创建销售订单类别选择框
		 */
		function createSpecialSaleGlossinessSelect_add(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
			//得到选中的值
			var specialSaleGlossinessSelected_addVal;
			if(specialSaleGlossinessSelected_add[rowid]) {
				specialSaleGlossinessSelected_addVal = specialSaleGlossinessSelected_add[rowid].id;
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
			if(url) {
				$.ajax({
					url: getRoot() + url,
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						for(var i = 0; i < json.length; i ++) {
							var id = json[i].id;
							var name = json[i].name;
							var option = document.createElement("option");
							option.value=id;
							option.text=name;
							
							if(id == specialSaleGlossinessSelected_addVal) {
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
		function operateSpecialSaleGlossinessSelectValue_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
				specialSaleGlossinessSelected_add[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function createSpecialSaleFaceSelect_add(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
			//得到选中的值
			var specialSaleFaceSelected_addVal;
			if(specialSaleFaceSelected_add[rowid]) {
				specialSaleFaceSelected_addVal = specialSaleFaceSelected_add[rowid].id;
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
			if(url) {
				$.ajax({
					url: getRoot() + url,
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						for(var i = 0; i < json.length; i ++) {
							var id = json[i].id;
							var name = json[i].name;
							var option = document.createElement("option");
							option.value=id;
							option.text=name;
							
							if(id == specialSaleFaceSelected_addVal) {
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
		function operateSpecialSaleFaceSelectValue_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
				specialSaleFaceSelected_add[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function createSpecialSaleEffectSelect_add(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
			//得到选中的值
			var specialSaleEffectSelected_addVal;
			if(specialSaleEffectSelected_add[rowid]) {
				specialSaleEffectSelected_addVal = specialSaleEffectSelected_add[rowid].id;
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
			if(url) {
				$.ajax({
					url: getRoot() + url,
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						for(var i = 0; i < json.length; i ++) {
							var id = json[i].id;
							var name = json[i].name;
							var option = document.createElement("option");
							option.value=id;
							option.text=name;
							
							if(id == specialSaleEffectSelected_addVal) {
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
		function operateSpecialSaleEffectSelectValue_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
				specialSaleEffectSelected_add[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function createSpecialSaleArticleUnitSelect_add(value, options) {
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
			//得到选中的值
			var specialSaleArticleUnitSelected_addVal;
			if(specialSaleArticleUnitSelected_add[rowid]) {
				specialSaleArticleUnitSelected_addVal = specialSaleArticleUnitSelected_add[rowid].id;
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
			if(url) {
				$.ajax({
					url: getRoot() + url,
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						for(var i = 0; i < json.length; i ++) {
							var id = json[i].id;
							var name = json[i].name;
							var option = document.createElement("option");
							option.value=id;
							option.text=name;
							
							if(id == specialSaleArticleUnitSelected_addVal) {
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
		function operateSpecialSaleArticleUnitSelectValue_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
				specialSaleArticleUnitSelected_add[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function createSpecialSaleMaterialFormatSelect_add(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
			//得到选中的值
			var specialSaleMaterialFormatSelected_addVal;
			if(specialSaleMaterialFormatSelected_add[rowid]) {
				specialSaleMaterialFormatSelected_addVal = specialSaleMaterialFormatSelected_add[rowid].id;
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
			var article = specialSaleSaleArticleSelected_add[rowid];
			$(selectEl).select2();
			if(article && article.id && ""!=$.trim(article.id)) {
				var formatUrl = getRoot() + "workflow/saleArticle/queryMaterialFormatsByArticleId.action?saleArticle.id="+article.id;
				getSaleSpecialOptions_add($(selectEl), formatUrl, specialSaleMaterialFormatSelected_add);
			}
			
			return div;
		}
		
		function operateSpecialSaleMaterialFormatSelectValue_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
				specialSaleMaterialFormatSelected_add[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
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
			div.setAttribute("class", "input-group spinner");
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
			var cols = $("#specialSaleContentDatagrid_add").getGridParam("colModel");
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
		
		function delSpecialSaleContent_add(id, rowId) {
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
		   						url: getRoot() + "workflow/specialSale/delContentById.action",
			    				data: "specialSaleContent.id="+id,
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
			    						$("#specialSaleContentDatagrid_add").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#specialSaleContentDatagrid_add").delRowData(rowId);
		            	}
	   				}
	            }
   			});
		}
		
		function getSaleSpecialOptions_add($select, url, selected) {
			var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
			try {
				$select.select2("val", "");
			} catch(ex) {
				
			}
			$select.empty();
			var option = document.createElement("option");
			option.value="";
			option.text="请选择";
			$select.append(option);
			$.ajax({
				url: url,
				type: "POST",
				success: function(data) {
					var json = eval("("+data+")");
					if(json && json.length > 0) {
						var selectVal;
						if(rowid) {
							selectVal = selected[rowid]
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
		
		/**
		 * 统计
		 */
		function countSpecialSale_add() {
			//产品金额=数量*单价+损耗
           	//成品金额=产品金额合计+配件
           	//总金额=成品金额+运费
  			var rowid = $("#specialSaleContentDatagrid_add").getGridParam("selrow");
			var $tr = $("#specialSaleContentDatagrid_add").find("tr[id='"+rowid+"']");
			var price = $tr.find("input[name=price]").val() ? $tr.find("input[name=price]").val() : "0";
			var height = $tr.find("input[name=height]").val() ? $tr.find("input[name=height]").val() : "0";
			var width = $tr.find("input[name=width]").val() ? $tr.find("input[name=width]").val() : "0";
			var quantity = $tr.find("input[name=quantity]").val() ? $tr.find("input[name=quantity]").val() : "0";
			var lossAreaAmount = $tr.find("input[name=lossAreaAmount]").val() ? $tr.find("input[name=lossAreaAmount]").val() : "0";
			//当前这个编辑行里面的配件金额，因为用jqgrid的sum功能不能够把当前编辑行的内容统计出来
			var photoFrameAmount = $tr.find("input[name=photoFrameAmount]").val() ? $tr.find("input[name=photoFrameAmount]").val() : "0";
			//运费
			var freight = $("#addSpecialSaleForm").find("input[name='specialSale.freight']").val() ? $("#addSpecialSaleForm").find("input[name='specialSale.freight']").val() : "0";
			//产品金额
			var materialAmount = parseFloat(quantity)*parseFloat(price) + parseFloat(lossAreaAmount);
			materialAmount = materialAmount.toFixed(2);
			var $materialAmount = $tr.find("td[aria-describedby=specialSaleContentDatagrid_add_materialAmount]");
			$materialAmount.html(materialAmount);
			var materialAmountSum = $("#specialSaleContentDatagrid_add").getCol("materialAmount", false, "sum");
			var photoFrameAmountSum = $("#specialSaleContentDatagrid_add").getCol("photoFrameAmount", false, "sum");
			photoFrameAmountSum = parseFloat(photoFrameAmountSum) + parseFloat(photoFrameAmount);
			photoFrameAmountSum = photoFrameAmountSum.toFixed(2);
			
			//成品金额
			var productAmount = parseFloat(materialAmountSum) + parseFloat(photoFrameAmountSum);
			
			productAmount = productAmount.toFixed(2);
			
			var totalAmount = parseFloat(productAmount) + parseFloat(freight);
			totalAmount = totalAmount.toFixed(2);
			
			$("#addSpecialSaleForm").find("input[name='specialSale.totalAmount']").val(totalAmount);
			$("#addSpecialSaleForm").find("label.totalAmount").html(totalAmount);
			
			$("#addSpecialSaleForm").find("input[name='specialSale.productAmount']").val(productAmount);
			$("#addSpecialSaleForm").find("label.productAmount").html(productAmount);
		}
		
		function addSpecialSaleSpecialCustomerInfo_add(obj) {
			var $select = $("#editSpecialSaleForm").find("select[name='specialSale.receiverId']");
			$select.select2("val", "");
			$select.empty();
			var $option = $(obj).find("option:selected");
			var customer = $option.attr("customer");
			var customerPhone = $option.attr("customerPhone");
			//var receiver = $option.attr("receiver");
			//var receiverPhone = $option.attr("receiverPhone");
			//var receiverAddress = $option.attr("receiverAddress");
			$("#addSpecialSaleForm").find("input[name='specialSale.customer']").val(customer);
			$("#addSpecialSaleForm").find("input[name='specialSale.customerPhone']").val(customerPhone);
			//$("#addSpecialSaleForm").find("input[name='specialSale.receiver']").val(receiver);
			//$("#addSpecialSaleForm").find("input[name='specialSale.receiverPhone']").val(receiverPhone);
			//$("#addSpecialSaleForm").find("input[name='specialSale.receiverAddress']").val(receiverAddress);
			var customerId = $option.attr("value");
			if(customerId) {
				$.ajax({
					url: getRoot() + "workflow/customerReceiver/queryByCustomerId.action?customerId="+customerId,
					type: "POST",
					success: function(data) {
						try {
							var json = eval("("+data+")");
							var $select = $("#addSpecialSaleForm").find("select[name='specialSale.receiverId']");
							$select.html("");
							var option = document.createElement("option");
							option.value = "";
							option.text = "请选择";
							$select.append(option);
							for(var i = 0; i < json.length; i ++) {
								var receiver = json[i];
								var name = receiver.name;
								var cellphone = receiver.cellphone;
								var receiverAddress = receiver.receiverAddress;
								var option = document.createElement("option");
								option.text = name;
								option.value = receiver.id;
								option.setAttribute("cellphone", cellphone);
								option.setAttribute("receiverAddress", receiverAddress);
								$select.append(option);
							}
						} catch(ex) {
							
						}
					},
					error: function() {
						
					}
				});
			}
		}
		
		function addSpecialSaleSpecialCustomerReceiverInfo_add(obj) {
			var $option = $(obj).find("option:selected");
			var cellphone = $option.attr("cellphone");
			var receiverAddress = $option.attr("receiverAddress");
			var receiver = $option.text();
			$("#addSpecialSaleForm").find("input[name='specialSale.receiver']").val(receiver);
			$("#addSpecialSaleForm").find("input[name='specialSale.receiverPhone']").val(cellphone);
			$("#addSpecialSaleForm").find("input[name='specialSale.receiverAddress']").val(receiverAddress);
		}
   	</script>
  </body>
</html>
