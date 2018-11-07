package com.linuslan.oa.workflow.flows.achievement.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

/**
 * 评分阶段的项目评分的记录
 * @author LinusLan
 *
 */
@Entity
@Table(name="wf_achievement_content_score")
public class AchievementContentScore {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfAchievementContentScore", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfAchievementContentScore", sequenceName="wf_achievement_content_scr_seq")
	private Long id;
	
	/**
	 * 绩效项目的主键
	 */
	@Column(name="content_id")
	private Long contentId;
	
	@Column(name="user_id")
	private Long userId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=user_id)")
	private String userName;
	
	@Column(name="score")
	private Integer score;
	
	@Column(name="opinion")
	private String opinion;
	
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="is_delete")
	private Integer isDelete=0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
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
	
}
