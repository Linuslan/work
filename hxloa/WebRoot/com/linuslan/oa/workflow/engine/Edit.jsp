<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'Edit.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	<style type="text/css">
		#editFlowPanel {
			margin: 0;
			pading: 0;
			text-align: left;
			font-family: Arial, sans-serif, Helvetica, Tahoma;
			font-size: 12px;
			line-height: 1.5;
			color: black;
			background-image: url(${basePath}resources/javascript/myflow/img/bg.png);
		}
		
		.node {
			width: 70px;
			text-align: center;
			vertical-align: middle;
			border: 1px solid #fff;
		}
		
		.mover {
			border: 1px solid #ddd;
			background-color: #ddd;
		}
		
		.selected {
			background-color: #ddd;
		}
		
		.state {
			
		}
		
		#myflow_props table {
			
		}
		
		#myflow_props th {
			letter-spacing: 2px;
			text-align: left;
			padding: 6px;
			background: #ddd;
		}
		
		#myflow_props td {
			background: #fff;
			padding: 6px;
		}
		
		#pointer {
			background-repeat: no-repeat;
			background-position: center;
		}
		
		#path {
			background-repeat: no-repeat;
			background-position: center;
		}
		
		#task {
			background-repeat: no-repeat;
			background-position: center;
		}
		
		#state {
			background-repeat: no-repeat;
			background-position: center;
		}
	</style>
  </head>
  
  <body>
  	<input id="updateFlowId" type="hidden" name="flow.id" value="${flow.id }" />
    <div id="myflow_tools"
		style="position: absolute; top: 150; left: 10; border: 1px solid #09C; background-color: #fff; width: 70px; cursor: default; padding: 3px;z-index: 10000"
		class="ui-widget-content">
		<div id="myflow_tools_handle" style="text-align: center;"
			class="ui-widget-header">工具集</div>
			<div class="node" id="myflow_save"><img src="<%=basePath %>resources/javascript/myflow/img/save.gif" />&nbsp;&nbsp;保存</div>
		<div>
			<hr />
		</div>
		<div class="node selectable" id="pointer"><img
			src="<%=basePath %>resources/javascript/myflow/img/select16.gif" />&nbsp;&nbsp;选择</div>
		<div class="node selectable" id="path"><img
			src="<%=basePath %>resources/javascript/myflow/img/16/flow_sequence.png" />&nbsp;&nbsp;转换</div>
		<div>
			<hr />
		</div>
		<div class="node state" id="start" type="start"><img
			src="<%=basePath %>resources/javascript/myflow/img/16/start_event_empty.png" />&nbsp;&nbsp;开始</div>
		<div class="node state" id="state" type="state"><img
			src="<%=basePath %>resources/javascript/myflow/img/16/task_empty.png" />&nbsp;&nbsp;状态</div>
		<div class="node state" id="task" type="task"><img
			src="<%=basePath %>resources/javascript/myflow/img/16/task_empty.png" />&nbsp;&nbsp;任务</div>
		<div class="node state" id="fork" type="fork"><img
			src="<%=basePath %>resources/javascript/myflow/img/16/gateway_parallel.png" />&nbsp;&nbsp;分支</div>
		<div class="node state" id="join" type="join"><img
			src="<%=basePath %>resources/javascript/myflow/img/16/gateway_parallel.png" />&nbsp;&nbsp;合并</div>
		<div class="node state" id="end" type="end"><img
			src="<%=basePath %>resources/javascript/myflow/img/16/end_event_terminate.png" />&nbsp;&nbsp;结束</div>
		<div class="node state" id="end-cancel" type="end-cancel"><img
			src="<%=basePath %>resources/javascript/myflow/img/16/end_event_cancel.png" />&nbsp;&nbsp;取消</div>
		<div class="node state" id="end-error" type="end-error"><img
			src="<%=basePath %>resources/javascript/myflow/img/16/end_event_error.png" />&nbsp;&nbsp;错误</div>
	</div>

	<div id="myflow_props"
		style="position: absolute; top: 150; right: 50; border: 1px solid #09C; background-color: #fff; width: 320px; padding: 3px; z-index: 10000"
		class="ui-widget-content">
		<div id="myflow_props_handle" class="ui-widget-header">属性</div>
		<table border="1" width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td>aaa</td>
			</tr>
			<tr>
				<td>aaa</td>
			</tr>
		</table>
		<div>&nbsp;</div>
	</div>
    <div class="pageContent" id="editFlowPanel">
    	
    </div>
    <script type="text/javascript">
    	$(function() {
    		$.ajax({
	  			url: getRoot() + "/workflow/flow/init.action",
	  			data: "flow.id="+$("#updateFlowId").val(),
	  			type: "POST",
	  			success: function(flowData) {
	  				try {
	  					var json = eval("("+flowData+")");
	  					$.extend(true,$.myflow.config.props.props,json.props.props);
	  					$("#editFlowPanel").myflow({
				  			basePath : getRoot() + "resources/javascript/myflow/",
				  			restore : json,
				  			tools : {
				  				save : {
				  					onclick : function(data) {
				  						$.ajax({
				  							url: getRoot() + "workflow/flow/update.action",
				  							data: {
				  								"data": data,
				  								"flow.id": $("#updateFlowId").val()
				  							},
				  							type: "POST",
				  							success: function(dat) {
				  								var json = eval("("+dat+")");
				  								if(json.success == true) {
				  									BootstrapDialog.success(json.msg);
				  									flowDialog.close();
				  									flowgrid.jqGrid().trigger("reloadGrid");
				  								} else {
				  									BootstrapDialog.danger(json.msg);
				  								}
				  							},
				  							error: function() {
				  								BootstrapDialog.danger(json.msg);
				  							}
				  						});
				  					}
				  				}
				  			}
				  		});
	  				} catch(e) {
	  					BootstrapDialog.danger("系统异常："+e);
	  				}
	  			}
	  		});
    	});
    </script>
  </body>
</html>
