package com.linuslan.oa.system.user.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table(name="sys_user")
public class User {
	@Id
	@Column(name="id")
	@GeneratedValue(generator="sysUserSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="sysUserSeq", sequenceName="sys_user_seq")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="login_name")
	private String loginName;
	
	@Column(name="password")
	private String password;
	
	@Column(name="real_password")
	private String realPassword;
	
	@Column(name="email")
	private String email;
	
	@Column(name="telephone")
	private String telephone;
	
	@Column(name="other_phone")
	private String otherPhone;
	
	@Column(name="sex")
	private Integer sex = 0;
	
	@Column(name="qq")
	private String qq;
	
	@Column(name="department_id")
	private Long departmentId;
	
	@Formula("(SELECT t.name FROM sys_department t WHERE t.id=department_id)")
	private String departmentName;
	
	@Column(name="birthday")
	private Date birthday;
	
	@Column(name="address")
	private String address;
	
	@Column(name="register_date")
	private Date registerDate;
	
	@Column(name="is_login")
	private Integer isLogin;
	
	@Column(name="is_delete")
	private Integer isDelete=0;
	
	@Column(name="order_num")
	private Integer orderNum=0;
	
	@Column(name="leader_id")
	private Long leaderId;
	
	@Formula("(SELECT t.name FROM sys_user t WHERE t.id=leader_id)")
	private String leaderName;
	
	@Column(name="group_id")
	private Long groupId;
	
	@Formula("(SELECT t.text FROM sys_group t WHERE t.id=group_id)")
	private String groupText;
	
	/**
	 * 0：离职
	 * 1：在职
	 */
	@Column(name="is_leave")
	private Integer isLeave=1;
	
	@Column(name="employee_no")
	private String employeeNo;
	
	@Column(name="company_id")
	private Long companyId;
	
	@Formula("(SELECT t.name FROM sys_company t WHERE t.id=company_id)")
	private String companyName;
	
	@Column(name="post_id")
	private Long postId;
	
	@Formula("(SELECT t.name FROM sys_post t WHERE t.id=post_id)")
	private String postName;
	
	/**
	 * 离职时间
	 */
	@Column(name="leave_date")
	private Date leaveDate;
	
	/**
	 * 离职原因
	 */
	@Column(name="leave_memo")
	private String leaveMemo;
	
	/**
	 * 在职状态
	 * 0：试用
	 * 1：转正
	 */
	@Column(name="inservice_status")
	private Integer inserviceStatus=0;
	
	/**
	 * 毕业学校
	 */
	@Column(name="graduate_school")
	private String graduateSchool;
	
	/**
	 * 在校所学专业
	 */
	@Column(name="major")
	private String major;
	
	/**
	 * 学历
	 */
	@Column(name="education")
	private String education;
	
	/**
	 * 户籍所在地
	 */
	@Column(name="domicile_place")
	private String domicilePlace;
	
	/**
	 * 是否结婚，0：未婚；1：已婚
	 */
	@Column(name="matrimony")
	private Integer matrimony=0;
	
	/**
	 * 身份证号
	 */
	@Column(name="id_number")
	private String idNumber;
	
	/**
	 * 联系地址
	 */
	@Column(name="contact_address")
	private String contactAddress;
	
	@Column(name="hiredate")
	private Date hiredate;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealPassword() {
		return realPassword;
	}

	public void setRealPassword(String realPassword) {
		this.realPassword = realPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getOtherPhone() {
		return otherPhone;
	}

	public void setOtherPhone(String otherPhone) {
		this.otherPhone = otherPhone;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Integer getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(Integer isLogin) {
		this.isLogin = isLogin;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Long getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(Long leaderId) {
		this.leaderId = leaderId;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupText() {
		return groupText;
	}

	public void setGroupText(String groupText) {
		this.groupText = groupText;
	}

	public Integer getIsLeave() {
		return isLeave;
	}

	public void setIsLeave(Integer isLeave) {
		this.isLeave = isLeave;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getLeaveMemo() {
		return leaveMemo;
	}

	public void setLeaveMemo(String leaveMemo) {
		this.leaveMemo = leaveMemo;
	}

	public Integer getInserviceStatus() {
		return inserviceStatus;
	}

	public void setInserviceStatus(Integer inserviceStatus) {
		this.inserviceStatus = inserviceStatus;
	}

	public String getGraduateSchool() {
		return graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getDomicilePlace() {
		return domicilePlace;
	}

	public void setDomicilePlace(String domicilePlace) {
		this.domicilePlace = domicilePlace;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public Integer getMatrimony() {
		return matrimony;
	}

	public void setMatrimony(Integer matrimony) {
		this.matrimony = matrimony;
	}

	public Date getHiredate() {
		return hiredate;
	}

	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}
}
