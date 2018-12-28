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
	<style>
		.ui-jqgrid table>tbody>tr>td.vertical_top,
		.ui-jqgrid table>tbody>tr>th.vertical_top,
		.ui-jqgrid table>tfoot>tr>td.vertical_top,
		.ui-jqgrid table>tfoot>tr>th.vertical_top,
		.ui-jqgrid table>thead>tr>td.vertical_top,
		.ui-jqgrid table>thead>tr>th.vertical_top {
			vertical-align: top;
		}
	</style>
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
							<label for="text" class="col-md-2 col-sm-4 control-label">总分：</label>
							<div class="col-md-3 col-sm-8 left-label totalScore">
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
								${(achievement.leaderScore + achievement.addScore) == 0 ? achievement.userScore : (achievement.leaderScore + achievement.addScore)}
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">加分：</label>
							<div class="col-md-3 col-sm-8 left-label">
								<input name="achievement.addScore" type="number" class="form-control" value="${achievement.addScore == null ? 0 : achievement.addScore }" />
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">奖金：</label>
							<div class="col-md-3 col-sm-8 left-label">
								<input name="achievement.addMoney" value="0" type="number" class="form-control" />
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
		   					<div class="form-group bottom-dotted-border padding-bottom5">
								<label for="leaderGroup" class="col-md-1 col-sm-1 control-label">意见：</label>
								<div class="col-md-10 col-sm-10">
									<textarea name="opinion" class="form-control" rows="3" placeholder="请输入"></textarea>
								</div>
							</div>
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
   			$("#leaderScoreAchievementForm input[name='achievement.addScore']").blur(function() {
   				getAchievementTotalLeaderScore();
   			});
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
            }, {
            	label: "领导评分", name: "leaderScore", width: 50, align: "center", editable: true, edittype: "custom",
            	editoptions: {custom_element: createNumberBox, custom_value: operateNumberValue, name: "leaderScore"},
            	formatter: function(cellValue, options, rowObject) {
            		if(rowObject.leaderScore <= 0) {
            			return rowObject.userScore;
            		} else {
            			return rowObject.leaderScore;
            		}
            	}
            }, {
            	label: "领导意见", name: "leaderScoreOpinion", width: 100, align: "center", editable: true, edittype: "custom",
            	editoptions: {
            		rows:"10",
            		width:"100%",
            		custom_element: createTextareaBox,
            		custom_value: operateTextareaValue,
            		name: "leaderScoreOpinion"
            	},
            	formatter: function(cellValue, options, rowObject) {
            		return decode(rowObject.leaderScoreOpinion);
            	}
            }];
   			var id = $("#leaderScoreAchievementForm").find("input.achievementId").val();
   			$.ajax({
   				url: getRoot() + "workflow/achievement/queryContentsByAchievementId.action?achievement.id="+id,
   				method: "POST",
   				success: function(data) {
   					var json = eval("("+data+")");
   					var leaders = json.leaders;
   					var gridData = json.contents;
   					for(var i = 0; i < leaders.length; i ++) {
   						var leader = leaders[i];
   						columns.push({label: leader.text+"评分结果", name: "contentScore"+(i+1), width: 130, align: "left", classes: "vertical_top",
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
   		                ondblClickRow: function(id){
   		                	if(id && id!==achievementAuditlastsel2){
   		                		if(achievementAuditlastsel2) {
   		                			jQuery("#achievementContentDatagrid_leaderScore").saveRow(achievementAuditlastsel2, {
   			                			url: "clientArray",
   			                			aftersavefunc: function() {
   			                				getAchievementTotalLeaderScore();
   			                			}
   			                		});
   		                		}
   		                		jQuery("#achievementContentDatagrid_leaderScore").editRow(id,true);
   		                		achievementAuditlastsel2=id;
   		                	}
   					    }
   		            });
   				}
   			});
   		}
   		
   		/**
		 * 创建金额输入框
		 */
		function createNumberBox(value, options) {
			if(!value) {
				//value = 0;
			}
			var name = options.name;
			
			//创建外围的div
			var div = document.createElement("div");
			div.setAttribute("class", "input-group spinner");
			div.setAttribute("style", "width: 99%");
			
			//创建输入框
			var input = document.createElement("input");
			div.appendChild(input);
			input.setAttribute("name", name);
			input.setAttribute("type", "number");
			input.setAttribute("class", "form-control");
			input.setAttribute("style", "width: 99%");
			input.setAttribute("value", value);
			
			return div;
		}
		
		function operateNumberValue(elem, operation, value) {
			if(operation == "get") {
				return $("input", elem).val();
			} else {
				$("input", elem).val(value);
			}
		}
		
		function getAchievementTotalLeaderScore() {
			var rowDatas = $("#achievementContentDatagrid_leaderScore").getRowData();
			var totalScore = 0;
			if(rowDatas && 0 < rowDatas.length) {
				var rowData;
				for(var i = 0; i < rowDatas.length; i ++) {
					rowData = rowDatas[i];
					try {
						var score = 0;
						if(rowData.leaderScore && rowData.leaderScore > 0) {
							score = parseInt(rowData.leaderScore);
						} else {
							score = parseInt(rowData.selfScore);
						}
						if(score) {
							totalScore += score;
						}
					} catch(ex) {
						BootstrapDialog.danger("第"+(i+1)+"项项目分数填写错误");
						break;
					}
				}
			}
			//var addScore = $("#leaderScoreAchievementForm").find("input[name='achievement.addScore']").val();
			var addScore = 0;
			totalScore = totalScore + parseInt(addScore);
			$("#leaderScoreAchievementForm").find("div.leaderTotalScore").html(totalScore);
			//$("#leaderScoreAchievementForm").find("div.totalScore").html(totalScore);
			return totalScore;
		}
   	</script>
  </body>
</html>