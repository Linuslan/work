<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>待登录用户审核的绩效列表</title>
    
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
    <div id="salary_auditlist">
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<div class="box-header with-border">
   					<h3 class="box-title">已审薪资</h3>
   					<div class="box-tools pull-right">
   						<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
	               	</div>
   				</div>
   				<div class="box-body">
   					<table id="salaryAuditedDatagrid"></table>
   					<div id="salaryAuditedDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var auditedSalaryGrid;
   		var auditedSalaryDialog;
   		$(function() {
   			auditedSalaryGrid = $("#salaryAuditedDatagrid").jqGrid({
                url: getRoot() + "workflow/salary/queryAuditedPage.action",
                mtype: "POST",
                rownumbers: true,
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                footerrow: true,
                userDataOnFooter: true,
                gridComplete: function() {
                	/*$("#salaryAuditedDatagrid").setGridWidth($("#salary_auditlist").width()*0.99);
                	$("#salaryAuditedDatagrid").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
                	$("#salaryAuditedDatagrid").jqGrid("setFrozenColumns");
                
                	$(".ui-jqgrid-sdiv").show();*/
                	var supposedSum = $(this).getCol("supposedSum",false,"sum");
                	var actualSum = $(this).getCol("actualSum",false,"sum");
                	var travelAllowanceSum = $(this).getCol("travelAllowanceSum",false,"sum");
                	var mealSubsidySum = $(this).getCol("mealSubsidySum",false,"sum");
                	var telChargeSum = $(this).getCol("telChargeSum",false,"sum");
                	var housingSubsidySum = $(this).getCol("housingSubsidySum",false,"sum");
                	var benefitSum = $(this).getCol("benefitSum",false,"sum");
                	var socialInsuranceSum = $(this).getCol("socialInsuranceSum",false,"sum");
                	var healthInsuranceSum = $(this).getCol("healthInsuranceSum",false,"sum");
                	var insuranceSum = $(this).getCol("insuranceSum",false,"sum");
                	var totalSum = $(this).getCol("totalSum",false,"sum");
                	$(this).footerData("set", {
                		"departmentName": "合计",
                		"supposedSum": supposedSum.toFixed(2),
                		"actualSum": actualSum.toFixed(2),
                		"travelAllowanceSum": travelAllowanceSum.toFixed(2),
                		"mealSubsidySum": mealSubsidySum.toFixed(2),
                		"telChargeSum": telChargeSum.toFixed(2),
                		"housingSubsidySum": housingSubsidySum.toFixed(2),
                		"benefitSum": benefitSum.toFixed(2),
                		"socialInsuranceSum": socialInsuranceSum.toFixed(2),
                		"healthInsuranceSum": healthInsuranceSum.toFixed(2),
                		"insuranceSum": insuranceSum.toFixed(2),
                		"totalSum": totalSum.toFixed(2)
                	});
                	//setTimeout("hackHeight(\"#salaryAuditedDatagrid\")", 0);
                },
                colModel: [{
                	label: "薪资年月", name: "date", width: 80, align: "center", frozen: true,
                	formatter: function(cellvalue, options, rowObject) {
                		return rowObject.year+"-"+rowObject.month;
                	}
                }, {
                	label: "归属部门", name: "departmentName", width: 130, align: "center", frozen: true
                }, {
                	label: "应发合计", name: "supposedSum", width: 80, align: "center"
                }, {
                	label: "实发工资", name: "actualSum", width: 80, align: "center"
                }, {
                	label: "车补", name: "travelAllowanceSum", width: 80, align: "center"
                }, {
                	label: "餐补", name: "mealSubsidySum", width: 80, align: "center"
                }, {
                	label: "话补", name: "telChargeSum", width: 80, align: "center"
                }, {
                	label: "住房补贴", name: "housingSubsidySum", width: 80, align: "center"
                }, {
                	label: "福利合计", name: "benefitSum", width: 80, align: "center"
                }, {
                	label: "社保", name: "socialInsuranceSum", width: 80, align: "center"
                }, {
                	label: "医保", name: "healthInsuranceSum", width: 80, align: "center"
                }, {
                	label: "总投保", name: "insuranceSum", width: 80, align: "center"
                }, {
                	label: "总合计", name: "totalSum", width: 80, align: "center"
                }, {
                	label: "流程状态", name: "flowStatus", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		return getFlowStatus(rowObject.status);
                	}
                }, {
                	label: "当前审核", name: "auditors", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		var flowStatus = rowObject.allFlowStatus;
                		var text = cellValue;
                		if(flowStatus) {
                			var statusArr = flowStatus.split(",");
                			for(var i = 0; i < statusArr.length; i ++) {
                				var status = statusArr[i];
                				if(5 == status) {
                					text = "";
                					break;
                				}
                			}
                		}
                		return text;
                	}
                }, {
                	label: "操作", width: 200, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewAuditedSalary("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: getHeight(400)+"px",
                //width: "100%",
                rowNum: 20,
                pager: "#salaryAuditedDatagridpager"
            });
            
            $("#salaryAuditedDatagrid").jqGrid("setGroupHeaders", {
   				useColSpanStyle: true,
   				groupHeaders:[{
   					startColumnName: "travelAllowanceSum", numberOfColumns: 4, titleText: "补贴"
   				}, {
   					startColumnName: "socialInsuranceSum", numberOfColumns: 3, titleText: "公司投保"
   				}]
   			});
   		});
   		
   		function viewAuditedSalary(id, passBtn, rejectBtn, allFlowStatus) {
   			var returnType = "view";
   			auditedSalaryDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "99%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/salary/queryById.action?returnType="+returnType+"&salary.id="+id),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [{
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-warning",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
   		}
   	</script>
  </body>
</html>
