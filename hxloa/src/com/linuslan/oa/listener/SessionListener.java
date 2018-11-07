package com.linuslan.oa.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.linuslan.oa.common.UserSession;

public class SessionListener implements HttpSessionListener {

	private static Map<String, UserSession> SESSIONMAP = new HashMap<String, UserSession> ();
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		// TODO Auto-generated method stub
		String sessionId = event.getSession().getId();
		event.getSession().setAttribute("sessionId", sessionId);
		UserSession userSession = new UserSession();
		userSession.setSessionId(sessionId);
		SessionListener.getSessionMap().put(sessionId, userSession);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		// TODO Auto-generated method stub
		String sessionId = event.getSession().getId();
		SessionListener.getSessionMap().remove(sessionId);
	}
	
	public static Map<String, UserSession> getSessionMap() {
		if(null == SessionListener.SESSIONMAP) {
			SESSIONMAP = new HashMap<String, UserSession> ();
		}
		return SESSIONMAP;
	}

}
