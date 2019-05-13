package com.shrek.example.service;


import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-05-08 16:47:05
 */
public interface ShrekGoodsImgService {
    JSONObject update(JSONObject jsonObject);

    JSONObject insert(JSONObject jsonObject);

    JSONObject list(JSONObject jsonObject);

    JSONObject delete(Long id);
}