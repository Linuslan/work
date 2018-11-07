package com.linuslan.oa.system.message.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 消息提醒类
 * @author LinusLan
 *
 */

@Entity
@Table(name="sys_message")
public class Message {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysMessageSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysMessageSeq", sequenceName="sys_message_seq")
	private Long id;
	
	/**
	 * 关联的表名
	 */
	@Column(name="tb_name")
	private String tbName;
	
	/**
	 * 关联表的id
	 */
	@Column(name="tb_id")
	private Long tbId;
	
	/**
	 * 是否已读
	 * 0：未读
	 * 1：已读
	 */
	@Column(name="is_read")
	private Integer isRead = 0;
	
	/**
	 * 是否已处理
	 * 0：未处理
	 * 1：已处理
	 */
	@Column(name="is_deal")
	private Integer isDeal = 0;
	
	/**
	 * 展示方式
	 * 0：主页弹出展示
	 * 1：右下角提示
	 */
	@Column(name="show_type")
	private Integer showType = 0;
	
	/**
	 * 消息接收人
	 */
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="user_name")
	private String userName;
	
	/**
	 * 消息发送时间
	 */
	@Column(name="send_date")
	private Date sendDate;
	
	@Column(name="is_delete")
	private Integer isDelete = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTbName() {
		return tbName;
	}

	public void setTbName(String tbName) {
		this.tbName = tbName;
	}

	public Long getTbId() {
		return tbId;
	}

	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public Integer getIsDeal() {
		return isDeal;
	}

	public void setIsDeal(Integer isDeal) {
		this.isDeal = isDeal;
	}

	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
}
