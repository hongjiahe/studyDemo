package com.hohe.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//import com.gzzl.commons.code.SortTypeEnum;
import com.hohe.importExcel.SortTypeEnum;
//import com.hohe.vo.FileVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.aspose.cells.License;
//import com.aspose.cells.SaveFormat;
//import com.aspose.cells.Workbook;
//import com.gzzl.commons.util.vo.FileVo;
//import com.ucicplatform.office.CvtContainer;
//import com.ucicplatform.office.api.FileType;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName FileUtils
 * @Description 文件处理类
 */
public class FileUtils {
	
	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
//	public static FileVo convertPdf(String fileFullName, InputStream  inputStream) {
//		FileVo fileVo = new FileVo();
//		String filePath = PropertiesUtil.getString("filePath");
//		try {
//			String suffix = fileFullName.substring(fileFullName.lastIndexOf('.') + 1, fileFullName.length()).toLowerCase();
//			String fileName = fileFullName.substring(0, fileFullName.lastIndexOf('.'));
//			if (!new File(filePath).isDirectory()) {
//				new File(filePath).mkdirs();
//			}
//			File pdfFile = new File(filePath + fileName+".pdf");
//			long fileSize = 0;
//			if ("jpg".equals(suffix) || "gif".equals(suffix) || "png".equals(suffix) || "jpeg".equals(suffix)
//					|| "bmp".equals(suffix) || "tif".equals(suffix) || "rtf".equals(suffix) || "ppt".equals(suffix) || "pptx".equals(suffix)) {
//				CvtContainer.getInstance().getConvertor(suffix).convert(inputStream, pdfFile, FileType.PDF);
//			} else if ("xlsx".equals(suffix) || "xls".equals(suffix) || "doc".equals(suffix) || "docx".equals(suffix)) {// office组件
//				FileUtils.excel2pdf(inputStream, pdfFile);
//			} else if ("rar".equals(suffix) || "zip".equals(suffix)) {
//				String[] fileNames = CvtContainer.getInstance().getConvertor(suffix).convert(inputStream,
//						new File(filePath), FileType.PDF);
//			} else {
//				throw new Exception("不支持此文件格式：" + suffix);
//			}
//			fileSize = pdfFile.length();
//			InputStream pdfIn = new FileInputStream(pdfFile);
//			fileVo.setInputStream(pdfIn);
//			fileVo.setFileName(fileName+".pdf");
//			fileVo.setFilePath(filePath + fileName+".pdf");
//			fileVo.setFileSize(fileSize);
//			System.out.println("文件大小为：" + fileSize);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("上传出错", e);
//		}
//		return fileVo;
//	}
//
//	/**
//	 * @Title excel2pdf
//	 * @Description excel转PDF
//	 * @param inputStream excel文件
//     * @param pdfFile pdf文件
//	 */
//	public static void excel2pdf(InputStream inputStream, File pdfFile) {
//		FileOutputStream fileOS = null;
//		try {
//			if (!getLicense()) {
//				return;
//			}
//			Workbook wb = new Workbook(inputStream);
//			fileOS = new FileOutputStream(pdfFile);
//			wb.save(fileOS, SaveFormat.PDF);
//			fileOS.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (null != fileOS) {
//				try {
//					fileOS.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

//	public static boolean getLicense() {
//		boolean result = false;
//		InputStream inputStream = null;
//		try {
//			inputStream = FileUtils.class.getClassLoader().getResourceAsStream("xlsxlicense.xml");
//			License aposeLic = new License();
//			aposeLic.setLicense(inputStream);
//			result = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (null != inputStream) {
//				try {
//					inputStream.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return result;
//	}

    /**
     * 将InputStream写入本地文件
     * @param destination 写入本地目录
     * @param input 输入流
     * @throws IOException IOException
     */
    public static void writeToLocal(String destination,String fileName, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        File t = new File(destination);
        if(!t.exists()) {
        	t.mkdirs();
        }
        FileOutputStream downloadFile = new FileOutputStream(destination+"/"+fileName);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();

    }

	/**
	 * 下载或者预览文件，如果exportFileName不为空则是下载，为空则预览
	 * @param file
	 * @param response
	 * @param exportfileName
	 * @param contentType
	 */
	public static void downloadOrPreview(File file, HttpServletResponse response, String exportfileName, String contentType) {
		BufferedInputStream fis = null;
		OutputStream toClient   = null;
		try {
			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(file.getPath()));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			response.reset();
			if (StringUtils.isNotBlank(contentType)) {
				response.setContentType(contentType);
			} else {
				response.setContentType("application/octet-stream");
			}
			toClient = new BufferedOutputStream(response.getOutputStream());
			//火狐
			response.setCharacterEncoding("utf-8");
			if (StringUtils.isNotBlank(exportfileName)) {
				String fileName  = new String(exportfileName.getBytes("utf-8"), "iso-8859-1");
				if (exportfileName.contains(".pdf")) {
					response.setHeader("Content-disposition", "inline;filename=" + fileName);
				} else {
					response.setHeader("Content-disposition", "attachment;filename=" + fileName);
				}
			}
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			// file.delete();        //将生成的服务器端文件删除
		}catch (IOException ex) {
			ex.printStackTrace();
		}finally{
			if(toClient!=null){
				try {
					toClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void previewPdf(File file, HttpServletResponse response) {
		downloadOrPreview(file, response, null, "application/pdf");
	}

	public static void previewPdf(String filePath, HttpServletResponse response) {
		File file = new File(filePath);
		downloadOrPreview(file, response, null, "application/pdf");
	}

	public static void previewPdf(String filePath, HttpServletResponse response, String exportFileName) {
		File file = new File(filePath);
		downloadOrPreview(file, response, exportFileName, "application/pdf");
	}

	public static void downloadExcel(String filePath, HttpServletResponse response, String exportFileName) {
		File file = new File(filePath);
		downloadOrPreview(file, response, exportFileName, "application/vnd.ms-excel");
	}

	/**
	 * 获取文件夹下的所有文件
	 * @param folder
	 * @return
	 */
	public static List<String> getFilePaths(File folder) {
		List<String> files = new ArrayList<String>();
		File[] subs = folder.listFiles();
		if (subs != null && subs.length > 0) {
			for (int i = 0; i < subs.length; i++) {
				File sub = subs[i];
				if (sub.isFile()) {
					files.add(sub.getPath());
				}
			}
			return files;
		}
		return null;
	}

	/**
	 * 获取文件夹下的所有文件
	 * @param folder
	 * @param sortType 排序类型，ASC升序，DESC降序，null表示默认
	 * @return
	 */
	public static List<String> getFilePaths(File folder, String sortType) {
		List<String> filePaths = getFilePaths(folder);

		if (filePaths == null || filePaths.size() == 0) {
			return filePaths;
		}

		if (StringUtils.isNotBlank(sortType) && sortType.equals(SortTypeEnum.ASC.getCode())){
			// 表示升序
			Collections.sort(filePaths);
		}

		if (StringUtils.isNotBlank(sortType) && sortType.equals(SortTypeEnum.DESC.getCode())){
			// 表示降序
			Collections.sort(filePaths, Collections.<String>reverseOrder());
		}

		return filePaths;
	}

	/**
	 * 获取文件夹下的所有文件
	 * @param folder
	 * @return
	 */
	public static List<File> getFiles(File folder) {
		List<File> files = new ArrayList<File>();
		File[] subs = folder.listFiles();
		if (subs != null && subs.length > 0) {
			for (int i = 0; i < subs.length; i++) {
				File sub = subs[i];
				if (sub.isFile()) {
					files.add(sub);
				}
			}
			return files;
		}
		return null;
	}

	/**
	 * 获取文件夹下的所有文件
	 * @param folder
	 * @param sortType 排序类型，ASC升序，DESC降序，null表示默认
	 * @return
	 */
	public static List<File> getFiles(File folder, String sortType) {
		List<File> files = getFiles(folder);

		if (files == null || files.size() == 0) {
			return files;
		}

		if (StringUtils.isNotBlank(sortType) && sortType.equals(SortTypeEnum.ASC.getCode())){
			// 表示升序
			Collections.sort(files, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
		}

		if (StringUtils.isNotBlank(sortType) && sortType.equals(SortTypeEnum.DESC.getCode())){
			// 表示降序
			Collections.sort(files, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return o2.getName().compareTo(o1.getName());
				}
			});
		}

		return files;
	}
}
