package com.linuslan.oa.workflow.flows.achievement.dao;

import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.IBaseDao;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.achievement.model.Achievement;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContent;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContentOpinion;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContentScore;
import com.linuslan.oa.workflow.flows.achievement.model.YearAchievement;

public interface IAchievementDao extends IBaseDao {

	/**
	 * 查询登陆用户的申请单
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Achievement> queryPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Achievement> queryAuditPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询登陆用户审核过的绩效
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Achievement> queryAuditedPage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询待登陆用户评分的绩效申请
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Achievement> queryScorePage(Map<String, String> paramMap, int page, int rows);
	
	/**
	 * 查询绩效汇总
	 * @param paramMap
	 * @param year 当前年
	 * @param month 当前月
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> queryReportPage(Map<String, String> paramMap, int year, int month, int page, int rows);
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Achievement queryById(Long id);
	
	/**
	 * 通过日期查询用户是否已经有存在的绩效
	 * @param date
	 * @return
	 */
	public Achievement queryByDate(String date, Long userId, Long achievementId);
	
	/**
	 * 查询ids集合中的绩效
	 * @param ids
	 * @return
	 */
	public List<Achievement> queryInIds(List<Long> ids);
	
	/**
	 * 通过绩效主表的id查询绩效项目
	 * @param id
	 * @return
	 */
	public List<AchievementContent> queryContentsByAchievementId(Long id);
	
	/**
	 * 通过绩效id查询绩效的评分日志
	 * @param id
	 * @return
	 */
	public List<AchievementContentScore> queryScoreOpinionByAchievementId(Long id);
	
	/**
	 * 通过绩效项目的id查询绩效的项目的审核意见记录
	 * @param id
	 * @return
	 */
	public List<AchievementContentOpinion> queryContentOpinionByContentId(Long id);
	
	/**
	 * 通过id查询绩效项目
	 * @param ids
	 * @return
	 */
	public List<AchievementContent> queryContentsInIds(List<Long> ids);
	
	/**
	 * 查询全年绩效
	 * @param year
	 * @return
	 */
	public List<YearAchievement> queryYearAchievement(int year);
	
	/**
	 * 新增申请单
	 * @param achievement
	 * @return
	 */
	public boolean add(Achievement achievement);
	
	/**
	 * 批量更新绩效项目，有id则更新，没有id，则新增
	 * @param contents
	 * @return
	 */
	public boolean mergeContents(List<AchievementContent> contents);
	
	/**
	 * 更新申请单
	 * @param achievement
	 * @return
	 */
	public boolean update(Achievement achievement);
	
	/**
	 * 删除绩效项目的id不在ids集合中的
	 * @param ids
	 * @return
	 */
	public boolean delContentsNotInIds(List<Long> ids, Long achievementId);
	
	/**
	 * 删除绩效项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 批量添加绩效项目的审核意见
	 * @param opinions
	 * @return
	 */
	public boolean saveContentOpinionBatch(List<AchievementContentOpinion> opinions);
	
	/**
	 * 批量添加绩效项目的审核意见
	 * @param opinions
	 * @return
	 */
	public boolean saveContentScoreBatch(List<AchievementContentScore> scores);
	
}
