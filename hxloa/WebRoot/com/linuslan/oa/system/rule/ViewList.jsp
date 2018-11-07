<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    
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
  	<div>
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<div class="box-body">
   					<table id="ruleViewDatagrid"></table>
   					<div id="ruleViewDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var ruleDataGrid;
   		$(function() {
   			ruleDataGrid = $("#ruleViewDatagrid").jqGrid({
                url: getRoot() + "sys/rule/queryPage.action",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "制度名", name: "title", width: 200, align: "left"
                }, {
                	label: "操作", align: "center", width: 10, formatter: function(cellvalue, options, rowObject) {
                		var filePath = encodeURI(rowObject.filePath);
   						var buttons = createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewRuleView("+rowObject.id+", \""+filePath+"\")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20,
                pager: "#ruleViewDatagridpager"
            });
   		});
   		
   		function viewRuleView(id, filePath) {
   			/*BootstrapDialog.show({
			    title: "查看制度",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/rule/queryById.action?returnType=view&rule.id="+id),
			    draggable: true,
			   	width: "99%",
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [{
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-danger",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });*/
   			showPdf(true);
   			$("#pdfContainer").attr("src", getRoot() + "resources/office/web/viewer.html?name="+filePath);
   		}
   	</script>
  </body>
</html>
