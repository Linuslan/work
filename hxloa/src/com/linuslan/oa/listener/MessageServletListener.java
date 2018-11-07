package com.linuslan.oa.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.comet4j.core.CometContext;

import com.linuslan.oa.quartz.MessageQuartz;

/**
 * 向web端推送消息的servlet
 * @author LinusLan
 *
 */
public class MessageServletListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		System.out.println("=================================消息推送服务监听开始启动============================");
		// TODO Auto-generated method stub
		CometContext context = CometContext.getInstance();
		//注册频道unRead和text2
		context.registChannel("unRead");
		context.registChannel("unReadNotice");
		Thread thread = new Thread(new MessageQuartz(), "unRead");
		thread.setDaemon(true);
		thread.start();
		System.out.println("==============================消息推送服务监听启动成功===========================");
	}

}
