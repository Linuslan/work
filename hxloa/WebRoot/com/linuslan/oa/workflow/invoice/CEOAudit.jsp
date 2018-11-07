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
    
    <title>总经理审核</title>
    
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
   	<form id="addInvoiceForm" action="" class="form-horizontal">
   		<input type="hidden" name="invoice.id" id="viewInvoiceId" value="${invoice.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#invoice" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#auditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="invoice">
   					<div class="box-body">
			    		<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">开票公司：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${invoice.companyName }
							</div>
							<div class="col-md-1 col-sm-12 left-label"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">编号：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${invoice.serialNo }
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2 col-sm-4 control-label">收入归属部门：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${invoice.incomeDeptName }
							</div>
							<div class="col-md-1 col-sm-12 left-label"></div>
							<label for="departmentId" class="col-md-2 col-sm-4 control-label">开票类型：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${invoice.invoiceTypeName }
							</div>
						</div>
						<div class="form-group">
							<label for="moneyDate" class="col-md-2 col-sm-4 control-label">开票时间：</label>
							<div class="col-md-3 col-sm-8 left-label">
								<fmt:formatDate value="${invoice.invoiceDate }" pattern="yyyy-MM-dd"/>
							</div>
							<div class="col-md-1 col-sm-12 left-label"></div>
							<label for="groupId" class="col-md-2 col-sm-4 control-label">开票金额：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${invoice.invoiceMoney }
							</div>
						</div>
						<div class="form-group">
							<label for="orderNo" class="col-md-2 col-sm-4 control-label">应回款金额：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${invoice.supposedMoney }
							</div>
							<div class="col-md-1 col-sm-12 left-label"></div>
							<label for="orderNo" class="col-md-2 col-sm-4 control-label">实回款金额：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${invoice.actualMoney }
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">预计到款日：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${invoice.planRestreamDate }" pattern="yyyy-MM-dd"/>
							</div>
							<div class="col-md-1 col-sm-12 left-label"></div>
							<label for="orderNo" class="col-md-2 col-sm-4 control-label">纳税人识别号：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${invoice.taxPayerId }
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">电话：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${invoice.phone }
							</div>
							<div class="col-md-1 col-sm-12 left-label"></div>
							<label for="orderNo" class="col-md-2 col-sm-4 control-label">开户行：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${invoice.bank }
							</div>
						</div>
						<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">银行账号：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${invoice.bankNo }
							</div>
							<div class="col-md-1 col-sm-12 left-label"></div>
							<label class="col-md-2 col-sm-4 control-label">状态：</label>
							<div class="col-md-3 col-sm-8 left-label">
								<c:choose>
	    							<c:when test="${invoice.invoiceStatus == 0 }">
	    								已回款
	    							</c:when>
	    							<c:when test="${invoice.invoiceStatus == 1 }">
	    								部分回款
	    							</c:when>
	    							<c:when test="${invoice.invoiceStatus == 2 }">
	    								未回款
	    							</c:when>
	    							<c:when test="${invoice.invoiceStatus == 5 }">
	    								未开票作废
	    							</c:when>
	    							<c:when test="${invoice.invoiceStatus == 6 }">
	    								已开票作废
	    							</c:when>
	    							<c:otherwise>
	    								未回款
	    							</c:otherwise>
	    						</c:choose>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-4 control-label">地址：</label>
							<div class="col-md-10 col-sm-8 left-label">
								${invoice.address }
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-4 control-label">财务开票时间：</label>
							<div class="col-md-3 col-sm-8 left-label">
								<fmt:formatDate value="${invoice.conformInvoiceDate }" pattern="yyyy-MM-dd"/> （${invoice.invoiceUserName == null ? "无" : invoice.invoiceUserName }）
							</div>
							<div class="col-md-1 col-sm-12 left-label"></div>
							<label class="col-md-2 col-sm-4 control-label">回款时间：</label>
							<div class="col-md-3 col-sm-8 left-label">
								<fmt:formatDate value="${invoice.confirmRestreamDate }" pattern="yyyy-MM-dd"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-4 control-label">开票名称：</label>
							<div class="col-md-10 col-sm-8 left-label">
								${invoice.title }
						</div>
						<div class="form-group">
							<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">开票项目：</label>
							<div class="col-md-9 col-sm-8 left-label">
								${invoice.content }
							</div>
						</div>
						<div class="form-group">
							<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">备注：</label>
							<div class="col-md-9 col-sm-8 left-label">
								${invoice.remark }
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
			   					<table id="invoiceauditorlogsdatagrid"></table>
	   							<div id="invoiceauditorlogsdatagridpager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
   		</div>
    	
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#invoiceauditorlogsdatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=invoice&wfId="+$("#viewInvoiceId").val(),
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
                pager: "#invoiceauditorlogsdatagridpager"
            });
   		});
   	</script>
  </body>
</html>
