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
   				<form id="search_form_notice_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">标题：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.title" type="text" class="form-control">
							</div>
							<div class="col-md-3 col-sm-3">
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchNotice"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="reset" class="btn btn-info btn-sm margin-right5" data-toggle="tooltip" title="重置"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增公告" id="addNotice"><i class="fa fa-fw fa-user-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="noticedatagrid"></table>
   					<div id="noticedatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var noticeDataGrid;
   		$(function() {
   			noticeDataGrid = $("#noticedatagrid").jqGrid({
                url: getRoot() + "sys/notice/queryPage.action",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "标题", name: "title", width: 200
                }, {
                	label: "是否发送", name: "isSend", width: 150,
                	formatter: function(cellValue, options, rowObject) {
                		if(rowObject.isSend == 0) {
                			return "<font color='red'>未发送</font>";
                		} else {
                			return "已发送";
                		}
                	}
                }, {
                	label: "发送时间", name: "sendDate", width: 150
                }, {
                	label: "发送人", name: "senderName", width: 150
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewNotice("+rowObject.id+")");
                		if(rowObject.isSend == 0) {
                			buttons = buttons + createBtn("修改", "btn-success btn-xs", "fa-pencil", "editNotice("+rowObject.id+")");
       						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delNotice("+rowObject.id+")");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#noticedatagridpager"
            });
            
            $("#searchNotice").click(function() {
   				$("#noticedatagrid").setGridParam({
   					postData:parsePostData("search_form_notice_list")
   				}).trigger("reloadGrid");
   			});
            
			$("#addNotice").click(function() {
				BootstrapDialog.show({
				    title: "新增公告",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "sys/notice/queryById.action?returnType=add"),
				    draggable: true,
				    width: getA4Width(),
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
				    			for ( instance in CKEDITOR.instances ) {
				    				CKEDITOR.instances[instance].updateElement();
				    			}
				    			var form = dialog.getModalBody().find("form");
				    			alert(form.serialize());
				    			$.ajax({
				    				url: getRoot() + "sys/notice/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						noticeDataGrid.jqGrid().trigger("reloadGrid");
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
				    			BootstrapDialog.danger("系统异常，请联系管理员！"+e);
				    		}
				    	}
				    }, {
				    	label: "发送",
				    	icon: "fa fa-fw fa-save",
				    	cssClass: "btn-share",
				    	action: function(dialog) {
				    		var $button = this; // 'this' here is a jQuery object that wrapping the <button> DOM element.
				    		try {
				    			BootstrapDialog.confirm("您确定发送吗？", function(y) {
				       				if(y) {
				       					for ( instance in CKEDITOR.instances ) {
						    				CKEDITOR.instances[instance].updateElement();
						    			}
						    			var form = dialog.getModalBody().find("form");
						    			alert(form.serialize());
						    			$.ajax({
						    				url: getRoot() + "sys/notice/send.action",
						    				data: form.serialize(),
						    				type: "POST",
						    				success: function(data) {
						    					var json = eval("("+data+")");
						    					if(json.success) {
						    						BootstrapDialog.success(json.msg);
						    						dialog.close();
						    						noticeDataGrid.jqGrid().trigger("reloadGrid");
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
				       				}
				       			});
				    		} catch(e) {
				    			dialog.enableButtons(true);
				    			dialog.setClosable(true);
				    			$button.stopSpin();
				    			BootstrapDialog.danger("系统异常，请联系管理员！"+e);
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
   		
   		function editNotice(id) {
   			BootstrapDialog.show({
			    title: "编辑公告",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/notice/queryById.action?returnType=edit&notice.id="+id),
			    draggable: true,
			    width: getA4Width(),
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
			    			for ( instance in CKEDITOR.instances ) {
			    				CKEDITOR.instances[instance].updateElement();
			    			}
			    			var form = dialog.getModalBody().find("form");
			    			//alert(form.serialize());
			    			//return false;
			    			$.ajax({
			    				url: getRoot() + "sys/notice/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						noticeDataGrid.jqGrid().trigger("reloadGrid");
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
			    			BootstrapDialog.danger("系统异常，请联系管理员！"+e);
			    		}
			    	}
			    }, {
			    	label: "发送",
			    	icon: "fa fa-fw fa-share",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		var $button = this; // 'this' here is a jQuery object that wrapping the <button> DOM element.
			    		try {
			    			BootstrapDialog.confirm("您确定发送吗？", function(y) {
			       				if(y) {
			       					for ( instance in CKEDITOR.instances ) {
					    				CKEDITOR.instances[instance].updateElement();
					    			}
					    			var form = dialog.getModalBody().find("form");
					    			alert(form.serialize());
					    			$.ajax({
					    				url: getRoot() + "sys/notice/send.action",
					    				data: form.serialize(),
					    				type: "POST",
					    				success: function(data) {
					    					var json = eval("("+data+")");
					    					if(json.success) {
					    						BootstrapDialog.success(json.msg);
					    						dialog.close();
					    						noticeDataGrid.jqGrid().trigger("reloadGrid");
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
			       				}
			       			});
			    		} catch(e) {
			    			dialog.enableButtons(true);
			    			dialog.setClosable(true);
			    			$button.stopSpin();
			    			BootstrapDialog.danger("系统异常，请联系管理员！"+e);
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
   		
   		function delNotice(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/notice/del.action",
	    				data: "notice.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						noticeDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function viewNotice(id) {
   			BootstrapDialog.show({
			    title: "公告详情",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/notice/queryById.action?returnType=view&notice.id="+id),
			    draggable: true,
			   	width: getA4Width(),
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
	        });
   		}
   	</script>
  </body>
</html>
