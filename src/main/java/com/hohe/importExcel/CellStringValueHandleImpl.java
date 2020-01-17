package com.hohe.importExcel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

/**
 * 获取单元格字符串数据
 * @author wanpeng 2016-09-14
 *
 */
public class CellStringValueHandleImpl implements ICellValueHandle {

	@Override
	public Object getCellValue(Cell cell, String formatValue) {
		return StringUtils.trim(cell.getStringCellValue());
	}
}
