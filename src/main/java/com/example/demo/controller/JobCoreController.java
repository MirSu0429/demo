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
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private Scheduler scheduler;

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
        addJob(jobClassName, jobGroupName, cronExpression);
        return "success";
    }

    private void addJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
        // 启动调度器
        scheduler.start();

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
                                            .withSchedule(scheduleBuilder).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            System.out.println("创建定时任务失败" + e);
            throw new Exception("创建定时任务失败");
        }
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
        jobResume(jobClassName, jobGroupName);
        return "success!";
    }

    public void jobResume(String jobClassName, String jobGroupName) throws Exception {
        scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
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
        jobPause(jobClassName, jobGroupName);
        return "success!";
    }
    public void jobPause(String jobClassName, String jobGroupName) throws Exception {
        scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
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
        jobDelete(jobClassName, jobGroupName);
        return "success!";
    }
    public void jobDelete(String jobClassName, String jobGroupName) throws Exception {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
        scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
    }
    /**
     * @Description 打开修改页面
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
     * @Description //打开修改页面 数据回填
     * @Param
     * @return
     **/
    @PostMapping("/update_job")
    @ResponseBody
    public String updateJob(@RequestParam(value="jobClassName")String jobClassName,
                            @RequestParam(value="jobGroupName")String jobGroupName,
                            @RequestParam(value="cronExpression")String cronExpression) throws Exception{
        jobReschedule(jobClassName, jobGroupName, cronExpression);

        return "success!";
    }
    public void jobReschedule(String jobClassName, String jobGroupName, String cronExpression) throws Exception
    {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            System.out.println("更新定时任务失败"+e);
            throw new Exception("更新定时任务失败");
        }
    }
    public static BaseJob getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob) class1.newInstance();
    }
}
