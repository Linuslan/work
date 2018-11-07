
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
 * 将输入回车，空格的字符串在存入数据库时替换成换行符等
 * @param str
 * @returns
 */
function replaceTextareaSave(str) {
	if(str) {
		var reg= new RegExp("\n|\r|(\r\n)","g");
		var reg1= new RegExp(" ","g");
		
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

function replaceSpace(id) {
	
}

/**
 * 将输入回车，空格的字符串在存入数据库时替换成换行符等
 * @param str
 * @returns
 */
function replacePSave(str) {
	var strArr = str.split("</p>");
	var newStr = "";
	for(var i = 0; i < strArr.length; i++) {
		newStr = newStr + strArr[i]+"</p><br/>";
	}
	/*
	var reg= new RegExp("</p>","g");
	var reg1= new RegExp("<p>","g");
	
	str=str.replace(reg,"<br/>");
	//str=str.replace(/\n|\r|(\r\n)|(\u0085)|(\u2028)|(\u2029)/g,"<br/>");
	str =str.replace(reg1,"");*/
	return newStr;
}

/**
 * 在显示时，将字符串的空格什么的显示为换行
 * @param str
 * @returns
 */
function replaceTextareaShow(str) {
	var reg= new RegExp("<br/>","g");
	var reg1= new RegExp("&nbsp;","g");
	str=str.replace(reg,"\r\n");
	str =str.replace(reg1," ");
	return str;
}

/**
 * 刷新标签页
 */
function refreshPage() {
	//得到当前标签页的地址
	var url = navTab._getTabs().eq(navTab._currentIndex).attr("url");
	navTab.reload(url);
	getWorkflowNotes();
}

/**
 * 判断金额格式是否正确
 * @param money
 * @returns {Boolean}
 */
function checkMoney(money){
	var a=/^[0-9]*(\.[0-9]{1,2})?$/;
	if(!a.test(money)) {
		alertMsg.error("金额格式不正确，请重新输入！");
		return false;
	} else {
		return true;
	}
}

function validStrOrNum(v){
	var r=/^[a-zA-Z0-9]+$/g;
	return r.test(v);
}

/**
 * 设置textarea的高度自适应，但如果超过100的时候，就出现滚动条
 */
$(function() {
	$(document).on("keyup", "textarea", function() {
		var $textarea = $(this);
		if (!$.browser.msie) {
            $textarea.height(0);
        }
		var realHeight = $textarea.get(0).scrollHeight;
		
		var currentHeight = $textarea.height();
		$parent = $textarea.parent();
		var height = 100;
		if($parent.length > 0) {
			height = $parent.height();
		}
		if(realHeight >= height) {
			$textarea.css("overflow-y", "auto");
		} else {
			$textarea.css("overflow-y", "visible");
		}
		if(realHeight < 50) {
			realHeight=50;
		}
		if(realHeight >= height) {
			$textarea.height(realHeight);
		} else {
			if(realHeight > 50) {
				$textarea.height($textarea.get(0).scrollHeight);
			} else {
				$textarea.height(50);
			}
		}
		if(realHeight > $parent.scrollHeight) {
			$parent.scrollHeight=realHeight;
		}
		//alert($textarea.height());
		$textarea.css("overflow-y", "auto");
		/*var $textarea = $(this);
		$textarea.textareaAutoHeight();*/
	});
});
$.fn.extend({
    textareaAutoHeight: function (options) {
        this._options = {
            minHeight: 0,
            maxHeight: 1000
        }

        this.init = function () {
            for (var p in options) {
                this._options[p] = options[p];
            }
            if (this._options.minHeight == 0) {
                this._options.minHeight=parseFloat($(this).height());
            }
            for (var p in this._options) {
                if ($(this).attr(p) == null) {
                    $(this).attr(p, this._options[p]);
                }
            }
            $(this).keyup(this.resetHeight).change(this.resetHeight)
            .focus(this.resetHeight);
        }
        this.resetHeight = function () {
            var _minHeight = parseFloat($(this).attr("minHeight"));
            var _maxHeight = parseFloat($(this).attr("maxHeight"));

            if (!$.browser.msie) {
                $(this).height(0);
            }
            var h = parseFloat(this.scrollHeight);
            h = h < _minHeight ? _minHeight :
                        h > _maxHeight ? _maxHeight : h;
            $(this).height(h).scrollTop(h);
            if (h >= _maxHeight) {
                $(this).css("overflow-y", "scroll");
            }
            else {
                $(this).css("overflow-y", "hidden");
            }
        }
        this.init();
    }
});
function lazySetTXTAutoSize() {
	setTimeout(function() {
		$("textarea").each(function() {
			$textarea = $(this);
			$parent = $textarea.parent();
			var height = 100;
			var realHeight = $textarea.get(0).scrollHeight;
			if($parent.length > 0) {
				height = $parent.height();
			}
			if(realHeight > height) {
				$textarea.css("overflow-y", "auto");
				$textarea.height(realHeight);
			} else {
				if(realHeight > 50) {
					$textarea.height(realHeight);
				} else {
					$textarea.height(50);
				}
			}
		});
	}, "0");
}

function freezeScroll(obj) {
	var startH = $(obj).height();
	var endH = $(obj).scrollTop();
	var diffH = startH - endH;
	var realH = startH-diffH;
	$(".freezeRow").css({"position" : "relative", "top" : realH+"px", "left": "0px"});
	/*
	var startW = $("#w_list_print_capital").width();
	var endW = $("#w_list_print_capital").scrollLeft();
	var diffW = startW - endW;
	var realW = startW-diffW;
	$(".relativeTD").css({"position" : "relative", "top" : "0px", "left": realW + "px"});*/
}

function freezeAchievementScroll(obj) {
	var startH = $(obj).height();
	var endH = $(obj).scrollTop();
	var diffH = startH - endH;
	var realH = startH-diffH;
	$(".freezeRow").css({"position" : "relative", "top" : realH+"px", "left": "0px", "z-index": "2"});
	/*
	var startW = $("#w_list_print_capital").width();
	var endW = $("#w_list_print_capital").scrollLeft();
	var diffW = startW - endW;
	var realW = startW-diffW;
	$(".relativeTD").css({"position" : "relative", "top" : "0px", "left": realW + "px"});*/
}


/**
 * 初始化编辑器，用这个初始化，在谷歌浏览器忠也能用
 * @param keid
 * @param width
 * @param height
 */
function keditWithBar(obj, keid, width, height){ 
	obj =  KindEditor.create(
	'#' + keid,
	{     
	width : width, //编辑器的宽度为70%
	height : height, //编辑器的高度为100px 
	filterMode : false, //不会过滤HTML代码
	resizeMode : 1 ,//编辑器只能调整高度 
	imageUploadJson : '/kindeditor-4.1.7/jsp/upload_json.jsp',
	        fileManagerJson : '/kindeditor-4.1.7/jsp/file_manager_json.jsp',
	        allowUpload : true,
	        allowFileManager : true,
	afterCreate : function() {
	var self = this;
	KindEditor.ctrl(document, 13, function() {
	self.sync();
	document.forms['example'].submit();
	});
	KindEditor.ctrl(self.edit.doc, 13, function() {
	self.sync();
	document.forms['example'].submit();
	});
	},
	items : [
	'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'code', 'cut', 'copy', 'paste',
	'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
	'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
	'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
	'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
	'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|',
	'table', 'hr', 'pagebreak',
	'anchor', 'link', 'unlink', '|', 'image','multiimage','flash','media','insertfile','editImage'
	],
	afterBlur: function(){this.sync();},//和DWZ 的 Ajax onsubmit 冲突,提交表单时 编辑器失去焦点执行填充内容
	newlineTag : "br"
	});
	
	return obj
}

/**
 * 初始化编辑器，用这个初始化，在谷歌浏览器忠也能用
 * @param keid
 * @param width
 * @param height
 */
function kedit(obj, keid, width, height){ 
	obj =  KindEditor.create(
	'#' + keid,
	{     
	width : width, //编辑器的宽度为70%
	height : height, //编辑器的高度为100px 
	filterMode : false, //不会过滤HTML代码
	resizeMode : 1 ,//编辑器只能调整高度 
	imageUploadJson : '/kindeditor-4.1.7/jsp/upload_json.jsp',
	        fileManagerJson : '/kindeditor-4.1.7/jsp/file_manager_json.jsp',
	        allowUpload : true,
	        allowFileManager : true,
	afterCreate : function() {
	var self = this;
	KindEditor.ctrl(document, 13, function() {
	self.sync();
	document.forms['example'].submit();
	});
	KindEditor.ctrl(self.edit.doc, 13, function() {
	self.sync();
	document.forms['example'].submit();
	});
	},
	items : [
	
	],
	afterBlur: function(){this.sync();},//和DWZ 的 Ajax onsubmit 冲突,提交表单时 编辑器失去焦点执行填充内容
	newlineTag : "br"
	});
	
	return obj
}

function orderMenu(obj, html) {
	html = html + "<h4>"+getWorkflowName(obj.name)+"<small><i class='fa fa-clock-o'></i> 5 mins</small></h4>";
	if(obj.data[0] && obj.data[0].count > 0) {
		if(obj.data[0].userClass == 0) {
            html = html + "<p>申请："+obj.data[0].count+"</p>";
		} else if(obj.data[0].userClass == 1) {
			html = html + "<p>需审核："+obj.data[0].count+"</p>";
		} else if(obj.data[0].userClass == 2) {
			html = html + "<p>已完成："+obj.data[0].count+"</p>";
		}
	}
	if(obj.data[1] && obj.data[1].count > 0) {
		if(obj.data[1].userClass == 0) {
			html = html + "<p>申请："+obj.data[1].count+"</p>";
		} else if(obj.data[1].userClass == 1) {
			html = html + "<p>需审核："+obj.data[1].count+"</p>";
		} else if(obj.data[1].userClass == 2) {
			html = html + "<p>已完成："+obj.data[1].count+"</p>";
		}
	}
	return html;
}

function getWorkflowName(type) {
	if(type == "achievement") {
		return "绩效";
	}
	if(type == "salary") {
		return "薪资";
	}
	if(type == "buy") {
		return "采购";
	}
	if(type == "borrow") {
		return "借款";
	}
	if(type == "companyPay") {
		return "企业付款";
	}
	if(type == "contract") {
		return "业务合同";
	}
	if(type == "contractBorrow") {
		return "合同借阅";
	}
	if(type == "dimission") {
		return "离职申请";
	}
	if(type == "insurance") {
		return "医社保";
	}
	if(type == "invoice") {
		return "开票";
	}
	if(type == "leave") {
		return "请假";
	}
	if(type == "pettyCash") {
		return "备用金";
	}
	if(type == "recruitment") {
		return "岗位申请";
	}
	if(type == "reimburse") {
		return "报销";
	}
	if(type == "worklist") {
		return "工作计划";
	}
	if(type == "workOvertime") {
		return "加班";
	}
}


function openNavTab(a, userClass, name, url) {
	var title = getWorkflowName(name);
	$.ajax({
		url: getRoot() + "auditorStatus/readWfNotes.action",
		data: {"type": name, "userClass": userClass},
		type: "post",
		success: function() {
			getWorkflowNotes();
		},
		error: function() {
			
		}
	});
	navTab.openTab(name, url, {"title": title});
}

/**
 * 只用于到期未回款的打开
 * @param a
 * @param title
 * @param name
 * @param url
 */
function openNavTab2(a, title, name, url) {
	navTab.openTab(name, url, {"title": title});
}

function openMenuNavTab(name, url, title) {
	$("#menuListDiv").slideUp();
	navTab.openTab(name, url,  {"title": title});
}

function getHeight(height) {
	var winH = $(window).height();
	if(parseInt(height) >= parseInt(winH)) {
		return winH;
	} else {
		return height;
	}
}

function getIsOpen(id) {
	var isOpen = $("#layout").find("#container").find("#navTab").find(".navTab-panel").find(".page:visible").find("#isOpen").val();
	var currTab = navTab.getCurrentPanel();
	var afterUrl = "";
	if(isOpen) {
		var url = $("#layout").find("#container").find("#navTab").find(".tabsPageHeader").find(".tabsPageHeaderContent").find(".navTab-tab").find(".selected").attr("url");
		var urlArr = url.split("?");
		afterUrl = urlArr[0];
		var param = urlArr[1];
		var paramVal = param.split("=");
		var title = paramVal[1];
		$("#layout").find("#container").find("#navTab").find(".tabsPageHeader").find(".tabsPageHeaderContent").find(".navTab-tab").find(".selected").find("a").find("span").html(title);
		idClick(id);
		$("#layout").find("#container").find("#navTab").find(".tabsPageHeader").find(".tabsPageHeaderContent").find(".navTab-tab").find(".selected").attr("url", afterUrl);
		$("#layout").find("#container").find("#navTab").find(".navTab-panel").find(".page:visible").find("#isOpen").val("");
	}
}

/**
 * 通过传入的id，触发该id的点击事件
 * @param id
 */
function idClick(id) {
	$("#"+id).click();
	
}