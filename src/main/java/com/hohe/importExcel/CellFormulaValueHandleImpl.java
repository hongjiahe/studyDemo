package com.hohe.importExcel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

/**
 * 获取单元格公式内容
 * @author wanpeng 2016-09-14
 *
 */
public class CellFormulaValueHandleImpl implements ICellValueHandle {
	
	private static ICellValueHandle cellDateValueHandle = null;
	private static ICellValueHandle cellNumberValueHandle = null;
	
	public CellFormulaValueHandleImpl () {
		if (null == cellDateValueHandle) {
			cellDateValueHandle = new CellDateValueHandleImpl();
		}
		if (null == cellNumberValueHandle) {
			cellNumberValueHandle = new CellNumberValueHandleImpl();
		}
	}

	@Override
	public Object getCellValue(Cell cell, String formatValue) {
		Object value = "";
		try {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				value = cellDateValueHandle.getCellValue(cell, formatValue);
			} else {
				value = cellNumberValueHandle.getCellValue(cell, formatValue);
			}  
		} catch(IllegalStateException e) {
			value = String.valueOf(cell.getRichStringCellValue());
		}
		return value;
	}

}
