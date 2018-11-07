<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>部门管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body>
    <div class="row" style="height: 100%; overflow-y: auto;">
   		<div class="col-xs-12">
   			<div class="box box-solid">
   				<div class="box-body no-padding">
   					<!-- <div class="row search-panel width-border">
   						<div class="col-md-12 col-xs-12">
   							<div class="col-md-1 col-xs-12 text-align-right no-padding">部门名称：</div>
   							<div class="col-md-2 col-xs-12 no-padding">
   								<div id="combotree"></div>
   							</div>
   						</div>
   					</div> -->
   					<div class="toolbar with-border">
	   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增用户组" id="addGroup"><i class="fa fa-fw fa-plus"></i>新增</button>
	   				</div>
	   				<div id="grouptreegrid" style="height: 100; overflow-y: auto;"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var groupList;
   		$(function() {
   			/*$("#combotree").combotree({
   				url: getRoot() + "group/queryGroupList.action",
   				async: true,
   				singleSelect: false,
   				loadOnExpand: true,
   				loadParams: {
   					"id": "group.id"
   				},
   				idField: "id",
   				textField: "text"
   			});*/
   			groupList = $("#grouptreegrid").treegrid({
   				url: getRoot() + "sys/group/queryTree.action",
   				sizeUnit: "%",
   				loadOnExpand: false,
   				idField: "id",
   				textField: "text",
   				pidField: "pid",
   				loadParams: {
   					"id": "group.id"
   				},
   				async: true,
   				onRowClick: function() {
   					
   				},
   				columns: [{
   					field: "用户组名称", dataIndex: "text", width: 20
   				}, {
   					field: "ID", dataIndex: "id", width: 10, align: "center"
   				}, {
   					field: "用户组标识", dataIndex: "groupId", width: 20, align: "center"
   				}, {
   					field: "归属部门", dataIndex: "departmentName", width: 10, align: "center"
   				}, {
   					field: "排序值", dataIndex: "orderNo", align: "center", width: 10, align: "center"
   				}, {
   					field: "操作", align: "center", width: 20,
   					formatter: function(data) {
   						var buttons = createBtn("修改", "btn-success btn-xs", "fa-pencil", "editGroup("+data.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delGroup("+data.id+")");
   						return buttons;
   					}
   				}]
   			});
   			
   			$("#addGroup").click(function() {
   				BootstrapDialog.show({
				    title: "新增用户组",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/group/Add.jsp"),
				    draggable: true,
				    size: BootstrapDialog.SIZE_WIDE,
				    autodestroy: false,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	dialogRef.getModalBody().find("form").validate();
				    	dialogRef.getModalBody().find("#parentCombotree").combotree({
   							url: getRoot() + "sys/group/queryTree.action",
							async: true,
							singleSelect: true,
							loadOnExpand: false,
							loadParams: {
								"id": "group.id"
							},
							idField: "id",
							textField: "text",
							name: "group.pid"
   						});
   						dialogRef.getModalBody().find("#departmentCombotree").combotree({
   							url: getRoot() + "sys/department/queryTree.action",
   							async: true,
   							singleSelect: true,
   							loadOnExpand: false,
   							loadParams: {
   								"id": "department.id"
   							},
   							idField: "id",
   							textField: "name",
   							name: "group.departmentId"
   						});
				    },
				    buttons: [{
				    	label: "保存",
				    	icon: "fa fa-fw fa-save",
				    	cssClass: "btn-success",
				    	autospin: true,
				    	action: function(dialog) {
				    		var $button = this;
				    		try {
				    			var form = dialog.getModalBody().find("form");
				    			
				    			$.ajax({
				    				url: getRoot() + "sys/group/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						groupList.refresh();
				    					} else {
				    						BootstrapDialog.danger(json.msg);
				    					}
				    				},
				    				error: function() {
				    					BootstrapDialog.danger("系统异常，请联系管理员");
				    				},
				    				beforeSend: function() {
				    					dialog.enableButtons(false);
				    					dialog.setClosable(false);
				    				},
				    				complete: function() {
				    					$button.stopSpin();
				    					dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    				}
				    			});
				    		} catch(e) {
				    			BootstrapDialog.danger("系统异常，请联系管理员！");
				    		}
				    	}
				    }, {
				    	label: "关闭",
				    	icon: "fa fa-fw fa-close",
				    	cssClass: "btn-danger",
				    	action: function(dialog) {
				    		dialog.close();
				    	}
				    }]
		        });
   			});
   		});
   		
   		function editGroup(id) {
   			BootstrapDialog.show({
			    title: "编辑用户组",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/group/queryById.action?returnType=edit&group.id="+id),
			    draggable: true,
			    size: BootstrapDialog.SIZE_WIDE,
			    autodestroy: false,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	dialogRef.getModalBody().find("form").validate();
			    	//初始化父部门树，只有在这里等弹出框的内容都加载完之后再加载树才能够获取到宽度
			    	dialogRef.getModalBody().find("#parentCombotree").combotree({
  						url: getRoot() + "sys/group/queryTree.action",
						async: true,
						singleSelect: true,
						loadOnExpand: false,
						loadParams: {
							"id": "group.id"
						},
						idField: "id",
						textField: "text",
						pidField: "pid",
						name: "group.pid"
  					});
  					
  					dialogRef.getModalBody().find("#departmentCombotree").combotree({
						url: getRoot() + "sys/department/queryTree.action",
						async: true,
						singleSelect: true,
						loadOnExpand: false,
						loadParams: {
							"id": "department.id"
						},
						idField: "id",
						textField: "name",
						name: "group.departmentId"
					});
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	autospin: true,
			    	action: function(dialog) {
			    		try {
			    			var $button = this;
			    			var form = dialog.getModalBody().find("form");
			    			$.ajax({
			    				url: getRoot() + "sys/group/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						groupList.refresh();
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				},
			    				beforeSend: function() {
			    					dialog.enableButtons(false);
			    					dialog.setClosable(false);
			    				},
			    				complete: function() {
			    					dialog.enableButtons(true);
			    					dialog.setClosable(true);
			    					$button.stopSpin();
			    				}
			    			});
			    		} catch(e) {
			    			dialog.enableButtons(true);
			    			dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-danger",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
   		}
   		
   		function delGroup(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/group/del.action",
	    				data: "group.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						groupList.refresh();
	    					} else {
	    						BootstrapDialog.danger(json.msg);
	    					}
	    				}
   					});
   				}
   			});
   		}
   	</script>
  </body>
</html>
