<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>客户回款管理</title>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
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
   				<form id="search_form_customerPayback_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
   							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">开始时间：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.payDate_startDate" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">结束时间：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.payDate_endDate" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">公司：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.companyId" class="form-control select2" onchange="getCustomers_payback_list(this)">
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">客户：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.customerId" class="form-control select2">
								</select>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<button type="button" class="btn btn-info btn-sm pull-right" data-toggle="tooltip" title="查询" id="searchCustomerPayback"><i class="fa fa-fw fa-search"></i>查询</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增回款" id="addCustomerPayback"><i class="fa fa-fw fa-user-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="customerPaybackdatagrid"></table>
   					<div id="customerPaybackdatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var customerPaybackDataGrid;
   		$(function() {
   			$("#search_form_customerPayback_list").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			initSelect_customerPayback("search_form_customerPayback_list");
   			customerPaybackDataGrid = $("#customerPaybackdatagrid").jqGrid({
                url: getRoot() + "workflow/customerPayback/queryPage.action",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "客户名称", name: "customerName", width: 200
                }, {
                	label: "公司名称", name: "companyName", width: 150
                }, {
                	label: "回款时间", name: "payDate", width: 100
                }, {
                	label: "操作人", name: "userName", width: 150
                }, {
                	label: "操作时间", name: "logtime", width: 150
                }, {
                	label: "排序值", name: "orderNo", width: 150
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
   						var buttons = createBtn("修改", "btn-success btn-xs", "fa-pencil", "editCustomerPayback("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delCustomerPayback("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#customerPaybackdatagridpager"
            });
   			
   			$("#searchCustomerPayback").click(function() {
   				$("#customerPaybackdatagrid").setGridParam({
   					postData:parsePostData("search_form_customerPayback_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchCompanyPayList").click(function() {
   				$("#search_form_customerPayback_list")[0].reset();
   				$("#search_form_customerPayback_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   				$("#search_form_customerPayback_list select.select2[name='paramMap.customerId']").each(function() {
   					$(this).empty();
   				});
   			});
   			
			$("#addCustomerPayback").click(function() {
				BootstrapDialog.show({
				    title: "新增账户",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "workflow/customerPayback/queryById.action?returnType=add"),
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
				    				url: getRoot() + "workflow/customerPayback/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						customerPaybackDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function editCustomerPayback(id) {
   			BootstrapDialog.show({
			    title: "编辑账户",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/customerPayback/queryById.action?returnType=edit&customerPayback.id="+id),
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
			    				url: getRoot() + "workflow/customerPayback/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						customerPaybackDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function delCustomerPayback(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "workflow/customerPayback/del.action",
	    				data: "customerPayback.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						customerPaybackDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function initSelect_customerPayback(id) {
   			try {
   				$("#"+id+" select.select2").select2();
   				$.ajax({
   	   				url: getRoot() + "workflow/customerPayback/initSelect.action",
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						for(var key in json) {
   	   							var selector = "#"+id+" select[name='paramMap."+key+"']";
   	   							initOptions_customerPayback(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化企业付款查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_customerPayback(data, selector) {
   			if(data && data.length > 0 && selector) {
   				var option = document.createElement("option");
   				option.value = "";
   				option.text = "请选择";
   				$(selector).append(option);
   				for(var i = 0;i < data.length; i ++) {
   					var opData = data[i];
   					var option = document.createElement("option");
   					$(selector).append(option);
   					if(opData.id) {
   						option.value = opData.id;
   					} else if(opData.ID) {
   						option.value = opData.ID;
   					}
   					if(opData.name) {
   						option.text = opData.name;
   					} else if(opData.text) {
   						option.text = opData.text;
   					} else if(opData.TEXT) {
   						option.text = opData.TEXT;
   					} else if(opData.NAME) {
   						option.text = opData.NAME;
   					}
   				}
   			}
   		}
   		
   		function getCustomers_payback_list(obj) {
   			var $customers = $("#search_form_customerPayback_list").find(".select2[name='paramMap.customerId']");
   			$customers.empty();
   			var option = document.createElement("option");
			$customers.append(option);
			option.value = "";
			option.text = "请选择";
   			var val = $(obj).val();
   			if(val) {
   				$.ajax({
   					url: getRoot() + "workflow/customer/queryByCompanyId.action",
   					data: {
   						"companyId": val
   					},
   					type: "POST",
   					success: function(data) {
   						if(data) {
   							var json = eval("("+data+")");
   							if(json && 0 < json.length) {
   								for(var i = 0; i < json.length; i ++) {
   									var opData = json[i];
   									if(opData.id) {
   										var option = document.createElement("option");
   										$customers.append(option);
   										option.value = opData.id;
   										option.text = opData.name;
   									}
   								}
   							}
   						}
   					}
   				});
   			}
   			$customers.select2("val", "");
   		}
   	</script>
  </body>
</html>
