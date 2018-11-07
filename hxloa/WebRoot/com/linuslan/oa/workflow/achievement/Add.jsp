<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新增绩效申请</title>
    
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
   	<form id="addAchievementForm" action="" class="form-horizontal">
   		<input type="hidden" name="type" value="update">
    	<div class="box-body padding-bottom5 bottom-dotted-border" >
    		<div class="form-group">
				<label for="text" class="col-md-2 col-sm-4 control-label">日期：</label>
				<div class="col-md-3 col-sm-8 no-padding">
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</div>
						<input readonly="readonly" name="date" type="text" class="form-control pull-right date">
					</div>
				</div>
				<div class="col-md-1 col-sm-12 no-padding"></div>
				<label for="text" class="col-md-2 col-sm-4 control-label">总分：</label>
				<div class="col-md-3 col-sm-8 totalScore left-label">
					0
				</div>
			</div>
    	</div>
    	<div class="toolbar with-border">
    		<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="复制" id="addAchievementContent_copy"><i class="fa fa-fw fa-clone"></i>复制</button>
			<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增绩效项目" id="addAchievementContent_add"><i class="fa fa-fw fa-plus"></i>新增</button>
		</div>
    	<div class="box-body">
    		<table id="achievementContentDatagrid_add"></table>
    	</div>
   	</form>
   	<script type="text/javascript">
   		var achievementDeptCombotree;
   		$(function() {
   			$("#addAchievementForm").find(".date").datepicker({
   				startView: 1,
   				maxViewMode: 1,
   				minViewMode:1,
   				forceParse: false,
   				language: "zh-CN",
   				format: "yyyy-mm"
   			});
   			$("#addAchievementForm").find(".select2").select2();
   			var lastDate;
   			$("#addAchievementForm").find(".date").change(function() {
   				var date = $("#addAchievementForm").find(".date").val();
   				if(!lastDate || (lastDate && date != lastDate)) {
   					$.ajax({
	   					url: getRoot() + "workflow/achievement/checkExistByDate.action",
	   					data: {
	   						"date": $("#addAchievementForm").find(".date").val()
	   					},
	   					type: "POST",
	   					success: function(data) {
	   						var json = eval("("+data+")");
	   						if(json.success == false) {
	   							BootstrapDialog.danger(json.msg);
	   						}
	   					},
	   					error: function() {
	   						BootstrapDialog.danger("系统异常，请联系管理员");
	   					}
	   				});
   				}
   				lastDate = date;
   			});
   			
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateAchievementGrid_add()", 0);
            
            $("#addAchievementContent_add").click(function() {
            	var rowId = Math.random();
            	$("#achievementContentDatagrid_add").addRowData(rowId, {
            		id: "",
            		title: "",
            		content: "",
            		source: "",
            		formula: "",
            		scoreWeight: ""
            	});
            });
            
            $("#addAchievementContent_copy").click(function() {
            	var date = $("#addAchievementForm").find(".date").val();
            	if(!date || "" == $.trim(date)) {
            		BootstrapDialog.danger("请选择需要创建绩效的日期");
            		return;
            	}
            	BootstrapDialog.show({
            		title: "选择复制绩效的日期",
            		message: $("<div></div>").load(getRoot() + "com/linuslan/oa/workflow/achievement/copyDateSelect.jsp"),
            		draggable: true,
    			    autodestroy: true,
    			    closeByBackdrop: false,
    			    autospin: true,
    			    onshown: function(dialogRef) {
    			    	
    			    },
    			    buttons: [{
    			    	label: "确定",
    			    	icon: "fa fa-fw fa-save",
    			    	cssClass: "btn-success",
    			    	action: function(dialog) {
    			    		var oldDate = dialog.getModalBody().find("form").find(".date").val();
    			    		if(!oldDate || "" == $.trim(oldDate)) {
    			    			BootstrapDialog.danger("请选择复制绩效的日期");
    			    			return;
    			    		}
    			    		$.ajax({
    			    			url: getRoot() + "workflow/achievement/copy.action",
    			    			data: {
    			    				"oldDate": oldDate,
    			    				"date": date
    			    			},
    			    			type: "POST",
    			    			success: function(data) {
    			    				var json = eval("("+data+")");
    			    				if(json.success == true) {
    			    					BootstrapDialog.success(json.msg+"，复制成功");
    			    					dialog.close();
    			    					achievementDialog.close();
			    						achievementGrid.jqGrid().trigger("reloadGrid");
    			    				} else {
    			    					BootstrapDialog.danger(json.msg);
    			    				}
    			    			},
    			    			error: function() {
    			    				BootstrapDialog.danger("系统异常，请联系管理员");
    			    			}
    			    		});
    			    	}
    			    }, {
    			    	label: "取消",
    			    	icon: "fa fa-fw fa-close",
    			    	cssClass: "btn-danger",
    			    	action: function(dialog) {
    			    		dialog.close();
    			    	}
    			    }]
            	});
            });
   		});
   		var rowDatas;
   		function generateAchievementGrid_add() {
   			$("#achievementContentDatagrid_add").jqGrid({
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "local",
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "achievementClassName": "d", "achievementClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "考核项目", name: "title", editable: true, edittype: "text", width: 300, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.title);
                	}
                }, {
                	label: "具体指标", name: "content", editable: true, width: 350, align: "left", edittype: "custom",
                	editoptions:{
                		rows: "10",
                		width: "100%",
                		custom_element: createTextareaBox,
                		custom_value: operateTextareaValue,
                		name: "content"
                	},
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.content);
                	}
                }, {
                	label: "数据来源", name: "source", width: 350, align: "left", editable: true, edittype: "custom",
                	editoptions: {
                		rows:"10",
                		width:"100%",
                		custom_element: createTextareaBox,
                		custom_value: operateTextareaValue,
                		name: "source"
                	},
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.source);
                	}
                }, {
                	label: "标准", name: "formula", width: 350, align: "left", editable: true, edittype: "custom",
                	editoptions: {
                		rows:"10",
                		width:"100%",
                		custom_element: createTextareaBox,
                		custom_value: operateTextareaValue,
                		name: "formula"
                	},
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.formula);
                	}
                }, {
                	label: "权重", name: "scoreWeight", width: 100, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "scoreWeight"
                	}
                }, {
                	label: "操作", name: "operationCell", align: "center", width: 70, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		var rowId = options.rowId;
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delAddAchievementContent(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                ondblClickRow: function(id){
                	if(id && id!==achievementLastSel2){
                		//jQuery("#achievementContentDatagrid_add").saveRow(achievementLastSel2, false, "clientArray");
                		jQuery("#achievementContentDatagrid_add").saveRow(achievementLastSel2, {
                			url: "clientArray",
                			aftersavefunc: function() {
                				getAddAchievementTotalScore();
                			}
                		});
                		jQuery("#achievementContentDatagrid_add").editRow(id,true);
                		achievementLastSel2=id;
                	}
			    },
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20
            });
   		}
		
		function delAddAchievementContent(id, rowId) {
			BootstrapDialog.confirm({
	            title: "温馨提示",
	            message: "您确定删除吗？",
	            type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
	            callback: function(y) {
	            	
	                // result will be true if button was click, while it will be false if users close the dialog directly.
	                if(y) {
	                	if(id && ""!=$.trim(id) && isNumber(id)) {
	                		$.ajax({
		   						url: getRoot() + "workflow/achievement/delContentById.action",
			    				data: "achievementContent.id="+id,
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
			    						$("#achievementContentDatagrid_add").delRowData(rowId);
			    						getAddAchievementTotalScore();
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#achievementContentDatagrid_add").delRowData(rowId);
		            		getAddAchievementTotalScore();
		            	}
	   				}
	            }
   			});
		}
		
		/**
		 * 创建金额输入框
		 */
		function createNumberBox(value, options) {
			if(!value) {
				value = 0;
			}
			var name = options.name;
			
			//创建外围的div
			var div = document.createElement("div");
			div.setAttribute("class", "input-group spinner");
			div.setAttribute("style", "width: 99%");
			
			//创建输入框
			var input = document.createElement("input");
			div.appendChild(input);
			input.setAttribute("name", name);
			input.setAttribute("type", "number");
			input.setAttribute("class", "form-control");
			input.setAttribute("style", "width: 99%");
			input.setAttribute("value", value);
			
			return div;
		}
		
		function operateNumberValue(elem, operation, value) {
			if(operation == "get") {
				return $("input", elem).val();
			} else {
				$("input", elem).val(value);
			}
		}
		
		function getAddAchievementTotalScore() {
			var rowDatas = $("#achievementContentDatagrid_add").getRowData();
			var totalScore = 0;
			if(rowDatas && 0 < rowDatas.length) {
				var rowData;
				for(var i = 0; i < rowDatas.length; i ++) {
					rowData = rowDatas[i];
					try {
						if(rowData.scoreWeight) {
							var score = parseInt(rowData.scoreWeight);
							if(score) {
								totalScore += score;
							}
						}
					} catch(ex) {
						BootstrapDialog.danger("第"+(i+1)+"项项目分数填写错误");
						break;
					}
				}
			}
			$("#addAchievementForm").find("div.totalScore").html(totalScore);
		}
   	</script>
  </body>
</html>
