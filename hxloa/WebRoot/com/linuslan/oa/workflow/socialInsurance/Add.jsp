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
    
    <title>社保申请</title>
    
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
   	<form id="addSocialInsuranceForm" action="<%=basePath %>/workflow/socialInsurance/add.action" class="form-horizontal" method="POST" enctype="multipart/form-data">
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
					<p class="help-block">请选择社保文件</p>
				</div>
			</div>
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">备注：</label>
				<div class="col-md-9 col-sm-8 no-padding">
					<textarea name="socialInsurance.remark" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
    	</div>
    	<div class="toolbar with-border">
			<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增社保项目" id="addSocialInsuranceContent_add"><i class="fa fa-fw fa-plus"></i>新增</button>
			<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="删除" id="delSocialInsuranceContent_add"><i class="fa fa-fw fa-trash"></i>删除</button>
		</div>
    	<div class="box-body" style="overflow-x: auto;">
    		<table id="socialInsuranceContentDatagrid_add"></table>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addSocialInsuranceForm").find(".date").datepicker({format: "yyyy-mm", language: "zh-CN"});
   			
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateAddSocialInsuranceContentGrid()", 0);
            
            $("#addSocialInsuranceContent_add").click(function() {
            	if(socialInsuranceLastsel2){
               		jQuery("#socialInsuranceContentDatagrid_add").saveRow(socialInsuranceLastsel2, false, "clientArray");
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
            	$("#socialInsuranceContentDatagrid_add").jqGrid("addRow",parameters);*/
            	$("#socialInsuranceContentDatagrid_add").addRowData(rowId, {
            		orderNo: "0",
            		userName: "",
            		idNo: "",
            		company: "",
            		companyEndowmentBasicCharge: "",
            		companyEndowmentRate: "",
            		companyEndowmentCharge: "",
            		companyUnemploymentBasicCharge: "",
            		companyUnemploymentRate: "",
            		companyUnemploymentCharge: "",
            		companyEmploymentInjuryBasicCharge: "",
            		companyEmploymentInjuryRate: "",
            		companyEmploymentInjuryCharge: "",
            		companySum: "",
            		userEndowmentBasicCharge: "",
            		userEndowmentRate: "",
            		userEndowmentCharge: "",
            		userUnemploymentBasicCharge: "",
            		userUnemploymentRate: "",
            		userUnemploymentCharge: "",
            		userEmploymentInjuryCharge: "",
            		userSum: "",
            		totalSum: "",
            		remark: ""
            	});
            });
   		});
   		var rowDatas;
   		function generateAddSocialInsuranceContentGrid() {
   			$("#socialInsuranceContentDatagrid_add").jqGrid({
                mtype: "POST",
                //shrinkToFit: true,
                //autowidth: true,
                scrollrows: false,
                scroll: true,
				styleUI : "Bootstrap",
                datatype: "local",
                multiselect: true,
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "socialInsuranceClassName": "d", "socialInsuranceClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
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
                	label: "身份证号", name: "idNo", editable: true, edittype: "text", width: 150, align: "center"
                }, {
                	label: "归属公司", name: "company", width: 150, align: "center", editable: true, edittype: "text"
                }, {//公司基本养老保险
                	label: "缴费基数", name: "companyEndowmentBasicCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companyEndowmentBasicCharge"
                	}
                }, {//公司基本养老保险
                	label: "缴费比率", name: "companyEndowmentRate", width: 110, align: "center", editable: true, edittype: "text"
                }, {//公司基本养老保险
                	label: "缴纳金额", name: "companyEndowmentCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companyEndowmentCharge"
                	}
                }, {//公司失业保险
                	label: "缴费基数", name: "companyUnemploymentBasicCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companyUnemploymentBasicCharge"
                	}
                }, {//公司失业保险
                	label: "缴费比率", name: "companyUnemploymentRate", width: 110, align: "center", editable: true, edittype: "text"
                }, {//公司失业保险
                	label: "缴纳金额", name: "companyUnemploymentCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companyUnemploymentCharge"
                	}
                }, {//公司工伤保险
                	label: "缴费基数", name: "companyEmploymentInjuryBasicCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companyEmploymentInjuryBasicCharge"
                	}
                }, {//公司工伤保险
                	label: "缴费比率", name: "companyEmploymentInjuryRate", width: 110, align: "center", editable: true, edittype: "text"
                }, {//公司工伤保险
                	label: "缴费金额", name: "companyEmploymentInjuryCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companyEmploymentInjuryCharge"
                	}
                }, {
                	label: "单位缴费合计", name: "companySum", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companySum"
                	}
                }, {//个人基本养老保险
                	label: "缴费基数", name: "userEndowmentBasicCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "userEndowmentBasicCharge"
                	}
                }, {//个人基本养老保险
                	label: "缴费比率", name: "userEndowmentRate", width: 110, align: "center", editable: true, edittype: "text"
                }, {//个人基本养老保险
                	label: "缴纳金额", name: "userEndowmentCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "userEndowmentCharge"
                	}
                }, {//个人失业保险
                	label: "缴费基数", name: "userUnemploymentBasicCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "userUnemploymentBasicCharge"
                	}
                }, {//个人失业保险
                	label: "缴费比率", name: "userUnemploymentRate", width: 110, align: "center", editable: true, edittype: "text"
                }, {//个人失业保险
                	label: "缴纳金额", name: "userUnemploymentCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "userUnemploymentCharge"
                	}
                }, {//个人工伤保险
                	label: "缴纳金额", name: "userEmploymentInjuryCharge", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "userEmploymentInjuryCharge"
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
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delAddSocialInsuranceContent(\""+rowObject.id+"\", \""+rowId+"\")");
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
                	if(id && id!==socialInsuranceLastsel2){
                		jQuery("#socialInsuranceContentDatagrid_add").saveRow(socialInsuranceLastsel2, false, "clientArray");
                		jQuery("#socialInsuranceContentDatagrid_add").editRow(id,true);
                		socialInsuranceLastsel2=id;
                	}
			    },
				viewrecords: true,
                height: "100%",
                rowNum: 100
            });
   			
   			$("#socialInsuranceContentDatagrid_add").jqGrid("setGroupHeaders", {
   				useColSpanStyle: true,
   				groupHeaders:[{
   					startColumnName: "companyEndowmentBasicCharge", numberOfColumns: 3, titleText: "公司基本养老保险"
   				}, {
   					startColumnName: "companyUnemploymentBasicCharge", numberOfColumns: 3, titleText: "公司失业保险"
   				}, {
   					startColumnName: "companyEmploymentInjuryBasicCharge", numberOfColumns: 3, titleText: "公司工伤保险"
   				}, {
   					startColumnName: "userEndowmentBasicCharge", numberOfColumns: 3, titleText: "个人基本养老保险"
   				}, {
   					startColumnName: "userUnemploymentBasicCharge", numberOfColumns: 3, titleText: "个人失业保险"
   				}, {
   					startColumnName: "userEmploymentInjuryCharge", numberOfColumns: 1, titleText: "个人工伤保险"
   				}]
   			});
   			
   			$("#delSocialInsuranceContent_add").click(function() {
   				var selectedIds = $("#socialInsuranceContentDatagrid_add").getGridParam("selarrrow");
   				
   				delBatchSocialInsuranceContent_add(selectedIds);
   			});
   		}
   		
   		/**
   		 * 批量删除
   		 */
   		function delBatchSocialInsuranceContent_add(ids) {
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
				            		$("#socialInsuranceContentDatagrid_add").delRowData(id);
				            	}
			            	}
			            	if(persistIds && 0 < persistIds.length) {
			            		$.ajax({
			   						url: getRoot() + "workflow/socialInsurance/delContentInIds.action",
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
				    							$("#socialInsuranceContentDatagrid_add").delRowData(id);
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
		
		function delAddSocialInsuranceContent(id, rowId) {
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
		   						url: getRoot() + "workflow/socialInsurance/delContentById.action",
			    				data: "socialInsuranceContent.id="+id,
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
			    						$("#socialInsuranceContentDatagrid_add").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#socialInsuranceContentDatagrid_add").delRowData(rowId);
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
