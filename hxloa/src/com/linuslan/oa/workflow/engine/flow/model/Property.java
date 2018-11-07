package com.linuslan.oa.workflow.engine.flow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="workflow_property")
public class Property {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="workflowPropertySeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="workflowPropertySeq", sequenceName="workflow_property_seq")
	private Long id;
	
	/**
	 * 属性名
	 */
	@Column(name="text")
	private String text;
	
	/**
	 * 属性显示出来的名字
	 */
	@Column(name="label")
	private String label;
	
	@Column(name="name")
	private String name;
	
	@Column(name="value")
	private String value;
	
	/**
	 * form类型，是input还是select
	 * 值例如：editor: function() {return new $.myflow.editors.selectEditor([{name:'与', value:'and'}, {name:'或', value: 'or'}])}
	 */
	@Column(name="editor")
	private String editor;
	
	/**
	 * 属性的类型，是node还是path的属性
	 * node: node属性
	 * path: path属性
	 */
	@Column(name="prop_type")
	private String propType;
	
	/**
	 * Node或者Path的主键
	 */
	@Column(name="pid")
	private Long pid;
	
	/**
	 * 附加列，便于删除
	 */
	@Column(name="flow_id")
	private Long flowId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPropType() {
		return propType;
	}

	public void setPropType(String propType) {
		this.propType = propType;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	
}
