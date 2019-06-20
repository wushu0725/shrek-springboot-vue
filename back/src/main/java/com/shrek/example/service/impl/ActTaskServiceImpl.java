package com.shrek.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shrek.example.dao.mysql.OaLeaveBillDao;
import com.shrek.example.model.CommentDto;
import com.shrek.example.model.TaskDTO;
import com.shrek.example.service.ActTaskService;
import com.shrek.example.util.CommonUtil;
import com.shrek.example.util.constants.TaskStatusEnum;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class ActTaskServiceImpl implements ActTaskService{

    private static final String FLAG = "审批";

    @Autowired
    private OaLeaveBillDao oaLeaveBillDao;

    @Autowired
    private  TaskService taskService;
    @Autowired
    private  RuntimeService runtimeService;
    @Autowired
    private  RepositoryService repositoryService;
    @Autowired
    private  HistoryService historyService;
    @Autowired
    private  ProcessEngineFactoryBean processEngine;


    @Override
    public JSONObject getTaskByName(JSONObject jsonObject, String name) {
        int page = Integer.valueOf(jsonObject.getOrDefault("page",1).toString());
        int limit =Integer.valueOf(jsonObject.getOrDefault("limit",10).toString());
        JSONObject result = new JSONObject();

        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskCandidateOrAssigned(name);

        List<TaskDTO> taskDTOList = taskQuery.orderByTaskCreateTime().desc()
                .listPage((page - 1) * limit, limit).stream().map(task -> {
                    TaskDTO dto = new TaskDTO();
                    dto.setTaskId(task.getId());
                    dto.setTaskName(task.getName());
                    dto.setProcessInstanceId(task.getProcessInstanceId());
                    dto.setNodeKey(task.getTaskDefinitionKey());
                    dto.setCategory(task.getCategory());
                    dto.setTime(task.getCreateTime());
                    return dto;
                }).collect(Collectors.toList());
        result.put("list",taskDTOList);
        result.put("totalCount",taskQuery.count());
        return CommonUtil.successJson(result);
    }

    /**
     * 通过任务ID查询任务信息关联申请单信息
     *
     * @param taskId
     * @return
     */
    @Override
    public JSONObject getTaskById(String taskId) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();

        String businessKey = pi.getBusinessKey();
        if (StringUtils.isNotBlank(businessKey)) {
            businessKey = businessKey.split("_")[1];
        }

        List<String> comeList = findOutFlagListByTaskId(task, pi);
        JSONObject leaveBill = oaLeaveBillDao.selectById(Integer.valueOf(businessKey).longValue());

//        LeaveBillDto leaveBillDto = new LeaveBillDto();
//        BeanUtils.copyProperties(leaveBill, leaveBillDto);
//        leaveBillDto.setTaskId(taskId);
//        leaveBillDto.setTime(task.getCreateTime());
//        leaveBillDto..setFlagList(comeList);
        leaveBill.put("flagList",comeList);
        return leaveBill;
    }

    /**
     * 提交任务
     *
     * @param leaveBillDto
     * @return
     */
    @Override
    public JSONObject submitTask(JSONObject leaveBillDto) {
        String taskId = leaveBillDto.getString("taskId");
        String message = leaveBillDto.getString("comment");
        Integer id = leaveBillDto.getIntValue("id");

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        String processInstanceId = task.getProcessInstanceId();
        Authentication.setAuthenticatedUserId(SecurityUtils.getSubject().getPrincipal().toString());
        taskService.addComment(taskId, processInstanceId, message);

        Map<String, Object> variables = new HashMap<>(1);
        variables.put("flag", leaveBillDto.getString("flag"));
        variables.put("days", leaveBillDto.getString("days"));

        taskService.complete(taskId, variables);
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (pi == null) {
            JSONObject bill = oaLeaveBillDao.selectById(Long.valueOf(id));
            bill.put("state",StringUtils.equals(TaskStatusEnum.OVERRULE.getDescription()
                    , leaveBillDto.getString("flag")) ? TaskStatusEnum.OVERRULE.getStatus()
                    : TaskStatusEnum.COMPLETED.getStatus());
            oaLeaveBillDao.update(bill);
        }
        return CommonUtil.successJson();
    }

    @Override
    public List<CommentDto> getCommentByTaskId(String taskId) {
        //使用当前的任务ID，查询当前流程对应的历史任务ID
        //使用当前任务ID，获取当前任务对象
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        //获取流程实例ID
        List<CommentDto> commentDtoList = taskService
                .getProcessInstanceComments(task.getProcessInstanceId())
                .stream().map(comment -> {
                            CommentDto commentDto = new CommentDto();
                            commentDto.setId(comment.getId());
                            commentDto.setTime(comment.getTime());
                            commentDto.setType(comment.getType());
                            commentDto.setTaskId(comment.getTaskId());
                            commentDto.setUserId(comment.getUserId());
                            commentDto.setFullMessage(comment.getFullMessage());
                            commentDto.setProcessInstanceId(comment.getProcessInstanceId());
                            return commentDto;
                        }
                ).collect(Collectors.toList());
        return commentDtoList;
    }

    /**
     * 追踪图片节点
     *
     * @param id
     */
    @Override
    public InputStream viewByTaskId(String id) {
        //使用当前任务ID，获取当前任务对象
        Task task = taskService.createTaskQuery()
                .taskId(id)
                .singleResult();

        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = null;
        List<String> executedActivityIdList = new ArrayList<>();
        if (processInstance != null) {
            processDefinitionId = processInstance.getProcessDefinitionId();
            executedActivityIdList = this.runtimeService.getActiveActivityIds(processInstance.getId());
        } else if (historicProcessInstance != null) {
            processDefinitionId = historicProcessInstance.getProcessDefinitionId();
            executedActivityIdList = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricActivityInstanceId().asc().list()
                    .stream().map(HistoricActivityInstance::getActivityId)
                    .collect(Collectors.toList());
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();

        return diagramGenerator.generateDiagram(
                bpmnModel, "png",
                executedActivityIdList, Collections.emptyList(),
                "宋体",
                "宋体",
                "宋体",
                null, 1.0);

    }

    private List<String> findOutFlagListByTaskId(Task task, ProcessInstance pi) {
        //查询ProcessDefinitionEntiy对象
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
                .getProcessDefinition(task.getProcessDefinitionId());

        ActivityImpl activityImpl = processDefinitionEntity.findActivity(pi.getActivityId());
        //获取当前活动完成之后连线的名称
        List<String> nameList = activityImpl.getOutgoingTransitions().stream()
                .map(pvm -> {
                    String name = (String) pvm.getProperty("name");
                    return StringUtils.isNotBlank(name) ? name : FLAG;
                }).collect(Collectors.toList());
        return nameList;
    }
}
