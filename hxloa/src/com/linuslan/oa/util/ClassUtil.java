package com.linuslan.oa.util;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;

public class ClassUtil {
	
	/**
	 * 通过包名和类名查找类
	 * @param packageName
	 * @param name
	 * @return
	 */
	public static Class getClassByName(String packageName, String name) {
		Class clz = null;
		try {
			String packagePath = packageName.replace(".", "/");
			//得到包所在的实际文件位置
			Enumeration<URL> urls = ClassUtil.class.getClassLoader().getResources(packagePath);
			URL url = null;
			while(urls.hasMoreElements()) {
				url = urls.nextElement();
				String protocol = url.getProtocol();
				if("file".equals(protocol.toLowerCase())) {
					String path = URLDecoder.decode(url.getFile(), "UTF-8");
					clz = ClassUtil.findClassByPackage(packageName, path, name);
					if(null != clz) {
						break;
					}
				}
			}
			if(null == clz) {
				CodeUtil.throwRuntimeExcep("找不到"+name+"类");
			}
		} catch(Exception ex) {
			CodeUtil.throwRuntimeExcep(ex.getMessage());
		}
		return clz;
	}
	
	/**
	 * 通过包名，包所在的实际地址以及类名，一层一层的到文件夹中查找类，直到查找到匹配的类
	 * @param packageName
	 * @param path
	 * @param className
	 * @return
	 */
	private static Class findClassByPackage(String packageName, String path, String className) {
		Class clz = null;
		try {
			File file = new File(path);
			if(!file.exists() || !file.isDirectory()) {
				CodeUtil.throwRuntimeExcep("查找"+className+"类异常，包路径不存在");
			}
			File[] files = file.listFiles(new FileFilter() {

				public boolean accept(File file) {
					return file.isDirectory() || file.getName().endsWith(".class");
				}
				
			});
			for(File sub : files) {
				if(sub.isDirectory()) {
					clz = findClassByPackage(packageName+"."+sub.getName(), sub.getAbsolutePath(), className);
				} else if(className.equals(sub.getName().replaceAll(".class", ""))) {
					String classPath = packageName+"."+sub.getName().replaceAll(".class", "");
					clz = Class.forName(classPath);
				}
				if(null != clz) {
					break;
				}
			}
		} catch(Exception ex) {
			CodeUtil.throwRuntimeExcep(CodeUtil.getStackTrace(ex));
		}
		
		return clz;
	}
}
