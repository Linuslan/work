<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>登录用户企业付款申请单列表</title>
    
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
   				<form id="search_form_invoice_list" action="" class="form-horizontal">
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
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">开票项目：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.content" type="text" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">备注：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.remark" type="text" class="form-control">
							</div>
							<div class="col-md-3 col-sm-6">
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchInvoiceList"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm margin-left5" data-toggle="tooltip" title="重置" id="resetSearchInvoiceList"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="开票申请" id="addInvoice"><i class="fa fa-fw fa-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="invoicedatagrid"></table>
   					<div id="invoicedatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var invoiceGrid;
   		var invoiceDialog;
   		$(function() {
   			$("#search_form_invoice_list").find(".departmentTree").combotree({
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
			$("#search_form_invoice_list").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
			initSelect_invoice("search_form_invoice_list");
			$("#searchInvoiceList").click(function() {
   				$("#invoicedatagrid").setGridParam({
   					postData:parsePostData("search_form_invoice_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchInvoiceList").click(function() {
   				$("#search_form_invoice_list")[0].reset();
   				$("#search_form_invoice_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			invoiceGrid = $("#invoicedatagrid").jqGrid({
                url: getRoot() + "workflow/invoice/queryPage.action",
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
                	label: "编号", name: "serialNo", width: 100, align: "center"
                }, {
                	label: "收入归属部门", name: "incomeDeptName", width: 200, align: "center"
                }, {
                	label: "开票公司", name: "companyName", width: 150, align: "center"
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
                		/*var flowStatus = rowObject.flowStatus;
                		if(0 == flowStatus) {
                			return "未提交";
                		} else if(1 == flowStatus) {
                			return "退回";
                		} else if(2 == flowStatus) {
                			return "撤销";
                		} else if(4 == flowStatus) {
                			return "完成";
                		} else if(5 == flowStatus) {
                			return "待开票";
                		} else if(6 == flowStatus) {
                			return "待回款";
                		} else {
                			//状态为3和7的都统一归为待审核
                			return "待审核";
                		}*/
                		return getFlowStatus(rowObject.status);
                	}
                }, {
                	label: "当前审核组", name: "auditors", width: 150, align: "center"
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewInvoice("+rowObject.id+")");
                		buttons = buttons + createBtn("打印", "btn-info btn-xs", "fa-print", "printInvoice("+rowObject.id+")");
                		if(rowObject.status == 0 || rowObject.status == 1 || rowObject.status == 2) {
                			buttons = buttons + createBtn("修改", "btn-info btn-xs", "fa-pencil", "editInvoice("+rowObject.id+")");
                			buttons = buttons + createBtn("提交", "btn-success btn-xs", "fa-share", "commitInvoice("+rowObject.id+")");
                			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delInvoice("+rowObject.id+")");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#invoicedatagridpager"
            });
   			$("#addInvoice").click(function() {
   				invoiceDialog = BootstrapDialog.show({
				    title: "新增申请",
				    width: "50%",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "workflow/invoice/queryById.action?returnType=add"),
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
				    		try {
				    			var form = dialog.getModalBody().find("form");
				    			var $button = this;
				    			$.ajax({
				    				url: getRoot() + "workflow/invoice/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						invoiceGrid.jqGrid().trigger("reloadGrid");
				    					} else {
				    						BootstrapDialog.danger(json.msg);
				    					}
				    				},
				    				error: function() {
				    					BootstrapDialog.danger("系统异常，请联系管理员！");
				    				},
				    				beforeSend: function() {
				    					dialog.enableButtons(false);
				    					dialog.setClosable(false);
				    					$button.spin();
				    				},
				    				complete: function() {
				    					dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    					$button.stopSpin();
				    				}
				    			});
				    		} catch(e) {
				    			dialog.enableButtons(true);
			    				dialog.setClosable(true);
				    			BootstrapDialog.danger("系统异常，请联系管理员！");
				    		}
				    	}
				    }, {
				    	label: "提交",
				    	icon: "fa fa-fw fa-share",
				    	cssClass: "btn-success",
				    	action: function(dialog) {
				    		try {
				    			var $button = this;
				    			BootstrapDialog.confirm({
						            title: "温馨提示",
						            message: "您确定提交吗？",
						            type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
						            closable: true, // <-- Default value is false
						            draggable: true, // <-- Default value is false
						            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
						            btnOKLabel: "确定", // <-- Default value is 'OK',
						            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
						            callback: function(y) {
						                // result will be true if button was click, while it will be false if users close the dialog directly.
						                if(y) {
						   					var form = dialog.getModalBody().find("form");
							    			$.ajax({
							    				url: getRoot() + "workflow/invoice/commit.action",
							    				data: form.serialize(),
							    				type: "POST",
							    				success: function(data) {
							    					var json = eval("("+data+")");
							    					if(json.success) {
							    						BootstrapDialog.success(json.msg);
							    						dialog.close();
							    						invoiceGrid.jqGrid().trigger("reloadGrid");
							    					} else {
							    						BootstrapDialog.danger(json.msg);
							    					}
							    				},
							    				error: function() {
							    					BootstrapDialog.danger("系统异常，请联系管理员！");
							    				},
							    				beforeSend: function() {
							    					dialog.enableButtons(false);
							    					dialog.setClosable(false);
							    					$button.spin();
							    				},
							    				complete: function() {
							    					dialog.enableButtons(true);
							    					dialog.setClosable(true);
							    					$button.stopSpin();
							    				}
							    			});
						   				}
						            }
					   			});
				    			
				    		} catch(e) {
				    			dialog.enableButtons(true);
			    				dialog.setClosable(true);
				    			BootstrapDialog.danger("系统异常，请联系管理员！");
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
   		});
   		
   		function editInvoice(id) {
   			invoiceDialog = BootstrapDialog.show({
			    title: "编辑申请单",
			    width: "50%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/invoice/queryById.action?returnType=edit&invoice.id="+id),
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
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			var $button = this;
			    			$.ajax({
			    				url: getRoot() + "workflow/invoice/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						invoiceGrid.jqGrid().trigger("reloadGrid");
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				},
			    				beforeSend: function() {
			    					dialog.enableButtons(false);
			    					dialog.setClosable(false);
			    					$button.spin();
			    				},
			    				complete: function() {
			    					dialog.enableButtons(true);
			    					dialog.setClosable(true);
			    					$button.stopSpin();
			    				}
			    			});
			    		} catch(e) {
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "提交",
			    	icon: "fa fa-fw fa-share",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		var $button = this;
			    		try {
			    			BootstrapDialog.confirm({
					            title: "温馨提示",
					            message: "您确定提交吗？",
					            type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
					            closable: true, // <-- Default value is false
					            draggable: true, // <-- Default value is false
					            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
					            btnOKLabel: "确定", // <-- Default value is 'OK',
					            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
					            callback: function(y) {
					                // result will be true if button was click, while it will be false if users close the dialog directly.
					                if(y) {
					   					var form = dialog.getModalBody().find("form");
						    			$.ajax({
						    				url: getRoot() + "workflow/invoice/commit.action",
						    				data: form.serialize(),
						    				type: "POST",
						    				success: function(data) {
						    					var json = eval("("+data+")");
						    					if(json.success) {
						    						BootstrapDialog.success(json.msg);
						    						dialog.close();
						    						invoiceGrid.jqGrid().trigger("reloadGrid");
						    					} else {
						    						BootstrapDialog.danger(json.msg);
						    					}
						    				},
						    				error: function() {
						    					BootstrapDialog.danger("系统异常，请联系管理员！");
						    				},
						    				beforeSend: function() {
						    					dialog.enableButtons(false);
						    					dialog.setClosable(false);
						    					$button.spin();
						    				},
						    				complete: function() {
						    					dialog.enableButtons(true);
						    					dialog.setClosable(true);
						    					$button.stopSpin();
						    				}
						    			});
					   				}
					            }
				   			});
			    		} catch(e) {
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
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
   		}
   		
   		function commitInvoice(id) {
   			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定提交吗？",
	            type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	   					$.ajax({
	   						url: getRoot() + "workflow/invoice/commit.action",
		    				data: "invoice.id="+id,
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
		    						invoiceGrid.jqGrid().trigger("reloadGrid");
		    					} else {
		    						BootstrapDialog.danger(json.msg);
		    					}
		    				},
		    				error: function() {
		    					BootstrapDialog.danger("系统异常，请联系管理员！");
		    				}
	   					});
	   				}
	            }
   			});
   		}
   		
   		function delInvoice(id) {
   			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定删除吗？",
	            type: BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	   					$.ajax({
	   						url: getRoot() + "workflow/invoice/del.action",
		    				data: "invoice.id="+id,
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
		    						invoiceGrid.jqGrid().trigger("reloadGrid");
		    					} else {
		    						BootstrapDialog.danger(json.msg);
		    					}
		    				},
		    				error: function() {
		    					BootstrapDialog.danger("系统异常，请联系管理员！");
		    				}
	   					});
	   				}
	            }
   			});
   		}
   		
   		function viewInvoice(id) {
   			invoiceDialog = BootstrapDialog.show({
			    title: "申请单详情",
			    width: "50%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/invoice/queryById.action?returnType=view&invoice.id="+id),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [/*{
			    	label: "打印",
			    	icon: "fa fa-print",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		var html = "<!DOCTYPE html><html><head><meta charset='utf-8'><meta http-equiv='X-UA-Compatible' content='IE=edge'>";
			    		html = html + "<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>";
			    		html = html + getLinkCss()+"</head>";
			    		html = html + "<body><form class='form-horizontal'>"+dialog.getModalBody().find("form").find("#invoice").html()+"</form></body>";
			    		Lodop = getLodop();
			    		LODOP.PRINT_INIT("开票打印");
			    		LODOP.ADD_PRINT_HTM(5,5,"200mm","287mm", html);
						LODOP.PREVIEW();
			    	}
			    }, */{
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-danger",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
   		}
   		
   		/*function printInvoice(id) {
   			window.open(getRoot() + "workflow/invoice/queryById.action?returnType=print&invoice.id="+id, "开票打印");
   		}*/
   		function printInvoice(id) {
   			var printDiv = $("<div></div>").load(getRoot() + "workflow/invoice/queryById.action?returnType=print&invoice.id="+id, function(response) {
   				var html = "<!DOCTYPE html><html><head><meta charset='utf-8'><meta http-equiv='X-UA-Compatible' content='IE=edge'>";
   	    		html = html + "<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>";
   	    		html = html + getLinkCss()+"</head>";
   	    		html = html + "<body>"+$(this).html()+"</body>";
   	    		Lodop = getLodop();
   	    		LODOP.PRINT_INIT("开票打印");
   	    		LODOP.ADD_PRINT_HTM(5,5,"200mm","287mm", html);
   				LODOP.PREVIEW();
   			});
   		}
   		
   		function initSelect_invoice(id) {
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
   	   							initOptions_invoice(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化企业付款查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_invoice(data, selector) {
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
   	</script>
  </body>
</html>
