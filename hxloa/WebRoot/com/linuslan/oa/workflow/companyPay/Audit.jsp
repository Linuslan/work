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
   	<form id="auditCompanyPayForm" action="" class="form-horizontal">
   		<input type="hidden" name="companyPay.id" id="viewCompanyPayId" value="${companyPay.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#companyPay" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#auditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="companyPay">
   					<div class="box-body">
			    		<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">归属公司：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${companyPay.companyName }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">编号：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${companyPay.serialNo }
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2 col-sm-4 control-label">费用承担部门：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${companyPay.payDeptName }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="departmentId" class="col-md-2 col-sm-4 control-label">费用承担公司：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${companyPay.payCompanyName }
							</div>
						</div>
						<div class="form-group">
							<label for="moneyDate" class="col-md-2 col-sm-4 control-label">费用产生时间：</label>
							<div class="col-md-3 col-sm-8 left-label">
								<fmt:formatDate value="${companyPay.moneyDate }" pattern="yyyy-MM-dd"/>
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="groupId" class="col-md-2 col-sm-4 control-label">金额：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${companyPay.money }
							</div>
						</div>
						<div class="form-group">
							<label for="orderNo" class="col-md-2 col-sm-4 control-label">付款方式：</label>
							<div class="col-md-3 col-sm-8 left-label">
								<%--${companyPay.payType == 0 ? "银行转账" : "现金" } --%>
								${companyPay.payTypeName }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">收款方：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${companyPay.receiver }
							</div>
						</div>
						<div class="form-group">
							<label for="orderNo" class="col-md-2 col-sm-4 control-label">开户行：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${companyPay.bank }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">银行账号：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${companyPay.bankNo }
							</div>
						</div>
						<div class="form-group">
							<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">付款项目：</label>
							<div class="col-md-9 col-sm-8 left-label textarea">
								${companyPay.content }
							</div>
						</div>
						<div class="form-group">
							<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">备注：</label>
							<div class="col-md-9 col-sm-8 left-label textarea">
								${companyPay.remark }
							</div>
						</div>
			    	</div>
   				</div>
   				<div class="tab-pane" id="auditorLogs">
   					<div class="box box-solid">
		   				
		   				<div class="box-body">
		   					<div class="form-group bottom-dotted-border padding-bottom5">
								<label for="leaderGroup" class="col-md-1 col-sm-1 control-label">意见：</label>
								<div class="col-md-10 col-sm-10">
									<textarea name="opinion" class="form-control" rows="3" placeholder="请输入"></textarea>
								</div>
							</div>
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="auditorlogsdatagrid"></table>
	   							<div id="auditorlogsdatagridpager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
   		</div>
    	
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			
   			$("#auditCompanyPayForm .textarea").each(function() {
   				var str = decode($(this).html());
   				$(this).html(str);
   			});
   			
   			$("#auditorlogsdatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=companyPay&wfId="+$("#viewCompanyPayId").val(),
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
                pager: "#auditorlogsdatagridpager"
            });
   		});
   	</script>
  </body>
</html>
