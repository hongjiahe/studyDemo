package com.hohe.importExcel;

import org.apache.poi.ss.usermodel.Cell;

/**
 * FCell 值处理接口
 * @author wanpeng 2016-09-13
 *
 */
public interface ICellValueHandle {
	
	/**
	 * 获取单元格内容
	 * @param cell 单元格
	 * @param formatValue 格式
	 * @return
	 */
	Object getCellValue(Cell cell, String formatValue);
	
}
