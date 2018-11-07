<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>待登录用户审核的出库单列表</title>
    
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
   				<form id="search_form_checkout_audit_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">出库时间始：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.checkoutDate_start" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">出库时间止：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.checkoutDate_end" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属客户：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.customerId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属公司：</label>
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
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属仓库：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.warehouseId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">出库类型：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.checkoutTypeId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">编号：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.serialNo" type="text" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<button type="button" class="btn btn-info btn-sm pull-right" data-toggle="tooltip" title="重置" id="resetSearchCheckoutAuditList"><i class="fa fa-fw fa-undo"></i>重置</button>
								<button type="button" class="btn btn-info btn-sm margin-right5 pull-right" data-toggle="tooltip" title="查询" id="searchCheckoutAuditList"><i class="fa fa-fw fa-search"></i>查询</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="box-body">
   					<table id="checkoutAuditDatagrid"></table>
   					<div id="checkoutAuditDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var auditCheckoutGrid;
   		var auditCheckoutDialog;
   		$(function() {
   			initSelect_checkoutAudit("search_form_checkout_audit_list");
   			$("#search_form_checkout_audit_list .date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			
   			$("#searchCheckoutAuditList").click(function() {
   				$("#checkoutAuditDatagrid").setGridParam({
   					postData:parsePostData("search_form_checkout_audit_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchCheckoutAuditList").click(function() {
   				$("#search_form_checkout_audit_list")[0].reset();
   				$("#search_form_checkout_audit_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			auditCheckoutGrid = $("#checkoutAuditDatagrid").jqGrid({
                url: getRoot() + "workflow/checkout/queryAuditPage.action",
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
                	label: "申请人", name: "userName", width: 150, align: "center"
                }, {
                	label: "出库编号", name: "serialNo", width: 100, align: "center"
                }, {
                	label: "归属公司", name: "companyName", width: 150, align: "center"
                }, {
                	label: "客户名称", name: "customerName", width: 150, align: "center"
                }, {
                	label: "出库时间", name: "checkoutDate", width: 150, align: "center"
                }, {
                	label: "出库仓库", name: "warehouseName", width: 200, align: "center"
                }, {
                	label: "出库类型", name: "checkoutTypeName", width: 150, align: "center"
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewAuditCheckout("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+")");
                		if(true == rowObject.passBtn) {
                			buttons = buttons + createBtn("通过", "btn-success btn-xs", "fa-share", "auditCheckout("+rowObject.id+", 0)");
                		}
                		if(true == rowObject.rejectBtn) {
                			buttons = buttons + createBtn("退回", "btn-danger btn-xs", "fa-reply", "auditCheckout("+rowObject.id+", 1)");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#checkoutAuditDatagridpager"
            });
   		});
   		
   		function viewAuditCheckout(id, passBtn, rejectBtn) {
   			var passCss = "";
   			var rejectCss = "";
   			if(true != passBtn) {
   				passCss = "hidden";
   			}
   			if(true != rejectBtn) {
   				rejectCss = "hidden";
   			}
   			auditCheckoutDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "60%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/checkout/queryById.action?returnType=audit&checkout.id="+id),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [{
			    	label: "通过",
			    	icon: "fa fa-fw fa-share",
			    	cssClass: "btn-success "+passCss,
			    	action: function(dialog) {
			    		try {
			    			var $button = this;
			    			var $form = dialog.getModalBody().find("form");
			    			commitAuditCheckout(0, $form.serialize(), dialog, $button)
			    		} catch(ex) {
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "退回",
			    	icon: "fa fa-fw fa-reply",
			    	cssClass: "btn-danger "+rejectCss,
			    	action: function(dialog) {
			    		try {
			    			var $button = this;
			    			var $form = dialog.getModalBody().find("form");
			    			commitAuditCheckout(1, $form.serialize(), dialog, $button)
			    		} catch(ex) {
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-warning",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
   		}
   		
   		/**
   		 * 审核方法
   		 */
   		function auditCheckout(id, passType) {
   			if(!id) {
   				BootstrapDialog.danger("ID无效");
   				return false;
   			}
   			var param = "checkout.id="+id;
   			commitAuditCheckout(passType, param);
   		}
   		
   		/**
   		 * 将传进来的参数提交到server端
   		 * @param passType 操作类型 0:通过；1：退回
   		 * @param params 传递的参数
   		 * @param dialog 打开的窗口
   		 * @param $button 点击的按钮
   		 */
   		function commitAuditCheckout(passType, params, dialog, $button) {
   			var tips = "";
   			if(passType == 0) {
   				tips = "通过";
   			} else {
   				tips = "退回";
   			}
   			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定"+tips+"申请吗？",
	            type: passType == 0 ? BootstrapDialog.TYPE_SUCCESS : BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	   					$.ajax({
	   						url: getRoot() + "workflow/checkout/audit.action",
		    				data: "passType="+passType+"&"+params,
		    				type: "POST",
		    				beforeSend: function() {
		    					if(dialog) {
		    						dialog.enableButtons(false);
			    					dialog.setClosable(false);
		    					}
		    					if($button) {
		    						$button.spin();
		    					}
		    				},
		    				complete: function() {
		    					if(dialog) {
		    						dialog.enableButtons(true);
			    					dialog.setClosable(true);
		    					}
		    					if($button) {
		    						$button.stopSpin();
		    					}
		    				},
		    				success: function(data) {
		    					var json = eval("("+data+")");
		    					if(json.success) {
		    						BootstrapDialog.success(json.msg);
		    						if(dialog) {
		    							dialog.close();
		    						}
		    						auditCheckoutGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function initSelect_checkoutAudit(id) {
   			try {
   				$("#"+id+" select.select2").select2();
   				$.ajax({
   	   				url: getRoot() + "workflow/checkout/initSelect.action",
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						for(var key in json) {
   	   							var selector = "#"+id+" select[name='paramMap."+key+"']";
   	   							initOptions_checkoutAudit(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化固资查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_checkoutAudit(data, selector) {
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
