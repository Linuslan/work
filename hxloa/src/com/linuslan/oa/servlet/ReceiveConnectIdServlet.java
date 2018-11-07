package com.linuslan.oa.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linuslan.oa.listener.SessionListener;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.ConstantVar;

/**
 * 用来获取客户端消息推送连上服务端之后生成的连接id
 * 从而和sessionid对应
 * @author LinusLan
 *
 */
public class ReceiveConnectIdServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String connectId = request.getParameter("connectId");
		if(CodeUtil.isNotEmpty(connectId)) {
			String sessionId = request.getSession().getId();
			User user = (User) request.getSession().getAttribute(ConstantVar.LOGINUSER);
			if(null != user && null != user.getId()) {
				SessionListener.getSessionMap().get(sessionId).setConnectId(connectId);
				SessionListener.getSessionMap().get(sessionId).setUserId(user.getId());
				String lastConnectId = CodeUtil
						.parseString(SessionListener.getSessionMap().get(sessionId).getMessageCache().get("lastConnectId"));
				if(CodeUtil.isEmpty(lastConnectId)) {
					SessionListener.getSessionMap().get(sessionId).getMessageCache().put("lastConnectId", connectId);
				}
			}
		}
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
	
}
