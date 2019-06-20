package com.shrek.example.controller;


import com.alibaba.fastjson.JSONObject;
import com.shrek.example.model.ProcessDefDTO;
import com.shrek.example.util.CommonUtil;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    RepositoryService repositoryService;

    /**
     * 获取所有模型
     *
     * @return
     */
    @GetMapping
    public JSONObject modelList(HttpServletRequest request) {
        //RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc();

        JSONObject jsonObject=CommonUtil.request2Json(request);

        //第几页 从0开始
        int page = jsonObject.getIntValue("page");
        //每页大小
        int limit = jsonObject.getIntValue("limit");

        List<ProcessDefDTO> deploymentList = query.list().stream()
                .map(processDefinition -> {
                    Deployment deployment = repositoryService.createDeploymentQuery()
                            .deploymentId(processDefinition.getDeploymentId()).singleResult();
                    return ProcessDefDTO.toProcessDefDTO(processDefinition, deployment);
                }).collect(Collectors.toList());;

        JSONObject json = new JSONObject();
        //json.put("totalPage",count);
        json.put("list",deploymentList);
        json.put("totalCount",query.count());

        return CommonUtil.successJson(json);
    }

}
