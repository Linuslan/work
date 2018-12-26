<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>绩效汇总列表</title>
    
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
   				<form id="search_form_achievement_report_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">日期：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="date" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属部门：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="departmentTree"></div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.company_id" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">员工：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.id" class="form-control select2">
									
								</select>
							</div>
							
						</div>
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<button type="button" class="btn btn-info btn-sm pull-right" data-toggle="tooltip" title="重置" id="resetSearchReportAchievement"><i class="fa fa-fw fa-undo"></i>重置</button>
								<button type="button" class="btn btn-info btn-sm margin-right5 pull-right" data-toggle="tooltip" title="查询" id="searchReportAchievement"><i class="fa fa-fw fa-search"></i>查询</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-info btn-sm" data-toggle="tooltip" title="导出" id="exportAchievementReport"><i class="fa fa-fw fa-share-square-o"></i>导出</button>
   				</div>
   				<div class="box-body">
   					<table id="achievementReportDatagrid"></table>
   					<div id="achievementReportDatagridPager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var reportAchievementGrid;
   		var resetTitle = true;
   		$(function() {
   			$("#search_form_achievement_report_list input.date").datepicker({
   				startView: 1,
   				maxViewMode: 1,
   				minViewMode:1,
   				forceParse: false,
   				language: "zh-CN",
   				format: "yyyy-mm"
   			});
   			initSelect_achievementReport("search_form_achievement_report_list");
   			$("#search_form_achievement_report_list").find(".departmentTree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "paramMap.department_id",
				pidField: "pid"
			});
   			var lastTime;
   			var currentTime;
            reportAchievementGrid = $("#achievementReportDatagrid").jqGrid({
                url: getRoot() + "workflow/achievement/queryReportPage.action",
                mtype: "POST",
                rownumbers: true,
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                gridComplete: function() {
                	$("#achievementReportDatagrid").jqGrid("destroyGroupHeader");
               		$("#achievementReportDatagrid").jqGrid("setGroupHeaders", {
              			useColSpanStyle: true,
              			groupHeaders:[{
              				startColumnName: "last_status", numberOfColumns: 1, titleText: lastTime
              			}, {
              				startColumnName: "status", numberOfColumns: 1, titleText: currentTime
              			}]
              		});
                },
                colModel: [{
                	label: "ID", name: "id", hidden: true, key: true
                }, {
                	label: "创建人", name: "NAME", width: 100, align: "center"
                }, {
                	label: "归属部门", name: "DEPARTMENT_NAME", width: 200, align: "center"
                }/*, {
                	label: "归属公司", name: "COMPANY_NAME", width: 200, align: "center"
                }, {
                	label: "年月", name: "last_date", width: 100, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		lastTime = rowObject.LAST_YEAR+"-"+rowObject.LAST_MONTH;
                		return lastTime;
                	}
                }*/, {
                	label: "当前状态", name: "last_status", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		lastTime = rowObject.LAST_YEAR+"-"+rowObject.LAST_MONTH;
                		var score = rowObject.LAST_TOTAL_SCORE;
                		var text = "<a href='javascript:viewAchievement_report("+rowObject.LAST_ACHIEVEMENT_ID+")'>";
                		if(rowObject.LAST_STATUS == 3) {
                			text = text + "<font color='red'>"+rowObject.LAST_FLOW_STATUS+" [ "+rowObject.LAST_AUDITOR+(score>0 ? " : "+score : "")+" ] </font>";
                		} else if(rowObject.LAST_STATUS == 0) {
                			text = text + "未提交";
                		} else if(rowObject.LAST_STATUS == 1) {
                			text = text + "退回";
                		} else if(rowObject.LAST_STATUS == 2) {
                			text = text + "撤销";
                		} else if(rowObject.LAST_STATUS == 4) {
                			text = text + "<font color='green'>已完成</font>";
                		} else {
                			text = "未创建";
                		}
                		text = text + "</a>";
                		return text;
                	}
                }/*, {
                	label: "绩效总分", name: "LAST_SCORE_WEIGHT", width: 100, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		if(rowObject.LAST_ACHIEVEMENT_ID) {
                			return "<a href='javascript:viewAchievement_report("+rowObject.LAST_ACHIEVEMENT_ID+")'>"+rowObject.LAST_SCORE_WEIGHT+"</a>";
                		} else {
                			return rowObject.LAST_SCORE_WEIGHT;
                		}
                	}
                }, {
                	label: "自评分", name: "LAST_USER_SCORE", width: 100, align: "center"
                }, {
                	label: "领导评分", name: "LAST_LEADER_SCORE", width: 100, align: "center"
                }, {
                	label: "加分", name: "LAST_ADD_SCORE", width: 100, align: "center"
                }, {
                	label: "奖金", name: "LAST_ADD_MONEY", width: 100, align: "center"
                }, {
                	label: "当前得分", name: "LAST_TOTAL_SCORE", width: 100, align: "center"
                }, {
                	label: "审核人", name: "LAST_AUDITOR", width: 100, align: "center"
                }, {
                	label: "年月", name: "date", width: 100, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		return rowObject.YEAR+"-"+rowObject.MONTH;
                	}
                }*/, {
                	label: "当前状态", name: "status", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		currentTime = rowObject.YEAR+"-"+rowObject.MONTH;
                		var score = rowObject.TOTAL_SCORE;
                		var text = "<a href='javascript:viewAchievement_report("+rowObject.ACHIEVEMENT_ID+")'>";
                		if(rowObject.STATUS == 3) {
                			text = text + "<font color='red'>"+rowObject.FLOW_STATUS+" [ "+rowObject.AUDITOR+(score>0 ? " : "+score : "")+" ] </font>";
                		} else if(rowObject.STATUS == 0) {
                			text = text + "未提交";
                		} else if(rowObject.STATUS == 1) {
                			text = text + "退回";
                		} else if(rowObject.STATUS == 2) {
                			text = text + "撤销";
                		} else if(rowObject.STATUS == 4) {
                			text = text + "<font color='green'>已完成</font>";
                		} else {
                			text = "未创建";
                		}
                		text = text + "</a>";
                		return text;
                	}
                }/*, {
                	label: "绩效总分", name: "SCORE_WEIGHT", width: 100, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		if(rowObject.ACHIEVEMENT_ID) {
                			return "<a href='javascript:viewAchievement_report("+rowObject.ACHIEVEMENT_ID+")'>"+rowObject.SCORE_WEIGHT+"</a>";
                		} else {
                			return rowObject.SCORE_WEIGHT;
                		}
                	}
                }, {
                	label: "自评分数", name: "USER_SCORE", width: 100, align: "center"
                }, {
                	label: "领导评分", name: "LEADER_SCORE", width: 100, align: "center"
                }, {
                	label: "加分", name: "ADD_SCORE", width: 100, align: "center"
                }, {
                	label: "奖金", name: "ADD_MONEY", width: 100, align: "center"
                }, {
                	label: "当前得分", name: "TOTAL_SCORE", width: 100, align: "center"
                }, {
                	label: "审核人", name: "AUDITOR", width: 100, align: "center"
                }*/],
				viewrecords: true,
                height: "580px",
                //width: "100%",
                rowNum: 20,
                pager: "#achievementReportDatagridPager"
            });
   			
   			$("#searchReportAchievement").click(function() {
   				$("#achievementReportDatagrid").setGridParam({
   					postData:parsePostData("search_form_achievement_report_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#exportAchievementReport").click(function() {
	  			var year = $("#search_form_achievement_report_list input[name=date]").val().split("-")[0];
	  			if(!year) {
	  				var currentDate = new Date();
	  				year = currentDate.getFullYear();
	  			}
	  			BootstrapDialog.confirm({
		            title: "温馨提示",
		            message: "确定导出<font color='red'>"+year+"年</font>全年绩效吗？",
		            type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
		            closable: true, // <-- Default value is false
		            draggable: true, // <-- Default value is false
		            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
		            btnOKLabel: "确定", // <-- Default value is 'OK',
		            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
		            callback: function(y) {
		                // result will be true if button was click, while it will be false if users close the dialog directly.
		                if(y) {
		                	var form = $("<form>");
		  					form.attr("style", "display:none");
		  					form.attr("action", getRoot() + "workflow/achievement/exportYearAchievement.action");
		  					form.attr("method", "POST");
		  					var input = $("<input>");
		  					input.attr("value", year);
		  					input.attr("name", "year");
		  					$("body").append(form);
		  					form.append(input);
		  					form.submit();
		  					form.remove();
		                }
		            }
	  			});
   			});
   		});
   		
   		function initSelect_achievementReport(id) {
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
   	   							initOptions_achievementReport(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化绩效查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_achievementReport(data, selector) {
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
   		
   		function viewAchievement_report(id) {
   			var returnType = "finish";
   			
   			achievementDialog = BootstrapDialog.show({
			    title: "绩效详情",
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
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-danger",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
   		}
   	</script>
  </body>
</html>
