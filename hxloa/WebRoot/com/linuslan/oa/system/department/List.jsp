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
    <div id="departmentList_tab">
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<div class="box-body">
   					<div class="nav-tabs-top-border">
			   			<ul class="nav nav-tabs">
			   				<li class="active">
			   					<a href="#department_departmentlist" data-toggle="tab">部门管理</a>
			   				</li>
			   				<li>
			   					<a href="#department_postlist" data-toggle="tab">岗位管理</a>
			   				</li>
			   				<li>
			   					<a href="#department_reimburseClassList" data-toggle="tab">报销类别管理</a>
			   				</li>
			   			</ul>
			   			<div class="tab-content">
			   				<div class="active tab-pane" id="department_departmentlist">
			   					<div class="box box-solid" style="height: 99%;">
					   				<div class="box-body no-padding" style="height: 90%; overflow-y: auto;">
					   					<div class="toolbar with-border">
						   					<button class="btn btn-info btn-sm" data-toggle="tooltip" title="新增部门" id="addDepartment"><i class="fa fa-fw fa-plus"></i>新增</button>
						   				</div>
						   				<div id="departmenttreegrid" style="height: 100; overflow-y: auto;"></div>
					   				</div>
					   			</div>
					   		</div>
					   		<div class="tab-pane" id="department_postlist">
					   			<div class="box box-solid" style="height: 99%;">
					   				<div class="box-body no-padding" style="height: 90%; overflow-y: auto;">
					   					<div class="toolbar with-border">
						   					<button class="btn btn-info btn-sm" data-toggle="tooltip" title="新增岗位" id="addPost"><i class="fa fa-fw fa-plus"></i>新增</button>
						   				</div>
						   				<table id="postgrid"></table>
						   				<div id="postdatagridpager"></div>
					   				</div>
					   			</div>
					   		</div>
					   		<div class="tab-pane" id="department_reimburseClassList">
					   			<div class="box box-solid" style="height: 99%;">
					   				<div class="box-body no-padding" style="height: 90%; overflow-y: auto;">
					   					<div class="toolbar with-border">
						   					<button class="btn btn-info btn-sm" data-toggle="tooltip" title="新增报销类别" id="addReimburseClass_department"><i class="fa fa-fw fa-plus"></i>新增</button>
						   				</div>
						   				<table id="reimburseClassDatagrid_department"></table>
						   				<div id="reimburseClassDatagrid_departmentPager"></div>
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
   		var departmentList;
   		var postList;
   		var reimburseClassList;
   		$(function() {
   			/*$("#combotree").combotree({
   				url: getRoot() + "department/queryDepartmentList.action",
   				async: true,
   				singleSelect: false,
   				loadOnExpand: true,
   				loadParams: {
   					"id": "department.id"
   				},
   				idField: "id",
   				textField: "text"
   			});*/
   			
   			$("#departmentList_tab a[data-toggle='tab']").on("shown.bs.tab", function (e) {
   				var href = $(this).attr("href");
   				var id = href.substring(1);
   				$("#"+id).find("table").setGridWidth($("#"+id).width()*0.99);
   			});
   			
   			postList = $("#postgrid").jqGrid({
                url: getRoot() + "sys/post/queryPageByDepartmentId.action",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "岗位名称", name: "name", key: true, width: 200
                }, {
                	label: "排序值", name: "orderNo", width: 150
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
   						var buttons = createBtn("修改", "btn-success btn-xs", "fa-pencil", "editPost("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delPost("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#postdatagridpager"
            });
            
            reimburseClassList = $("#reimburseClassDatagrid_department").jqGrid({
                url: "",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "报销类别名称", name: "NAME", key: true, width: 200
                }, {
                	label: "排序值", name: "ORDER_NO", width: 150
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
   						var buttons = createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delReimburseClass_department("+rowObject.ID+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#reimburseClassDatagrid_departmentPager"
            });
   			
   			departmentList = $("#departmenttreegrid").treegrid({
   				url: getRoot() + "sys/department/queryTree.action",
   				sizeUnit: "%",
   				loadOnExpand: false,
   				idField: "id",
   				textField: "name",
   				pidField: "pid",
   				loadParams: {
   					"id": "department.id"
   				},
   				async: true,
   				onRowClick: function(data) {
   					var id = data.id;
   					if(id && "" != $.trim(id)) {
   						$("#postgrid").jqGrid("setGridParam",{ 
				            postData:{"department.id": id} //发送数据 
				        }).trigger("reloadGrid");
				        $("#reimburseClassDatagrid_department").jqGrid("setGridParam",{ 
				            postData:{"paramMap.departmentId": id},
				            url: getRoot() + "workflow/reimburseClass/queryPageBySql.action"
				        }).trigger("reloadGrid");
   					}
   				},
   				columns: [{
   					field: "部门名称", dataIndex: "name", width: 30
   				}, {
   					field: "备注", dataIndex: "memo", align: "center", width: 40
   				}, {
   					field: "排序值", dataIndex: "orderNo", align: "center", width: 10
   				}, {
   					field: "操作", align: "center", width: 20,
   					formatter: function(data) {
   						var buttons = createBtn("修改", "btn-success btn-xs", "fa-pencil", "editDepartment("+data.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delDepartment("+data.id+")");
   						return buttons;
   					}
   				}]
   			});
   			
   			$("#addDepartment").click(function() {
   				BootstrapDialog.show({
				    title: "新增部门",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "sys/department/queryById.action?returnType=add"),
				    draggable: true,
				    size: BootstrapDialog.SIZE_WIDE,
				    autodestroy: true,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	
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
				    				url: getRoot() + "sys/department/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						departmentList.refresh();
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
   			
   			$("#addPost").click(function() {
   				BootstrapDialog.show({
				    title: "新增岗位",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/post/Add.jsp"),
				    draggable: true,
				    size: BootstrapDialog.SIZE_WIDE,
				    autodestroy: true,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	//初始化上级组字段的选择项
				    	dialogRef.getModalBody().find(".parentCombotree").combotree({
   							url: getRoot() + "sys/department/queryTree.action",
							async: true,
							singleSelect: true,
							loadOnExpand: false,
							loadParams: {
								"id": "department.id"
							},
							idField: "id",
							textField: "name",
							name: "post.departmentId",
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
				    				url: getRoot() + "sys/post/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						$("#postgrid").jqGrid().trigger("reloadGrid");
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
   			
   			$("#addReimburseClass_department").click(function() {
   				var departmentId = departmentList.getSelected().id;
   				BootstrapDialog.show({
				    title: "新增岗位",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "sys/department/queryById.action?department.id="+departmentId+"&returnType=addClass"),
				    draggable: true,
				    size: BootstrapDialog.SIZE_WIDE,
				    autodestroy: true,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	//初始化上级组字段的选择项
				    	dialogRef.getModalBody().find(".parentCombotree").combotree({
   							url: getRoot() + "sys/department/queryTree.action",
							async: true,
							singleSelect: true,
							loadOnExpand: false,
							loadParams: {
								"id": "department.id"
							},
							idField: "id",
							textField: "name",
							name: "department.id",
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
				    				url: getRoot() + "sys/department/addClass.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						$("#reimburseClassDatagrid_department").jqGrid().trigger("reloadGrid");
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
   		
   		function editDepartment(id) {
   			BootstrapDialog.show({
			    title: "编辑部门",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/department/queryById.action?returnType=edit&department.id="+id),
			    draggable: true,
			    size: BootstrapDialog.SIZE_WIDE,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	//初始化父部门树，只有在这里等弹出框的内容都加载完之后再加载树才能够获取到宽度
			    	
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
			    				url: getRoot() + "sys/department/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						departmentList.refresh();
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
   		
   		function delDepartment(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/department/del.action",
	    				data: "department.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						departmentList.refresh();
	    					} else {
	    						BootstrapDialog.danger(json.msg);
	    					}
	    				}
   					});
   				}
   			});
   		}
   		
   		function editPost(id) {
   			BootstrapDialog.show({
			    title: "编辑岗位",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/post/queryById.action?returnType=edit&post.id="+id),
			    draggable: true,
			    size: BootstrapDialog.SIZE_WIDE,
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
			    		dialog.enableButtons(false);
			    		dialog.setClosable(false);
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			$.ajax({
			    				url: getRoot() + "sys/post/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						$("#postgrid").jqGrid().trigger("reloadGrid");
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
   		
   		function delPost(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/post/del.action",
	    				data: "post.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						$("#postgrid").jqGrid().trigger("reloadGrid");
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
   		
   		function delReimburseClass_department(id) {
   			var departmentId = departmentList.getSelected().id;
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/department/delClass.action",
	    				data: "reimburseClass.id="+id+"&department.id="+departmentId,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						$("#reimburseClassDatagrid_department").jqGrid().trigger("reloadGrid");
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
