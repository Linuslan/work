<%@ page language="java" import="java.util.*,com.linuslan.oa.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/s" prefix="s" %>
<%@ taglib uri="/c" prefix="c" %>
<%@ taglib uri="/fn" prefix="fn" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>办公协同系统</title>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<link rel="stylesheet" href="resources/bootstrap/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="resources/Font-Awesome-master/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="resources/ionicons-master/css/ionicons.min.css">
    
    <!-- jvectormap -->
    <link rel="stylesheet" href="resources/bootstrap/plugins/jvectormap/jquery-jvectormap-1.2.2.css">
    <link rel="stylesheet" href="resources/bootstrap/plugins/fullcalendar/fullcalendar.min.css">
    <link rel="stylesheet" href="resources/bootstrap/plugins/fullcalendar/fullcalendar.print.css" media="print">
    <!-- Select2 -->
    <link rel="stylesheet" href="resources/bootstrap/plugins/select2/select2.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="resources/bootstrap/dist/css/AdminLTE.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="resources/bootstrap/dist/css/skins/_all-skins.min.css">
    <link rel="stylesheet" type="text/css" href="resources/dialog/dist/css/bootstrap-dialog.css">
  	<!-- <link rel="stylesheet" href="resources/bootstrap/plugins/datatables/jquery.dataTables_themeroller.css"> -->
	
	<link rel="stylesheet" type="text/css" href="resources/javascript/plugins/combotree/combotree.css">
	<!-- <link rel="stylesheet" type="text/css" href="resources/javascript/plugins/datagrid/datagrid.css"> -->
	<link rel="stylesheet" type="text/css" href="resources/javascript/plugins/treegrid/treegrid.css">
	<link rel="stylesheet" type="text/css" href="resources/javascript/plugins/datalist/datalist.css">
	<link rel="stylesheet" href="resources/bootstrap/plugins/daterangepicker/daterangepicker-bs3.css">
    <!-- iCheck for checkboxes and radio inputs -->
    <link rel="stylesheet" href="resources/bootstrap/plugins/iCheck/all.css">
    <link rel="stylesheet" href="resources/bootstrap/plugins/colorpicker/bootstrap-colorpicker.min.css">
    <!-- Bootstrap time Picker -->
    <link rel="stylesheet" href="resources/bootstrap/plugins/timepicker/bootstrap-timepicker.min.css">
    <link rel="stylesheet" href="resources/bootstrap/plugins/datepicker/datepicker3.css">
    <!-- The link to the CSS that the grid needs -->
    <link rel="stylesheet" type="text/css" media="screen" href="resources/javascript/plugins/jqgrid/ui.jqgrid-bootstrap.css" />
    <link rel="stylesheet" type="text/css" media="screen" href="resources/javascript/plugins/inputspinner/spinner.css">
    <link rel="stylesheet" type="text/css" href="resources/css/mybooststrap.css">
    <link rel="stylesheet" type="text/css" href="resources/javascript/plugins/My97DatePicker/skin/WdatePicker.css">
    <link rel="stylesheet" type="text/css" href="resources/css/print1.css">
    <style type="text/css">
    	.lightbox{
			position: fixed;
		    top: 0px;
		    left: 0px;
		    height: 100%;
		    width: 100%;
		    z-index: 7;
		    opacity: 0.3;
		    display: block;
		    background-color: rgb(0, 0, 0);
		    display: none;
		}
		.pop,iframe{
			position: absolute;
		    left: 50%;
		    top:0px;
			width: 893px;
		    height: 100%;
		    margin-left: -446.5px;
		    z-index: 2000;
		}
    </style>
  </head>
  <body class="hold-transition skin-blue sidebar-mini">
  	<input type="hidden" value="${sessionScope.sessionId }" id="loginUserSessionId">
  	<input type="hidden" id="loginUserGroupIds" value="<%=BeanUtil.parseLongListToString(HttpUtil.getLoginUserGroupIds(), ",") %>" />
  	<div class="wrapper">
  		<header class="main-header">
  			<!-- Logo -->
	        <a href="javascript: void(0);" class="logo">
	        	<!-- mini logo for sidebar mini 50x50 pixels -->
	        	<span class="logo-mini"><b>OA</b></span>
	        	<!-- logo for regular state and mobile devices -->
	        	<span class="logo-lg"><i class="ion ion-coffee"></i>欢迎您</span>
	        </a>
	
	        <!-- Header Navbar: style can be found in header.less -->
	        <nav class="navbar navbar-static-top" role="navigation">
	        	<!-- Sidebar toggle button-->
	        	<a href="javascript:void(0);" class="sidebar-toggle" data-toggle="offcanvas" role="button">
	        		<span class="sr-only">Toggle navigation</span>
	        	</a>
	        	<img height="51px" src="resources/css/images/logo.png" />
	        	<!-- Navbar Right Menu -->
	        	<div class="navbar-custom-menu">
	        		<ul class="nav navbar-nav">
	        			<li>
	        				<a href="javascript: showUserInfo(${loginUser.id });">
	        					<i class="fa fa-user"></i>
	        					个人信息
	        				</a>
	        			</li>
	        			<li>
	        				<a href="javascript: updatePassword();">
	        					<i class="fa fa-gear"></i>
	        					密码修改
	        				</a>
	        			</li>
	        			<li>
	        				<!-- <a href="download/print/CLodopPrint_Setup_for_Win32NT.exe">打印控件</a> -->
	        				<a href="javascript: downloadLodop();">
	        					<i class="fa fa-download"></i>
	        					打印控件
	        				</a>
	        			</li>
	        			<!-- Messages: style can be found in dropdown.less-->
		        		<li class="dropdown notifications-menu">
		        			<a href="javascript: void(0);" class="dropdown-toggle" data-toggle="dropdown">
		        				<i class="fa fa-envelope-o"></i>
		        				<span class="label label-success" id="msg_count"></span>
		        			</a>
		        			<ul class="dropdown-menu" id="comet4j">
		        				<li class="header">消息提醒</li>
								<li id="sys_contract"></li>
		      					<li id="sys_user_contract"></li>
		      					<li id="sys_phone"></li>
		      					<li id="sys_cellphone"></li>
		      					<li id="sys_certificate"></li>
							</ul>
						</li>
						<!-- User Account: style can be found in dropdown.less -->
						<li class="dropdown user user-menu">
							<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" style="height: 50px;">
								<img src="resources/bootstrap/dist/img/avatar5.png" class="user-image" alt="User Image">
								<span class="hidden-xs">${sessionScope.loginUser.name}</span>
							</a>
							<ul class="dropdown-menu" style="width: 300px;">
								<!-- User image -->
								<li class="user-header">
									<img src="resources/bootstrap/dist/img/avatar5.png" class="img-circle" alt="User Image">
									<p>${sessionScope.loginUser.name} <%--- ${sessionScope.user.position.name} --%><small>${sessionScope.loginUser.employeeNo }</small></p>
								</li>
								<!-- Menu Body -->
								<li class="user-body">
									<!-- <div class="col-xs-4 text-center">
										<a href="javascript: void(0);">个人信息</a>
									</div>
									<div class="col-xs-4 text-center">
										<a href="javascript: updatePassword();">密码修改</a>
									</div>
									<div class="col-xs-4 text-center">
										<a href="download/print/CLodopPrint_Setup_for_Win32NT.exe">打印控件</a>
									</div> -->
								</li>
								<!-- Menu Footer-->
								<li class="user-footer">
									<!-- <div class="pull-left">
										<a href="#" class="btn btn-default btn-flat">Profile</a>
									</div> -->
									<div class="pull-left">
										<a href="javascript:void(0);" class="btn btn-success btn-flat"><i class="fa fa-lock"></i>锁屏</a>
									</div>
									<div class="pull-right">
										<a href="javascript:logout();" class="btn btn-danger btn-flat"><i class="fa fa-sign-out"></i>退出</a>
									</div>
								</li>
							</ul>
						</li>
						<!-- Control Sidebar Toggle Button
						<li>
							<a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
						</li> -->
					</ul>
				</div>
			</nav>
		</header>
		<aside class="main-sidebar">
			<!-- sidebar: style can be found in sidebar.less -->
			<section class="sidebar">
				<!-- Sidebar user panel -->
	            <div class="user-panel">
	            	<div class="pull-left image">
	            		<img src="resources/bootstrap/dist/img/avatar5.png" class="img-circle" alt="User Image">
	            	</div>
	            	<div class="pull-left info">
	            		<p>${sessionScope.user.name }</p>
	            		<a href="javascript: void(0);"><i class="fa fa-circle text-success"></i> 在线</a>
	            	</div>
	            </div>
	            <!-- search form
	            <form action="javascript:void(0);" method="get" class="sidebar-form">
	                <div class="input-group">
	                    <input type="text" name="q" class="form-control" placeholder="Search..."/>
	                    <span class="input-group-btn">
	                        <button type='submit' name='seach' id='search-btn' class="btn btn-flat"><i class="fa fa-search"></i></button>
	                    </span>
	                </div>
	            </form> -->
	            <!-- /.search form -->
	            <!-- sidebar menu: : style can be found in sidebar.less -->
	            <ul class="sidebar-menu">
	            	<li class="header">导航栏</li>
	                <li class="active">
	                    <a href="javascript:void(0);">
	                    	<i class="fa fa-dashboard"></i>
	                        <span>主页</span>
	                    </a>
	                </li>
	                <c:forEach items="${menuList}" var="menu">
						<li class="treeview">
		                  	<a href="javascript:void(0);">
		                  		<i class="fa ${menu.icon }"></i>
		                  		<span>${menu.text }</span>
		                  		<i class="fa fa-angle-left pull-right"></i>
		                  	</a>
		                  	<ul class="treeview-menu">
		                  		<c:forEach items="${menu.children }" var="child">
		                   			<li class="treeview">
			                    		<c:choose>
											<c:when test="${child.url == null}">
												<a href="javascript:void(0);">
													<i class="fa ${child.icon }"></i>
													<span>${child.text }</span>
													<c:if test="${fn:length(child.children) > 0 }">
														<i class="fa fa-angle-left pull-right"></i>
													</c:if>
												</a>
											</c:when>
											<c:otherwise>
												<a url="<%=basePath %>${child.url}" href="javascript: void(0);" tabid="${child.value }">
													<i class="fa ${child.icon }"></i>
													<span>${child.text }</span>
													<c:if test="${fn:length(child.children) > 0 }">
														<i class="fa fa-angle-left pull-right"></i>
													</c:if>
												</a>
											</c:otherwise>
										</c:choose>
										<c:if test="${fn:length(child.children) > 0 }">
											<ul class="treeview-menu">
												<c:forEach items="${child.children}" var="thirdMenu">
													<li>
														<c:choose>
															<c:when test="${thirdMenu.url == null}">
																<a href="javascript:void(0);">
																	<i class="fa ${thirdMenu.icon }"></i>
																	<span>${thirdMenu.text }</span>
																	<c:if test="${fn:length(thirdMenu.children) > 0 }">
																		<i class="fa fa-angle-left pull-right"></i>
																	</c:if>
																</a>
															</c:when>
															<c:otherwise>
																<a url="${thirdMenu.url }" href="javascript: void(0);" tabid="${thirdMenu.value }">
																	<i class="fa ${thirdMenu.icon }"></i>
																	<span>${thirdMenu.text }</span>
																	<c:if test="${fn:length(thirdMenu.children) > 0 }">
																		<i class="fa fa-angle-left pull-right"></i>
																	</c:if>
																</a>
															</c:otherwise>
														</c:choose>
													</li>
												</c:forEach>
											</ul>
										</c:if>
									</li>
		                  		</c:forEach>
		                  	</ul>
		                </li>
					</c:forEach>
	            </ul>
	        </section>
	        <!-- /.sidebar -->
	    </aside>
    	<!-- 必须要有这个，否则message和用户的信息点击无效 -->
	    <div class="content-wrapper">
	    	<!-- <section class="content-header">
	    		<h1>
	    			华夏蓝办公协同系统
		            <small>Version 1.0</small>
		        </h1>
	          	<ol class="breadcrumb">
	            	<li><a href="#"><i class="fa fa-dashboard"></i>主页</a></li>
	          	</ol>
	        </section>
	        <section class="content" id="mainpage" style="padding: 1px 2px; overflow-x: hidden; overflow-y: hidden;">
	        	<ul class="nav nav-tabs">
		      		<li class="active">
		      			<a href="#home2" data-toggle="tab">
		      				<label>Home</label>
		      			</a>
		      		</li>
		      	</ul>
		      	<div class="tab-content" style="width: 100%; height: 96.6%; overflow: auto; padding: 5px 2px 2px 5px;">
		      		<div class="tab-pane active" id="home2" style="height: 100%; width: 100%;">
		      			主页
				    </div>
			    </div>
	        </section> -->
	        <section class="content" id="mainpage" style="padding-top: 1px; padding-left: 5px; padding-right: 5px;">
	        	<!-- <ul class="nav nav-tabs custom-nav-tabs">
		      		<li class="active">
		      			<a href="#home2_tab" data-toggle="tab">
		      				<label>主页</label>
		      			</a>
		      		</li>
		      	</ul>
		      	<div class="tab-content" style="overflow-y: auto;">
		      		<div class="tab-pane active" id="home2_tab" style="height: 98%; width: 99.7%;">
		      			<div class="box box-solid" style="height: 99%;">
		      				<div class="box-body" id="home2" style="height: 99%;">
		      					
		      				</div>
		      			</div>
				    </div>
			    </div>
			     -->
			     
			    <div class="nav-tabs-top-border " style="margin-top: 2px;">
		   			<ul class="nav nav-tabs mytabs">
		   				<li class="active">
		   					<a href="#home2_tab" data-toggle="tab">
			      				<label>主页</label>
			      			</a>
		   				</li>
		   			</ul>
		   			<div class="tab-content">
			   			<div class="active tab-pane" id="home2_tab">
			   				<div class="box box-solid" style="height: 99%;">
			      				<div class="box-body" id="home2" style="height: 99%;">
			      					
			      				</div>
			      				<div class="box-body">
			      					
			      				</div>
			      			</div>
			   			</div>
			   		</div>
			   	</div>
	        </section>
	    </div>
   		<div id="showMsgEl">
   			<div class="msg-box panel-hide col-md-2 col-sm-6 col-xs-12 no-padding">
   				<span class="info-box-icon bg-aqua"><i class="fa fa-envelope-o"></i></span>
   				<div class="info-box-content">
   					<span class="info-box-number" style="font-weight: normal; font-size: 16px;">您有新消息，请注意查收</span>
   					<span class="info-box-text"></span>
   				</div><!-- /.info-box-content -->
   			</div>
   			<!-- 
  			<div class="info-box panel-hide" style="min-height: 100px; min-width: 200px;">
  				<div class="title" style="min-height: 20px;min-width: 200px;">您有新消息，请注意查收</div>
  				<div class="con" style="min-height: 80px;min-width: 200px;">测试测试111111</div>
  			</div>
  			 -->
  		</div>
  		<div id="menuListDiv" tabIndex=1 style="z-index: 9999; width: 130px;"></div>
  	</div>
  	<button type="button" class="btn btn-info btn-xs" id="showMsg"><i class="fa fa-envelope-o"></i></button>
    <!-- jQuery 2.1.4 -->
    <script src="resources/bootstrap/plugins/jQuery/jQuery-2.1.4.min.js"></script>
    <!-- Bootstrap 3.3.5 -->
    <script src="resources/bootstrap/bootstrap/js/bootstrap.min.js"></script>
    <!-- FastClick -->
    <script src="resources/bootstrap/plugins/fastclick/fastclick.min.js"></script>
    <!-- AdminLTE App -->
    <script src="resources/bootstrap/dist/js/app.min.js"></script>
    <!-- Sparkline -->
    <script src="resources/bootstrap/plugins/sparkline/jquery.sparkline.min.js"></script>
    <!-- jvectormap -->
    <script src="resources/bootstrap/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
    <script src="resources/bootstrap/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
    <!-- SlimScroll 1.3.0 -->
    <script src="resources/bootstrap/plugins/slimScroll/jquery.slimscroll.js"></script>
    <!-- ChartJS 1.0.1 -->
    <script src="resources/bootstrap/plugins/chartjs/Chart.min.js"></script>
    <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
    <script src="resources/bootstrap/dist/js/pages/dashboard2.js"></script>
    <!-- AdminLTE for demo purposes -->
    <script src="resources/bootstrap/dist/js/demo.js"></script>
    <script src="resources/dialog/dist/js/bootstrap-dialog.js"></script>
    <script type="text/javascript" src="resources/javascript/oa.js"></script>
    <script type="text/javascript" src="resources/javascript/util.js"></script>
    <script type="text/javascript" src="resources/javascript/plugins/combotree/combotree.js"></script>
    <script src="resources/javascript/moment.min.js"></script>
    <script src="resources/bootstrap/plugins/fullcalendar/fullcalendar.min.js"></script>
    <script type="text/javascript" src="resources/javascript/plugins/datagrid/datagrid.js"></script>
    <script type="text/javascript" src="resources/javascript/plugins/treegrid/treegrid.js"></script>
    <script type="text/javascript" src="resources/javascript/plugins/combotree/util.js"></script>
    <script type="text/javascript" src="resources/javascript/plugins/datalist/datalist.js"></script>
    <!-- Select2 -->
    <script src="resources/bootstrap/plugins/select2/select2.full.min.js"></script>
    <!-- InputMask -->
    <script src="resources/bootstrap/plugins/input-mask/jquery.inputmask.js"></script>
    <script src="resources/bootstrap/plugins/input-mask/jquery.inputmask.date.extensions.js"></script>
    <script src="resources/bootstrap/plugins/input-mask/jquery.inputmask.extensions.js"></script>
    
    <!-- bootstrap color picker -->
    <script type="text/javascript" src="resources/bootstrap/plugins/colorpicker/bootstrap-colorpicker.min.js"></script>
    <!-- bootstrap time picker -->
    <script type="text/javascript" src="resources/bootstrap/plugins/timepicker/bootstrap-timepicker.min.js"></script>
    <!-- SlimScroll 1.3.0 -->
    <script type="text/javascript" src="resources/bootstrap/plugins/slimScroll/jquery.slimscroll.min.js"></script>
    <!-- iCheck 1.0.1 -->
    <script type="text/javascript" src="resources/bootstrap/plugins/iCheck/icheck.min.js"></script>
    <script type="text/javascript" src="resources/bootstrap/plugins/datepicker/bootstrap-datepicker.js"></script>
    <script type="text/javascript" src="resources/bootstrap/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
    <script type="text/javascript" src="resources/bootstrap/plugins/daterangepicker/daterangepicker.js"></script>
    <script type="text/javascript" src="resources/javascript/util.js"></script>
    <script type="text/javascript" src="resources/javascript/myflow/lib/raphael-min.js"></script>
	<script type="text/javascript" src="resources/javascript/myflow/jquery-ui.js"></script>
	<script type="text/javascript" src="resources/javascript/myflow/myflow.js"></script>
	<script type="text/javascript" src="resources/javascript/myflow/myflow.jpdl4.js"></script>
	<script type="text/javascript" src="resources/javascript/myflow/myflow.editors.js"></script>
	<!-- We support more than 40 localizations -->
    <script type="text/ecmascript" src="resources/javascript/plugins/jqgrid/grid.locale-cn.js"></script>
    <!-- This is the Javascript file of jqGrid -->   
    <script type="text/ecmascript" src="resources/javascript/plugins/jqgrid/jquery.jqGrid.js"></script>
    <script type="text/ecmascript" src="resources/javascript/jquery-validate/jquery.validate.js"></script>
    <script type="text/ecmascript" src="resources/javascript/jquery-validate/localization/message-cn.js"></script>
    <script type="text/javascript" src="resources/javascript/plugins/inputspinner/spinner.js"></script>
    <script src="resources/bootstrap/plugins/ckeditor/ckeditor.js"></script>
    <script type="text/javascript" src="resources/javascript/comet4j.js"></script>
    <script type="text/javascript" src="resources/javascript/plugins/My97DatePicker/WdatePicker.js"></script>
    <script src="resources/javascript/LodopFuncs.js"></script>
    <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
	</object>
    <div class="lightbox" id="lightbox"></div>
   	<div id="pop" class="pop" style="display: none;">
		<iframe src="" frameborder="0" id="pdfContainer" name="pdfContainer"></iframe>
		<a href="javascript:closePDFViewer()" style="
		    position: absolute;
		    right: -90px;
		    top: 0px;
		    display: inline-block;
		    width: 80px;
		    height: 30px;
		    color: black;
		    font-size: 30px;
		"><i class="fa fa- fa-times"></i></a>
	</div>
    <script type="text/javascript">
    	var Lodop;
    	$(function() {
    		refreshIndex2();
    		//开始消息推送
    		//startComet4j();
    		$("#showMsg").click(function() {
    			$("#showMsgEl").addClass("panel-open");
    			$("#showMsg").hide();
    			$("#showMsgEl").find("span.info-box-icon").click(function() {
    				$("#showMsgEl").removeClass("panel-open");
    				$("#showMsg").show();
    			});
    		});
    		//$("#showMsgEl").popover();
    		//重置modal弹出框的方法，否则select2在弹出框里面的搜索栏不能用
    		//$.fn.modal.Constructor.prototype.enforceFocus = function() {};
    		$.jgrid.defaults.styleUI = "Bootstrap";
    		//初始化checkbox和radio
    		$("input[type='checkbox'].minimal, input[type='radio'].minimal").iCheck({
    			checkboxClass: "icheckbox_minimal-blue",
    			radioClass: "iradio_minimal-blue"
	        });
    		BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DEFAULT] = "温馨提示";
    		BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_INFO] = "温馨提示";
    		BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_PRIMARY] = "温馨提示";
    		BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_SUCCESS] = "成功";
    		BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_WARNING] = "警告";
    		BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DANGER] = "错误";
    		BootstrapDialog.DEFAULT_TEXTS['OK'] = "确定";
    		BootstrapDialog.DEFAULT_TEXTS['CANCEL'] = "取消";
    		BootstrapDialog.DEFAULT_TEXTS['CONFIRM'] = "温馨提示";
    		$("#mainpage").css({
    			//"height": $("#mainpage").parent().height() - $("#mainpage").prev().height()-5,
    			//"height": $("#mainpage").parent().height() - $("#mainpage").prev().height()-1,
    			//"height": "99%",
    			//"overflow-y": "hidden"
    		});
    		$("#mainpage>div.tab-content").css({
    			//"height": $("#mainpage").height() - $("#mainpage>ul.nav-tabs").height() - 5
    			"height": "99%"
    		});
    		/*$(document).on("click", "ul.custom-nav-tabs>li>a>span.close", function() {
    			try {
    				var idArr = $(this).parent().attr("href").split("#");
    				var id = idArr[1];
    				var $li = $(this).parent().parent();
    				var $prevLi = $li.prev();
    				var $nextLi = $li.next();
    				var $a;
    				if($prevLi && 0 < $prevLi.length) {
    					$a = $prevLi.find("a");
    				} else if($nextLi && 0 < $nextLi.length) {
    					$a = $nextLi.find("a");
    				}
    				$a.tab("show");
    				$li.remove();
    				$("#"+id).remove();
    			} catch(ex) {
    				alert(ex);
    			}
    		});*/
    		
    		$(document).on("click", "ul.mytabs>li>a>span.close", function() {
    			try {
    				var idArr = $(this).parent().attr("href").split("#");
    				var id = idArr[1];
    				var $li = $(this).parent().parent();
    				var $prevLi = $li.prev();
    				var $nextLi = $li.next();
    				var $a;
    				if($prevLi && 0 < $prevLi.length) {
    					$a = $prevLi.find("a");
    				} else if($nextLi && 0 < $nextLi.length) {
    					$a = $nextLi.find("a");
    				}
    				$a.tab("show");
    				$li.remove();
    				$("#"+id).remove();
    			} catch(ex) {
    				alert(ex);
    			}
    		});
    		
    		/*$("ul.sidebar-menu li a").click(function() {
    			var $this = $(this);
    			var href = $this.attr("url");
    			var tabid = $this.attr("tabid");
    			if(href && 0 < href.length && tabid && 0 < tabid.length) {
    				var $tab = $("ul.custom-nav-tabs>li>a[href=#"+tabid+"]");
    				if($tab && 0 < $tab.length) {
    					$tab.tab("show");
    				} else {
    					$("div.content-wrapper>section>ul.custom-nav-tabs>li.active").removeClass("active");
    					$("div.content-wrapper>section>div.tab-content>div.active").removeClass("active");
    					var title = $this.find("span").html();
    					$("div.content-wrapper>section>ul.custom-nav-tabs").append("<li class='active'><a href='#"+tabid+"' data-toggle='tab'><label>"+title+"</label><span class='close'></span></a></li>");
    					//$("div.content-wrapper>section>div.tab-content").append("<div class='tab-pane active' style='height: 100%; width: 100%;' id='"+tabid+"'></div>");
    					$("div.content-wrapper>section>div.tab-content").append("<div class='tab-pane active' style='height: 100%;' id='"+tabid+"'></div>");
    					$("div.content-wrapper>section>ul.custom-nav-tabs>li.active>a").tab("show");
              			$("div.content-wrapper>section>div.tab-content>#"+tabid).load(href, function(response) {
              				$("div.content-wrapper>section>div.tab-content>#"+tabid+">div.row").css({"overflow-y": "auto"});
              			});
    				}
    			}
    		});*/
    		
    		$("ul.sidebar-menu li a").click(function() {
    			var $this = $(this);
    			var href = $this.attr("url");
    			var tabid = $this.attr("tabid");
    			if(href && 0 < href.length && tabid && 0 < tabid.length) {
    				var $tab = $("ul.mytabs>li>a[href=#"+tabid+"]");
    				if($tab && 0 < $tab.length) {
    					$tab.tab("show");
    				} else {
    					$("div.content-wrapper>section>div>ul.mytabs>li.active").removeClass("active");
    					$("div.content-wrapper>section>div>div.tab-content>div.active").removeClass("active");
    					var title = $this.find("span").html();
    					$("div.content-wrapper>section>div>ul.mytabs").append("<li class='active'><a href='#"+tabid+"' data-toggle='tab'><label>"+title+"</label><span class='close'></span></a></li>");
    					//$("div.content-wrapper>section>div.tab-content").append("<div class='tab-pane active' style='height: 100%; width: 100%;' id='"+tabid+"'></div>");
    					$("div.content-wrapper>section>div>div.tab-content").append("<div class='tab-pane active' style='height: 100%;' id='"+tabid+"'></div>");
    					$("div.content-wrapper>section>div>ul.mytabs>li.active>a").tab("show");
              			$("div.content-wrapper>section>div>div.tab-content>#"+tabid).load(href, function(response) {
              				$("div.content-wrapper>section>div>div.tab-content>#"+tabid+">div.row").css({"overflow-y": "auto"});
              			});
    				}
    			}
    		});
    		
    		$.validator.setDefaults({
	            highlight: function (element) {
	                $(element).closest(".form-group").removeClass("has-success").addClass("has-error");
	            },
	            success: function (element) {
	                element.closest(".form-group").removeClass("has-error").addClass("has-success");
	            },
	            errorElement: "span",
	            errorPlacement: function (error, element) {
	                if (element.is(":radio") || element.is(":checkbox")) {
	                    error.appendTo(element.parent().parent().parent());
	                } else {
	                    error.appendTo(element.parent());
	                }
	            },
	            errorClass: "help-block m-b-none",
	            validClass: "help-block m-b-none"
	        });
    		
    		$("body").not(".quickMenuList").bind("click", function() {
    			$("#menuListDiv div.quickMenuNavigateList").slideUp();
    		});
    	});
    	var urlArray = new Array();
    	
    	function logout() {
    		BootstrapDialog.confirm({
    			title: "温馨提示",
    			message: "您确定退出吗？",
    			type: BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
    			callback: function(y) {
	   				if(y) {
	   					$.ajax({
	   						url: getRoot() + "sys/user/logout.action",
		    				type: "POST",
		    				success: function(data) {
		    					var json = eval("("+data+")");
		    					if(json.success) {
		    						BootstrapDialog.success(json.msg);
		    						window.location.href=getRoot();
		    					} else {
		    						BootstrapDialog.danger(json.msg);
		    					}
		    				}
	   					});
	   				}
	   			}
    		});
    	}
    	
    	/*function addTab(title, tabid, url, type) {
    		$("div.content-wrapper>section>ul.custom-nav-tabs>li.active").removeClass("active");
			$("div.content-wrapper>section>div.tab-content>div.active").removeClass("active");
			$("div.content-wrapper>section>ul.custom-nav-tabs").append("<li class='active'><a href='#"+tabid+"' data-toggle='tab'><label>"+title+"</label><span class='close'></span></a></li>");
			//$("div.content-wrapper>section>div.tab-content").append("<div class='tab-pane active' style='height: 100%; width: 100%;' id='"+tabid+"'></div>");
			$("div.content-wrapper>section>div.tab-content").append("<div class='tab-pane active' style='height: 100%;' id='"+tabid+"'></div>");
			$("div.content-wrapper>section>ul.custom-nav-tabs>li.active>a").tab("show");
  			$("div.content-wrapper>section>div.tab-content>#"+tabid).load(url, function(response) {
  				$("div.content-wrapper>section>div.tab-content>#"+tabid+">div.row").css({"overflow-y": "auto"});
  			});
  			if(type) {
  				try {
  					$.ajax({
  						url: getRoot() + "workflow/auditlog/updateAuditLogByWfType.action",
  						data: "wfType="+type,
  						type: "POST",
  						success: function(data) {
  							var json = eval("("+data+")");
  							if(json.success == false) {
  								BootstrapDialog.danger(json.msg);
  							} else {
  								refreshIndex();
  							}
  						},
  						error: function() {
  							BootstrapDialog.danger("系统网络异常");
  						}
  					});
  				} catch(ex) {
  					
  				}
  			}
    	}*/
    	
    	function addTab(title, tabid, url, type) {
    		$("div.content-wrapper>section>div>ul.mytabs>li.active").removeClass("active");
			$("div.content-wrapper>section>div>div.tab-content>div.active").removeClass("active");
			$("div.content-wrapper>section>div>ul.mytabs").append("<li class='active'><a href='#"+tabid+"' data-toggle='tab'><label>"+title+"</label><span class='close'></span></a></li>");
			//$("div.content-wrapper>section>div.tab-content").append("<div class='tab-pane active' style='height: 100%; width: 100%;' id='"+tabid+"'></div>");
			$("div.content-wrapper>section>div>div.tab-content").append("<div class='tab-pane active' style='height: 100%;' id='"+tabid+"'></div>");
			$("div.content-wrapper>section>div>ul.mytabs>li.active>a").tab("show");
  			$("div.content-wrapper>section>div>div.tab-content>#"+tabid).load(url, function(response) {
  				$("div.content-wrapper>section>div>div.tab-content>#"+tabid+">div.row").css({"overflow-y": "auto"});
  			});
  			if(type) {
  				try {
  					$.ajax({
  						url: getRoot() + "workflow/auditlog/updateAuditLogByWfType.action",
  						data: "wfType="+type,
  						type: "POST",
  						success: function(data) {
  							var json = eval("("+data+")");
  							if(json.success == false) {
  								BootstrapDialog.danger(json.msg);
  							} else {
  								refreshIndex2();
  							}
  						},
  						error: function() {
  							BootstrapDialog.danger("系统网络异常");
  						}
  					});
  				} catch(ex) {
  					
  				}
  			}
    	}
    	
    	function showUserInfo(id) {
      		BootstrapDialog.show({
   			    title: "个人信息详情",
   			    width: "80%",
   			    type: BootstrapDialog.TYPE_DEFAULT,
   			    message: $("<div></div>").load(getRoot() + "sys/user/queryById.action?user.id="+id+"&returnType=view"),
   			    draggable: true,
   			    autodestroy: true,
   			    closeByBackdrop: false,
   			    autospin: true,
   			    onshown: function(dialogRef) {
   		   			
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
    	}
    	
    	function updatePassword() {
    		BootstrapDialog.show({
			    title: "密码修改",
			    width: "40%",
			    type: BootstrapDialog.TYPE_DEFAULT,
			    message: $("<div></div>").load(getRoot() + "com/linuslan/oa/system/user/updatePassword.jsp"),
			    draggable: true,
			    autodestroy: true,
			    closeByBackdrop: false,
			    autospin: true,
			    onshown: function(dialogRef) {
			    	dialogRef.getModalBody().find("form").validate();
			    },
			    buttons: [{
			    	label: "保存",
			    	icon: "fa fa-fw fa-save",
			    	cssClass: "btn-success",
			    	action: function(dialog) {
			    		try {
			    			var form = dialog.getModalBody().find("form");
			    			var $button = this;
			    			if(form.valid()) {
			    				$.ajax({
				    				url: getRoot() + "sys/user/updatePassword.action",
				    				data: form.serialize(),
				    				type: "POST",
				    				success: function(data) {
				    					var json = eval("("+data+")");
				    					if(json.success) {
				    						BootstrapDialog.success(json.msg);
				    						dialog.close();
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
				    					$button.spin();
				    				},
				    				complete: function() {
				    					dialog.enableButtons(true);
				    					dialog.setClosable(true);
				    					$button.stopSpin();
				    				}
				    			});
			    			}
			    			
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
    	
    	function downloadLodop() {
    		BootstrapDialog.confirm({
    			title: "温馨提示",
    			message: "您确定下载吗？",
    			type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
	            closable: true, // <-- Default value is false
	            draggable: true, // <-- Default value is false
	            btnCancelLabel: "取消", // <-- Default value is 'Cancel',
	            btnOKLabel: "确定", // <-- Default value is 'OK',
	            btnOKClass: "btn-success", // <-- If you didn't specify it, dialog type will be used,
    			callback: function(y) {
	   				if(y) {
	   					//window.open("download/print/CLodopPrint_Setup_for_Win32NT.exe", "_blank","height=0,width=0,toolbar=no,menubar=no,scrollbars=no,resizable=on,location=no,status=no");
	   					var a = document.createElement("a");
	   					a.setAttribute("href", "download/print/CLodopPrint_Setup_for_Win32NT.exe");
	   					//a.setAttribute("target", "_blank");
	   					$("body").append(a);
	   					a.click();
	   					$(a).remove();
	   				}
	   			}
    		});
    	}
    </script>
  </body>
  
</html>
