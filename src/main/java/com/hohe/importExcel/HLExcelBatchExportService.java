package com.hohe.importExcel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import cn.afterturn.easypoi.exception.excel.ExcelExportException;
import cn.afterturn.easypoi.exception.excel.enums.ExcelExportEnum;
import cn.afterturn.easypoi.util.PoiExcelGraphDataUtil;
import cn.afterturn.easypoi.util.PoiPublicUtil;

public class HLExcelBatchExportService extends ExcelExportService {
	private static ThreadLocal<HLExcelBatchExportService> THREAD_LOCAL = new ThreadLocal();
	private Workbook workbook;
	private Sheet sheet;
	private List<ExcelExportEntity> excelParams;
	private ExportParams entity;
	private int titleHeight;
	private Drawing patriarch;
	private short rowHeight;
	private int index;

	public void init(ExportParams entity, Class<?> pojoClass) {
		List excelParams = createExcelExportEntityList(entity, pojoClass);
		init(entity, excelParams);
	}

	public void init(ExportParams entity, Class<?> pojoClass, Workbook workbook) {
		List excelParams = createExcelExportEntityList(entity, pojoClass);
		init(entity, excelParams, workbook);
	}

	public void init(ExportParams entity, List<ExcelExportEntity> excelParams) {
		LOGGER.debug("ExcelBatchExportServer only support SXSSFWorkbook");
		entity.setType(ExcelType.XSSF);
		this.workbook = new SXSSFWorkbook();
		this.entity = entity;
		this.excelParams = excelParams;
		this.type = entity.getType();
		createSheet(this.workbook, entity, excelParams);
		if (entity.getMaxNum() == 0) {
			entity.setMaxNum(1000000);
		}
		insertDataToSheet(this.workbook, entity, excelParams, null, this.sheet);
	}

	public void init(ExportParams entity, List<ExcelExportEntity> excelParams, Workbook workbook) {
		LOGGER.debug("ExcelBatchExportServer only support SXSSFWorkbook");
		entity.setType(ExcelType.XSSF);
		this.workbook = workbook;
		this.entity = entity;
		this.excelParams = excelParams;
		this.type = entity.getType();
		createSheet(this.workbook, entity, excelParams);
		if (entity.getMaxNum() == 0) {
			entity.setMaxNum(1000000);
		}
		insertDataToSheet(this.workbook, entity, excelParams, null, this.sheet);
	}

	public List<ExcelExportEntity> createExcelExportEntityList(ExportParams entity, Class<?> pojoClass) {
		try {
			List excelParams = new ArrayList();
			if (entity.isAddIndex()) {
				excelParams.add(indexExcelEntity(entity));
			}

			Field[] fileds = PoiPublicUtil.getClassFields(pojoClass);
			ExcelTarget etarget = (ExcelTarget) pojoClass.getAnnotation(ExcelTarget.class);
			String targetId = etarget == null ? null : etarget.value();
			getAllExcelField(entity.getExclusions(), targetId, fileds, excelParams, pojoClass, null, null);

			sortAllParams(excelParams);

			return excelParams;
		} catch (Exception e) {
			throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, e);
		}
	}

	public void createSheet(Workbook workbook, ExportParams entity, List<ExcelExportEntity> excelParams) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Excel export start ,List<ExcelExportEntity> is {}", excelParams);
			LOGGER.debug("Excel version is {}", entity.getType().equals(ExcelType.HSSF) ? "03" : "07");
		}
		if ((workbook == null) || (entity == null) || (excelParams == null))
			throw new ExcelExportException(ExcelExportEnum.PARAMETER_ERROR);
		try {
			try {
				this.sheet = workbook.createSheet(entity.getSheetName());
			} catch (Exception e) {
				this.sheet = workbook.createSheet();
			}
		} catch (Exception e) {
			throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, e);
		}
	}

	public Workbook appendData(Collection<?> dataSet) {
		if (this.sheet.getLastRowNum() + dataSet.size() > this.entity.getMaxNum()) {
			this.sheet = this.workbook.createSheet();
			this.index = 0;
		}

		Iterator its = dataSet.iterator();
		while (its.hasNext()) {
			Object t = its.next();
			try {
				this.index += createCells(this.patriarch, this.index, t, this.excelParams, this.sheet, this.workbook,
						this.rowHeight);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, e);
			}
		}
		return this.workbook;
	}

	protected void insertDataToSheet(Workbook workbook, ExportParams entity, List<ExcelExportEntity> entityList,
			Collection<?> dataSet, Sheet sheet) {
		try {
			this.dataHandler = entity.getDataHandler();
			if ((this.dataHandler != null) && (this.dataHandler.getNeedHandlerFields() != null)) {
				this.needHandlerList = Arrays.asList(this.dataHandler.getNeedHandlerFields());
			}

			setExcelExportStyler((IExcelExportStyler) entity.getStyle().getConstructor(new Class[] { Workbook.class })
					.newInstance(new Object[] { workbook }));
			this.patriarch = PoiExcelGraphDataUtil.getDrawingPatriarch(sheet);
			List excelParams = new ArrayList();
			if (entity.isAddIndex()) {
				excelParams.add(indexExcelEntity(entity));
			}
			excelParams.addAll(entityList);
			sortAllParams(excelParams);
			this.index = (entity.isCreateHeadRows() ? createHeaderAndTitle(entity, sheet, workbook, excelParams) : 0);
			this.titleHeight = this.index;
			setCellWith(excelParams, sheet);
			this.rowHeight = getRowHeight(excelParams);
			setCurrentIndex(1);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, e.getCause());
		}
	}

	public static HLExcelBatchExportService getExcelBatchExportService(ExportParams entity, Class<?> pojoClass) {
		if (THREAD_LOCAL.get() == null) {
			HLExcelBatchExportService batchServer = new HLExcelBatchExportService();
			batchServer.init(entity, pojoClass);
			THREAD_LOCAL.set(batchServer);
		}
		return (HLExcelBatchExportService) THREAD_LOCAL.get();
	}
	
	public static HLExcelBatchExportService getExcelBatchExportService(ExportParams entity, Class<?> pojoClass,
			Workbook workBook) {
		if (THREAD_LOCAL.get() == null) {
			HLExcelBatchExportService batchServer = new HLExcelBatchExportService();
			batchServer.init(entity, pojoClass, workBook);
			THREAD_LOCAL.set(batchServer);
		}
		return (HLExcelBatchExportService) THREAD_LOCAL.get();
	}

	public static HLExcelBatchExportService getExcelBatchExportService(ExportParams entity,
			List<ExcelExportEntity> excelParams) {
		if (THREAD_LOCAL.get() == null) {
			HLExcelBatchExportService batchServer = new HLExcelBatchExportService();
			batchServer.init(entity, excelParams);
			THREAD_LOCAL.set(batchServer);
		}
		return (HLExcelBatchExportService) THREAD_LOCAL.get();
	}
	
	public static HLExcelBatchExportService getExcelBatchExportService(ExportParams entity,
			List<ExcelExportEntity> excelParams, Workbook workBook) {
		if (THREAD_LOCAL.get() == null) {
			HLExcelBatchExportService batchServer = new HLExcelBatchExportService();
			batchServer.init(entity, excelParams, workBook);
			THREAD_LOCAL.set(batchServer);
		}
		return (HLExcelBatchExportService) THREAD_LOCAL.get();
	}

	public static HLExcelBatchExportService getCurrentExcelBatchExportService() {
		return (HLExcelBatchExportService) THREAD_LOCAL.get();
	}

	public void closeExportBigExcel() {
		if (this.entity.getFreezeCol() != 0) {
			this.sheet.createFreezePane(this.entity.getFreezeCol(), 0, this.entity.getFreezeCol(), 0);
		}
		mergeCells(this.sheet, this.excelParams, this.titleHeight);

		addStatisticsRow(getExcelExportStyler().getStyles(true, null), this.sheet);
		THREAD_LOCAL.remove();
	}
}