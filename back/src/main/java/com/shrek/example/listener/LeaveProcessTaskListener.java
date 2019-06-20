package com.shrek.example.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.shiro.SecurityUtils;

public class LeaveProcessTaskListener implements TaskListener {


    /**
     * 根据节点分配下级处理人
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println(delegateTask.getName());

        delegateTask.addCandidateUser(SecurityUtils.getSubject().getPrincipal().toString());
        delegateTask.addCandidateUser("000005");
    }
}
