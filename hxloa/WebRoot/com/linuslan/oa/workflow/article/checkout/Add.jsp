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
    
    <title>新增商品</title>
    
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
   	<form id="addCheckoutArticleForm" action="" class="form-horizontal">
    	<div class="box-body padding-bottom5 bottom-dotted-border" >
    		<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">商品名称：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<select class="select2" name="checkoutArticle.checkinArticleId" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${articles }" var="article">
							<option value="${article.id }">${article.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 control-label">商品编号：</label>
				<div class="col-md-3 col-sm-8 totalScore no-padding">
					<input name="checkoutArticle.serialNo" type="text" value="" class="form-control required" id="text">
				</div>
			</div>
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">归属客户：</label>
				<div class="col-md-3 col-sm-8 totalScore no-padding">
					<select class="select2" name="checkoutArticle.customerId" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${customers }" var="customer">
							<option value="${customer.id }">${customer.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 control-label">排序号：</label>
				<div class="col-md-3 col-sm-8 totalScore no-padding">
					<input name="checkoutArticle.orderNo" type="number" value="0" class="form-control required" id="text">
				</div>
			</div>
    	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addCheckoutArticleForm").find(".select2").select2();
   		});
   	</script>
  </body>
</html>
