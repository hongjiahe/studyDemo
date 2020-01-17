package com.hohe.importExcel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hohe.util.FilePropertyUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.vo.BaseEntityTypeConstants;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Excel组件
 * @author 
 * @version 2.0
 */
@SuppressWarnings({"resource","rawtypes"})
public class ExcelHelper {
	//支持30位小数位
	public static final String EXPORT_NUMFORMAT_DEFAULT_30 = "0.##############################";
	/**
	 * 读取Excel内容
	 * @param file 文件
	 * @param sheetNum Sheet下标
	 * @param startRow 第几行开始
	 * @param columnNames 字段定义
	 * @throws IOException
	 * @throws ZLException
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @return List
	 */
	public static List<Map<String, Object>> exportListFromExcel(File file, int sheetNum, int startRow,
			List<String> columnNames) throws IOException, ZLException, EncryptedDocumentException, InvalidFormatException {
		Workbook workbook = WorkbookFactory.create(file);
		Sheet sheet = workbook.getSheetAt(sheetNum);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int maxRowIx = sheet.getLastRowNum();
		for (int rowIx = startRow; rowIx <= maxRowIx; rowIx++) {
			Row row = sheet.getRow(rowIx);
			if(row==null) continue;
			short minColIx = row.getFirstCellNum();
			short maxColIx = row.getLastCellNum();
			if(maxColIx!=columnNames.size()) {
				throw new ZLException("Excel中的Title与模板不匹配");
			}
			Map<String, Object> result = new HashMap<String, Object>();
			for (short colIx = minColIx; colIx < maxColIx; colIx++) {
				String val = getCellStrVal(evaluator, row, new Integer(colIx));
				result.put(columnNames.get(colIx),val);
			}
			list.add(result);
		}
		return list;
	}
	
	public static Object getCellValue(FormulaEvaluator evaluator, Row row, int index){
		Object obj = new Object();
		Cell cell = row.getCell(index);
		CellValue cellValue = evaluator.evaluate(cell);
		if (cellValue == null) {
			obj = null;
		}else {
			switch (cellValue.getCellType()) {
			case 4://CellType.BOOLEAN
				obj = cellValue.getBooleanValue();
				break;
			case 0://CellType.NUMERIC
				if (DateUtil.isCellDateFormatted(cell)) {
					obj = cell.getDateCellValue();
				} else if (cell.getCellStyle().getDataFormat() == 58) {
					double value = cell.getNumericCellValue();
					Date date = DateUtil.getJavaDate(value);
					obj = date;
				} else {
					obj = cell.getNumericCellValue();
				}
				break;
			case 1://CellType.STRING
				obj = cellValue.getStringValue();
				break;
			default:
				break;
			}
		}
		return obj;
	}
	
	public static String getCellStrVal(FormulaEvaluator evaluator, Row row, int index){
		String str = null;
		Object obj = getCellValue(evaluator, row, index);
		if(obj instanceof String){
        	str = obj.toString();
        }else if(obj instanceof Double){
        	DecimalFormat df = new DecimalFormat("##################.####");
        	str = df.format(obj);
        	str = killZero(str);
        }else if(obj instanceof Date) {
        	Date date = (Date) obj;
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        	str = format.format(date);
        }else if(obj!=null){
        	str = obj.toString();
        }
		str = str==null?str:str.trim();
		return str;
	}
	
	public static String killZero(String str){
		if(StringUtils.isNotBlank(str) && str.contains(".")){
			str = str.replaceAll("0*$", "");
			str = str.replaceAll("\\.*$", "");
		}
		return str;
	}
	
	public static String generateExcelFromListToTemp(String fileName, List<String> titles, 
			List<String> keys, List<Map<String, Object>> data) {
		String path = generatExcelFromList(FilePropertyUtil.getWebTempPath(), titles, keys, data, fileName);
		return StringUtils.isBlank(path)?null:FilePropertyUtil.getWebTempUrl()+path;
	}
	
	/** 根据给定的数据List生成一个Excel文件,返回Excel文件的路径 **/
	//此方法负载之下有问题
	public static String generateExcelFromList(HttpServletRequest request, List<String> titles, 
			List<String> keys, List<Map<String, Object>> data) {
		if(request==null) {
			return null;
		}
		String filePath = request.getSession().getServletContext().getRealPath("");
		return generatExcelFromList(filePath, titles, keys, data, "error.xls");
	}
	
	public static String generatExcelFromList(String filePath, List<String> titles, List<String> keys, 
			List<Map<String, Object>> data, String fileName){
		String result = null;
		WritableWorkbook workbook = null;
		WritableSheet sheet = null;
		try {
			if(CollectionUtils.isNotEmpty(titles) && CollectionUtils.isNotEmpty(keys) && 
				(titles.size()==keys.size()) && CollectionUtils.isNotEmpty(data) ){
				//0.设置Excel文件输出路径
				String realPath = File.separator+"file"+File.separator+"export"+File.separator+"error"+File.separator;
				String path = filePath+realPath;
				//String path = "F:/ABC/cba/";
				File file = new File(path+fileName);
				//如果当前文件路径未创建,则先创建对应保存目录
				if(!file.getParentFile().exists()){
					file.mkdirs();
				}
				//如果当前文件已存在,则认为是上次保留的Excel文件,删除
				if(file.exists()){
					file.delete();
				}
				//1.创建工作簿
				workbook = jxl.Workbook.createWorkbook(file);
				//2.创建工作表Sheet
				sheet = workbook.createSheet("Sheet1", 0);
				
				//设置纵横打印（默认为纵打）、打印纸
				jxl.SheetSettings sheetset = sheet.getSettings();  
				sheetset.setProtected(false); 
				
				//设置三种单元格样式
				WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);  
				WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);
				//单元格样式:标题居中
				WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);  
				wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条  
				wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
				wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐  
				wcf_center.setWrap(false); // 文字是否换行 
				//单元格样式:正文居左
				WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);  
				wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条  
				wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
				wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐  
				wcf_left.setWrap(false); // 文字是否换行 
				
				//3.填充 标题栏内容
				int size = titles.size();
				for(int i=0; i<size; i++){
					String tt = titles.get(i);
					sheet.addCell(new Label(i, 0, tt,wcf_center));
				}
				
				//4.填充 正文内容
				int length = data.size();
				for(int j=0; j<length; j++){
					Map<String, Object> map = data.get(j);
					if(map!=null && !map.isEmpty()){
						//sheet.addCell(new Label(j, i,va.toString(),wcf_left)); 
						for(int k=0; k<size; k++){
							String key="";
							if(k>=2){
								key = keys.get(k)+"_value";
							}else{
								key = keys.get(k);
							}
							if(StringUtils.isNotBlank(key)){
								String content = "";
								if(map.containsKey(key)){
									Object o = map.get(key);
									if(o!=null){
										content = o+"";
									}
								}
								sheet.addCell(new Label(k, j+1, content, wcf_left)); 
							}
						}
					}
				}
				workbook.write();//写入数据
				result = realPath+fileName;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} finally{
			if(workbook!=null){
				try {
					workbook.close();
					workbook = null;
				} catch (WriteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @Title commonExcelExport 
	 * @Description 通用Excel2007导出(xlsx)
	 * @param  excelPath Excel路径 eg:request.getSession().getServletContext().getRealPath("")+File.separator+"file"+File.separator+"errorExport"+File.separator+xx.xlsx
	 * @param  title     标题栏          eg:[标题1,标题2,标题3]
	 * @param  keys      键值栏          eg:[aaaa,bbbb,cccc]
	 * @param  data      数据栏          eg:[{"aaaa":XX1,"bbbb":YY1,"cccc":ZZ1},{"aaaa":XX2,"bbbb":YY2,"cccc":ZZ2},{"aaaa":XX3,"bbbb":YY3,"cccc":ZZ3}]
	 * @return 错误信息          如果为空表示正常导出,没有发生错误
	 */
	public static String commonExcelExport(String excelPath, List<String> title, List<String> keys, List<Map<String, Object>> data){
		//错误提示:(非空则表示有错误)
		String tips = null;
		SXSSFWorkbook workBook = new SXSSFWorkbook(-1);//设置最大行数，如果不想做限制可以设置为-1
		FileOutputStream out = null;
		//WritableSheet sheet = null;
		try {
			if(StringUtils.isNotBlank(excelPath) && title!=null && title.size()>0 && keys!=null && keys.size()>0 && (title.size()==keys.size()) && data!=null && data.size()>0){
				//0.设置Excel文件输出路径
				//String path = request.getSession().getServletContext().getRealPath("")+File.separator+"file"+File.separator+"errorExport"+File.separator+"xx.xls";
				File file = new File(excelPath);
				//如果当前文件路径未创建,则先创建对应保存目录
				if(!file.getParentFile().exists()){
					file.mkdirs();
				}
				//如果当前文件已存在,则认为是上次保留的Excel文件,删除
				if(file.exists()){
					file.delete();
				}
				//创建文件输出流
				out = new FileOutputStream(excelPath);
				
				//标题样式
				CellStyle titleStyle = workBook.createCellStyle();  
		        Font titleFont  = workBook.createFont();   
		        //titleFont.setFontHeightInPoints((short) 12);//字号         
		        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);//加粗     
		        //titleFont.setFontName("微软雅黑"); // 将“微软雅黑”字体应用到当前单元格上  
		        //titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());//背景颜色  
		        //titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  
		        titleStyle.setFont(titleFont);
		        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//内容左右居中       
		        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//内容上下居中
		        //titleStyle.setWrapText(false);//文字是否换行
		        //正文样式
		        CellStyle contentStyle = workBook.createCellStyle();  
		        Font contentFont  = workBook.createFont();   
		        //contentFont.setFontHeightInPoints((short) 12);//字号         
		        //contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);//不加粗     
		        //contentFont.setFontName("黑体"); // 将“黑体”字体应用到当前单元格上  
		        //contentStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());//背景颜色  
		        //contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  
		        contentStyle.setFont(contentFont);
		        contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//内容左右居中       
		        contentStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//内容上下居中
		        //contentStyle.setWrapText(false);//文字是否换行
		        
				//数据记录总数
				int length = data.size();
				//每个Sheet最多展示多少条数据(默认每个Sheet页展示100万条记录)
				int pageSize  = 1000000;
				int pageCount = (length>0?(length%pageSize!=0?(length/pageSize+1):(length/pageSize)):0); 
				if(pageCount>0){
					//1.创建工作簿
					for(int mm=0; mm<pageCount; mm++){
						int pageStart = mm*pageSize;
						int pageEnd   = ((mm+1)*pageSize>length?length:((mm+1)*pageSize));
						
						//2.创建工作表Sheet
						Sheet sheet = workBook.createSheet("Sheet"+(mm+1));
						
						//3.填充 标题栏内容
						int size = title.size();
						Row titleRow = sheet.createRow(0);
						Cell titleCell = null;
						for(int i=0; i<size; i++){
							String tt = title.get(i);
							//设置每一列的宽度,注意 要乘以256,因为1代表excel中一个字符的1/256的宽度 )
							sheet.setColumnWidth(i, 22 * 256);
							titleCell = titleRow.createCell(i);
							titleCell.setCellValue(tt);
							titleCell.setCellStyle(titleStyle);
						}
						
						//4.填充 正文内容
						int  columnIndex = 1;
						Row  contentRow  = null;
						Cell contentCell = null;
						for(int j=pageStart; j<pageEnd; j++,columnIndex++){
							Map<String, Object> map = data.get(j);
							if(map!=null && !map.isEmpty()){
								//sheet.addCell(new Label(j, i,va.toString(),wcf_left)); 
								contentRow  = sheet.createRow(columnIndex);
								for(int k=0; k<size; k++){
									String key=keys.get(k);
									
									if(StringUtils.isNotBlank(key)){
										String content = "";
										if(map.containsKey(key)){
											Object o = map.get(key);
											if(o!=null){
												content = o+"";
											}
										}
										contentCell = contentRow.createCell(k);
										contentCell.setCellValue(content);
										contentCell.setCellStyle(contentStyle);
										//sheet.addCell(new Label(k, columnIndex, content, wcf_left));
									}
								}
								
								//每次写入1000条记录
								if(columnIndex%1000==0){
									((SXSSFSheet) sheet).flushRows(); //flushRows(0)表示刷新写入所有
								}
								
							}
						}
					}
					//写入数据
					workBook.write(out);
					//System.err.println("处理完成");
				}else{
					tips = "请注意:当前没有需要导出的数据!";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(out!=null){
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return tips;
	}
	
	/**
	 * 
	 * @Title commonExcelExport 
	 * @Description 通用Excel2003导出
	 * @param  excelPath Excel路径 eg:request.getSession().getServletContext().getRealPath("")+File.separator+"file"+File.separator+"errorExport"+File.separator+xx.xls
	 * @param  title     标题栏          eg:[标题1,标题2,标题3]
	 * @param  keys      键值栏          eg:[aaaa,bbbb,cccc]
	 * @param  data      数据栏          eg:[{"aaaa":XX1,"bbbb":YY1,"cccc":ZZ1},{"aaaa":XX2,"bbbb":YY2,"cccc":ZZ2},{"aaaa":XX3,"bbbb":YY3,"cccc":ZZ3}]
	 * @return 错误信息          如果为空表示正常导出,没有发生错误
	 */
	public static String commonExcelExport2(String excelPath, List<String> title, List<String> keys, List<Map<String, Object>> data){
		//错误提示:(非空则表示有错误)
		String tips = null;
		WritableWorkbook workbook = null;
		//WritableSheet sheet = null;
		try {
			if(StringUtils.isNotBlank(excelPath) && title!=null && title.size()>0 && keys!=null && keys.size()>0 && (title.size()==keys.size()) && data!=null && data.size()>0){
				//0.设置Excel文件输出路径
				//String path = request.getSession().getServletContext().getRealPath("")+File.separator+"file"+File.separator+"errorExport"+File.separator+"xx.xls";
				File file = new File(excelPath);
				//如果当前文件路径未创建,则先创建对应保存目录
				if(!file.getParentFile().exists()){
					file.mkdirs();
				}
				//如果当前文件已存在,则认为是上次保留的Excel文件,删除
				if(file.exists()){
					file.delete();
				}
				
				//设置单元格样式
				WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);  
				WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);
				//单元格样式:标题居中
				WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);  
				wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条  
				wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
				wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐  
				wcf_center.setWrap(false); // 文字是否换行 
				//单元格样式:正文居左
				WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);  
				wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条  
				wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
				wcf_left.setAlignment(Alignment.CENTRE); // 文字水平对齐  
				wcf_left.setWrap(false); // 文字是否换行 
				
				//数据记录总数
				int length = data.size();
				//每个Sheet最多展示多少条数据(默认每个Sheet页展示3万条记录)
				int pageSize  = 30000;
				int pageCount = (length>0?(length%pageSize!=0?(length/pageSize+1):(length/pageSize)):0); 
				if(pageCount>0){
					//1.创建工作簿
					workbook = jxl.Workbook.createWorkbook(file);
					for(int mm=0; mm<pageCount; mm++){
						int pageStart = mm*pageSize;
						int pageEnd   = ((mm+1)*pageSize>length?length:((mm+1)*pageSize));
						
						//2.创建工作表Sheet
						WritableSheet sheet = workbook.createSheet("Sheet"+(mm+1), mm);
						//设置纵横打印（默认为纵打）、打印纸
						jxl.SheetSettings sheetset = sheet.getSettings();  
						sheetset.setProtected(false); 
						
						//3.填充 标题栏内容
						//设置第一行的行高
						sheet.setRowView(0, 360);
						int size = title.size();
						for(int i=0; i<size; i++){
							String tt = title.get(i);
							sheet.addCell(new Label(i, 0, tt,wcf_center));
							//设置每一列的宽度
							sheet.setColumnView(i, 16);
						}
						
						//4.填充 正文内容
						int columnIndex = 1;
						for(int j=pageStart; j<pageEnd; j++,columnIndex++){
							Map<String, Object> map = data.get(j);
							if(map!=null && !map.isEmpty()){
								//sheet.addCell(new Label(j, i,va.toString(),wcf_left)); 
								for(int k=0; k<size; k++){
									String key=keys.get(k);
									if(StringUtils.isNotBlank(key)){
										String content = "";
										if(map.containsKey(key)){
											Object o = map.get(key);
											if(o!=null){
												content = o+"";
											}
										}
										sheet.addCell(new Label(k, columnIndex, content, wcf_left));
									}
								}
								
							}
						}
					}
					//写入数据
					workbook.write();
					//System.err.println("处理完成");
				}else{
					tips = "请注意:当前没有需要导出的数据!";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} finally{
			if(workbook!=null){
				try {
					workbook.close();
					workbook = null;
				} catch (WriteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return tips;
	}

	/*public static String getEasyPoiPath() {
		String path = FilePropertyUtil.getTemPath() + File.separator + "file" + File.separator + "easypoiExample"
				+ File.separator + "scheduleReport" + File.separator;
		return path;
	}
	
	
	public static String commonExcelExportEasyPoi(String fileName, List<String> title, List<String> keys, List<Map<String, Object>> data){
		String servletPath = FilePropertyUtil.getTemPath() + File.separator + "file" + File.separator + "easypoiExample"
				+ File.separator + "scheduleReport" + File.separator;
		
		String destFilePath = servletPath + fileName + System.currentTimeMillis() + ".xls";
		
		return commonExcelExportEasyPoi(servletPath,destFilePath, title, keys, data);
	}*/
	/**
	 * 
	 * @Title commonExcelExportEasyPoi 
	 * @Description 通用Excel2003导出(xls)
	 * @param  fileName 文件名
	 * @param  title     标题栏          eg:[标题1,标题2,标题3]
	 * @param  keys      键值栏          eg:[aaaa,bbbb,cccc]
	 * @param  data      数据栏          eg:[{"aaaa":XX1,"bbbb":YY1,"cccc":ZZ1},{"aaaa":XX2,"bbbb":YY2,"cccc":ZZ2},{"aaaa":XX3,"bbbb":YY3,"cccc":ZZ3}]
	 * @return 错误信息          如果为空表示正常导出,没有发生错误
	 */
	public static String commonExcelExportEasyPoi(String servletPath,String destFilePath, List<String> title, List<String> keys, List<Map<String, Object>> data){
		//Sheet a = null;
		List<ExcelExportEntity> colList = new ArrayList<ExcelExportEntity>();
		ExcelExportEntity colEntity = null;
		for (int i = 0; i < title.size(); i++) {
			String labtitle = title.get(i);
			String key = keys.get(i);
			colEntity = new ExcelExportEntity(labtitle, key);
			colList.add(colEntity);
		}
		
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(null, "Sheet1"), colList, data);
        FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(destFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			File file = new File(servletPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			try {
				fos = new FileOutputStream(destFilePath);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				throw new RuntimeException(e1);
			}
		}
        try {
			workbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
        try {
			fos.close();
			return URLEncoder.encode(destFilePath,"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * EXPORT BIG EXCEL
	 * 		生成文件到FilePropertyUtil.getTemPath()目录下
	 * 
	 * @param fileName 要生成的文件名 【请自行确保唯一】
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String downloadBigExcel(String fileName, Map<String, List> data) throws ApiException {
		return downloadBigExcel(fileName, FilePropertyUtil.getWebTempPath(), data);
	}

	/**
	 * EXPORT BIG EXCEL
	 * 		生成文件到FilePropertyUtil.getTemPath()目录下
	 *
	 * @param fileName 要生成的文件名 【请自行确保唯一】
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String downloadBigExcel(String fileName, List<ExcelExportEntity> entityList, List data, String sheetName) throws ApiException {
		return downloadBigExcel(fileName, FilePropertyUtil.getWebTempPath(), entityList,  data, sheetName);
	}
	
	/**
	 * 生成文件到filePath目录下
	 * 
	 * @param fileName 要生成的文件名 【请自行确保唯一】
	 * @param filePath
	 * @param data
	 * @return
	 * @throws ApiException
	 */
	public static String downloadBigExcel(String fileName, String filePath, Map<String, List> data) throws ApiException {
		String servletPath = filePath + File.separator + "file"+ File.separator + "easypoiExample" + File.separator;
		String destFilePath = servletPath + fileName;
		String returnPath = "file"+ File.separator + "easypoiExample" + File.separator + fileName;
		ExportParams params = null;
		Workbook book = null;
		File fileParent = new File(servletPath);
		if(!fileParent.exists()){
			fileParent.mkdirs();
		}
		if(data!=null && !data.isEmpty()) {
			for(String sheetName : data.keySet()) {
				params = new ExportParams(null, sheetName);
				List list = data.get(sheetName);
				if (CollectionUtils.isEmpty(list)) {
					continue;
				}
				if(book==null) {
					book = HLExcelExportUtil.exportBigExcel(params, list.get(0).getClass(), list);
				}else{
					book = HLExcelExportUtil.exportBigExcel(params, list.get(0).getClass(), list, book);
				}
				HLExcelExportUtil.closeExportBigExcel();
			}
		}
		if(book==null) {
			throw new ApiException("暂无数据");
		}
		try (OutputStream tempout= new FileOutputStream(destFilePath)){
			book.write(tempout);
			returnPath = FilePropertyUtil.getWebTempUrl()+"/"+returnPath;
			return URLEncoder.encode(returnPath,"UTF-8");
		}catch (FileNotFoundException e) {
			throw new ApiException("文件生成失败");
		}catch(IOException e) {
			throw new ApiException("文件生成失败");
		} finally {
			if(book!=null) {
				try {
					book.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 生成文件到WEBROOT目录下
	 *
	 * @param fileName 要生成的文件名 【请自行确保唯一】
	 * @param filePath
	 * @param data
	 * @return
	 * @throws ApiException
	 */
	
	public static String downloadBigExcel(String fileName, String filePath, List<ExcelExportEntity> entityList, List datas, String sheetName) throws ApiException {
		String servletPath = filePath + File.separator + "file"+ File.separator + "easypoiExample" + File.separator;
		String destFilePath = servletPath + fileName;
		String returnPath = "file"+ File.separator + "easypoiExample" + File.separator + fileName;
		ExportParams params = null;
		Workbook book = null;
		File fileParent = new File(servletPath);
		if(!fileParent.exists()){
			fileParent.mkdirs();
		}
		if(datas != null && !datas.isEmpty()) {
			params = new ExportParams(null, sheetName);
			params.setType(ExcelType.XSSF);
			book = ExcelExportUtil.exportBigExcel(params, entityList, datas);
			if(book==null) {
				throw new ApiException("暂无数据");
			}
			//解决数字类型numformat设置无效的问题
			Sheet sheet = book.getSheetAt(0);
			if (sheet != null && sheet.getLastRowNum() >= 1) {
				for (int i = 0; i < entityList.size(); i++) {
					if (StringUtils.isNotBlank(entityList.get(i).getNumFormat()) && BaseEntityTypeConstants.DOUBLE_TYPE == entityList.get(i).getType()) {
						CellStyle cellStyle = book.createCellStyle();
						cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(entityList.get(i).getNumFormat()));
						for (int j = 1; j <= sheet.getLastRowNum(); j++) {
							if (sheet.getRow(j) != null && sheet.getRow(j).getCell(i) != null) {
								sheet.getRow(j).getCell(i).setCellStyle(cellStyle);
							}
						}
					}
				}
			}
			
			ExcelExportUtil.closeExportBigExcel();
		}
		try (OutputStream tempout= new FileOutputStream(destFilePath)){
			book.write(tempout);
			returnPath = FilePropertyUtil.getWebTempUrl()+"/"+returnPath;
			return URLEncoder.encode(returnPath,"UTF-8");
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new ApiException("文件生成失败");
		}catch(IOException e) {
			e.printStackTrace();
			throw new ApiException("文件生成失败");
		} finally {
			if(book!=null) {
				try {
					book.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 *  动态生成下载样本
	 * @param template
	 * @param os
	 * @throws IOException
	 */
	public static void createTemplate(SelfDefinedTemplate template,OutputStream os) throws IOException {
		if (template == null) return;
		
		String sheetName = "Sheet1";
		if (StringUtils.isNotBlank(template.getSheetName())) {
			sheetName = template.getSheetName();
		}
	
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		
		// 生成一个字体
		HSSFFont redFont = wb.createFont();
		redFont.setColor(HSSFColor.RED.index);// HSSFColor.VIOLET.index//字体颜色
		redFont.setFontName("宋体");
		redFont.setFontHeightInPoints((short) 9);
		redFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 字体增粗
		
		HSSFCellStyle redStyle = wb.createCellStyle(); // 样式对象
		redStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		redStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		/** 字体begin */
		redStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		redStyle.setFont(redFont);
		
		//黑字体
		HSSFFont blackFont = wb.createFont();
		blackFont.setColor(HSSFColor.BLACK.index);// HSSFColor.VIOLET.index//字体颜色
		blackFont.setFontName("宋体");
		blackFont.setFontHeightInPoints((short) 9);
		blackFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 字体增粗
		
		HSSFCellStyle blackStyle = wb.createCellStyle(); // 样式对象
		blackStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		blackStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		/** 字体begin */
		blackStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		blackStyle.setFont(blackFont);
		
		List<SelfDefinedCell> sdcList = template.getTitleList();
		
		HSSFRow row = sheet.createRow(0);
		if (sdcList != null && !sdcList.isEmpty()) {
			for (int i = 0; i < sdcList.size(); i++) {
				SelfDefinedCell  sdc = sdcList.get(i);
				
				HSSFCell ce = row.createCell(i);
				ce.setCellValue(sdc.getName());
				if (sdc.getIsRequire()) {
					ce.setCellStyle(redStyle);
				} else {
					ce.setCellStyle(blackStyle);
				}
			}
		}
		
		//样例数据
		List<List<Object>> dataList = template.getDataList();
		if (dataList != null && !dataList.isEmpty()) {
			for (int i = 0; i < dataList.size(); i++) {
				List<Object> data = dataList.get(i);
				HSSFRow dataRow = sheet.createRow((i+1));
				if (data != null && !data.isEmpty()) {
					for (int j = 0; j < data.size(); j++) {
						HSSFCell ce = dataRow.createCell(j);
						ce.setCellValue(String.valueOf(data.get(j)));
					}
				}
			}
		}
		
		wb.write(os);
		wb.close();
		os.flush();
	}
	
	
	public static void exportExcel(HttpServletResponse response,String orignFile,String excelName,Map<String, Object> beanParams) {
		OutputStream outputStream = null;
		XLSTransformer transformer = new XLSTransformer();
		try (InputStream in = new BufferedInputStream(new FileInputStream(orignFile))){
			Workbook workbook = transformer.transformXLS(in, beanParams);
			excelName = URLEncoder.encode(excelName, "UTF-8");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("content-disposition", "attachment;filename*=utf-8'zh_cn'" + excelName + ".xls");
			outputStream = response.getOutputStream();
			workbook.write(outputStream);// 将内容写入输出流并把缓存的内容全部发出去
			outputStream.flush();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//表单导出
	public static void exportBigExcel(HttpServletRequest request, HttpServletResponse response,
			Class<?> classinfo,List<?> dataList,String exportfileName,String sheetName) throws IOException{
		ExportParams paramsifno = new ExportParams(null, sheetName);
		Workbook workbook = ExcelExportUtil.exportBigExcel(paramsifno, classinfo, dataList);
		ExcelExportUtil.closeExportBigExcel();
		String fileName = exportfileName+".xlsx";
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setHeader("content-Type", "application/vnd.ms-excel");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();// 刷新流
        outputStream.close();// 关闭流
	}
	
	private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("content-Type", "application/vnd.ms-excel");
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	/**
	 * EXPORT BIG EXCEL
	 * @param fileName 要生成的文件名
	 * @param data
	 */
	public static void exportBigExcelMoreSheet(HttpServletResponse response,
			String fileName, Map<String, List> data) {
		ExportParams params = null;
		Workbook book = null;
		if (data != null && !data.isEmpty()) {
			for (String sheetName : data.keySet()) {
				params = new ExportParams(null, sheetName);
				List list = data.get(sheetName);
				if (list == null || list.size() == 0) {
					continue;
				}
				if (book == null) {
					book = HLExcelExportUtil.exportBigExcel(params, list.get(0).getClass(), list);
				} else {
					//book = HLExcelExportUtil.exportBigExcel(params, list.get(0).getClass(), list);
					book = HLExcelExportUtil.exportBigExcel(params, list.get(0).getClass(), list, book);
				}
				HLExcelExportUtil.closeExportBigExcel();
			}
		}

		downLoadExcel(fileName + ".xlsx", response, book);
	}
	
	public static String xlsTransformer(Map<String, Object> beanParams, String fileName, String filePatch)
			throws ApiException {
		try {
			XLSTransformer transformer = new XLSTransformer();
//			String currentDate = com.gzzl.commons.util.DateUtil.convertDateToString(new Date(),
//					com.gzzl.commons.util.DateUtil.FORMAT_yyyyMMddHHmmss);
			String exportfileName = fileName + ".xlsx";
			String filePath = FilePropertyUtil.getWebTempPath();
			String servletPath = filePath + File.separator + "file" + File.separator + "easypoiExample"
					+ File.separator;
			String destFilePath = servletPath + exportfileName;
			String returnPath = FilePropertyUtil.getWebTempUrl() + "/" + "file" + File.separator + "easypoiExample"
					+ File.separator + exportfileName;
			File ffile = new File(filePath);
			if (!ffile.exists()) {
				ffile.mkdirs();
			}
			transformer.transformXLS(filePatch, beanParams, destFilePath);
			return URLEncoder.encode(returnPath, "UTF-8");
		} catch (Exception e) {
			throw new ApiException("文件生成失败");
		}
	}
	
	public static List<ExcelExportEntity> generateExcelExportEntitys(List<BaseExportDto> columns,Field[] fields){
		if (CollectionUtils.isEmpty(columns) || ArrayUtils.isEmpty(fields)) {
			return null;
		}
		Map<String, Integer> typeMap = new HashMap<String, Integer>();
		Map<String, String> numFormatMap = new HashMap<String, String>();

		List<ExcelExportEntity> excelExportEntities = new ArrayList<ExcelExportEntity>(columns.size());
		for (Field f : fields) {
			Excel excel = f.getAnnotation(Excel.class);
			if (excel == null) {
				continue;
			}
			typeMap.put(f.getName(), excel.type());
			if (StringUtils.isNotBlank(excel.numFormat())) {
				numFormatMap.put(f.getName(), excel.numFormat());
			}
		}
		for (int i = 0; i < columns.size(); i++) {
			BaseExportDto column = columns.get(i);
			ExcelExportEntity excelExportEntity = new ExcelExportEntity(column.getName(), column.getKey(), 20);
			int type = typeMap.containsKey(column.getKey()) ? typeMap.get(column.getKey()) : 1;
			if (numFormatMap.containsKey(column.getKey())) {
				String numFormat = numFormatMap.get(column.getKey());
				excelExportEntity.setNumFormat(numFormat);
			}else if (type == 10){
				//如果是小数类型且没有设置数字格式,设置一个默认的数字格式
				excelExportEntity.setNumFormat(ExcelHelper.EXPORT_NUMFORMAT_DEFAULT_30);
			}
			excelExportEntity.setType(type);
			excelExportEntities.add(excelExportEntity);
		}
		return excelExportEntities;
	}
}
