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
   				<form id="search_form_saleReport_report_list" action="" class="form-horizontal">
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
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属客户：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.customerId" class="form-control select2">
									
								</select>
							</div>
							<div class="col-md-3 col-sm-3">
								<button type="button" class="btn btn-info btn-sm margin-right5" data-toggle="tooltip" title="查询" id="searchSaleReportReportList"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="重置" id="resetSearchSaleReportReportList"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				<!-- 暂时先将总计隐藏不删，如果确实需要时，再使用 -->
   				<!-- <div class="box-body top-dotted-border bottom-dotted-border form-horizontal" id="sumReportPage_saleReport">
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
   					<table id="saleReportReportDatagrid"></table>
   					<div id="saleReportReportDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var reportSaleReportGrid;
   		var reportSaleReportDialog;
   		$(function() {
   			//sumReportPage_saleReport();
			$("#search_form_saleReport_report_list [data-mask]").inputmask();
			initSelect_saleReportReport("search_form_saleReport_report_list");
			$("#searchSaleReportReportList").click(function() {
   				$("#saleReportReportDatagrid").setGridParam({
   					postData:parsePostData("search_form_saleReport_report_list")
   				}).trigger("reloadGrid");
   				sumReportPage_saleReport();
   			});
   			
   			$("#resetSearchSaleReportReportList").click(function() {
   				$("#search_form_saleReport_report_list")[0].reset();
   				$("#search_form_saleReport_report_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			
   			reportSaleReportGrid = $("#saleReportReportDatagrid").jqGrid({
                url: getRoot() + "workflow/report/querySaleReportPage.action",
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "客户名称", name: "NAME", width: 100, align: "center"
                }, {
                	label: "归属公司", name: "COMPANY_NAME", width: 200, align: "center"
                }, {
                	label: "总销售额", name: "TOTAL_SALE_AMOUNT", width: 200, align: "center"
                }, {
                	label: "上月销售额", name: "LAST_SALE_AMOUNT", width: 200, align: "center"
                }, {
                	label: "本月销售额", name: "MONTH_SALE_AMOUNT", width: 200, align: "center"
                }, {
                	label: "上月已回款", name: "LAST_MONTH_BACK_AMOUNT", width: 200, align: "center"
                }, {
                	label: "本月已回款", name: "MONTH_BACK_AMOUNT", width: 200, align: "center"
                }, {
                	label: "本月应回款", name: "remainder", width: 150, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		//本月应回款=总销售额-总回款
                		var remainder = 0;
                		try {
                			var totalBackAmount = rowObject.TOTAL_BACK_AMOUNT;
                			var totalSaleAmount = rowObject.TOTAL_SALE_AMOUNT;
                			remainder = totalSaleAmount - totalBackAmount
                		} catch(ex) {
                			remainder = 0;
                		}
                		remainder = remainder.toFixed(2);
                		return remainder;
                	}
                }, {
                	label: "总回款", name: "TOTAL_BACK_AMOUNT", width: 200, align: "center"
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewReportSaleReport("+rowObject.ID+", \""+rowObject.NAME+"\")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "700px",
                //width: "100%",
                rowNum: 20,
                pager: "#saleReportReportDatagridpager"
            });
   		});
   		
   		function viewReportSaleReport(id, name) {
   			var url = getRoot() + "com/linuslan/oa/workflow/report/SaleDetailReportList.jsp?customerId="+id;
   			addTab(name+"销售明细账", "abc", url);
   			/*reportSaleReportDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "90%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/workflow/report/SaleDetailReportList.jsp?customerId="+id),
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
   		
   		function initSelect_saleReportReport(id) {
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
   	   							initOptions_saleReportReport(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化报销查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_saleReportReport(data, selector) {
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
   				data: $("#search_form_saleReport_report_list").serialize(),
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
