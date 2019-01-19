package com.example.demo.controller;

import com.example.demo.entity.JobAndTrigger;
import com.example.demo.service.IJobAndTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: supengfei
 * @Date: 2019/1/19 10:24
 * @Description:
 */
@Controller
public class JobCoreController {

    @Autowired
    private IJobAndTriggerService jobAndTriggerService;

    /**
     * @Description //TODO 查询
     * @Param
     * @return
     **/
    @GetMapping(value="/queryJob")
    @ResponseBody
    public List<JobAndTrigger> queryJob() {
        List<JobAndTrigger> jobAndTriggers = jobAndTriggerService.selectJobAndTriggerDetails();
        return jobAndTriggers;

    }
    /**
     * @Description 打开新增窗口
     * @Param 
     * @return 
     **/
    @RequestMapping("/open_insert")
    public String openInsert(){
        return "view/insert.html";
    }
}
