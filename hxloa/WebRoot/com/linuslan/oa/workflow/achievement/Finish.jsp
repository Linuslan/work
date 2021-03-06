<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/c" %>
<%@ taglib prefix="fmt" uri="/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP "Add.jsp" starting page</title>
    
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
   	<form id="leaderScoreAchievementForm" action="" class="form-horizontal">
   		<input class="achievementId" type="hidden" name="achievement.id" id="viewAchievementId" value="${achievement.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#achievement" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#achievementAuditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="achievement">
   					<div class="box-body padding-bottom5 bottom-dotted-border">
   						<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">申请人：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${achievement.userName }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">归属部门：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${achievement.userDeptName }
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">归属公司：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${achievement.companyName }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">日期：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${achievement.year }-${achievement.month }
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">当前总分：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${achievement.leaderScore }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">自评总分：</label>
							<div class="col-md-3 col-sm-8 left-label selfTotalScore">
								${achievement.userScore }
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">备注：</label>
							<div class="col-md-10 col-sm-8 left-label totalScore" style="color: red">
								1、绩效在创建、自评、提交领导评分，这三个阶段所有需填项必需填满，绩效才可以提交。（如以下情况：①员工创建绩效时，多添加两行无内容时，提示提交不了绩效；②创建阶段时，如出现考核项目、具体指标、数据来源、标准、权重这几项内容只要出现一项未填写，则无法提交。③ 自评阶段时，如出现完成情况、自评分数这两项内容只要出现一项未填写，则无法提交，分数出现小数点情况也无法提交。）
								<br />
								2、 直属领导需对员工每项绩效指标进行评分意见填写，不可用同意等概括词语，领导1意见、领导2意见如对直属上级的评分认同，可以“同意”，或是填写其它意见。
							</div>
						</div>
						<!-- <div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">当前得分：</label>
							<div class="col-md-3 col-sm-8 left-label leaderTotalScore">
								${achievement.leaderScore }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">加分：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${achievement.addScore }
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">奖金：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${achievement.addMoney }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label"></label>
							<div class="col-md-3 col-sm-8 left-label">
								
							</div>
						</div> -->
			    	</div>
			    	<div class="box-body">
			    		<table id="achievementContentDatagrid_leaderScore"></table>
			    	</div>
   				</div>
   				<div class="tab-pane" id="achievementAuditorLogs">
   					<div class="box box-solid">
		   				
		   				<div class="box-body">
		   					
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="achievementauditorlogsdatagrid"></table>
	   							<div id="achievementauditorlogsdatagridpager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
   		</div>
    	
   	</form>
   	<script type="text/javascript">
   		$(function() {
            //需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateAchievementAuditLogGrid()", 0);
			setTimeout("generateAuditAchievementContenGrid()", 0);
   		});
   		
   		function generateAchievementAuditLogGrid() {
   			var id = $("#leaderScoreAchievementForm").find("input.achievementId").val();
   			$("#achievementauditorlogsdatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=achievement&wfId="+id,
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
                	label: "创建审核人", name: "auditorName", width: 150, align: "center"
                }, {
                	label: "创建审核时间", name: "auditDate", width: 200, align: "center"
                }, {
                	label: "创建审核意见", name: "opinion", width: 400, align: "center"
                }, {
                	label: "操作类型", name: "passType", width: 100, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		if(rowObject.passType == "0") {
                			return "通过";
                		} else {
                			return "退回";
                		}
                	}
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20,
                pager: "#achievementauditorlogsdatagridpager"
            });
   		}
   		
   		function generateAuditAchievementContenGrid() {
   			console.log("开始查询绩效数据");
   			var id = $("#leaderScoreAchievementForm").find("input.achievementId").val();
   			var columns = [{
            	label: "ID", name: "id", hidden: true
            }, {
            	label: "考核项目", name: "title", width: 100, align: "left",
            	formatter: function(cellvalue, options, rowObject) {
            		return decode(rowObject.title);
            	}
            }, {
            	label: "具体指标", name: "content", width: 250, align: "left",
            	formatter: function(cellvalue, options, rowObject) {
            		return decode(rowObject.content);
            	}
            }, {
            	label: "数据来源", name: "source", width: 200, align: "left",
            	formatter: function(cellvalue, options, rowObject) {
            		return decode(rowObject.source);
            	}
            }, {
            	label: "标准", name: "formula", width: 200, align: "left",
            	formatter: function(cellvalue, options, rowObject) {
            		return decode(rowObject.formula);
            	}
            }, {
            	label: "权重", name: "scoreWeight", width: 50, align: "center"
            }, {
            	label: "完成情况", name: "performance", width: 200, align: "left",
            	formatter: function(cellvalue, options, rowObject) {
            		return decode(rowObject.performance);
            	}
            }, {
            	label: "自评", name: "userScore", width: 50, align: "center"
            }/*, {
            	label: "领导评分", name: "leaderScore", width: 50, align: "center"
            }*/];
   			console.log("开始请求数据");
   			$.ajax({
   				url: getRoot() + "workflow/achievement/queryContentsByAchievementId.action?achievement.id="+id,
   				method: "POST",
   				success: function(data) {
   					console.log("获取到的数据为："+data);
   					var json = eval("("+data+")");
   					var leaders = json.leaders;
   					var gridData = json.contents;
   					for(var i = 0; i < leaders.length; i ++) {
   						var leader = leaders[i];
   						columns.push({label: leader.text+"评分", name: "contentScore"+(i+1), width: 130, align: "left", classes: "vertical_top",
   	   		            	formatter: function(cellvalue, options, rowObject) {
   	   		            		var opinion = "暂未填写";
   	   		            		var score = "未评分";
   	   		            		if(cellvalue) {
   	   		            			opinion = cellvalue.opinion;
   	   		            			score = cellvalue.score;
   	   		            		}
   	   		            		//return "<div style='height: 100%;'><div style='height: 30px; text-align: center; margin: 0 auto;'>"+score+"</div><div style='border-top: 1px solid black;height: 30px; text-align: center; vertical-align: middle;'>审核意见</div><div style='border-top: 1px solid black; height: 50px;'>"+opinion+"</div></div>";
   	   		            		return "<div style='line-height:30px;'>分数："+score+"</div><div style='line-height:30px;'>意见："+opinion+"</div>";
   	   		            	}
   						});
   					}
   					console.log("开始生成表格");
   					$("#achievementContentDatagrid_leaderScore").jqGrid({
   		   				//url: getRoot() + "workflow/achievement/queryContentsByAchievementId.action?achievement.id="+id,
   		                //mtype: "POST",
   		                data: gridData,
   		                shrinkToFit: true,
   		                //autowidth: true,
   		                //scrollrows: false,
   		                //scroll: false,
   						styleUI : "Bootstrap",
   		                datatype: "local",
		                gridComplete: function() {
		                	$("#achievementContentDatagrid_leaderScore").setGridWidth($("#achievement").width()*0.99);
		                	//$("#achievementContentDatagrid_leaderScore").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
		                },
   		                //data: [{"id": 1, "remittanceDate": "2016-04-05", "achievementClassName": "d", "achievementClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
   		                colModel: columns,
   						viewrecords: true,
   		                height: "100%",
   		                //width: "100%",
   		                rowNum: 20,
   		                subGrid: true,
   		                subGridOptions: {
   		                	expandOnLoad: true
   		                },
   		                subGridRowColapsed: function(subgrid_id, rowid) {
   		                	return true;
   		                },
   		                subGridRowExpanded: function(subgrid_id, rowid) {
   		                	//console.log(rowid);
   		                	var rowData = $("#achievementContentDatagrid_edit").getRowData(rowid);
   		                	//console.log(rowData);
   		                	var id = rowData.id;
   		                	if(rowid) {
   		                		var tableId = subgrid_id+"_table";
   		                		var html = "<div style='padding: 10px;'>";
   		                		html = html + "<table id='"+tableId+"' class='scroll'></table>";
   		                		html = html + "</div>";
   		                		var subgrid = $("#"+subgrid_id).html(html);
   		                		$("#"+tableId).jqGrid({
   		                			url: getRoot() + "workflow/achievement/queryContentOpinionsByContentId.action?achievementContent.id="+rowid,
   		                			datatype: "json",
   		                			height: "100%",
   		                			shrinkToFit: true,
   					                autowidth: true,
   					                scrollrows: false,
   					                scroll: false,
   		                			colModel: [{
   					                	label: "ID", name: "id", hidden: true
   					                }, {
   					                	label: "创建审核人", name: "userName", width: 150, align: "center"
   					                }, {
   					                	label: "创建审核意见", name: "opinion", width: 400, align: "center"
   					                }, {
   					                	label: "创建审核时间", name: "createDate", width: 150, align: "center"
   					                }]
   		                		});
   		                	}
   		                }
   		            });
   				}
   			});
   		}
   		
   		/*function generateAuditAchievementContenGrid() {
   			var id = $("#leaderScoreAchievementForm").find("input.achievementId").val();
   			$("#achievementContentDatagrid_leaderScore").jqGrid({
   				url: getRoot() + "workflow/achievement/queryContentsByAchievementId.action?achievement.id="+id,
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "achievementClassName": "d", "achievementClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "考核项目", name: "title", width: 300, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.title);
                	}
                }, {
                	label: "具体指标", name: "content", width: 350, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.content);
                	}
                }, {
                	label: "数据来源", name: "source", width: 350, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.source);
                	}
                }, {
                	label: "标准", name: "formula", width: 350, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.formula);
                	}
                }, {
                	label: "权重", name: "scoreWeight", width: 50, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.scoreWeight);
                	}
                }, {
                	label: "完成情况", name: "performance", width: 350, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.performance);
                	}
                }, {
                	label: "自评分数", name: "userScore", width: 100, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.userScore);
                	}
                }, {
                	label: "领导评分", name: "leaderScore", width: 100, align: "center"
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20,
                subGrid: true,
                subGridOptions: {
                	expandOnLoad: false
                },
                subGridRowColapsed: function(subgrid_id, rowid) {
                	return true;
                },
                subGridRowExpanded: function(subgrid_id, rowid) {
                	//console.log(rowid);
                	var rowData = $("#achievementContentDatagrid_edit").getRowData(rowid);
                	//console.log(rowData);
                	var id = rowData.id;
                	if(rowid) {
                		var tableId = subgrid_id+"_table";
                		var html = "<div style='padding: 10px;'>";
                		html = html + "<table id='"+tableId+"' class='scroll'></table>";
                		html = html + "</div>";
                		var subgrid = $("#"+subgrid_id).html(html);
                		$("#"+tableId).jqGrid({
                			url: getRoot() + "workflow/achievement/queryContentOpinionsByContentId.action?achievementContent.id="+rowid,
                			datatype: "json",
                			height: "100%",
                			shrinkToFit: true,
			                autowidth: true,
			                scrollrows: false,
			                scroll: false,
                			colModel: [{
			                	label: "ID", name: "id", hidden: true
			                }, {
			                	label: "审核人", name: "userName", width: 150, align: "center"
			                }, {
			                	label: "审核意见", name: "opinion", width: 400, align: "center"
			                }, {
			                	label: "审核时间", name: "createDate", width: 150, align: "center"
			                }]
                		});
                	}
                }
            });
   		}*/
   	</script>
  </body>
</html>