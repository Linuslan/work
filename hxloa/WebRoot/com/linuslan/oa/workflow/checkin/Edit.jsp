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
    
    <title>编辑入库申请</title>
    
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
   	<form id="editCheckinForm" class="form-horizontal" action="<%=basePath %>workflow/checkin/update.action" method="POST" enctype="multipart/form-data">
   		<input id="editCheckinId" class="checkinId" name="checkin.id" type="hidden" value="${checkin.id }" />
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#checkin_edit" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#checkinUploadFile_edit" data-toggle="tab">附件列表</a>
   				</li>
   				<li>
   					<a href="#checkinAuditorLogs" data-toggle="tab">审核意见</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="checkin_edit">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select id="checkinCompany_edit" class="select2" name="checkin.companyId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${companys }" var="company">
										<option value="${company.id }" ${checkin.companyId == company.id ? "selected='selected'" : "" }>${company.name }</option>
									</c:forEach>
								</select>
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">入库编号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="checkin.serialNo" type="text" value="${checkin.serialNo }" readonly="readonly" class="form-control showText" id="text">
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">入库仓库：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select class="select2" name="checkin.warehouseId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${warehouses }" var="warehouse">
										<option value="${warehouse.id }" ${checkin.warehouseId == warehouse.id ? "selected='selected'" : "" }>${warehouse.name }</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="departmentId" class="col-md-2-sm col-sm-4 control-label">入库类型：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select class="select2" name="checkin.checkinTypeId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${dictionaries }" var="dictionary">
										<option value="${dictionary.id }" ${checkin.checkinTypeId == dictionary.id ? "selected='selected'" : "" }>${dictionary.text }</option>
									</c:forEach>
								</select>
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">采购单号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="checkin.purchaseSerialNo" type="text" value="${checkin.purchaseSerialNo }" class="form-control showText" id="text">
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">入库时间：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input value="<fmt:formatDate value="${checkin.checkinDate }" pattern="yyyy-MM-dd"/>" readonly="readonly" name="checkin.checkinDate" type="text" class="form-control pull-right date">
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="moneyDate" class="col-md-2-sm col-sm-4 control-label">单位名称：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select id="editCheckinUnit" class="select2" name="checkin.unitId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${units }" var="unit">
										<option ${checkin.unitId == unit.id ? "selected='selected'" : "" } value="${unit.id }" telephone="${unit.telephone }" address="${unit.address }">${unit.name }</option>
									</c:forEach>
								</select>
							</div>
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">联系电话：</label>
							<div class="col-md-2 col-sm-8 telephone no-padding" >
								<input value="${checkin.telephone }" name="checkin.telephone" type="text" class="form-control telephone">
							</div>
						</div>
						<div class="form-group padding-bottom5">
							<label for="orderNo" class="col-md-2-sm col-sm-4 control-label">单位地址：</label>
							<div class="col-md-11-sm col-sm-8 address no-padding">
								<input value="${checkin.address }" name="checkin.address" type="text" class="form-control address">
							</div>
						</div>
			    	</div>
			    	<div class="toolbar with-border">
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增入库项目" id="addCheckinContent_edit"><i class="fa fa-fw fa-plus"></i>新增</button>
					</div>
			    	<div class="box-body">
			    		<table id="checkinContentDatagrid_edit"></table>
			    	</div>
			    </div>
			    <div class="tab-pane" id="checkinUploadFile_edit">
			    	<div class="toolbar with-border">
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增附件" id="addCheckinUploadFile_edit"><i class="fa fa-fw fa-plus"></i>新增</button>
					</div>
					<div class="box-body" id="checkinUploadFileList_edit">
						<c:forEach items="${uploadFiles }" var="uploadFile">
							<div class="form-group padding-bottom5">
								<label for="orderNo" class="col-md-2-sm col-sm-4 control-label">
									<button type="button" class="btn btn-danger btn-xs" data-toggle="tooltip" title="删除" onclick="delCheckinFile_edit(this, '${uploadFile.id}')"><i class="fa fa-trash"></i></button>
								</label>
								<div class="col-md-11-sm col-sm-8 align-left padding-top7">
									<a onclick="checkinDownloadFile_edit(${uploadFile.id })" href="javascript: void(0);">${uploadFile.fileName }</a>
								</div>
							</div>
						</c:forEach>
					</div>
			    </div>
			   	<div class="tab-pane" id="checkinAuditorLogs">
   					<div class="box box-solid">
		   				<div class="box-body">
							<div class="col-md-12 col-sm-12 no-padding">
			   					<table id="editcheckinauditorlogsdatagrid"></table>
	   							<div id="editcheckinauditorlogsdatagridpager"></div>
   							</div>
		   				</div>
		   			</div>
   				</div>
   			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#editCheckinForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#editCheckinForm").find("select[name='checkin.unitId']").change(function() {
   				var $option = $(this).find("option:selected");
   				var telephone = $option.attr("telephone");
   				var address = $option.attr("address");
   				$("#editCheckinForm").find("input.telephone").val(telephone);
   				$("#editCheckinForm").find("input.address").val(address);
   			});
   			$("#checkinContentDatagrid_edit").on("blur", "input.count", function() {
   				var rowid = $("#checkinContentDatagrid_edit").getGridParam("selrow");
				var $tr = $("#checkinContentDatagrid_edit").find("tr[id='"+rowid+"']");
				var price = $tr.find("input[name=price]").val();
				var quantity = $tr.find("input[name=quantity]").val();
				var $totalPrice = $tr.find("td[aria-describedby=checkinContentDatagrid_edit_totalPrice]");
				var loss = $tr.find("input[name=loss]").val();
				var $remainder = $tr.find("td[aria-describedby=checkinContentDatagrid_edit_remainder]");
				var totalPrice = price*quantity;
				totalPrice = totalPrice.toFixed(2);
				var remainder = quantity - loss;
				remainder = remainder.toFixed(2);
				$totalPrice.html(totalPrice);
				$remainder.html(remainder);
   			});
   			
   			$("#addCheckinUploadFile_edit").click(function() {
   				var html = "<div class=\"form-group padding-bottom5\">"
							+"<label for=\"orderNo\" class=\"col-md-2-sm col-sm-4 control-label\">"+createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delCheckinFile_edit(this)")+"</label>"
							+"<div class=\"col-md-11-sm col-sm-8 address no-padding\">"
								+"<input name=\"files\" type=\"file\" class=\"form-control files\">"
							+"</div>"
						+"</div>";
				$("#checkinUploadFileList_edit").append(html);
   			});
   			$("#editCheckinForm").find(".select2").select2();
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateCheckinContentGrid_edit()", 0);
            
            $("#addCheckinContent_edit").click(function() {
            	if(checkinLastSel){
               		jQuery("#checkinContentDatagrid_edit").saveRow(checkinLastSel, false, "clientArray");
               	}
            	var checkinCompanyId = $("#checkinCompany_edit").val();
				if(!checkinCompanyId) {
					BootstrapDialog.danger("请先选择归属公司");
					return false;
				}
            	var rowId = Math.random();
            	$("#checkinContentDatagrid_edit").addRowData(rowId, {
            		id: "",
            		articleSerialNo: "",
            		articleId: "",
            		formatId: "",
            		unit: "",
            		quantity: "",
            		price: "",
            		totalPrice: "",
            		loss: "",
            		remainder: "",
            		inspection: "",
            		remark: "",
            	});
            });
   		});
   		var rowDatas;
   		function generateCheckinContentGrid_edit() {
   			var id = $("#editCheckinForm").find("input.checkinId").val();
   			$("#checkinContentDatagrid_edit").jqGrid({
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
                	label: "商品编号", name: "articleSerialNo", editable: false
                }, {
                	label: "商品名称", name: "articleId", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/article/queryCheckinArticlesByCompanyId.action",
                		name: "articleId",
                		custom_element: createCheckinArticleSelect2_edit,
                		custom_value: operateCheckinArticleSelect2Value_edit
					},
					formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!checkinSelectedArticle_edit[rowId]) {
               				checkinSelectedArticle_edit[rowId] = {id: rowObject.articleId, name: rowObject.articleName}
               			}
               			text = checkinSelectedArticle_edit[rowId].name ? checkinSelectedArticle_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return checkinSelectedArticle_edit[rowId].id;
                	}
                }, {
                	label: "规格", name: "formatId", width: 150, align: "center", editable: true, edittype: "custom",
					editoptions: {
						url: "workflow/article/queryFormatsByArticleId.action",
                		name: "formatId",
                		id: "checkinFormatId_edit",
                		custom_element: createCheckinFormatSelect2_edit,
                		custom_value: operateCheckinFormatSelect2Value_edit
					},
					formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!checkinSelectedFormat_edit[rowId]) {
               				checkinSelectedFormat_edit[rowId] = {id: rowObject.formatId, name: rowObject.formatName}
               			}
               			text = checkinSelectedFormat_edit[rowId].name ? checkinSelectedFormat_edit[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return checkinSelectedFormat_edit[rowId].id;
                	}
                }, {
                	label: "单位", name: "unit", width: 150, align: "center", editable: false
                }, {
                	label: "数量", name: "quantity", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "quantity",
                		cls: "count"
                	}
                }, {
                	label: "单价", name: "price", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		cls: "count",
                		name: "price"
                	}
                }, {
                	label: "金额", name: "totalPrice", width: 150, align: "center", editable: false
                }, {
               		label: "损耗", name: "loss", width: 150, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "loss",
                		cls: "count"
                	}
                }, {
                	label: "余", name: "remainder", width: 150, align: "center", editable: false
                }, {
                	label: "抽检编号", name: "inspection", width: 150, align: "center", editable: true, edittype: "text"
                }, {
                	label: "备注", name: "remark", width: 300, align: "center", editable: true, edittype: "textarea", editoptions: {rows:"2",width:"100%"}
                }, {
                	label: "操作", name: "operationCell", align: "center", width: 70, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		var rowId = options.rowId;
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delEditCheckinContent(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                loadComplete: function(data) {
                	rowDatas = data;
                },
                beforeSelectRow: function(rowid) {
                	var companyId = $("#checkinCompany_edit").val();
                	if(companyId) {
                		return true;
                	} else {
                		return false;
                	}
                },
                onSelectRow: function(id){
                	if(id && id!==checkinLastSel){
                		jQuery("#checkinContentDatagrid_edit").saveRow(checkinLastSel, false, "clientArray");
                		var rowData = $("#checkinContentDatagrid_edit").getRowData(checkinLastSel);
                		jQuery("#checkinContentDatagrid_edit").jqGrid("editRow", id, {
                			oneditfunc: function(id) {
                				var $tr = $("#checkinContentDatagrid_edit").find("tr[id='"+id+"']");
                				$tr.find("select").select2();
                				/*if(checkinSelectedArticle_edit && checkinSelectedArticle_edit[id]) {
                					var articleId = checkinSelectedArticle_edit[id].id;
                					$tr.find("select[name=articleId]").find("option[value="+articleId+"]").attr("selected", "selected");
                				}*/
                				
                				$tr.find("select[name=articleId]").change(function() {
									var rowid = $("#checkinContentDatagrid_edit").getGridParam("selrow");
									var $tr = $("#checkinContentDatagrid_edit").find("tr[id='"+rowid+"']");
									var $formatSelect = $tr.find("select[name=formatId]");
									var $option = $(this).find("option:selected");
									var articleId = $option.val();
									var unit = $option.attr("unit");
									var serialNo = $option.attr("serialNo");
									getCheckinFormatOptions_edit($formatSelect, articleId);
									$tr.find("td[aria-describedby=checkinContentDatagrid_edit_unit]").html(unit);
									$tr.find("td[aria-describedby=checkinContentDatagrid_edit_articleSerialNo]").html(serialNo);
								});
                			}
                		});
                		checkinLastSel=id;
                	}
			    },
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20
            });
   		}
   		
   		function generateEditCheckinLogGrid() {
   			$("#editcheckinauditorlogsdatagrid").jqGrid({
                url: getRoot() + "workflow/auditlog/queryOpinionPage.action?wfType=checkin&wfId="+$("#editCheckinId").val(),
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
                pager: "#editcheckinauditorlogsdatagridpager"
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
			var cls = options.cls;
			if(!cls) {
				cls="";
			}
			//创建外围的div
			var div = document.createElement("div");
			div.setAttribute("class", "input-group spinner ");
			div.setAttribute("style", "width: 99%");
			
			//创建输入框
			var input = document.createElement("input");
			div.appendChild(input);
			input.setAttribute("name", name);
			input.setAttribute("type", "number");
			input.setAttribute("class", "form-control "+cls);
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
		
		/**
		 * 通过配置的name获取列内容
		 */
		function getColModelByName(name) {
			var cols = $("#checkinContentDatagrid_edit").getGridParam("colModel");
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
		
		function delEditCheckinContent(id, rowId) {
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
		   						url: getRoot() + "workflow/checkin/delContentById.action",
			    				data: "checkinContent.id="+id,
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
			    						$("#checkinContentDatagrid_edit").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#checkinContentDatagrid_edit").delRowData(rowId);
		            	}
	   				}
	            }
   			});
		}
		
		/**
		 * 创建入库类别选择框
		 */
		function createCheckinArticleSelect2_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#checkinContentDatagrid_edit").getGridParam("selrow");
			
			//得到选中的值
			var selectedVal;
			if(checkinSelectedArticle_edit[rowid]) {
				selectedVal = checkinSelectedArticle_edit[rowid].id;
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
			var companyId = $("#checkinCompany_edit").val();
			if(url) {
				$.ajax({
					url: getRoot() + url,
					data: "companyId="+companyId,
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						for(var i = 0; i < json.length; i ++) {
							var article = json[i];
							var id = article.id;
							var name = article.name;
							var unit = article.unit;
							var serialNo = article.serialNo;
							var option = document.createElement("option");
							option.value=id;
							option.text=name;
							$(option).attr("unit", unit);
							$(option).attr("serialNo", serialNo);
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
		//存放选中的入库类别的id
		var checkinSelectedArticle_edit = new Object();
		function operateCheckinArticleSelect2Value_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#checkinContentDatagrid_edit").getGridParam("selrow");
				checkinSelectedArticle_edit[checkinLastSel] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		/**
		 * 创建入库类别选择框
		 */
		function createCheckinFormatSelect2_edit(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#checkinContentDatagrid_edit").getGridParam("selrow");
			//得到选中的值
			var selectedVal;
			if(checkinSelectedFormat_edit[rowid]) {
				selectedVal = checkinSelectedFormat_edit[rowid].id;
			}
			var selectEl = document.createElement("select");
			div.appendChild(selectEl);
			selectEl.setAttribute("class", "select2");
			selectEl.setAttribute("name", name);
			selectEl.setAttribute("style", "width: 100%");
			var id = options.id;
			selectEl.setAttribute("id", id);
			
			var url = options.url;
			var datas;
			var article = checkinSelectedArticle_edit[rowid];
			$(selectEl).select2();
			if(article && article.id && ""!=$.trim(article.id)) {
				getCheckinFormatOptions_edit($(selectEl), article.id);
			}
			
			return div;
		}
		//存放选中的入库类别的id
		var checkinSelectedFormat_edit = new Object();
		function operateCheckinFormatSelect2Value_edit(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#checkinContentDatagrid_edit").getGridParam("selrow");
				checkinSelectedFormat_edit[checkinLastSel] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function getCheckinFormatOptions_edit($select, articleId) {
			//$select.html("");
			var rowid = $("#checkinContentDatagrid_edit").getGridParam("selrow");
			try {
				$select.select2("val", "");
			} catch(ex) {
				
			}
			$select.empty();
			if(articleId && "" != $.trim(articleId)) {
				var url = getRoot() + "workflow/article/queryFormatsByArticleId.action";
			
				$.ajax({
					url: url,
					data: {
						"article.id": articleId
					},
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						if(json && json.length > 0) {
							var selectVal;
							if(rowid) {
								selectVal = checkinSelectedFormat_edit[rowid]
							}
							for(var i = 0; i < json.length; i ++) {
								var format = json[i];
								var option = document.createElement("option");
								option.value=format.id;
								option.text=format.name;
								$select.append(option);
								if(selectVal && selectVal.id && selectVal.id == format.id) {
									option.selected = true;
									$select.val(format.id).trigger("change");
								}
							}
						}
					},
					error: function(data) {
						
					}
				});
			}
			
		}
		
		function delCheckinFile_edit(obj, id) {
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
		   						url: getRoot() + "sys/uploadFile/del.action",
			    				data: "uploadFile.id="+id,
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
			    						$(obj).parent().parent().remove();
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$(obj).parent().parent().remove();
		            	}
	   				}
	            }
   			});
		}
		
		function checkinDownloadFile_edit(id) {
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
