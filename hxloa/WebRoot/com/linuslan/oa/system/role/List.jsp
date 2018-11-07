<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    
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
  	<div>
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<form id="search_form_role_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">角色名称：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.name" type="text" class="form-control">
							</div>
							<div class="col-md-3 col-sm-6">
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchRole"><i class="fa fa-fw fa-search"></i>查询</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增角色" id="addRole"><i class="fa fa-fw fa-user-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="roledatagrid"></table>
   					<div id="roledatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var roleDataGrid;
   		$(function() {
   			roleDataGrid = $("#roledatagrid").jqGrid({
                url: getRoot() + "sys/role/queryPage.action",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "角色名", name: "name", key: true, width: 200
                }, {
                	label: "备注", name: "loginName", width: 150
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
   						var buttons = createBtn("修改", "btn-success btn-xs", "fa-pencil", "editRole("+rowObject.id+")");
   						buttons = buttons + createBtn("授权", "btn-warning btn-xs", "fa-unlock-alt", "authorize("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delRole("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "600px",
                //width: "100%",
                rowNum: 20,
                pager: "#roledatagridpager"
            });
            
            $("#searchRole").click(function() {
   				$("#roledatagrid").setGridParam({
   					postData:parsePostData("search_form_role_list")
   				}).trigger("reloadGrid");
   			});
            
			$("#addRole").click(function() {
				BootstrapDialog.show({
				    title: "新增角色",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/role/Add.jsp"),
				    draggable: true,
				    size: BootstrapDialog.SIZE_WIDE,
				    autodestroy: false,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	
				    },
				    buttons: [{
				    	label: "保存",
				    	icon: "fa fa-fw fa-save",
				    	cssClass: "btn-success",
				    	action: function(dialog) {
				    		var $button = this;
				    		try {
				    			var form = dialog.getModalBody().find("form");
				    			$.ajax({
				    				url: getRoot() + "sys/role/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						roleDataGrid.jqGrid().trigger("reloadGrid");
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
						    			$button.spin();
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
			    				$button.stopSpin();
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
   		
   		function authorize(id) {
   			var authorizetreegrid;
   			BootstrapDialog.show({
			    title: "角色授权",
			    width: "90%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/role/Authorize.jsp?id="+id),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	authorizetreegrid = $("#authorizetreegrid").treegrid({
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
					});
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		
		    			var $button = this;
			    		try {
			    			var $pEl = $("#roleAuthorize");
			    			var roleId = $pEl.find("#roleId").val();
							if(authorizetreegrid && roleId && ""!=$.trim(roleId)) {
								var menuIds = authorizetreegrid.getCheckedValues();
								if(!menuIds || ""==$.trim(menuIds)) {
									BootstrapDialog.danger("请至少选择一个菜单资源！");
									return false;
								}
								var buttons = $pEl.find(".authorize_button_table").find("input:checked");
								var buttonArray = new Array();
								for(var i = 0; i < buttons.length; i ++) {
					    			var button = buttons[i];
					    			buttonArray.push(button.value);
					    		}
					    		var buttonIds = buttonArray.join(",");
					    		$.ajax({
					    			url: getRoot() + "sys/role/authorize.action",
					    			type: "POST",
									data: {
										"role.id": roleId,
										"menuIds": menuIds,
										"buttonIds": buttonIds
									},
									success: function(data) {
										var json = eval("("+data+")");
										if(true == json.success) {
											BootstrapDialog.success(json.msg);
											dialog.close();
										} else {
											$("button").removeClass("disabled");
											BootstrapDialog.danger(json.msg);
										}
									},
									error: function() {
										$("button").removeClass("disabled");
										BootstrapDialog.danger("系统异常，请联系管理员！");
									},
				    				beforeSend: function() {
				    					dialog.enableButtons(false);
						    			dialog.setClosable(false);
						    			$button.spin();
				    				},
				    				complete: function() {
				    					dialog.setClosable(true);
						    			$button.stopSpin();
						    			dialog.enableButtons(true);
				    				}
					    		});
							}
			    		} catch(e) {
			    			alert(e);
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
		    				$button.stopSpin();
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
   		
   		function editRole(id) {
   			BootstrapDialog.show({
			    title: "编辑角色",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/role/queryById.action?returnType=edit&role.id="+id),
			    draggable: true,
			    size: BootstrapDialog.SIZE_WIDE,
			    autodestroy: false,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		var $button = this; // 'this' here is a jQuery object that wrapping the <button> DOM element.
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			$.ajax({
			    				url: getRoot() + "sys/role/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						roleDataGrid.jqGrid().trigger("reloadGrid");
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				},
			    				beforeSend: function() {
				                    $button.spin();
				                    dialog.setClosable(false);
				                    dialog.enableButtons(false);
			    				},
			    				complete: function() {
					    			dialog.setClosable(true);
					    			$button.stopSpin();
					    			dialog.enableButtons(true);
			    				}
			    			});
			    		} catch(e) {
			    			dialog.enableButtons(true);
			    			dialog.setClosable(true);
			    			$button.stopSpin();
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
   		
   		function delRole(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/role/del.action",
	    				data: "role.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						roleDataGrid.jqGrid().trigger("reloadGrid");
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
