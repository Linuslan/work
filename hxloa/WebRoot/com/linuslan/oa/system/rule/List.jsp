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
	<style type="text/css">
		.lightbox{
			position: fixed;
		    top: 0px;
		    left: 0px;
		    height: 100%;
		    width: 100%;
		    z-index: 7;
		    opacity: 0.3;
		    display: block;
		    background-color: rgb(0, 0, 0);
		    display: none;
		}
		.pop,iframe{
			position: absolute;
		    left: 50%;
		    top:0px;
			width: 893px;
		    height: 100%;
		    margin-left: -446.5px;
		    z-index: 2000;
		}
	</style>
	
  </head>
  
  <body>
  	<div>
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增制度" id="addRule"><i class="fa fa-fw fa-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="ruledatagrid"></table>
   					<div id="ruledatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var ruleDataGrid;
   		$(function() {
   			ruleDataGrid = $("#ruledatagrid").jqGrid({
                url: getRoot() + "sys/rule/queryPage.action",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "制度名", name: "title", width: 200, align: "left"
                }, {
                	label: "排序值", name: "orderNo", width: 150
                }, {
                	label: "备注", name: "memo", width: 300
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		filePath = encodeURI(rowObject.filePath);
   						var buttons = createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewRule("+rowObject.id+", \""+filePath+"\")")
   						//buttons = buttons + createBtn("修改", "btn-success btn-xs", "fa-pencil", "editRule("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delRule("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20,
                pager: "#ruledatagridpager"
            });
			$("#addRule").click(function() {
				BootstrapDialog.show({
				    title: "新增制度",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/rule/Add.jsp"),
				    draggable: true,
				    width: "60%",
				    autodestroy: true,
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
				    			/*for ( instance in CKEDITOR.instances ) {
				    				CKEDITOR.instances[instance].updateElement();
				    			}*/
				    			var $form = dialog.getModalBody().find("form");
				    			/*$.ajax({
				    				url: getRoot() + "sys/rule/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						ruleDataGrid.jqGrid().trigger("reloadGrid");
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
				    			});*/
				    			dialog.enableButtons(false);
				    			dialog.setClosable(false);
				    			$button.spin();
				    			upload($form, function(response) {
				    				$form.find("input.checkinContent").remove();
				    				try {
				    					var json = eval("("+response+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						ruleDataGrid.jqGrid().trigger("reloadGrid");
				    					} else {
				    						BootstrapDialog.danger(json.msg);
				    						dialog.enableButtons(true);
					    					dialog.setClosable(true);
					    					$button.stopSpin();
				    					}
				    				} catch(ex) {
				    					BootstrapDialog.danger(response);
			    						dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    					$button.stopSpin();
				    				}
				    				
				    			});
				    		} catch(e) {
				    			dialog.enableButtons(true);
			    				dialog.setClosable(true);
			    				$button.stopSpin();
				    			BootstrapDialog.danger("系统异常，请联系管理员，"+e);
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
   		
   		function viewRule(id, filePath) {
   			//window.open(getRoot() + "sys/rule/queryById.action?returnType=viewer&rule.id="+id);
   			showPdf(true);
   			$("#pdfContainer").attr("src", getRoot() + "resources/office/web/viewer.html?name="+filePath);
   			/*BootstrapDialog.show({
			    title: "查看制度",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/rule/queryById.action?returnType=viewer&rule.id="+id),
			    draggable: true,
			   	width: "60%",
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [{
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-danger",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });*/
   		}
   		
   		function editRule(id) {
   			BootstrapDialog.show({
			    title: "编辑制度",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/rule/queryById.action?returnType=edit&rule.id="+id),
			    draggable: true,
			   	width: "60%",
			    autodestroy: true,
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
			    			var $form = dialog.getModalBody().find("form");
			    			/*for ( instance in CKEDITOR.instances ) {
			    				CKEDITOR.instances[instance].updateElement();
			    			}*/
			    			/*$.ajax({
			    				url: getRoot() + "sys/rule/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						ruleDataGrid.jqGrid().trigger("reloadGrid");
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
			    			});*/
			    			$button.spin();
		                    dialog.setClosable(false);
		                    dialog.enableButtons(false);
			    			upload($form, function(response) {
			    				$form.find("input.checkinContent").remove();
			    				try {
			    					var json = eval("("+response+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						ruleDataGrid.jqGrid().trigger("reloadGrid");
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    						dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    					$button.stopSpin();
			    					}
			    				} catch(ex) {
			    					BootstrapDialog.danger(response);
		    						dialog.enableButtons(true);
			    					dialog.setClosable(true);
			    					$button.stopSpin();
			    				}
			    			});
			    		} catch(e) {
			    			dialog.enableButtons(true);
			    			dialog.setClosable(true);
			    			$button.stopSpin();
			    			BootstrapDialog.danger("系统异常，请联系管理员，"+e);
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
   		
   		function delRule(id) {
   			BootstrapDialog.confirm({
   				title: "温馨提示",
   				message: "您确定删除吗？",
   				type: BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-primary", // <-- If you didn't specify it, dialog type will be used,
   				callback: function(y) {
	   				if(y) {
	   					$.ajax({
	   						url: getRoot() + "sys/rule/del.action",
		    				data: "rule.id="+id,
		    				type: "POST",
		    				success: function(data) {
		    					var json = eval("("+data+")");
		    					if(json.success) {
		    						BootstrapDialog.success(json.msg);
		    						ruleDataGrid.jqGrid().trigger("reloadGrid");
		    					} else {
		    						BootstrapDialog.danger(json.msg);
		    					}
		    				},
		    				error: function() {
		    					BootstrapDialog.danger("系统异常，请联系管理员！");
		    				}
	   					});
	   				}
	   			}
   			});
   		}
   	</script>
  </body>
</html>
