package com.linuslan.oa.system.rule.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 制度管理
 * @author LinusLan
 *
 */
@Entity
@Table(name="sys_rule")
public class Rule {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysRuleSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysRuleSeq", sequenceName="sys_rule_seq")
	private Long id;
	
	/**
	 * 制度标题
	 */
	@Column(name="title")
	private String title;
	
	/**
	 * 唯一标识符，当作上传的word转换成Html之后的文件名
	 */
	@Column(name="value")
	private String value;
	
	/**
	 * 备注
	 */
	@Column(name="memo")
	private String memo;
	
	/**
	 * 制度内容
	 */
	@Column(name="content")
	private String content;
	
	@Column(name="is_delete")
	private Integer isDelete = 0;
	
	@Column(name="order_no")
	private Integer orderNo = 0;
	
	/**
	 * 上传的文件对应的id
	 */
	@Column(name="file_path")
	private String filePath;

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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
