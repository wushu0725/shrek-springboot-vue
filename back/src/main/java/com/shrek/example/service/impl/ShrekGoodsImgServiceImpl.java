package com.shrek.example.service.impl;


import com.shrek.example.dao.mysql.ShrekGoodsImgDao;
import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.ShrekGoodsImgService;
import com.shrek.example.util.CommonUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-05-08 16:47:05
 */
@Service
public class ShrekGoodsImgServiceImpl implements ShrekGoodsImgService {

    @Autowired
    private ShrekGoodsImgDao shrekGoodsImgDao;

    @Override
    public JSONObject update(JSONObject jsonObject){
        shrekGoodsImgDao.update(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject insert(JSONObject jsonObject){
        shrekGoodsImgDao.insert(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject list(JSONObject jsonObject){
        List<JSONObject> list = shrekGoodsImgDao.listByPage(jsonObject);
        return CommonUtil.successPage(list);
    }

    @Override
    public JSONObject delete (Long id){
        shrekGoodsImgDao.delete(id);
        return CommonUtil.successJson();
    }
}