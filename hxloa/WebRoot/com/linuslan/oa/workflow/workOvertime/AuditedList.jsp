<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>待登录用户审核的加班申请列表</title>
    
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
   				
   				<form id="search_form_workOvertime_audited_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">加班开始时间：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.startDate" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">加班结束时间：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.endDate" readonly="readonly" type="text" class="form-control pull-right date">
								</div>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">加班类型：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.classId" class="form-control select2">
									
								</select>
							</div>
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">申请人：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<select name="paramMap.userId" class="form-control select2">
									
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">事由：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<input name="paramMap.content" type="text" class="form-control">
							</div>
							<div class="col-md-3 col-sm-6">
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchLeaveAuditedList"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm margin-left5" data-toggle="tooltip" title="重置" id="resetSearchLeaveAuditedList"><i class="fa fa-fw fa-undo"></i>重置</button>
							</div>
						</div>
   					</div>
   				</form>
   				
   				<div class="box-body">
   					<table id="workOvertimeAuditedDatagrid"></table>
   					<div id="workOvertimeAuditedDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var auditedWorkOvertimeGrid;
   		var auditedWorkOvertimeDialog;
   		$(function() {
   			
   			$("#search_form_workOvertime_audited_list").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
			initSelect_workOvertimeAudited("search_form_workOvertime_audited_list");
			$("#searchLeaveAuditedList").click(function() {
   				$("#workOvertimeAuditedDatagrid").setGridParam({
   					postData:parsePostData("search_form_workOvertime_audited_list")
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchLeaveAuditedList").click(function() {
   				$("#search_form_workOvertime_audited_list")[0].reset();
   				$("#search_form_workOvertime_audited_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			
   			auditedWorkOvertimeGrid = $("#workOvertimeAuditedDatagrid").jqGrid({
                url: getRoot() + "workflow/workOvertime/queryAuditedPage.action",
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
                	label: "申请人", name: "userName", width: 100, align: "center"
                }, {
                	label: "归属部门", name: "userDeptName", width: 150, align: "center"
                }, {
                	label: "归属公司", name: "companyName", width: 150, align: "center"
                }, {
                	label: "加班类型", name: "className", width: 150, align: "center"
                }, {
                	label: "加班时长", name: "duration", width: 100, align: "center"
                }, {
                	label: "开始时间", name: "startDate", width: 200, align: "center"
                }, {
                	label: "结束时间", name: "endDate", width: 200, align: "center"
                }, {
                	label: "事由", name: "content", width: 300, align: ""
                }, {
                	label: "流程状态", name: "flowStatus", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		return getFlowStatus(rowObject.status);
                	}
                }, {
                	label: "当前审核组", name: "auditors", width: 150, align: "center"
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewAuditedWorkOvertime("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+")");
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: "550px",
                //width: "100%",
                rowNum: 20,
                pager: "#workOvertimeAuditedDatagridpager"
            });
   		});
   		
   		function viewAuditedWorkOvertime(id, passBtn, rejectBtn) {
   			auditedWorkOvertimeDialog = BootstrapDialog.show({
			    title: "详情",
			    width: "50%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/workOvertime/queryById.action?returnType=view&workOvertime.id="+id),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    buttons: [{
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-warning",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
   		}
   		
   		function initSelect_workOvertimeAudited(id) {
   			try {
   				$("#"+id+" select.select2").select2();
   				$.ajax({
   	   				url: getRoot() + "workflow/workOvertime/initSelect.action",
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						for(var key in json) {
   	   							var selector = "#"+id+" select[name='paramMap."+key+"']";
   	   							initOptions_workOvertimeAudited(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化加班查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_workOvertimeAudited(data, selector) {
   			if(data && data.length > 0 && selector) {
   				var option = document.createElement("option");
   				option.value = "";
   				option.text = "请选择";
   				$(selector).append(option);
   				for(var i = 0;i < data.length; i ++) {
   					var opData = data[i];
   					var option = document.createElement("option");
   					$(selector).append(option);
   					if(opData.id) {
   						option.value = opData.id;
   					} else if(opData.ID) {
   						option.value = opData.ID;
   					}
   					if(opData.name) {
   						option.text = opData.name;
   					} else if(opData.text) {
   						option.text = opData.text;
   					} else if(opData.TEXT) {
   						option.text = opData.TEXT;
   					} else if(opData.NAME) {
   						option.text = opData.NAME;
   					}
   				}
   			}
   		}
   		
   	</script>
  </body>
</html>
