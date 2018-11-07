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
    
    <title>编辑报销申请</title>
    
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
   	<form id="editReimburseForm" action="" class="form-horizontal">
   		<input id="editReimburseId" class="reimburseId" name="reimburse.id" type="hidden" value="${reimburse.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#reimburse" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#reimburseAuditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="reimburse">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-2 col-sm-4 control-label">出款公司：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<select class="select2" name="reimburse.companyId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${companys }" var="company">
										<option value="${company.id }" ${company.id == reimburse.companyId ? "selected='selected'" : "" }>${company.name }</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label">编号：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<input name="reimburse.serialNo" type="text" value="${reimburse.serialNo }" readonly="readonly" class="form-control showText" id="text">
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2 col-sm-4 control-label">报销部门：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<div class="reimburseDept" value="${reimburse.reimburseDeptId }" text="${reimburse.reimburseDeptName }"></div>
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="departmentId" class="col-md-2 col-sm-4 control-label">收款人：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<div class="input-group">
									<input name="reimburse.receiver" type="text" value="${reimburse.receiver }" class="form-control required" id="text">
									<span class="input-group-btn">
										<button class="btn btn-success btn-flat" type="button" onclick="selectReimburseAccount_edit()"><i class="fa fa-search"></i></button>
									</span>
								</div>
								<!-- <input name="reimburse.receiver" type="text" value="${reimburse.receiver }" class="form-control required" id="text"> -->
							</div>
						</div>
						<div class="form-group">
							<label for="moneyDate" class="col-md-2 col-sm-4 control-label">开户行：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<input name="reimburse.bank" type="text" value="${reimburse.bank }" class="form-control pull-right">
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="groupId" class="col-md-2 col-sm-4 control-label">账号：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								<input name="reimburse.bankNo" type="text" class="form-control pull-right" value="${reimburse.bankNo }">
							</div>
						</div>
						<div class="form-group padding-bottom5">
							<label for="orderNo" class="col-md-2 col-sm-4 control-label">总金额：</label>
							<div class="col-md-3 col-sm-8 no-padding">
								${reimburse.totalMoney }
							</div>
							<div class="col-md-1 col-sm-12 no-padding"></div>
							<label for="text" class="col-md-2 col-sm-4 control-label"></label>
							<div class="col-md-3 col-sm-8 no-padding">
							</div>
						</div>
			    	</div>
			    	<div class="toolbar with-border">
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增报销项目" id="addReimburseContent_edit"><i class="fa fa-fw fa-plus"></i>新增</button>
					</div>
			    	<div class="box-body">
			    		<table id="reimburseContentDatagrid_edit"></table>
			    	</div>
			    </div>
			   	<div class="tab-pane" id="reimburseAuditorLogs">
   					<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="editreimburseauditorlogsdatagrid"></table>
	   							<div id="editreimburseauditorlogsdatagridpager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		var reimburseDeptCombotree_edit;
   		var lastsel2 = "";
   		$(function() {
   			$("#editReimburseForm").find(".select2").select2();
   			reimburseDeptCombotree_edit = $("#editReimburseForm").find(".reimburseDept").combotree({
				url: getRoot() + "sys/department/queryTree.action",
				async: true,
				singleSelect: true,
				loadOnExpand: false,
				loadParams: {
					"id": "department.id"
				},
				idField: "id",
				textField: "name",
				name: "reimburse.reimburseDeptId",
				pidField: "pid",
				onRowClick: function() {
					$("#reimburseContentDatagrid_edit").saveRow(lastsel2, false, "clientArray");
					var rowDatas = $("#reimburseContentDatagrid_edit").getRowData();
					//$("#reimburseContentDatagrid_edit").clearGridData();
					var rowData;
					selected = new Object();
					for(var i = 0; i < rowDatas.length; i ++) {
						rowData = rowDatas[i];
						rowData.reimburseClassId="";
						rowData.reimburseClassName="";
						var id = rowData.id;
						//$("#reimburseContentDatagrid_edit").addRowData(id, rowData);
						$("#reimburseContentDatagrid_edit").setRowData(id, rowData);
					}
				}
			});
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateGrid()", 0);
            setTimeout("generateEditReimburseLogGrid()", 0);
            $("#addReimburseContent_edit").click(function() {
            	if(lastsel2){
               		jQuery("#reimburseContentDatagrid_edit").saveRow(lastsel2, false, "clientArray");
               	}
            	var reimburseDeptId = reimburseDeptCombotree_edit.getValue();
				if(!reimburseDeptId) {
					BootstrapDialog.danger("请先选择报销部门");
					return false;
				}
            	var rowId = Math.random();
            	/*var parameters = {
					rowID : rowId,
					initdata : {},
					position :"first",
					useDefValues : false,
					useFormatter : false,
					addRowParams : {extraparam:{}}
				}
            	$("#reimburseContentDatagrid_edit").jqGrid("addRow",parameters);*/
            	$("#reimburseContentDatagrid_edit").addRowData(rowId, {
            		id: "",
            		remittanceDate: "",
            		reimburseClassId: "",
            		content: "",
            		money: "",
            		remark: ""
            	});
            });
   		});
   		var rowDatas;
   		function generateGrid() {
   			var id = $("#editReimburseForm").find("input.reimburseId").val();
   			$("#reimburseContentDatagrid_edit").jqGrid({
   				url: getRoot() + "workflow/reimburse/queryContentsByReimburseId.action?reimburse.id="+id,
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "json",
                //data: [{"id": 1, "remittanceDate": "2016-04-05", "reimburseClassName": "d", "reimburseClassId": 4, "content": "测试", "money": "5000", "remark": "cs"}],
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "费用产生时间", name: "remittanceDate", editable: true, edittype: "custom", editoptions: {custom_element: createDateBox, custom_value:operateDateValue}, width: 150, align: "center"
                }, {
                	label: "报销类别", name: "reimburseClassId", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/reimburse/queryReimburseClassesByDepartmentId.action",
                		name: "reimburseClassId",
                		custom_element: createSelect2_editReimburse,
                		custom_value: operateSelect2Value_editReimburse
                	}, width: 200, align: "center",
                	formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!selected[rowId]) {
               				selected[rowId] = {id: rowObject.reimburseClassId, name: rowObject.reimburseClassName}
               			}
               			text = selected[rowId].name ? selected[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return selected[rowId].id;
                	}
                }, {
                	label: "付款项目", name: "content", width: 300, align: "center", editable: true, edittype: "custom",
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
                	label: "金额", name: "money", width: 110, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox_editReimburse,
                		custom_value: operateNumberValue_editReimburse,
                		name: "money"
                	}
                }, {
                	label: "备注", name: "remark", width: 300, align: "center", editable: true, edittype: "custom",
                	editoptions:{
                		rows: "10",
                		width: "100%",
                		custom_element: createTextareaBox,
                		custom_value: operateTextareaValue,
                		name: "remark"
                	},
                	formatter: function(cellvalue, options, rowObject) {
                		return decode(rowObject.remark);
                	}
                }, {
                	label: "操作", name: "operationCell", align: "center", width: 70, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		var rowId = options.rowId;
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delEditReimburseContent(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                loadComplete: function(data) {
                	rowDatas = data;
                },
                beforeSelectRow: function(rowid) {
                	var departmentId = reimburseDeptCombotree_edit.getValue();
                	if(departmentId) {
                		return true;
                	} else {
                		return false;
                	}
                },
                onSelectRow: function(id){
                	if(id && id!==lastsel2){
                		jQuery("#reimburseContentDatagrid_edit").saveRow(lastsel2, false, "clientArray");
                		jQuery("#reimburseContentDatagrid_edit").editRow(id,true);
                		lastsel2=id;
                	}
			    },
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20
            });
   		}
   		
   		function generateEditReimburseLogGrid() {
   			$("#reimburseauditorlogsdatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=reimburse&wfId="+$("#editReimburseId").val(),
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
                pager: "#reimburseeditlogsdatagridpager"
            });
   		}
		
   		/**
   		 * 创建日期选择框
   		 */
		function createDateBox (value, options) {
			var inputGroup = document.createElement("div");
			inputGroup.setAttribute("class", "input-group");
			
			var inputGroupAddon = document.createElement("div");
			inputGroup.appendChild(inputGroupAddon);
			inputGroupAddon.setAttribute("class", "input-group-addon");
			
			var iEl = document.createElement("i");
			inputGroupAddon.appendChild(iEl);
			iEl.setAttribute("class", "fa fa-calendar");
		
			var inputEl = document.createElement("input");
			inputEl.value = value;
			inputGroup.appendChild(inputEl);
			inputEl.setAttribute("readonly", "readonly");
			inputEl.setAttribute("name", "remittanceDate");
			inputEl.setAttribute("type", "text");
			inputEl.setAttribute("class", "form-control pull-right date");
			$(inputEl).datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
			
			return inputGroup;
		}
		 
		function operateDateValue(elem, operation, value) {
		    if(operation === "get") {
		    	return $("input", elem).val();
		    } else if(operation === "set") {
		    	$("input",elem).val(value);
		    }
		}
		
		/**
		 * 创建报销类别选择框
		 */
		function createSelect2_editReimburse(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#reimburseContentDatagrid_edit").getGridParam("selrow");
			//得到选中的值
			var selectedVal;
			if(selected[rowid]) {
				selectedVal = selected[rowid].id;
			}
			var selectEl = document.createElement("select");
			div.appendChild(selectEl);
			selectEl.setAttribute("class", "select2");
			selectEl.setAttribute("name", name);
			selectEl.setAttribute("style", "width: 100%");
			var option = document.createElement("option");
			option.value="";
			option.text="请选择";
			selectEl.appendChild(option);
			var url = options.url;
			var datas;
			var reimburseDeptId = reimburseDeptCombotree_edit.getValue();
			if(url) {
				$.ajax({
					url: getRoot() + url,
					data: "departmentId="+reimburseDeptId,
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						for(var i = 0; i < json.length; i ++) {
							var id = json[i].id;
							var name = json[i].name;
							var option = document.createElement("option");
							option.value=id;
							option.text=name;
							
							if(id == selectedVal) {
								option.selected=true;
							}
							selectEl.appendChild(option);
						}
						$(selectEl).select2();
					},
					error: function() {
						
					},
					complete: function() {
						
					}
				});
			} else {
				$(selectEl).select2();
			}
			
			return div;
		}
		//存放选中的报销类别的id
		var selected = new Object();
		function operateSelect2Value_editReimburse(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				//var selectCol = getColModelByName_editReimburse(name);
				/*if(selectCol) {
					var editOptions = selectCol.editoptions;
					$.extend(selectCol.editoptions, {selectedVal: $("select", elem).find("option:selected").val()});
				}*/
				//$(elem).parentsUtil("td[role=gridcell]").attr("selected", $("select", elem).find("option:selected").val());
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#reimburseContentDatagrid_edit").getGridParam("selrow");
				selected[lastsel2] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		/**
		 * 创建金额输入框
		 */
		function createNumberBox_editReimburse(value, options) {
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
			
			//创建存放按钮的div
			/*var btnDiv = document.createElement("div");
			div.appendChild(btnDiv);
			btnDiv.setAttribute("class", "input-group-btn-vertical");
			
			//创建加号按钮
			var addBtn = document.createElement("button");
			btnDiv.appendChild(addBtn);
			addBtn.setAttribute("class", "btn btn-default");
			addBtn.setAttribute("type", "button");
			
			//创建加号按钮的图标
			var addBtnIcon = document.createElement("i");
			addBtn.appendChild(addBtnIcon);
			addBtnIcon.setAttribute("class", "fa fa-caret-up");
			
			//创建减号按钮
			var minusBtn = document.createElement("button");
			btnDiv.appendChild(minusBtn);
			minusBtn.setAttribute("class", "btn btn-default");
			minusBtn.setAttribute("type", "button");
			
			//创建减号按钮的图标
			var minusBtnIcon = document.createElement("button");
			minusBtn.appendChild(minusBtnIcon);
			minusBtnIcon.setAttribute("class", "fa fa-caret-down");*/
			
			return div;
		}
		
		function operateNumberValue_editReimburse(elem, operation, value) {
			if(operation == "get") {
				return $("input", elem).val();
			} else {
				$("input", elem).val(value);
			}
		}
		
		/**
		 * 通过配置的name获取列内容
		 */
		function getColModelByName_editReimburse(name) {
			var cols = $("#reimburseContentDatagrid_edit").getGridParam("colModel");
			var selectCol;
			for(var i in cols) {
				var col = cols[i];
				var colName = col.name;
				if(colName == name) {
					selectCol = col;
				}
			}
			return selectCol;
		}
		
		function delEditReimburseContent(id, rowId) {
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
		   						url: getRoot() + "workflow/reimburse/delContentById.action",
			    				data: "reimburseContent.id="+id,
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
			    						$("#reimburseContentDatagrid_edit").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#reimburseContentDatagrid_edit").delRowData(rowId);
		            	}
	   				}
	            }
   			});
		}
		
		function selectReimburseAccount_edit() {
   			BootstrapDialog.show({
			    title: "选择账户",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/account/queryByUserId.action"),
			    draggable: true,
			    size: BootstrapDialog.SIZE_WIDE,
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
			    		var $button = this;
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			var $select = form.find("select");
			    			var $option = $select.find("option:selected");
			    			var receiver = $option.attr("receiver");
			    			var taxPayerId = $option.attr("taxPayerId");
			    			var address = $option.attr("address");
			    			var cellphone = $option.attr("cellphone");
			    			var bankNo = $option.attr("bankNo");
			    			var bankName = $option.attr("bankName");
			    			$("#editReimburseForm").find("input[name='reimburse.receiver']").val(receiver);
			    			$("#editReimburseForm").find("input[name='reimburse.bank']").val(bankName);
			    			$("#editReimburseForm").find("input[name='reimburse.bankNo']").val(bankNo);
			    			dialog.close();
			    		} catch(e) {
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
   	</script>
  </body>
</html>
