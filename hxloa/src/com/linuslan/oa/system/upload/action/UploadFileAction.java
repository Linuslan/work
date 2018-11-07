package com.linuslan.oa.system.upload.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.linuslan.oa.common.BaseAction;
import com.linuslan.oa.system.upload.model.UploadFile;
import com.linuslan.oa.system.upload.service.IUploadFileService;
import com.linuslan.oa.util.CodeUtil;

@Controller
public class UploadFileAction extends BaseAction {
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	private UploadFile uploadFile;
	
	public void del() {
		try {
			if(null == uploadFile || null == uploadFile.getId()) {
				CodeUtil.throwExcep("请选择删除的数据");
			}
			this.uploadFileService.del(uploadFile);
			this.setupSuccessMap("删除成功");
		} catch(Exception ex) {
			this.setupFailureMap("删除文件异常，异常原因："+CodeUtil.getStackTrace(ex));
		}
		this.printResultMap();
	}
	
	public void download() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		try {
			String idStr = request.getParameter("id");
			if(null != idStr && !"".equals(idStr.trim())) {
				Long id = Long.parseLong(idStr.trim());
				UploadFile file = this.uploadFileService.queryById(id);
				if(null != file && null != file.getFilePath() && !"".equals(file.getFilePath().trim())) {
					response.setContentType("application/x-msdownload");//文件下载
					response.addHeader("Content-Disposition","attachment; filename=\""
					+ URLEncoder.encode(file.getFileName(), "UTF-8") + "\"");
					FileInputStream fis = new FileInputStream(new File(file.getFilePath().trim()));
					OutputStream output = response.getOutputStream();
					byte[] temp = new byte[1024];
					while(fis.available() > 0) {
						fis.read(temp, 0, 1024);
						output.write(temp);
					}
					output.flush();
					output.close();
					fis.close();
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public UploadFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(UploadFile uploadFile) {
		this.uploadFile = uploadFile;
	}
}
