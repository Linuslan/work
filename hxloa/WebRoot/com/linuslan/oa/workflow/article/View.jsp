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
   	<form id="viewArticleFormat" action="" class="form-horizontal">
   		<input class="articleId" type="hidden" name="article.id" id="viewArticleId" value="${article.id }" />
   		
		<div class="box-body padding-bottom5 bottom-dotted-border">
			<div class="form-group">
				<label for="text" class="col-md-1 col-sm-4 control-label">商品名称：</label>
				<div class="col-md-2 col-sm-8 left-label">
					${article.name }
				</div>
				<label for="text" class="col-md-2 col-sm-4 control-label">商品编号：</label>
				<div class="col-md-2 col-sm-8 totalScore left-label">
					${article.serialNo }
				</div>
				<label for="text" class="col-md-2 col-sm-4 control-label">单位：</label>
				<div class="col-md-2 col-sm-8 totalScore left-label">
					${article.unit }
				</div>
			</div>
			<div class="form-group">
				<label for="text" class="col-md-1 col-sm-4 control-label">归属公司：</label>
				<div class="col-md-2 col-sm-8 totalScore left-label">
					${article.companyName }
				</div>
				<label for="text" class="col-md-2 col-sm-4 control-label">排序号：</label>
				<div class="col-md-2 col-sm-8 totalScore left-label">
					${article.orderNo }
				</div>
			</div>
	   	</div>
	   	<div class="box-body">
	   		<table id="formatDatagrid_view"></table>
	   	</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
            //需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateFormatGrid_view()", 0);
   		});
   		
   		function generateReimburseAuditLogGrid() {
   			var id = $("#viewArticleFormat").find("input.articleId").val();
   			$("#reimburseviewauditorlogsdatagrid").jqGrid({
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
                pager: "#reimburseviewauditorlogsdatagridpager"
            });
   		}
   		
   		function generateFormatGrid_view() {
   			var id = $("#viewArticleFormat").find("input.articleId").val();
   			$("#formatDatagrid_view").jqGrid({
   				url: getRoot() + "workflow/article/queryFormatsByArticleId.action?article.id="+id,
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
                	label: "规格名称", name: "name", editable: true, edittype: "text", width: 300, align: "center"
                }, {
                	label: "价格", name: "price", editable: true, edittype: "custom", width: 350, align: "center"
                }, {
                	label: "排序号", name: "orderNo", width: 350, align: "center"
                }, {
                	label: "备注", name: "memo", width: 350, align: "center"
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
