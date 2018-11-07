package com.linuslan.oa.workflow.engine.flow.model;

import java.util.List;

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

import org.hibernate.annotations.Where;

/**
 * 流程节点对象
 * @author LinusLan
 *
 */
@Entity
@Table(name="workflow_node")
public class Node {

	@Id
	@Column(name="id")
	@SequenceGenerator(allocationSize=1, name="workflowNodeSeq", sequenceName="workflow_node_seq")
	@GeneratedValue(generator="workflowNodeSeq", strategy=GenerationType.SEQUENCE)
	private Long id;
	
	/**
	 * 所属的流程
	 */
	@Column(name="flow_id")
	private Long flowId;
	
	/**
	 * 节点名称，例如：rect4
	 */
	@Column(name="name")
	private String name;
	
	@Column(name="type")
	private String type;
	
	@Column(name="text")
	private String text;
	
	@Column(name="attr")
	private String attr;
	
	@OneToMany(targetEntity=Property.class, fetch=FetchType.EAGER)
	@JoinColumn(name="pid", referencedColumnName="id")
	@Where(clause="prop_type='node'")
	private List<Property> props;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public List<Property> getProps() {
		return props;
	}

	public void setProps(List<Property> props) {
		this.props = props;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
}
