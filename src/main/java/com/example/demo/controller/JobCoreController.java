package com.example.demo.controller;

import com.example.demo.entity.JobAndTrigger;
import com.example.demo.job.BaseJob;
import com.example.demo.service.IJobAndTriggerService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
     * @return
     * @Description //TODO 查询
     * @Param
     **/
    @GetMapping(value = "/queryJob")
    @ResponseBody
    public List<JobAndTrigger> queryJob() {
        List<JobAndTrigger> jobAndTriggers = jobAndTriggerService.selectJobAndTriggerDetails();
        return jobAndTriggers;

    }

    /**
     * @return
     * @Description 打开新增窗口
     * @Param
     **/
    @RequestMapping("/open_insert")
    public String openInsert() {
        return "view/insert.html";
    }

    /**
     * @return
     * @Description //TODO 添加任务
     * @Param
     **/
    @PostMapping("/insert")
    @ResponseBody
    public String insertJob(@RequestParam(value = "jobClassName") String jobClassName,
                            @RequestParam(value = "jobGroupName") String jobGroupName,
                            @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
        jobAndTriggerService.addJob(jobClassName, jobGroupName, cronExpression);
        return "success";
    }


    /**
     * @return
     * @Description //TODO 启动任务
     * @Param
     **/
    @PostMapping("/start_job")
    @ResponseBody
    public String startJob(@RequestParam(value = "jobClassName") String jobClassName,
                           @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        jobAndTriggerService.jobResume(jobClassName, jobGroupName);
        return "success!";
    }


    /**
     * @return
     * @Description //TODO 暂停任务
     * @Param
     **/
    @PostMapping("/pause_job")
    @ResponseBody
    public String pauseJob(@RequestParam(value="jobClassName")String jobClassName,
                           @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
        jobAndTriggerService.jobPause(jobClassName, jobGroupName);
        return "success!";
    }

    /**
     * @return
     * @Description //TODO 删除任务
     * @Param
     **/
    @PostMapping("/delete_job")
    @ResponseBody
    public String deleteJob(@RequestParam(value="jobClassName")String jobClassName,
                           @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
        jobAndTriggerService.jobDelete(jobClassName, jobGroupName);
        return "success!";
    }

    /**
     * @Description 打开修改页面 数据回填
     * @Param
     * @return
     **/
    @RequestMapping("/open_update")
    public String openUpdate(@RequestParam String jobClassName,
                             @RequestParam String jobGroupName,
                             @RequestParam String cronExpression,
                             Model model){
        JobAndTrigger jobAndTriggerDetail = jobAndTriggerService.getJobAndTriggerDetail(jobClassName, jobGroupName, cronExpression);
        model.addAttribute("item", jobAndTriggerDetail);
        return "view/update.html";
    }
    /**
     * @Description //修改
     * @Param
     * @return
     **/
    @PostMapping("/update_job")
    @ResponseBody
    public String updateJob(@RequestParam(value="jobClassName")String jobClassName,
                            @RequestParam(value="jobGroupName")String jobGroupName,
                            @RequestParam(value="cronExpression")String cronExpression) throws Exception{
        jobAndTriggerService.jobReschedule(jobClassName, jobGroupName, cronExpression);
        return "success!";
    }


}
