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
   			<input name="paramMap.articleId" value="${param.articleId }" type="hidden" id="stockDetailArticleId" />
   			<input name="paramMap.formatId" value="${param.formatId }" type="hidden" id="stockDetailFormatId" />
   			<div class="box box-solid">
   				<form id="search_form_stockDetailReport_report_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">开始时间：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.order_date_startDate" type="text" class="form-control pull-right" data-inputmask="'mask': '9999-99-99'" data-mask>
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">结束时间：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.order_date_endDate" type="text" class="form-control pull-right" data-inputmask="'mask': '9999-99-99'" data-mask>
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.companyId" class="form-control select2">
									
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
								<button type="button" class="btn btn-info btn-sm pull-right" data-toggle="tooltip" title="重置" id="resetSearchStockDetailReportReportList"><i class="fa fa-fw fa-undo"></i>重置</button>
								<button type="button" class="btn btn-info btn-sm margin-right5 pull-right" data-toggle="tooltip" title="查询" id="searchStockDetailReportReportList"><i class="fa fa-fw fa-search"></i>查询</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="box-body">
   					<table id="stockDetailReportReportDatagrid"></table>
   					<div id="stockDetailReportReportDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var reportStockReportGrid;
   		var reportStockReportDialog;
   		$(function() {
   			//sumReportPage_stockReport();
			$("#search_form_stockDetailReport_report_list [data-mask]").inputmask();
			initSelect_stockDetailReportReport("search_form_stockDetailReport_report_list");
			$("#searchStockDetailReportReportList").click(function() {
   				$("#stockDetailReportReportDatagrid").setGridParam({
   					postData:parsePostData("search_form_stockDetailReport_report_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchStockDetailReportReportList").click(function() {
   				$("#search_form_stockDetailReport_report_list")[0].reset();
   				$("#search_form_stockDetailReport_report_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			
   			reportStockReportGrid = $("#stockDetailReportReportDatagrid").jqGrid({
                url: getRoot() + "workflow/report/queryStockDetailReportPage.action?paramMap.articleId="+$("#stockDetailArticleId").val()+"&paramMap.formatId="+$("#stockDetailFormatId").val(),
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "日期", name: "ORDER_DATE", width: 200, align: "center"
                }, {
                	label: "年", name: "YEAR", width: 200, align: "center"
                }, {
                	label: "月", name: "MONTH", width: 200, align: "center"
                }, {
                	label: "归属公司", name: "COMPANY_NAME", width: 200, align: "center"
                }, {
                	label: "归属客户", name: "CUSTOMER_NAME", width: 200, align: "center"
                }, {
                	label: "数量(KG)", name: "CHECKIN_QUANTITY", width: 200, align: "center"
                }, {
                	label: "单价(元)", name: "PRICE", width: 150, align: "center"
                }, {
                	label: "金额(元)", name: "TOTALPRICE", width: 150, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		//出库总量=出库量+销售出库+损毁量
                		var totalAmount = 0;
                		try {
                			var checkinQuantity = rowObject.CHECKIN_QUANTITY;
                			var price = rowObject.PRICE;
                			totalAmount = checkinQuantity*price;
                		} catch(ex) {
                			totalAmount = 0;
                		}
                		totalAmount = totalAmount.toFixed(2);
                		return totalAmount;
                	}
                }, {
                	label: "数量(KG)", name: "CHECKOUT_QUANTITY", width: 150, align: "center"
                }, {
                	label: "单价(元)", name: "PRICE", width: 150, align: "center"
                }, {
                	label: "金额(远)", name: "checkedOut_total", width: 150, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		//出库总量=出库量+销售出库+损毁量
                		var totalAmount = 0;
                		try {
                			var checkoutQuantity = rowObject.CHECKOUT_QUANTITY;
                			var price = rowObject.PRICE;
                			totalAmount = checkoutQuantity*price;
                		} catch(ex) {
                			totalAmount = 0;
                		}
                		totalAmount = totalAmount.toFixed(2);
                		return totalAmount;
                	}
                }, {
                	label: "数量(KG)", name: "remainderTotal", width: 150, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		//本月损毁=入库损毁+损毁出库
                		var loss = 0;
                		try {
                			var checkinQuantity = rowObject.CHECKIN_TOTAL;
                			var checkoutQuantity = rowObject.CHECKOUT_TOTAL;
                			loss = checkinQuantity - checkoutQuantity;
                		} catch(ex) {
                			loss = 0;
                		}
                		loss = loss.toFixed(2);
                		return loss;
                	}
                }, {
                	label: "单价(元)", name: "PRICE", width: 150, align: "center"
                }, {
                	label: "金额(元)", name: "checkingOutTotal", width: 150, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		//库存=总入库量-出库总量
                		var loss = 0;
                		var totalAmount=0;
                		try {
                			var checkinQuantity = rowObject.CHECKIN_TOTAL;
                			var checkoutQuantity = rowObject.CHECKOUT_TOTAL;
                			var price = rowObject.PRICE;
                			loss = checkinQuantity - checkoutQuantity;
                			totalAmount = loss * price;
                		} catch(ex) {
                			totalAmount = 0;
                		}
                		totalAmount = totalAmount.toFixed(2);
                		return totalAmount;
                	}
                }/*, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewReportStockDetailReport("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+")");
   						return buttons;
                	}
                }*/],
				viewrecords: true,
                height: "700px",
                rowNum: 20,
                grouping: true,
			   	groupingView : {
			   		groupField : ["YEAR", "MONTH"],
			   		groupColumnShow : [false, false],
			   		groupText : ['<b>{0}</b>'],
			   		groupCollapse : false,
					groupOrder: ['asc', 'asc'],
					groupSummary : [false, false]
			   	},
                pager: "#stockDetailReportReportDatagridpager"
            });
            
            $("#stockDetailReportReportDatagrid").jqGrid("setGroupHeaders", {
   				useColSpanStyle: true,
   				groupHeaders:[{
   					startColumnName: "CHECKIN_QUANTITY", numberOfColumns: 3, titleText: "入库"
   				}, {
   					startColumnName: "CHECKOUT_QUANTITY", numberOfColumns: 3, titleText: "出库"
   				}, {
   					startColumnName: "remainderTotal", numberOfColumns: 3, titleText: "结存"
   				}]
   			});
   		});
   		
   		function viewReportStockDetailReport(id, passBtn, rejectBtn) {
   			reportStockReportDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "60%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/report/queryById.action?returnType=audit&stockReport.id="+id),
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
   		
   		function initSelect_stockDetailReportReport(id) {
   			try {
   				$("#"+id+" select.select2").select2();
   				$.ajax({
   	   				url: getRoot() + "workflow/report/initSelect.action",
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						for(var key in json) {
   	   							var selector = "#"+id+" select[name='paramMap."+key+"']";
   	   							initOptions_stockDetailReportReport(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化报销查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_stockDetailReportReport(data, selector) {
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
   		
   		/*function sumReportPage_stockReport() {
   			$.ajax({
   				url: getRoot() + "workflow/stockReport/sumReportPage.action",
   				data: $("#search_form_stockDetailReport_report_list").serialize(),
   				type: "POST",
   				success: function(data) {
   					try {
   						if(data) {
	   						var json = eval("("+data+")");
	   						for(var key in json) {
	   							var sumAmount = json[key];
	   							sumAmount = sumAmount.toFixed(2);
	   							$("#sumReportPage_stockReport ."+key).html(sumAmount);
	   						}
	   					}
   					} catch(ex) {
   						BootstrapDialog.danger("统计汇总页面异常，"+ex);
   					}
   				},
   				error: function() {
   					BootstrapDialog.danger("系统异常，请联系管理员！");
   				}
   			});
   		}*/
   	</script>
  </body>
</html>
