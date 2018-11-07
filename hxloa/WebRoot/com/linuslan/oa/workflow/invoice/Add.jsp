<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新增开票</title>
    
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
    	<div class="box-body">
    		<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">开票公司：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<select class="select2" name="invoice.companyId" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${companys }" var="company">
							<option value="${company.id }">${company.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 control-label">编号：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="invoice.serialNo" type="text" value="${serialNo }" readonly="readonly" class="form-control required" id="text">
				</div>
			</div>
			<div class="form-group">
				<label for="departmentId" class="col-md-2 col-sm-4 control-label">收入归属部门：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="incomeDept"></div>
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="departmentId" class="col-md-2 col-sm-4 control-label">开票类型：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<select class="select2" name="invoice.invoiceType" style="width: 100%;">
						<option value="">请选择</option>
						<c:forEach var="dictionary" items="${dictionarys }">
							<option value="${dictionary.id }">${dictionary.text }</option>
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
						<input readonly="readonly" name="invoice.invoiceDate" type="text" class="form-control pull-right date">
					</div>
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="groupId" class="col-md-2 col-sm-4 control-label">开票金额：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="invoice.invoiceMoney" type="number" class="form-control showText" value="0">
				</div>
			</div>
			<div class="form-group">
				<label for="orderNo" class="col-md-2 col-sm-4 control-label">应回款金额：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="invoice.supposedMoney" type="number" class="form-control" value="0">
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 control-label">预计到款日：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input readonly="readonly" name="invoice.planRestreamDate" type="text" class="form-control pull-right date">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="orderNo" class="col-md-2 col-sm-4 control-label">纳税人识别号：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="input-group">
						<input name="invoice.taxPayerId" type="text" class="form-control required" id="text">
						<span class="input-group-btn">
							<button class="btn btn-success btn-flat" type="button" onclick="selectInvoiceAccount_add()"><i class="fa fa-search"></i></button>
						</span>
					</div>
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 control-label">电话：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="invoice.phone" type="tel" class="form-control required" id="text">
				</div>
			</div>
			<div class="form-group">
				<label for="orderNo" class="col-md-2 col-sm-4 control-label">开户行：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="invoice.bank" type="text" class="form-control required" id="text">
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 control-label">银行账号：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="invoice.bankNo" type="text" class="form-control required" id="text">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 col-sm-4 control-label">地址：</label>
				<div class="col-md-9 col-sm-8 no-padding">
					<input name="invoice.address" type="text" class="form-control required" id="text">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 col-sm-4 control-label">开票名称：</label>
				<div class="col-md-9 col-sm-8 no-padding">
					<input name="invoice.title" type="text" class="form-control required" id="text">
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">开票项目：</label>
				<div class="col-md-9 col-sm-8 no-padding">
					<textarea name="invoice.content" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">备注：</label>
				<div class="col-md-9 col-sm-8 no-padding">
					<textarea name="invoice.remark" class="form-control" rows="3" placeholder="请输入"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addInvoiceForm").find(".select2").select2();
   			$("#addInvoiceForm").find(".incomeDept").combotree({
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
			$("#addInvoiceForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   		});
   		
   		function selectInvoiceAccount_add() {
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
			    			$("#addInvoiceForm").find("input[name='invoice.taxPayerId']").val(taxPayerId);
			    			$("#addInvoiceForm").find("input[name='invoice.phone']").val(cellphone);
			    			$("#addInvoiceForm").find("input[name='invoice.bank']").val(bankName);
			    			$("#addInvoiceForm").find("input[name='invoice.bankNo']").val(bankNo);
			    			$("#addInvoiceForm").find("input[name='invoice.address']").val(address);
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
