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
   	<form id="addLeaveForm" action="" class="form-horizontal">
    	<div class="box-body">
			<div class="form-group">
				<label for="departmentId" class="col-md-2 col-sm-4 control-label">请假类型：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<select class="select2" name="leave.classId" style="width: 100%;">
						<option value="">请选择</option>
						<c:forEach items="${leaveClasses }" var="leaveClass">
							<option value="${leaveClass.id }">${leaveClass.text }</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="departmentId" class="col-md-2 col-sm-4 control-label">累计时长：</label>
				<div class="col-md-3 col-sm-8 left-label duration">
					
				</div>
			</div>
			<div class="form-group">
				<label for="moneyDate" class="col-md-2 col-sm-4 control-label">备注：</label>
				<div class="col-md-10 col-sm-8 left-label">
					${perDayDuration }小时/天，上午：${amStartTime }-${amEndTime }；下午：${pmStartTime }-${pmEndTime }。
				</div>
			</div>
			<div class="form-group">
				<label for="moneyDate" class="col-md-2 col-sm-4 control-label"></label>
				<div class="col-md-10 col-sm-8 left-label">
					遇跨周请假，正常双休时间系统会进行合计，请分两次填写请假条！
				</div>
			</div>
			<div class="form-group">
				<label for="moneyDate" class="col-md-2 col-sm-4 control-label">请假时间：</label>
				<div class="col-md-10 col-sm-8 no-padding">
					<div class="col-md-4 col-sm-12 no-padding">
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-clock-o"></i>
							</div>
							<input type="text" name="startDate" class="form-control pull-right dateTimeRange">
						</div>
					</div>
					<div class="col-md-1 col-sm-12 center-label" style="text-align: center">
						至
					</div>
					<div class="col-md-4 col-sm-12 no-padding">
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-clock-o"></i>
							</div>
							<input type="text" name="endDate" class="form-control pull-right dateTimeRange">
						</div>
					</div>
					
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">事由：</label>
				<div class="col-md-9 col-sm-8 no-padding">
					<textarea name="leave.content" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addLeaveForm").find(".select2").select2();
			$("#addLeaveForm").find("input.dateTimeRange[name='startDate'], input.dateTimeRange[name='endDate']").daterangepicker({timePicker: true, timePicker12Hour: false, timePickerIncrement: 1, format: "YYYY-MM-DD HH:mm"});
			$("#addLeaveForm").find("input.dateTimeRange").on("apply.daterangepicker", function(ev, picker) {
				$("#addLeaveForm").find("input.dateTimeRange[name='startDate']").val(picker.startDate.format("YYYY-MM-DD HH:mm"));
				$("#addLeaveForm").find("input.dateTimeRange[name='endDate']").val(picker.endDate.format("YYYY-MM-DD HH:mm"));
				$.ajax({
					url: getRoot() + "workflow/leave/countLeaveDuration.action",
					data: {
						"startDate": picker.startDate.format("YYYY-MM-DD HH:mm"),
						"endDate": picker.endDate.format("YYYY-MM-DD HH:mm")
					},
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						if(json.success) {
							$("#addLeaveForm").find("div.duration").html(json.msg);
						} else {
							BootstrapDialog.danger(json.msg)
						}
					},
					error: function() {
						BootstrapDialog.danger("获取请假时长失败，系统异常！");
					}
				});
			});
   		});
   	</script>
  </body>
</html>
