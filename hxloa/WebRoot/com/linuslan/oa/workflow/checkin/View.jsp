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
   	<form id="viewCheckinForm" action="" class="form-horizontal">
   		<input class="checkinId" type="hidden" name="checkin.id" id="viewCheckinId" value="${checkin.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#checkin_view" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#checkinUploadFile_view" data-toggle="tab">附件列表</a>
   				</li>
   				<li>
   					<a href="#checkinAuditorLogs_view" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="checkin_view">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${checkin.companyName }
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">入库编号：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${checkin.serialNo }
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">入库仓库：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${checkin.warehouseName }
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">入库类型：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${checkin.checkinTypeName }
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">采购单号：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${checkin.purchaseSerialNo }
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">入库时间：</label>
							<div class="col-md-2 col-sm-8 left-label">
								<fmt:formatDate value="${checkin.checkinDate }" pattern="yyyy-MM-dd"/>
							</div>
						</div>
						<div class="form-group">
							<label for="moneyDate" class="col-md-2-sm col-sm-4 control-label">单位名称：</label>
							<div class="col-md-2 col-sm-8 left-label">
								${checkin.unitName }
							</div>
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">联系电话：</label>
							<div class="col-md-2 col-sm-8 telephone no-padding" >
								${checkin.telephone }
							</div>
						</div>
						<div class="form-group padding-bottom5">
							<label for="orderNo" class="col-md-2-sm col-sm-4 control-label">单位地址：</label>
							<div class="col-md-11-sm col-sm-8 address no-padding">
								${checkin.address }
							</div>
						</div>
			    	</div>
			    	
			    	<div class="box-body">
			    		<table id="checkinContentDatagrid_view"></table>
			    	</div>
			    </div>
			    <div class="tab-pane" id="checkinUploadFile_view">
					<div class="box-body" id="checkinUploadFileList_view">
						<c:forEach items="${uploadFiles }" var="uploadFile">
							<div class="form-group padding-bottom5">
								<div class="col-md-11-sm col-sm-8 align-left padding-top7">
									<a onclick="checkinDownloadFile_view(${uploadFile.id })" href="javascript: void(0);">${uploadFile.fileName }</a>
								</div>
							</div>
						</c:forEach>
					</div>
			    </div>
			   	<div class="tab-pane" id="checkinAuditorLogs_view">
   					<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="checkinViewAuditorlogsDatagrid"></table>
	   							<div id="checkinViewAuditorlogsDatagridPager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
   		</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
            //需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateCheckinAuditLogGrid()", 0);
			setTimeout("generateCheckinContentGrid_view()", 0);
   		});
   		
   		function generateCheckinAuditLogGrid() {
   			var id = $("#viewCheckinForm").find("input.checkinId").val();
   			$("#checkinViewAuditorlogsDatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=checkin&wfId="+id,
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
                pager: "#checkinViewAuditorlogsDatagridPager"
            });
   		}
   		
   		function generateCheckinContentGrid_view() {
   			var id = $("#viewCheckinForm").find("input.checkinId").val();
   			$("#checkinContentDatagrid_view").jqGrid({
   				url: getRoot() + "workflow/checkin/queryContentsByCheckinId.action?checkin.id="+id,
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "checkinClassName": "d", "checkinClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "商品编号", name: "articleSerialNo"
                }, {
                	label: "商品名称", name: "articleName"
                }, {
                	label: "规格", name: "formatName", width: 150, align: "center"
                }, {
                	label: "单位", name: "unit", width: 150, align: "center"
                }, {
                	label: "数量", name: "quantity", width: 150, align: "center"
                }, {
                	label: "单价", name: "price", width: 150, align: "center"
                }, {
                	label: "金额", name: "totalPrice", width: 150, align: "center"
                }, {
               		label: "损耗", name: "loss", width: 150, align: "center"
                }, {
                	label: "余", name: "remainder", width: 150, align: "center"
                }, {
                	label: "抽检编号", name: "inspection", width: 150, align: "center"
                }, {
                	label: "备注", name: "remark", width: 300, align: "center"
                }],
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20
            });
   		}
   		
   		function checkinDownloadFile_view(id) {
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
