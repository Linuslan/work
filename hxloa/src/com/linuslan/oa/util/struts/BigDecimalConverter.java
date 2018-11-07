package com.linuslan.oa.util.struts;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

/**
 * 自定义strus2的BigDecimal类型转换器
 * 可以将前端传过来的string转换成BigDecimal
 * @author LinusLan
 *
 */
public class BigDecimalConverter extends StrutsTypeConverter {

	@Override
	public Object convertFromString(Map arg0, String[] arg1, Class arg2) {
		BigDecimal bd = null;
		try {
			if(BigDecimal.class ==arg2){
	            String bdStr = arg1[0];
	            if(null != bdStr && !"".equals(bdStr.trim())){
	                bd = new BigDecimal(bdStr);
	            }else{
	                //bd = BigDecimal.valueOf(-1);
	            }
	        }
		} catch(Exception ex) {
			bd = BigDecimal.ZERO;
		}
		return bd;
	}

	@Override
	public String convertToString(Map arg0, Object arg1) {
		if(arg1 instanceof BigDecimal){  
            BigDecimal b = new BigDecimal(arg1.toString()).setScale(2,BigDecimal.ROUND_HALF_DOWN);  
            return b.toString();  
        }         
        return arg1.toString();
	}
}
