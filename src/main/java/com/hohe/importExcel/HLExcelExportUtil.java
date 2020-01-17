package com.hohe.importExcel;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import cn.afterturn.easypoi.excel.export.template.ExcelExportOfTemplateUtil;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HLExcelExportUtil {
	public static Workbook exportBigExcel(ExportParams entity, Class<?> pojoClass, Collection<?> dataSet) {
		HLExcelBatchExportService batchService = HLExcelBatchExportService.getExcelBatchExportService(entity,
				pojoClass);

		return batchService.appendData(dataSet);
	}
	
	public static Workbook exportBigExcel(ExportParams entity, Class<?> pojoClass, Collection<?> dataSet,
			Workbook workbook) {
		HLExcelBatchExportService batchService = HLExcelBatchExportService.getExcelBatchExportService(entity, pojoClass,
				workbook);

		return batchService.appendData(dataSet);
	}

	public static Workbook exportBigExcel(ExportParams entity, List<ExcelExportEntity> excelParams,
			Collection<?> dataSet) {
		HLExcelBatchExportService batchService = HLExcelBatchExportService.getExcelBatchExportService(entity,
				excelParams);

		return batchService.appendData(dataSet);
	}
	
	public static Workbook exportBigExcel(ExportParams entity, List<ExcelExportEntity> excelParams,
			Collection<?> dataSet, Workbook workbook) {
		HLExcelBatchExportService batchService = HLExcelBatchExportService.getExcelBatchExportService(entity,
				excelParams, workbook);

		return batchService.appendData(dataSet);
	}

	public static void closeExportBigExcel() {
		HLExcelBatchExportService batchService = HLExcelBatchExportService.getCurrentExcelBatchExportService();
		if (batchService != null)
			batchService.closeExportBigExcel();
	}

	public static Workbook exportExcel(ExportParams entity, Class<?> pojoClass, Collection<?> dataSet) {
		Workbook workbook = getWorkbook(entity.getType(), dataSet.size());
		new ExcelExportService().createSheet(workbook, entity, pojoClass, dataSet);
		return workbook;
	}

	private static Workbook getWorkbook(ExcelType type, int size) {
		if (ExcelType.HSSF.equals(type))
			return new HSSFWorkbook();
		if (size < 100000) {
			return new XSSFWorkbook();
		}
		return new SXSSFWorkbook();
	}

	public static Workbook exportExcel(ExportParams entity, List<ExcelExportEntity> entityList, Collection<?> dataSet) {
		Workbook workbook = getWorkbook(entity.getType(), dataSet.size());
		new ExcelExportService().createSheetForMap(workbook, entity, entityList, dataSet);
		return workbook;
	}

	public static Workbook exportExcel(List<Map<String, Object>> list, ExcelType type) {
		Workbook workbook = getWorkbook(type, 0);
		for (Map map : list) {
			ExcelExportService service = new ExcelExportService();
			service.createSheet(workbook, (ExportParams) map.get("title"), (Class) map.get("entity"),
					(Collection) map.get("data"));
		}
		return workbook;
	}

	@Deprecated
	public static Workbook exportExcel(TemplateExportParams params, Class<?> pojoClass, Collection<?> dataSet,
			Map<String, Object> map) {
		return new ExcelExportOfTemplateUtil().createExcleByTemplate(params, pojoClass, dataSet, map);
	}

	public static Workbook exportExcel(TemplateExportParams params, Map<String, Object> map) {
		return new ExcelExportOfTemplateUtil().createExcleByTemplate(params, null, null, map);
	}

	public static Workbook exportExcel(Map<Integer, Map<String, Object>> map, TemplateExportParams params) {
		return new ExcelExportOfTemplateUtil().createExcleByTemplate(params, map);
	}
}