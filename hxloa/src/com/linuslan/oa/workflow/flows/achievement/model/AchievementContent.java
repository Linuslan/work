package com.linuslan.oa.workflow.flows.achievement.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="wf_achievement_content")
public class AchievementContent {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfAchievementContentSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfAchievementContentSeq", sequenceName="wf_achievement_content_seq")
	private Long id;
	
	@Column(name="achievement_id")
	private Long achievementId;
	
	@Column(name="title")
	private String title;
	
	@Column(name="content")
	private String content;
	
	@Column(name="source")
	private String source;
	
	@Column(name="formula")
	private String formula;
	
	@Column(name="score_weight")
	private Integer scoreWeight;
	
	@Column(name="performance")
	private String performance;
	
	@Column(name="user_score")
	private Integer userScore;
	
	@Column(name="leader_score")
	private Integer leaderScore;
	
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="order_no")
	private Integer orderNo=0;
	
	@Column(name="is_delete")
	private Integer isDelete=0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAchievementId() {
		return achievementId;
	}

	public void setAchievementId(Long achievementId) {
		this.achievementId = achievementId;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Integer getScoreWeight() {
		return scoreWeight;
	}

	public void setScoreWeight(Integer scoreWeight) {
		this.scoreWeight = scoreWeight;
	}

	public String getPerformance() {
		return performance;
	}

	public void setPerformance(String performance) {
		this.performance = performance;
	}

	public Integer getUserScore() {
		return userScore;
	}

	public void setUserScore(Integer userScore) {
		this.userScore = userScore;
	}

	public Integer getLeaderScore() {
		return leaderScore;
	}

	public void setLeaderScore(Integer leaderScore) {
		this.leaderScore = leaderScore;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}
