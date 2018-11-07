<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>登录用户报销申请单列表</title>
    
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
   				<div class="toolbar with-border">
   					<button class="btn btn-info btn-sm" data-toggle="tooltip" title="医保申请" id="addHealthInsurance"><i class="fa fa-fw fa-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="healthInsurancedatagrid"></table>
   					<div id="healthInsurancedatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var healthInsuranceGrid;
   		var addHealthInsuranceDialog;
   		var healthInsuranceLastsel2;
   		$(function() {
   			healthInsuranceGrid = $("#healthInsurancedatagrid").jqGrid({
                url: getRoot() + "workflow/healthInsurance/queryPage.action",
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
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewHealthInsurance("+rowObject.id+")");
                		if(rowObject.status == 0 || rowObject.status == 1 || rowObject.status == 2) {
                			buttons = buttons + createBtn("修改", "btn-info btn-xs", "fa-pencil", "editHealthInsurance("+rowObject.id+")");
                			buttons = buttons + createBtn("提交", "btn-success btn-xs", "fa-share", "commitHealthInsurance("+rowObject.id+")");
                			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delHealthInsurance("+rowObject.id+")");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "600px",
                //width: "100%",
                rowNum: 20,
                pager: "#healthInsurancedatagridpager"
            });
   			$("#addHealthInsurance").click(function() {
   				addHealthInsuranceDialog = BootstrapDialog.show({
				    title: "新增申请",
				    width: "95%",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "workflow/healthInsurance/queryById.action?returnType=add"),
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
				    		var $grid = jQuery("#healthInsuranceContentDatagrid_add");
				    		var $form = dialog.getModalBody().find("form");
				    		try {
				    			//将未保存的先保存
				    			jQuery("#healthInsuranceContentDatagrid_add").jqGrid("saveRow", healthInsuranceLastsel2, false, "clientArray");
				    			var contents = getHealthInsuranceContents($grid, $form);
				    			var $button = this;
				    			dialog.enableButtons(false);
				    			dialog.setClosable(false);
				    			$button.spin();
				    			upload($form, function(response) {
				    				$form.find("input.healthInsuranceContent").remove();
				    				try {
				    					var json = eval("("+response+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						healthInsuranceGrid.jqGrid().trigger("reloadGrid");
				    					} else {
				    						BootstrapDialog.danger(json.msg);
				    						dialog.enableButtons(true);
					    					dialog.setClosable(true);
					    					$button.stopSpin();
				    					}
				    				} catch(ex) {
				    					BootstrapDialog.danger(response);
			    						dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    					$button.stopSpin();
				    				}
				    				
				    			});
				    			
				    		} catch(e) {
				    			$form.find("input.healthInsuranceContent").remove();
				    			dialog.enableButtons(true);
			    				dialog.setClosable(true);
				    			BootstrapDialog.danger("系统异常，请联系管理员！"+e);
				    		}
				    	}
				    }, {
				    	label: "提交",
				    	icon: "fa fa-fw fa-share",
				    	cssClass: "btn-success",
				    	action: function(dialog) {
				    		var $button = this;
				    		var $grid = jQuery("#healthInsuranceContentDatagrid_add");
				    		var $form = dialog.getModalBody().find("form");
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
						   					//将未保存的先保存
							    			jQuery("#healthInsuranceContentDatagrid_add").jqGrid("saveRow", healthInsuranceLastsel2, false, "clientArray");
							    			var contents = getHealthInsuranceContents($grid, $form);
							    			dialog.enableButtons(false);
							    			dialog.setClosable(false);
							    			$button.spin();
							    			$form.attr("action", getRoot() + "workflow/healthInsurance/commit.action");
							    			upload($form, function(response) {
							    				$form.find("input.healthInsuranceContent").remove();
							    				try {
							    					var json = eval("("+response+")");
							    					if(json.success) {
							    						BootstrapDialog.success(json.msg);
							    						dialog.close();
							    						healthInsuranceGrid.jqGrid().trigger("reloadGrid");
							    					} else {
							    						BootstrapDialog.danger(json.msg);
							    						dialog.enableButtons(true);
								    					dialog.setClosable(true);
								    					$button.stopSpin();
							    					}
							    				} catch(ex) {
							    					BootstrapDialog.danger(response);
						    						dialog.enableButtons(true);
							    					dialog.setClosable(true);
							    					$button.stopSpin();
							    				}
							    			});
						   				}
						            }
					   			});
				    		} catch(e) {
				    			$form.find("input.healthInsuranceContent").remove();
				    			dialog.enableButtons(true);
			    				dialog.setClosable(true);
				    			BootstrapDialog.danger("系统异常，请联系管理员！"+e);
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
   		
   		function editHealthInsurance(id) {
   			healthInsuranceDialog = BootstrapDialog.show({
			    title: "编辑医保",
			    width: "95%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/healthInsurance/queryById.action?returnType=edit&healthInsurance.id="+id),
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
			    		var $grid = jQuery("#healthInsuranceContentDatagrid_edit");
			    		var $form = dialog.getModalBody().find("form");
			    		try {
			    			
			    			//将未保存的先保存
			    			jQuery("#healthInsuranceContentDatagrid_edit").jqGrid("saveRow", healthInsuranceLastsel2, false, "clientArray");
			    			var contents = getHealthInsuranceContents($grid, $form);
			    			var $button = this;
			    			dialog.enableButtons(false);
			    			dialog.setClosable(false);
			    			$button.spin();
			    			upload($form, function(response) {
			    				$form.find("input.healthInsuranceContent").remove();
			    				try {
			    					var json = eval("("+response+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						healthInsuranceGrid.jqGrid().trigger("reloadGrid");
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    						dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    					$button.stopSpin();
			    					}
			    				} catch(ex) {
			    					BootstrapDialog.danger(response);
		    						dialog.enableButtons(true);
			    					dialog.setClosable(true);
			    					$button.stopSpin();
			    				}
			    				
			    			});
			    		} catch(e) {
			    			$form.find("input.healthInsuranceContent").remove();
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
			    		var $grid = jQuery("#healthInsuranceContentDatagrid_edit");
			    		var $form = dialog.getModalBody().find("form");
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
					   					//将未保存的先保存
						    			jQuery("#healthInsuranceContentDatagrid_edit").jqGrid("saveRow", healthInsuranceLastsel2, false, "clientArray");
						    			var contents = getHealthInsuranceContents($grid, $form);
						    			
						    			dialog.enableButtons(false);
						    			dialog.setClosable(false);
						    			$button.spin();
						    			$form.attr("action", getRoot() + "workflow/healthInsurance/commit.action");
						    			upload($form, function(response) {
						    				$form.find("input.healthInsuranceContent").remove();
						    				try {
						    					var json = eval("("+response+")");
						    					if(json.success) {
						    						BootstrapDialog.success(json.msg);
						    						dialog.close();
						    						healthInsuranceGrid.jqGrid().trigger("reloadGrid");
						    					} else {
						    						BootstrapDialog.danger(json.msg);
						    						dialog.enableButtons(true);
							    					dialog.setClosable(true);
							    					$button.stopSpin();
						    					}
						    				} catch(ex) {
						    					BootstrapDialog.danger(response);
					    						dialog.enableButtons(true);
						    					dialog.setClosable(true);
						    					$button.stopSpin();
						    				}
						    				
						    			});
					   				}
					            }
				   			});
			    			
			    		} catch(e) {
			    			$form.find("input.healthInsuranceContent").remove();
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
   		
   		function commitHealthInsurance(id) {
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
	   						url: getRoot() + "workflow/healthInsurance/commit.action",
		    				data: "healthInsurance.id="+id+"&type=commit",
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
		    						healthInsuranceGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function delHealthInsurance(id) {
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
	   						url: getRoot() + "workflow/healthInsurance/del.action",
		    				data: "healthInsurance.id="+id,
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
		    						healthInsuranceGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function viewHealthInsurance(id) {
   			healthInsuranceDialog = BootstrapDialog.show({
			    title: "医保详情",
			    width: "95%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/healthInsurance/queryById.action?returnType=view&healthInsurance.id="+id),
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
   		
   		function getHealthInsuranceContents($grid, $form) {
   			if(!$grid || 0 >= $grid.length) {
   				BootstrapDialog.error("获取医保项目异常");
   				return false;
   			}
   			var rows = $grid.getRowData();
   			var contents = new Array();
   			var param = "contents[#index#].#prop#=#value#";
   			for(var i = 0; i < rows.length; i ++) {
   				var content = rows[i];
   				for(var name in content) {
   					if(!name || ""==$.trim(name) || "operationCell" == name) {
   						continue;
   					}
   					var value = content[name];
   					if("id" == name) {
   						var re = /^[0-9]*$/;
   						if(!re.test(value)) {
   							value = "";
   						}
   					}
   					$form.append("<input class='healthInsuranceContent' name='contents["+i+"]."+name+"' value='"+value+"' type='hidden' />");
   					//var value = content[name];
   					contents.push(param.replace("#index#", i).replace("#prop#", name).replace("#value#", value));
   				}
   			}
   			/*if(0 >= contents.length) {
   				BootstrapDialog.danger("获取到的项目为空");
   				return false;
   			}*/
   			return contents;
   		}
   		
   		function addHealthInsuranceContentCallback(data) {
   			
   		}
   	</script>
  </body>
</html>
