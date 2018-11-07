function mask(el) {
	//刷新遮罩层
	var overlay = document.createElement("div");
	try {
		$(overlay).addClass("overlay");
		var iEl = document.createElement("i");
		overlay.appendChild(iEl);
		$(iEl).addClass("fa fa-refresh fa-spin");
		el.append(overlay);
		el.find("button").attr("disabled", true);
		el.find("input[type=button]").attr("disabled", true);
	} catch(e) {
		alert(e);
	}
	return overlay;
}

function unmask(el) {
	try {
		el.find(">div.overlay").remove();
		el.find("button").attr("disabled", false);
		el.find("input[type=button]").attr("disabled", false);
	} catch(e) {
		//alert(e);
	}
}

function createBtn(text, btnCls, iconCls, eventMethod) {
	var button = "<button type='button' class='btn "+(btnCls? btnCls : "btn-success btn-xs")+"' data-placement='left' data-toggle='tooltip' title='"+text+"' onclick='"+eventMethod+"'><i class='fa "+iconCls+"'></i></button>&nbsp;&nbsp;";
	//var button = "<button type='button' class='btn "+(btnCls? btnCls : "btn-success btn-xs")+"' title='"+text+"' onclick='"+eventMethod+"'><i class='fa "+iconCls+"'></i></button>&nbsp;&nbsp;";
	return button;
}

function getFlowStatus(status) {
	if(status == "0") {
		return "未提交";
	} else if(status == "1") {
		return "退回";
	} else if(status == "2") {
		return "撤销";
	} else if(status == "3") {
		return "待审核";
	} else if(status == "4") {
		return "完成"
	}
}

/**
 * 是否是整数数字
 * @param param
 * @returns {Boolean}
 */
function isNumber(param) {
	var re = /^[0-9]*$/;
	if(!re.test(param)) {
		return false;
	} else {
		return true;
	}
}

/**
 * 是否是实数
 * @param param
 * @returns {Boolean}
 */
function isDecimal(param) {
	var re = /^[-]?\d+(\.\d+)?$/;
	if(!re.test(param)) {
		return false;
	} else {
		return true;
	}
}

function upload($form, callback) {
	var id = Math.random();
	var $body = $("body").append("<iframe name='"+id+"'>");
	var $iframe = $body.find("iframe[name='"+id+"']");
	
	$form.attr("target", id);
	$iframe.css({
		"display": "none"
	});
	var $iframeDoc = $iframe.contents();
	$iframe.load(function() {
		var response = $iframe.contents().find("body").text();
		callback.call(this, response);
		$iframe.remove();
	});
	$form.submit();
}

/**
 * 该方法解决jqgrid冻结列后会出现错位的问题
 * @param listId
 */
function hackHeight(listId) {
	var tableid = listId.substring(1);
    $(listId + '_frozen tr').slice(1).each(function() {
        var rowId = $(this).attr('id');
        var frozenTdHeight = parseFloat($('td:first', this).height());
        var normalHeight = parseFloat($(listId + ' #' + $(this).attr('id')).find('td:first').height());

        // 如果冻结的列高度小于未冻结列的高度则hack之
        if (frozenTdHeight < normalHeight) {

        	
            $('td', this).each(function() {

                /*
                 * 浏览器差异高度hack
                 */
            	var isChrome = /chrome/.test(navigator.userAgent.toLowerCase());
            	var isIe = /trident/.test(navigator.userAgent.toLowerCase());
            	var isMozilla = /mozilla/.test(navigator.userAgent.toLowerCase());
                var space = 0; // opera默认使用0就可以
                if (isChrome) {
                    space = 3;
                } else if (isIe) {
                    space = -0.2;
                } else if (isMozilla) {
                    space = 0.5;
                }

                //$(this).attr('style', $(this).attr('style') + ";height:" + (normalHeight + space) + "px !important");
                if (!$(this).attr('style') || $(this).attr('style').indexOf('height:') == -1) {
                    //$(this).attr('style', $(this).attr('style') + "height:" + (normalHeight + space) + "px");
                	$(this).height(normalHeight);
                }
            });
        }
    });
    $("#gbox_"+tableid+" .frozen-div tr").each(function() {
    	var $this = $(this);
    	$this.find("th").each(function() {
    		var id = $(this).attr("id");
    		if(id) {
    			var height = $("#"+id).height();
    			if(height>$(this).height()) {
    				$(this).height(height);
    			}
    		}
    	});
    });
}

function refreshIndex() {
	$.ajax({
		url: getRoot() + "sys/menuIndex/queryIndex.action",
		type: "POST",
		success: function(data) {
			if(data && "" != $.trim(data)) {
				var json = eval("("+data+")");
				if(json.success == true) {
					var html = "";
					var datas = eval("("+json.msg+")");
					var total = datas.length;
					var size = 6;
					var css = ["bg-aqua", "bg-green", "bg-yellow", "bg-red", "bg-light-blue", "bg-teal"];
					//var css = ["bg-green", "bg-light-blue", "bg-light-blue", "bg-light-blue", "bg-light-blue", "bg-light-blue"];
					for(var j = 0; j < datas.length; ) {
						var menus = new Array();
						for(var k = 0; k < size && datas[j]; k ++) {
							menus[k]=datas[j];
							j++;
						}
						html = html + "<div class='row'>";
						for(var i = 0; i < menus.length; i ++) {
							var menu = menus[i];
							html = html + 
							"<div class=\"col-lg-2 col-xs-6\">"+
				              "<!-- small box -->"+
				              "<div class=\"small-box "+css[i]+"\">"+
				                "<div class=\"inner\" style=''>"+
				                  "<font style='font-size: 20px'>"+menu.TEXT+"</font>"+
				                  "<p>&nbsp;&nbsp;</p>"+
				                "</div>"+
				                "<div class=\"icon\" style=''>"+
				                  "<i class=\"ion "+menu.INDEX_ICON+"\"></i>"+
				                "</div>";
							var isAppend = false;
							if(menu.children && 0 < menu.children.length) {
								for(var m = 0; m < menu.children.length; m ++) {
									var child = menu.children[m];
									if(child.TEXT && child.VALUE && child.URL && child.INDEX_NAME && child.TOTAL > 0) {
										html = html + "<a href=\"javascript: addTab('"+child.TEXT+"', '"+child.VALUE+"', '"+getRoot() + child.URL+"', '"+menu.XTYPE+"')\" class=\"small-box-footer\" style='font-size: 13px; color: red'>"+child.INDEX_NAME+"："+child.TOTAL+" <i class=\"fa fa-arrow-circle-right\"></i></a>";
										isAppend = true;
									}
								}
							}
							if(isAppend == false) {
								html = html + "<a href=\"javascript: void(0);\" class=\"small-box-footer\">&nbsp;&nbsp;</a>";
							}
				            html = html + "</div>";
				            html = html + "</div>";
						}
						html = html + "</div>";
					}
					$("#home2").html(html);
				} else {
					BootstrapDialog.danger(json.msg);
				}
			} else {
				BootstrapDialog.danger(json.msg);
			}
		}
	});
}

function refreshIndex2() {
	$.ajax({
		url: getRoot() + "sys/menuIndex/queryIndex.action",
		type: "POST",
		success: function(data) {
			if(data && "" != $.trim(data)) {
				var json = eval("("+data+")");
				if(json.success == true) {
					var html = "";
					var datas = eval("("+json.msg+")");
					var total = datas.length;
					var size = 6;
					var css = ["bg-aqua", "bg-green", "bg-yellow", "bg-red", "bg-light-blue", "bg-teal"];
					//var css = ["bg-green", "bg-light-blue", "bg-light-blue", "bg-light-blue", "bg-light-blue", "bg-light-blue"];
					for(var j = 0; j < datas.length; ) {
						var menus = new Array();
						for(var k = 0; k < size && datas[j]; k ++) {
							menus[k]=datas[j];
							j++;
						}
						html = html + "<div class='row' style='margin-top: 20px; margin-bottom: 50px;'>";
						for(var i = 0; i < menus.length; i ++) {
							var menu = menus[i];
							html = html + "<div id='"+menu.XTYPE+"_msg_container' class=\"mainQuickNaviDiv col-lg-2 col-md-4 col-xs-6\" style='text-align: center; border: 0px;' >";
							//html = html + "<div id='"+menu.XTYPE+"_msg_container' class='mainQuickNaviDiv' tabIndex=1 style='margin-left: 8%; height: 120px; width: 90px; float: left; text-align: center; padding-top: 10px;'>";
							html = html + "<span>"+menu.TEXT+"</span>";
							html = html + "<div onclick=\"showSubMenus(this, '"+menu.XTYPE+"')\" class='quickMenuList' objName='"+menu.XTYPE+"' style='margin: 0 auto; width: 130px; height: 130px; background: url("+getRoot()+"resources/css/oaimg/"+menu.XTYPE+".png) no-repeat; background-position: center; cursor: pointer;'>";
							html = html + "</div>";
							var isAppend = false;
							if(menu.children && 0 < menu.children.length) {
								for(var m = 0; m < menu.children.length; m ++) {
									var child = menu.children[m];
									if(child.TEXT && child.VALUE && child.URL && child.INDEX_NAME && child.TOTAL > 0) {
										html = html + "<div style='margin-bottom: 7px;'><a href=\"javascript: addTab('"+child.TEXT+"', '"+child.VALUE+"', '"+getRoot() + child.URL+"', '"+menu.XTYPE+"')\" class=\"small-box-footer\" style='font-size: 13px; color: red'>"+child.INDEX_NAME+"："+child.TOTAL+" <i class=\"fa fa-arrow-circle-right\"></i></a></div>";
										isAppend = true;
									}
								}
							}
							if(menu.allChildren && 0 <menu.allChildren.length) {
								var subMenuHtml = "<div class='quickMenuNavigateList "+menu.XTYPE+"' style='display: none; background-color: #ECF5FF; z-index:9999; filter:alpha(opacity=100);'>";
								for(var m = 0; m < menu.allChildren.length; m ++) {
									var child = menu.allChildren[m];
									var divStr = "<div onclick=\"addTab('"+child.text+"', '"+child.value+"', '"+getRoot() + child.url+"', '"+menu.xtype+"')\" class='aTagWrap' style='width: 98%; height: 25px; border-left: 1px solid #90d7ec; border-top: 1px solid #90d7ec; border-right: 1px solid #90d7ec; text-align: center; line-height: 25px; vertical-align: middle; cursor: pointer; font-size: 10px;'>"+child.text+"</div>";
				  					subMenuHtml = subMenuHtml + divStr;
								}
								subMenuHtml = subMenuHtml + "</div>";
								$("#menuListDiv").append(subMenuHtml);
							}
							html = html + "<div style='display:none;' id='"+menu.TEXT+"_naviList'></div>";
							html = html + "</div>";
						}
						html = html + "</div>";
					}
					$("#home2").html(html);
				} else {
					BootstrapDialog.danger(json.msg);
				}
			} else {
				BootstrapDialog.danger(json.msg);
			}
		}
	});
}

function getOrderNo(id) {
	var rowDatas = $("#"+id).getRowData();
	var rowData;
	var maxNo = 0;
	for(var i = 0; i < rowDatas.length; i ++) {
		rowData = rowDatas[i];
		var orderNo = rowData.orderNo;
		if(parseInt(orderNo) > parseInt(maxNo)) {
			maxNo = orderNo;
		}
	}
	return parseInt(maxNo) + 1;
}

/**
 * 打印相关
 * @returns {String}
 */
function getLinkCss() {
	var links = "<link rel='stylesheet' href='"+getRoot() +"resources/bootstrap/bootstrap/css/bootstrap.min.css'>";
    links = links + "<link rel='stylesheet' href='"+getRoot() +"resources/Font-Awesome-master/css/font-awesome.min.css'>";
    links = links + "<link rel='stylesheet' href='"+getRoot() +"resources/ionicons-master/css/ionicons.min.css'>";
    links = links + "<link rel='stylesheet' href='"+getRoot() +"resources/bootstrap/dist/css/AdminLTE.css'>";
    links = links + "<link rel='stylesheet' type='text/css' href='"+getRoot()+"resources/css/print1.css'>";
    links = links + "<link rel='stylesheet' type='text/css' href='"+getRoot()+"resources/css/mybooststrap.css'>";
    return links;
}

function getPrintCss() {
	var links = "<link rel='stylesheet' type='text/css' href='"+getRoot()+"resources/css/print1.css'>";
    return links;
}

function startComet4j() {
	JS.Engine.start(getRoot() + "conn");
	JS.Engine.on({
		//频道text1
		unRead: function(msg) {
			var content = "";
			if(msg) {
				var json = eval("("+msg+")");
				if(json && 0 < json.length) {
					var count = 0;
					for(var i = 0 ; i < json.length; i ++) {
						var msg = json[i];
						//var msgJson = eval("("+msg")");
						var memo = msg.MEMO.replace(/\\/g, "");
						$("#comet4j").find("#"+msg.VALUE).html(memo);
						if(parseInt(msg.COUNT) > 0 || parseInt(msg.EXPIRED_COUNT) > 0
								|| (msg.ANNUAL_COUNT && parseInt(msg.ANNUAL_COUNT) > 0)
								|| (msg.ANNUAL_EXPIRED_COUNT && parseInt(msg.ANNUAL_EXPIRED_COUNT) > 0)) {
							$("#comet4j").find("#"+msg.VALUE).attr("count", 1);
							content = content + "<div>"+memo+"</div>"
						} else {
							$("#comet4j").find("#"+msg.VALUE).attr("count", 0);
						}
					}
				}
				
				var $lis = $("#comet4j").find("li");
				var $li;
				var total = 0;
				for(var i = 0; i < $lis.length; i ++) {
					$li = $($lis[i]);
					var count = $li.attr("count");
					if(count) {
						try {
							count = parseInt(count);
							count = count ? count : 0;
						} catch(ex) {
							count = 0;
						}
					} else {
						count = 0;
					}
					total = total + count;
				}
				if(total > 0) {
					$("#msg_count").html(total);
				} else {
					$("#msg_count").html("");
				}
			}
			showMsg(content);
		},
		
		unReadNotice: function(data) {
			if(data && data.length > 0) {
				var json = eval("("+data+")");
				var index = 0;
				showNotice(json, index);
			}
			
		},
		
		//开始事件
		start: function(cId, channelList, engine) {
			console.log(cId);
			$.ajax({
				url: getRoot() + "receiveConnectId",
				data: {
					"connectId": cId
				},
				type: "POST",
				success: function(data) {
					
				}
			});
		},
		
		//结束事件
		stop: function(cause, cId, url, engine) {
			//alert('连接已断开，连接ID为：' + cId + ',断开原因：' + cause + ',断开的连接地址：'+ url);
		}
	});
}

function showMsg(content) {
	
	if(content && 0 < content.length) {
		$("#showMsgEl").find("span.info-box-text").html(content);
		$("#showMsgEl").addClass("panel-open");
		setTimeout("hideMsg()", 5000);
		$("#showMsg").hide();
		$("#showMsgEl").find("span.info-box-icon").click(function() {
			$("#showMsgEl").removeClass("panel-open");
			$("#showMsg").show();
		});
	}
}

function hideMsg() {
	//$("#showMsgEl").find("span.info-box-text").html("");
	$("#showMsgEl").removeClass("panel-open");
	$("#showMsg").show();
}

function showNotice(json, index) {
	if(!index) {
		index = 0;
	}
	if(json && json.length > 0) {
		var notice = json[index];
		var width = getA4Width();
		if(notice.TB_ID
				&& "sys_notice" == $.trim(notice.TB_NAME)) {
			BootstrapDialog.show({
			    title: "公告",
			    width: width,
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "sys/notice/queryById.action?returnType=view&notice.id="+notice.TB_ID),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	
			    },
			    onhidden: function(dialogRef) {
			    	if((index + 1) <= json.length) {
			    		index ++;
			    		showNotice(json, index);
			    	}
			    },
			    buttons: [{
			    	label: "关闭",
			    	icon: "fa fa-fw fa-close",
			    	cssClass: "btn-danger",
			    	action: function(dialog) {
			    		dialog.close();
			    	}
			    }]
	        });
		} else {
			if((index + 1) <= json.length) {
	    		index ++;
	    		showNotice(json, index);
	    	}
		}
	}
}

function getA4Width() {
	var width = $(window).width();
	width = width > 850 ? "800px" : "90%";
	return width;
}

function showSubMenus(obj, xtype) {
	if(xtype) {
		$("#menuListDiv div.quickMenuNavigateList").not("."+xtype).slideUp();
		$("#menuListDiv ."+xtype).slideDown();
		var $this = $(".quickMenuList[objName="+xtype+"]");
		var $offset=$this.offset();
		var left = $offset.left;
		var top = $offset.top;
		var height = $this.height();
		var x = left;
		var y = top + height;
		x = left-(($("#menuListDiv ."+xtype).width()-$(obj).width())/2);
		$("#menuListDiv ."+xtype).css({
			"position":"absolute",
			"left": x+"px",
			"top": y+"px",
			"width": "130px",
			"border-bottom": "1px solid #90d7ec"
		});
		
		//阻止后续事件，否则又会执行以下事件，从而把下拉框又拉上去
		/*$("body").not(".quickMenuList").bind("click", function() {
			$("#menuListDiv div.quickMenuNavigateList").slideUp();
		});*/
		event.stopPropagation();
	}
}


/**
 * 将输入回车，空格的字符串在存入数据库时替换成换行符等
 * @param str
 * @returns
 */
function replaceTextareaSave(str) {
	if(str) {
		var reg= new RegExp(/\n|\r|(\r\n)/g);
		var reg1= new RegExp(/\s/g);
		
		str=str.replace(reg,"<br/>");
		//str=str.replace(/\n|\r|(\r\n)|(\u0085)|(\u2028)|(\u2029)/g,"<br/>");
		str =str.replace(reg1,"&nbsp;");
	}
	return str;
}

function replaceDataStr(id) {
	var textareaArr = $("#"+id).find("textarea");
		
	for(var i = 0; i < textareaArr.length; i ++) {
		var $textarea = $(textareaArr[i]);
		var text = $textarea.val();
		text = replaceTextareaSave(text);
		//text = encode(text);
		$textarea.val(text);
	}
	var dataStr = $("#"+id).serialize();
	for(var i = 0; i < textareaArr.length; i ++) {
		var $textarea = $(textareaArr[i]);
		var text = $textarea.val();
		text = replaceTextareaShow(text);
		$textarea.val(text);
	}
	return dataStr;
}

function showTextarea(id) {
	var textareaArr = $("#"+id).find("textarea");
	for(var i = 0; i < textareaArr.length; i ++) {
		var $textarea = $(textareaArr[i]);
		var text = $textarea.val();
		text = replaceTextareaShow(text);
		$textarea.val(text);
	}
}

/**
 * 在显示时，将字符串的空格什么的显示为换行
 * @param str
 * @returns
 */
function replaceTextareaShow(str) {
	var reg= new RegExp("<br/>","g");
	var reg2= new RegExp("<br>","g");
	var reg3= new RegExp("<br />","g");
	var reg1= new RegExp("&nbsp;","g");
	var reg4 = new RegExp("%25", "g");
	var reg5 = new RegExp("%26", "g");
	var reg6 = new RegExp("%2B", "g");
	str = str.replace(reg4, "%");
	str = str.replace(reg5, "&");
	str = str.replace(reg6, "+");
	
	str=str.replace(reg,"\r\n");
	str=str.replace(reg2,"\r\n");
	str=str.replace(reg3,"\r\n");
	str =str.replace(reg1," ");
	
	return str;
}

function encode(param) {
	if(param) {
		try {
			param = param.replace(/%/g, "%25");
			param = param.replace(/\&/g, "%26");
			param = param.replace(/\+/g, "%2B");
		} catch(ex) {
			
		}
	}
	return param
}

function decode(param) {
	if(param && ""!=$.trim(param)) {
		//param = param.replace(/%25/g, "%");
		//param = param.replace(/%26/g, "&");
		//param = param.replace(/%2B/g, "+");
		try {
			var reg4 = new RegExp("%25", "g");
			var reg5 = new RegExp("%26", "g");
			var reg6 = new RegExp("%2B", "g");
			param = param.replace(reg4, "%");
			param = param.replace(reg5, "&");
			param = param.replace(reg6, "+");
		} catch(ex) {
			
		}
	}
	return param;
}

function createTextareaBox(value, options) {
	var name = options.name;
	var rows = options.rows;
	if(!rows) {
		rows = 3;
	}
	var width = options.width;
	if(!width) {
		width = "99%";
	}
	if(value && ""!=$.trim(value)) {
		value = replaceTextareaShow(value);
	}
	var textarea = document.createElement("textarea");
	textarea.setAttribute("name", name);
	textarea.setAttribute("class", "form-control");
	textarea.setAttribute("rows", rows);
	textarea.setAttribute("style", "width: "+width);
	textarea.value=value;
	return textarea;
}

function operateTextareaValue(elem, operation, value) {
	if(operation == "get") {
		var value = $(elem).val();
		value = replaceTextareaSave(value);
		return value
	} else {
		var value = replaceTextareaShow(value);
		$(elem).val(value);
	}
}

function getHeight(height) {
	var winH = $(window).height();
	if(parseInt(height) >= parseInt(winH)) {
		return winH;
	} else {
		return height;
	}
}

/**
 * 将form的内容转换成Json格式
 * 目前主要用户查询条件的form转换
 * @param formId
 * @returns
 */
function parsePostData(formId) {
	var postData = "{";
	if($("#"+formId) && 1 <= $("#"+formId).length) {
		var postDataArr = $("#"+formId).serializeArray();
		for(var i = 0; i < postDataArr.length; i ++) {
			if(postDataArr[i].name) {
				postData = postData + "\"" + postDataArr[i].name + "\"" + ":" + "\""+postDataArr[i].value+"\"";
				if(i < postDataArr.length && postDataArr[i + 1] && postDataArr[i + 1].name) {
					postData = postData + ", ";
				}
				//postData = postData + ", ";
			}
		}
		postData = postData;
	}
	postData = postData + "}";
	return $.parseJSON(postData);
}

function showPdf(isShow){
	var state = "";
	if(isShow){
		state = "block";
	}else{
		state = "none";
	}
	var pop = document.getElementById("pop");
	pop.style.display = state;
	var lightbox = document.getElementById("lightbox");
	lightbox.style.display = state;
}
	
function closePDFViewer(){
	showPdf(false);
}

function specialSaleCheckoutAuth() {
	var groupIds = $("#loginUserGroupIds").val();
	var groupIdArr = groupIds.split(",");
	var isAuth = false;
	for(var i = 0; i < groupIdArr.length; i ++) {
		var groupId = groupIdArr[i];
		if(groupId == 96 || groupId == 41 || groupId == 48) {
			isAuth = true;
			break;
		}
	}
	return isAuth;
}

function specialSaleStatementAuth() {
	var groupIds = $("#loginUserGroupIds").val();
	var groupIdArr = groupIds.split(",");
	var isAuth = false;
	for(var i = 0; i < groupIdArr.length; i ++) {
		var groupId = groupIdArr[i];
		if(groupId == 96) {
			isAuth = true;
			break;
		}
	}
	return isAuth;
}
