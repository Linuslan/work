package com.linuslan.oa.workflow.flows.achievement.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.linuslan.oa.common.IBaseService;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.flows.achievement.model.Achievement;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContent;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContentOpinion;
import com.linuslan.oa.workflow.flows.achievement.model.YearAchievement;

public interface IAchievementService extends IBaseService {

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
	 * 通过绩效主表的id查询绩效项目
	 * @param id
	 * @return
	 */
	public List<AchievementContent> queryContentsByAchievementId(Long id);
	
	/**
	 * 通过绩效项目的id查询绩效的项目的审核意见记录
	 * @param id
	 * @return
	 */
	public List<AchievementContentOpinion> queryContentOpinionByContentId(Long id);
	
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
	public boolean add(Achievement achievement, List<AchievementContent> contents);
	
	/**
	 * 更新申请单
	 * @param achievement
	 * @return
	 */
	public boolean update(Achievement achievement, List<AchievementContent> contents);
	
	/**
	 * 删除申请单，伪删除，将isDelete=0更新为isDelete=1
	 * @param achievement
	 * @return
	 */
	public boolean del(Achievement achievement);
	
	/**
	 * 申请人提交申请
	 * @param achievement
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Achievement achievement, List<AchievementContent> contents, int passType, String opinion, boolean isUpdate);
	
	/**
	 * 审核申请单
	 * @param achievement
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Achievement achievement, List<AchievementContent> contents, List<AchievementContentOpinion> opinions, int passType, String opinion);
	
	/**
	 * 批量审核
	 * @param ids
	 * @param passType
	 * @param opinion
	 * @return
	 * @throws Exception
	 */
	public int auditBatch(List<Long> ids, int passType, String opinion);
	
	/**
	 * 删除绩效项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id);
	
	/**
	 * 验证对象的有效性
	 * @param achievement
	 */
	public void valid(Achievement achievement);
	
	/**
	 * 将数据写入到excel中，返回excel对象
	 * @param year
	 * @param list
	 * @return
	 */
	public HSSFWorkbook getHSSFWorkbook(int year, List<YearAchievement> list);
	
}
