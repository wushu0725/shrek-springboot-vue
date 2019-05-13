package com.shrek.example.service;


import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-01-28 09:54:22
 */
public interface BsCityService {
    JSONObject update(JSONObject jsonObject);

    JSONObject insert(JSONObject jsonObject);

    JSONObject list(JSONObject jsonObject);

    JSONObject listAll(JSONObject jsonObject);

    JSONObject delete(Long id);
}