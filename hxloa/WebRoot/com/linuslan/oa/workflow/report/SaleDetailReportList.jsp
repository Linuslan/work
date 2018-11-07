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
    <div id="sale_detail_report_list">
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<input id="sale_detail_report_customerId" name="paramMap.customerId" type="hidden" value="${param.customerId }" />
   				<form id="search_form_saleDetailReport_report_list" action="" class="form-horizontal">
   					
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">日期：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.order_date_startDate" type="text" class="form-control pull-right" data-inputmask="'mask': '9999-99-99'" data-mask>
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.companyId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">产品：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.articleId" class="form-control select2">
									
								</select>
							</div>
							<div class="col-md-3 col-sm-3">
								<button type="button" class="btn btn-info btn-sm margin-right5" data-toggle="tooltip" title="查询" id="searchSaleDetailReportReportList"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="重置" id="resetSearchSaleDetailReportReportList"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="box-body">
   					<div class="nav-tabs-top-border">
			   			<ul class="nav nav-tabs">
			   				<li class="active">
			   					<a href="#sale_detail_report" data-toggle="tab">销售明细</a>
			   				</li>
			   				<li>
			   					<a href="#payback_detail_report" data-toggle="tab">回款明细</a>
			   				</li>
			   			</ul>
			   			<div class="tab-content">
			   				<div class="active tab-pane" id="sale_detail_report">
				   				<div class="box-body">
				   					<table id="saleDetailReportReportDatagrid"></table>
				   					<div id="saleDetailReportReportDatagridpager"></div>
				   				</div>
				   			</div>
				   			<div class="tab-pane" id="payback_detail_report">
				   				<div class="box-body">
				   					<table id="paybackDetailReportReportDatagrid"></table>
				   					<div id="paybackDetailReportReportDatagridpager"></div>
				   				</div>
				   			</div>
				   		</div>
				   	</div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var reportSaleReportGrid;
   		var reportPaybackDetailGrid;
   		var reportSaleReportDialog;
   		$(function() {
   			$("#sale_detail_report_list a[data-toggle='tab']").on("shown.bs.tab", function (e) {
   				var href = $(this).attr("href");
   				var id = href.substring(1);
   				$("#"+id).find("table").setGridWidth($("#"+id).width()*0.99);
   			});
			$("#search_form_saleDetailReport_report_list [data-mask]").inputmask();
			initSelect_saleDetailReportReport("search_form_saleDetailReport_report_list");
			$("#searchSaleDetailReportReportList").click(function() {
   				$("#saleDetailReportReportDatagrid").setGridParam({
   					postData:parsePostData("search_form_saleDetailReport_report_list")
   				}).trigger("reloadGrid");
   				$("#paybackDetailReportReportDatagrid").setGridParam({
   					postData:parsePostData("search_form_saleDetailReport_report_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchSaleDetailReportReportList").click(function() {
   				$("#search_form_saleDetailReport_report_list")[0].reset();
   				$("#search_form_saleDetailReport_report_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			
   			reportSaleReportGrid = $("#saleDetailReportReportDatagrid").jqGrid({
                url: getRoot() + "workflow/report/querySaleDetailReportPage.action?paramMap.customerId="+$("#sale_detail_report_customerId").val(),
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "日期", name: "ORDER_DATE", width: 100, align: "center"
                }, {
                	label: "年", name: "YEAR", width: 200, align: "center"
                }, {
                	label: "月", name: "MONTH", width: 200, align: "center"
                }, {
                	label: "摘要", name: "ARTICLE_NAME", width: 200, align: "center"
                }, {
                	label: "规格", name: "FORMAT_NAME", width: 200, align: "center"
                }, {
                	label: "数量", name: "QUANTITY", width: 200, align: "center", summaryType:"sum", summaryTpl : "共({0})"
                }, {
                	label: "单价", name: "PRICE", width: 200, align: "center"
                }, {
                	label: "销售金额", name: "TOTAL_AMOUNT", width: 150, align: "center", summaryType:"sum", summaryTpl : "共({0})"
                }/*, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewReportSaleDetailReport("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+")");
   						return buttons;
                	}
                }*/],
				viewrecords: true,
                height: "700px",
                rowNum: 20,
                pager: "#saleDetailReportReportDatagridpager",
                grouping: true,
			   	groupingView : {
			   		groupField : ["YEAR", "MONTH"],
			   		groupColumnShow : [false, false],
			   		groupText : ['<b>{0}</b>'],
			   		groupCollapse : false,
					groupOrder: ['asc', 'asc'],
					groupSummary : [true, true]
			   	}
            });
            
            reportPaybackDetailGrid = $("#paybackDetailReportReportDatagrid").jqGrid({
                url: getRoot() + "workflow/report/queryPaybackDetailReportPage.action?paramMap.customerId="+$("#sale_detail_report_customerId").val(),
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "日期", name: "ORDER_DATE", width: 100, align: "center"
                }, {
                	label: "年", name: "YEAR", width: 200, align: "center"
                }, {
                	label: "月", name: "MONTH", width: 200, align: "center"
                }, {
                	label: "归属公司", name: "COMPANY_NAME", width: 200, align: "center"
                }, {
                	label: "总销售额", name: "SALE_AMOUNT", width: 200, align: "center"
                }, {
                	label: "回款", name: "PAYBACK_AMOUNT", width: 200, align: "center", summaryType:"sum", summaryTpl : "共({0})"
                }, {
                	label: "总回款", name: "TOTAL_PAYBACK_AMOUNT", width: 200, align: "center"
                }, {
                	label: "未回款", name: "unpayback", width: 150, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		var saleAmount = rowObject.SALE_AMOUNT;
                		var totalPayback = rowObject.TOTAL_PAYBACK_AMOUNT;
                		var unpayback = saleAmount - totalPayback;
                		return unpayback.toFixed(2);
                	}
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewReportSaleDetailReport("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "700px",
                rowNum: 20,
                pager: "#paybackDetailReportReportDatagridpager",
                grouping: true,
			   	groupingView : {
			   		groupField : ["YEAR", "MONTH"],
			   		groupColumnShow : [false, false],
			   		groupText : ['<b>{0}</b>'],
			   		groupCollapse : false,
					groupOrder: ['asc', 'asc'],
					groupSummary : [true, true]
			   	}
            });
   		});
   		
   		function initSaleDetailGrid() {
   			$("#saleDetailReportReportDatagrid").setGridParam({
   				url: getRoot() + "workflow/report/querySaleDetailReportPage.action",
   				postData:$("#search_form_saleDetailReport_report_list").serialize()
   			}).trigger("reloadGrid");
   			
   			$("#paybackDetailReportReportDatagrid").setGridParam({
   				url: getRoot() + "workflow/report/queryPaybackDetailReportPage.action",
   				postData:$("#search_form_saleDetailReport_report_list").serialize()
   			}).trigger("reloadGrid");
   		}
   		
   		function viewReportSaleDetailReport(id, passBtn, rejectBtn) {
   			reportSaleReportDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "60%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/report/querySaleDetailReportPage.action?returnType=audit&saleReport.id="+id),
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
   		
   		function initSelect_saleDetailReportReport(id) {
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
   	   							initOptions_saleDetailReportReport(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化报销查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_saleDetailReportReport(data, selector) {
   			if(data && data.length > 0 && selector) {
   				var option = document.createElement("option");
   				option.value = "";
   				option.text = "请选择";
   				$(selector).append(option);
   				for(var i = 0;i < data.length; i ++) {
   					var opData = data[i];
   					var option = document.createElement("option");
   					$(selector).append(option);
   					if(selector.indexOf("customerId") >= 0) {
   						var value = opData.ID+"_"+opData.TYPE;
   						var text = opData.NAME+"-"+(opData.TYPE == "customer" ? "联拓" : "华夏蓝");
   						option.value = value;
   						option.text = text;
   					} else {
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
   		}
   		
   		/*function sumReportPage_saleReport() {
   			$.ajax({
   				url: getRoot() + "workflow/saleReport/sumReportPage.action",
   				data: $("#search_form_saleDetailReport_report_list").serialize(),
   				type: "POST",
   				success: function(data) {
   					try {
   						if(data) {
	   						var json = eval("("+data+")");
	   						for(var key in json) {
	   							var sumAmount = json[key];
	   							sumAmount = sumAmount.toFixed(2);
	   							$("#sumReportPage_saleReport ."+key).html(sumAmount);
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
