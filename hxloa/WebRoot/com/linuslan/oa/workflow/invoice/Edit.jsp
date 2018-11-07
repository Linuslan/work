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
    
    <title>编辑开票申请</title>
    
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
   	<form id="updateInvoiceForm" action="" class="form-horizontal">
   		<input type="hidden" name="invoice.id" value="${invoice.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#invoice" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#invoiceAuditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="invoice">
   					<div class="box-body">
			    		<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">开票公司：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<select class="select2" name="invoice.companyId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${companys }" var="company">
										<option value="${company.id }" ${invoice.companyId == company.id ? "selected='selecte'" : "" }>${company.name }</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">编号：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<input name="invoice.serialNo" type="text" value="${invoice.serialNo }" readonly="readonly" class="form-control required" id="text">
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2 col-sm-4 control-label">收入归属部门：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<div class="incomeDept" value="${invoice.incomeDeptId }" text="${invoice.incomeDeptName }"></div>
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="departmentId" class="col-md-2 col-sm-4 control-label">开票类型：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<select class="select2" name="invoice.invoiceType" style="width: 100%;">
									<option value="">请选择</option>
									<c:forEach var="dictionary" items="${dictionarys }">
										<option value="${dictionary.id }" ${dictionary.id == invoice.invoiceType ? "selected='selected'" : "" }>${dictionary.text }</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="moneyDate" class="col-md-2 col-sm-4 control-label">开票时间：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" value="<fmt:formatDate value="${invoice.invoiceDate }" pattern="yyyy-MM-dd"/>" name="invoice.invoiceDate" type="text" class="form-control pull-right date">
								</div>
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="groupId" class="col-md-2 col-sm-4 control-label">开票金额：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<input name="invoice.invoiceMoney" type="number" class="form-control" value="${invoice.invoiceMoney }">
							</div>
						</div>
						<div class="form-group">
							<label for="orderNo" class="col-md-2 col-sm-4 control-label">应回款金额：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<input name="invoice.supposedMoney" type="number" class="form-control" value="${invoice.supposedMoney }">
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">预计到款日：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" value="<fmt:formatDate value="${invoice.planRestreamDate }" pattern="yyyy-MM-dd"/>" name="invoice.planRestreamDate" type="text" class="form-control pull-right date">
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="orderNo" class="col-md-2 col-sm-4 control-label">纳税人识别号：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<div class="input-group">
									<input name="invoice.taxPayerId" value="${invoice.taxPayerId }" type="text" class="form-control required" id="text">
									<span class="input-group-btn">
										<button class="btn btn-success btn-flat" type="button" onclick="selectInvoiceAccount_edit()"><i class="fa fa-search"></i></button>
									</span>
								</div>
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">电话：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<input name="invoice.phone" type="tel" value="${invoice.phone }" class="form-control required" id="text">
							</div>
						</div>
						<div class="form-group">
							<label for="orderNo" class="col-md-2 col-sm-4 control-label">开户行：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<input name="invoice.bank" type="text" value="${invoice.bank }" class="form-control required" id="text">
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">银行账号：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<input name="invoice.bankNo" type="text" value="${invoice.bankNo }" class="form-control required" id="text">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-4 control-label">地址：</label>
							<div class="col-md-9 col-sm-8 no-padding">
								<input name="invoice.address" type="text" value="${invoice.address }" class="form-control required" id="text">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-4 control-label">开票名称：</label>
							<div class="col-md-9 col-sm-8 no-padding">
								<input name="invoice.title" type="text" value="${invoice.title }" class="form-control required" id="text">
							</div>
						</div>
						<div class="form-group">
							<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">开票项目：</label>
							<div class="col-md-9 col-sm-8 no-padding">
								<textarea name="invoice.content" class="form-control" rows="3" placeholder="请输入">${invoice.content }</textarea>
							</div>
						</div>
						<div class="form-group">
							<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">备注：</label>
							<div class="col-md-9 col-sm-8 no-padding">
								<textarea name="invoice.remark" class="form-control" rows="3" placeholder="请输入">${invoice.remark }</textarea>
							</div>
						</div>
			    	</div>
   				</div>
   				<div class="tab-pane" id="invoiceAuditorLogs">
   					<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="editinvoiceauditorlogsdatagrid"></table>
	   							<div id="editinvoiceauditorlogsdatagridpager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			setTimeout("generateEditInvoiceLogGrid()", 0);
   			$("#updateInvoiceForm").find(".select2").select2();
   			$("#updateInvoiceForm").find(".incomeDept").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "invoice.incomeDeptId",
				pidField: "pid"
			});
			$("#updateInvoiceForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   		});
   		
   		function generateEditInvoiceLogGrid() {
   			$("#invoiceauditorlogsdatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=invoice&wfId="+$("#editReimburseId").val(),
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
                pager: "#invoiceeditlogsdatagridpager"
            });
   		}
   		
   		function selectInvoiceAccount_edit() {
   			BootstrapDialog.show({
			    title: "选择账户",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/account/queryByUserId.action"),
			    draggable: true,
			    size: BootstrapDialog.SIZE_WIDE,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [{
			    	label: "确定",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		var $button = this;
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			var $select = form.find("select");
			    			var $option = $select.find("option:selected");
			    			var receiver = $option.attr("receiver");
			    			var taxPayerId = $option.attr("taxPayerId");
			    			var address = $option.attr("address");
			    			var cellphone = $option.attr("cellphone");
			    			var bankNo = $option.attr("bankNo");
			    			var bankName = $option.attr("bankName");
			    			$("#updateInvoiceForm").find("input[name='invoice.taxPayerId']").val(taxPayerId);
			    			$("#updateInvoiceForm").find("input[name='invoice.phone']").val(cellphone);
			    			$("#updateInvoiceForm").find("input[name='invoice.bank']").val(bankName);
			    			$("#updateInvoiceForm").find("input[name='invoice.bankNo']").val(bankNo);
			    			$("#updateInvoiceForm").find("input[name='invoice.address']").val(address);
			    			dialog.close();
			    		} catch(e) {
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-danger",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
   		}
   	</script>
  </body>
</html>
