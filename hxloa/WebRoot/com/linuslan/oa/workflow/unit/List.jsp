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
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增单位" id="addUnit"><i class="fa fa-fw fa-user-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="unitdatagrid"></table>
   					<div id="unitdatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var unitDataGrid;
   		$(function() {
   			unitDataGrid = $("#unitdatagrid").jqGrid({
                url: getRoot() + "workflow/unit/queryPage.action",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "单位名称", name: "name", width: 200
                }, {
                	label: "联系电话", name: "telephone", width: 200
                }, {
                	label: "排序值", name: "orderNo", width: 150
                }, {
                	label: "地址", name: "address", width: 300
                }, {
                	label: "备注", name: "remark", width: 300
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
   						var buttons = createBtn("修改", "btn-success btn-xs", "fa-pencil", "editUnit("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delUnit("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 10,
                pager: "#unitdatagridpager"
            });
			$("#addUnit").click(function() {
				BootstrapDialog.show({
				    title: "新增单位",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/workflow/unit/Add.jsp"),
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
				    				url: getRoot() + "workflow/unit/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						unitDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function editUnit(id) {
   			BootstrapDialog.show({
			    title: "编辑单位",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/unit/queryById.action?returnType=edit&unit.id="+id),
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
			    				url: getRoot() + "workflow/unit/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						unitDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function delUnit(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "workflow/unit/del.action",
	    				data: "unit.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						unitDataGrid.jqGrid().trigger("reloadGrid");
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
