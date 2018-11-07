<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/c" %>
<%@ taglib prefix="fmt" uri="/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP "Add.jsp" starting page</title>
    
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
   	<div id="usersalary_list">
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<form id="search_form_user_slary_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">日期：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.date" type="text" class="form-control pull-right date">
								</div>
							</div>
							<div class="col-md-3 col-sm-3">
								<button type="button" class="btn btn-info btn-sm margin-right5" data-toggle="tooltip" title="查询" id="searchUserSalary"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="重置" id="resetSearchUserSalary"><i class="fa fa-fw fa-undo"></i>重置</button>
								
							</div>
						</div>
   					</div>
   				</form>
		    	<div class="box-body">
		    		<table id="userSalaryContentDatagrid_view"></table>
		    	</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		$(function() {
            //需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateUserSalaryContenGrid()", 0);
            
			$("#search_form_user_slary_list input.date").datepicker({
   				startView: 1,
   				maxViewMode: 1,
   				minViewMode:1,
   				forceParse: false,
   				language: "zh-CN",
   				format: "yyyy-mm"
   			});
   			
   			$("#searchUserSalary").click(function() {
   				var year = $("#search_form_user_slary_list input[name='paramMap.date']").val().split("-")[0];
	  			var month = $("#search_form_user_slary_list input[name='paramMap.date']").val().split("-")[1]
	  			if(!year) {
	  				var currentDate = new Date();
	  				year = currentDate.getFullYear();
	  			}
	  			if(!month) {
	  				var currentDate = new Date();
	  				month = currentDate.getMonth() - 1;
	  			}
   				$("#userSalaryContentDatagrid_view").setGridParam({
   					postData: {
   						"year": year,
   						"month": month
   					}
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchUserSalary").click(function() {
   				$("#search_form_user_slary_list")[0].reset();
   				$("#search_form_user_slary_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   		});
   		
   		function generateUserSalaryContenGrid() {
   			$("#userSalaryContentDatagrid_view").jqGrid({
   				url: getRoot() + "workflow/salary/queryContentByUserId.action",
                mtype: "POST",
                shrinkToFit: false,
				styleUI : "Bootstrap",
                datatype: "json",
                loadComplete: function() {
                	//countSalary_view();
                },
                gridComplete: function() {
                	$("#userSalaryContentDatagrid_view").setGridWidth($("#usersalary_list").width()*0.99);
                	$("#userSalaryContentDatagrid_view").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
                	$("#userSalaryContentDatagrid_view").jqGrid("setFrozenColumns");
                	
                	//setTimeout("hackHeight(\"#userSalaryContentDatagrid_view\")", 0);
                	
                	hackHeight("#salarydatagrid");
                },
                colModel: [{
                	label: "姓名", name: "userName", width: 100, align: "center", frozen: true
                }, {
                	label: "id", name: "id", hidden: true
                }, {
                	label: "应出勤", name: "supposedDutyDay", width: 100, align: "center"
                }, {
                	label: "天", name: "actualDutyDay", width: 100, align: "center"
                }, {
                	label: "时", name: "actualDutyHour", width: 100, align: "center"
                }, {
                	label: "基本工资", name: "basicSalary", width: 100, align: "center"
                }, {
                	label: "岗位工资", name: "postSalary", width: 100, align: "center"
                }, {
                	label: "绩效标准", name: "achievementSalary", width: 100, align: "center"
                }, {
                	label: "绩效分数", name: "achievementScore", width: 100, align: "center"
                }, {
                	label: "实发绩效", name: "actualAchievementSalary", width: 100, align: "center"
                }, {
                	label: "抽成", name: "commission", width: 100, align: "center"
                }, {
                	label: "满勤奖", name: "fullAttendanceAward", width: 100, align: "center"
                }, {
                	label: "加班费", name: "overtimePay", width: 100, align: "center"
                }, {
                	label: "工龄工资", name: "seniorityPay", width: 100, align: "center"
                }, {
                	label: "病假", name: "sickLeaveDeduct", width: 100, align: "center"
                }, {
                	label: "事假", name: "affairLeaveDeduct", width: 100, align: "center"
                }, {
                	label: "迟到", name: "lateDeduct", width: 100, align: "center"
                }, {
                	label: "处罚金", name: "punishDeduct", width: 100, align: "center"
                }, {
                	label: "应发合计", name: "supposedTotalSalary", width: 100, align: "center"
                }, {
                	label: "社保", name: "socialInsurance", width: 100, align: "center"
                }, {
                	label: "医保", name: "healthInsurance", width: 100, align: "center"
                }, {
                	label: "总计", name: "totalInsurance", width: 100, align: "center"
                }, {
                	label: "话补", name: "telCharge", width: 100, align: "center"
                }, {
                	label: "餐补", name: "mealSubsidy", width: 100, align: "center"
                }, {
                	label: "车补", name: "travelAllowance", width: 100, align: "center"
                }, {
                	label: "住房补贴", name: "housingSubsidy", width: 100, align: "center"
                }, {
                	label: "总计", name: "totalSubsidy", width: 100, align: "center"
                }, {
                	label: "其他", name: "other", width: 100, align: "center"
                }, {
                	label: "税前工资", name: "pretaxSalary", width: 100, align: "center"
                }, {
                	label: "个税", name: "tax", width: 100, align: "center"
                }, {
                	label: "实发工资", name: "actualTotalSalary", width: 100, align: "center"
                }, {
                	label: "社保", name: "companySocialInsurance", width: 100, align: "center"
                }, {
                	label: "医保", name: "companyHealthInsurance", width: 100, align: "center"
                }, {
                	label: "备注", name: "info", width: 350, align: "center"
                }],
				viewrecords: true,
                height: "600px",
                //width: "100%",
                rowNum: 1000
            });
            
            $("#userSalaryContentDatagrid_view").jqGrid("setGroupHeaders", {
   				useColSpanStyle: true,
   				groupHeaders:[{
   					startColumnName: "actualDutyDay", numberOfColumns: 2, titleText: "实出勤"
   				}, {
   					startColumnName: "sickLeaveDeduct", numberOfColumns: 3, titleText: "出勤扣款"
   				}, {
   					startColumnName: "socialInsurance", numberOfColumns: 3, titleText: "个人应扣款"
   				}, {
   					startColumnName: "telCharge", numberOfColumns: 5, titleText: "补贴"
   				}, {
   					startColumnName: "companySocialInsurance", numberOfColumns: 2, titleText: "公司投保"
   				}]
   			});
   		}
   		
   		/**
		 * 统计工资
		 */
		function countSalary_view() {
			var rowDatas = $("#userSalaryContentDatagrid_view").getRowData();
			var rowIds = $("#userSalaryContentDatagrid_view").getDataIDs();
			var sumHeaderId = undefined;
			var sumDataId = undefined;
			var benefitSumId = undefined;
			var totalSumId = undefined;
			for(var i = 0; i < rowIds.length; i ++) {
				var rowId = rowIds[i];
				if(rowId.indexOf("_sum_header") >= 0) {
					sumHeaderId = rowId;
				}
				if(rowId.indexOf("_sum_data") >= 0) {
					sumDataId = rowId;
				}
				if(rowId.indexOf("_benefit_sum") >= 0) {
					benefitSumId = rowId;
				}
				if(rowId.indexOf("_total_sum") >= 0) {
					totalSumId = rowId;
				}
			}
			
			var supposedTotal = 0;
			var actualTotal = 0;
			var telChargeTotal = 0;
			var mealSubsidyTotal = 0;
			var travelAllowanceTotal = 0;
			var housingSubsidyTotal = 0;
			var companySocialInsuranceTotal = 0;
			var companyHealthInsuranceTotal = 0;
			var benefitTotal = 0;
			var companyInsuranceTotal = 0;
			var totalSalary = 0;
			var commissionSum = 0;
			var socialInsuranceSum = 0;	//个人社保合计
			var healthInsuranceSum = 0;	//个人医保合计
			var insuranceSum = 0;	//个人医社保总计
			var taxSum = 0;
			for(var i = 0; i < rowDatas.length; i ++) {
				var rowData = rowDatas[i];
				if(rowData.id.indexOf("_sum_header") >= 0 || rowData.id.indexOf("_sum_data") >= 0
					|| rowData.id.indexOf("_benefit_sum") >= 0 || rowData.id.indexOf("_total_sum") >= 0) {
					continue;
				}
				supposedTotal += parseFloat(rowData.supposedTotalSalary);
				actualTotal += parseFloat(rowData.actualTotalSalary);
				telChargeTotal += parseFloat(rowData.telCharge);
				mealSubsidyTotal += parseFloat(rowData.mealSubsidy);
				travelAllowanceTotal += parseFloat(rowData.travelAllowance);
				housingSubsidyTotal += parseFloat(rowData.housingSubsidy);
				companySocialInsuranceTotal += parseFloat(rowData.companySocialInsurance);
				companyHealthInsuranceTotal += parseFloat(rowData.companyHealthInsurance);
				commissionSum += parseFloat(rowData.commission);
				socialInsuranceSum += parseFloat(rowData.socialInsurance);
				healthInsuranceSum += parseFloat(rowData.healthInsurance);
				taxSum += parseFloat(rowData.tax);
			}
			benefitTotal = telChargeTotal + mealSubsidyTotal + travelAllowanceTotal + housingSubsidyTotal;
			companyInsuranceTotal = companySocialInsuranceTotal + companyHealthInsuranceTotal;
			totalSalary = supposedTotal + benefitTotal + companyInsuranceTotal;
			
			supposedTotal = supposedTotal.toFixed(2);
			actualTotal = actualTotal.toFixed(2);
			telChargeTotal = telChargeTotal.toFixed(2);
			mealSubsidyTotal = mealSubsidyTotal.toFixed(2);
			travelAllowanceTotal = travelAllowanceTotal.toFixed(2);
			housingSubsidyTotal = housingSubsidyTotal.toFixed(2);
			companySocialInsuranceTotal = companySocialInsuranceTotal.toFixed(2);
			companyHealthInsuranceTotal = companyHealthInsuranceTotal.toFixed(2);
			benefitTotal = benefitTotal.toFixed(2);
			companyInsuranceTotal = companyInsuranceTotal.toFixed(2);
			totalSalary = totalSalary.toFixed(2);
			commissionSum = commissionSum.toFixed(2);
			socialInsuranceSum = socialInsuranceSum.toFixed(2);
			healthInsuranceSum = healthInsuranceSum.toFixed(2);
			insuranceSum = parseFloat(socialInsuranceSum) + parseFloat(healthInsuranceSum);
			insuranceSum = insuranceSum.toFixed(2);
			taxSum = taxSum.toFixed(2);
			if(!sumHeaderId) {
				var rowId = Math.random();
				rowId=rowId+"_sum_header";
				sumHeaderId = rowId;
				srcRowId = rowId;
	           	$("#userSalaryContentDatagrid_view").addRowData(rowId, {
	           		id: rowId,
	           		userId: "",
	           		supposedDutyDay: "",
	           		actualDutyDay: "",
	           		actualDutyHour: "",
	           		basicSalary: "",
	           		achievementSalary: "",
	           		achievementScore: "",
	           		actualAchievementSalary: "",
	           		commission: "抽成合计",
	           		fullAttendanceAward: "",
	           		overtimePay: "",
	           		seniorityPay: "",
	           		sickLeaveDeduct: "",
	           		affairLeaveDeduct: "",
	           		lateDeduct: "",
	           		punishDeduct: "",
	           		supposedTotalSalary: "应发合计",
	           		socialInsurance: "个人社保合计",
	           		healthInsurance: "个人医保合计",
	           		totalInsurance: "总计",
	           		other: "",
	           		actualTotalSalary: "实发合计",
	           		telCharge: "话补",
	           		mealSubsidy: "餐补",
	           		travelAllowance: "车补",
	           		housingSubsidy: "住房补贴",
	           		companySocialInsurance: "社保",
	           		companyHealthInsurance: "医保",
	           		info: "",
	           		operationCell: "",
	           		tax: "个税合计"
	           	});
			}
			if(!sumDataId) {
				rowId = Math.random();
	           	rowId = rowId+"_sum_data"
	           	$("#userSalaryContentDatagrid_view").addRowData(rowId, {
	           		id: rowId,
					supposedTotalSalary: supposedTotal,
					actualTotalSalary: actualTotal,
					telCharge: telChargeTotal,
					mealSubsidy: mealSubsidyTotal,
					travelAllowance: travelAllowanceTotal,
					housingSubsidy: housingSubsidyTotal,
					companySocialInsurance: companySocialInsuranceTotal,
					companyHealthInsurance: companyHealthInsuranceTotal,
					operationCell: "",
					commission: commissionSum,
					socialInsurance: socialInsuranceSum,
					healthInsurance: healthInsuranceSum,
					totalInsurance: insuranceSum,
					tax: taxSum
				});
			} else {
				$("#userSalaryContentDatagrid_view").setRowData(sumDataId, {
					supposedTotalSalary: supposedTotal,
					actualTotalSalary: actualTotal,
					telCharge: telChargeTotal,
					mealSubsidy: mealSubsidyTotal,
					travelAllowance: travelAllowanceTotal,
					housingSubsidy: housingSubsidyTotal,
					companySocialInsurance: companySocialInsuranceTotal,
					companyHealthInsurance: companyHealthInsuranceTotal,
					operationCell: "",
					commission: commissionSum,
					socialInsurance: socialInsuranceSum,
					healthInsurance: healthInsuranceSum,
					totalInsurance: insuranceSum,
					tax: taxSum
				});
			}
			if(!benefitSumId) {
				rowId = Math.random();
	           	rowId = rowId+"_benefit_sum";
	           	benefitSumId = rowId;
	           	$("#userSalaryContentDatagrid_view").addRowData(rowId, {
	           		id: rowId,
					other: "福利合计",
					actualTotalSalary: supposedTotal,
					telCharge: benefitTotal,
					companySocialInsurance: companyInsuranceTotal
				});
			} else {
				$("#userSalaryContentDatagrid_view").setRowData(benefitSumId, {
					other: "福利合计",
					actualTotalSalary: supposedTotal,
					telCharge: benefitTotal,
					companySocialInsurance: companyInsuranceTotal
				});
			}
			mergeCell_usersalary_view("userSalaryContentDatagrid_view", benefitSumId, "telCharge", "housingSubsidy", 4, benefitTotal);
			mergeCell_usersalary_view("userSalaryContentDatagrid_view", benefitSumId, "companySocialInsurance", "companyHealthInsurance", 2, companyInsuranceTotal)
			
			if(!totalSumId) {
				rowId = Math.random();
	           	rowId = rowId+"_total_sum";
	           	totalSumId = rowId;
	           	$("#userSalaryContentDatagrid_view").addRowData(rowId, {
	           		id: rowId,
					other: "工资总计",
					actualTotalSalary: totalSalary
				});
			} else {
				$("#userSalaryContentDatagrid_view").setRowData(totalSumId, {
					other: "工资总计",
					actualTotalSalary: totalSalary
				});
			}
			mergeCell_usersalary_view("userSalaryContentDatagrid_view", totalSumId, "actualTotalSalary", "companyHealthInsurance", 7, totalSalary);
			return sumHeaderId;
		}
		
		function mergeCell_usersalary_view(gridId, rowid, name, endName, size, value) {
			var $startTd = $("#"+gridId).find("tr[id='"+rowid+"']").find("td[aria-describedby='"+gridId+"_"+name+"']");
			var startIndex = $startTd.index();
			
			var $endTd = $("#"+gridId).find("tr[id='"+rowid+"']").find("td[aria-describedby='"+gridId+"_"+endName+"']");
			var endIndex = $endTd.index();
			var len = endIndex - startIndex;
			if($startTd.length > 0 && $endTd.length > 0) {
				if(len > 0) {
					for(var i = 0; i < len; i ++) {
						$startTd.next().remove();
					}
				}
			}
			$("#"+gridId).find("tr[id='"+rowid+"']").find("td[aria-describedby='"+gridId+"_"+name+"']").attr("colspan", size);
			$("#"+gridId).find("tr[id='"+rowid+"']").find("td[aria-describedby='"+gridId+"_"+name+"']").html(value);
		}
   	</script>
  </body>
</html>
