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

@Entity
@Table(name="workflow_path")
public class Path {

	@Id
	@Column(name="id")
	@GeneratedValue(generator="workflowPathSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="workflowPathSeq", sequenceName="workflow_path_seq")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	/**
	 * 从哪个节点
	 */
	@Column(name="from_node")
	private String fromNode;
	
	/**
	 * 到哪个节点
	 */
	@Column(name="to_node")
	private String toNode;
	
	/**
	 * 
	 */
	@Column(name="dots")
	private String dots;
	
	/**
	 * 显示的名字
	 */
	@Column(name="text")
	private String text;
	
	/**
	 * 坐标点
	 */
	@Column(name="text_pos")
	private String textPos;
	
	@OneToMany(targetEntity=Property.class, fetch=FetchType.EAGER)
	@JoinColumn(name="pid", referencedColumnName="id")
	@Where(clause="prop_type='path'")
	private List<Property> props;
	
	@Column(name="flow_id")
	private Long flowId;

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

	public String getFromNode() {
		return fromNode;
	}

	public void setFromNode(String fromNode) {
		this.fromNode = fromNode;
	}

	public String getToNode() {
		return toNode;
	}

	public void setToNode(String toNode) {
		this.toNode = toNode;
	}

	public String getDots() {
		return dots;
	}

	public void setDots(String dots) {
		this.dots = dots;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTextPos() {
		return textPos;
	}

	public void setTextPos(String textPos) {
		this.textPos = textPos;
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
