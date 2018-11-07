package com.linuslan.oa.system.upload.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="upload_file")
public class UploadFile {
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="uploadFileSeq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, name="uploadFileSeq", sequenceName="upload_file_seq")
	private Long id;
	
	@Column(name="file_name")
	private String fileName;
	
	@Column(name="file_path")
	private String filePath;
	
	@Column(name="tb_name")
	private String tbName;
	
	@Column(name="tb_id")
	private Long tbId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTbName() {
		return tbName;
	}

	public void setTbName(String tbName) {
		this.tbName = tbName;
	}

	public Long getTbId() {
		return tbId;
	}

	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}
}
