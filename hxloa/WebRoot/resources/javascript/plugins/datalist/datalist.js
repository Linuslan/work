(function($) {
	$.fn.datalist = function(options) {
		var $this;
		var opts;
		$this = $(this);
		$this.opts = {
			displayField: "name",
			valueField: "id",
			singleSelect: true,
			leftBox: {
				title: "待选",
				type: "left",
				url: "",
				data: [],
				reader: {
					root: "data",
					message: "msg"
				},
				buttons: [{
					tooltip: "选择",
					icon: "fa-angle-right",
					onRowClick: function() {
						var items = $this.find("div.leftBox").find("div.box-body").find("div.selected");
						var rightBox = $this.find("div.rightBox").find("div.box-body");
						$this.buttonClick(items, rightBox);
					}
				}, {
					tooltip: "全选",
					icon: "fa-angle-double-right",
					onRowClick: function() {
						var items = $this.find("div.leftBox").find("div.box-body").find("div");
						var rightBox = $this.find("div.rightBox").find("div.box-body");
						$this.buttonClick(items, rightBox);
					}
				}]
			},
			rightBox: {
				title: "已选",
				type: "right",
				url: "",
				data: [],
				reader: {
					root: "data",
					message: "msg"
				},
				buttons: [{
					tooltip: "移除",
					icon: "fa-angle-left",
					onRowClick: function(box) {
						var items = $this.find("div.rightBox").find("div.box-body").find("div.selected");
						var leftBox = $this.find("div.leftBox").find("div.box-body");
						$this.buttonClick(items, leftBox);
					}
				}, {
					tooltip: "移除所有",
					icon: "fa-angle-double-left",
					onRowClick: function(box) {
						var items = $this.find("div.rightBox").find("div.box-body").find("div");
						var leftBox = $this.find("div.leftBox").find("div.box-body");
						$this.buttonClick(items, leftBox);
					}
				}]
			}
		};
		if(!options.leftBox) {
			options.leftBox = $this.opts.leftBox;
		}
		if(!options.leftBox || !options.leftBox.reader) {
			options.leftBox.reader = $this.opts.leftBox.reader;
		}
		
		if(!options.leftBox || !options.leftBox.buttons) {
			options.leftBox.buttons = $this.opts.leftBox.buttons;
		}
		
		if(!options.rightBox) {
			options.rightBox = $this.opts.rightBox;
		}
		if(!options.rightBox || !options.rightBox.reader) {
			options.rightBox.reader = $this.opts.rightBox.reader;
		}
		
		if(!options.rightBox || !options.rightBox.buttons) {
			options.rightBox.buttons = $this.opts.rightBox.buttons;
		}
		//互相设置值之后，在$.extend($this.opts, options);中，最后得到的值才是完整的
		$.extend($this.opts.leftBox, options.leftBox);
		$.extend(options.leftBox, $this.opts.leftBox);
		$.extend($this.opts.rightBox, options.rightBox);
		$.extend(options.rightBox, $this.opts.rightBox);
		$.extend($this.opts, options);
		//存放左侧被选中项的对象，以obj.id=text的形式存放
		$this.leftSelection = new Object();
		//存放右侧被选中项的对象，以obj.id=text的形式存放
		$this.rightSelection = new Object();
		$.extend($this, {
			/**
			 * 初始化数据
			 */
			initData: function(items, boxOpts) {
				if(items) {
					var itemsArr = items.split(",");
					if(itemsArr && 0 < itemsArr.length) {
						for(var i = 0; i < itemsArr.length; i ++) {
							var item = itemsArr[i];
							var itemArr = item.split("=");
							if(itemArr && 2 == itemArr.length) {
								var id = itemArr[0];
								var text = itemArr[1];
								if(id && ""!=$.trim(id)
										&& text && ""!=$.trim(text)) {
									var obj = {
										
									}
									obj[$this.opts.valueField]=id;
									obj[$this.opts.displayField]=text;
									boxOpts.data.push(obj);
								}
							}
						}
					}
				}
			},
			init: function() {
				$this.addClass("row datalist");
				$this.initLeftBox();
				$this.initRightBox();
				return $this;
			},
			
			/**
			 * 初始化左边的待选框
			 */
			initLeftBox: function() {
				
				var boxBody = $this.initBox("leftBox");
				var boxOpts = $this.opts.leftBox;
				var leftItems = $this.attr("leftItems");
				//如果有初始化的值，则设置
				$this.initData(leftItems, boxOpts);
				$this.load(boxOpts, boxBody);
			},
			
			/**
			 * 初始化右边的已选框
			 */
			initRightBox: function() {
				var boxBody = $this.initBox("rightBox");
				var boxOpts = $this.opts.rightBox;
				var rightItems = $this.attr("rightItems");
				$this.initData(rightItems, boxOpts);
				$this.load(boxOpts, boxBody);
			},
			
			/**
			 * 初始化待选框，通过传入的参数boxType来确定初始化右边已选框还是左边的待选框
			 */
			initBox: function(boxType) {
				var boxOpts = $this.opts[boxType];
				var div = document.createElement("div");
				$this.append(div);
				$(div).addClass("col-md-6 col-sm-12 "+boxType);
				
				var box = document.createElement("div");
				div.appendChild(box);
				$(box).addClass("box box-primary box-solid");
				
				//存放选择项值的input
				var input = document.createElement("input");
				box.appendChild(input);
				$(input).attr("type", "hidden");
				//有name，则赋予input的属性name
				if(boxOpts.name && "" != $.trim(boxOpts.name)) {
					$(input).attr("name", boxOpts.name);
				}
				
				var header = document.createElement("div");
				box.appendChild(header);
				$(header).addClass("box-header with-border");
				var title = document.createElement("h3");
				header.appendChild(title);
				$(title).addClass("box-title");
				//设置左侧待选框的标题
				$(title).html(boxOpts.title);
				var boxTools = document.createElement("div");
				header.appendChild(boxTools);
				$(boxTools).addClass("box-tools pull-right");
				/*
				 * 添加boxTools的按钮
				 */
				if(boxOpts.buttons && 0 < boxOpts.buttons.length) {
					var buttons = boxOpts.buttons;
					for(var i = 0; i < buttons.length; i ++) {
						var btn = buttons[i];
						var button = document.createElement("button");
						boxTools.appendChild(button);
						$(button).attr("type", "button");
						$(button).addClass("btn btn-default btn-flat");
						if(btn.tooltip && ""!=$.trim(btn.tooltip)) {
							$(button).attr("title", btn.tooltip);
							$(button).attr("data-toggle", "tooltip");
						}
						var iEl = document.createElement("i");
						button.appendChild(iEl);
						$(iEl).addClass("fa");
						//添加按钮类型
						if(btn.icon && ""!=$.trim(btn.icon)) {
							$(iEl).addClass(btn.icon);
						}
						//添加事件
						
						/*button.onclick = function() {
							if(btn.onRowClick) {
								
							}
						}*/
						button.onclick = btn.onRowClick;
					}
				}
				/*
				 * 开始添加box-body
				 */
				var body = document.createElement("div");
				box.appendChild(body);
				$(body).addClass("box-body");
				$(body).css({
					"height": "200px",
					"overflow-y": "auto"
				});
				return body;
			},
			/**
			 * 初始化选择框或者待选框的数据
			 */
			initBody: function(boxOpts, boxBody) {
				var dataSet = boxOpts.data;
				var textKey = $this.opts.displayField;
				var idKey = $this.opts.valueField;
				if(dataSet && 0 < dataSet.length) {
					for(var i = 0; i < dataSet.length; i ++) {
						var data = dataSet[i];
						var id = data[idKey];
						var text = data[textKey];
						$this.createItem(id, text, boxBody);
					}
				}
				//触发左右选择框同步事件，将两边相同的选项移除
				$this.syncRightBox();
				$this.syncLeftBox();
			},
			createItem: function(id, text, boxBody) {
				if(id && ""!=$.trim(id)
						&& text && ""!=$.trim(text)) {
					var div = document.createElement("div");
					boxBody.appendChild(div);
					$(div).css({
						"height": "30px",
						"line-height": "30px",
						"padding-left": "5px"
					});
					$(div).html(text);
					$(div).attr("value", id);
					$(div).click(function() {
						if($this.opts.singleSelect) {
							$(boxBody).find("div").removeClass("selected");
							$(this).addClass("selected");
						} else {
							$(this).toggleClass("selected");
						}
					});
				}
			},
			refresh: function() {
				
			},
			reload: function() {
				
			},
			load: function(boxOpts, boxBody) {
				var url = boxOpts.url;
				//如果有url，则从远程进行加载，否则直接从boxOpts.data中加载数据
				if(url && ""!=$.trim(url)) {
					//刷新遮罩层
					var overlay = document.createElement("div");
					$(overlay).addClass("overlay");
					var iEl = document.createElement("i");
					overlay.appendChild(iEl);
					$(iEl).addClass("fa fa-refresh fa-spin");
					$.ajax({
						url: url,
						data: {
							
						},
						type: "POST",
						beforeSend: function() {
							//添加遮罩层
							$(boxBody).parent().append(overlay);
						},
						complete: function() {
							//移除遮罩层
							overlay.remove();
						},
						success: function(data) {
							var json = eval("("+data+")");
							if(json.success == true) {
								if(boxOpts) {
									var dataKey = boxOpts.reader.root;
									boxOpts.data = json[dataKey];
									$this.initBody(boxOpts, boxBody);
								}
							} else {
								alert("加载数据异常！");
							}
						},
						error: function() {
							alert("系统异常，请联系管理员！");
						}
					});
				} else {
					$this.initBody(boxOpts, boxBody);
				}
			},
			syncLeftBox: function() {
				var leftItems = $this.find("div.leftBox").find("div.box-body").find("div");
				var $rightBoxBody = $this.find("div.rightBox").find("div.box-body");
				if(leftItems && 0 < leftItems.length) {
					for(var i = 0; i < leftItems.length; i ++) {
						var $item = $(leftItems[i]);
						var val = $item.attr("value");
						if(val && ""!=$.trim(val)) {
							$rightBoxBody.find("div[value="+val+"]").remove();
						}
					}
				}
				$this.setValues($rightBoxBody);
			},
			syncRightBox: function() {
				var rightItems = $this.find("div.rightBox").find("div.box-body").find("div");
				var $leftBoxBody = $this.find("div.leftBox").find("div.box-body");
				if(rightItems && 0 < rightItems.length) {
					for(var i = 0; i < rightItems.length; i ++) {
						var $item = $(rightItems[i]);
						var val = $item.attr("value");
						if(val && ""!=$.trim(val)) {
							$leftBoxBody.find("div[value="+val+"]").remove();
						}
					}
				}
				$this.setValues($leftBoxBody);
			},
			setValues: function(boxBody) {
				var $input = boxBody.parent().find("input:hidden");
				var idArr = new Array();
				var items = boxBody.find("div");
				if(items && 0 < items.length) {
					for(var i = 0; i < items.length; i ++) {
						var $item = $(items[i]);
						var val = $item.attr("value");
						if(val && ""!=$.trim(val)) {
							idArr.push($.trim(val));
						}
					}
				}
				$input.val(idArr.join(","));
			},
			buttonClick: function(items, target) {
				if(items && 0 < items.length) {
					for(var i = 0; i < items.length; i ++) {
						var $item = $(items[i]);
						var val = $item.attr("value");
						var text = $item.html();
						if(val && ""!=$.trim(val)) {
							if(0 >= target.find("div[value="+val+"]").length) {
								$this.createItem(val, text, target[0]);
								$item.remove();
							}
						}
					}
				}
				$this.syncRightBox();
				$this.syncLeftBox();
			}
		});
		return $this.init();
	}
})(jQuery);