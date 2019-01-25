package com.shrek.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.WebChatService;
import com.shrek.example.util.CommonUtil;
import com.shrek.example.util.constants.ErrorEnum;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/webchat")
public class WebChatController {

	@Autowired
	private WebChatService webChatService;
	
	@Value("${prop.upload-folder}")
    private String UPLOAD_FOLDER;
	
	@GetMapping("/getSign")
    public JSONObject listOrg(HttpServletRequest request) {
		Subject currentUser = SecurityUtils.getSubject();
		System.out.println(currentUser.getPrincipal());
		
		JSONObject jsobject = new JSONObject();
		jsobject.put("url", request.getParameter("url"));
		
		//jsobject.put("url", "http://wx.gzstrong.com:183/");
		
		System.out.println("url="+request.getParameter("url"));
		
		return CommonUtil.successJson(webChatService.getSignInfo(jsobject));
    }
	
	@PostMapping("/singlefile")
    public JSONObject singleFileUpload(MultipartFile file,String id,String inOrOut,String imgOrder) {
		
		System.out.println("============================");
		System.out.println(JSON.toJSONString(id));
		System.out.println(imgOrder);
		String romdom= UUID.randomUUID().toString();
		try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER + romdom+file.getOriginalFilename());
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(UPLOAD_FOLDER));
            }
            //文件写入指定路径
            Files.write(path, bytes);
            
            JSONObject object = new JSONObject();
            object.put("inOrOut", inOrOut);
            object.put("id", id);
            object.put("imgOrder", imgOrder);
            object.put("image", romdom+file.getOriginalFilename());
            
            
           // busCheckInfoService.updateBusCheckInfoImage(object);
            //busStopService.updateBusStopimg(id, file.getOriginalFilename());
            
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("imageUrl", "/api/images/"+(romdom+file.getOriginalFilename()));
            
            
            return CommonUtil.successJson(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }
	}
	
}
