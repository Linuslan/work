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
  	<div id="capitalList">
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增合同" id="addCapital"><i class="fa fa-fw fa-user-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="capitaldatagrid"></table>
   					<div id="capitaldatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var capitalDataGrid;
   		$(function() {
   			capitalDataGrid = $("#capitaldatagrid").jqGrid({
                url: getRoot() + "sys/capital/queryPage.action",
                mtype: "POST",
                shrinkToFit: false,
                rownumbers: true,
                height: "600px",
				styleUI : "Bootstrap",
                datatype: "json",
                gridComplete: function() {
                	$("#capitaldatagrid").setGridWidth($("#capitalList").width()*0.99);
                	$("#capitaldatagrid").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
                	$("#capitaldatagrid").jqGrid("setFrozenColumns");
                	setTimeout("hackHeight(\"#capitaldatagrid\")", 0);
                },
                colModel: [{
                	label: "固资类别", name: "className", width: 200, frozen: true
                }, {
                	label: "编号", name: "serial", width: 150, frozen: true
                }, {
                	label: "名称", name: "name", width: 150, frozen: true
                }, {
                	label: "型号", name: "model", width: 150
                }, {
                	label: "厂家", name: "shopName", width: 150
                }, {
                	label: "存放地", name: "address", width: 200
                }, {
                	label: "归属部门", name: "department", width: 150
                }, {
                	label: "借用/领用时间", name: "borrowDate", width: 100
                }, {
                	label: "借用部门", name: "borrowDepartment", width: 150
                }, {
                	label: "使用方", name: "borrowUser", width: 150
                }, {
                	label: "计量单位", name: "unit", width: 150
                }, {
                	label: "购置时间", name: "buyDate", width: 150
                }, {
                	label: "购置价格", name: "buyMoney", width: 150
                }, {
                	label: "折旧月数", name: "depreciationYear", width: 150
                }, {
                	label: "月折旧额", name: "depreciationMoney", width: 150
                }, {
                	label: "目前状况", name: "depreciationState", width: 150
                }, {
                	label: "至今折旧额", name: "depreciation", width: 150
                }, {
                	label: "净额", name: "netamount", width: 150
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
   						var buttons = createBtn("修改", "btn-success btn-xs", "fa-pencil", "editCapital("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delCapital("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                rowNum: 20,
                pager: "#capitaldatagridpager"
            });
			$("#addCapital").click(function() {
				BootstrapDialog.show({
				    title: "新增固资",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/capital/Add.jsp"),
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
				    			var form = dialog.getModalBody().find("form");
				    			$.ajax({
				    				url: getRoot() + "sys/capital/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						capitalDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function editCapital(id) {
   			BootstrapDialog.show({
			    title: "编辑固资",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/capital/initContent.action?returnType=edit&capital.id="+id),
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
			    			var form = dialog.getModalBody().find("form");
			    			$.ajax({
			    				url: getRoot() + "sys/capital/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						capitalDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function delCapital(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/capital/del.action",
	    				data: "capital.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						capitalDataGrid.jqGrid().trigger("reloadGrid");
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
