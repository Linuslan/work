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
  	<div id="contractList">
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<form id="search_form_contract_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">合同编号：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.contractNum" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">合作单位：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.name" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">有效期始：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.startDate" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">有效期止：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.endDate" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">签约时间始：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.signDate_Start" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">签约时间止：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.signDate_End" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">签约人：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.contractor" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">状态：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.outOfDate" class="form-control select2">
									<option value="">请选择</option>
									<option value="0">正常</option>
									<option value="2">即将过期</option>
									<option value="3">最新过期</option>
									<option value="1">已过期</option>
								</select>
							</div>
							
						</div>
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<button type="button" class="btn btn-info btn-sm pull-right" data-toggle="tooltip" title="重置" id="resetSearchContract"><i class="fa fa-fw fa-undo"></i>重置</button>
								<button type="button" class="btn btn-info btn-sm pull-right margin-right5" data-toggle="tooltip" title="查询" id="searchContract"><i class="fa fa-fw fa-search"></i>查询</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增合同" id="addContract"><i class="fa fa-fw fa-user-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="contractdatagrid"></table>
   					<div id="contractdatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var contractDataGrid;
   		$(function() {
   			initSelect_contract();
   			$("#search_form_contract_list").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			contractDataGrid = $("#contractdatagrid").jqGrid({
                url: getRoot() + "sys/contract/queryPage.action",
                mtype: "POST",
                shrinkToFit: false,
                rownumbers: true,
                height: "535px",
				styleUI : "Bootstrap",
                datatype: "json",
                gridComplete: function() {
                	$("#contractdatagrid").setGridWidth($("#contractList").width()*0.99);
                	$("#contractdatagrid").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
                	$("#contractdatagrid").jqGrid("setFrozenColumns");
                	setTimeout("hackHeight(\"#contractdatagrid\")", 0);
                },
                colModel: [{
                	label: "合同编号", name: "contractNum", width: 200, frozen: true
                }, {
                	label: "合作单位", name: "name", width: 150, frozen: true
                }, {
                	label: "合作期限", name: "date", width: 150, frozen: true,
                	formatter: function(cellvalue, options, rowObject) {
                		return "起："+rowObject.startDate+"<br />止："+rowObject.endDate;
                	}
                }, {
                	label: "合同状态", name: "outOfDate", width: 150, frozen: true,
                	formatter: function(cellvalue, options, rowObject) {
                		var text = "";
                		if(rowObject.outOfDate == 0) {
                			text = "正常";
                		} else if(rowObject.outOfDate == 1) {
                			text = "<font color='red'>过期</font>";
                		} else if(rowObject.outOfDate == 2) {
                			text = "<font color='green'>即将过期</font>";
                		} else if(rowObject.outOfDate == 3) {
                			text = "<font color='orange'>新过期</font>";
                		}
                		if(rowObject.isReload == 1) {
                			text = text + "<font>（自动延续）</font>";
                		}
                		return text;
                	}
                }, {
                	label: "复印件份数", name: "copyNum", width: 150
                }, {
                	label: "签约部门", name: "departmentName", width: 150
                }, {
                	label: "签约人", name: "contractorName", width: 150
                }, {
                	label: "简介", name: "content", width: 300, frozen: true
                }, {
                	label: "备注", name: "info", width: 300
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-o", "viewContract("+rowObject.id+")");
   						buttons = buttons + createBtn("修改", "btn-success btn-xs", "fa-pencil", "editContract("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delContract("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                rowNum: 20,
                pager: "#contractdatagridpager"
            });
            
            $("#searchContract").click(function() {
   				$("#contractdatagrid").setGridParam({
   					postData: parsePostData("search_form_contract_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchContract").click(function() {
   				$("#search_form_contract_list")[0].reset();
   				
   				$("#search_form_contract_list .select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
            
			$("#addContract").click(function() {
				BootstrapDialog.show({
				    title: "新增合同",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "sys/contract/queryById.action?returnType=add"),
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
				    			var $form = dialog.getModalBody().find("form");
				    			upload($form, function(response) {
				    				try {
				    					var json = eval("("+response+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						contractDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function viewContract(id) {
   			BootstrapDialog.show({
			    title: "合同详情",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/contract/queryById.action?returnType=view&contract.id="+id),
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
   		
   		function editContract(id) {
   			BootstrapDialog.show({
			    title: "编辑合同",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/contract/queryById.action?returnType=edit&contract.id="+id),
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
			    			upload($form, function(response) {
			    				try {
			    					var json = eval("("+response+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						contractDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function delContract(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/contract/del.action",
	    				data: "contract.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						contractDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function initSelect_contract() {
   			try {
   				$("select.select2").select2();
   				$.ajax({
   	   				url: getRoot() + "sys/contract/initSelect.action",
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						for(var key in json) {
   	   							var selector = "select[name='paramMap."+key+"']";
   	   							initOptions_contract(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化固资查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_contract(data, selector) {
   			if(data && data.length > 0 && selector) {
   				var option = document.createElement("option");
   				option.value = "";
   				option.text = "请选择";
   				$(selector).append(option);
   				for(var i = 0;i < data.length; i ++) {
   					var opData = data[i];
   					var option = document.createElement("option");
   					$(selector).append(option);
   					option.value = opData.id;
   					option.text = opData.name;
   				}
   			}
   		}
   	</script>
  </body>
</html>
