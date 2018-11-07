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
  	<div id="phoneList">
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<form id="search_form_phone_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">电话号码：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.phoneNo" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">使用部门：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.department" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">使用人：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.userName" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">户名：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.accountName" type="text" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">缴费方式：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.paymentWay" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">是否过期：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.outOfDate" class="form-control select2">
									<option value="">请选择</option>
									<option value="0">未过期</option>
									<option value="2">即将过期</option>
									<option value="2">最新过期</option>
									<option value="3">已过期</option>
								</select>
							</div>
							<div class="col-md-3 col-sm-6">
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchPhone"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm margin-left5" data-toggle="tooltip" title="重置" id="resetSearchPhone"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增固话" id="addPhone"><i class="fa fa-fw fa-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="phonedatagrid"></table>
   					<div id="phonedatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var phoneDataGrid;
   		$(function() {
   			phoneDataGrid = $("#phonedatagrid").jqGrid({
                url: getRoot() + "sys/phone/queryPage.action",
                mtype: "POST",
                shrinkToFit: false,
                rownumbers: true,
				styleUI : "Bootstrap",
                datatype: "json",
                gridComplete: function() {
                	$("#phonedatagrid").setGridWidth($("#phoneList").width()*0.99);
                	$("#phonedatagrid").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
                	$("#phonedatagrid").jqGrid("setFrozenColumns");
                	
                	setTimeout("hackHeight(\"#phonedatagrid\")", 0);
                },
                colModel: [{
                	label: "使用部门", name: "department", width: 150, align: "center", frozen: true
                }, {
                	label: "电话号码", name: "phoneNo", width: 150, align: "center", frozen: true
                }, {
                	label: "使用人", name: "userName", width: 100, align: "center", frozen: true
                }, {
                	label: "使用地点", name: "address", width: 200, align: "center"
                }, {
                	label: "月租", name: "monthlyRent", width: 100, align: "center"
                }, {
                	label: "套餐", name: "phonePackage", width: 300, align: "center"
                }, {
                	label: "套餐结束时间", name: "packageEndDate", width: 110, align: "center"
                }, {
                	label: "户名", name: "accountName", width: 100, align: "center"
                }, {
                	label: "缴费方式", name: "paymentWay", width: 100, align: "center"
                }, {
                	label: "用途", name: "content", width: 300, align: "center"
                }, {
                	label: "备注", name: "info", width: 300, align: "center"
                }, {
                	label: "是否提醒", name: "isRemind", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		if(rowObject.isRemind == 0) {
                			return "是";
                		} else {
                			return "<font color='red'>否</font>";
                		}
                	}
                }, {
                	label: "操作", width: 100, align: "center", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-o", "viewPhone("+rowObject.id+")");
   						buttons = buttons + createBtn("修改", "btn-success btn-xs", "fa-pencil", "editPhone("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delPhone("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#phonedatagridpager"
            });
            
            $("#searchPhone").click(function() {
   				$("#phonedatagrid").setGridParam({
   					postData:parsePostData("search_form_phone_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchPhone").click(function() {
   				$("#search_form_phone_list")[0].reset();
   				$("#search_form_phone_list .select2").val("").trigger("change");
   			});
            
			$("#addPhone").click(function() {
				BootstrapDialog.show({
				    title: "新增固话",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/phone/Add.jsp"),
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
				    		var $button = this;
				    		try {
				    			var form = dialog.getModalBody().find("form");
				    			$.ajax({
				    				url: getRoot() + "sys/phone/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						phoneDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function editPhone(id) {
   			BootstrapDialog.show({
			    title: "编辑固话",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/phone/queryById.action?returnType=edit&phone.id="+id),
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
			    		var $button = this; // 'this' here is a jQuery object that wrapping the <button> DOM element.
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			$.ajax({
			    				url: getRoot() + "sys/phone/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						phoneDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function viewPhone(id) {
   			BootstrapDialog.show({
			    title: "固话详情",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/phone/queryById.action?returnType=view&phone.id="+id),
			    draggable: true,
			    size: BootstrapDialog.SIZE_WIDE,
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
   		
   		function delPhone(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/phone/del.action",
	    				data: "phone.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						phoneDataGrid.jqGrid().trigger("reloadGrid");
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
