package com.shrek.example.service;


import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-05-07 14:26:31
 */
public interface ShrekGoodsService {
    JSONObject update(JSONObject jsonObject);

    JSONObject insert(JSONObject jsonObject);

    JSONObject list(JSONObject jsonObject);

    JSONObject delete(Long id);

    List<JSONObject> index(JSONObject jsonObject);

    List<JSONObject> sction2(JSONObject jsonObject);

    List<JSONObject> sction3(JSONObject jsonObject);

    List<JSONObject> aside(JSONObject jsonObject);

    JSONObject detail(JSONObject jsonObject);

    List<JSONObject> swiper(JSONObject jsonObject);
}