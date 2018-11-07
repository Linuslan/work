<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>字典管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body>
    <div class="row" style="height: 100%; overflow-y: auto;">
   		<div class="col-xs-12">
   			<div class="box box-solid">
   				<div class="box-body no-padding">
   					<!-- <div class="row search-panel width-border">
   						<div class="col-md-12 col-xs-12">
   							<div class="col-md-1 col-xs-12 text-align-right no-padding">部门名称：</div>
   							<div class="col-md-2 col-xs-12 no-padding">
   								<div id="combotree"></div>
   							</div>
   						</div>
   					</div> -->
   					<div class="toolbar with-border">
	   					<button class="btn btn-success btn-sm" data-toggle="tooltip" title="新增字典项" id="addDictionary"><i class="fa fa-fw fa-plus"></i>新增</button>
	   				</div>
	   				<div id="dictionarytreegrid" style="height: 100; overflow-y: auto;"></div>
   				</div>
   			</div>
   		</div>
   	</div>
   	<script type="text/javascript">
   		var dictionaryList;
   		$(function() {
   			/*$("#combotree").combotree({
   				url: getRoot() + "dictionary/queryDictionaryList.action",
   				async: true,
   				singleSelect: false,
   				loadOnExpand: true,
   				loadParams: {
   					"id": "dictionary.id"
   				},
   				idField: "id",
   				textField: "text"
   			});*/
   			dictionaryList = $("#dictionarytreegrid").treegrid({
   				url: getRoot() + "sys/dictionary/queryTree.action",
   				sizeUnit: "%",
   				loadOnExpand: false,
   				idField: "id",
   				textField: "text",
   				pidField: "pid",
   				loadParams: {
   					"id": "dictionary.id"
   				},
   				async: true,
   				onRowClick: function() {
   					
   				},
   				columns: [{
   					field: "字典项名称", dataIndex: "text", width: 20
   				}, {
   					field: "字典ID", dataIndex: "id", width: 20, align: "center"
   				}, {
   					field: "字典值", dataIndex: "value", width: 20, align: "center"
   				}, {
   					field: "排序值", dataIndex: "orderNo", align: "center", width: 10, align: "center"
   				}, {
   					field: "操作", align: "center", width: 20,
   					formatter: function(data) {
   						var buttons = createBtn("修改", "btn-success btn-xs", "fa-pencil", "editDictionary("+data.id+")");
   						buttons = buttons + createBtn("删除", "btn-danger btn-xs", "fa-trash-o", "delDictionary("+data.id+")");
   						return buttons;
   					}
   				}]
   			});
   			
   			$("#addDictionary").click(function() {
   				BootstrapDialog.show({
				    title: "新增字典项",
				    type: BootstrapDialog.TYPE_DEFAULT,
				    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/dictionary/Add.jsp"),
				    draggable: true,
				    size: BootstrapDialog.SIZE_WIDE,
				    autodestroy: true,
				    closeByBackdrop: false,
				    autospin: true,
				    onshown: function(dialogRef) {
				    	dialogRef.getModalBody().find("form").validate();
				    	dialogRef.getModalBody().find("#parentCombotree").combotree({
   							url: getRoot() + "sys/dictionary/queryTree.action",
							async: true,
							singleSelect: true,
							loadOnExpand: false,
							loadParams: {
								"id": "dictionary.id"
							},
							idField: "id",
							textField: "text",
							name: "dictionary.pid"
   						});
				    },
				    buttons: [{
				    	label: "保存",
				    	icon: "fa fa-fw fa-save",
				    	cssClass: "btn-success",
				    	autospin: true,
				    	action: function(dialog) {
				    		var $button = this;
				    		try {
				    			var form = dialog.getModalBody().find("form");
				    			
				    			$.ajax({
				    				url: getRoot() + "sys/dictionary/add.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
				    						dictionaryList.refresh();
				    					} else {
				    						BootstrapDialog.danger(json.msg);
				    					}
				    				},
				    				error: function() {
				    					BootstrapDialog.danger("系统异常，请联系管理员");
				    				},
				    				beforeSend: function() {
				    					dialog.enableButtons(false);
				    					dialog.setClosable(false);
				    				},
				    				complete: function() {
				    					$button.stopSpin();
				    					dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    				}
				    			});
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
   			});
   		});
   		
   		function editDictionary(id) {
   			BootstrapDialog.show({
			    title: "编辑字典项",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/dictionary/queryById.action?returnType=edit&dictionary.id="+id),
			    draggable: true,
			    size: BootstrapDialog.SIZE_WIDE,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	dialogRef.getModalBody().find("form").validate();
			    	//初始化父部门树，只有在这里等弹出框的内容都加载完之后再加载树才能够获取到宽度
			    	dialogRef.getModalBody().find("#parentCombotree").combotree({
  						url: getRoot() + "sys/dictionary/queryTree.action",
						async: true,
						singleSelect: true,
						loadOnExpand: false,
						loadParams: {
							"id": "dictionary.id"
						},
						idField: "id",
						textField: "text",
						pidField: "pid",
						name: "dictionary.pid"
  					});
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	autospin: true,
			    	action: function(dialog) {
			    		try {
			    			var $button = this;
			    			var form = dialog.getModalBody().find("form");
			    			$.ajax({
			    				url: getRoot() + "sys/dictionary/update.action",
			    				data: form.serialize(),
			    				type: "POST",
			    				success: function(data) {
			    					var json = eval("("+data+")");
			    					if(json.success) {
			    						BootstrapDialog.success(json.msg);
			    						dialog.close();
			    						dictionaryList.refresh();
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
   		
   		function delDictionary(id) {
   			BootstrapDialog.confirm("您确定删除吗？", function(y) {
   				if(y) {
   					$.ajax({
   						url: getRoot() + "sys/dictionary/del.action",
	    				data: "dictionary.id="+id,
	    				type: "POST",
	    				success: function(data) {
	    					var json = eval("("+data+")");
	    					if(json.success) {
	    						BootstrapDialog.success(json.msg);
	    						dictionaryList.refresh();
	    					} else {
	    						BootstrapDialog.danger(json.msg);
	    					}
	    				}
   					});
   				}
   			});
   		}
   	</script>
  </body>
</html>
