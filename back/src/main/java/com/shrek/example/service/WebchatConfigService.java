package com.shrek.example.service;


import com.alibaba.fastjson.JSONObject;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2018-12-19 12:01:13
 */
public interface WebchatConfigService {
    JSONObject update(JSONObject jsonObject);

    JSONObject insert(JSONObject jsonObject);

    JSONObject list(JSONObject jsonObject);

    JSONObject listAll(JSONObject jsonObject);

    JSONObject delete(Long id);

    JSONObject getByPkOrg(JSONObject jsonObject);
}