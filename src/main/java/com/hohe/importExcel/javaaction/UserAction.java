package com.hohe.importExcel.javaaction;

import com.hohe.importExcel.JavaAction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserAction implements JavaAction {

    String notNull ="";
    String notRepeat = "";
    StringBuffer length = null;
    StringBuffer errorInfos=null;
    private final String VALUES="_value";
    private Integer userId;
    private String userName;
    private String password;
    private String phone;
    private String createName;

    @SuppressWarnings("rawtypes")
    @Override
    public void excute(Map params) {
        if (params == null) return ;

        List<Map<String, Object>> dataList = (List<Map<String, Object>>) params.get("dataList");
        if (dataList == null) {
            System.out.println("CustomersAction:数据集为空....");
            return;
        }

        // 存储（类型 + 简称地址 + 详细地址）作为的key，判断导入的数据是否有重复
        Map<String, Integer> keyMap = new HashMap<String, Integer>();
        int index = 1;

        for (Map<String, Object> m : dataList) {
            errorInfos = new StringBuffer();
            notNull = "";
            notRepeat = "";
            length = new StringBuffer();
            userName = (String) m.get("userName");
            password = (String) m.get("password");
            phone = (String) m.get("phone");
            createName = (String) m.get("createName");

            //需要显示在列表上的字段需要加上_value后缀
            m.put("userName" + VALUES, userName);
            m.put("password" + VALUES, password);
            m.put("phone" + VALUES, phone);

            // 进行非空字段校验
            if (StringUtils.isBlank(userName)){
                notNull += "用户名称,";
            }
            if (StringUtils.isBlank(password)) {
                notNull += "密码,";
            }
            if (StringUtils.isBlank(phone)) {
                notNull += "手机号码,";
            }

            // 进行长度校验


            // 判断导入的数据是否有重复的

            // 校验类型是否正确
//            if (StringUtils.isNotBlank(type)) {
//                CommonAddressTypeEnum commonAddressTypeEnum = CommonAddressTypeEnum.fromName(type);
//                if (commonAddressTypeEnum == null) {
//                    errorInfos.append("类型填写不正确;");
//                } else {
//                    // 判断数据库是否已经存在
//                    boolean existSign = this.existCommonAddress(type, shortAddress, address, companyUid);
//                    if (existSign) {
//                        errorInfos.append("【" + key + "】已经存在;");
//                    }
//                }
//            }

            // 将错误信息添加到errorInfos中
            if(notNull.indexOf(",")>0){
                errorInfos.append(notNull.substring(0, notNull.length()-1)+"必填,不允许为空;");
            }
            if(notRepeat.indexOf(",")>0){
                errorInfos.append(notRepeat.substring(0, notRepeat.length()-1)+"重复了,请检查重新再导入!;");
            }
            if(length.indexOf(",") > 0){
                length.deleteCharAt(length.lastIndexOf(","));
                errorInfos.append(length);
            }

            if(!"".equals(errorInfos.toString())){
                m.put(ERRORINFO,errorInfos.toString());
            }

            index ++;
        }

    }

    /**
     * 判断是否已经存在相同记录
     * @param type 类型
     * @param shortAddress 简称地址
     * @param address 详细地址
     * @param companyUid 企业UID
     * @return boolean true表示存在，false表示不存在
     */
//    private boolean existCommonAddress(String type, String shortAddress, String address, String companyUid) {
//        IScmCommonAddressService scmCommonAddressService = (IScmCommonAddressService) SpringBeanService.getInstance().get("scmCommonAddressService");
//        Hl4plCommonAddressVo addressVo = new Hl4plCommonAddressVo();
//        // 订单来源：
//        //	 * 0:代客下单
//        //	 * 1:委托方自己下单 （默认 0）
//        addressVo.setSources(0);
//        // 订单类型 （默认 4 : 陆运服务单）
//        addressVo.setOrderType(4);
//        addressVo.setCompanyUid(companyUid);
//        CommonAddressTypeEnum typeEnum = CommonAddressTypeEnum.fromName(type);
//        addressVo.setType(typeEnum.getId());
//        addressVo.setShortAddress(shortAddress);
//        addressVo.setAddress(address);
//        OrderResult<Hl4plCommonAddressVo> result = scmCommonAddressService.findIsExisted(addressVo);
//        if (result.getData() != null) {
//            return true;
//        }
//        return false;
//    }
}
