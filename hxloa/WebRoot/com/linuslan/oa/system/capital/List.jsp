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
   				<form id="search_form_capital_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">年月：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.date" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">类别：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.className" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">编号：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.serial" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">名称：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.name" type="text" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">型号：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.model" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">厂家：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.shopName" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">存放地：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.address" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属部门：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.department" class="form-control select2">
									
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">借用部门：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.borrowDepartment" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">使用人：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.borrowUser" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">当前状态：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.state" class="form-control select2">
									<option value="">全部</option>
		    						<option value="2" selected="selected">正常</option>
		    						<option value="1">作废</option>
								</select>
							</div>
							<div class="col-md-3 col-sm-6">
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchCapital"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm margin-left5" data-toggle="tooltip" title="重置" id="resetSearchCapital"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增固资" id="addCapital"><i class="fa fa-fw fa-user-plus"></i>新增</button>
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
   			initSelect_capital();
   			$("#search_form_capital_list").find(".date").datepicker({format: "yyyy-mm", language: "zh-CN"});
   			capitalDataGrid = $("#capitaldatagrid").jqGrid({
                url: getRoot() + "sys/capital/queryPage.action",
                mtype: "POST",
                shrinkToFit: false,
                rownumbers: true,
                height: "530px",
				styleUI : "Bootstrap",
                datatype: "json",
                gridComplete: function() {
                	$("#capitaldatagrid").setGridWidth($("#capitalList").width()*0.99);
                	$("#capitaldatagrid").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
                	$("#capitaldatagrid").jqGrid("setFrozenColumns");
                	setTimeout("hackHeight(\"#capitaldatagrid\")", 0);
                },
                colModel: [{
                	label: "固资类别", name: "className", width: 150, frozen: true
                }, {
                	label: "编号", name: "serial", width: 100, frozen: true
                }, {
                	label: "名称", name: "name", width: 150, frozen: true
                }, {
                	label: "型号", name: "model", width: 100
                }, {
                	label: "厂家", name: "shopName", width: 100
                }, {
                	label: "存放地", name: "address", width: 150
                }, {
                	label: "归属部门", name: "department", width: 100
                }, {
                	label: "借用/领用时间", name: "borrowDate", width: 80
                }, {
                	label: "借用部门", name: "borrowDepartment", width: 100
                }, {
                	label: "使用方", name: "borrowUser", width: 100
                }, {
                	label: "计量单位", name: "unit", width: 70
                }, {
                	label: "购置时间", name: "buyDate", width: 100
                }, {
                	label: "购置价格", name: "buyMoney", width: 80
                }/*, {
                	label: "折旧月数", name: "depreciationYear", width: 150
                }, {
                	label: "月折旧额", name: "depreciationMoney", width: 150
                }, {
                	label: "目前状况", name: "depreciationState", width: 150
                }, {
                	label: "至今折旧额", name: "depreciation", width: 150
                }, {
                	label: "净额", name: "netamount", width: 150
                }*/, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-o", "viewCapital("+rowObject.id+")");
   						buttons = buttons + createBtn("修改", "btn-success btn-xs", "fa-pencil", "editCapital("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delCapital("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                rowNum: 20,
                pager: "#capitaldatagridpager"
            });
   			
   			$("#searchCapital").click(function() {
   				$("#capitaldatagrid").setGridParam({
   					postData:parsePostData("search_form_capital_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchCapital").click(function() {
   				$("#search_form_capital_list")[0].reset();
   				$("#search_form_capital_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
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
   		
   		function viewCapital(id) {
   			BootstrapDialog.show({
			    title: "固资详情",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/capital/initContent.action?returnType=view&capital.id="+id),
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
	        });
   		}
   		
   		function delCapital(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/capital/delById.action",
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
   		
   		function initSelect_capital() {
   			try {
   				$("#search_form_capital_list select.select2").select2();
   				$.ajax({
   	   				url: getRoot() + "sys/capital/initSelect.action",
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						for(var key in json) {
   	   							var selector = "#search_form_capital_list select[name='paramMap."+key+"']";
   	   							initOptions_capital(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化固资查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_capital(data, selector) {
   			if(data && data.length > 0 && selector) {
   				var option = document.createElement("option");
   				option.value = "";
   				option.text = "请选择";
   				$(selector).append(option);
   				for(var i = 0;i < data.length; i ++) {
   					var opData = data[i];
   					var option = document.createElement("option");
   					$(selector).append(option);
   					option.value = opData.ID;
   					option.text = opData.TEXT;
   				}
   			}
   		}
   	</script>
  </body>
</html>
