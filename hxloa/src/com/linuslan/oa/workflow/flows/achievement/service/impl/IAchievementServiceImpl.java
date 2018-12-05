package com.linuslan.oa.workflow.flows.achievement.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linuslan.oa.common.IBaseServiceImpl;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;
import com.linuslan.oa.workflow.engine.auditlog.dao.IAuditLogDao;
import com.linuslan.oa.workflow.engine.auditlog.model.AuditorOpinion;
import com.linuslan.oa.workflow.engine.util.EngineUtil;
import com.linuslan.oa.workflow.flows.achievement.dao.IAchievementDao;
import com.linuslan.oa.workflow.flows.achievement.model.Achievement;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContent;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContentOpinion;
import com.linuslan.oa.workflow.flows.achievement.model.AchievementContentScore;
import com.linuslan.oa.workflow.flows.achievement.model.YearAchievement;
import com.linuslan.oa.workflow.flows.achievement.service.IAchievementService;

@Component("achievementService")
@Transactional
public class IAchievementServiceImpl extends IBaseServiceImpl implements
		IAchievementService {
	
	private static Logger logger = Logger.getLogger(IAchievementServiceImpl.class);
	
	@Autowired
	private IAchievementDao achievementDao;
	
	@Autowired
	private EngineUtil engineUtil;
	
	@Autowired
	private IAuditLogDao auditLogDao;
	
	/**
	 * 查询登陆用户的申请单列表
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Achievement> queryPage(Map<String, String> paramMap, int page, int rows) {
		return this.achievementDao.queryPage(paramMap, page, rows);
	}
	
	/**
	 * 分页查询待登录用户审核的申请
	 * @param userId
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Achievement> queryAuditPage(Map<String, String> paramMap, int page, int rows) {
		return this.achievementDao.queryAuditPage(paramMap, page, rows);
	}
	
	/**
	 * 查询登陆用户审核过的绩效
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Achievement> queryAuditedPage(Map<String, String> paramMap, int page, int rows) {
		return this.achievementDao.queryAuditedPage(paramMap, page, rows);
	}
	
	/**
	 * 查询待登陆用户评分的绩效申请
	 * @param paramMap
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Achievement> queryScorePage(Map<String, String> paramMap, int page, int rows) {
		return this.achievementDao.queryScorePage(paramMap, page, rows);
	}
	
	/**
	 * 查询绩效汇总
	 * @param paramMap
	 * @param year 当前年
	 * @param month 当前月
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Map<String, Object>> queryReportPage(Map<String, String> paramMap, int year, int month, int page, int rows) {
		return this.achievementDao.queryReportPage(paramMap, year, month, page, rows);
	}
	
	/**
	 * 通过id查询申请单
	 * @param id
	 * @return
	 */
	public Achievement queryById(Long id) {
		return this.achievementDao.queryById(id);
	}
	
	/**
	 * 通过日期查询用户是否已经有存在的绩效
	 * @param date
	 * @return
	 */
	public Achievement queryByDate(String date, Long userId, Long achievementId) {
		return this.achievementDao.queryByDate(date, userId, achievementId);
	}
	
	/**
	 * 通过绩效主表的id查询绩效项目
	 * @param id
	 * @return
	 */
	public List<AchievementContent> queryContentsByAchievementId(Long id) {
		return this.achievementDao.queryContentsByAchievementId(id);
	}
	/**
	 * 通过绩效id查询绩效的评分日志
	 * @param id
	 * @return
	 */
	public List<AchievementContentScore> queryScoreOpinionByAchievementId(Long id) {
		return this.achievementDao.queryScoreOpinionByAchievementId(id);
	}
	
	/**
	 * 通过绩效项目的id查询绩效的项目的审核意见记录
	 * @param id
	 * @return
	 */
	public List<AchievementContentOpinion> queryContentOpinionByContentId(Long id) {
		return this.achievementDao.queryContentOpinionByContentId(id);
	}
	
	/**
	 * 查询全年绩效
	 * @param year
	 * @return
	 */
	public List<YearAchievement> queryYearAchievement(int year) {
		return this.achievementDao.queryYearAchievement(year);
	}
	
	/**
	 * 新增企业付款
	 * @param achievement
	 * @return
	 */
	public boolean add(Achievement achievement, List<AchievementContent> contents) {
		boolean success = false;
		if(null == achievement) {
			CodeUtil.throwRuntimeExcep("获取数据异常");
		}
		achievement.setUserId(HttpUtil.getLoginUser().getId());
		achievement.setCompanyId(HttpUtil.getLoginUser().getCompanyId());
		//验证对象的有效性
		this.valid(achievement);
		this.achievementDao.add(achievement);
		Map<String, Long> map = new HashMap<String, Long> ();
		map.put("achievementId", achievement.getId());
		//将绩效主表的id赋值给绩效项目
		BeanUtil.setValueBatch(contents, map);
		//检查绩效项目的有效性
		this.validContentBatch(contents, false);
		this.achievementDao.mergeContents(contents);
		//启动流程，生成流程实例
		this.engineUtil.startFlow(achievement, CodeUtil.getClassName(Achievement.class));
		success = true;
		return success;
	}
	
	/**
	 * 更新企业付款
	 * @param achievement
	 * @return
	 */
	public boolean update(Achievement achievement, List<AchievementContent> contents) {
		boolean success = false;
		if(null == achievement || null == achievement.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，获取数据异常");
		}
		Achievement persist = this.achievementDao.queryById(achievement.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("更新失败，数据不存在");
		}
		persist = (Achievement) BeanUtil.updateBean(persist, achievement);
		this.valid(persist);
		this.achievementDao.update(persist);
		Map<String, Long> map = new HashMap<String, Long> ();
		map.put("achievementId", achievement.getId());
		//获取已保存且未被用户删除的绩效项目
		String contentIdStr = BeanUtil.parseString(contents, "id", ",");
		List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
		//先删除不在contentIds中的绩效项目，不在contentIds中，即被用户在前端界面删除了
		if(0 < contentIds.size()) {
			this.achievementDao.delContentsNotInIds(contentIds, persist.getId());
			List<AchievementContent> persists = this.achievementDao.queryContentsInIds(contentIds);
			contents = (List<AchievementContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			//检查绩效项目的有效性
			this.validContentBatch(contents, false);
			this.achievementDao.mergeContents(contents);
		}
		success = true;
		return success;
	}
	
	/**
	 * 删除企业付款，伪删除，将isDelete=0更新为isDelete=1
	 * @param achievement
	 * @return
	 */
	public boolean del(Achievement achievement) {
		boolean success = false;
		if(null == achievement || null == achievement.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，获取数据异常");
		}
		Achievement persist = this.achievementDao.queryById(achievement.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("删除失败，数据不存在");
		}
		persist.setIsDelete(1);
		if(this.achievementDao.update(persist)) {
			success = true;
		}
		return success;
	}
	
	/**
	 * 申请人提交申请
	 * @param achievement
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean commit(Achievement achievement, List<AchievementContent> contents, int passType, String opinion, boolean isUpdate) {
		boolean success = false;
		boolean updateSuccess = false;
		if(null == achievement) {
			CodeUtil.throwRuntimeExcep("提交失败，获取数据异常");
		}
		/*
		 * 判断如果有id，则更新申请单，如果没有id，则新增申请单
		 */
		if(null != achievement.getId()) {
			Achievement persist = this.achievementDao.queryById(achievement.getId());
			if(null == persist || null == persist.getId()) {
				CodeUtil.throwRuntimeExcep("提交失败，申请单不存在");
			}
			persist = (Achievement) BeanUtil.updateBean(persist, achievement);
			achievement = persist;
			this.valid(persist);
			updateSuccess = this.achievementDao.update(persist);
		} else {
			achievement.setUserId(HttpUtil.getLoginUser().getId());
			achievement.setCompanyId(HttpUtil.getLoginUser().getCompanyId());
			this.valid(achievement);
			updateSuccess = this.achievementDao.add(achievement);
			if(updateSuccess) {
				//启动流程，生成流程实例
				this.engineUtil.startFlow(achievement, CodeUtil.getClassName(Achievement.class));
			}
		}
		if(updateSuccess) {
			/*
			 * 判断是直接提交还是在更新页面提交
			 */
			if(isUpdate) {
				Map<String, Long> map = new HashMap<String, Long> ();
				map.put("achievementId", achievement.getId());
				//获取已保存且未被用户删除的绩效项目
				String contentIdStr = BeanUtil.parseString(contents, "id", ",");
				List<Long> contentIds = BeanUtil.parseStringToLongList(contentIdStr, ",");
				List<AchievementContent> persists = null;
				//先删除不在contentIds中的绩效项目，不在contentIds中，即被用户在前端界面删除了
				if(0 < contentIds.size()) {
					this.achievementDao.delContentsNotInIds(contentIds, achievement.getId());
					persists = this.achievementDao.queryContentsInIds(contentIds);
				}
				contents = (List<AchievementContent>) BeanUtil.updateBeans(persists, contents, "id", map);
			} else {
				contents = this.achievementDao.queryContentsByAchievementId(achievement.getId());
			}
			
			boolean isSelfScore = false;
			if(achievement.getFlowStatus() == 5) {
				isSelfScore = true;
			}
			//检查绩效项目的有效性
			this.validContentBatch(contents, isSelfScore);
			this.achievementDao.mergeContents(contents);
		}
		logger.info("登录用户"+HttpUtil.getLoginUser().getId()+"提交的绩效主对象："+JSONObject.fromObject(achievement)+", 绩效内容为："+JSONArray.fromObject(contents));
		//验证流程实例是否可以提交
		this.validFlowStatus(achievement, false);
		this.engineUtil.execute(achievement, CodeUtil.getClassName(Achievement.class), passType);
		success = true;
		return success;
	}
	
	/**
	 * 审核申请单
	 * @param achievement
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean audit(Achievement achievement, List<AchievementContent> contents, List<AchievementContentOpinion> opinions, int passType, String opinion) {
		boolean success = false;
		if(null == achievement || null == achievement.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，获取数据异常");
		}
		Achievement persist = this.achievementDao.queryById(achievement.getId());
		if(null == persist || null == persist.getId()) {
			CodeUtil.throwRuntimeExcep("审核失败，申请单不存在");
		}
		this.achievementDao.saveContentOpinionBatch(opinions);
		String idStr = BeanUtil.parseString(contents, "id", ",");
		List<Long> ids = BeanUtil.parseStringToLongList(idStr, ",");
		List<AchievementContent> persistContens = null;
		if(0 < ids.size()) {
			persistContens = this.achievementDao.queryContentsInIds(ids);
		} else {
			persistContens = this.achievementDao.queryContentsByAchievementId(persist.getId());
		}
		
		success = this.executeAudit(achievement, persist, contents, persistContens, passType, opinion);
		return success;
	}
	
	/**
	 * 批量审核
	 * @param ids
	 * @param passType
	 * @param opinion
	 * @return
	 * @throws Exception
	 */
	public int auditBatch(List<Long> ids, int passType, String opinion) {
		int count = 0;
		if(null == ids || 0 >= ids.size()) {
			CodeUtil.throwRuntimeExcep("请至少选择一条数据");
		}
		List<Achievement> achievements = this.achievementDao.queryInIds(ids);
		if(0 >= achievements.size()) {
			CodeUtil.throwRuntimeExcep("您所选择的数据不存在");
		}
		Iterator<Achievement> iter = achievements.iterator();
		Achievement achievement = null;
		List<AchievementContent> contents = null;
		while(iter.hasNext()) {
			achievement = iter.next();
			if(null == achievement || null == achievement.getId()) {
				continue;
			}
			achievement.getAuditLogs();
			if((passType == 0 && achievement.isPassBtn())
					|| (passType == 1 && achievement.isRejectBtn())) {
				contents = this.achievementDao.queryContentsByAchievementId(achievement.getId());
				if(this.executeAudit(achievement, achievement, contents, contents, passType, opinion)) {
					count ++;
				}
			}
		}
		return count;
	}
	
	/**
	 * 执行审核的方法，批量审核和单条审核在从数据库中查询完数据之后，调用该方法来完成审核
	 * @param achievement
	 * @param persist
	 * @param contents
	 * @param persistContens
	 * @param passType
	 * @param opinion
	 * @return
	 */
	public boolean executeAudit(Achievement achievement, Achievement persist, List<AchievementContent> contents, List<AchievementContent> persistContens, int passType, String opinion) {
		/*
		 * 如果是评分阶段，则添加评分记录
		 */
		if(6 == persist.getFlowStatus()) {
			List<AchievementContentScore> scores = this.generateContentScoreLog(contents);
			this.achievementDao.saveContentScoreBatch(scores);
		}
		if(6 == persist.getFlowStatus() || 5 == persist.getFlowStatus() || 7 == persist.getFlowStatus()) {
			
			BeanUtil.updateBeans(persistContens, contents, "id", null);
			if(6 == persist.getFlowStatus() || 7 == persist.getFlowStatus()) {
				BeanUtil.updateBean(persist, achievement);
				this.achievementDao.update(persist);
				Iterator<AchievementContent> iter = persistContens.iterator();
				AchievementContent content = null;
				while(iter.hasNext()) {
					content = iter.next();
					if(null == content.getLeaderScore()) {
						content.setLeaderScore(content.getUserScore());
					}
				}
			} else {	//自评阶段，则判断自评的分数是否有效
				Iterator<AchievementContent> iter = persistContens.iterator();
				AchievementContent content = null;
				int selfScore = 0;
				while(iter.hasNext()) {
					content = iter.next();
					//if(null == content.getLeaderScore()) {
						selfScore += content.getUserScore();
					//}
				}
				if(selfScore <= 0) {
					CodeUtil.throwRuntimeExcep("自评分数无效，请重新自评");
				}
			}
			this.achievementDao.mergeContents(persistContens);
		}
		//验证流程实例是否为审核中
		this.validFlowStatus(persist, true);
		this.engineUtil.execute(persist, CodeUtil.getClassName(Achievement.class), passType);
		//添加审核意见
		AuditorOpinion op = EngineUtil.generateOpinion(opinion, passType, persist);
		this.auditLogDao.addOpinion(op);
		return true;
	}
	
	/**
	 * 删除绩效项目，伪删除
	 * @param id
	 * @return
	 */
	public boolean delContentById(Long id) {
		return this.achievementDao.delContentById(id);
	}
	
	/**
	 * 验证对象的有效性
	 * @param achievement
	 */
	public void valid(Achievement achievement) {
		
	}
	
	public void validContent(AchievementContent content, int i) {
		
	}
	
	/**
	 * 验证绩效内容的有效性
	 * @param contents
	 * @param isSelfScore 是否自评阶段，true：是；false：不是
	 */
	public void validContentBatch(List<AchievementContent> contents, boolean isSelfScore) {
		if(null == contents) {
			CodeUtil.throwRuntimeExcep("获取绩效项目数据异常");
		}
		if(0 >= contents.size()) {
			CodeUtil.throwRuntimeExcep("请至少填写一条绩效项目");
		}
		AchievementContent content = null;
		int userScore = 0;
		for(int i = 0; i < contents.size(); i ++) {
			content = contents.get(i);
			this.validContent(content, i);
			if(isSelfScore) {
				if(null != content.getUserScore()) {
					userScore = content.getUserScore();
				}
			}
		}
		/*
		 * 如果在自评阶段，且自评分数小于等于0，则不允许操作
		 */
		if(userScore <= 0 && isSelfScore) {
			CodeUtil.throwRuntimeExcep("请填写自评分数");
		}
	}
	
	public List<AchievementContentScore> generateContentScoreLog(List<AchievementContent> contents) {
		List<AchievementContentScore> scores = new ArrayList<AchievementContentScore> ();
		Iterator<AchievementContent> iter = contents.iterator();
		AchievementContent content = null;
		AchievementContentScore score = null;
		while(iter.hasNext()) {
			content = iter.next();
			score = new AchievementContentScore();
			score.setContentId(content.getId());
			score.setCreateDate(new Date());
			score.setIsDelete(0);
			score.setScore(content.getLeaderScore());
			score.setUserId(HttpUtil.getLoginUser().getId());
			score.setOpinion(content.getLeaderScoreOpinion());
			scores.add(score);
		}
		return scores;
	}
	
	/**
	 * 将数据写入到excel中，返回excel对象
	 * @param year
	 * @param list
	 * @return
	 */
	public HSSFWorkbook getHSSFWorkbook(int year, List<YearAchievement> list) {
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook();
			Sheet sheet = wb.createSheet();
			sheet.setDefaultRowHeightInPoints(30);
			sheet.setColumnWidth(0, 200*10);
			sheet.setColumnWidth(1, 300*15);
			//设置固定行
			sheet.createFreezePane(0, 2, 2, 3);
			CellStyle cs = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			cs.setFont(font);
			cs.setAlignment(CellStyle.ALIGN_CENTER);
			Row titleRow = sheet.createRow(0);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellValue(year+"年员工绩效列表");
			titleCell.setCellStyle(cs);
			
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 13));
			Row infoRow = sheet.createRow(1);
			infoRow.setHeightInPoints(30);
			CellStyle cs2 = wb.createCellStyle();
			Font font2 = wb.createFont();
			font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
			cs2.setFont(font2);
			Cell nameCell = infoRow.createCell(0);
			nameCell.setCellValue("姓名");
			nameCell.setCellStyle(cs2);
			Cell departmentCell = infoRow.createCell(1);
			departmentCell.setCellValue("部门");
			departmentCell.setCellStyle(cs2);
			Cell januaryCell = infoRow.createCell(2);
			januaryCell.setCellValue("一月");
			januaryCell.setCellStyle(cs2);
			Cell februaryCell = infoRow.createCell(3);
			februaryCell.setCellValue("二月");
			februaryCell.setCellStyle(cs2);
			Cell marchCell = infoRow.createCell(4);
			marchCell.setCellValue("三月");
			marchCell.setCellStyle(cs2);
			Cell aprilCell = infoRow.createCell(5);
			aprilCell.setCellValue("四月");
			aprilCell.setCellStyle(cs2);
			Cell mayCell = infoRow.createCell(6);
			mayCell.setCellValue("五月");
			mayCell.setCellStyle(cs2);
			Cell juneCell = infoRow.createCell(7);
			juneCell.setCellValue("六月");
			juneCell.setCellStyle(cs2);
			Cell julyCell = infoRow.createCell(8);
			julyCell.setCellValue("七月");
			julyCell.setCellStyle(cs2);
			Cell augustCell = infoRow.createCell(9);
			augustCell.setCellValue("八月");
			augustCell.setCellStyle(cs2);
			Cell septemberCell = infoRow.createCell(10);
			septemberCell.setCellValue("九月");
			septemberCell.setCellStyle(cs2);
			Cell octoberCell = infoRow.createCell(11);
			octoberCell.setCellValue("十月");
			octoberCell.setCellStyle(cs2);
			Cell novemberCell = infoRow.createCell(12);
			novemberCell.setCellValue("十一月");
			novemberCell.setCellStyle(cs2);
			Cell decemberCell = infoRow.createCell(13);
			decemberCell.setCellValue("十二月");
			decemberCell.setCellStyle(cs2);
			int rowIndex = 2;
			if(null != list) {
				Iterator<YearAchievement> iter = list.iterator();
				while(iter.hasNext()) {
					YearAchievement ya = iter.next();
					if(null != ya) {
						Row dataRow = sheet.createRow(rowIndex);
						dataRow.setHeightInPoints(30);
						Cell dataNameCell = dataRow.createCell(0);
						dataNameCell.setCellValue(ya.getUser().getName());
						Cell dataDepartmentCell = dataRow.createCell(1);
						dataDepartmentCell.setCellValue(ya.getDepartment().getName());
						Cell dataJanuaryCell = dataRow.createCell(2);
						int score = 0;
						score = (ya.getMonthScoreMap().get(1) == null ? 0 : ya.getMonthScoreMap().get(1));
						dataJanuaryCell.setCellValue(score);
						Cell dataFebruaryCell = dataRow.createCell(3);
						score = (ya.getMonthScoreMap().get(2) == null ? 0 : ya.getMonthScoreMap().get(2));
						dataFebruaryCell.setCellValue(score);
						Cell dataMarchCell = dataRow.createCell(4);
						score = (ya.getMonthScoreMap().get(3) == null ? 0 : ya.getMonthScoreMap().get(3));
						dataMarchCell.setCellValue(score);
						Cell dataAprilCell = dataRow.createCell(5);
						score = (ya.getMonthScoreMap().get(4) == null ? 0 : ya.getMonthScoreMap().get(4));
						dataAprilCell.setCellValue(score);
						Cell dataMayCell = dataRow.createCell(6);
						score = (ya.getMonthScoreMap().get(5) == null ? 0 : ya.getMonthScoreMap().get(5));
						dataMayCell.setCellValue(score);
						Cell dataJuneCell = dataRow.createCell(7);
						score = (ya.getMonthScoreMap().get(6) == null ? 0 : ya.getMonthScoreMap().get(6));
						dataJuneCell.setCellValue(score);
						Cell dataJulyCell = dataRow.createCell(8);
						score = (ya.getMonthScoreMap().get(7) == null ? 0 : ya.getMonthScoreMap().get(7));
						dataJulyCell.setCellValue(score);
						Cell dataAugustCell = dataRow.createCell(9);
						score = (ya.getMonthScoreMap().get(8) == null ? 0 : ya.getMonthScoreMap().get(8));
						dataAugustCell.setCellValue(score);
						Cell dataSeptemberCell = dataRow.createCell(10);
						score = (ya.getMonthScoreMap().get(9) == null ? 0 : ya.getMonthScoreMap().get(9));
						dataSeptemberCell.setCellValue(score);
						Cell dataOctoberCell = dataRow.createCell(11);
						score = (ya.getMonthScoreMap().get(10) == null ? 0 : ya.getMonthScoreMap().get(10));
						dataOctoberCell.setCellValue(score);
						Cell dataNovemberCell = dataRow.createCell(12);
						score = (ya.getMonthScoreMap().get(11) == null ? 0 : ya.getMonthScoreMap().get(11));
						dataNovemberCell.setCellValue(score);
						Cell dataDecemberCell = dataRow.createCell(13);
						score = (ya.getMonthScoreMap().get(12) == null ? 0 : ya.getMonthScoreMap().get(12));
						dataDecemberCell.setCellValue(score);
						rowIndex ++;
					}
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return wb;
	}
}