(function($) {
	$.fn.datagrid = function(options) {
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
			url: "",
			extraParams: {
				currentPage: 1,
				pageSize: 10
			},
			
			/*
			 * 数据解析器
			 */
			reader: {
				root: "data",	//服务端返回前端的json数据存放需显示记录的key
				totalRecord: "totalRecord",	//服务端返回前端的json数据存放记录共有多少条的key
				message: "msg"
			},
			columns: [],
			data: []
		};
		$.extend($this.opts, options);
		$this.opts.extraParams.pageSize=$this.opts.pageSize;
		$.extend($this, {
			initUI: function ($this, opts) {
				var fixedGridDiv = document.createElement("div");
				$(fixedGridDiv).addClass("datagrid-fixed-container");
				var frozenGridDiv = document.createElement("div");
				$(frozenGridDiv).addClass("datagrid-frozen-container");
				var container = document.createElement("div");
				$(container).addClass("datagrid");
				container.appendChild(frozenGridDiv);
				container.appendChild(fixedGridDiv);
				$(container).css({
					"width": containerWidth
				});
				
				var clearDiv = document.createElement("div");
				$(clearDiv).css("clear", "both");
				container.appendChild(clearDiv);
				
				var pager = $this.initPageTool();
				container.appendChild(pager);
				$this.html(container);
				
				//获得解析显示数据的key
				var dataKey = $this.opts.reader.root;
				var sizeUnit = $this.opts.sizeUnit;
				
				//获得解析数据总数的key
				var totalRecordKey = $this.opts.reader.totalRecord;
				var borderColor = "#CDCDCD";
				var containerWidth = $this.opts.width ? $this.opts.width+sizeUnit : "100%";
				
				/*
				 * 创建表格的表头部分
				 */
				var dataTableHeaderContainer = document.createElement("div");
				$(dataTableHeaderContainer).addClass("datagrid-fixed-container-header");
				var dataTableHeader = document.createElement("table");
				$(dataTableHeader).addClass("datagrid-fixed-container-header-table");
				var dataTableThead = document.createElement("thead");
				var dataTableTheadTr = document.createElement("tr");
				
				var frozenColumnHeaderContainer = document.createElement("div");
				$(frozenColumnHeaderContainer).addClass("datagrid-frozen-container-header");
				var frozenColumnHeaderTable = document.createElement("table");
				$(frozenColumnHeaderTable).addClass("datagrid-frozen-container-header-table");
				var frozenColumnHeaderThead = document.createElement("thead");
				var frozenColumnHeaderTr = document.createElement("tr");
				
				var columns = opts.columns;
				var frozenTableWidth = 0;
				var fixedTableWidth = 0;
				for(var index in columns) {
					var borderRightStyle = "";
					/*
					 * 如果不是最后一列，则右边有边框
					 */
					if((parseInt(index) + 1) == columns.length) {
						borderRightStyle = "border-right: 1px dotted #CDCDCD;";
					}
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
					var columnWidth = "";
					if(column.width && column.width != "") {
						var sizeType = "px";
						if(sizeUnit && "" != $.trim(sizeUnit)) {
							sizeType = sizeUnit;
						}
						columnWidth = "width: "+column.width+sizeType;
					}
					
					var dataTableTheadTrTh = document.createElement("th");
					dataTableTheadTrTh.setAttribute("style", align+borderRightStyle+columnWidth);
					dataTableTheadTrTh.innerHTML = column.field;
					if(column.frozen == true) {
						frozenTableWidth += column.width;
						frozenColumnHeaderTr.appendChild(dataTableTheadTrTh);
					} else {
						fixedTableWidth += column.width;
						dataTableTheadTr.appendChild(dataTableTheadTrTh);
					}
				}
				dataTableThead.appendChild(dataTableTheadTr);
				dataTableHeader.appendChild(dataTableThead);
				$(dataTableHeader).css({
					"width": fixedTableWidth+sizeUnit
				});
				dataTableHeaderContainer.appendChild(dataTableHeader);
				fixedGridDiv.appendChild(dataTableHeaderContainer);
				
				frozenColumnHeaderThead.appendChild(frozenColumnHeaderTr);
				frozenColumnHeaderTable.appendChild(frozenColumnHeaderThead);
				$(frozenColumnHeaderTable).css({
					"width": frozenTableWidth+sizeUnit
				});
				frozenColumnHeaderContainer.appendChild(frozenColumnHeaderTable);
				
				frozenGridDiv.appendChild(frozenColumnHeaderContainer);
				//创建固定表头结束
				
				
				
				//开始创建表格的内容主体部分（非冻结列）
				var dataTableBodyContainer = document.createElement("div");
				$(dataTableBodyContainer).addClass("datagrid-fixed-container-body");
				var dataTableBodyHeight = $this.opts.height ? $this.opts.height+"px" : "100%";
				$(dataTableBodyContainer).css("height", dataTableBodyHeight);
				var dataTableBody = document.createElement("table");
				$(dataTableBody).addClass("datagrid-fixed-container-body-table");
				var dataTableTbody = document.createElement("tbody");
				
				//创建冻结列主体部分
				var frozenColumnBodyContainer = document.createElement("div");
				$(frozenColumnBodyContainer).addClass("datagrid-frozen-container-body");
				var frozenColumnBodyContainerHeight = $this.opts.height ? $this.opts.height+"px" : "100%";
				$(frozenColumnBodyContainer).css("height", frozenColumnBodyContainerHeight);
				var frozenColumnBodyTable = document.createElement("table");
				$(frozenColumnBodyTable).addClass("datagrid-frozen-container-body-table");
				var frozenColumnBodyTbody = document.createElement("tbody");
				
				
				//get grid's data set
				var dataKey = opts.reader.root;
				var datas = opts[dataKey];
				if(datas && datas.length > 0) {
					/*
					 * generate data's grid
					 */
					var totalWidth = 0;
					for(var index in datas) {
						var borderBottomStyle = "";
						/*
						 * 如果不是最后一行的td，则border-bottom:1px dotted #c5c5c5
						 */
						if((parseInt(index)+1) < datas.length) {
							borderBottomStyle = "border-bottom: 1px dotted "+borderColor+";";
						}
						var data = datas[index];
						var dataTableTbodyTr = document.createElement("tr");
						var frozenColumnTbodyTr = document.createElement("tr");
						dataTableTbodyTr.onmouseenter = function() {
							var rowIndex = this.rowIndex;
							var $tr = $(this);
							$tr.addClass("hover-tr");
							$($(frozenColumnBodyTbody).find("tr")[rowIndex]).addClass("hover-tr");
						}
						dataTableTbodyTr.onmouseleave = function() {
							var rowIndex = this.rowIndex;
							var $tr = $(this);
							$tr.removeClass("hover-tr");
							$($(frozenColumnBodyTbody).find("tr")[rowIndex]).removeClass("hover-tr");
						}
						dataTableTbodyTr.onclick = function() {
							var rowIndex = this.rowIndex;
							var $tr = $(this);
							var $table = $tr.parent();
							var $trs = $table.find("tr");
							$trs.removeClass("select-tr");
							$tr.addClass("select-tr");
							
							var $frozenTable = $(frozenColumnTbodyTr).parent();
							var $frozenTrs = $frozenTable.find("tr");
							$frozenTrs.removeClass("select-tr");
							$($(frozenColumnBodyTbody).find("tr")[rowIndex]).addClass("select-tr");
						}
						
						
						frozenColumnTbodyTr.onmouseenter = function() {
							var rowIndex = this.rowIndex;
							var $tr = $(this);
							$tr.addClass("hover-tr");
							$($(dataTableTbody).find("tr")[rowIndex]).addClass("hover-tr");
						}
						frozenColumnTbodyTr.onmouseleave = function() {
							var rowIndex = this.rowIndex;
							var $tr = $(this);
							$tr.removeClass("hover-tr");
							$($(dataTableTbody).find("tr")[rowIndex]).removeClass("hover-tr");
						}
						frozenColumnTbodyTr.onclick = function() {
							var rowIndex = this.rowIndex;
							var $tr = $(this);
							var $table = $tr.parent();
							var $trs = $table.find("tr");
							$trs.removeClass("select-tr");
							$tr.addClass("select-tr");
							
							var $fixedTable = $(dataTableTbodyTr).parent();
							var $fixedTrs = $fixedTable.find("tr");
							$fixedTrs.removeClass("select-tr");
							$($(dataTableTbody).find("tr")[rowIndex]).addClass("select-tr");
						}
						totalWidth = 0;
						for(var i in columns) {
							var column = columns[i];
							//text align style
							var align = "text-align: center;";
							/*
							 * if column defined align, setup the align what it defined
							 * else default the align center
							 */
							if(column.align && "" != column.align) {
								align = "text-align: "+column.align+";";
							}
							var borderRightStyle = "";
							/*
							 * 如果不是最后一个td，则borderStyle是1px dotted #CDCDCD;
							 */
							if((parseInt(i) + 1) == columns.length) {
								borderRightStyle = "border-right: 1px dotted #CDCDCD;";
							}
							var dataIndex = columns[i].dataIndex;
							var dataTableTbodyTrTd = document.createElement("td");
							var columnWidth = "";
							if(column.width && column.width != "") {
								var sizeType = "px";
								if(sizeUnit && "" != $.trim(sizeUnit)) {
									sizeType = sizeUnit;
								}
								columnWidth = "width: "+column.width+sizeType;
								totalWidth = parseInt(totalWidth) + parseInt(column.width);
							}
							dataTableTbodyTrTd.setAttribute("style", align+borderBottomStyle+borderRightStyle+columnWidth);
							if(columns[i].formatter) {
								var formatter = columns[i].formatter(data);
								dataTableTbodyTrTd.innerHTML = formatter;
							} else {
								dataTableTbodyTrTd.innerHTML = $this.getDataByDataIndex(data, dataIndex);
							}
							if(column.frozen == true) {
								frozenColumnTbodyTr.appendChild(dataTableTbodyTrTd);
							} else {
								dataTableTbodyTr.appendChild(dataTableTbodyTrTd);
							}
						}
						dataTableTbody.appendChild(dataTableTbodyTr);
						frozenColumnBodyTbody.appendChild(frozenColumnTbodyTr);
					}
				} 
				
				frozenColumnBodyTable.appendChild(frozenColumnBodyTbody);
				$(frozenColumnBodyTable).css({
					"width": frozenTableWidth+sizeUnit
				});
				frozenColumnBodyContainer.appendChild(frozenColumnBodyTable);
				frozenGridDiv.appendChild(frozenColumnBodyContainer);
				
				dataTableBody.appendChild(dataTableTbody);
				$(dataTableBody).css({
					"width": fixedTableWidth+sizeUnit
				});
				dataTableBodyContainer.appendChild(dataTableBody);
				/*
				 * 滚动事件监听
				 * 当横向滚动时，同时固定表头内容
				 * 当纵向滚动时，同时滚动固定列的内容
				 */
				$(dataTableBodyContainer).scroll(function() {
					var fixheader = $(this).parent(".datagrid-fixed-container").find(".datagrid-fixed-container-header").find(".datagrid-fixed-container-header-table");
					fixheader.css({
						"position": "relative",
						"right": $(this).scrollLeft(),
						"top": 0
					});
					var frozen = $(this).parent(".datagrid-fixed-container").prev(".datagrid-frozen-container").find(".datagrid-frozen-container-body").find(".datagrid-frozen-container-body-table");
					frozen.css({
						"position": "relative",
						"left": 0,
						"bottom": $(this).scrollTop()
					});
				});
				fixedGridDiv.appendChild(dataTableBodyContainer);
				
				
				
				/*
				 * 如果宽度单位是%号，则将自定义的宽度换算成百分比，重新设置
				 */
				if(sizeUnit == "%") {
					var width = $(container).width();
					$(frozenGridDiv).css({
						"width": parseFloat(frozenTableWidth)/100*width
					});
					$(frozenColumnBodyTable).css({
						"width": "100%"
					});
					$(frozenColumnHeaderTable).css({
						"width": "100%"
					});
					$(dataTableBody).css({
						"width": "100%"
					});
					$(dataTableHeader).css({
						"width": "100%",
					});
					if(dataTableBodyContainer.scrollHeight > dataTableBodyContainer.offsetHeight
							&& navigator.userAgent.indexOf("Android") == -1 && navigator.userAgent.indexOf("iPhone")==-1) {
						$(dataTableHeaderContainer).css("padding-right", "17px");
					} else {
						$(dataTableHeaderContainer).css("padding-right", "0px");
					}
				}
				var width = $(container).width() - $(frozenGridDiv).width()-5;
				$(fixedGridDiv).width(width);
				var trs = $(frozenColumnBodyTable).find("tr");
				var fixedTrs = $(dataTableBody).find("tr");
				if(fixedTrs && fixedTrs.length > 0) {
					for(var i in fixedTrs) {
						var tr = fixedTrs[i];
						var height = $(tr).height();
						var maxHeight = 0;
						if(maxHeight < height) {
							maxHeight = height;
						}
						var frozenTr = trs[i];
						height = $(frozenTr).height();
						
						if(maxHeight < height) {
							maxHeight = height;
						}
						$(tr).height(maxHeight);
						$(frozenTr).height(maxHeight);
					}
				}
				return $this;
			},
			init: function() {
				if($this.opts.url && ""!=$.trim($this.opts.url)) {
					$this.getPageData(1);
				} else {
					$this.initUI($this, $this.opts)
				}
				return $this;
			},
			reload: function() {
				return $this.init();
			},
			
			initPageTool: function() {
				var url = $this.opts.url;
				var totalRecordKey = $this.opts.reader.totalRecord;
				var totalRecord = 0;
				try {
					if($this.opts[totalRecordKey]) {
						totalRecord = parseInt($this.opts[totalRecordKey]);
					}
				} catch(ex) {
					totalRecord = 0;
				}
				
				var page = 1;
				try {
					page = parseInt($this.opts.currentPage);
				} catch(ex) {
					page = 1;
				}
				var pageSize = $this.opts.pageSize;
				//得到总页数
				var totalPage = totalRecord%pageSize == 0 ? parseInt(totalRecord/pageSize) : (parseInt(totalRecord/pageSize)+1);
				$this.opts.totalPage = totalPage;
				var pager = "";
				var topDiv = document.createElement("div");
				$(topDiv).addClass("table-responsive no-padding");
				var div = document.createElement("div");
				$(div).addClass("box-footer clearfix");
				var span = document.createElement("span");
				$(span).append("每页&nbsp;");
				var select = document.createElement("select");
				
				$(select).addClass("form-control");
				$(select).css({
					"width": "50px",
					"height": "25px",
					"display": "inline",
					"padding": "4px"
				});
				var option = document.createElement("option");
				option.innerHTML = 5;
				if(pageSize == 5) {
					option.selected = true;
				}
				select.appendChild(option);
				
				var option1 = document.createElement("option");
				option1.innerHTML = 10;
				if(pageSize == 10) {
					option1.selected = true;
				}
				select.appendChild(option1);
				var option2 = document.createElement("option");
				option2.innerHTML = 15;
				if(pageSize == 15) {
					option2.selected = true;
				}
				select.appendChild(option2);
				var option3 = document.createElement("option");
				option3.innerHTML = 20;
				if(pageSize == 20) {
					option3.selected = true;
				}
				select.appendChild(option3);
				var option4 = document.createElement("option");
				option4.innerHTML = 25;
				if(pageSize == 25) {
					option4.selected = true;
				}
				select.appendChild(option4);
				var option5 = document.createElement("option");
				option5.innerHTML = 30;
				if(pageSize == 30) {
					option5.selected = true;
				}
				select.appendChild(option5);
				var option6 = document.createElement("option");
				option6.innerHTML = 35;
				if(pageSize == 35) {
					option6.selected = true;
				}
				select.appendChild(option6);
				span.appendChild(select);
				select.addEventListener("change", function() {
					var val = $(this).val();
					$this.getPageData(1, val);
				});
				$(span).append("&nbsp;行&nbsp;&nbsp;&nbsp;共&nbsp;"+totalPage+"&nbsp;页");
				div.appendChild(span);
				topDiv.appendChild(div);
				var ul = document.createElement("ul");
				$(ul).addClass("no-margin pull-right");
				$(ul).css("padding-left", "10px");
				$(ul).append("跳转至");
				var input = document.createElement("input");
				$(input).addClass("form-control");
				$(input).attr("style", "width: 30px; height: 25px; padding: 4px; display: inline; margin-left: 5px; margin-right: 5px;");
				ul.appendChild(input);
				button = document.createElement("button");
				$(button).addClass("btn btn-info btn-flat");
				$(button).attr("style", "padding: 3px;");
				button.onclick = function() {
					var gotoPage = $(input).val();
					if(gotoPage && ""!=$.trim(gotoPage)) {
						if(gotoPage>totalPage) {
							gotoPage = totalPage;
						} else if(gotoPage <= 0) {
							gotoPage = 1;
						}
						$this.getPageData(gotoPage);
					}
				}
				button.innerHTML = "Go";
				ul.appendChild(button);
				div.appendChild(ul);
				
				var pageToolUl = document.createElement("ul");
				$(pageToolUl).addClass("pagination pagination-sm no-margin pull-right");
				if(page == 1) {
					var li = document.createElement("li");
					var a = document.createElement("a");
					a.innerHTML = "首页";
					$(a).attr("style", "background-color: #fff;color: #afafaf;cursor:not-allowed;");
					$(a).attr("href", "javascript: void(0)");
					li.appendChild(a);
					pageToolUl.appendChild(li);
				} else {
					var li = document.createElement("li");
					var a = document.createElement("a");
					a.innerHTML = "首页";
					$(a).attr("href", "javascript: void(0)");
					a.onclick = function() {
						$this.getPageData(1);
					}
					li.appendChild(a);
					pageToolUl.appendChild(li);
				}
				
				if(page == 1) {
					var li = document.createElement("li");
					var a = document.createElement("a");
					a.innerHTML = "上一页";
					$(a).attr("style", "background-color: #fff;color: #afafaf;cursor:not-allowed;");
					$(a).attr("href", "javascript: void(0)");
					li.appendChild(a);
					pageToolUl.appendChild(li);
				} else {
					var pre = page - 1;
					var li = document.createElement("li");
					var a = document.createElement("a");
					a.innerHTML = "上一页";
					$(a).attr("href", "javascript: void(0)");
					a.onclick = function() {
						$this.getPageData(pre);
					}
					li.appendChild(a);
					pageToolUl.appendChild(li);
				}
				
				var i = 1;
				var end = 5;
				if(totalPage <= 5) {
					i = 1;
					end = totalPage;
				} else if(page > 3) {
					i = page - 2;
					end = i + 4;
					if(end >= totalPage) {
						end = totalPage;
						i = end - 5;
						if(i <= 0) {
							i = 1;
						}
					}
				}
				
				for(; i <= end; i ++) {
					if(i == page) {
						var li = document.createElement("li");
						var a = document.createElement("a");
						a.innerHTML = i;
						$(a).attr("style", "background-color: #fff;color: #afafaf;cursor:not-allowed;");
						$(a).attr("href", "javascript: void(0)");
						li.appendChild(a);
						pageToolUl.appendChild(li);
					} else {
						var li = document.createElement("li");
						var a = document.createElement("a");
						a.innerHTML = i;
						$(a).attr("href", "javascript: void(0)");
						a.onclick = function() {
							var page = $(this).html();
							$this.getPageData(page);
						}
						li.appendChild(a);
						pageToolUl.appendChild(li);
					}
				}
				if(page == totalPage) {
					var li = document.createElement("li");
					var a = document.createElement("a");
					a.innerHTML = "下一页";
					$(a).attr("style", "background-color: #fff;color: #afafaf;cursor:not-allowed;");
					$(a).attr("href", "javascript: void(0)");
					li.appendChild(a);
					pageToolUl.appendChild(li);
		        } else {
		        	var next = page + 1;
		        	var li = document.createElement("li");
					var a = document.createElement("a");
					a.innerHTML = "下一页";
					$(a).attr("href", "javascript: void(0)");
					a.onclick = function() {
						$this.getPageData(next);
					}
					li.appendChild(a);
					pageToolUl.appendChild(li);
		        }
		        if(page == totalPage) {
		        	var li = document.createElement("li");
					var a = document.createElement("a");
					a.innerHTML = "尾页";
					$(a).attr("style", "background-color: #fff;color: #afafaf;cursor:not-allowed;");
					$(a).attr("href", "javascript: void(0)");
					li.appendChild(a);
					pageToolUl.appendChild(li);
		        } else {
		        	var li = document.createElement("li");
					var a = document.createElement("a");
					a.innerHTML = "尾页";
					$(a).attr("href", "javascript: void(0)");
					a.onclick = function() {
						$this.getPageData(totalPage);
					}
					li.appendChild(a);
					pageToolUl.appendChild(li);
		        }
				div.appendChild(pageToolUl);
		        var pager = document.createElement("div");
		        pager.appendChild(topDiv);
		        return pager;
			},
			
			refresh: function () {
				$this.initUI($this, $this.opts);
			},
			getData: function () {
				return $this.opts.datas;
			},
			setData: function (datas) {
				$this.opts.data = datas;
			},
			addData: function(data) {
			},
			addDatas: function (datas) {
				if(datas) {
				}
			},
			getDataByDataIndex: function(data, dataIndex) {
				var text = "";
				try {
					var dataIndexArr = dataIndex.split(".");
					if(dataIndexArr && dataIndexArr.length > 0) {
						for(var i in dataIndexArr) {
							var index = dataIndexArr[i];
							data = data[index];
						}
						text = data;
					}
				} catch(ex) {
					
				}
				return text;
			},
			/**
			 * 设置数据总数
			 */
			setTotalRecord: function(totalRecord) {
				$this.opts.totalRecord = totalRecord;
			},
			getPageData: function(page, pageSize) {
				var url=$this.opts.url;
				$this.opts.currentPage = page;
				if(!pageSize || "" == $.trim(pageSize)) {
					pageSize = $this.opts.extraParams.pageSize;
				}
				$this.opts.pageSize = pageSize;
				$this.opts.extraParams.currentPage = $this.opts.currentPage;
				$this.opts.extraParams.pageSize = $this.opts.pageSize;
				if(url!="") {
					$this.requestData(url, $this.opts.extraParams);
				}
			},
			
			setExtraParams: function(extraParams) {
				if(extraParams) {
					$.extend($this.opts.extraParams, extraParams);
				}
			},
			
			requestData: function(url, params) {
				var msg = $this.opts.reader.message;
				
				//获得解析显示数据的key
				var dataKey = $this.opts.reader.root;
				
				//获得解析数据总数的key
				var totalRecordKey = $this.opts.reader.totalRecord;
				$.ajax({
					url: url,
					data: params,
					type: "POST",
					success: function(data) {
						var json = eval("("+data+")");
						if(json.success) {
							if(true == json.success) {
								var data = json.root;
								if(data) {
									var totalRecord = data[totalRecordKey];
									var list = data[dataKey];
									$this.setTotalRecord(totalRecord);
									$this.setData(list);
									$this.refresh();
								}
							} else {
								alert(json[msg]);
							}
						} else {
							var data = json[dataKey];
							if(data) {
								var totalRecord = json[totalRecordKey];
								$this.setTotalRecord(totalRecord);
								$this.setData(data);
								$this.refresh();
							}
						}
						
					},
					error: function() {
						alert("系统异常，请联系管理员！");
					}
				});
			}
		});
		return $this.init($this, $this.opts);
	}
})(jQuery);