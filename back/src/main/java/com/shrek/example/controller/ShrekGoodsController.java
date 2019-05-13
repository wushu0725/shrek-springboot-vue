package com.shrek.example.controller;

import com.shrek.example.service.ShrekGoodsService;
import com.shrek.example.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("shrekGoods")
public class ShrekGoodsController {

    @Autowired
    private ShrekGoodsService shrekGoodsService;

    @Value("${prop.upload-folder}")
    private String UPLOAD_FOLDER;

    @PostMapping
    public JSONObject insert(@RequestBody JSONObject requestJson) {
        return shrekGoodsService.insert(requestJson);
    }

    @PutMapping
    public JSONObject update(@RequestBody JSONObject requestJson) {
        return shrekGoodsService.update(requestJson);
    }

    @DeleteMapping
    public JSONObject delete(HttpServletRequest request) {
        return shrekGoodsService.delete(Long.valueOf(request.getParameter("id")));
    }

    @GetMapping
    public JSONObject list(HttpServletRequest request) {
        JSONObject jsonObject=CommonUtil.request2Json(request);
        return shrekGoodsService.list(jsonObject);
    }

    @PostMapping("/singlefile")
    public JSONObject singleFileUpload(MultipartFile file, String id) {

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(UPLOAD_FOLDER));
            }
            //文件写入指定路径
            Files.write(path, bytes);
            //busStopService.updateBusStopimg(id, file.getOriginalFilename());
            return CommonUtil.successJson(file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }
}