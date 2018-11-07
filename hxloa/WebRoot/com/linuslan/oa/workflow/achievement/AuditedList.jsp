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
    <div>
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<form id="search_form_achievement_audited_list" action="" class="form-horizontal">
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
								<button type="button" class="btn btn-info btn-sm pull-right" data-toggle="tooltip" title="重置" id="resetSearchAuditedAchievement"><i class="fa fa-fw fa-undo"></i>重置</button>
								<button type="button" class="btn btn-info btn-sm margin-right5 pull-right" data-toggle="tooltip" title="查询" id="searchAuditedAchievement"><i class="fa fa-fw fa-search"></i>查询</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="box-body">
   					<table id="achievementAuditedDatagrid"></table>
   					<div id="achievementAuditedDatagridPager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var auditedAchievementGrid;
   		var auditedAchievementDialog;
   		$(function() {
   			$("#search_form_achievement_audited_list input.date").datepicker({
   				startView: 1,
   				maxViewMode: 1,
   				minViewMode:1,
   				forceParse: false,
   				language: "zh-CN",
   				format: "yyyy-mm"
   			});
   			initSelect_achievementAudited("search_form_achievement_audited_list");
   			$("#search_form_achievement_audited_list").find(".departmentTree").combotree({
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
            auditedAchievementGrid = $("#achievementAuditedDatagrid").jqGrid({
                url: getRoot() + "workflow/achievement/queryAuditedPage.action",
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
                	label: "当前得分", name: "totalScore", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		var leaderScore = rowObject.leaderScore;
                		var addScore = rowObject.addScore;
                		return leaderScore+addScore;
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
                	label: "操作", width: 100, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewAuditedAchievement("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+", \""+rowObject.allFlowStatus+"\")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#achievementAuditedDatagridPager"
            });
            
            $("#searchAuditedAchievement").click(function() {
   				$("#achievementAuditedDatagrid").setGridParam({
   					postData:parsePostData("search_form_achievement_audited_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchAuditedAchievement").click(function() {
   				$("#search_form_achievement_audited_list")[0].reset();
   				$("#search_form_achievement_audited_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   		});
   		
   		function viewAuditedAchievement(id, passBtn, rejectBtn, allFlowStatus) {
   			var returnType = "view";
   			
   			auditAchievementDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "80%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/achievement/queryById.action?returnType="+returnType+"&achievement.id="+id),
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
   		
   		function initSelect_achievementAudited(id) {
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
   	   							initOptions_achievementAudited(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化绩效查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_achievementAudited(data, selector) {
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
