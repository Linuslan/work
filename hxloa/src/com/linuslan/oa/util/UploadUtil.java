package com.linuslan.oa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.linuslan.oa.system.upload.model.UploadFile;
import com.linuslan.oa.workflow.flows.checkin.model.Checkin;

public class UploadUtil {
	
	public static UploadFile upload(File file, String fileName, String dirName, String className) throws RuntimeException {
		UploadFile uploadFile = new UploadFile();
		try {
			String path = PropertyUtil.getConfigPropertyValue(dirName);
			if(CodeUtil.isEmpty(path)) {
				CodeUtil.throwExcep("文件上传路径为空");
			}
			String realPath = HttpUtil.getRealPath(path);
			if(null != file) {
				String realName = null;
				realName = SerialNoFactory.buildRandom(10) + "_" + DateUtil.parseDateToStr(new Date(), "yyyyMMddHHmmss")+ "_" + SerialNoFactory.buildRandom(10);
				String filePath = realPath+"\\"+realName;
				FileOutputStream fos = new FileOutputStream(filePath);
				FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                fis.close();
                fos.close();
                
                uploadFile.setFileName(fileName);
                uploadFile.setFilePath(filePath);
                uploadFile.setTbName(className);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex.getMessage());
		}
		return uploadFile;
	}
	
	/**
	 * 上传文件，并转换成UploadFile对象
	 * @param files
	 * @param fileNames
	 * @return
	 * @throws RuntimeException
	 */
	public static List<UploadFile> uploadBatch(List<File> files, List<String> fileNames, String dirName, String className) throws RuntimeException {
		List<UploadFile> list = new ArrayList<UploadFile> ();
		try {
			String path = PropertyUtil.getConfigPropertyValue(dirName);
			if(CodeUtil.isEmpty(path)) {
				CodeUtil.throwExcep("文件上传路径为空");
			}
			String realPath = HttpUtil.getRealPath(path);
			if(null != files) {
				File file = null;
				String fileName = null;
				String realName = null;
				for(int i = 0; i < files.size(); i ++) {
					file = files.get(i);
					fileName = fileNames.get(i);
					realName = SerialNoFactory.buildRandom(10) + "_" + DateUtil.parseDateToStr(new Date(), "yyyyMMddHHmmss")+ "_" + + SerialNoFactory.buildRandom(10);
					String filePath = realPath+"\\"+realName;
					FileOutputStream fos = new FileOutputStream(filePath);
					FileInputStream fis = new FileInputStream(file);
	                byte[] buffer = new byte[1024];
	                int len = 0;
	                while ((len = fis.read(buffer)) > 0) {
	                    fos.write(buffer, 0, len);
	                }
	                fos.flush();
	                fis.close();
	                fos.close();
	                
	                UploadFile upFile = new UploadFile();
	                upFile.setFileName(fileName);
	                upFile.setFilePath(filePath);
	                upFile.setTbName(className);
	                list.add(upFile);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex.getMessage());
		}
		return list;
	}
	
	/**
	 * 批量删除上传的文件
	 * 当保存失败或者其他情况下，需要将上传的文件删除
	 * @param uploadFiles
	 * @throws RuntimeException
	 */
	public static void delBatch(List<UploadFile> uploadFiles) throws RuntimeException {
		try {
			if(null != uploadFiles) {
				Iterator<UploadFile> iter = uploadFiles.iterator();
				UploadFile file = null;
				while(iter.hasNext()) {
					file = iter.next();
					UploadUtil.del(file);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep(ex.getMessage());
		}
	}
	
	/**
	 * 删除单个文件
	 * @param uploadFile
	 */
	public static void del(UploadFile uploadFile) {
		try {
			if(null != uploadFile) {
				String realPath = uploadFile.getFilePath();
				if(CodeUtil.isNotEmpty(realPath)) {
					File file = new File(realPath);
					if(file.exists()) {
						file.delete();
					}
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
