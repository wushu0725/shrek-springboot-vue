package com.shrek.example.service.impl;


import com.shrek.example.dao.mysql.ShrekGoodsDao;
import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.ShrekGoodsService;
import com.shrek.example.util.CommonUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-05-07 14:26:31
 */
@Service
public class ShrekGoodsServiceImpl implements ShrekGoodsService {

    @Autowired
    private ShrekGoodsDao shrekGoodsDao;

    @Override
    public JSONObject update(JSONObject jsonObject){
        shrekGoodsDao.update(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject insert(JSONObject jsonObject){
        shrekGoodsDao.insert(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject list(JSONObject jsonObject){
        CommonUtil.fillPageParam(jsonObject);
        int count = shrekGoodsDao.count(jsonObject);
        List<JSONObject> list = shrekGoodsDao.listByPage(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject delete (Long id){
        shrekGoodsDao.delete(id);
        return CommonUtil.successJson();
    }

    @Override
    public List<JSONObject> index(JSONObject jsonObject) {
        return shrekGoodsDao.index(jsonObject);
    }

    @Override
    public List<JSONObject> sction2(JSONObject jsonObject) {
        return shrekGoodsDao.sction2(jsonObject);
    }

    @Override
    public List<JSONObject> sction3(JSONObject jsonObject) {
        return shrekGoodsDao.sction3(jsonObject);
    }

    @Override
    public List<JSONObject> aside(JSONObject jsonObject) {
        return shrekGoodsDao.aside(jsonObject);
    }

    @Override
    public JSONObject detail(JSONObject jsonObject) {
        return shrekGoodsDao.detail(jsonObject).get(0);
    }

    @Override
    public List<JSONObject> swiper(JSONObject jsonObject) {
        return shrekGoodsDao.swiper(jsonObject);
    }
}