package com.shrek.example.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shrek.example.dao.mysql.SysGeneratorDao;
import com.shrek.example.util.CommonUtil;
import com.shrek.example.util.GeneratorUtils;
import com.shrek.example.service.SysGeneratorService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Service
public class SysGeneratorServiceImpl implements SysGeneratorService{

    @Autowired
    private SysGeneratorDao sysGeneratorDao;


    public Map<String, String> queryTable(String tableName) {
        return sysGeneratorDao.queryTable(tableName);
    }

    public List<Map<String, String>> queryColumns(String tableName) {
        return sysGeneratorDao.queryColumns(tableName);
    }

    @Override
    public byte[] generatorCode(String tableName) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        List clomes = sysGeneratorDao.queryColumns(tableName);


        Map<String, String> table = queryTable(tableName);
        //查询列信息
        List<Map<String, String>> columns = queryColumns(tableName);

        GeneratorUtils.generatorCode(table, columns, zip);

        System.out.println(clomes.size());



        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    @Override
    public JSONObject queryAllTable() {
        List<JSONObject> jsonArray = sysGeneratorDao.queryAllTable();
        return CommonUtil.successPage(jsonArray);
    }
}
