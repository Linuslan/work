package com.linuslan.oa.system.notice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="sys_notice_receiver")
public class NoticeReceiver {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysNoticeReceiverSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysNoticeReceiverSeq", sequenceName="sys_notice_receiver_seq")
	private Long id;
	
	/**
	 * 接受对象的类型
	 * sys_group表示用户组对象
	 * sys_user表示用户
	 * sys_department表示部门用户
	 */
	@Column(name="type")
	private String type;
	
	/**
	 * 对应type的主键id
	 */
	@Column(name="type_id")
	private Long typeId;
	
	/**
	 * typeId对应的名字
	 * 例如type为department，type_id为1
	 * 假设sys_department的主键为1的数据的名称为测试
	 * 则typeName为测试
	 */
	@Column(name="type_name")
	private String typeName;
	
	@Column(name="create_date")
	private Date createDate;
	
	/**
	 * 对应的notice主表的id
	 * 表示属于哪个公告
	 */
	@Column(name="notice_id")
	private Long noticeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
