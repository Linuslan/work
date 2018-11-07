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
   	<form id="auditSalaryForm" action="" class="form-horizontal">
   		<input class="salaryId" type="hidden" name="salary.id" id="viewSalaryId" value="${salary.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#salaryAuditList" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#salaryAuditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="salaryAuditList">
			    	<div class="box-body">
			    		<table id="salaryContentDatagrid_audit"></table>
			    	</div>
   				</div>
   				<div class="tab-pane" id="salaryAuditorLogs">
   					<div class="box box-solid">
		   				
		   				<div class="box-body">
		   					<div class="form-group bottom-dotted-border padding-bottom5">
								<label for="leaderGroup" class="col-md-1 col-sm-1 control-label">意见：</label>
								<div class="col-md-10 col-sm-10">
									<textarea name="opinion" class="form-control" rows="3" placeholder="请输入"></textarea>
								</div>
							</div>
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="salaryauditorlogsdatagrid"></table>
	   							<div id="salaryauditorlogsdatagridpager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
   		</div>
    	
   	</form>
   	<script type="text/javascript">
   		$(function() {
            //需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateSalaryAuditLogGrid()", 0);
			setTimeout("generateAuditSalaryContenGrid()", 0);
   		});
   		
   		function generateSalaryAuditLogGrid() {
   			var id = $("#auditSalaryForm").find("input.salaryId").val();
   			$("#salaryauditorlogsdatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=salary&wfId="+id,
                mtype: "POST",
                shrinkToFit: false,
                height: "600px",
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "ID", name: "id", hidden: true, key: true
                }, {
                	label: "审核人", name: "auditorName", width: 150, align: "center"
                }, {
                	label: "审核时间", name: "auditDate", width: 200, align: "center"
                }, {
                	label: "意见", name: "opinion", width: 400, align: "center"
                }, {
                	label: "操作类型", name: "passType", width: 100, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		if(rowObject.passType == "0") {
                			return "通过";
                		} else {
                			return "退回";
                		}
                	}
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20,
                pager: "#salaryauditorlogsdatagridpager"
            });
   		}
   		
   		function generateAuditSalaryContenGrid() {
   			var id = $("#auditSalaryForm").find("input.salaryId").val();
   			$("#salaryContentDatagrid_audit").jqGrid({
   				url: getRoot() + "workflow/salary/queryContentsBySalaryId.action?salary.id="+id,
                mtype: "POST",
                height: "600px",
                shrinkToFit: false,
				styleUI : "Bootstrap",
                datatype: "json",
                loadComplete: function() {
                	countSalary_audit();
                },
                gridComplete: function() {
                	$("#salaryContentDatagrid_audit").setGridWidth($("#salaryAuditList").width()*0.99);
                	$("#salaryContentDatagrid_audit").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
                	$("#salaryContentDatagrid_audit").jqGrid("setFrozenColumns");
                	
                	setTimeout("hackHeight(\"#salaryContentDatagrid_audit\")", 0);
                	
                	//hackHeight("#salarydatagrid");
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
                rowNum: 1000/*,
                subGrid: true,
                subGridOptions: {
                	expandOnLoad: true
                },
                subGridRowColapsed: function(subgrid_id, rowid) {
                	return false;
                },
                subGridBeforeExpand: function(subgrid_id, rowid) {
                	if(rowid.indexOf("_sum") >= 0) {
                		return false;
                	} else {
                		return true;
                	}
                },
                subGridRowExpanded: function(subgrid_id, rowid) {
                	var rowData = $("#salaryContentDatagrid_audit").getRowData(rowid);
                	var id = rowData.id;
                	if(id) {
                		var tableId = subgrid_id+"_table";
                		var html = "<table style='width: 100%' class='contentOpinion'><tr><td class='align-right' style='width: 10%;'>审核意见：</td><td><input name='contentId' type='hidden' value='"+id+"' /><textarea name='contentOpinion' style='width: 100%' rows='5'></textarea></td></tr></table>";
                		html = html + "<table id='"+tableId+"' class='scroll'></table>";
                		var subgrid = $("#"+subgrid_id).html(html);
                		$("#"+tableId).jqGrid({
                			url: getRoot() + "workflow/salary/queryContentOpinionsByContentId?salaryContent.id="+id,
                			datatype: "json",
                			height: "100%",
                			shrinkToFit: true,
			                autowidth: true,
			                scrollrows: false,
			                scroll: false,
                			colModel: [{
			                	label: "ID", name: "id", hidden: true
			                }, {
			                	label: "审核人", name: "userName", width: 150, align: "center"
			                }, {
			                	label: "审核意见", name: "opinion", width: 400, align: "center"
			                }, {
			                	label: "审核时间", name: "createDate", width: 150, align: "center"
			                }]
                		});
                	}
                }*/
            });
            
            $("#salaryContentDatagrid_audit").jqGrid("setGroupHeaders", {
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
		function countSalary_audit() {
			var rowDatas = $("#salaryContentDatagrid_audit").getRowData();
			var rowIds = $("#salaryContentDatagrid_audit").getDataIDs();
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
	           	$("#salaryContentDatagrid_audit").addRowData(rowId, {
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
	           	$("#salaryContentDatagrid_audit").addRowData(rowId, {
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
				$("#salaryContentDatagrid_audit").setRowData(sumDataId, {
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
	           	$("#salaryContentDatagrid_audit").addRowData(rowId, {
	           		id: rowId,
					tax: "福利合计",
					actualTotalSalary: supposedTotal,
					telCharge: benefitTotal,
					companySocialInsurance: companyInsuranceTotal
				});
			} else {
				$("#salaryContentDatagrid_audit").setRowData(benefitSumId, {
					tax: "福利合计",
					actualTotalSalary: supposedTotal,
					telCharge: benefitTotal,
					companySocialInsurance: companyInsuranceTotal
				});
			}
			mergeCell_salary_audit("salaryContentDatagrid_audit", benefitSumId, "telCharge", "housingSubsidy", 4, benefitTotal);
			mergeCell_salary_audit("salaryContentDatagrid_audit", benefitSumId, "companySocialInsurance", "companyHealthInsurance", 2, companyInsuranceTotal)
			
			if(!totalSumId) {
				rowId = Math.random();
	           	rowId = rowId+"_total_sum";
	           	totalSumId = rowId;
	           	$("#salaryContentDatagrid_audit").addRowData(rowId, {
	           		id: rowId,
					tax: "工资总计",
					actualTotalSalary: totalSalary
				});
			} else {
				$("#salaryContentDatagrid_audit").setRowData(totalSumId, {
					tax: "工资总计",
					actualTotalSalary: totalSalary
				});
			}
			//mergeCell_salary_audit("salaryContentDatagrid_audit", totalSumId, "actualTotalSalary", "companyHealthInsurance", 7, totalSalary);
			return sumHeaderId;
		}
		
		function mergeCell_salary_audit(gridId, rowid, name, endName, size, value) {
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
