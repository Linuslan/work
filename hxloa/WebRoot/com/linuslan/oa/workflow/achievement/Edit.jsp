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
    
    <title>编辑绩效申请</title>
    
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
   	<form id="editAchievementForm" action="" class="form-horizontal">
   		<input id="editAchievementId" class="achievementId" name="achievement.id" type="hidden" value="${achievement.id }" />
   		<input type="hidden" name="type" value="update">
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#achievement" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#achievementAuditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="achievement">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">时间：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input readonly="readonly" name="date" value="${achievement.year }-${achievement.month}" type="text" class="form-control pull-right date">
								</div>
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">总分：</label>
							<div class="col-md-3 col-sm-8 left-label totalScore">
								${achievement.totalScore }
							</div>
						</div>
			    	</div>
			    	<div class="toolbar with-border">
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增绩效项目" id="addAchievementContent_edit"><i class="fa fa-fw fa-plus"></i>新增</button>
					</div>
			    	<div class="box-body">
			    		<table id="achievementContentDatagrid_edit"></table>
			    	</div>
			    </div>
			   	<div class="tab-pane" id="achievementAuditorLogs">
   					<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="editachievementauditorlogsdatagrid"></table>
	   							<div id="editachievementauditorlogsdatagridpager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		var achievementDeptCombotree_edit;
   		$(function() {
   			$("#editAchievementForm").find(".select2").select2();
   			$("#editAchievementForm").find(".date").datepicker({
   				startView: 1,
   				maxViewMode: 1,
   				minViewMode:1,
   				forceParse: false,
   				language: "zh-CN",
   				format: "yyyy-mm"
   			});
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateAchievementGrid_edit()", 0);
            setTimeout("generateEditAchievementLogGrid()", 0);
            $("#addAchievementContent_edit").click(function() {
            	var rowId = Math.random();
            	$("#achievementContentDatagrid_edit").addRowData(rowId, {
            		id: "",
            		title: "",
            		content: "",
            		source: "",
            		formula: "",
            		scoreWeight: ""
            	});
            });
            var lastDate;
            $("#editAchievementForm").find(".date").change(function() {
   				var date = $("#editAchievementForm").find(".date").val();
   				if(!lastDate || (lastDate && date != lastDate)) {
   					$.ajax({
	   					url: getRoot() + "workflow/achievement/checkExistByDate.action",
	   					data: {
	   						"date": $("#editAchievementForm").find(".date").val(),
	   						"achievement.id": $("#editAchievementId").val()
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
   		});
   		var rowDatas;
   		function generateAchievementGrid_edit() {
   			var id = $("#editAchievementForm").find("input.achievementId").val();
   			$("#achievementContentDatagrid_edit").jqGrid({
   				url: getRoot() + "workflow/achievement/queryContentsByAchievementId.action?achievement.id="+id,
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
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
                	editoptions: {
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
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delEditAchievementContent(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                ondblClickRow: function(id){
                	if(id && id!==achievementLastSel2){
                		jQuery("#achievementContentDatagrid_edit").saveRow(achievementLastSel2, {
                			url: "clientArray",
                			aftersavefunc: function() {
                				getEditAchievementTotalScore();
                			}
                		});
                		jQuery("#achievementContentDatagrid_edit").editRow(id,true);
                		achievementLastSel2=id;
                	}
			    },
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20,
                subGrid: true,
                subGridOptions: {
                	expandOnLoad: false
                },
                subGridRowColapsed: function(subgrid_id, rowid) {
                	return true;
                },
                subGridRowExpanded: function(subgrid_id, rowid) {
                	var rowData = $("#achievementContentDatagrid_edit").getRowData(rowid);
                	var id = rowData.id;
                	if(id) {
                		var tableId = subgrid_id+"_table";
                		var html = "<div style='padding: 10px;'>";
                		html = html + "<table id='"+tableId+"' class='scroll'></table>";
                		html = html + "</div>";
                		var subgrid = $("#"+subgrid_id).html(html);
                		$("#"+tableId).jqGrid({
                			url: getRoot() + "workflow/achievement/queryContentOpinionsByContentId.action?achievementContent.id="+id,
                			datatype: "json",
                			height: "100%",
                			shrinkToFit: true,
			                autowidth: true,
			                scrollrows: false,
			                scroll: false,
                			colModel: [{
			                	label: "ID", name: "id", hidden: true
			                }, {
			                	label: "创建审核人", name: "userName", width: 150, align: "center"
			                }, {
			                	label: "创建审核意见", name: "opinion", width: 400, align: "center"
			                }, {
			                	label: "创建审核时间", name: "createDate", width: 150, align: "center"
			                }]
                		});
                	}
                }
            });
   		}
   		
   		function generateEditAchievementLogGrid() {
   			$("#editachievementauditorlogsdatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=achievement&wfId="+$("#editAchievementId").val(),
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
                	label: "创建审核人", name: "auditorName", width: 150, align: "center"
                }, {
                	label: "创建审核时间", name: "auditDate", width: 200, align: "center"
                }, {
                	label: "创建审核意见", name: "opinion", width: 400, align: "center"
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
                pager: "#editachievementauditorlogsdatagridpager"
            });
   		}
		
		function delEditAchievementContent(id, rowId) {
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
			    						$("#achievementContentDatagrid_edit").delRowData(rowId);
			    						getEditAchievementTotalScore();
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#achievementContentDatagrid_edit").delRowData(rowId);
		            		getEditAchievementTotalScore();
		            	}
	   				}
	            }
   			});
		}
		
		function getEditAchievementTotalScore() {
			var rowDatas = $("#achievementContentDatagrid_edit").getRowData();
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
			$("#editAchievementForm").find("div.totalScore").html(totalScore);
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
   	</script>
  </body>
</html>
