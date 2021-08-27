package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.TblUser;
import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 2021/7/30 0030
 */
public interface UserService {

    //按id获取user对象
    public TblUser selectUserById(String id);

    User queryUserByLoginActAndPwd(Map<String,Object> map);

    List<User> queryAllUsers();
}
