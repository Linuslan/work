package com.linuslan.oa.workflow.flows.achievement.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	
	@Transient
	private Long leader1;
	
	@Transient
	private Long leader2;
	
	@Transient
	private Long leader3;
	
	@Transient
	private AchievementContentScore contentScore1;
	
	@Transient
	private AchievementContentScore contentScore2;
	
	@Transient
	private AchievementContentScore contentScore3;
	
	@Transient
	private String leaderScoreOpinion;

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

	public Long getLeader1() {
		return leader1;
	}

	public void setLeader1(Long leader1) {
		this.leader1 = leader1;
	}

	public Long getLeader2() {
		return leader2;
	}

	public void setLeader2(Long leader2) {
		this.leader2 = leader2;
	}

	public Long getLeader3() {
		return leader3;
	}

	public void setLeader3(Long leader3) {
		this.leader3 = leader3;
	}

	public AchievementContentScore getContentScore1() {
		return contentScore1;
	}

	public void setContentScore1(AchievementContentScore contentScore1) {
		this.contentScore1 = contentScore1;
	}

	public AchievementContentScore getContentScore2() {
		return contentScore2;
	}

	public void setContentScore2(AchievementContentScore contentScore2) {
		this.contentScore2 = contentScore2;
	}

	public AchievementContentScore getContentScore3() {
		return contentScore3;
	}

	public void setContentScore3(AchievementContentScore contentScore3) {
		this.contentScore3 = contentScore3;
	}

	public String getLeaderScoreOpinion() {
		return leaderScoreOpinion;
	}

	public void setLeaderScoreOpinion(String leaderScoreOpinion) {
		this.leaderScoreOpinion = leaderScoreOpinion;
	}
}
