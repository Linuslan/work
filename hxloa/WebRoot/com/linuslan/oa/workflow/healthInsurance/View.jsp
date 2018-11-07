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
   	<form id="viewHealthInsuranceForm" action="" class="form-horizontal">
   		<input class="healthInsuranceId" type="hidden" name="healthInsurance.id" id="viewHealthInsuranceId" value="${healthInsurance.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#healthInsuranceView" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#healthInsuranceViewAuditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="healthInsuranceView">
   					<div class="box-body padding-bottom5 bottom-dotted-border">
			    		<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">时间：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${healthInsurance.year }-${healthInsurance.month}
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label"></label>
							<div class="col-md-3 col-sm-8 no-padding">
								
							</div>
						</div>
			    	</div>
			    	<div class="box-body">
			    		<table id="healthInsuranceContentDatagrid_view"></table>
			    	</div>
   				</div>
   				<div class="tab-pane" id="healthInsuranceViewAuditorLogs">
   					<div class="box box-solid">
		   				
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="healthInsuranceviewauditorlogsdatagrid"></table>
	   							<div id="healthInsuranceviewauditorlogsdatagridpager"></div>
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
			setTimeout("generateHealthInsuranceAuditLogGrid()", 0);
			setTimeout("generateAuditHealthInsuranceContenGrid()", 0);
   		});
   		
   		function generateHealthInsuranceAuditLogGrid() {
   			var id = $("#viewHealthInsuranceForm").find("input.healthInsuranceId").val();
   			$("#healthInsuranceviewauditorlogsdatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=healthInsurance&wfId="+id,
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
                pager: "#healthInsuranceviewauditorlogsdatagridpager"
            });
   		}
   		
   		function generateAuditHealthInsuranceContenGrid() {
   			var id = $("#viewHealthInsuranceForm").find("input.healthInsuranceId").val();
   			$("#healthInsuranceContentDatagrid_view").jqGrid({
   				url: getRoot() + "workflow/healthInsurance/queryContentsByHealthInsuranceId.action?healthInsurance.id="+id,
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                footerrow: true,
                gridComplete: function() {
                	var companyMedicalSum = $(this).getCol("companyMedicalCharge", false, "sum");
                	var companyMeternitySum = $(this).getCol("companyMeternityCharge", false, "sum");
                	var companyIllnessSum = $(this).getCol("companyIllnessCharge", false, "sum");
                	var companySum = $(this).getCol("companySum", false, "sum");
                	var userMedicalSum = $(this).getCol("userMedicalCharge", false, "sum");
                	var userMeternitySum = $(this).getCol("userMeternityCharge", false, "sum");
                	var userSum = $(this).getCol("userSum", false, "sum");
                	var totalSum = $(this).getCol("totalSum", false, "sum");
                	$(this).footerData("set", {
                		"company": "合计",
                		"companyMedicalCharge": companyMedicalSum.toFixed(2),
                		"companyMeternityCharge": companyMeternitySum.toFixed(2),
                		"companyIllnessCharge": companyIllnessSum.toFixed(2),
                		"companySum": companySum.toFixed(2),
                		"userMedicalCharge": userMedicalSum.toFixed(2),
                		"userMeternityCharge": userMeternitySum.toFixed(2),
                		"userSum": userSum.toFixed(2),
                		"totalSum": totalSum.toFixed(2)
                	});
                },
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "healthInsuranceClassName": "d", "healthInsuranceClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "序号", name: "orderNo", width: 100, align: "center"
                }, {
                	label: "姓名", name: "userName", width: 100, align: "center"
                }, {
                	label: "身份证号", name: "idNo", width: 150, align: "center"
                }, {
                	label: "归属公司", name: "company", width: 110, align: "center"
                }, {//公司医疗保险
                	label: "缴费基数", name: "companyMedicalBasicCharge", width: 110, align: "center"
                }, {//公司医疗保险
                	label: "缴费比率", name: "companyMedicalRate", width: 110, align: "center"
                }, {//公司医疗保险
                	label: "缴纳金额", name: "companyMedicalCharge", width: 110, align: "center"
                }, {//公司生育保险
                	label: "缴费基数", name: "companyMeternityBasicCharge", width: 110, align: "center"
                }, {//公司生育保险
                	label: "缴费比率", name: "companyMeternityRate", width: 110, align: "center"
                }, {//公司生育保险
                	label: "缴纳金额", name: "companyMeternityCharge", width: 110, align: "center"
                }, {
                	label: "大病补充", name: "companyIllnessCharge", width: 110, align: "center"
                }, {
                	label: "单位缴费合计", name: "companySum", width: 110, align: "center"
                }, {//个人医疗保险
                	label: "缴费基数", name: "userMedicalBasicCharge", width: 110, align: "center"
                }, {//个人医疗保险
                	label: "缴费比率", name: "userMedicalRate", width: 110, align: "center"
                }, {//个人医疗保险
                	label: "缴纳金额", name: "userMedicalCharge", width: 110, align: "center"
                }, {//个人生育保险
                	label: "缴费基数", name: "userMeternityBasicCharge", width: 110, align: "center"
                }, {//个人生育保险
                	label: "缴费比率", name: "userMeternityRate", width: 110, align: "center"
                }, {//个人生育保险
                	label: "缴纳金额", name: "userMeternityCharge", width: 110, align: "center"
                }, {
                	label: "个人缴费合计", name: "userSum", width: 110, align: "center"
                }, {
                	label: "合计", name: "totalSum", width: 110, align: "center"
                }, {
                	label: "备注", name: "remark", width: 200, align: "left"
                }],
				viewrecords: true,
                height: getHeight(600)+"px",
                rowNum: 100
            });
            
            $("#healthInsuranceContentDatagrid_view").jqGrid("setGroupHeaders", {
   				useColSpanStyle: true,
   				groupHeaders:[{
   					startColumnName: "companyMedicalBasicCharge", numberOfColumns: 3, titleText: "公司医疗保险"
   				}, {
   					startColumnName: "companyMeternityBasicCharge", numberOfColumns: 3, titleText: "公司生育保险"
   				}, {
   					startColumnName: "userMedicalBasicCharge", numberOfColumns: 3, titleText: "个人医疗保险"
   				}, {
   					startColumnName: "userMeternityBasicCharge", numberOfColumns: 3, titleText: "个人生育保险"
   				}]
   			});
   		}
   	</script>
  </body>
</html>
