package com.linuslan.oa.workflow.flows.achievement.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import com.linuslan.oa.common.BaseFlow;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditLog;
import com.linuslan.oa.workflow.engine.util.EngineUtil;

@Entity
@Table(name="wf_achievement")
public class Achievement extends BaseFlow {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="wfAchievementSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="wfAchievementSeq", sequenceName="wf_achievement_seq")
	private Long id;
	
	@Formula("(SELECT t.name FROM sys_department t, sys_user t2 WHERE t.id=t2.department_id AND t2.id=user_id)")
	private String userDeptName;
	
	@Column(name="year")
	private Integer year;
	
	@Column(name="month")
	private Integer month;
	
	@Transient
	private Integer flowStatus;
	
	@Formula("(SELECT NVL(SUM(t.user_score), 0) FROM wf_achievement_content t WHERE t.is_delete=0 AND t.achievement_id=id)")
	private Integer userScore;
	
	@Formula("(SELECT NVL(SUM(t.leader_score), 0) FROM wf_achievement_content t WHERE t.is_delete=0 AND t.achievement_id=id)")
	private Integer leaderScore;
	
	@Column(name="add_score")
	private Integer addScore;
	
	@Column(name="add_money")
	private BigDecimal addMoney;
	
	@Formula("(SELECT NVL(SUM(t.score_weight), 0) FROM wf_achievement_content t WHERE t.is_delete=0 AND t.achievement_id=id)")
	private Integer totalScore;
	
	/**
	 * 审核记录
	 * 如果没有加FetchMode.SUBSELECT，则如果是多个onetomany且fetch=FetchType.EAGER，则会报错
	 */
	@OneToMany(targetEntity=AuditLog.class, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name="wf_id", referencedColumnName="id")
	@Where(clause="wf_type='achievement' AND is_audit=0")
	private List<AuditLog> auditLogs;
	
	@Transient
	private String auditors;
	
	@Transient
	private String allFlowStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
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

	public Integer getAddScore() {
		return addScore;
	}

	public void setAddScore(Integer addScore) {
		this.addScore = addScore;
	}

	public BigDecimal getAddMoney() {
		return addMoney;
	}

	public void setAddMoney(BigDecimal addMoney) {
		this.addMoney = addMoney;
	}
	
	public Integer getFlowStatus() {
		this.flowStatus = EngineUtil.getStatus(auditLogs);
		return this.flowStatus;
	}

	public void setFlowStatus(Integer flowStatus) {
		this.flowStatus = flowStatus;
	}
	
	public String getAllFlowStatus() {
		this.allFlowStatus = EngineUtil.getAllStatus(auditLogs);
		return allFlowStatus;
	}

	public void setAllFlowStatus(String allFlowStatus) {
		this.allFlowStatus = allFlowStatus;
	}

	public List<AuditLog> getAuditLogs() {
		Map<String, Boolean> map = EngineUtil.checkAuditBtn(this.auditLogs);
		this.passBtn = map.get("passBtn");
		this.rejectBtn = map.get("rejectBtn");
		return auditLogs;
	}

	public void setAuditLogs(List<AuditLog> auditLogs) {
		this.auditLogs = auditLogs;
	}

	public String getAuditors() {
		this.auditors = EngineUtil.getAuditors(auditLogs);
		return auditors;
	}

	public void setAuditors(String auditors) {
		this.auditors = auditors;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public String getUserDeptName() {
		return userDeptName;
	}

	public void setUserDeptName(String userDeptName) {
		this.userDeptName = userDeptName;
	}
}