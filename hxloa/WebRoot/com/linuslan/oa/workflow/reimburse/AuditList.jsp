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
   				<form id="search_form_reimburse_audit_list" action="" class="form-horizontal">
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
							<div class="col-md-3 col-sm-6">
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchReimburseAuditList"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm margin-left5" data-toggle="tooltip" title="重置" id="resetSearchReimburseAuditList"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="box-body">
   					<table id="reimburseAuditDatagrid"></table>
   					<div id="reimburseAuditDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var auditReimburseGrid;
   		var auditReimburseDialog;
   		$(function() {
   			$("#search_form_reimburse_audit_list").find(".departmentTree").combotree({
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
			$("#search_form_reimburse_audit_list").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
			initSelect_reimburseAudit("search_form_reimburse_audit_list");
			$("#searchReimburseAuditList").click(function() {
   				$("#reimburseAuditDatagrid").setGridParam({
   					postData:parsePostData("search_form_reimburse_audit_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchReimburseAuditList").click(function() {
   				$("#search_form_reimburse_audit_list")[0].reset();
   				$("#search_form_reimburse_audit_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			auditReimburseGrid = $("#reimburseAuditDatagrid").jqGrid({
                url: getRoot() + "workflow/reimburse/queryAuditPage.action",
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
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewAuditReimburse("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+")");
                		if(true == rowObject.passBtn) {
                			buttons = buttons + createBtn("通过", "btn-success btn-xs", "fa-share", "auditReimburse("+rowObject.id+", 0)");
                		}
                		if(true == rowObject.rejectBtn) {
                			buttons = buttons + createBtn("退回", "btn-danger btn-xs", "fa-reply", "auditReimburse("+rowObject.id+", 1)");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#reimburseAuditDatagridpager"
            });
   		});
   		
   		function viewAuditReimburse(id, passBtn, rejectBtn) {
   			var passCss = "";
   			var rejectCss = "";
   			if(true != passBtn) {
   				passCss = "hidden";
   			}
   			if(true != rejectBtn) {
   				rejectCss = "hidden";
   			}
   			auditReimburseDialog = BootstrapDialog.show({
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
			    	label: "通过",
			    	icon: "fa fa-fw fa-share",
			    	cssClass: "btn-success "+passCss,
			    	action: function(dialog) {
			    		try {
			    			var $button = this;
			    			var $form = dialog.getModalBody().find("form");
			    			commitAuditReimburse(0, $form.serialize(), dialog, $button)
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
			    			commitAuditReimburse(1, $form.serialize(), dialog, $button)
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
   		function auditReimburse(id, passType) {
   			if(!id) {
   				BootstrapDialog.danger("ID无效");
   				return false;
   			}
   			var param = "reimburse.id="+id;
   			commitAuditReimburse(passType, param);
   		}
   		
   		/**
   		 * 将传进来的参数提交到server端
   		 * @param passType 操作类型 0:通过；1：退回
   		 * @param params 传递的参数
   		 * @param dialog 打开的窗口
   		 * @param $button 点击的按钮
   		 */
   		function commitAuditReimburse(passType, params, dialog, $button) {
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
	   						url: getRoot() + "workflow/reimburse/audit.action",
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
		    						auditReimburseGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function initSelect_reimburseAudit(id) {
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
   	   							initOptions_reimburseAudit(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化报销查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_reimburseAudit(data, selector) {
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
