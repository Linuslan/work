<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>登录用户的绩效列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style type="text/css">
		/*#salarydatagrid_departmentName {
			height: 68px;
		}
		#salarydatagrid_frozen tr td {
			height: 38px;
		}*/
	</style>
  </head>
  
  <body>
    <div id="salary_list">
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<form id="search_form_slary_list" action="" class="form-horizontal">
   					<div class="box-body padding20">
   						<div class="form-group">
							<label for="name" class="col-md-1 col-sm-2 col-xs-4 control-label">日期：</label>
							<div class="col-md-2 col-sm-4 col-xs-8">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input name="paramMap.date" type="text" class="form-control pull-right date">
								</div>
							</div>
							<div class="col-md-3 col-sm-3">
								<button type="button" class="btn btn-info btn-sm margin-right5" data-toggle="tooltip" title="查询" id="searchSalary"><i class="fa fa-fw fa-search"></i>查询</button>
								<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="重置" id="resetSearchSalary"><i class="fa fa-fw fa-undo"></i>重置</button>
								
							</div>
						</div>
   					</div>
   				</form>
   				<div class="toolbar with-border">
   					<button class="btn btn-info btn-sm" data-toggle="tooltip" title="创建工资" id="createSalary"><i class="fa fa-fw fa-clone"></i>创建</button>
   					
   					<button class="btn btn-info btn-sm" data-toggle="tooltip" title="导出" id="exportSalary"><i class="fa fa-fw fa-share-square-o"></i>导出</button>
   				</div>
   				<div class="box-body">
   					<table id="salarydatagrid"></table>
   					<div id="salarydatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var salaryGrid;
   		var salaryDialog;
   		var salaryLastSel2;
   		$(function() {
   			$("#search_form_slary_list input.date").datepicker({
   				startView: 1,
   				maxViewMode: 1,
   				minViewMode:1,
   				forceParse: false,
   				language: "zh-CN",
   				format: "yyyy-mm"
   			});
   			
   			$("#searchSalary").click(function() {
   				var year = $("#search_form_slary_list input[name='paramMap.date']").val().split("-")[0];
	  			var month = $("#search_form_slary_list input[name='paramMap.date']").val().split("-")[1]
	  			if(!year) {
	  				var currentDate = new Date();
	  				year = currentDate.getFullYear();
	  			}
	  			if(!month) {
	  				var currentDate = new Date();
	  				month = currentDate.getMonth() - 1;
	  			}
   				$("#salarydatagrid").setGridParam({
   					postData: {
   						"paramMap.date": $("#search_form_slary_list input[name='paramMap.date']").val()
   					}
   				}).trigger("reloadGrid");
   			});
   			
   			$("#resetSearchSalary").click(function() {
   				$("#search_form_slary_list")[0].reset();
   				$("#search_form_slary_list select.select2").each(function() {
   					$(this).val("").trigger("change");
   				});
   			});
   			
   			salaryGrid = $("#salarydatagrid").jqGrid({
                url: getRoot() + "workflow/salary/queryPage.action",
                mtype: "POST",
                rownumbers: true,
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
                height: getHeight(400)+"px",
				styleUI : "Bootstrap",
                datatype: "json",
                footerrow: true,
                userDataOnFooter: true,
                loadComplete: function() {
                	//$('.ui-jqgrid-bdiv').scrollTop(0);
                	//$(this).setJqGridRowHeight(28);
                },
                gridComplete: function() {
                	//$("#salarydatagrid").setGridWidth($("#salary_list").width()*0.99);
                	//$("#salarydatagrid").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
                	//$("#salarydatagrid").jqGrid("setFrozenColumns");
                	
                	//$(".ui-jqgrid-sdiv").show();
                	var supposedSum = $(this).getCol("supposedSum",false,"sum");
                	var actualSum = $(this).getCol("actualSum",false,"sum");
                	var travelAllowanceSum = $(this).getCol("travelAllowanceSum",false,"sum");
                	var mealSubsidySum = $(this).getCol("mealSubsidySum",false,"sum");
                	var telChargeSum = $(this).getCol("telChargeSum",false,"sum");
                	var housingSubsidySum = $(this).getCol("housingSubsidySum",false,"sum");
                	var benefitSum = $(this).getCol("benefitSum",false,"sum");
                	var socialInsuranceSum = $(this).getCol("socialInsuranceSum",false,"sum");
                	var healthInsuranceSum = $(this).getCol("healthInsuranceSum",false,"sum");
                	var insuranceSum = $(this).getCol("insuranceSum",false,"sum");
                	var totalSum = $(this).getCol("totalSum",false,"sum");
                	$(this).footerData("set", {
                		"departmentName": "合计",
                		"supposedSum": supposedSum.toFixed(2),
                		"actualSum": actualSum.toFixed(2),
                		"travelAllowanceSum": travelAllowanceSum.toFixed(2),
                		"mealSubsidySum": mealSubsidySum.toFixed(2),
                		"telChargeSum": telChargeSum.toFixed(2),
                		"housingSubsidySum": housingSubsidySum.toFixed(2),
                		"benefitSum": benefitSum.toFixed(2),
                		"socialInsuranceSum": socialInsuranceSum.toFixed(2),
                		"healthInsuranceSum": healthInsuranceSum.toFixed(2),
                		"insuranceSum": insuranceSum.toFixed(2),
                		"totalSum": totalSum.toFixed(2)
                	});
                	//setTimeout("hackHeight(\"#salarydatagrid\")", 0);
                	//hackHeight("#salarydatagrid");
                },
                colModel: [{
                	label: "薪资年月", name: "year", width: 80, align: "center",/* frozen: true,*/
                	formatter: function(cellValue, options, rowObject) {
                		return rowObject.year+"-"+rowObject.month;
                	}
                }, {
                	label: "部门", name: "departmentName", width: 130, align: "center"/*, frozen: true*/
                }, {
                	label: "应发合计", name: "supposedSum", width: 80, align: "center"
                }, {
                	label: "实发工资", name: "actualSum", width: 80, align: "center"
                }, {
                	label: "车补", name: "travelAllowanceSum", width: 80, align: "center"
                }, {
                	label: "餐补", name: "mealSubsidySum", width: 80, align: "center"
                }, {
                	label: "话补", name: "telChargeSum", width: 80, align: "center"
                }, {
                	label: "住房补贴", name: "housingSubsidySum", width: 80, align: "center"
                }, {
                	label: "福利合计", name: "benefitSum", width: 80, align: "center"
                }, {
                	label: "社保", name: "socialInsuranceSum", width: 80, align: "center"
                }, {
                	label: "医保", name: "healthInsuranceSum", width: 80, align: "center"
                }, {
                	label: "总投保", name: "insuranceSum", width: 80, align: "center"
                }, {
                	label: "总合计", name: "totalSum", width: 80, align: "center"
                }, {
                	label: "流程状态", name: "flowStatus", width: 80, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		return getFlowStatus(rowObject.status);
                	}
                }, {
                	label: "当前审核人", name: "auditors", width: 100, align: "center",
                	formatter: function(cellValue, options, rowObject) {
                		var flowStatus = rowObject.allFlowStatus;
                		var text = cellValue;
                		if(flowStatus) {
                			var statusArr = flowStatus.split(",");
                			for(var i = 0; i < statusArr.length; i ++) {
                				var status = statusArr[i];
                				if(5 == status) {
                					text = "";
                					break;
                				}
                			}
                		}
                		return text;
                	}
                }, {
                	label: "操作", formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewSalary("+rowObject.id+", \""+rowObject.allFlowStatus+"\", "+rowObject.status+")");
                		if(rowObject.status == 0 || rowObject.status == 1 || rowObject.status == 2) {
                			buttons = buttons + createBtn("修改", "btn-info btn-xs", "fa-pencil", "editSalary("+rowObject.id+")");
                			buttons = buttons + createBtn("提交", "btn-success btn-xs", "fa-share", "commitSalary("+rowObject.id+")");
                			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delSalary("+rowObject.id+")");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                rowNum: 20,
                pager: "#salarydatagridpager"
            });
            
            $("#salarydatagrid").jqGrid("setGroupHeaders", {
   				useColSpanStyle: true,
   				groupHeaders:[{
   					startColumnName: "travelAllowanceSum", numberOfColumns: 4, titleText: "补贴"
   				}, {
   					startColumnName: "socialInsuranceSum", numberOfColumns: 3, titleText: "公司投保"
   				}]
   			});
   			
   			$("#createSalary").click(function() {
   				BootstrapDialog.confirm({
		            title: "温馨提示",
		            message: "您确定创建上月工资吗？",
		            type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
		            closable: true, // <-- Default value is false
		            draggable: true, // <-- Default value is false
		            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
		            btnOKLabel: "确定", // <-- Default value is 'OK',
		            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
		            callback: function(y) {
		                // result will be true if button was click, while it will be false if users close the dialog directly.
		                if(y) {
		   					$.ajax({
		   						url: getRoot() + "workflow/salary/createSalary.action",
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
			    						salaryGrid.jqGrid().trigger("reloadGrid");
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
   			});
   			
   			$("#exportSalary").click(function() {
	  			var year = $("#search_form_slary_list input[name='paramMap.date']").val().split("-")[0];
	  			var month = $("#search_form_slary_list input[name='paramMap.date']").val().split("-")[1]
	  			if(!year) {
	  				var currentDate = new Date();
	  				year = currentDate.getFullYear();
	  			}
	  			if(!month) {
	  				var currentDate = new Date();
	  				month = currentDate.getMonth();
	  				if(month == 0) {
	  					month = 12;
	  					year = year - 1;
	  				}
	  			}
	  			BootstrapDialog.confirm({
		            title: "温馨提示",
		            message: "确定导出<font color='red'>"+year+"年"+month+"月</font>的工资吗？",
		            type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
		            closable: true, // <-- Default value is false
		            draggable: true, // <-- Default value is false
		            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
		            btnOKLabel: "确定", // <-- Default value is 'OK',
		            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
		            callback: function(y) {
		                // result will be true if button was click, while it will be false if users close the dialog directly.
		                if(y) {
		                	var form = $("<form>");
		  					form.attr("style", "display:none");
		  					form.attr("action", getRoot() + "workflow/salary/export.action");
		  					form.attr("method", "POST");
		  					var input = $("<input>");
		  					input.attr("value", year);
		  					input.attr("name", "year");
		  					var monthInput = $("<input>");
		  					monthInput.attr("value", month);
		  					monthInput.attr("name", "month");
		  					$("body").append(form);
		  					form.append(input);
		  					form.append(monthInput);
		  					form.submit();
		  					form.remove();
		                }
		            }
	  			});
   			});
   		});
   		
   		function editSalary(id) {
   			//addTab("编辑薪资", "editSalary_"+id, getRoot() + "workflow/salary/queryById.action?returnType=edit&salary.id="+id);
   			salaryDialog = BootstrapDialog.show({
			    title: "编辑薪资",
			    width: "99%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/salary/queryById.action?returnType=edit&salary.id="+id),
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
			    			var $grid = dialog.getModalBody().find("form").find("table#salaryContentDatagrid_edit");
			    			//将未保存的先保存
			    			jQuery("#salaryContentDatagrid_edit").jqGrid("saveRow", salaryLastSel2, false, "clientArray");
			    			var contents = getSalaryContents($grid);
			    			if(!contents) {
			    				return false;
			    			}
			    			var form = dialog.getModalBody().find("form");
			    			var $button = this;
			    			$.ajax({
			    				url: getRoot() + "workflow/salary/update.action",
			    				data: form.serialize()+"&"+contents.join("&"),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						//dialog.close();
			    						//salaryGrid.jqGrid().trigger("reloadGrid");
			    						jQuery("#salaryContentDatagrid_edit").jqGrid().trigger("reloadGrid");
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
			    			alert(e);
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "提交",
			    	icon: "fa fa-fw fa-share",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		var $button = this;
			    		try {
			    			BootstrapDialog.confirm({
					            title: "温馨提示",
					            message: "您确定提交吗？",
					            type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
					            closable: true, // <-- Default value is false
					            draggable: true, // <-- Default value is false
					            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
					            btnOKLabel: "确定", // <-- Default value is 'OK',
					            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
					            callback: function(y) {
					                // result will be true if button was click, while it will be false if users close the dialog directly.
					                if(y) {
					   					var $grid = dialog.getModalBody().find("form").find("table#salaryContentDatagrid_edit");
						    			//将未保存的先保存
						    			jQuery("#salaryContentDatagrid_edit").jqGrid("saveRow", salaryLastSel2, false, "clientArray");
						    			var contents = getSalaryContents($grid);
						    			if(!contents) {
						    				return false;
						    			}
						    			var form = dialog.getModalBody().find("form");
						    			
						    			$.ajax({
						    				url: getRoot() + "workflow/salary/commit.action",
						    				data: form.serialize()+"&"+contents.join("&"),
						    				type: "POST",
						    				success: function(data) {
						    					var json = eval("("+data+")");
						    					if(json.success) {
						    						BootstrapDialog.success(json.msg);
						    						dialog.close();
						    						salaryGrid.jqGrid().trigger("reloadGrid");
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
					   				}
					            }
				   			});
			    			
			    		} catch(e) {
			    			alert(e);
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
   		
   		function commitSalary(id) {
   			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定提交吗？",
	            type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	   					$.ajax({
	   						url: getRoot() + "workflow/salary/commit.action?type=commit",
		    				data: "salary.id="+id,
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
		    						salaryGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function delSalary(id) {
   			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定删除吗？",
	            type: BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	   					$.ajax({
	   						url: getRoot() + "workflow/salary/del.action",
		    				data: "salary.id="+id,
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
		    						salaryGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function viewSalary(id, allFlowStatus, status) {
   			var returnType = "view";
   			
   			salaryDialog = BootstrapDialog.show({
			    title: "工资详情",
			    width: "99%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "workflow/salary/queryById.action?returnType="+returnType+"&salary.id="+id),
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
   		
   		function getSalaryContents($grid) {
   			if(!$grid || 0 >= $grid.length) {
   				BootstrapDialog.error("获取工资项目异常");
   				return false;
   			}
   			
   			var rows = $grid.getRowData();
   			var contents = new Array();
   			var param = "contents[#index#].#prop#=#value#";
   			if(!rows && 0 >= rows.length) {
   				BootstrapDialog.error("请至少添加一项项目");
   				return false;
   			}
   			for(var i = 0; i < rows.length; i ++) {
   				var content = rows[i];
   				if(content.id.indexOf("_sum") >= 0) {
   					continue;
   				}
   				for(var name in content) {
   					if(!name || ""==$.trim(name) || "operationCell" == name) {
   						continue;
   					}
   					var value = content[name];
   					if("id" == name) {
   						var re = /^[0-9]*$/;
   						if(!re.test(value)) {
   							value = "";
   						}
   					}
   					//var value = content[name];
   					contents.push(param.replace("#index#", i).replace("#prop#", name).replace("#value#", value));
   				}
   			}
   			if(0 >= contents.length) {
   				BootstrapDialog.danger("获取到的项目为空");
   				return false;
   			}
   			console.log(contents);
   			return contents;
   		}
   	</script>
  </body>
</html>
