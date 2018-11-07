package com.linuslan.oa.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.linuslan.oa.util.ConstantVar;

public class LoginFilter implements Filter {

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		String url = req.getRequestURL().toString();
		String root = "http://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath();
		String errorUrl = "http://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath()+"/mainhome/error.jsp";
		
		/*
		 * 如果访问的地址中包括error.jsp或者login.jsp或者logout.action或者logout.jsp或者login.action的字符串
		 * 则不做处理
		 */
		if((req.getServletPath().indexOf(".js") > 0 && req.getServletPath().indexOf(".jsp") <= 0)
				|| req.getServletPath().indexOf(".css") > 0
				|| req.getServletPath().indexOf("error.jsp") > 0
				|| req.getServletPath().indexOf("login.jsp") > 0
				|| req.getServletPath().indexOf("logout.jsp") > 0
				|| req.getServletPath().indexOf("logout.action") > 0
				|| req.getServletPath().indexOf("login.action") > 0
				|| req.getServletPath().indexOf(".jpg") >0
				|| req.getServletPath().indexOf(".png") > 0
				|| req.getServletPath().indexOf(".gif") > 0
				|| req.getServletPath().indexOf(".valid") > 0) {
			chain.doFilter(request, response);
		} else {
			if(null == session || null == session.getAttribute(ConstantVar.LOGINUSER)) {
				//resp.sendRedirect(errorUrl);
				String event = "window.location.href='"+root+"';";
				resp.getWriter().print("<script>"+event+"</script>");
			} else {
				chain.doFilter(request, response);
			}
		}
		
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

}
