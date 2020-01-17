package com.hohe.importExcel;

import java.text.SimpleDateFormat;

import com.hohe.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

/**
 * 获取单元格日期数据
 * @author wanpeng 2016-09-14
 *
 */
public class CellDateValueHandleImpl implements ICellValueHandle {

	@Override
	public Object getCellValue(Cell cell, String formatValue) {
		if (StringUtil.isBlank(formatValue)) {
			formatValue = DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Object value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
        return value;
	}


}
