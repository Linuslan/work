<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新增字典项</title>
    
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
   	<form id="addDictionaryForm" action="" class="form-horizontal">
    	<div class="box-body">
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">字典项名称：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="dictionary.text" type="text" class="form-control required" id="text" placeholder="请输入名称">
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="pid" class="col-md-2 col-sm-4 control-label">上级字典项：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div id="parentCombotree"></div>
				</div>
			</div>
			<div class="form-group">
				<label for="orderNo" class="col-md-2 col-sm-4 control-label">字典值：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="dictionary.value" type="text" class="form-control" id="value" placeholder="请输入字典值">
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="orderNo" class="col-md-2 col-sm-4 control-label">排序值：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="dictionary.orderNo" type="number" class="form-control" id="orderNo" placeholder="请选择正整数">
				</div>
			</div>
			<div class="form-group">
				<label for="orderNo" class="col-md-2 col-sm-4 control-label">是否启用：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input type="radio" name="dictionary.isUse" class="minimal" value="0" checked>&nbsp;启用&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="dictionary.isUse" class="minimal" value="1" >&nbsp;禁用
				</div>
				<div class="col-md-1 col-sm-1 no-padding"></div>
				<label for="orderNo" class="col-md-2 col-sm-4 control-label"></label>
				<div class="col-md-3 col-sm-8 no-padding">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderDictionary" class="col-md-2 col-sm-4 control-label">备注：</label>
				<div class="col-md-9 col-sm-8 no-padding">
					<textarea name="dictionary.memo" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addDictionaryForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
   		});
   	</script>
  </body>
</html>
