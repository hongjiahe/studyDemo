package com.hohe.controller;

import com.alibaba.fastjson.JSON;
import com.hohe.importExcel.ExcelHelper;
import com.hohe.importExcel.JavaAction;
import com.hohe.model.ImprotTemplate;
import com.hohe.service.IImprotTemplateService;
import com.hohe.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping(value = "/import")
/**
 * 自定义导入Excel
 */
public class ImprotExcelController {

    @Autowired
    private IImprotTemplateService improtTemplateService;

    /**
     * 导入数据
     * @param request
     * @param uploadFile
     * @param templateCode 模板编码
     * @param defaultCols 页面参数json字符串{"key":"value"}
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/improtExcel_cache")
    public Map<String, Object> improtExcel_cache(HttpServletRequest request, @RequestParam("uploadFile") MultipartFile[] uploadFile,
                                                 String templateCode, String defaultCols, String rid, String configIds){
        Map<String,Object> pageResponse = new HashMap<String,Object>();
        pageResponse.put("status", false);
        InputStream in = null;
        String fileName = "",msg = "",userName = "";
//        SysUser user=SessionUserUtil.getUserVerify(request);
        try {
//            if(null==user){throw new MyException("登录超时");};
////            userName = user.getUserName();
            if(null==uploadFile || uploadFile.length==0){throw new Exception("请选择导入文件");};
            in = uploadFile[0].getInputStream();
            fileName = uploadFile[0].getOriginalFilename();
            //添加当前用户信息和日期
            //String now=DateUtil.convertDateToString(new Date(),DateUtil.FORMAT_yyyy_MM_dd);
            String nowTime=DateUtil.convertDateToString(new Date(),DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss);
            //附加信息开始
            Map<String,Object> extInfo = new HashMap<String,Object>();
            extInfo.put("createTime", nowTime);
            defaultCols = JSON.toJSONString(extInfo);

            //configIds = "3";
            //附加信息结束
            Map<String,Object> map = null;

            if(StringUtils.isBlank(configIds)) {
                map = improtTemplateService.getDataExecl(templateCode, fileName, defaultCols, userName, in);
            }

            List<Map<String, Object>> dataList=(List<Map<String, Object>>)map.get("dataList");
            List<Map<String, Object>> errorDataList=new ArrayList<>();
            if(dataList!=null && dataList.size()>0){
                //有错的排序
                Collections.sort(dataList , new Comparator<Map<String, Object>>(){
                    public int compare(Map<String, Object> obj1,Map<String, Object> obj2){
                        Map<String, Object> a=(Map<String, Object>)obj1;
                        Map<String, Object> b=(Map<String, Object>)obj2;
                        String error1=(String)a.get(JavaAction.ERRORINFO);
                        String error2=(String)b.get(JavaAction.ERRORINFO);
                        if(error1==null && error2==null ){
                            return 0;
                        }else if(error1==null &&error2 !=null &&!"".equals(error2)){
                            return 1;
                        }else if(error2 !=null && !"".equals(error1) && !"".equals(error2)&&!JavaAction.DATAEXISTS.equals(error2)){
                            return 1;
                        }else if(error1 !=null && !"".equals(error1)){
                            return -1;
                        }
                        return 0;
                    }
                });
                int x=1;
                for(Map<String,Object> data:dataList){
                    //移除参数数据
                    data.remove("userId");
                    data.remove("userName");
                    data.remove("nowTime");
                    data.put("trId", x++);
                    if(data.get("errorInfo")!=null && !"".equals(data.get("errorInfo"))){
                        String[] errors=data.get("errorInfo").toString().split(";");
                        errorDataList.add(data);
                        if(!(errors.length==1&&errors[0].contains(JavaAction.DATAEXISTSNO))){
                            data.put("dataError","true");
                        }
                    }
                }
            }
            msg = (String)map.get("msg");
            List<ImprotTemplate> list = (List<ImprotTemplate>) map.get("tableTHList");
            pageResponse.put("message", msg);
            pageResponse.put("dataList",dataList);
            if(templateCode.equals("generalITBondedPlication")){
                List<ImprotTemplate> thList = list;
                for(ImprotTemplate iTemp : thList){
                    if(StringUtils.isNotBlank(iTemp.getColsLableName()) && iTemp.getColsLableName().contains("/")){
                        String title = iTemp.getColsLableName();
                        title = title.substring(0, title.indexOf("/"));
                        iTemp.setColsLableName(title);
                    }
                }
                pageResponse.put("tableTHList", thList);
            }else{
                pageResponse.put("tableTHList", map.get("tableTHList"));
            }
            processExcelList(request,list,errorDataList);
            if(map.get("insertUpdateSqlMap")!=null){
                pageResponse.put("insertUpdateSqlMap", map.get("insertUpdateSqlMap"));
            }
            pageResponse.put("status", true);
            pageResponse.put("errorsize", errorDataList.size());

            /*String key = EssCacheUtil.getKey(templateCode);
            ImportCachVo icVo = new ImportCachVo();
            icVo.setDataList(dataList);
            icVo.setInsertUpdateSqlMap(map.get("insertUpdateSqlMap"));
            icVo.setOtherParamMap(MapUtils.getMap(map, "otherParamMap"));
            EssCacheUtil.getInstance().setToDataImportMap(key, icVo);

            pageResponse.put("key", key);
            if (StringUtil.isNotBlank(msg)) {
                if (msg.indexOf("数据导入失败") != -1) {
                    pageResponse.put("status", false);
                }
            }*/   //这里可以存入redis
//        }catch(MyException mye){
//            pageResponse.put("message", mye.getMessage());
//        }catch(ApiException apie){
//            pageResponse.put("message", apie.getMessage());
//        }catch(ZLException zle){
//            pageResponse.put("message", zle.getMessage());
        }catch (IOException ioe) {
            ioe.printStackTrace();
            pageResponse.put("message", "文件读取失败:"+ioe.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            pageResponse.put("message", "操作失败："+e.getMessage());
        }
        return pageResponse;
    }



    //表头
    private void processExcelList( HttpServletRequest request,List<ImprotTemplate> list,List<Map<String, Object>> dataList){
        List<String> keys=new ArrayList<>();
        List<String> contents=new ArrayList<>();
        //List <List<Map<String,String>>> allRecord=new ArrayList<>();
        List<Map<String,String>> listrecord=new ArrayList<>();
        if(list!=null&&list.size()>0){
            Map<String,String> map=new HashMap<>();
            map.put("trId", "行号");
            keys.add("trId");
            contents.add("行号");
            map.put("errorInfo", "错误信息");
            keys.add("errorInfo");
            contents.add("错误信息");
            for(ImprotTemplate bean:list){
                if(bean.getIsShow()==0){
                    map.put(bean.getColsName(),bean.getColsLableName());
                    keys.add(bean.getColsName());
                    contents.add(bean.getColsLableName());
                }
            }
            listrecord.add(map);
        }
        ExcelHelper.generateExcelFromList(request, contents, keys, dataList);
    }

}
