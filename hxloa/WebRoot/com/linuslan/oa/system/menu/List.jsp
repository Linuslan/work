<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>菜单管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body>
    <div id="menulist_tab">
    	<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<div class="box-body">
   					<div class="nav-tabs-top-border">
			   			<ul class="nav nav-tabs">
			   				<li class="active">
			   					<a href="#menu_menulist" data-toggle="tab">菜单管理</a>
			   				</li>
			   				<li>
			   					<a href="#menu_buttonlist" data-toggle="tab">按钮</a>
			   				</li>
			   				<li>
			   					<a href="#menu_menuindexlist" data-toggle="tab">首页索引</a>
			   				</li>
			   			</ul>
			   			<div class="tab-content">
			   				<div class="active tab-pane" id="menu_menulist">
			   					<div class="box box-solid" style="height: 99%;">
					   				<div class="box-body no-padding" style="height: 90%; overflow-y: auto;">
					   					<div class="toolbar with-border">
						   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增菜单" id="addMenu"><i class="fa fa-fw fa-plus"></i>新增</button>
						   				</div>
						   				<div id="treegrid"></div>
					   				</div>
					   			</div>
			   				</div>
			   				<div class="tab-pane" id="menu_buttonlist">
			   					<div class="box box-solid">
					   				<div class="box-body no-padding" style="height: 90%; overflow-y: auto;">
					   					<div class="toolbar with-border">
						   					<button class="btn btn-primary btn-sm" data-toggle="tooltip" title="新增按钮" id="addButton"><i class="fa fa-fw fa-plus"></i>新增</button>
						   				</div>
						   				<table id="buttongrid"></table>
						   				<div id="buttondatagridpager"></div>
					   				</div>
					   			</div>
			   				</div>
			   				<div class="tab-pane" id="menu_menuindexlist">
			   					<div class="box box-solid">
			   						<div>
				   						<div class="toolbar with-border">
						   					<button class="btn btn-primary btn-sm" data-toggle="tooltip" title="新增索引" id="addMenuIndex"><i class="fa fa-fw fa-plus"></i>新增</button>
						   				</div>
						   				<table id="menuIndexDatagrid"></table>
						   				<div id="menuIndexDatagridPager"></div>
						   			</div>
			   					</div>
			   				</div>
			   			</div>
			   		</div>
			   	</div>
			</div>
		</div>
	</div>
   	
   	<script type="text/javascript">
   		var menuList;
   		var buttonList;
   		var menuIndexList;
   		$(function() {
   			$("#menulist_tab a[data-toggle='tab']").on("shown.bs.tab", function (e) {
   				var href = $(this).attr("href");
   				var id = href.substring(1);
   				$("#"+id).find("table").setGridWidth($("#"+id).width()*0.99);
   			});
   			buttonList = $("#buttongrid").jqGrid({
                url: getRoot() + "sys/button/queryPageByMenuId.action",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "主键", name: "id", hidden: true, key: true
                }, {
                	label: "按钮名称", name: "name", width: 200
                }, {
                	label: "按钮值", name: "value", width: 150
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
   						var buttons = createBtn("修改", "btn-success btn-xs", "fa-pencil", "editButton("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delButton("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 4,
                pager: "#buttondatagridpager"
            });
   			
   			menuIndexList = $("#menuIndexDatagrid").jqGrid({
                url: getRoot() + "sys/menuIndex/queryPageByMenuId.action",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "主键", name: "id", hidden: true, key: true
                }, {
                	label: "索引名称", name: "name", width: 200
                }, {
                	label: "索引值", name: "value", width: 150
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
   						var buttons = createBtn("修改", "btn-success btn-xs", "fa-pencil", "editMenuIndex("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delMenuIndex("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 4,
                pager: "#menuIndexDatagridPager"
            });
   			
   			
   			menuList = $("#treegrid").treegrid({
   				url: getRoot() + "sys/menu/queryTree.action",
   				pidField: "pid",
   				sizeUnit: "%",
   				loadOnExpand: false,
   				loadParams: {
   					"id": "menu.id"
   				},
   				async: true,
   				onRowClick: function(data) {
   					var menuId = data.id;
   					if(menuId && "" != $.trim(menuId)) {
   						$("#buttongrid").jqGrid("setGridParam",{ 
				            postData:{"menu.id": menuId} //发送数据 
				        }).trigger("reloadGrid");
   						
   						$("#menuIndexDatagrid").jqGrid("setGridParam", {
   							postData: {"menu.id": menuId}
   						}).trigger("reloadGrid");
   					}
   				},
   				columns: [{
   					field: "菜单名称", dataIndex: "text", width: 30
   				}, {
   					field: "菜单值", dataIndex: "value", width: 10, align: "center"
   				}, {
   					field: "URL", dataIndex: "url", width: 20, align: "center"
   				}, {
   					field: "图标", dataIndex: "icon", width: 20, align: "center"
   				}, {
   					field: "排序值", dataIndex: "orderNo", align: "center", width: 10
   				}, {
   					field: "操作", align: "center", width: 10,
   					formatter: function(data) {
   						var buttons = "<button class='btn btn-success btn-xs' data-toggle='tooltip' title='修改' onclick='editMenu("+data.id+")'><i class='fa fa-pencil'></i></button>";
   						buttons = buttons + "&nbsp;&nbsp;<button class='btn btn-danger btn-xs' data-toggle='tooltip' title='删除' onclick='delMenu("+data.id+")'><i class='fa fa-trash-o'></i></button>";
   						return buttons;
   					}
   				}]
   			});
   			
   			$("#addMenu").click(function() {
   				BootstrapDialog.show({
				    title: "新增菜单",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/menu/Add.jsp"),
				    draggable: true,
				    size: BootstrapDialog.SIZE_WIDE,
				    autodestroy: true,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	//初始化上级组字段的选择项
				    	dialogRef.getModalBody().find(".parentCombotree").combotree({
   							url: getRoot() + "sys/menu/queryTree.action",
							async: true,
							singleSelect: true,
							loadOnExpand: false,
							loadParams: {
								"id": "menu.id"
							},
							idField: "id",
							textField: "text",
							name: "menu.pid",
							pidField: "pid"
   						});
				    },
				    buttons: [{
				    	label: "保存",
				    	icon: "fa fa-fw fa-save",
				    	cssClass: "btn-success",
				    	action: function(dialog) {
				    		dialog.enableButtons(false);
			    			dialog.setClosable(false);
				    		try {
				    			var form = dialog.getModalBody().find("form");
				    			$.ajax({
				    				url: getRoot() + "sys/menu/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						menuList.refresh();
				    					} else {
				    						dialog.enableButtons(false);
			    							dialog.setClosable(false);
				    						BootstrapDialog.danger(json.msg);
				    					}
				    				},
				    				error: function() {
				    					dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    					BootstrapDialog.danger("系统异常，请联系管理员！");
				    				}
				    			});
				    		} catch(e) {
				    			dialog.enableButtons(false);
			    				dialog.setClosable(false);
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
   			
   			$("#addButton").click(function() {
   				BootstrapDialog.show({
				    title: "新增按钮",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/button/Add.jsp"),
				    draggable: true,
				    size: BootstrapDialog.SIZE_WIDE,
				    autodestroy: true,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	//初始化上级组字段的选择项
				    	dialogRef.getModalBody().find(".parentCombotree").combotree({
   							url: getRoot() + "sys/menu/queryTree.action",
							async: true,
							singleSelect: true,
							loadOnExpand: false,
							loadParams: {
								"id": "menu.id"
							},
							idField: "id",
							textField: "text",
							name: "button.menuId",
							pidField: "pid"
   						});
				    },
				    buttons: [{
				    	label: "保存",
				    	icon: "fa fa-fw fa-save",
				    	cssClass: "btn-success",
				    	action: function(dialog) {
				    		dialog.enableButtons(false);
			    			dialog.setClosable(false);
				    		try {
				    			var form = dialog.getModalBody().find("form");
				    			$.ajax({
				    				url: getRoot() + "sys/button/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						$("#buttongrid").jqGrid().trigger("reloadGrid");
				    					} else {
				    						dialog.enableButtons(true);
			    							dialog.setClosable(true);
				    						BootstrapDialog.danger(json.msg);
				    					}
				    				},
				    				error: function() {
				    					dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    					BootstrapDialog.danger("系统异常，请联系管理员！");
				    				}
				    			});
				    		} catch(e) {
				    			dialog.enableButtons(false);
			    				dialog.setClosable(false);
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
   			
   			$("#addMenuIndex").click(function() {
   				var menuId = menuList.getSelected().id;
   				BootstrapDialog.show({
				    title: "新增索引",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "sys/menuIndex/queryById.action?returnType=add&menu.id="+menuId),
				    draggable: true,
				    size: BootstrapDialog.SIZE_WIDE,
				    autodestroy: true,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	//初始化上级组字段的选择项
				    	dialogRef.getModalBody().find(".parentCombotree").combotree({
   							url: getRoot() + "sys/menu/queryTree.action",
							async: true,
							singleSelect: true,
							loadOnExpand: false,
							loadParams: {
								"id": "menu.id"
							},
							idField: "id",
							textField: "text",
							name: "menuIndex.menuId",
							pidField: "pid"
   						});
				    },
				    buttons: [{
				    	label: "保存",
				    	icon: "fa fa-fw fa-save",
				    	cssClass: "btn-success",
				    	action: function(dialog) {
				    		dialog.enableButtons(false);
			    			dialog.setClosable(false);
				    		try {
				    			var form = dialog.getModalBody().find("form");
				    			$.ajax({
				    				url: getRoot() + "sys/menuIndex/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						$("#menuIndexDatagrid").jqGrid().trigger("reloadGrid");
				    					} else {
				    						dialog.enableButtons(true);
			    							dialog.setClosable(true);
				    						BootstrapDialog.danger(json.msg);
				    					}
				    				},
				    				error: function() {
				    					dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    					BootstrapDialog.danger("系统异常，请联系管理员！");
				    				}
				    			});
				    		} catch(e) {
				    			dialog.enableButtons(false);
			    				dialog.setClosable(false);
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
   		
   		function editMenu(id) {
   			BootstrapDialog.show({
			    title: "编辑菜单",
			    type: BootstrapDialog.TYPE_PRIMARY,
			    message: $("<div></div>").load(getRoot() + "sys/menu/queryById.action?returnType=edit&menu.id="+id),
			    draggable: true,
			    size: BootstrapDialog.SIZE_WIDE,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	//初始化父部门树，只有在这里等弹出框的内容都加载完之后再加载树才能够获取到宽度
			    	//初始化上级组字段的选择项
			    	dialogRef.getModalBody().find(".parentCombotree").combotree({
	 					url: getRoot() + "sys/menu/queryTree.action",
						async: true,
						singleSelect: true,
						loadOnExpand: false,
						loadParams: {
							"id": "menu.id"
						},
						idField: "id",
						textField: "text",
						name: "menu.pid",
						pidField: "pid"
	 				});
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		dialog.enableButtons(false);
			    		dialog.setClosable(false);
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			$.ajax({
			    				url: getRoot() + "sys/menu/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						menuList.refresh();
			    					} else {
			    						dialog.enableButtons(true);
			    						dialog.setClosable(true);
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					dialog.enableButtons(true);
			    					dialog.setClosable(true);
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
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
   		
   		function delMenu(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/menu/del.action",
	    				data: "menu.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						menuList.refresh();
	    					} else {
	    						BootstrapDialog.danger(json.msg);
	    					}
	    				},
	    				error: function() {
	    					BootstrapDialog.danger("系统异常，请联系管理员！");
	    				}
   					});
   				}
   			});
   		}
   		
   		
   		function editButton(id) {
   			BootstrapDialog.show({
			    title: "编辑按钮",
			    type: BootstrapDialog.TYPE_PRIMARY,
			    message: $("<div></div>").load(getRoot() + "sys/button/queryById.action?returnType=edit&button.id="+id),
			    draggable: true,
			    size: BootstrapDialog.SIZE_WIDE,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	//初始化父部门树，只有在这里等弹出框的内容都加载完之后再加载树才能够获取到宽度
			    	//初始化上级组字段的选择项
			    	dialogRef.getModalBody().find(".parentCombotree").combotree({
	 					url: getRoot() + "sys/menu/queryTree.action",
						async: true,
						singleSelect: true,
						loadOnExpand: true,
						loadParams: {
							"id": "menu.id"
						},
						idField: "id",
						textField: "text",
						name: "button.menuId",
						pidField: "pid"
	 				});
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		dialog.enableButtons(false);
			    		dialog.setClosable(false);
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			$.ajax({
			    				url: getRoot() + "sys/button/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						$("#buttongrid").jqGrid().trigger("reloadGrid");
			    					} else {
			    						dialog.enableButtons(true);
			    						dialog.setClosable(true);
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					dialog.enableButtons(true);
			    					dialog.setClosable(true);
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
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
   		
   		function delButton(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/button/del.action",
	    				data: "button.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						$("#buttongrid").jqGrid().trigger("reloadGrid");
	    					} else {
	    						BootstrapDialog.danger(json.msg);
	    					}
	    				},
	    				error: function() {
	    					BootstrapDialog.danger("系统异常，请联系管理员！");
	    				}
   					});
   				}
   			});
   		}
   		
   		function editMenuIndex(id) {
   			BootstrapDialog.show({
			    title: "编辑索引",
			    type: BootstrapDialog.TYPE_PRIMARY,
			    message: $("<div></div>").load(getRoot() + "sys/menuIndex/queryById.action?returnType=edit&menuIndex.id="+id),
			    draggable: true,
			    size: BootstrapDialog.SIZE_WIDE,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	//初始化父部门树，只有在这里等弹出框的内容都加载完之后再加载树才能够获取到宽度
			    	//初始化上级组字段的选择项
			    	dialogRef.getModalBody().find(".parentCombotree").combotree({
	 					url: getRoot() + "sys/menu/queryTree.action",
						async: true,
						singleSelect: true,
						loadOnExpand: true,
						loadParams: {
							"id": "menu.id"
						},
						idField: "id",
						textField: "text",
						name: "menuIndex.menuId",
						pidField: "pid"
	 				});
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		dialog.enableButtons(false);
			    		dialog.setClosable(false);
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			$.ajax({
			    				url: getRoot() + "sys/menuIndex/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						$("#menuIndexDatagrid").jqGrid().trigger("reloadGrid");
			    					} else {
			    						dialog.enableButtons(true);
			    						dialog.setClosable(true);
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					dialog.enableButtons(true);
			    					dialog.setClosable(true);
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
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
   		
   		function delMenuIndex(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/menuIndex/del.action",
	    				data: "menuIndex.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						$("#menuIndexDatagrid").jqGrid().trigger("reloadGrid");
	    					} else {
	    						BootstrapDialog.danger(json.msg);
	    					}
	    				},
	    				error: function() {
	    					BootstrapDialog.danger("系统异常，请联系管理员！");
	    				}
   					});
   				}
   			});
   		}
   	</script>
  </body>
</html>
