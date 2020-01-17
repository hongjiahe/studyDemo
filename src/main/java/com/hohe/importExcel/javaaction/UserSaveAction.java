package com.hohe.importExcel.javaaction;

import com.hohe.importExcel.JavaAction;
import com.hohe.model.User;
import com.hohe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UserSaveAction implements JavaAction {

    private Integer userId;
    private String userName;
    private String password;
    private String phone;

    @Autowired
    private UserService userService;

    @SuppressWarnings("rawtypes")
    @Override
    public void excute(Map params) {
        if (params == null) return ;

        List<Map<String, Object>> dataList = (List<Map<String, Object>>) params.get("dataList");
        if (dataList == null) {
            System.out.println("CustomersAction:数据集为空....");
            return;
        }

        List<User> userList = new ArrayList<User>();
        for (Map<String, Object> m : dataList) {
            userName = (String) m.get("userName");
            password = (String) m.get("password");
            phone = (String) m.get("phone");
        }

        if (userList != null && userList.size() > 0) {
            try {
//                IScmCommonAddressService scmCommonAddressService = (IScmCommonAddressService) SpringBeanService.getInstance().get("scmCommonAddressService");
//                scmCommonAddressService.saveBatch(hl4plCommonAddressVos);
                int insert = userService.insert(userList.get(0));
                params.put("flag", "保存成功!");
            } catch (Exception e) {
                e.printStackTrace();
                params.put("flag", "数据保存失败");
            }
        } else {
            params.put("flag", "数据保存失败，没有要保存的数据！");
        }
    }

}
