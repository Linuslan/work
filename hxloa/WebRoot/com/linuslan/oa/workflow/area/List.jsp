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
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增区域" id="addArea"><i class="fa fa-fw fa-user-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="areadatagrid"></table>
   					<div id="areadatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var areaDataGrid;
   		$(function() {
   			areaDataGrid = $("#areadatagrid").jqGrid({
                url: getRoot() + "workflow/area/queryPage.action",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "区域名", name: "name", width: 200
                }, {
                	label: "排序值", name: "orderNo", width: 150
                }, {
                	label: "备注", name: "memo", width: 300
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
   						var buttons = createBtn("修改", "btn-success btn-xs", "fa-pencil", "editArea("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delArea("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 10,
                pager: "#areadatagridpager"
            });
			$("#addArea").click(function() {
				BootstrapDialog.show({
				    title: "新增区域",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/workflow/area/Add.jsp"),
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
				    				url: getRoot() + "workflow/area/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						areaDataGrid.jqGrid().trigger("reloadGrid");
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
			    title: "区域授权",
			    width: "90%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/workflow/area/Authorize.jsp?id="+id),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	authorizetreegrid = $("#authorizetreegrid").treegrid({
						url: getRoot() + "workflow/menu/queryAuthorizeTree.action?area.id="+$("#areaAuthorize #areaId").val(),
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
			    			var $pEl = $("#areaAuthorize");
			    			var areaId = $pEl.find("#areaId").val();
							if(authorizetreegrid && areaId && ""!=$.trim(areaId)) {
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
					    			url: getRoot() + "workflow/area/authorize.action",
					    			type: "POST",
									data: {
										"area.id": areaId,
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
   		
   		function editArea(id) {
   			BootstrapDialog.show({
			    title: "编辑区域",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/area/queryById.action?returnType=edit&area.id="+id),
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
			    				url: getRoot() + "workflow/area/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						areaDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function delArea(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "workflow/area/del.action",
	    				data: "area.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						areaDataGrid.jqGrid().trigger("reloadGrid");
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
