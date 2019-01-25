package com.shrek.example.service.impl;


import com.shrek.example.dao.mysql.StudentDao;
import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.StudentService;
import com.shrek.example.util.CommonUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-01-25 09:13:01
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;

    @Override
    public JSONObject update(JSONObject jsonObject){
        studentDao.update(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject insert(JSONObject jsonObject){
        studentDao.insert(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject list(JSONObject jsonObject){
        CommonUtil.fillPageParam(jsonObject);
        int count = studentDao.count(jsonObject);
        List<JSONObject> list = studentDao.listByPage(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject delete (Long id){
        studentDao.delete(id);
        return CommonUtil.successJson();
    }
}