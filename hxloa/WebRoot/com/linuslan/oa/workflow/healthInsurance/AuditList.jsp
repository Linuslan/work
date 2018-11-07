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
   				<div class="box-body">
   					<table id="healthInsuranceAuditDatagrid"></table>
   					<div id="healthInsurancedatagridpager_audit"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var auditHealthInsuranceGrid;
   		var auditHealthInsuranceDialog;
   		$(function() {
   			auditHealthInsuranceGrid = $("#healthInsuranceAuditDatagrid").jqGrid({
                url: getRoot() + "workflow/healthInsurance/queryAuditPage.action",
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
                	label: "时间", name: "date", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		return rowObject.year+"-"+rowObject.month;
                	}
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
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewAuditHealthInsurance("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+")");
                		if(true == rowObject.passBtn) {
                			buttons = buttons + createBtn("通过", "btn-success btn-xs", "fa-share", "auditHealthInsurance("+rowObject.id+", 0)");
                		}
                		if(true == rowObject.rejectBtn) {
                			buttons = buttons + createBtn("退回", "btn-danger btn-xs", "fa-reply", "auditHealthInsurance("+rowObject.id+", 1)");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "600px",
                //width: "100%",
                rowNum: 20,
                pager: "#healthInsurancedatagridpager_audit"
            });
   		});
   		
   		function viewAuditHealthInsurance(id, passBtn, rejectBtn) {
   			var passCss = "";
   			var rejectCss = "";
   			if(true != passBtn) {
   				passCss = "hidden";
   			}
   			if(true != rejectBtn) {
   				rejectCss = "hidden";
   			}
   			auditHealthInsuranceDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "95%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/healthInsurance/queryById.action?returnType=audit&healthInsurance.id="+id),
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
			    			commitAuditHealthInsurance(0, $form.serialize(), dialog, $button)
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
			    			commitAuditHealthInsurance(1, $form.serialize(), dialog, $button)
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
   		function auditHealthInsurance(id, passType) {
   			if(!id) {
   				BootstrapDialog.danger("ID无效");
   				return false;
   			}
   			var param = "healthInsurance.id="+id;
   			commitAuditHealthInsurance(passType, param);
   		}
   		
   		/**
   		 * 将传进来的参数提交到server端
   		 * @param passType 操作类型 0:通过；1：退回
   		 * @param params 传递的参数
   		 * @param dialog 打开的窗口
   		 * @param $button 点击的按钮
   		 */
   		function commitAuditHealthInsurance(passType, params, dialog, $button) {
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
	   						url: getRoot() + "workflow/healthInsurance/audit.action",
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
		    						auditHealthInsuranceGrid.jqGrid().trigger("reloadGrid");
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
   	</script>
  </body>
</html>
