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
  	<div id="userlist">
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<div class="box-body">
   					<div class="nav-tabs-top-border">
			   			<ul class="nav nav-tabs">
			   				<li class="active">
			   					<a href="#user_userlist" data-toggle="tab">员工列表</a>
			   				</li>
			   				<li>
			   					<a href="#user_userSalary" data-toggle="tab">薪资调整详情</a>
			   				</li>
			   				<li>
			   					<a href="#user_userContract" data-toggle="tab">合同</a>
			   				</li>
			   				<li>
			   					<a href="#user_userCapital" data-toggle="tab">固资</a>
			   				</li>
			   			</ul>
			   			<div class="tab-content">
			   				<div class="active tab-pane" id="user_userlist">
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
												<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">状态：</label>
												<div class="col-md-2 col-sm-4 col-xs-8">
													<select name="paramMap.isLeave" class="form-control select2">
														<option value="">请选择</option>
														<option value="1">在职</option>
														<option value="0">离职</option>
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
			   						<div class="toolbar with-border">
					   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增人员" id="addUser"><i class="fa fa-fw fa-user-plus"></i>新增</button>
					   				</div>
					   				<div class="box-body">
					   					<table id="userdatagrid"></table>
   										<div id="userdatagridpager"></div>
					   				</div>
			   					</div>
			   				</div>
			   				<div class="tab-pane" id="user_userSalary">
			   					<div class="box box-solid">
			   						<div class="toolbar with-border">
					   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增工资" id="addUserSalary"><i class="fa fa-fw fa-plus"></i>新增</button>
					   				</div>
			   						<div class="box-body">
			   							<table id="userSalaryDatagrid"></table>
			   							<div id="userSalaryDatagridPager"></div>
			   						</div>
			   					</div>
			   				</div>
			   				<div class="tab-pane" id="user_userContract">
			   					<div class="box box-solid">
			   						<div class="toolbar with-border">
					   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增合同" id="addUserContract"><i class="fa fa-fw fa-plus"></i>新增</button>
					   				</div>
			   						<div class="box-body">
			   							<table id="userContractDatagrid"></table>
			   							<div id="userContractDatagridPager"></div>
			   						</div>
			   					</div>
			   				</div>
			   				<div class="tab-pane" id="user_userCapital">
			   					<div class="box box-solid">
			   						<div class="box-body">
			   							<table id="userCapitalDatagrid"></table>
			   							<div id="userCapitalDatagridPager"></div>
			   						</div>
			   					</div>
			   				</div>
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
                	label: "登录名", name: "loginName", width: 150, align: "center"
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
                }, {
                	label: "在职状态", name: "inserviceStatus", width: 70, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		if(rowObject.inserviceStatus == 1) {
                			return "转正";
                		} else {
                			return "<font color='red'>试用</font>";
                		}
                	}
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = createBtn("查看", "btn-info btn-xs", "fa-file-o", "viewUser("+rowObject.id+")");
                		buttons = buttons + createBtn("修改", "btn-success btn-xs", "fa-pencil", "editUser("+rowObject.id+")");
                		if(rowObject.isLeave == 1) {
                			buttons = buttons + createBtn("离职", "btn-danger btn-xs", "fa-user-times", "fireUser("+rowObject.id+")");
                		} else {
                			buttons = buttons + createBtn("复职", "btn-warning btn-xs", "fa-history", "resumeUser("+rowObject.id+")");
                		}
                		buttons = buttons + createBtn("分配公司", "btn-info btn-xs", "fa-clone", "assignCompany("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delUser("+rowObject.id+")");
   						return buttons;
                	}
                }],
                onSelectRow: function(id){
                	var rowObject = $("#userdatagrid").getRowData(id);
                	var userId = rowObject.id;
                	if(userId) {
                		$("#userSalaryDatagrid").jqGrid("setGridParam",{ 
				            postData:{"user.id": userId} //发送数据 
				        }).trigger("reloadGrid");
				        $("#userCapitalDatagrid").jqGrid("setGridParam",{ 
				            postData:{"paramMap.userId": userId},
				            url: getRoot() + "sys/capital/queryPage.action"
				        }).trigger("reloadGrid");
				        $("#userContractDatagrid").jqGrid("setGridParam",{ 
				            postData:{"paramMap.userId": userId},
				            url: getRoot() + "sys/userContract/queryPage.action"
				        }).trigger("reloadGrid");
                	}
			    },
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
   			
   			userSalaryDatagrid = $("#userSalaryDatagrid").jqGrid({
   				url: getRoot() + "sys/userSalary/queryPageByUserId.action",
   				mtype: "POST",
                shrinkToFit: false,
                frozenColumns: true,
                datatype: "json",
                rowList: [10, 15, 20],
                rownumbers: true,
                colModel: [{
                	label: "姓名", name: "userName", width: 100, frozen: true
                }, {
                	label: "薪资类型", name: "typeName", width: 100, frozen: true
                }, {
                	label: "基本工资", name: "basicSalary", width: 100, frozen: true
                }, {
                	label: "岗位工资", name: "postSalary", width: 100, frozen: true
                }, {
                	label: "绩效工资", name: "achievementSalary", width: 100, frozen: true
                }, {
                	label: "生效时间", name: "effectDate", width: 100, frozen: true
                }, {
                	label: "是否生效", name: "status", width: 100,
                	formatter: function(cellValue, options, rowObject) {
                		if(rowObject.status == 1) {
                			return "<font color='red'>是</font>";
                		} else {
                			return "否";
                		}
                	}
                }, {
                	label: "强制生效", name: "isForceEffect", width: 100,
                	formatter: function(cellValue, options, rowObject) {
                		if(rowObject.isForceEffect == 1) {
                			return "<font color='red'>是</font>";
                		} else {
                			return "否";
                		}
                	}
                }, {
                	label: "工作餐补贴", name: "mealSubsidy", width: 100
                }, {
                	label: "工龄工资", name: "serviceAgeSalary", width: 100
                }, {
                	label: "工龄工资开始时间", name: "serviceAgeSalaryStartDate", width: 150
                }, {
                	label: "住房补贴", name: "housingSubsidy", width: 100
                }, {
                	label: "社保基数", name: "socialInsurance", width: 100
                }, {
                	label: "社保开始时间", name: "socialInsuranceStartDate", width: 150
                }, {
                	label: "医保基数", name: "healthInsurance", width: 100
                }, {
                	label: "医保开始时间", name: "healthInsuranceStartDate", width: 150
                }, {
                	label: "公积金", name: "housingFund", width: 100
                }, {
                	label: "公积金开始时间", name: "housingFundStartDate", width: 150
                }, {
                	label: "话费补贴", name: "telCharge", width: 100
                }, {
                	label: "话费补贴开始时间", name: "telChargeStartDate", width: 150
                }, {
                	label: "交通补贴", name: "travelAllowance", width: 100
                }, {
                	label: "交通补贴开始时间", name: "travelAllowanceStartDate", width: 150
                }, {
                	label: "操作", width: 90, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-o", "viewUserSalary("+rowObject.id+")");
                		buttons = buttons + createBtn("修改", "btn-success btn-xs", "fa-pencil", "editUserSalary("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delUserSalary("+rowObject.id+")");
   						return buttons;
                	}
                }],
                gridComplete: function() {
                	
                },
				viewrecords: true,
                height: "400px",
                width: "500px",
                //width: "100%",
                rowNum: 10,
                pager: "#userSalaryDatagridPager"
   			});
   			
   			userCapitalDatagrid = $("#userCapitalDatagrid").jqGrid({
   				url: "",
   				mtype: "POST",
                shrinkToFit: true,
                datatype: "json",
                rowList: [10, 15, 20],
                rownumbers: true,
                colModel: [{
                	label: "编号", name: "serial", width: 100, frozen: true
                }, {
                	label: "固资类别", name: "className", width: 100, frozen: true
                }, {
                	label: "名称", name: "name", width: 100, frozen: true
                }, {
                	label: "型号", name: "model", width: 100, frozen: true
                }, {
                	label: "借用/领用时间", name: "borrowDate", width: 100
                }, {
                	label: "计量单位", name: "unit", width: 100
                }],
                gridComplete: function() {
                	
                },
				viewrecords: true,
                height: "400px",
                width: "500px",
                //width: "100%",
                rowNum: 10,
                pager: "#userCapitalDatagridPager"
   			});
   			
   			userContractDatagrid = $("#userContractDatagrid").jqGrid({
   				url: "",
   				mtype: "POST",
                shrinkToFit: true,
                datatype: "json",
                rowList: [10, 15, 20],
                rownumbers: true,
                colModel: [{
                	label: "用户名", name: "userName", width: 100, frozen: true
                }, {
                	label: "合同编号", name: "num", width: 100, frozen: true
                }, {
                	label: "合同标题", name: "title", width: 100, frozen: true
                }, {
                	label: "开始时间", name: "startDate", width: 100
                }, {
                	label: "结束时间", name: "endDate", width: 100
                }, {
                	label: "是否提醒", name: "isRemind", width: 100,
                	formatter: function(cellvalue, options, rowObject) {
                		if(rowObject.isRemind == 1) {
                			return "否";
                		} else {
                			return "<font color='red'>是</font>";
                		}
                	}
                }, {
                	label: "是否生效", name: "isEffective", width: 100,
                	formatter: function(cellvalue, options, rowObject) {
                		if(rowObject.isEffective == 1) {
                			return "否";
                		} else {
                			return "<font color='red'>是</font>";
                		}
                	}
                }, {
                	label: "合同内容", name: "content", width: 300, frozen: true
                }, {
                	label: "操作", width: 90, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-o", "viewUserContract("+rowObject.id+")");
                		buttons = buttons + createBtn("修改", "btn-success btn-xs", "fa-pencil", "editUserContract("+rowObject.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delUserContract("+rowObject.id+")");
   						return buttons;
                	}
                }],
                gridComplete: function() {
                	
                },
				viewrecords: true,
                height: "400px",
                width: "500px",
                //width: "100%",
                rowNum: 10,
                pager: "#userContractDatagridPager"
   			});
   			
			$("#addUser").click(function() {
				BootstrapDialog.show({
				    title: "新增用户",
				    width: "80%",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "sys/user/queryById.action?returnType=add"),
				    draggable: true,
				    autodestroy: true,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	
				    },
				    buttons: [{
				    	label: "保存",
				    	icon: "fa fa-fw fa-save",
				    	cssClass: "btn-success",
				    	action: function(dialog) {
				    		try {
				    			var form = dialog.getModalBody().find("form");
				    			var $button = this;
				    			$.ajax({
				    				url: getRoot() + "sys/user/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						userDataGrid.jqGrid().trigger("reloadGrid");
				    					} else {
				    						BootstrapDialog.danger(json.msg);
				    					}
				    				},
				    				error: function() {
				    					BootstrapDialog.danger("系统异常，请联系管理员！");
				    				},
				    				beforeSend: function() {
				    					dialog.enableButtons(false);
				    					dialog.setClosable(false);
				    					$button.spin();
				    				},
				    				complete: function() {
				    					dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    					$button.stopSpin();
				    				}
				    			});
				    		} catch(e) {
				    			dialog.enableButtons(true);
			    				dialog.setClosable(true);
				    			BootstrapDialog.danger("系统异常，请联系管理员！");
				    		}
				    	}
				    }, {
				    	label: "关闭",
				    	icon: "fa fa-fw fa-close",
				    	cssClass: "btn-danger",
				    	action: function(dialog) {
				    		dialog.close();
				    	}
				    }]
		        });
			});
			
			$("#addUserSalary").click(function() {
				var rowid = $("#userdatagrid").getGridParam("selrow");
				if(!rowid) {
					BootstrapDialog.danger("请先选择用户");
					return false;
				}
				var userObject = $("#userdatagrid").getRowData(rowid);
				if(!userObject || 1 > userObject.length) {
					BootstrapDialog.danger("获取用户数据异常");
					return false;
				}
				var userId = userObject.id;
				if(!userId) {
					BootstrapDialog.danger("用户ID不存在");
					return false;
				}
				BootstrapDialog.show({
				    title: "新增用户薪资",
				    width: "60%",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "sys/userSalary/queryById.action?returnType=add&user.id="+userId),
				    draggable: true,
				    autodestroy: true,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	
				    },
				    buttons: [{
				    	label: "保存",
				    	icon: "fa fa-fw fa-save",
				    	cssClass: "btn-success",
				    	action: function(dialog) {
				    		try {
				    			var form = dialog.getModalBody().find("form");
				    			var $button = this;
				    			$.ajax({
				    				url: getRoot() + "sys/userSalary/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						userSalaryDatagrid.jqGrid().trigger("reloadGrid");
				    					} else {
				    						BootstrapDialog.danger(json.msg);
				    					}
				    				},
				    				error: function() {
				    					BootstrapDialog.danger("系统异常，请联系管理员！");
				    				},
				    				beforeSend: function() {
				    					dialog.enableButtons(false);
				    					dialog.setClosable(false);
				    					$button.spin();
				    				},
				    				complete: function() {
				    					dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    					$button.stopSpin();
				    				}
				    			});
				    		} catch(e) {
				    			dialog.enableButtons(true);
			    				dialog.setClosable(true);
				    			BootstrapDialog.danger("系统异常，请联系管理员！");
				    		}
				    	}
				    }, {
				    	label: "关闭",
				    	icon: "fa fa-fw fa-close",
				    	cssClass: "btn-danger",
				    	action: function(dialog) {
				    		dialog.close();
				    	}
				    }]
		        });
			});
			
			$("#addUserContract").click(function() {
				var rowid = $("#userdatagrid").getGridParam("selrow");
				if(!rowid) {
					BootstrapDialog.danger("请先选择用户");
					return false;
				}
				var userObject = $("#userdatagrid").getRowData(rowid);
				if(!userObject || 1 > userObject.length) {
					BootstrapDialog.danger("获取用户数据异常");
					return false;
				}
				var userId = userObject.id;
				if(!userId) {
					BootstrapDialog.danger("用户ID不存在");
					return false;
				}
				BootstrapDialog.show({
				    title: "新增用户合同",
				    width: "40%",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "sys/userContract/initContent.action?returnType=add&user.id="+userId),
				    draggable: true,
				    autodestroy: true,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	
				    },
				    buttons: [{
				    	label: "保存",
				    	icon: "fa fa-fw fa-save",
				    	cssClass: "btn-success",
				    	action: function(dialog) {
				    		try {
				    			var form = dialog.getModalBody().find("form");
				    			var $button = this;
				    			$.ajax({
				    				url: getRoot() + "sys/userContract/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						userContractDatagrid.jqGrid().trigger("reloadGrid");
				    					} else {
				    						BootstrapDialog.danger(json.msg);
				    					}
				    				},
				    				error: function() {
				    					BootstrapDialog.danger("系统异常，请联系管理员！");
				    				},
				    				beforeSend: function() {
				    					dialog.enableButtons(false);
				    					dialog.setClosable(false);
				    					$button.spin();
				    				},
				    				complete: function() {
				    					dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    					$button.stopSpin();
				    				}
				    			});
				    		} catch(e) {
				    			dialog.enableButtons(true);
			    				dialog.setClosable(true);
				    			BootstrapDialog.danger("系统异常，请联系管理员！");
				    		}
				    	}
				    }, {
				    	label: "关闭",
				    	icon: "fa fa-fw fa-close",
				    	cssClass: "btn-danger",
				    	action: function(dialog) {
				    		dialog.close();
				    	}
				    }]
		        });
			});
   		});
   		
   		function editUser(id) {
   			BootstrapDialog.show({
			    title: "编辑用户",
			    width: "80%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/user/queryById.action?user.id="+id+"&returnType=edit"),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
		   			
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			var $button = this;
			    			$.ajax({
			    				url: getRoot() + "sys/user/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						userDataGrid.jqGrid().trigger("reloadGrid");
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				},
			    				beforeSend: function() {
			    					dialog.enableButtons(false);
			    					dialog.setClosable(false);
			    					$button.spin();
			    				},
			    				complete: function() {
			    					dialog.enableButtons(true);
			    					dialog.setClosable(true);
			    					$button.stopSpin();
			    				}
			    			});
			    		} catch(e) {
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-danger",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
		}
   		
   		function viewUser(id) {
   			BootstrapDialog.show({
			    title: "用户详情",
			    width: "80%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/user/queryById.action?user.id="+id+"&returnType=view"),
			    draggable: true,
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
   		
   		function delUser(id) {
   			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定删除吗？",
	            type: BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-danger", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	   					$.ajax({
	   						url: getRoot() + "sys/user/del.action",
		    				data: "user.id="+id,
		    				type: "POST",
		    				beforeSend: function() {
		    					//mask($("#userdatagrid").parent());
		    				},
		    				complete: function() {
		    					//unmask($("#userdatagrid").parent());
		    				},
		    				success: function(data) {
		    					var json = eval("("+data+")");
		    					if(json.success) {
		    						BootstrapDialog.success(json.msg);
		    						userDataGrid.jqGrid().trigger("reloadGrid");
		    					} else {
		    						BootstrapDialog.danger(json.msg);
		    					}
		    				},
		    				error: function() {
		    					BootstrapDialog.danger("系统异常，请联系管理员！");
		    				}
	   					});
	   				}
	            }
   			});
   		}
   		
   		function editUserSalary(id) {
   			BootstrapDialog.show({
			    title: "编辑用户薪资",
			    width: "60%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/userSalary/queryById.action?userSalary.id="+id+"&returnType=edit"),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
		   			
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			var $button = this;
			    			$.ajax({
			    				url: getRoot() + "sys/userSalary/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						userSalaryDatagrid.jqGrid().trigger("reloadGrid");
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				},
			    				beforeSend: function() {
			    					dialog.enableButtons(false);
			    					dialog.setClosable(false);
			    					$button.spin();
			    				},
			    				complete: function() {
			    					dialog.enableButtons(true);
			    					dialog.setClosable(true);
			    					$button.stopSpin();
			    				}
			    			});
			    		} catch(e) {
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-danger",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
		}
   		
   		function viewUserSalary(id) {
   			BootstrapDialog.show({
			    title: "用户薪资详情",
			    width: "60%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/userSalary/queryById.action?userSalary.id="+id+"&returnType=view"),
			    draggable: true,
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
   		
   		function delUserSalary(id) {
   			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定删除吗？",
	            type: BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-danger", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	   					$.ajax({
	   						url: getRoot() + "sys/userSalary/del.action",
		    				data: "userSalary.id="+id,
		    				type: "POST",
		    				beforeSend: function() {
		    					//mask($("#userdatagrid").parent());
		    				},
		    				complete: function() {
		    					//unmask($("#userdatagrid").parent());
		    				},
		    				success: function(data) {
		    					var json = eval("("+data+")");
		    					if(json.success) {
		    						BootstrapDialog.success(json.msg);
		    						userSalaryDatagrid.jqGrid().trigger("reloadGrid");
		    					} else {
		    						BootstrapDialog.danger(json.msg);
		    					}
		    				},
		    				error: function() {
		    					BootstrapDialog.danger("系统异常，请联系管理员！");
		    				}
	   					});
	   				}
	            }
   			});
   		}
   		
   		function editUserContract(id) {
   			BootstrapDialog.show({
			    title: "编辑用户合同",
			    width: "40%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/userContract/initContent.action?userContract.id="+id+"&returnType=edit"),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
		   			
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			var $button = this;
			    			$.ajax({
			    				url: getRoot() + "sys/userContract/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						userContractDatagrid.jqGrid().trigger("reloadGrid");
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				},
			    				beforeSend: function() {
			    					dialog.enableButtons(false);
			    					dialog.setClosable(false);
			    					$button.spin();
			    				},
			    				complete: function() {
			    					dialog.enableButtons(true);
			    					dialog.setClosable(true);
			    					$button.stopSpin();
			    				}
			    			});
			    		} catch(e) {
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-danger",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
		}
   		
   		function viewUserContract(id) {
   			BootstrapDialog.show({
			    title: "用户合同详情",
			    width: "40%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/userContract/initContent.action?userContract.id="+id+"&returnType=view"),
			    draggable: true,
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
   		
   		function delUserContract(id) {
   			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定删除吗？",
	            type: BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-danger", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	   					$.ajax({
	   						url: getRoot() + "sys/userContract/delById.action",
		    				data: "userContract.id="+id,
		    				type: "POST",
		    				beforeSend: function() {
		    					//mask($("#userdatagrid").parent());
		    				},
		    				complete: function() {
		    					//unmask($("#userdatagrid").parent());
		    				},
		    				success: function(data) {
		    					var json = eval("("+data+")");
		    					if(json.success) {
		    						BootstrapDialog.success(json.msg);
		    						userContractDatagrid.jqGrid().trigger("reloadGrid");
		    					} else {
		    						BootstrapDialog.danger(json.msg);
		    					}
		    				},
		    				error: function() {
		    					BootstrapDialog.danger("系统异常，请联系管理员！");
		    				}
	   					});
	   				}
	            }
   			});
   		}
   		
   		function fireUser(id) {
   			if(id) {
   				BootstrapDialog.confirm({
   		            title: "温馨提示",
   		            message: "您确定为该员工办理离职吗？",
   		            type: BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
   		            closable: true, // <-- Default value is false
   		            draggable: true, // <-- Default value is false
   		            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
   		            btnOKLabel: "确定", // <-- Default value is 'OK',
   		            btnOKClass: "btn-danger", // <-- If you didn't specify it, dialog type will be used,
   		            callback: function(y) {
   		                // result will be true if button was click, while it will be false if users close the dialog directly.
   		                if(y) {
   		                	BootstrapDialog.show({
	   		     			    title: "离职资料",
	   		     			    width: "40%",
	   		     			    type: BootstrapDialog.TYPE_DEFAULT,
	   		     			    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/user/Fire.jsp?userId="+id),
	   		     			    draggable: true,
	   		     			    autodestroy: true,
	   		     			    closeByBackdrop: false,
	   		     			    autospin: true,
	   		     			    onshown: function(dialogRef) {
	   		     		   			
	   		     			    },
	   		     			    buttons: [{
	   		     			    	label: "保存",
	   		     			    	icon: "fa fa-fw fa-save",
	   		     			    	cssClass: "btn-success",
	   		     			    	action: function(dialog) {
	   		     			    		try {
	   		     			    			var form = dialog.getModalBody().find("form");
	   		     			    			var $button = this;
		   		     			    		$.ajax({
		   		   		   						url: getRoot() + "sys/user/fire.action",
		   		   			    				data: form.serialize(),
		   		   			    				type: "POST",
			   		   			    			beforeSend: function() {
	   		     			    					dialog.enableButtons(false);
	   		     			    					dialog.setClosable(false);
	   		     			    					$button.spin();
	   		     			    				},
	   		     			    				complete: function() {
	   		     			    					dialog.enableButtons(true);
	   		     			    					dialog.setClosable(true);
	   		     			    					$button.stopSpin();
	   		     			    				},
		   		   			    				success: function(data) {
		   		   			    					var json = eval("("+data+")");
		   		   			    					if(json.success) {
		   		   			    						BootstrapDialog.success(json.msg);
		   		   			    						dialog.close();
		   		   			    						userDataGrid.jqGrid().trigger("reloadGrid");
		   		   			    					} else {
		   		   			    						BootstrapDialog.danger(json.msg);
		   		   			    					}
		   		   			    				},
		   		   			    				error: function() {
		   		   			    					BootstrapDialog.danger("系统异常，请联系管理员！");
		   		   			    				}
		   		   		   					});
	   		     			    		} catch(e) {
	   		     			    			dialog.enableButtons(true);
	   		     		    				dialog.setClosable(true);
	   		     			    			BootstrapDialog.danger("系统异常，请联系管理员！");
	   		     			    		}
	   		     			    	}
	   		     			    }, {
	   		     			    	label: "关闭",
	   		     			    	icon: "fa fa-fw fa-close",
	   		     			    	cssClass: "btn-danger",
	   		     			    	action: function(dialog) {
	   		     			    		dialog.close();
	   		     			    	}
	   		     			    }]
	   		     	        });
   		   				}
   		            }
   	   			});
   			}
   		}
   		
   		function resumeUser(id) {
   			if(id) {
   				BootstrapDialog.confirm({
   		            title: "温馨提示",
   		            message: "您确定复职该员工吗？",
   		            type: BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
   		            closable: true, // <-- Default value is false
   		            draggable: true, // <-- Default value is false
   		            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
   		            btnOKLabel: "确定", // <-- Default value is 'OK',
   		            btnOKClass: "btn-danger", // <-- If you didn't specify it, dialog type will be used,
   		            callback: function(y) {
   		                // result will be true if button was click, while it will be false if users close the dialog directly.
   		                if(y) {
   		                	$.ajax({
   		                		url: getRoot() + "sys/user/resume.action",
   		                		data: {
   		                			"user.id": id
   		                		},
   		                		type: "POST",
   		                		success: function(data) {
   		                			var json = eval("("+data+")");
   		                			if(json.success) {
  			    						BootstrapDialog.success(json.msg);
  			    						userDataGrid.jqGrid().trigger("reloadGrid");
  			    					} else {
  			    						BootstrapDialog.danger(json.msg);
  			    					}
   		                		},
   			    				error: function() {
	   			    				BootstrapDialog.danger("系统异常，请联系管理员！");
	   			    			}
   		                	});
   		   				}
   		            }
   	   			});
   			}
   		}
   		
   		function assignCompany(userId) {
   			if(!userId) {
   				BootstrapDialog.warning("人员ID异常");
   				return false;
   			}
			BootstrapDialog.show({
			    title: "用户公司分配",
			    width: "40%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/user/queryById.action?user.id="+userId+"&returnType=assignCompany"),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
		   			
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			var $button = this;
			    			$.ajax({
			    				url: getRoot() + "sys/user/assignCompany.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						userDataGrid.jqGrid().trigger("reloadGrid");
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				},
			    				beforeSend: function() {
			    					dialog.enableButtons(false);
			    					dialog.setClosable(false);
			    					$button.spin();
			    				},
			    				complete: function() {
			    					dialog.enableButtons(true);
			    					dialog.setClosable(true);
			    					$button.stopSpin();
			    				}
			    			});
			    		} catch(e) {
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-danger",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
   		}
   		
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
