package com.linuslan.oa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.BufferedWriter;  
import java.io.OutputStreamWriter;  
import java.util.List;  
  
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.ParserConfigurationException;  
import javax.xml.transform.OutputKeys;  
import javax.xml.transform.Transformer;  
import javax.xml.transform.TransformerException;  
import javax.xml.transform.TransformerFactory;  
import javax.xml.transform.dom.DOMSource;  
import javax.xml.transform.stream.StreamResult;  
  
import org.apache.commons.io.output.ByteArrayOutputStream;  
import org.apache.poi.hwpf.HWPFDocument;  
import org.apache.poi.hwpf.converter.PicturesManager;  
import org.apache.poi.hwpf.converter.WordToHtmlConverter;  
import org.apache.poi.hwpf.usermodel.Picture;  
import org.apache.poi.hwpf.usermodel.PictureType;  
import org.w3c.dom.Document;

public class OfficeUtil {
	
	private static Logger logger = Logger.getLogger(OfficeUtil.class);
	
	/**
	 * 获取操作excel（2003版）文件的HSSFWorkbook对象
	 * @param path
	 * @return
	 */
	public static HSSFWorkbook readExcel(String path) {
		HSSFWorkbook wb = null;
		try {
			FileInputStream fis = new FileInputStream(path);
			wb = new HSSFWorkbook(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			wb = null;
		}
		return wb;
	}
	
	/**
	 * 获取列值，目前只支持数字和字符串
	 * @param row
	 * @param cellIndex
	 * @return
	 */
	public static Object getCellValue(Row row, int cellIndex) {
		try {
			try {
				return row.getCell(cellIndex).getNumericCellValue();
			} catch(Exception ex) {
				return row.getCell(cellIndex).getStringCellValue();
			}
		} catch(Exception ex) {
			logger.error("获取列值异常, rownum = "+row.getRowNum()+", cellIndex="+cellIndex, ex);
			return null;
		}
		
	}
	
	public static Integer getIntegerCellValue(Row row, int cellIndex) {
		try {
			try {
				return (int) row.getCell(cellIndex).getNumericCellValue();
			} catch(Exception ex) {
				ex.printStackTrace();
				String value = row.getCell(cellIndex).getStringCellValue();
				return Integer.parseInt(value);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			logger.error("获取int型列值异常, rownum = "+row.getRowNum()+", cellIndex="+cellIndex, ex);
			return 0;
		}
		
	}
	
	public static Long getLongCellValue(Row row, int cellIndex) {
		try {
			try {
				return (long) row.getCell(cellIndex).getNumericCellValue();
			} catch(Exception ex) {
				ex.printStackTrace();
				String value = row.getCell(cellIndex).getStringCellValue();
				return Long.parseLong(value);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			logger.error("获取long型列值异常, rownum = "+row.getRowNum()+", cellIndex="+cellIndex, ex);
			return 0l;
		}
		
	}
	
	public static BigDecimal getBigDecimalCellValue(Row row, int cellIndex) {
		try {
			try {
				return new BigDecimal(String.valueOf(row.getCell(cellIndex).getNumericCellValue()));
			} catch(Exception ex) {
				String value = row.getCell(cellIndex).getStringCellValue();
				return new BigDecimal(value);
			}
		} catch(Exception ex) {
			logger.error("获取BigDecimal型列值异常，rownum = "+row.getRowNum()+", cellIndex="+cellIndex, ex);
			return new BigDecimal("0");
		}
	}
	
	public static String getStringCellValue(Row row, int cellIndex) {
		try {
			try {
				return row.getCell(cellIndex).getStringCellValue();
			} catch(Exception ex) {
				return String.valueOf(row.getCell(cellIndex).getNumericCellValue());
			}
		} catch(Exception ex) {
			logger.error("获取String型列值异常，rownum = "+row.getRowNum()+", cellIndex="+cellIndex, ex);
			return "";
		}
	}
	
	/**
	 * 将docx转换成html，支持的并不好
	 * @param fileInName
	 * @throws IOException
	 */
	public static void doGenerateHTMLFile(String fileInName) throws IOException {

		String root = "D:\\Linus\\study\\Java\\wordToHtml";
		String filePath = "D:\\Linus\\study\\Java\\wordToHtml\\"+fileInName+".docx";
		String fileOutName = root + "\\" + fileInName + ".html";

		long startTime = System.currentTimeMillis();

		FileInputStream fis = new FileInputStream(new File(filePath));
		XWPFDocument document = new XWPFDocument(fis);

		XHTMLOptions options = XHTMLOptions.create();// .indent( 4 );
		// Extract image
		File imageFolder = new File(root + "\\images\\" + fileInName);
		options.setExtractor(new FileImageExtractor(imageFolder));
		// URI resolver
		options.URIResolver( new BasicURIResolver(root + "\\images\\" + fileInName));

		OutputStream out = new FileOutputStream(new File(fileOutName));
		XHTMLConverter.getInstance().convert(document, out, options);

		System.out.println("Generate " + fileOutName + " with "
				+ (System.currentTimeMillis() - startTime) + " ms.");
	}
	
	/********************************以下为将.doc的word文件转换成html，转换的更彻底*******************************************/
	public static void writeFile(String content, String path) {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			bw.write(content);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fos != null)
					fos.close();
			} catch (IOException ie) {
			}
		}
	}

	public static String convert2Html(String fileName, final String path)
			throws TransformerException, IOException,
			ParserConfigurationException {
		HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(
				fileName));// WordToHtmlUtils.loadDoc(new
							// FileInputStream(inputFile));
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder()
						.newDocument());
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType,
					String suggestedName, float widthInches, float heightInches) {
				return path+"\\images\\" + suggestedName;
			}
		});
		wordToHtmlConverter.processDocument(wordDocument);
		// save pictures
		List pics = wordDocument.getPicturesTable().getAllPictures();
		if (pics != null) {
			for (int i = 0; i < pics.size(); i++) {
				Picture pic = (Picture) pics.get(i);
				System.out.println();
				try {
					pic.writeImageContent(new FileOutputStream(
							path + "\\images\\"
									+ pic.suggestFullFileName()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		Document htmlDocument = wordToHtmlConverter.getDocument();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(out);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);
		out.close();
		//writeFile(new String(out.toByteArray()), outPutFile);
		return new String(out.toByteArray());
	}
	
	/**
	 * 将word文件转换成html文件
	 * @param file
	 * @param realPath 存放图片的地址
	 * @param path 页面显示的图片访问url地址
	 * @return
	 * @throws TransformerException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static String convert2Html(File file, final String realPath, final String path)
			throws TransformerException, IOException,
			ParserConfigurationException {
		HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(file));// WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder()
						.newDocument());
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType,
					String suggestedName, float widthInches, float heightInches) {
				return path+"/images/" + suggestedName;
			}
		});
		wordToHtmlConverter.processDocument(wordDocument);
		// save pictures
		List pics = wordDocument.getPicturesTable().getAllPictures();
		if (pics != null) {
			for (int i = 0; i < pics.size(); i++) {
				Picture pic = (Picture) pics.get(i);
				System.out.println();
				try {
					pic.writeImageContent(new FileOutputStream(
							realPath + "\\images\\"
									+ pic.suggestFullFileName()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		Document htmlDocument = wordToHtmlConverter.getDocument();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(out);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);
		out.close();
		//writeFile(new String(out.toByteArray()), outPutFile);
		return new String(out.toByteArray(), "UTF-8");
	}
	
	public static void main(String[] args) {
		try {
			//OfficeUtil.convert2Html("D:\\Linus\\study\\Java\\wordToHtml\\humanityRule.doc", "D:\\Linus\\study\\Java\\wordToHtml\\222.html");
			OfficeUtil.convert2Html("D:\\Study\\Java\\wordToHtml\\aaaa.doc", "D:\\Study\\Java\\wordToHtml\\111.html");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
}
