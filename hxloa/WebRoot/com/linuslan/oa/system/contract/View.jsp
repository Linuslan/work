<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/c" prefix="c" %>
<%@ taglib uri="/fmt" prefix="fmt" %>
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
   	<form id="viewContractForm" action="" class="form-horizontal" method="POST" enctype="multipart/form-data">
   		<input type="hidden" value="${contract.id }" name="contract.id" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">归属部门：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${contract.departmentName }
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">签约人：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${contract.contractorName }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">合同编号：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${contract.contractNum }
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">合作单位：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${contract.name }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">签约时间：</label>
				<div class="col-md-3 col-sm-8 left-label">
					<fmt:formatDate value="${contract.signDate }" pattern="yyyy-MM-dd" />
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">复印件份数：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${contract.copyNum }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">开始时间：</label>
				<div class="col-md-3 col-sm-8 left-label">
					<fmt:formatDate value="${contract.startDate }" pattern="yyyy-MM-dd" />
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label">结束时间：</label>
				<div class="col-md-3 col-sm-8 left-label">
					<fmt:formatDate value="${contract.endDate }" pattern="yyyy-MM-dd" />
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">自动延续：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${contract.isReload == 1 ? "是" : "否" }
				</div>
				<div class="col-md-1 col-sm-12"></div>
				<label for="name" class="col-md-2 col-sm-4 control-label"></label>
				<div class="col-md-3 col-sm-8 left-label">
					
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2 col-sm-4 control-label">附件列表：</label>
				<div class="col-md-9 col-sm-8 left-label">
					
				</div>
			</div>
			<c:forEach items="${uploadFiles }" var="uploadFile">
				<div class="form-group padding-bottom5 contract_file">
					<label class="col-md-2-sm col-sm-4 control-label">
						
					</label>
					<div class="col-md-11-sm col-sm-8 align-left padding-top7">
						<a onclick="contractDownloadFile_view(${uploadFile.id })" href="javascript: void(0);">${uploadFile.fileName }</a>
					</div>
				</div>
			</c:forEach>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">合作简介：</label>
				<div class="col-md-9 col-sm-8 left-label">
					${contract.content }
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2 col-sm-4 control-label">备注：</label>
				<div class="col-md-9 col-sm-8 left-label">
					${contract.info }
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			
   		});
   		
   		function contractDownloadFile_view(id) {
			if(id && "" != $.trim(id)) {
	  			var form = $("<form>");
				form.attr("style","display:none");  
				form.attr("target","");  
				form.attr("method","post");  
				form.attr("action",getRoot() + "sys/uploadFile/download.action?id="+id);  
				$("body").append(form);
				form.submit();
				form.remove();
	  		} else {
	  			BootstrapDialog.danger("附件ID无效！");
	  		}
		}
   	</script>
  </body>
</html>
