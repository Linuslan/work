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
   	<form id="auditReimburseForm" action="" class="form-horizontal">
   		<input class="reimburseId" type="hidden" name="reimburse.id" id="viewReimburseId" value="${reimburse.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#reimburse" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#reimburseAuditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="reimburse">
   					<div class="box-body padding-bottom5 bottom-dotted-border">
   						<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">申请人：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${reimburse.userName }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">归属部门：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${reimburse.userDeptName }
							</div>
						</div>
			    		<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">出款公司：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${reimburse.companyName }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">编号：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${reimburse.serialNo }
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2 col-sm-4 control-label">报销部门：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${reimburse.reimburseDeptName }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="departmentId" class="col-md-2 col-sm-4 control-label">收款人：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${reimburse.receiver }
							</div>
						</div>
						<div class="form-group">
							<label for="moneyDate" class="col-md-2 col-sm-4 control-label">开户行</label>
							<div class="col-md-3 col-sm-8 left-label">
								${reimburse.bank }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="groupId" class="col-md-2 col-sm-4 control-label">账号：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${reimburse.bankNo }
							</div>
						</div>
						<div class="form-group">
							<label for="orderNo" class="col-md-2 col-sm-4 control-label">总金额：</label>
							<div class="col-md-3 col-sm-8 left-label">
								${reimburse.totalMoney }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label"></label>
							<div class="col-md-3 col-sm-8 left-label">
							</div>
						</div>
			    	</div>
			    	<div class="box-body">
			    		<table id="reimburseContentDatagrid_audit"></table>
			    	</div>
   				</div>
   				<div class="tab-pane" id="reimburseAuditorLogs">
   					<div class="box box-solid">
		   				
		   				<div class="box-body">
		   					<div class="form-group bottom-dotted-border padding-bottom5">
								<label for="leaderGroup" class="col-md-1 col-sm-1 control-label">意见：</label>
								<div class="col-md-10 col-sm-10">
									<textarea name="opinion" class="form-control" rows="3" placeholder="请输入"></textarea>
								</div>
							</div>
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="reimburseauditorlogsdatagrid"></table>
	   							<div id="reimburseauditorlogsdatagridpager"></div>
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
			setTimeout("generateReimburseAuditLogGrid()", 0);
			setTimeout("generateAuditReimburseContenGrid()", 0);
   		});
   		
   		function generateReimburseAuditLogGrid() {
   			var id = $("#auditReimburseForm").find("input.reimburseId").val();
   			$("#reimburseauditorlogsdatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=reimburse&wfId="+id,
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
                pager: "#reimburseauditorlogsdatagridpager"
            });
   		}
   		
   		function generateAuditReimburseContenGrid() {
   			var id = $("#auditReimburseForm").find("input.reimburseId").val();
   			$("#reimburseContentDatagrid_audit").jqGrid({
   				url: getRoot() + "workflow/reimburse/queryContentsByReimburseId.action?reimburse.id="+id,
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "reimburseClassName": "d", "reimburseClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "费用产生时间", name: "remittanceDate", editable: false, width: 150, align: "center"
                }, {
                	label: "报销类别", name: "reimburseClassId", editable: false, width: 200, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		return rowObject.reimburseClassName;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return selected[rowId].id;
                	}
                }, {
                	label: "付款项目", name: "content", width: 300, align: "left", editable: false,
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.content);
                	}
                }, {
                	label: "金额", name: "money", width: 110, align: "center", editable: false
                }, {
                	label: "备注", name: "remark", width: 300, align: "left", editable: false,
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.remark);
                	}
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20
            });
   		}
   	</script>
  </body>
</html>
