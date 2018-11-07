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
   				<form id="search_form_stockReport_report_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">年月：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="date" type="text" class="form-control pull-right" data-inputmask="'mask': '9999-99'" data-mask>
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.companyId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">商品：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.articleId" class="form-control select2">
									
								</select>
							</div>
							<div class="col-md-3 col-sm-3">
								<button type="button" class="btn btn-info btn-sm margin-right5" data-toggle="tooltip" title="查询" id="searchStockReportReportList"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="重置" id="resetSearchStockReportReportList"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				<!-- 暂时先将总计隐藏不删，如果确实需要时，再使用 -->
   				<!-- <div class="box-body top-dotted-border bottom-dotted-border form-horizontal" id="sumReportPage_stockReport">
   					<div class="form-group">
						<label for="name" class="col-md-2 col-sm-4 col-xs-4 control-label">已打款总额：</label>
						<div class="col-md-2 col-sm-8 col-xs-8 sumPaid left-label">
							
						</div>
						<label for="name" class="col-md-2 col-sm-4 col-xs-4 control-label">待打款总额：</label>
						<div class="col-md-2 col-sm-8 col-xs-8 sumPay left-label">
							
						</div>
						<label for="name" class="col-md-2 col-sm-4 col-xs-4 control-label">待审核总额：</label>
						<div class="col-md-2 col-sm-8 col-xs-8 sumAudit left-label">
							
						</div>
					</div>
   				</div> -->
   				<div class="box-body">
   					<table id="stockReportReportDatagrid"></table>
   					<div id="stockReportReportDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var reportStockReportGrid;
   		var reportStockReportDialog;
   		$(function() {
   			//sumReportPage_stockReport();
			$("#search_form_stockReport_report_list [data-mask]").inputmask();
			initSelect_stockReportReport("search_form_stockReport_report_list");
			$("#searchStockReportReportList").click(function() {
   				$("#stockReportReportDatagrid").setGridParam({
   					postData:parsePostData("search_form_stockReport_report_list")
   				}).trigger("reloadGrid");
   				sumReportPage_stockReport();
   			});
   			
   			$("#resetSearchStockReportReportList").click(function() {
   				$("#search_form_stockReport_report_list")[0].reset();
   				$("#search_form_stockReport_report_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			
   			reportStockReportGrid = $("#stockReportReportDatagrid").jqGrid({
                url: getRoot() + "workflow/report/queryStockReportPage.action",
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "商品名称", name: "ARTICLE_NAME", width: 100, align: "center"
                }, {
                	label: "规格", name: "FORMAT_NAME", width: 100, align: "center"
                }, {
                	label: "归属公司", name: "COMPANY_NAME", width: 200, align: "center"
                }, {
                	label: "上月结余", name: "lastMonthRemainder", width: 200, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		//上月结余=入库数量-出库数量-销售出库-损毁量
                		var remainder = 0;
                		try {
                			//截止上月入库量
                			var lastMonthCheckinTotal = rowObject.LAST_MONTH_CHECKIN;
                			//截止上月入库损毁
                			var lastMonthCheckinLoss = rowObject.LAST_MONTH_LOSS;
                			//截止上月出库量
                			var lastMonthCheckedOut = rowObject.LAST_MONTH_CHECKED_OUT;
                			//截止上月出库损毁
                			var lastMonthCheckedOutLoss = rowObject.LAST_MONTH_CHECKED_OUT_LOSS;
                			//截止上月销售出库总额
                			var lastMonthSaleCheckedOut = rowObject.LAST_MONTH_SALE_CHECKED_OUT;
                			remainder = lastMonthCheckinTotal - lastMonthCheckinLoss - lastMonthCheckedOut - lastMonthCheckedOutLoss - lastMonthSaleCheckedOut;
                		} catch(ex) {
                			remainder = 0;
                		}
                		remainder = remainder.toFixed(2);
                		return remainder;
                	}
                }, {
                	label: "本月入库", name: "MONTH_CHECKIN", width: 150, align: "center"
                }, {
                	label: "本月出库", name: "monthCheckedOut", width: 150, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		//本月出库=出库数量+销售出库+损毁量
                		var checkedOutTotal = 0;
                		try {
                			//出库
                			var monthCheckedOut = rowObject.MONTH_CHECKED_OUT;
                			//销售出库
                			var monthSaleCheckedOut = rowObject.MONTH_SALE_CHECKED_OUT;
                			//入库损毁
                			var monthCheckinLoss = rowObject.MONTH_CHECKIN_LOSS;
                			//损毁出库
                			var monthCheckedOutLoss = rowObject.MONTH_CHECKED_OUT_LOSS;
                			checkedOutTotal = monthCheckedOut + monthSaleCheckedOut+monthCheckinLoss+monthCheckedOutLoss;
                		} catch(ex) {
                			checkedOutTotal = 0;
                		}
                		checkedOutTotal = checkedOutTotal.toFixed(2);
                		return checkedOutTotal;
                	}
                }, {
                	label: "本月耗损", name: "monthLoss", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		//本月损毁=入库损毁+损毁出库
                		var loss = 0;
                		try {
                			var checkinLoss = rowObject.MONTH_CHECKIN_LOSS;
                			var checkoutLoss = rowObject.MONTH_CHECKED_OUT_LOSS;
                			loss = checkinLoss + checkoutLoss;
                		} catch(ex) {
                			loss = 0;
                		}
                		loss = loss.toFixed(2);
                		return loss;
                	}
                }, {
                	label: "总入库量", name: "CHECKIN_QUANTITY", width: 150, align: "center"
                }, {
                	label: "总出库量", name: "checkedOut_total", width: 150, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		//出库总量=出库量+销售出库+损毁量
                		var checkedOutTotal = 0;
                		try {
                			var checkedOut = rowObject.CHECKED_OUT_TOTAL;
                			var saleCheckedOut = rowObject.SALE_CHECKED_OUT_TOTAL;
                			//入库总损毁
                			var lossTotal = rowObject.LOSS_TOTAL;
                			var checkedOutLoss = rowObject.CHECKED_OUT_LOSS;
                			checkedOutTotal = checkedOut + saleCheckedOut + lossTotal + checkedOutLoss;
                		} catch(ex) {
                			checkedOutTotal = 0;
                		}
                		checkedOutTotal = checkedOutTotal.toFixed(2);
                		return checkedOutTotal;
                	}
                }, {
                	label: "总耗损量", name: "lossTotal", width: 150, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		//本月损毁=入库损毁+损毁出库
                		var loss = 0;
                		try {
                			var checkinLoss = rowObject.LOSS_TOTAL;
                			var checkoutLoss = rowObject.CHECKED_OUT_LOSS;
                			loss = checkinLoss + checkoutLoss;
                		} catch(ex) {
                			loss = 0;
                		}
                		loss = loss.toFixed(2);
                		return loss;
                	}
                }, {
                	label: "总待审量", name: "checkingOutTotal", width: 150, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		var checkingOutTotal = 0;
                		try {
                			var checkingOut = rowObject.CHECKING_OUT_TOTAL;
                			var saleCheckingOut = rowObject.SALE_CHECKING_OUT_TOTAL;
                			checkingOutTotal = checkingOut + saleCheckingOut;
                		} catch(ex) {
                			checkingOutTotal = 0;
                		}
                		checkingOutTotal = checkingOutTotal.toFixed(2);
                		return checkingOutTotal;
                	}
                }, {
                	label: "总库存量", name: "checkingOutTotal", width: 150, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		//库存=总入库量-出库总量
                		var remainderTotal = 0;
                		try {
                			var checkin = rowObject.CHECKIN_QUANTITY;
                			var loss = rowObject.LOSS_TOTAL;
                			var checkedOut = rowObject.CHECKED_OUT_TOTAL;
                			var saleCheckedOut = rowObject.SALE_CHECKED_OUT_TOTAL;
                			var checkedOutLoss = rowObject.CHECKED_OUT_LOSS;
                			remainderTotal = checkin - loss - checkedOut - saleCheckedOut - checkedOutLoss;
                		} catch(ex) {
                			remainderTotal = 0;
                		}
                		remainderTotal = remainderTotal.toFixed(2);
                		return remainderTotal;
                	}
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewReportStockReport("+rowObject.ARTICLE_ID+", "+rowObject.FORMAT_ID+", \""+rowObject.ARTICLE_NAME+"\", \""+rowObject.FORMAT_NAME+"\")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "700px",
                //width: "100%",
                rowNum: 20,
                pager: "#stockReportReportDatagridpager"
            });
   		});
   		
   		function viewReportStockReport(articleId, formatId, articleName, formatName) {
   			var url = getRoot() + "com/linuslan/oa/workflow/report/StockDetailReportList.jsp?articleId="+articleId+"&formatId="+formatId;
   			addTab(articleName+"-"+formatName+"出入库明细账", "stockDetail_articleId"+articleId+"_formatId"+formatId, url);
   			/*reportStockReportDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "60%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(),
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
	        });*/
   		}
   		
   		function initSelect_stockReportReport(id) {
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
   	   							initOptions_stockReportReport(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化报销查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_stockReportReport(data, selector) {
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
   				data: $("#search_form_stockReport_report_list").serialize(),
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
