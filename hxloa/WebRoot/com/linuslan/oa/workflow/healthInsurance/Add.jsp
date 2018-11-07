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
    
    <title>医保申请</title>
    
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
   	<form id="addHealthInsuranceForm" action="<%=basePath %>/workflow/healthInsurance/add.action" class="form-horizontal" method="POST" enctype="multipart/form-data">
    	<div class="box-body padding-bottom5 bottom-dotted-border" >
    		<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">时间：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input readonly="readonly" name="date" type="text" class="form-control pull-right date">
					</div>
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 control-label">选择导入文件：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input type="file" name="file" id="exampleInputFile">
					<p class="help-block">请选择医保文件</p>
				</div>
			</div>
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">备注：</label>
				<div class="col-md-9 col-sm-8 no-padding">
					<textarea name="healthInsurance.remark" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
    	</div>
    	<div class="toolbar with-border">
			<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增医保项目" id="addHealthInsuranceContent_add"><i class="fa fa-fw fa-plus"></i>新增</button>
			<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="删除" id="delHealthInsuranceContent_add"><i class="fa fa-fw fa-trash"></i>删除</button>
		</div>
    	<div class="box-body">
    		<table id="healthInsuranceContentDatagrid_add"></table>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addHealthInsuranceForm").find(".date").datepicker({format: "yyyy-mm", language: "zh-CN"});
   			
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateAddHealthInsuranceContentGrid()", 0);
            
            $("#addHealthInsuranceContent_add").click(function() {
            	if(healthInsuranceLastsel2){
               		jQuery("#healthInsuranceContentDatagrid_add").saveRow(healthInsuranceLastsel2, false, "clientArray");
               	}
            	
            	var rowId = Math.random();
            	/*var parameters = {
					rowID : rowId,
					initdata : {},
					position :"first",
					useDefValues : false,
					useFormatter : false,
					addRowParams : {extraparam:{}}
				}
            	$("#healthInsuranceContentDatagrid_add").jqGrid("addRow",parameters);*/
            	$("#healthInsuranceContentDatagrid_add").addRowData(rowId, {
            		orderNo: "",
            		userName: "",
            		idNo: "",
            		company: "",
            		companyMedicalBasicCharge: "",
            		companyMedicalRate: "",
            		companyMedicalCharge: "",
            		companyMeternityBasicCharge: "",
            		companyMeternityRate: "",
            		companyMeternityCharge: "",
            		companyIllnessCharge: "",
            		companySum: "",
            		userMedicalBasicCharge: "",
            		userMedicalRate: "",
            		userMedicalCharge: "",
            		userMeternityBasicCharge: "",
            		userMeternityRate: "",
            		userMeternityCharge: "",
            		userSum: "",
            		totalSum: "",
            		remark: ""
            	});
            });
   		});
   		var rowDatas;
   		function generateAddHealthInsuranceContentGrid() {
   			$("#healthInsuranceContentDatagrid_add").jqGrid({
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "local",
                multiselect: true,
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "healthInsuranceClassName": "d", "healthInsuranceClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "序号", name: "orderNo", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value:operateNumberValue,
                		name: "orderNo"
                	}, width: 100, align: "center"
                }, {
                	label: "姓名", name: "userName", editable: true, edittype: "text", width: 100, align: "center"
                }, {
                	label: "身份证号", name: "idNo", width: 150, align: "center", editable: true, edittype: "text"
                }, {
                	label: "归属公司", name: "company", width: 110, align: "center", editable: true, edittype: "text"
                }, {//公司医疗保险
                	label: "缴费基数", name: "companyMedicalBasicCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companyMedicalBasicCharge"
                	}
                }, {//公司医疗保险
                	label: "缴费比率", name: "companyMedicalRate", width: 110, align: "center", editable: true, edittype: "text"
                }, {//公司医疗保险
                	label: "缴纳金额", name: "companyMedicalCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companyMedicalCharge"
                	}
                }, {//公司生育保险
                	label: "缴费基数", name: "companyMeternityBasicCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companyMeternityBasicCharge"
                	}
                }, {//公司生育保险
                	label: "缴费比率", name: "companyMeternityRate", width: 110, align: "center", editable: true, edittype: "text"
                }, {//公司生育保险
                	label: "缴纳金额", name: "companyMeternityCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companyMeternityCharge"
                	}
                }, {
                	label: "大病补充", name: "companyIllnessCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companyIllnessCharge"
                	}
                }, {
                	label: "单位缴费合计", name: "companySum", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companySum"
                	}
                }, {//个人医疗保险
                	label: "缴费基数", name: "userMedicalBasicCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "userMedicalBasicCharge"
                	}
                }, {//个人医疗保险
                	label: "缴费比率", name: "userMedicalRate", width: 110, align: "center", editable: true, edittype: "text"
                }, {//个人医疗保险
                	label: "缴纳金额", name: "userMedicalCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "userMedicalCharge"
                	}
                }, {//个人生育保险
                	label: "缴费基数", name: "userMeternityBasicCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "userMeternityBasicCharge"
                	}
                }, {//个人生育保险
                	label: "缴费比率", name: "userMeternityRate", width: 110, align: "center", editable: true, edittype: "text"
                }, {//个人生育保险
                	label: "缴纳金额", name: "userMeternityCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "userMeternityCharge"
                	}
                }, {
                	label: "个人缴费合计", name: "userSum", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "userSum"
                	}
                }, {
                	label: "合计", name: "totalSum", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "totalSum"
                	}
                }, {
                	label: "备注", name: "remark", width: 200, align: "center", editable: true, edittype: "textarea", editoptions: {rows:"2",width:"100%"}
                }, {
                	label: "操作", name: "operationCell", align: "center", width: 70, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		var rowId = options.rowId;
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delAddHealthInsuranceContent(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                loadComplete: function(data) {
                	rowDatas = data;
                },
                beforeSelectRow: function(rowid) {
                	return true;
                },
                ondblClickRow: function(id){
                	if(id && id!==healthInsuranceLastsel2){
                		jQuery("#healthInsuranceContentDatagrid_add").saveRow(healthInsuranceLastsel2, false, "clientArray");
                		jQuery("#healthInsuranceContentDatagrid_add").editRow(id,true);
                		healthInsuranceLastsel2=id;
                	}
			    },
				viewrecords: true,
                height: "100%",
                rowNum: 100
            });
   			
   			$("#healthInsuranceContentDatagrid_add").jqGrid("setGroupHeaders", {
   				useColSpanStyle: true,
   				groupHeaders:[{
   					startColumnName: "companyMedicalBasicCharge", numberOfColumns: 3, titleText: "公司医疗保险"
   				}, {
   					startColumnName: "companyMeternityBasicCharge", numberOfColumns: 3, titleText: "公司生育保险"
   				}, {
   					startColumnName: "userMedicalBasicCharge", numberOfColumns: 3, titleText: "个人医疗保险"
   				}, {
   					startColumnName: "userMeternityBasicCharge", numberOfColumns: 3, titleText: "个人生育保险"
   				}]
   			});
   			
   			$("#delHealthInsuranceContent_add").click(function() {
   				var selectedIds = $("#healthInsuranceContentDatagrid_add").getGridParam("selarrrow");
   				
   				delBatchHealthInsuranceContent_add(selectedIds);
   			});
   		}
   		
   		/**
   		 * 批量删除
   		 */
   		function delBatchHealthInsuranceContent_add(ids) {
   			if(!ids && 0 >= ids.length) {
				BootstrapDialog.danger("请至少选择一条删除数据");
				return false;
			}
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
	                	try {
	                		var persistIds = new Array();
		                	for(var i = 0; i < ids.length; i ++) {
			            		var id = ids[i];
			            		if(id && ""!=$.trim(id) && isNumber(id)) {
			                		persistIds.push(id);
				            	} else {
				            		$("#healthInsuranceContentDatagrid_add").delRowData(id);
				            	}
			            	}
			            	if(persistIds && 0 < persistIds.length) {
			            		$.ajax({
			   						url: getRoot() + "workflow/healthInsurance/delContentInIds.action",
				    				data: "ids="+persistIds.join(","),
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
				    						for(var i = 0; i < persistIds.length; i ++) {
				    							var id = persistIds[i];
				    							$("#healthInsuranceContentDatagrid_add").delRowData(id);
				    						}
				    					} else {
				    						BootstrapDialog.danger(json.msg);
				    					}
				    				},
				    				error: function() {
				    					BootstrapDialog.danger("系统异常，请联系管理员！");
				    				}
			   					});
			            	} else {
			            		BootstrapDialog.danger("删除成功");
			            	}
	                	} catch(ex) {
	                		BootstrapDialog.danger("删除失败，"+ex);
	                	}
	                	
	   				}
	            }
   			});
   		}
		
		function delAddHealthInsuranceContent(id, rowId) {
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
		   						url: getRoot() + "workflow/healthInsurance/delContentById.action",
			    				data: "healthInsuranceContent.id="+id,
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
			    						$("#healthInsuranceContentDatagrid_add").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#healthInsuranceContentDatagrid_add").delRowData(rowId);
		            	}
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
			
			//创建外围的div
			var div = document.createElement("div");
			div.setAttribute("class", "input-group spinner");
			div.setAttribute("style", "width: 99%");
			
			//创建输入框
			var input = document.createElement("input");
			div.appendChild(input);
			input.setAttribute("name", name);
			input.setAttribute("type", "number");
			input.setAttribute("class", "form-control");
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
