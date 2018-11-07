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
   	<form id="addWorkOvertimeForm" action="" class="form-horizontal">
   		<input type="hidden" id="viewWorkOvertimeId" value="${workOvertime.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#workOvertime" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#auditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="workOvertime">
   					<div class="box-body">
			    		<div class="form-group">
							<label for="departmentId" class="col-md-2 col-sm-4 control-label">加班类型：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								${workOvertime.className }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="departmentId" class="col-md-2 col-sm-4 control-label">累计时长：</label>
							<div class="col-md-3 col-sm-8 left-label duration">
								${workOvertime.duration }
							</div>
						</div>
						<div class="form-group">
							<label for="moneyDate" class="col-md-2 col-sm-4 control-label">备注：</label>
							<div class="col-md-10 col-sm-8 left-label">
								${perDayDuration }小时/天，上午：${amStartTime }-${amEndTime }；下午：${pmStartTime }-${pmEndTime }。
							</div>
						</div>
						<div class="form-group">
							<label for="moneyDate" class="col-md-2 col-sm-4 control-label">加班时间：</label>
							<div class="col-md-10 col-sm-8 left-label">
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${workOvertime.startDate }"/>
								&nbsp;&nbsp;至&nbsp;&nbsp;
								<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${workOvertime.endDate }"/>
							</div>
						</div>
						<div class="form-group">
							<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">事由：</label>
							<div class="col-md-9 col-sm-8 left-label">
								${workOvertime.content }
							</div>
						</div>
			    	</div>
   				</div>
   				<div class="tab-pane" id="auditorLogs">
   					<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="auditorlogsdatagrid"></table>
	   							<div id="auditorlogsdatagridpager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
   		</div>
    	
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#auditorlogsdatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=workOvertime&wfId="+$("#viewWorkOvertimeId").val(),
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
                pager: "#auditorlogsdatagridpager"
            });
   		});
   	</script>
  </body>
</html>
