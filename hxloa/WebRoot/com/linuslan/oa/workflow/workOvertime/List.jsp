<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>登录用户加班申请单列表</title>
    
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
   				<form id="search_form_workOvertime_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">加班开始时间：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.startDate" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">加班结束时间：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.endDate" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">加班类型：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.classId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">事由：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.content" type="text" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<button type="button" class="btn btn-info btn-sm pull-right" data-toggle="tooltip" title="重置" id="resetSearchLeaveList"><i class="fa fa-fw fa-undo"></i>重置</button>
								<button type="button" class="btn btn-info btn-sm pull-right margin-right5" data-toggle="tooltip" title="查询" id="searchLeaveList"><i class="fa fa-fw fa-search"></i>查询</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="加班申请" id="addWorkOvertime"><i class="fa fa-fw fa-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="workOvertimedatagrid"></table>
   					<div id="workOvertimedatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var workOvertimeGrid;
   		var workOvertimeDialog;
   		$(function() {
   		
   			$("#search_form_workOvertime_list").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
			initSelect_workOvertime("search_form_workOvertime_list");
			$("#searchLeaveList").click(function() {
   				$("#workOvertimedatagrid").setGridParam({
   					postData:parsePostData("search_form_workOvertime_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchLeaveList").click(function() {
   				$("#search_form_workOvertime_list")[0].reset();
   				$("#search_form_workOvertime_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			
   			workOvertimeGrid = $("#workOvertimedatagrid").jqGrid({
                url: getRoot() + "workflow/workOvertime/queryPage.action",
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
                	label: "加班类型", name: "className", width: 100, align: "center"
                }, {
                	label: "时长", name: "duration", width: 200, align: "center"
                }, {
                	label: "开始时间", name: "startDate", width: 150, align: "center"
                }, {
                	label: "结束时间", name: "endDate", width: 100, align: "center"
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
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewWorkOvertime("+rowObject.id+")");
                		if(rowObject.status == 0 || rowObject.status == 1 || rowObject.status == 2) {
                			buttons = buttons + createBtn("修改", "btn-info btn-xs", "fa-pencil", "editWorkOvertime("+rowObject.id+")");
                			buttons = buttons + createBtn("提交", "btn-success btn-xs", "fa-share", "commitWorkOvertime("+rowObject.id+")");
                			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delWorkOvertime("+rowObject.id+")");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#workOvertimedatagridpager"
            });
   			$("#addWorkOvertime").click(function() {
   				workOvertimeDialog = BootstrapDialog.show({
				    title: "新增加班申请",
				    width: "50%",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "workflow/workOvertime/queryById.action?returnType=add"),
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
				    				url: getRoot() + "workflow/workOvertime/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						workOvertimeGrid.jqGrid().trigger("reloadGrid");
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
							    				url: getRoot() + "workflow/workOvertime/commit.action",
							    				data: form.serialize(),
							    				type: "POST",
							    				success: function(data) {
							    					var json = eval("("+data+")");
							    					if(json.success) {
							    						BootstrapDialog.success(json.msg);
							    						dialog.close();
							    						workOvertimeGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function editWorkOvertime(id) {
   			workOvertimeDialog = BootstrapDialog.show({
			    title: "编辑加班",
			    width: "50%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/workOvertime/queryById.action?returnType=edit&workOvertime.id="+id),
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
			    				url: getRoot() + "workflow/workOvertime/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						workOvertimeGrid.jqGrid().trigger("reloadGrid");
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
						    				url: getRoot() + "workflow/workOvertime/commit.action",
						    				data: form.serialize(),
						    				type: "POST",
						    				success: function(data) {
						    					var json = eval("("+data+")");
						    					if(json.success) {
						    						BootstrapDialog.success(json.msg);
						    						dialog.close();
						    						workOvertimeGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function commitWorkOvertime(id) {
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
	   						url: getRoot() + "workflow/workOvertime/commit.action",
		    				data: "workOvertime.id="+id,
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
		    						workOvertimeGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function delWorkOvertime(id) {
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
	   						url: getRoot() + "workflow/workOvertime/del.action",
		    				data: "workOvertime.id="+id,
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
		    						workOvertimeGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function viewWorkOvertime(id) {
   			workOvertimeDialog = BootstrapDialog.show({
			    title: "加班详情",
			    width: "50%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/workOvertime/queryById.action?returnType=view&workOvertime.id="+id),
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
   		
   		function initSelect_workOvertime(id) {
   			try {
   				$("#"+id+" select.select2").select2();
   				$.ajax({
   	   				url: getRoot() + "workflow/workOvertime/initSelect.action",
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						for(var key in json) {
   	   							var selector = "#"+id+" select[name='paramMap."+key+"']";
   	   							initOptions_workOvertime(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化请假查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_workOvertime(data, selector) {
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
