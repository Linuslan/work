package com.linuslan.oa.common;

import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;

public class IBaseServiceImpl {
	public void valid(Object obj) throws Exception {
		
	}
	
	/**
	 * 
	 * @param obj
	 * @param isAudit 是否审核中, false则当前申请单位未提交状态
	 */
	public void validFlowStatus(Object obj, boolean isAudit) {
		int status = (Integer) BeanUtil.getValue(obj, "status");
		/*
		 * 判断要进行的操作时，该流程的状态是否为true(审核中)还是false(未提交)，从而对相应的流程状态匹配和判断
		 * 如果流程状态和isAudit不一致，则报异常
		 */
		if(isAudit) {
			/*
			 * isAudit=true，但是流程状态不为3，即审核中，则报异常
			 */
			if(status != 3) {
				CodeUtil.throwRuntimeExcep("当前流程状态异常");
			}
		} else {
			/*
			 * isAudit=false，即此时的流程状态应该为未提交，但是如果流程状态status=3(审核中)或者status=4(已结束)
			 * 则流程状态异常，不让提交
			 */
			if(status == 3) {
				CodeUtil.throwRuntimeExcep("当前流程状态为正在审核中，不能重复提交");
			} else if(status == 4) {
				CodeUtil.throwRuntimeExcep("当前流程状态为已完成，不能提交");
			}
		}
	}
}
