<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>待登录用户审核的报销单列表</title>
    
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
   				<form id="search_form_specialSale_audited_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">订单时间始：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.saleDate_start" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">订单时间止：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.saleDate_end" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">出货时间始：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.checkoutDate_start" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">出货时间止：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.checkoutDate_end" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.companyId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">申请人：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.userId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">付款方式：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.payTypeId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">送货方式：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.deliverTypeId" class="form-control select2">
									
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">编号：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.serialNo" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">客户：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.customer" type="text" class="form-control">
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">收货人：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.receiver" type="text" class="form-control">
							</div>
							<div class="col-md-3 col-sm-6">
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchSpecialSaleAuditedList"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm margin-left5" data-toggle="tooltip" title="重置" id="resetSearchSpecialSaleAuditedList"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
						
   					</div>
   				</form>
   				<div class="box-body">
   					<table id="specialSaleAuditedDatagrid"></table>
   					<div id="specialSaleAuditedDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var auditedSpecialSaleGrid;
   		var auditedSpecialSaleDialog;
   		$(function() {
   			
   			$("#search_form_specialSale_audited_list").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
			initSelect_specialSaleAudited("search_form_specialSale_audited_list");
			$("#searchSpecialSaleAuditedList").click(function() {
   				$("#specialSaleAuditedDatagrid").setGridParam({
   					postData:parsePostData("search_form_specialSale_audited_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchSpecialSaleAuditedList").click(function() {
   				$("#search_form_specialSale_audited_list")[0].reset();
   				$("#search_form_specialSale_audited_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			
   			auditedSpecialSaleGrid = $("#specialSaleAuditedDatagrid").jqGrid({
                url: getRoot() + "workflow/specialSale/queryAuditedPage.action",
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
                	label: "申请人", name: "userName", width: 110, align: "center"
                }, {
                	label: "编号", name: "serialNo", width: 110, align: "center"
                }, {
                	label: "客户", name: "customer", width: 150, align: "center"
                }, {
                	label: "付款方式", name: "payTypeName", width: 150, align: "center"
                }, {
                	label: "送货方式", name: "deliverTypeName", width: 150, align: "center"
                }, {
                	label: "总金额", name: "totalAmount", width: 100, align: "center"
                }, {
                	label: "定金", name: "frontMoney", width: 100, align: "center"
                }, {
                	label: "回款金额", name: "actualMoney", width: 100, align: "center"
                }, {
                	label: "应收账款", name: "supposedMoney", width: 100, align: "center"
                }, {
                	label: "结款日期", name: "payDate", width: 150, align: "center"
                }, {
                	label: "是否开票", name: "isInvoice", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		if(rowObject.isInvoice == 1) {
                			return "<font color='red'>是</font>";
                		} else {
                			return "否";
                		}
                	}
                }, {
                	label: "出库状态", name: "checkoutStatus", width: 150, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		if(rowObject.checkoutStatus == 1) {
                			return "<font color='orange'>部分出库</font>";
                		} else if(rowObject.checkoutStatus == 2) {
                			return "<font color='green'>已出库</font>";
                		} else {
                			return "未出库";
                		}
                	}
                }, {
                	label: "流程状态", name: "flowStatus", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		return getFlowStatus(rowObject.status);
                	}
                }, {
                	label: "当前审核", name: "auditors", width: 150, align: "center"
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewAuditedSpecialSale("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+", \""+rowObject.flowStatus+"\")");
                		var isCheckoutAuth = specialSaleCheckoutAuth();
                		var isStatementEditAuth = specialSaleStatementAuth();
                		if(isCheckoutAuth == true && (rowObject.allFlowStatus.indexOf("7") || rowObject.allFlowStatus.indexOf("8") || rowObject.status == 4)) {
                			if(rowObject.flowStatus == 7 || rowObject.flowStatus == 8) {
                				buttons = buttons + createBtn("出货单", "btn-info btn-xs", "fa-file-text-o", "auditCheckoutSpecialSale_edit("+rowObject.id+")");
                			} else {
                				buttons = buttons + createBtn("出货单", "btn-info btn-xs", "fa-file-text-o", "auditCheckoutSpecialSale_view("+rowObject.id+")");
                			}
                			
                		}
                		if(rowObject.status != 0 && rowObject.status != 1 && rowObject.status != 2) {
                			if(isStatementEditAuth == true) {
                				buttons = buttons + createBtn("结算单", "btn-info btn-xs", "fa-file-text-o", "auditStatementSpecialSale_edit("+rowObject.id+")");
                			} else {
                				buttons = buttons + createBtn("结算单", "btn-info btn-xs", "fa-file-text-o", "auditStatementSpecialSale_view("+rowObject.id+")");
                			}
                		}
                		
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#specialSaleAuditedDatagridpager"
            });
   		});
   		
   		function viewAuditedSpecialSale(id, passBtn, rejectBtn, flowStatus) {
   			
   			var returnType = "view";
   			
   			auditedSpecialSaleDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "90%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/specialSale/queryById.action?returnType="+returnType+"&specialSale.id="+id),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [{
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-warning",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
   		}
   		
   		function initSelect_specialSaleAudited(id) {
   			try {
   				$("#"+id+" select.select2").select2();
   				$.ajax({
   	   				url: getRoot() + "workflow/specialSale/initSelect.action",
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						for(var key in json) {
   	   							var selector = "#"+id+" select[name='paramMap."+key+"']";
   	   							initOptions_specialSaleAudited(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化销售查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_specialSaleAudited(data, selector) {
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
   		
   		
   		function auditedCheckoutSpecialSale_edit(id) {
   			specialSaleDialog = BootstrapDialog.show({
			    title: "销售出货单详情",
			    width: "90%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/specialSale/queryById.action?returnType=checkoutEdit&specialSale.id="+id),
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
			    			var form = dialog.getModalBody().find("form");
			    			var $trs = form.find("table").find("tr.contents");
			    			var contents =  new Array();
			    			for(var i = 0; i < $trs.length; i ++) {
			    				var $tr = $($trs[i]);
			    				var id = $tr.find("input[name='contentId']").val();
			    				var originalMaterialFormat = $tr.find("input[name='originalMaterialFormat']").val();
			    				var checkoutUnit = $tr.find("input[name='checkoutUnit']").val();
			    				var checkoutQuantity = $tr.find("input[name='checkoutQuantity']").val();
			    				contents.push("contents["+i+"].id="+id);
			    				contents.push("contents["+i+"].originalMaterialFormat="+originalMaterialFormat);
			    				contents.push("contents["+i+"].checkoutUnit="+checkoutUnit);
			    				contents.push("contents["+i+"].checkoutQuantity="+checkoutQuantity);
			    			}
			    			var $button = this;
			    			$.ajax({
			    				url: getRoot() + "workflow/specialSale/updateContentBatch.action",
			    				data: contents.join("&"),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
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
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "打印",
			    	icon: "fa fa-fw fa-print",
			    	cssClass: "btn-info",
			    	action: function(dialog) {
			    		var form = dialog.getModalBody().find("form");
		    			var $inputs = form.find("table").find("input.update");
		    			for(var i = 0; i < $inputs.length; i ++) {
		    				var $input = $($inputs[i]);
		    				var val = $input.val();
		    				$input.parent().html(val);
		    			}
		    			
		    			var html = "<!DOCTYPE html><html><head><meta charset='utf-8'><meta http-equiv='X-UA-Compatible' content='IE=edge'>";
		   	    		html = html + "<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>";
		   	    		//var html = "";
		   	    		html = html + getPrintCss();
		   	    		html = html + "<style>table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse} .tab_css_2 td{ text-align:left; overflow: visible; }.tab_css_2{ border: 1px solid #cad9ea; border-collapse: collapse; color:#000; }";
		   	    		html = html + ".tab_css_2 th { background-image: url("+getRoot()+"resources/css/oaimg/th_bg1.gif); background-repeat:repeat-x; color:#000; }";
		   	    		html = html + ".tab_css_2 td{ border-bottom:1px solid #cad9ea; border-right: 1px solid #cad9ea; padding:0 2px 0; }";
		   	    		html = html + ".tab_css_2 th{ border:1px solid #a7d1fd; padding:0 2px 0; }";
		   	    		html = html + ".tab_css_2 tr.tr_css td{ background-color:#eaf4ff; } table{ empty-cells:show; border-collapse: collapse; margin:0 auto; font-size:12px; line-height: 20px;}</style></head>";
		   	    		Lodop = getLodop();
		   	    		LODOP.PRINT_INIT("销售出货单打印");
		   	    		html = html + "<body>"+form.html()+"</body>";
		   	    		LODOP.ADD_PRINT_HTM(5,5,"200mm","287mm", html);
		   				LODOP.PREVIEW();
		    			
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
   		
   		function auditedCheckoutSpecialSale_view(id) {
   			specialSaleDialog = BootstrapDialog.show({
			    title: "销售出货单详情",
			    width: "90%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/specialSale/queryById.action?returnType=checkoutView&specialSale.id="+id),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [{
			    	label: "打印",
			    	icon: "fa fa-fw fa-print",
			    	cssClass: "btn-info",
			    	action: function(dialog) {
			    		var form = dialog.getModalBody().find("form");
		    			var html = "<!DOCTYPE html><html><head><meta charset='utf-8'><meta http-equiv='X-UA-Compatible' content='IE=edge'>";
		   	    		html = html + "<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>";
		   	    		//var html = "";
		   	    		html = html + getPrintCss();
		   	    		html = html + "<style>table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse} .tab_css_2 td{ text-align:left; overflow: visible; }.tab_css_2{ border: 1px solid #cad9ea; border-collapse: collapse; color:#000; }";
		   	    		html = html + ".tab_css_2 th { background-image: url("+getRoot()+"resources/css/oaimg/th_bg1.gif); background-repeat:repeat-x; color:#000; }";
		   	    		html = html + ".tab_css_2 td{ border-bottom:1px solid #cad9ea; border-right: 1px solid #cad9ea; padding:0 2px 0; }";
		   	    		html = html + ".tab_css_2 th{ border:1px solid #a7d1fd; padding:0 2px 0; }";
		   	    		html = html + ".tab_css_2 tr.tr_css td{ background-color:#eaf4ff; } table{ empty-cells:show; border-collapse: collapse; margin:0 auto; font-size:12px; line-height: 20px;}</style></head>";
		   	    		Lodop = getLodop();
		   	    		LODOP.PRINT_INIT("销售出货单打印");
		   	    		html = html + "<body>"+form.html()+"</body>";
		   	    		LODOP.ADD_PRINT_HTM(5,5,"200mm","287mm", html);
		   				LODOP.PREVIEW();
		    			
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
   		
   		function auditedStatementSpecialSale_edit(id) {
   			specialSaleDialog = BootstrapDialog.show({
			    title: "销售结算单详情",
			    width: "90%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/specialSale/queryById.action?returnType=statementEdit&specialSale.id="+id),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-print",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		var form = dialog.getModalBody().find("form");
			    		var $button = this;
		    			$.ajax({
		    				url: getRoot() + "workflow/specialSale/updateSale.action",
		    				data: form.serialize(),
		    				type: "POST",
		    				success: function(data) {
		    					var json = eval("("+data+")");
		    					if(json.success) {
		    						BootstrapDialog.success(json.msg);
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
			    	}
			    }, {
			    	label: "打印",
			    	icon: "fa fa-fw fa-print",
			    	cssClass: "btn-info",
			    	action: function(dialog) {
			    		var form = dialog.getModalBody().find("form");
		    			var $selects = form.find("div.form-group").find("select");
		    			for(var i = 0; i < $selects.length; i ++) {
		    				var $select = $($selects[i]);
		    				var val = $select.find("option:selected").text();
		    				$select.parent().html(val);
		    			}
		    			
		    			var html = "<!DOCTYPE html><html><head><meta charset='utf-8'>";
		    			//html = html + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>"
		   	    		//html = html + "<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>";
		   	    		//var html = "";
		   	    		html = html + getPrintCss();
		   	    		html = html + "<style>table td{padding: 0px; text-align: left;}; .align-right{text-align: right;}</style>";
		   	    		html = html + "</head>";
		   	    		Lodop = getLodop();
		   	    		LODOP.PRINT_INIT("销售结算单打印");
		   	    		html = html + "<body>"+form.html()+"</body>";
		   	    		LODOP.ADD_PRINT_HTM(5,5,"200mm","287mm", html);
		   				LODOP.PREVIEW();
		    			
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
   		
   		function auditedStatementSpecialSale_view(id) {
   			specialSaleDialog = BootstrapDialog.show({
			    title: "销售结算单详情",
			    width: "90%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/specialSale/queryById.action?returnType=statementView&specialSale.id="+id),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [{
			    	label: "打印",
			    	icon: "fa fa-fw fa-print",
			    	cssClass: "btn-info",
			    	action: function(dialog) {
			    		var form = dialog.getModalBody().find("form");
		    			var $selects = form.find("div.form-group").find("select");
		    			for(var i = 0; i < $selects.length; i ++) {
		    				var $select = $($selects[i]);
		    				var val = $select.find("option:selected").text();
		    				$select.parent().html(val);
		    			}
		    			
		    			var html = "<!DOCTYPE html><html><head><meta charset='utf-8'>";
		    			//html = html + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>"
		   	    		//html = html + "<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>";
		   	    		//var html = "";
		   	    		html = html + getPrintCss();
		   	    		html = html + "<style>table td{padding: 0px; text-align: left;}; .align-right{text-align: right;}</style>";
		   	    		html = html + "</head>";
		   	    		Lodop = getLodop();
		   	    		LODOP.PRINT_INIT("销售结算单打印");
		   	    		html = html + "<body>"+form.html()+"</body>";
		   	    		LODOP.ADD_PRINT_HTM(5,5,"200mm","287mm", html);
		   				LODOP.PREVIEW();
		    			
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
   		
   	</script>
  </body>
</html>
