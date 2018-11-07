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
    
    <title>新增商品</title>
    
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
   	<form id="addCheckinArticleForm" action="" class="form-horizontal">
    	<div class="box-body padding-bottom5 bottom-dotted-border" >
    		<div class="form-group">
				<label for="text" class="col-md-1 col-sm-4 control-label">商品名称：</label>
				<div class="col-md-2 col-sm-8 no-padding">
					<input name="article.name" type="text" value="" class="form-control required" id="text">
				</div>
				<label for="text" class="col-md-2 col-sm-4 control-label">商品编号：</label>
				<div class="col-md-2 col-sm-8 totalScore no-padding">
					<input name="article.serialNo" type="text" value="" class="form-control required" id="text">
				</div>
				<label for="text" class="col-md-2 col-sm-4 control-label">单位：</label>
				<div class="col-md-2 col-sm-8 totalScore no-padding">
					<input name="article.unit" type="text" value="" class="form-control required" id="text">
				</div>
			</div>
			<div class="form-group">
				<label for="text" class="col-md-1 col-sm-4 control-label">归属公司：</label>
				<div class="col-md-2 col-sm-8 totalScore no-padding">
					<select class="select2" name="article.companyId" style="width: 100%">
						<option value="">请选择</option>
						<c:forEach items="${companys }" var="company">
							<option value="${company.id }">${company.name }</option>
						</c:forEach>
					</select>
				</div>
				<label for="text" class="col-md-2 col-sm-4 control-label">排序号：</label>
				<div class="col-md-2 col-sm-8 totalScore no-padding">
					<input name="article.orderNo" type="number" value="0" class="form-control required" id="text">
				</div>
			</div>
    	</div>
    	<div class="toolbar with-border">
			<button type="button" class="btn btn-info btn-sm" data-toggle="tooltip" title="新增规格" id="addFormat_add"><i class="fa fa-fw fa-plus"></i>新增</button>
		</div>
    	<div class="box-body">
    		<table id="formatDatagrid_add"></table>
    	</div>
   	</form>
   	<script type="text/javascript">
   		var achievementDeptCombotree;
   		$(function() {
   			$("#addCheckinArticleForm").find(".select2").select2();
   			
			//需要延时，否则加载时，设置宽度会出异常，宽度不能自适应
			setTimeout("generateFormatGrid()", 0);
            
            $("#addFormat_add").click(function() {
            	var rowId = Math.random();
            	$("#formatDatagrid_add").addRowData(rowId, {
            		id: "",
            		name: "",
            		price: "",
            		orderNo: "",
            		memo: ""
            	});
            });
   		});
   		var rowDatas;
   		function generateFormatGrid() {
   			$("#formatDatagrid_add").jqGrid({
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
                	label: "规格名称", name: "name", editable: true, edittype: "text", width: 300, align: "center"
                }, {
                	label: "价格", name: "price", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "price"
                	}, width: 350, align: "center"
                }, {
                	label: "排序号", name: "orderNo", width: 350, align: "center", editable: true, edittype: "custom",
                	editoptions: {
                		custom_element: createNumberBox,
                		custom_value: operateNumberValue,
                		name: "orderNo"
                	}
                }, {
                	label: "备注", name: "memo", width: 350, align: "center", editable: true, edittype: "textarea",
                	editoptions: {rows:"3",width:"100%"}
                }, {
                	label: "操作", name: "operationCell", align: "center", width: 70, formatter: function(cellvalue, options, rowObject) {
                		var buttons = "";
                		var rowId = options.rowId;
               			buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delAddFormat(\""+rowObject.id+"\", \""+rowId+"\")");
   						return buttons;
                	}
                }],
                ondblClickRow: function(id){
                	if(id && id!==formatLastSel2){
                		//jQuery("#achievementContentDatagrid_add").saveRow(formatLastSel2, false, "clientArray");
                		jQuery("#formatDatagrid_add").saveRow(formatLastSel2, false, "clientArray");
                		jQuery("#formatDatagrid_add").editRow(id,true);
                		formatLastSel2=id;
                	}
			    },
				viewrecords: true,
                height: "100%",
                //width: "100%",
                rowNum: 20
            });
   		}
		
		function delAddFormat(id, rowId) {
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
		   						url: getRoot() + "workflow/article/delFormatById.action",
			    				data: "format.id="+id,
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
			    						$("#formatDatagrid_add").delRowData(rowId);
			    					} else {
			    						BootstrapDialog.danger(json.msg);
			    					}
			    				},
			    				error: function() {
			    					BootstrapDialog.danger("系统异常，请联系管理员！");
			    				}
		   					});
		            	} else {
		            		$("#formatDatagrid_add").delRowData(rowId);
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
   	</script>
  </body>
</html>
