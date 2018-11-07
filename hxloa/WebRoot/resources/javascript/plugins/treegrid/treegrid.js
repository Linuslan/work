(function($) {
	$.fn.treegrid = function(options) {
		var $this;
		var opts;
		$this = $(this);
		$this.opts = {
			title: "",
			currentPage: 1,
			pageSize: 10,
			totalPage: 0,
			startIndex: 0,
			endIndex: 0,
			sizeUnit: "px",
			singleSelect: true,
			url: "",
			async: false,
			idField: "id",
			textField: "text",
			pidField: "pid",
			loadOnExpand: false,
			totalWidth: 0,
			loadParams: {
				id: "id"
			},
			extraParams: {
				currentPage: 1,
				pageSize: 10
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
		};
		$.extend($this.opts.reader, options.reader);
		$.extend(options.reader, $this.opts.reader);
		$.extend($this.opts, options);
		$.extend($this, {
			init: function() {
				$this.html("");
				var container = document.createElement("div");
				$(container).addClass("treegrid");
				$this.append(container);
				$this.css({
					"overflow-x": "hidden",
					"overflow-y": "auto",
					"height": "99%"
				});
				var containerWidth = $this.opts.width ? $this.opts.width+$this.opts.sizeUnit : "100%";
				var fixedGridDiv = document.createElement("div");
				container.appendChild(fixedGridDiv);
				$(fixedGridDiv).addClass("treegrid-fixed-container");
				$(fixedGridDiv).css({
					"overflow-x": "hidden",
					"width": "100%",
					"height": "99%",
					"overflow-y": "auto"
				});
				$this.createHeader(fixedGridDiv);
				$this.createBody(fixedGridDiv);
				return $this;
			},
			
			/**
			 * 创建表格的头
			 */
			createHeader: function(fixedGridDiv, frozenGridDiv) {
				var columnWidthMap = $this.opts.columnWidthMap;
				if(!columnWidthMap) {
					columnWidthMap = new Object();
					$.extend($this.opts, {"columnWidthMap": columnWidthMap});
				}
				
				//获得解析显示数据的key
				var dataKey = $this.opts.reader.root;
				var sizeUnit = $this.opts.sizeUnit;
				
				//获得解析数据总数的key
				var totalRecordKey = $this.opts.reader.totalRecord;
				var containerWidth = $this.opts.width ? $this.opts.width+sizeUnit : "100%";
				
				/*
				 * 创建表格的表头部分
				 */
				var dataTableHeaderContainer = document.createElement("div");
				fixedGridDiv.appendChild(dataTableHeaderContainer);
				$(dataTableHeaderContainer).addClass("treegrid-fixed-container-header");
				$(dataTableHeaderContainer).attr("style", "width: "+containerWidth);
				var dataTableHeader = document.createElement("table");
				dataTableHeaderContainer.appendChild(dataTableHeader);
				$(dataTableHeader).addClass("treegrid-fixed-container-header-table");
				if($this.opts.sizeUnit == "%") {
					$(dataTableHeader).width("100%");
				}
				var dataTableThead = document.createElement("thead");
				dataTableHeader.appendChild(dataTableThead);
				var dataTableTheadTr = document.createElement("tr");
				dataTableThead.appendChild(dataTableTheadTr);
				
				//多选则有checkbox列
				var checkboxTd = $this.createCheckbox(dataTableTheadTr);
				if(checkboxTd && $(checkboxTd).length > 0) {
					$(checkboxTd).addClass("top-border");
				}
				var columns = $this.opts.columns;
				for(var index in columns) {
					var dataTableTheadTrTh = document.createElement("th");
					var borderRightStyle = "";
					if(parseInt(index) == 0 && $this.opts.singleSelect == true) {
						$(dataTableTheadTrTh).addClass("left-border");
						$(dataTableTheadTrTh).addClass("right-dotted-border");
					} else {
						$(dataTableTheadTrTh).addClass("right-dotted-border");
					}
					/*
					 * 如果是最后一列，则右边有边框
					 */
					if((parseInt(index) + 1) == columns.length) {
						$(dataTableTheadTrTh).addClass("right-border");
					}
					$(dataTableTheadTrTh).addClass("top-border");
					$(dataTableTheadTrTh).addClass("bottom-dotted-border");
					
					var column = columns[index];
					//text align style
					var align = "text-align: center;";
					/*
					 * if column defined align, setup the align what it defined
					 * else default the align center
					 */
					if(column.align && "" != column.align) {
						align = "text-align: "+column.align+";";
					}
					var columnWidth = column.width;
					if(column.width && column.width != "") {
						var sizeType = "px";
						if(sizeUnit && "" != $.trim(sizeUnit)) {
							sizeType = sizeUnit;
						}
						if(sizeType == "%") {
							var trWidth = $(dataTableTheadTr).width();
							columnWidth = columnWidth/100*trWidth-5;
						}
						//columnWidth = "width: "+column.width+sizeType;
					}
					$this.opts.totalWidth = parseFloat($this.opts.totalWidth) + columnWidth;
					dataTableTheadTr.appendChild(dataTableTheadTrTh);
					//dataTableTheadTrTh.setAttribute("style", align+borderRightStyle+columnWidth);
					dataTableTheadTrTh.setAttribute("style", align);
					$(dataTableTheadTrTh).width(columnWidth);
					columnWidthMap[index]=columnWidth;
					dataTableTheadTrTh.innerHTML = column.field;
				}
				if("%" == $this.opts.sizeUnit) {
					$this.opts.totalWidth = $(dataTableHeader).width();
				}
				$(dataTableHeader).css({
					"width": $this.opts.totalWidth+"px"
				});
				//创建固定表头结束
				
			},
			createBody: function(fixedGridDiv) {
				//开始创建表格的内容主体部分（非冻结列）
				/*var fixedBodyContainer = document.createElement("div");
				fixedGridDiv.appendChild(fixedBodyContainer);
				$(fixedBodyContainer).addClass("treegrid-fixed-container-body");
				var fixedBodyHeight = $this.opts.height ? $this.opts.height+$this.opts.sizeUnit : "100%";
				$(fixedBodyContainer).css("height", fixedBodyHeight);
				$(fixedBodyContainer).css({
					"overflow-y": "auto"
				});*/
				var fixedBodyContainerTable = document.createElement("table");
				/*fixedBodyContainer.appendChild(fixedBodyContainerTable);*/
				fixedGridDiv.appendChild(fixedBodyContainerTable);
				$(fixedBodyContainerTable).css({
					"width": $this.opts.totalWidth+"px"
				});
				$(fixedBodyContainerTable).addClass("treegrid-fixed-container-body-table");
				var fixedBodyContainerTableTbody = document.createElement("tbody");
				fixedBodyContainerTable.appendChild(fixedBodyContainerTableTbody);
				var level = 0;
				//$this.createChildren($this.opts.data, true, null, fixedBodyContainerTableTbody, level, true);
				if($this.opts.async == true) {
					$this.requestData($this.opts.url);
				} else {
					$this.createChildren($this.opts.data, true, null, fixedBodyContainerTableTbody, level, true);
				}
			},
			/**
			 * 传入数据，创建子节点
			 */
			createChildren: function(children, isRoot, parent, fixedBodyContainerTableTbody, level, isOpen) {
				var dataKey = $this.opts.reader.root;
				var childCountKey = $this.opts.reader.childCount;
				var idField = $this.opts.idField;
				var rowMap = $this.opts.rowMap;
				if(!rowMap || rowMap.length <= 0) {
					rowMap = new Object();
					$.extend($this.opts, {"rowMap": rowMap});
				}
				if(isRoot) {
					if(fixedBodyContainerTableTbody && $(fixedBodyContainerTableTbody).length >= 1) {
						if(children && children.length > 0) {
							for(var index in children) {
								var data = children[index];
								var childCount = data[childCountKey];
								var fixedTr = document.createElement("tr");
								var nodeData = children[index];
								$.extend(fixedTr, {"rowData": data});
								var pid = $this.generateSeq(10);
								if(isOpen) {

								} else {
									$(tr).css({
										"display": "none"
									});
								}
								fixedBodyContainerTableTbody.appendChild(fixedTr);
								
								fixedTr.onmouseenter = function() {
									$(this).addClass("hover-tr");
								}
								fixedTr.onmouseleave = function() {
									$(this).removeClass("hover-tr");
								}
								fixedTr.onclick = function() {
									var $tr = $(this);
									var nodeData = $tr[0].nodeData;
									var $table = $tr.parent();
									var $trs = $table.find("tr");
									$trs.removeClass("select-tr");
									$tr.addClass("select-tr");
									if($this.opts.onRowClick) {
										$this.opts.onRowClick(nodeData);
									}
								}
								fixedTr.ondblclick = function() {
									if($(this)[0].opened == true) {
										$this.collapse($(this)[0]);
									} else {
										$this.expand($(this)[0]);
									}
								}
								var checkboxTd = $this.createCheckbox(fixedTr);
								$this.createFieldTd(data, fixedTr, level);
								var childrenData = data[dataKey];
								var leaf = data.leaf;
								$.extend(nodeData, {"randomPid": pid, "level": level, "leaf": leaf});
								$.extend(fixedTr, {"nodeData": nodeData});
								var id = data[idField];
								rowMap[id] = fixedTr;
								if((childrenData && 0 < childrenData.length)
									 || (childCount && 0 < childCount)) {
									var childLevel = level + 1;
									leaf = false;
									$this.createChildren(childrenData, false, fixedTr, null, childLevel, data.isOpen);
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
								var table = $(parent).parent();
								var tr = $(table).find("tr."+pNodeId);
								var td = undefined;
								var div = undefined;
								var table = undefined;
								var tbody = undefined;
								if(!tr || $(tr).length <= 0) {
									tr = document.createElement("tr");
									$(tr).addClass(""+pNodeId);
									if(isOpen) {

									} else {
										$(tr).css({
											"display": "none"
										});
									}
									
									var pEl = parent.parentNode;
									//如果父节点的父标签元素的最后一个元素就是当前父节点
									//则在父节点的父标签添加存放子节点的tr标签
									//否则在父节点的下一个节点前添加存放子节点的tr标签
									if(pEl.lastChild == parent) {
										pEl.appendChild(tr);
									} else {
										pEl.insertBefore(tr, parent.nextSibling)
									}
									$(tr).width($(pEl).width());
									td = document.createElement("td");
									td.colSpan = $this.opts.columns.length;
									tr.appendChild(td);
									$(td).width($(tr).width());
									div = document.createElement("div");
									td.appendChild(div);
									$(div).width($(td).outerWidth());
									table = document.createElement("table");
									div.appendChild(table);
									$(table).width($(div).width());
									tbody = document.createElement("tbody");
									table.appendChild(tbody);
									$(tbody).width($(table).width());
								} else {
									td = $(tr).find("td");
									div = $(td).find("div");
									table = $(div).find("table");
									tbody = $(table).find("tbody")[0];
								}
								
								var childTr = document.createElement("tr");
								$.extend(childTr, {"rowData": data});
								var nodeData = children[index];
								var pid = $this.generateSeq(10);
								tbody.appendChild(childTr);
								childTr.onmouseenter = function() {
									$(this).addClass("hover-tr");
								}
								childTr.onmouseleave = function() {
									$(this).removeClass("hover-tr");
								}
								childTr.onclick = function() {
									var $tr = $(this);
									var nodeData = $tr[0].nodeData;
									var $table = $tr.parents("div.treegrid");
									var $trs = $table.find("tr");
									$trs.removeClass("select-tr");
									$tr.addClass("select-tr");
									if($this.opts.onRowClick) {
										$this.opts.onRowClick(nodeData);
									}
								}
								childTr.ondblclick = function() {
									if($(this)[0].opened == true) {
										$this.collapse($(this)[0]);
									} else {
										$this.expand($(this)[0]);
									}
								}
								$(childTr).width($(tbody).width());
								$this.createCheckbox(childTr);
								$this.createFieldTd(data, childTr, level);
								
								var childrenData = data[dataKey];
								var leaf = data.leaf;
								$.extend(nodeData, {"randomPid": pid, "level": level, "leaf": leaf});
								$.extend(childTr, {"nodeData": nodeData});
								var id = data[idField];
								rowMap[id] = childTr;
								if((childrenData && 0 < childrenData.length)
									 || (childCount && 0 < childCount)) {
									leaf = false;
									var childLevel = level + 1;
									$this.createChildren(childrenData, false, childTr, null, childLevel, data.isOpen);
								}
							}
						}
					}
				}
			},
			createCheckbox: function(tr) {
				var checkboxTd;
				if(tr && $(tr).length > 0) {
					//多选则有checkbox列
					if(false == $this.opts.singleSelect) {
						var trParent = $(tr).parent();
						var checkboxSpan = document.createElement("span");
						$(checkboxSpan).addClass("checkbox");
						if(tr.rowData) {
							if(tr.rowData.checked == true) {
								$(checkboxSpan).addClass("checkedbox");
								$(checkboxSpan).removeClass("checkbox");
								var checkedIds = $this.checkedIds;
								if(!checkedIds) {
									checkedIds = new Array();
								}
								checkedIds.push(tr.rowData[$this.opts.idField]);
								$.extend($this, {"checkedIds": checkedIds});
							}
							$.extend(checkboxSpan, {"data": tr.rowData});
						}
						if(trParent[0].nodeName == "THEAD") {
							checkboxTd = document.createElement("th");
							//如果是标题里面的td，则点击的时候为全选或者全不选
							checkboxSpan.onclick = function() {
								if($(this).hasClass("checkbox")) {
									$(this).addClass("checkedbox");
									$(this).removeClass("checkbox");
									$this.find("table.treegrid-fixed-container-body-table").find("tr").find("td").find("span.checkbox").addClass("checkedbox");
									$this.find("table.treegrid-fixed-container-body-table").find("tr").find("td").find("span.checkbox").removeClass("checkbox");
									$this.find("table.treegrid-fixed-container-body-table").find("tr").find("td").addClass("checked");
									var checkeds = $this.find("table.treegrid-fixed-container-body-table").find("tr").find("td").find("span.checkedbox");
									var checkedIds = new Array();
									for(var i = 0; i < checkeds.length; i ++) {
										var checked = checkeds[i];
										var data = checked.data;
										if(data[$this.opts.idField]) {
											checkedIds.push(data[$this.opts.idField]);
										}
									}
									$.extend($this, {"checkedIds": checkedIds});
								} else {
									$(this).addClass("checkbox");
									$(this).removeClass("checkedbox");
									$this.find("table.treegrid-fixed-container-body-table").find("tr").find("td").find("span.checkedbox").addClass("checkbox");
									$this.find("table.treegrid-fixed-container-body-table").find("tr").find("td").find("span.checkedbox").removeClass("checkedbox");
									$this.find("table.treegrid-fixed-container-body-table").find("tr").find("td").removeClass("checked");
									var checkedIds = new Array();
									$.extend($this, {"checkedIds": checkedIds});
								}
							}
						} else {
							checkboxTd = document.createElement("td");
							//如果是内容行，则点击的时候只为单行的选择或者取消选择
							checkboxSpan.onclick = function() {
								if($(this).hasClass("checkbox")) {
									$(this).addClass("checkedbox");
									$(this).removeClass("checkbox");
									$(tr).addClass("checked");
									var checkedIds = $this.checkedIds;
									if(!checkedIds) {
										checkedIds = new Array();
									}
									checkedIds.push(this.data[$this.opts.idField]);
									$.extend($this, {"checkedIds": checkedIds});
								} else {
									$(this).addClass("checkbox");
									$(this).removeClass("checkedbox");
									$(tr).removeClass("checked");
									var checkedIds = $this.checkedIds;
									if(!checkedIds) {
										checkedIds = new Array();
									}
									$this.remove(checkedIds, this.data[$this.opts.idField]);
									$.extend($this, {"checkedIds": checkedIds});
								}
							}
						}
						
						tr.appendChild(checkboxTd);
						$(checkboxTd).addClass("left-border");
						$(checkboxTd).addClass("right-dotted-border");
						$(checkboxTd).addClass("bottom-dotted-border");
						$(checkboxTd).css({
							"width": "30px",
							"text-align": "center"
						});
						checkboxTd.appendChild(checkboxSpan);
					}
				}
				
				return checkboxTd;
			},
			createFieldTd: function(data, tr, level) {
				var dataKey = $this.opts.reader.root;
				var childCountKey = $this.opts.reader.childCount;
				var childCount = data[childCountKey];
				if(tr && $(tr).length > 0
						&& data) {
					var columns = $this.opts.columns;
					if(columns && columns.length > 0) {
						var i = 0;
						for(var index in columns) {
							var column = columns[index];
							var dataIndex = column.dataIndex;
							var width = column.width;
							var td = document.createElement("td");
							var align="left";
							if(column.align) {
								align = column.align;
							}
							if(parseInt(index) == 0 && $this.opts.singleSelect == true) {
								$(td).addClass("left-border");
								$(td).addClass("right-dotted-border");
							} else {
								$(td).addClass("right-dotted-border");
								$(td).css({
									"text-align": align
								});
							}
							/*
							 * 如果是最后一列，则右边有边框
							 */
							if((parseInt(index) + 1) == columns.length) {
								$(td).addClass("right-border");
							}
							$(td).addClass("bottom-dotted-border");
							tr.appendChild(td);
							//treegrid中，i==0表示第一列，只有第一列才有各种图标之类的
							//大于0则表示是表格的其他列，则只是显示数据
							if(i == 0) {
								i ++;
								for(var j = 0; j < level; j ++) {
									var indentSpan = document.createElement("span");
									$(indentSpan).addClass("tree-indent");
									td.appendChild(indentSpan);
								}
								var children = data[dataKey];
								var expandSpan = document.createElement("span");
								$(expandSpan).addClass("node-state-span");
								td.appendChild(expandSpan);
								var folderSpan = document.createElement("span");
								$(folderSpan).addClass("node-type-span");
								td.appendChild(folderSpan);
								if(children && children.length > 0) {
									if(data.isOpen == true || data.leaf == false) {
										$(expandSpan).addClass("tree-expanded");
										$(folderSpan).addClass("tree-folder-open");
										expandSpan.onclick = (function(parentNode){
											return function() {
												$this.collapse(parentNode);
											}
											
										})(tr);
										data.isOpen = true;
									} else {
										$(expandSpan).addClass("tree-collapsed");
										$(folderSpan).addClass("tree-folder");
										expandSpan.onclick = (function(parentNode) {
											return function() {
												$this.expand(parentNode);
											}
										})(tr);
									}
									
								} else {
									if((childCount && childCount > 0) || data.leaf == false) {
										if(data.isOpen == true) {
											$(expandSpan).addClass("tree-expanded");
											$(folderSpan).addClass("tree-folder-open");
											expandSpan.onclick = (function(parentNode) {
												return function() {
													$this.collapse(parentNode);
												}
											})(tr);
										} else {
											$(expandSpan).addClass("tree-collapsed");
											$(folderSpan).addClass("tree-folder");
											expandSpan.onclick = (function(parentNode) {
												return function() {
													$this.expand(parentNode);
												}
											})(tr);
										}
									} else {
										$(expandSpan).addClass("tree-indent");
										$(folderSpan).addClass("tree-file");
									}
								}
								
							}
							/*if($this.opts.sizeUnit == "%") {
								var trWidth = $(tr).width();
								width = width/100*trWidth;
							}*/
							$(td).width($this.opts.columnWidthMap[index]);
							var contentSpan = document.createElement("span");
							$(contentSpan).addClass("tree-title");
							if(column.formatter) {
								var formatter = column.formatter(data);
								contentSpan.innerHTML = formatter;
							} else {
								contentSpan.innerHTML = $this.getDataByDataIndex(data, dataIndex);
							}
							td.appendChild(contentSpan);
						}
					}
				}
			},
			
			expandChild: function(parentNode) {
				if(parentNode) {
					var pid = parentNode.nodeData.randomPid;
					var children = $(parentNode).parent().find("tr."+pid);
					if(children
						&& children.length > 0) {
						for(var i = 0; i < children.length; i ++) {
							var childTr = children[i];
							if(childTr && childTr.nodeName == "TR") {
								$(childTr).slideDown(0, function() {
				    				
			    				});
							}
						}
						var stateSpan = $(parentNode).find("span.node-state-span");
		    			var typeSpan = $(parentNode).find("span.node-type-span");
		    			$(stateSpan).toggleClass("tree-expanded");
		    			$(stateSpan).toggleClass("tree-collapsed");
		    			$(typeSpan).toggleClass("tree-folder");
		    			$(typeSpan).toggleClass("tree-folder-open");
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
						var children = $(parentNode).parent().find("tr."+pid);
						//如果loadOnExpand配置为false，则表示每次展开子节点时不用从服务端加载子节点
						//如果当前父节点已经有所属的子节点了，那么就展开子节点
						//如果以上两个条件有一个不满足时，则从服务端加载子节点
						if($this.opts.loadOnExpand == false && children.length > 0) {
							$this.expandChild(parentNode);
						} else {
							var loadParams = $this.opts.loadParams;
							var param = new Object();
							for(var key in loadParams) {
								param[loadParams[key]]=nodeData[key];
							}
							$this.requestData($this.opts.url, param);
						}
						
					}
				} else {
					$this.expandChild(parentNode);
				}
				parentNode.opened = true;
			},

			collapse: function(parentNode) {
				var pid = parentNode.nodeData.randomPid;
				var children = $(parentNode).parent().find("tr."+pid);
				var async = $this.opts.async;
				if(children
					&& children.length > 0) {
					for(var i = 0; i < children.length; i ++) {
						var childTr = children[i];
						if(childTr && childTr.nodeName == "TR") {
							$(childTr).slideUp(0, function() {
			    				
		    				});
							
						}
						//如果配置了loadOnExpand == true，则表示每次展开都从服务端加载子节点
						//所以再收起子节点的时候要删除子节点，否则会重复添加子节点
						if($this.opts.loadOnExpand == true) {
							childTr.remove();
						}
					}
					var stateSpan = $(parentNode).find("span.node-state-span");
	    			var typeSpan = $(parentNode).find("span.node-type-span");
	    			$(stateSpan).toggleClass("tree-expanded");
	    			$(stateSpan).toggleClass("tree-collapsed");
	    			$(typeSpan).toggleClass("tree-folder");
	    			$(typeSpan).toggleClass("tree-folder-open");
	    			$(stateSpan)[0].onclick=(function(pNode) {
						return function() {
							$this.expand(pNode);
						}
					})(parentNode);
				}
				parentNode.opened = false;
			},

			/**
			 * 通过传入的dataIndex，从data中获取显示的值
			 * 如果dataIndex中包含.号，则分割dataIndex，
			 * 再层层从data中取出来
			 */
			getDataByDataIndex: function(data, dataIndex) {
				var text = "";
				try {
					var dataIndexArr = dataIndex.split(".");
					if(dataIndexArr && dataIndexArr.length > 0) {
						for(var i in dataIndexArr) {
							var index = dataIndexArr[i];
							data = data[index];
						}
						if(data || data == 0) {
							text = data;
						}
					} else {
						text = data;
					}
				} catch(ex) {
					
				}
				return text;
			},
			requestData: function(url, params, isClear) {
				var msg = $this.opts.reader.message;
				
				//获得解析显示数据的key
				var dataKey = $this.opts.reader.root;
				
				//获得解析数据总数的key
				var totalRecordKey = $this.opts.reader.totalRecord;
				var pidKey = $this.opts.pidField;
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
									var totalRecord = json[totalRecordKey];
									$this.setTotalRecord(totalRecord);
									$this.setData(data);
									var param = {
										"pid": pid,
										"data": data
									}
									if(true == isClear) {
										$this.clear();
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
								var totalRecord = json[totalRecordKey];
								$this.setTotalRecord(totalRecord);
								$this.setData(data);
								var param = {
									"pid": pid,
									"data": list
								}
								if(true == isClear) {
									$this.clear();
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
			appendChild: function(param) {
				var pid = param.pid;
				var isRoot = true;
				var fixedBodyContainerTableTbody = null;
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
					fixedBodyContainerTableTbody = $this.find("table.treegrid-fixed-container-body-table").find("tbody");
					if(fixedBodyContainerTableTbody && fixedBodyContainerTableTbody.length > 0) {
						fixedBodyContainerTableTbody = fixedBodyContainerTableTbody[0];
					}
				}
				
				$this.createChildren(data, isRoot, parent, fixedBodyContainerTableTbody, level, isOpen);
				$this.expandChild(parent);
			},
			clear: function() {
				fixedBodyContainerTableTbody = $this.find("table.treegrid-fixed-container-body-table").find("tbody");
				if(fixedBodyContainerTableTbody && fixedBodyContainerTableTbody.length > 0) {
					fixedBodyContainerTableTbody.html("");
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
			setTotalRecord: function(totalRecord) {
				$this.opts.totalRecord = totalRecord;
			},
			refresh: function() {
				$this.init();
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
			getCheckedValues: function() {
				var checkedIds = $this.checkedIds;
				if(checkedIds && 0 < checkedIds.length) {
					return checkedIds.join(",");
				} else {
					return "";
				}
			},
			getSelected: function() {
				var data;
				try {
					var $table = $this.find("table.treegrid-fixed-container-body-table");
					var selectTR = $table.find("tr.select-tr")[0];
					var data = selectTR.rowData;
				} catch(ex) {
					
				}
				return data;
			},
			load: function(param) {
				var url = $this.opts.url;
				$this.clear();
				$this.requestData(url, param, true);
			},
			loadByUrl: function(url, param) {
				if(url && 0 < url.length) {
					$this.clear();
					$this.requestData(url, param, true);
				}
			}
		});
		return $this.init();
	}
})(jQuery);