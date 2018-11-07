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
   	<form id="editSpecialSaleForm" action="" class="form-horizontal">
   		<input id="editSpecialSaleId" class="specialSaleId" name="specialSale.id" type="hidden" value="${specialSale.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#specialSale_edit" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#specialSale_auditLog_edit" data-toggle="tab">审核记录</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="specialSale_edit">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-1 col-sm-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select class="select2" name="specialSale.companyId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${companys }" var="company">
										<option value="${company.id }" ${specialSale.companyId == company.id ? "selected='selected'" : "" }>${company.name }</option>
									</c:forEach>
								</select>
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">是否开票：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<input name="specialSale.isInvoice" type="radio" value="0" class="minimal" ${specialSale.isInvoice == 0 ? "checked" : "" } />&nbsp;否&nbsp;&nbsp;&nbsp;&nbsp;
								<input name="specialSale.isInvoice" type="radio" value="1" class="minimal" ${specialSale.isInvoice == 1 ? "checked" : "" }/>&nbsp;是
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-1 col-sm-4 control-label">订单号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="specialSale.serialNo" type="text" value="${specialSale.serialNo }" readonly="readonly" class="form-control showText" id="text">
							</div>
							<label for="text" class="col-md-1 col-sm-4 control-label">订单日期：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="specialSale.saleDate" type="text" value="<fmt:formatDate value="${specialSale.saleDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
								</div>
							</div>
							<label for="text" class="col-md-1 col-sm-4 control-label">出货日期：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="specialSale.checkoutDate" type="text" value="<fmt:formatDate value="${specialSale.checkoutDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
								</div>
							</div>
							<label for="groupId" class="col-md-1 col-sm-4 control-label">结款日期：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="specialSale.payDate" type="text" value="<fmt:formatDate value="${specialSale.payDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" class="form-control pull-right date showText" id="text">
								</div>
							</div>
							
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">业务员：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="specialSale.salesman" type="text" value="${specialSale.salesman }" class="form-control" id="text">
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">客户：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select onchange="addSpecialSaleSpecialCustomerInfo_edit(this)" class="select2" name="specialSale.customerId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${customers }" var="customer">
										<option value="${customer.id }" customer="${customer.name }"
										 customerPhone="${customer.cellphone }" receiver="${customer.receiver }"
										 receiverPhone="${customer.receiverPhone }"
										 receiverAddress="${customer.receiverAddress }"
										 ${customer.id == specialSale.customerId ? "selected='selected'" : "" }>${customer.name }</option>
									</c:forEach>
								</select>
								<input name="specialSale.customer" type="hidden" value="${specialSale.customer }" class="form-control" id="text">
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">手机：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="specialSale.customerPhone" type="text" value="${specialSale.customerPhone }" class="form-control" id="text">
							</div>
							
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">付款方式：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select class="select2" name="specialSale.payTypeId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${payTypes }" var="dictionary">
										<option value="${dictionary.id }" ${specialSale.payTypeId == dictioanry.id ? "selected='selected'" : "" }>${dictionary.text }</option>
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
										<option value="${deliverType.id }" ${specialSale.deliverTypeId == deliverType.id ? "selected='selected'" : "" }>${deliverType.text }</option>
									</c:forEach>
								</select>
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">收货人：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select onchange="addSpecialSaleSpecialCustomerReceiverInfo_edit(this)" class="select2" name="specialSale.receiverId" style="width: 100%">
									<option value="">请选择</option>
								<c:forEach items="${receivers }" var="receiver">
									<option value="${receiver.id }" cellphone="${receiver.cellphone }"
									 receiverAddress="${receiver.receiverAddress }"
									 ${specialSale.receiverId == receiver.id ? "selected='selected'" : "" }>${receiver.name }</option>
								</c:forEach>
								</select>
								<input name="specialSale.receiver" type="hidden" value="${specialSale.receiver }" class="form-control" id="text">
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">手机：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="specialSale.receiverPhone" type="text" value="${specialSale.receiverPhone }" class="form-control" id="text">
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">收货地址：</label>
							<div class="col-md-11 col-sm-8 no-padding">
								<input name="specialSale.receiverAddress" type="text" value="${specialSale.receiverAddress }" class="form-control" id="text">
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">定金：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="specialSale.frontMoney" type="number" value="${specialSale.frontMoney }" class="form-control" id="text">
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">总金额：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<label class="totalAmount">${specialSale.totalAmount }</label>
								<input name="specialSale.totalAmount" type="hidden" value="${specialSale.totalAmount }" class="form-control" id="text">
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">成品金额：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<label class="productAmount">${specialSale.productAmount }</label>
								<input name="specialSale.productAmount" type="hidden" value="${specialSale.productAmount }" class="form-control" id="text">
							</div>
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">运费：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="specialSale.freight" type="number" value="${specialSale.freight }" class="form-control freight" id="text">
							</div>
						</div>
						
						<!-- 
						<div class="form-group">
							
							<label for="departmentId" class="col-md-1 col-sm-4 control-label">物流：</label>
							<div class="col-md-7-sm col-sm-8 no-padding">
								<input name="specialSale.logistics" type="text" value="${specialSale.logistics }" class="form-control" id="text">
							</div>
						</div>
						 -->
			    	</div>
			    	<div class="toolbar with-border">
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增销售订单项目" id="addSpecialSaleContent_edit"><i class="fa fa-fw fa-plus"></i>新增</button>
					</div>
			    	<div class="box-body" style="overflow-x: auto;">
			    		<table id="specialSaleContentDatagrid_edit"></table>
			    	</div>
			    </div>
			    <div class="tab-pane" id="specialSale_auditLog_edit">
			    	<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="specialSaleAuditorlogsDatagrid_edit"></table>
	   							<div id="specialSaleAuditorlogsDatagridPager_edit"></div>
   							</div>
		   				</div>
		   			</div>
			    </div>
			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		//存放选中的销售订单品名的id和name
		var specialSaleSaleArticleSelected_edit = new Object();
		//存放选中的销售订单材质的id和name
   		var specialSaleMaterialSelected_edit = new Object();
   		//存放选中的销售订单光泽度的id和name
   		var specialSaleGlossinessSelected_edit = new Object();
   		//存放选中的销售订单面数的id和name
   		var specialSaleFaceSelected_edit = new Object();
   		//存放选中的销售订单单位的id和name
   		var specialSaleArticleUnitSelected_edit = new Object();
   		//存放选中的销售订单效果的id和name
   		var specialSaleEffectSelected_edit = new Object();
   		//存放选中的销售订单材质规格的id和name
   		var specialSaleMaterialFormatSelected_edit = new Object();
   		$(function() {
   			$("#editSpecialSaleForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#editSpecialSaleForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
	        $("#editSpecialSaleForm").find("input[type='radio'][name='specialSale.isInvoice']").on("ifChecked", function() {
	        	var val = $(this).val();
	        	if(val == 0) {
	        		$("#specialSale_invoice_edit").find("div:eq(0)").hide();
	        	} else {
	        		$("#specialSale_invoice_edit").find("div:eq(0)").show();
	        	}
	        });
	        $("#editSpecialSaleForm a[data-toggle='tab']").on("shown.bs.tab", function (e) {
   				var href = $(this).attr("href");
   				var id = href.substring(1);
   				if(id == "specialSale_auditLog_edit") {
   					$("#"+id).find("table").setGridWidth($("#"+id).width()*0.99);
   				}
   				
   				if(id == "specialSale_invoice_edit") {
   					specialSaleDepartment_edit.destory();
   					$("#editSpecialSaleForm").find(".departmentTree").combotree({
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
   			
   			var specialSaleDepartment_edit = $("#editSpecialSaleForm").find(".departmentTree").combotree({
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
	        
   			$("#editSpecialSaleForm").find(".select2").select2();
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("buildSpecialSaleContentGrid_edit()", 0);
			setTimeout("buildSpecialSaleLogGrid_edit()", 0);
            
            $("#addSpecialSaleContent_edit").click(function() {
            	if(specialSaleLastSel2){
            		countSpecialSale_edit();
               		jQuery("#specialSaleContentDatagrid_edit").saveRow(specialSaleLastSel2, false, "clientArray");
               		specialSaleLastSel2 = undefined;
               	}
               	
            	var rowId = Math.random();
            	$("#specialSaleContentDatagrid_edit").addRowData(rowId, {
            		id: "",
            		orderNo: getOrderNo("specialSaleContentDatagrid_edit"),
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
            		unitName: ""
            	});
            });
            
            $("#editSpecialSaleForm").on("blur", "input.count", function() {
   				countSpecialSale_edit();
   			});
   			
   			/**
   			 * 运费单独做事件运算，因为如果已经不在编辑状态，则countSpecialSale_add方法运行出来的费用有误
   			 */
   			$("#editSpecialSaleForm").on("blur", "input.freight", function() {
   				var freight = $(this).val();
   				var totalAmount = $("#editSpecialSaleForm").find("input[name='specialSale.totalAmount']").val();
   				var productAmount = $("#editSpecialSaleForm").find("input[name='specialSale.productAmount']").val();
   				var totalAmount = parseFloat(productAmount) + parseFloat(freight);
   				totalAmount = totalAmount.toFixed(2);
   				$("#editSpecialSaleForm").find("input[name='specialSale.totalAmount']").val(totalAmount);
   				$("#editSpecialSaleForm").find("label.totalAmount").html(totalAmount);
   			});
   		});
   		var rowDatas;
   		
   		function buildSpecialSaleLogGrid_edit() {
   			$("#specialSaleAuditorlogsDatagrid_edit").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=specialSale&wfId="+$("#editSpecialSaleId").val(),
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
                pager: "#specialSaleAuditorlogsDatagridPager_edit"
            });
   		}
   		
   		function buildSpecialSaleContentGrid_edit() {
   			var specialSaleId = $("#editSpecialSaleForm").find("input[type=hidden][name='specialSale.id']").val();
   			$("#specialSaleContentDatagrid_edit").jqGrid({
                mtype: "POST",
                url: getRoot() + "workflow/specialSale/queryContentsBySpecialSaleId.action?specialSale.id="+specialSaleId,
                //shrinkToFit: false,
                //autowidth: false,
                scrollrows: false,
                scroll: true,
				styleUI : "Bootstrap",
                datatype: "json",
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
                		custom_element: createSpecialSaleSaleArticleSelect_edit,
                		custom_value: operateSpecialSaleSaleArticleSelectValue_edit
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleSaleArticleSelected_edit[rowId]) {
               				specialSaleSaleArticleSelected_edit[rowId] = {id: rowObject.saleArticleId, name: rowObject.saleArticleName}
               			}
               			text = specialSaleSaleArticleSelected_edit[rowId].name ? specialSaleSaleArticleSelected_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleSaleArticleSelected_edit[rowId].id;
                	}
                }, {
                	label: "图编号", name: "pictureNo", width: 150, align: "center", editable: true, edittype: "text"
                }, {
                	label: "基材", name: "materialId", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/saleArticle/queryMaterialsByArticleId.action",
                		name: "materialId",
                		custom_element: createSpecialSaleMaterialSelect_edit,
                		custom_value: operateSpecialSaleMaterialSelectValue_edit
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleMaterialSelected_edit[rowId]) {
               				specialSaleMaterialSelected_edit[rowId] = {id: rowObject.materialId, name: rowObject.materialName}
               			}
               			text = specialSaleMaterialSelected_edit[rowId].name ? specialSaleMaterialSelected_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleMaterialSelected_edit[rowId].id;
                	}
                }, {
                	label: "打印面", name: "faceId", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/face/queryAll.action",
                		name: "faceId",
                		custom_element: createSpecialSaleFaceSelect_edit,
                		custom_value: operateSpecialSaleFaceSelectValue_edit
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleFaceSelected_edit[rowId]) {
               				specialSaleFaceSelected_edit[rowId] = {id: rowObject.faceId, name: rowObject.faceName}
               			}
               			text = specialSaleFaceSelected_edit[rowId].name ? specialSaleFaceSelected_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleFaceSelected_edit[rowId].id;
                	}
                }, {
                	label: "光泽度", name: "glossinessId", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/glossiness/queryAll.action",
                		name: "glossinessId",
                		custom_element: createSpecialSaleGlossinessSelect_edit,
                		custom_value: operateSpecialSaleGlossinessSelectValue_edit
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleGlossinessSelected_edit[rowId]) {
               				specialSaleGlossinessSelected_edit[rowId] = {id: rowObject.glossinessId, name: rowObject.glossinessName}
               			}
               			text = specialSaleGlossinessSelected_edit[rowId].name ? specialSaleGlossinessSelected_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleGlossinessSelected_edit[rowId].id;
                	}
                }, {
                	label: "效果", name: "effectId", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/effect/queryAll.action",
                		name: "effectId",
                		custom_element: createSpecialSaleEffectSelect_edit,
                		custom_value: operateSpecialSaleEffectSelectValue_edit
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleEffectSelected_edit[rowId]) {
               				specialSaleEffectSelected_edit[rowId] = {id: rowObject.effectId, name: rowObject.effectName}
               			}
               			text = specialSaleEffectSelected_edit[rowId].name ? specialSaleEffectSelected_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleEffectSelected_edit[rowId].id;
                	}
                }, {
                	label: "基材规格", name: "materialFormatId", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/saleArticle/queryMaterialFormatsByArticleId.action",
                		name: "materialFormatId",
                		custom_element: createSpecialSaleMaterialFormatSelect_edit,
                		custom_value: operateSpecialSaleMaterialFormatSelectValue_edit
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleMaterialFormatSelected_edit[rowId]) {
               				specialSaleMaterialFormatSelected_edit[rowId] = {id: rowObject.materialFormatId, name: rowObject.materialFormatName}
               			}
               			text = specialSaleMaterialFormatSelected_edit[rowId].name ? specialSaleMaterialFormatSelected_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleMaterialFormatSelected_edit[rowId].id;
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
                		custom_element: createSpecialSaleArticleUnitSelect_edit,
                		custom_value: operateSpecialSaleArticleUnitSelectValue_edit
                	},
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!specialSaleArticleUnitSelected_edit[rowId]) {
               				specialSaleArticleUnitSelected_edit[rowId] = {id: rowObject.articleUnitId, name: rowObject.articleUnitName}
               			}
               			text = specialSaleArticleUnitSelected_edit[rowId].name ? specialSaleArticleUnitSelected_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return specialSaleArticleUnitSelected_edit[rowId].id;
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
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delSpecialSaleContent_edit(\""+rowObject.id+"\", \""+rowId+"\")");
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
                		jQuery("#specialSaleContentDatagrid_edit").saveRow(specialSaleLastSel2, false, "clientArray");
                		var rowData = $("#specialSaleContentDatagrid_edit").getRowData(specialSaleLastSel2);
                		jQuery("#specialSaleContentDatagrid_edit").jqGrid("editRow", id, {
                			oneditfunc: function(id) {
                				var $tr = $("#specialSaleContentDatagrid_edit").find("tr[id='"+id+"']");
                				$tr.find("select").select2();
                				
                				$tr.find("select[name=saleArticleId]").change(function() {
									var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
									var $tr = $("#specialSaleContentDatagrid_edit").find("tr[id='"+rowid+"']");
									var $materialFormatSelect = $tr.find("select[name=materialFormatId]");
									var $materialSelect = $tr.find("select[name=materialId]");
									var $option = $(this).find("option:selected");
									var articleId = $option.attr("value");
									var formatUrl = getRoot() + "workflow/saleArticle/queryMaterialFormatsByArticleId.action?saleArticle.id="+articleId
									var materialUrl = getRoot() + "workflow/saleArticle/queryMaterialsByArticleId.action?saleArticle.id="+articleId;
									getSaleSpecialOptions_edit($materialFormatSelect, formatUrl, specialSaleMaterialFormatSelected_edit);
									getSaleSpecialOptions_edit($materialSelect, materialUrl, specialSaleMaterialSelected_edit);
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
		function createSpecialSaleSaleArticleSelect_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
			//得到选中的值
			var specialSaleSaleArticleSelected_editVal;
			if(specialSaleSaleArticleSelected_edit[rowid]) {
				specialSaleSaleArticleSelected_editVal = specialSaleSaleArticleSelected_edit[rowid].id;
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
							
							if(id == specialSaleSaleArticleSelected_editVal) {
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
		function operateSpecialSaleSaleArticleSelectValue_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
				specialSaleSaleArticleSelected_edit[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		/**
		 * 创建销售订单类别选择框
		 */
		function createSpecialSaleMaterialSelect_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
			//得到选中的值
			var specialSaleSaleArticleSelected_editVal;
			if(specialSaleMaterialSelected_edit[rowid]) {
				specialSaleMaterialSelected_editVal = specialSaleMaterialSelected_edit[rowid].id;
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
			var article = specialSaleSaleArticleSelected_edit[rowid];
			$(selectEl).select2();
			if(article && article.id && ""!=$.trim(article.id)) {
				var materialUrl = getRoot() + "workflow/saleArticle/queryMaterialsByArticleId.action?saleArticle.id="+article.id;
				getSaleSpecialOptions_edit($(selectEl), materialUrl, specialSaleMaterialSelected_edit);
			}
			
			return div;
		}
		function operateSpecialSaleMaterialSelectValue_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
				specialSaleMaterialSelected_edit[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		/**
		 * 创建销售订单类别选择框
		 */
		function createSpecialSaleGlossinessSelect_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
			//得到选中的值
			var specialSaleGlossinessSelected_editVal;
			if(specialSaleGlossinessSelected_edit[rowid]) {
				specialSaleGlossinessSelected_editVal = specialSaleGlossinessSelected_edit[rowid].id;
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
							
							if(id == specialSaleGlossinessSelected_editVal) {
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
		function operateSpecialSaleGlossinessSelectValue_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
				specialSaleGlossinessSelected_edit[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function createSpecialSaleFaceSelect_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
			//得到选中的值
			var specialSaleFaceSelected_editVal;
			if(specialSaleFaceSelected_edit[rowid]) {
				specialSaleFaceSelected_editVal = specialSaleFaceSelected_edit[rowid].id;
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
							
							if(id == specialSaleFaceSelected_editVal) {
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
		function operateSpecialSaleFaceSelectValue_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
				specialSaleFaceSelected_edit[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function createSpecialSaleEffectSelect_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
			//得到选中的值
			var specialSaleEffectSelected_editVal;
			if(specialSaleEffectSelected_edit[rowid]) {
				specialSaleEffectSelected_editVal = specialSaleEffectSelected_edit[rowid].id;
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
							
							if(id == specialSaleEffectSelected_editVal) {
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
		function operateSpecialSaleEffectSelectValue_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
				specialSaleEffectSelected_edit[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function createSpecialSaleArticleUnitSelect_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
			//得到选中的值
			var specialSaleArticleUnitSelected_editVal;
			if(specialSaleEffectSelected_edit[rowid]) {
				specialSaleArticleUnitSelected_editVal = specialSaleArticleUnitSelected_edit[rowid].id;
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
							
							if(id == specialSaleArticleUnitSelected_editVal) {
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
		function operateSpecialSaleArticleUnitSelectValue_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
				specialSaleArticleUnitSelected_edit[specialSaleLastSel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function createSpecialSaleMaterialFormatSelect_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
			//得到选中的值
			var specialSaleMaterialFormatSelected_editVal;
			if(specialSaleMaterialFormatSelected_edit[rowid]) {
				specialSaleMaterialFormatSelected_editVal = specialSaleMaterialFormatSelected_edit[rowid].id;
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
			var article = specialSaleSaleArticleSelected_edit[rowid];
			$(selectEl).select2();
			if(article && article.id && ""!=$.trim(article.id)) {
				var formatUrl = getRoot() + "workflow/saleArticle/queryMaterialFormatsByArticleId.action?saleArticle.id="+article.id;
				getSaleSpecialOptions_edit($(selectEl), formatUrl, specialSaleMaterialFormatSelected_edit);
			}
			
			return div;
		}
		
		function operateSpecialSaleMaterialFormatSelectValue_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
				specialSaleMaterialFormatSelected_edit[specialSaleLastSel2] = {id: val, name: text};
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
			var cols = $("#specialSaleContentDatagrid_edit").getGridParam("colModel");
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
		
		function delSpecialSaleContent_edit(id, rowId) {
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
			    						$("#specialSaleContentDatagrid_edit").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#specialSaleContentDatagrid_edit").delRowData(rowId);
		            	}
	   				}
	            }
   			});
		}
		
		function getSaleSpecialOptions_edit($select, url, selected) {
			var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
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
		
		function countSpecialSale_edit() {
			
			//产品金额=数量*单价+损耗
           	//成品金额=产品金额合计+配件
           	//总金额=成品金额+运费
  			var rowid = $("#specialSaleContentDatagrid_edit").getGridParam("selrow");
			var $tr = $("#specialSaleContentDatagrid_edit").find("tr[id='"+rowid+"']");
			var price = $tr.find("input[name=price]").val() ? $tr.find("input[name=price]").val() : "0";
			var height = $tr.find("input[name=height]").val() ? $tr.find("input[name=height]").val() : "0";
			var width = $tr.find("input[name=width]").val() ? $tr.find("input[name=width]").val() : "0";
			var quantity = $tr.find("input[name=quantity]").val() ? $tr.find("input[name=quantity]").val() : "0";
			var lossAreaAmount = $tr.find("input[name=lossAreaAmount]").val() ? $tr.find("input[name=lossAreaAmount]").val() : "0";
			//当前这个编辑行里面的配件金额，因为用jqgrid的sum功能不能够把当前编辑行的内容统计出来
			var photoFrameAmount = $tr.find("input[name=photoFrameAmount]").val() ? $tr.find("input[name=photoFrameAmount]").val() : "0";
			//运费
			var freight = $("#editSpecialSaleForm").find("input[name='specialSale.freight']").val() ? $("#editSpecialSaleForm").find("input[name='specialSale.freight']").val() : "0";
			//产品金额
			var materialAmount = parseFloat(quantity)*parseFloat(price) + parseFloat(lossAreaAmount);
			materialAmount = materialAmount.toFixed(2);
			var $materialAmount = $tr.find("td[aria-describedby=specialSaleContentDatagrid_edit_materialAmount]");
			$materialAmount.html(materialAmount);
			var materialAmountSum = $("#specialSaleContentDatagrid_edit").getCol("materialAmount", false, "sum");
			var photoFrameAmountSum = $("#specialSaleContentDatagrid_edit").getCol("photoFrameAmount", false, "sum");
			photoFrameAmountSum = parseFloat(photoFrameAmountSum) + parseFloat(photoFrameAmount);
			photoFrameAmountSum = photoFrameAmountSum.toFixed(2);
			
			//成品金额
			var productAmount = parseFloat(materialAmountSum) + parseFloat(photoFrameAmountSum);
			
			productAmount = productAmount.toFixed(2);
			
			var totalAmount = parseFloat(productAmount) + parseFloat(freight);
			totalAmount = totalAmount.toFixed(2);
			
			$("#editSpecialSaleForm").find("input[name='specialSale.totalAmount']").val(totalAmount);
			$("#editSpecialSaleForm").find("label.totalAmount").html(totalAmount);
			
			$("#editSpecialSaleForm").find("input[name='specialSale.productAmount']").val(productAmount);
			$("#editSpecialSaleForm").find("label.productAmount").html(productAmount);
		}
		
		function addSpecialSaleSpecialCustomerInfo_edit(obj) {
			var $select = $("#editSpecialSaleForm").find("select[name='specialSale.receiverId']");
			$select.select2("val", "");
			$select.empty();
			var $option = $(obj).find("option:selected");
			var customer = $option.attr("customer");
			var customerPhone = $option.attr("customerPhone");
			//var receiver = $option.attr("receiver");
			//var receiverPhone = $option.attr("receiverPhone");
			//var receiverAddress = $option.attr("receiverAddress");
			$("#editSpecialSaleForm").find("input[name='specialSale.customer']").val(customer);
			$("#editSpecialSaleForm").find("input[name='specialSale.customerPhone']").val(customerPhone);
			//$("#editSpecialSaleForm").find("input[name='specialSale.receiver']").val(receiver);
			//$("#editSpecialSaleForm").find("input[name='specialSale.receiverPhone']").val(receiverPhone);
			//$("#editSpecialSaleForm").find("input[name='specialSale.receiverAddress']").val(receiverAddress);
			
			var customerId = $option.attr("value");
			if(customerId) {
				$.ajax({
					url: getRoot() + "workflow/customerReceiver/queryByCustomerId.action?customerId="+customerId,
					type: "POST",
					success: function(data) {
						try {
							var json = eval("("+data+")");
							
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
		
		function addSpecialSaleSpecialCustomerReceiverInfo_edit(obj) {
			var $option = $(obj).find("option:selected");
			var cellphone = $option.attr("cellphone");
			var receiverAddress = $option.attr("receiverAddress");
			var receiver = $option.text();
			$("#editSpecialSaleForm").find("input[name='specialSale.receiver']").val(receiver);
			$("#editSpecialSaleForm").find("input[name='specialSale.receiverPhone']").val(cellphone);
			$("#editSpecialSaleForm").find("input[name='specialSale.receiverAddress']").val(receiverAddress);
		}
   	</script>
  </body>
</html>
