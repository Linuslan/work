package com.linuslan.oa.system.capital.action;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.capital.model.Capital;
import com.linuslan.oa.system.capital.service.ICapitalService;
import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.system.department.service.IDepartmentService;
import com.linuslan.oa.system.user.model.User;
import com.linuslan.oa.system.user.service.IUserService;
import com.linuslan.oa.util.DateProcessor;
import com.linuslan.oa.util.DateUtil;
import com.linuslan.oa.util.HttpUtil;
import com.linuslan.oa.util.Page;

@Controller
public class CapitalAction extends BaseAction {
	
	@Autowired
	private ICapitalService capitalService;
	
	@Autowired
	private IDepartmentService departmentService;
	
	@Autowired
	private IUserService userService;
	
	private Page<Capital> pageData;
	
	private int year = DateUtil.getYear(new Date());;
	
	private int month = DateUtil.getMonth(new Date());
	
	private List<String> classNameList;
	
	private List<String> addressList;
	
	private List<String> shopNameList;
	
	private List<Department> departmentList;
	
	private List<User> userList;
	
	private List<String> borrowUserList;
	
	private Capital capital;
	
	/**
	 * 查询我提交的备用金申请
	 * @return
	 */
	public void queryPage() {
		try {
			String date = "";
			try {
				if(null != paramMap) {
					if(null != paramMap.get("date") && !"".equals(paramMap.get("date").trim())) {
						date = paramMap.get("date");
						this.year = DateUtil.getYear(date);
						this.month = DateUtil.getMonth(date);
						paramMap.remove("date");
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			this.pageData = this.capitalService.queryPage(paramMap, this.year, this.month, this.page, this.rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
			/*if(null != paramMap) {
				paramMap.put("date", date);
			}
			//this.shopNameList = this.capitalService.queryShopName();
			//this.addressList = this.capitalService.queryAddress();
			//this.classNameList = this.capitalService.queryClassName();
			//this.borrowUserList = this.capitalService.queryUser();*/
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		//return SUCCESS;
	}
	
	public void initSelect() {
		try {
			List<Map<String, Object>> shopNames = this.capitalService.queryShopName();
			List<Map<String, Object>> addresses = this.capitalService.queryAddress();
			List<Map<String, Object>> classNames = this.capitalService.queryClassName();
			List<Map<String, Object>> users = this.capitalService.queryUser();
			List<Map<String, Object>> departments = this.capitalService.queryDepartment();
			List<Map<String, Object>> borrowDepartments = this.capitalService.queryBorrowDepartment();
			Map<String, List<Map<String, Object>>> maps = new HashMap<String, List<Map<String, Object>>> ();
			maps.put("shopName", shopNames);
			maps.put("address", addresses);
			maps.put("className", classNames);
			maps.put("borrowUser", users);
			maps.put("department", departments);
			maps.put("borrowDepartment", borrowDepartments);
			JSONObject json = JSONObject.fromObject(maps);
			this.printResult(json.toString());
		} catch(Exception ex) {
			
		}
	}
	
	/**
	 * 查询我提交的备用金申请
	 * @return
	 */
	public void queryUserPage() {
		try {
			String date = "";
			try {
				if(null != paramMap) {
					if(null != paramMap.get("date") && !"".equals(paramMap.get("date").trim())) {
						date = paramMap.get("date");
						this.year = DateUtil.getYear(date);
						this.month = DateUtil.getMonth(date);
						paramMap.remove("date");
					} else {
						this.year = DateUtil.getYear(new Date());
						this.month = DateUtil.getMonth(new Date());
						date = this.year+"-"+this.month;
					}
					
				} else {
					this.paramMap = new HashMap<String, String>();
					this.year = DateUtil.getYear(new Date());
					this.month = DateUtil.getMonth(new Date());
					date = this.year+"-"+this.month;
				}
				this.paramMap.put("state", "2");
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			this.pageData = this.capitalService.queryPage(paramMap, this.year, this.month, this.page, this.rows);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor("yyyy-MM-dd"));
			JSONObject json = JSONObject.fromObject(this.pageData, jsonConfig);
			this.printResult(json.toString());
			/*if(null != paramMap) {
				paramMap.put("date", date);
			}*/
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 新增或修改时的初始化内容
	 * @return
	 */
	public String initContent() {
		try {
			if(null != this.capital) {
				if(null != this.capital.getId()) {
					this.capital = this.capitalService.queryById(this.capital.getId());
				}
			}
			this.userList = this.userService.queryAll();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return returnType;
	}
	
	/**
	 * 新增备用金申请
	 */
	public void add() {
		StringBuffer result = new StringBuffer("{'success':");
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try {
			this.capital.setYear(DateUtil.getYear(new Date()));
			this.capital.setMonth(DateUtil.getMonth(new Date()));
			this.capital.setAddId(HttpUtil.getLoginUser().getId());
			this.capital.setAddTime(new Date());
			this.capital = this.capitalService.add(this.capital);
			if(null != this.capital) {
				result.append(true);
				result.append(", 'id': "+this.capital.getId());
			} else {
				result.append(false);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result.append(false);
		}
		result.append("}");
		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(result.toString());
			out.flush();
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 更新备用金
	 */
	public void update() {
		StringBuffer result = new StringBuffer("{'success':");
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			this.capital = this.capitalService.update(this.capital);
			if(null != this.capital) {
				result.append(true);
				result.append(", 'id': "+this.capital.getId());
			} else {
				result.append(false);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result.append(false);
		}
		result.append("}");
		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(result.toString());
			out.flush();
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 通过id删除开票
	 */
	public void delById() {
		StringBuffer result = new StringBuffer("{'success':");
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			if(this.capitalService.delById(this.capital.getId())) {
				result.append(true);
			} else {
				result.append(false);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result.append(false);
		}
		result.append("}");
		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(result.toString());
			out.flush();
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 按部门导出固资
	 */
	public void exportXls() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		try {
			String date = "";
			try {
				if(null != paramMap) {
					if(null != paramMap.get("date") && !"".equals(paramMap.get("date").trim())) {
						date = paramMap.get("date");
						this.year = DateUtil.getYear(date);
						this.month = DateUtil.getMonth(date);
						paramMap.remove("date");
					}
					
					/*
					 * 前端传过来的参数是经过encodeURI($("#capitalSearchForm").serialize())
					 * 编码的，所以需要进行解码
					 */
					Set<String> keySet = paramMap.keySet();
					Iterator<String> keyIter = keySet.iterator();
					while(keyIter.hasNext()) {
						String key = keyIter.next();
						if(null != paramMap.get(key) && !"".equals(paramMap.get(key).trim())) {
							String value = paramMap.get(key);
							value = URLDecoder.decode(value, "UTF-8");
							paramMap.put(key, value);
						}
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			List<Capital> list = this.capitalService.queryCapitals(paramMap, this.year, this.month);
			Map<String, List<Capital>> map = new HashMap<String, List<Capital>> ();
			Iterator<Capital> iter = list.iterator();
			Capital capital = null;
			while(iter.hasNext()) {
				capital = iter.next();
				String name = capital.getDepartment();
				if(null != map.get(name)) {
					map.get(name).add(capital);
				} else {
					List<Capital> capitals = new ArrayList<Capital> ();
					capitals.add(capital);
					map.put(name, capitals);
				}
			}
			HSSFWorkbook wb = new HSSFWorkbook();
			Set<String> keySet = map.keySet();
			if(null != keySet) {
				Iterator<String> keyIter = keySet.iterator();
				String key = null;
				while(keyIter.hasNext()) {
					key = keyIter.next();
					if(null != map.get(key)) {
						Sheet sh = null;
						if(null != key && !"".equals(key.trim())) {
							sh = wb.createSheet(key.trim());
						} else {
							sh = wb.createSheet("其他");
						}
						Row headRow = sh.createRow(0);
						Cell cell = headRow.createCell(0);
						cell.setCellValue("固资类别");
						Cell cell1 = headRow.createCell(1);
						cell1.setCellValue("编号");
						Cell cell2 = headRow.createCell(2);
						cell2.setCellValue("名称");
						Cell cell3 = headRow.createCell(3);
						cell3.setCellValue("型号");
						Cell cell4 = headRow.createCell(4);
						cell4.setCellValue("产家");
						Cell cell5 = headRow.createCell(5);
						cell5.setCellValue("存放地");
						Cell cell6 = headRow.createCell(6);
						cell6.setCellValue("归属部门");
						Cell cell7 = headRow.createCell(7);
						cell7.setCellValue("借用/领用时间");
						Cell cell8 = headRow.createCell(8);
						cell8.setCellValue("借用部门");
						Cell cell9 = headRow.createCell(9);
						cell9.setCellValue("使用方");
						Cell cell10 = headRow.createCell(10);
						cell10.setCellValue("计量单位");
						Cell cell11 = headRow.createCell(11);
						cell11.setCellValue("购置时间");
						Cell cell12 = headRow.createCell(12);
						cell12.setCellValue("购置价格");
						Cell cell13 = headRow.createCell(13);
						cell13.setCellValue("折旧月数");
						Cell cell14 = headRow.createCell(14);
						cell14.setCellValue("月折旧额");
						Cell cell15 = headRow.createCell(15);
						cell15.setCellValue("至今折旧额");
						Cell cell16 = headRow.createCell(16);
						cell16.setCellValue("目前状况");
						Cell cell17 = headRow.createCell(17);
						cell17.setCellValue("净额");
						Cell cell18 = headRow.createCell(18);
						cell18.setCellValue("备注");
						
						List<Capital> capitals = map.get(key);
						Iterator<Capital> capIter = capitals.iterator();
						int i = 1;
						while(capIter.hasNext()) {
							Capital cap = capIter.next();
							Row valueRow = sh.createRow(i);
							Cell className = valueRow.createCell(0);
							className.setCellValue(cap.getClassName());
							Cell serial = valueRow.createCell(1);
							serial.setCellValue(cap.getSerial());
							Cell name = valueRow.createCell(2);
							name.setCellValue(cap.getName());
							Cell model = valueRow.createCell(3);
							model.setCellValue(cap.getModel());
							Cell shopName = valueRow.createCell(4);
							shopName.setCellValue(cap.getShopName());
							Cell address = valueRow.createCell(5);
							address.setCellValue(cap.getAddress());
							Cell department = valueRow.createCell(6);
							department.setCellValue(cap.getDepartment());
							Cell borrowDate = valueRow.createCell(7);
							if(null != cap.getBorrowDate()) {
								borrowDate.setCellValue(DateUtil.parseDateToStr(cap.getBorrowDate(), "yyyy-MM-dd"));
							}
							Cell borrowDepartment = valueRow.createCell(8);
							borrowDepartment.setCellValue(cap.getBorrowDepartment());
							Cell borrowUser = valueRow.createCell(9);
							borrowUser.setCellValue(cap.getBorrowUser());
							Cell unit = valueRow.createCell(10);
							unit.setCellValue(cap.getUnit());
							Cell buyDate = valueRow.createCell(11);
							if(null != cap.getBuyDate()) {
								buyDate.setCellValue(DateUtil.parseDateToStr(cap.getBuyDate(), "yyyy-MM-dd"));
							}
							Cell buyMoney = valueRow.createCell(12);
							buyMoney.setCellValue(cap.getBuyMoney());
							
							//折旧约束
							Cell depreciationYear = valueRow.createCell(13);
							depreciationYear.setCellValue(cap.getDepreciationYear());
							
							//月折旧额
							Cell depreciationMoney = valueRow.createCell(14);
							depreciationMoney.setCellValue(cap.getDepreciationMoney());
							
							//至今折旧额
							Cell depreciation = valueRow.createCell(15);
							depreciation.setCellValue(cap.getDepreciation());
							
							//目前状况
							Cell depreciationState = valueRow.createCell(16);
							depreciationState.setCellValue(cap.getDepreciationState());
							
							//净额
							Cell netmount = valueRow.createCell(17);
							netmount.setCellValue(cap.getNetamount());
							
							Cell memo = valueRow.createCell(18);
							memo.setCellValue(cap.getInfo());
							
							i ++;
						}
					}
				}
			}
			String fileName = URLEncoder.encode("华夏蓝"+year+"年"+month+"月固资.xls", "UTF-8");
			response.addHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.flush();
			out.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public Page<Capital> getPageData() {
		return pageData;
	}

	public void setPageData(Page<Capital> pageData) {
		this.pageData = pageData;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public List<String> getClassNameList() {
		return classNameList;
	}

	public void setClassNameList(List<String> classNameList) {
		this.classNameList = classNameList;
	}

	public List<String> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<String> addressList) {
		this.addressList = addressList;
	}

	public List<String> getShopNameList() {
		return shopNameList;
	}

	public void setShopNameList(List<String> shopNameList) {
		this.shopNameList = shopNameList;
	}

	public List<Department> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<String> getBorrowUserList() {
		return borrowUserList;
	}

	public void setBorrowUserList(List<String> borrowUserList) {
		this.borrowUserList = borrowUserList;
	}

	public Capital getCapital() {
		return capital;
	}

	public void setCapital(Capital capital) {
		this.capital = capital;
	}
}
