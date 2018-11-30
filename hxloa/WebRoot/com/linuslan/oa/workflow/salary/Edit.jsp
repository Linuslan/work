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
    
    <title>编辑工资申请</title>
    
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
   	<form id="editSalaryForm" action="" class="form-horizontal">
   		<input id="editSalaryId" class="salaryId" name="salary.id" type="hidden" value="${salary.id }" />
   		<input id="editSalaryYear" name="year" type="hidden" value="${salary.year }"/>
   		<input id="editSalaryMonth" name="month" type="hidden" value="${salary.month }"/>
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#salary" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#salaryAuditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="salary">
			    	<div class="toolbar with-border">
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增薪资" id="addSalaryContent_edit"><i class="fa fa-fw fa-user-plus"></i>新增</button>
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="单条保存" id="saveSingleSalaryContent_edit"><i class="fa fa-fw fa-save"></i>单条保存</button>
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="设置应出勤天数" id="setupDutyDay_edit"><i class="fa fa-fw fa-save"></i>出勤天数</button>
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="统计餐补" id="countMealSubsidy_edit"><i class="fa fa-fw fa-save"></i>统计餐补</button>
					</div>
			    	<div class="box-body" style="overflow-x: auto;">
			    		<table id="salaryContentDatagrid_edit"></table>
			    	</div>
			    </div>
			   	<div class="tab-pane" id="salaryAuditorLogs">
   					<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="editSalaryAuditorlogsDatagrid"></table>
	   							<div id="editSalaryAuditorlogsDatagridPager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		var salaryDeptCombotree_edit;
   		var srcRowId;
   		$(function() {
   			salaryLastSel2 = "";
   			$("#editSalaryForm").find(".select2").select2();
   			$("#editSalaryForm").find(".date").datepicker({format: "yyyy-mm", language: "zh-CN"});
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("buildSalaryContentDatagrid_edit()", 0);
            setTimeout("buildSalaryAuditLogGrid_edit()", 0);
            $("#addSalaryContent_edit").click(function() {
            	var year = $("#editSalaryYear").val();
            	var month = $("#editSalaryMonth").val();
            	if(srcRowId) {
            		var rowId = Math.random();
	            	$("#salaryContentDatagrid_edit").addRowData(rowId, {
	            		id: "",
	            		year: year,
	            		month: month,
	            		userId: "",
	            		supposedDutyDay: "",
	            		actualDutyDay: "",
	            		actualDutyHour: "",
	            		basicSalary: "",
	            		achievementSalary: "",
	            		achievementScore: "",
	            		actualAchievementSalary: "",
	            		commission: "",
	            		fullAttendanceAward: "",
	            		overtimePay: "",
	            		seniorityPay: "",
	            		sickLeaveDeduct: "",
	            		affairLeaveDeduct: "",
	            		lateDeduct: "",
	            		punishDeduct: "",
	            		supposedTotalSalary: "",
	            		socialInsurance: "",
	            		healthInsurance: "",
	            		totalInsurance: "",
	            		other: "",
	            		actualTotalSalary: "",
	            		telCharge: "",
	            		mealSubsidy: "",
	            		travelAllowance: "",
	            		housingSubsidy: "",
	            		companySocialInsurance: "",
	            		companyHealthInsurance: "",
	            		info: ""
	            	}, "before", srcRowId);
            	}
            });
            
            $("#saveSingleSalaryContent_edit").click(function() {
            	saveRowSalary_edit();
			});
			
			$("#setupDutyDay_edit").click(function() {
				saveRowSalary_edit();
				BootstrapDialog.show({
				    title: "请输入应出勤天数",
				    width: "20%",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: "<input class='form-control duty-day' type='number' />",
				    draggable: true,
				    autodestroy: true,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	
				    },
				    buttons: [{
				    	label: "保存",
				    	icon: "fa fa-fw fa-save",
				    	cssClass: "btn-success",
				    	action: function(dialog) {
				    		var $input = dialog.getModalBody().find("input.duty-day");
				    		var value = $input.val();
				    		if(value) {
				    			var rowDatas = $("#salaryContentDatagrid_edit").getRowData();
				    			var rowData = null;
				    			for(var i = 0; i < rowDatas.length; i ++) {
				    				rowData = rowDatas[i];
				    				var rowId = rowData.id;
				    				if(rowId.indexOf("_sum") >= 0) {
				    					continue;
				    				}
				    				rowData.supposedDutyDay = value;
				    				$("#salaryContentDatagrid_edit").setRowData(rowData.id, rowData);
				    			}
				    			dialog.close();
				    		}
				    	}
				    }, {
				    	label: "关闭",
				    	icon: "fa fa-fw fa-close",
				    	cssClass: "btn-danger",
				    	action: function(dialog) {
				    		dialog.close();
				    	}
				    }]
		        });
			});
			
			$("#countMealSubsidy_edit").click(function() {
				saveRowSalary_edit();
				BootstrapDialog.confirm({
		            title: "温馨提示",
		            message: "该功能适用于建档初期，按实际出勤天数×员工餐补计算！<font color='red'>后期请慎用，因为会覆盖您手动输入的餐补数据！</font>确定统计吗？",
		            type: BootstrapDialog.TYPE_WARNING, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
		            closable: true, // <-- Default value is false
		            draggable: true, // <-- Default value is false
		            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
		            btnOKLabel: "确定", // <-- Default value is 'OK',
		            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
		            callback: function(y) {
		                // result will be true if button was click, while it will be false if users close the dialog directly.
		                if(y) {
		                	var rowDatas = $("#salaryContentDatagrid_edit").getRowData();
			    			var rowData = null;
			    			for(var i = 0; i < rowDatas.length; i ++) {
			    				rowData = rowDatas[i];
			    				var rowId = rowData.id;
			    				if(rowId.indexOf("_sum") >= 0) {
			    					continue;
			    				}
			    				var originalMealSubsidy = rowData.originalMealSubsidy;
			    				var mealSubsidy = 0;
			    				if(originalMealSubsidy) {
				    				var dutyDay = rowData.actualDutyDay;
				    				var mealSubsidy = parseFloat(dutyDay)*originalMealSubsidy;
				    				mealSubsidy = mealSubsidy.toFixed(0);
				    				rowData.mealSubsidy = mealSubsidy;
			    				}
			    				$("#salaryContentDatagrid_edit").setRowData(rowData.id, rowData);
			    			}
			    			countSalary();
		   				}
		            }
	   			});
			});
   		});
   		var rowDatas;
   		function buildSalaryContentDatagrid_edit() {
   			var id = $("#editSalaryForm").find("input.salaryId").val();
   			$("#salaryContentDatagrid_edit").jqGrid({
   				url: getRoot() + "workflow/salary/queryContentsBySalaryId.action?salary.id="+id,
                mtype: "POST",
                shrinkToFit: false,
				styleUI : "Bootstrap",
                datatype: "json",
                height: "550px",
                footerrow: false,
                gridComplete: function() {
                	$("#salaryContentDatagrid_edit").setGridWidth($("#salary").width()*0.99);
                	$("#salaryContentDatagrid_edit").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
                },
                loadComplete: function() {
                	countSalary();
                },
                colModel: [{
                	label: "id", name: "id", hidden: true
                }, {
                	label: "year", name: "year", hidden: true
                }, {
                	label: "month", name: "month", hidden: true
                }, {
                	label: "originalMealSubsidy", name: "originalMealSubsidy", hidden: true
                }, {
                	label: "姓名", name: "userName", width: 100, align: "center", editable: true, edittype: "text",
                	formatter: function(cellvalue, options, rowObject) {
               			if(rowObject.userName) {
               				return rowObject.userName;
               			} else {
               				return "";
               			}
                	},
                	unformat: function(cellvalue, options, cell) {
                		if(rowObject.userId) {
                			return rowObject.userId
                		} else if(rowObject.userName) {
                			return rowObject.userName;
                		} else {
                			return "";
                		}
                	}
                }, {
                	label: "应出勤", name: "supposedDutyDay", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "supposedDutyDay"
                	}
                }, {
                	label: "天", name: "actualDutyDay", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "actualDutyDay"
                	}
                }, {
                	label: "时", name: "actualDutyHour", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "actualDutyHour"
                	}
                }, {
                	label: "基本工资", name: "basicSalary", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "basicSalary"
                	}
                }, {
                	label: "岗位工资", name: "postSalary", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "postSalary"
                	}
                }, {
                	label: "绩效标准", name: "achievementSalary", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "achievementSalary"
                	}
                }, {
                	label: "绩效分数", name: "achievementScore", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "achievementScore"
                	}
                }, {
                	label: "实发绩效", name: "actualAchievementSalary", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "actualAchievementSalary"
                	}
                }, {
                	label: "抽成", name: "commission", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "commission"
                	}
                }, {
                	label: "满勤奖", name: "fullAttendanceAward", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "fullAttendanceAward"
                	}
                }, {
                	label: "加班费", name: "overtimePay", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "overtimePay"
                	}
                }, {
                	label: "工龄工资", name: "seniorityPay", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "seniorityPay"
                	}
                }, {
                	label: "病假", name: "sickLeaveDeduct", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "sickLeaveDeduct"
                	}
                }, {
                	label: "事假", name: "affairLeaveDeduct", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "affairLeaveDeduct"
                	}
                }, {
                	label: "迟到", name: "lateDeduct", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "lateDeduct"
                	}
                }, {
                	label: "处罚金", name: "punishDeduct", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "punishDeduct"
                	}
                }, {
                	label: "应发合计", name: "supposedTotalSalary", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "supposedTotalSalary"
                	}
                }, {
                	label: "社保", name: "socialInsurance", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "socialInsurance"
                	}
                }, {
                	label: "医保", name: "healthInsurance", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "healthInsurance"
                	}
                }, {
                	label: "总计", name: "totalInsurance", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "totalInsurance"
                	}
                }, {
                	label: "话补", name: "telCharge", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "telCharge"
                	}
                }, {
                	label: "餐补", name: "mealSubsidy", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "mealSubsidy"
                	}
                }, {
                	label: "车补", name: "travelAllowance", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "travelAllowance"
                	}
                }, {
                	label: "住房补贴", name: "housingSubsidy", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "housingSubsidy"
                	}
                }, {
                	label: "总计", name: "totalSubsidy", width: 100, align: "center"
                }, {
                	label: "其他", name: "other", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "other"
                	}
                }, {
                	label: "子女教育费", name: "childcareExpense", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "childcareExpense"
                	}
                }, {
                	label: "继续教育费", name: "continuingEducationFee", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "continuingEducationFee"
                	}
                }, {
                	label: "大病医疗费", name: "seriousIllnessExpense", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "seriousIllnessExpense"
                	}
                }, {
                	label: "住房贷款利息", name: "housingLoan", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "housingLoan"
                	}
                }, {
                	label: "住房租金费", name: "housingRent", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "housingRent"
                	}
                }, {
                	label: "赡养老人费", name: "alimony", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "alimony"
                	}
                }, {
                	label: "税前工资", name: "pretaxSalary", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "pretaxSalary"
                	}
                }, {
                	label: "个税", name: "tax", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "tax"
                	}
                }, {
                	label: "实发工资", name: "actualTotalSalary", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "actualTotalSalary"
                	}
                }, {
                	label: "社保", name: "companySocialInsurance", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companySocialInsurance"
                	}
                }, {
                	label: "医保", name: "companyHealthInsurance", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "companyHealthInsurance"
                	}
                }, {
                	label: "备注", name: "info", width: 350, align: "center", editable: true, edittype: "textarea", editoptions: {rows:"2",width:"100%"}
                }, {
                	label: "操作", name: "operationCell", align: "center", width: 70, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		var rowId = options.rowId;
                		if(options.rowId.indexOf("_sum") < 0) {
                			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delSalaryContent_edit(\""+rowObject.id+"\", \""+rowId+"\")");
   							return buttons;
                		} else {
                			return "";
                		}
                	}
                }],
                ondblClickRow: function(id){
                	if(id.indexOf("_sum") >= 0) {
                		return false;
                	}
                	if(id && id!==salaryLastSel2){
                		jQuery("#salaryContentDatagrid_edit").saveRow(salaryLastSel2, {
                			url: "clientArray",
                			aftersavefunc: function() {
                				var rowData = $("#salaryContentDatagrid_edit").getRowData(salaryLastSel2);
                				var rowId = rowData.id;
                				$.ajax({
                					url: getRoot() + "workflow/salary/calculate.action",
                					data: parseRowData(rowData),
                					type: "POST",
                					async: false,
                					success: function(data) {
                						var json = eval("("+data+")");
                						console.log(json);
                						$("#salaryContentDatagrid_edit").setRowData(rowId, json);
                						countSalary();
                					},
                					error: function() {
                						BootstrapDialog.danger("计算工资异常");
                					}
                				});
                			}
                		});
                		jQuery("#salaryContentDatagrid_edit").editRow(id,true);
                		salaryLastSel2=id;
                	}
			    },
				viewrecords: true,
                rowNum: 1000,
                subGrid: true,
                subGridOptions: {
                	expandOnLoad: false
                },
                subGridRowColapsed: function(subgrid_id, rowid) {
                	return true;
                },
                subGridBeforeExpand: function(subgrid_id, rowid) {
                	if(rowid.indexOf("_sum") >= 0) {
                		return false;
                	} else {
                		return true;
                	}
                },
                subGridRowExpanded: function(subgrid_id, rowid) {
                	var rowData = $("#salaryContentDatagrid_edit").getRowData(rowid);
                	var id = rowData.id;
                	if(id) {
                		var tableId = subgrid_id+"_table";
                		var html = "<div style='padding: 10px;'>";
                		html = html + "<table id='"+tableId+"' class='scroll'></table>";
                		html = html + "</div>";
                		var subgrid = $("#"+subgrid_id).html(html);
                		$("#"+tableId).jqGrid({
                			url: getRoot() + "workflow/salary/queryContentOpinionsByContentId?salaryContent.id="+id,
                			datatype: "json",
                			height: "100%",
                			shrinkToFit: true,
			                autowidth: true,
			                scrollrows: false,
			                scroll: false,
			                caption: "工资项目审核意见",
                			colModel: [{
			                	label: "ID", name: "id", hidden: true
			                }, {
			                	label: "审核人", name: "userName", width: 150, align: "center"
			                }, {
			                	label: "审核意见", name: "opinion", width: 400, align: "center"
			                }, {
			                	label: "审核时间", name: "createDate", width: 150, align: "center"
			                }]
                		});
                	}
                }
            });
   			$("#salaryContentDatagrid_edit").jqGrid("setGroupHeaders", {
   				useColSpanStyle: true,
   				groupHeaders:[{
   					startColumnName: "actualDutyDay", numberOfColumns: 2, titleText: "实出勤"
   				}, {
   					startColumnName: "sickLeaveDeduct", numberOfColumns: 3, titleText: "出勤扣款"
   				}, {
   					startColumnName: "socialInsurance", numberOfColumns: 3, titleText: "个人应扣款"
   				}, {
   					startColumnName: "telCharge", numberOfColumns: 5, titleText: "补贴"
   				}, {
   					startColumnName: "childcareExpense", numberOfColumns: 6, titleText: "个税扣减专项附加扣除费用"
   				}, {
   					startColumnName: "companySocialInsurance", numberOfColumns: 2, titleText: "公司投保"
   				}]
   			});
   		}
   		
   		function buildSalaryAuditLogGrid_edit() {
   			$("#editSalaryAuditorlogsDatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=salary&wfId="+$("#editSalaryId").val(),
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
                pager: "#editSalaryAuditorlogsDatagridPager"
            });
   		}
		
		function delSalaryContent_edit(id, rowId) {
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
		   						url: getRoot() + "workflow/salary/delContentById.action",
			    				data: "salaryContent.id="+id,
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
			    						$("#salaryContentDatagrid_edit").delRowData(rowId);
			    						getEditSalaryTotalScore();
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#salaryContentDatagrid_edit").delRowData(rowId);
		            		getEditSalaryTotalScore();
		            	}
	   				}
	            }
   			});
		}
		
		function getEditSalaryTotalScore() {
			var rowDatas = $("#salaryContentDatagrid_edit").getRowData();
			var totalScore = 0;
			if(rowDatas && 0 < rowDatas.length) {
				var rowData;
				for(var i = 0; i < rowDatas.length; i ++) {
					rowData = rowDatas[i];
					try {
						if(rowData.scoreWeight) {
							var score = parseInt(rowData.scoreWeight);
							if(score) {
								totalScore += score;
							}
						}
					} catch(ex) {
						BootstrapDialog.danger("第"+(i+1)+"项项目分数填写错误");
						break;
					}
				}
			}
			$("#editSalaryForm").find("div.totalScore").html(totalScore);
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
		
		function parseRowData(rowData) {
			try {
				var contents = new Array();
				for(var key in rowData) {
					if("id" == key) {
						var value = rowData[key];
						if(value.indexOf("_sum") >= 0) {
							continue;
						}
						var re = /^[0-9]*$/;
   						if(!re.test(value)) {
   							value = "";
   						}
   						rowData[key] = value;
					}
					if(key != "operationCell") {
						contents.push("salaryContent."+key+"="+rowData[key]);
					}
				}
			} catch(ex) {
				
			}
			return contents.join("&");
		}
		
		/**
		 * 统计工资
		 */
		function countSalary() {
			var year = $("#editSalaryYear").val();
        	var month = $("#editSalaryMonth").val();
			var rowDatas = $("#salaryContentDatagrid_edit").getRowData();
			var rowIds = $("#salaryContentDatagrid_edit").getDataIDs();
			var sumHeaderId = undefined;
			var sumDataId = undefined;
			var benefitSumId = undefined;
			var totalSumId = undefined;
			for(var i = 0; i < rowIds.length; i ++) {
				var rowId = rowIds[i];
				if(rowId.indexOf("_sum_header") >= 0) {
					sumHeaderId = rowId;
				}
				if(rowId.indexOf("_sum_data") >= 0) {
					sumDataId = rowId;
				}
				if(rowId.indexOf("_benefit_sum") >= 0) {
					benefitSumId = rowId;
				}
				if(rowId.indexOf("_total_sum") >= 0) {
					totalSumId = rowId;
				}
			}
			
			var supposedTotal = 0;
			var actualTotal = 0;
			var telChargeTotal = 0;
			var mealSubsidyTotal = 0;
			var travelAllowanceTotal = 0;
			var housingSubsidyTotal = 0;
			var companySocialInsuranceTotal = 0;
			var companyHealthInsuranceTotal = 0;
			var benefitTotal = 0;
			var companyInsuranceTotal = 0;
			var totalSalary = 0;
			var commissionSum = 0;
			var socialInsuranceSum = 0;	//个人社保合计
			var healthInsuranceSum = 0;	//个人医保合计
			var insuranceSum = 0;	//个人医社保总计
			var taxSum = 0;
			for(var i = 0; i < rowDatas.length; i ++) {
				var rowData = rowDatas[i];
				if(rowData.id.indexOf("_sum_header") >= 0 || rowData.id.indexOf("_sum_data") >= 0
					|| rowData.id.indexOf("_benefit_sum") >= 0 || rowData.id.indexOf("_total_sum") >= 0) {
					continue;
				}
				supposedTotal += parseFloat(rowData.supposedTotalSalary);
				actualTotal += parseFloat(rowData.actualTotalSalary);
				telChargeTotal += parseFloat(rowData.telCharge);
				mealSubsidyTotal += parseFloat(rowData.mealSubsidy);
				travelAllowanceTotal += parseFloat(rowData.travelAllowance);
				housingSubsidyTotal += parseFloat(rowData.housingSubsidy);
				companySocialInsuranceTotal += parseFloat(rowData.companySocialInsurance);
				companyHealthInsuranceTotal += parseFloat(rowData.companyHealthInsurance);
				commissionSum += parseFloat(rowData.commission);
				socialInsuranceSum += parseFloat(rowData.socialInsurance);
				healthInsuranceSum += parseFloat(rowData.healthInsurance);
				taxSum += parseFloat(rowData.tax);
			}
			benefitTotal = telChargeTotal + mealSubsidyTotal + travelAllowanceTotal + housingSubsidyTotal;
			companyInsuranceTotal = companySocialInsuranceTotal + companyHealthInsuranceTotal;
			totalSalary = supposedTotal + benefitTotal + companyInsuranceTotal;
			
			supposedTotal = supposedTotal.toFixed(2);
			actualTotal = actualTotal.toFixed(2);
			telChargeTotal = telChargeTotal.toFixed(2);
			mealSubsidyTotal = mealSubsidyTotal.toFixed(2);
			travelAllowanceTotal = travelAllowanceTotal.toFixed(2);
			housingSubsidyTotal = housingSubsidyTotal.toFixed(2);
			companySocialInsuranceTotal = companySocialInsuranceTotal.toFixed(2);
			companyHealthInsuranceTotal = companyHealthInsuranceTotal.toFixed(2);
			benefitTotal = benefitTotal.toFixed(2);
			companyInsuranceTotal = companyInsuranceTotal.toFixed(2);
			totalSalary = totalSalary.toFixed(2);
			commissionSum = commissionSum.toFixed(2);
			socialInsuranceSum = socialInsuranceSum.toFixed(2);
			healthInsuranceSum = healthInsuranceSum.toFixed(2);
			insuranceSum = parseFloat(socialInsuranceSum) + parseFloat(healthInsuranceSum);
			try {
				insuranceSum = insuranceSum.toFixed(2);
			} catch(e) {
				console.log(e);
			}
			taxSum = taxSum.toFixed(2);
			if(!sumHeaderId) {
				var rowId = Math.random();
				rowId=rowId+"_sum_header";
				sumHeaderId = rowId;
				srcRowId = rowId;
	           	$("#salaryContentDatagrid_edit").addRowData(rowId, {
	           		id: rowId,
	           		year: year,
	           		month: month,
	           		userId: "",
	           		supposedDutyDay: "",
	           		actualDutyDay: "",
	           		actualDutyHour: "",
	           		basicSalary: "",
	           		achievementSalary: "",
	           		achievementScore: "",
	           		actualAchievementSalary: "",
	           		commission: "抽成合计",
	           		fullAttendanceAward: "",
	           		overtimePay: "",
	           		seniorityPay: "",
	           		sickLeaveDeduct: "",
	           		affairLeaveDeduct: "",
	           		lateDeduct: "",
	           		punishDeduct: "",
	           		supposedTotalSalary: "应发合计",
	           		socialInsurance: "个人社保合计",
	           		healthInsurance: "个人医保合计",
	           		totalInsurance: "总计",
	           		telCharge: "话补",
	           		mealSubsidy: "餐补",
	           		travelAllowance: "车补",
	           		housingSubsidy: "住房补贴",
	           		totalSubsidy: "总计",
	           		other: "",
	           		childcareExpense: "",
	           		continuingEducationFee: "",
	           		seriousIllnessExpense: "",
	           		housingLoan: "",
	           		housingRent: "",
	           		alimony: "",
	           		pretaxSalary: "",
	           		tax: "",
	           		actualTotalSalary: "实发合计",
	           		companySocialInsurance: "社保",
	           		companyHealthInsurance: "医保",
	           		info: "",
	           		operationCell: "",
	           		tax: "个税合计"
	           	});
			}
			if(!sumDataId) {
				rowId = Math.random();
	           	rowId = rowId+"_sum_data"
	           	$("#salaryContentDatagrid_edit").addRowData(rowId, {
	           		id: rowId,
					supposedTotalSalary: supposedTotal,
					actualTotalSalary: actualTotal,
					telCharge: telChargeTotal,
					mealSubsidy: mealSubsidyTotal,
					travelAllowance: travelAllowanceTotal,
					housingSubsidy: housingSubsidyTotal,
					companySocialInsurance: companySocialInsuranceTotal,
					companyHealthInsurance: companyHealthInsuranceTotal,
					operationCell: "",
					commission: commissionSum,
					socialInsurance: socialInsuranceSum,
					healthInsurance: healthInsuranceSum,
					totalInsurance: insuranceSum,
					tax: taxSum
				});
			} else {
				$("#salaryContentDatagrid_edit").setRowData(sumDataId, {
					supposedTotalSalary: supposedTotal,
					actualTotalSalary: actualTotal,
					telCharge: telChargeTotal,
					mealSubsidy: mealSubsidyTotal,
					travelAllowance: travelAllowanceTotal,
					housingSubsidy: housingSubsidyTotal,
					companySocialInsurance: companySocialInsuranceTotal,
					companyHealthInsurance: companyHealthInsuranceTotal,
					operationCell: "",
					commission: commissionSum,
					socialInsurance: socialInsuranceSum,
					healthInsurance: healthInsuranceSum,
					totalInsurance: insuranceSum,
					tax: taxSum
				});
			}
			if(!benefitSumId) {
				rowId = Math.random();
	           	rowId = rowId+"_benefit_sum";
	           	benefitSumId = rowId;
	           	$("#salaryContentDatagrid_edit").addRowData(rowId, {
	           		id: rowId,
					tax: "福利合计",
					actualTotalSalary: supposedTotal,
					telCharge: benefitTotal,
					companySocialInsurance: companyInsuranceTotal
				});
			} else {
				var gridId = "salaryContentDatagrid_edit";
				$("#"+gridId).find("tr[id='"+benefitSumId+"']").find("td[aria-describedby='"+gridId+"_tax']").html("福利合计");
				$("#"+gridId).find("tr[id='"+benefitSumId+"']").find("td[aria-describedby='"+gridId+"_telCharge']").html(benefitTotal);
				$("#"+gridId).find("tr[id='"+benefitSumId+"']").find("td[aria-describedby='"+gridId+"_actualTotalSalary']").html(supposedTotal);
				$("#"+gridId).find("tr[id='"+benefitSumId+"']").find("td[aria-describedby='"+gridId+"_companySocialInsurance']").html(companyInsuranceTotal);
				/*$("#salaryContentDatagrid_edit").setRowData(benefitSumId, {
					other: "福利合计",
					actualTotalSalary: supposedTotal,
					telCharge: benefitTotal,
					companySocialInsurance: companyInsuranceTotal
				});*/
			}
			mergeCell("salaryContentDatagrid_edit", benefitSumId, "telCharge", "housingSubsidy", 4, benefitTotal);
			mergeCell("salaryContentDatagrid_edit", benefitSumId, "companySocialInsurance", "companyHealthInsurance", 2, companyInsuranceTotal)
			
			if(!totalSumId) {
				rowId = Math.random();
	           	rowId = rowId+"_total_sum";
	           	totalSumId = rowId;
	           	$("#salaryContentDatagrid_edit").addRowData(rowId, {
	           		id: rowId,
					tax: "工资总计",
					actualTotalSalary: totalSalary
				});
			} else {
				$("#salaryContentDatagrid_edit").setRowData(totalSumId, {
					tax: "工资总计",
					actualTotalSalary: totalSalary
				});
			}
			//mergeCell("salaryContentDatagrid_edit", totalSumId, "actualTotalSalary", "companyHealthInsurance", 7, totalSalary);
			return sumHeaderId;
		}
		
		function mergeCell(gridId, rowid, name, endName, size, value) {
			var $startTd = $("#"+gridId).find("tr[id='"+rowid+"']").find("td[aria-describedby='"+gridId+"_"+name+"']");
			var startIndex = $startTd.index();
			
			var $endTd = $("#"+gridId).find("tr[id='"+rowid+"']").find("td[aria-describedby='"+gridId+"_"+endName+"']");
			var endIndex = $endTd.index();
			var len = endIndex - startIndex;
			if($startTd.length > 0 && $endTd.length > 0) {
				if(len > 0) {
					for(var i = 0; i < len; i ++) {
						$startTd.next().remove();
					}
				}
			}
			$("#"+gridId).find("tr[id='"+rowid+"']").find("td[aria-describedby='"+gridId+"_"+name+"']").attr("colspan", size);
			$("#"+gridId).find("tr[id='"+rowid+"']").find("td[aria-describedby='"+gridId+"_"+name+"']").html(value);
		}
		
		function saveRowSalary_edit() {
			if(salaryLastSel2 && ""!=salaryLastSel2) {
				jQuery("#salaryContentDatagrid_edit").saveRow(salaryLastSel2, {
	       			url: "clientArray",
	       			aftersavefunc: function() {
	       				var rowData = $("#salaryContentDatagrid_edit").getRowData(salaryLastSel2);
	       				var rowId = rowData.id
	       				$.ajax({
	       					url: getRoot() + "workflow/salary/calculate.action",
	       					data: parseRowData(rowData),
	       					type: "POST",
	       					success: function(data) {
	       						var json = eval("("+data+")");
	       						$("#salaryContentDatagrid_edit").setRowData(rowId, json);
	       						countSalary();
	       					},
	       					error: function() {
	       						BootstrapDialog.danger("计算工资异常");
	       					}
	       				});
	       			}
	           	});
	           	salaryLastSel2 = "";
			}
		}
   	</script>
  </body>
</html>
