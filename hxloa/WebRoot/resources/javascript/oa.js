function getRoot() {
	/**/
	var app = "hxloa";
	var webRoot = "";
	var href = window.location.href;
	var pathname = window.location.pathname;
	if($.trim(pathname) && $.trim(pathname) != "/") {
		var webpath = href.substring(0, href.indexOf(pathname)+1);
		var pathnameArr = pathname.split("/");
		var appname = pathnameArr[1];
		if($.trim(appname) && $.trim(appname) == app) {
			webRoot = webpath + appname + "/";
		} else {
			webRoot = webpath;
		}
	} else {
		webRoot = href;
	}
	return webRoot;
	//return webpath + appname + "/";
	
	//return "http://oa.520aj.com:8080/";
}

/**
 * 重新设置表单的name
 * 因为form加载数据需要，所以初始化的form表单的name只是设置为name: property，而不是name: object.property；
 * 这样设置，会导致服务端的对象没办法获得值，所以在提交到服务端之前，在submit里取到form的各个组件，将name改
 * 为name: object.property
 * 参考role.Edit.js
 */
function resetFieldsName(form, prefix, filter) {
	var basicForm = form.getForm();
	var fields = basicForm.getFields();
	
	var filterNames = "|";
	/*
	 * 得到需过滤的参数，即不需要添加前缀的
	 */
	if(filter && ""!=$.trim(filter) && 0 <= filter.indexOf(",")) {
		var arr = filter.split(",");
		
		for(var index in arr) {
			var name = arr[index];
			if(name && "" != $.trim(name)) {
				filterNames = filterNames + name + "|";
			}
		}
	}
	
	fields.each(function(item) {
		var name = item.name;
		var filterName = "|"+name+"|";
		/*
		 * 如果0>filterNames.indexOf(filterName)
		 * 则说明过滤的字段里面不包含该字段，不需要过滤，则添加前缀
		 */
		if(0 > filterNames.indexOf(filterName)) {
			item.name = prefix+"."+name;
		}
	});
}

/**
 * 将改变过后的name，重新还原，否则，如果没有还原，则会导致报错没有关闭重新打开的时候，
 * 调用resetFieldsName会再次将prefix加入。
 * @param form
 * @param prefix
 */
function restoreFieldsName(form) {
	var basicForm = form.getForm();
	var fields = basicForm.getFields();
	fields.each(function(item) {
		var name = item.name;
		if(0 < name.indexOf(".")) {
			var arr = name.split(".");
			var restoreName = "";
			for(var i = 1; i < arr.length; i ++) {
				var restoreName = restoreName + arr[i] + ".";
			}
			restoreName = restoreName.substring(0, restoreName.length - 1);
			item.name = restoreName;
		}
	});
}

/**
 * 展开树的节点
 * @param tree 需展开的树对象
 * @param node 树的节点对象
 * @param path 需展开的节点所处的路劲，从根路径一直到节点，例如/0/1/2，0为根路径，1为父节点id，2为子节点id
 * @param record 需展开的节点对象
 */
function expandTree(tree, node, path, record) {
	//alert(path.indexOf(node.getId()) > 0);
	//alert(path+", "+node.getId());
	if(path.indexOf(node.getId()) > 0) {
		tree.getStore().load({
			node: node,
			callback: function() {
				tree.expandNode(node);
				if(node.getId() != record.getId()) {
					node.eachChild(function(child) {
						if(path.indexOf(child.getId()) > 0) {
							expandTree(tree, child, path, record);
						}
					});
				} else {
					tree.getSelectionModel().select(node);
					tree.expandNode(node);
				}
			}
		});
	}
}

/**
 * 展开树的节点，每个节点的子节点都会被遍历到
 * @param tree 需展开的树对象
 * @param node 树的节点对象
 * @param path 需展开的节点所处的路劲，从根路径一直到节点，例如/0/1/2，0为根路径，1为父节点id，2为子节点id
 * @param id 需展开的节点对象的id
 */
function expandTreeById(tree, node, id) {
	tree.getStore().load({
		node: node,
		callback: function() {
			tree.expandNode(node);
			if(node.getId() != id) {
				node.eachChild(function(child) {
					expandTreeById(tree, child, id);
				});
			} else {
				tree.getSelectionModel().select(node);
				tree.expandNode(node);
			}
		}
	});
}

function getNodePath(tree, selRec) {
	var path = "";
	if(tree) {
		var node = tree.getStore().getById(selRec.get("id"));
		path = node.getPath("id");
	}
	return path;
}

/**
 * 判断按钮权限
 * @param value
 * @returns {Boolean}
 */
function buttonPermission(value) {
	var results = false;
	if(permission) {
		if(permission.buttons.indexOf(value) >= 0) {
			results = true;
		}
	}
	alert(results);
	return results;
}

/**
 * 自定义checkbox的样式控制
 */
$(function(){
    $(document).on("click", "span.checkbox", function(){
    		var $input = $(this).parent("span.checkbox-div").find("input[type=checkbox]");
    		var checked = $input.attr("checked");
        if (checked) {
            $(this).removeAttr("class");
            $(this).attr("class", "checkbox");
            $(this).parent("span.checkbox-div").find("input[type=checkbox]").removeAttr("checked");
            return false;
        }else{
        	$(this).removeAttr("class");
            $(this).attr("class", "check checkbox");
            $(this).parent("span.checkbox-div").find("input[type=checkbox]").attr("checked", "checked");;
            return false;
        }
    });
    $(document).on("mouseenter", "span.checkbox", function() {
    	var $input = $(this).parent("span.checkbox-div").find("input[type=checkbox]");
    	var checked = $input.attr("checked");
    	if(checked) {
            $(this).removeAttr("class");
            $(this).attr("class", "checked-hover checkbox");
            return false;
       }else{
            $(this).removeAttr("class");
            $(this).attr("class", "unchecked-hover checkbox");
            return false;
       }
    });
   	$(document).on("mouseleave", "span.checkbox", function() {
    	var $input = $(this).parent("span.checkbox-div").find("input[type=checkbox]");
    	var checked = $input.attr("checked");
    	if(checked) {
            $(this).removeAttr("class");
            $(this).attr("class", "check checkbox");
            return false;
       }else{
            $(this).removeAttr("class");
            $(this).attr("class", "checkbox");
            return false;
       }
    });
});