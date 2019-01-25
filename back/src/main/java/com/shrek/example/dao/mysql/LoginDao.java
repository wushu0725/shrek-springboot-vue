package com.shrek.example.dao.mysql;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

/**
 * @author: hxy
 * @description: 登录相关dao
 * @date: 2017/10/24 11:02
 */
public interface LoginDao {
    /**
     * 根据用户名和密码查询对应的用户
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    JSONObject getUser(@Param("username") String username, @Param("password") String password);
    
    JSONObject getUserByWebUserName(JSONObject jsonObject);
    
    JSONObject getUserByPhone(@Param("mobile") String mobile);
}
