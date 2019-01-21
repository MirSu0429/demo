package com.example.demo.util.quertz;

import com.example.demo.entity.JobEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;

/**
 * @Author: supengfei
 * @Date: 2019/1/21 11:10
 * @Description:
 */
@Service
@Slf4j
public class JobTaskUtil {
    @Autowired
    private Scheduler scheduler;

    /**
     * true 存在 false 不存在
     *
     * @param
     * @return
     */
    public boolean checkJob(JobEntity job) {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getId(), Scheduler.DEFAULT_GROUP);
        try {
            if (scheduler.checkExists(triggerKey)) {
                return true;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 开启
     */
    //@Log(desc = "开启定时任务")
    public boolean startJob(JobEntity job) {
        try {
            Class clazz = Class.forName(job.getClazzPath());
            JobDetail jobDetail = JobBuilder.newJob(clazz).build();
            // 触发器
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getId(), Scheduler.DEFAULT_GROUP);
            CronTrigger trigger = TriggerBuilder.newTrigger()
                                                .withIdentity(triggerKey)
                                                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron())).build();
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
                log.info("---任务[" + triggerKey.getName() + "]启动成功-------");
                return true;
            } else {
                log.info("---任务[" + triggerKey.getName() + "]已经运行，请勿再次启动-------");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 更新
     */
    //@Log(desc = "更新定时任务", type = LOG_TYPE.UPDATE)
    public boolean updateJob(JobEntity job) {
        String createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");

        TriggerKey triggerKey = TriggerKey.triggerKey(job.getId(), Scheduler.DEFAULT_GROUP);
        try {
            if (scheduler.checkExists(triggerKey)) {
                return false;
            }

            JobKey jobKey = JobKey.jobKey(job.getId(), Scheduler.DEFAULT_GROUP);

            CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(job.getCron())
                                                                  .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                                                .withDescription(createTime).withSchedule(schedBuilder).build();

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            HashSet<Trigger> triggerSet = new HashSet<>();
            triggerSet.add(trigger);
            scheduler.scheduleJob(jobDetail, triggerSet, true);
            log.info("---任务[" + triggerKey.getName() + "]更新成功-------");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 删除
     */
    //@Log(desc = "删除定时任务", type = LOG_TYPE.DEL)
    public boolean remove(JobEntity job) {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getId(), Scheduler.DEFAULT_GROUP);
        try {
            if (checkJob(job)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(JobKey.jobKey(job.getId(), Scheduler.DEFAULT_GROUP));
                log.info("---任务[" + triggerKey.getName() + "]删除成功-------");
                return true;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }
}
