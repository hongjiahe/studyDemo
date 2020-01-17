package com.hohe.importExcel;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.poi.ss.usermodel.Cell;


/**
 * 获取单元格数值数据
 * @author wanpeng 2016-09-14
 *
 */
public class CellNumberValueHandleImpl implements ICellValueHandle {

	@Override
	public Object getCellValue(Cell cell, String formatValue) {
		BigDecimal cellValue = new BigDecimal(cell.getNumericCellValue()); 
		String value = "";
		if(String.valueOf(cellValue) == null){ 
			value = "";
		} else {
			DecimalFormat df = new DecimalFormat("#.000000000");
			value = df.format(cell.getNumericCellValue());
			
			value = new BigDecimal(value).stripTrailingZeros().toPlainString();
			//value = String.valueOf(cellValue);
		}
		
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+cell.getNumericCellValue()+">>>>"+value);
		return value;
	}

}
