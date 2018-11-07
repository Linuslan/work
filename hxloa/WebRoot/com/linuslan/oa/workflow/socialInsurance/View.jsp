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
   	<form id="viewSocialInsuranceForm" action="" class="form-horizontal">
   		<input class="socialInsuranceId" type="hidden" name="socialInsurance.id" id="viewSocialInsuranceId" value="${socialInsurance.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#socialInsuranceView" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#socialInsuranceViewAuditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="socialInsuranceView">
   					<div class="box-body padding-bottom5 bottom-dotted-border">
			    		<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">时间：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${socialInsurance.year }-${socialInsurance.month}
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label"></label>
							<div class="col-md-3 col-sm-8 no-padding">
								
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">备注：</label>
							<div class="col-md-9 col-sm-8 left-label">
								${socialInsurance.remark }
							</div>
						</div>
			    	</div>
			    	<div class="box-body">
			    		<table id="socialInsuranceContentDatagrid_view"></table>
			    		<div id="socialInsuranceContentDatagrid_viewPager"></div>
			    	</div>
   				</div>
   				<div class="tab-pane" id="socialInsuranceViewAuditorLogs">
   					<div class="box box-solid">
		   				
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="socialInsuranceviewauditorlogsdatagrid"></table>
	   							<div id="socialInsuranceviewauditorlogsdatagridpager"></div>
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
			setTimeout("generateSocialInsuranceAuditLogGrid()", 0);
			setTimeout("generateAuditSocialInsuranceContenGrid()", 0);
   		});
   		
   		function generateSocialInsuranceAuditLogGrid() {
   			var id = $("#viewSocialInsuranceForm").find("input.socialInsuranceId").val();
   			$("#socialInsuranceviewauditorlogsdatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=socialInsurance&wfId="+id,
                mtype: "POST",
                shrinkToFit: true,
                /*autowidth: true,
                scrollrows: false,
                scroll: false,*/
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
                pager: "#socialInsuranceviewauditorlogsdatagridpager"
            });
   		}
   		
   		function generateAuditSocialInsuranceContenGrid() {
   			var id = $("#viewSocialInsuranceForm").find("input.socialInsuranceId").val();
   			$("#socialInsuranceContentDatagrid_view").jqGrid({
   				url: getRoot() + "workflow/socialInsurance/queryContentsBySocialInsuranceId.action?socialInsurance.id="+id,
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                footerrow: true,
                gridComplete: function() {
                	//$("#socialInsuranceContentDatagrid_view").setGridWidth($("#socialInsuranceView").width()*0.99);
                	var companyEndowmentSum = $(this).getCol("companyEndowmentCharge", false, "sum");
                	var companyUnemploymentSum = $(this).getCol("companyUnemploymentCharge", false, "sum");
                	var companyEmploymentInjurySum = $(this).getCol("companyEmploymentInjuryCharge", false, "sum");
                	var companySum = $(this).getCol("companySum", false, "sum");
                	var userEndowmentSum = $(this).getCol("userEndowmentCharge", false, "sum");
                	var userUnemploymentSum = $(this).getCol("userUnemploymentCharge", false, "sum");
                	var userEmploymentInjurySum = $(this).getCol("userEmploymentInjuryCharge", false, "sum");
                	var userSum = $(this).getCol("userSum", false, "sum");
                	var totalSum = $(this).getCol("totalSum", false, "sum");
                	$(this).footerData("set", {
                		"company": "合计",
                		"companyEndowmentCharge": companyEndowmentSum.toFixed(2),
                		"companyUnemploymentCharge": companyUnemploymentSum.toFixed(2),
                		"companyEmploymentInjuryCharge": companyEmploymentInjurySum.toFixed(2),
                		"companySum": companySum.toFixed(2),
                		"userEndowmentCharge": userEndowmentSum.toFixed(2),
                		"userUnemploymentCharge": userUnemploymentSum.toFixed(2),
                		"userEmploymentInjuryCharge": userEmploymentInjurySum.toFixed(2),
                		"userSum": userSum.toFixed(2),
                		"totalSum": totalSum.toFixed(2)
                	});
                },
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "序号", name: "orderNo", width: 60, align: "center"
                }, {
                	label: "姓名", name: "userName", width: 100, align: "center"
                }, {
                	label: "身份证号", name: "idNo", width: 150, align: "center"
                }, {
                	label: "归属公司", name: "company", width: 150, align: "center"
                }, {//公司基本养老保险
                	label: "缴费基数", name: "companyEndowmentBasicCharge", width: 110, align: "center"
                }, {//公司基本养老保险
                	label: "缴费比率", name: "companyEndowmentRate", width: 110, align: "center"
                }, {//公司基本养老保险
                	label: "缴纳金额", name: "companyEndowmentCharge", width: 110, align: "center"
                }, {//公司失业保险
                	label: "缴费基数", name: "companyUnemploymentBasicCharge", width: 110, align: "center"
                }, {//公司失业保险
                	label: "缴费比率", name: "companyUnemploymentRate", width: 110, align: "center"
                }, {//公司失业保险
                	label: "缴纳金额", name: "companyUnemploymentCharge", width: 110, align: "center"
                }, {//公司工伤保险
                	label: "缴费基数", name: "companyEmploymentInjuryBasicCharge", width: 110, align: "center"
                }, {//公司工伤保险
                	label: "缴费比率", name: "companyEmploymentInjuryRate", width: 110, align: "center"
                }, {//公司工伤保险
                	label: "缴费金额", name: "companyEmploymentInjuryCharge", width: 110, align: "center"
                }, {
                	label: "单位合计", name: "companySum", width: 110, align: "center"
                }, {//个人基本养老保险
                	label: "缴费基数", name: "userEndowmentBasicCharge", width: 110, align: "center"
                }, {//个人基本养老保险
                	label: "缴费比例", name: "userEndowmentRate", width: 110, align: "center"
                }, {//个人基本养老保险
                	label: "缴纳金额", name: "userEndowmentCharge", width: 110, align: "center"
                }, {//个人失业保险
                	label: "缴费基数", name: "userUnemploymentBasicCharge", width: 110, align: "center"
                }, {//个人失业保险
                	label: "缴费比率", name: "userUnemploymentRate", width: 110, align: "center"
                }, {//个人失业保险
                	label: "缴纳金额", name: "userUnemploymentCharge", width: 110, align: "center"
                }, {//个人工伤保险
                	label: "缴纳金额", name: "userEmploymentInjuryCharge", width: 110, align: "center"
                }, {
                	label: "个人合计", name: "userSum", width: 110, align: "center"
                }, {
                	label: "合计", name: "totalSum", width: 110, align: "center"
                }, {
                	label: "备注", name: "remark", width: 200, align: "left"
                }],
				viewrecords: true,
                height: getHeight(600)+"px",
                //height: "100%",
                rowNum: 100
            });
            
            $("#socialInsuranceContentDatagrid_view").jqGrid("setGroupHeaders", {
   				useColSpanStyle: true,
   				groupHeaders:[{
   					startColumnName: "companyEndowmentBasicCharge", numberOfColumns: 3, titleText: "公司基本养老保险"
   				}, {
   					startColumnName: "companyUnemploymentBasicCharge", numberOfColumns: 3, titleText: "公司失业保险"
   				}, {
   					startColumnName: "companyEmploymentInjuryBasicCharge", numberOfColumns: 3, titleText: "公司工伤保险"
   				}, {
   					startColumnName: "userEndowmentBasicCharge", numberOfColumns: 3, titleText: "个人基本养老保险"
   				}, {
   					startColumnName: "userUnemploymentBasicCharge", numberOfColumns: 3, titleText: "个人失业保险"
   				}, {
   					startColumnName: "userEmploymentInjuryCharge", numberOfColumns: 1, titleText: "个人工伤保险"
   				}]
   			});
   		}
   	</script>
  </body>
</html>
