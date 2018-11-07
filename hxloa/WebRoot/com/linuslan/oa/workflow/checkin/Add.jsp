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
    
    <title>新增入库申请</title>
    
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
   	<form id="addCheckinForm" class="form-horizontal" action="<%=basePath %>workflow/checkin/add.action" method="POST" enctype="multipart/form-data">
   		<div class="nav-tabs-top-border">
   			<ul class="nav nav-tabs">
   				<li class="active">
   					<a href="#checkin_add" data-toggle="tab">详情</a>
   				</li>
   				<li>
   					<a href="#checkinUploadFile_add" data-toggle="tab">附件列表</a>
   				</li>
   			</ul>
   			<div class="tab-content">
   				<div class="active tab-pane" id="checkin_add">
			    	<div class="box-body padding-bottom5 bottom-dotted-border" >
			    		<div class="form-group">
							<label for="text" class="col-md-2-sm col-sm-4 control-label">归属公司：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select id="checkinCompany_add" class="select2" name="checkin.companyId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${companys }" var="company">
										<option value="${company.id }">${company.name }</option>
									</c:forEach>
								</select>
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">入库编号：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<input name="checkin.serialNo" type="text" value="${serialNo }" readonly="readonly" class="form-control showText" id="text">
							</div>
							<label for="text" class="col-md-2-sm col-sm-4 control-label">入库仓库：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select class="select2" name="checkin.warehouseId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${warehouses }" var="warehouse">
										<option value="${warehouse.id }">${warehouse.name }</option>
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
										<option value="${dictionary.id }">${dictionary.text }</option>
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
									<input readonly="readonly" name="checkin.checkinDate" type="text" class="form-control pull-right date">
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="moneyDate" class="col-md-2-sm col-sm-4 control-label">单位名称：</label>
							<div class="col-md-2 col-sm-8 no-padding">
								<select id="addCheckinUnit" class="select2" name="checkin.unitId" style="width: 100%">
									<option value="">请选择</option>
									<c:forEach items="${units }" var="unit">
										<option value="${unit.id }" telephone="${unit.telephone }" address="${unit.address }">${unit.name }</option>
									</c:forEach>
								</select>
							</div>
							<label for="groupId" class="col-md-2-sm col-sm-4 control-label">联系电话：</label>
							<div class="col-md-2 col-sm-8 telephone no-padding" >
								<input name="checkin.telephone" type="text" class="form-control telephone">
							</div>
						</div>
						<div class="form-group padding-bottom5">
							<label for="orderNo" class="col-md-2-sm col-sm-4 control-label">单位地址：</label>
							<div class="col-md-11-sm col-sm-8 address no-padding">
								<input name="checkin.address" type="text" class="form-control address">
							</div>
						</div>
			    	</div>
			    	<div class="toolbar with-border">
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增入库项目" id="addCheckinContent_add"><i class="fa fa-fw fa-plus"></i>新增</button>
					</div>
			    	<div class="box-body">
			    		<table id="checkinContentDatagrid_add"></table>
			    	</div>
			    </div>
			    <div class="tab-pane" id="checkinUploadFile_add">
			    	<div class="toolbar with-border">
						<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增附件" id="addCheckinUploadFile_add"><i class="fa fa-fw fa-plus"></i>新增</button>
					</div>
					<div class="box-body" id="checkinUploadFileList_add">
						
					</div>
			    </div>
			</div>
		</div>
   	</form>
   	<script type="text/javascript">
   		$(function() {
   			$("#addCheckinForm").find(".date").datepicker({format: "yyyy-mm-dd", language: "zh-CN"});
   			$("#addCheckinForm").find("select[name='checkin.unitId']").change(function() {
   				var $option = $(this).find("option:selected");
   				var telephone = $option.attr("telephone");
   				var address = $option.attr("address");
   				$("#addCheckinForm").find("input.telephone").val(telephone);
   				$("#addCheckinForm").find("input.address").val(address);
   			});
   			$("#checkinContentDatagrid_add").on("blur", "input.count", function() {
   				var rowid = $("#checkinContentDatagrid_add").getGridParam("selrow");
				var $tr = $("#checkinContentDatagrid_add").find("tr[id='"+rowid+"']");
				var price = $tr.find("input[name=price]").val();
				var quantity = $tr.find("input[name=quantity]").val();
				var $totalPrice = $tr.find("td[aria-describedby=checkinContentDatagrid_add_totalPrice]");
				var loss = $tr.find("input[name=loss]").val();
				var $remainder = $tr.find("td[aria-describedby=checkinContentDatagrid_add_remainder]");
				var totalPrice = price*quantity;
				totalPrice = totalPrice.toFixed(2);
				var remainder = quantity - loss;
				remainder = remainder.toFixed(2);
				$totalPrice.html(totalPrice);
				$remainder.html(remainder);
   			});
   			
   			$("#addCheckinUploadFile_add").click(function() {
   				var html = "<div class=\"form-group padding-bottom5\">"
							+"<label for=\"orderNo\" class=\"col-md-2-sm col-sm-4 control-label\">"+createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delCheckinFile_add(this)")+"</label>"
							+"<div class=\"col-md-11-sm col-sm-8 address no-padding\">"
								+"<input name=\"files\" type=\"file\" class=\"form-control files\">"
							+"</div>"
						+"</div>";
				$("#checkinUploadFileList_add").append(html);
   			});
   			$("#addCheckinForm").find(".select2").select2();
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateCheckinContentGrid_add()", 0);
            
            $("#addCheckinContent_add").click(function() {
            	if(checkinLastSel){
               		jQuery("#checkinContentDatagrid_add").saveRow(checkinLastSel, false, "clientArray");
               	}
            	var checkinCompanyId = $("#checkinCompany_add").val();
				if(!checkinCompanyId) {
					BootstrapDialog.danger("请先选择归属公司");
					return false;
				}
            	var rowId = Math.random();
            	$("#checkinContentDatagrid_add").addRowData(rowId, {
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
   		function generateCheckinContentGrid_add() {
   			$("#checkinContentDatagrid_add").jqGrid({
                mtype: "POST",
                shrinkToFit: true,
                autowidth: true,
                scrollrows: false,
                scroll: false,
				styleUI : "Bootstrap",
                datatype: "local",
                colModel: [{
                	label: "ID", name: "id", hidden: true
                }, {
                	label: "商品编号", name: "articleSerialNo", editable: false
                }, {
                	label: "商品名称", name: "articleId", editable: true, edittype: "custom",
                	editoptions: {
                		url: "workflow/article/queryCheckinArticlesByCompanyId.action",
                		name: "articleId",
                		custom_element: createCheckinArticleSelect2_add,
                		custom_value: operateCheckinArticleSelect2Value_add
					},
					formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!checkinSelectedArticle_add[rowId]) {
               				checkinSelectedArticle_add[rowId] = {id: rowObject.articleId, name: rowObject.articleId}
               			}
               			text = checkinSelectedArticle_add[rowId].name ? checkinSelectedArticle_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return checkinSelectedArticle_add[rowId].id;
                	}
                }, {
                	label: "规格", name: "formatId", width: 150, align: "center", editable: true, edittype: "custom",
					editoptions: {
						url: "workflow/article/queryFormatsByArticleId.action",
                		name: "formatId",
                		id: "checkinFormatId_add",
                		custom_element: createCheckinFormatSelect2_add,
                		custom_value: operateCheckinFormatSelect2Value_add
					},
					formatter: function(cellvalue, options, rowObject) {
               			var rowId = options.rowId;
               			var text;
               			if(!checkinSelectedFormat_add[rowId]) {
               				checkinSelectedFormat_add[rowId] = {id: rowObject.formatId, name: rowObject.formatId}
               			}
               			text = checkinSelectedFormat_add[rowId].name ? checkinSelectedFormat_add[rowId].name : "";
                		return text;
                	},
                	unformat: function(cellvalue, options, cell) {
                		var rowId = options.rowId;
                		return checkinSelectedFormat_add[rowId].id;
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
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delAddCheckinContent(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                loadComplete: function(data) {
                	rowDatas = data;
                },
                beforeSelectRow: function(rowid) {
                	var companyId = $("#checkinCompany_add").val();
                	if(companyId) {
                		return true;
                	} else {
                		return false;
                	}
                },
                onSelectRow: function(id){
                	if(id && id!==checkinLastSel){
                		jQuery("#checkinContentDatagrid_add").saveRow(checkinLastSel, false, "clientArray");
                		var rowData = $("#checkinContentDatagrid_add").getRowData(checkinLastSel);
                		jQuery("#checkinContentDatagrid_add").jqGrid("editRow", id, {
                			oneditfunc: function(id) {
                				var $tr = $("#checkinContentDatagrid_add").find("tr[id='"+id+"']");
                				$tr.find("select").select2();
                				/*if(checkinSelectedArticle_add && checkinSelectedArticle_add[id]) {
                					var articleId = checkinSelectedArticle_add[id].id;
                					$tr.find("select[name=articleId]").find("option[value="+articleId+"]").attr("selected", "selected");
                				}*/
                				
                				$tr.find("select[name=articleId]").change(function() {
									var rowid = $("#checkinContentDatagrid_add").getGridParam("selrow");
									var $tr = $("#checkinContentDatagrid_add").find("tr[id='"+rowid+"']");
									var $formatSelect = $tr.find("select[name=formatId]");
									var $option = $(this).find("option:selected");
									var articleId = $option.val();
									var unit = $option.attr("unit");
									var serialNo = $option.attr("serialNo");
									getCheckinFormatOptions_add($formatSelect, articleId);
									$tr.find("td[aria-describedby=checkinContentDatagrid_add_unit]").html(unit);
									$tr.find("td[aria-describedby=checkinContentDatagrid_add_articleSerialNo]").html(serialNo);
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
			var cols = $("#checkinContentDatagrid_add").getGridParam("colModel");
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
		
		function delAddCheckinContent(id, rowId) {
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
			    						$("#checkinContentDatagrid_add").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#checkinContentDatagrid_add").delRowData(rowId);
		            	}
	   				}
	            }
   			});
		}
		
		/**
		 * 创建报销类别选择框
		 */
		function createCheckinArticleSelect2_add(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#checkinContentDatagrid_add").getGridParam("selrow");
			
			//得到选中的值
			var selectedVal;
			if(checkinSelectedArticle_add[rowid]) {
				selectedVal = checkinSelectedArticle_add[rowid].id;
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
			var companyId = $("#checkinCompany_add").val();
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
		//存放选中的报销类别的id
		var checkinSelectedArticle_add = new Object();
		function operateCheckinArticleSelect2Value_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#checkinContentDatagrid_add").getGridParam("selrow");
				checkinSelectedArticle_add[checkinLastSel] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		/**
		 * 创建报销类别选择框
		 */
		function createCheckinFormatSelect2_add(value, options) {
			
			//得要将select2放在div里面
			var div = document.createElement("div");
			var name = options.name;
			var rowid = $("#checkinContentDatagrid_add").getGridParam("selrow");
			//得到选中的值
			var selectedVal;
			if(checkinSelectedFormat_add[rowid]) {
				selectedVal = checkinSelectedFormat_add[rowid].id;
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
			var article = checkinSelectedArticle_add[rowid];
			$(selectEl).select2();
			if(article && article.id && ""!=$.trim(article.id)) {
				getCheckinFormatOptions_add($(selectEl), article.id);
			}
			
			return div;
		}
		//存放选中的报销类别的id
		var checkinSelectedFormat_add = new Object();
		function operateCheckinFormatSelect2Value_add(elem, operation, value) {
			if(operation === "get") {
				var name = $("select", elem).attr("name");
				var text = $("select", elem).find("option:selected").text();
				var val = $("select", elem).find("option:selected").val();
				var rowid = $("#checkinContentDatagrid_add").getGridParam("selrow");
				checkinSelectedFormat_add[checkinLastSel] = {id: val, name: text};
				return text;
		    } else if(operation === "set") {
		    	$("select", elem).val(value);
		    }
		}
		
		function getCheckinFormatOptions_add($select, articleId) {
			
			//$select.html("");
			var rowid = $("#checkinContentDatagrid_add").getGridParam("selrow");
			try {
				$select.select2("val", "");
			} catch(ex) {
				
			}
			$select.empty();
			var option = document.createElement("option");
			option.value="";
			option.text="请选择";
			$select.append(option);
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
								selectVal = checkinSelectedFormat_add[rowid]
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
		
		function delCheckinFile_add(obj, id) {
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
   	</script>
  </body>
</html>
