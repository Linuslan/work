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
   				<form id="search_form_notice_viewlist" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">标题：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.title" type="text" class="form-control">
							</div>
							<div class="col-md-3 col-sm-3">
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchNoticeViewList"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="reset" class="btn btn-info btn-sm margin-right5" data-toggle="tooltip" title="重置"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				<div class="box-body">
   					<table id="viewNoticeDatagrid"></table>
   					<div id="viewNoticeDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var noticeDataGrid;
   		$(function() {
   			noticeDataGrid = $("#viewNoticeDatagrid").jqGrid({
                url: getRoot() + "sys/notice/queryPageByUserId.action",
                mtype: "POST",
                shrinkToFit: true,
                rownumbers: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                colModel: [{
                	label: "标题", name: "TITLE", width: 200
                }, {
                	label: "是否已读", name: "IS_READ", width: 150,
                	formatter: function(cellValue, options, rowObject) {
                		if(rowObject.IS_READ == 0) {
                			return "<font color='red'>未读</font>";
                		} else {
                			return "已读";
                		}
                	}
                }, {
                	label: "发送时间", name: "SENDDATE", width: 150
                }, {
                	label: "发送人", name: "SENDER_NAME", width: 150
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewLoginUserNotice("+rowObject.NOTICEID+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#viewNoticeDatagridpager"
            });
            
            $("#searchNoticeViewList").click(function() {
   				$("#viewNoticeDatagrid").setGridParam({
   					postData:parsePostData("search_form_notice_viewlist")
   				}).trigger("reloadGrid");
   			});
   		});
   		
   		function viewLoginUserNotice(id) {
   			BootstrapDialog.show({
			    title: "公告详情",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/notice/queryById.action?returnType=view&notice.id="+id),
			    draggable: true,
			   	width: getA4Width(),
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
	        });
   		}
   	</script>
  </body>
</html>
