<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>待登录用户审核的企业付款列表</title>
    
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
   				<form id="search_form_invoice_report_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">开票时间始：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.invoiceDate_start" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">开票时间止：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.invoiceDate_end" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">收入归属部门：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="departmentTree"></div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">开票公司：</label>
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
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">开票类型：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.invoiceType" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">开票名称：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.title" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">编号：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.serialNo" type="text" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">开票状态：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.invoiceStatus" class="form-control select2">
									<option value="">请选择</option>
									<option value="2">未回款</option>
									<option value="1">部分回款</option>
									<option value="0">已回款</option>
									<option value="5">未开票作废</option>
									<option value="6">已开票作废</option>
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">开票项目：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.content" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">备注：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.remark" type="text" class="form-control">
							</div>
							
							<div class="col-md-3 col-sm-6">
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchInvoiceReportList"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm margin-left5" data-toggle="tooltip" title="重置" id="resetSearchInvoiceReportList"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="box-body top-dotted-border bottom-dotted-border form-horizontal" id="sumReportPage_invoice">
   					<div class="form-group">
						<label for="name" class="col-md-2 col-sm-4 col-xs-4 control-label">开票总额：</label>
						<div class="col-md-2 col-sm-8 col-xs-8 sumTotal left-label">
							
						</div>
						<label for="name" class="col-md-2 col-sm-4 col-xs-4 control-label">实回款总额：</label>
						<div class="col-md-2 col-sm-8 col-xs-8 sumRestream left-label">
							
						</div>
						<label for="name" class="col-md-2 col-sm-4 col-xs-4 control-label">未回款总额：</label>
						<div class="col-md-2 col-sm-8 col-xs-8 sumUnRestream left-label">
							
						</div>
					</div>
   				</div>
   				<div class="box-body">
   					<table id="invoiceReportDatagrid"></table>
   					<div id="invoiceReportDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var reportInvoiceGrid;
   		var reportInvoiceDialog;
   		$(function() {
   			sumReportPage_invoice();
   			$("#search_form_invoice_report_list").find(".departmentTree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "paramMap.incomeDeptId",
				pidField: "pid"
			});
			$("#search_form_invoice_report_list").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
			initSelect_invoiceReport("search_form_invoice_report_list");
			$("#searchInvoiceReportList").click(function() {
   				$("#invoiceReportDatagrid").setGridParam({
   					postData:parsePostData("search_form_invoice_report_list")
   				}).trigger("reloadGrid");
   				sumReportPage_invoice();
   			});
   			
   			$("#resetSearchInvoiceReportList").click(function() {
   				$("#search_form_invoice_report_list")[0].reset();
   				$("#search_form_invoice_report_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			reportInvoiceGrid = $("#invoiceReportDatagrid").jqGrid({
                url: getRoot() + "workflow/invoice/queryReportPage.action",
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
                rownumbers: true,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "ID", name: "id", hidden: true, key: true
                }, {
                	label: "编号", name: "serialNo", width: 100, align: "center"
                }, {
                	label: "申请人", name: "userName", width: 100, align: "center"
                }, {
                	label: "归属部门", name: "userDeptName", width: 100, align: "center"
                }, {
                	label: "收入归属部门", name: "incomeDeptName", width: 200, align: "center"
                }, {
                	label: "开票公司", name: "companyName", width: 150, align: "center"
                }, {
                	label: "开票名称", name: "title", width: 200, align: "left"
                }, {
                	label: "开票时间", name: "invoiceDate", width: 100, align: "center"
                }, {
                	label: "金额", name: "invoiceMoney", width: 100, align: "center"
                }, {
                	label: "应回款", name: "supposedMoney", width: 100, align: "center"
                }, {
                	label: "实回款", name: "actualMoney", width: 100, align: "center"
                }, {
                	label: "开票类型", name: "invoiceTypeName", width: 100, align: "center"
                }, {
                	label: "开票内容", name: "content", width: 300, align: "center"
                }, {
                	label: "开票状态", name: "invoiceStatus", width: 100, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		var invoiceStatus = rowObject.invoiceStatus;
                		if(0 == invoiceStatus) {
                			return "已回款";
                		} else if(1 == invoiceStatus) {
                			return "部分回款";
                		} else if(2 == invoiceStatus) {
                			return "未回款";
                		} else if(5 == invoiceStatus) {
                			return "未开票作废";
                		} else if(6 == invoiceStatus) {
                			return "已开票作废";
                		} else {
                			return "未回款";
                		}
                	}
                }, {
                	label: "流程状态", name: "flowStatus", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		return getFlowStatus(rowObject.status);
                	}
                }, {
                	label: "当前审核", name: "auditors", width: 100, align: "center"
                }, {
                	label: "操作", width: 70, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewReportInvoice("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+", "+rowObject.flowStatus+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#invoiceReportDatagridpager"
            });
   		});
   		
   		function viewReportInvoice(id, passBtn, rejectBtn, flowStatus) {
   			
   			var returnType = "view";
   			reportInvoiceDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "50%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/invoice/queryById.action?returnType="+returnType+"&invoice.id="+id),
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
   		
   		function initSelect_invoiceReport(id) {
   			try {
   				$("#"+id+" select.select2").select2();
   				$.ajax({
   	   				url: getRoot() + "workflow/invoice/initSelect.action",
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						for(var key in json) {
   	   							var selector = "#"+id+" select[name='paramMap."+key+"']";
   	   							initOptions_invoiceReport(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化企业付款查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_invoiceReport(data, selector) {
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
   		
   		function sumReportPage_invoice() {
   			$.ajax({
   				url: getRoot() + "workflow/invoice/sumReportPage.action",
   				data: $("#search_form_invoice_report_list").serialize(),
   				type: "POST",
   				success: function(data) {
   					try {
   						if(data) {
	   						var json = eval("("+data+")");
	   						for(var key in json) {
	   							var sumAmount = json[key];
	   							sumAmount = sumAmount.toFixed(2);
	   							$("#sumReportPage_invoice ."+key).html(sumAmount);
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
