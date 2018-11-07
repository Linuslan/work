<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
   	<form id="viewCapitalForm" action="" class="form-horizontal">
   		<input type="hidden" value="${capital.id }" name="capital.id" />
    	<div class="box-body">
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">固资类别：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.className }
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">编号：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.serial }
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">名称：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.name }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">型号：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.model }
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">厂家：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.shopName }
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">资产存放地：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.address }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">归属部门：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.department }
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">计量单位：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.unit }
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">状态：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.state == 2 ? "正常" : "作废" }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">借用时间：</label>
				<div class="col-md-2 col-sm-8 left-label">
					<fmt:formatDate value="${capital.borrowDate }" pattern="yyyy-MM-dd"/>
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">借用单位：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.borrowDepartment }
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">使用方：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.borrowUser }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">购置时间：</label>
				<div class="col-md-2 col-sm-8 left-label">
					<fmt:formatDate value="${capital.buyDate }" pattern="yyyy-MM-dd"/>
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">购置价格：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.buyMoney }
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">折旧月数：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.depreciationYear }
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-md-2-sm col-sm-4 control-label">月折旧额：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.depreciationMoney }
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">至今折旧额：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.depreciation }
				</div>
				<label for="name" class="col-md-2-sm col-sm-4 control-label">净额：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${capital.netamount }
				</div>
			</div>
			<div class="form-group">
				<label for="leaderGroup" class="col-md-2-sm col-sm-4 control-label">备注：</label>
				<div class="col-md-11-sm col-sm-8 left-label">
					${capital.info }
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			
   		});
   	</script>
  </body>
</html>
