package com.linuslan.oa.system.notice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 公告管理
 * @author LinusLan
 *
 */
@Entity
@Table(name="sys_notice")
public class Notice {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysNoticeSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysNoticeSeq", sequenceName="sys_notice_seq")
	private Long id;
	
	@Column(name="title")
	private String title;
	
	@Column(name="content")
	private String content;
	
	/**
	 * 创建人
	 */
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="is_delete")
	private Integer isDelete = 0;
	
	/**
	 * 是否发送
	 * 0：未发送
	 * 1：已发送
	 */
	@Column(name="is_send")
	private Integer isSend = 0;
	
	/**
	 * 发送人
	 */
	@Column(name="sender")
	private Long sender;
	
	@Column(name="sender_name")
	private String senderName;
	
	/**
	 * 发送时间
	 */
	@Column(name="send_date")
	private Date sendDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getIsSend() {
		return isSend;
	}

	public void setIsSend(Integer isSend) {
		this.isSend = isSend;
	}

	public Long getSender() {
		return sender;
	}

	public void setSender(Long sender) {
		this.sender = sender;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
}
