package com.hohe.service;

import com.hohe.model.ImprotTemplate;
import com.hohe.model.Template;
import com.hohe.model.TemplateAop;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IImprotTemplateService {


    void aopAction(Map map, TemplateAop aop,boolean pref) ;

    /**
     * 获取导入模板集合
     * @return
     */
    List<Template> getTemplate();

    /**
     * 根据模板编码获取模板信息
     * @param templateCode
     * @return
     */
    Template getTemplateByCode(String templateCode);

    /**
     * 根据模板编码获取模板
     * @param templateCode
     * @return
     */
    List<ImprotTemplate> findImprotTemplateByTemplateCode(String templateCode);

    /**
     * 增加日志
     * @param log
     */
//    void insertImprotLog(ImprotLog log);

    /**
     * 查询日志
     * @return
     */
//    Page<ImprotLog> getImprotLog(int pageNumber,int pageSize,Map<String, Object> map);

    /**
     *
     * @param templateCode 模板编码
     * @param searchColsName 关联字段
     * @param colsValue 关联字段值
     * @param Map 查询条件
     * @param pageNumber 当前页数
     * @param pageSize 每页显示记录数
     * @return
     */
    List<Map<String, Object>> getDataByTemplateCode(String templateCode, String searchColsName, String colsValue,
                                                    Map<String, String> Map, int pageNumber,int pageSize);

    /**
     * 根据ID获取一条记录
     * @param templateCode
     * @param id
     * @return
     */
    Map<String, Object> getDataById(String templateCode, int id);

    /**
     * 获取记录数
     * @param templateCode 模板编码
     * @param colsName 关联字段
     * @param colsValue 关联字段值
     * @param  Map<String, String> map 查询条件
     * @return
     */
    int getTotleSize(String templateCode, String colsName, String colsValue, Map<String, String> map);

    /**
     * 根据模板编码获取模板关系
     * @param templateCode
     * @return
     */
//    List<TemplateConf> getTemplateConf(String templateCode);

    /**
     * 返回AOP处理模板;
     * @param templateCode
     * @return
     */
    TemplateAop getTemplateAop(String templateCode,String methodName);

    /**
     * 根据模板修改数据
     * @param templateCode 模板编码
     * @param ids 数据ID 格式为 (1,2,3)
     * @return
     */
    int deleteData(String templateCode, String ids);

    /**
     * 插入数据
     * @param templateCode 模板编码
     * @param map 字段值
     * @return
     */
    int insertData(String templateCode, Map<String, Object> map);

    /**
     * 更新数据
     * @param templateCode
     * @param dataMap
     * @return
     */
    int updateData(String templateCode,Map<String, Object> dataMap);


    /**
     * 根据模板编码和角色ID获取查询条件<br/>
     * 1、查询公司定制的查询条件<br/>
     * 2、未查询到定制的查询条件，则使用默认查询条件<br/>
     * @param templateCode 模板编码
     * @param roleId 角色ID
     * @param currCompanyId 公司ID
     * @return
     */
//    TemplateQueryCriteria getTemplateQueryCriteria(String templateCode, int roleId, int currCompanyId);

    /**
     * 根据value获取key值
     * @param templateCode 模板编码
     * @param colsName 列名称
     * @param value 值
     * @return
     */
    Object getColsKeyByValue(String templateCode, String colsName,Object value);

    /**
     * 导入excel
     * @param templateCode 模板编码
     * @param fileName 文件名称
     * @param defaultCols 默认值
     * @param userName 当前用户名称
     * @param in 文件流
     * @return
     */
    String improtExecl(String templateCode,String fileName, String defaultCols, String userName, InputStream  in);


    /**
     * 获取列表excel 数据
     * @param sheet excel
     * @param titleRowNum 标题行号
     * @param defaultColsMap 前台值
     * @param template 模板
     * @param templateList 模板字段设置
     * @param fileName 文件名称
     * @param userName 操作人员
     * @param resultMap返回值
     * @return 数据集合
     */
    List<Map<String, Object>> createExcelListData(Sheet sheet, Map<String, String> defaultColsMap,
                                                  Template template, List<ImprotTemplate> templateList, String fileName, String userName, Map<String, Object> resultMap);

    /**
     * 获取表单数据
     * @param sheet excel
     * @param defaultColsMap 前台值
     * @param template 模板
     * @param templateList 模板字段设置
     * @param fileName 文件名称
     * @param userName 操作人员
     * @param resultMap返回值
     * @return 数据集合
     */
    List<Map<String, Object>> createExcelFormData(Sheet sheet, Map<String, String> defaultColsMap,
                                                  Template template, List<ImprotTemplate> templateList, String fileName, String userName, Map<String, Object> resultMap);

    /**
     * 导入单表单excel
     * @param sheet excel文件
     * @param template 模板
     * @param improtTemplateList 表字段配置
     * @param defaultColsMap 默认值
     * @param fileName 文件名称
     * @param userName 用户名
     * @return
     */
    String improtFormExcel(Sheet sheet, Template template, List<ImprotTemplate> improtTemplateList,
                           Map<String, String> defaultColsMap, String fileName, String userName, Map<String, Object> resultMap) throws Exception;

    /**
     * 导入列表excel
     * @param sheet excel文件
     * @param template 模板
     * @param improtTemplateList 表字段配置
     * @param defaultColsMap 默认值
     * @param fileName 文件名称
     * @param userName 用户名
     * @return
     */
    String improtListExcel(Sheet sheet, Template template, List<ImprotTemplate> improtTemplateList,
                           Map<String, String> defaultColsMap, String fileName, String userName, Map<String, Object> resultMap) throws Exception;
    /**
     * 根据用户和表名判断表是否存在
     * @param userName 数据库用户
     * @param tableName 表名称
     * @return 返回查找表个数
     */
//    int findTable(String userName, String tableName);
    /**
     * 从Excel中获取数据
     * @param templateCode
     * @param fileName
     * @param defaultCols
     * @param userName
     * @param in
     * @return
     */
    public Map<String,Object> getDataExecl(String templateCode, String fileName,
                                           String defaultCols, String userName, InputStream in);
    /**
     * 保存数据
     * @param templateCode
     * @param fileName
     * @param defaultCols
     * @param userName
     * @param in
     * @param dataList
     * @return
     */
    public String saveData(String templateCode, String userName ,String fileName,Map<String, Object> resultMap) throws Exception;
    /**
     * 执行SQL插入
     * @param sql
     * @param templateCode
     * @param userName
     * @return
     */
    public String dataHandleBySQL(String sql,String templateCode,String userName);
    /**
     * 批量保存数据，此方法只调后处理接口，保存数据动作由接口实现
     * @param templateCode
     * @param userName
     * @param resultMap
     * @return
     */
    public String saveDataList(String templateCode, String userName ,Map<String, Object> resultMap);

    public Map<String,Object> getDataExecl(String[] templateCodeList, String fileName,
                                           String defaultCols, String userName, InputStream in);
    Map<String, Object> getExcelDataList(String templateCode, String defaultCols, String dataJson);
    Map<String, Object> getExcelData(String templateCode, String defaultCols, String dataJson);
    Map<String, Object> saveDataByAop(String templateCode, Map<String, Object> resultMap);
//    public TemplateCompanyConf getTemplateCompanyConf(String templateCode, String companyCode);
}
