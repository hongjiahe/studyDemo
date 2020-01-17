package com.hohe.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hohe.importExcel.*;
import com.hohe.importExcel.JavaAction;
import com.hohe.model.ImprotTemplate;
import com.hohe.model.Template;
import com.hohe.model.TemplateAop;
import com.hohe.service.IImprotTemplateService;
import com.hohe.util.DateUtil;
import com.hohe.util.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.*;

@Service("improtTemplateService")
public class ImprotTemplateServiceImpl implements IImprotTemplateService {

    private static Logger logger = Logger
            .getLogger(ImprotTemplateServiceImpl.class);

    private ICellValueHandle cellHandle = null;

    @Override
    public List<Template> getTemplate() {

        List<Template> templateList = new ArrayList<Template>();
        try {
            DataSource ds = ConnectionFactory.getConnectionPool()
                    .getDataSource();
            QueryRunner runner = new QueryRunner(ds);
            String sql = "select TEMPLATE_CODE,TEMPLATE_NAME,SHEET_NAME,TABLE_NAME from t_template  ";
            List<Map<String, Object>> rs = runner.query(sql,
                    new MapListHandler(), new Object[] {});

            Iterator<Map<String, Object>> itr = rs.iterator();
            while (itr.hasNext()) {
                Template vo = new Template();
                Map<String, Object> mp = (Map<String, Object>) itr.next();
                vo.setTemplateCode((String) mp.get("TEMPLATE_CODE"));
                vo.setTemplateName((String) mp.get("TEMPLATE_NAME"));
                vo.setTableName((String) mp.get("TABLE_NAME"));
                vo.setSheetName((String) mp.get("SHEET_NAME"));
                templateList.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return templateList;
    }

    @Override
    public List<ImprotTemplate> findImprotTemplateByTemplateCode(
            String templateCode) {
        List<ImprotTemplate> improtTemplateList = new ArrayList<ImprotTemplate>();
        String sql = "select t.* from t_improt_template t where t.TEMPLATE_CODE=? order by t.SORT asc";
        try {
            DataSource ds = ConnectionFactory.getConnectionPool()
                    .getDataSource();
            QueryRunner runner = new QueryRunner(ds);
            List<Map<String, Object>> rs = runner.query(sql,
                    new MapListHandler(), new Object[] { templateCode });

            Iterator<Map<String, Object>> itr = rs.iterator();
            while (itr.hasNext()) {
                ImprotTemplate vo = new ImprotTemplate();
                Map<String, Object> mp = (Map<String, Object>) itr.next();
                vo.setId((Integer) mp.get("ID"));
                vo.setTableName((String) mp.get("TABLE_NAME"));
                vo.setTemplateCode((String) mp.get("TEMPLATE_CODE"));
                vo.setColsName((String) mp.get("COLS_NAME"));
                vo.setColsType((String) mp.get("COLS_TYPE"));
                vo.setSort((Integer) mp.get("SORT"));
                vo.setColsLableName((String) mp.get("COLS_LABLE_NAME"));
                vo.setFormat((String) mp.get("FORMAT"));
                if (null != mp.get("IS_SHOW")) {
                    vo.setIsShow((Integer) mp.get("IS_SHOW"));
                } else {
                    vo.setIsShow(0);
                }
                if (null != mp.get("IS_UPDATE")) {
                    vo.setIsUpdate((Integer) mp.get("IS_UPDATE"));
                } else {
                    vo.setIsUpdate(0);
                }
                if (null != mp.get("NOT_INSERT")) {
                    vo.setNotInsert((Integer) mp.get("NOT_INSERT"));
                } else {
                    vo.setNotInsert(0);
                }
                if (null != mp.get("IS_KEY")) {
                    vo.setIsKey((Integer) mp.get("IS_KEY"));
                } else {
                    vo.setIsKey(0);
                }
                if (null != mp.get("IS_CHANGE_VALUE")) {
                    vo.setIsChangeValue((Integer) mp.get("IS_CHANGE_VALUE"));
                } else {
                    vo.setIsChangeValue(0);
                }
                if (null != mp.get("IS_DEFAULT_COLS")) {
                    vo.setIsDefaultCols((Integer) mp.get("IS_DEFAULT_COLS"));
                } else {
                    vo.setIsDefaultCols(0);
                }
                if (null != mp.get("ROW_NUM")) {
                    vo.setRowNum((Integer) mp.get("ROW_NUM"));
                } else {
                    vo.setRowNum(0);
                }
                if (null != mp.get("COL_NUM")) {
                    vo.setColNum((Integer) mp.get("COL_NUM"));
                } else {
                    vo.setColNum(0);
                }
                improtTemplateList.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return improtTemplateList;
    }

    @Override
    public Template getTemplateByCode(String templateCode) {
        Template template = new Template();
        try {
            DataSource ds = ConnectionFactory.getConnectionPool()
                    .getDataSource();
            QueryRunner runner = new QueryRunner(ds);
            //String sql = "select TEMPLATE_CODE,TEMPLATE_NAME,SHEET_NAME,TABLE_NAME,EXCEL_FORM_TYPE,TITLE_ROWNUM from t_template t where t.id=? ";
            String sql = "select TEMPLATE_CODE,TEMPLATE_NAME,SHEET_NAME,TABLE_NAME,EXCEL_FORM_TYPE,TITLE_ROWNUM from t_template t where t.TEMPLATE_CODE = ? ";
            List<Map<String, Object>> rs = runner.query(sql,
                    new MapListHandler(), new Object[] {templateCode});
            System.out.println(rs.toString()+"====");
            Iterator<Map<String, Object>> itr = rs.iterator();
            while (itr.hasNext()) {
                Map<String, Object> mp = (Map<String, Object>) itr.next();
                template.setTemplateCode((String) mp.get("TEMPLATE_CODE"));
                template.setTemplateName((String) mp.get("TEMPLATE_NAME"));
                template.setSheetName((String) mp.get("SHEET_NAME"));
                template.setTableName((String) mp.get("TABLE_NAME"));
                if (null != mp.get("EXCEL_FORM_TYPE")) {
                    template.setExcelFormType((Integer) mp
                            .get("EXCEL_FORM_TYPE"));
                } else {
                    template.setExcelFormType(1);
                }
                if (null != mp.get("TITLE_ROWNUM")) {
                    template.setTitleRownum((Integer) mp.get("TITLE_ROWNUM"));
                } else {
                    template.setTitleRownum(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return template;
    }

    @Override
    public List<Map<String, Object>> getDataByTemplateCode(String templateCode,
                                                           String searchColsName, String colsValue, Map<String, String> map,
                                                           int pageNumber, int pageSize) {
        TemplateAop aop = this.getTemplateAop(templateCode,
                "getDataByTemplateCode");
        aopAction(map, aop, true);
        List<ImprotTemplate> improtTemplateList = this
                .findImprotTemplateByTemplateCode(templateCode);
        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        List<Object> list = new ArrayList<Object>();
        if (null != improtTemplateList && improtTemplateList.size() > 0) {
            String tableName = improtTemplateList.get(0).getTableName();
            StringBuffer sb = new StringBuffer();
            sb.append("select ");
            String colsNames = " id,";
            List<String> colsList = new ArrayList<String>();
            for (ImprotTemplate improtTemplate : improtTemplateList) {
                String colsName = improtTemplate.getColsName();
                colsNames += colsName + ",";
                colsList.add(colsName);
            }
            colsNames = colsNames.substring(0, colsNames.length() - 1);
            sb.append(colsNames);
            sb.append(" from ");
            sb.append(tableName);
            sb.append(" where 1=1 ");
            if (StringUtil.isNotBlank(searchColsName)) {
                sb.append(" and " + searchColsName + "=?");
                list.add(colsValue);
            }
            // 拼装查询条件
            if (null != map) {
                for (String queryKey : map.keySet()) {
                    if (colsList.contains(queryKey)) {
                        Object keyValue = map.get(queryKey);
                        // 获取查询条件类型 默认为String类型，进行模糊匹配
                        String queryType = "LIKE";
                        if (null != map.get(queryKey + "_query_type")) {
                            queryType = (String) map.get(queryKey
                                    + "_query_type");
                        }
                        if ("EQUALS".equals(queryType)) {
                            if (null != keyValue
                                    && StringUtil
                                    .isNotBlank(((String) keyValue)
                                            .trim())) {
                                sb.append(" and " + queryKey + "=?");
                                list.add(keyValue);
                            }

                        } else if ("BETWEEN".equals(queryType)) {
                            Object startDate = map.get(queryKey + "_start");
                            if (null != startDate
                                    && StringUtil
                                    .isNotBlank(((String) startDate)
                                            .trim())) {
                                String strStartDate = ((String) startDate)
                                        .trim() + " 00:00:00";
                                sb.append(" and " + queryKey + ">=?");
                                list.add(strStartDate);
                            }
                            Object endDate = map.get(queryKey + "_end");
                            if (null != endDate
                                    && StringUtil.isNotBlank(((String) endDate)
                                    .trim())) {
                                String strEndDate = ((String) endDate).trim()
                                        + " 00:00:00";
                                sb.append(" and " + queryKey + "<=?");
                                list.add(strEndDate);
                            }
                        } else {
                            if (null != keyValue
                                    && StringUtil
                                    .isNotBlank(((String) keyValue)
                                            .trim())) {
                                sb.append(" and " + queryKey + " like ?");
                                list.add("%" + ((String) keyValue).trim() + "%");
                            }
                        }
                    }
                }
            }

            sb.append(" limit " + (pageNumber - 1) * pageSize + "," + pageSize);
            DataSource ds = ConnectionFactory.getConnectionPool()
                    .getDataSource();
            QueryRunner runner = new QueryRunner(ds);
            try {
                Object[] paprm = list.toArray();
                logger.info(sb.toString());
                listData = (List<Map<String, Object>>) runner.query(
                        sb.toString(), new MapListHandler(), paprm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        aopAction(map, aop, false);
        return listData;
    }

    @Override
    public void aopAction(Map map, TemplateAop aop,boolean pref) {
        String aopaction=pref?aop.getPreAction():aop.getPosAction();
        if (aopaction!=null && !aopaction.isEmpty()) {
            if("java".equals(aop.getJsOrJava())){
                try {
                    JavaAction action=(JavaAction)Class.forName(aopaction).newInstance();
                    action.excute(map);
                /*} catch (ApiException e) {
                    throw e;*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    ScriptActionFactory.getDefaultAction().executeAction(map,
                            aopaction);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public Map<String, Object> getDataById(String templateCode, int id) {
        List<ImprotTemplate> improtTemplateList = this
                .findImprotTemplateByTemplateCode(templateCode);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<Object> list = new ArrayList<Object>();
        if (null != improtTemplateList && improtTemplateList.size() > 0) {
            String tableName = improtTemplateList.get(0).getTableName();
            StringBuffer sb = new StringBuffer();
            sb.append("select ");
            String colsNames = " id,";
            for (ImprotTemplate improtTemplate : improtTemplateList) {
                String colsName = improtTemplate.getColsName();
                colsNames += colsName + ",";
            }
            colsNames = colsNames.substring(0, colsNames.length() - 1);
            sb.append(colsNames);
            sb.append(" from ");
            sb.append(tableName);
            sb.append(" where 1=1 ");
            sb.append(" and id = ?");
            list.add(id);
            DataSource ds = ConnectionFactory.getConnectionPool()
                    .getDataSource();
            QueryRunner runner = new QueryRunner(ds);
            try {
                Object[] paprm = list.toArray();
                logger.info(sb.toString());
                List<Map<String, Object>> listData = (List<Map<String, Object>>) runner
                        .query(sb.toString(), new MapListHandler(), paprm);
                if (null != listData && listData.size() > 0) {
                    dataMap = listData.get(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataMap;
    }

    @Override
    public int getTotleSize(String templateCode, String colsName,
                            String colsValue, Map<String, String> map) {
        List<ImprotTemplate> improtTemplateList = this
                .findImprotTemplateByTemplateCode(templateCode);
        int rows = 0;
        List<Object> list = new ArrayList<Object>();
        if (null != improtTemplateList && improtTemplateList.size() > 0) {
            String tableName = improtTemplateList.get(0).getTableName();
            StringBuffer sb = new StringBuffer();
            sb.append("select count(1) as rows from ");
            sb.append(tableName);
            sb.append(" where 1=1 ");
            if (StringUtil.isNotBlank(colsName)) {
                sb.append(" and " + colsName + "= ?");
                list.add(colsValue);
            }
            // 拼装查询条件
            if (null != map) {
                List<String> colsList = new ArrayList<String>();
                for (ImprotTemplate improtTemplate : improtTemplateList) {
                    colsList.add(improtTemplate.getColsName());
                }
                for (String queryKey : map.keySet()) {
                    if (colsList.contains(queryKey)) {
                        Object keyValue = map.get(queryKey);
                        // 获取查询条件类型 默认为String类型，进行模糊匹配
                        String queryType = "LIKE";
                        if (null != map.get(queryKey + "_query_type")) {
                            queryType = (String) map.get(queryKey
                                    + "_query_type");
                        }
                        if ("EQUALS".equals(queryType)) {
                            if (null != keyValue
                                    && StringUtil
                                    .isNotBlank(((String) keyValue)
                                            .trim())) {
                                sb.append(" and " + queryKey + "=?");
                                list.add(keyValue);
                            }
                        } else if ("BETWEEN".equals(queryType)) {
                            Object startDate = map.get(queryKey + "_start");
                            if (null != startDate
                                    && StringUtil
                                    .isNotBlank(((String) startDate)
                                            .trim())) {
                                String strStartDate = ((String) startDate)
                                        .trim() + " 00:00:00";
                                sb.append(" and " + queryKey + ">=?");
                                list.add(strStartDate);
                            }
                            Object endDate = map.get(queryKey + "_end");
                            if (null != endDate
                                    && StringUtil.isNotBlank(((String) endDate)
                                    .trim())) {
                                String strEndDate = ((String) endDate).trim()
                                        + " 23:59:59";
                                sb.append(" and " + queryKey + "<=?");
                                list.add(strEndDate);
                            }
                        } else {
                            if (null != keyValue
                                    && StringUtil
                                    .isNotBlank(((String) keyValue)
                                            .trim())) {
                                sb.append(" and " + queryKey + " like ?");
                                list.add("%" + ((String) keyValue).trim() + "%");
                            }
                        }
                    }
                }
            }
            DataSource ds = ConnectionFactory.getConnectionPool()
                    .getDataSource();
            QueryRunner runner = new QueryRunner(ds);

            try {
                Object[] paprm = list.toArray();
                logger.info(sb.toString());
                List<Map<String, Object>> dataList = runner.query(
                        sb.toString(), new MapListHandler(), paprm);
                if (null != dataList) {
                    rows = ((Long) dataList.get(0).get("rows")).intValue();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rows;
    }

    @Override
    public int deleteData(String templateCode, String ids) {
        int rows = 0;
        List<ImprotTemplate> improtTemplateList = this
                .findImprotTemplateByTemplateCode(templateCode);
        if (null != improtTemplateList && improtTemplateList.size() > 0) {
            String tableName = improtTemplateList.get(0).getTableName();
            StringBuffer sb = new StringBuffer();
            DataSource ds = ConnectionFactory.getConnectionPool()
                    .getDataSource();
            QueryRunner runner = new QueryRunner(ds);
            try {
                sb.append(" delete from " + tableName + " where id in ").append(ids);
                logger.info(sb.toString() + ",paprm:" + ids);
                rows = runner.update(sb.toString(), new Object[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rows;
    }

    @Override
    public int insertData(String templateCode, Map<String, Object> map) {
        List<ImprotTemplate> improtTemplateList = this
                .findImprotTemplateByTemplateCode(templateCode);
        int successNum = 0;
        if (null != improtTemplateList && improtTemplateList.size() > 0) {
            String tableName = improtTemplateList.get(0).getTableName();
            StringBuffer _colsName = new StringBuffer();
            StringBuffer _paramName = new StringBuffer();
            List<Object> insertValues = new ArrayList<Object>();
            for (ImprotTemplate improtTemplate : improtTemplateList) {
                String colsName = improtTemplate.getColsName();
                String colsType = improtTemplate.getColsType();
                _colsName.append(colsName);
                _colsName.append(",");
                _paramName.append("?,");
                Object obj = map.get(colsName);
                if (obj == null) {
                    if ("NUMBER".equals(colsType)) {
                        obj = 0;
                    } else if ("DATE".equals(colsType)) {
                        obj = new Date();
                    } else {
                        obj = "";
                    }
                }
                insertValues.add(obj);
            }
            String pram = _paramName.toString();
            String cols = _colsName.toString();
            if (pram.endsWith(",")) {
                pram = pram.substring(0, pram.length() - 1);
            }
            if (cols.endsWith(",")) {
                cols = cols.substring(0, cols.length() - 1);
            }
            DataSource ds = ConnectionFactory.getConnectionPool()
                    .getDataSource();
            QueryRunner runner = new QueryRunner(ds);
            String sql = " insert into " + tableName + "(" + cols + ")"
                    + "values " + "(" + pram + ")";
            logger.info(sql + ":parmValue=" + insertValues);
            try {
                runner.update(sql, insertValues.toArray());
                successNum++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return successNum;
    }

    @Override
    public int updateData(String templateCode, Map<String, Object> dataMap) {
        List<ImprotTemplate> improtTemplateList = this
                .findImprotTemplateByTemplateCode(templateCode);
        int successNum = 0;
        if (null != improtTemplateList && improtTemplateList.size() > 0) {
            String tableName = improtTemplateList.get(0).getTableName();
            StringBuffer _colsName = new StringBuffer();
            List<Object> updateValues = new ArrayList<Object>();
            //Map<String, Object> keyMap = new HashMap<String, Object>();
            for (ImprotTemplate improtTemplate : improtTemplateList) {
                String colsName = improtTemplate.getColsName();
                String colsType = improtTemplate.getColsType();
                if (dataMap.containsKey(colsName)) {
                    Object obj = dataMap.get(colsName);
                    _colsName.append(colsName);
                    _colsName.append("=?,");
                    if (obj == null) {
                        if ("NUMBER".equals(colsType)) {
                            obj = 0;
                        } else if ("DATE".equals(colsType)) {
                            obj = new Date();
                        } else {
                            obj = "";
                        }
                    }
                    updateValues.add(obj);
                }
            }
            String updateColsName = _colsName.toString().replaceAll(",$", "");
            updateValues.add(dataMap.get("id")==null?dataMap.get("ID"):dataMap.get("id"));
            DataSource ds = ConnectionFactory.getConnectionPool()
                    .getDataSource();
            QueryRunner runner = new QueryRunner(ds);
            String sql = " update " + tableName + " set " + updateColsName
                    + " where 1=1  and id=?";
            logger.info(sql + ":parmValue=" + updateValues);
            try {
                runner.update(sql, updateValues.toArray());
                successNum++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return successNum;
    }

    @Override
    public Object getColsKeyByValue(String templateCode, String colsName,
                                    Object value) {
        String sql = "select cols_key,cols_type from t_template_cols t where t.template_code = ? and t.cols_name=? and t.cols_value=?  limit 0,1";
        List<Object> list = new ArrayList<Object>();
        list.add(templateCode);
        list.add(colsName);
        list.add(value);
        Object obj = null;
        DataSource ds = ConnectionFactory.getConnectionPool().getDataSource();
        QueryRunner runner = new QueryRunner(ds);
        try {
            List<Map<String, Object>> dataList = runner.query(sql,
                    new MapListHandler(), list.toArray());
            if (null != dataList && dataList.size() > 0) {
                Map<String, Object> map = dataList.get(0);
                if (null != map) {
                    String cols_type = (String) map.get("cols_type");
                    String cols_key = (String) map.get("cols_key");
                    if ("Integer".equals(cols_type)) {
                        obj = new Integer(cols_key);
                    } else if ("Double".equals(cols_type)) {
                        obj = new Double(cols_key);
                    } else {
                        obj = cols_key;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public String improtExecl(String templateCode, String fileName,
                              String defaultCols, String userName, InputStream in) {
        String msg = "";
        Template template = this.getTemplateByCode(templateCode);
        if (null == template || StringUtil.isBlank(template.getTemplateCode())
                || StringUtil.isBlank(template.getTableName())) {
            msg = "数据导入失败：模板未配置或配置不正确，请检查模板！1";
            return msg;
        }
        List<ImprotTemplate> improtTemplateList = this
                .findImprotTemplateByTemplateCode(templateCode);
        if (null == improtTemplateList || improtTemplateList.size() <= 0) {
            msg = "数据导入失败：未配置字段映射关系！";
            return msg;
        }
        String sheetName = template.getSheetName();
        // 默认字段需要从页面传递
        Map<String, String> defaultColsMap = new HashMap<String, String>();
        if (StringUtil.isNotBlank(defaultCols)) {
            JSONObject jsonObject = JSONObject.parseObject(defaultCols);
            for (String key : jsonObject.keySet()) {
                String value = jsonObject.getString(key);
                defaultColsMap.put(key, value);
            }
        }
        try {
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheet(sheetName);
            if (null == sheet) {
                msg = "数据导入失败：导入的excel中未找到名称为【" + sheetName
                        + "】的shell页，请修改导入的excel文件。";
                return msg;
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("excelFormType", template.getExcelFormType());
            this.startImprot(sheet, template, improtTemplateList,
                    defaultColsMap, fileName, userName, resultMap);
//            List<TemplateConf> templateConfList = this
//                    .getTemplateConf(templateCode); // 获取包含的子模板
//            if (null != templateConfList && templateConfList.size() > 0) {
//                for (TemplateConf tempConf : templateConfList) {
//                    Template childTemplate = this.getTemplateByCode(tempConf
//                            .getChildTemplateCode());
//                    int id = 0;
//                    if (null != resultMap.get("id")) {// 关联ID
//                        id = (Integer) resultMap.get("id");
//                    }
//                    defaultColsMap.put(tempConf.getColsName(),
//                            String.valueOf(id));
//                    resultMap.put("excelFormType",
//                            childTemplate.getExcelFormType());
//                    if (null == childTemplate
//                            || StringUtil.isBlank(childTemplate
//                            .getTemplateCode())
//                            || StringUtil.isBlank(childTemplate.getTableName())) {
//                        msg = "数据导入失败：子模板未配置或配置不正确，请检查模板！";
//                        break;
//                    }
//                    List<ImprotTemplate> childImprotTemplateList = this
//                            .findImprotTemplateByTemplateCode(childTemplate
//                                    .getTemplateCode());
//                    if (null == improtTemplateList
//                            || improtTemplateList.size() <= 0) {
//                        msg = "数据导入失败：子模板未配置字段映射关系！";
//                        return msg;
//                    }
//                    this.startImprot(sheet, childTemplate,
//                            childImprotTemplateList, defaultColsMap, fileName,
//                            userName, resultMap);
//                }
//            }
            msg = (String) resultMap.get("msg");
        } catch (Exception e) {
            msg = "数据导入失败：" + e.getMessage();
        }
        return msg;
    }

    /**
     * 执行导入
     *
     * @param sheet
     *            excel sheet页
     * @param template
     *            模板
     * @param improtTemplateList
     *            字段配置列
     * @param defaultColsMap
     *            默认值
     * @param fileName
     *            文件名称
     * @param userName
     *            用户名
     * @return
     */
    private String startImprot(Sheet sheet, Template template,
                               List<ImprotTemplate> improtTemplateList,
                               Map<String, String> defaultColsMap, String fileName,
                               String userName, Map<String, Object> resultMap) throws Exception{
        String msg = "";
        int fromType = template.getExcelFormType();
        if (ExcelFormType.FORM.getId() == fromType) {// 表单类型导入
            try {
                this.improtFormExcel(sheet, template, improtTemplateList,
                        defaultColsMap, fileName, userName, resultMap);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(e);
            }
        } else if (ExcelFormType.LIST.getId() == fromType) { // 列表类型导入
            this.improtListExcel(sheet, template, improtTemplateList,
                    defaultColsMap, fileName, userName, resultMap);
        } else {
            msg = "数据导入失败：未配置相应的导入类型，请检查！";
        }
        return msg;
    }

    @Override
    public String improtFormExcel(Sheet sheet, Template template,
                                  List<ImprotTemplate> improtTemplateList,
                                  Map<String, String> defaultColsMap, String fileName,
                                  String userName, Map<String, Object> resultMap)  throws Exception{
        String msg = "";
        List<Map<String, Object>> dataList = this.createExcelFormData(sheet,
                defaultColsMap, template, improtTemplateList, fileName,
                userName, resultMap);
        //判断是否需要保存数据
        if(resultMap.get("notSaveData")==null){
            if (null != dataList && dataList.size() > 0) {
                msg = this.dataHandle(dataList, template, improtTemplateList,
                        fileName, userName, resultMap);
            } else {
                msg = "数据导入失败：未匹配到对应的数据，请检查模板设置和导入的文件内容。";
            }
        }else{
            resultMap.put("dataList", dataList);//将数据放到Map中返回前台
        }
        return msg;
    }

    @Override
    public String improtListExcel(Sheet sheet, Template template,
                                  List<ImprotTemplate> improtTemplateList,
                                  Map<String, String> defaultColsMap, String fileName,
                                  String userName, Map<String, Object> resultMap) throws Exception{
        String msg = "";
        List<Map<String, Object>> dataList = createExcelListData(sheet,
                defaultColsMap, template, improtTemplateList, fileName,
                userName, resultMap);
        //判断是否需要保存数据
        if(resultMap.get("notSaveData")==null){
            if (null != dataList && dataList.size() > 0) {
                try {
                    msg = this.dataHandle(dataList, template, improtTemplateList,
                            fileName, userName, resultMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception(e);
                }
            } else {
                msg = "数据导入失败：未匹配到对应的数据，请检查模板设置和导入的文件内容。";
            }
        }else{
            //多个SHEET的情况
            if (BooleanUtils.isTrue(MapUtils.getBoolean(resultMap, "isChild"))) {
                resultMap.put("childDataList", dataList);//将数据放到Map中返回前台
            } else {
                resultMap.put("dataList", dataList);//将数据放到Map中返回前台
            }
        }
        return msg;
    }

//    @Override
//    public int findTable(String userName, String tableName) {
//        DataSource ds = ConnectionFactory.getConnectionPool().getDataSource();
//        Connection connection = null;
//        DatabaseMetaData dbmd = null;
//        int tableSize = 0;
//        try {
//            connection = ds.getConnection();
//            dbmd = connection.getMetaData();
//            String[] tableType = { "TABLE" };
//            ResultSet rs = dbmd.getTables(connection.getCatalog(), userName,
//                    tableName, tableType);
//            while (rs.next()) {
//                tableSize++;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (null != connection) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return tableSize;
//    }

    @Override
    public List<Map<String, Object>> createExcelListData(Sheet sheet,
                                                         Map<String, String> defaultColsMap, Template template,
                                                         List<ImprotTemplate> templateList, String fileName,
                                                         String userName, Map<String, Object> resultMap) {
        String templateCode = template.getTemplateCode();
        String templateName = template.getTemplateName();
        String tableName = template.getTableName();
        List<String> lableNameList = new ArrayList<String>();
        // 主键
        Map<String, String> colsLable = new HashMap<String, String>();
        // 字段类型
        Map<String, String> colsTypeMap = new HashMap<String, String>();
        // excel 数据
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Integer> isDefaultColsMap = new HashMap<String, Integer>();
        // 获取导入模板设置中配置的表和字段
        for (ImprotTemplate t1 : templateList) {
            tableName = t1.getTableName();
            String colsName = t1.getColsName();
            String colsLableName = t1.getColsLableName();
            String colsType = t1.getColsType();
            Integer isDefaultCols = t1.getIsDefaultCols();
            isDefaultColsMap.put(colsName, isDefaultCols);
            lableNameList.add(colsLableName);
            colsLable.put(colsLableName, colsName);
            colsTypeMap.put(colsLableName, colsType);
        }
        int titleRowNum = template.getTitleRownum();
        Map<String, Integer> titleMap = new HashMap<String, Integer>();
        if (titleRowNum == 0) {
            resultMap.put("msg", "数据导入失败：模板未配置标题行号，请检查模板配置！");
            return dataList;
        }
        Row titleRow = sheet.getRow(titleRowNum - 1);
        if(titleRow==null) {
            resultMap.put("msg", "数据导入失败：excel数据加载异常,请检查导入表格！");
            return dataList;
        }
        // 设置字段所在excel中的列号
        for (int colNum = 0; colNum < titleRow.getLastCellNum(); colNum++) {
            Cell cell = titleRow.getCell(colNum);
            if (null != cell) {
                String tiltle = (String) this.getCellValue(cell, "");
                if (StringUtil.isNotBlank(tiltle)) {
                    for (int j = 0; j < lableNameList.size(); j++) {
                        if (tiltle.trim().equals(lableNameList.get(j).trim())) {
                            titleMap.put(tiltle.trim(), colNum);
                            break;
                        }
                    }
                }
            }
        }
        if (titleMap.isEmpty()) {
            resultMap.put("msg", "数据导入失败：未找到excel的标题行数据，请检查模板配置！");
            return dataList;
        }
        // 读取数据
        int rowNum = sheet.getLastRowNum();
        for (int rows = titleRowNum; rows < rowNum + 1; rows++) {
            Row row = sheet.getRow(rows);
            if (row != null) {
                Object[] _paramValues = new Object[lableNameList.size()];
                int index = 0;
                Map<String, Object> dataMap = new HashMap<String, Object>();
                for (String colsLableName : lableNameList) {

                    String colsName = colsLable.get(colsLableName);
                    // 是否来源于参数
                    Integer isDefaultCols = isDefaultColsMap.get(colsName);
                    String colsType = colsTypeMap.get(colsLableName);
                    if (TemplateColsIsDefaultEnum.IS_DEFAULT.getId() == isDefaultCols) {
                        String deColsValue = defaultColsMap.get(colsName);
                        Object obj = getDefaultValue(colsType, deColsValue);
                        dataMap.put(colsName, obj);
                    } else {
                        int columns = 0;
                        try {
                            if (null != titleMap.get(colsLableName)) {
                                columns = titleMap.get(colsLableName);
                                Cell cell = row.getCell(columns);
                                if (cell != null) {
                                    Object value = this.getCellValue(cell, "");
                                    _paramValues[index] = value;
                                    dataMap.put(colsName, _paramValues[index]);
                                    if (null == dataMap
                                            .get("canUpdateOrInsert")) {
                                        dataMap.put("canUpdateOrInsert", true);
                                    }
                                    index++;
                                }
                            } else {
                                String remark = "数据导入失败：excel不包含【"
                                        + colsLableName + "】列，请检查模板配置。";
                                resultMap.put("msg", remark);
                                System.out.println("remark>>>"+remark);
                                String expLog = "";
//                                ImprotLog improtLog = createImprotLog(1,
////                                        templateCode, templateName, fileName,
////                                        tableName, 0, remark, expLog, userName);
////                                this.insertImprotLog(improtLog);
                                dataMap.put("canUpdateOrInsert", false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            String remark = "数据导入失败：第" + (rows + 1) + "行第"
                                    + (columns + 1) + "列【" + colsLableName
                                    + "】数据格式不正确，要求的数据格式为：" + colsType;
                            resultMap.put("msg", remark);
                            String expLog = "异常堆栈信息：" + e;
//                            ImprotLog improtLog = createImprotLog(1,
//                                    templateCode, templateName, fileName,
//                                    tableName, 0, remark, expLog, userName);
//                            this.insertImprotLog(improtLog);
                            dataMap.put("canUpdateOrInsert", false);
                        }
                    }
                }
                dataList.add(dataMap);
            }
        }
        return dataList;
    }

    @Override
    public List<Map<String, Object>> createExcelFormData(Sheet sheet,
                                                         Map<String, String> defaultColsMap, Template template,
                                                         List<ImprotTemplate> improtTemplateList, String fileName,
                                                         String userName, Map<String, Object> resultMap) {
        String templateCode = template.getTemplateCode();
        String templateName = template.getTemplateName();
        String tableName = template.getTableName();
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Integer> isChangeValueMap = new HashMap<String, Integer>();
        List<String> keyList = new ArrayList<String>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        for (ImprotTemplate improtTemplate : improtTemplateList) {
            int rowNum = improtTemplate.getRowNum();// 行号
            int colNum = improtTemplate.getColNum();// 列号
            String colsLableName = improtTemplate.getColsLableName();
            String colsName = improtTemplate.getColsName();
            String colsType = improtTemplate.getColsType();
            Integer isDefaultCols = improtTemplate.getIsDefaultCols();
            if (TemplateColsIsDefaultEnum.IS_DEFAULT.getId() == isDefaultCols) {
                String deColsValue = defaultColsMap.get(colsName);
                Object obj = getDefaultValue(colsType, deColsValue);
                dataMap.put(colsName, obj);
            } else {
                Row row = sheet.getRow(rowNum - 1);
                if (null != row) {
                    Cell cell = row.getCell(colNum - 1);
                    if (cell != null) {
                        try {
                            Object value = this.getCellValue(cell, "");
                            dataMap.put(colsName, value);
                            if (null == dataMap.get("canUpdateOrInsert")) {
                                dataMap.put("canUpdateOrInsert", true);
                            }
                        } catch (Exception e) {
                            String remark = "异常描述：第" + (rowNum) + "行第"
                                    + (colNum) + "列【" + colsLableName
                                    + "】数据格式不正确。";
                            String expLog = "异常堆栈信息：" + e;
//                            ImprotLog improtLog = createImprotLog(1,
//                                    templateCode, templateName, fileName,
//                                    tableName, 0, remark, expLog, userName);
//                            this.insertImprotLog(improtLog);
                            dataMap.put("canUpdateOrInsert", false);
                        }
                    }
                }
            }
            if (TemplateColsIsKeyEnum.IS_KEY.getId() == improtTemplate
                    .getIsKey()) {
                keyList.add(colsName);
            }
            isChangeValueMap.put(colsName, improtTemplate.getIsChangeValue());
        }
        dataList.add(dataMap);
        return dataList;
    }

    /**
     * 获取单元格数据
     *
     * @param cell
     * @param formatValue
     * @return
     */
    private Object getCellValue(Cell cell, String formatValue) {
        Object value = "";
        int cellType = cell.getCellType();
        // 默认为String类型
        int cellTrueType = Cell.CELL_TYPE_STRING;
        switch (cellType) {
            case Cell.CELL_TYPE_STRING:
                cellTrueType = CellTypeEnum.STRING.getId();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    cellTrueType = CellTypeEnum.DATE.getId();
                } else {
                    cellTrueType = CellTypeEnum.NUMBER.getId();
                }
                break;
            case Cell.CELL_TYPE_FORMULA:
                cellTrueType = CellTypeEnum.FORMULA.getId();
                break;
            default:
                cellTrueType = Cell.CELL_TYPE_STRING;
                break;
        }
        cellHandle = CellValueHandleFactory.getCellValueHandle(cellTrueType);
        if (null != cellHandle) {
            value = cellHandle.getCellValue(cell, formatValue);
        }
        return value;
    }

    /**
     * 获取默认值
     *
     * @param colsType
     *            默认值格式
     * @param deColsValue
     *            值
     * @return
     */
    private Object getDefaultValue(String colsType, String deColsValue) {
        Object obj = "";
        if ("NUMBER".equals(colsType)) {
            if (StringUtil.isNotBlank(deColsValue)) {
                obj = new Integer(deColsValue);
            } else {
                obj = 0;
            }
        } else if ("DATE".equals(colsType)) {
            Date now = new Date();
            if (StringUtil.isNotBlank(deColsValue)) {
                obj = deColsValue;
            } else {
                obj = DateUtil.convertDateToString(now,
                        DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss);
            }
        } else {
            if (StringUtil.isNotBlank(deColsValue)) {
                obj = deColsValue;
            } else {
                obj = "";
            }
        }
        return obj;
    }

    /**
     * 处理数据
     *            键值转换
     * @return
     */
    private String dataHandle(List<Map<String, Object>> dataList,
                              Template template, List<ImprotTemplate> improtTemplateList,
                              String fileName, String userName, Map<String, Object> resultMap) throws Exception{
        String tableName = template.getTableName();
        String templateCode = template.getTemplateCode();
        String templateName = template.getTemplateName();
        // 主键
        List<String> keyList = new ArrayList<String>();
        Map<String, Integer> isChangeValueMap = new HashMap<String, Integer>();
        Map<String,Integer> isNotInsert =new HashMap<String, Integer>();
        Map<String,Integer> isUpdate =new HashMap<String, Integer>();
        for (ImprotTemplate t1 : improtTemplateList) {
            String colsName = t1.getColsName();
            int isKey = t1.getIsKey();
            if (TemplateColsIsKeyEnum.IS_KEY.getId() == isKey) {
                keyList.add(colsName);
            }
            int isChangeValue = t1.getIsChangeValue();
            isChangeValueMap.put(colsName, isChangeValue);
            isNotInsert.put(colsName, t1.getNotInsert());
            isUpdate.put(colsName, t1.getIsUpdate());
        }
        String msg = "";
        DataSource ds = ConnectionFactory.getConnectionPool().getDataSource();
        QueryRunner runner = new QueryRunner(ds);
        int rows = 0;
        int fileNum = 0;
        int insertNum = 0;
        int updateNum = 0;
        int excelFormType = (Integer) resultMap.get("excelFormType");
        try {
            for (Map<String, Object> map : dataList) {
                rows++;
                String _colsName = "";
                String updateColsName = "";
                List<Object> insertValues = new ArrayList<Object>();
                List<Object> updateValues = new ArrayList<Object>();
                StringBuffer _paramName = new StringBuffer();
                List<Object> keyValueList = new ArrayList<Object>();
                boolean canUpdateOrInsert = (Boolean) map
                        .get("canUpdateOrInsert");
                // 操作类型：operType 0 :表示插入，1:表示更新
                int operType = 0;
                if (canUpdateOrInsert) {
                    for (String colsName : map.keySet()) {
                        if (!"canUpdateOrInsert".equals(colsName) && !"errorInfo".equals(colsName) &&
                                isNotInsert.get(colsName)!=null && isNotInsert.get(colsName)==0) {
                            Object value = map.get(colsName);
                            // 判断是否需要转换数据
                            Integer isChange = isChangeValueMap.get(colsName);
                            if (isChange == TemplateColsIsChangeEnum.IS_CHANGE
                                    .getId()) {
                                value = this.getColsKeyByValue(templateCode,
                                        colsName, value);
                            }
                            _colsName += colsName + ",";
                            if (!keyList.contains(colsName) && isUpdate.get(colsName)==0 ) {
                                if(!"id".equals(colsName)){
                                    updateColsName += colsName + "=?,";
                                    updateValues.add(value);
                                }
                            }
                            _paramName.append("?,");
                            insertValues.add(value);
                        }else if("errorInfo".equals(colsName)){
                            Object value = map.get(colsName);
                            if(value!=null && ((String)value).contains(JavaAction.DATAEXISTSNO)){
                                operType =1 ;
                            }
                        }
                    }
                    // 根据主键查询数据
                    String keyQueryCri = "";
                    for (String keyCols : keyList) {
                        Object keyValue = map.get(keyCols);
                        if ("".equals(keyValue)) {
                            System.out.println(map.toString());
                            String remark = "异常描述：第" + (rows + 1) + "行【"
                                    + keyCols + "】为唯一标示，不能为空！";
                            String expLog = keyCols + "为唯一标示，不能为空！";
//                            ImprotLog improtLog = createImprotLog(1,
//                                    templateCode, templateName, fileName,
//                                    tableName, 0, remark, expLog, userName);
//                            this.insertImprotLog(improtLog);
                            operType = 3;
                            fileNum++;
                        } else {
                            keyQueryCri += "and " + keyCols + "=? ";
                            keyValueList.add(keyValue);
                            updateValues.add(keyValue);
                        }
                    }
                    if (operType != 3 && operType!=1 && StringUtil.isNotBlank(keyQueryCri)) {
                        String keyQuerySql = "select count(1) as counts from "
                                + tableName + " where 1=1 " + keyQueryCri;
                        List<Map<String, Object>> resList = runner.query(
                                keyQuerySql, new MapListHandler(),
                                keyValueList.toArray());
                        if (null != resList && resList.size() > 0) {
                            Long counts = (Long) resList.get(0).get("counts");
                            if (counts > 0) {
                                operType = 1;
                            }
                        }
                    }

                    if (_colsName.endsWith(",")) {
                        _colsName = _colsName.substring(0,
                                _colsName.length() - 1);
                    }
                    if (updateColsName.endsWith(",")) {
                        updateColsName = updateColsName.substring(0,
                                updateColsName.length() - 1);
                    }
                    String pram = _paramName.toString();
                    if (pram.endsWith(",")) {
                        pram = pram.substring(0, pram.length() - 1);
                    }
                    if (0 == operType) {
                        String sql = " insert into " + tableName + "("
                                + _colsName + ")" + "values " + "(" + pram
                                + ")";
                        logger.info(sql + ":parmValue=" + insertValues);
                        try{
                            if(!insertValues.isEmpty()) {
                                runner.update(sql, insertValues.toArray());
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            throw new Exception(e);
                        }
                        insertNum++;
                        if (ExcelFormType.FORM.getId() == excelFormType) {
                            // 获取ID
                            String queryIDsql = "select max(id) as mid from "
                                    + tableName;
                            List<Map<String, Object>> re = runner.query(
                                    queryIDsql, new MapListHandler(),
                                    new Object[] {});
                            if (re != null && re.size() > 0) {
                                int mid = (Integer) re.get(0).get("mid");
                                resultMap.put("id", mid);
                            }
                        }
                    } else if (1 == operType) {
                        String sql = " update " + tableName + " set "
                                + updateColsName + " where 1=1 " + keyQueryCri;
                        logger.info(sql + ":parmValue=" + updateValues);
                        try{
                            runner.update(sql, updateValues.toArray());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        updateNum++;
                        if (ExcelFormType.FORM.getId() == excelFormType) {
                            // 获取ID
                            String queryIDsql = "select id from " + tableName
                                    + " where 1=1 " + keyQueryCri;
                            List<Map<String, Object>> re = runner.query(
                                    queryIDsql, new MapListHandler(),
                                    new Object[] { "" });
                            if (re != null && re.size() > 0) {
                                int mid = (Integer) re.get(0).get("id");
                                resultMap.put("id", mid);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileNum++;
            String remark = "异常描述：第" + rows + "行插入不成功。";
            String expLog = "异常堆栈信息：" + e;
//            ImprotLog improtLog = createImprotLog(1, templateCode,
//                    templateName, fileName, tableName, 0, remark, expLog,
//                    userName);
//            this.insertImprotLog(improtLog);
        }
        // 插入新增成功的记录
        if (insertNum > 0) {
            String remark = "成功导入[" + insertNum + "]条记录";
//            ImprotLog improtLog = createImprotLog(0, templateCode,
//                    templateName, fileName, tableName, insertNum, remark, "",
//                    userName);
//            this.insertImprotLog(improtLog);
        }
        if (updateNum > 0) {
            String remark = "成功更新[" + updateNum + "]条记录";
//            ImprotLog improtLog = createImprotLog(0, templateCode,
//                    templateName, fileName, tableName, updateNum, remark, "",
//                    userName);
//            this.insertImprotLog(improtLog);
        }
        msg = "成功导入[" + insertNum + "]条记录," + "成功更新[" + updateNum + "]条记录,"
                + "失败[" + fileNum + "]条记录。";
        resultMap.put("msg", msg);
        return msg;
    }

    /**
     * 组织日志对象
     *
     * @return
     */
//    private ImprotLog createImprotLog(int isSuccess, String templateCode,
//                                      String templateName, String fileName, String tableName,
//                                      int numbers, String remark, String expLog, String userName) {
//        ImprotLog improtLog = new ImprotLog();
//        improtLog.setIsSuccess(isSuccess);
//        improtLog.setTemplateCode(templateCode);
//        improtLog.setTemplateName(templateName);
//        improtLog.setExcelName(fileName);
//        improtLog.setTableName(tableName);
//        improtLog.setNumbers(numbers);
//        improtLog.setRemark(remark);
//        improtLog.setExpLog(expLog);
//        improtLog.setOperator(userName);
//        improtLog.setCreateDate(new Date());
//        return improtLog;
//    }

    @Override
    public TemplateAop getTemplateAop(String templateCode, String methodName) {
        TemplateAop template = new TemplateAop();
        try {
            DataSource ds = ConnectionFactory.getConnectionPool()
                    .getDataSource();
            QueryRunner runner = new QueryRunner(ds);
            String sql = "select ID,TEMPLATE_CODE,PRE_ACTION,POS_ACTION,METHOD_NAME,JS_OR_JAVA from t_template_aop t where t.TEMPLATE_CODE=? and t.METHOD_NAME=?";
            List<Map<String, Object>> rs = runner.query(sql,
                    new MapListHandler(), new Object[] { templateCode,methodName });

            Iterator<Map<String, Object>> itr = rs.iterator();
            while (itr.hasNext()) {
                Map<String, Object> mp = (Map<String, Object>) itr.next();
                template.setId((Integer) mp.get("ID"));
                template.setTemplateCode((String) mp.get("TEMPLATE_CODE"));
                template.setDomainId((Integer) mp.get("DOMAIN_ID"));
                template.setDomainName((String) mp.get("DOMAIN_NAME"));
                template.setMethodName((String) mp.get("METHOD_NAME"));
                template.setPreAction((String) mp.get("PRE_ACTION"));
                template.setPosAction((String) mp.get("POS_ACTION"));
                template.setJsOrJava((String)mp.get("JS_OR_JAVA"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return template;
    }

    @Override
    public Map<String,Object> getDataExecl(String templateCode, String fileName,
                                           String defaultCols, String userName, InputStream in) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("notSaveData",true);//标识数据不保存数据库
        TemplateAop aop = this.getTemplateAop(templateCode,
                "getDataByTemplateCode");
        aopAction(resultMap, aop, true);//预处理
        String msg = "";
        Template template = this.getTemplateByCode(templateCode);
            if (null == template || StringUtil.isBlank(template.getTemplateCode())
                || StringUtil.isBlank(template.getTableName())) {
            msg = "数据导入失败：模板未配置或配置不正确，请检查模板！2";
            resultMap.put("msg", msg);
            return resultMap;
        }
        List<ImprotTemplate> improtTemplateList = this
                .findImprotTemplateByTemplateCode(templateCode);
        if (null == improtTemplateList || improtTemplateList.size() <= 0) {
            msg = "数据导入失败：未配置字段映射关系！";
            resultMap.put("msg", msg);
            return resultMap;
        }
        resultMap.put("tableTHList", improtTemplateList);
        String sheetName = template.getSheetName();
        // 默认字段需要从页面传递
        Map<String, String> defaultColsMap = new HashMap<String, String>();
        if (StringUtil.isNotBlank(defaultCols)) {
            JSONObject jsonObject = JSONObject.parseObject(defaultCols);
            for (String key : jsonObject.keySet()) {
                String value = jsonObject.getString(key);
                defaultColsMap.put(key, value);
            }
        }
        try {
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheet(sheetName);
            if (null == sheet) {
                msg = "数据导入失败：导入的excel中未找到名称为【" + sheetName
                        + "】的sheet页，请修改导入的excel文件。";
                resultMap.put("msg", msg);
                return resultMap;
            }else if (templateCode.equals("bomLmgImport") && sheet.getLastRowNum() > 40000 ) {
                resultMap.put("msg", "单次导入不能超过4w笔数据 ");
                return resultMap;
            }
            resultMap.put("excelFormType", template.getExcelFormType());
            this.startImprot(sheet, template, improtTemplateList,
                    defaultColsMap, fileName, userName, resultMap);
//            List<TemplateConf> templateConfList = this
//                    .getTemplateConf(templateCode); // 获取包含的子模板
//            if (null != templateConfList && templateConfList.size() > 0) {
//                for (TemplateConf tempConf : templateConfList) {
//                    Template childTemplate = this.getTemplateByCode(tempConf
//                            .getChildTemplateCode());
//                    int id = 0;
//                    if (null != resultMap.get("id")) {// 关联ID
//                        id = (Integer) resultMap.get("id");
//                    }
//                    defaultColsMap.put(tempConf.getColsName(),
//                            String.valueOf(id));
//                    resultMap.put("excelFormType",
//                            childTemplate.getExcelFormType());
//                    if (null == childTemplate
//                            || StringUtil.isBlank(childTemplate
//                            .getTemplateCode())
//                            || StringUtil.isBlank(childTemplate.getTableName())) {
//                        msg = "数据导入失败：子模板未配置或配置不正确，请检查模板！";
//                        break;
//                    }
//                    List<ImprotTemplate> childImprotTemplateList = this
//                            .findImprotTemplateByTemplateCode(childTemplate
//                                    .getTemplateCode());
//                    if (null == childImprotTemplateList
//                            || childImprotTemplateList.size() <= 0) {
//                        msg = "数据导入失败：子模板未配置字段映射关系！";
//                        resultMap.put("msg", msg);
//                        return resultMap;
//                    }
//                    resultMap.put("childTableTHList", childImprotTemplateList);
//
//                    Sheet childSheet = workbook.getSheet(childTemplate.getSheetName());
//                    if (null == childSheet) {
//                        msg = "数据导入失败：导入的excel中未找到名称为【" + sheetName
//                                + "】的sheet页，请修改导入的excel文件。";
//                        resultMap.put("msg", msg);
//                        return resultMap;
//                    }U
//                    resultMap.put("isChild", true);
//                    this.startImprot(childSheet, childTemplate,
//                            childImprotTemplateList, defaultColsMap, fileName,
//                            userName, resultMap);
//                }
//            }
            aopAction(resultMap, aop, false);//后处理
            msg = (String) resultMap.get("msg");
        } catch (Exception e) {
            e.printStackTrace();
            msg = "数据导入失败：" + e.getMessage();
        }
        resultMap.put("msg", msg);
        return resultMap;
    }
    @Override
    public Map<String,Object> getDataExecl(String[] templateCodeList, String fileName,
                                           String defaultCols, String userName, InputStream in) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String msg = "";
        try {
            Workbook workbook = WorkbookFactory.create(in);
            for(String templateCode: templateCodeList){
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("notSaveData",true);//标识数据不保存数据库
                TemplateAop aop = this.getTemplateAop(templateCode,
                        "getDataByTemplateCode");
                aopAction(resultMap, aop, true);//预处理
                Template template = this.getTemplateByCode(templateCode);
                if (null == template || StringUtil.isBlank(template.getTemplateCode())
                        || StringUtil.isBlank(template.getTableName())) {
                    msg = "数据导入失败：模板未配置或配置不正确，请检查模板！3";
                    resultMap.put("msg", msg);
                    return resultMap;
                }
                List<ImprotTemplate> improtTemplateList = this
                        .findImprotTemplateByTemplateCode(templateCode);
                if (null == improtTemplateList || improtTemplateList.size() <= 0) {
                    msg = "数据导入失败：未配置字段映射关系！";
                    resultMap.put("msg", msg);
                    return resultMap;
                }
                String sheetName = template.getSheetName();
                // 默认字段需要从页面传递
                Map<String, String> defaultColsMap = new HashMap<String, String>();
                if (StringUtil.isNotBlank(defaultCols)) {
                    JSONObject jsonObject = JSONObject.parseObject(defaultCols);
                    for (String key : jsonObject.keySet()) {
                        String value = jsonObject.getString(key);
                        defaultColsMap.put(key, value);
                    }
                }
                Sheet sheet = workbook.getSheet(sheetName);
                if (null == sheet) {
                    msg = "数据导入失败：导入的excel中未找到名称为【" + sheetName
                            + "】的shell页，请修改导入的excel文件。";
                    resultMap.put("msg", msg);
                    return resultMap;
                }
                resultMap.put("excelFormType", template.getExcelFormType());
                this.startImprot(sheet, template, improtTemplateList,
                        defaultColsMap, fileName, userName, resultMap);
//                List<TemplateConf> templateConfList = this
//                        .getTemplateConf(templateCode); // 获取包含的子模板
//                if (null != templateConfList && templateConfList.size() > 0) {
//                    for (TemplateConf tempConf : templateConfList) {
//                        Template childTemplate = this.getTemplateByCode(tempConf
//                                .getChildTemplateCode());
//                        int id = 0;
//                        if (null != resultMap.get("id")) {// 关联ID
//                            id = (Integer) resultMap.get("id");
//                        }
//                        defaultColsMap.put(tempConf.getColsName(),
//                                String.valueOf(id));
//                        resultMap.put("excelFormType",
//                                childTemplate.getExcelFormType());
//                        if (null == childTemplate
//                                || StringUtil.isBlank(childTemplate
//                                .getTemplateCode())
//                                || StringUtil.isBlank(childTemplate.getTableName())) {
//                            msg = "数据导入失败：子模板未配置或配置不正确，请检查模板！";
//                            break;
//                        }
//                        List<ImprotTemplate> childImprotTemplateList = this
//                                .findImprotTemplateByTemplateCode(childTemplate
//                                        .getTemplateCode());
//                        if (null == improtTemplateList
//                                || improtTemplateList.size() <= 0) {
//                            msg = "数据导入失败：子模板未配置字段映射关系！";
//                            resultMap.put("msg", msg);
//                            return resultMap;
//                        }
//                        this.startImprot(sheet, childTemplate,
//                                childImprotTemplateList, defaultColsMap, fileName,
//                                userName, resultMap);
//                    }
//                }
                aopAction(resultMap, aop, false);//后处理
                List<Map<String, Object>> dataList=(List<Map<String, Object>>)resultMap.get("dataList");
                paramMap.put(templateCode, dataList);//数据放到Map中
            }
            msg = (String) paramMap.get("msg");
        } catch (Exception e) {
            e.printStackTrace();
            msg = "数据导入失败：" + e.getMessage();
        }
        paramMap.put("msg", msg);
        return paramMap;
    }
    @Override
    public String saveData(String templateCode, String userName ,String fileName,Map<String, Object> resultMap) throws Exception{
        resultMap.put("excelFormType",ExcelFormType.LIST.getId());
        resultMap.put("notSaveData",true);//标识数据不保存数据库
        TemplateAop aop = this.getTemplateAop(templateCode,
                "dataSave");
        aopAction(resultMap, aop, true);//预处理
        List<Map<String, Object>> dataList = (List<Map<String, Object>>)resultMap.get("dataList");
        String msg = "";
        Template template = this.getTemplateByCode(templateCode);
        if (null == template || StringUtil.isBlank(template.getTemplateCode())
                || StringUtil.isBlank(template.getTableName())) {
            msg = "数据导入失败：模板未配置或配置不正确，请检查模板！4";
            return msg;
        }
        List<ImprotTemplate> improtTemplateList = this
                .findImprotTemplateByTemplateCode(templateCode);
        if (null == improtTemplateList || improtTemplateList.size() <= 0) {
            msg = "数据导入失败：未配置字段映射关系！";
            return msg;
        }

        //判断是否执行dataHandle，默认为true
        boolean doDataHandle = MapUtils.getBooleanValue(resultMap, "doDataHandle", true);
        if (doDataHandle) {
            msg = this.dataHandle(dataList, template, improtTemplateList,
                    fileName, userName, resultMap);
        }
        resultMap.put("dataList", dataList);
        aopAction(resultMap, aop, false);//后处理
        msg = (String) resultMap.get("msg");
        return msg;
    }
    @Override
    public String saveDataList(String templateCode, String userName ,Map<String, Object> resultMap) {
        TemplateAop aop = this.getTemplateAop(templateCode,
                "dataSave");
        if(aop==null ||StringUtils.isEmpty(aop.getPosAction())){
            return "未配置后处理接口";
        }
        aopAction(resultMap, aop, false);//后处理
        return "处理成功";
    }
    /**
     * 执行SQL插入
     * @param sql
     * @param templateCode
     * @param userName
     * @return
     */
    public String dataHandleBySQL(String sql,String templateCode,String userName) {
        String msg = "";
        DataSource ds = ConnectionFactory.getConnectionPool().getDataSource();
        QueryRunner runner = new QueryRunner(ds);
        try {
            runner.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
            String remark = "异常描述：执行SQL插入不成功。";
            String expLog = "异常堆栈信息：" + e;
//            ImprotLog improtLog = createImprotLog(1, templateCode,
//                    "", "", "", 0, remark, expLog,
//                    userName);
//            this.insertImprotLog(improtLog);
        }
        return msg;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getExcelDataList(String templateCode, String defaultCols, String dataJson) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("notSaveData",true);//标识数据不保存数据库
        TemplateAop aop = this.getTemplateAop(templateCode,
                "getDataByTemplateCode");
        aopAction(resultMap, aop, true);//预处理
        String msg = "";
        List<ImprotTemplate> improtTemplateList = this
                .findImprotTemplateByTemplateCode(templateCode);
        if (null == improtTemplateList || improtTemplateList.size() <= 0) {
            msg = "数据导入失败：未配置字段映射关系！";
            resultMap.put("msg", msg);
            return resultMap;
        }
        resultMap.put("tableTHList", improtTemplateList);
        // 默认字段需要从页面传递,将值转换成字符串类型
        JSONObject defaultColsMap = new JSONObject();
        if (StringUtil.isNotBlank(defaultCols)) {
            JSONObject jsonObject = JSONObject.parseObject(defaultCols);
            for (String key : jsonObject.keySet()) {
                String value = jsonObject.getString(key);
                defaultColsMap.put(key, value);
            }
        }
        try {
            //System.out.println(dataJson);
            List<JSONObject> obJson = JSON.parseArray(dataJson, JSONObject.class);
            List<JSONObject> list =  obJson;
            List<JSONObject> dataList = new ArrayList<JSONObject>();
            for(JSONObject row : list) {
                if(row.keySet().size()>1) {
                    JSONObject head = new JSONObject();
                    Set<String> keys = new HashSet<String>();
                    for(String key : row.keySet()) {
                        if(row.get(key) instanceof String){
                            head.put(key,row.get(key));
                        }else if(row.get(key) instanceof List){
                            keys.add(key);
                        }
                    }
                    for(String key : keys) {
                        List<JSONObject> rows = (List<JSONObject>) row.get(key);
                        for(JSONObject r : rows) {
                            if(r!=null && !r.isEmpty()) {
                                r.putAll(defaultColsMap);
                                r.putAll(head);
                                r.put("canUpdateOrInsert", true);
                                dataList.add(r);
                            }
                        }
                    }
                }else {
                    for(String key : row.keySet()) {
                        if(row.get(key) instanceof List){
                            List<JSONObject> rows = (List<JSONObject>) row.get(key);
                            for(JSONObject r : rows) {
                                if(r!=null && !r.isEmpty()) {
                                    r.putAll(defaultColsMap);
                                    r.put("canUpdateOrInsert", true);
                                    dataList.add(r);
                                }
                            }
                        }
                    }
                }
            }
            resultMap.put("dataList", dataList);//将数据放到Map中返回前台
            aopAction(resultMap, aop, false);//后处理
            msg = (String) resultMap.get("msg");
        } catch (Exception e) {
            e.printStackTrace();
            msg = "数据导入失败：" + e.getMessage();
        }
        resultMap.put("msg", msg);
        return resultMap;
    }

    @Override
    public Map<String, Object> getExcelData(String templateCode, String defaultCols, String dataJson) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("notSaveData",true);//标识数据不保存数据库
        TemplateAop aop = this.getTemplateAop(templateCode, "getDataByTemplateCode");
        aopAction(resultMap, aop, true);//预处理
        String msg = "";
        // 默认字段需要从页面传递,将值转换成字符串类型
        JSONObject data = new JSONObject();
        if (StringUtil.isNotBlank(defaultCols)) {
            JSONObject jsonObject = JSONObject.parseObject(defaultCols);
            for (String key : jsonObject.keySet()) {
                String value = jsonObject.getString(key);
                data.put(key, value);
            }
        }
        try {
            JSONObject obJson = JSON.parseObject(dataJson, JSONObject.class);
            if(obJson!=null && !obJson.isEmpty()) {
                data.putAll(obJson);
            }
            resultMap.put("data", data);//将数据放到Map中返回前台
            aopAction(resultMap, aop, false);//后处理
            if(resultMap.get("msg") instanceof String) {
                msg = (String) resultMap.get("msg");
            }else if(resultMap.get("msg")!=null){
                msg = JSON.toJSONString(resultMap.get("msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "数据导入失败：" + e.getMessage();
        }
        resultMap.put("msg", msg);
        return resultMap;
    }

    @Override
    public Map<String, Object> saveDataByAop(String templateCode, Map<String, Object> resultMap) {
        Map<String, Object> response = new HashMap<String, Object>();
        TemplateAop aop = this.getTemplateAop(templateCode, "dataSave");
        aopAction(resultMap, aop, true);//预处理
        resultMap.put("resultMap", resultMap);
        aopAction(resultMap, aop, false);//后处理
        Object rep = resultMap.get("response");
        if(rep!=null) {
            if(rep instanceof Map) {
                response.putAll((Map<String, Object>) rep);
            }else {
                response.put("msg", rep);
            }
        }else {
            response.put("msg", resultMap.get("msg"));
        }
        return response;
    }

//    @Override
//    public TemplateCompanyConf getTemplateCompanyConf(String templateCode, String companyCode) {
//        TemplateCompanyConf template = new TemplateCompanyConf();
//        try {
//            DataSource ds = ConnectionFactory.getConnectionPool()
//                    .getDataSource();
//            QueryRunner runner = new QueryRunner(ds);
//            String sql = "select ID,company_code,template_code,company_template_code from t_template_company_conf t where t.TEMPLATE_CODE=? and t.company_code=?";
//            List<Map<String, Object>> rs = runner.query(sql,
//                    new MapListHandler(), new Object[] { templateCode,companyCode });
//
//            Iterator<Map<String, Object>> itr = rs.iterator();
//            while (itr.hasNext()) {
//                Map<String, Object> mp = (Map<String, Object>) itr.next();
//                template.setId((Integer) mp.get("ID"));
//                template.setTemplateCode((String) mp.get("template_code"));
//                template.setCompanyCode((String) mp.get("company_code"));
//                template.setCompanyTemplateCode((String) mp.get("company_template_code"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return template;
//    }

}
