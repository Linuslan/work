<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>编辑绩效申请</title>
    
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
   	<form id="selfScoreAchievementForm" action="" class="form-horizontal">
   		<input id="editAchievementId" class="achievementId" name="achievement.id" type="hidden" value="${achievement.id }" />
   		<input id="editAchievementId" class="achievementUserScore" name="achievement.userScore" type="hidden" value="${achievement.userScore }" />
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
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
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
								${achievement.totalScore }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">自评总分：</label>
							<div class="col-md-3 col-sm-8 left-label selfTotalScore">
								
							</div>
						</div>
			    	</div>
			    	<div class="box-body">
			    		<table id="achievementContentDatagrid_selfScore"></table>
			    	</div>
			    </div>
			   	<div class="tab-pane" id="achievementAuditorLogs">
   					<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="achievementauditorlogsdatagrid_selfScore"></table>
	   							<div id="achievementauditorlogsdatagridpager_selfScore"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		var achievementDeptCombotree_edit;
   		$(function() {
   			$("#selfScoreAchievementForm").find(".select2").select2();
   			$("#selfScoreAchievementForm").find(".date").datepicker({format: "yyyy-mm", language: "zh-CN"});
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateSelfScoreGrid()", 0);
            setTimeout("generateSelfScoreAchievementLogGrid()", 0);
   		});
   		var rowDatas;
   		function generateSelfScoreGrid() {
   			var id = $("#selfScoreAchievementForm").find("input.achievementId").val();
   			$("#achievementContentDatagrid_selfScore").jqGrid({
   				url: getRoot() + "workflow/achievement/queryContentsByAchievementId.action?achievement.id="+id,
                mtype: "POST",
                shrinkToFit: true,
                /*autowidth: true,
                scrollrows: false,
                scroll: false,*/
				styleUI : "Bootstrap",
                datatype: "json",
                gridComplete: function() {
                	$("#achievementContentDatagrid_selfScore").setGridWidth($("#achievement").width()*0.99);
                	//$("#achievementContentDatagrid_selfScore").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
                },
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "achievementClassName": "d", "achievementClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "考核项目", name: "title", width: 150, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.title);
                	}
                }, {
                	label: "具体指标", name: "content", width: 350, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.content);
                	}
                }, {
                	label: "数据来源", name: "source", width: 150, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.source);
                	}
                }, {
                	label: "标准", name: "formula", width: 200, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.formula);
                	}
                }, {
                	label: "权重", name: "scoreWeight", width: 50, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.scoreWeight);
                	}
                }, {
                	label: "完成情况", name: "performance", width: 350, align: "left", editable: true, edittype: "custom",
                	editoptions: {
                		rows: "10",
                		width: "100%",
                		custom_element: createTextareaBox,
                		custom_value: operateTextareaValue,
                		name: "performance"
                	},
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.performance);
                	}
                }, {
                	label: "自评分数", name: "userScore", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "userScore"
                	}
                }],
                ondblClickRow: function(id){
                	if(achievementLastSel2) {
            			jQuery("#achievementContentDatagrid_selfScore").saveRow(achievementLastSel2, {
                			url: "clientArray",
                			aftersavefunc: function() {
                				getSelfScoreAchievementTotalUserScore();
                			}
                		});
            		}
            		jQuery("#achievementContentDatagrid_selfScore").editRow(id,true);
            		achievementLastSel2=id;
			    },
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20
            });
   		}
   		
   		function generateSelfScoreAchievementLogGrid() {
   			$("#achievementauditorlogsdatagrid_selfScore").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=achievement&wfId="+$("#editAchievementId").val(),
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
                	label: "审核人", name: "auditorName", width: 150, align: "center"
                }, {
                	label: "审核时间", name: "auditDate", width: 200, align: "center"
                }, {
                	label: "意见", name: "opinion", width: 400, align: "center"
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
                pager: "#achievementeditlogsdatagridpager_selfScore"
            });
   		}
   		
   		/**
		 * 创建金额输入框
		 */
		function createNumberBox(value, options) {
			if(!value) {
				value = 0;
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
		
		function getSelfScoreAchievementTotalUserScore() {
			var rowDatas = $("#achievementContentDatagrid_selfScore").getRowData();
			var totalScore = 0;
			if(rowDatas && 0 < rowDatas.length) {
				var rowData;
				for(var i = 0; i < rowDatas.length; i ++) {
					rowData = rowDatas[i];
					try {
						if(rowData.scoreWeight) {
							var score = parseInt(rowData.userScore);
							if(score) {
								totalScore += score;
							}
						}
					} catch(ex) {
						BootstrapDialog.danger("第"+(i+1)+"项项目分数填写错误");
						break;
					}
				}
			}
			$("#selfScoreAchievementForm").find("div.selfTotalScore").html(totalScore);
			$("#selfScoreAchievementForm").find("input.achievementUserScore").val(totalScore);
			return totalScore;
		}
   	</script>
  </body>
</html>
