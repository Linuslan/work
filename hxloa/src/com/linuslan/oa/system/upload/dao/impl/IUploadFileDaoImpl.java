package com.linuslan.oa.system.upload.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linuslan.oa.common.IBaseDaoImpl;
import com.linuslan.oa.system.upload.dao.IUploadFileDao;
import com.linuslan.oa.system.upload.model.UploadFile;
import com.linuslan.oa.util.CodeUtil;

@Component("uploadFileDao")
public class IUploadFileDaoImpl extends IBaseDaoImpl implements IUploadFileDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 通过tbId和tbName查询相应的附件
	 * @param id
	 * @param name
	 * @return
	 */
	public List<UploadFile> queryByTbIdAndTbName(Long id, String name) {
		List<UploadFile> list = new ArrayList<UploadFile> ();
		if(null != id && CodeUtil.isNotEmpty(name)) {
			StringBuffer hql = new StringBuffer("FROM UploadFile uf WHERE uf.tbId=:id AND uf.tbName=:name");
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setParameter("id", id);
			query.setParameter("name", name);
			list.addAll(query.list());
		}
		return list;
	}
	
	public UploadFile queryById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (UploadFile) session.get(UploadFile.class, id);
	}
	
	/**
	 * 批量新增上传文件对象
	 * @param files
	 * @throws RuntimeException
	 */
	public void addBatch(List<UploadFile> files) throws RuntimeException {
		try {
			if(null != files) {
				Iterator<UploadFile> iter = files.iterator();
				UploadFile file = null;
				while(iter.hasNext()) {
					file = iter.next();
					this.add(file);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
	}
	
	/**
	 * 单个新增上传文件对象
	 * @param uploadFile
	 * @throws RuntimeException
	 */
	public void add(UploadFile uploadFile) throws RuntimeException {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.save(uploadFile);
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
	}
	
	/**
	 * 删除
	 * @param uploadFile
	 * @throws RuntimeException
	 */
	public void del(UploadFile uploadFile) throws RuntimeException {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.delete(uploadFile);
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex);
		}
	}
}
