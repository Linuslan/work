<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>入库商品列表</title>
    
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
   				<div class="box-header with-border">
   					<h3 class="box-title">商品列表</h3>
   					<div class="box-tools pull-right">
   						<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
	               	</div>
   				</div>
   				<div class="toolbar with-border">
   					<button class="btn btn-info btn-sm" data-toggle="tooltip" title="新增商品" id="addArticle"><i class="fa fa-fw fa-clone"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="checkinArticleDatagrid"></table>
   					<div id="checkinArticleDatagridPager"></div>
   				</div>
   			</div>
   			<div class="box box-solid">
   				<div class="box-header with-border">
   					<h3 class="box-title">规格列表</h3>
   				</div>
   				<div class="box-body">
   					<table id="formatDatagrid"></table>
   					<div id="formatDatagridPager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var checkinArticleGrid;
   		var formatGrid;
   		var checkinArticleDialog;
   		var formatLastSel2;
   		$(function() {
   			checkinArticleGrid = $("#checkinArticleDatagrid").jqGrid({
                url: getRoot() + "workflow/article/queryPage.action",
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "ID", name: "id", hidden: true, key: true
                }, {
                	label: "商品名称", name: "name", width: 200, align: "center"
                }, {
                	label: "编号", name: "serialNo", width: 150, align: "center"
                }, {
                	label: "单位", name: "unit", width: 100, align: "center"
                }, {
                	label: "归属公司", name: "companyName", width: 100, align: "center"
                }, {
                	label: "序号", name: "orderNo", width: 100, align: "center"
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewCheckinArticle("+rowObject.id+", \""+rowObject.allFlowStatus+"\", "+rowObject.status+")");
                		buttons = buttons + createBtn("修改", "btn-info btn-xs", "fa-pencil", "editCheckinArticle("+rowObject.id+")");
                		buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delCheckinArticle("+rowObject.id+")");
   						return buttons;
                	}
                }],
                onSelectRow: function(id){
                	var rowObject = $("#checkinArticleDatagrid").getRowData(id);
                	var checkinArticleId = rowObject.id;
                	if(checkinArticleId) {
                		$("#formatDatagrid").jqGrid("setGridParam",{ 
				            postData:{"article.id": checkinArticleId} //发送数据 
				        }).trigger("reloadGrid");
                	}
			    },
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20,
                pager: "#checkinArticleDatagridPager"
            });
            
            formatGrid = $("#formatDatagrid").jqGrid({
                url: getRoot() + "workflow/article/queryFormatsByArticleId.action",
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "ID", name: "id", hidden: true, key: true
                }, {
                	label: "规格名称", name: "name", width: 200, align: "center"
                }, {
                	label: "价格", name: "price", width: 150, align: "center"
                }, {
                	label: "排序号", name: "orderNo", width: 100, align: "center"
                }, {
                	label: "备注", name: "memo", width: 300, align: "center"
                }/*, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewFormat("+rowObject.id+", \""+rowObject.allFlowStatus+"\", "+rowObject.status+")");
                		buttons = buttons + createBtn("修改", "btn-info btn-xs", "fa-pencil", "editFormat("+rowObject.id+")");
                		buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delFormat("+rowObject.id+")");
   						return buttons;
                	}
                }*/],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20,
                pager: "#formatDatagridPager"
            });
            
   			$("#addArticle").click(function() {
   				checkinArticleDialog = BootstrapDialog.show({
				    title: "新增商品",
				    width: "80%",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "workflow/article/queryById.action?returnType=add"),
				    draggable: true,
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
				    		try {
				    			var $grid = dialog.getModalBody().find("form").find("table");
				    			//将未保存的先保存
				    			jQuery("#formatDatagrid_add").saveRow(formatLastSel2, false, "clientArray");
				    			var contents = getArticleFormats($grid);
				    			if(!contents) {
				    				return false;
				    			}
				    			var form = dialog.getModalBody().find("form");
				    			var $button = this;
				    			$.ajax({
				    				url: getRoot() + "workflow/article/add.action",
				    				data: form.serialize()+"&"+contents.join("&"),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						checkinArticleGrid.jqGrid().trigger("reloadGrid");
				    					} else {
				    						BootstrapDialog.danger(json.msg);
				    					}
				    				},
				    				error: function() {
				    					BootstrapDialog.danger("系统异常，请联系管理员！");
				    					jQuery("#checkinArticleContentDatagrid_add").trigger("reloadGrid");
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
   		
   		function editCheckinArticle(id) {
   			checkinArticleDialog = BootstrapDialog.show({
			    title: "编辑商品",
			    width: "80%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/article/queryById.action?returnType=edit&article.id="+id),
			    draggable: true,
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
			    		try {
			    			var $grid = dialog.getModalBody().find("form").find("table#formatDatagrid_edit");
			    			//将未保存的先保存
			    			jQuery("#formatDatagrid_edit").saveRow(formatLastSel2, false, "clientArray");
			    			var contents = getArticleFormats($grid);
			    			if(!contents) {
			    				return false;
			    			}
			    			var form = dialog.getModalBody().find("form");
			    			var $button = this;
			    			$.ajax({
			    				url: getRoot() + "workflow/article/update.action",
			    				data: form.serialize()+"&"+contents.join("&"),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						checkinArticleGrid.jqGrid().trigger("reloadGrid");
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
			    			alert(e);
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
   		
   		function delCheckinArticle(id) {
   			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定删除吗？",
	            type: BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	                	alert(id);
	   					$.ajax({
	   						url: getRoot() + "workflow/article/del.action",
		    				data: "article.id="+id,
		    				type: "POST",
		    				beforeSend: function() {
		    					//mask($("#userdatagrid").parent());
		    				},
		    				complete: function() {
		    					//unmask($("#userdatagrid").parent());
		    				},
		    				success: function(data) {
		    					var json = eval("("+data+")");
		    					if(json.success) {
		    						BootstrapDialog.success(json.msg);
		    						checkinArticleGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function viewCheckinArticle(id) {
   			var returnType = "view";
   			
   			checkinArticleDialog = BootstrapDialog.show({
			    title: "商品详情",
			    width: "90%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/article/queryById.action?returnType="+returnType+"&article.id="+id),
			    draggable: true,
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
   		
   		function getArticleFormats($grid) {
   			if(!$grid || 0 >= $grid.length) {
   				//BootstrapDialog.error("获取报销项目异常");
   				return false;
   			}
   			
   			var rows = $grid.getRowData();
   			var contents = new Array();
   			var param = "formats[#index#].#prop#=#value#";
   			if(!rows && 0 >= rows.length) {
   				//BootstrapDialog.error("请至少添加一项项目");
   				return false;
   			}
   			for(var i = 0; i < rows.length; i ++) {
   				var content = rows[i];
   				for(var name in content) {
   					if(!name || ""==$.trim(name) || "operationCell" == name) {
   						continue;
   					}
   					var value = content[name];
   					if("id" == name) {
   						var re = /^[0-9]*$/;
   						if(!re.test(value)) {
   							value = "";
   						}
   					}
   					//var value = content[name];
   					contents.push(param.replace("#index#", i).replace("#prop#", name).replace("#value#", value));
   				}
   			}
   			if(0 >= contents.length) {
   				//BootstrapDialog.danger("获取到的项目为空");
   				return false;
   			}
   			return contents;
   		}
   	</script>
  </body>
</html>
