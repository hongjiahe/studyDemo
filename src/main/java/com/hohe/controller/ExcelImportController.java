package com.hohe.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.hohe.importExcel.ExcelHelper;
import com.hohe.importExcel.ImportExcelVo;
import com.hohe.importExcel.JavaAction;
import com.hohe.model.ImprotTemplate;
import com.hohe.service.IImprotTemplateService;
import com.hohe.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.google.gson.Gson;

/**
 * 新版Excel导入
 * @Date 2019年05月11日
 */

@Controller
@SuppressWarnings("unchecked")
@RequestMapping(value = "/common/excel/import")
public class ExcelImportController {

	@Resource
	private HttpServletRequest request;
	@Autowired 
	private IImprotTemplateService improtTemplateService;
//	@Resource
//	private EssReadConfigService readConfigService;
	
//	@Resource
//	private IRedisService redisService;
	
	private static Map<String,String> tempFilesPath = null;
	static {
		tempFilesPath = new HashMap<String,String>();
		tempFilesPath.put("itemMasterImport_flex", "/file/jg/basic/物料信息-导入样例.xlsx");
	}

	/**
	 * 获取导入样例
	 * @param templateCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/template", method=RequestMethod.GET)
	public Result<String> template(String templateCode) {
		try {
//			SysUser user = SessionUserUtil.getUserVerify(request);
			if(StringUtils.isBlank(templateCode)) {
				throw new ApiException("请指定TemplateCode");
			}
			//检查是否有做个性化配置，如果配了个性化用对应templateCode
//			TemplateCompanyConf templateCompanyConf = improtTemplateService.getTemplateCompanyConf(templateCode, user.getCompanyCode());
//			if(templateCompanyConf!=null && StringUtils.isNoneBlank(templateCompanyConf.getCompanyTemplateCode())) {
//				templateCode = templateCompanyConf.getCompanyTemplateCode();
//			}
			String webroot = request.getServletContext().getRealPath("/");
			String filePath = tempFilesPath.get(templateCode);
			if(StringUtils.isBlank(filePath)) {
				throw new ApiException("未配置模板");
			}
			File file = new File(webroot+filePath);
			if(!file.exists()) {
				throw new ApiException("模板已被移除");
			}
			return Result.success("获取成功", filePath);
		}catch (ApiException e) {
			return Result.error(e.getMessage(), null);
		}
	}

	/**
	 * 通用导入-上传附件
	 * @param uploadFile
	 * @param templateCode
	 * @param extParams 附加参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public Result<ImportExcelResult> upload(MultipartFile uploadFile, String templateCode, String extParams) {
		
		Result<ImportExcelResult> pageResponse = new Result<ImportExcelResult>();
		ImportExcelResult ieResult = new ImportExcelResult();
		pageResponse.setStatus(Result.ERROR);
		InputStream in = null;
		String fileName = "", msg = "", userName = "";
		try {
//			SysUser user = SessionUserUtil.getUserVerify(request);
//			userName = user.getUserName();
			if(null==uploadFile){
				throw new MyException("请选择导入文件");
			}else if(StringUtils.isBlank(templateCode)) {
				throw new MyException("请指定TemplateCode");
			}
			in = uploadFile.getInputStream();
			fileName = uploadFile.getOriginalFilename();

			//添加当前用户信息和日期
			//String now=DateUtil.convertDateToString(new Date(),DateUtil.FORMAT_yyyy_MM_dd);
			String nowTime=DateUtil.convertDateToString(new Date(),DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss);

//			String uploadSaveFile = redisService.get("scm:uploadSaveFile");//是否保存导入文件
//			if(!StringUtils.isNotBlank(uploadSaveFile) && in !=null) {
//				try {
//					String webroot = request.getServletContext().getRealPath("/");
//					String patch=webroot+"/uploadTempate/"+DateUtil.convertDateToString(new Date(),DateUtil.FORMAT_yyyy_MM_dd);
//					FileUtils.writeToLocal(patch, fileName,in);
//					in =  new FileInputStream(patch+"/"+fileName);
//				} catch (Exception e) {
//				}
//			}

			//附加信息开始
			Map<String,Object> extInfo = new HashMap<String,Object>();
//			extInfo.put("creater_id", user.getId());
			extInfo.put("creater_name", "hohea");
//			extInfo.put("create_time", nowTime);
//			extInfo.put("createid", user.getId());
//			extInfo.put("createrId", user.getId());
//			extInfo.put("company_code", user.getCompanyCode());
//			extInfo.put("companyUid", user.getCompanyUid());//ADD ON 2019-05-10
//			extInfo.put("groupId", user.getGroupId());//ADD ON 2019-05-14
//			extInfo.put("groupUid", user.getGroupId() == null ? null : String.valueOf(user.getGroupId()));
//			extInfo.put("groupCode", user.getGroupCode());
//			extInfo.put("groupName", user.getGroupName());//ADD ON 2019-05-14
			
//			extInfo.put("createTime", nowTime);
			extInfo.put("createrName", "hoheb");

			if (StringUtil.isNotBlank(extParams)) {
				Gson gson = new Gson();
				Map<String, String> map = gson.fromJson(extParams, Map.class);
				extInfo.put("extParamMap", map);
			}
			String defaultCols = JSON.toJSONString(extInfo);
			//检查是否有做个性化配置，如果配了个性化用对应templateCode
//			TemplateCompanyConf templateCompanyConf = improtTemplateService.getTemplateCompanyConf(templateCode, user.getCompanyCode()) ;
//			if(templateCompanyConf!=null && StringUtils.isNoneBlank(templateCompanyConf.getCompanyTemplateCode())) {
//				templateCode = templateCompanyConf.getCompanyTemplateCode();
//			}
			//附加信息结束
			Map<String,Object> map = improtTemplateService.getDataExecl(templateCode, fileName, defaultCols, userName, in);
			
			List<Map<String, Object>> dataList=(List<Map<String, Object>>)map.get("dataList");
			List<Map<String, Object>> errorDataList=new ArrayList<>();
			if(dataList!=null && dataList.size()>0){
				if (templateCode.equals("dclEntBillHeadE") || templateCode.equals("dclEntBillHeadI")) {
					//错误信息放list末尾
					Collections.sort(dataList, new Comparator<Map<String, Object>>() {
						public int compare(Map<String, Object> obj1, Map<String, Object> obj2) {
							Map<String, Object> a = (Map<String, Object>) obj1;
							Map<String, Object> b = (Map<String, Object>) obj2;
							String error1 = (String) a.get(JavaAction.ERRORINFO);
							String error2 = (String) b.get(JavaAction.ERRORINFO);
							if (StringUtil.isNotBlank(error1) && StringUtil.isBlank(error2)) {
								return -1;
							}else if (StringUtil.isBlank(error1) && StringUtil.isNotBlank(error2)) {
								return 1;
							}
							return 0;
						}
					});
				} else {
					// 有错的排序
					Collections.sort(dataList, new Comparator<Map<String, Object>>() {
						public int compare(Map<String, Object> obj1, Map<String, Object> obj2) {
							Map<String, Object> a = (Map<String, Object>) obj1;
							Map<String, Object> b = (Map<String, Object>) obj2;
							String error1 = (String) a.get(JavaAction.ERRORINFO);
							String error2 = (String) b.get(JavaAction.ERRORINFO);
							if (error1 == null && error2 == null) {
								return 0;
							} else if (error1 == null && error2 != null && !"".equals(error2)) {
								return 1;
							} else if (error2 != null && !"".equals(error1) && !"".equals(error2)
									&& !JavaAction.DATAEXISTS.equals(error2)) {
								return 1;
							} else if (error1 != null && !"".equals(error1)) {
								return -1;
							}
							return 0;
						}
					});
				}
				int x=1;
				for(Map<String,Object> data:dataList){
					//移除参数数据
					data.remove("userId");
//					data.remove("userName");
					data.remove("nowTime");
					data.put("trId", x++);
					
					//此写法比较e❤
					String errorInfo = MapUtils.getString(data, JavaAction.ERRORINFO);
					if(StringUtils.isNotBlank(errorInfo)){
						String[] errors = errorInfo.split(";");
						errorDataList.add(data);
						if(!(errors.length==1&&errors[0].contains(JavaAction.DATAEXISTSNO))){
							data.put("dataError", "true");
						}
					}
				}
			}
			msg = MapUtils.getString(map, "msg");
			List<ImprotTemplate> list = (List<ImprotTemplate>) map.get("tableTHList");
			pageResponse.setMsg(msg);
			ieResult.setDataList(dataList);
			ieResult.setTableTHList(list);
			pageResponse.setStatus(Result.SUCCESS);
			ieResult.setErrorsize(errorDataList.size());

//			String key = EssCacheUtil.getKey(templateCode);
//			String errorFileName = "";
//			if(CollectionUtils.isNotEmpty(errorDataList)) {
//				errorFileName = "导入数据错误集-"+request.getSession().getId()+".xls";
//				String fileUrl = processExcelList(errorFileName,list,errorDataList);
//				if(StringUtils.isNotBlank(fileUrl)) {
//					EssCacheUtil.getInstance().setToDataMap(key+"_error_url", fileUrl);
//				}
//			}
//			ImportCachVo icVo = new ImportCachVo();
//			icVo.setDataList(dataList);
//			icVo.setOtherParamMap(MapUtils.getMap(map, "otherParamMap"));
//			EssCacheUtil.getInstance().setToDataImportMap(key, icVo);

//			ieResult.setKey(key);
//			ieResult.setErrorFileName(errorFileName);
			pageResponse.setData(ieResult);
			pageResponse.setStatus(Result.SUCCESS);
			if (StringUtil.isNotBlank(msg)) {
				if (msg.indexOf("数据导入失败") != -1) {
					pageResponse.setStatus(Result.ERROR);
				}
			}
    	}catch(MyException mye){
    		pageResponse.setMsg(mye.getMessage());
//    	}catch(ZLException zle){
//    		pageResponse.setMsg(zle.getMessage());
    	}catch (IOException ioe) {
    		ioe.printStackTrace();
    		pageResponse.setMsg("文件读取失败:"+ioe.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			pageResponse.setMsg("操作失败："+e.getMessage());
		}
        return pageResponse;
	}
	
	/**
	 * 保存数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<String> save(@RequestBody ImportExcelVo vo) {
		Result<String> pageResponse = new Result<String>();
		pageResponse.setStatus(Result.ERROR);
		try {
			String fileName="", msg="", userName="";
			Map<String, Object> resultMap = new HashMap<String, Object>();
//			SysUser user = SessionUserUtil.getUserVerify(request);
//			if (null== user) {
//				throw new ApiException("登录超时");
//			}
//			userName = user.getUserName();
			//从缓存获取数据
//			ImportCachVo tCachVo = EssCacheUtil.getInstance().getFromDataImportMap(vo.getKey());
//			if(tCachVo==null) {
//				throw new ApiException("缓存失效，请重新导入");
//			}
//			vo.setDataList(tCachVo.getDataList());
	    	if (null==vo.getDataList() || vo.getDataList().isEmpty()) {
	    		throw new ApiException("缓存失效，请重新导入");
	    	}
    		List<String> removeKeyList = null;
    		Map<String,Object> addMapList =  null;
    		List<Map<String,Object>> newDataList=new ArrayList<Map<String,Object>>();
    		for(Map<String,Object> data : vo.getDataList()){
				// 检验
				boolean isUpdate = vo.getIsUpdate().equals("true");
				// 1;//检测无错误;2;//用户勾选覆盖时可以提交;3;//不能提交保存
				Integer rowErrorSubmit = MapUtils.getInteger(data, "row_error_submit");
				if (rowErrorSubmit != null  && rowErrorSubmit.equals(2) && isUpdate) {
					newDataList.add(data);
				} else if (rowErrorSubmit != null && rowErrorSubmit.equals(1)) {
					newDataList.add(data);
				}
				data.remove("trId");
				data.remove("dataError");
//				data.put("company_code", user.getCompanyCode());
				// 处理UUID
				if (data.get("id") != null && data.get("id") instanceof UUID) {
					data.put("id", data.get("id").toString());
				}
				// 删除显示用的数据以_value结尾的
				removeKeyList = new ArrayList<String>();
				addMapList = new HashMap<String, Object>();
				for (String key : data.keySet()) {
					if (key.contains("_value" + JavaAction.VALEX)) {
						addMapList.put(key.replaceAll(JavaAction.VALEX, ""), data.get(key));
						removeKeyList.add(key);
					} else if (key.contains("_value")) {
						removeKeyList.add(key);
					}
				}
				if (removeKeyList.size() > 0) {
					for (String key : removeKeyList) {
						data.remove(key);
					}
				}
				if (addMapList != null && addMapList.size() > 0) {
					data.putAll(addMapList);
				}
			}

    		 vo.setDataList(newDataList);
    		 resultMap.put("dataList", vo.getDataList());
//    		 if (tCachVo.getOtherParamMap() != null) {
//    			 resultMap.putAll(tCachVo.getOtherParamMap());
//    		 }
    		 String templateCode = vo.getTemplateCode();
 			//检查是否有做个性化配置，如果配了个性化用对应templateCode
// 			TemplateCompanyConf templateCompanyConf = improtTemplateService.getTemplateCompanyConf(templateCode, user.getCompanyCode()) ;
// 			if(templateCompanyConf!=null  && StringUtils.isNoneBlank(templateCompanyConf.getCompanyTemplateCode())) {
// 				templateCode = templateCompanyConf.getCompanyTemplateCode();
// 			}
 			
    		 msg = improtTemplateService.saveData(templateCode, userName, fileName,resultMap);
    		 
    		 pageResponse.setMsg(msg);
    		 pageResponse.setStatus(Result.SUCCESS);
		     boolean clearCache=true;
    		 if (StringUtil.isBlank(msg)) {
    			 throw new ApiException("接口服务有误");
    		 }
    		 if (msg.indexOf("数据保存失败") != -1) {
    			 pageResponse.setStatus(Result.ERROR);
    			 clearCache=false;
    		 }
//    		if(clearCache){
//				EssCacheUtil.getInstance().removeFromDataImportMap(vo.getKey());//清除缓存
//    		}
		}catch (ApiException e) {
			pageResponse.setMsg(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			pageResponse.setMsg("操作失败："+e.getMessage());
		}
        return pageResponse;
	}

	/**
	 * 导出Excel导入的错误信息
	 * @param key
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/error", method = RequestMethod.GET)
//	@ApiOperation(value = "导出Excel导入的错误信息")
    public Result<String> error(String key) {
		Result<String> pageResponse = new Result<String>();
		pageResponse.setStatus(Result.ERROR);
		try {
//			Object obj =
//			pageResponse.setStatus(Result.SUCEssCacheUtil.getInstance().getFromDataMap(key+"_error_url");
//			if(obj==null) {
//				throw new ApiException("未找到错误信息");
//			}CESS);
//			pageResponse.setData(obj.toString());
			pageResponse.setMsg("正在导出");
		}catch (ApiException e) {
			pageResponse.setMsg(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			pageResponse.setMsg("导出失败");
		}
		return pageResponse;
	}
	
	//表头
	private String processExcelList(String fileName, List<ImprotTemplate> list, List<Map<String, Object>> dataList){
		List<String> keys = new ArrayList<>();
		List<String> titles = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(list)){
			Map<String,String> rows = new HashMap<>();
			rows.put("trId", "行号");
			keys.add("trId");
			titles.add("行号");
			rows.put("errorInfo", "错误信息");
			keys.add("errorInfo");
			titles.add("错误信息");
			for(ImprotTemplate bean : list){
				if(bean.getIsShow()==0){
					rows.put(bean.getColsName(), bean.getColsLableName());
					keys.add(bean.getColsName());
					titles.add(bean.getColsLableName());
				}
			}
		}
		return ExcelHelper.generateExcelFromListToTemp(fileName, titles, keys, dataList);
	}
}
