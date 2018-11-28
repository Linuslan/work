<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>登录用户的绩效列表</title>
    
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
   				<form id="search_form_achievement_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">日期：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.year" type="text" class="form-control pull-right date">
								</div>
							</div>
							<div class="col-md-3 col-sm-6">
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchAchievement"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="reset" class="btn btn-info btn-sm margin-left5" data-toggle="tooltip" title="重置"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-info btn-sm" data-toggle="tooltip" title="创建绩效" id="addAchievement"><i class="fa fa-fw fa-clone"></i>创建</button>
   				</div>
   				<div class="box-body">
   					<table id="achievementdatagrid"></table>
   					<div id="achievementdatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var achievementGrid;
   		var achievementDialog;
   		var achievementLastSel2;
   		$(function() {
   			$("#search_form_achievement_list input.date").datepicker({
   				startView: 2,
   				maxViewMode: 2,
   				minViewMode:2,
   				forceParse: false,
   				language: "zh-CN",
   				format: "yyyy"
   			});
   			$("#editAchievementForm").find(".select2").select2();
   			achievementGrid = $("#achievementdatagrid").jqGrid({
                url: getRoot() + "workflow/achievement/queryPage.action",
                mtype: "POST",
                rownumbers: true,
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "ID", name: "id", hidden: true, key: true
                }, {
                	label: "日期", name: "year", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		return rowObject.year+"-"+rowObject.month;
                	}
                }, {
                	label: "总分", name: "totalScore", width: 100, align: "center"
                }, {
                	label: "自评", name: "userScore", width: 100, align: "center"
                }, {
                	label: "领导评分", name: "leaderScore", width: 100, align: "center"
                }, {
                	label: "加分", name: "addScore", width: 100, align: "center"
                }, {
                	label: "奖金", name: "addMoney", width: 100, align: "center"
                }, {
                	label: "流程状态", name: "flowStatus", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		var flowStatus = rowObject.allFlowStatus;
                		var textes = new Array();
                		if(flowStatus) {
                			var statusArr = flowStatus.split(",");
                			for(var i = 0; i < statusArr.length; i ++) {
                				var status = statusArr[i];
                				if(0 == status) {
                					textes.push("未提交");
                				} else if(1 == status) {
                					textes.push("退回");
                				} else if(2 == status) {
                					textes.push("撤销");
                				} else if(3 == status) {
                					textes.push("待审核");
                				} else if(4 == status) {
                					textes.push("已完成");
                				} else if(5 == status) {
                					textes.push("待自评");
                				} else if(6 == status) {
                					textes.push("待领导评分");
                				} else if(7 == status) {
                					textes.push("待总经理评分");
                				}
                			}
                		}
                		return textes.join(",") == "" ? "已完成": textes.join(",");
                	}
                }, {
                	label: "当前审核组", name: "auditors", width: 150, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		var flowStatus = rowObject.allFlowStatus;
                		var text = cellValue;
                		if(flowStatus) {
                			var statusArr = flowStatus.split(",");
                			for(var i = 0; i < statusArr.length; i ++) {
                				var status = statusArr[i];
                				if(5 == status) {
                					text = "";
                					break;
                				}
                			}
                		}
                		return text;
                	}
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewAchievement("+rowObject.id+", \""+rowObject.allFlowStatus+"\", "+rowObject.status+")");
                		if(rowObject.status == 0 || rowObject.status == 1 || rowObject.status == 2) {
                			buttons = buttons + createBtn("修改", "btn-info btn-xs", "fa-pencil", "editAchievement("+rowObject.id+")");
                			buttons = buttons + createBtn("提交", "btn-success btn-xs", "fa-share", "commitAchievement("+rowObject.id+")");
                			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delAchievement("+rowObject.id+")");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "600px",
                //width: "100%",
                rowNum: 20,
                pager: "#achievementdatagridpager"
            });
   			
   			$("#searchAchievement").click(function() {
   				$("#achievementdatagrid").setGridParam({
   					postData:parsePostData("search_form_achievement_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#addAchievement").click(function() {
   				achievementDialog = BootstrapDialog.show({
				    title: "创建绩效",
				    width: "80%",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "workflow/achievement/queryById.action?returnType=add"),
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
				    			jQuery("#achievementContentDatagrid_add").jqGrid("saveRow", achievementLastSel2, false, "clientArray");
				    			var contents = getAchievementContents($grid);
				    			if(!contents) {
				    				return false;
				    			}
				    			var form = dialog.getModalBody().find("form");
				    			var $button = this;
				    			$.ajax({
				    				url: getRoot() + "workflow/achievement/add.action",
				    				data: form.serialize()+"&"+contents.join("&"),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						achievementGrid.jqGrid().trigger("reloadGrid");
				    					} else {
				    						BootstrapDialog.danger(json.msg);
				    					}
				    				},
				    				error: function() {
				    					BootstrapDialog.danger("系统异常，请联系管理员！");
				    					jQuery("#achievementContentDatagrid_add").trigger("reloadGrid");
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
				    	                	var $grid = dialog.getModalBody().find("form").find("table");
							    			//将未保存的先保存
							    			jQuery("#achievementContentDatagrid_add").jqGrid("saveRow", achievementLastSel2, false, "clientArray");
							    			var contents = getAchievementContents($grid);
							    			if(!contents) {
							    				return false;
							    			}
							    			var form = dialog.getModalBody().find("form");
							    			$.ajax({
							    				url: getRoot() + "workflow/achievement/commit.action?type=update",
							    				data: form.serialize()+"&"+contents.join("&")+"&passType=0",
							    				type: "POST",
							    				success: function(data) {
							    					var json = eval("("+data+")");
							    					if(json.success) {
							    						BootstrapDialog.success(json.msg);
							    						dialog.close();
							    						achievementGrid.jqGrid().trigger("reloadGrid");
							    					} else {
							    						BootstrapDialog.danger(json.msg);
							    					}
							    				},
							    				error: function() {
							    					BootstrapDialog.danger("系统异常，请联系管理员！");
							    					jQuery("#achievementContentDatagrid_add").trigger("reloadGrid");
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
   		});
   		
   		function editAchievement(id) {
   			achievementDialog = BootstrapDialog.show({
			    title: "编辑绩效",
			    width: "80%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/achievement/queryById.action?returnType=edit&achievement.id="+id),
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
			    			var $button = this;
			    			var $grid = dialog.getModalBody().find("form").find("table#achievementContentDatagrid_edit");
			    			//将未保存的先保存
			    			jQuery("#achievementContentDatagrid_edit").jqGrid("saveRow", achievementLastSel2, false, "clientArray");
			    			var contents = getAchievementContents($grid);
			    			if(!contents) {
			    				return false;
			    			}
			    			var form = dialog.getModalBody().find("form");
			    			$.ajax({
			    				url: getRoot() + "workflow/achievement/update.action",
			    				data: form.serialize()+"&"+contents.join("&"),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						achievementGrid.jqGrid().trigger("reloadGrid");
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
			    			alert(e);
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
			    	                	var $grid = dialog.getModalBody().find("form").find("table#achievementContentDatagrid_edit");
						    			//将未保存的先保存
						    			jQuery("#achievementContentDatagrid_edit").jqGrid("saveRow", achievementLastSel2, false, "clientArray");
						    			var contents = getAchievementContents($grid);
						    			if(!contents) {
						    				return false;
						    			}
						    			var form = dialog.getModalBody().find("form");
						    			$.ajax({
						    				url: getRoot() + "workflow/achievement/commit.action?type=update",
						    				data: form.serialize()+"&"+contents.join("&")+"&passType=0",
						    				type: "POST",
						    				success: function(data) {
						    					var json = eval("("+data+")");
						    					if(json.success) {
						    						BootstrapDialog.success(json.msg);
						    						dialog.close();
						    						achievementGrid.jqGrid().trigger("reloadGrid");
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
			    			alert(e);
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
   		
   		function commitAchievement(id) {
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
	   						url: getRoot() + "workflow/achievement/commit.action?type=commit",
		    				data: "achievement.id="+id,
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
		    						achievementGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function delAchievement(id) {
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
	   						url: getRoot() + "workflow/achievement/del.action",
		    				data: "achievement.id="+id,
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
		    						achievementGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function viewAchievement(id, allFlowStatus, status) {
   			var returnType = "finish";
   			var passCss = "";
   			if(allFlowStatus) {
       			var statusArr = allFlowStatus.split(",");
       			for(var i = 0; i < statusArr.length; i ++) {
       				var status = statusArr[i];
       				if(5 == status) {
       					returnType = "selfScore";
       					break;
       				} else if(6 == status) {
       					returnType = "finish";
       					break;
       				}
       			}
       		}
       		if(returnType != "selfScore") {
       			passCss = "hidden";
       		}
   			achievementDialog = BootstrapDialog.show({
			    title: "绩效详情",
			    width: "90%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/achievement/queryById.action?returnType="+returnType+"&achievement.id="+id),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [{
			    	label: "提交",
			    	icon: "fa fa-fw fa-share",
			    	cssClass: "btn-success "+passCss,
			    	action: function(dialog) {
			    		try {
			    			var totalScore = 0;
			    			var $grid = dialog.getModalBody().find("form").find("table#achievementContentDatagrid_selfScore");
			    			//将未保存的先保存
			    			jQuery("#achievementContentDatagrid_selfScore").jqGrid("saveRow", achievementLastSel2, {
	                			url: "clientArray",
	                			aftersavefunc: function() {
	                				totalScore = getSelfScoreAchievementTotalUserScore();
	                			}
	                		});
			    			var contents = getAchievementContents($grid);
			    			if(!contents) {
			    				return false;
			    			}
			    			var $button = this;
			    			var $form = dialog.getModalBody().find("form");
			    			BootstrapDialog.confirm({
					            title: "温馨提示",
					            message: "您确定自评分数为<font color='red'>"+totalScore+"</font>并提交吗？",
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
					   						url: getRoot() + "workflow/achievement/audit.action",
						    				data: "passType=0&"+$form.serialize()+"&"+contents.join("&"),
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
						    						achievementGrid.jqGrid().trigger("reloadGrid");
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
			    		} catch(ex) {
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！"+ex);
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
   		
   		function getAchievementContents($grid) {
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
   					if(!name || ""==$.trim(name) || "operationCell" == name || name.indexOf("contentScore")>=0) {
   						continue;
   					}
   					var value = content[name];
   					if("id" == name) {
   						var re = /^[0-9]*$/;
   						if(!re.test(value)) {
   							value = "";
   						}
   					}
   					value = encode(value);
   					//var value = content[name];
   					contents.push(param.replace("#index#", i).replace("#prop#", name).replace("#value#", value));
   				}
   				contents.push(param.replace("#index#", i).replace("#prop#", "orderNo").replace("#value#", i));
   			}
   			if(0 >= contents.length) {
   				BootstrapDialog.danger("获取到的项目为空");
   				return false;
   			}
   			return contents;
   		}
   	</script>
  </body>
</html>
