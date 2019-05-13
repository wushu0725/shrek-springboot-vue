package com.shrek.example.service;


import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-05-07 14:49:02
 */
public interface ShrekCategoryService {
    JSONObject update(JSONObject jsonObject);

    JSONObject insert(JSONObject jsonObject);

    JSONObject list(JSONObject jsonObject);

    JSONObject delete(Long id);

    JSONObject selectList(JSONObject jsonObject);
}