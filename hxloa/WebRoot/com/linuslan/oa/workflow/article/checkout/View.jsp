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
   	<form id="viewCheckoutArticleForm" action="" class="form-horizontal">
   		<input class="checkoutArticleId" type="hidden" name="checkoutArticle.id" id="viewCheckoutArticleId" value="${checkoutArticle.id }" />
   		
		<div class="box-body padding-bottom5 bottom-dotted-border">
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">商品名称：</label>
				<div class="col-md-3 col-sm-8 left-label">
					${checkoutArticle.checkinArticleName }
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 control-label">商品编号：</label>
				<div class="col-md-3 col-sm-8 totalScore left-label">
					${checkoutArticle.serialNo }
				</div>
				
			</div>
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">归属客户：</label>
				<div class="col-md-3 col-sm-8 totalScore left-label">
					${checkoutArticle.customerName }
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 control-label">排序号：</label>
				<div class="col-md-3 col-sm-8 totalScore left-label">
					${checkoutArticle.orderNo }
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
