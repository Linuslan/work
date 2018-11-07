package com.linuslan.oa.system.department.action;

import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.company.model.Company;
import com.linuslan.oa.system.company.service.ICompanyService;
import com.linuslan.oa.system.department.model.Department;
import com.linuslan.oa.system.department.service.IDepartmentService;
import com.linuslan.oa.util.BeanUtil;
import com.linuslan.oa.util.CodeUtil;
import com.linuslan.oa.util.TreeUtil;
import com.linuslan.oa.workflow.flows.reimburse.model.ReimburseClass;
import com.linuslan.oa.workflow.flows.reimburse.service.IReimburseClassService;

@Controller
public class DepartmentAction extends BaseAction {
	
	@Autowired
	private IDepartmentService departmentService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IReimburseClassService reimburseClassService;
	
	private Department department;
	
	private List<Company> companys;
	
	private List<ReimburseClass> classes;
	
	private ReimburseClass reimburseClass;
	
	public void queryTree() {
		this.resultMap.clear();
		try {
			List<Department> list = this.departmentService.queryAll();
			list = (List<Department>) TreeUtil.buildTree(list);
			JSONArray json = JSONArray.fromObject(list);
			this.resultMap.put("success", true);
			this.resultMap.put("children", json.toString());
			this.resultMap.put("pid", "");
		} catch(Exception ex) {
			ex.printStackTrace();
			this.resultMap.put("success", false);
			this.resultMap.put("msg", ex.getMessage());
		}
		this.printMap(this.resultMap);
	}
	
	public String queryById() {
		try {
			this.companys = this.companyService.queryAllCompanys();
			this.classes = this.reimburseClassService.queryAllReimburseClasss();
			if(null != this.department.getId()) {
				this.department = this.departmentService.queryById(this.department.getId());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return this.returnType;
	}
	
	public void add() {
		try {
			if(null == this.department) {
				CodeUtil.throwExcep("获取数据异常");
			}
			this.departmentService.valid(this.department);
			if(this.departmentService.add(this.department)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(resultMap);
	}
	
	public void update() {
		try {
			if(null == this.department || null == this.department.getId()) {
				CodeUtil.throwExcep("获取数据异常");
			}
			Department persist = this.departmentService.queryById(this.department.getId());
			BeanUtil.updateBean(persist, this.department);
			this.departmentService.valid(persist);
			if(this.departmentService.update(persist)) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getMessage());
		}
		this.printMap(resultMap);
	}
	
	public void del() {
		try {
			if(null == this.department || null == this.department.getId()) {
				CodeUtil.throwExcep("请选择一条数据删除");
			}
			if(this.departmentService.delBatchByPid(this.department.getId())) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
			this.setupFailureMap(ex.getCause().toString());
		}
		this.printMap(resultMap);
	}
	
	public void addClass() {
		try {
			if(null == this.department || null == this.department.getId()) {
				CodeUtil.throwExcep("获取部门异常");
			}
			if(null == this.reimburseClass || null == this.reimburseClass.getId()) {
				CodeUtil.throwExcep("获取报销类别异常");
			}
			if(this.departmentService.addClass(this.department.getId(), this.reimburseClass.getId())) {
				this.setupSimpleSuccessMap();
			} else {
				CodeUtil.throwExcep("保存失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}
	
	public void delClass() {
		try {
			if(null == this.department || null == this.department.getId()) {
				CodeUtil.throwExcep("获取部门异常");
			}
			if(null == this.reimburseClass || null == this.reimburseClass.getId()) {
				CodeUtil.throwExcep("获取报销类别异常");
			}
			if(this.departmentService.delClass(this.department.getId(), this.reimburseClass.getId())) {
				this.setupSuccessMap("删除成功");
			} else {
				CodeUtil.throwExcep("删除失败");
			}
		} catch(Exception ex) {
			this.setupFailureMap(ex.getMessage());
		}
		this.printResultMap();
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Company> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Company> companys) {
		this.companys = companys;
	}

	public List<ReimburseClass> getClasses() {
		return classes;
	}

	public void setClasses(List<ReimburseClass> classes) {
		this.classes = classes;
	}

	public ReimburseClass getReimburseClass() {
		return reimburseClass;
	}

	public void setReimburseClass(ReimburseClass reimburseClass) {
		this.reimburseClass = reimburseClass;
	}
}
