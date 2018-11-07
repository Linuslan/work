(function($) {
	$.fn.combotree = function(options) {
		var $this = $(this);
		//取初始值
		var checkedIds = new Array();
		$this.checkedIds = checkedIds;
		var value = $this.attr("value");
		if(!value) {
			value = "";
		}
		var valueArray = new Array();
		var valueArr = value.split(",");
		for(var i = 0; i < value.split(",").length; i ++) {
			var v = valueArr[i];
			if(v) {
				try {
					$this.checkedIds.push(parseInt(v));
				} catch(e) {
					$this.checkedIds.push(v);
				}
			}
		}
		//暂时没有用，只是作个临时保存
		$this.initValues = $this.checkedIds;
		
		//取初始值
		var checkedTexts = new Array();
		$this.checkedTexts = checkedTexts;
		var text = $this.attr("text");
		if(!text) {
			text = "";
		}
		var textArray = new Array();
		var textArr = text.split(",");
		for(var i = 0; i < text.split(",").length; i ++) {
			var v = textArr[i];
			if(v) {
				try {
					$this.checkedTexts.push(v);
				} catch(e) {
					$this.checkedTexts.push(v);
				}
			}
		}
		//暂时没有用，只是作个临时保存
		$this.initTexts = $this.checkedTexts;
		
		//记录点击了多少次，第一次点击，则从原始数据中判断节点是否被选中，多次点击后，则节点的选中状态有可能已经改变
		$this.count = 0;
		$this.opts = {
			singleSelect: true,
			url: "",
			idField: "id",
			textField: "text",
			loadOnExpand: false,
			async: false,
			pidField: "pid",
			name: "",
			loadParams: {
				id: "id"
			},
			/*
			 * 数据解析器
			 */
			reader: {
				root: "children",	//服务端返回前端的json数据存放需显示记录的key
				totalRecord: "totalRecord",	//服务端返回前端的json数据存放记录共有多少条的key
				message: "msg",
				childCount: "childCount"
			},
			columns: [],
			data: []
		}
		//在重载opts之前，先把reader重载，否则前端重写了reader之后，重写的reader会覆盖掉默认的reader
		//所以先把重写的reader加载到默认的reader里，使默认的reader最新之后，再把默认的reader覆盖重写的reader
		//这样在重载opts时，reader覆盖掉的就是属性全的，且最新的reader
		$.extend($this.opts.reader, options.reader);
		$.extend(options.reader, $this.opts.reader);
		
		$.extend($this.opts, options);
		$.extend($this, {
			init: function() {
				$this.attr("tabindex", 1);
				$this.css({
					"position": "relative"
				});
				var div = document.createElement("div");
				$(div).css({
					"width": "100%",
					"position": "relative"
				});
				$this.append(div);
				$(div).addClass("input-group combotree");
				$(div).attr("tabindex", 1);
				$this.appendChild(div);
				//创建隐藏的input，用于真正的提交数据
				var hiddenInput = document.createElement("input");
				div.appendChild(hiddenInput);
				$(hiddenInput).attr("type", "hidden");
				if($this.opts.name && "" != $.trim($this.opts.name)) {
					$(hiddenInput).attr("name", $this.opts.name);
				}
				var input = document.createElement("input");
				$(input).addClass("form-control showText");
				$(input).attr("readonly", "readonly");
				$(input).css({
					"background": "#fff"
				});
				div.appendChild(input);
				var span = document.createElement("span");
				$(span).addClass("input-group-btn");
				div.appendChild(span);
				var btn = document.createElement("button");
				$(btn).attr("type", "button");
				span.appendChild(btn);
				$(btn).addClass("btn btn-info btn-flat");
				$(btn).css({
					"background": "#fff",
					"border-color": "#d2d6de",
					"color": "#000"
				});
				var btnIcon = document.createElement("i");
				btn.appendChild(btnIcon);
				$(btnIcon).addClass("fa fa-caret-down");

				var height = $(div).height();
				var width = $(div).width();
				var offset = $(div).offset();
				var left = offset.left;
				var top = offset.top+height;

				$(btn).click(function() {
					$(input).click();
				});
				if($this.find("div.combotree-body").length <= 0) {
					var treebody = document.createElement("div");
					$(treebody).addClass("combotree-body");
					$(treebody).css({
						"width": "100%",
						"position": "absolute",
						"left": 0,
						"top": height,
						"padding": "5px",
						"display": "none"
					});
					$(div).after(treebody);
					var ul = document.createElement("ul");
					treebody.appendChild(ul);
					var level = 0;
					if($this.opts.async == true) {
						$this.load();
					} else {
						$this.createChildren($this.opts.data, true, null, ul, level, true);
					}
				} else {
					$this.find("div.combotree-body").show();
				}
				$(input).click(function() {
					$this.focus();
					$this.count ++;
					if($this.find("div.combotree-body").length <= 0) {
						var treebody = document.createElement("div");
						$(treebody).addClass("combotree-body");
						$(treebody).css({
							"width": "100%",
							"position": "absolute",
							"left": 0,
							"top": height,
							"padding": "5px"
						});
						$(div).after(treebody);
						var ul = document.createElement("ul");
						treebody.appendChild(ul);
						var level = 0;
						if($this.opts.async == true) {
							$this.load();
						} else {
							$this.createChildren($this.opts.data, true, null, ul, level, true);
						}
					} else {
						$this.find("div.combotree-body").show();
					}
				});
				$this.blur(function() {
					var combotree = $this.find(".combotree-body");
					if(combotree && combotree.length >= 1) {
						if($this.opts.loadOnExpand == true) {
							combotree.remove();
						} else {
							combotree.hide();
						}
						
					}
				});
			},
			load: function(params) {

				var url = $this.opts.url;
				//获得解析显示数据的key
				var dataKey = $this.opts.reader.root;
				var msg = $this.opts.reader.message;
				var pidKey = $this.opts.pidField;
				if(params) {
					params = params.join("&");
				}
				$.ajax({
					url: url,
					data: params,
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						if(json.success) {
							if(true == json.success) {
								var data = json[dataKey];
								data = eval("("+data+")");
								var pid = json[pidKey];
								if(data && data.length > 0) {
									$this.setData(data);
									var param = {
										"pid": pid,
										"data": data
									}
									$this.appendChild(param);
								}
							} else {
								alert(json[msg]);
							}
						} else {
							var data = json[dataKey];
							data = eval("("+data+")");
							var pid = data[pidKey];
							if(data && data.length > 0) {
								$this.setData(data);
								var param = {
									"pid": pid,
									"data": list
								}
								$this.appendChild(param);
							}
						}
						
					},
					error: function() {
						alert("系统异常，请联系管理员！");
					}
				});
			},

			/**
			 * 传入数据，创建子节点
			 */
			createChildren: function(children, isRoot, parent, ul, level, isOpen) {
				var dataKey = $this.opts.reader.root;
				var childCountKey = $this.opts.reader.childCount;
				var idField = $this.opts.idField;
				var rowMap = $this.opts.rowMap;
				if(!rowMap || rowMap.length <= 0) {
					rowMap = new Object();
					$.extend($this.opts, {"rowMap": rowMap});
				}
				if(isRoot) {
					if(ul && $(ul).length >= 1) {
						if(children && children.length > 0) {
							for(var index in children) {
								var data = children[index];
								var childCount = data[childCountKey];
								var li = document.createElement("li");
								var nodeData = children[index];
								$.extend(li, {"nodeData": nodeData});
								var pid = $this.generateSeq(10);
								
								ul.appendChild(li);
								
								li.onmouseenter = function() {
									$(this).addClass("hover-li");
								}
								li.onmouseleave = function() {
									$(this).removeClass("hover-li");
								}
								li.onclick = function() {
									var $li = $(this);
									var $ul = $li.parent();
									var $lis = $ul.find("li");
									$lis.removeClass("select-li");
									$li.addClass("select-li");
									var nodeData = $li[0].nodeData;
									if($this.opts.onRowClick) {
										$this.opts.onRowClick(nodeData);
									}
								}

								var leaf = data.leaf;
								$.extend(nodeData, {"randomPid": pid, "level": level, "leaf": leaf});
								$.extend(li, {"nodeData": nodeData});
								var id = data[idField];
								rowMap[id] = li;
								//首次点击，则去判断原来的数据是否是选中的
								//如果不是首次点击，则有可能节点的选中已经改变了
								if($this.count>1) {
									var checkedIds = $this.checkedIds;
									if(0 <= checkedIds.indexOf(data[idField])) {
										li.checked = true;
									} else {
										li.checked = false;
									}
								} else {
									if(data.checked == true) {
										li.checked = true;
									} else if(0 <= $this.checkedIds.indexOf(data[idField])) {	//有可能有初始值
										li.checked = true;
									} else {
										li.checked = false;
									}
								}

								var childrenData = data[dataKey];
								if((childrenData && 0 < childrenData.length)
									 || (childCount && 0 < childCount)) {
									leaf = false;
									$.extend(nodeData, {"leaf": leaf});
								}
								$this.createNode(data, li, level);
								$this.setCheckedData(li);
								
								if((childrenData && 0 < childrenData.length)
									 || (childCount && 0 < childCount)) {
									var childLevel = level + 1;
									leaf = false;
									$.extend(nodeData, {"leaf": leaf});
									$this.createChildren(childrenData, false, li, null, childLevel, data.isOpen);
								}

							}
						}
					}
				} else {
					if(parent && 0 < $(parent).length) {
						if(children && children.length > 0) {
							for(var index in children) {
								var data = children[index];
								var childCount = data[childCountKey];
								var pNodeId = parent.nodeData.randomPid;
								var ul = $(parent).find("ul")[0];
								if(!ul || $(ul).length <= 0) {
									ul = document.createElement("ul");
									$(ul).addClass(""+pNodeId);
									if(isOpen) {

									} else {
										$(ul).css({
											"display": "none"
										});
									}
									parent.appendChild(ul);
								}
								
								var li = document.createElement("li");
								var nodeData = children[index];
								
								$.extend(li, {"nodeData": nodeData});
								
								var pid = $this.generateSeq(10);
								ul.appendChild(li);

								li.onmouseenter = function() {
									$(this).addClass("hover-li");
								}
								li.onmouseleave = function() {
									$(this).removeClass("hover-li");
								}
								li.onclick = function() {
									var $li = $(this);
									var $div = $li.parents("div.combotree-body");
									var $lis = $div.find("li");
									$lis.removeClass("select-li");
									$li.addClass("select-li");
									var nodeData = $li[0].nodeData;
									if($this.opts.onRowClick) {
										$this.opts.onRowClick(nodeData);
									}
								}

								var leaf = data.leaf;
								$.extend(nodeData, {"randomPid": pid, "level": level, "leaf": leaf});
								$.extend(li, {"nodeData": nodeData});
								var id = data[idField];
								rowMap[id] = li;
								if($this.count > 1) {
									var checkedIds = $this.checkedIds;
									if(0 <= checkedIds.indexOf(data[idField])) {
										li.checked = true;
									} else {
										li.checked = false;
									}
								} else {
									if(data.checked == true) {
										li.checked = true;
									} else if(0 <= $this.checkedIds.indexOf(data[idField])) {	//有可能有初始值
										li.checked = true;
									} else {
										li.checked = false;
									}
								}
								var childrenData = data[dataKey];
								if((childrenData && 0 < childrenData.length)
									 || (childCount && 0 < childCount)) {
									leaf = false;
									$.extend(nodeData, {"leaf": leaf});
								}
								
								$this.createNode(data, li, level);
								$this.setCheckedData(li);
								
								if((childrenData && 0 < childrenData.length)
									 || (childCount && 0 < childCount)) {
									leaf = false;
									$.extend(nodeData, {"leaf": leaf});
									var childLevel = level + 1;
									$this.createChildren(childrenData, false, li, null, childLevel, data.isOpen);
								}
								
							}
						}
					}
				}
			},
			createNode: function(data, li, level) {
				var dataKey = $this.opts.reader.root;
				var childCountKey = $this.opts.reader.childCount;
				var childCount = data[childCountKey];
				var textKey = $this.opts.textField;
				var idKey = $this.opts.idField;
				if(li && $(li).length > 0
						&& data) {
					var div = document.createElement("div");
					li.appendChild(div);
					for(var j = 0; j < level; j ++) {
						var indentSpan = document.createElement("span");
						$(indentSpan).addClass("tree-indent");
						div.appendChild(indentSpan);
					}
					
					var children = data[dataKey];
					var expandSpan = document.createElement("span");
					$(expandSpan).addClass("node-state-span");
					div.appendChild(expandSpan);
					var folderSpan = document.createElement("span");
					$(folderSpan).addClass("node-type-span");
					div.appendChild(folderSpan);
					/*if(data.name == "行政部") {
						alert(data.leaf);
					}*/
					if((children && children.length > 0) || data.leaf == false) {
						if(data.isOpen == true) {
							$(expandSpan).addClass("expanded");
							$(folderSpan).addClass("folder-open");
							expandSpan.onclick = (function(parentNode){
								return function() {
									$this.collapse(parentNode);
								}
								
							})(li);
						} else {
							$(expandSpan).addClass("collapsed");
							$(folderSpan).addClass("folder");
							expandSpan.onclick = (function(parentNode) {
								return function() {
									$this.expand(parentNode);
									data.isOpen = true;
								}
							})(li);
						}
					} else {
						if((childCount && childCount > 0) || data.leaf == false) {
							if(data.isOpen == true) {
								$(expandSpan).addClass("expanded");
								$(folderSpan).addClass("folder-open");
								expandSpan.onclick = (function(parentNode) {
									return function() {
										$this.collapse(parentNode);
									}
								})(li);
							} else {
								$(expandSpan).addClass("collapsed");
								$(folderSpan).addClass("folder");
								expandSpan.onclick = (function(parentNode) {
									return function() {
										$this.expand(parentNode);
										data.isOpen = true;
									}
								})(li);
							}
						} else {
							$(expandSpan).addClass("tree-indent");
							$(folderSpan).addClass("leaf");
						}
					}
					
					/**
					 * 如果不是单选，则添加复选框
					 */
					if($this.opts.singleSelect == false) {
						var checkboxSpan = document.createElement("span");
						$(checkboxSpan).addClass("checkbox");
						div.appendChild(checkboxSpan);
						if(li.checked == true) {
							$(checkboxSpan).removeClass("checkbox");
							$(checkboxSpan).addClass("checkedbox");
						} else {
							$(checkboxSpan).removeClass("checkedbox");
							$(checkboxSpan).addClass("checkbox");
						}
						//checkbox点击时，将节点的数据存入到checkedNodes中
						//以便显示在combotree的input中
						checkboxSpan.onclick=function() {
							var checked = li.checked;
							if(checked == true) {
								li.checked = false;
								$(this).removeClass("checkedbox");
								$(this).addClass("checkbox");
							} else {
								li.checked = true;
								$(this).removeClass("checkbox");
								$(this).addClass("checkedbox");
							}
							
							$this.setCheckedData(li);
						}
					} else {
						//创建li的时候，已经判断是否li的checked属性为true
						//包括判断了checkedIds里面是否包含有该li的id
						$(li).find(">div").removeClass("selected");
						if(li.checked == true) {
							$(li).find(">div").addClass("selected");
						}
						var $title = $(li).find(">div>span.node-title")
						if(!$title || $title.length <= 0) {
							//显示节点名称的span
							var title = document.createElement("span");
							$(title).addClass("node-title");
							div.appendChild(title);
							var text = data[textKey];
							title.innerHTML = text;
							$title = $(title);
						}
						$title.css({
							"cursor": "pointer"
						});
						$title[0].onclick = function() {
							var checked = li.checked;
							if(checked == false) {
								li.checked = true;
							}
							$this.find("div.combotree-body").find("li>div").removeClass("selected");
							$(this).parent().addClass("selected");
							$this.setCheckedData(li);
						}
					}
					var $title = $(li).find(">div>span.node-title")
					if(!$title || $title.length <= 0) {
						//显示节点名称的span
						var title = document.createElement("span");
						$(title).addClass("node-title");
						div.appendChild(title);
						var text = data[textKey];
						title.innerHTML = text;
					}
				}
			},

			expandChild: function(parentNode) {
				if(parentNode) {
					var pid = parentNode.nodeData.randomPid;
					var children = $(parentNode).find(">ul");
					if(children
						&& children.length > 0) {
						for(var i = 0; i < children.length; i ++) {
							var ul = children[i];
							if(ul && ul.nodeName == "UL") {
								$(ul).fadeTo(200, 1, function() {
									$(ul).slideDown(0, function() {
				    				
				    				});
								});
							}
						}
						var stateSpan = $(parentNode).find("span.node-state-span")[0];
		    			var typeSpan = $(parentNode).find("span.node-type-span")[0];
		    			$(stateSpan).toggleClass("expanded");
		    			$(stateSpan).toggleClass("collapsed");
		    			$(typeSpan).toggleClass("folder");
		    			$(typeSpan).toggleClass("folder-open");
		    			$(stateSpan)[0].onclick=(function(pNode) {
							return function() {
								$this.collapse(pNode);
							}
						})(parentNode);
					}
				}
			},

			expand: function(parentNode) {
				var async = $this.opts.async;
				if(async) {
					var leaf = parentNode.nodeData.leaf;
					if(leaf == true) {
						return false;
					} else {
						var nodeData = parentNode.nodeData;
						var pid = nodeData.randomPid;
						var children = $(parentNode).find(">ul");
						//如果loadOnExpand配置为false，则表示每次展开子节点时不用从服务端加载子节点
						//如果当前父节点已经有所属的子节点了，那么就展开子节点
						//如果以上两个条件有一个不满足时，则从服务端加载子节点
						if($this.opts.loadOnExpand == false && children.length > 0) {
							$this.expandChild(parentNode);
						} else {
							var loadParams = $this.opts.loadParams;
							var param = new Array();
							for(var key in loadParams) {
								var paramStr = loadParams[key]+"="+nodeData[key];
								param.push(paramStr);
								//param[loadParams[key]]=nodeData[key];
							}
							$this.load(param);
						}
						
					}
				} else {
					$this.expandChild(parentNode);
				}
				parentNode.opened = true;
			},

			collapse: function(parentNode) {
				var pid = parentNode.nodeData.randomPid;
				var children = $(parentNode).find(">ul");
				var async = $this.opts.async;
				if(children
					&& children.length > 0) {
					for(var i = 0; i < children.length; i ++) {
						var ul = children[i];
						if(ul && ul.nodeName == "UL") {
							$(ul).fadeTo(200, 0.1, function() {
								$(ul).slideUp(0, function() {
			    				
			    				});
							});
						}
						//如果配置了loadOnExpand == true，则表示每次展开都从服务端加载子节点
						//所以再收起子节点的时候要删除子节点，否则会重复添加子节点
						if($this.opts.loadOnExpand == true) {
							ul.remove();
						}
					}
					var stateSpan = $(parentNode).find("span.node-state-span")[0];
	    			var typeSpan = $(parentNode).find("span.node-type-span")[0];
	    			$(stateSpan).toggleClass("expanded");
	    			$(stateSpan).toggleClass("collapsed");
	    			$(typeSpan).toggleClass("folder");
	    			$(typeSpan).toggleClass("folder-open");
	    			$(stateSpan)[0].onclick=(function(pNode) {
						return function() {
							$this.expand(pNode);
						}
					})(parentNode);
				}
				parentNode.opened = false;
			},

			//通过传入的参数，获取到节点的数据，从而加载节点
			appendChild: function(param) {
				var pid = param.pid;
				var isRoot = true;
				var ul = null;
				var data = param.data;
				var parent = null;
				var level = 0;
				var isOpen = false;
				if(pid && "" != $.trim(pid)) {
					parent = $this.opts.rowMap[pid];
					if(parent) {
						level = parent.nodeData.level;
						//添加子节点，则是下一级，所以父节点的Level加上一
						level ++;
						isOpen = parent.nodeData.isOpen;
					}
					isRoot = false;
				} else {
					level = 0;
					parent = null;
					ul = $this.find("div.combotree-body").children("ul");
					if(ul && ul.length > 0) {
						ul = ul[0];
					}
				}
				$this.createChildren(data, isRoot, parent, ul, level, isOpen);
				$this.expandChild(parent);
			},
			getSelection: function() {
				var checkedNodes = $this.checkedNodes;
				if(!checkedNodes) {
					checkedNodes = new Object();
				}
				return checkedNodes;
			},
			setCheckedData: function(node) {
				var idKey = $this.opts.idField;
				var textKey = $this.opts.textField;
				if(node.nodeData) {
					var checkedNodes = $this.checkedNodes;
					if(!checkedNodes) {
						checkedNodes = new Object();
						$.extend($this, checkedNodes);
					} else if($this.opts.singleSelect == true) {
						checkedNodes = new Object();
						$.extend($this, checkedNodes);
					}
					
					var checkedIds = $this.checkedIds;
					if(!checkedIds) {
						checkedIds = new Array();
						$.extend($this, checkedIds);
					} else if($this.opts.singleSelect == true) {	//如果是单选，则只会有一个选中的，那么每次都是新的储存容器
						if(node.checked == true) {	//只有该节点为选中状态时，才重新创建，否则说明没有选中其他节点，还是原来选中的节点，则不需要新创建
							checkedIds = new Array();
							$.extend($this, checkedIds);
						}
					}
					
					var checkedTexts = $this.checkedTexts;
					if(!checkedTexts) {
						checkedTexts = new Array();
						$.extend($this, checkedTexts);
					}  else if($this.opts.singleSelect == true) {	//如果是单选，则只会有一个选中的，那么每次都是新的储存容器
						if(node.checked == true) {	//只有该节点为选中状态时，才重新创建，否则说明没有选中其他节点，还是原来选中的节点，则不需要新创建
							checkedTexts = new Array();
							$.extend($this, checkedTexts);
						}
					}
					if(node.checked == true) {
						checkedNodes[node.nodeData.randomPid] = node;
						$this.remove(checkedIds, node.nodeData[idKey]);
						checkedIds.push(node.nodeData[idKey]);
						$this.remove(checkedTexts, node.nodeData[textKey]);
						checkedTexts.push(node.nodeData[textKey]);
					} else {
						delete checkedNodes[node.nodeData.randomPid];
						$this.remove(checkedIds, node.nodeData[idKey]);
						$this.remove(checkedTexts, node.nodeData[textKey]);
						//checkedIds.remove(node.nodeData[idKey]);
						//checkedTexts.remove(node.nodeData[textKey]);
					}
					$this.checkedTexts = checkedTexts;
					$this.checkedIds = checkedIds;
					$this.setValue();
				}
				
			},
			setValue: function() {
				var checkedTexts = $this.checkedTexts;
				var checkedIds = $this.checkedIds;
				if($this.opts.singleSelect == false) {
					$this.find("input.showText").val(checkedTexts.join(","));
					$this.find("input:hidden").val(checkedIds.join(","));
				} else {
					$this.find("input.showText").val(checkedTexts[0]);
					$this.find("input:hidden").val(checkedIds[0]);
				}
			},
			getValue: function() {
				if($this.opts.singleSelect == false) {
					return $this.checkedIds.join(",");
				} else {
					return $this.checkedIds[0];
				}
			},
			generateSeq: function(num) {
				var rnd="";
				for(var i=0;i<num;i++)
					rnd+=Math.floor(Math.random()*10);
				return rnd;
			},
			getData: function () {
				return $this.opts.datas;
			},
			setData: function (datas) {
				$this.opts.data = datas;
			},
			remove: function(array, obj) {
				if(array && 0 < array.length) {
					var a = array.indexOf(obj);
					if(a >= 0) {
						array.splice(a, 1);
						return true;
					}
				}
				return false;
			},
			destory: function() {
				$this.find("div").remove();
			}
		});
		$this.init();
		return $this;
	}
})(jQuery);;