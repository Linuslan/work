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
   				<form id="search_form_certificate_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">公司名称：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.company" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">证件名称：</label>
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
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">年检时间始：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.inspectionDateStart" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">年检时间止：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.inspectionDateEnd" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">文件状态：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.status" class="form-control select2">
									<option value="">请选择</option>
									<option value="1">正常</option>
									<option value="0">即将过期</option>
									<option value="3">最新过期</option>
									<option value="2">已过期</option>
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">年检状态：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.inspectionStatus" class="form-control select2">
									<option value="">请选择</option>
									<option value="1">正常</option>
									<option value="0">即将过期</option>
									<option value="3">最新过期</option>
									<option value="2">已过期</option>
								</select>
							</div>
							
						</div>
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<button type="reset" class="btn btn-info btn-sm pull-right" data-toggle="tooltip" title="重置" id="resetSearchCertificate"><i class="fa fa-fw fa-search"></i>重置</button>
								<button type="button" class="btn btn-info btn-sm pull-right margin-right5" data-toggle="tooltip" title="查询" id="searchCertificate"><i class="fa fa-fw fa-search"></i>查询</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增证件" id="addCertificate"><i class="fa fa-fw fa-user-plus"></i>新增</button>
   				</div>
   				<div class="box-body">
   					<table id="certificatedatagrid"></table>
   					<div id="certificatedatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var certificateDataGrid;
   		$(function() {
   			$("#search_form_certificate_list").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			certificateDataGrid = $("#certificatedatagrid").jqGrid({
                url: getRoot() + "sys/certificate/queryPage.action",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "公司名称", name: "company", width: 200, align: "center"
                }, {
                	label: "证件名称", name: "name", width: 200, align: "center"
                }, {
                	label: "有效期始", name: "startDate", width: 100, align: "center"
                }, {
                	label: "有效期止", name: "endDate", width: 100, align: "center"
                }, {
                	label: "剩余时间", name: "restTime", width: 100, align: "center"
                }, {
                	label: "年检时间", name: "inspectionDate", width: 100, align: "center"
                }, {
                	label: "状态", name: "status", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		if(rowObject.status == 0) {
                			return "<font color='green'>即将到期</font>";
                		} else if(rowObject.status == 2) {
                			return "<font color='red'>过期</font>";
                		} else if(rowObject.status == 3) {
                			return "<font color='orange'>最新过期</font>";
                		} else {
                			return "正常";
                		}
                	}
                }, {
                	label: "年检状态", name: "inspectionStatus", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		if(rowObject.inspectionStatus == 0) {
                			return "<font color='green'>即将到期</font>";
                		} else if(rowObject.inspectionStatus == 2) {
                			return "<font color='red'>过期</font>";
                		} else if(rowObject.inspectionStatus == 3) {
                			return "<font color='orange'>最新过期</font>";
                		} else {
                			return "正常";
                		}
                	}
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
                	label: "操作", width: 70, align: "center", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-o", "viewCertificate("+rowObject.id+")");
   						var buttons = createBtn("修改", "btn-success btn-xs", "fa-pencil", "editCertificate("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delCertificate("+rowObject.id+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#certificatedatagridpager"
            });
   			
   			$("#searchCertificate").click(function() {
   				$("#certificatedatagrid").setGridParam({
   					postData: parsePostData("search_form_certificate_list")
   				}).trigger("reloadGrid");
   			});
   			
			$("#addCertificate").click(function() {
				BootstrapDialog.show({
				    title: "新增公司",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/certificate/Add.jsp"),
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
				    				url: getRoot() + "sys/certificate/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						certificateDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function editCertificate(id) {
   			BootstrapDialog.show({
			    title: "编辑公司",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/certificate/queryById.action?returnType=edit&certificate.id="+id),
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
			    				url: getRoot() + "sys/certificate/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						certificateDataGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function viewCertificate(id) {
   			BootstrapDialog.show({
			    title: "公司详情",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/certificate/queryById.action?returnType=view&certificate.id="+id),
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
   		
   		function delCertificate(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/certificate/del.action",
	    				data: "certificate.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						certificateDataGrid.jqGrid().trigger("reloadGrid");
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
