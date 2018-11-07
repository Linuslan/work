<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>薪资列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style type="text/css">
		/*#salarydatagrid_report_departmentName {
			height: 68px;
		}
		#salarydatagrid_report_frozen tr td {
			height: 38px;
		}*/
	</style>
  </head>
  
  <body>
    <div id="salary_list_report">
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<form id="search_form_slary_list_report" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">日期：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.date" type="text" class="form-control pull-right date">
								</div>
							</div>
							<div class="col-md-3 col-sm-3">
								<button type="button" class="btn btn-info btn-sm margin-right5" data-toggle="tooltip" title="查询" id="searchSalary_report"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="重置" id="resetSearchSalary_report"><i class="fa fa-fw fa-undo"></i>重置</button>
								
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-info btn-sm" data-toggle="tooltip" title="导出" id="exportSalary_report"><i class="fa fa-fw fa-share-square-o"></i>导出</button>
   				</div>
   				<div class="box-body">
   					<table id="salarydatagrid_report"></table>
   					<div id="salarydatagrid_reportpager_report"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var salaryGrid;
   		var salaryDialog;
   		var salaryLastSel2;
   		$(function() {
   			$("#search_form_slary_list_report input.date").datepicker({
   				startView: 1,
   				maxViewMode: 1,
   				minViewMode:1,
   				forceParse: false,
   				language: "zh-CN",
   				format: "yyyy-mm"
   			});
   			
   			$("#searchSalary_report").click(function() {
   				$("#salarydatagrid_report").setGridParam({
   					postData:parsePostData("search_form_slary_list_report")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchSalary_report").click(function() {
   				$("#search_form_slary_list_report")[0].reset();
   				$("#search_form_slary_list_report select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			
   			salaryGrid = $("#salarydatagrid_report").jqGrid({
                url: getRoot() + "workflow/salary/queryReportPage.action",
                mtype: "POST",
                rownumbers: true,
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
                height: getHeight(400)+"px",
				styleUI : "Bootstrap",
                datatype: "json",
                footerrow: true,
                userDataOnFooter: true,
                loadComplete: function() {
                	//$('.ui-jqgrid-bdiv').scrollTop(0);
                	//$(this).setJqGridRowHeight(28);
                },
                gridComplete: function() {
                	//$("#salarydatagrid_report").setGridWidth($("#salary_list_report").width()*0.99);
                	//$("#salarydatagrid_report").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
                	//$("#salarydatagrid_report").jqGrid("setFrozenColumns");
                	
                	//$(".ui-jqgrid-sdiv").show();
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
                	//setTimeout("hackHeight(\"#salarydatagrid_report\")", 0);
                	//hackHeight("#salarydatagrid_report");
                },
                colModel: [{
                	label: "薪资年月", name: "year", width: 80, align: "center",/* frozen: true,*/
                	formatter: function(cellValue, options, rowObject) {
                		return rowObject.year+"-"+rowObject.month;
                	}
                }, {
                	label: "部门", name: "departmentName", width: 130, align: "center"/*, frozen: true*/
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
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewSalary_report("+rowObject.id+", \""+rowObject.allFlowStatus+"\", "+rowObject.status+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                rowNum: 20,
                pager: "#salarydatagrid_reportpager_report"
            });
            
            $("#salarydatagrid_report").jqGrid("setGroupHeaders", {
   				useColSpanStyle: true,
   				groupHeaders:[{
   					startColumnName: "travelAllowanceSum", numberOfColumns: 4, titleText: "补贴"
   				}, {
   					startColumnName: "socialInsuranceSum", numberOfColumns: 3, titleText: "公司投保"
   				}]
   			});
   			
   			$("#exportSalary_report").click(function() {
	  			var year = $("#search_form_slary_list_report input[name='paramMap.date']").val().split("-")[0];
	  			var month = $("#search_form_slary_list_report input[name='paramMap.date']").val().split("-")[1]
	  			if(!year) {
	  				var currentDate = new Date();
	  				year = currentDate.getFullYear();
	  			}
	  			if(!month) {
	  				var currentDate = new Date();
	  				month = currentDate.getMonth();
	  				if(month == 0) {
	  					month = 12;
	  				}
	  			}
	  			BootstrapDialog.confirm({
		            title: "温馨提示",
		            message: "确定导出<font color='red'>"+year+"年"+month+"月</font>的工资吗？",
		            type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
		            closable: true, // <-- Default value is false
		            draggable: true, // <-- Default value is false
		            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
		            btnOKLabel: "确定", // <-- Default value is 'OK',
		            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
		            callback: function(y) {
		                // result will be true if button was click, while it will be false if users close the dialog directly.
		                if(y) {
		                	var form = $("<form>");
		  					form.attr("style", "display:none");
		  					form.attr("action", getRoot() + "workflow/salary/export.action");
		  					form.attr("method", "POST");
		  					var input = $("<input>");
		  					input.attr("value", year);
		  					input.attr("name", "year");
		  					var monthInput = $("<input>");
		  					monthInput.attr("value", month);
		  					monthInput.attr("name", "month");
		  					$("body").append(form);
		  					form.append(input);
		  					form.append(monthInput);
		  					form.submit();
		  					form.remove();
		                }
		            }
	  			});
   			});
   		});
   		
   		function viewSalary_report(id, allFlowStatus, status) {
   			var returnType = "view";
   			
   			salaryDialog = BootstrapDialog.show({
			    title: "工资详情",
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
			    	cssClass: "btn-danger",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
   		}
   		
   	</script>
  </body>
</html>
