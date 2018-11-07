<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>待登录用户审核的绩效列表</title>
    
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
    <div id="salary_auditlist">
   		<div class="col-xs-12 no-padding">
   			<div class="box box-solid">
   				<div class="box-header with-border">
   					<h3 class="box-title">待审薪资</h3>
   					<div class="box-tools pull-right">
   						<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
	               	</div>
   				</div>
   				<div class="box-body">
   					<table id="salaryAuditDatagrid"></table>
   					<div id="salaryAuditDatagridpager"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var auditSalaryGrid;
   		var scoreSalaryGrid;
   		var auditSalaryDialog;
   		var salaryAuditlastsel2;
   		$(function() {
   			auditSalaryGrid = $("#salaryAuditDatagrid").jqGrid({
                url: getRoot() + "workflow/salary/queryAuditPage.action",
                mtype: "POST",
                rownumbers: true,
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                footerrow: true,
                userDataOnFooter: true,
                gridComplete: function() {
                	/*$("#salaryAuditDatagrid").setGridWidth($("#salary_auditlist").width()*0.99);
                	$("#salaryAuditDatagrid").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "auto"});
                	$("#salaryAuditDatagrid").jqGrid("setFrozenColumns");
                
                	$(".ui-jqgrid-sdiv").show();*/
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
                	//setTimeout("hackHeight(\"#salaryAuditDatagrid\")", 0);
                },
                colModel: [{
                	label: "薪资年月", name: "date", width: 80, align: "center", frozen: true,
                	formatter: function(cellvalue, options, rowObject) {
                		return rowObject.year+"-"+rowObject.month;
                	}
                }, {
                	label: "归属部门", name: "departmentName", width: 130, align: "center", frozen: true
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
                	label: "操作", width: 100, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		buttons = buttons + createBtn("查看", "btn-info btn-xs", "fa-file-text-o", "viewAuditSalary("+rowObject.id+", "+rowObject.passBtn+", "+rowObject.rejectBtn+")");
                		if(true == rowObject.passBtn) {
                			buttons = buttons + createBtn("通过", "btn-success btn-xs", "fa-share", "auditSalary("+rowObject.id+", 0)");
                		}
                		if(true == rowObject.rejectBtn) {
                			buttons = buttons + createBtn("退回", "btn-danger btn-xs", "fa-reply", "auditSalary("+rowObject.id+", 1)");
                		}
   						return buttons;
                	}
                }],
				viewrecords: true,
                height: getHeight(400)+"px",
                //width: "100%",
                rowNum: 20,
                pager: "#salaryAuditDatagridpager"
            });
            
            $("#salaryAuditDatagrid").jqGrid("setGroupHeaders", {
   				useColSpanStyle: true,
   				groupHeaders:[{
   					startColumnName: "travelAllowanceSum", numberOfColumns: 4, titleText: "补贴"
   				}, {
   					startColumnName: "socialInsuranceSum", numberOfColumns: 3, titleText: "公司投保"
   				}]
   			});
   		});
   		
   		function viewAuditSalary(id, passBtn, rejectBtn, allFlowStatus) {
   			var returnType = "audit";
   			var passCss = "";
   			var rejectCss = "";
   			if(true != passBtn) {
   				passCss = "hidden";
   			}
   			if(true != rejectBtn) {
   				rejectCss = "hidden";
   			}
   			
   			auditSalaryDialog = BootstrapDialog.show({
			    title: "详情",
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
			    	label: "通过",
			    	icon: "fa fa-fw fa-share",
			    	cssClass: "btn-success "+passCss,
			    	action: function(dialog) {
			    		try {
			    			var $button = this;
			    			var $form = dialog.getModalBody().find("form");
		    				var $contentOpinions = dialog.getModalBody().find("table.contentOpinion");
		    				var contentOpinions = getContentOpinions($contentOpinions);
		    				commitAuditSalary(0, $form.serialize()+"&"+contentOpinions.join("&"), dialog, $button);
			    			
			    		} catch(ex) {
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "退回",
			    	icon: "fa fa-fw fa-reply",
			    	cssClass: "btn-danger "+rejectCss,
			    	action: function(dialog) {
			    		try {
			    			var $button = this;
			    			var $form = dialog.getModalBody().find("form");
			    			var $contentOpinions = dialog.getModalBody().find("table.contentOpinion");
			    			var contentOpinions = getContentOpinions($contentOpinions);
			    			commitAuditSalary(1, $form.serialize()+"&"+contentOpinions.join("&"), dialog, $button)
			    		} catch(ex) {
			    			dialog.enableButtons(true);
		    				dialog.setClosable(true);
			    			BootstrapDialog.danger("系统异常，请联系管理员！");
			    		}
			    	}
			    }, {
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-warning",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
   		}
   		
   		/**
   		 * 审核方法
   		 */
   		function auditSalary(id, passType) {
   			if(!id) {
   				BootstrapDialog.danger("ID无效");
   				return false;
   			}
   			var param = "salary.id="+id;
   			commitAuditSalary(passType, param);
   		}
   		
   		/**
   		 * 将传进来的参数提交到server端
   		 * @param passType 操作类型 0:通过；1：退回
   		 * @param params 传递的参数
   		 * @param dialog 打开的窗口
   		 * @param $button 点击的按钮
   		 */
   		function commitAuditSalary(passType, params, dialog, $button) {
   			var tips = "";
   			if(passType == 0) {
   				tips = "通过";
   			} else {
   				tips = "退回";
   			}
   			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定"+tips+"申请吗？",
	            type: passType == 0 ? BootstrapDialog.TYPE_SUCCESS : BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	   					$.ajax({
	   						url: getRoot() + "workflow/salary/audit.action",
		    				data: "passType="+passType+"&"+params,
		    				type: "POST",
		    				beforeSend: function() {
		    					if(dialog) {
		    						dialog.enableButtons(false);
			    					dialog.setClosable(false);
		    					}
		    					if($button) {
		    						$button.spin();
		    					}
		    				},
		    				complete: function() {
		    					if(dialog) {
		    						dialog.enableButtons(true);
			    					dialog.setClosable(true);
		    					}
		    					if($button) {
		    						$button.stopSpin();
		    					}
		    				},
		    				success: function(data) {
		    					var json = eval("("+data+")");
		    					if(json.success) {
		    						BootstrapDialog.success(json.msg);
		    						if(dialog) {
		    							dialog.close();
		    						}
		    						auditSalaryGrid.jqGrid().trigger("reloadGrid");
		    						scoreSalaryGrid.jqGrid().trigger("reloadGrid");
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
   		
   		function getContentOpinions($contentOpinions) {
   			var param = "contentOpinions[#index#].#prop#=#value#";
   			var contentOpinions = new Array();
   			if($contentOpinions && $contentOpinions.length > 0) {
   				for(var i = 0; i < $contentOpinions.length; i ++) {
   					var $contentOpinion = $($contentOpinions[i]);
   					var contentId = $contentOpinion.find("input[name=contentId]").val();
   					var opinion = $contentOpinion.find("textarea[name=contentOpinion]").val();
   					if(contentId && "" != $.trim(contentId) && opinion && "" != $.trim(opinion)) {
   						contentOpinions.push(param.replace("#index#", i).replace("#prop#", "contentId").replace("#value#", contentId));
   						contentOpinions.push(param.replace("#index#", i).replace("#prop#", "opinion").replace("#value#", opinion));
   					}
   				}
   			}
   			return contentOpinions;
   		}
   		
   		function getLeaderScoreContent($grid) {
   			if(!$grid || 0 >= $grid.length) {
   				BootstrapDialog.error("获取报销项目异常");
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
   			return contents;
   		}
   	</script>
  </body>
</html>
