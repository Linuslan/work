package com.linuslan.oa.system.message.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.system.message.dao.IMessageDao;
import com.linuslan.oa.system.message.model.Message;
import com.linuslan.oa.util.CodeUtil;

@Component("messageDao")
public class IMessageDaoImpl extends IBaseDaoImpl implements IMessageDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 批量新增
	 * 返回成功数
	 * @param list
	 * @return
	 */
	public int addBatch(List<Message> list) {
		Connection conn = null;
		PreparedStatement ps = null;
		int count = 0;
		try {
			conn = SessionFactoryUtils.getDataSource(this.sessionFactory).getConnection();
			if(null != list && 0 < list.size()) {
				String sql = "INSERT INTO sys_message(id, tb_name, tb_id, is_read, is_deal, show_type, user_id, user_name, send_date, is_delete)" +
						" VALUES(sys_message_seq.nextval, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?)";
				ps = conn.prepareStatement(sql);
				Iterator<Message> iter = list.iterator();
				Message message = null;
				while(iter.hasNext()) {
					message = iter.next();
					if(null != message.getTbId()
							&& CodeUtil.isNotEmpty(message.getTbName())
							&& null != message.getUserId()) {
						ps.setString(1, message.getTbName());
						ps.setLong(2, message.getTbId());
						ps.setInt(3, message.getIsRead());
						ps.setInt(4, message.getIsDeal());
						ps.setInt(5, message.getShowType());
						ps.setLong(6, message.getUserId());
						ps.setString(7, message.getUserName());
						ps.setInt(8, message.getIsDelete());
						ps.addBatch();
					}
				}
				count = ps.executeBatch().length;
			}
			conn.commit();
		} catch(Exception ex) {
			try {
				conn.rollback();
			} catch(Exception e) {
				ex.printStackTrace();
			}
			CodeUtil.throwRuntimeExcep(ex);
		} finally {
			try {
				if(null != ps) {
					ps.close();
				}
				if(null != conn) {
					conn.close();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return count;
	}
}
