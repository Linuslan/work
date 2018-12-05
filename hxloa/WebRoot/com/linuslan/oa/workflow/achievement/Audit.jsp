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
   	<form id="auditAchievementForm" action="" class="form-horizontal">
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
							<label for="text" class="col-md-2 col-sm-4 control-label">备注：</label>
							<div class="col-md-10 col-sm-8 left-label totalScore" style="color: red">
								直属领导需对员工每项绩效指标进行评分意见填写，不可用同意等词语，领导1意见、领导2意见可对上级的评分进行同意，或是另写意见。
							</div>
						</div>
			    	</div>
			    	<div class="box-body">
			    		<table id="achievementContentDatagrid_audit"></table>
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
   			$("#auditAchievementForm a[data-toggle='tab']").on("shown.bs.tab", function (e) {
   				var href = $(this).attr("href");
   				var id = href.substring(1);
   				if(id == "achievementAuditorLogs") {
   					$("#"+id).find("table").setGridWidth($("#"+id).width()*0.99);
   				}
   				
   			});
            //需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateAchievementAuditLogGrid()", 0);
			setTimeout("generateAuditAchievementContenGrid()", 0);
   		});
   		
   		function generateAchievementAuditLogGrid() {
   			var id = $("#auditAchievementForm").find("input.achievementId").val();
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
   			var id = $("#auditAchievementForm").find("input.achievementId").val();
   			$("#achievementContentDatagrid_audit").jqGrid({
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
                	label: "考核项目", name: "title", width: 150, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.title);
                	}
                }, {
                	label: "具体指标", name: "content", width: 400, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.content);
                	}
                }, {
                	label: "数据来源", name: "source", width: 300, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.source);
                	}
                }, {
                	label: "标准", name: "formula", width: 300, align: "left",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.formula);
                	}
                }, {
                	label: "权重", name: "scoreWeight", width: 80, align: "center"
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20,
                subGrid: true,
                jsonReader: {
                	root: "contents"
                },
                subGridOptions: {
                	expandOnLoad: true
                },
                subGridRowColapsed: function(subgrid_id, rowid) {
                	return false;
                },
                subGridRowExpanded: function(subgrid_id, rowid) {
                	var rowData = $("#achievementContentDatagrid_audit").getRowData(rowid);
                	var id = rowData.id;
                	if(id) {
                		var tableId = subgrid_id+"_table";
                		var html = "<table style='width: 100%' class='contentOpinion'><tr><td class='align-right' style='width: 10%;'>审核意见：</td><td><input name='contentId' type='hidden' value='"+id+"' /><textarea name='contentOpinion' style='width: 100%' rows='5'></textarea></td></tr></table>";
                		html = html + "<table id='"+tableId+"' class='scroll'></table>";
                		var subgrid = $("#"+subgrid_id).html(html);
                		$("#"+tableId).jqGrid({
                			url: getRoot() + "workflow/achievement/queryContentOpinionsByContentId.action?achievementContent.id="+id,
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
   	</script>
  </body>
</html>
