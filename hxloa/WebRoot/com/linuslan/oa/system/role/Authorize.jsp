<%@page import="java.net.URLDecoder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'Add.jsp' starting page</title>
    
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
  	<div class="row" style="height: 100%;" id="roleAuthorize">
  		<input type="hidden" name="role.id" id="roleId" value="${param.id }" />
   		<div class="col-xs-12" style="height: 99%">
   			<div class="box box-solid" style="height: 99%">
   				<div class="box-body" style="height: 90%">
	   				<div id="authorizetreegrid"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		$(function() {
   			/*var authorizetreegrid = $("#authorizetreegrid").treegrid({
				url: getRoot() + "sys/menu/queryAuthorizeTree.action?role.id="+$("#roleAuthorize #roleId").val(),
 				pidField: "pid",
 				sizeUnit: "%",
 				loadOnExpand: false,
 				singleSelect: false,
 				loadParams: {
 					"id": "menu.id"
 				},
 				async: true,
 				columns: [{
 					field: "菜单名称", dataIndex: "text", width: 20
 				}, {
 					field: "资源列表", align: "center", width: 79,
 					formatter: function(data) {
 						var html = "<table class='authorize_button_table' style='width: 98%; border: 0; padding-left: 10px;'>";
 						var buttons = data.buttons;
 						for(var i = 0; i < buttons.length; i ++) {
 							var button = buttons[i];
 							var checked="";
							if(button.checked == true) {
								checked = "checked";
							}
							html = html + "<td style='width: 20%; border: 0; text-align: left;'><input type='checkbox' value='"+button.id+"' "+checked+" />"+button.name+"</td>";
							i ++;
							if(i < buttons.length) {
								button = buttons[i];
								checked = "";
								if(button.checked == true) {
									checked = "checked";
								}
								html = html + "<td style='width: 20%; border: 0; text-align: left;'><input type='checkbox' value='"+button.id+"' "+checked+" />"+button.name+"</td>";
							} else {
								i --;
								html = html + "<td style='width: 20%; border: 0;'></td>";
							}
							i ++;
							if(i < buttons.length) {
								button = buttons[i];
								checked = "";
								if(button.checked == true) {
									checked = "checked";
								}
								html = html + "<td style='width: 20%; border: 0; text-align: left;'><input type='checkbox' value='"+button.id+"' "+checked+" />"+button.name+"</td>";
							} else {
								i--;
								html = html + "<td style='width: 20%; border: 0;'></td>";
							}
							i ++;
							if(i < buttons.length) {
								button = buttons[i];
								checked = "";
								if(button.checked == true) {
									checked = "checked";
								}
								html = html + "<td style='width: 20%; border: 0; text-align: left;'><input type='checkbox' value='"+button.id+"' "+checked+" />"+button.name+"</td>";
							} else {
								i--;
								html = html + "<td style='width: 20%; border: 0;'></td>";
							}
							i++;
							if(i < buttons.length) {
								button = buttons[i];
								checked = "";
								if(button.checked == true) {
									checked = "checked";
								}
								html = html + "<td style='width: 20%; border: 0; text-align: left;'><input type='checkbox' value='"+button.id+"' "+checked+" />"+button.name+"</td>";
							} else {
								i--;
								html = html + "<td style='width: 20%; border: 0;'></td>";
							}
							html = html + "</tr>";
 						}
 						html = html + "</table>";
 						return html;
 					}
 				}]
			});*/
   		});
   	</script>
  </body>
</html>
