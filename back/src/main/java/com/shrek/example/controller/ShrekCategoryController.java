package com.shrek.example.controller;

import com.shrek.example.service.ShrekCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.alibaba.fastjson.JSONObject;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.shrek.example.util.CommonUtil;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("shrekCategory")
public class ShrekCategoryController {

    @Autowired
    private ShrekCategoryService shrekCategoryService;

    @PostMapping
    public JSONObject insert(@RequestBody JSONObject requestJson) {
        return shrekCategoryService.insert(requestJson);
    }

    @PutMapping
    public JSONObject update(@RequestBody JSONObject requestJson) {
        return shrekCategoryService.update(requestJson);
    }

    @DeleteMapping
    public JSONObject delete(HttpServletRequest request) {
        return shrekCategoryService.delete(Long.valueOf(request.getParameter("id")));
    }

    @GetMapping
    public JSONObject list(HttpServletRequest request) {
        JSONObject jsonObject=CommonUtil.request2Json(request);
        return shrekCategoryService.list(jsonObject);
    }

    @GetMapping("selectList")
    public JSONObject selectList(HttpServletRequest request) {
        JSONObject jsonObject=CommonUtil.request2Json(request);
        return shrekCategoryService.selectList(jsonObject);
    }
}