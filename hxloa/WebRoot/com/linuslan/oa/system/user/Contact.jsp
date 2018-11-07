<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>通讯录</title>
    
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
  	<div id="userlist">
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<div class="box-body">
   					<div class="box box-solid">
   						<form id="search_form_user_list" action="" class="form-horizontal">
		   					<div class="box-body padding20">
		   						<div class="form-group">
									<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">姓名：</label>
									<div class="col-md-2 col-sm-4 col-xs-8">
										<input name="paramMap.name" type="text" class="form-control">
									</div>
									<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">工号：</label>
									<div class="col-md-2 col-sm-4 col-xs-8">
										<input name="paramMap.employeeNo" type="text" class="form-control">
									</div>
									<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">登陆名：</label>
									<div class="col-md-2 col-sm-4 col-xs-8">
										<input name="paramMap.loginName" type="text" class="form-control">
									</div>
									<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属部门：</label>
									<div class="col-md-2 col-sm-4 col-xs-8">
										<div class="departmentTree"></div>
									</div>
								</div>
								<div class="form-group">
									<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">归属公司：</label>
									<div class="col-md-2 col-sm-4 col-xs-8">
										<select name="paramMap.companyId" class="form-control select2">
											
										</select>
									</div>
									<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">在职状态：</label>
									<div class="col-md-2 col-sm-4 col-xs-8">
										<select name="paramMap.inserviceStatus" class="form-control select2">
											<option value="">请选择</option>
											<option value="0">试用</option>
											<option value="1">转正</option>
										</select>
									</div>
									<div class="col-md-3 col-sm-6">
										<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="查询" id="searchUser"><i class="fa fa-fw fa-search"></i>查询</button>
										<button type="button" class="btn btn-info btn-sm margin-left5" data-toggle="tooltip" title="重置" id="resetSearchUser"><i class="fa fa-fw fa-undo"></i>重置</button>
									</div>
								</div>
		   					</div>
		   				</form>
   						
		   				<div class="box-body">
		   					<table id="userdatagrid"></table>
										<div id="userdatagridpager"></div>
		   				</div>
   					</div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var userDataGrid;
   		var userSalaryDatagrid;
   		var userCapitalDatagrid;
   		var userContractDatagrid;
   		$(function() {
   			initSelect_user();
   			$("#search_form_user_list").find(".departmentTree").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "paramMap.departmentId",
				pidField: "pid"
			});
   			$("#userlist a[data-toggle='tab']").on("shown.bs.tab", function (e) {
   				var href = $(this).attr("href");
   				var id = href.substring(1);
   				$("#"+id).find("table").setGridWidth($("#"+id).width()*0.99);
   				if(id == "user_userSalary") {
   					$("#"+id).find("table").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
   	   				$("#"+id).find("table").jqGrid("setFrozenColumns");
   	   				hackHeight("#userSalaryDatagrid");
   				}
   			});
   			userDataGrid = $("#userdatagrid").jqGrid({
                url: getRoot() + "sys/user/queryPage.action",
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
                rownumbers: true,
                datatype: "json",
                rowList: [10, 20, 30],
                colModel: [{
                	label: "id", name: "id", key: true, hidden: true
                }, {
                	label: "姓名", name: "name", width: 150, align: "center"
                }, {
                	label: "工号", name: "employeeNo", width: 100, align: "center"
                }, {
                	label: "E-mail", name: "email", width: 100, align: "center"
                }, {
                	label: "电话", name: "telephone", width: 100, align: "center"
                }, {
                	label: "紧急联系电话", name: "otherPhone", width: 150, align: "center"
                }, {
                	label: "状态", name: "isLeave", width: 70, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		if(rowObject.isLeave == 1) {
                			return "在职";
                		} else {
                			return "离职";
                		}
                	}
                }],
				viewrecords: true,
                height: "600px",
                //width: "100%",
                rowNum: 20,
                pager: "#userdatagridpager"
            });
   			
   			$("#searchUser").click(function() {
   				$("#userdatagrid").setGridParam({
   					postData: parsePostData("search_form_user_list"),
   					page: 1
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchUser").click(function() {
   				$("#search_form_user_list")[0].reset();
   				$("#search_form_user_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   		});
   		
   		function initSelect_user() {
   			try {
   				$("#search_form_user_list select.select2").select2();
   				$.ajax({
   	   				url: getRoot() + "sys/user/initSelect.action",
   	   				type: "POST",
   	   				success: function(data) {
   	   					if(data) {
   	   						var json = eval("("+data+")");
   	   						for(var key in json) {
   	   							var selector = "#search_form_user_list select[name='paramMap."+key+"']";
   	   							initOptions_user(json[key], selector);
   	   						}
   	   					}
   	   				}
   	   			});
   			} catch(ex) {
   				BootstrapDialog.danger("初始化固资查询选项异常，"+ex);
   			}
   		}
   		
   		function initOptions_user(data, selector) {
   			if(data && data.length > 0 && selector) {
   				var option = document.createElement("option");
   				option.value = "";
   				option.text = "请选择";
   				$(selector).append(option);
   				for(var i = 0;i < data.length; i ++) {
   					var opData = data[i];
   					var option = document.createElement("option");
   					$(selector).append(option);
   					option.value = opData.id;
   					option.text = opData.name;
   				}
   			}
   		}
   		
   	</script>
  </body>
</html>
