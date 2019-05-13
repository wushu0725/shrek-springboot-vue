package com.shrek.example.controller;

import com.shrek.example.service.ShrekSctionService;
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
@RequestMapping("shrekSction")
public class ShrekSctionController {

    @Autowired
    private ShrekSctionService shrekSctionService;

    @PostMapping
    public JSONObject insert(@RequestBody JSONObject requestJson) {
        return shrekSctionService.insert(requestJson);
    }

    @PutMapping
    public JSONObject update(@RequestBody JSONObject requestJson) {
        return shrekSctionService.update(requestJson);
    }

    @DeleteMapping
    public JSONObject delete(HttpServletRequest request) {
        return shrekSctionService.delete(Long.valueOf(request.getParameter("id")));
    }

    @GetMapping
    public JSONObject list(HttpServletRequest request) {
        JSONObject jsonObject=CommonUtil.request2Json(request);
        return shrekSctionService.list(jsonObject);
    }

    @GetMapping("selectList")
    public JSONObject selectList(HttpServletRequest request) {
        JSONObject jsonObject=CommonUtil.request2Json(request);
        return shrekSctionService.selectList(jsonObject);
    }
}