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
   	<form id="viewSaleArticleForm" action="" class="form-horizontal">
   		<input class="articleId" type="hidden" name="saleArticle.id" id="viewSaleArticleId" value="${saleArticle.id }" />
   		
		<div class="box-body padding-bottom5 bottom-dotted-border">
			<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">商品名称：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${saleArticle.name }
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 control-label">排序号：</label>
				<div class="col-md-2 col-sm-8 totalScore left-label">
					${saleArticle.orderNo }
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 col-sm-4 control-label">备注：</label>
				<div class="col-md-9 col-sm-8 left-label">
					${saleArticle.remark }
				</div>
			</div>
	   	</div>
	   	<div class="col-md-6 col-xs-12 no-padding">
   			<div class="box box-solid">
   				<div class="box-header with-border">
   					<h3 class="box-title">材质列表</h3>
   				</div>
   				<div class="box-body">
   					<table id="materialDatagrid_view"></table>
   					<div id="materialDatagridPager_view"></div>
   				</div>
   			</div>
   		</div>
   		<div class="col-md-6 col-xs-12 no-padding">
   			<div class="box box-solid">
   				<div class="box-header with-border">
   					<h3 class="box-title">材质规格列表</h3>
   				</div>
   				<div class="box-body">
   					<table id="materialFormatDatagrid_view"></table>
   					<div id="materialFormatDatagridPager_view"></div>
   				</div>
   			</div>
   		</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
            //需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateMaterialFormatGrid_view()", 0);
            setTimeout("generateMaterialGrid_view()", 0);
   		});
   		
   		function generateMaterialFormatGrid_view() {
   			var id = $("#viewSaleArticleForm").find("input#viewSaleArticleId").val();
   			$("#materialFormatDatagrid_view").jqGrid({
                mtype: "POST",
                url: getRoot() + "workflow/saleArticle/queryMaterialFormatsByArticleId?saleArticle.id="+id,
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "achievementClassName": "d", "achievementClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "材质规格", name: "name", width: 300, align: "center"
                }, {
                	label: "排序号", name: "orderNo", width: 150, align: "center"
                }, {
                	label: "备注", name: "remark", width: 350, align: "center"
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20
            });
   		}
   		
   		function generateMaterialGrid_view() {
   			var id = $("#viewSaleArticleForm").find("input#viewSaleArticleId").val();
   			$("#materialDatagrid_view").jqGrid({
                mtype: "POST",
                url: getRoot() + "workflow/saleArticle/queryMaterialsByArticleId?saleArticle.id="+id,
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "achievementClassName": "d", "achievementClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "材质", name: "name", width: 300, align: "center"
                }, {
                	label: "排序号", name: "orderNo", width: 150, align: "center"
                }, {
                	label: "备注", name: "remark", width: 350, align: "center"
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
