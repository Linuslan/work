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
   				<form id="search_form_reimburse_list" action="" class="form-horizontal">
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
							
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">收款方：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.receiver" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">编号：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.serialNo" type="text" class="form-control">
							</div>
							<div class="col-md-3 col-sm-6">
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchReimburseList"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm margin-left5" data-toggle="tooltip" title="重置" id="resetSearchReimburseList"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-info btn-sm" data-toggle="tooltip" title="报销申请" id="addReimburse"><i class="fa fa-fw fa-plus"></i>新增</button>
   					<button class="btn btn-info btn-sm" data-toggle="tooltip" title="打印" id="printReimburses"><i class="fa fa-fw fa-print"></i>打印</button>
   				</div>
   				<div class="box-body">
   					<table id="reimbursedatagrid"></table>
   					<div id="reimbursedatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var reimburseGrid;
   		var reimburseDialog;
   		var lastsel2;
   		$(function() {
   			$("#search_form_reimburse_list").find(".departmentTree").combotree({
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
			$("#search_form_reimburse_list").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
			initSelect_reimburse("search_form_reimburse_list");
			$("#searchReimburseList").click(function() {
   				$("#reimbursedatagrid").setGridParam({
   					postData:parsePostData("search_form_reimburse_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchReimburseList").click(function() {
   				$("#search_form_reimburse_list")[0].reset();
   				$("#search_form_reimburse_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			reimburseGrid = $("#reimbursedatagrid").jqGrid({
                url: getRoot() + "workflow/reimburse/queryPage.action",
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
                rownumbers: true,
                multiselect: true,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "ID", name: "id", hidden: true, key: true
                }, {
                	label: "编号", name: "serialNo", width: 100, align: "center"
                }, {
                	label: "出款公司", name: "companyName", width: 150, align: "center"
                }, {
                	label: "报销部门", name: "reimburseDeptName", width: 150, align: "center"
                }, {
                	label: "总金额", name: "totalMoney", width: 150, align: "center"
                }, {
                	label: "收款方", name: "receiver", width: 200, align: "center"
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
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewReimburse("+rowObject.id+")");
                		if(true == rowObject.print) {
                			buttons = buttons + createBtn("打印", "btn-info btn-xs", "fa-print", "printReimburse("+rowObject.id+")");
                		}
                		
                		if(rowObject.status == 0 || rowObject.status == 1 || rowObject.status == 2) {
                			buttons = buttons + createBtn("修改", "btn-info btn-xs", "fa-pencil", "editReimburse("+rowObject.id+")");
                			buttons = buttons + createBtn("提交", "btn-success btn-xs", "fa-share", "commitReimburse("+rowObject.id+")");
                			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delReimburse("+rowObject.id+")");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#reimbursedatagridpager"
            });
   			$("#addReimburse").click(function() {
   				reimburseDialog = BootstrapDialog.show({
				    title: "新增申请",
				    width: "60%",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "workflow/reimburse/queryById.action?returnType=add"),
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
				    			var $grid = dialog.getModalBody().find("form").find("table");
				    			//将未保存的先保存
				    			jQuery("#reimburseContentDatagrid_add").jqGrid("saveRow", lastsel2, false, "clientArray");
				    			var contents = getReimburseContents($grid);
				    			if(!contents) {
				    				return false;
				    			}
				    			var form = dialog.getModalBody().find("form");
				    			var $button = this;
				    			$.ajax({
				    				url: getRoot() + "workflow/reimburse/add.action",
				    				data: form.serialize()+"&"+contents.join("&"),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						reimburseGrid.jqGrid().trigger("reloadGrid");
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
				    			BootstrapDialog.danger("系统异常，请联系管理员！"+e);
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
						   					var $grid = dialog.getModalBody().find("form").find("table");
							    			//将未保存的先保存
							    			jQuery("#reimburseContentDatagrid_add").jqGrid("saveRow", lastsel2, false, "clientArray");
							    			var contents = getReimburseContents($grid);
							    			if(!contents) {
							    				return false;
							    			}
							    			var form = dialog.getModalBody().find("form");
							    			$.ajax({
							    				url: getRoot() + "workflow/reimburse/commit.action?type=update",
							    				data: form.serialize()+"&"+contents.join("&"),
							    				type: "POST",
							    				success: function(data) {
							    					var json = eval("("+data+")");
							    					if(json.success) {
							    						BootstrapDialog.success(json.msg);
							    						dialog.close();
							    						reimburseGrid.jqGrid().trigger("reloadGrid");
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
   			
   			$("#printReimburses").click(function() {
   				var selectedIds = $("#reimbursedatagrid").getGridParam("selarrrow");
   				if(!selectedIds || 0 >= selectedIds.length) {
   					BootstrapDialog.danger("请至少选择一条记录");
   					return false;
   				}
   				var printDiv = $("<div></div>").load(getRoot() + "workflow/reimburse/queryPrintList.action?ids="+selectedIds, function(response) {
	   				var html = "<!DOCTYPE html><html><head><meta charset='utf-8'><meta http-equiv='X-UA-Compatible' content='IE=edge'>";
	   	    		html = html + "<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>";
	   	    		//var html = "";
	   	    		html = html + getPrintCss();
	   	    		html = html + "<style>table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse} .tab_css_2 td{ text-align:left; overflow: visible; }.tab_css_2{ border: 1px solid #cad9ea; border-collapse: collapse; color:#000; }";
	   	    		html = html + ".tab_css_2 th { background-image: url("+getRoot()+"resources/css/oaimg/th_bg1.gif); background-repeat:repeat-x; color:#000; }";
	   	    		html = html + ".tab_css_2 td{ border-bottom:1px solid #cad9ea; border-right: 1px solid #cad9ea; padding:0 2px 0; }";
	   	    		html = html + ".tab_css_2 th{ border:1px solid #a7d1fd; padding:0 2px 0; }";
	   	    		html = html + ".tab_css_2 tr.tr_css td{ background-color:#eaf4ff; } table{ empty-cells:show; border-collapse: collapse; margin:0 auto; font-size:12px; line-height: 20px;}</style></head>";
	   	    		Lodop = getLodop();
	   	    		LODOP.PRINT_INIT("报销打印");
	   	    		html = html + "<body>"+decode($(this).html())+"</body>";
	   	    		LODOP.ADD_PRINT_HTM(5,5,"200mm","287mm", html);
	   				LODOP.PREVIEW();
	   			});
   			});
   		});
   		
   		function editReimburse(id) {
   			reimburseDialog = BootstrapDialog.show({
			    title: "编辑报销单",
			    width: "60%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/reimburse/queryById.action?returnType=edit&reimburse.id="+id),
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
			    			var $grid = dialog.getModalBody().find("form").find("table#reimburseContentDatagrid_edit");
			    			//将未保存的先保存
			    			jQuery("#reimburseContentDatagrid_edit").jqGrid("saveRow", lastsel2, false, "clientArray");
			    			var contents = getReimburseContents($grid);
			    			if(!contents) {
			    				return false;
			    			}
			    			var form = dialog.getModalBody().find("form");
			    			var $button = this;
			    			$.ajax({
			    				url: getRoot() + "workflow/reimburse/update.action",
			    				data: form.serialize()+"&"+contents.join("&"),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						reimburseGrid.jqGrid().trigger("reloadGrid");
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
					   					var $grid = dialog.getModalBody().find("form").find("table#reimburseContentDatagrid_edit");
						    			//将未保存的先保存
						    			jQuery("#reimburseContentDatagrid_edit").jqGrid("saveRow", lastsel2, false, "clientArray");
						    			var contents = getReimburseContents($grid);
						    			if(!contents) {
						    				return false;
						    			}
						    			var form = dialog.getModalBody().find("form");
						    			
						    			$.ajax({
						    				url: getRoot() + "workflow/reimburse/commit.action?type=update",
						    				data: form.serialize()+"&"+contents.join("&"),
						    				type: "POST",
						    				success: function(data) {
						    					var json = eval("("+data+")");
						    					if(json.success) {
						    						BootstrapDialog.success(json.msg);
						    						dialog.close();
						    						reimburseGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function commitReimburse(id) {
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
	   						url: getRoot() + "workflow/reimburse/commit.action?type=commit",
		    				data: "reimburse.id="+id,
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
		    						reimburseGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function delReimburse(id) {
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
	   						url: getRoot() + "workflow/reimburse/del.action",
		    				data: "reimburse.id="+id,
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
		    						reimburseGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function viewReimburse(id) {
   			reimburseDialog = BootstrapDialog.show({
			    title: "报销单详情",
			    width: "50%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/reimburse/queryById.action?returnType=view&reimburse.id="+id),
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
   		
   		function printReimburse(id) {
   			var printDiv = $("<div></div>").load(getRoot() + "workflow/reimburse/queryById.action?returnType=print&reimburse.id="+id, function(response) {
   				var html = "<!DOCTYPE html><html><head><meta charset='utf-8'><meta http-equiv='X-UA-Compatible' content='IE=edge'>";
   	    		html = html + "<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>";
   	    		html = html + getLinkCss();
   	    		html = html + "<style>.tab_css_2 td{ height:40px; text-align:left; overflow: visible; }.tab_css_2{ border: 1px solid #cad9ea; border-collapse: collapse; color:#000; }";
   	    		html = html + ".tab_css_2 th { background-image: url("+getRoot()+"resources/css/oaimg/th_bg1.gif); background-repeat:repeat-x; height:40px; color:#000; }";
   	    		html = html + ".tab_css_2 td{ border-bottom:1px solid #cad9ea; border-right: 1px solid #cad9ea; padding:0 10px 0; }";
   	    		html = html + ".tab_css_2 th{ border:1px solid #a7d1fd; padding:0 2px 0; }";
   	    		html = html + ".tab_css_2 tr.tr_css td{ background-color:#eaf4ff; } table{ empty-cells:show; border-collapse: collapse; margin:0 auto; font-size:12px; line-height: 20px;}</style></head>";
   	    		html = html + "<body>"+decode($(this).html())+"</body>";
   	    		Lodop = getLodop();
   	    		LODOP.PRINT_INIT("报销打印");
   	    		LODOP.ADD_PRINT_HTM(5,5,"200mm","287mm", html);
   				LODOP.PREVIEW();
   			});
   		}
   		
   		function getReimburseContents($grid) {
   			if(!$grid || 0 >= $grid.length) {
   				BootstrapDialog.error("获取报销项目异常");
   				return false;
   			}
   			
   			var rows = $grid.getRowData();
   			var contents = new Array();
   			var param = "contents[#index#].#prop#=#value#";
   			if(!rows && 0 >= rows.length) {
   				BootstrapDialog.error("请至少添加一项项目");
   				return false;
   			}
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
   					} else if("reimburseClassId" == name) {
   						if(!value) {
   							BootstrapDialog.danger("请选择第"+(i+1)+"项的报销类别");
   							return false;
   						}
   					} else if("money" == name) {
   						if(!value) {
   							BootstrapDialog.danger("请输入第"+(i+1)+"项的金额");
   							return false;
   						}
   						if(!isDecimal(value)) {
   							BootstrapDialog.danger("第"+(i+1)+"项的金额格式不正确");
   							return false;
   						}
   					}
   					var value = encode(value);
   					//var value = content[name];
   					contents.push(param.replace("#index#", i).replace("#prop#", name).replace("#value#", value));
   				}
   			}
   			if(0 >= contents.length) {
   				BootstrapDialog.danger("获取到的项目为空");
   				return false;
   			}
   			return contents;
   		}
   		
   		function initSelect_reimburse(id) {
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
   	   							initOptions_reimburse(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化报销查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_reimburse(data, selector) {
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
