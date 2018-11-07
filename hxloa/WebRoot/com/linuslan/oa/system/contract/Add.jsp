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
    
    <title>My JSP 'Add.jsp' starting page</title>
    
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
   	<form id="addContractForm" action="<%=basePath %>sys/contract/add.action" class="form-horizontal" method="POST" enctype="multipart/form-data">
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">归属部门：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="departmentTree"></div>
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">签约人：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<select name="contract.contractor" class="form-control select2" style="width: 100%;">
						<option value="">请选择</option>
						<c:forEach items="${users }" var="user">
							<option value="${user.id }">${user.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">合同编号：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="contract.contractNum" type="text" class="form-control">
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">合作单位：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="contract.name" type="text" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">签约时间：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="contract.signDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">复印件份数：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<input name="contract.copyNum" type="number" class="form-control" value="0">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">开始时间：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="contract.startDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">结束时间：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input name="contract.endDate" type="text" value="" readonly="readonly" class="form-control pull-right date showText" id="text">
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">自动延续：</label>
				<div class="col-md-3 col-sm-8 left-label">
					<input name="contract.isReload" type="checkbox" class="minimal" value="1" checked>&nbsp;是
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label"></label>
				<div class="col-md-3 col-sm-8 left-label">
					
				</div>
			</div>
			<div class="form-group" id="addContractFile_add">
				<label for="name" class="col-md-2 col-sm-4 control-label">附件列表：</label>
				<div class="col-md-9 col-sm-8 left-label">
					<button type="button" class="btn btn-success btn-sm" data-toggle="tooltip" title="添加附件" id="addContractFile"><i class="fa fa-fw fa-plus"></i>添加附件</button>
				</div>
			</div>
			<div class="form-group padding-bottom5 contract_file">
				<label class="col-md-2-sm col-sm-4 control-label">
					<button type="button" class="btn btn-danger btn-xs" data-toggle="tooltip" title="删除附件" onclick="delContractFile_add(this)"><i class="fa fa-trash"></i></button>
					&nbsp;
				</label>
				<div class="col-md-11-sm col-sm-8 address no-padding">
					<input type="file" name="files" class="form-control files" />
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">合作简介：</label>
				<div class="col-md-9 col-sm-8 no-padding">
					<textarea name="contract.content" class="form-control" rows="3"></textarea>
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">备注：</label>
				<div class="col-md-9 col-sm-8 no-padding">
					<textarea name="contract.info" class="form-control" rows="3"></textarea>
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addContractForm").find(".select2").select2();
   			$("#addContractForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#addContractForm").find("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
	        
	        $("#addContractForm").find(".departmentTree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "contract.departmentId",
				pidField: "pid"
			});
	        
	        $("#addContractFile").click(function() {
   				var html = "<div class=\"form-group padding-bottom5 contract_file\">"
							+"<label class=\"col-md-2-sm col-sm-4 control-label\">"+createBtn("删除", "btn-danger btn-xs", "fa-trash", "delContractFile_add(this)")+"</label>"
							+"<div class=\"col-md-11-sm col-sm-8 address no-padding\">"
								+"<input name=\"files\" type=\"file\" class=\"form-control files\">"
							+"</div>"
						+"</div>";
				$(".contract_file:last").after(html);
   			});
   		});
   		
   		function delContractFile_add(obj, id) {
			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定删除吗？",
	            type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	            	
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	                	if(id && ""!=$.trim(id) && isNumber(id)) {
	                		$.ajax({
		   						url: getRoot() + "sys/uploadFile/del.action",
			    				data: "uploadFile.id="+id,
			    				type: "POST",
			    				beforeSend: function() {
			    					//mask($("#userdatagrid").parent());
			    				},
			    				complete: function() {
			    					//unmask($("#userdatagrid").parent());
			    				},
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						$(obj).parent().parent().remove();
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$(obj).parent().parent().remove();
		            	}
	   				}
	            }
   			});
		}
   	</script>
  </body>
</html>
