package com.hohe.importExcel;


/**
 * 单元格数据处理Handle
 * @author wanpeng 2016-09-14
 *
 */
public class CellValueHandleFactory {
	
	private static ICellValueHandle cellDateValueHandle = null;
	private static ICellValueHandle cellFormulaValueHandle = null;
	private static ICellValueHandle cellNumberValueHandle = null;
	private static ICellValueHandle cellStringValueHandle = null;
	
	/**
	 * 根据单元格数据类型获取处理对象
	 * @param cellType 单元格数据类型
	 * @return
	 */
	public static ICellValueHandle getCellValueHandle(int cellType){
		if (cellType == CellTypeEnum.NUMBER.getId()) {
			if (cellNumberValueHandle == null) {
				cellNumberValueHandle = new CellNumberValueHandleImpl();
			}
			return cellNumberValueHandle;
		} else if (cellType == CellTypeEnum.FORMULA.getId()) {
			if (cellFormulaValueHandle == null) {
				cellFormulaValueHandle = new CellFormulaValueHandleImpl();
			}
			return cellFormulaValueHandle;
		} else if (cellType == CellTypeEnum.DATE.getId()) {
			if (cellDateValueHandle == null) {
				cellDateValueHandle = new CellDateValueHandleImpl();
			}
			return cellDateValueHandle;
		} else {
			if (cellStringValueHandle == null) {
				cellStringValueHandle = new CellStringValueHandleImpl();
			}
			return cellStringValueHandle;
		}
	}

}
