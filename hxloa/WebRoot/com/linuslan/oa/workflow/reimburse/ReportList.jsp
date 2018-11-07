<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>待登录用户审核的报销单列表</title>
    
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
    <div>
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<form id="search_form_reimburse_report_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">费用时间始：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.moneyDate_start" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">费用时间止：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.moneyDate_end" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">报销部门：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="departmentTree"></div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">出款公司：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.companyId" class="form-control select2">
									
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">申请人：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.userId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">收款方：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.receiver" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">编号：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.serialNo" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">状态：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.payStatus" class="form-control select2">
									<option value="">请选择</option>
									<option value="3,5,6">待审核</option>
									<option value="7">待打款</option>
									<option value="4">已打款</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<button type="button" class="btn btn-info btn-sm pull-right" data-toggle="tooltip" title="重置" id="resetSearchReimburseReportList"><i class="fa fa-fw fa-undo"></i>重置</button>
								<button type="button" class="btn btn-info btn-sm pull-right margin-right5" data-toggle="tooltip" title="查询" id="searchReimburseReportList"><i class="fa fa-fw fa-search"></i>查询</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="box-body top-dotted-border bottom-dotted-border form-horizontal" id="sumReportPage_reimburse">
   					<div class="form-group">
						<label for="name" class="col-md-2 col-sm-4 col-xs-4 control-label">已打款总额：</label>
						<div class="col-md-2 col-sm-8 col-xs-8 sumPaid left-label">
							
						</div>
						<label for="name" class="col-md-2 col-sm-4 col-xs-4 control-label">待打款总额：</label>
						<div class="col-md-2 col-sm-8 col-xs-8 sumPay left-label">
							
						</div>
						<label for="name" class="col-md-2 col-sm-4 col-xs-4 control-label">待审核总额：</label>
						<div class="col-md-2 col-sm-8 col-xs-8 sumAudit left-label">
							
						</div>
					</div>
   				</div>
   				<div class="box-body">
   					<table id="reimburseReportDatagrid"></table>
   					<div id="reimburseReportDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var reportReimburseGrid;
   		var reportReimburseDialog;
   		$(function() {
   			sumReportPage_reimburse();
   			$("#search_form_reimburse_report_list").find(".departmentTree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "paramMap.reimburseDeptId",
				pidField: "pid"
			});
			$("#search_form_reimburse_report_list").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
			initSelect_reimburseReport("search_form_reimburse_report_list");
			$("#searchReimburseReportList").click(function() {
   				$("#reimburseReportDatagrid").setGridParam({
   					postData: parsePostData("search_form_reimburse_report_list")
   				}).trigger("reloadGrid");
   				sumReportPage_reimburse();
   			});
   			
   			$("#resetSearchReimburseReportList").click(function() {
   				$("#search_form_reimburse_report_list")[0].reset();
   				$("#search_form_reimburse_report_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			
   			reportReimburseGrid = $("#reimburseReportDatagrid").jqGrid({
                url: getRoot() + "workflow/reimburse/queryReportPage.action",
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                rownumbers: true,
                colModel: [{
                	label: "ID", name: "id", hidden: true, key: true
                }, {
                	label: "编号", name: "serialNo", width: 100, align: "center"
                }, {
                	label: "申请人", name: "userName", width: 100, align: "center"
                }, {
                	label: "出款公司", name: "companyName", width: 200, align: "center"
                }, {
                	label: "报销部门", name: "reimburseDeptName", width: 200, align: "center"
                }, {
                	label: "总金额", name: "totalMoney", width: 150, align: "center"
                }, {
                	label: "收款方", name: "receiver", width: 150, align: "center"
                }, {
                	label: "流程状态", name: "flowStatus", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		return getFlowStatus(rowObject.status);
                	}
                }, {
                	label: "当前审核组", name: "auditors", width: 150, align: "center"
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewReportReimburse("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#reimburseReportDatagridpager"
            });
   		});
   		
   		function viewReportReimburse(id, passBtn, rejectBtn) {
   			reportReimburseDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "60%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/reimburse/queryById.action?returnType=audit&reimburse.id="+id),
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
   		
   		function initSelect_reimburseReport(id) {
   			try {
   				$("#"+id+" select.select2").select2();
   				$.ajax({
   	   				url: getRoot() + "workflow/reimburse/initSelect.action",
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						for(var key in json) {
   	   							var selector = "#"+id+" select[name='paramMap."+key+"']";
   	   							initOptions_reimburseReport(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化报销查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_reimburseReport(data, selector) {
   			if(data && data.length > 0 && selector) {
   				var option = document.createElement("option");
   				option.value = "";
   				option.text = "请选择";
   				$(selector).append(option);
   				for(var i = 0;i < data.length; i ++) {
   					var opData = data[i];
   					var option = document.createElement("option");
   					$(selector).append(option);
   					if(opData.id) {
   						option.value = opData.id;
   					} else if(opData.ID) {
   						option.value = opData.ID;
   					}
   					if(opData.name) {
   						option.text = opData.name;
   					} else if(opData.text) {
   						option.text = opData.text;
   					} else if(opData.TEXT) {
   						option.text = opData.TEXT;
   					} else if(opData.NAME) {
   						option.text = opData.NAME;
   					}
   				}
   			}
   		}
   		
   		function sumReportPage_reimburse() {
   			$.ajax({
   				url: getRoot() + "workflow/reimburse/sumReportPage.action",
   				data: $("#search_form_reimburse_report_list").serialize(),
   				type: "POST",
   				success: function(data) {
   					try {
   						if(data) {
	   						var json = eval("("+data+")");
	   						for(var key in json) {
	   							var sumAmount = json[key];
	   							sumAmount = sumAmount.toFixed(2);
	   							$("#sumReportPage_reimburse ."+key).html(sumAmount);
	   						}
	   					}
   					} catch(ex) {
   						BootstrapDialog.danger("统计汇总页面异常，"+ex);
   					}
   				},
   				error: function() {
   					BootstrapDialog.danger("系统异常，请联系管理员！");
   				}
   			});
   		}
   	</script>
  </body>
</html>
