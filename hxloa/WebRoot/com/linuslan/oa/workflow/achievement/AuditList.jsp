<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>待登录用户审核的绩效列表</title>
    
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
    <div id="achievement_audit_list">
   		<div class="col-xs-12 no-padding">
   			<!-- <div class="box box-solid">
   				<div class="box-header with-border">
   					<h3 class="box-title">待审绩效</h3>
   					<div class="box-tools pull-right">
   						<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
	               	</div>
   				</div>
   				<form id="search_form_achievement_audit_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">年月：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.date" type="text" class="form-control pull-right" data-inputmask="'mask': '9999-99'" data-mask>
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属部门：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="departmentTree"></div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.companyId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">员工：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.userId" class="form-control select2">
									
								</select>
							</div>
							
						</div>
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<button type="button" class="btn btn-info btn-sm pull-right" data-toggle="tooltip" title="重置" id="resetSearchAuditAchievement"><i class="fa fa-fw fa-undo"></i>重置</button>
								<button type="button" class="btn btn-info btn-sm margin-right5 pull-right" data-toggle="tooltip" title="查询" id="searchAuditAchievement"><i class="fa fa-fw fa-search"></i>查询</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="box-body">
   					<table id="achievementAuditDatagrid"></table>
   					<div id="achievementAuditDatagridpager"></div>
   				</div>
   			</div> -->
   			<div class="box box-solid">
   				<!-- <div class="box-header with-border">
   					<h3 class="box-title">待审绩效</h3>
   				</div> -->
   				<form id="search_form_achievement_audit_score_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">日期：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.date" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属部门：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="departmentTree"></div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.companyId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">员工：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.userId" class="form-control select2">
									
								</select>
							</div>
							
						</div>
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<button type="button" class="btn btn-info btn-sm pull-right" data-toggle="tooltip" title="重置" id="resetSearchAuditScoreAchievement"><i class="fa fa-fw fa-undo"></i>重置</button>
								<button type="button" class="btn btn-info btn-sm margin-right5 pull-right" data-toggle="tooltip" title="查询" id="searchAuditScoreAchievement"><i class="fa fa-fw fa-search"></i>查询</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="批量通过" id="passAchievementBatch"><i class="fa fa-fw fa-share"></i>批量通过</button>
   					<button class="btn btn-danger btn-sm" data-toggle="tooltip" title="批量退回" id="rejectAchievementBatch"><i class="fa fa-fw fa-share"></i>批量退回</button>
   				</div>
   				<div class="box-body">
   					<table id="achievementScoreDatagrid"></table>
   					<div id="achievementScoreDatagridPager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var auditAchievementGrid;
   		var scoreAchievementGrid;
   		var auditAchievementDialog;
   		var achievementAuditlastsel2;
   		$(function() {
   			$("#achievement_audit_list input.date").datepicker({
   				startView: 1,
   				maxViewMode: 1,
   				minViewMode:1,
   				forceParse: false,
   				language: "zh-CN",
   				format: "yyyy-mm"
   			});
   			initSelect_achievementAudit("search_form_achievement_audit_list");
   			initSelect_achievementAudit("search_form_achievement_audit_score_list");
   			$("#search_form_achievement_audit_list").find(".departmentTree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "paramMap.departmentId",
				pidField: "pid"
			});
   			
   			$("#search_form_achievement_audit_score_list").find(".departmentTree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "paramMap.departmentId",
				pidField: "pid"
			});
   			/**
   			 * 待审核的列表
   			 */
   			auditAchievementGrid = $("#achievementAuditDatagrid").jqGrid({
                url: getRoot() + "workflow/achievement/queryAuditPage.action",
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
                	formatter: function(cellvalue, options, rowObject) {
                		return rowObject.year+"-"+rowObject.month;
                	}
                }, {
                	label: "创建人", name: "userName", width: 100, align: "center"
                }, {
                	label: "归属部门", name: "userDeptName", width: 200, align: "center"
                }, {
                	label: "归属公司", name: "companyName", width: 200, align: "center"
                }, {
                	label: "操作", width: 100, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewAuditAchievement("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+", \""+rowObject.allFlowStatus+"\")");
                		if(true == rowObject.passBtn) {
                			buttons = buttons + createBtn("通过", "btn-success btn-xs", "fa-share", "auditAchievement("+rowObject.id+", 0)");
                		}
                		if(true == rowObject.rejectBtn) {
                			buttons = buttons + createBtn("退回", "btn-danger btn-xs", "fa-reply", "auditAchievement("+rowObject.id+", 1)");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "250px",
                //width: "100%",
                rowNum: 20,
                pager: "#achievementAuditDatagridpager"
            });
            
   			$("#searchAuditAchievement").click(function() {
   				$("#achievementAuditDatagrid").setGridParam({
   					postData:parsePostData("search_form_achievement_audit_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchAuditAchievement").click(function() {
   				$("#search_form_achievement_audit_list")[0].reset();
   				$("#search_form_achievement_audit_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			
   			/**
   			 * 待评分的列表
   			 */
            scoreAchievementGrid = $("#achievementScoreDatagrid").jqGrid({
                url: getRoot() + "workflow/achievement/queryScorePage.action",
                mtype: "POST",
                rownumbers: true,
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                multiselect: true,
                colModel: [{
                	label: "ID", name: "id", hidden: true, key: true
                }, {
                	label: "时间", name: "date", width: 100, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		return rowObject.year+"-"+rowObject.month;
                	}
                }, {
                	label: "创建人", name: "userName", width: 100, align: "center"
                }, {
                	label: "归属部门", name: "userDeptName", width: 200, align: "center"
                }, {
                	label: "归属公司", name: "companyName", width: 200, align: "center"
                }, {
                	label: "绩效总分", name: "totalScore", width: 100, align: "center"
                }, {
                	label: "自评分数", name: "userScore", width: 100, align: "center"
                }, {
                	label: "当前得分", name: "leaderScore", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		var totalScore = rowObject.leaderScore + rowObject.addScore;
                		if(totalScore > 0) {
                			return totalScore;
                		} else {
                			return rowObject.userScore;
                		}
                	}
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
                	label: "操作", width: 100, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewAuditAchievement("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+", \""+rowObject.allFlowStatus+"\")");
                		var totalScore = 0;
                		if(rowObject.leaderScore && rowObject.leaderScore > 0) {
                			totalScore = rowObject.leaderScore;
                		} else {
                			totalScore = rowObject.userScore;
                		}
                		totalScore = totalScore + rowObject.addScore;
                		if(true == rowObject.passBtn) {
                			buttons = buttons + createBtn("通过", "btn-success btn-xs", "fa-share", "auditAchievement("+rowObject.id+", 0, \""+rowObject.allFlowStatus+"\", \""+totalScore+"\")");
                		}
                		if(true == rowObject.rejectBtn) {
                			buttons = buttons + createBtn("退回", "btn-danger btn-xs", "fa-reply", "auditAchievement("+rowObject.id+", 1, \""+rowObject.allFlowStatus+"\", \""+totalScore+"\")");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#achievementScoreDatagridPager"
            });
            
            $("#searchAuditScoreAchievement").click(function() {
   				$("#achievementScoreDatagrid").setGridParam({
   					postData:$("#search_form_achievement_audit_score_list").serialize()
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchAuditScoreAchievement").click(function() {
   				$("#search_form_achievement_audit_score_list")[0].reset();
   				$("#search_form_achievement_audit_score_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
            
   			/**
   			 * 批量通过
   			 */
   			$("#passAchievementBatch").click(function() {
   				var selectedIds = $("#achievementScoreDatagrid").getGridParam("selarrrow");
   				if(!selectedIds || 0 >= selectedIds.length) {
   					BootstrapDialog.danger("请至少选择一条记录");
   					return false;
   				}
   				BootstrapDialog.confirm({
   		            title: "温馨提示",
   		            message: "您确定通过<font color='red'>"+selectedIds.length+"</font>条申请吗？",
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
   		   						url: getRoot() + "workflow/achievement/auditBatch.action",
   			    				data: "passType=0&ids="+selectedIds+"&opinion=同意",
   			    				type: "POST",
   			    				success: function(data) {
   			    					var json = eval("("+data+")");
   			    					if(json.success) {
   			    						BootstrapDialog.success(json.msg);
   			    						scoreAchievementGrid.jqGrid().trigger("reloadGrid");
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
   			});
   			
   			/**
   			 * 批量退回
   			 */
   			$("#rejectAchievementBatch").click(function() {
   				var selectedIds = $("#achievementScoreDatagrid").getGridParam("selarrrow");
   				if(!selectedIds || 0 >= selectedIds.length) {
   					BootstrapDialog.danger("请至少选择一条记录");
   					return false;
   				}
   				BootstrapDialog.confirm({
   		            title: "温馨提示",
   		            message: "您确定退回<font color='red'>"+selectedIds.length+"</font>条申请吗？",
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
   		   						url: getRoot() + "workflow/achievement/auditBatch.action",
   			    				data: "passType=1&ids="+selectedIds+"&opinion=同意",
   			    				type: "POST",
   			    				success: function(data) {
   			    					var json = eval("("+data+")");
   			    					if(json.success) {
   			    						BootstrapDialog.success(json.msg);
   			    						scoreAchievementGrid.jqGrid().trigger("reloadGrid");
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
   			});
   			
   		});
   		
   		function viewAuditAchievement(id, passBtn, rejectBtn, allFlowStatus) {
   			var returnType = "view";
   			var passCss = "";
   			var rejectCss = "";
   			if(true != passBtn) {
   				passCss = "hidden";
   			}
   			if(true != rejectBtn) {
   				rejectCss = "hidden";
   			}
   			
   			if(allFlowStatus) {
       			var statusArr = allFlowStatus.split(",");
       			for(var i = 0; i < statusArr.length; i ++) {
       				var status = statusArr[i];
       				if(6 == status || 7 == status) {
       					returnType = "leaderScore";
       					break;
       				} else if(3 == status) {
       					returnType = "audit";
       				}
       			}
       		} else {
       			allFlowStatus = "";
       		}
   			auditAchievementDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "95%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/achievement/queryById.action?returnType="+returnType+"&achievement.id="+id),
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
			    			if(returnType == "leaderScore") {
			    				var totalScore = 0;
			    				var $grid = dialog.getModalBody().find("form").find("table#achievementContentDatagrid_leaderScore");
				    			//将未保存的先保存
				    			jQuery("#achievementContentDatagrid_leaderScore").jqGrid("saveRow", achievementAuditlastsel2, {
		                			url: "clientArray",
		                			aftersavefunc: function() {
		                				//totalScore = getAchievementTotalLeaderScore();
		                				//alert(totalScore);
		                			}
		                		});
				    			var contents = getLeaderScoreContent($grid);
				    			if(!contents) {
				    				return false;
				    			}
				    			totalScore = getAchievementTotalLeaderScore();
				    			var leaderScore = $form.find("div.totalScore").html();
				    			commitAuditAchievement(0, $form.serialize()+"&"+contents.join("&"), allFlowStatus, totalScore, dialog, $button);
			    			} else {
			    				var $contentOpinions = dialog.getModalBody().find("table.contentOpinion");
			    				var contentOpinions = getContentOpinions($contentOpinions);
			    				commitAuditAchievement(0, $form.serialize()+"&"+contentOpinions.join("&"), allFlowStatus, 0, dialog, $button);
			    			}
			    			
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
			    			var $contentOpinions = dialog.getModalBody().find("table.contentOpinion");
			    			var contentOpinions = getContentOpinions($contentOpinions);
			    			commitAuditAchievement(1, $form.serialize()+"&"+contentOpinions.join("&"), allFlowStatus, 0, dialog, $button)
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
   		function auditAchievement(id, passType, allFlowStatus, leaderScore) {
   			if(!id) {
   				BootstrapDialog.danger("ID无效");
   				return false;
   			}
   			var param = "achievement.id="+id;
   			commitAuditAchievement(passType, param, allFlowStatus, leaderScore);
   		}
   		
   		/**
   		 * 将传进来的参数提交到server端
   		 * @param passType 操作类型 0:通过；1：退回
   		 * @param params 传递的参数
   		 * @param dialog 打开的窗口
   		 * @param $button 点击的按钮
   		 */
   		function commitAuditAchievement(passType, params, allFlowStatus, leaderScore, dialog, $button) {
   			var tips = "";
   			if(passType == 0) {
   				tips = "通过申请";
   			} else {
   				tips = "退回申请";
   			}
   			if(allFlowStatus) {
       			var statusArr = allFlowStatus.split(",");
       			for(var i = 0; i < statusArr.length; i ++) {
       				var status = statusArr[i];
       				if(6 == status || 7 == status) {
       					tips = tips + "，评分总分为<font color='red'>"+leaderScore+"</font>";
       					break;
       				} else if(3 == status) {
       					returnType = "audit";
       				}
       			}
       		}
   			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定"+tips+"吗？",
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
	   						url: getRoot() + "workflow/achievement/audit.action",
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
		    						auditAchievementGrid.jqGrid().trigger("reloadGrid");
		    						scoreAchievementGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function getContentOpinions($contentOpinions) {
   			var param = "contentOpinions[#index#].#prop#=#value#";
   			var contentOpinions = new Array();
   			if($contentOpinions && $contentOpinions.length > 0) {
   				var j = 0;
   				for(var i = 0; i < $contentOpinions.length; i ++) {
   					var $contentOpinion = $($contentOpinions[i]);
   					var contentId = $contentOpinion.find("input[name=contentId]").val();
   					var opinion = $contentOpinion.find("textarea[name=contentOpinion]").val();
   					if(contentId && "" != $.trim(contentId) && opinion && "" != $.trim(opinion)) {
   						contentOpinions.push(param.replace("#index#", j).replace("#prop#", "contentId").replace("#value#", contentId));
   						contentOpinions.push(param.replace("#index#", j).replace("#prop#", "opinion").replace("#value#", opinion));
   						j ++;
   					}
   				}
   			}
   			return contentOpinions;
   		}
   		
   		function getLeaderScoreContent($grid) {
   			if(!$grid || 0 >= $grid.length) {
   				BootstrapDialog.error("获取绩效项目异常");
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
   				console.log(content);
   				for(var name in content) {
   					if(!name || ""==$.trim(name) || "operationCell" == name || name.indexOf("contentScore")>=0) {
   						continue;
   					}
   					var value = content[name];
  					if (""==$.trim(value)) {
	   					BootstrapDialog.danger("有空白项无法提交");
	   	   				return false;
   					}
   					if("id" == name) {
   						var re = /^[0-9]*$/;
   						if(!re.test(value)) {
   							value = "";
   						}
   					}
   					if ("leaderScoreOpinion" == name) {
	   					if (value.indexOf("同意")>=0) {
		   					BootstrapDialog.danger("不可用同意等概括词语");
		   	   				return false;
	   					}
   					}
   					//var value = content[name];
   					contents.push(param.replace("#index#", i).replace("#prop#", name).replace("#value#", value));
   				}
   			}
   			if(0 >= contents.length) {
   				BootstrapDialog.danger("获取到的项目为空");
   				return false;
   			}
   			console.log(contents);
   			return contents;
   		}
   		
   		function initSelect_achievementAudit(id) {
   			try {
   				$("#"+id+" select.select2").select2();
   				$.ajax({
   	   				url: getRoot() + "workflow/achievement/initSelect.action",
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						for(var key in json) {
   	   							
   	   							var selector = "#"+id+" select[name='paramMap."+key+"']";
   	   							initOptions_achievementAudit(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化绩效查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_achievementAudit(data, selector) {
   			if(data && data.length > 0 && selector) {
   				var option = document.createElement("option");
   				option.value = "";
   				option.text = "请选择";
   				$(selector).append(option);
   				for(var i = 0;i < data.length; i ++) {
   					var opData = data[i];
   					var option = document.createElement("option");
   					$(selector).append(option);
   					option.value = opData.id;
   					option.text = opData.name;
   				}
   			}
   		}
   	</script>
  </body>
</html>