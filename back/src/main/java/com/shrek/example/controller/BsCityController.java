package com.shrek.example.controller;

import com.shrek.example.service.BsCityService;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;


import org.springframework.beans.factory.annotation.Autowired;
import com.shrek.example.util.CommonUtil;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("vi/citys")
public class BsCityController {

    @Autowired
    private BsCityService bsCityService;

    @PostMapping
    public JSONObject insert(@RequestBody JSONObject requestJson) {
        return bsCityService.insert(requestJson);
    }

    @PutMapping
    public JSONObject update(@RequestBody JSONObject requestJson) {
        return bsCityService.update(requestJson);
    }

    @DeleteMapping
    public JSONObject delete(HttpServletRequest request) {
        return bsCityService.delete(Long.valueOf(request.getParameter("id")));
    }

    @GetMapping
    @CrossOrigin
    public JSONObject list(HttpServletRequest request) {
        JSONObject jsonObject=CommonUtil.request2Json(request);
        return bsCityService.listAll(jsonObject);
    }
}